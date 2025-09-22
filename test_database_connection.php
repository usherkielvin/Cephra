<?php
// Test Database Connection and Table Structure
require_once "Appweb/User/config/database.php";

echo "<h1>Database Connection Test</h1>";

try {
    $db = (new Database())->getConnection();
    if (!$db) {
        echo "<h2 style='color: red;'>❌ Database connection failed!</h2>";
        echo "<p>Check your database configuration and make sure MySQL is running.</p>";
        exit();
    }

    echo "<h2 style='color: green;'>✅ Database connected successfully!</h2>";

    // Test if database exists
    $db_name = 'cephradb';
    $stmt = $db->query("SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '$db_name'");
    if ($stmt->rowCount() == 0) {
        echo "<h2 style='color: red;'>❌ Database '$db_name' does not exist!</h2>";
        echo "<p>You need to create the database first. Run this SQL command in MySQL:</p>";
        echo "<code>CREATE DATABASE $db_name;</code>";
        exit();
    }

    echo "<h3 style='color: green;'>✅ Database '$db_name' exists</h3>";

    // Check if tables exist
    $tables = ['otp_codes', 'password_reset_tokens', 'users'];
    foreach ($tables as $table) {
        $stmt = $db->query("SHOW TABLES LIKE '$table'");
        if ($stmt->rowCount() > 0) {
            echo "<p style='color: green;'>✅ Table '$table' exists</p>";
        } else {
            echo "<p style='color: red;'>❌ Table '$table' does not exist</p>";
        }
    }

    // Show table structure for otp_codes
    echo "<h3>OTP Codes Table Structure:</h3>";
    $stmt = $db->query("DESCRIBE otp_codes");
    $columns = $stmt->fetchAll(PDO::FETCH_ASSOC);

    if (count($columns) > 0) {
        echo "<table border='1' cellpadding='5'>";
        echo "<tr><th>Column</th><th>Type</th><th>Null</th><th>Key</th><th>Default</th></tr>";
        foreach ($columns as $column) {
            echo "<tr>";
            echo "<td>{$column['Field']}</td>";
            echo "<td>{$column['Type']}</td>";
            echo "<td>{$column['Null']}</td>";
            echo "<td>{$column['Key']}</td>";
            echo "<td>{$column['Default']}</td>";
            echo "</tr>";
        }
        echo "</table>";
    } else {
        echo "<p style='color: red;'>❌ otp_codes table structure not found</p>";
    }

    // Test inserting a sample OTP code
    echo "<h3>Testing OTP Code Storage:</h3>";
    $test_username = 'test_user_' . time();
    $test_code = '123456';
    $expires_at = date('Y-m-d H:i:s', strtotime('+30 minutes'));

    try {
        // First mark any existing unused codes as used
        $stmt = $db->prepare("UPDATE otp_codes SET used = 1 WHERE username = ? AND used = 0");
        $stmt->execute([$test_username]);

        // Insert new code
        $stmt = $db->prepare("INSERT INTO otp_codes (username, otp_code, expires_at, used, created_at) VALUES (?, ?, ?, 0, NOW())");
        $result = $stmt->execute([$test_username, $test_code, $expires_at]);

        if ($result) {
            echo "<p style='color: green;'>✅ Test OTP code inserted successfully!</p>";
            echo "<p>Username: $test_username</p>";
            echo "<p>Code: $test_code</p>";
            echo "<p>Expires: $expires_at</p>";

            // Verify it was stored
            $stmt = $db->prepare("SELECT * FROM otp_codes WHERE username = ? AND otp_code = ?");
            $stmt->execute([$test_username, $test_code]);
            $stored = $stmt->fetch(PDO::FETCH_ASSOC);

            if ($stored) {
                echo "<p style='color: green;'>✅ OTP code verified in database!</p>";
                echo "<pre>" . print_r($stored, true) . "</pre>";
            } else {
                echo "<p style='color: red;'>❌ OTP code not found in database after insert!</p>";
            }
        } else {
            echo "<p style='color: red;'>❌ Failed to insert test OTP code!</p>";
            echo "<p>Database error: " . json_encode($db->errorInfo()) . "</p>";
        }
    } catch (Exception $e) {
        echo "<p style='color: red;'>❌ Error testing OTP storage: " . $e->getMessage() . "</p>";
    }

    // Show current OTP codes in database
    echo "<h3>Current OTP Codes in Database:</h3>";
    $stmt = $db->query("SELECT id, username, otp_code, expires_at, used, created_at FROM otp_codes ORDER BY created_at DESC LIMIT 10");
    $codes = $stmt->fetchAll(PDO::FETCH_ASSOC);

    if (count($codes) > 0) {
        echo "<table border='1' cellpadding='5'>";
        echo "<tr><th>ID</th><th>Username</th><th>Code</th><th>Expires</th><th>Used</th><th>Created</th></tr>";
        foreach ($codes as $code) {
            echo "<tr>";
            echo "<td>{$code['id']}</td>";
            echo "<td>{$code['username']}</td>";
            echo "<td>{$code['otp_code']}</td>";
            echo "<td>{$code['expires_at']}</td>";
            echo "<td>{$code['used']}</td>";
            echo "<td>{$code['created_at']}</td>";
            echo "</tr>";
        }
        echo "</table>";
    } else {
        echo "<p>No OTP codes found in database.</p>";
    }

} catch (Exception $e) {
    echo "<h2 style='color: red;'>❌ Error: " . $e->getMessage() . "</h2>";
}
?>
