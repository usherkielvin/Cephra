<?php
// Database Setup Script for Cephra Admin
session_start();

// Enable error reporting
error_reporting(E_ALL);
ini_set('display_errors', 1);

echo "<h1>🗄️ Database Setup for Cephra Admin</h1>";
echo "<div style='font-family: Arial, sans-serif; max-width: 1000px; margin: 0 auto; padding: 20px;'>";

// Database configuration
$host = 'localhost';
$dbname = 'cephradb';
$username = 'root';
$password = '';

try {
    // Connect to MySQL server (without database)
    $pdo = new PDO("mysql:host=$host", $username, $password);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    
    echo "<div style='color: green; padding: 10px; background: #d4edda; border-radius: 5px; margin: 10px 0;'>";
    echo "✅ Connected to MySQL server successfully<br>";
    echo "</div>";
    
    // Check if database exists
    $stmt = $pdo->query("SHOW DATABASES LIKE '$dbname'");
    if ($stmt->rowCount() > 0) {
        echo "<div style='color: green; padding: 10px; background: #d4edda; border-radius: 5px; margin: 10px 0;'>";
        echo "✅ Database '$dbname' already exists<br>";
        echo "</div>";
    } else {
        // Create database
        $pdo->exec("CREATE DATABASE $dbname");
        echo "<div style='color: green; padding: 10px; background: #d4edda; border-radius: 5px; margin: 10px 0;'>";
        echo "✅ Database '$dbname' created successfully<br>";
        echo "</div>";
    }
    
    // Connect to the specific database
    $pdo = new PDO("mysql:host=$host;dbname=$dbname", $username, $password);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    
    echo "<div style='color: green; padding: 10px; background: #d4edda; border-radius: 5px; margin: 10px 0;'>";
    echo "✅ Connected to database '$dbname' successfully<br>";
    echo "</div>";
    
    // Create tables if they don't exist
    $tables = [
        'users' => "
            CREATE TABLE IF NOT EXISTS users (
                id INT AUTO_INCREMENT PRIMARY KEY,
                username VARCHAR(50) UNIQUE NOT NULL,
                firstname VARCHAR(50) NOT NULL,
                lastname VARCHAR(50) NOT NULL,
                email VARCHAR(100) UNIQUE NOT NULL,
                password VARCHAR(255) NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        ",
        'queue_tickets' => "
            CREATE TABLE IF NOT EXISTS queue_tickets (
                id INT AUTO_INCREMENT PRIMARY KEY,
                ticket_id VARCHAR(20) UNIQUE NOT NULL,
                username VARCHAR(50) NOT NULL,
                service_type VARCHAR(50) NOT NULL,
                status VARCHAR(20) DEFAULT 'Waiting',
                payment_status VARCHAR(20) DEFAULT 'Pending',
                initial_battery_level INT DEFAULT 0,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        ",
        'charging_bays' => "
            CREATE TABLE IF NOT EXISTS charging_bays (
                id INT AUTO_INCREMENT PRIMARY KEY,
                bay_number VARCHAR(10) UNIQUE NOT NULL,
                bay_type VARCHAR(20) NOT NULL,
                status VARCHAR(20) DEFAULT 'Available',
                current_ticket_id VARCHAR(20),
                current_username VARCHAR(50),
                start_time TIMESTAMP NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        "
    ];
    
    foreach ($tables as $tableName => $sql) {
        try {
            $pdo->exec($sql);
            echo "<div style='color: green; margin: 5px 0;'>✅ Table '$tableName' created/verified</div>";
        } catch (PDOException $e) {
            echo "<div style='color: red; margin: 5px 0;'>❌ Error creating table '$tableName': " . $e->getMessage() . "</div>";
        }
    }
    
    // Insert sample data if tables are empty
    echo "<h2>📊 Sample Data Setup</h2>";
    
    // Check if users table is empty
    $stmt = $pdo->query("SELECT COUNT(*) as count FROM users");
    $userCount = $stmt->fetch(PDO::FETCH_ASSOC)['count'];
    
    if ($userCount == 0) {
        $sampleUsers = [
            ['admin', 'Admin', 'User', 'admin@cephra.com', 'admin123'],
            ['dizon', 'John', 'Dizon', 'dizon@cephra.com', 'dizon123'],
            ['testuser', 'Test', 'User', 'test@cephra.com', 'test123']
        ];
        
        $stmt = $pdo->prepare("INSERT INTO users (username, firstname, lastname, email, password) VALUES (?, ?, ?, ?, ?)");
        foreach ($sampleUsers as $user) {
            $stmt->execute($user);
        }
        echo "<div style='color: green; margin: 5px 0;'>✅ Added " . count($sampleUsers) . " sample users</div>";
    } else {
        echo "<div style='color: blue; margin: 5px 0;'>ℹ️ Users table already has $userCount records</div>";
    }
    
    // Check if queue_tickets table is empty
    $stmt = $pdo->query("SELECT COUNT(*) as count FROM queue_tickets");
    $ticketCount = $stmt->fetch(PDO::FETCH_ASSOC)['count'];
    
    if ($ticketCount == 0) {
        $sampleTickets = [
            ['FCH001', 'dizon', 'Fast Charging', 'Pending', 'Pending', 25],
            ['NOR002', 'testuser', 'Normal Charging', 'Waiting', 'Pending', 45],
            ['FCH003', 'admin', 'Fast Charging', 'Processing', 'Paid', 15]
        ];
        
        $stmt = $pdo->prepare("INSERT INTO queue_tickets (ticket_id, username, service_type, status, payment_status, initial_battery_level) VALUES (?, ?, ?, ?, ?, ?)");
        foreach ($sampleTickets as $ticket) {
            $stmt->execute($ticket);
        }
        echo "<div style='color: green; margin: 5px 0;'>✅ Added " . count($sampleTickets) . " sample tickets</div>";
    } else {
        echo "<div style='color: blue; margin: 5px 0;'>ℹ️ Queue tickets table already has $ticketCount records</div>";
    }
    
    // Check if charging_bays table is empty
    $stmt = $pdo->query("SELECT COUNT(*) as count FROM charging_bays");
    $bayCount = $stmt->fetch(PDO::FETCH_ASSOC)['count'];
    
    if ($bayCount == 0) {
        $sampleBays = [
            ['BAY001', 'Fast Charging', 'Available', null, null, null],
            ['BAY002', 'Normal Charging', 'Occupied', 'FCH003', 'admin', date('Y-m-d H:i:s')],
            ['BAY003', 'Fast Charging', 'Maintenance', null, null, null],
            ['BAY004', 'Normal Charging', 'Available', null, null, null]
        ];
        
        $stmt = $pdo->prepare("INSERT INTO charging_bays (bay_number, bay_type, status, current_ticket_id, current_username, start_time) VALUES (?, ?, ?, ?, ?, ?)");
        foreach ($sampleBays as $bay) {
            $stmt->execute($bay);
        }
        echo "<div style='color: green; margin: 5px 0;'>✅ Added " . count($sampleBays) . " sample charging bays</div>";
    } else {
        echo "<div style='color: blue; margin: 5px 0;'>ℹ️ Charging bays table already has $bayCount records</div>";
    }
    
    // Test the clean API
    echo "<h2>🧪 Testing Clean API</h2>";
    
    $testUrl = "api/admin-clean.php?action=dashboard";
    $context = stream_context_create([
        'http' => [
            'method' => 'GET',
            'timeout' => 10
        ]
    ]);
    
    $response = @file_get_contents($testUrl, false, $context);
    
    if ($response === false) {
        echo "<div style='color: red; padding: 10px; background: #f8d7da; border-radius: 5px; margin: 10px 0;'>";
        echo "❌ Clean API test failed<br>";
        echo "Error: " . error_get_last()['message'] . "<br>";
        echo "</div>";
    } else {
        $data = json_decode($response, true);
        if ($data && isset($data['success']) && $data['success']) {
            echo "<div style='color: green; padding: 10px; background: #d4edda; border-radius: 5px; margin: 10px 0;'>";
            echo "✅ Clean API test successful!<br>";
            echo "Total Users: " . $data['stats']['total_users'] . "<br>";
            echo "Queue Count: " . $data['stats']['queue_count'] . "<br>";
            echo "Active Bays: " . $data['stats']['active_bays'] . "<br>";
            echo "Recent Activities: " . count($data['recent_activity']) . "<br>";
            echo "</div>";
        } else {
            echo "<div style='color: orange; padding: 10px; background: #fff3cd; border-radius: 5px; margin: 10px 0;'>";
            echo "⚠️ Clean API responded but with errors<br>";
            echo "Response: " . $response . "<br>";
            echo "</div>";
        }
    }
    
    echo "<h2>🎉 Setup Complete!</h2>";
    echo "<div style='background: #e7f3ff; padding: 20px; border-radius: 10px; margin: 20px 0;'>";
    echo "<h3>✅ What's Ready:</h3>";
    echo "<ul>";
    echo "<li><strong>Database:</strong> '$dbname' with all required tables</li>";
    echo "<li><strong>Sample Data:</strong> Users, tickets, and charging bays</li>";
    echo "<li><strong>Clean API:</strong> admin-clean.php (no authentication required)</li>";
    echo "<li><strong>Test Page:</strong> test-clean.html for testing</li>";
    echo "</ul>";
    
    echo "<h3>🚀 Next Steps:</h3>";
    echo "<ol>";
    echo "<li>Visit <code>mobileweb/admin/test-clean.html</code> to test the admin interface</li>";
    echo "<li>Check that all panels load correctly</li>";
    echo "<li>Verify that navbar buttons switch between panels</li>";
    echo "<li>Test the recent activity loading</li>";
    echo "</ol>";
    echo "</div>";
    
} catch (PDOException $e) {
    echo "<div style='color: red; padding: 10px; background: #f8d7da; border-radius: 5px; margin: 10px 0;'>";
    echo "❌ Database error: " . $e->getMessage() . "<br>";
    echo "<br><strong>Solutions:</strong><br>";
    echo "1. Start XAMPP Control Panel<br>";
    echo "2. Start MySQL service<br>";
    echo "3. Check MySQL port (default: 3306)<br>";
    echo "4. Verify username 'root' has access<br>";
    echo "</div>";
}

echo "</div>";
?>
