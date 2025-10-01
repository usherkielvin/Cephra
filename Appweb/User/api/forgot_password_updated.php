<?php
date_default_timezone_set('Asia/Shanghai'); // Set PHP timezone explicitly

// Updated Forgot Password API with PHPMailer email sending and OTP code logic

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

require_once "../config/database.php";
require_once "../config/email_config.php";
require_once "../vendor/autoload.php";

header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST");
header("Access-Control-Allow-Headers: Content-Type");

$db = (new Database())->getConnection();
if (!$db) {
    echo json_encode([
        "success" => false,
        "error" => "Database connection failed"
    ]);
    exit();
}

// Set MySQL session timezone to match PHP timezone
$db->exec("SET time_zone = '+08:00'");

// Log PHP timezone
error_log("PHP timezone: " . date_default_timezone_get());

// Log MySQL timezone
$stmt = $db->query("SELECT @@global.time_zone, @@session.time_zone");
$timezones = $stmt->fetch(PDO::FETCH_ASSOC);
error_log("MySQL global timezone: " . $timezones['@@global.time_zone'] . ", session timezone: " . $timezones['@@session.time_zone']);

$method = $_SERVER["REQUEST_METHOD"];
$action = $method === "POST" ? ($_POST["action"] ?? "") : ($_GET["action"] ?? "");
$action = trim(strtolower($action));



try {
    switch ($action) {
        case "request-reset":
            if ($method !== "POST") {
                echo json_encode(["success" => false, "error" => "Method not allowed"]);
                break;
            }

            $email = $_POST["email"] ?? "";
            if (!$email || !filter_var($email, FILTER_VALIDATE_EMAIL)) {
                echo json_encode(["success" => false, "error" => "Invalid email"]);
                break;
            }

            // Check if user exists and get username
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
            $username = $user['username'];

            // Generate 6-digit reset code
            $reset_code = str_pad(rand(100000, 999999), 6, '0', STR_PAD_LEFT);
            $expires_at = date('Y-m-d H:i:s', strtotime('+30 minutes'));

            // First, mark any existing unused codes as used to avoid constraint issues
            $stmt = $db->prepare("UPDATE otp_codes SET used = 1 WHERE username = ? AND used = 0");
            $stmt->execute([$username]);

            // Now insert the new code
            $stmt = $db->prepare("
                INSERT INTO otp_codes (username, otp_code, expires_at, used, created_at)
                VALUES (?, ?, ?, 0, NOW())
            ");
            $result = $stmt->execute([$username, $reset_code, $expires_at]);

            // Debug logging
            error_log("OTP Storage Debug - Username: $username, Code: $reset_code, Expires: $expires_at, Result: " . ($result ? 'Success' : 'Failed'));
            error_log("Database error info: " . json_encode($db->errorInfo()));

            // Send email using PHPMailer
            $mail = new PHPMailer(true);
            try {
                $mail->isSMTP();
                $mail->Host = EmailConfig::$smtp_host;
                $mail->SMTPAuth = true;
                $mail->Username = EmailConfig::$smtp_username;
                $mail->Password = EmailConfig::$smtp_password;
                $mail->SMTPSecure = EmailConfig::$smtp_encryption;
                $mail->Port = EmailConfig::$smtp_port;

                $mail->setFrom(EmailConfig::$from_email, EmailConfig::$from_name);
                $mail->addAddress($email);

                $mail->isHTML(true);
                $mail->Subject = 'Password Reset Code - Cephra';
                $mail->Body = EmailConfig::getResetEmailTemplate($email, $reset_code)['message'];

                $mail->send();

                // Clean up expired codes after successful email send - TEMPORARILY DISABLED
                // $cleanedCount = cleanupExpiredOTPCodes($db);
                // if ($cleanedCount > 0) {
                //     error_log("OTP Cleanup: Cleaned up $cleanedCount expired codes after successful reset request");
                // }

                echo json_encode([
                    "success" => true,
                    "message" => "Reset code sent to your email"
                ]);
            } catch (Exception $e) {
                echo json_encode([
                    "success" => false,
                    "error" => "Mailer Error: " . $mail->ErrorInfo
                ]);
            }
            break;

        case "resend-code":
            if ($method !== "POST") {
                echo json_encode(["success" => false, "error" => "Method not allowed"]);
                break;
            }

            $email = $_POST["email"] ?? "";
            if (!$email || !filter_var($email, FILTER_VALIDATE_EMAIL)) {
                echo json_encode(["success" => false, "error" => "Invalid email"]);
                break;
            }

            // Check if user exists and get username
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
            $username = $user['username'];

            // Check if there's an existing code and if it's still valid
            $stmt = $db->prepare("SELECT otp_code, expires_at FROM otp_codes WHERE username = ? AND used = 0");
            $stmt->execute([$username]);
            $existingCode = $stmt->fetch(PDO::FETCH_ASSOC);

            if ($existingCode) {
                $expiresAt = new DateTime($existingCode['expires_at']);
                $now = new DateTime();
                $minutesRemaining = ($expiresAt->getTimestamp() - $now->getTimestamp()) / 60;

                if ($minutesRemaining > 0) {
                    $minutes = ceil($minutesRemaining);
                    echo json_encode([
                        "success" => false,
                        "error" => "Please wait $minutes minutes before requesting another code"
                    ]);
                    break;
                }
            }

            // Generate new code and update
            $reset_code = str_pad(rand(100000, 999999), 6, '0', STR_PAD_LEFT);
            $expires_at = date('Y-m-d H:i:s', strtotime('+30 minutes'));

            // First, mark any existing unused codes as used to avoid constraint issues
            $stmt = $db->prepare("UPDATE otp_codes SET used = 1 WHERE username = ? AND used = 0");
            $stmt->execute([$username]);

            // Now insert the new code
            $stmt = $db->prepare("
                INSERT INTO otp_codes (username, otp_code, expires_at, used, created_at)
                VALUES (?, ?, ?, 0, NOW())
            ");
            $result = $stmt->execute([$username, $reset_code, $expires_at]);

            // Debug logging
            error_log("OTP Resend Debug - Username: $username, Code: $reset_code, Expires: $expires_at, Result: " . ($result ? 'Success' : 'Failed'));
            error_log("Database error info: " . json_encode($db->errorInfo()));

            // Send email using PHPMailer
            $mail = new PHPMailer(true);
            try {
                $mail->isSMTP();
                $mail->Host = EmailConfig::$smtp_host;
                $mail->SMTPAuth = true;
                $mail->Username = EmailConfig::$smtp_username;
                $mail->Password = EmailConfig::$smtp_password;
                $mail->SMTPSecure = EmailConfig::$smtp_encryption;
                $mail->Port = EmailConfig::$smtp_port;

                $mail->setFrom(EmailConfig::$from_email, EmailConfig::$from_name);
                $mail->addAddress($email);

                $mail->isHTML(true);
                $mail->Subject = 'Password Reset Code - Cephra';
                $mail->Body = EmailConfig::getResetEmailTemplate($email, $reset_code)['message'];

                $mail->send();

                // Clean up expired codes after successful email send - TEMPORARILY DISABLED
                // $cleanedCount = cleanupExpiredOTPCodes($db);
                // if ($cleanedCount > 0) {
                //     error_log("OTP Cleanup: Cleaned up $cleanedCount expired codes after successful resend");
                // }

                echo json_encode([
                    "success" => true,
                    "message" => "Reset code resent to your email"
                ]);
            } catch (Exception $e) {
                echo json_encode([
                    "success" => false,
                    "error" => "Mailer Error: " . $mail->ErrorInfo
                ]);
            }
            break;

        case "verify-code":
            if ($method !== "POST") {
                echo json_encode(["success" => false, "error" => "Method not allowed"]);
                break;
            }

            $email = $_POST["email"] ?? "";
            $code = $_POST["code"] ?? "";

            if (!$email || !filter_var($email, FILTER_VALIDATE_EMAIL)) {
                echo json_encode(["success" => false, "error" => "Invalid email"]);
                break;
            }

            if (!$code || strlen($code) !== 6 || !ctype_digit($code)) {
                echo json_encode(["success" => false, "error" => "Invalid code format"]);
                break;
            }

            // Check if user exists and get username
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
            $username = $user['username'];

            // Verify the code
            $stmt = $db->prepare("SELECT otp_code, expires_at FROM otp_codes WHERE username = ? AND used = 0");
            $stmt->execute([$username]);
            $storedCode = $stmt->fetch(PDO::FETCH_ASSOC);

            if (!$storedCode) {
                echo json_encode([
                    "success" => false,
                    "error" => "No reset code found. Please request a new one."
                ]);
                break;
            }

            // Check if code has expired
            $expiresAt = new DateTime($storedCode['expires_at']);
            $now = new DateTime();
            if ($now > $expiresAt) {
                echo json_encode([
                    "success" => false,
                    "error" => "Code has expired. Please request a new one."
                ]);
                break;
            }

            // Check if code matches
            if ($storedCode['otp_code'] !== $code) {
                echo json_encode([
                    "success" => false,
                    "error" => "Invalid verification code"
                ]);
                break;
            }

            // Mark the OTP code as verified (used=0 remains, but we can check it later)
            // No temp token needed

            // Clean up expired codes after successful verification - TEMPORARILY DISABLED
            // $cleanedCount = cleanupExpiredOTPCodes($db);
            // if ($cleanedCount > 0) {
            //     error_log("OTP Cleanup: Cleaned up $cleanedCount expired codes after successful verification");
            // }

            echo json_encode([
                "success" => true,
                "message" => "Code verified successfully"
            ]);
            break;

        case "reset-password":
            if ($method !== "POST") {
                echo json_encode(["success" => false, "error" => "Method not allowed"]);
                break;
            }

            $email = $_POST["email"] ?? "";
            $new_password = $_POST["new_password"] ?? "";

            if (!$email || !filter_var($email, FILTER_VALIDATE_EMAIL)) {
                echo json_encode(["success" => false, "error" => "Invalid email"]);
                break;
            }

            if (!$new_password || strlen($new_password) < 8) {
                echo json_encode(["success" => false, "error" => "Password must be at least 8 characters long"]);
                break;
            }

            // Validate password strength
            if (!preg_match('/[A-Z]/', $new_password) ||
                !preg_match('/[a-z]/', $new_password) ||
                !preg_match('/\d/', $new_password)) {
                echo json_encode(["success" => false, "error" => "Password must contain at least one uppercase letter, one lowercase letter, and one number"]);
                break;
            }

            // Check if user exists and get username
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
            $username = $user['username'];

            // Verify there is a valid unused OTP code for this user
            $stmt = $db->prepare("SELECT otp_code, expires_at FROM otp_codes WHERE username = ? AND used = 0");
            $stmt->execute([$username]);
            $otpData = $stmt->fetch(PDO::FETCH_ASSOC);

            if (!$otpData) {
                echo json_encode(["success" => false, "error" => "No valid reset code found. Please request a new password reset."]);
                break;
            }

            // Check if OTP has expired
            $expiresAt = new DateTime($otpData['expires_at']);
            $now = new DateTime();
            if ($now > $expiresAt) {
                echo json_encode(["success" => false, "error" => "Reset code has expired. Please request a new password reset."]);
                break;
            }

            // Store the new password (no hashing)
            $plain_password = $new_password;

            // Update the user's password
            $stmt = $db->prepare("UPDATE users SET password = ? WHERE username = ?");
            $result = $stmt->execute([$plain_password, $username]);

            if ($result) {
                // Mark the OTP code as used
                $stmt = $db->prepare("UPDATE otp_codes SET used = 1 WHERE username = ? AND otp_code = ?");
                $stmt->execute([$username, $otpData['otp_code']]);

                // Unset the session flag after successful password reset
                if (session_status() == PHP_SESSION_NONE) {
                    session_start();
                }
                unset($_SESSION['forgot_password_flow']);

                echo json_encode([
                    "success" => true,
                    "message" => "Password reset successfully"
                ]);
            } else {
                echo json_encode([
                    "success" => false,
                    "error" => "Failed to update password. Please try again."
                ]);
            }
            break;

        default:
            echo json_encode([
                "success" => false,
                "error" => "Invalid action"
            ]);
            break;
    }
} catch (Exception $e) {
    error_log("Exception: " . $e->getMessage());
    echo json_encode([
        "success" => false,
        "error" => $e->getMessage()
    ]);
}
?>
