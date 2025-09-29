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
    <title>Privacy Policy - Cephra</title>
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

        .privacy-hero {
            background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
            padding: 120px 0 80px;
            text-align: center;
            position: relative;
            overflow: hidden;
        }

        .privacy-hero::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: radial-gradient(circle at 20% 80%, rgba(0, 194, 206, 0.15) 0%, transparent 50%);
            z-index: 1;
        }

        .privacy-hero-content {
            position: relative;
            z-index: 2;
        }

        .privacy-content {
            padding: 80px 0;
            background: white;
        }

        .privacy-section {
            margin-bottom: 3rem;
        }

        .privacy-section h2 {
            font-size: 1.8rem;
            font-weight: 600;
            color: var(--text-primary);
            margin-bottom: 1rem;
            border-bottom: 2px solid var(--primary-color);
            padding-bottom: 0.5rem;
        }

        .privacy-section p {
            color: var(--text-secondary);
            line-height: 1.6;
            margin-bottom: 1rem;
        }

        .privacy-section ul {
            margin-left: 1.5rem;
            color: var(--text-secondary);
            line-height: 1.6;
        }

        .privacy-section li {
            margin-bottom: 0.5rem;
        }

        @media (max-width: 768px) {
            .privacy-hero {
                padding: 100px 0 60px;
            }
        }
    </style>
</head>
<body>
    <!-- Header -->
    <?php include __DIR__ . '/partials/header.php'; ?>

    <!-- Privacy Hero -->
    <section class="privacy-hero">
        <div class="container">
            <div class="privacy-hero-content">
                <h1 style="font-size: clamp(2.5rem, 6vw, 4rem); font-weight: 900; margin-bottom: 1rem; color: var(--text-primary);">Privacy Policy</h1>
                <p style="font-size: 1.3rem; color: var(--text-secondary); max-width: 700px; margin: 0 auto 2rem;">Your privacy is important to us. Please read our policy carefully.</p>
            </div>
        </div>
    </section>

    <!-- Privacy Content -->
    <section class="privacy-content">
        <div class="container">
            <div class="privacy-section">
                <h2>1. Information We Collect</h2>
                <p>We collect information to provide better services to our users. This includes:</p>
                <ul>
                    <li>Personal information such as name, email address, and contact details</li>
                    <li>Usage data including how you interact with our services</li>
                    <li>Device information and IP addresses</li>
                </ul>
            </div>

            <div class="privacy-section">
                <h2>2. How We Use Information</h2>
                <p>We use the information we collect to:</p>
                <ul>
                    <li>Provide, maintain, and improve our services</li>
                    <li>Communicate with you about your account and updates</li>
                    <li>Personalize your experience</li>
                    <li>Protect against fraud and unauthorized access</li>
                </ul>
            </div>

            <div class="privacy-section">
                <h2>3. Sharing of Information</h2>
                <p>We do not sell your personal information. We may share information with:</p>
                <ul>
                    <li>Service providers who help us operate our services</li>
                    <li>Law enforcement or legal requests as required by law</li>
                </ul>
            </div>

            <div class="privacy-section">
                <h2>4. Data Security</h2>
                <p>We implement reasonable security measures to protect your information from unauthorized access, alteration, disclosure, or destruction.</p>
            </div>

            <div class="privacy-section">
                <h2>5. Your Choices</h2>
                <p>You can manage your account settings and opt out of certain communications. Please contact us if you wish to delete your account.</p>
            </div>

            <div class="privacy-section">
                <h2>6. Cookies and Tracking</h2>
                <p>We use cookies and similar technologies to enhance your experience. You can control cookie preferences through your browser settings.</p>
            </div>

            <div class="privacy-section">
                <h2>7. Children's Privacy</h2>
                <p>Our services are not intended for children under 13. We do not knowingly collect personal information from children under 13.</p>
            </div>

            <div class="privacy-section">
                <h2>8. Changes to This Policy</h2>
                <p>We may update this Privacy Policy from time to time. We will notify you of any changes by posting the new policy on this page.</p>
            </div>

            <div class="privacy-section">
                <h2>9. Contact Us</h2>
                <p>If you have any questions about this Privacy Policy, please contact us at privacy@cephra.com.</p>
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
