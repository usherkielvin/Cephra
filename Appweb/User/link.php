<?php
session_start();
if (!isset($_SESSION['username'])) {
    header("Location: index.php");
    exit();
}

require_once 'config/database.php';

$db = new Database();
$conn = $db->getConnection();

$username = $_SESSION['username'];

// Check car_index from users table
$carIndex = null;
if ($conn) {
    $stmt = $conn->prepare("SELECT car_index FROM users WHERE username = :username");
    $stmt->bindParam(':username', $username);
    $stmt->execute();
    $result = $stmt->fetch(PDO::FETCH_ASSOC);
    $carIndex = $result['car_index'] ?? null;
}

// Handle form submissions
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (isset($_POST['link_car'])) {
        // Link car functionality
        if ($conn) {
            // Generate random car_index between 1-10
            $newCarIndex = rand(1, 10);
            $stmt = $conn->prepare("UPDATE users SET car_index = :car_index WHERE username = :username");
            $stmt->bindParam(':car_index', $newCarIndex);
            $stmt->bindParam(':username', $username);
            if ($stmt->execute()) {
                $_SESSION['car_linked'] = true;
                header("Location: link.php"); // Refresh to show "With Car"
                exit();
            } else {
                $error = "Failed to link car. Please try again.";
            }
        }
    }
}
?>
<!DOCTYPE HTML>
<html>
<head>
    <title>Link Your Car - Cephra</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="icon" type="image/png" href="images/logo.png?v=2" />
    <link rel="apple-touch-icon" href="images/logo.png?v=2" />
    <link rel="manifest" href="manifest.webmanifest" />
    <meta name="theme-color" content="#062635" />
    <link rel="stylesheet" href="css/vantage-style.css" />
    <link rel="stylesheet" href="assets/css/fontawesome-all.min.css" />
    <link rel="stylesheet" href="assets/css/pages/link.css" />
</head>
<body class="homepage is-preload">
    <div id="page-wrapper">
        <header class="header">
            <div class="container">
                <div class="header-content">
                    <!-- Logo -->
                    <div class="logo">
                        <img src="images/logo.png" alt="Cephra" class="logo-img" />
                        <span class="logo-text">CEPHRA</span>
                    </div>

                    <!-- Navigation -->
                    <nav class="nav">
                        <ul class="nav-list">
                            <li><a href="dashboard.php" class="nav-link">Dashboard</a></li>
                            <li class="current_page_item"><a href="link.php" class="nav-link">Link</a></li>
                            <li><a href="history.php" class="nav-link">History</a></li>
                            <li><a href="profile.php" class="nav-link">Profile</a></li>
                        </ul>
                    </nav>

                    <!-- Header Actions -->
                    <div class="header-actions">
                        <div class="auth-buttons">
                            <a href="dashboard.php" class="nav-link auth-link">Back</a>
                        </div>
                    </div>

                    <!-- Mobile Menu Toggle -->
                    <button class="mobile-menu-toggle" id="mobileMenuToggle">
                        <span></span>
                        <span></span>
                        <span></span>
                    </button>
                </div>
            </div>
        </header>

        <!-- Link Section -->
        <section class="link-section" style="padding: 100px 0; background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);">
            <div class="container">
                <div class="section-header" style="text-align: center; margin-bottom: 60px;">
                    <h2 class="section-title" style="font-size: 2.5rem; font-weight: 700; margin-bottom: 1rem; background: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text;">Link Your Car</h2>
                    <p class="section-description" style="font-size: 1.2rem; color: rgba(26, 32, 44, 0.8); max-width: 600px; margin: 0 auto;">Connect your electric vehicle to start charging at Cephra stations</p>
                </div>

                <div class="link-container" style="background: white; border-radius: 20px; padding: 2rem; border: 1px solid rgba(26, 32, 44, 0.1); box-shadow: 0 5px 15px rgba(0, 194, 206, 0.1); max-width: 600px; margin: 0 auto;">
                    <?php if (is_null($carIndex) || $carIndex == 0): ?>
                        <!-- No Car Design -->
                        <h3 style="text-align: center; margin-bottom: 1rem; color: #1a202c;">Link Your Electric Vehicle</h3>
                        <p style="text-align: center; margin-bottom: 2rem; color: rgba(26, 32, 44, 0.7);">Connect your EV to start charging at Cephra stations</p>
                        <img src="images/ConnectCar.gif" alt="Connect Car" style="max-width: 100%; height: auto; margin: 20px 0; border-radius:8px; display: block; margin-left: auto; margin-right: auto;">
                        <form method="post" id="linkForm">
                            <div class="terms-checkbox" style="margin-bottom: 1rem;">
                                <input type="checkbox" id="terms" name="terms" required>
                                <label for="terms">By linking, I agree to the <a href="#" onclick="showTerms(); return false;">Terms & Conditions</a></label>
                            </div>
                            <?php if (isset($error)): ?>
                                <div class="error-message" style="color: red; margin-bottom: 1rem;"><?php echo htmlspecialchars($error); ?></div>
                            <?php endif; ?>
                            <button type="submit" name="link_car" class="link-button" id="linkBtn" disabled style="width: 100%; padding: 0.75rem; background: #cccccc; color: white; border: none; border-radius: 8px; cursor: not-allowed; transition: all 0.3s ease;">
                                Link My Car
                            </button>
                        </form>
                    <?php else: ?>
                        <!-- With Car Design -->
                        <h3 style="text-align: center; margin-bottom: 1rem; color: #1a202c;">Your Vehicle is Linked</h3>
                        <p style="text-align: center; margin-bottom: 2rem; color: rgba(26, 32, 44, 0.7);">Porsche connected. You're ready to charge.</p>
                        <img src="images/ads.png" alt="Porsche" style="max-width: 100%; height: auto; margin: 20px 0; border-radius:8px; display: block; margin-left: auto; margin-right: auto;">
                        <div class="car-details" style="margin-top: 2rem;">
                            <div class="detail-row" style="margin-bottom: 1rem;">
                                <span class="detail-label">Car Model:</span>
                                <span class="detail-value">Porsche <?php echo $carIndex; ?></span>
                            </div>
                            <div class="detail-row" style="margin-bottom: 1rem;">
                                <span class="detail-label">Performance:</span>
                                <span class="detail-value"><?php echo rand(200, 400); ?> HP</span>
                            </div>
                            <div class="detail-row" style="margin-bottom: 1rem;">
                                <span class="detail-label">Kms Remaining:</span>
                                <span class="detail-value"><?php echo rand(50, 300); ?> km</span>
                            </div>
                            <div class="detail-row" style="margin-bottom: 1rem;">
                                <span class="detail-label">Time to Charge:</span>
                                <span class="detail-value"><?php echo rand(20, 60); ?> minutes</span>
                            </div>
                            <div class="detail-row" style="margin-bottom: 1rem;">
                                <span class="detail-label">Battery Level:</span>
                                <div class="progress-bar" style="width: 100%; background: #e0e0e0; border-radius: 10px; overflow: hidden;">
                                    <div class="progress-fill" style="height: 20px; background: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%); width: <?php echo rand(20, 100); ?>%; transition: width 0.3s ease;"></div>
                                </div>
                                <span class="progress-text" style="display: block; text-align: center; margin-top: 0.5rem;"><?php echo rand(20, 100); ?>%</span>
                            </div>
                        </div>
                        <div style="display:flex; gap:12px; justify-content:center; margin-top: 2rem;">
                            <a href="dashboard.php" class="button">Go to Dashboard</a>
                            <a href="ChargingPage.php" class="button alt">Start Charging</a>
                        </div>
                    <?php endif; ?>
                </div>
            </div>
        </section>

        <footer class="footer">
            <div class="container">
                <div class="footer-content">
                    <div class="footer-section">
                        <div class="footer-logo">
                            <img src="images/logo.png" alt="Cephra" class="footer-logo-img" />
                            <span class="footer-logo-text">CEPHRA</span>
                        </div>
                        <p class="footer-description">
                            Your ultimate electric vehicle charging platform,
                            powering the future of sustainable transportation.
                        </p>
                    </div>

                    <div class="footer-section">
                        <h4 class="footer-title">Platform</h4>
                        <ul class="footer-links">
                            <li><a href="dashboard.php">Dashboard</a></li>
                            <li><a href="ChargingPage.php">Charging</a></li>
                            <li><a href="history.php">History</a></li>
                        </ul>
                    </div>

                    <div class="footer-section">
                        <h4 class="footer-title">Support</h4>
                        <ul class="footer-links">
                            <li><a href="help_center.php">Help Center</a></li>
                            <li><a href="contact_us.php">Contact Us</a></li>
                        </ul>
                    </div>

                    <div class="footer-section">
                        <h4 class="footer-title">Company</h4>
                        <ul class="footer-links">
                            <li><a href="about_us.php">About Us</a></li>
                            <li><a href="our_team.php">Our Team</a></li>
                        </ul>
                    </div>
                </div>

                <div class="footer-bottom">
                    <p>&copy; 2025 Cephra. All rights reserved. | <a href="#privacy">Privacy Policy</a> | <a href="#terms">Terms of Service</a></p>
                </div>
            </div>
        </footer>
    </div>

    <!-- Scripts -->
    <script src="assets/js/jquery.min.js"></script>
    <script src="assets/js/jquery.dropotron.min.js"></script>
    <script src="assets/js/browser.min.js"></script>
    <script src="assets/js/breakpoints.min.js"></script>
    <script src="assets/js/util.js"></script>
    <script src="assets/js/main.js"></script>

    <script>
        // Mobile menu toggle
        document.getElementById('mobileMenuToggle').addEventListener('click', function() {
            const nav = document.querySelector('.nav');
            nav.classList.toggle('mobile-menu-open');
            this.classList.toggle('active');
        });

        // Enable/disable button based on terms checkbox
        document.getElementById('terms').addEventListener('change', function() {
            const linkBtn = document.getElementById('linkBtn');
            if (this.checked) {
                linkBtn.disabled = false;
                linkBtn.style.background = '#4CAF50';
                linkBtn.style.cursor = 'pointer';
            } else {
                linkBtn.disabled = true;
                linkBtn.style.background = '#cccccc';
                linkBtn.style.cursor = 'not-allowed';
            }
        });

        function showTerms() {
            const termsText = `CEPHRA EV LINKING TERMS AND CONDITIONS
Effective Date: <?php echo date('Y-m-d'); ?>
Version: 1.0

1. ACCEPTANCE OF TERMS
By linking your electric vehicle (EV) to the Cephra app (the "Service"), you agree to these Terms.

2. LINKING PURPOSE
Linking enables ticketing, charging session history, and status updates within the app.

3. DATA COLLECTED
Vehicle identifiers, session timestamps, kWh consumed, payment references, and diagnostic status necessary for Service delivery.

4. USER RESPONSIBILITIES
You confirm you are authorized to link the vehicle and will keep your account secure.

5. CONSENT TO COMMUNICATIONS
You consent to in-app notifications and transactional emails about charging and tickets.

6. PRIVACY
We process data per our Privacy Policy. Data is retained only as long as needed for the Service.

7. LIMITATIONS
The Service provides information "as is" and availability may vary by station and network conditions.

8. SECURITY
We employ reasonable safeguards, but you acknowledge inherent risks in networked systems.

9. UNLINKING
You may unlink your vehicle at any time from the app; some records may be retained for compliance.

10. LIABILITY
To the maximum extent permitted by law, Cephra is not liable for indirect or consequential damages.

11. GOVERNING LAW
These Terms are governed by the laws of Pasay City, Philippines.

12. CONTACT
Cephra Support — support@cephra.com | +63 2 8XXX XXXX

By checking "I agree", you confirm you have read and accept these Terms.`;

            const modal = document.createElement('div');
            modal.style.cssText = `
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0,0,0,0.8);
                display: flex;
                align-items: center;
                justify-content: center;
                z-index: 10000;
            `;

            const modalContent = document.createElement('div');
            modalContent.style.cssText = `
                background: white;
                padding: 20px;
                border-radius: 10px;
                max-width: 600px;
                max-height: 80vh;
                overflow-y: auto;
                position: relative;
            `;

            modalContent.innerHTML = `
                <h3>Terms and Conditions</h3>
                <pre style="white-space: pre-wrap; font-family: inherit; font-size: 14px; line-height: 1.5;">${termsText}</pre>
                <button onclick="this.closest('div').parentElement.remove(); document.getElementById('terms').checked = true;" style="margin-top: 20px; padding: 10px 20px; background: #007bff; color: white; border: none; border-radius: 5px; cursor: pointer;">I Agree</button>
            `;

            modal.appendChild(modalContent);
            document.body.appendChild(modal);
        }
    </script>
</body>
</html>
