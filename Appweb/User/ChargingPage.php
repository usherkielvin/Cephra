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

// Debug comments removed to prevent JSON parsing errors

} else {
    $firstname = 'User';
}

// Handle ticket processing
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['ticketId'])) {
    header('Content-Type: application/json');

    $ticketId = trim($_POST['ticketId']);
    $username = $_SESSION['username'];

    try {
        // Update ticket status to 'processing'
        $stmt = $conn->prepare("UPDATE charging_tickets SET status = 'processing', processed_at = NOW() WHERE ticket_id = :ticketId AND username = :username AND status = 'queued'");
        $stmt->bindParam(':ticketId', $ticketId);
        $stmt->bindParam(':username', $username);
        $stmt->execute();

        if ($stmt->rowCount() > 0) {
            echo json_encode(['success' => true, 'message' => 'Ticket processed successfully']);
        } else {
            echo json_encode(['success' => false, 'error' => 'Ticket not found or already processed']);
        }
    } catch (Exception $e) {
        echo json_encode(['success' => false, 'error' => 'Database error: ' . $e->getMessage()]);
    }

    exit();
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
        
<?php include __DIR__ . '/partials/header.php'; ?>

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
                <p>&copy; 2025 Cephra. All rights reserved.</p>
                <p><a href="privacy_policy.php">Privacy Policy</a> | <a href="terms_of_service.php">Terms of Service</a></p>
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
                const href = this.getAttribute('href');
                if (href && href !== '#') {
                    const target = document.querySelector(href);
                    if (target) {
                        target.scrollIntoView({
                            behavior: 'smooth',
                            block: 'start'
                        });
                    }
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
            // Prevent multiple rapid clicks
            if (window.chargingInProgress) {
                return false;
            }
            window.chargingInProgress = true;

            // Force exact service type strings expected by backend
            let serviceTypeMapped = '';
            if (serviceType === 'Normal Charging' || serviceType === 'normal charging') {
                serviceTypeMapped = 'Normal Charging';
            } else if (serviceType === 'Fast Charging' || serviceType === 'fast charging') {
                serviceTypeMapped = 'Fast Charging';
            } else {
                serviceTypeMapped = serviceType; // fallback
            }

            // Disable all charging buttons immediately
            document.querySelectorAll('.charging-link').forEach(link => {
                link.style.pointerEvents = 'none';
                link.style.opacity = '0.6';
                link.style.cursor = 'not-allowed';
            });

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
                // Reset state and re-enable links
                window.chargingInProgress = false;
                document.querySelectorAll('.charging-link').forEach(link => {
                    link.style.pointerEvents = 'auto';
                    link.style.opacity = '1';
                    link.style.cursor = 'pointer';
                });
            });
        }

        function showQueueTicketProceedPopup(response) {
            if (response.success) {
                var ticketId = response.ticketId;
                var serviceType = response.serviceType;
                var batteryLevel = response.batteryLevel;

                // Determine charging icon based on service type
                var chargingIcon = serviceType === 'Fast Charging' ? 'fas fa-bolt' : 'fas fa-battery-half';
                var chargingColor = serviceType === 'Fast Charging' ? '#ff6b35' : '#00c2ce';

                // Create modal HTML (QueueTicketProceed) with enhanced design
                var modalHtml = '<div id="queueModal" class="modal" style="display: none; position: fixed; z-index: 2000; left: 0; top: 0; width: 100%; height: 100%; background: rgba(0, 0, 0, 0.5); backdrop-filter: blur(5px);">';
                modalHtml += '<div class="modal-content" style="background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%); border-radius: 20px; padding: 0.5rem; max-width: 380px; width: 90%; box-shadow: 0 20px 40px rgba(0, 194, 206, 0.2); position: relative; font-size: 0.85rem;">';

                modalHtml += '<div class="modal-header" style="text-align: center; margin-bottom: 1rem; position: relative; z-index: 2;">';
                modalHtml += '<div class="success-icon" style="display: inline-flex; align-items: center; justify-content: center; width: 50px; height: 50px; background: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%); border-radius: 50%; margin-bottom: 0.75rem; margin-top: 1rem; box-shadow: 0 8px 20px rgba(0, 194, 206, 0.3);">';
                modalHtml += '<i class="fas fa-check" style="font-size: 1.25rem; color: white;"></i>';
                modalHtml += '</div>';
                modalHtml += '<h3 class="modal-title" style="font-size: 1.25rem; font-weight: 700; color: #1a202c; margin-bottom: 0.25rem; background: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text;">Ticket Created Successfully!</h3>';
                modalHtml += '<p class="modal-subtitle" style="color: rgba(26, 32, 44, 0.7); font-size: 0.85rem; margin: 0;">Your charging session has been queued</p>';
                modalHtml += '</div>';

                // Ticket details with enhanced styling
                modalHtml += '<div class="ticket-details" style="background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%); border-radius: 12px; padding: 0.75rem; margin-bottom: 0.75rem; border: 1px solid rgba(0, 194, 206, 0.1); position: relative; z-index: 2;">';

                // Ticket ID section
                modalHtml += '<div class="ticket-id-section" style="display: flex; align-items: center; margin-bottom: 1rem; padding: 0.75rem; background: white; border-radius: 12px; box-shadow: 0 3px 10px rgba(0, 0, 0, 0.05);">';
                modalHtml += '<div class="section-icon" style="width: 40px; height: 40px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius: 10px; display: flex; align-items: center; justify-content: center; margin-right: 0.75rem;">';
                modalHtml += '<i class="fas fa-ticket-alt" style="color: white; font-size: 1rem;"></i>';
                modalHtml += '</div>';
                modalHtml += '<div class="section-content">';
                modalHtml += '<div class="section-label" style="font-size: 0.8rem; color: rgba(26, 32, 44, 0.6); font-weight: 600; text-transform: uppercase; letter-spacing: 0.5px;">Ticket ID</div>';
                modalHtml += '<div class="section-value" style="font-size: 1.2rem; font-weight: 800; color: #1a202c; font-family: monospace;">' + ticketId + '</div>';
                modalHtml += '</div>';
                modalHtml += '</div>';

                // Charging Type section
                modalHtml += '<div class="charging-type-section" style="display: flex; align-items: center; margin-bottom: 1rem; padding: 0.75rem; background: white; border-radius: 12px; box-shadow: 0 3px 10px rgba(0, 0, 0, 0.05);">';
                modalHtml += '<div class="section-icon" style="width: 40px; height: 40px; background: linear-gradient(135deg, ' + chargingColor + ' 0%, ' + (serviceType === 'Fast Charging' ? '#e74c3c' : '#0e3a49') + ' 100%); border-radius: 10px; display: flex; align-items: center; justify-content: center; margin-right: 0.75rem;">';
                modalHtml += '<i class="' + chargingIcon + '" style="color: white; font-size: 1rem;"></i>';
                modalHtml += '</div>';
                modalHtml += '<div class="section-content">';
                modalHtml += '<div class="section-label" style="font-size: 0.8rem; color: rgba(26, 32, 44, 0.6); font-weight: 600; text-transform: uppercase; letter-spacing: 0.5px;">Charging Type</div>';
                modalHtml += '<div class="section-value" style="font-size: 1.2rem; font-weight: 800; color: ' + chargingColor + ';">' + serviceType + '</div>';
                modalHtml += '</div>';
                modalHtml += '</div>';

                // Battery status
                modalHtml += '<div class="battery-status-section" style="display: flex; align-items: center; padding: 0.75rem; background: white; border-radius: 12px; box-shadow: 0 3px 10px rgba(0, 0, 0, 0.05);">';
                modalHtml += '<div class="section-icon" style="width: 40px; height: 40px; background: linear-gradient(135deg, #27ae60 0%, #2ecc71 100%); border-radius: 10px; display: flex; align-items: center; justify-content: center; margin-right: 0.75rem;">';
                modalHtml += '<i class="fas fa-car-battery" style="color: white; font-size: 1rem;"></i>';
                modalHtml += '</div>';
                modalHtml += '<div class="section-content">';
                modalHtml += '<div class="section-label" style="font-size: 0.8rem; color: rgba(26, 32, 44, 0.6); font-weight: 600; text-transform: uppercase; letter-spacing: 0.5px;">Current Battery</div>';
                modalHtml += '<div class="section-value" style="font-size: 1.2rem; font-weight: 800; color: #27ae60;">' + batteryLevel + '%</div>';
                modalHtml += '</div>';
                modalHtml += '</div>';

                modalHtml += '</div>';

                // Safety message with icon
                modalHtml += '<div class="safety-notice" style="background: linear-gradient(135deg, #fff3cd 0%, #ffeaa7 100%); border: 1px solid #f39c12; border-radius: 10px; padding: 0.75rem; margin-bottom: 0.75rem; position: relative; z-index: 2;">';
                modalHtml += '<div class="safety-content" style="display: flex; align-items: flex-start;">';
                modalHtml += '<div class="safety-icon" style="width: 28px; height: 28px; background: #f39c12; border-radius: 50%; display: flex; align-items: center; justify-content: center; margin-right: 0.5rem; flex-shrink: 0; margin-top: 0.125rem;">';
                modalHtml += '<i class="fas fa-shield-alt" style="color: white; font-size: 0.7rem;"></i>';
                modalHtml += '</div>';
                modalHtml += '<div class="safety-text">';
                modalHtml += '<div class="safety-title" style="font-weight: 700; color: #8b4513; margin-bottom: 0.125rem; font-size: 0.8rem;">Safety Priority Notice</div>';
                modalHtml += '<div class="safety-description" style="color: #8b4513; line-height: 1.3; font-size: 0.75rem;">Under Cephra\'s 20% Rule, vehicles below 20% battery are prioritized for safety. Your session will be processed according to queue priority.</div>';
                modalHtml += '</div>';
                modalHtml += '</div>';
                modalHtml += '</div>';

                modalHtml += '<div class="modal-actions" style="display: flex; gap: 1rem; justify-content: center; margin-top: 1rem; margin-bottom: 1rem; position: relative; z-index: 2;">';
                modalHtml += '<button onclick="processTicket()" class="btn-primary" style="background: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%); color: white; border: none; padding: 0.5rem 1.5rem; border-radius: 20px; cursor: pointer; font-weight: 700; font-size: 0.85rem; transition: all 0.3s ease; box-shadow: 0 5px 15px rgba(0, 194, 206, 0.3); position: relative; overflow: hidden;">';
                modalHtml += '<span style="position: relative; z-index: 2;">Process Ticket</span>';
                modalHtml += '<div style="position: absolute; top: 0; left: -100%; width: 100%; height: 100%; background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent); transition: left 0.5s; z-index: 1;"></div>';
                modalHtml += '</button>';
                modalHtml += '<button onclick="cancelTicket(\'' + ticketId + '\')" class="btn-cancel" style="background: transparent; color: rgba(26, 32, 44, 0.6); border: 2px solid rgba(26, 32, 44, 0.3); padding: 0.5rem 1.5rem; border-radius: 20px; cursor: pointer; font-weight: 700; font-size: 0.85rem; transition: all 0.3s ease;">Cancel Ticket</button>';
                modalHtml += '</div>';
                modalHtml += '</div>';
                modalHtml += '</div>';

                // Add CSS animations
                modalHtml += '<style>';
                modalHtml += '@keyframes modalScaleIn { from { transform: scale(0.8); opacity: 0; } to { transform: scale(1); opacity: 1; } }';
                modalHtml += '.modal-content { animation: modalScaleIn 0.4s ease-out; }';
                modalHtml += '.btn-primary:hover div { left: 100%; }';
                modalHtml += '.btn-primary:hover { transform: translateY(-2px); box-shadow: 0 8px 25px rgba(0, 194, 206, 0.4); }';
                modalHtml += '.btn-cancel:hover { background: rgba(239, 68, 68, 0.1); border-color: #ef4444; color: #ef4444; }';
                modalHtml += '</style>';

                // Append to body
                document.body.insertAdjacentHTML('beforeend', modalHtml);

                // Show modal with animation
                setTimeout(function() {
                    const modal = document.getElementById('queueModal');
                    modal.style.display = 'flex';
                    modal.style.alignItems = 'center';
                    modal.style.justifyContent = 'center';
                    document.body.style.overflow = 'hidden';
                }, 10);
            }
        }

        // Function to close popup (defined globally)
        window.closePopup = function() {
            const modal = document.getElementById('queueModal');
            if (modal) {
                modal.style.display = 'none';
                document.body.style.overflow = '';
                modal.remove();
            }
        };

        // Function to cancel ticket
        function cancelTicket(ticketId) {
            fetch('cancel_charge_action.php', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'ticketId=' + encodeURIComponent(ticketId)
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    closePopup();
                } else if (data.error) {
                    alert('Error cancelling ticket: ' + data.error);
                }
            })
            .catch(error => {
                alert('An error occurred while cancelling the ticket. Please try again.');
                console.error('Error:', error);
            });
        }

        // Function to process ticket
        function processTicket() {
            // Get the ticket ID from the modal
            const modal = document.getElementById('queueModal');
            if (!modal) return;

            // Find the ticket ID in the modal content
            const ticketIdElement = modal.querySelector('.section-value');
            if (!ticketIdElement) return;

            const ticketId = ticketIdElement.textContent.trim();

            // Check if the Process Ticket button was pressed (this function is called only on button press)
            // So no additional check needed here, but if you want to enforce, you can disable the button until clicked.

            // Perform the fetch to the same page to process the ticket
            fetch(window.location.href, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'ticketId=' + encodeURIComponent(ticketId)
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    showDialog('Ticket Processing', 'Your Bay assignment will be announced in the dashboard, please wait for further notice.');
                    closePopup();
                } else if (data.error) {
                    showDialog('Processing Error', data.error);
                }
            })
            .catch(error => {
                showDialog('Processing Error', 'An error occurred while processing the ticket. Please try again.');
                console.error('Error:', error);
            });
        }
    </script>
</body>
</html>
