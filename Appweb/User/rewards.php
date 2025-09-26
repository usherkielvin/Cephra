<?php
session_start();
if (!isset($_SESSION['username'])) {
    header("Location: index.php");
    exit();
}
require_once 'config/database.php';
$db = new Database();
$conn = $db->getConnection();

$total_points = 0;
if ($conn) {
    $username = $_SESSION['username'];
    $stmt = $conn->prepare("SELECT total_points FROM user_points WHERE username = :username");
    $stmt->bindParam(':username', $username);
    $stmt->execute();
    $result = $stmt->fetch(PDO::FETCH_ASSOC);
    $total_points = $result ? (int)$result['total_points'] : 0;
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Rewards - Cephra</title>
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
            --gradient-primary: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%);
            --gradient-secondary: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
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
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

        .header-content {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 1rem 0;
            width: 100%;
            position: relative;
        }

        .points-display {
            position: absolute;
            top: 50%;
            right: 20px;
            transform: translateY(-50%);
            background: var(--gradient-primary);
            color: white;
            padding: 0.75rem 1.5rem;
            border-radius: 25px;
            font-weight: 600;
            font-size: 1rem;
            display: flex;
            align-items: center;
            gap: 0.5rem;
            box-shadow: 0 5px 15px var(--shadow-medium);
            white-space: nowrap;
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

        .rewards-hero {
            background: var(--gradient-secondary);
            padding: 120px 0 80px;
            text-align: center;
            position: relative;
            overflow: hidden;
        }

        .rewards-hero::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: radial-gradient(circle at 20% 80%, rgba(0, 194, 206, 0.15) 0%, transparent 50%);
            z-index: 1;
        }

        .rewards-greeting {
            font-size: clamp(2.5rem, 6vw, 4rem);
            font-weight: 900;
            line-height: 1.1;
            margin-bottom: 1rem;
            position: relative;
            z-index: 2;
        }

        .rewards-greeting-main {
            display: block;
            background: var(--gradient-primary);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        .section-header {
            text-align: center;
            margin-bottom: 60px;
        }

        .section-title {
            font-size: 2.5rem;
            font-weight: 700;
            margin-bottom: 1rem;
            background: var(--gradient-primary);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        .section-description {
            font-size: 1.2rem;
            color: var(--text-secondary);
            max-width: 600px;
            margin: 0 auto;
        }

        .rewards-section {
            padding: 80px 0;
            background: var(--bg-secondary);
        }

        .category {
            margin-bottom: 4rem;
        }

        .category h3 {
            font-size: 2rem;
            font-weight: 700;
            color: var(--text-primary);
            margin-bottom: 2rem;
            text-align: center;
        }

        .swipe-container {
            overflow-x: auto;
            overflow-y: hidden;
            scrollbar-width: none;
            -ms-overflow-style: none;
            scroll-snap-type: x mandatory;
            -webkit-overflow-scrolling: touch;
            padding: 1rem 0;
        }

        .swipe-container::-webkit-scrollbar {
            display: none;
        }

        .swipe-track {
            display: flex;
            gap: 1.5rem;
            padding: 0 1rem;
            min-width: max-content;
        }

        .reward-card {
            background: var(--bg-card);
            border-radius: 20px;
            padding: 1.5rem;
            text-align: center;
            min-width: 160px;
            flex-shrink: 0;
            border: 1px solid var(--border-color);
            transition: all 0.3s ease;
            box-shadow: 0 5px 15px var(--shadow-light);
            scroll-snap-align: start;
        }

        .reward-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 30px var(--shadow-medium);
            border-color: var(--primary-color);
        }

        .reward-card img {
            width: 80px;
            height: 80px;
            border-radius: 15px;
            object-fit: cover;
            margin-bottom: 1rem;
            background: var(--gradient-primary);
            padding: 1rem;
        }

        .reward-card h4 {
            font-size: 1.2rem;
            font-weight: 600;
            color: var(--text-primary);
            margin-bottom: 0.5rem;
        }

        .reward-card p {
            font-size: 1.1rem;
            font-weight: 700;
            color: var(--primary-color);
            margin-bottom: 1rem;
        }

        .redeem-btn {
            background: var(--gradient-primary);
            color: white;
            border: none;
            padding: 0.75rem 1.5rem;
            border-radius: 25px;
            cursor: pointer;
            font-weight: 600;
            transition: all 0.3s ease;
            width: 100%;
        }

        .redeem-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px var(--shadow-medium);
        }

        .footer {
            background: var(--primary-dark);
            color: white;
            padding: 3rem 0 1rem;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 1rem;
        }

        .footer-content {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 2rem;
            margin-bottom: 2rem;
        }

        .footer-logo-img {
            width: 40px;
            height: 40px;
            border-radius: 10px;
            object-fit: cover;
        }

        .footer-logo-text {
            font-size: 24px;
            font-weight: 800;
            color: white;
            letter-spacing: 1px;
        }

        .footer-description {
            color: rgba(255, 255, 255, 0.8);
            line-height: 1.6;
        }

        .footer-title {
            font-size: 1.2rem;
            font-weight: 600;
            margin-bottom: 1rem;
            color: white;
        }

        .footer-links {
            list-style: none;
            padding: 0;
        }

        .footer-links li {
            margin-bottom: 0.5rem;
        }

        .footer-links a {
            color: rgba(255, 255, 255, 0.8);
            text-decoration: none;
            transition: color 0.3s ease;
        }

        .footer-links a:hover {
            color: var(--primary-color);
        }

        .footer-bottom {
            text-align: center;
            padding-top: 2rem;
            border-top: 1px solid rgba(255, 255, 255, 0.1);
            color: rgba(255, 255, 255, 0.6);
        }

        .footer-bottom a {
            color: var(--primary-color);
            text-decoration: none;
        }

        @media (max-width: 768px) {
            .nav-list {
                display: none;
            }

            .header-actions {
                display: none;
            }

            .mobile-menu-toggle {
                display: flex;
            }

            .points-display {
                position: static;
                transform: none;
                right: auto;
                margin-left: auto;
                order: 3;
            }

            .header-content {
                flex-wrap: wrap;
            }

            .swipe-track {
                gap: 1rem;
            }

            .reward-card {
                min-width: 140px;
                padding: 1rem;
            }

            .reward-card img {
                width: 60px;
                height: 60px;
            }
        }
    </style>
</head>
<body>
    <?php include __DIR__ . '/partials/header.php'; ?>

    <!-- Rewards Hero Section (show points inside panel, not navbar) -->
    <section class="rewards-hero">
        <div class="container">
            <h1 class="rewards-greeting">
                <span class="rewards-greeting-main">Unlock Rewards</span>
            </h1>
            <p style="font-size: 1.2rem; color: var(--text-secondary); max-width: 600px; margin: 0 auto; position: relative; z-index: 2;">
                Earn and redeem points for exclusive perks as you charge with Cephra.
            </p>
            <div style="margin-top:16px;display:flex;justify-content:center;">
                <div class="points-display" style="position:static;background: var(--gradient-primary);color:#fff;padding:0.75rem 1.5rem;border-radius:25px;font-weight:600;box-shadow:0 5px 15px var(--shadow-medium);">
                    <i class="fas fa-star" style="margin-right:8px"></i>
                    <?php echo $total_points; ?> pts
                </div>
            </div>
        </div>
    </section>

    <!-- Rewards Section -->
    <section class="rewards-section">
        <div class="container">
            <div class="section-header">
                <h2 class="section-title">Reward Categories</h2>
                <p class="section-description">Swipe to explore available rewards in each category</p>
            </div>

            <!-- Essentials Category -->
            <div class="category">
                <h3>Exclusive Essentials</h3>
                <div class="swipe-container" id="essentials-swipe">
                    <div class="swipe-track">
                        <div class="reward-card">
                            <img src="images/logo.png" alt="Powerbank">
                            <h4>Powerbank</h4>
                            <p>50 pts</p>
                            <button class="redeem-btn">Redeem</button>
                        </div>
                        <div class="reward-card">
                            <img src="images/logo.png" alt="Umbrella">
                            <h4>Umbrella</h4>
                            <p>35 pts</p>
                            <button class="redeem-btn">Redeem</button>
                        </div>
                        <div class="reward-card">
                            <img src="images/logo.png" alt="Charger">
                            <h4>Charger</h4>
                            <p>40 pts</p>
                            <button class="redeem-btn">Redeem</button>
                        </div>
                        <div class="reward-card">
                            <img src="images/logo.png" alt="Case">
                            <h4>Case</h4>
                            <p>30 pts</p>
                            <button class="redeem-btn">Redeem</button>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Wearables Category -->
            <div class="category">
                <h3>Exclusive Wearables</h3>
                <div class="swipe-container" id="wearables-swipe">
                    <div class="swipe-track">
                        <div class="reward-card">
                            <img src="images/logo.png" alt="T-Shirt">
                            <h4>T-Shirt</h4>
                            <p>70 pts</p>
                            <button class="redeem-btn">Redeem</button>
                        </div>
                        <div class="reward-card">
                            <img src="images/logo.png" alt="Hoodie">
                            <h4>Hoodie</h4>
                            <p>100 pts</p>
                            <button class="redeem-btn">Redeem</button>
                        </div>
                        <div class="reward-card">
                            <img src="images/logo.png" alt="Cap">
                            <h4>Cap</h4>
                            <p>50 pts</p>
                            <button class="redeem-btn">Redeem</button>
                        </div>
                        <div class="reward-card">
                            <img src="images/logo.png" alt="Socks">
                            <h4>Socks</h4>
                            <p>25 pts</p>
                            <button class="redeem-btn">Redeem</button>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Sips Category -->
            <div class="category">
                <h3>Refreshing Sips</h3>
                <div class="swipe-container" id="sips-swipe">
                    <div class="swipe-track">
                        <div class="reward-card">
                            <img src="images/logo.png" alt="Coffee">
                            <h4>Coffee</h4>
                            <p>20 pts</p>
                            <button class="redeem-btn">Redeem</button>
                        </div>
                        <div class="reward-card">
                            <img src="images/logo.png" alt="Tea">
                            <h4>Tea</h4>
                            <p>15 pts</p>
                            <button class="redeem-btn">Redeem</button>
                        </div>
                        <div class="reward-card">
                            <img src="images/logo.png" alt="Juice">
                            <h4>Juice</h4>
                            <p>18 pts</p>
                            <button class="redeem-btn">Redeem</button>
                        </div>
                        <div class="reward-card">
                            <img src="images/logo.png" alt="Water">
                            <h4>Water</h4>
                            <p>10 pts</p>
                            <button class="redeem-btn">Redeem</button>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Boost Category -->
            <div class="category">
                <h3>Energy Boost</h3>
                <div class="swipe-container" id="boost-swipe">
                    <div class="swipe-track">
                        <div class="reward-card">
                            <img src="images/logo.png" alt="Energy Drink">
                            <h4>Energy Drink</h4>
                            <p>50 pts</p>
                            <button class="redeem-btn">Redeem</button>
                        </div>
                        <div class="reward-card">
                            <img src="images/logo.png" alt="Vitamin">
                            <h4>Vitamin</h4>
                            <p>30 pts</p>
                            <button class="redeem-btn">Redeem</button>
                        </div>
                        <div class="reward-card">
                            <img src="images/logo.png" alt="Snack">
                            <h4>Snack</h4>
                            <p>25 pts</p>
                            <button class="redeem-btn">Redeem</button>
                        </div>
                        <div class="reward-card">
                            <img src="images/logo.png" alt="Boost Pack">
                            <h4>Boost Pack</h4>
                            <p>80 pts</p>
                            <button class="redeem-btn">Redeem</button>
                        </div>
                    </div>
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

    <!-- Scripts -->
    <script src="assets/js/jquery.min.js"></script>
    <script src="assets/js/jquery.dropotron.min.js"></script>
    <script src="assets/js/browser.min.js"></script>
    <script src="assets/js/breakpoints.min.js"></script>
    <script src="assets/js/util.js"></script>
    <script src="assets/js/main.js"></script>
    <script>
        // Mobile Menu Toggle
        function initMobileMenu() {
            const mobileMenuToggle = document.getElementById('mobileMenuToggle');
            const mobileMenu = document.getElementById('mobileMenu');
            const mobileMenuOverlay = document.createElement('div');
            mobileMenuOverlay.className = 'mobile-menu-overlay';
            mobileMenuOverlay.id = 'mobileMenuOverlay';
            document.body.appendChild(mobileMenuOverlay);

            function toggleMobileMenu() {
                const isActive = mobileMenu.classList.contains('active');
                if (isActive) {
                    closeMobileMenu();
                } else {
                    openMobileMenu();
                }
            }

            function openMobileMenu() {
                mobileMenu.classList.add('active');
                mobileMenuToggle.classList.add('active');
                mobileMenuOverlay.classList.add('active');
                document.body.style.overflow = 'hidden';
                mobileMenuOverlay.addEventListener('click', closeMobileMenu);
                document.addEventListener('keydown', handleEscapeKey);
            }

            function closeMobileMenu() {
                mobileMenu.classList.remove('active');
                mobileMenuToggle.classList.remove('active');
                mobileMenuOverlay.classList.remove('active');
                document.body.style.overflow = '';
                mobileMenuOverlay.removeEventListener('click', closeMobileMenu);
                document.removeEventListener('keydown', handleEscapeKey);
            }

            function handleEscapeKey(e) {
                if (e.key === 'Escape') {
                    closeMobileMenu();
                }
            }

            mobileMenuToggle.addEventListener('click', toggleMobileMenu);
            const mobileNavLinks = document.querySelectorAll('.mobile-nav-link');
            mobileNavLinks.forEach(link => link.addEventListener('click', closeMobileMenu));

            document.addEventListener('click', function(e) {
                if (window.innerWidth <= 768) {
                    if (!mobileMenu.contains(e.target) && !mobileMenuToggle.contains(e.target)) {
                        if (mobileMenu.classList.contains('active')) {
                            closeMobileMenu();
                        }
                    }
                }
            });
        }

        // Swipe Functionality
        document.addEventListener('DOMContentLoaded', function() {
            initMobileMenu();

            const swipeContainers = document.querySelectorAll('.swipe-container');
            swipeContainers.forEach(container => {
                let startX = 0;
                let currentX = 0;

                container.addEventListener('touchstart', function(e) {
                    startX = e.touches[0].clientX;
                    currentX = container.scrollLeft;
                });

                container.addEventListener('touchmove', function(e) {
                    if (!startX) return;
                    const deltaX = startX - e.touches[0].clientX;
                    container.scrollLeft = currentX + deltaX * 0.8; // Smooth scrolling factor
                });

                container.addEventListener('touchend', function() {
                    startX = 0;
                });

                // Mouse support for desktop
                container.addEventListener('mousedown', function(e) {
                    startX = e.clientX;
                    currentX = container.scrollLeft;
                    container.style.cursor = 'grabbing';
                });

                container.addEventListener('mousemove', function(e) {
                    if (startX === 0) return;
                    const deltaX = startX - e.clientX;
                    container.scrollLeft = currentX + deltaX * 0.8;
                });

                container.addEventListener('mouseup', function() {
                    startX = 0;
                    container.style.cursor = 'grab';
                });

                container.addEventListener('mouseleave', function() {
                    startX = 0;
                    container.style.cursor = 'default';
                });

                // Set grab cursor
                container.style.cursor = 'grab';
            });

            // Redeem button handlers (placeholder)
            document.querySelectorAll('.redeem-btn').forEach(btn => {
                btn.addEventListener('click', function() {
                    alert('Reward redeemed! Points deducted from your total.');
                });
            });
        });

        // Header scroll effect
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
