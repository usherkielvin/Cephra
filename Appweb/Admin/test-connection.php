<?php
// Test connection for web admin interface
session_start();

// Enable error reporting for debugging
error_reporting(E_ALL);
ini_set('display_errors', 1);

echo "<h1>🔧 Cephra Web Admin Connection Test</h1>";
echo "<div style='font-family: Arial, sans-serif; max-width: 800px; margin: 0 auto; padding: 20px;'>";

// Test 1: Database Connection
echo "<h2>📊 Database Connection Test</h2>";
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
                $stmt = $conn->query("SELECT COUNT(*) as count FROM {$table}");
                $result = $stmt->fetch(PDO::FETCH_ASSOC);
                echo "<div style='color: green; margin: 5px 0;'>✅ Table '{$table}': {$result['count']} records</div>";
            } catch (Exception $e) {
                echo "<div style='color: red; margin: 5px 0;'>❌ Table '{$table}': " . $e->getMessage() . "</div>";
            }
        }
    } else {
        echo "<div style='color: red; padding: 10px; background: #f8d7da; border-radius: 5px; margin: 10px 0;'>";
        echo "❌ Database connection failed!<br>";
        echo "Please check:<br>";
        echo "1. XAMPP is running<br>";
        echo "2. MySQL service is started<br>";
        echo "3. Database 'cephradb' exists<br>";
        echo "4. Username 'root' has access<br>";
        echo "</div>";
    }
} catch (Exception $e) {
    echo "<div style='color: red; padding: 10px; background: #f8d7da; border-radius: 5px; margin: 10px 0;'>";
    echo "❌ Database error: " . $e->getMessage();
    echo "</div>";
}

// Test 2: Admin API Endpoints
echo "<h2>🔌 Admin API Endpoints Test</h2>";

$apiEndpoints = [
    'dashboard' => '../api/admin.php?action=dashboard',
    'queue' => '../api/admin.php?action=queue',
    'bays' => '../api/admin.php?action=bays',
    'users' => '../api/admin.php?action=users'
];

foreach ($apiEndpoints as $name => $url) {
    echo "<div style='margin: 10px 0;'>";
    echo "<strong>Testing {$name} endpoint:</strong> ";
    
    $context = stream_context_create([
        'http' => [
            'timeout' => 10,
            'method' => 'GET'
        ]
    ]);
    
    $response = @file_get_contents($url, false, $context);
    
    if ($response === false) {
        echo "<span style='color: red;'>❌ Failed to connect</span>";
    } else {
        $data = json_decode($response, true);
        if ($data && isset($data['success'])) {
            echo "<span style='color: green;'>✅ Working</span>";
            if (isset($data['error'])) {
                echo " - Error: " . $data['error'];
            }
        } else {
            echo "<span style='color: orange;'>⚠️ Response error</span>";
        }
    }
    echo "</div>";
}

// Test 3: Session and Authentication
echo "<h2>🔐 Session & Authentication Test</h2>";

if (isset($_SESSION['admin_logged_in']) && $_SESSION['admin_logged_in'] === true) {
    echo "<div style='color: green; padding: 10px; background: #d4edda; border-radius: 5px; margin: 10px 0;'>";
    echo "✅ Admin session active<br>";
    echo "Username: " . ($_SESSION['admin_username'] ?? 'Unknown') . "<br>";
    echo "</div>";
} else {
    echo "<div style='color: orange; padding: 10px; background: #fff3cd; border-radius: 5px; margin: 10px 0;'>";
    echo "⚠️ No admin session found<br>";
    echo "You need to login first at <a href='login.php'>login.php</a><br>";
    echo "</div>";
}

// Test 4: File Permissions
echo "<h2>📁 File Permissions Test</h2>";

$files = [
    '../api/admin.php',
    '../config/database.php',
    'index.php',
    'login.php',
    'js/admin.js',
    'css/admin.css'
];

foreach ($files as $file) {
    if (file_exists($file)) {
        if (is_readable($file)) {
            echo "<div style='color: green; margin: 5px 0;'>✅ {$file} - Readable</div>";
        } else {
            echo "<div style='color: red; margin: 5px 0;'>❌ {$file} - Not readable</div>";
        }
    } else {
        echo "<div style='color: red; margin: 5px 0;'>❌ {$file} - File not found</div>";
    }
}

// Test 5: JavaScript Console Errors
echo "<h2>🖥️ Browser Console Test</h2>";
echo "<div style='background: #f8f9fa; padding: 15px; border-radius: 5px; margin: 10px 0;'>";
echo "<strong>Instructions:</strong><br>";
echo "1. Open browser developer tools (F12)<br>";
echo "2. Go to Console tab<br>";
echo "3. Refresh the admin page<br>";
echo "4. Look for any red error messages<br>";
echo "5. Check Network tab for failed API requests<br>";
echo "</div>";

// Test 6: Quick Fixes
echo "<h2>🛠️ Quick Fixes</h2>";
echo "<div style='background: #e7f3ff; padding: 15px; border-radius: 5px; margin: 10px 0;'>";
echo "<strong>If you're experiencing issues:</strong><br>";
echo "1. <strong>Recent Activity Loading:</strong> Check database connection above<br>";
echo "2. <strong>Navbar Buttons Not Working:</strong> Check JavaScript console for errors<br>";
echo "3. <strong>API Calls Failing:</strong> Verify XAMPP MySQL is running<br>";
echo "4. <strong>Session Issues:</strong> Clear browser cookies and login again<br>";
echo "5. <strong>File Not Found:</strong> Check file paths and permissions<br>";
echo "</div>";

echo "<h2>🔗 Quick Links</h2>";
echo "<div style='margin: 10px 0;'>";
echo "<a href='login.php' style='margin-right: 10px; padding: 8px 16px; background: #007bff; color: white; text-decoration: none; border-radius: 4px;'>Admin Login</a>";
echo "<a href='index.php' style='margin-right: 10px; padding: 8px 16px; background: #28a745; color: white; text-decoration: none; border-radius: 4px;'>Admin Dashboard</a>";
echo "<a href='../api/admin.php?action=dashboard' style='margin-right: 10px; padding: 8px 16px; background: #17a2b8; color: white; text-decoration: none; border-radius: 4px;'>API Test</a>";
echo "</div>";

echo "</div>";
?>
