<?php
// Forgot Password API with PHPMailer for sending email

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
                    "success" => true,
                    "message" => "If the email exists, a reset code has been sent"
                ]);
                break;
            }

            // Generate reset code and expiration
            $reset_code = str_pad(rand(100000, 999999), 6, '0', STR_PAD_LEFT);
            $expires_at = date('Y-m-d H:i:s', strtotime('+30 minutes'));

            // Insert or update password_reset_tokens table
            $stmt = $db->prepare("
                INSERT INTO password_reset_tokens (email, reset_code, expires_at, used)
                VALUES (?, ?, ?, 0)
                ON DUPLICATE KEY UPDATE reset_code = VALUES(reset_code), expires_at = VALUES(expires_at), used = 0
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

        // Add other cases like verify-code, reset-password, resend-code as needed

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
