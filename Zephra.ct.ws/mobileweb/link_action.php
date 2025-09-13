<?php
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
    echo json_encode(['error' => 'Database connection failed']);
    exit();
}

$username = $_SESSION['username'];

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode(['error' => 'Method not allowed']);
    exit();
}

// Check if terms are accepted
if (!isset($_POST['terms_accepted']) || $_POST['terms_accepted'] !== 'true') {
    echo json_encode(['error' => 'You must accept the Terms & Conditions to continue.']);
    exit();
}

// Check if car is already linked
$stmt = $conn->prepare("SELECT COUNT(*) FROM battery_levels WHERE username = :username");
$stmt->bindParam(':username', $username);
$stmt->execute();
$hasBatteryData = $stmt->fetchColumn() > 0;

if ($hasBatteryData) {
    echo json_encode(['error' => 'Your car is already linked.']);
    exit();
}

// Link car - initialize battery level
try {
    // Generate random battery level (15-50%)
    $batteryLevel = 15 + rand(0, 35);

    $stmt = $conn->prepare("INSERT INTO battery_levels (username, battery_level, initial_battery_level, battery_capacity_kwh) VALUES (:username, :battery_level, :initial_battery_level, 40.0)");
    $stmt->bindParam(':username', $username);
    $stmt->bindParam(':battery_level', $batteryLevel);
    $stmt->bindParam(':initial_battery_level', $batteryLevel);

    if ($stmt->execute()) {
        // Set car as linked in session
        $_SESSION['car_linked'] = true;

        echo json_encode([
            'success' => true,
            'message' => 'Car linked successfully! Battery level initialized to ' . $batteryLevel . '%.',
            'redirect' => 'dashboard.php'
        ]);
    } else {
        echo json_encode(['error' => 'Failed to link car. Please try again.']);
    }
} catch (Exception $e) {
    error_log('Link car error: ' . $e->getMessage());
    echo json_encode(['error' => 'An error occurred while linking your car. Please try again.']);
}

exit();
?>
