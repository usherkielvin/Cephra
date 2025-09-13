<?php
// Test Fixes for Admin
session_start();

// Enable error reporting
error_reporting(E_ALL);
ini_set('display_errors', 1);

echo "<h1>🔧 Admin Fixes Test</h1>";
echo "<div style='font-family: Arial, sans-serif; max-width: 1000px; margin: 0 auto; padding: 20px;'>";

// Test 1: Database Connection
echo "<h2>📊 Database Connection Test</h2>";

try {
    require_once 'config/database.php';
    $db = new Database();
    $conn = $db->getConnection();
    
    if ($conn) {
        echo "<div style='color: green; padding: 10px; background: #d4edda; border-radius: 5px; margin: 10px 0;'>";
        echo "✅ Database connection successful!<br>";
        echo "</div>";
        
        // Test if staff_records table exists (needed for admin login)
        try {
            $stmt = $conn->query("SELECT COUNT(*) as count FROM staff_records");
            $count = $stmt->fetch(PDO::FETCH_ASSOC)['count'];
            echo "<div style='color: green; margin: 5px 0;'>✅ Table 'staff_records': $count records</div>";
        } catch (Exception $e) {
            echo "<div style='color: orange; margin: 5px 0;'>⚠️ Table 'staff_records': " . $e->getMessage() . "</div>";
            echo "<div style='color: blue; margin: 5px 0;'>ℹ️ Creating staff_records table...</div>";
            
            // Create staff_records table
            $createTable = "
                CREATE TABLE IF NOT EXISTS staff_records (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    firstname VARCHAR(50) NOT NULL,
                    lastname VARCHAR(50) NOT NULL,
                    email VARCHAR(100) UNIQUE NOT NULL,
                    status VARCHAR(20) DEFAULT 'Active',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            ";
            
            $conn->exec($createTable);
            
            // Insert default admin user
            $stmt = $conn->prepare("INSERT INTO staff_records (username, password, firstname, lastname, email, status) VALUES (?, ?, ?, ?, ?, ?)");
            $stmt->execute(['admin', 'admin123', 'Admin', 'User', 'admin@cephra.com', 'Active']);
            
            echo "<div style='color: green; margin: 5px 0;'>✅ Created staff_records table with default admin user</div>";
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

// Test 2: Logo File Check
echo "<h2>🖼️ Logo File Check</h2>";

$logoPath = 'images/logo.png';
if (file_exists($logoPath)) {
    echo "<div style='color: green; padding: 10px; background: #d4edda; border-radius: 5px; margin: 10px 0;'>";
    echo "✅ Logo file exists: $logoPath<br>";
    echo "File size: " . filesize($logoPath) . " bytes<br>";
    echo "</div>";
} else {
    echo "<div style='color: red; padding: 10px; background: #f8d7da; border-radius: 5px; margin: 10px 0;'>";
    echo "❌ Logo file not found: $logoPath<br>";
    echo "</div>";
}

// Test 3: Admin Files Check
echo "<h2>📁 Admin Files Check</h2>";

$adminFiles = [
    'login.php' => 'Admin Login Page',
    'index.php' => 'Admin Dashboard',
    'config/database.php' => 'Database Configuration',
    'css/admin.css' => 'Admin Stylesheet'
];

foreach ($adminFiles as $file => $description) {
    if (file_exists($file)) {
        echo "<div style='color: green; margin: 5px 0;'>✅ {$description}: {$file} - Exists</div>";
    } else {
        echo "<div style='color: red; margin: 5px 0;'>❌ {$description}: {$file} - Not found</div>";
    }
}

// Test 4: Direct Links
echo "<h2>🔗 Direct Access Links</h2>";

echo "<h3>Admin Interface:</h3>";
echo "<ul>";
echo "<li><a href='login.php' target='_blank'>Admin Login</a></li>";
echo "<li><a href='index.php' target='_blank'>Admin Dashboard</a></li>";
echo "</ul>";

echo "<h3>Test Tools:</h3>";
echo "<ul>";
echo "<li><a href='../database-connection-checker.php' target='_blank'>Database Connection Checker</a></li>";
echo "<li><a href='../api-connection-checker.php' target='_blank'>API Connection Checker</a></li>";
echo "<li><a href='../quick-test.php' target='_blank'>Quick Test</a></li>";
echo "</ul>";

// Summary
echo "<h2>📋 Summary</h2>";
echo "<div style='background: #e7f3ff; padding: 20px; border-radius: 10px; margin: 20px 0;'>";
echo "<h3>✅ What's Fixed:</h3>";
echo "<ul>";
echo "<li><strong>Database Path:</strong> Fixed require_once paths in all Admin files</li>";
echo "<li><strong>Logo Path:</strong> Fixed logo image paths in login.php and index.php</li>";
echo "<li><strong>Images Folder:</strong> Copied images from User to Admin folder</li>";
echo "<li><strong>Staff Records:</strong> Created staff_records table for admin authentication</li>";
echo "</ul>";

echo "<h3>🚀 Next Steps:</h3>";
echo "<ol>";
echo "<li>Test admin login: <code>Appweb/Admin/login.php</code></li>";
echo "<li>Use default credentials: username='admin', password='admin123'</li>";
echo "<li>Check that logo appears on login page</li>";
echo "<li>Test admin dashboard functionality</li>";
echo "</ol>";
echo "</div>";

echo "</div>";
?>
