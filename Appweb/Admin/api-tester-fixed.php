<?php
// Fixed API Endpoint Tester for Admin
session_start();

// Enable error reporting
error_reporting(E_ALL);
ini_set('display_errors', 1);

echo "<h1>🔌 Fixed Admin API Tester</h1>";
echo "<div style='font-family: Arial, sans-serif; max-width: 1000px; margin: 0 auto; padding: 20px;'>";

// Test 1: Check Database Connection
echo "<h2>📊 Step 1: Database Connection Test</h2>";
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
        echo "</div>";
    }
} catch (Exception $e) {
    echo "<div style='color: red; padding: 10px; background: #f8d7da; border-radius: 5px; margin: 10px 0;'>";
    echo "❌ Database error: " . $e->getMessage() . "<br>";
    echo "</div>";
}

// Test 2: Test API Files Exist
echo "<h2>📁 Step 2: API Files Check</h2>";

$apiFiles = [
    'api/admin-clean.php' => 'Clean Admin API',
    'api/admin.php' => 'Main Admin API',
    'api/mobile.php' => 'Mobile API'
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

// Test 3: Direct API Testing
echo "<h2>🧪 Step 3: Direct API Testing</h2>";

$adminEndpoints = [
    'dashboard' => 'Dashboard statistics and recent activity',
    'queue' => 'Queue tickets data',
    'bays' => 'Charging bays status',
    'users' => 'User management data'
];

foreach ($adminEndpoints as $action => $description) {
    echo "<h3>Testing Admin: $action</h3>";
    echo "<p><strong>Description:</strong> $description</p>";
    
    // Test by including the API file directly
    try {
        // Set up the environment for the API
        $_GET['action'] = $action;
        
        // Capture output
        ob_start();
        include 'api/admin-clean.php';
        $response = ob_get_clean();
        
        if ($response) {
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
        } else {
            echo "<div style='color: red; padding: 10px; background: #f8d7da; border-radius: 5px; margin: 10px 0;'>";
            echo "❌ Admin API endpoint returned empty response<br>";
            echo "</div>";
        }
    } catch (Exception $e) {
        echo "<div style='color: red; padding: 10px; background: #f8d7da; border-radius: 5px; margin: 10px 0;'>";
        echo "❌ Admin API endpoint error: " . $e->getMessage() . "<br>";
        echo "</div>";
    }
}

// Test 4: Browser Testing Links
echo "<h2>🔍 Step 4: Browser Testing Links</h2>";

echo "<h3>Admin API Endpoints:</h3>";
echo "<ul>";
echo "<li><a href='api/admin-clean.php?action=dashboard' target='_blank'>Admin Dashboard API</a></li>";
echo "<li><a href='api/admin-clean.php?action=queue' target='_blank'>Admin Queue API</a></li>";
echo "<li><a href='api/admin-clean.php?action=bays' target='_blank'>Admin Bays API</a></li>";
echo "<li><a href='api/admin-clean.php?action=users' target='_blank'>Admin Users API</a></li>";
echo "</ul>";

echo "<h3>Admin Interface:</h3>";
echo "<ul>";
echo "<li><a href='index.php' target='_blank'>Admin Dashboard</a></li>";
echo "<li><a href='login.php' target='_blank'>Admin Login</a></li>";
echo "</ul>";

// Test 5: JavaScript API Test
echo "<h2>🔧 Step 5: JavaScript API Test</h2>";

echo "<div id='js-test-results'></div>";
echo "<button onclick='testAdminAPIs()' style='padding: 10px 20px; background: #3498db; color: white; border: none; border-radius: 5px; cursor: pointer;'>Test Admin APIs with JavaScript</button>";

echo "<script>
async function testAdminAPIs() {
    const resultsDiv = document.getElementById('js-test-results');
    resultsDiv.innerHTML = '<p>Testing Admin APIs...</p>';
    
    const adminEndpoints = ['dashboard', 'queue', 'bays', 'users'];
    let results = '<h3>Admin API Test Results:</h3>';
    
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
echo "<h3>✅ Admin API Status:</h3>";
echo "<ul>";
echo "<li><strong>Database Connection:</strong> Tested and verified</li>";
echo "<li><strong>API Files:</strong> Checked for existence and readability</li>";
echo "<li><strong>Direct Testing:</strong> API endpoints tested directly</li>";
echo "<li><strong>Browser Testing:</strong> Direct links provided</li>";
echo "<li><strong>JavaScript Testing:</strong> Client-side API testing</li>";
echo "</ul>";

echo "<h3>🚀 Next Steps:</h3>";
echo "<ol>";
echo "<li>Click the browser testing links to test APIs directly</li>";
echo "<li>Use the JavaScript test button to test client-side</li>";
echo "<li>Check browser console for any errors</li>";
echo "<li>Test the admin interface functionality</li>";
echo "</ol>";
echo "</div>";

echo "</div>";
?>
