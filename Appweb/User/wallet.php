<?php
session_start();
if (!isset($_SESSION['username'])) {
    header("Location: index.php");
    exit();
}
require_once 'config/database.php';
$db = new Database();
$conn = $db->getConnection();

$balance = 0.00;
$transactions = [];
if ($conn) {
    $username = $_SESSION['username'];
    // Fetch balance from wallet_balance (shared with Java)
    $stmt = $conn->prepare("SELECT balance FROM wallet_balance WHERE username = :username");
    $stmt->bindParam(':username', $username);
    $stmt->execute();
    $result = $stmt->fetch(PDO::FETCH_ASSOC);
    $balance = $result ? (float)$result['balance'] : 0.00;

    // Fetch latest 3 wallet transactions
    $stmt = $conn->prepare("SELECT transaction_type, amount, description, reference_id, transaction_date FROM wallet_transactions WHERE username = :username ORDER BY transaction_date DESC LIMIT 3");
    $stmt->bindParam(':username', $username);
    $stmt->execute();
    $transactions = $stmt->fetchAll(PDO::FETCH_ASSOC);
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Wallet - Cephra</title>
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

        .wallet-hero {
            background: var(--gradient-secondary);
            padding: 120px 0 80px;
            text-align: center;
            position: relative;
            overflow: hidden;
            animation: fadeInUp 0.6s ease;
        }

        .wallet-hero::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: radial-gradient(circle at 20% 80%, rgba(0, 194, 206, 0.15) 0%, transparent 50%);
            z-index: 1;
        }

        .wallet-greeting {
            font-size: clamp(2.5rem, 6vw, 4rem);
            font-weight: 900;
            line-height: 1.1;
            margin-bottom: 1rem;
            position: relative;
            z-index: 2;
        }

        .wallet-greeting-main {
            display: block;
            background: var(--gradient-primary);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        .balance-container {
            background: var(--gradient-primary);
            padding: 3rem 2rem;
            border-radius: 25px;
            margin: 2rem auto;
            max-width: 500px;
            text-align: center;
            box-shadow: 0 10px 30px var(--shadow-medium), inset 0 0 20px rgba(255, 255, 255, 0.1);
            position: relative;
            animation: fadeInUp 0.6s ease 0.2s both;
        }

        @keyframes pulse {
            0% { transform: scale(1); }
            50% { transform: scale(1.05); }
            100% { transform: scale(1); }
        }

        @keyframes fadeInUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        @keyframes modalScaleIn {
            from {
                transform: scale(0.8);
                opacity: 0;
            }
            to {
                transform: scale(1);
                opacity: 1;
            }
        }

        @keyframes ripple {
            to {
                transform: scale(4);
                opacity: 0;
            }
        }

        .ripple {
            position: absolute;
            border-radius: 50%;
            background-color: rgba(255, 255, 255, 0.6);
            transform: scale(0);
            animation: ripple 0.6s linear;
            pointer-events: none;
        }

        .balance-text {
            font-size: 3rem;
            font-weight: 900;
            color: white;
            margin-bottom: 1rem;
            transition: all 0.3s ease;
            animation: pulse 2s infinite;
        }

        .eye-toggle {
            background: rgba(255, 255, 255, 0.2);
            border: 2px solid white;
            color: white;
            padding: 0.5rem;
            border-radius: 50%;
            cursor: pointer;
            font-size: 1.2rem;
            transition: all 0.3s ease;
            position: absolute;
            top: 1rem;
            right: 1rem;
        }

        .eye-toggle:hover {
            background: rgba(255, 255, 255, 0.3);
        }

        .wallet-buttons {
            display: flex;
            gap: 1rem;
            justify-content: center;
            margin: 2rem 0;
            flex-wrap: wrap;
        }

        .wallet-btn {
            background: var(--bg-card);
            color: var(--primary-color);
            border: 2px solid var(--primary-color);
            padding: 1rem 2rem;
            border-radius: 25px;
            cursor: pointer;
            font-weight: 600;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
            text-align: center;
        }

        .wallet-btn:hover {
            background: var(--primary-color);
            color: white;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px var(--shadow-medium);
        }

        .transactions-section {
            padding: 80px 0;
            background: var(--bg-secondary);
            animation: fadeInUp 0.6s ease 0.4s both;
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

        .trans-list {
            max-width: 1000px;
            margin: 0 auto;
        }

        .trans-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 1.5rem;
            background: var(--bg-card);
            border-radius: 15px;
            margin-bottom: 1rem;
            border: 1px solid var(--border-color);
            box-shadow: 0 5px 15px var(--shadow-light);
            transition: all 0.3s ease;
        }

        .trans-item:nth-child(odd) {
            background: var(--accent-color);
        }

        .trans-item:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 25px var(--shadow-medium);
        }

        .trans-desc {
            font-weight: 600;
            color: var(--text-primary);
        }

        .trans-amount {
            font-weight: 700;
            font-size: 1.1rem;
        }

        .positive {
            color: #4CAF50;
        }

        .negative {
            color: #f44336;
        }

        .trans-date {
            font-size: 0.9rem;
            color: var(--text-muted);
            margin-top: 0.25rem;
        }

        .footer {
            background: var(--primary-dark);
            color: white;
            padding: 3rem 0 1rem;
        }

        .container {
            max-width: 1400px;
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
            z-index: 2000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
            backdrop-filter: blur(5px);
        }

        .modal.show {
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .modal-content {
            background: var(--bg-card);
            border-radius: 20px;
            padding: 2rem;
            max-width: 500px;
            width: 90%;
            max-height: 90vh;
            overflow-y: auto;
            box-shadow: 0 20px 40px var(--shadow-strong);
        }

        .modal-header {
            text-align: center;
            margin-bottom: 2rem;
        }

        .modal-title {
            font-size: 1.5rem;
            font-weight: 700;
            color: var(--text-primary);
            margin-bottom: 1rem;
        }

        .modal-close {
            position: absolute;
            top: 1rem;
            right: 1rem;
            background: none;
            border: none;
            font-size: 1.5rem;
            cursor: pointer;
            color: var(--text-muted);
        }

        .form-group {
            margin-bottom: 1.5rem;
        }

        .form-label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: 600;
            color: var(--text-primary);
        }

        .form-input {
            width: 100%;
            padding: 0.75rem;
            border: 1px solid var(--border-color);
            border-radius: 8px;
            font-size: 1rem;
            transition: border-color 0.3s ease;
        }

        .quick-amounts {
            display: flex;
            gap: 0.5rem;
            margin-top: 1rem;
            flex-wrap: wrap;
        }

        .quick-btn {
            background: var(--bg-secondary);
            border: 1px solid var(--border-color);
            padding: 0.5rem 1rem;
            border-radius: 20px;
            cursor: pointer;
            font-weight: 500;
            transition: all 0.3s ease;
            color: var(--text-primary);
        }

        .quick-btn:hover,
        .quick-btn.active {
            background: var(--primary-color);
            color: white;
            border-color: var(--primary-color);
        }

        .payment-methods {
            margin: 1.5rem 0;
        }

        .form-input:focus {
            outline: 2px solid var(--primary-color);
            border-color: var(--primary-color);
            box-shadow: 0 0 0 3px rgba(0, 194, 206, 0.1);
        }

        /* Remove native number input spinners (arrows) */
        input[type=number]::-webkit-outer-spin-button,
        input[type=number]::-webkit-inner-spin-button {
            -webkit-appearance: none;
            margin: 0;
        }
        input[type=number] {
            -moz-appearance: textfield; /* Firefox */
        }

        .modal-actions {
            display: flex;
            gap: 1rem;
            justify-content: center;
            margin-top: 2rem;
        }

        .btn-primary {
            background: var(--gradient-primary);
            color: white;
            border: none;
            padding: 0.75rem 2rem;
            border-radius: 25px;
            cursor: pointer;
            font-weight: 600;
            transition: all 0.3s ease;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px var(--shadow-medium);
        }

        .btn-cancel {
            background: transparent;
            color: var(--text-muted);
            border: 2px solid var(--text-muted);
            padding: 0.75rem 2rem;
            border-radius: 25px;
            cursor: pointer;
            font-weight: 600;
            transition: all 0.3s ease;
        }

        .btn-cancel:hover {
            background: var(--text-muted);
            color: white;
        }

        .dropdown {
            position: relative;
            display: inline-block;
        }

        .dropdown-content {
            display: none;
            position: absolute;
            background: var(--bg-card);
            min-width: 160px;
            box-shadow: 0 8px 16px var(--shadow-light);
            border-radius: 8px;
            z-index: 1000;
            border: 1px solid var(--border-color);
        }

        .dropdown-content.show {
            display: block;
        }

        .dropdown-content a {
            color: var(--text-primary);
            padding: 12px 16px;
            text-decoration: none;
            display: block;
            transition: background 0.3s ease;
        }

        .dropdown-content a:hover {
            background: var(--bg-secondary);
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

            .balance-text {
                font-size: 2.5rem;
            }

            .wallet-buttons {
                flex-direction: column;
                align-items: center;
            }

            .wallet-btn {
                width: 100%;
                max-width: 300px;
            }

            .trans-item {
                flex-direction: column;
                align-items: flex-start;
                gap: 0.5rem;
            }

            .modal-actions {
                flex-direction: column;
            }

            .btn-primary, .btn-cancel {
                width: 100%;
            }
        }
    </style>
</head>
<body>
    <?php include __DIR__ . '/partials/header.php'; ?>

    <!-- Wallet Hero Section -->
    <section class="wallet-hero">
        <div class="container">
            <h1 class="wallet-greeting">
                <span class="wallet-greeting-main">Digital Wallet</span>
            </h1>
            <p style="font-size: 1.2rem; color: var(--text-secondary); max-width: 600px; margin: 0 auto; position: relative; z-index: 2;">
                Manage your Cephra balance and view transaction history.
            </p>
        </div>
    </section>

    <!-- Balance Section -->
    <section class="balance-section" style="padding: 80px 0;">
        <div class="container">
            <div class="balance-container">
                <button class="eye-toggle" id="eyeToggle">
                    <i class="fas fa-eye"></i>
                </button>
                <div class="balance-text" id="balanceText">₱<?php echo number_format($balance, 2); ?></div>
                <p style="color: rgba(255, 255, 255, 0.9); margin: 0;">Current Balance</p>
            </div>

            <div class="wallet-buttons">
                <button class="wallet-btn" onclick="showTopupModal()">Topup</button>
                <a href="wallet_history.php" class="wallet-btn">History</a>
            </div>
        </div>
    </section>

    <!-- Transactions Section -->
    <section class="transactions-section">
        <div class="container">
            <div class="section-header">
                <h2 class="section-title">Latest Transactions</h2>
                <p class="section-description">Your recent wallet activity</p>
            </div>

            <div class="trans-list">
                <?php if (empty($transactions)): ?>
                    <div class="trans-item" style="text-align: center; color: var(--text-muted);">
                        <span>No transactions found.</span>
                    </div>
                <?php else: ?>
                    <?php foreach ($transactions as $trans): ?>
                        <div class="trans-item">
                            <div>
                                <div class="trans-desc"><i class="fas fa-<?php echo $trans['amount'] >= 0 ? 'plus-circle' : 'minus-circle'; ?>"></i> <?php echo htmlspecialchars($trans['description'] . ($trans['reference_id'] ? " (Ref: " . $trans['reference_id'] . ")" : "")); ?></div>
                                <div class="trans-date"><?php echo date('M d, Y h:i A', strtotime($trans['transaction_date'])); ?></div>
                            </div>
                            <div class="trans-amount <?php echo $trans['amount'] >= 0 ? 'positive' : 'negative'; ?>">
                                <?php echo ($trans['amount'] >= 0 ? '+' : '-') . '₱' . number_format(abs($trans['amount']), 2); ?>
                            </div>
                        </div>
                    <?php endforeach; ?>
                <?php endif; ?>
            </div>
        </div>
    </section>

    <!-- Topup Modal -->
    <div id="topupModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title">Topup Wallet</h3>
                <button class="modal-close" onclick="closeTopupModal()">&times;</button>
            </div>
            <form id="topupForm">
                <div class="form-group">
                    <label class="form-label" for="topupAmount">Amount (₱)</label>
                    <input type="number" id="topupAmount" name="amount" class="form-input" min="1" step="0.01" required placeholder="Enter amount">
                    <div class="quick-amounts">
                        <button type="button" class="quick-btn" data-amount="500">₱500</button>
                        <button type="button" class="quick-btn" data-amount="1000">₱1000</button>
                        <button type="button" class="quick-btn" data-amount="2000">₱2000</button>
                        <button type="button" class="quick-btn" data-amount="5000">₱5000</button>
                        <button type="button" class="quick-btn" data-amount="10000">₱10000</button>
                    </div>
                </div>

                <div class="payment-methods">
                    <label class="form-label" for="paymentMethod">Choose your payment method</label>
                    <select id="paymentMethod" name="payment_method" class="form-input" required>
                        <option value="" disabled selected>Select a payment method</option>
                        <option value="gcash">GCash - XXXX XXXX 0921 (Expire 12/27)</option>
                        <option value="paymaya">Paymaya - XXXX XXXX 5690 (Expire 9/26)</option>
                        <option value="visa">Visa - XXXX XXXX 0921 (Expire 4/26)</option>
                        <option value="tbabank">TBA Bank - XXXX XXXX 7777 (Expire 7/27)</option>
                        <option value="dpwh">DPWH Funds - Default (Expire ∞)</option>
                    </select>
                </div>

                <div class="modal-actions">
                    <button type="submit" class="btn-primary">Proceed</button>
                    <button type="button" class="btn-cancel" onclick="closeTopupModal()">Cancel</button>
                </div>
            </form>
        </div>
    </div>

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

        // Eye Toggle for Balance
        let balanceVisible = true;
        const balanceText = document.getElementById('balanceText');
        const eyeToggle = document.getElementById('eyeToggle');
        const originalBalance = balanceText.textContent;

        eyeToggle.addEventListener('click', function() {
            balanceVisible = !balanceVisible;
            if (balanceVisible) {
                balanceText.textContent = originalBalance;
                eyeToggle.innerHTML = '<i class="fas fa-eye"></i>';
            } else {
                balanceText.textContent = '••••';
                eyeToggle.innerHTML = '<i class="fas fa-eye-slash"></i>';
            }
        });

        // Topup Modal
        function showTopupModal() {
            document.getElementById('topupModal').classList.add('show');
            document.body.style.overflow = 'hidden';
        }

        function closeTopupModal() {
            document.getElementById('topupModal').classList.remove('show');
            document.body.style.overflow = '';
        }

        // Topup Form Submit -> calls wallet-topup API (extend for payment method if needed)
        document.getElementById('topupForm').addEventListener('submit', async function(e) {
            e.preventDefault();
            const amountInput = document.getElementById('topupAmount');
            const amount = Number(amountInput.value);
            const selectedMethod = document.getElementById('paymentMethod');
            if (!amount || amount <= 0) {
                alert('Please enter a valid amount');
                return;
            }
            if (!selectedMethod.value) {
                alert('Please select a payment method');
                return;
            }
            try {
                const resp = await fetch('api/mobile.php', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    body: new URLSearchParams({
                        action: 'wallet-topup',
                        username: '<?php echo htmlspecialchars($_SESSION['username']); ?>',
                        amount: String(amount),
                        payment_method: selectedMethod.value
                    })
                });
                const data = await resp.json();
                if (data && data.success) {
                    // Update balance text without full reload
                    balanceText.textContent = '₱' + Number(data.balance).toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 });
                    closeTopupModal();
                } else {
                    alert(data && data.error ? data.error : 'Topup failed');
                }
            } catch (err) {
                alert('Topup failed: ' + err.message);
            }
        });

        // Removed More dropdown per design

        // Header scroll effect
        window.addEventListener('scroll', function() {
            const header = document.querySelector('.header');
            if (window.scrollY > 100) {
                header.classList.add('scrolled');
            } else {
                header.classList.remove('scrolled');
            }
        });

        // Ripple effect for buttons
        function createRipple(event) {
            const button = event.currentTarget;
            const circle = document.createElement("span");
            const diameter = Math.max(button.clientWidth, button.clientHeight);
            const radius = diameter / 2;
            circle.style.width = circle.style.height = `${diameter}px`;
            circle.style.left = `${event.clientX - button.offsetLeft - radius}px`;
            circle.style.top = `${event.clientY - button.offsetTop - radius}px`;
            circle.classList.add("ripple");
            const ripple = button.getElementsByClassName("ripple")[0];
            if (ripple) {
                ripple.remove();
            }
            button.appendChild(circle);
        }

        // Initialize
        document.addEventListener('DOMContentLoaded', function() {
            initMobileMenu();

            // Quick amount buttons
            const quickBtns = document.querySelectorAll('.quick-btn');
            const amountInput = document.getElementById('topupAmount');
            quickBtns.forEach(btn => {
                btn.addEventListener('click', function() {
                    const amount = this.dataset.amount;
                    amountInput.value = amount;
                    // Update active state
                    quickBtns.forEach(b => b.classList.remove('active'));
                    this.classList.add('active');
                });
            });

            const buttons = document.querySelectorAll('.wallet-btn, .btn-primary, .btn-cancel, .quick-btn');
            buttons.forEach(button => {
                button.addEventListener('click', createRipple);
            });
        });
    </script>
</body>
</html>
