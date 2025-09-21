<?php
// Forgot Password API - WORKING VERSION
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST");
header("Access-Control-Allow-Headers: Content-Type");

// Enable error reporting for debugging
error_reporting(E_ALL);
ini_set("display_errors", 1);

require_once "../../config/database.php";

$db = (new Database())->getConnection();
if (!$db) {
    echo json_encode([
        "success" => false,
        "error" => "Database connection failed",
        "details" => "Check database configuration and XAMPP MySQL service"
    ]);
    exit();
}

$method = $_SERVER["REQUEST_METHOD"];

// Get action from POST data or query string
$action = "";
if ($method === "POST") {
    $action = $_POST["action"] ?? "";
} else {
    $action = $_GET["action"] ?? "";
}

try {
    switch ($action) {
        case "request-reset":
            if ($method !== "POST") {
                echo json_encode([
                    "success" => false,
                    "error" => "Method not allowed"
                ]);
                break;
            }

            $email = $_POST["email"] ?? "";

            if (!$email) {
                echo json_encode([
                    "success" => false,
                    "error" => "Email is required"
                ]);
                break;
            }

            // Validate email format
            if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
                echo json_encode([
                    "success" => false,
                    "error" => "Invalid email format"
                ]);
                break;
            }

            // Check if user exists
            $stmt = $db->prepare("SELECT username, email FROM users WHERE email = ?");
            $stmt->execute([$email]);
            $user = $stmt->fetch(PDO::FETCH_ASSOC);

            if (!$user) {
                // For security, don't reveal if email exists or not
                echo json_encode([
                    "success" => true,
                    "message" => "If the email exists, a reset code has been sent"
                ]);
                break;
            }

            // Generate 6-digit reset code
            $reset_code = str_pad(rand(100000, 999999), 6, '0', STR_PAD_LEFT);

            // Set expiration time (30 minutes from now)
            $expires_at = date('Y-m-d H:i:s', strtotime('+30 minutes'));

            // Insert or update reset token - FIXED: Use username instead of email
            $stmt = $db->prepare("
                INSERT INTO otp_codes (username, otp_code, expires_at, used)
                VALUES (?, ?, ?, 0)
                ON DUPLICATE KEY UPDATE
                otp_code = ?, expires_at = ?, used = 0
            ");
            $stmt->execute([$user['username'], $reset_code, $expires_at, $reset_code, $expires_at]);

            // TODO: Send email with reset code
            // For now, we'll return the code in the response (remove in production)
            echo json_encode([
                "success" => true,
                "message" => "Reset code sent to your email",
                "reset_code" => $reset_code, // Remove this in production
                "email" => $email
            ]);
            break;

        case "verify-code":
            if ($method !== "POST") {
                echo json_encode([
                    "success" => false,
                    "error" => "Method not allowed"
                ]);
                break;
            }

            $email = $_POST["email"] ?? "";
            $code = $_POST["code"] ?? "";

            if (!$email || !$code) {
                echo json_encode([
                    "success" => false,
                    "error" => "Email and code are required"
                ]);
                break;
            }

            // First get the username from the email
            $stmt = $db->prepare("SELECT username FROM users WHERE email = ?");
            $stmt->execute([$email]);
            $user = $stmt->fetch(PDO::FETCH_ASSOC);

            if (!$user) {
                echo json_encode([
                    "success" => false,
                    "error" => "Email not found"
                ]);
                break;
            }

            // Check if reset token exists and is valid using username
            $stmt = $db->prepare("
                SELECT id, otp_code, expires_at, used
                FROM otp_codes
                WHERE username = ? AND used = 0
                ORDER BY created_at DESC
                LIMIT 1
            ");
            $stmt->execute([$user['username']]);
            $token = $stmt->fetch(PDO::FETCH_ASSOC);

            if (!$token) {
                echo json_encode([
                    "success" => false,
                    "error" => "No reset request found for this email"
                ]);
                break;
            }

            // Check if code matches
            if ($token['otp_code'] !== $code) {
                echo json_encode([
                    "success" => false,
                    "error" => "Invalid reset code"
                ]);
                break;
            }

            // Check if code has expired
            if (strtotime($token['expires_at']) < time()) {
                echo json_encode([
                    "success" => false,
                    "error" => "Reset code has expired. Please request a new one."
                ]);
                break;
            }

            // Generate a temporary token for password reset
            $temp_token = bin2hex(random_bytes(32));

            // Update the token as used and store temp token
            $stmt = $db->prepare("
                UPDATE otp_codes
                SET used = 1, temp_token = ?, updated_at = NOW()
                WHERE id = ?
            ");
            $stmt->execute([$temp_token, $token['id']]);

            echo json_encode([
                "success" => true,
                "message" => "Code verified successfully",
                "temp_token" => $temp_token
            ]);
            break;

        case "reset-password":
            if ($method !== "POST") {
                echo json_encode([
                    "success" => false,
                    "error" => "Method not allowed"
                ]);
                break;
            }

            $temp_token = $_POST["temp_token"] ?? "";
            $new_password = $_POST["new_password"] ?? "";

            if (!$temp_token || !$new_password) {
                echo json_encode([
                    "success" => false,
                    "error" => "Token and new password are required"
                ]);
                break;
            }

            // Validate password strength
            if (strlen($new_password) < 8) {
                echo json_encode([
                    "success" => false,
                    "error" => "Password must be at least 8 characters long"
                ]);
                break;
            }

            if (!preg_match('/[A-Z]/', $new_password)) {
                echo json_encode([
                    "success" => false,
                    "error" => "Password must contain at least one uppercase letter"
                ]);
                break;
            }

            if (!preg_match('/[a-z]/', $new_password)) {
                echo json_encode([
                    "success" => false,
                    "error" => "Password must contain at least one lowercase letter"
                ]);
                break;
            }

            if (!preg_match('/[0-9]/', $new_password)) {
                echo json_encode([
                    "success" => false,
                    "error" => "Password must contain at least one number"
                ]);
                break;
            }

            // Find the reset token
            $stmt = $db->prepare("
                SELECT username, used
                FROM otp_codes
                WHERE temp_token = ? AND used = 1
                ORDER BY updated_at DESC
                LIMIT 1
            ");
            $stmt->execute([$temp_token]);
            $token = $stmt->fetch(PDO::FETCH_ASSOC);

            if (!$token) {
                echo json_encode([
                    "success" => false,
                    "error" => "Invalid or expired reset token"
                ]);
                break;
            }

            // Update user's password
            $stmt = $db->prepare("
                UPDATE users
                SET password = ?
                WHERE username = ?
            ");
            $result = $stmt->execute([$new_password, $token['username']]);

            if ($result) {
                // Mark token as fully used
                $stmt = $db->prepare("
                    UPDATE otp_codes
                    SET password_updated = 1, updated_at = NOW()
                    WHERE temp_token = ?
                ");
                $stmt->execute([$temp_token]);

                echo json_encode([
                    "success" => true,
                    "message" => "Password updated successfully"
                ]);
            } else {
                echo json_encode([
                    "success" => false,
                    "error" => "Failed to update password"
                ]);
            }
            break;

        case "resend-code":
            if ($method !== "POST") {
                echo json_encode([
                    "success" => false,
                    "error" => "Method not allowed"
                ]);
                break;
            }

            $email = $_POST["email"] ?? "";

            if (!$email) {
                echo json_encode([
                    "success" => false,
                    "error" => "Email is required"
                ]);
                break;
            }

            // Get username from email
            $stmt = $db->prepare("SELECT username FROM users WHERE email = ?");
            $stmt->execute([$email]);
            $user = $stmt->fetch(PDO::FETCH_ASSOC);

            if (!$user) {
                echo json_encode([
                    "success" => false,
                    "error" => "Email not found"
                ]);
                break;
            }

            // Check if there's a recent reset request
            $stmt = $db->prepare("
                SELECT created_at
                FROM otp_codes
                WHERE username = ? AND used = 0
                ORDER BY created_at DESC
                LIMIT 1
            ");
            $stmt->execute([$user['username']]);
            $recent = $stmt->fetch(PDO::FETCH_ASSOC);

            if ($recent) {
                $last_request = strtotime($recent['created_at']);
                $now = time();

                // If less than 2 minutes ago, don't allow resend
                if (($now - $last_request) < 120) {
                    echo json_encode([
                        "success" => false,
                        "error" => "Please wait at least 2 minutes before requesting another code"
                    ]);
                    break;
                }
            }

            // Generate new code (same as request-reset)
            $reset_code = str_pad(rand(100000, 999999), 6, '0', STR_PAD_LEFT);
            $expires_at = date('Y-m-d H:i:s', strtotime('+30 minutes'));

            $stmt = $db->prepare("
                INSERT INTO otp_codes (username, otp_code, expires_at, used)
                VALUES (?, ?, ?, 0)
                ON DUPLICATE KEY UPDATE
                otp_code = ?, expires_at = ?, used = 0
            ");
            $stmt->execute([$user['username'], $reset_code, $expires_at, $reset_code, $expires_at]);

            echo json_encode([
                "success" => true,
                "message" => "New reset code sent to your email",
                "reset_code" => $reset_code // Remove in production
            ]);
            break;

        default:
            echo json_encode([
                "success" => false,
                "error" => "Invalid action",
                "available_actions" => [
                    "request-reset", "verify-code", "reset-password", "resend-code"
                ]
            ]);
            break;
    }
} catch (Exception $e) {
    echo json_encode([
        "success" => false,
        "error" => "Server error: " . $e->getMessage()
    ]);
}
?>
