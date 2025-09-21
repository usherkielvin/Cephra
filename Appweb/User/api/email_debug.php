<?php
// Debug script to test PHPMailer SMTP email sending with detailed error output

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

require_once __DIR__ . '/../vendor/autoload.php';
require_once __DIR__ . '/../config/email_config.php';

$mail = new PHPMailer(true);

try {
    //Server settings
    $mail->SMTPDebug = 2;                      // Enable verbose debug output
    $mail->isSMTP();                                            // Send using SMTP
    $mail->Host       = EmailConfig::$smtp_host;               // Set the SMTP server
    $mail->SMTPAuth   = true;                                   // Enable SMTP authentication
    $mail->Username   = EmailConfig::$smtp_username;           // SMTP username
    $mail->Password   = EmailConfig::$smtp_password;           // SMTP password
    $mail->SMTPSecure = EmailConfig::$smtp_encryption;         // Enable TLS encryption; `PHPMailer::ENCRYPTION_SMTPS` also accepted
    $mail->Port       = EmailConfig::$smtp_port;               // TCP port to connect to

    //Recipients
    $mail->setFrom(EmailConfig::$from_email, EmailConfig::$from_name);
    $mail->addAddress('dwayynnee@gmail.com', 'Test Recipient');     // Add a recipient

    // Content
    $mail->isHTML(true);                                  // Set email format to HTML
    $mail->Subject = 'SMTP Email Debug Test';
    $mail->Body    = 'This is a test email to check SMTP configuration and debug output.';

    $mail->send();
    echo json_encode(['success' => true, 'message' => 'Test email sent successfully']);
} catch (Exception $e) {
    echo json_encode(['success' => false, 'error' => "Message could not be sent. Mailer Error: {$mail->ErrorInfo}"]);
}
?>
