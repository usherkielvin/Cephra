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

require_once '../config/database.php';

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

// Check bay availability
$bayType = ($serviceType === 'Fast Charging') ? 'Fast' : 'Normal';
$stmt = $conn->prepare("SELECT COUNT(*) FROM charging_bays WHERE bay_type = :bayType AND status = 'Available'");
$stmt->bindParam(':bayType', $bayType);
$stmt->execute();
$availableBays = $stmt->fetchColumn();

if ($availableBays == 0) {
    $message = ($serviceType === 'Fast Charging')
        ? 'Fast charging is currently unavailable. All fast charging bays are either unavailable or occupied. Please try normal charging or try again later.'
        : 'Normal charging is currently unavailable. All normal charging bays are either unavailable or occupied. Please try fast charging or try again later.';
    echo json_encode(['error' => $message]);
    exit();
}

// Generate ticket ID
$ticketPrefix = ($serviceType === 'Fast Charging') ? 'FCH' : 'NCH';

// Get the next ticket number
$prefixPattern = $ticketPrefix . '%';
$stmt = $conn->prepare("SELECT MAX(CAST(SUBSTRING(ticket_id, 4) AS UNSIGNED)) as max_num FROM active_tickets WHERE ticket_id LIKE :prefix");
$stmt->bindParam(':prefix', $prefixPattern);
$stmt->execute();
$result = $stmt->fetch(PDO::FETCH_ASSOC);
$nextNum = ($result['max_num'] ?? 0) + 1;
$ticketId = $ticketPrefix . str_pad($nextNum, 3, '0', STR_PAD_LEFT);

// Insert ticket into active_tickets table with bay_number assigned
// Find an available bay number for the service type
$stmt = $conn->prepare("SELECT bay_number FROM charging_bays WHERE bay_type = :bayType AND status = 'Available' LIMIT 1");
$stmt->bindParam(':bayType', $bayType);
$stmt->execute();
$bay = $stmt->fetch(PDO::FETCH_ASSOC);
$bayNumber = $bay ? $bay['bay_number'] : null;

if (!$bayNumber) {
    http_response_code(500);
    echo json_encode(['error' => 'No available charging bay found']);
    exit();
}

$stmt = $conn->prepare("INSERT INTO active_tickets (username, ticket_id, service_type, initial_battery_level, current_battery_level, status, bay_number) VALUES (:username, :ticket_id, :service_type, :battery_level, :battery_level, 'Active', :bay_number)");
$stmt->bindParam(':username', $username);
$stmt->bindParam(':ticket_id', $ticketId);
$stmt->bindParam(':service_type', $bayType);
$stmt->bindParam(':battery_level', $batteryLevel);
$stmt->bindParam(':bay_number', $bayNumber);

if (!$stmt->execute()) {
    $errorInfo = $stmt->errorInfo();
    http_response_code(500);
    echo json_encode(['error' => 'Failed to create ticket', 'db_error' => $errorInfo[2]]);
    exit();
}

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
