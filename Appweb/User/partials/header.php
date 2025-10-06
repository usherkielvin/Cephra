<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
require_once __DIR__ . '/../config/database.php';
$db = new Database();
$conn = $db->getConnection();
$username = isset($_SESSION['username']) ? $_SESSION['username'] : '';
?>
<style>
/* ============================================
   VANTAGE MARKETS INSPIRED WHITE THEME
   ============================================ */

:root {
    /* Light Theme - Professional White Theme */
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
    --gradient-primary: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%);
    --gradient-secondary: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
    --gradient-accent: linear-gradient(135deg, #00c2ce 0%, #0e3a49 50%, #1a202c 100%);
}

/* ============================================
   MODERN DASHBOARD STYLES - WHITE THEME
   ============================================ */

			/* Header Styles */
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
				box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
			}

			.header.scrolled {
				background: rgba(255, 255, 255, 0.98);
				box-shadow: 0 2px 20px rgba(0, 0, 0, 0.15);
			}

			.header-content {
				display: flex;
				align-items: center;
				justify-content: space-between;
				padding: 1rem 0;
				width: 100%;
			}

			.logo {
				display: flex;
				align-items: center;
				gap: 12px;
				text-decoration: none;
			}

			.logo-img {
				width: 40px;
				height: 40px;
				border-radius: 10px;
				object-fit: cover;
			}

			.logo-text {
				font-size: 24px;
				font-weight: 800;
				color: var(--text-primary);
				letter-spacing: 1px;
			}

			.nav-list {
				display: flex;
				list-style: none;
				gap: 2rem;
				align-items: center;
			}

			.nav-link {
				color: var(--text-secondary);
				text-decoration: none;
				font-weight: 500;
				transition: all 0.3s ease;
				position: relative;
			}

			.nav-link:hover {
				color: var(--primary-color);
			}

			.header-actions {
				display: flex;
				align-items: center;
				gap: 1.5rem;
			}

			.auth-link {
				color: var(--text-secondary);
				text-decoration: none;
				font-weight: 500;
				transition: all 0.3s ease;
				position: relative;
				padding: 0.5rem 0;
			}

			.auth-link:hover {
				color: var(--primary-color);
			}

			.mobile-menu-toggle {
				display: flex;
				flex-direction: column;
				background: none;
				border: none;
				cursor: pointer;
				padding: 8px;
				gap: 4px;
			}

			.mobile-menu-toggle span {
				width: 25px;
				height: 3px;
				background: var(--text-primary);
				transition: all 0.3s ease;
			}

			.mobile-menu-toggle.active span:nth-child(1) {
				transform: rotate(45deg) translate(6px, 6px);
			}

			.mobile-menu-toggle.active span:nth-child(2) {
				opacity: 0;
			}

			.mobile-menu-toggle.active span:nth-child(3) {
				transform: rotate(-45deg) translate(6px, -6px);
			}

			/* Mobile Menu Styles */
			.mobile-menu {
				position: fixed;
				top: 0;
				right: -100%;
				width: 280px;
				height: 100vh;
				background: rgba(255, 255, 255, 0.98);
				backdrop-filter: blur(20px);
				border-left: 1px solid var(--border-color);
				z-index: 1001;
				transition: right 0.3s ease;
				box-shadow: -5px 0 20px rgba(0, 0, 0, 0.1);
				overflow: visible;
			}

.mobile-menu.mobile-menu-open {
    right: 0;
}

			.mobile-menu-content {
				padding: 80px 2rem 2rem;
				height: 100%;
				display: flex;
				flex-direction: column;
				gap: 2rem;
			}

			.mobile-nav-list {
				list-style: none;
				padding: 0;
				margin: 0;
				display: flex;
				flex-direction: column;
				gap: 1rem;
			}

			.mobile-nav-link {
				color: var(--text-primary);
				text-decoration: none;
				font-weight: 500;
				padding: 1rem;
				border-radius: 8px;
				transition: all 0.3s ease;
				display: block;
				text-align: center;
			}

			.mobile-nav-link:hover {
				background: var(--bg-secondary);
				color: var(--primary-color);
			}

			.mobile-header-actions {
				margin-top: auto;
				padding-top: 2rem;
				border-top: 1px solid var(--border-color);
			}

			.mobile-auth-link {
				color: var(--text-secondary);
				text-decoration: none;
				font-weight: 500;
				padding: 1rem;
				border-radius: 8px;
				transition: all 0.3s ease;
				display: block;
				text-align: center;
				background: var(--gradient-primary);
				color: white;
				margin: 0 auto;
				max-width: 200px;
			}

			.mobile-auth-link:hover {
				transform: translateY(-2px);
				box-shadow: 0 5px 15px var(--shadow-medium);
			}

			/* Mobile Menu Overlay */
			.mobile-menu-overlay {
				position: fixed;
				top: 0;
				left: 0;
				width: 100%;
				height: 100%;
				background: rgba(0, 0, 0, 0.5);
				z-index: 998;
				opacity: 0;
				visibility: hidden;
				transition: all 0.3s ease;
			}

			.mobile-menu-overlay.active {
				opacity: 1;
				visibility: visible;
			}
/* Notification Dropdown Styles */
.notification-item:hover {
    background: var(--bg-secondary);
}

.notification-badge {
    position: absolute;
    top: -2px;
    right: -2px;
    width: 8px;
    height: 8px;
    background: #ff4757;
    border-radius: 50%;
    display: block;
}

/* Responsive Design */
@media (max-width: 768px) {
    .header-content {
        flex-wrap: wrap;
    }

    .nav {
        display: none;
    }

    .header-actions {
        display: none;
    }

    .mobile-menu-toggle {
        display: flex;
    }

    .notification-dropdown {
        min-width: 250px !important;
        max-width: 280px !important;
        right: -10px !important;
    }
}

@media (max-width: 500px) {
    .wallet-link {
        display: none !important;
    }
}

/* Mobile Language Selector removed - language selection available in profile dropdown */

</style>

		<!-- Header -->
		<header class="header">
			<div class="container">
				<div class="header-content"
					 style="display: flex;
							align-items: center;
							justify-content: space-between;
							gap: 16px;
							width: 100%;">
					<!-- Logo -->
            <a href="dashboard.php" class="logo"
               style="display: flex;
                      align-items: center;
                      gap: 12px;
                      margin-right: 16px;
                      text-decoration: none;">
                <img src="images/logo.png" alt="Cephra" class="logo-img" />
                <span class="logo-text">CEPHRA</span>
            </a>

					<!-- Navigation -->
					<nav class="nav" style="flex: 1;">
						<ul class="nav-list"
							style="display: flex;
								   gap: 1.25rem;
								   align-items: center;">
							<li><a href="dashboard.php" class="nav-link">Home</a></li>
							<li><a href="link.php" class="nav-link">Link</a></li>
							<li><a href="history.php" class="nav-link">History</a></li>
							<li><a href="rewards.php" class="nav-link">Rewards</a></li>
						</ul>
					</nav>

					<!-- Header Actions -->
					<div class="header-actions"
						 style="display: flex;
								align-items: center;
								gap: 24px;
								margin-left: auto;">
						<!-- Wallet button -->
						<a href="wallet.php" class="wallet-link"
						   title="Wallet"
						   style="display: inline-flex;
								  align-items: center;
								  justify-content: center;
								  width: auto;
								  height: auto;
								  border: none;
								  background: transparent;
								  color: inherit;
								  cursor: pointer;
								  padding: 4px;">
							<i class="fas fa-wallet" aria-hidden="true" style="font-size: 18px;"></i>
						</a>
                <!-- Notification bell -->
                <div class="notifications" style="position: relative;">
                    <button id="notifBtn"
                            title="Notifications"
                            style="display: inline-flex;
                                   align-items: center;
                                   justify-content: center;
                                   width: auto;
                                   height: auto;
                                   border: none;
                                   background: transparent;
                                   color: inherit;
                                   cursor: pointer;
                                   padding: 4px;
                                   position: relative;">
                        <i class="fas fa-bell" aria-hidden="true" style="font-size: 18px;"></i>
                        <span class="notification-badge" style="position: absolute; top: -2px; right: -2px; width: 8px; height: 8px; background: #ff4757; border-radius: 50%; display: block;"></span>
                    </button>
                    <div class="notification-dropdown" id="notificationDropdown"
                         style="display: none;
                                position: absolute;
                                top: 100%;
                                right: 0;
                                background: white;
                                border: 1px solid var(--border-color);
                                border-radius: 8px;
                                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
                                min-width: 280px;
                                max-width: 320px;
                                z-index: 1001;
                                max-height: 300px;
                                overflow-y: auto;
                                margin-top: 8px;">
                        <div class="notification-item" style="padding: 12px 16px; border-bottom: 1px solid var(--border-color); cursor: pointer; transition: background 0.2s; display: flex; align-items: flex-start; gap: 10px;">
                            <i class="fas fa-info-circle" style="color: var(--primary-color); font-size: 16px; margin-top: 2px;"></i>
                            <div style="flex: 1;">
                                <div style="font-weight: 600; color: var(--text-primary); margin-bottom: 4px;">Welcome to Cephra!</div>
                                <div style="font-size: 0.9rem; color: var(--text-secondary);">Your personalized dashboard is ready to use.</div>
                                <div style="font-size: 0.8rem; color: var(--text-muted); margin-top: 4px;">Just now</div>
                            </div>
                        </div>
                        <div class="notification-item" style="padding: 12px 16px; border-bottom: 1px solid var(--border-color); cursor: pointer; transition: background 0.2s; display: flex; align-items: flex-start; gap: 10px;">
                            <i class="fas fa-bolt" style="color: #28a745; font-size: 16px; margin-top: 2px;"></i>
                            <div style="flex: 1;">
                                <div style="font-weight: 600; color: var(--text-primary); margin-bottom: 4px;">Charging Session Complete</div>
                                <div style="font-size: 0.9rem; color: var(--text-secondary);">Your EV has finished charging at Station A.</div>
                                <div style="font-size: 0.8rem; color: var(--text-muted); margin-top: 4px;">2 hours ago</div>
                            </div>
                        </div>
                        <div class="notification-item" style="padding: 12px 16px; border-bottom: 1px solid var(--border-color); cursor: pointer; transition: background 0.2s; display: flex; align-items: flex-start; gap: 10px;">
                            <i class="fas fa-gift" style="color: #ff6b6b; font-size: 16px; margin-top: 2px;"></i>
                            <div style="flex: 1;">
                                <div style="font-weight: 600; color: var(--text-primary); margin-bottom: 4px;">New Rewards Available</div>
                                <div style="font-size: 0.9rem; color: var(--text-secondary);">You've earned 50 Green Points from your last charge.</div>
                                <div style="font-size: 0.8rem; color: var(--text-muted); margin-top: 4px;">1 day ago</div>
                            </div>
                        </div>
                        <div class="notification-item" style="padding: 12px 16px; cursor: pointer; transition: background 0.2s; display: flex; align-items: flex-start; gap: 10px;">
                            <i class="fas fa-tools" style="color: #ffa502; font-size: 16px; margin-top: 2px;"></i>
                            <div style="flex: 1;">
                                <div style="font-weight: 600; color: var(--text-primary); margin-bottom: 4px;">System Maintenance Scheduled</div>
                                <div style="font-size: 0.9rem; color: var(--text-secondary);">Station B will be under maintenance tomorrow.</div>
                                <div style="font-size: 0.8rem; color: var(--text-muted); margin-top: 4px;">2 days ago</div>
                            </div>
                        </div>
                    </div>
                </div>

						<!-- Language selector (globe icon only) placed to the right of Download -->

						<?php
						// Compute avatar source
						$pfpSrc = 'images/logo.png';
						if ($conn) {
							try {
								$stmt2 = $conn->prepare("SELECT profile_picture FROM users WHERE username = :u LIMIT 1");
								$stmt2->bindParam(':u', $username);
								$stmt2->execute();
								$row2 = $stmt2->fetch(PDO::FETCH_ASSOC);
								if ($row2 && !empty($row2['profile_picture'])) {
									$pp = $row2['profile_picture'];
									if (strpos($pp, 'data:image') === 0) {
										$pfpSrc = $pp;
									} elseif (strpos($pp, 'iVBORw0KGgo') === 0) {
										$pfpSrc = 'data:image/png;base64,' . $pp;
									} elseif (preg_match('/\.(jpg|jpeg|png|gif)$/i', $pp)) {
										$path = 'uploads/profile_pictures/' . $pp;
										if (file_exists(__DIR__ . '/../' . $path)) {
											$pfpSrc = $path;
										}
									}
								}
							} catch (Exception $e) { /* ignore */ }
						}
						?>
						<div class="profile-container" style="position: relative;">
							<button class="profile-btn" id="profileBtn"
								   title="Profile Menu"
								   style="display: inline-flex;
										  width: 38px;
										  height: 38px;
										  border-radius: 50%;
										  overflow: hidden;
										  border: 2px solid rgba(0, 0, 0, 0.08);
										  background: transparent;
										  cursor: pointer;
										  padding: 0;">
								<img src="<?php echo htmlspecialchars($pfpSrc); ?>"
									 alt="Profile"
									 style="width: 100%;
											height: 100%;
											object-fit: cover;
											display: block;" />
							</button>
							<ul class="profile-dropdown" id="profileDropdown"
								style="position: absolute;
									   top: 100%;
									   right: 0;
									   background: white;
									   border: 1px solid rgba(0, 0, 0, 0.1);
									   border-radius: 8px;
									   box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
									   min-width: 120px;
									   list-style: none;
									   padding: 0;
									   margin: 0;
									   z-index: 1001;
									   display: none;">
								<li style="border-bottom: 1px solid rgba(0, 0, 0, 0.05);">
									<a href="profile.php" style="display: block; padding: 12px 16px; color: #333; text-decoration: none; transition: background 0.2s;">Profile</a>
								</li>
								<li style="position: relative; border-bottom: 1px solid rgba(0, 0, 0, 0.05);">
									<a href="#" id="languageMenuItem" style="display: block; padding: 12px 16px; color: #333; text-decoration: none; transition: background 0.2s;">Language</a>
                                    <ul class="language-sub-dropdown" id="languageSubDropdown" style="position: absolute; top: 0; right: 100%; left: auto; background: white; border: 1px solid rgba(0, 0, 0, 0.1); border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); min-width: 120px; list-style: none; padding: 0; margin: 0; display: none;">
										<li><a onclick="event.preventDefault(); event.stopPropagation(); setLanguage('en');" style="display: block; padding: 8px 12px; color: #333; text-decoration: none; transition: background 0.2s; cursor: pointer;">English</a></li>
										<li><a onclick="event.preventDefault(); event.stopPropagation(); setLanguage('fil');" style="display: block; padding: 8px 12px; color: #333; text-decoration: none; transition: background 0.2s; cursor: pointer;">Filipino</a></li>
										<li><a onclick="event.preventDefault(); event.stopPropagation(); setLanguage('ceb');" style="display: block; padding: 8px 12px; color: #333; text-decoration: none; transition: background 0.2s; cursor: pointer;">Bisaya</a></li>
										<li><a onclick="event.preventDefault(); event.stopPropagation(); setLanguage('zh');" style="display: block; padding: 8px 12px; color: #333; text-decoration: none; transition: background 0.2s; cursor: pointer;">中文</a></li>
									</ul>
								</li>
								<li>
									<a href="profile_logout.php" style="display: block; padding: 12px 16px; color: #333; text-decoration: none; transition: background 0.2s;">Logout</a>
								</li>
							</ul>
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
                                    <a href="dashboard.php" class="mobile-nav-link">Home</a>
                                </li>
                                <li class="mobile-nav-item">
                                    <a href="../Monitor/index.php" class="mobile-nav-link">Monitor</a>
                                </li>
                                <li class="mobile-nav-item">
                                    <a href="link.php" class="mobile-nav-link">Link</a>
                                </li>
                                <li class="mobile-nav-item">
                                    <a href="wallet.php" class="mobile-nav-link">Wallet</a>
                                </li>
								<li class="mobile-nav-item">
                                    <a href="history.php" class="mobile-nav-link">History</a>
                                </li>
								<li class="mobile-nav-item">
                                    <a href="rewards.php" class="mobile-nav-link">Rewards</a>
                                </li>
                            </ul>
                        </div>

                        <!-- Mobile Header Actions -->
                        <div class="mobile-header-actions" style="display:flex;gap:16px;align-items:center;justify-content:center;flex-wrap:wrap;">
                            <!-- Language selector removed - available in profile dropdown -->
                        </div>

                        <!-- Actions row: Download + Logout in one row on small screens -->
                        <div class="mobile-actions-row" style="display:flex;gap:16px;align-items:center;justify-content:center;margin-top:12px;">


                            <!-- Mobile Logout Button -->
                            <div class="mobile-auth-buttons" style="display:flex;gap:12px;align-items:center;justify-content:center;width:100%;">
                                <a href="profile_logout.php" class="nav-link auth-link" style="text-align:center;padding:0.75rem 1.5rem;background:var(--gradient-primary);color:white;border-radius:25px;text-decoration:none;font-weight:600;transition:all 0.3s ease;min-width:120px;">Logout</a>
                            </div>
                        </div>
                    </div>
                </div>
		</header>

		<!-- Mobile Menu Overlay -->
		<div class="mobile-menu-overlay" id="mobileMenuOverlay"></div>

		<!-- Scripts -->
			<script src="assets/js/jquery.min.js"></script>
			<script src="assets/js/jquery.dropotron.min.js"></script>
			<script src="assets/js/browser.min.js"></script>
			<script src="assets/js/breakpoints.min.js"></script>
			<script src="assets/js/util.js"></script>
			<script src="assets/js/main.js"></script>
            <script>
                // Profile dropdown functionality
                (function() {
                    const profileBtn = document.getElementById('profileBtn');
                    const profileDropdown = document.getElementById('profileDropdown');
                    const languageMenuItem = document.getElementById('languageMenuItem');
                    const languageSubDropdown = document.getElementById('languageSubDropdown');

                    if (profileBtn && profileDropdown) {
                        profileBtn.addEventListener('click', function(e) {
                            e.stopPropagation();
                            const isVisible = profileDropdown.style.display === 'block';
                            profileDropdown.style.display = isVisible ? 'none' : 'block';
                        });

                        // Close dropdown when clicking outside
                        document.addEventListener('click', function(e) {
                            if (!profileBtn.contains(e.target) && !profileDropdown.contains(e.target)) {
                                profileDropdown.style.display = 'none';
                            }
                        });

            // Language sub-dropdown toggle
            if (languageMenuItem && languageSubDropdown) {
                languageMenuItem.addEventListener('click', function(e) {
                    e.stopPropagation();
                    const isVisible = languageSubDropdown.style.display === 'block';
                    languageSubDropdown.style.display = isVisible ? 'none' : 'block';
                });
                // Close sub-dropdown when clicking outside
                document.addEventListener('click', function(e) {
                    if (!languageMenuItem.contains(e.target) && !languageSubDropdown.contains(e.target)) {
                        languageSubDropdown.style.display = 'none';
                    }
                });
            }
        }



// Notification dropdown functionality
(function() {
    const notifBtn = document.getElementById('notifBtn');
    const notificationDropdown = document.getElementById('notificationDropdown');

    if (notifBtn && notificationDropdown) {
        notifBtn.addEventListener('click', function(e) {
            e.stopPropagation();
            const isVisible = notificationDropdown.style.display === 'block';
            notificationDropdown.style.display = isVisible ? 'none' : 'block';
        });

        // Close dropdown when clicking outside
        document.addEventListener('click', function(e) {
            if (!notifBtn.contains(e.target) && !notificationDropdown.contains(e.target)) {
                notificationDropdown.style.display = 'none';
            }
        });
    }
})();

        // Set language function
        window.setLanguage = function(lang) {
            localStorage.setItem('selectedLanguage', lang);
            if (typeof translateDashboard === 'function') {
                translateDashboard();
            }
            // Update language text
            const langMap = {en: 'EN', fil: 'FIL', ceb: 'BIS', zh: '中文'};
            const langText = document.querySelector('.language-text');
            if (langText) langText.textContent = langMap[lang] || 'EN';
            // Close dropdowns
            const profileDropdownEl = document.getElementById('profileDropdown');
            const languageSubDropdownEl = document.getElementById('languageSubDropdown');
            const languageDropdownEl = document.getElementById('languageDropdown');
            const mobileLanguageDropdownEl = document.getElementById('mobileLanguageDropdown');
            if (profileDropdownEl) profileDropdownEl.style.display = 'none';
            if (languageSubDropdownEl) languageSubDropdownEl.style.display = 'none';
            if (languageDropdownEl) languageDropdownEl.style.display = 'none';
            if (mobileLanguageDropdownEl) mobileLanguageDropdownEl.style.display = 'none';
        };

                    // Download QR hover (if exists)
                    const downloadBtn = document.getElementById('downloadBtn');
                    const qrPopup = document.getElementById('qrPopup');
                    if (downloadBtn && qrPopup) {
                        downloadBtn.addEventListener('mouseenter', function(){
                            qrPopup.classList.add('show');
                        });
                        downloadBtn.addEventListener('mouseleave', function(){
                            setTimeout(()=>{
                                if (!downloadBtn.matches(':hover') && !qrPopup.matches(':hover')) {
                                    qrPopup.classList.remove('show');
                                }
                            }, 100);
                        });
                        qrPopup.addEventListener('mouseleave', function(){
                            qrPopup.classList.remove('show');
                        });
                    }
                })();

                // Mobile language selector
                const mobileLanguageBtn = document.getElementById('mobileLanguageBtn');
                const mobileLanguageDropdown = document.getElementById('mobileLanguageDropdown');
                if (mobileLanguageBtn && mobileLanguageDropdown) {
                    mobileLanguageBtn.addEventListener('click', function(e) {
                        e.stopPropagation();
                        const isVisible = mobileLanguageDropdown.style.display === 'block';
                        mobileLanguageDropdown.style.display = isVisible ? 'none' : 'block';
                    });
                    // Close when clicking outside
                    document.addEventListener('click', function(e) {
                        if (!mobileLanguageBtn.contains(e.target) && !mobileLanguageDropdown.contains(e.target)) {
                            mobileLanguageDropdown.style.display = 'none';
                        }
                    });
                    // Handle option clicks
                    mobileLanguageDropdown.addEventListener('click', function(e) {
                        if (e.target.classList.contains('language-option')) {
                            const lang = e.target.getAttribute('data-lang');
                            setLanguage(lang);
                        }
                    });
                }

                // Mobile download
                const mobileDownloadBtn = document.getElementById('mobileDownloadBtn');
                const mobileQrPopup = document.getElementById('mobileQrPopup');
                if (mobileDownloadBtn && mobileQrPopup) {
                    mobileDownloadBtn.addEventListener('mouseenter', function(){
                        mobileQrPopup.classList.add('show');
                    });
                    mobileDownloadBtn.addEventListener('mouseleave', function(){
                        setTimeout(()=>{
                            if (!mobileDownloadBtn.matches(':hover') && !mobileQrPopup.matches(':hover')) {
                                mobileQrPopup.classList.remove('show');
                            }
                        }, 100);
                    });
                    mobileQrPopup.addEventListener('mouseleave', function(){
                        mobileQrPopup.classList.remove('show');
                    });
                }
            </script>

           <script>
                function showDialog(title, message) {
                    // Remove any existing dialogs first
                    const existingDialogs = document.querySelectorAll('[data-dialog-overlay]');
                    existingDialogs.forEach(dialog => {
                        try {
                            if (dialog.parentNode) {
                                dialog.parentNode.removeChild(dialog);
                            }
                        } catch (e) {
                            // Element already removed
                        }
                    });
                    
                    const overlay = document.createElement('div');
                    overlay.setAttribute('data-dialog-overlay', 'true');
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
                    ok.onclick = () => {
                        try {
                            if (overlay && overlay.parentNode) {
                                overlay.style.display = 'none';
                                document.body.removeChild(overlay);
                            }
                        } catch (e) {
                            // Element already removed
                        }
                    };
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

                // Function to close Green Points popup with state management
                function closeGreenPointsPopup() {
                    // Prevent multiple rapid clicks
                    if (window.greenPointsPopupClosing) {
                        return;
                    }
                    window.greenPointsPopupClosing = true;
                    
                    document.getElementById('greenPointsPopup').style.display = 'none';
                    
                    // Reset state after a short delay
                    setTimeout(function() {
                        window.greenPointsPopupClosing = false;
                    }, 500);
                }

                // Show popup after page loads (with delay)
                $(document).ready(function() {
                    // Green Points popup removed - users can access rewards via navigation or View Rewards button

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
                                    // Show QueueTicketProceed popup
                                    // Small popup removed - now handled by ChargingPage.php modal
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

                    // Removed showQueueTicketProceedPopup - now handled by ChargingPage.php modal

                    // Function to close popup (defined globally) - simple and clean
                    window.closePopup = function() {
                        try {
                            // Remove all popups/modals
                            const popups = document.querySelectorAll('#queuePopup, [data-dialog-overlay]');
                            popups.forEach(popup => {
                                if (popup && popup.parentNode) {
                                    popup.style.display = 'none';
                                    popup.remove();
                                }
                            });
                            document.body.style.overflow = '';
                        } catch (e) {
                            // Force cleanup
                            document.body.style.overflow = '';
                            const overlays = document.querySelectorAll('div[style*="position: fixed"][style*="background: rgba"]');
                            overlays.forEach(overlay => overlay.remove());
                        }
                    };
                });

                // Function to open Monitor Web in new tab
                window.openMonitorWeb = function() {
                    const monitorUrl = '../Monitor/';
                    window.open(monitorUrl, '_blank', 'noopener,noreferrer');
                };

                // Modal Functions for New Features
                function showNearbyStations() {
                    document.getElementById('stationsModal').style.display = 'flex';
                }

                function closeStationsModal() {
                    document.getElementById('stationsModal').style.display = 'none';
                }

                function showScheduleModal() {
                    document.getElementById('scheduleModal').style.display = 'flex';
                }

                function closeScheduleModal() {
                    document.getElementById('scheduleModal').style.display = 'none';
                }

                function showSupportModal() {
                    document.getElementById('supportModal').style.display = 'flex';
                }

                function closeSupportModal() {
                    document.getElementById('supportModal').style.display = 'none';
                }

                function showEstimatedCost() {
                    showDialog('Estimated Cost', 'View detailed cost breakdown for your charging sessions:\n\n• Normal Charge: ₱45.00 per session\n• Fast Charge: ₱75.00 per session\n• Monthly Savings: ₱1,250 (based on usage)\n• Green Points: 340 earned this month\n• Total Sessions: 12 completed\n• Energy Consumed: 45.2 kWh\n\nTrack your spending and maximize your savings with our cost analysis tools.');
                }

                function showBatteryHealth() {
                    showDialog('Battery Health Monitor', 'Detailed battery health information:\n\n• Health Score: 92% (Excellent)\n• Degradation: 8% over 2 years\n• Temperature: Optimal (25°C)\n• Cycles: 340/1000 remaining\n• Voltage: 3.7V per cell\n• Capacity: 95% of original\n\nRecommendations:\n- Continue current usage patterns\n- Schedule maintenance in 6 months\n- Monitor temperature during fast charging');
                }

                function showRangeCalculator() {
                    showDialog('Range Calculator', 'Calculate your remaining range:\n\nCurrent Conditions:\n• Battery Level: 45%\n• Highway Range: 38 km\n• City Range: 52 km\n• Weather Impact: -5 km (rain)\n• Temperature Impact: -3 km (cold)\n\nEstimated Total Range: 42 km\n\nFactors affecting range:\n- Driving style: 15% impact\n- Speed: 20% impact\n- Weather: 10% impact\n- Temperature: 8% impact\n\nTips to maximize range:\n- Maintain steady speed\n- Use regenerative braking\n- Keep tires properly inflated');
                }

                function showDiagnostics() {
                    showDialog('Vehicle Diagnostics', 'System diagnostic results:\n\n✓ Battery Management System: Normal\n✓ Charging System: Normal\n✓ Motor Controller: Normal\n✓ Thermal Management: Normal\n✓ Communication Module: Normal\n✓ Safety Systems: Normal\n\nLast Diagnostic Run: 2 hours ago\nNext Scheduled: Due in 1,200 km\n\nNo issues detected. All systems operating within normal parameters.\n\nFor detailed reports, visit your service center or use the mobile app.');
                }

                function showChargingOptions() {
                    showDialog('Charging Options', 'Select your charging option:\n\n• Normal Charging: ₱45.00 (Standard rate)\n• Fast Charging: ₱75.00 (Express rate)\n\nClick "Start Charging" to proceed to the charging page.');
                    // Optionally redirect after dialog
                    setTimeout(() => {
                        window.location.href = 'ChargingPage.php';
                    }, 3000);
                }

                function submitSchedule() {
                    const form = document.getElementById('scheduleForm');
                    const formData = new FormData(form);

                    // Basic validation
                    const date = document.getElementById('scheduleDate').value;
                    const time = document.getElementById('scheduleTime').value;
                    const chargingType = document.getElementById('chargingType').value;
                    const duration = document.getElementById('estimatedDuration').value;

                    if (!date || !time || !chargingType || !duration) {
                        showDialog('Schedule Charging', 'Please fill in all required fields.');
                        return;
                    }

                    // Show loading dialog
                    showDialog('Schedule Charging', 'Scheduling your charging session...');

                    // Simulate API call (replace with actual endpoint)
                    setTimeout(() => {
                        closeScheduleModal();
                        showDialog('Schedule Charging', 'Your charging session has been scheduled successfully! You will receive a notification when your slot becomes available.');
                    }, 2000);
                }

                function showFAQ() {
                    showDialog('FAQ', 'Frequently Asked Questions:\n\n1. How do I start charging?\n   - Click on "Start Charging" and select your preferred charging type.\n\n2. How do I view my charging history?\n   - Navigate to the "History" section in the navigation menu.\n\n3. How do I update my profile?\n   - Go to "Profile" in the navigation menu to manage your settings.\n\n4. What are Green Points?\n   - Green Points are rewards earned for using our charging stations.\n\nFor more questions, please contact our support team.');
                }

                function contactSupport() {
                    showDialog('Contact Support', 'You can reach our support team through:\n\n📞 Phone: +63 (2) 123-4567\n📧 Email: support@cephra.com\n💬 Live Chat: Available 24/7\n\nOur support team is available Monday to Sunday, 6:00 AM to 10:00 PM.');
                }

                function reportIssue() {
                    showDialog('Report Issue', 'To report a technical issue:\n\n1. Describe the problem in detail\n2. Include any error messages\n3. Mention your device and browser\n4. Note the time when the issue occurred\n\nPlease contact our technical support team at:\n📞 Phone: +63 (2) 123-4567\n📧 Email: techsupport@cephra.com\n\nWe appreciate your feedback and will resolve the issue as quickly as possible.');
                }

                function navigateToStation(stationName) {
                    // Check if geolocation is available
                    if (navigator.geolocation) {
                        navigator.geolocation.getCurrentPosition(
                            function(position) {
                                const lat = position.coords.latitude;
                                const lng = position.coords.longitude;

                                // Use Google Maps or Waze for navigation
                                const mapsUrl = `https://www.google.com/maps/dir/${lat},${lng}/${encodeURIComponent(stationName)}`;
                                window.open(mapsUrl, '_blank', 'noopener,noreferrer');

                                showDialog('Navigation', `Opening navigation to ${stationName}...`);
                            },
                            function(error) {
                                // Fallback if geolocation fails
                                const mapsUrl = `https://www.google.com/maps/search/${encodeURIComponent(stationName + ' charging station')}`;
                                window.open(mapsUrl, '_blank', 'noopener,noreferrer');
                                showDialog('Navigation', 'Opening map directions. Please enable location services for precise navigation.');
                            }
                        );
                    } else {
                        // Fallback for browsers without geolocation
                        const mapsUrl = `https://www.google.com/maps/search/${encodeURIComponent(stationName + ' charging station')}`;
                        window.open(mapsUrl, '_blank', 'noopener,noreferrer');
                        showDialog('Navigation', 'Opening map directions. Please enable location services for precise navigation.');
                    }
                }

                // Load dashboard statistics
                function loadDashboardStats() {
                    // Simulate loading stats from API
                    setTimeout(() => {
                        const currentQueueEl = document.getElementById('currentQueue');
                        if (currentQueueEl) currentQueueEl.textContent = '3';

                        const waitTimeEl = document.getElementById('waitTime');
                        if (waitTimeEl) waitTimeEl.textContent = '8 minutes';

                        const activeSessionsEl = document.getElementById('activeSessions');
                        if (activeSessionsEl) activeSessionsEl.textContent = '7';

                        const avgDurationEl = document.getElementById('avgDuration');
                        if (avgDurationEl) avgDurationEl.textContent = '45 min';
                    }, 1000);
                }

                // Fetch live status from Admin API (same source as admin panel)
                function fetchAndRenderLiveStatus() {
                    // Try user public API first; fallback to admin API if accessible
                    fetch('api/mobile.php?action=live-status')
                        .then(res => res.json())
                        .then(data => {
                            if (!data || !data.success) throw new Error('fallback');
                            const queueCount = Number(data.queue_count || 0);
                            const activeBays = Number(data.active_bays || 0);

                            const queueEl = document.getElementById('currentQueue');
                            const activeEl = document.getElementById('activeSessions');
                            const waitEl = document.getElementById('waitTime');

                            if (queueEl) queueEl.textContent = queueCount;
                            if (activeEl) activeEl.textContent = activeBays;
                            if (waitEl) waitEl.textContent = `${Math.max(0, queueCount)} minutes`;
                        })
                        .catch(() => {
                            // Fallback to admin endpoint if session exists
                            fetch('../Admin/api/admin.php?action=dashboard')
                                .then(r => r.json())
                                .then(d => {
                                    if (!d || !d.success || !d.stats) return;
                                    const queueCount = Number(d.stats.queue_count || 0);
                                    const activeBays = Number(d.stats.active_bays || 0);
                                    document.getElementById('currentQueue').textContent = queueCount;
                                    document.getElementById('activeSessions').textContent = activeBays;
                                    document.getElementById('waitTime').textContent = `${Math.max(0, queueCount)} minutes`;
                                })
                                .catch(() => {});
                        });
                }

                // Start live updates every 3 seconds
                function updateLiveStatus() {
                    fetchAndRenderLiveStatus();
                    setInterval(fetchAndRenderLiveStatus, 3000);
                }

         // Mobile menu toggle
        document.getElementById('mobileMenuToggle').addEventListener('click', function() {
            const mobileMenu = document.getElementById('mobileMenu');
            const overlay = document.getElementById('mobileMenuOverlay');
            mobileMenu.classList.toggle('mobile-menu-open');
            this.classList.toggle('active');
            if (overlay) {
                overlay.classList.toggle('active');
            }
        });
                

				// Simple i18n for dashboard (EN, Bisaya, 中文)
				(function() {
					const dict = {
						en: {
							Monitor: 'Home', Link: 'Link', History: 'History', Rewards: 'Rewards', Profile: 'Profile', Logout: 'Logout',
							LiveStatus: 'Live Status', LiveDesc: 'Real-time charging station information',
							SystemStatus: 'System Status', AllOperational: 'All system operational',
							CurrentQueue: 'Current Queue', VehiclesWaiting: 'vehicles waiting',
							ActiveSessions: 'Active Sessions', ChargingNow: 'charging now',
							EstWait: 'Estimated wait time:', AvgSess: 'Average session duration:',
							VehicleStatus: 'Vehicle Status', VehicleDesc: "Monitor your electric vehicle's charging status and performance",
							BatteryHealthMonitor: 'Battery Health Monitor', RangeCalculator: 'Range Calculator', EstimatedCost: 'Estimated Cost', VehicleDiagnostics: 'Vehicle Diagnostics',
							RewardsWallet: 'Rewards & Wallet', RewardsWalletDesc: 'Manage your rewards and wallet balance',
							RecentActivity: 'Recent Activity', RecentDesc: 'Your latest charging sessions and transactions'
						},
						fil: {
							Monitor: 'Monitor', Link: 'Link', History: 'Kasaysayan', Rewards: 'Rewards', Profile: 'Profile', Logout: 'Mag-logout',
							LiveStatus: 'Live Status', LiveDesc: 'Impormasyong real-time ng charging station',
							SystemStatus: 'Katayuan ng Sistema', AllOperational: 'Maayos ang lahat ng sistema',
							CurrentQueue: 'Kasalukuyang Pila', VehiclesWaiting: 'sasakyang naghihintay',
							ActiveSessions: 'Aktibong Session', ChargingNow: 'kasalukuyang nagcha-charge',
							EstWait: 'Tinatayang oras ng paghihintay:', AvgSess: 'Karaniwang tagal ng session:',
							VehicleStatus: 'Katayuan ng Sasakyan', VehicleDesc: 'Subaybayan ang estado at performance ng iyong EV',
							BatteryHealthMonitor: 'Kalusugan ng Baterya', RangeCalculator: 'Range Calculator', EstimatedCost: 'Tantyang Gastos', VehicleDiagnostics: 'Diagnostics ng Sasakyan',
							RewardsWallet: 'Rewards at Wallet', RewardsWalletDesc: 'Pamahalaan ang iyong rewards at balanse',
							RecentActivity: 'Kamakailang Aktibidad', RecentDesc: 'Pinakabagong charging sessions at transaksyon'
						},
						ceb: {
							Monitor: 'Monitor', Link: 'Link', History: 'Kasaysayan', Rewards: 'Rewards', Profile: 'Profile', Logout: 'Gawas',
							LiveStatus: 'Buhi nga Kahimtang', LiveDesc: 'Tinuod‑panahong impormasyon sa charging station',
							SystemStatus: 'Kahimtang sa Sistema', AllOperational: 'Tanan sistema nagdagan',
							CurrentQueue: 'Karon nga Linya', VehiclesWaiting: 'sakyanan naghulat',
							ActiveSessions: 'Aktibong mga Sesyon', ChargingNow: 'nag‑charge karon',
							EstWait: 'Gibanabana nga paghulat:', AvgSess: 'Average nga gikatigayon sa sesyon:',
							VehicleStatus: 'Kahimtang sa Salakyanan', VehicleDesc: 'Subaya ang kahimtang sa imong EV ug performance',
							BatteryHealthMonitor: 'Kahimsog sa Baterya', RangeCalculator: 'Kalkulasyon sa Gilay-on', EstimatedCost: 'Gibanabana nga Gasto', VehicleDiagnostics: 'Diagnostics sa Salakyanan',
							RewardsWallet: 'Ganti & Wallet', RewardsWalletDesc: 'Dumala ang imong ganti ug balanse sa wallet',
							RecentActivity: 'Bag-ong Kalihokan', RecentDesc: 'Pinakabag-ong mga sesyon ug transaksiyon'
						},
						zh: {
							Monitor: '监控', Link: '连接', History: '历史', Rewards: '奖励', Profile: '资料', Logout: '登出',
							LiveStatus: '实时状态', LiveDesc: '充电站实时信息',
							SystemStatus: '系统状态', AllOperational: '系统正常运行',
							CurrentQueue: '当前排队', VehiclesWaiting: '辆等待中',
							ActiveSessions: '进行中的会话', ChargingNow: '正在充电',
							EstWait: '预计等待时间：', AvgSess: '平均会话时长：',
							VehicleStatus: '车辆状态', VehicleDesc: '监控您的电动车充电状态与性能',
							BatteryHealthMonitor: '电池健康监控', RangeCalculator: '续航计算器', EstimatedCost: '费用估算', VehicleDiagnostics: '车辆诊断',
							RewardsWallet: '奖励与钱包', RewardsWalletDesc: '管理您的奖励与钱包余额',
							RecentActivity: '近期活动', RecentDesc: '您最近的充电会话与交易'
						}
					};
					function translateDashboard() {
						const lang = localStorage.getItem('selectedLanguage') || 'en';
						const t = dict[lang] || dict.en;
						// Top navigation
						const nav = document.querySelectorAll('.nav-list .nav-link');
						if (nav[0]) nav[0].textContent = t.Monitor;
						if (nav[1]) nav[1].textContent = t.Link;
						if (nav[2]) nav[2].textContent = t.History;
						if (nav[3]) nav[3].textContent = t.Rewards;
						const logout = document.querySelector('.mobile-auth-link');
						if (logout) logout.textContent = t.Logout;
						// Live status
						const lsTitle = document.querySelector('.live-status .section-title');
						const lsDesc = document.querySelector('.live-status .section-description');
						if (lsTitle) lsTitle.textContent = t.LiveStatus;
						if (lsDesc) lsDesc.textContent = t.LiveDesc;
						const cards = document.querySelectorAll('.status-card');
						if (cards[0]) {
							cards[0].querySelector('.status-title').textContent = t.SystemStatus;
							const op = cards[0].querySelector('.status-text');
							if (op) op.textContent = t.AllOperational;
						}
						if (cards[1]) {
							cards[1].querySelector('.status-title').textContent = t.CurrentQueue;
							const lbl = cards[1].querySelector('.queue-label');
							if (lbl) lbl.textContent = t.VehiclesWaiting;
							const est = cards[1].querySelector('.status-description');
							if (est) est.firstChild.textContent = `${t.EstWait} `;
						}
						if (cards[2]) {
							cards[2].querySelector('.status-title').textContent = t.ActiveSessions;
							const lbl = cards[2].querySelector('.session-label');
							if (lbl) lbl.textContent = t.ChargingNow;
							const avg = cards[2].querySelector('.status-description');
							if (avg) avg.firstChild.textContent = `${t.AvgSess} `;
						}
						// Section headers
						const vsTitle = document.querySelector('.features .section-title');
						const vsDesc = document.querySelector('.features .section-description');
						if (vsTitle) vsTitle.textContent = t.VehicleStatus;
						if (vsDesc) vsDesc.textContent = t.VehicleDesc;
						const rwTitle = document.querySelector('.rewards-wallet .section-title');
						const rwDesc = document.querySelector('.rewards-wallet .section-description');
						if (rwTitle) rwTitle.textContent = t.RewardsWallet;
						if (rwDesc) rwDesc.textContent = t.RewardsWalletDesc;
						const raTitle = document.querySelector('.recent-activity .section-title');
						const raDesc = document.querySelector('.recent-activity .section-description');
						if (raTitle) raTitle.textContent = t.RecentActivity;
						if (raDesc) raDesc.textContent = t.RecentDesc;
					}
					window.translateDashboard = translateDashboard;
				})();

				// Initialize dashboard features
                $(document).ready(function() {
                    loadDashboardStats();
                    updateLiveStatus();
					// Apply saved language translations
					setTimeout(() => { try { window.translateDashboard(); } catch(e){} }, 0);

                    // Intersection Observer for animations
                    const observerOptions = {
                        threshold: 0.1,
                        rootMargin: '0px 0px -50px 0px'
                    };

                    const observer = new IntersectionObserver((entries) => {
                        entries.forEach(entry => {
                            if (entry.isIntersecting) {
                                entry.target.classList.add('animate-in');
                            }
                        });
                    }, observerOptions);

                    // Observe all feature cards and sections
                    document.querySelectorAll('.feature-card, .status-card, .stat-card, .promo-card, .charging-card').forEach(el => {
                        observer.observe(el);
                    });

                    // Add click handlers for modal triggers
                    $(document).on('click', function(e) {
                        // Close modals when clicking outside
                        if (e.target.classList.contains('modal-overlay')) {
                            closeStationsModal();
                            closeScheduleModal();
                            closeSupportModal();
                        }
                    });

                    // Add keyboard support for modals
                    $(document).on('keydown', function(e) {
                        if (e.key === 'Escape') {
                            closeStationsModal();
                            closeScheduleModal();
                            closeSupportModal();
                        }
                    });
                });
            </script>