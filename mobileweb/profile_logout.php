<?php
session_start();
require_once '../config/database.php';

if (!isset($_SESSION['username'])) {
	header('Location: index.php');
	exit();
}

$username = $_SESSION['username'];

$db = new Database();
$conn = $db->getConnection();

if ($conn) {
	// If battery level is 100, randomize to 15–50%; otherwise retain existing level
	try {
		$stmt = $conn->prepare("SELECT battery_level FROM battery_levels WHERE username = :username LIMIT 1");
		$stmt->bindParam(':username', $username);
		$stmt->execute();
		$row = $stmt->fetch(PDO::FETCH_ASSOC);
		if ($row) {
			$level = (int)($row['battery_level'] ?? 0);
			if ($level >= 100) {
				$newLevel = 15 + rand(0, 35); // 15–50
				$reset = $conn->prepare("UPDATE battery_levels SET battery_level = :lvl, initial_battery_level = :lvl WHERE username = :username");
				$reset->bindParam(':lvl', $newLevel);
				$reset->bindParam(':username', $username);
				$reset->execute();
			}
		}
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


