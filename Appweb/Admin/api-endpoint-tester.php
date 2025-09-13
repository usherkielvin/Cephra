<?php
// Comprehensive API Endpoint Tester for WEBSITE Structure
session_start();

// Enable error reporting
error_reporting(E_ALL);
ini_set('display_errors', 1);

echo "<h1>🔌 API Endpoint Tester - WEBSITE Structure</h1>";
echo "<div style='font-family: Arial, sans-serif; max-width: 1200px; margin: 0 auto; padding: 20px;'>";

// Step 1: Check Database Connection
echo "<h2>📊 Step 1: Database Connection Test</h2>";
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
        
        // Test database tables
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
        echo "This will cause all API endpoints to fail.<br>";
        echo "</div>";
    }
} catch (Exception $e) {
    echo "<div style='color: red; padding: 10px; background: #f8d7da; border-radius: 5px; margin: 10px 0;'>";
    echo "❌ Database error: " . $e->getMessage() . "<br>";
    echo "</div>";
}

// Step 2: Test Admin API Endpoints
echo "<h2>🔧 Step 2: Admin API Endpoints Test</h2>";

$adminEndpoints = [
    'dashboard' => 'Dashboard statistics and recent activity',
    'queue' => 'Queue tickets data',
    'bays' => 'Charging bays status',
    'users' => 'User management data'
];

foreach ($adminEndpoints as $action => $description) {
    echo "<h3>Testing Admin: $action</h3>";
    echo "<p><strong>Description:</strong> $description</p>";
    
    // Test the admin clean API
    $url = "api/admin-clean.php?action=$action";
    echo "<p><strong>URL:</strong> <code>$url</code></p>";
    
    // Use absolute path for file_get_contents
    $absolutePath = __DIR__ . "/api/admin-clean.php?action=$action";
    echo "<p><strong>Absolute Path:</strong> <code>$absolutePath</code></p>";
    
    $context = stream_context_create([
        'http' => [
            'method' => 'GET',
            'timeout' => 10
        ]
    ]);
    
    $response = @file_get_contents($absolutePath, false, $context);
    
    if ($response === false) {
        echo "<div style='color: red; padding: 10px; background: #f8d7da; border-radius: 5px; margin: 10px 0;'>";
        echo "❌ Failed to connect to Admin API endpoint<br>";
        echo "Error: " . error_get_last()['message'] . "<br>";
        echo "</div>";
    } else {
        $data = json_decode($response, true);
        if ($data && isset($data['success']) && $data['success']) {
            echo "<div style='color: green; padding: 10px; background: #d4edda; border-radius: 5px; margin: 10px 0;'>";
            echo "✅ Admin API endpoint working correctly<br>";
            echo "Response length: " . strlen($response) . " characters<br>";
            echo "Response preview: " . substr($response, 0, 200) . "...<br>";
            echo "</div>";
        } else {
            echo "<div style='color: orange; padding: 10px; background: #fff3cd; border-radius: 5px; margin: 10px 0;'>";
            echo "⚠️ Admin API endpoint responded but with errors<br>";
            echo "Response: " . $response . "<br>";
            echo "</div>";
        }
    }
}

// Step 3: Test User API Endpoints
echo "<h2>👥 Step 3: User API Endpoints Test</h2>";

$userEndpoints = [
    'mobile' => 'Mobile API for user interface',
    'test' => 'Test endpoint'
];

foreach ($userEndpoints as $action => $description) {
    echo "<h3>Testing User: $action</h3>";
    echo "<p><strong>Description:</strong> $description</p>";
    
    // Test the user API
    $url = "../User/api/mobile.php?action=$action";
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
            echo "Response length: " . strlen($response) . " characters<br>";
            echo "Response preview: " . substr($response, 0, 200) . "...<br>";
            echo "</div>";
        } else {
            echo "<div style='color: orange; padding: 10px; background: #fff3cd; border-radius: 5px; margin: 10px 0;'>";
            echo "⚠️ User API endpoint responded but with errors<br>";
            echo "Response: " . $response . "<br>";
            echo "</div>";
        }
    }
}

// Step 4: Test Monitor API Endpoints
echo "<h2>📊 Step 4: Monitor API Endpoints Test</h2>";

echo "<h3>Testing Monitor Interface</h3>";
echo "<p><strong>Description:</strong> Monitor interface access</p>";

// Test the monitor interface
$url = "../Monitor/index.php";
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
    echo "❌ Failed to connect to Monitor interface<br>";
    echo "Error: " . error_get_last()['message'] . "<br>";
    echo "</div>";
} else {
    echo "<div style='color: green; padding: 10px; background: #d4edda; border-radius: 5px; margin: 10px 0;'>";
    echo "✅ Monitor interface accessible<br>";
    echo "Response length: " . strlen($response) . " characters<br>";
    echo "Response preview: " . substr($response, 0, 200) . "...<br>";
    echo "</div>";
}

// Step 5: Direct Browser Testing Links
echo "<h2>🔍 Step 5: Direct Browser Testing</h2>";

echo "<h3>Admin API Endpoints:</h3>";
echo "<ul>";
echo "<li><a href='api/admin-clean.php?action=dashboard' target='_blank'>Admin Dashboard API</a></li>";
echo "<li><a href='api/admin-clean.php?action=queue' target='_blank'>Admin Queue API</a></li>";
echo "<li><a href='api/admin-clean.php?action=bays' target='_blank'>Admin Bays API</a></li>";
echo "<li><a href='api/admin-clean.php?action=users' target='_blank'>Admin Users API</a></li>";
echo "</ul>";

echo "<h3>User Interface:</h3>";
echo "<ul>";
echo "<li><a href='../User/index.php' target='_blank'>User Dashboard</a></li>";
echo "<li><a href='../User/profile.php' target='_blank'>User Profile</a></li>";
echo "<li><a href='../User/history.php' target='_blank'>User History</a></li>";
echo "</ul>";

echo "<h3>Monitor Interface:</h3>";
echo "<ul>";
echo "<li><a href='../Monitor/index.php' target='_blank'>Monitor Dashboard</a></li>";
echo "</ul>";

// Step 6: JavaScript API Test
echo "<h2>🔧 Step 6: JavaScript API Test</h2>";

echo "<div id='js-test-results'></div>";
echo "<button onclick='testAllAPIs()' style='padding: 10px 20px; background: #3498db; color: white; border: none; border-radius: 5px; cursor: pointer;'>Test All APIs with JavaScript</button>";

echo "<script>
async function testAllAPIs() {
    const resultsDiv = document.getElementById('js-test-results');
    resultsDiv.innerHTML = '<p>Testing all APIs...</p>';
    
    const adminEndpoints = ['dashboard', 'queue', 'bays', 'users'];
    let results = '<h3>Admin API Tests:</h3>';
    
    for (const endpoint of adminEndpoints) {
        try {
            const response = await fetch('api/admin-clean.php?action=' + endpoint);
            const data = await response.json();
            
            if (data.success) {
                results += '<div style=\"color: green; margin: 5px 0;\">✅ Admin ' + endpoint + ': Working</div>';
            } else {
                results += '<div style=\"color: red; margin: 5px 0;\">❌ Admin ' + endpoint + ': ' + data.error + '</div>';
            }
        } catch (error) {
            results += '<div style=\"color: red; margin: 5px 0;\">❌ Admin ' + endpoint + ': ' + error.message + '</div>';
        }
    }
    
    resultsDiv.innerHTML = results;
}
</script>";

// Summary
echo "<h2>📋 Summary</h2>";
echo "<div style='background: #e7f3ff; padding: 20px; border-radius: 10px; margin: 20px 0;'>";
echo "<h3>🔧 API Endpoint Status:</h3>";
echo "<ul>";
echo "<li><strong>Admin API:</strong> Clean API without authentication</li>";
echo "<li><strong>User API:</strong> Mobile API for user interface</li>";
echo "<li><strong>Monitor API:</strong> Monitor interface access</li>";
echo "</ul>";

echo "<h3>🚀 Next Steps:</h3>";
echo "<ol>";
echo "<li>Test Admin API endpoints directly in browser</li>";
echo "<li>Test User interface functionality</li>";
echo "<li>Test Monitor interface access</li>";
echo "<li>Check browser console for JavaScript errors</li>";
echo "</ol>";

echo "<h3>🔧 If Issues Found:</h3>";
echo "<ul>";
echo "<li>Check database connection in each folder</li>";
echo "<li>Verify file paths are correct</li>";
echo "<li>Check file permissions</li>";
echo "<li>Look at browser console for errors</li>";
echo "</ul>";
echo "</div>";

echo "</div>";
?>
