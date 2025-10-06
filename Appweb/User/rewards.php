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
            padding: 40px 0;
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

        .rewards-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 2rem;
            padding: 1rem 0;
        }

        .reward-card {
            background: var(--bg-card);
            border-radius: 20px;
            padding: 2rem;
            text-align: center;
            border: 1px solid var(--border-color);
            transition: all 0.3s ease;
            box-shadow: 0 5px 15px var(--shadow-light);
        }

        .reward-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 30px var(--shadow-medium);
            border-color: var(--primary-color);
        }

        .reward-card img {
            width: 150px;
            height: 150px;
            border-radius: 15px;
            object-fit: contain;
            object-position: center;
            margin-bottom: 1rem;
            background: transparent;
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

        /* Modal Styles */
        .modal {
            display: none;
            position: fixed;
            z-index: 10000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            backdrop-filter: blur(5px);
        }

        .modal-content {
            background-color: var(--bg-card);
            margin: 15% auto;
            padding: 2rem;
            border: 1px solid var(--border-color);
            border-radius: 20px;
            width: 90%;
            max-width: 500px;
            box-shadow: 0 10px 30px var(--shadow-medium);
            text-align: center;
            position: relative;
        }

        .close-modal {
            position: absolute;
            top: 15px;
            right: 20px;
            color: var(--text-secondary);
            font-size: 28px;
            font-weight: bold;
            cursor: pointer;
            transition: color 0.3s ease;
        }

        .close-modal:hover {
            color: var(--primary-color);
        }

        .modal h2 {
            color: var(--text-primary);
            margin-bottom: 1rem;
            font-size: 1.5rem;
        }

        .modal p {
            color: var(--text-secondary);
            margin-bottom: 1rem;
            line-height: 1.6;
        }

        .modal-buttons {
            display: flex;
            gap: 1rem;
            justify-content: center;
            margin-top: 2rem;
        }

        .modal-btn {
            padding: 0.75rem 1.5rem;
            border: none;
            border-radius: 25px;
            cursor: pointer;
            font-weight: 600;
            transition: all 0.3s ease;
            flex: 1;
            max-width: 200px;
        }

        .pickup {
            background: var(--gradient-primary);
            color: white;
        }

        .pickup:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px var(--shadow-medium);
        }

        .deliver {
            background: var(--bg-secondary);
            color: var(--text-primary);
            border: 1px solid var(--border-color);
        }

        .deliver:hover {
            background: var(--text-primary);
            color: white;
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
                margin: 0 auto;
                order: 3;
                display: flex;
                justify-content: center;
                align-items: center;
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
                width: 100px;
                height: 100px;
                object-fit: contain;
                object-position: center;
                background: transparent;
            }

            .modal-content {
                margin: 20% auto;
                padding: 1.5rem;
            }

            .modal-buttons {
                flex-direction: column;
                gap: 0.5rem;
            }

            .modal-btn {
                width: 100%;
            }
        }

        @media (max-width: 370px) {
            .rewards-grid {
                grid-template-columns: 1fr;
                gap: 0.75rem;
            }
            .reward-card {
                padding: 0.75rem;
            }
            .reward-card h4 {
                font-size: 0.9rem;
            }
            .reward-card p {
                font-size: 0.75rem;
            }
            .redeem-btn {
                padding: 0.4rem 0.8rem;
                font-size: 0.75rem;
            }
            .rewards-section {
                padding: 50px 0;
            }
            .section-header h2 {
                font-size: 2rem;
            }
            .section-header p {
                font-size: 1rem;
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
            <div style="margin-top:4rem;display:flex;justify-content:center;">
                <div class="points-display" style="position:static;background: var(--gradient-primary);color:#fff;padding:1rem 1.75rem;border-radius:25px;font-weight:600;box-shadow:0 5px 15px var(--shadow-medium);">
                    <i class="fas fa-star" style="margin-right:12px;font-size:1.1rem;"></i>
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
                <p class="section-description">Explore available rewards in each category</p>
            </div>

            <!-- Essentials Category -->
            <div class="category">
                <h3>Exclusive Essentials</h3>
                <div class="rewards-grid">
                    <div class="reward-card">
                        <img src="images/rewards/powerbank.png" alt="Powerbank">
                        <h4>Powerbank</h4>
                        <p>50 pts</p>
                        <button class="redeem-btn">Redeem</button>
                    </div>
                    <div class="reward-card">
                        <img src="images/rewards/umbrella.png" alt="Umbrella">
                        <h4>Umbrella</h4>
                        <p>35 pts</p>
                        <button class="redeem-btn">Redeem</button>
                    </div>
                    <div class="reward-card">
                        <img src="images/rewards/charger.png" alt="Charger">
                        <h4>Charger</h4>
                        <p>40 pts</p>
                        <button class="redeem-btn">Redeem</button>
                    </div>
                    <div class="reward-card">
                        <img src="images/rewards/case.png" alt="Case">
                        <h4>Phone Case</h4>
                        <p>30 pts</p>
                        <button class="redeem-btn">Redeem</button>
                    </div>
                </div>
            </div>

            <!-- Wearables Category -->
            <div class="category">
                <h3>Exclusive Wearables</h3>
                <div class="rewards-grid">
                    <div class="reward-card">
                        <img src="images/rewards/tshirt.png" alt="T-Shirt">
                        <h4>T-Shirt</h4>
                        <p>70 pts</p>
                        <button class="redeem-btn">Redeem</button>
                    </div>
                    <div class="reward-card">
                        <img src="images/rewards/hoodie.png" alt="Hoodie">
                        <h4>Hoodie</h4>
                        <p>100 pts</p>
                        <button class="redeem-btn">Redeem</button>
                    </div>
                    <div class="reward-card">
                        <img src="images/rewards/cap.png" alt="Cap">
                        <h4>Cap</h4>
                        <p>50 pts</p>
                        <button class="redeem-btn">Redeem</button>
                    </div>
                    <div class="reward-card">
                        <img src="images/rewards/socks.jpeg" alt="Socks">
                        <h4>Socks</h4>
                        <p>25 pts</p>
                        <button class="redeem-btn">Redeem</button>
                    </div>
                </div>
            </div>

            <!-- Sips Category -->
            <div class="category">
                <h3>Refreshing Sips</h3>
                <div class="rewards-grid">
                    <div class="reward-card">
                        <img src="images/rewards/coffee.png" alt="Coffee">
                        <h4>Coffee</h4>
                        <p>20 pts</p>
                        <button class="redeem-btn">Redeem</button>
                    </div>
                    <div class="reward-card">
                        <img src="images/rewards/edrink.png" alt="Tea">
                        <h4>Energy Drink</h4>
                        <p>15 pts</p>
                        <button class="redeem-btn">Redeem</button>
                    </div>
                    <div class="reward-card">
                        <img src="images/rewards/juice.png" alt="Juice">
                        <h4>Juice</h4>
                        <p>18 pts</p>
                        <button class="redeem-btn">Redeem</button>
                    </div>
                    <div class="reward-card">
                        <img src="images/rewards/water.png" alt="Water">
                        <h4>Water</h4>
                        <p>10 pts</p>
                        <button class="redeem-btn">Redeem</button>
                    </div>
                </div>
            </div>

            <!-- Tools Category -->
            <div class="category">
                <h3>Tools</h3>
                <div class="rewards-grid">
                    <div class="reward-card">
                        <img src="images/rewards/flashlight.png" alt="Flashlight">
                        <h4>Flashlight</h4>
                        <p>25 pts</p>
                        <button class="redeem-btn">Redeem</button>
                    </div>
                    <div class="reward-card">
                        <img src="images/rewards/hammer.png" alt="Hammer">
                        <h4>Hammer</h4>
                        <p>25 pts</p>
                        <button class="redeem-btn">Redeem</button>
                    </div>
                    <div class="reward-card">
                        <img src="images/rewards/ratchet.png" alt="Ratchet Set">
                        <h4>Ratchet Set</h4>
                        <p>60 pts</p>
                        <button class="redeem-btn">Redeem</button>
                    </div>
                    <div class="reward-card">
                        <img src="images/rewards/screwdriver.png" alt="Screwdriver">
                        <h4>Screwdriver</h4>
                        <p>25 pts</p>
                        <button class="redeem-btn">Redeem</button>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Redeem Success Modal -->
    <div id="redeem-modal" class="modal">
        <div class="modal-content">
            <span class="close-modal">&times;</span>
            <h2>Reward Redeemed Successfully!</h2>
            <p>Your points have been deducted and the reward is now yours.</p>
            <p>How would you like to receive your reward?</p>
            <div class="modal-buttons">
                <button id="pickup-btn" class="modal-btn pickup">Pickup at Store</button>
                <button id="deliver-btn" class="modal-btn deliver">Deliver to My House</button>
            </div>
        </div>
    </div>

    <!-- Success Modal -->
    <div id="success-modal" class="modal">
        <div class="modal-content">
            <span class="close-modal">&times;</span>
            <h2>Order Confirmed!</h2>
            <p id="success-message">Your reward has been processed successfully.</p>
            <div class="modal-buttons">
                <button id="success-ok-btn" class="modal-btn pickup">OK</button>
            </div>
        </div>
    </div>

    <!-- Error Modal -->
    <div id="error-modal" class="modal">
        <div class="modal-content">
            <span class="close-modal">&times;</span>
            <h2>Redeem Failed</h2>
            <p id="error-message">An error occurred while processing your request.</p>
            <div class="modal-buttons">
                <button id="error-ok-btn" class="modal-btn deliver">OK</button>
            </div>
        </div>
    </div>

		<!-- Footer -->
		<?php include __DIR__ . '/partials/footer.php'; ?>

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

        // Initialize page functionality
        document.addEventListener('DOMContentLoaded', function() {
            initMobileMenu();

            // Modal functionality
            const redeemModal = document.getElementById('redeem-modal');
            const successModal = document.getElementById('success-modal');
            const errorModal = document.getElementById('error-modal');
            const closeBtns = document.querySelectorAll('.close-modal');
            const pickupBtn = document.getElementById('pickup-btn');
            const deliverBtn = document.getElementById('deliver-btn');
            const successOkBtn = document.getElementById('success-ok-btn');
            const errorOkBtn = document.getElementById('error-ok-btn');

            let currentReward = null;

            // Redeem button handlers
            document.querySelectorAll('.redeem-btn').forEach(btn => {
                btn.addEventListener('click', function() {
                    // Get reward details from the card
                    const card = this.closest('.reward-card');
                    const rewardName = card.querySelector('h4').textContent;
                    const rewardPointsText = card.querySelector('p').textContent;
                    const pointsRequired = parseInt(rewardPointsText.replace(' pts', ''));

                    // Check if user has enough points first
                    const currentPointsText = document.querySelector('.points-display').textContent;
                    const currentPointsMatch = currentPointsText.match(/(\d+)/);
                    const currentPoints = currentPointsMatch ? parseInt(currentPointsMatch[1]) : 0;

                    if (currentPoints < pointsRequired) {
                        // Show error modal for insufficient points
                        const errorMessage = document.getElementById('error-message');
                        errorMessage.textContent = `You don't have enough points. You need ${pointsRequired} points but you only have ${currentPoints} points.`;
                        errorModal.style.display = 'block';
                        document.body.style.overflow = 'hidden';
                        return;
                    }

                    currentReward = {
                        name: rewardName,
                        points: pointsRequired
                    };

                    // Show redeem modal
                    redeemModal.style.display = 'block';
                    document.body.style.overflow = 'hidden';
                });
            });

            // Close modal handlers
            closeBtns.forEach(btn => {
                btn.addEventListener('click', function() {
                    redeemModal.style.display = 'none';
                    successModal.style.display = 'none';
                    errorModal.style.display = 'none';
                    document.body.style.overflow = '';
                });
            });

            // Close modal when clicking outside
            [redeemModal, successModal, errorModal].forEach(modal => {
                window.addEventListener('click', function(e) {
                    if (e.target === modal) {
                        modal.style.display = 'none';
                        document.body.style.overflow = '';
                    }
                });
            });

            // Function to handle redemption
            function handleRedemption(deliveryType) {
                if (!currentReward) return;

                // Create form data
                const formData = new FormData();
                formData.append('reward_name', currentReward.name);
                formData.append('points_required', currentReward.points);

                // Make AJAX request
                fetch('redeem_action.php', {
                    method: 'POST',
                    body: formData
                })
                .then(response => response.json())
                .then(data => {
                    redeemModal.style.display = 'none';

                    if (data.success) {
                        // Update points display
                        const pointsDisplay = document.querySelector('.points-display');
                        if (pointsDisplay) {
                            const starIcon = pointsDisplay.querySelector('i');
                            const textNode = pointsDisplay.lastChild;
                            textNode.textContent = ` ${data.new_total} pts`;
                        }

                        // Show success modal with appropriate message
                        const successMessage = document.getElementById('success-message');
                        if (deliveryType === 'pickup') {
                            successMessage.textContent = 'Thank you! Your reward will be ready for pickup at our store within 24 hours.';
                        } else {
                            successMessage.textContent = 'Thank you! Your reward will be delivered to your registered address within 3-5 business days.';
                        }
                        successModal.style.display = 'block';
                    } else {
                        // Show error modal
                        const errorMessage = document.getElementById('error-message');
                        errorMessage.textContent = data.message || 'An error occurred while processing your request.';
                        errorModal.style.display = 'block';
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    redeemModal.style.display = 'none';
                    const errorMessage = document.getElementById('error-message');
                    errorMessage.textContent = 'Network error. Please try again.';
                    errorModal.style.display = 'block';
                });
            }

            // Pickup button handler
            pickupBtn.addEventListener('click', function() {
                handleRedemption('pickup');
            });

            // Deliver button handler
            deliverBtn.addEventListener('click', function() {
                handleRedemption('deliver');
            });

            // Success OK button handler
            successOkBtn.addEventListener('click', function() {
                successModal.style.display = 'none';
                document.body.style.overflow = '';
            });

            // Error OK button handler
            errorOkBtn.addEventListener('click', function() {
                errorModal.style.display = 'none';
                document.body.style.overflow = '';
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
