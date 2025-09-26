<?php
session_start();
if (!isset($_SESSION['username'])) {
    header("Location: index.php");
    exit();
}
require_once 'config/database.php';
$db = new Database();
$conn = $db->getConnection();
if ($conn) {
    $username = $_SESSION['username'];
    $stmt = $conn->prepare("SELECT firstname FROM users WHERE username = :username");
    $stmt->bindParam(':username', $username);
    $stmt->execute();
    $user = $stmt->fetch(PDO::FETCH_ASSOC);
$firstname = $user ? $user['firstname'] : 'User';

echo "<!-- DEBUG: Session username: " . htmlspecialchars($_SESSION['username']) . " -->";
echo "<!-- DEBUG: Fetched firstname: " . htmlspecialchars($firstname) . " -->";

} else {
    $firstname = 'User';
}
?>
<!DOCTYPE HTML>
<!--
	ZeroFour by HTML5 UP
	html5up.net | @ajlkn
	Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)
-->
<html>
	<head>
		<title>Cephra</title>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
		<link rel="icon" type="image/png" href="images/logo.png?v=2" />
		<link rel="apple-touch-icon" href="images/logo.png?v=2" />
		<link rel="manifest" href="manifest.webmanifest" />
		<meta name="theme-color" content="#1a1a2e" />
	<link rel="stylesheet" href="css/vantage-style.css" />
	<link rel="stylesheet" href="assets/css/fontawesome-all.min.css" />
	</head>
	<body>
    <!-- Header -->
    <header class="header">
        <div class="container">
                <div class="header-content">
						<!-- Logo -->
						<div class="logo">
							<img src="images/logo.png" alt="Cephra" class="logo-img">
							<span class="logo-text">CEPHRA</span>
						</div>

						<!-- Navigation -->
						<nav class="nav">
							<ul class="nav-list">
								<li><a href="dashboard.php" class="nav-link">Dashboard</a></li>
								<li><a href="link.php" class="nav-link">Link</a></li>
								<li><a href="history.php" class="nav-link">History</a></li>
								<li class="current_page_item"><a href="profile.php" class="nav-link">Profile</a></li>
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

        <!-- Mobile Menu -->
        <div class="mobile-menu" id="mobileMenu">
            <div class="mobile-menu-content">
                <!-- Mobile Navigation -->
                <div class="mobile-nav">
                    <ul class="mobile-nav-list">
                        <li class="mobile-nav-item">
                            <a href="dashboard.php" class="mobile-nav-link current">Home</a>
                        </li>
                        <li class="mobile-nav-item">
                            <a href="link.php" class="mobile-nav-link">Link</a>
                        </li>
                        <li class="mobile-nav-item">
                            <a href="history.php" class="mobile-nav-link">History</a>
                        </li>
                        <li class="mobile-nav-item">
                            <a href="profile.php" class="mobile-nav-link">Profile</a>
                        </li>
                    </ul>
                </div>

                <!-- Mobile User Actions -->
                <div class="mobile-header-actions">
                    <a href="profile.php" class="nav-link auth-link">Profile</a>
                    <a href="profile_logout.php" class="nav-link auth-link">Logout</a>
                </div>
            </div>
        </div>
    </header>

    <!-- Charging Section -->
    <section class="charging-types">
        <div class="container">
            <div class="section-header">
                <h2 class="section-title">Choose Your Charging Speed</h2>
                <p class="section-description">Select the perfect charging option for your needs</p>
            </div>

            <div class="charging-grid">
                <!-- Normal Charging Card -->
                <div class="charging-card">
                    <div class="charging-icon">
                        <i class="fas fa-battery-half"></i>
                    </div>
                    <h3 class="charging-title">Normal Charging</h3>
                    <p class="charging-description">
                        Perfect for regular charging when you're not in a rush. Ideal for everyday use,
                        overnight charging at home, or during extended parking periods. Provides
                        steady, reliable charging without putting stress on your vehicle's battery.
                    </p>
                    <div class="charging-specs">
                        <span class="spec-item">3-7 kW</span>
                        <span class="spec-item">2-3 hours</span>
                    </div>
                    <a href="#" id="normalChargeBtn" class="charging-link" onclick="processChargeRequest('Normal Charging'); return false;">Start Normal Charging →</a>
                </div>

                <!-- Fast Charging Card -->
                <div class="charging-card">
                    <div class="charging-icon">
                        <i class="fas fa-bolt"></i>
                    </div>
                    <h3 class="charging-title">Fast Charging</h3>
                    <p class="charging-description">
                        When time is of the essence, fast charging delivers rapid power
                        to get you back on the road quickly. Perfect for lunch breaks,
                        shopping stops, or quick top-ups during long drives. Our advanced
                        fast charging technology provides optimal charging curves to maximize
                        efficiency while protecting your battery health.
                    </p>
                    <div class="charging-specs">
                        <span class="spec-item">50-150 kW</span>
                        <span class="spec-item">20-40 minutes</span>
                    </div>
                    <a href="#" id="fastChargeBtn" class="charging-link" onclick="processChargeRequest('Fast Charging'); return false;">Start Fast Charging →</a>
                </div>
            </div>
        </div>
    </section>

    <!-- Footer -->
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
                        <li><a href="link.php">Link Device</a></li>
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
                    <h4 class="footer-title">Account</h4>
                    <ul class="footer-links">
                        <li><a href="profile.php">Profile</a></li>
                        <li><a href="profile_logout.php">Logout</a></li>
                    </ul>
                </div>
            </div>

            <div class="footer-bottom">
                <p>&copy; 2025 Cephra. All rights reserved. | <a href="#privacy">Privacy Policy</a> | <a href="#terms">Terms of Service</a></p>
            </div>
        </div>
    </footer>

    <script>
        // Mobile menu toggle
        document.getElementById('mobileMenuToggle').addEventListener('click', function() {
            const mobileMenu = document.getElementById('mobileMenu');
            mobileMenu.classList.toggle('mobile-menu-open');
            this.classList.toggle('active');
        });

        // Smooth scrolling for anchor links
        document.querySelectorAll('a[href^="#"]').forEach(anchor => {
            anchor.addEventListener('click', function (e) {
                e.preventDefault();
                const target = document.querySelector(this.getAttribute('href'));
                if (target) {
                    target.scrollIntoView({
                        behavior: 'smooth',
                        block: 'start'
                    });
                }
            });
        });

        // Add scroll effect to header
        window.addEventListener('scroll', function() {
            const header = document.querySelector('.header');
            if (window.scrollY > 100) {
                header.classList.add('scrolled');
            } else {
                header.classList.remove('scrolled');
            }
        });

        // Add animation on scroll for charging cards
        const observerOptions = {
            threshold: 0.1,
            rootMargin: '0px 0px -50px 0px'
        };

        const observer = new IntersectionObserver(function(entries) {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('animate-in');
                }
            });
        }, observerOptions);

        document.querySelectorAll('.charging-card').forEach(card => {
            observer.observe(card);
        });

        // Close mobile menu when clicking on mobile nav links
        document.querySelectorAll('.mobile-nav-link').forEach(link => {
            link.addEventListener('click', function() {
                const mobileMenu = document.getElementById('mobileMenu');
                const mobileMenuToggle = document.getElementById('mobileMenuToggle');
                mobileMenu.classList.remove('mobile-menu-open');
                mobileMenuToggle.classList.remove('active');
            });
        });

        // Charging functionality
        function showDialog(title, message) {
            const overlay = document.createElement('div');
            overlay.style.cssText = 'position:fixed;inset:0;background:rgba(0,0,0,0.6);display:flex;align-items:center;justify-content:center;z-index:10000;padding:16px;';
            const dialog = document.createElement('div');
            dialog.style.cssText = 'width:100%;max-width:360px;background:#fff;border-radius:12px;box-shadow:0 10px 30px rgba(0,0,0,0.25);overflow:hidden;';
            const header = document.createElement('div');
            header.style.cssText = 'background:#00c2ce;color:#fff;padding:12px 16px;font-weight:700';
            header.textContent = title || 'Notice';
            const body = document.createElement('div');
            body.style.cssText = 'padding:16px;color:#333;line-height:1.5;';
            body.textContent = message || '';
            const footer = document.createElement('div');
            footer.style.cssText = 'padding:12px 16px;display:flex;justify-content:flex-end;gap:8px;background:#f7f7f7;';
            const ok = document.createElement('button');
            ok.textContent = 'OK';
            ok.style.cssText = 'background:#00c2ce;color:#fff;border:0;padding:8px 14px;border-radius:8px;cursor:pointer;';
            ok.onclick = () => document.body.removeChild(overlay);
            footer.appendChild(ok);
            dialog.appendChild(header);
            dialog.appendChild(body);
            dialog.appendChild(footer);
            overlay.appendChild(dialog);
            document.body.appendChild(overlay);
        }

        function processChargeRequest(serviceType) {
            // Force exact service type strings expected by backend
            let serviceTypeMapped = '';
            if (serviceType === 'Normal Charging' || serviceType === 'normal charging') {
                serviceTypeMapped = 'Normal Charging';
            } else if (serviceType === 'Fast Charging' || serviceType === 'fast charging') {
                serviceTypeMapped = 'Fast Charging';
            } else {
                serviceTypeMapped = serviceType; // fallback
            }

            // Disable links during processing
            document.querySelectorAll('.charging-link').forEach(link => link.style.pointerEvents = 'none');

            fetch('charge_action.php', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'serviceType=' + encodeURIComponent(serviceTypeMapped)
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    showQueueTicketProceedPopup(data);
                } else if (data.error) {
                    showDialog('Charging', data.error);
                }
            })
            .catch(error => {
                showDialog('Charging', 'An error occurred while processing your request. Please try again.');
                console.error('Error:', error);
            })
            .finally(() => {
                // Re-enable links
                document.querySelectorAll('.charging-link').forEach(link => link.style.pointerEvents = 'auto');
            });
        }

        function showQueueTicketProceedPopup(response) {
            if (response.success) {
                var ticketId = response.ticketId;
                var serviceType = response.serviceType;
                var batteryLevel = response.batteryLevel;

                // Create popup HTML (QueueTicketProceed)
                var popupHtml = '<div id="queuePopup" style="position: fixed; top: 20%; left: 50%; transform: translate(-50%, -20%); background: white; border: 2px solid #007bff; border-radius: 10px; padding: 20px; width: 320px; z-index: 10000; box-shadow: 0 0 10px rgba(0,0,0,0.5);">';
                popupHtml += '<h2 style="margin-top: 0; color: #007bff; text-align: center;">Queue Ticket Proceed</h2>';
                popupHtml += '<div style="margin: 10px 0; font-size: 16px; text-align: center;"><strong>Ticket ID:</strong> ' + ticketId + '</div>';
                popupHtml += '<div style="margin: 10px 0; font-size: 16px; text-align: center;"><strong>Service:</strong> ' + serviceType + '</div>';
                popupHtml += '<div style="margin: 10px 0; font-size: 16px; text-align: center;"><strong>Battery Level:</strong> ' + batteryLevel + '%</div>';
                popupHtml += '<div style="margin: 10px 0; font-size: 16px; text-align: center;"><strong>Estimated Wait Time:</strong> 5 minutes</div>';
                popupHtml += '<div style="display:flex;gap:10px;justify-content:center;margin-top:12px;">';
                popupHtml += '<button onclick="closePopup()" style="padding: 10px 16px; background: #00a389; color: white; border: none; border-radius: 6px; cursor: pointer;">OK</button>';
                popupHtml += '</div>';
                popupHtml += '</div>';

                // Append to body
                document.body.insertAdjacentHTML('beforeend', popupHtml);
            }
        }

        // Function to close popup (defined globally)
        window.closePopup = function() {
            const popup = document.getElementById('queuePopup');
            if (popup) popup.remove();
        };
    </script>
</body>
</html>
