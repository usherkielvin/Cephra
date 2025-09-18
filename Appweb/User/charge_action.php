<?php
// Enable error display for debugging
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

session_start();
if (!isset($_SESSION['username'])) {
    http_response_code(401);
    echo json_encode(['error' => 'Unauthorized']);
    exit();
}

require_once 'config/database.php';

header('Content-Type: application/json');

$db = new Database();
$conn = $db->getConnection();

if (!$conn) {
    http_response_code(500);
    echo json_encode(['error' => 'Database connection failed', 'details' => 'Check database configuration']);
    exit();
}

$username = $_SESSION['username'];

$serviceType = $_POST['serviceType'] ?? '';

// Debug logging
error_log("Charge action called - Username: $username, ServiceType: $serviceType");
error_log("POST data: " . json_encode($_POST));

// If no serviceType provided, return error
if (empty($serviceType)) {
    http_response_code(400);
    echo json_encode(['error' => 'Service type not provided']);
    exit();
}

if (!in_array($serviceType, ['Normal Charging', 'Fast Charging'])) {
    http_response_code(400);
    echo json_encode(['error' => 'Invalid service type']);
    exit();
}

// Check if user has active ticket
$stmt = $conn->prepare("SELECT COUNT(*) FROM active_tickets WHERE username = :username");
$stmt->bindParam(':username', $username);
$stmt->execute();
$activeTicketCount = $stmt->fetchColumn();

if ($activeTicketCount > 0) {
    echo json_encode(['error' => 'You already have an active charging ticket. Please complete your current session first.']);
    exit();
}

// Check if car is linked (assuming car linking is tracked in users table or separate table)
// For now, we'll assume car is linked if user exists and has battery level data
$stmt = $conn->prepare("SELECT COUNT(*) FROM battery_levels WHERE username = :username");
$stmt->bindParam(':username', $username);
$stmt->execute();
$hasBatteryData = $stmt->fetchColumn() > 0;

if (!$hasBatteryData) {
    echo json_encode(['error' => 'Please link your car first before charging.']);
    exit();
}

// Check battery level
$stmt = $conn->prepare("SELECT battery_level FROM battery_levels WHERE username = :username");
$stmt->bindParam(':username', $username);
$stmt->execute();
$result = $stmt->fetch(PDO::FETCH_ASSOC);
$batteryLevel = (int)($result['battery_level'] ?? 0);

if ($batteryLevel >= 100) {
    echo json_encode(['error' => 'Your battery is already 100%.']);
    exit();
}

// Determine service type for database storage
$bayType = ($serviceType === 'Fast Charging') ? 'Fast' : 'Normal';
// Queue ticket should mirror Phone Java panel labels
$queueServiceType = ($serviceType === 'Fast Charging') ? 'Fast Charging' : 'Normal Charging';

// Generate ticket ID using DB-driven increment across both tables
$basePrefix = ($serviceType === 'Fast Charging') ? 'FCH' : 'NCH';
$ticketPrefix = ($priority == 1) ? $basePrefix . 'P' : $basePrefix;
$prefixPattern = $ticketPrefix . '%';

// Compute the next number based on the greatest ticket suffix present in either queue_tickets or active_tickets
$sql = "SELECT GREATEST(
            IFNULL((SELECT MAX(CAST(SUBSTRING(ticket_id," . (strlen($ticketPrefix) + 1) . ") AS UNSIGNED)) FROM queue_tickets WHERE ticket_id LIKE :prefix1), 0),
            IFNULL((SELECT MAX(CAST(SUBSTRING(ticket_id," . (strlen($ticketPrefix) + 1) . ") AS UNSIGNED)) FROM active_tickets WHERE ticket_id LIKE :prefix2), 0)
        ) AS max_num";
$stmt = $conn->prepare($sql);
$stmt->bindParam(':prefix1', $prefixPattern);
$stmt->bindParam(':prefix2', $prefixPattern);
$stmt->execute();
$row = $stmt->fetch(PDO::FETCH_ASSOC);
$nextNum = (isset($row['max_num']) ? (int)$row['max_num'] : 0) + 1;
$ticketId = $ticketPrefix . str_pad($nextNum, 3, '0', STR_PAD_LEFT);

// Do NOT assign a bay at ticket creation time; mirror Java flow
// Ticket stays in Pending state until routed to a bay by Admin
$bayNumber = null;

// Determine priority based on battery level (priority 1 for <20%, priority 0 for >=20%)
$priority = ($batteryLevel < 20) ? 1 : 0;

// Determine initial status - priority tickets go directly to In Progress
$initialStatus = ($priority == 1) ? 'In Progress' : 'Pending';

// First, record the ticket in queue_tickets so it appears on Admin Queue
$stmt = $conn->prepare("INSERT INTO queue_tickets (ticket_id, username, service_type, status, payment_status, initial_battery_level, priority) VALUES (:ticket_id, :username, :service_type, :status, '', :battery_level, :priority)");
$stmt->bindParam(':ticket_id', $ticketId);
$stmt->bindParam(':username', $username);
$stmt->bindParam(':service_type', $queueServiceType);
$stmt->bindParam(':status', $initialStatus);
$stmt->bindParam(':battery_level', $batteryLevel);
$stmt->bindParam(':priority', $priority);
if (!$stmt->execute()) {
    $errorInfo = $stmt->errorInfo();
    http_response_code(500);
    echo json_encode(['error' => 'Failed to create queue record', 'db_error' => $errorInfo[2]]);
    exit();
}

// If this is a priority ticket (In Progress status), try to assign to available bay first
if ($priority == 1) {
    try {
        // Check for available charging bays first
        $isFastCharging = ($queueServiceType === 'Fast Charging');
        $bayType = $isFastCharging ? 'Fast' : 'Normal';
        
        // Find available bay
        $stmt = $conn->prepare("SELECT bay_number FROM charging_bays WHERE bay_type = :bay_type AND status = 'Available' ORDER BY bay_number LIMIT 1");
        $stmt->bindParam(':bay_type', $bayType);
        $stmt->execute();
        $availableBay = $stmt->fetchColumn();
        
        if ($availableBay) {
            // Directly assign to charging bay
            $conn->beginTransaction();
            
            try {
                // Update charging_bays table
                $stmt = $conn->prepare("UPDATE charging_bays SET ticket_id = :ticket_id, username = :username, status = 'Charging', start_time = CURRENT_TIMESTAMP WHERE bay_number = :bay_number");
                $stmt->bindParam(':ticket_id', $ticketId);
                $stmt->bindParam(':username', $username);
                $stmt->bindParam(':bay_number', $availableBay);
                $stmt->execute();
                
                // Update charging_grid table
                $stmt = $conn->prepare("UPDATE charging_grid SET ticket_id = :ticket_id, username = :username, service_type = :service_type, initial_battery_level = :battery_level, start_time = CURRENT_TIMESTAMP WHERE bay_number = :bay_number");
                $stmt->bindParam(':ticket_id', $ticketId);
                $stmt->bindParam(':username', $username);
                $stmt->bindParam(':service_type', $queueServiceType);
                $stmt->bindParam(':battery_level', $batteryLevel);
                $stmt->bindParam(':bay_number', $availableBay);
                $stmt->execute();
                
                // Update queue_tickets status to In Progress
                $stmt = $conn->prepare("UPDATE queue_tickets SET status = 'In Progress' WHERE ticket_id = :ticket_id");
                $stmt->bindParam(':ticket_id', $ticketId);
                $stmt->execute();
                
                $conn->commit();
                    error_log("Priority ticket $ticketId directly assigned to Bay-$availableBay (no waiting needed)");
                
            } catch (Exception $e) {
                $conn->rollback();
                error_log("Failed to assign priority ticket $ticketId to bay $availableBay: " . $e->getMessage());
                throw $e;
            }
            
        } else {
            // No available bays, add to waiting grid
            $stmt = $conn->prepare("SELECT slot_number FROM waiting_grid WHERE ticket_id IS NULL ORDER BY slot_number LIMIT 1");
            $stmt->execute();
            $availableSlot = $stmt->fetchColumn();
            
            if ($availableSlot) {
                // Add ticket to waiting grid
                $stmt = $conn->prepare("UPDATE waiting_grid SET ticket_id = :ticket_id, username = :username, service_type = :service_type, initial_battery_level = :battery_level, position_in_queue = :slot WHERE slot_number = :slot");
                $stmt->bindParam(':ticket_id', $ticketId);
                $stmt->bindParam(':username', $username);
                $stmt->bindParam(':service_type', $queueServiceType);
                $stmt->bindParam(':battery_level', $batteryLevel);
                $stmt->bindParam(':slot', $availableSlot);
                $stmt->execute();
                
                error_log("Priority ticket $ticketId added to waiting grid slot $availableSlot (no bays available)");
            } else {
                error_log("No available waiting slots for priority ticket $ticketId");
            }
        }
    } catch (Exception $e) {
        error_log("Failed to process priority ticket $ticketId: " . $e->getMessage());
    }
}

// Do NOT create an active_tickets entry at ticket creation time.
// Active ticket should be created only by Admin when assigning to a bay (Queue flow)

// Set current service in session
$_SESSION['currentService'] = $serviceType;
$_SESSION['currentTicketId'] = $ticketId;

// Respond success with ticket details
echo json_encode([
    'success' => true,
    'ticketId' => $ticketId,
    'serviceType' => $serviceType,
    'batteryLevel' => $batteryLevel
]);
exit();
?>
