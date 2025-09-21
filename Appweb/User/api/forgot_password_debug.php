<?php
// Debug script to test PHPMailer SMTP email sending

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

require_once "../config/email_config.php";
require_once "../vendor/autoload.php";

$mail = new PHPMailer(true);

try {
    $mail->SMTPDebug = 2; // Enable verbose debug output
    $mail->isSMTP();
    $mail->Host = EmailConfig::$smtp_host;
    $mail->SMTPAuth = true;
    $mail->Username = EmailConfig::$smtp_username;
    $mail->Password = EmailConfig::$smtp_password;
    $mail->SMTPSecure = EmailConfig::$smtp_encryption;
    $mail->Port = EmailConfig::$smtp_port;

    $mail->setFrom(EmailConfig::$from_email, EmailConfig::$from_name);
    $mail->addAddress(EmailConfig::$smtp_username); // Send test email to self

    $mail->isHTML(true);
    $mail->Subject = 'Test Email from PHPMailer';
    $mail->Body    = '<p>This is a test email sent using PHPMailer SMTP configuration.</p>';

    $mail->send();
    echo "Test email sent successfully";
} catch (Exception $e) {
    echo "Test email could not be sent. Mailer Error: {$mail->ErrorInfo}";
}
?>
