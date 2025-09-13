<?php
// Quick Connection Test for Cephra Admin
session_start();

// Enable error reporting
error_reporting(E_ALL);
ini_set('display_errors', 1);

echo "<h1>⚡ Quick Connection Test</h1>";
echo "<div style='font-family: Arial, sans-serif; max-width: 800px; margin: 0 auto; padding: 20px;'>";

// Test 1: Database Connection
echo "<h2>📊 Database Connection</h2>";
require_once 'config/database.php';

try {
    $db = new Database();
    $conn = $db->getConnection();
    
    if ($conn) {
        echo "<div style='color: green; padding: 10px; background: #d4edda; border-radius: 5px; margin: 10px 0;'>";
        echo "✅ Database connection successful!<br>";
        echo "Host: localhost<br>";
        echo "Database: cephradb<br>";
        echo "Username: root<br>";
        echo "</div>";
        
        // Quick table check
        $tables = ['users', 'queue_tickets', 'charging_bays'];
        foreach ($tables as $table) {
            try {
                $stmt = $conn->query("SELECT COUNT(*) as count FROM $table");
                $count = $stmt->fetch(PDO::FETCH_ASSOC)['count'];
                echo "<div style='color: green; margin: 5px 0;'>✅ Table '$table': $count records</div>";
            } catch (Exception $e) {
                echo "<div style='color: red; margin: 5px 0;'>❌ Table '$table': " . $e->getMessage() . "</div>";
            }
        }
    } else {
        echo "<div style='color: red; padding: 10px; background: #f8d7da; border-radius: 5px; margin: 10px 0;'>";
        echo "❌ Database connection failed!<br>";
        echo "This is the root cause of API failures.<br>";
        echo "</div>";
    }
} catch (Exception $e) {
    echo "<div style='color: red; padding: 10px; background: #f8d7da; border-radius: 5px; margin: 10px 0;'>";
    echo "❌ Database error: " . $e->getMessage() . "<br>";
    echo "</div>";
}

// Test 2: API Endpoints
echo "<h2>🔌 API Endpoints Test</h2>";

$endpoints = [
    'dashboard' => 'Dashboard data',
    'queue' => 'Queue tickets',
    'bays' => 'Charging bays',
    'users' => 'User data'
];

foreach ($endpoints as $action => $description) {
    echo "<h3>Testing: $action</h3>";
    
    // Test the clean API
    $url = "../api/admin-clean.php?action=$action";
    $context = stream_context_create([
        'http' => [
            'method' => 'GET',
            'timeout' => 5
        ]
    ]);
    
    $response = @file_get_contents($url, false, $context);
    
    if ($response === false) {
        echo "<div style='color: red; padding: 10px; background: #f8d7da; border-radius: 5px; margin: 10px 0;'>";
        echo "❌ Failed to connect to API endpoint<br>";
        echo "URL: $url<br>";
        echo "Error: " . error_get_last()['message'] . "<br>";
        echo "</div>";
    } else {
        $data = json_decode($response, true);
        if ($data && isset($data['success']) && $data['success']) {
            echo "<div style='color: green; padding: 10px; background: #d4edda; border-radius: 5px; margin: 10px 0;'>";
            echo "✅ API endpoint working correctly<br>";
            echo "Response: " . substr($response, 0, 100) . "...<br>";
            echo "</div>";
        } else {
            echo "<div style='color: orange; padding: 10px; background: #fff3cd; border-radius: 5px; margin: 10px 0;'>";
            echo "⚠️ API endpoint responded but with errors<br>";
            echo "Response: " . $response . "<br>";
            echo "</div>";
        }
    }
}

// Test 3: File Permissions
echo "<h2>📁 File Permissions Check</h2>";

$files = [
    '../api/admin-clean.php' => 'Clean API file',
    'js/admin-clean.js' => 'Clean JavaScript file',
    'test-clean.html' => 'Test page'
];

foreach ($files as $file => $description) {
    if (file_exists($file)) {
        if (is_readable($file)) {
            echo "<div style='color: green; margin: 5px 0;'>✅ $description: $file - Readable</div>";
        } else {
            echo "<div style='color: red; margin: 5px 0;'>❌ $description: $file - Not readable</div>";
        }
    } else {
        echo "<div style='color: red; margin: 5px 0;'>❌ $description: $file - Not found</div>";
    }
}

// Summary
echo "<h2>📋 Summary</h2>";
echo "<div style='background: #e7f3ff; padding: 20px; border-radius: 10px; margin: 20px 0;'>";
echo "<h3>🚀 Ready to Test:</h3>";
echo "<ol>";
echo "<li><strong>Database Setup:</strong> Run <code>mobileweb/admin/setup-database.php</code> if needed</li>";
echo "<li><strong>Test Admin Interface:</strong> Visit <code>mobileweb/admin/test-clean.html</code></li>";
echo "<li><strong>Check Console:</strong> Open browser developer tools to see any errors</li>";
echo "</ol>";

echo "<h3>🔧 If Still Having Issues:</h3>";
echo "<ul>";
echo "<li>Start XAMPP MySQL service</li>";
echo "<li>Check database 'cephradb' exists</li>";
echo "<li>Verify file permissions</li>";
echo "<li>Look at browser console for JavaScript errors</li>";
echo "</ul>";
echo "</div>";

echo "</div>";
?>
