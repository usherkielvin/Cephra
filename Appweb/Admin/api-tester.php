<?php
// Comprehensive API Tester
session_start();

// Enable error reporting
error_reporting(E_ALL);
ini_set('display_errors', 1);

echo "<h1>🔌 API Endpoint Tester</h1>";
echo "<div style='font-family: Arial, sans-serif; max-width: 1000px; margin: 0 auto; padding: 20px;'>";

// Test 1: Check API Files
echo "<h2>📁 Step 1: API File Check</h2>";

$apiFiles = [
    '../api/admin.php' => 'Main Admin API (with auth)',
    '../api/admin-clean.php' => 'Clean Admin API (no auth)',
    '../api/mobile.php' => 'Mobile API'
];

foreach ($apiFiles as $file => $description) {
    if (file_exists($file)) {
        if (is_readable($file)) {
            echo "<div style='color: green; margin: 5px 0;'>✅ {$description}: {$file} - Readable</div>";
        } else {
            echo "<div style='color: red; margin: 5px 0;'>❌ {$description}: {$file} - Not readable</div>";
        }
    } else {
        echo "<div style='color: red; margin: 5px 0;'>❌ {$description}: {$file} - Not found</div>";
    }
}

// Test 2: Database Connection
echo "<h2>📊 Step 2: Database Connection Test</h2>";
require_once 'config/database.php';

try {
    $db = new Database();
    $conn = $db->getConnection();
    
    if ($conn) {
        echo "<div style='color: green; padding: 10px; background: #d4edda; border-radius: 5px; margin: 10px 0;'>";
        echo "✅ Database connection successful!<br>";
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

// Test 3: Test API Endpoints
echo "<h2>🧪 Step 3: API Endpoint Tests</h2>";

$endpoints = [
    'dashboard' => 'Dashboard statistics and recent activity',
    'queue' => 'Queue tickets data',
    'bays' => 'Charging bays status',
    'users' => 'User management data'
];

foreach ($endpoints as $action => $description) {
    echo "<h3>Testing: $action</h3>";
    echo "<p><strong>Description:</strong> $description</p>";
    
    // Test the clean API
    $url = "../api/admin-clean.php?action=$action";
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
        echo "❌ Failed to connect to API endpoint<br>";
        echo "Error: " . error_get_last()['message'] . "<br>";
        echo "</div>";
    } else {
        $data = json_decode($response, true);
        if ($data && isset($data['success']) && $data['success']) {
            echo "<div style='color: green; padding: 10px; background: #d4edda; border-radius: 5px; margin: 10px 0;'>";
            echo "✅ API endpoint working correctly<br>";
            echo "Response length: " . strlen($response) . " characters<br>";
            echo "Response preview: " . substr($response, 0, 200) . "...<br>";
            echo "</div>";
        } else {
            echo "<div style='color: orange; padding: 10px; background: #fff3cd; border-radius: 5px; margin: 10px 0;'>";
            echo "⚠️ API endpoint responded but with errors<br>";
            echo "Response: " . $response . "<br>";
            echo "</div>";
        }
    }
}

// Test 4: Direct API Test
echo "<h2>🔍 Step 4: Direct API Test</h2>";

echo "<h3>Testing Dashboard Endpoint Directly</h3>";
echo "<p>Click the link below to test the API directly in your browser:</p>";
echo "<p><a href='../api/admin-clean.php?action=dashboard' target='_blank'>Test Dashboard API</a></p>";
echo "<p><a href='../api/admin-clean.php?action=queue' target='_blank'>Test Queue API</a></p>";
echo "<p><a href='../api/admin-clean.php?action=bays' target='_blank'>Test Bays API</a></p>";
echo "<p><a href='../api/admin-clean.php?action=users' target='_blank'>Test Users API</a></p>";

// Test 5: JavaScript Test
echo "<h2>🔧 Step 5: JavaScript API Test</h2>";

echo "<div id='js-test-results'></div>";
echo "<button onclick='testAPIs()' style='padding: 10px 20px; background: #3498db; color: white; border: none; border-radius: 5px; cursor: pointer;'>Test APIs with JavaScript</button>";

echo "<script>
async function testAPIs() {
    const resultsDiv = document.getElementById('js-test-results');
    resultsDiv.innerHTML = '<p>Testing APIs...</p>';
    
    const endpoints = ['dashboard', 'queue', 'bays', 'users'];
    let results = '';
    
    for (const endpoint of endpoints) {
        try {
            const response = await fetch('../api/admin-clean.php?action=' + endpoint);
            const data = await response.json();
            
            if (data.success) {
                results += '<div style=\"color: green; margin: 5px 0;\">✅ ' + endpoint + ': Working</div>';
            } else {
                results += '<div style=\"color: red; margin: 5px 0;\">❌ ' + endpoint + ': ' + data.error + '</div>';
            }
        } catch (error) {
            results += '<div style=\"color: red; margin: 5px 0;\">❌ ' + endpoint + ': ' + error.message + '</div>';
        }
    }
    
    resultsDiv.innerHTML = results;
}
</script>";

// Summary
echo "<h2>📋 Summary</h2>";
echo "<div style='background: #e7f3ff; padding: 20px; border-radius: 10px; margin: 20px 0;'>";
echo "<h3>🔧 Common API Issues & Solutions:</h3>";
echo "<ul>";
echo "<li><strong>Database Connection Failed:</strong> Start XAMPP MySQL service</li>";
echo "<li><strong>File Not Found:</strong> Check file paths and permissions</li>";
echo "<li><strong>JSON Parse Error:</strong> Check for PHP errors in API files</li>";
echo "<li><strong>Empty Response:</strong> Check database tables have data</li>";
echo "<li><strong>CORS Issues:</strong> Check Access-Control headers</li>";
echo "</ul>";

echo "<h3>🚀 Next Steps:</h3>";
echo "<ol>";
echo "<li>Fix any database connection issues</li>";
echo "<li>Test API endpoints directly in browser</li>";
echo "<li>Use JavaScript test button above</li>";
echo "<li>Check browser console for errors</li>";
echo "</ol>";
echo "</div>";

echo "</div>";
?>
