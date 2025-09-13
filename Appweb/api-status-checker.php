<?php
// Comprehensive API Status Checker for All WEBSITE Sections
session_start();

// Enable error reporting
error_reporting(E_ALL);
ini_set('display_errors', 1);

echo "<h1>🔌 Complete API Status Checker - WEBSITE Structure</h1>";
echo "<div style='font-family: Arial, sans-serif; max-width: 1200px; margin: 0 auto; padding: 20px;'>";

// Test 1: Database Connection Test
echo "<h2>📊 Step 1: Database Connection Test</h2>";

$configPaths = [
    'Admin' => 'Admin/config/database.php',
    'User' => 'User/config/database.php'
];

foreach ($configPaths as $section => $configPath) {
    echo "<h3>Testing $section Database Connection</h3>";
    
    try {
        require_once $configPath;
        $db = new Database();
        $conn = $db->getConnection();
        
        if ($conn) {
            echo "<div style='color: green; padding: 10px; background: #d4edda; border-radius: 5px; margin: 10px 0;'>";
            echo "✅ $section database connection successful!<br>";
            echo "</div>";
        } else {
            echo "<div style='color: red; padding: 10px; background: #f8d7da; border-radius: 5px; margin: 10px 0;'>";
            echo "❌ $section database connection failed!<br>";
            echo "</div>";
        }
    } catch (Exception $e) {
        echo "<div style='color: red; padding: 10px; background: #f8d7da; border-radius: 5px; margin: 10px 0;'>";
        echo "❌ $section database error: " . $e->getMessage() . "<br>";
        echo "</div>";
    }
}

// Test 2: Admin API Endpoints
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
    
    $url = "Admin/api/admin-clean.php?action=$action";
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

// Test 3: User API Endpoints
echo "<h2>👥 Step 3: User API Endpoints Test</h2>";

$userEndpoints = [
    'test' => 'Test API connection',
    'available-bays' => 'Get available charging bays',
    'user-profile' => 'Get user profile data',
    'user-history' => 'Get user charging history'
];

foreach ($userEndpoints as $action => $description) {
    echo "<h3>Testing User: $action</h3>";
    echo "<p><strong>Description:</strong> $description</p>";
    
    $url = "User/api/mobile.php?action=$action";
    if ($action === 'user-profile' || $action === 'user-history') {
        $url .= "&username=admin";
    }
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

// Test 4: Monitor Interface
echo "<h2>📊 Step 4: Monitor Interface Test</h2>";

echo "<h3>Testing Monitor Interface</h3>";
echo "<p><strong>Description:</strong> Monitor interface access</p>";

$url = "Monitor/index.php";
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

// Test 5: Direct Browser Testing Links
echo "<h2>🔍 Step 5: Direct Browser Testing Links</h2>";

echo "<h3>Admin API Endpoints:</h3>";
echo "<ul>";
echo "<li><a href='Admin/api/admin-clean.php?action=dashboard' target='_blank'>Admin Dashboard API</a></li>";
echo "<li><a href='Admin/api/admin-clean.php?action=queue' target='_blank'>Admin Queue API</a></li>";
echo "<li><a href='Admin/api/admin-clean.php?action=bays' target='_blank'>Admin Bays API</a></li>";
echo "<li><a href='Admin/api/admin-clean.php?action=users' target='_blank'>Admin Users API</a></li>";
echo "</ul>";

echo "<h3>User API Endpoints:</h3>";
echo "<ul>";
echo "<li><a href='User/api/mobile.php?action=test' target='_blank'>User Test API</a></li>";
echo "<li><a href='User/api/mobile.php?action=available-bays' target='_blank'>Available Bays API</a></li>";
echo "<li><a href='User/api/mobile.php?action=user-profile&username=admin' target='_blank'>User Profile API</a></li>";
echo "<li><a href='User/api/mobile.php?action=user-history&username=admin' target='_blank'>User History API</a></li>";
echo "</ul>";

echo "<h3>Interface Access:</h3>";
echo "<ul>";
echo "<li><a href='Admin/index.php' target='_blank'>Admin Interface</a></li>";
echo "<li><a href='User/index.php' target='_blank'>User Interface</a></li>";
echo "<li><a href='Monitor/index.php' target='_blank'>Monitor Interface</a></li>";
echo "</ul>";

// Test 6: JavaScript API Test
echo "<h2>🔧 Step 6: JavaScript API Test</h2>";

echo "<div id='js-test-results'></div>";
echo "<button onclick='testAllAPIs()' style='padding: 10px 20px; background: #3498db; color: white; border: none; border-radius: 5px; cursor: pointer;'>Test All APIs with JavaScript</button>";

echo "<script>
async function testAllAPIs() {
    const resultsDiv = document.getElementById('js-test-results');
    resultsDiv.innerHTML = '<p>Testing all APIs...</p>';
    
    let results = '<h3>API Test Results:</h3>';
    
    // Test Admin APIs
    const adminEndpoints = ['dashboard', 'queue', 'bays', 'users'];
    results += '<h4>Admin APIs:</h4>';
    
    for (const endpoint of adminEndpoints) {
        try {
            const response = await fetch('Admin/api/admin-clean.php?action=' + endpoint);
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
    
    // Test User APIs
    const userEndpoints = ['test', 'available-bays'];
    results += '<h4>User APIs:</h4>';
    
    for (const endpoint of userEndpoints) {
        try {
            const response = await fetch('User/api/mobile.php?action=' + endpoint);
            const data = await response.json();
            
            if (data.success) {
                results += '<div style=\"color: green; margin: 5px 0;\">✅ User ' + endpoint + ': Working</div>';
            } else {
                results += '<div style=\"color: red; margin: 5px 0;\">❌ User ' + endpoint + ': ' + data.error + '</div>';
            }
        } catch (error) {
            results += '<div style=\"color: red; margin: 5px 0;\">❌ User ' + endpoint + ': ' + error.message + '</div>';
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
echo "<li><strong>Admin API:</strong> Clean API without authentication - 4 endpoints</li>";
echo "<li><strong>User API:</strong> Mobile API for user interface - 6 endpoints</li>";
echo "<li><strong>Monitor Interface:</strong> Monitor dashboard access</li>";
echo "</ul>";

echo "<h3>🚀 Access URLs:</h3>";
echo "<ul>";
echo "<li><strong>Admin:</strong> <code>http://localhost/Cephra/WEBSITE/Admin/</code></li>";
echo "<li><strong>User:</strong> <code>http://localhost/Cephra/WEBSITE/User/</code></li>";
echo "<li><strong>Monitor:</strong> <code>http://localhost/Cephra/WEBSITE/Monitor/</code></li>";
echo "</ul>";

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
