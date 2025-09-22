<?php
// Database Setup Script for Password Reset Functionality
// Run this script to create the required tables

require_once "Appweb/User/config/database.php";

try {
    $db = (new Database())->getConnection();
    if (!$db) {
        die("Database connection failed");
    }

    echo "<h1>Setting up database tables...</h1>";

    // Create otp_codes table
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
    echo "<p>✅ Created/verified otp_codes table</p>";

    // Create password_reset_tokens table
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
    echo "<p>✅ Created/verified password_reset_tokens table</p>";

    // Check if users table exists (for email validation)
    $sql = "SHOW TABLES LIKE 'users'";
    $stmt = $db->query($sql);
    if ($stmt->rowCount() > 0) {
        echo "<p>✅ Users table exists</p>";
    } else {
        echo "<p>⚠️ Users table does not exist - email validation may not work</p>";
    }

    echo "<h2>Database setup completed successfully!</h2>";
    echo "<p>The following tables are now ready:</p>";
    echo "<ul>";
    echo "<li><strong>otp_codes:</strong> Stores verification codes with expiration</li>";
    echo "<li><strong>password_reset_tokens:</strong> Stores temporary tokens for password reset</li>";
    echo "</ul>";

    echo "<h3>API Endpoints Ready:</h3>";
    echo "<ul>";
    echo "<li><strong>request-reset:</strong> Send initial password reset code</li>";
    echo "<li><strong>resend-code:</strong> Resend verification code (2-minute cooldown)</li>";
    echo "<li><strong>verify-code:</strong> Verify the 6-digit code and generate reset token</li>";
    echo "</ul>";

} catch (Exception $e) {
    echo "<h2 style='color: red;'>Error setting up database:</h2>";
    echo "<p>" . $e->getMessage() . "</p>";
}
?>
