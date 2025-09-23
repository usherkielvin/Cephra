<?php
// Test script to verify OTP storage after fixes
require_once "Appweb/User/config/database.php";

echo "<h1>OTP Storage Test</h1>";

try {
    $db = (new Database())->getConnection();
    if (!$db) {
        die("Database connection failed");
    }

    echo "<p>✅ Database connection successful</p>";

    // Simulate OTP generation and storage
    $username = 'testuser'; // Use a test username
    $reset_code = str_pad(rand(100000, 999999), 6, '0', STR_PAD_LEFT);
    $expires_at = date('Y-m-d H:i:s', strtotime('+30 minutes'));

    echo "<h2>Testing OTP Storage:</h2>";
    echo "<ul>";
    echo "<li><strong>Username:</strong> $username</li>";
    echo "<li><strong>OTP Code:</strong> $reset_code</li>";
    echo "<li><strong>Expires At:</strong> $expires_at</li>";
    echo "</ul>";

    // Insert or update OTP code
    $stmt = $db->prepare("
        INSERT INTO otp_codes (username, otp_code, expires_at, used)
        VALUES (?, ?, ?, 0)
        ON DUPLICATE KEY UPDATE
        otp_code = ?, expires_at = ?, used = 0
    ");
    $result = $stmt->execute([$username, $reset_code, $expires_at, $reset_code, $expires_at]);

    if ($result) {
        echo "<p style='color: green;'>✅ OTP code stored successfully</p>";

        // Verify storage
        $stmt = $db->prepare("SELECT otp_code, expires_at, used FROM otp_codes WHERE username = ?");
        $stmt->execute([$username]);
        $stored = $stmt->fetch(PDO::FETCH_ASSOC);

        if ($stored) {
            echo "<h2>Stored Data Verification:</h2>";
            echo "<ul>";
            echo "<li><strong>Stored Code:</strong> {$stored['otp_code']}</li>";
            echo "<li><strong>Stored Expires:</strong> {$stored['expires_at']}</li>";
            echo "<li><strong>Used Flag:</strong> {$stored['used']}</li>";
            echo "</ul>";

            if ($stored['otp_code'] === $reset_code && $stored['used'] == 0) {
                echo "<p style='color: green;'>✅ Verification PASSED - OTP stored correctly</p>";
            } else {
                echo "<p style='color: red;'>❌ Verification FAILED - Mismatch in stored data</p>";
            }
        } else {
            echo "<p style='color: red;'>❌ No data found in database</p>";
        }
    } else {
        echo "<p style='color: red;'>❌ Failed to store OTP code</p>";
    }

    echo "<h2>Test Summary:</h2>";
    echo "<ul>";
    echo "<li>Database connection: ✅ Working</li>";
    echo "<li>OTP generation: ✅ Successful</li>";
    echo "<li>OTP storage: " . ($result ? "✅ Successful" : "❌ Failed") . "</li>";
    echo "<li>Data verification: " . ($stored && $stored['otp_code'] === $reset_code ? "✅ Passed" : "❌ Failed") . "</li>";
    echo "</ul>";

} catch (Exception $e) {
    echo "<h2 style='color: red;'>Test Error:</h2>";
    echo "<p>" . $e->getMessage() . "</p>";
}

echo "<hr>";
echo "<p><small>Test completed at: " . date('Y-m-d H:i:s') . "</small></p>";
?>
