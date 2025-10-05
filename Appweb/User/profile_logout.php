<?php
session_start();
require_once 'config/database.php';

if (!isset($_SESSION['username'])) {
	header('Location: index.php');
	exit();
}

$username = $_SESSION['username'];

$db = new Database();
$conn = $db->getConnection();

if ($conn) {
	// Reset battery level to 10-50% on logout (like phonejava behavior)
	try {
		$newLevel = 10 + rand(0, 40); // 10-50%
		$reset = $conn->prepare("UPDATE battery_levels SET battery_level = :lvl, initial_battery_level = :lvl WHERE username = :username");
		$reset->bindParam(':lvl', $newLevel);
		$reset->bindParam(':username', $username);
		$reset->execute();
		
		// Also update last_updated timestamp
		$timestamp = $conn->prepare("UPDATE battery_levels SET last_updated = NOW() WHERE username = :username");
		$timestamp->bindParam(':username', $username);
		$timestamp->execute();
	} catch (Exception $e) {
		// swallow DB errors to ensure logout completes
	}
}

// Clear session and redirect to login
$_SESSION = [];
session_destroy();
header('Location: index.php');
exit();
?>


