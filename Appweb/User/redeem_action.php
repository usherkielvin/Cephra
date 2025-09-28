<?php
session_start();
if (!isset($_SESSION['username'])) {
    http_response_code(401);
    echo json_encode(['success' => false, 'message' => 'Not logged in']);
    exit();
}

header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode(['success' => false, 'message' => 'Method not allowed']);
    exit();
}

require_once 'config/database.php';
$db = new Database();
$conn = $db->getConnection();

$username = $_SESSION['username'];
$reward_name = $_POST['reward_name'] ?? '';
$points_required = (int)($_POST['points_required'] ?? 0);

if (empty($reward_name) || $points_required <= 0) {
    echo json_encode(['success' => false, 'message' => 'Invalid reward data']);
    exit();
}

// Fetch current points
$stmt = $conn->prepare("SELECT total_points, lifetime_spent FROM user_points WHERE username = :username");
$stmt->bindParam(':username', $username);
$stmt->execute();
$result = $stmt->fetch(PDO::FETCH_ASSOC);

if (!$result) {
    echo json_encode(['success' => false, 'message' => 'User points not found']);
    exit();
}

$current_points = (int)$result['total_points'];
$lifetime_spent = (int)$result['lifetime_spent'];

if ($current_points < $points_required) {
    echo json_encode(['success' => false, 'message' => 'Insufficient points']);
    exit();
}

// Update points
$new_total = $current_points - $points_required;
$new_spent = $lifetime_spent + $points_required;

$stmt = $conn->prepare("UPDATE user_points SET total_points = :total, lifetime_spent = :spent, updated_at = CURRENT_TIMESTAMP WHERE username = :username");
$stmt->bindParam(':total', $new_total);
$stmt->bindParam(':spent', $new_spent);
$stmt->bindParam(':username', $username);

if ($stmt->execute()) {
    echo json_encode(['success' => true, 'message' => 'Reward redeemed successfully', 'new_total' => $new_total]);
} else {
    echo json_encode(['success' => false, 'message' => 'Failed to update points']);
}
?>
