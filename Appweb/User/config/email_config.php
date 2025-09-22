<?php
// Email Configuration for Password Reset
// Configure your email settings here

class EmailConfig {
    // SMTP Configuration
    public static $smtp_host = 'smtp.gmail.com'; // Change to your SMTP server
    public static $smtp_port = 587; // Usually 587 for TLS, 465 for SSL
    public static $smtp_username = 'Cephra.Industries@gmail.com'; // Your email address
    public static $smtp_password = 'ebttkfyqaloxliag'; // Your app password (not regular password)
    public static $smtp_encryption = 'tls'; // tls or ssl

    // Email Content Configuration
    public static $from_email = 'Cephra.Industries@gmail.com';
    public static $from_name = 'Cephra Support';
    public static $reply_to_email = 'support@cephra.com';

    // Email Templates
    public static function getResetEmailTemplate($email, $reset_code) {
        $subject = 'Password Reset Code - Cephra';

        $message = "
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset='UTF-8'>
            <meta name='viewport' content='width=device-width, initial-scale=1.0'>
            <title>Password Reset</title>
            <style>
                body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                .header { background: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                .content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }
                .code { font-size: 32px; font-weight: bold; color: #00c2ce; text-align: center; letter-spacing: 8px; background: white; padding: 20px; border-radius: 8px; margin: 20px 0; border: 2px dashed #00c2ce; }
                .footer { text-align: center; margin-top: 20px; color: #666; font-size: 14px; }
                .brand { font-size: 24px; font-weight: bold; color: #00c2ce; }
            </style>
        </head>
        <body>
            <div class='container'>
                <div class='header'>
                    <div class='brand'>CEPHRA</div>
                    <h1>Password Reset Request</h1>
                </div>
                <div class='content'>
                    <p>Hello,</p>
                    <p>You have requested to reset your password for your Cephra account. Please use the verification code below to complete the process:</p>

                    <div class='code'>{$reset_code}</div>

                    <p><strong>Security Notes:</strong></p>
                    <ul>
                        <li>This code will expire in 30 minutes</li>
                        <li>If you didn't request this reset, please ignore this email</li>
                        <li>For security reasons, never share this code with anyone</li>
                    </ul>

                    <p>If you're having trouble, please contact our support team.</p>

                    <p>Best regards,<br>The Cephra Team</p>
                </div>
                <div class='footer'>
                    <p>This is an automated message. Please do not reply to this email.</p>
                    <p>&copy; 2024 Cephra. All rights reserved.</p>
                </div>
            </div>
        </body>
        </html>
        ";

        return [
            'subject' => $subject,
            'message' => $message,
            'headers' => [
                'MIME-Version: 1.0',
                'Content-type: text/html; charset=UTF-8',
                'From: ' . self::$from_name . ' <' . self::$from_email . '>',
                'Reply-To: ' . self::$reply_to_email
            ]
        ];
    }
}

// Example usage in your forgot_password.php API:
/*
require_once "email_config.php";

$emailTemplate = EmailConfig::getResetEmailTemplate($user_email, $reset_code);

// Send email using PHP mail() function
$headers = implode("\r\n", $emailTemplate['headers']);
$mailSent = mail($user_email, $emailTemplate['subject'], $emailTemplate['message'], $headers);

if ($mailSent) {
    // Email sent successfully
} else {
    // Email failed to send
}
*/
?>
