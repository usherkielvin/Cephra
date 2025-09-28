<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
require_once __DIR__ . '/../config/database.php';
$db = new Database();
$conn = $db->getConnection();
$username = isset($_SESSION['username']) ? $_SESSION['username'] : '';

// Compute avatar source
$pfpSrc = 'images/logo.png';
if ($username && $conn) {
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
    display: none;
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
    z-index: 999;
    transition: right 0.3s ease;
    box-shadow: -5px 0 20px rgba(0, 0, 0, 0.1);
}

.mobile-menu.active {
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
                    <li><a href="#" onclick="openMonitorWeb(); return false;" class="nav-link">Monitor</a></li>
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
                <a href="wallet.php"
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
                                <li><a href="#" onclick="setLanguage('en'); return false;" style="display: block; padding: 8px 12px; color: #333; text-decoration: none; transition: background 0.2s;">English</a></li>
                                <li><a href="#" onclick="setLanguage('fil'); return false;" style="display: block; padding: 8px 12px; color: #333; text-decoration: none; transition: background 0.2s;">Filipino</a></li>
                                <li><a href="#" onclick="setLanguage('ceb'); return false;" style="display: block; padding: 8px 12px; color: #333; text-decoration: none; transition: background 0.2s;">Bisaya</a></li>
                                <li><a href="#" onclick="setLanguage('zh'); return false;" style="display: block; padding: 8px 12px; color: #333; text-decoration: none; transition: background 0.2s;">中文</a></li>
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
            <ul class="mobile-nav-list">
                <li><a href="#" onclick="openMonitorWeb(); return false;" class="mobile-nav-link">Monitor</a></li>
                <li><a href="link.php" class="mobile-nav-link">Link</a></li>
                <li><a href="history.php" class="mobile-nav-link">History</a></li>
                <li><a href="rewards.php" class="mobile-nav-link">Rewards</a></li>
                <li><a href="wallet.php" class="mobile-nav-link">Wallet</a></li>
            </ul>
            <div class="mobile-header-actions">
                <a href="profile_logout.php" class="mobile-auth-link">Logout</a>
            </div>
        </div>
    </div>
</header>

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
})();

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
    // Close dropdowns
    if (profileDropdown) profileDropdown.style.display = 'none';
    if (languageSubDropdown) languageSubDropdown.style.display = 'none';
    if (languageDropdown) languageDropdown.style.display = 'none';
};

// Mobile Menu Toggle Functionality
function initMobileMenu() {
    const mobileMenuToggle = document.getElementById('mobileMenuToggle');
    const mobileMenu = document.getElementById('mobileMenu');
    const mobileMenuOverlay = document.createElement('div');
    mobileMenuOverlay.className = 'mobile-menu-overlay';
    mobileMenuOverlay.id = 'mobileMenuOverlay';
    document.body.appendChild(mobileMenuOverlay);

    // Toggle mobile menu
    function toggleMobileMenu() {
        const isActive = mobileMenu.classList.contains('active');

        if (isActive) {
            closeMobileMenu();
        } else {
            openMobileMenu();
        }
    }

    // Open mobile menu
    function openMobileMenu() {
        mobileMenu.classList.add('active');
        mobileMenuToggle.classList.add('active');
        mobileMenuOverlay.classList.add('active');
        document.body.style.overflow = 'hidden';

        // Add click handlers
        mobileMenuOverlay.addEventListener('click', closeMobileMenu);
        document.addEventListener('keydown', handleEscapeKey);
    }

    // Close mobile menu
    function closeMobileMenu() {
        mobileMenu.classList.remove('active');
        mobileMenuToggle.classList.remove('active');
        mobileMenuOverlay.classList.remove('active');
        document.body.style.overflow = '';

        // Remove event listeners
        mobileMenuOverlay.removeEventListener('click', closeMobileMenu);
        document.removeEventListener('keydown', handleEscapeKey);
    }

    // Handle escape key
    function handleEscapeKey(e) {
        if (e.key === 'Escape') {
            closeMobileMenu();
        }
    }

    // Add click handler to toggle button
    mobileMenuToggle.addEventListener('click', toggleMobileMenu);

    // Add click handlers to mobile menu links
    const mobileNavLinks = document.querySelectorAll('.mobile-nav-link');
    mobileNavLinks.forEach(link => {
        link.addEventListener('click', closeMobileMenu);
    });

    // Close menu when clicking outside on mobile
    $(document).on('click', function(e) {
        if (window.innerWidth <= 768) {
            if (!mobileMenu.contains(e.target) && !mobileMenuToggle.contains(e.target)) {
                if (mobileMenu.classList.contains('active')) {
                    closeMobileMenu();
                }
            }
        }
    });
}

// Initialize mobile menu
initMobileMenu();
</script>


