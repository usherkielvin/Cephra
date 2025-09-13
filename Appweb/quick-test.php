<?php
// Quick Test Tool for Appweb
session_start();

// Enable error reporting
error_reporting(E_ALL);
ini_set('display_errors', 1);

echo "<h1>⚡ Quick Test - Appweb</h1>";
echo "<div style='font-family: Arial, sans-serif; max-width: 1000px; margin: 0 auto; padding: 20px;'>";

// Test 1: Database Connection
echo "<h2>📊 Database Connection Test</h2>";

$configPaths = [
    'Admin' => 'Admin/config/database.php',
    'User' => 'User/config/database.php'
];

foreach ($configPaths as $section => $configPath) {
    echo "<h3>$section Database</h3>";
    
    try {
        require_once $configPath;
        $db = new Database();
        $conn = $db->getConnection();
        
        if ($conn) {
            echo "<div style='color: green; padding: 10px; background: #d4edda; border-radius: 5px; margin: 10px 0;'>";
            echo "✅ $section database connection successful!<br>";
            echo "</div>";
            
            // Quick table check
            $tables = ['users', 'queue_tickets', 'charging_bays'];
            foreach ($tables as $table) {
                try {
                    $stmt = $conn->query("SELECT COUNT(*) as count FROM $table");
                    $count = $stmt->fetch(PDO::FETCH_ASSOC)['count'];
                    echo "<div style='color: green; margin: 5px 0;'>✅ $section Table '$table': $count records</div>";
                } catch (Exception $e) {
                    echo "<div style='color: red; margin: 5px 0;'>❌ $section Table '$table': " . $e->getMessage() . "</div>";
                }
            }
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

// Test 2: API Files Check
echo "<h2>📁 API Files Check</h2>";

$apiFiles = [
    'Admin/api/admin-clean.php' => 'Admin Clean API',
    'User/api/mobile.php' => 'User Mobile API'
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

// Test 3: Quick API Test
echo "<h2>🧪 Quick API Test</h2>";

// Test Admin API
echo "<h3>Testing Admin API</h3>";
try {
    $_GET['action'] = 'dashboard';
    ob_start();
    include 'Admin/api/admin-clean.php';
    $response = ob_get_clean();
    
    $data = json_decode($response, true);
    if ($data && isset($data['success']) && $data['success']) {
        echo "<div style='color: green; padding: 10px; background: #d4edda; border-radius: 5px; margin: 10px 0;'>";
        echo "✅ Admin API working correctly<br>";
        echo "Total Users: " . $data['stats']['total_users'] . "<br>";
        echo "Queue Count: " . $data['stats']['queue_count'] . "<br>";
        echo "Active Bays: " . $data['stats']['active_bays'] . "<br>";
        echo "</div>";
    } else {
        echo "<div style='color: orange; padding: 10px; background: #fff3cd; border-radius: 5px; margin: 10px 0;'>";
        echo "⚠️ Admin API responded but with errors<br>";
        echo "Response: " . $response . "<br>";
        echo "</div>";
    }
} catch (Exception $e) {
    echo "<div style='color: red; padding: 10px; background: #f8d7da; border-radius: 5px; margin: 10px 0;'>";
    echo "❌ Admin API error: " . $e->getMessage() . "<br>";
    echo "</div>";
}

// Test User API
echo "<h3>Testing User API</h3>";
try {
    $_GET['action'] = 'test';
    ob_start();
    include 'User/api/mobile.php';
    $response = ob_get_clean();
    
    $data = json_decode($response, true);
    if ($data && isset($data['success']) && $data['success']) {
        echo "<div style='color: green; padding: 10px; background: #d4edda; border-radius: 5px; margin: 10px 0;'>";
        echo "✅ User API working correctly<br>";
        echo "Message: " . $data['message'] . "<br>";
        echo "</div>";
    } else {
        echo "<div style='color: orange; padding: 10px; background: #fff3cd; border-radius: 5px; margin: 10px 0;'>";
        echo "⚠️ User API responded but with errors<br>";
        echo "Response: " . $response . "<br>";
        echo "</div>";
    }
} catch (Exception $e) {
    echo "<div style='color: red; padding: 10px; background: #f8d7da; border-radius: 5px; margin: 10px 0;'>";
    echo "❌ User API error: " . $e->getMessage() . "<br>";
    echo "</div>";
}

// Test 4: Interface Access
echo "<h2>🌐 Interface Access Test</h2>";

$interfaces = [
    'Admin/index.php' => 'Admin Interface',
    'User/index.php' => 'User Interface',
    'Monitor/index.php' => 'Monitor Interface'
];

foreach ($interfaces as $file => $description) {
    if (file_exists($file)) {
        echo "<div style='color: green; margin: 5px 0;'>✅ {$description}: {$file} - Exists</div>";
    } else {
        echo "<div style='color: red; margin: 5px 0;'>❌ {$description}: {$file} - Not found</div>";
    }
}

// Summary
echo "<h2>📋 Summary</h2>";
echo "<div style='background: #e7f3ff; padding: 20px; border-radius: 10px; margin: 20px 0;'>";
echo "<h3>🚀 Quick Access Links:</h3>";
echo "<ul>";
echo "<li><a href='database-connection-checker.php' target='_blank'>Database Connection Checker</a></li>";
echo "<li><a href='api-connection-checker.php' target='_blank'>API Connection Checker</a></li>";
echo "<li><a href='Admin/index.php' target='_blank'>Admin Interface</a></li>";
echo "<li><a href='User/index.php' target='_blank'>User Interface</a></li>";
echo "<li><a href='Monitor/index.php' target='_blank'>Monitor Interface</a></li>";
echo "</ul>";

echo "<h3>🔧 API Test Links:</h3>";
echo "<ul>";
echo "<li><a href='Admin/api/admin-clean.php?action=dashboard' target='_blank'>Admin Dashboard API</a></li>";
echo "<li><a href='User/api/mobile.php?action=test' target='_blank'>User Test API</a></li>";
echo "<li><a href='User/api/mobile.php?action=available-bays' target='_blank'>Available Bays API</a></li>";
echo "</ul>";
echo "</div>";

echo "</div>";
?>
