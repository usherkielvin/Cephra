<?php
// Test path resolution
echo "Current directory: " . __DIR__ . "\n";
echo "Attempting to include: " . __DIR__ . "/../config/database.php\n";

if (file_exists("../config/database.php")) {
    echo "File exists!\n";
    require_once "../config/database.php";

    try {
        $db = (new Database())->getConnection();
        if ($db) {
            echo "Database connection successful!\n";
        } else {
            echo "Database connection failed!\n";
        }
    } catch (Exception $e) {
        echo "Error: " . $e->getMessage() . "\n";
    }
} else {
    echo "File does not exist!\n";
    echo "Looking for: " . realpath("../config/database.php") . "\n";
}
?>
