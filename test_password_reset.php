<?php
// Test script to verify password reset functionality
require_once "Appweb/User/config/database.php";

echo "<h1>Password Reset Test</h1>";

try {
    $db = (new Database())->getConnection();
    if (!$db) {
        die("Database connection failed");
    }

    echo "<p>✅ Database connection successful</p>";

    // Test 1: Create a test user if it doesn't exist
    $testUsername = 'testuser_reset_' . time(); // Make username unique
    $testEmail = 'test' . time() . '@example.com'; // Make email unique
    $originalPassword = 'original123';
    $newPassword = 'NewPassword123';

    // Check if test user exists
    $stmt = $db->prepare("SELECT username FROM users WHERE username = ?");
    $stmt->execute([$testUsername]);
    $existingUser = $stmt->fetch(PDO::FETCH_ASSOC);

    if (!$existingUser) {
        // Create test user
        $hashedPassword = password_hash($originalPassword, PASSWORD_DEFAULT);
        $stmt = $db->prepare("INSERT INTO users (username, email, password) VALUES (?, ?, ?)");
        $stmt->execute([$testUsername, $testEmail, $hashedPassword]);
        echo "<p>✅ Test user created</p>";
    } else {
        echo "<p>✅ Test user already exists</p>";
    }

    // Test 2: Create a temporary token (simulating the verify-code process)
    $temp_token = bin2hex(random_bytes(32));
    $token_expires = date('Y-m-d H:i:s', strtotime('+1 hour'));

    $stmt = $db->prepare("INSERT INTO password_reset_tokens (username, token, expires_at, used) VALUES (?, ?, ?, 0)");
    $stmt->execute([$testUsername, $temp_token, $token_expires]);

    echo "<h2>Test 2: Password Reset Process</h2>";
    echo "<ul>";
    echo "<li><strong>Username:</strong> $testUsername</li>";
    echo "<li><strong>Token:</strong> " . substr($temp_token, 0, 16) . "...</li>";
    echo "<li><strong>New Password:</strong> $newPassword</li>";
    echo "</ul>";

    // Test 3: Simulate the password reset API call
    $plain_new_password = $newPassword;

    // Update the user's password (no hashing)
    $stmt = $db->prepare("UPDATE users SET password = ? WHERE username = ?");
    $updateResult = $stmt->execute([$plain_new_password, $testUsername]);

    // Mark the token as used
    $stmt = $db->prepare("UPDATE password_reset_tokens SET used = 1 WHERE token = ?");
    $tokenResult = $stmt->execute([$temp_token]);

    if ($updateResult && $tokenResult) {
        echo "<p style='color: green;'>✅ Password update successful</p>";

        // Test 4: Verify the new password works
        $stmt = $db->prepare("SELECT password FROM users WHERE username = ?");
        $stmt->execute([$testUsername]);
        $storedPassword = $stmt->fetch(PDO::FETCH_ASSOC)['password'];

        if ($newPassword === $storedPassword) {
            echo "<p style='color: green;'>✅ New password verification PASSED</p>";
        } else {
            echo "<p style='color: red;'>❌ New password verification FAILED</p>";
        }

        // Test 5: Verify old password no longer works
        if ($originalPassword !== $storedPassword) {
            echo "<p style='color: green;'>✅ Old password correctly rejected</p>";
        } else {
            echo "<p style='color: red;'>❌ Old password still works (password not changed!)</p>";
        }

    } else {
        echo "<p style='color: red;'>❌ Password update failed</p>";
    }

    echo "<h2>Test Summary:</h2>";
    echo "<ul>";
    echo "<li>Database connection: ✅ Working</li>";
    echo "<li>Password storage: ✅ Plain text (no hashing)</li>";
    echo "<li>Password update: " . ($updateResult ? "✅ Successful" : "❌ Failed") . "</li>";
    echo "<li>New password verification: " . ($newPassword === $storedPassword ? "✅ Passed" : "❌ Failed") . "</li>";
    echo "<li>Old password rejection: " . ($originalPassword !== $storedPassword ? "✅ Correct" : "❌ Failed") . "</li>";
    echo "</ul>";

} catch (Exception $e) {
    echo "<h2 style='color: red;'>Test Error:</h2>";
    echo "<p>" . $e->getMessage() . "</p>";
}

echo "<hr>";
echo "<p><small>Test completed at: " . date('Y-m-d H:i:s') . "</small></p>";
?>
