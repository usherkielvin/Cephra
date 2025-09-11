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
$ticketPrefix = ($serviceType === 'Fast Charging') ? 'FCH' : 'NCH';
$prefixPattern = $ticketPrefix . '%';

// Compute the next number based on the greatest ticket suffix present in either queue_tickets or active_tickets
$sql = "SELECT GREATEST(
            IFNULL((SELECT MAX(CAST(SUBSTRING(ticket_id,4) AS UNSIGNED)) FROM queue_tickets WHERE ticket_id LIKE :prefix1), 0),
            IFNULL((SELECT MAX(CAST(SUBSTRING(ticket_id,4) AS UNSIGNED)) FROM active_tickets WHERE ticket_id LIKE :prefix2), 0)
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

// First, record the ticket in queue_tickets so it appears on Admin Queue
$stmt = $conn->prepare("INSERT INTO queue_tickets (ticket_id, username, service_type, status, payment_status, initial_battery_level, priority) VALUES (:ticket_id, :username, :service_type, 'Pending', '', :battery_level, 0)");
$stmt->bindParam(':ticket_id', $ticketId);
$stmt->bindParam(':username', $username);
$stmt->bindParam(':service_type', $queueServiceType);
$stmt->bindParam(':battery_level', $batteryLevel);
if (!$stmt->execute()) {
    $errorInfo = $stmt->errorInfo();
    http_response_code(500);
    echo json_encode(['error' => 'Failed to create queue record', 'db_error' => $errorInfo[2]]);
    exit();
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
