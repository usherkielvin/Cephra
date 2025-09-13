<?php
// Cephra CT.WS Setup Script
// Run this once to initialize the database and verify the installation

require_once 'config/database.php';

echo "<h1>Cephra CT.WS Setup</h1>";
echo "<style>
    body { font-family: Arial, sans-serif; margin: 40px; background: #f5f5f5; }
    .container { background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
    .success { color: #27ae60; font-weight: bold; }
    .error { color: #e74c3c; font-weight: bold; }
    .info { color: #3498db; font-weight: bold; }
    .step { margin: 20px 0; padding: 15px; background: #f8f9fa; border-left: 4px solid #3498db; }
    .code { background: #2c3e50; color: white; padding: 10px; border-radius: 5px; font-family: monospace; }
</style>";

echo "<div class='container'>";

// Step 1: Database Connection Test
echo "<div class='step'>";
echo "<h2>Step 1: Database Connection Test</h2>";

$db = new Database();
$conn = $db->getConnection();

if ($conn) {
    echo "<p class='success'>✅ Database connection successful!</p>";
    echo "<p>Connected to: <strong>cephradb</strong> database</p>";
} else {
    echo "<p class='error'>❌ Database connection failed!</p>";
    echo "<p>Please check your XAMPP MySQL service and ensure the database exists.</p>";
    echo "<div class='code'>";
    echo "1. Start XAMPP Control Panel<br>";
    echo "2. Start MySQL service<br>";
    echo "3. Run: CREATE DATABASE cephradb;<br>";
    echo "4. Import the setup_database.sql file";
    echo "</div>";
    exit();
}
echo "</div>";

// Step 2: Check Required Tables
echo "<div class='step'>";
echo "<h2>Step 2: Database Tables Check</h2>";

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

if (!empty($missing_tables)) {
    echo "<p class='error'>Missing tables detected. Please run the database setup:</p>";
    echo "<div class='code'>";
    echo "1. Open phpMyAdmin<br>";
    echo "2. Select 'cephradb' database<br>";
    echo "3. Import setup_database.sql file<br>";
    echo "4. Refresh this page";
    echo "</div>";
} else {
    echo "<p class='success'>✅ All required tables exist!</p>";
}
echo "</div>";

// Step 3: Check Default Data
echo "<div class='step'>";
echo "<h2>Step 3: Default Data Check</h2>";

// Check for admin user
$stmt = $conn->prepare("SELECT COUNT(*) as count FROM staff_records WHERE username = 'admin'");
$stmt->execute();
$admin_count = $stmt->fetch(PDO::FETCH_ASSOC)['count'];

if ($admin_count > 0) {
    echo "<p class='success'>✅ Admin user exists</p>";
    
    // Show admin details
    $stmt = $conn->prepare("SELECT username, name, email, status FROM staff_records WHERE username = 'admin'");
    $stmt->execute();
    $admin = $stmt->fetch(PDO::FETCH_ASSOC);
    
    echo "<p><strong>Admin Details:</strong></p>";
    echo "<ul>";
    echo "<li>Username: " . htmlspecialchars($admin['username']) . "</li>";
    echo "<li>Name: " . htmlspecialchars($admin['name']) . "</li>";
    echo "<li>Email: " . htmlspecialchars($admin['email']) . "</li>";
    echo "<li>Status: " . htmlspecialchars($admin['status']) . "</li>";
    echo "</ul>";
} else {
    echo "<p class='error'>❌ Admin user not found</p>";
    echo "<p>Creating default admin user...</p>";
    
    $stmt = $conn->prepare("INSERT INTO staff_records (name, username, email, status, password) VALUES (?, ?, ?, ?, ?)");
    $result = $stmt->execute(['Admin User', 'admin', 'admin@cephra.com', 'Active', 'admin123']);
    
    if ($result) {
        echo "<p class='success'>✅ Default admin user created!</p>";
    } else {
        echo "<p class='error'>❌ Failed to create admin user</p>";
    }
}

// Check for charging bays
$stmt = $conn->query("SELECT COUNT(*) as count FROM charging_bays");
$bay_count = $stmt->fetch(PDO::FETCH_ASSOC)['count'];

if ($bay_count > 0) {
    echo "<p class='success'>✅ Charging bays configured ($bay_count bays)</p>";
} else {
    echo "<p class='info'>ℹ️ No charging bays found. You may need to add them manually.</p>";
}
echo "</div>";

// Step 4: Access URLs
echo "<div class='step'>";
echo "<h2>Step 4: Access URLs</h2>";
echo "<p class='info'>Your Cephra CT.WS installation is ready!</p>";
echo "<div class='code'>";
echo "<strong>Main Interface:</strong><br>";
echo "• <a href='mobileweb/index.php' target='_blank'>Mobile Interface</a><br>";
echo "• <a href='mobileweb/login.php' target='_blank'>User Login</a><br>";
echo "• <a href='mobileweb/Register_Panel.php' target='_blank'>User Registration</a><br><br>";
echo "<strong>Admin Panel:</strong><br>";
echo "• <a href='mobileweb/admin/login.php' target='_blank'>Admin Login</a><br>";
echo "• <a href='mobileweb/admin/index.php' target='_blank'>Admin Dashboard</a><br><br>";
echo "<strong>Monitor:</strong><br>";
echo "• <a href='mobileweb/monitor/index.php' target='_blank'>Queue Monitor</a>";
echo "</div>";
echo "</div>";

// Step 5: Environment Info
echo "<div class='step'>";
echo "<h2>Step 5: Environment Information</h2>";
$env_info = $db->getEnvironmentInfo();
echo "<p><strong>Environment:</strong> " . $env_info['environment'] . "</p>";
echo "<p><strong>Host:</strong> " . $env_info['host'] . "</p>";
echo "<p><strong>Database:</strong> " . $env_info['database'] . "</p>";
echo "<p><strong>Username:</strong> " . $env_info['username'] . "</p>";
echo "<p><strong>Note:</strong> " . $env_info['note'] . "</p>";
echo "</div>";

echo "<div class='step'>";
echo "<h2>Setup Complete!</h2>";
echo "<p class='success'>🎉 Cephra CT.WS is ready to use!</p>";
echo "<p>You can now access all the interfaces using the URLs above.</p>";
echo "</div>";

echo "</div>";
?>
