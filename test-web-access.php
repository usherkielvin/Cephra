<?php
// Test web access and show any errors
error_reporting(E_ALL);
ini_set('display_errors', 1);

echo "<h2>Cephra Web Access Test</h2>";

echo "<h3>1. PHP Information</h3>";
echo "PHP Version: " . phpversion() . "<br>";
echo "Server: " . ($_SERVER['SERVER_SOFTWARE'] ?? 'Unknown') . "<br>";
echo "Document Root: " . ($_SERVER['DOCUMENT_ROOT'] ?? 'Unknown') . "<br>";
echo "Script Path: " . __FILE__ . "<br>";

echo "<h3>2. Database Connection Test</h3>";
try {
    require_once 'config/database.php';
    $db = new Database();
    $conn = $db->getConnection();
    
    if ($conn) {
        echo "✅ Database connection: SUCCESS<br>";
        
        // Test if required tables exist
        $tables = ['staff_records', 'queue_tickets', 'charging_bays', 'charging_history'];
        foreach ($tables as $table) {
            $stmt = $conn->query("SHOW TABLES LIKE '$table'");
            if ($stmt->rowCount() > 0) {
                echo "✅ Table '$table': EXISTS<br>";
            } else {
                echo "❌ Table '$table': MISSING<br>";
            }
        }
        
    } else {
        echo "❌ Database connection: FAILED<br>";
    }
    
} catch (Exception $e) {
    echo "❌ Database Error: " . $e->getMessage() . "<br>";
}

echo "<h3>3. File Access Test</h3>";
$files_to_check = [
    'Appweb/Admin/index.php',
    'Appweb/Admin/login.php', 
    'Appweb/Admin/api/admin.php',
    'Appweb/Admin/config/database.php'
];

foreach ($files_to_check as $file) {
    if (file_exists($file)) {
        echo "✅ File '$file': EXISTS<br>";
    } else {
        echo "❌ File '$file': MISSING<br>";
    }
}

echo "<h3>4. Session Test</h3>";
session_start();
echo "✅ Session started successfully<br>";
echo "Session ID: " . session_id() . "<br>";

echo "<h3>5. Web Admin Links</h3>";
echo '<a href="Appweb/Admin/login.php">Admin Login</a><br>';
echo '<a href="Appweb/Admin/index.php">Admin Dashboard</a> (requires login)<br>';
echo '<a href="api/debug.php">API Debug</a><br>';
echo '<a href="api/environment-info.php">Environment Info</a><br>';

echo "<h3>6. Error Log Check</h3>";
$error_log = ini_get('error_log');
if ($error_log && file_exists($error_log)) {
    echo "Error log file: $error_log<br>";
    $recent_errors = file_get_contents($error_log);
    if (strlen($recent_errors) > 0) {
        echo "Recent errors:<br><pre>" . htmlspecialchars(substr($recent_errors, -1000)) . "</pre>";
    } else {
        echo "No recent errors found.<br>";
    }
} else {
    echo "No error log file found.<br>";
}
?>
