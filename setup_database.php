<?php
// Complete Database Setup Script
require_once "Appweb/User/config/database.php";

echo "<h1>Complete Database Setup</h1>";

try {
    $db = (new Database())->getConnection();
    if (!$db) {
        echo "<h2 style='color: red;'>❌ Database connection failed!</h2>";
        echo "<p>Please make sure MySQL is running and check your database configuration.</p>";
        exit();
    }

    echo "<h2 style='color: green;'>✅ Database connected successfully!</h2>";

    // Create database if it doesn't exist
    $db_name = 'cephradb';
    echo "<h3>Setting up database: $db_name</h3>";

    try {
        $db->exec("CREATE DATABASE IF NOT EXISTS $db_name");
        echo "<p style='color: green;'>✅ Database '$db_name' created or already exists</p>";
    } catch (Exception $e) {
        echo "<p style='color: orange;'>⚠️ Could not create database (might already exist): " . $e->getMessage() . "</p>";
    }

    // Select the database
    $db->exec("USE $db_name");
    echo "<p style='color: green;'>✅ Using database: $db_name</p>";

    // Create users table
    echo "<h3>Creating users table...</h3>";
    $sql = "CREATE TABLE IF NOT EXISTS users (
        id INT AUTO_INCREMENT PRIMARY KEY,
        username VARCHAR(50) NOT NULL UNIQUE,
        email VARCHAR(100) NOT NULL UNIQUE,
        password VARCHAR(255) NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    )";

    $db->exec($sql);
    echo "<p style='color: green;'>✅ Users table created successfully</p>";

    // Create otp_codes table
    echo "<h3>Creating otp_codes table...</h3>";
    $sql = "CREATE TABLE IF NOT EXISTS otp_codes (
        id INT AUTO_INCREMENT PRIMARY KEY,
        username VARCHAR(50) NOT NULL,
        otp_code VARCHAR(6) NOT NULL,
        expires_at DATETIME NOT NULL,
        used TINYINT(1) DEFAULT 0,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        UNIQUE KEY unique_username_unused (username, used)
    )";

    $db->exec($sql);
    echo "<p style='color: green;'>✅ OTP codes table created successfully</p>";

    // Create password_reset_tokens table
    echo "<h3>Creating password_reset_tokens table...</h3>";
    $sql = "CREATE TABLE IF NOT EXISTS password_reset_tokens (
        id INT AUTO_INCREMENT PRIMARY KEY,
        username VARCHAR(50) NOT NULL,
        token VARCHAR(64) NOT NULL,
        expires_at DATETIME NOT NULL,
        used TINYINT(1) DEFAULT 0,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        UNIQUE KEY unique_token (token)
    )";

    $db->exec($sql);
    echo "<p style='color: green;'>✅ Password reset tokens table created successfully</p>";

    // Insert a test user for testing
    echo "<h3>Creating test user...</h3>";
    $test_username = 'testuser';
    $test_email = 'test@example.com';
    $test_password = password_hash('password123', PASSWORD_DEFAULT);

    try {
        $stmt = $db->prepare("INSERT INTO users (username, email, password) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE email = VALUES(email)");
        $stmt->execute([$test_username, $test_email, $test_password]);
        echo "<p style='color: green;'>✅ Test user created/updated: $test_username / $test_email</p>";
    } catch (Exception $e) {
        echo "<p style='color: orange;'>⚠️ Could not create test user: " . $e->getMessage() . "</p>";
    }

    // Test OTP code storage
    echo "<h3>Testing OTP code storage...</h3>";
    $reset_code = str_pad(rand(100000, 999999), 6, '0', STR_PAD_LEFT);
    $expires_at = date('Y-m-d H:i:s', strtotime('+30 minutes'));

    try {
        // First, mark any existing unused codes as used
        $stmt = $db->prepare("UPDATE otp_codes SET used = 1 WHERE username = ? AND used = 0");
        $stmt->execute([$test_username]);

        // Insert new code
        $stmt = $db->prepare("INSERT INTO otp_codes (username, otp_code, expires_at, used, created_at) VALUES (?, ?, ?, 0, NOW())");
        $result = $stmt->execute([$test_username, $reset_code, $expires_at]);

        if ($result) {
            echo "<p style='color: green;'>✅ Test OTP code stored successfully!</p>";
            echo "<p>Username: $test_username</p>";
            echo "<p>Code: $reset_code</p>";
            echo "<p>Expires: $expires_at</p>";
        } else {
            echo "<p style='color: red;'>❌ Failed to store test OTP code!</p>";
        }
    } catch (Exception $e) {
        echo "<p style='color: red;'>❌ Error storing OTP code: " . $e->getMessage() . "</p>";
    }

    // Show current data
    echo "<h3>Current Database Contents:</h3>";

    // Users
    $stmt = $db->query("SELECT id, username, email, created_at FROM users");
    $users = $stmt->fetchAll(PDO::FETCH_ASSOC);
    echo "<h4>Users:</h4>";
    if (count($users) > 0) {
        echo "<table border='1' cellpadding='5'>";
        echo "<tr><th>ID</th><th>Username</th><th>Email</th><th>Created</th></tr>";
        foreach ($users as $user) {
            echo "<tr><td>{$user['id']}</td><td>{$user['username']}</td><td>{$user['email']}</td><td>{$user['created_at']}</td></tr>";
        }
        echo "</table>";
    } else {
        echo "<p>No users found.</p>";
    }

    // OTP Codes
    $stmt = $db->query("SELECT id, username, otp_code, expires_at, used, created_at FROM otp_codes ORDER BY created_at DESC");
    $codes = $stmt->fetchAll(PDO::FETCH_ASSOC);
    echo "<h4>OTP Codes:</h4>";
    if (count($codes) > 0) {
        echo "<table border='1' cellpadding='5'>";
        echo "<tr><th>ID</th><th>Username</th><th>Code</th><th>Expires</th><th>Used</th><th>Created</th></tr>";
        foreach ($codes as $code) {
            echo "<tr><td>{$code['id']}</td><td>{$code['username']}</td><td>{$code['otp_code']}</td><td>{$code['expires_at']}</td><td>{$code['used']}</td><td>{$code['created_at']}</td></tr>";
        }
        echo "</table>";
    } else {
        echo "<p>No OTP codes found.</p>";
    }

    echo "<h2 style='color: green;'>✅ Database setup completed successfully!</h2>";
    echo "<p>You can now test the OTP functionality using the test user:</p>";
    echo "<ul>";
    echo "<li><strong>Username:</strong> $test_username</li>";
    echo "<li><strong>Email:</strong> $test_email</li>";
    echo "<li><strong>Password:</strong> password123</li>";
    echo "</ul>";

    echo "<h3>API Endpoints Ready:</h3>";
    echo "<ul>";
    echo "<li><strong>request-reset:</strong> Send initial password reset code</li>";
    echo "<li><strong>resend-code:</strong> Resend verification code (2-minute cooldown)</li>";
    echo "<li><strong>verify-code:</strong> Verify the 6-digit code and generate reset token</li>";
    echo "</ul>";

    echo "<p><a href='test_database_connection.php'>🔍 View Database Connection Test</a></p>";

} catch (Exception $e) {
    echo "<h2 style='color: red;'>❌ Error setting up database:</h2>";
    echo "<p>" . $e->getMessage() . "</p>";
    echo "<h3>Troubleshooting:</h3>";
    echo "<ul>";
    echo "<li>Make sure MySQL is running</li>";
    echo "<li>Check your database credentials in Appweb/User/config/database.php</li>";
    echo "<li>Make sure you have permission to create databases and tables</li>";
    echo "</ul>";
}
?>
