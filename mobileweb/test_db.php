Database connection successful active_tickets table exists charging_bays table exists battery_levels table exists<?php
require_once '../config/database.php';

$db = new Database();
$conn = $db->getConnection();

if (!$conn) {
    echo "Database connection failed\n";
    exit(1);
}

echo "Database connection successful\n";

// Test if active_tickets table exists
try {
    $stmt = $conn->prepare("SHOW TABLES LIKE 'active_tickets'");
    $stmt->execute();
    $result = $stmt->fetch(PDO::FETCH_ASSOC);

    if ($result) {
        echo "active_tickets table exists\n";
    } else {
        echo "active_tickets table does not exist\n";
    }
} catch (Exception $e) {
    echo "Error checking table: " . $e->getMessage() . "\n";
}

// Test if charging_bays table exists
try {
    $stmt = $conn->prepare("SHOW TABLES LIKE 'charging_bays'");
    $stmt->execute();
    $result = $stmt->fetch(PDO::FETCH_ASSOC);

    if ($result) {
        echo "charging_bays table exists\n";
    } else {
        echo "charging_bays table does not exist\n";
    }
} catch (Exception $e) {
    echo "Error checking table: " . $e->getMessage() . "\n";
}

// Test if battery_levels table exists
try {
    $stmt = $conn->prepare("SHOW TABLES LIKE 'battery_levels'");
    $stmt->execute();
    $result = $stmt->fetch(PDO::FETCH_ASSOC);

    if ($result) {
        echo "battery_levels table exists\n";
    } else {
        echo "battery_levels table does not exist\n";
    }
} catch (Exception $e) {
    echo "Error checking table: " . $e->getMessage() . "\n";
}
?>
