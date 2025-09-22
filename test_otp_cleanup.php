<?php
// Test script to verify OTP cleanup functionality
// Run this script to test if expired OTP codes are being cleaned up

require_once "Appweb/User/config/database.php";

echo "<h1>OTP Cleanup Test</h1>";

try {
    $db = (new Database())->getConnection();
    if (!$db) {
        die("Database connection failed");
    }

    echo "<p>✅ Database connection successful</p>";

    // Check current OTP codes in database
    $stmt = $db->query("SELECT COUNT(*) as total_codes FROM otp_codes");
    $totalCodes = $stmt->fetch(PDO::FETCH_ASSOC)['total_codes'];

    $stmt = $db->query("SELECT COUNT(*) as expired_codes FROM otp_codes WHERE expires_at < NOW() AND used = 0");
    $expiredCodes = $stmt->fetch(PDO::FETCH_ASSOC)['expired_codes'];

    $stmt = $db->query("SELECT COUNT(*) as active_codes FROM otp_codes WHERE expires_at > NOW() AND used = 0");
    $activeCodes = $stmt->fetch(PDO::FETCH_ASSOC)['active_codes'];

    echo "<h2>Current Database Status:</h2>";
    echo "<ul>";
    echo "<li><strong>Total OTP codes:</strong> $totalCodes</li>";
    echo "<li><strong>Expired codes:</strong> $expiredCodes</li>";
    echo "<li><strong>Active codes:</strong> $activeCodes</li>";
    echo "</ul>";

    // Test the cleanup function
    echo "<h2>Testing Cleanup Function:</h2>";

    function cleanupExpiredOTPCodes($db) {
        try {
            $stmt = $db->prepare("DELETE FROM otp_codes WHERE expires_at < NOW() AND used = 0");
            $deletedCount = $stmt->execute() ? $stmt->rowCount() : 0;

            if ($deletedCount > 0) {
                error_log("OTP Cleanup Test: Deleted $deletedCount expired OTP codes");
                return $deletedCount;
            }

            return $deletedCount;
        } catch (Exception $e) {
            error_log("OTP Cleanup Test Error: " . $e->getMessage());
            return 0;
        }
    }

    $cleanedCount = cleanupExpiredOTPCodes($db);

    echo "<p><strong>Cleanup Result:</strong> $cleanedCount expired codes deleted</p>";

    // Check status after cleanup
    $stmt = $db->query("SELECT COUNT(*) as expired_codes_after FROM otp_codes WHERE expires_at < NOW() AND used = 0");
    $expiredCodesAfter = $stmt->fetch(PDO::FETCH_ASSOC)['expired_codes_after'];

    echo "<h2>Status After Cleanup:</h2>";
    echo "<ul>";
    echo "<li><strong>Expired codes remaining:</strong> $expiredCodesAfter</li>";
    echo "</ul>";

    if ($expiredCodesAfter == 0) {
        echo "<p style='color: green;'>✅ Cleanup test PASSED - No expired codes remaining</p>";
    } else {
        echo "<p style='color: red;'>❌ Cleanup test FAILED - $expiredCodesAfter expired codes still exist</p>";
    }

    echo "<h2>Test Summary:</h2>";
    echo "<ul>";
    echo "<li>Database connection: ✅ Working</li>";
    echo "<li>Cleanup function: ✅ Executed</li>";
    echo "<li>Expired codes cleanup: " . ($expiredCodesAfter == 0 ? "✅ Successful" : "❌ Failed") . "</li>";
    echo "</ul>";

} catch (Exception $e) {
    echo "<h2 style='color: red;'>Test Error:</h2>";
    echo "<p>" . $e->getMessage() . "</p>";
}

echo "<hr>";
echo "<p><small>Test completed at: " . date('Y-m-d H:i:s') . "</small></p>";
?>
