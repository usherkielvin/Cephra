<?php
session_start();
if (!isset($_SESSION['username'])) {
    header("Location: index.php");
    exit();
}
require_once 'config/database.php';
$db = new Database();
$conn = $db->getConnection();
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Terms of Service - Cephra</title>
    <link rel="icon" type="image/png" href="images/logo.png?v=2" />
    <link rel="apple-touch-icon" href="images/logo.png?v=2" />
    <link rel="manifest" href="manifest.webmanifest" />
    <meta name="theme-color" content="#1a1a2e" />
    <link rel="stylesheet" href="css/vantage-style.css" />
    <link rel="stylesheet" href="assets/css/fontawesome-all.min.css" />
    <style>
        :root {
            --primary-color: #00c2ce;
            --primary-dark: #0e3a49;
            --secondary-color: #f8fafc;
            --accent-color: #e2e8f0;
            --text-primary: #1a202c;
            --text-secondary: rgba(26, 32, 44, 0.8);
            --text-muted: rgba(26, 32, 44, 0.6);
            --bg-primary: #ffffff;
            --bg-secondary: #f8fafc;
            --bg-card: #ffffff;
            --border-color: rgba(26, 32, 44, 0.1);
            --shadow-light: rgba(0, 194, 206, 0.1);
            --shadow-medium: rgba(0, 194, 206, 0.2);
            --shadow-strong: rgba(0, 194, 206, 0.3);
        }

        .header {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            width: 100vw;
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(20px);
            border-bottom: 1px solid var(--border-color);
            z-index: 1000;
            transition: all 0.3s ease;
        }

        .header.scrolled {
            background: rgba(255, 255, 255, 0.98);
            box-shadow: 0 2px 20px rgba(0, 0, 0, 0.15);
        }

        .terms-hero {
            background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
            padding: 120px 0 80px;
            text-align: center;
            position: relative;
            overflow: hidden;
        }

        .terms-hero::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: radial-gradient(circle at 20% 80%, rgba(0, 194, 206, 0.15) 0%, transparent 50%);
            z-index: 1;
        }

        .terms-hero-content {
            position: relative;
            z-index: 2;
        }

        .terms-content {
            padding: 80px 0;
            background: white;
        }

        .terms-section {
            margin-bottom: 3rem;
        }

        .terms-section h2 {
            font-size: 1.8rem;
            font-weight: 600;
            color: var(--text-primary);
            margin-bottom: 1rem;
            border-bottom: 2px solid var(--primary-color);
            padding-bottom: 0.5rem;
        }

        .terms-section p {
            color: var(--text-secondary);
            line-height: 1.6;
            margin-bottom: 1rem;
        }

        .terms-section ul {
            margin-left: 1.5rem;
            color: var(--text-secondary);
            line-height: 1.6;
        }

        .terms-section li {
            margin-bottom: 0.5rem;
        }

        @media (max-width: 768px) {
            .terms-hero {
                padding: 100px 0 60px;
            }
        }
    </style>
</head>
<body>
    <!-- Header -->
    <?php include __DIR__ . '/partials/header.php'; ?>

    <!-- Terms Hero -->
    <section class="terms-hero">
        <div class="container">
            <div class="terms-hero-content">
                <h1 style="font-size: clamp(2.5rem, 6vw, 4rem); font-weight: 900; margin-bottom: 1rem; color: var(--text-primary);">Terms of Service</h1>
                <p style="font-size: 1.3rem; color: var(--text-secondary); max-width: 700px; margin: 0 auto 2rem;">Please read these terms carefully before using Cephra's services.</p>
            </div>
        </div>
    </section>

    <!-- Terms Content -->
    <section class="terms-content">
        <div class="container">
            <div class="terms-section">
                <h2>1. Introduction</h2>
                <p>Welcome to Cephra, an electric vehicle charging platform. These Terms of Service ("Terms") govern your use of our website, mobile application, and related services (collectively, the "Service"). By accessing or using the Service, you agree to be bound by these Terms.</p>
            </div>

            <div class="terms-section">
                <h2>2. Acceptance of Terms</h2>
                <p>By creating an account or using the Service, you acknowledge that you have read, understood, and agree to be bound by these Terms and our Privacy Policy. If you do not agree to these Terms, please do not use the Service.</p>
            </div>

            <div class="terms-section">
                <h2>3. Use of Service</h2>
                <p>You may use the Service only for lawful purposes and in accordance with these Terms. You agree not to:</p>
                <ul>
                    <li>Use the Service in any way that violates applicable laws or regulations</li>
                    <li>Attempt to gain unauthorized access to our systems</li>
                    <li>Interfere with or disrupt the Service</li>
                    <li>Use the Service for any commercial purpose without our written consent</li>
                </ul>
            </div>

            <div class="terms-section">
                <h2>4. User Accounts</h2>
                <p>To use certain features of the Service, you must create an account. You are responsible for maintaining the confidentiality of your account credentials and for all activities that occur under your account. You agree to provide accurate and complete information when creating your account.</p>
            </div>

            <div class="terms-section">
                <h2>5. Payment and Billing</h2>
                <p>Some services may require payment. All fees are non-refundable unless otherwise specified. You agree to pay all charges associated with your use of the Service. We reserve the right to change our pricing at any time.</p>
            </div>

            <div class="terms-section">
                <h2>6. Intellectual Property</h2>
                <p>The Service and its original content, features, and functionality are owned by Cephra and are protected by copyright, trademark, and other intellectual property laws. You may not reproduce, distribute, or create derivative works without our written consent.</p>
            </div>

            <div class="terms-section">
                <h2>7. Limitation of Liability</h2>
                <p>To the maximum extent permitted by law, Cephra shall not be liable for any indirect, incidental, special, or consequential damages arising out of or in connection with your use of the Service.</p>
            </div>

            <div class="terms-section">
                <h2>8. Termination</h2>
                <p>We reserve the right to terminate or suspend your account and access to the Service at our sole discretion, without prior notice, for conduct that we believe violates these Terms or is harmful to other users, us, or third parties.</p>
            </div>

            <div class="terms-section">
                <h2>9. Governing Law</h2>
                <p>These Terms shall be governed by and construed in accordance with the laws of the Philippines, without regard to its conflict of law provisions.</p>
            </div>

            <div class="terms-section">
                <h2>10. Changes to Terms</h2>
                <p>We reserve the right to modify these Terms at any time. We will notify users of any changes by posting the new Terms on this page. Your continued use of the Service after such changes constitutes your acceptance of the new Terms.</p>
            </div>

            <div class="terms-section">
                <h2>11. Contact Information</h2>
                <p>If you have any questions about these Terms, please contact us at support@cephra.com.</p>
            </div>

            <p style="text-align: center; margin-top: 3rem; color: var(--text-muted);">Last updated: September 2025</p>
        </div>
    </section>

    <!-- Footer -->
    <?php include __DIR__ . '/partials/footer.php'; ?>

    <script src="assets/js/jquery.min.js"></script>
    <script src="assets/js/jquery.dropotron.min.js"></script>
    <script src="assets/js/browser.min.js"></script>
    <script src="assets/js/breakpoints.min.js"></script>
    <script src="assets/js/util.js"></script>
    <script src="assets/js/main.js"></script>

    <script>
        // Add scroll effect to header
        window.addEventListener('scroll', function() {
            const header = document.querySelector('.header');
            if (window.scrollY > 100) {
                header.classList.add('scrolled');
            } else {
                header.classList.remove('scrolled');
            }
        });
    </script>
</body>
</html>
