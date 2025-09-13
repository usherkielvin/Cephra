<?php
// Test database connection for mobileweb
echo "<h2>Mobileweb Database Connection Test</h2>";

require_once 'config/database.php';

try {
    $db = new Database();
    $conn = $db->getConnection();
    
    if ($conn) {
        echo "<p style='color: green;'>✅ Database connection successful!</p>";
        
        // Get environment info
        $envInfo = $db->getEnvironmentInfo();
        echo "<h3>Connection Details:</h3>";
        echo "<ul>";
        echo "<li><strong>Environment:</strong> " . htmlspecialchars($envInfo['environment']) . "</li>";
        echo "<li><strong>Host:</strong> " . htmlspecialchars($envInfo['host']) . "</li>";
        echo "<li><strong>Database:</strong> " . htmlspecialchars($envInfo['database']) . "</li>";
        echo "<li><strong>Username:</strong> " . htmlspecialchars($envInfo['username']) . "</li>";
        echo "<li><strong>Note:</strong> " . htmlspecialchars($envInfo['note']) . "</li>";
        echo "</ul>";
        
        // Test query
        $stmt = $conn->query("SHOW TABLES");
        $tables = $stmt->fetchAll(PDO::FETCH_COLUMN);
        
        echo "<h3>Available Tables:</h3>";
        if (count($tables) > 0) {
            echo "<ul>";
            foreach ($tables as $table) {
                echo "<li>" . htmlspecialchars($table) . "</li>";
            }
            echo "</ul>";
        } else {
            echo "<p style='color: orange;'>⚠️ No tables found. Run setup_localhost.php to create tables.</p>";
        }
        
        // Test users table if it exists
        if (in_array('users', $tables)) {
            $stmt = $conn->query("SELECT COUNT(*) as count FROM users");
            $userCount = $stmt->fetch()['count'];
            echo "<p><strong>Users in database:</strong> $userCount</p>";
        }
        
    } else {
        echo "<p style='color: red;'>❌ Database connection failed!</p>";
        echo "<p>Please check:</p>";
        echo "<ul>";
        echo "<li>XAMPP is running</li>";
        echo "<li>MySQL service is started</li>";
        echo "<li>Database 'cephradb' exists</li>";
        echo "<li>Username 'root' has access</li>";
        echo "</ul>";
    }
    
} catch (Exception $e) {
    echo "<p style='color: red;'>❌ Error: " . htmlspecialchars($e->getMessage()) . "</p>";
}

echo "<hr>";
echo "<p><a href='setup_localhost.php'>Run Database Setup</a> | <a href='index.php'>Go to Mobile App</a></p>";
?>
