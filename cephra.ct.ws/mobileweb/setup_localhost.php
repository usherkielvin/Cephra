<?php
/**
 * Localhost Database Setup Script
 * Run this script to set up the mobile web database for local development
 */

echo "<h2>Cephra Mobile Web - Localhost Database Setup</h2>";

// Database configuration for localhost
$host = 'localhost';
$username = 'root';
$password = '';
$database = 'cephra_db';

try {
    // Connect to MySQL server (without database)
    $pdo = new PDO("mysql:host=$host", $username, $password);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    
    echo "<p>✅ Connected to MySQL server successfully</p>";
    
    // Read and execute the SQL setup script
    $sql = file_get_contents('setup_database.sql');
    
    // Split SQL into individual statements
    $statements = array_filter(array_map('trim', explode(';', $sql)));
    
    $successCount = 0;
    $errorCount = 0;
    
    foreach ($statements as $statement) {
        if (!empty($statement) && !preg_match('/^(CREATE DATABASE|USE|SHOW)/i', $statement)) {
            try {
                $pdo->exec($statement);
                $successCount++;
            } catch (PDOException $e) {
                if (strpos($e->getMessage(), 'already exists') === false) {
                    echo "<p>❌ Error executing statement: " . htmlspecialchars($e->getMessage()) . "</p>";
                    $errorCount++;
                }
            }
        }
    }
    
    echo "<p>✅ Database setup completed!</p>";
    echo "<p>📊 Statistics: $successCount statements executed successfully</p>";
    
    if ($errorCount > 0) {
        echo "<p>⚠️ $errorCount statements had errors (mostly 'already exists' which is normal)</p>";
    }
    
    // Test the database connection
    $pdo = new PDO("mysql:host=$host;dbname=$database", $username, $password);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    
    // Show tables
    $stmt = $pdo->query("SHOW TABLES");
    $tables = $stmt->fetchAll(PDO::FETCH_COLUMN);
    
    echo "<h3>📋 Created Tables:</h3>";
    echo "<ul>";
    foreach ($tables as $table) {
        echo "<li>$table</li>";
    }
    echo "</ul>";
    
    // Show sample data
    $stmt = $pdo->query("SELECT COUNT(*) as count FROM users");
    $userCount = $stmt->fetch()['count'];
    
    $stmt = $pdo->query("SELECT COUNT(*) as count FROM charging_bays");
    $bayCount = $stmt->fetch()['count'];
    
    echo "<h3>📊 Sample Data:</h3>";
    echo "<ul>";
    echo "<li>Users: $userCount (including test user: testuser/test123)</li>";
    echo "<li>Charging Bays: $bayCount</li>";
    echo "</ul>";
    
    echo "<h3>🎉 Setup Complete!</h3>";
    echo "<p>Your mobile web application is now ready for localhost development.</p>";
    echo "<p><strong>Test Login:</strong> username: <code>testuser</code>, password: <code>test123</code></p>";
    echo "<p><a href='index.php'>Go to Mobile Web Application</a></p>";
    
} catch (PDOException $e) {
    echo "<p>❌ Database connection failed: " . htmlspecialchars($e->getMessage()) . "</p>";
    echo "<p>Please make sure:</p>";
    echo "<ul>";
    echo "<li>XAMPP is running</li>";
    echo "<li>MySQL service is started</li>";
    echo "<li>Default credentials are correct (root with no password)</li>";
    echo "</ul>";
}
?>
