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

// Also block if user already has a ticket in queue (Waiting/Pending/Processing)
$stmt = $conn->prepare("SELECT COUNT(*) FROM queue_tickets WHERE username = :username AND status IN ('Waiting','Pending','Processing')");
$stmt->bindParam(':username', $username);
$stmt->execute();
$queuedCount = $stmt->fetchColumn();

if ($queuedCount > 0) {
    echo json_encode(['error' => 'You already have a ticket in queue. Please wait until it is processed.']);
    exit();
}

// Check if car is linked by verifying car_index in users table
$stmt = $conn->prepare("SELECT car_index FROM users WHERE username = :username");
$stmt->bindParam(':username', $username);
$stmt->execute();
$result = $stmt->fetch(PDO::FETCH_ASSOC);
$carIndex = $result['car_index'] ?? null;

if ($carIndex === null || !is_numeric($carIndex) || $carIndex < 0 || $carIndex > 8) {
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

// Determine priority based on battery level (priority 1 for <20%, priority 0 for >=20%)
$priority = ($batteryLevel < 20) ? 1 : 0;

// Generate ticket ID using a single shared counter per service (Fast/Normal),
// counting across priority/non-priority and across queue, active, and history
$basePrefix = ($serviceType === 'Fast Charging') ? 'FCH' : 'NCH';
$ticketPrefix = ($priority == 1) ? $basePrefix . 'P' : $basePrefix;
$prefixPatternBase = $basePrefix . '%'; // includes both e.g. FCH### and FCHP###

// Compute the next number using the last 3 digits across all relevant tables
$sql = "SELECT GREATEST(
            IFNULL((SELECT MAX(CAST(RIGHT(ticket_id, 3) AS UNSIGNED)) FROM queue_tickets WHERE ticket_id LIKE :p1), 0),
            IFNULL((SELECT MAX(CAST(RIGHT(ticket_id, 3) AS UNSIGNED)) FROM active_tickets WHERE ticket_id LIKE :p2), 0),
            IFNULL((SELECT MAX(CAST(RIGHT(ticket_id, 3) AS UNSIGNED)) FROM charging_history WHERE ticket_id LIKE :p3), 0)
        ) AS max_num";
$stmt = $conn->prepare($sql);
$stmt->bindParam(':p1', $prefixPatternBase);
$stmt->bindParam(':p2', $prefixPatternBase);
$stmt->bindParam(':p3', $prefixPatternBase);
$stmt->execute();
$row = $stmt->fetch(PDO::FETCH_ASSOC);
$nextNum = (isset($row['max_num']) ? (int)$row['max_num'] : 0) + 1;
$ticketId = $ticketPrefix . str_pad($nextNum, 3, '0', STR_PAD_LEFT);

// Do NOT assign a bay at ticket creation time; mirror Java flow
// Ticket stays in Pending state until routed to a bay by Admin
$bayNumber = null;

// Determine initial status (mirror Java): priority -> Waiting, others -> Pending
$initialStatus = ($priority == 1) ? 'Waiting' : 'Pending';

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

// If this is a priority ticket (Waiting status), add to waiting grid
if ($priority == 1) {
    try {
        // Add ticket to waiting grid
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
            
            error_log("Priority ticket $ticketId added to waiting grid slot $availableSlot");
        } else {
            error_log("No available waiting slots for priority ticket $ticketId");
        }
    } catch (Exception $e) {
        error_log("Failed to add priority ticket $ticketId to waiting grid: " . $e->getMessage());
    }
}

// Do not auto-create active_tickets here; Admin assignment handles bay routing (follows Java)

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
