<?php
<?php
// Forgot Password API with PHPMailer email sending and OTP code logic

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

$method = $_SERVER["REQUEST_METHOD"];
$action = $method === "POST" ? ($_POST["action"] ?? "") : ($_GET["action"] ?? "");

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

            // Check if user exists
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

            // Generate 6-digit reset code
            $reset_code = str_pad(rand(100000, 999999), 6, '0', STR_PAD_LEFT);

            // Insert or update reset code in otp_codes table
            $expires_at = date('Y-m-d H:i:s', strtotime('+30 minutes'));
            $stmt = $db->prepare("
                INSERT INTO otp_codes (email, otp_code, expires_at, used, created_at)
                VALUES (?, ?, ?, 0, NOW())
                ON DUPLICATE KEY UPDATE
                otp_code = VALUES(otp_code),
                expires_at = VALUES(expires_at),
                used = 0,
                created_at = NOW()
            ");
            $stmt->execute([$email, $reset_code, $expires_at]);

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

        case "verify-code":
            if ($method !== "POST") {
                echo json_encode(["success" => false, "error" => "Method not allowed"]);
                break;
            }

            $email = $_POST["email"] ?? "";
            $code = $_POST["code"] ?? "";

            if (!$email || !$code) {
                echo json_encode(["success" => false, "error" => "Email and code are required"]);
                break;
            }

            $stmt = $db->prepare("
                SELECT * FROM otp_codes
                WHERE email = ? AND otp_code = ? AND used = 0 AND expires_at > NOW()
                LIMIT 1
            ");
            $stmt->execute([$email, $code]);
            $otp = $stmt->fetch(PDO::FETCH_ASSOC);

            if (!$otp) {
                echo json_encode(["success" => false, "error" => "Invalid or expired code"]);
                break;
            }

            // Mark code as used
            $stmt = $db->prepare("UPDATE otp_codes SET used = 1 WHERE id = ?");
            $stmt->execute([$otp['id']]);

            // Generate a temporary token for password reset (simple example)
            $temp_token = bin2hex(random_bytes(16));

            // Store temp token in otp_codes table
            $stmt = $db->prepare("UPDATE otp_codes SET temp_token = ? WHERE id = ?");
            $stmt->execute([$temp_token, $otp['id']]);

            echo json_encode([
                "success" => true,
                "temp_token" => $temp_token
            ]);
            break;

        case "reset-password":
            if ($method !== "POST") {
                echo json_encode(["success" => false, "error" => "Method not allowed"]);
                break;
            }

            $temp_token = $_POST["temp_token"] ?? "";
            $new_password = $_POST["new_password"] ?? "";

            if (!$temp_token || !$new_password) {
                echo json_encode(["success" => false, "error" => "Token and new password are required"]);
                break;
            }

            // Validate temp token and get email
            $stmt = $db->prepare("SELECT email FROM otp_codes WHERE temp_token = ? AND used = 1 LIMIT 1");
            $stmt->execute([$temp_token]);
            $row = $stmt->fetch(PDO::FETCH_ASSOC);

            if (!$row) {
                echo json_encode(["success" => false, "error" => "Invalid or expired token"]);
                break;
            }

            $email = $row['email'];

            // Update user's password (hashing with password_hash)
            $hashed_password = password_hash($new_password, PASSWORD_DEFAULT);
            $stmt = $db->prepare("UPDATE users SET password = ? WHERE email = ?");
            $stmt->execute([$hashed_password, $email]);

            // Mark password updated and token used
            $stmt = $db->prepare("UPDATE otp_codes SET password_updated = 1 WHERE temp_token = ?");
            $stmt->execute([$temp_token]);

            echo json_encode([
                "success" => true,
                "message" => "Password has been reset successfully"
            ]);
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

            // Check for recent unused code
            $stmt = $db->prepare("
                SELECT created_at FROM otp_codes
                WHERE email = ? AND used = 0
                ORDER BY created_at DESC LIMIT 1
            ");
            $stmt->execute([$email]);
            $recent = $stmt->fetch(PDO::FETCH_ASSOC);

            if ($recent) {
                $last_request = strtotime($recent['created_at']);
                $now = time();
                if (($now - $last_request) < 120) {
                    echo json_encode([
                        "success" => false,
                        "error" => "Please wait at least 2 minutes before requesting another code"
                    ]);
                    break;
                }
            }

            // Generate new code and update
            $reset_code = str_pad(rand(100000, 999999), 6, '0', STR_PAD_LEFT);
            $expires_at = date('Y-m-d H:i:s', strtotime('+30 minutes'));
            $stmt = $db->prepare("
                INSERT INTO otp_codes (email, otp_code, expires_at, used, created_at)
                VALUES (?, ?, ?, 0, NOW())
                ON DUPLICATE KEY UPDATE
                otp_code = VALUES(otp_code),
                expires_at = VALUES(expires_at),
                used = 0,
                created_at = NOW()
            ");
            $stmt->execute([$email, $reset_code, $expires_at]);

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

        default:
            echo json_encode([
                "success" => false,
                "error" => "Invalid action"
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
