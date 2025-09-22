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
		<meta name="theme-color" content="#062635" />
		<link rel="stylesheet" href="css/main.css" />
		<style>
			/* ============================================
			   EASY-TO-ADJUST POPUP STYLES
			   ============================================
			   Simply change the values below to adjust:
			   - Popup size and position
			   - Image sizing and fit
			   - Border radius and appearance
			   ============================================ */

			.popup-overlay {
				position: fixed;
				top: 0;
				left: 0;
				width: 100%;
				height: 100%;
				background: rgba(0, 0, 0, 0.7);
				display: flex;
				align-items: center;
				justify-content: center;
				z-index: 10000;
				padding: 20px;
			}

			/* ADJUST POPUP CONTAINER SIZE */
			.popup-content {
				position: relative;
				width: 90%;                    /* ← CHANGE: 80% = smaller, 95% = larger */
				max-width: 400px;              /* ← CHANGE: 300px = smaller, 500px = larger */
				max-height: 85vh;              /* ← CHANGE: 70vh = shorter, 90vh = taller */
				border-radius: 20px;           /* ← CHANGE: 0px = sharp corners, 30px = more rounded */
				overflow: hidden;
				display: flex;
				align-items: center;
				justify-content: center;
			}

			/* ADJUST IMAGE SIZING */
			.popup-image {
				width: 100%;                   /* ← CHANGE: 90% = add margins, 95% = smaller margins */
				height: auto;
				max-height: 100%;              /* ← CHANGE: 90% = add padding inside */
				max-width: 100%;
				object-fit: contain;           /* ← CHANGE: 'cover' = fill entire space, 'scale-down' = never enlarge */
				display: block;
				border-radius: 20px;           /* ← CHANGE: 0px = sharp corners, 30px = more rounded */
			}

			.close-btn {
				position: absolute;
				top: 15px;
				right: 15px;
				background: rgba(0, 0, 0, 0.8);
				color: white;
				border: none;
				border-radius: 50%;
				width: 30px;
				height: 30px;
				font-size: 18px;
				cursor: pointer;
				z-index: 10001;
				display: flex;
				align-items: center;
				justify-content: center;
				font-weight: bold;
			}

			/* Animation for popup appearance */
			.popup-content {
				animation: popupSlideIn 0.3s ease-out;
			}

			@keyframes popupSlideIn {
				from {
					opacity: 0;
					transform: scale(0.8) translateY(-50px);
				}
				to {
					opacity: 1;
					transform: scale(1) translateY(0);
				}
			}
		</style>
	</head>
	<body class="homepage is-preload">
		<div id="page-wrapper">

			<!-- Header -->
				<div id="header-wrapper">
					<!-- Header -->
						<header id="header">
							<div class="inner">

								<!-- Logo -->
									<h1>

									<a href="dashboard.php" id="logo" style="display:inline-flex;align-items:center;gap:8px;"><img src="images/logo.png" alt="Cephra" style="width:28px;height:28px;border-radius:6px;object-fit:cover;vertical-align:middle;" /><span>Cephra</span></a>
									</h1>

								<!-- Nav -->
									<nav id="nav">
										<ul>
											<li class="current_page_item"><a href="dashboard.php">Home</a></li>
											<li><a href="link.php">Link</a></li>
											<li><a href="history.php">History</a></li>
											<li><a href="profile.php">Profile</a></li>
											<li><a href="#" onclick="openMonitorWeb(); return false;">Monitor</a></li>
										</ul>
									</nav>

							</div>
						</header>

					<div class="container">
                        <h2 class="greeting">Hello, <?php echo htmlspecialchars($firstname ?? 'User'); ?></h2>
						<!-- Banner -->
							<div id="banner">
								<h2>
								<a href="#" class="image featured"><img src="images/ads.png" alt="" /></a>
								</h2>
								<p>Every tap starts your power. With Cephra, charging isn’t just energy it’s freedom. </p>
								<a href="ChargingPage.php" id="start-charging" class="button large">Start Charging</a>
							</div>
					</div>
				</div>

			<!-- Main Wrapper -->
				<div id="main-wrapper">
					<div class="wrapper style1">
						<div class="inner">



							<!-- Feature 2 -->
								<section class="container box feature2">
									<div class="row">
										<div class="col-6 col-12-medium">
											<section>
												<header class="major">
													<h2>Rewards</h2>
													<p>Earn points every charge with Cephra Rewards and unlock perks made for you.</p>
												</header>
												<p><a href="#" class="image featured"><img src="images/ads.png" alt="" /></a></p>
												<footer>
													<a href="#" class="button medium alt icon solid fa-info-circle">Rewards</a>
												</footer>
											</section>
										</div>
										<div class="col-6 col-12-medium">
											<section>
												<header class="major">
													<h2>Wallet</h2>
													<p>Securely store, manage, and use your balance with ease through Cephra Wallet.</p>
												</header>
												<p><a href="#" class="image featured"><img src="images/ads.png" alt="" /></a></p>
												<footer>
													<a href="profile.php" class="button medium alt icon solid fa-info-circle">Profile</a>
												</footer>
											</section>
										</div>
									</div>
								</section>

							</div>
					</div>

			<!-- Footer Wrapper -->
				<div id="footer-wrapper">
					<footer id="footer" class="container">
						<div class="row">


								<!-- About -->
									<section>
										<h2><strong>Cephra</strong> by CephraCorp</h2>
										<p>Hi! This is <strong>Cephra</strong>, a free, fully responsive site for charging your
										electric vehicle. its built on HTML5 and CSS3 </a>.
										</p>
									</section>

								<!-- Contact -->
									<section>
										<h2>Get in touch</h2>
										<div>
											<div class="row">
												<div class="col-6 col-12-small">
													<dl class="contact">
														<dt>Twitter</dt>
														<dd><a href="https://www.x.com/@Cephra">@Cephra</a></dd>
														<dt>Facebook</dt>
														<dd><a href="https://www.facebook.com/CephraCorp">facebook.com/CephraCorp</a></dd>
														<dt>WWW</dt>
														<dd><a href="https://www.cephra.com">CephraCorp.tld</a></dd>
														<dt>Email</dt>
														<dd><a href="mailto:admin@cephra.com">admin@Cephra.gmail.com</a></dd>
													</dl>
												</div>
												<div class="col-6 col-12-small">
													<dl class="contact">
														<dt>Address</dt>
														<dd>
															Coral Way St.<br />
															Moa Complex, Pasay City<br />
															PH
														</dd>
														<dt>Phone</dt>
														<dd>(000) 000-0000</dd>
													</dl>
												</div>
											</div>
										</div>
									</section>

							</div>
							<div class="col-12">
								<div id="copyright">
									<ul class="menu">
										<li>&copy; Cephra. All rights reserved</li><li>Design: <a href="http://html5up.net">Cephra Designer</a></li>
									</ul>
								</div>
							</div>
						</div>
					</footer>
				</div>

		</div>

		<!-- Image-based Popup Ad -->
		<div id="greenPointsPopup" class="popup-overlay" style="display: none;">
			<div class="popup-content">
				<button class="close-btn" onclick="closeGreenPointsPopup()">×</button>
				<img src="images/pop-up.png" alt="Cephra Rewards Popup" class="popup-image" />
			</div>
		</div>

		<!-- Scripts -->
			<script src="assets/js/jquery.min.js"></script>
			<script src="assets/js/jquery.dropotron.min.js"></script>
			<script src="assets/js/browser.min.js"></script>
			<script src="assets/js/breakpoints.min.js"></script>
			<script src="assets/js/util.js"></script>
			<script src="assets/js/main.js"></script>

            <script>
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

                // Function to show Green Points popup
                function showGreenPointsPopup() {
                    document.getElementById('greenPointsPopup').style.display = 'flex';
                }

                // Function to close Green Points popup
                function closeGreenPointsPopup() {
                    document.getElementById('greenPointsPopup').style.display = 'none';
                }

                // Show popup after page loads (with delay)
                $(document).ready(function() {
                    // Show Green Points popup after 2 seconds
                    setTimeout(function() {
                        showGreenPointsPopup();
                    }, 2000);

                    // Normal Charge Button Click Handler
                    $('#normalChargeBtn').click(function(e) {
                        e.preventDefault();
                        processChargeRequest('Normal Charging');
                    });

                    // Fast Charge Button Click Handler
                    $('#fastChargeBtn').click(function(e) {
                        e.preventDefault();
                        processChargeRequest('Fast Charging');
                    });

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

                        // Disable buttons during processing
                        $('#normalChargeBtn, #fastChargeBtn').prop('disabled', true);

                        $.ajax({
                            url: 'charge_action.php',
                            type: 'POST',
                            data: { serviceType: serviceTypeMapped },
                            dataType: 'json',
                            success: function(response) {
                                if (response.success) {
                                    // Show queue ticket popup
                                    showQueueTicketPopup(response);
                                } else if (response.error) {
                                    showDialog('Charging', response.error);
                                }
                            },
                            error: function(xhr, status, error) {
                                showDialog('Charging', 'An error occurred while processing your request. Please try again.');
                                console.error('AJAX Error:', error);
                            },
                            complete: function() {
                                // Re-enable buttons
                                $('#normalChargeBtn, #fastChargeBtn').prop('disabled', false);
                            }
                        });
                    }

                    function showQueueTicketPopup(response) {
                        if (response.success) {
                            var ticketId = response.ticketId;
                            var serviceType = response.serviceType;
                            var batteryLevel = response.batteryLevel;

                            // Create popup HTML
                            var popupHtml = '<div id="queuePopup" style="position: fixed; top: 20%; left: 50%; transform: translate(-50%, -20%); background: white; border: 2px solid #007bff; border-radius: 10px; padding: 20px; width: 300px; z-index: 10000; box-shadow: 0 0 10px rgba(0,0,0,0.5);">';
                            popupHtml += '<h2 style="margin-top: 0; color: #007bff; text-align: center;">Your Queue Ticket</h2>';
                            popupHtml += '<div style="margin: 10px 0; font-size: 16px; text-align: center;"><strong>Ticket ID:</strong> ' + ticketId + '</div>';
                            popupHtml += '<div style="margin: 10px 0; font-size: 16px; text-align: center;"><strong>Service:</strong> ' + serviceType + '</div>';
                            popupHtml += '<div style="margin: 10px 0; font-size: 16px; text-align: center;"><strong>Battery Level:</strong> ' + batteryLevel + '%</div>';
                            popupHtml += '<div style="margin: 10px 0; font-size: 16px; text-align: center;"><strong>Estimated Wait Time:</strong> 5 minutes</div>';
                            popupHtml += '<button onclick="closePopup()" style="display: block; margin: 15px auto 0; padding: 10px 20px; background: #007bff; color: white; border: none; border-radius: 5px; cursor: pointer;">Close</button>';
                            popupHtml += '</div>';

                            // Append to body
                            $('body').append(popupHtml);
                        }
                    }

                    // Function to close popup (defined globally)
                    window.closePopup = function() {
                        $('#queuePopup').remove();
                    };
                });

                // Function to open Monitor Web in new tab
                window.openMonitorWeb = function() {
                    const monitorUrl = '../Monitor/';
                    window.open(monitorUrl, '_blank', 'noopener,noreferrer');
                };
            </script>

		</body>
	</html>
