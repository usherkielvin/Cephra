<?php
// Cephra CT.WS Connection Test
// This file tests all the key connections and functions

echo "<h1>Cephra CT.WS - Connection Test</h1>";
echo "<style>
    body { font-family: Arial, sans-serif; margin: 40px; background: #f5f5f5; }
    .container { background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
    .success { color: #27ae60; font-weight: bold; }
    .error { color: #e74c3c; font-weight: bold; }
    .info { color: #3498db; font-weight: bold; }
    .warning { color: #f39c12; font-weight: bold; }
    .test-section { margin: 20px 0; padding: 15px; background: #f8f9fa; border-left: 4px solid #3498db; }
    .code { background: #2c3e50; color: white; padding: 10px; border-radius: 5px; font-family: monospace; }
    .file-list { background: #ecf0f1; padding: 10px; border-radius: 5px; margin: 10px 0; }
</style>";

echo "<div class='container'>";

// Test 1: Database Connection
echo "<div class='test-section'>";
echo "<h2>Test 1: Database Connection</h2>";

try {
    require_once 'mobileweb/config/database.php';
    $db = new Database();
    $conn = $db->getConnection();
    
    if ($conn) {
        echo "<p class='success'>✅ Database connection successful!</p>";
        echo "<p>Connected to: <strong>cephradb</strong> database</p>";
        
        // Test basic query
        $stmt = $conn->query("SELECT 1 as test");
        $result = $stmt->fetch(PDO::FETCH_ASSOC);
        if ($result['test'] == 1) {
            echo "<p class='success'>✅ Database query test successful!</p>";
        }
    } else {
        echo "<p class='error'>❌ Database connection failed!</p>";
    }
} catch (Exception $e) {
    echo "<p class='error'>❌ Database error: " . $e->getMessage() . "</p>";
}
echo "</div>";

// Test 2: File Structure Check
echo "<div class='test-section'>";
echo "<h2>Test 2: File Structure Check</h2>";

$required_files = [
    'mobileweb/index.php' => 'Main mobile interface',
    'mobileweb/admin/login.php' => 'Admin login page',
    'mobileweb/admin/index.php' => 'Admin dashboard',
    'mobileweb/api/admin.php' => 'Admin API',
    'mobileweb/api/mobile.php' => 'Mobile API',
    'mobileweb/config/database.php' => 'Database configuration',
    'mobileweb/dashboard.php' => 'User dashboard',
    'mobileweb/Register_Panel.php' => 'User registration',
    'mobileweb/monitor/index.php' => 'Queue monitor',
    'setup_cephra_ct_ws.php' => 'Setup script'
];

$missing_files = [];
foreach ($required_files as $file => $description) {
    if (file_exists($file)) {
        echo "<p class='success'>✅ $description ($file)</p>";
    } else {
        echo "<p class='error'>❌ Missing: $description ($file)</p>";
        $missing_files[] = $file;
    }
}

if (empty($missing_files)) {
    echo "<p class='success'>✅ All required files are present!</p>";
} else {
    echo "<p class='warning'>⚠️ " . count($missing_files) . " files are missing</p>";
}
echo "</div>";

// Test 3: Database Tables Check
echo "<div class='test-section'>";
echo "<h2>Test 3: Database Tables Check</h2>";

if ($conn) {
    $required_tables = ['users', 'queue_tickets', 'charging_bays', 'staff_records', 'system_settings'];
    $missing_tables = [];
    
    foreach ($required_tables as $table) {
        $stmt = $conn->query("SHOW TABLES LIKE '$table'");
        if ($stmt->rowCount() > 0) {
            echo "<p class='success'>✅ Table '$table' exists</p>";
        } else {
            echo "<p class='error'>❌ Table '$table' missing</p>";
            $missing_tables[] = $table;
        }
    }
    
    if (empty($missing_tables)) {
        echo "<p class='success'>✅ All required tables exist!</p>";
    } else {
        echo "<p class='warning'>⚠️ " . count($missing_tables) . " tables are missing</p>";
    }
} else {
    echo "<p class='error'>❌ Cannot check tables - no database connection</p>";
}
echo "</div>";

// Test 4: Admin Authentication Test
echo "<div class='test-section'>";
echo "<h2>Test 4: Admin Authentication Test</h2>";

if ($conn) {
    // Check for admin user
    $stmt = $conn->prepare("SELECT COUNT(*) as count FROM staff_records WHERE username = 'admin' AND status = 'Active'");
    $stmt->execute();
    $admin_count = $stmt->fetch(PDO::FETCH_ASSOC)['count'];
    
    if ($admin_count > 0) {
        echo "<p class='success'>✅ Admin user exists and is active</p>";
        
        // Get admin details
        $stmt = $conn->prepare("SELECT username, name, email, status FROM staff_records WHERE username = 'admin'");
        $stmt->execute();
        $admin = $stmt->fetch(PDO::FETCH_ASSOC);
        
        echo "<div class='file-list'>";
        echo "<strong>Admin Details:</strong><br>";
        echo "Username: " . htmlspecialchars($admin['username']) . "<br>";
        echo "Name: " . htmlspecialchars($admin['name']) . "<br>";
        echo "Email: " . htmlspecialchars($admin['email']) . "<br>";
        echo "Status: " . htmlspecialchars($admin['status']) . "<br>";
        echo "</div>";
    } else {
        echo "<p class='error'>❌ Admin user not found or inactive</p>";
    }
} else {
    echo "<p class='error'>❌ Cannot check admin - no database connection</p>";
}
echo "</div>";

// Test 5: API Endpoints Test
echo "<div class='test-section'>";
echo "<h2>Test 5: API Endpoints Test</h2>";

$api_endpoints = [
    'mobileweb/api/mobile.php' => 'Mobile API',
    'mobileweb/api/admin.php' => 'Admin API'
];

foreach ($api_endpoints as $file => $description) {
    if (file_exists($file)) {
        // Check if file contains proper headers
        $content = file_get_contents($file);
        if (strpos($content, 'Content-Type: application/json') !== false) {
            echo "<p class='success'>✅ $description - Proper JSON headers</p>";
        } else {
            echo "<p class='warning'>⚠️ $description - Missing JSON headers</p>";
        }
        
        if (strpos($content, 'require_once') !== false) {
            echo "<p class='success'>✅ $description - Database connection included</p>";
        } else {
            echo "<p class='warning'>⚠️ $description - Database connection not found</p>";
        }
    } else {
        echo "<p class='error'>❌ Missing: $description ($file)</p>";
    }
}
echo "</div>";

// Test 6: Access URLs
echo "<div class='test-section'>";
echo "<h2>Test 6: Access URLs</h2>";
echo "<p class='info'>Test these URLs to verify functionality:</p>";
echo "<div class='code'>";
echo "<strong>Main Interface:</strong><br>";
echo "• <a href='mobileweb/index.php' target='_blank'>Mobile Interface (Login)</a><br>";
echo "• <a href='mobileweb/Register_Panel.php' target='_blank'>User Registration</a><br><br>";
echo "<strong>Admin Panel:</strong><br>";
echo "• <a href='mobileweb/admin/login.php' target='_blank'>Admin Login</a><br>";
echo "• <a href='mobileweb/admin/index.php' target='_blank'>Admin Dashboard</a><br><br>";
echo "<strong>Monitor:</strong><br>";
echo "• <a href='mobileweb/monitor/index.php' target='_blank'>Queue Monitor</a><br><br>";
echo "<strong>Setup:</strong><br>";
echo "• <a href='setup_cephra_ct_ws.php' target='_blank'>Setup Verification</a>";
echo "</div>";
echo "</div>";

// Test 7: Environment Information
echo "<div class='test-section'>";
echo "<h2>Test 7: Environment Information</h2>";
echo "<div class='file-list'>";
echo "<strong>Server Info:</strong><br>";
echo "PHP Version: " . phpversion() . "<br>";
echo "Server: " . ($_SERVER['SERVER_SOFTWARE'] ?? 'Unknown') . "<br>";
echo "Document Root: " . ($_SERVER['DOCUMENT_ROOT'] ?? 'Unknown') . "<br>";
echo "Current Directory: " . getcwd() . "<br>";
echo "Current URL: " . (isset($_SERVER['HTTP_HOST']) ? 'http://' . $_SERVER['HTTP_HOST'] . $_SERVER['REQUEST_URI'] : 'Unknown') . "<br>";
echo "</div>";

if ($conn) {
    $env_info = $db->getEnvironmentInfo();
    echo "<div class='file-list'>";
    echo "<strong>Database Info:</strong><br>";
    echo "Environment: " . $env_info['environment'] . "<br>";
    echo "Host: " . $env_info['host'] . "<br>";
    echo "Database: " . $env_info['database'] . "<br>";
    echo "Username: " . $env_info['username'] . "<br>";
    echo "</div>";
}
echo "</div>";

// Summary
echo "<div class='test-section'>";
echo "<h2>Test Summary</h2>";
echo "<p class='info'>All connection tests completed!</p>";
echo "<p>If all tests pass, your Cephra CT.WS installation is ready to use.</p>";
echo "<p>If any tests fail, please check the error messages above and fix the issues.</p>";
echo "</div>";

echo "</div>";
?>
