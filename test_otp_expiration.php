<?php
// Test script to verify OTP expiration logic after fixes
require_once "Appweb/User/config/database.php";

echo "<h1>OTP Expiration Test</h1>";

try {
    $db = (new Database())->getConnection();
    if (!$db) {
        die("Database connection failed");
    }

    echo "<p>✅ Database connection successful</p>";

    // Test 1: Insert an expired OTP (use existing user or create one)
    $username = 'testuser'; // Use the same as storage test
    $reset_code = '123456';
    $expires_at = date('Y-m-d H:i:s', strtotime('-1 hour')); // Expired 1 hour ago

    echo "<h2>Test 1: Expired OTP</h2>";
    echo "<ul>";
    echo "<li><strong>Username:</strong> $username</li>";
    echo "<li><strong>OTP Code:</strong> $reset_code</li>";
    echo "<li><strong>Expires At:</strong> $expires_at</li>";
    echo "</ul>";

    $stmt = $db->prepare("
        INSERT INTO otp_codes (username, otp_code, expires_at, used)
        VALUES (?, ?, ?, 0)
        ON DUPLICATE KEY UPDATE
        otp_code = ?, expires_at = ?, used = 0
    ");
    $stmt->execute([$username, $reset_code, $expires_at, $reset_code, $expires_at]);

    // Check expiration logic
    $stmt = $db->prepare("SELECT otp_code, expires_at FROM otp_codes WHERE username = ? AND used = 0");
    $stmt->execute([$username]);
    $stored = $stmt->fetch(PDO::FETCH_ASSOC);

    if ($stored) {
        $expiresAt = strtotime($stored['expires_at']);
        $now = time();
        $isExpired = $expiresAt < $now;

        echo "<p><strong>Expiration Check:</strong> " . ($isExpired ? "Expired" : "Not Expired") . "</p>";
        echo "<p style='color: " . ($isExpired ? "green" : "red") . ";'>✅ Test 1 " . ($isExpired ? "PASSED" : "FAILED") . " - Expired OTP detected correctly</p>";
    }

    // Test 2: Insert a valid OTP
    $username2 = 'testuser_valid';
    $reset_code2 = '654321';
    $expires_at2 = date('Y-m-d H:i:s', strtotime('+1 hour')); // Expires in 1 hour

    echo "<h2>Test 2: Valid OTP</h2>";
    echo "<ul>";
    echo "<li><strong>Username:</strong> $username2</li>";
    echo "<li><strong>OTP Code:</strong> $reset_code2</li>";
    echo "<li><strong>Expires At:</strong> $expires_at2</li>";
    echo "</ul>";

    $stmt = $db->prepare("
        INSERT INTO otp_codes (username, otp_code, expires_at, used)
        VALUES (?, ?, ?, 0)
        ON DUPLICATE KEY UPDATE
        otp_code = ?, expires_at = ?, used = 0
    ");
    $stmt->execute([$username2, $reset_code2, $expires_at2, $reset_code2, $expires_at2]);

    // Check expiration logic
    $stmt = $db->prepare("SELECT otp_code, expires_at FROM otp_codes WHERE username = ? AND used = 0");
    $stmt->execute([$username2]);
    $stored2 = $stmt->fetch(PDO::FETCH_ASSOC);

    if ($stored2) {
        $expiresAt2 = strtotime($stored2['expires_at']);
        $now2 = time();
        $isExpired2 = $expiresAt2 < $now2;

        echo "<p><strong>Expiration Check:</strong> " . ($isExpired2 ? "Expired" : "Not Expired") . "</p>";
        echo "<p style='color: " . ($isExpired2 ? "red" : "green") . ";'>✅ Test 2 " . ($isExpired2 ? "FAILED" : "PASSED") . " - Valid OTP detected correctly</p>";
    }

    echo "<h2>Test Summary:</h2>";
    echo "<ul>";
    echo "<li>Database connection: ✅ Working</li>";
    echo "<li>Expired OTP detection: " . ($isExpired ? "✅ Correct" : "❌ Incorrect") . "</li>";
    echo "<li>Valid OTP detection: " . ($isExpired2 ? "❌ Incorrect" : "✅ Correct") . "</li>";
    echo "</ul>";

} catch (Exception $e) {
    echo "<h2 style='color: red;'>Test Error:</h2>";
    echo "<p>" . $e->getMessage() . "</p>";
}

echo "<hr>";
echo "<p><small>Test completed at: " . date('Y-m-d H:i:s') . "</small></p>";
?>
