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
    <title>Help Center - Cephra</title>
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

        .help-hero {
            background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
            padding: 120px 0 80px;
            text-align: center;
        }

        .help-search {
            max-width: 600px;
            margin: 2rem auto;
            position: relative;
        }

        .help-search input {
            width: 100%;
            padding: 1rem 3rem 1rem 1rem;
            border: 2px solid var(--border-color);
            border-radius: 50px;
            font-size: 1.1rem;
            transition: all 0.3s ease;
        }

        .help-search input:focus {
            outline: none;
            border-color: var(--primary-color);
            box-shadow: 0 0 0 3px rgba(0, 194, 206, 0.1);
        }

        .help-search button {
            position: absolute;
            right: 10px;
            top: 50%;
            transform: translateY(-50%);
            background: var(--primary-color);
            color: white;
            border: none;
            border-radius: 50%;
            width: 40px;
            height: 40px;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .help-search button:hover {
            transform: translateY(-50%) scale(1.1);
        }

        .help-categories {
            padding: 80px 0;
            background: white;
        }

        .categories-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 2rem;
        }

        .category-card {
            background: var(--bg-card);
            border-radius: 20px;
            padding: 2rem;
            border: 1px solid var(--border-color);
            transition: all 0.3s ease;
            text-align: center;
        }

        .category-card:hover {
            transform: translateY(-10px);
            box-shadow: 0 20px 40px var(--shadow-medium);
            border-color: var(--primary-color);
        }

        .category-icon {
            width: 60px;
            height: 60px;
            background: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%);
            border-radius: 15px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 1.5rem;
            font-size: 24px;
            color: white;
        }

        .faq-section {
            padding: 80px 0;
            background: var(--bg-secondary);
        }

        .faq-container {
            max-width: 800px;
            margin: 0 auto;
        }

        .faq-item {
            background: white;
            border-radius: 12px;
            margin-bottom: 1rem;
            border: 1px solid var(--border-color);
            overflow: hidden;
        }

        .faq-question {
            padding: 1.5rem;
            background: none;
            border: none;
            width: 100%;
            text-align: left;
            font-size: 1.1rem;
            font-weight: 600;
            color: var(--text-primary);
            cursor: pointer;
            display: flex;
            justify-content: space-between;
            align-items: center;
            transition: all 0.3s ease;
        }

        .faq-question:hover {
            background: rgba(0, 194, 206, 0.08);
        }

        .faq-item.active {
            box-shadow: 0 4px 12px rgba(0, 194, 206, 0.15);
            border-color: var(--primary-color);
        }

        .faq-answer {
            padding: 0 1.5rem 1.5rem;
            color: var(--text-secondary);
            line-height: 1.6;
            max-height: 0;
            overflow: hidden;
            transition: all 0.3s ease;
        }

        .faq-item.active .faq-answer {
            max-height: 500px;
            padding: 1.5rem 1.5rem 1.5rem;
        }

        .faq-toggle {
            transition: transform 0.3s ease;
            font-size: 1rem;
            color: var(--text-secondary);
        }

        .faq-item.active .faq-toggle {
            transform: rotate(180deg);
            color: var(--primary-color);
        }

        .contact-support {
            padding: 80px 0;
            background: white;
            text-align: center;
        }

        .support-options {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 2rem;
            margin-top: 3rem;
        }

        .support-option {
            background: var(--bg-card);
            border-radius: 20px;
            padding: 2rem;
            border: 1px solid var(--border-color);
            transition: all 0.3s ease;
        }

        .support-option:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 30px var(--shadow-light);
            border-color: var(--primary-color);
        }

        .support-icon {
            width: 50px;
            height: 50px;
            background: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 1rem;
            color: white;
            font-size: 1.2rem;
        }

        .btn {
            padding: 12px 24px;
            border: none;
            border-radius: 8px;
            text-decoration: none;
            font-weight: 600;
            transition: all 0.3s ease;
            cursor: pointer;
            display: inline-block;
        }

        .btn-primary {
            background: var(--primary-color);
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px var(--shadow-medium);
        }

        @media (max-width: 768px) {
            .help-hero {
                padding: 100px 0 60px;
            }

            .categories-grid {
                grid-template-columns: 1fr;
                gap: 1.5rem;
            }

            .support-options {
                grid-template-columns: 1fr;
            }

            .faq-question {
                font-size: 1rem;
            }
        }
    </style>
</head>
<body>
    <!-- Header -->
        <?php include __DIR__ . '/partials/header.php'; ?>

        <div class="mobile-menu" id="mobileMenu">
            <div class="mobile-menu-content">
                <ul class="mobile-nav-list">
                    <li><a href="#" onclick="openMonitorWeb(); return false;" class="mobile-nav-link">Monitor</a></li>
                    <li><a href="link.php" class="mobile-nav-link">Link</a></li>
                    <li><a href="history.php" class="mobile-nav-link">History</a></li>
                    <li><a href="profile.php" class="mobile-nav-link">Profile</a></li>
                </ul>
                <div class="mobile-header-actions">
                    <a href="dashboard.php" class="mobile-auth-link">Dashboard</a>
                </div>
            </div>
        </div>
    </header>

    <!-- Help Center Hero -->
    <section class="help-hero">
        <div class="container">
            <h1 style="font-size: clamp(2.5rem, 6vw, 4rem); font-weight: 900; margin-bottom: 1rem; background: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text;">Help Center</h1>
            <p style="font-size: 1.3rem; color: var(--text-secondary); max-width: 600px; margin: 0 auto 2rem;">Find answers to your questions and get the help you need to make the most of your Cephra experience.</p>

            <div class="help-search">
                <input type="text" placeholder="Search for help..." id="helpSearch">
                <button onclick="searchHelp()"><i class="fas fa-search"></i></button>
            </div>
        </div>
    </section>

    <!-- Help Categories -->
    <section class="help-categories">
        <div class="container">
            <div class="section-header" style="text-align: center; margin-bottom: 60px;">
                <h2 style="font-size: 2.5rem; font-weight: 700; margin-bottom: 1rem; background: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text;">Browse by Category</h2>
                <p style="font-size: 1.2rem; color: var(--text-secondary);">Choose a category to find the help you need</p>
            </div>

            <div class="categories-grid">
                <div class="category-card">
                    <div class="category-icon">
                        <i class="fas fa-play-circle"></i>
                    </div>
                    <h3 style="font-size: 1.5rem; font-weight: 600; margin-bottom: 1rem; color: var(--text-primary);">Getting Started</h3>
                    <p style="color: var(--text-secondary); margin-bottom: 1.5rem;">Learn the basics of using Cephra and setting up your account.</p>
                    <a href="dashboard.php" class="btn btn-primary">Get Started</a>
                </div>

                <div class="category-card">
                    <div class="category-icon">
                        <i class="fas fa-bolt"></i>
                    </div>
                    <h3 style="font-size: 1.5rem; font-weight: 600; margin-bottom: 1rem; color: var(--text-primary);">Charging</h3>
                    <p style="color: var(--text-secondary); margin-bottom: 1.5rem;">Everything you need to know about charging your electric vehicle.</p>
                    <a href="ChargingPage.php" class="btn btn-primary">Start Charging</a>
                </div>

                <div class="category-card">
                    <div class="category-icon">
                        <i class="fas fa-user"></i>
                    </div>
                    <h3 style="font-size: 1.5rem; font-weight: 600; margin-bottom: 1rem; color: var(--text-primary);">Account & Profile</h3>
                    <p style="color: var(--text-secondary); margin-bottom: 1.5rem;">Manage your account settings, profile, and preferences.</p>
                    <a href="profile.php" class="btn btn-primary">Manage Profile</a>
                </div>

                <div class="category-card">
                    <div class="category-icon">
                        <i class="fas fa-tools"></i>
                    </div>
                    <h3 style="font-size: 1.5rem; font-weight: 600; margin-bottom: 1rem; color: var(--text-primary);">Troubleshooting</h3>
                    <p style="color: var(--text-secondary); margin-bottom: 1.5rem;">Common issues and how to resolve them quickly.</p>
                    <a href="#faq-section" class="btn btn-primary">View FAQs</a>
                </div>
            </div>
        </div>
    </section>

    <!-- FAQ Section -->
    <section id="faq-section" class="faq-section">
        <div class="container">
            <div class="section-header" style="text-align: center; margin-bottom: 60px;">
                <h2 style="font-size: 2.5rem; font-weight: 700; margin-bottom: 1rem; background: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text;">Frequently Asked Questions</h2>
                <p style="font-size: 1.2rem; color: var(--text-secondary);">Quick answers to common questions</p>
            </div>

            <div class="faq-container">
                <div class="faq-item">
                    <button class="faq-question" onclick="toggleFAQ(this)">
                        How do I start charging my vehicle?
                        <i class="fas fa-chevron-down faq-toggle"></i>
                    </button>
                    <div class="faq-answer">
                        <p>To start charging your vehicle:</p>
                        <ol>
                            <li>Ensure your vehicle is properly linked in your profile</li>
                            <li>Navigate to the Charging page from your dashboard</li>
                            <li>Select your preferred charging type (Normal or Fast)</li>
                            <li>Confirm your selection and wait for a charging bay assignment</li>
                            <li>Follow the on-screen instructions to connect your vehicle</li>
                        </ol>
                    </div>
                </div>

                <div class="faq-item">
                    <button class="faq-question" onclick="toggleFAQ(this)">
                        What should I do if I encounter a charging issue?
                        <i class="fas fa-chevron-down faq-toggle"></i>
                    </button>
                    <div class="faq-answer">
                        <p>If you experience charging issues:</p>
                        <ul>
                            <li>Check that your vehicle is properly connected to the charging station</li>
                            <li>Verify that your account has sufficient balance</li>
                            <li>Try restarting the charging process</li>
                            <li>Contact support if the issue persists</li>
                        </ul>
                        <p>You can also check the Monitor page for real-time status updates.</p>
                    </div>
                </div>

                <div class="faq-item">
                    <button class="faq-question" onclick="toggleFAQ(this)">
                        How do I update my profile information?
                        <i class="fas fa-chevron-down faq-toggle"></i>
                    </button>
                    <div class="faq-answer">
                        <p>To update your profile:</p>
                        <ol>
                            <li>Go to the Profile page from the navigation menu</li>
                            <li>Edit your personal information in the form provided</li>
                            <li>Upload a new profile picture if desired</li>
                            <li>Click "Update Profile" to save your changes</li>
                        </ol>
                        <p>All changes are saved automatically and will be reflected across the platform.</p>
                    </div>
                </div>

                <div class="faq-item">
                    <button class="faq-question" onclick="toggleFAQ(this)">
                        What are Green Points and how do I earn them?
                        <i class="fas fa-chevron-down faq-toggle"></i>
                    </button>
                    <div class="faq-answer">
                        <p>Green Points are our reward system:</p>
                        <ul>
                            <li>Earn 10 points for every hour of charging</li>
                            <li>Bonus points for off-peak charging (50% more)</li>
                            <li>Redeem points for discounts on future charges</li>
                            <li>Track your points in the Rewards section</li>
                        </ul>
                        <p>The more you charge sustainably, the more you save!</p>
                    </div>
                </div>

                <div class="faq-item">
                    <button class="faq-question" onclick="toggleFAQ(this)">
                        How do I view my charging history?
                        <i class="fas fa-chevron-down faq-toggle"></i>
                    </button>
                    <div class="faq-answer">
                        <p>Access your charging history:</p>
                        <ol>
                            <li>Click on "History" in the navigation menu</li>
                            <li>View detailed records of all your charging sessions</li>
                            <li>Filter by date, location, or charging type</li>
                            <li>Download reports for your records</li>
                        </ol>
                        <p>Your history includes energy consumed, costs, and Green Points earned.</p>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Contact Support -->
    <section class="contact-support">
        <div class="container">
            <div class="section-header" style="text-align: center; margin-bottom: 60px;">
                <h2 style="font-size: 2.5rem; font-weight: 700; margin-bottom: 1rem; background: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text;">Still Need Help?</h2>
                <p style="font-size: 1.2rem; color: var(--text-secondary);">Our support team is here to assist you</p>
            </div>

            <div class="support-options">
                <div class="support-option">
                    <div class="support-icon">
                        <i class="fas fa-envelope"></i>
                    </div>
                    <h4 style="font-size: 1.2rem; font-weight: 600; margin-bottom: 0.5rem; color: var(--text-primary);">Email Support</h4>
                    <p style="color: var(--text-secondary); margin-bottom: 1rem;">Get detailed help via email</p>
                    <a href="mailto:support@cephra.com" class="btn btn-primary">Email Us</a>
                </div>

                <div class="support-option">
                    <div class="support-icon">
                        <i class="fas fa-phone"></i>
                    </div>
                    <h4 style="font-size: 1.2rem; font-weight: 600; margin-bottom: 0.5rem; color: var(--text-primary);">Phone Support</h4>
                    <p style="color: var(--text-secondary); margin-bottom: 1rem;">Speak directly with our experts</p>
                    <a href="tel:+6321234567" class="btn btn-primary">Call Now</a>
                </div>

                <div class="support-option">
                    <div class="support-icon">
                        <i class="fas fa-comments"></i>
                    </div>
                    <h4 style="font-size: 1.2rem; font-weight: 600; margin-bottom: 0.5rem; color: var(--text-primary);">Live Chat</h4>
                    <p style="color: var(--text-secondary); margin-bottom: 1rem;">Instant help via live chat</p>
                    <button onclick="startLiveChat()" class="btn btn-primary">Start Chat</button>
                </div>
            </div>
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
        function toggleFAQ(button) {
            const faqItem = button.parentElement;
            const isActive = faqItem.classList.contains('active');

            // Close all FAQ items
            document.querySelectorAll('.faq-item').forEach(item => {
                item.classList.remove('active');
            });

            // Open clicked item if it wasn't active
            if (!isActive) {
                faqItem.classList.add('active');
            }
        }

        function searchHelp() {
            const query = document.getElementById('helpSearch').value.toLowerCase();
            if (query.trim() === '') {
                alert('Please enter a search term');
                return;
            }

            // Simple search functionality - highlight matching FAQ items
            const faqItems = document.querySelectorAll('.faq-item');
            let found = false;

            faqItems.forEach(item => {
                const question = item.querySelector('.faq-question').textContent.toLowerCase();
                const answer = item.querySelector('.faq-answer').textContent.toLowerCase();

                if (question.includes(query) || answer.includes(query)) {
                    item.style.display = 'block';
                    if (!found) {
                        item.classList.add('active');
                        item.scrollIntoView({ behavior: 'smooth', block: 'center' });
                        found = true;
                    }
                } else {
                    item.style.display = 'none';
                    item.classList.remove('active');
                }
            });

            if (!found) {
                alert('No results found for "' + query + '". Try different keywords.');
                // Show all items again
                faqItems.forEach(item => {
                    item.style.display = 'block';
                });
            }
        }

        function startLiveChat() {
            alert('Live chat feature coming soon! Please use email or phone support for now.');
        }

        // Mobile Menu Functionality
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
            mobileNavLinks.forEach(link => {
                link.addEventListener('click', closeMobileMenu);
            });
        }

        function openMonitorWeb() {
            const monitorUrl = '../Monitor/';
            window.open(monitorUrl, '_blank', 'noopener,noreferrer');
        }

        // Initialize on load
        document.addEventListener('DOMContentLoaded', function() {
            initMobileMenu();

            // Add scroll effect to header
            window.addEventListener('scroll', function() {
                const header = document.querySelector('.header');
                if (window.scrollY > 100) {
                    header.classList.add('scrolled');
                } else {
                    header.classList.remove('scrolled');
                }
            });

            // Search on Enter key
            document.getElementById('helpSearch').addEventListener('keypress', function(e) {
                if (e.key === 'Enter') {
                    searchHelp();
                }
            });
        });
    </script>
</body>
</html>
