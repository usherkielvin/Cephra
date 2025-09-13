<?php
// User API Tester
session_start();

// Enable error reporting
error_reporting(E_ALL);
ini_set('display_errors', 1);

echo "<h1>👥 User API Tester</h1>";
echo "<div style='font-family: Arial, sans-serif; max-width: 1000px; margin: 0 auto; padding: 20px;'>";

// Test 1: Database Connection
echo "<h2>📊 Database Connection Test</h2>";
require_once 'config/database.php';

try {
    $db = new Database();
    $conn = $db->getConnection();
    
    if ($conn) {
        echo "<div style='color: green; padding: 10px; background: #d4edda; border-radius: 5px; margin: 10px 0;'>";
        echo "✅ Database connection successful!<br>";
        echo "</div>";
    } else {
        echo "<div style='color: red; padding: 10px; background: #f8d7da; border-radius: 5px; margin: 10px 0;'>";
        echo "❌ Database connection failed!<br>";
        echo "</div>";
    }
} catch (Exception $e) {
    echo "<div style='color: red; padding: 10px; background: #f8d7da; border-radius: 5px; margin: 10px 0;'>";
    echo "❌ Database error: " . $e->getMessage() . "<br>";
    echo "</div>";
}

// Test 2: User API Endpoints
echo "<h2>🧪 User API Endpoints Test</h2>";

$userEndpoints = [
    'test' => 'Test API connection',
    'user-profile' => 'Get user profile data',
    'user-history' => 'Get user charging history',
    'available-bays' => 'Get available charging bays',
    'create-ticket' => 'Create new charging ticket',
    'register-user' => 'Register new user'
];

foreach ($userEndpoints as $action => $description) {
    echo "<h3>Testing: $action</h3>";
    echo "<p><strong>Description:</strong> $description</p>";
    
    // Test the user API
    $url = "api/mobile.php?action=$action";
    echo "<p><strong>URL:</strong> <code>$url</code></p>";
    
    $context = stream_context_create([
        'http' => [
            'method' => 'GET',
            'timeout' => 10
        ]
    ]);
    
    $response = @file_get_contents($url, false, $context);
    
    if ($response === false) {
        echo "<div style='color: red; padding: 10px; background: #f8d7da; border-radius: 5px; margin: 10px 0;'>";
        echo "❌ Failed to connect to User API endpoint<br>";
        echo "Error: " . error_get_last()['message'] . "<br>";
        echo "</div>";
    } else {
        $data = json_decode($response, true);
        if ($data && isset($data['success']) && $data['success']) {
            echo "<div style='color: green; padding: 10px; background: #d4edda; border-radius: 5px; margin: 10px 0;'>";
            echo "✅ User API endpoint working correctly<br>";
            echo "Response: " . substr($response, 0, 200) . "...<br>";
            echo "</div>";
        } else {
            echo "<div style='color: orange; padding: 10px; background: #fff3cd; border-radius: 5px; margin: 10px 0;'>";
            echo "⚠️ User API endpoint responded but with errors<br>";
            echo "Response: " . $response . "<br>";
            echo "</div>";
        }
    }
}

// Direct Browser Testing Links
echo "<h2>🔍 Direct Browser Testing</h2>";

echo "<h3>User API Endpoints:</h3>";
echo "<ul>";
echo "<li><a href='api/mobile.php?action=test' target='_blank'>Test API</a></li>";
echo "<li><a href='api/mobile.php?action=available-bays' target='_blank'>Available Bays</a></li>";
echo "<li><a href='api/mobile.php?action=user-profile&username=admin' target='_blank'>User Profile (admin)</a></li>";
echo "<li><a href='api/mobile.php?action=user-history&username=admin' target='_blank'>User History (admin)</a></li>";
echo "</ul>";

echo "<h3>User Interface:</h3>";
echo "<ul>";
echo "<li><a href='index.php' target='_blank'>User Dashboard</a></li>";
echo "<li><a href='profile.php' target='_blank'>User Profile</a></li>";
echo "<li><a href='history.php' target='_blank'>User History</a></li>";
echo "<li><a href='Register_Panel.php' target='_blank'>User Registration</a></li>";
echo "</ul>";

// Summary
echo "<h2>📋 Summary</h2>";
echo "<div style='background: #e7f3ff; padding: 20px; border-radius: 10px; margin: 20px 0;'>";
echo "<h3>✅ User API Status:</h3>";
echo "<ul>";
echo "<li><strong>Mobile API:</strong> User interface API endpoints</li>";
echo "<li><strong>User Profile:</strong> Profile management</li>";
echo "<li><strong>Charging History:</strong> User charging records</li>";
echo "<li><strong>Bay Management:</strong> Available charging bays</li>";
echo "<li><strong>Ticket Creation:</strong> New charging tickets</li>";
echo "<li><strong>User Registration:</strong> New user signup</li>";
echo "</ul>";
echo "</div>";

echo "</div>";
?>
