<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Cephra - Ultimate EV Charging Platform</title>
    <link rel="icon" type="image/png" href="images/logo.png?v=2" />
    <link rel="apple-touch-icon" href="images/logo.png?v=2" />
    <link rel="manifest" href="manifest.webmanifest" />
    <meta name="theme-color" content="#1a1a2e" />

    <!-- Open Graph meta tags for social media previews -->
    <meta property="og:title" content="Cephra - Your Ultimate EV Charging Platform" />
    <meta property="og:description" content="Join the future of electric vehicle charging with Cephra's advanced platform" />
    <meta property="og:image" content="https://cephra.ct.ws/images/thumbnail.png?v=1" />
    <meta property="og:url" content="https://cephra.ct.ws" />
    <meta property="og:type" content="website" />
    <meta property="og:site_name" content="Cephra" />

    <!-- Twitter Card meta tags -->
    <meta name="twitter:card" content="summary_large_image" />
    <meta name="twitter:title" content="Cephra - Your Ultimate EV Charging Platform" />
    <meta name="twitter:description" content="Join the future of electric vehicle charging with Cephra's advanced platform" />
    <meta name="twitter:image" content="https://cephra.ct.ws/images/thumbnail.png?v=1" />

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
                    <img src="images/logo.png" alt="Cephra" class="logo-img" />
                    <span class="logo-text">CEPHRA</span>
                </div>

                <!-- Navigation -->
                <nav class="nav">
                    <ul class="nav-list">
                        <li><a href="#charging" class="nav-link">Charging</a></li>
                        <li><a href="#platforms" class="nav-link">Platforms</a></li>
                        <li><a href="#support" class="nav-link">Support</a></li>
                        <li><a href="#analysis" class="nav-link">Analytics</a></li>
                    </ul>
                </nav>

                <!-- Auth and Feature Buttons -->
                <div class="header-actions">
                    <!-- Language Selector -->
                    <div class="language-selector">
                        <button class="language-btn" id="languageBtn">
                            <span class="language-text">EN</span>
                            <i class="fas fa-chevron-down language-arrow"></i>
                        </button>
                        <div class="language-dropdown" id="languageDropdown">
                            <div class="language-option" data-lang="en">English</div>
                            <div class="language-option" data-lang="es">Español</div>
                            <div class="language-option" data-lang="fr">Français</div>
                        </div>
                    </div>

                    <!-- Download App Button -->
                    <div class="download-app">
                        <button class="download-btn" id="downloadBtn">
                            <i class="fas fa-download"></i>
                        </button>
                        <div class="qr-popup" id="qrPopup">
                            <div class="qr-content">
                                <h4>Download Cephra App</h4>
                                <div class="qr-code">
                                    <img src="images/qr.png" alt="QR Code - Download Cephra App" width="120" height="120" style="display: block; border-radius: 8px;" />
                                </div>
                                <p>Scan to download the Cephra mobile app</p>
                            </div>
                        </div>
                    </div>

                    <!-- Separator -->
                    <span class="header-separator">|</span>

                    <!-- Auth Buttons -->
                    <div class="auth-buttons">
                        <a href="Register_Panel.php" class="nav-link auth-link">Register</a>
                        <a href="login.php" class="nav-link auth-link">Login</a>
                    </div>
                </div>

                <!-- Mobile Menu -->
                <div class="mobile-menu" id="mobileMenu">
                    <div class="mobile-menu-content">
                        <!-- Mobile Navigation -->
                        <div class="mobile-nav">
                            <ul class="mobile-nav-list">
                                <li class="mobile-nav-item">
                                    <a href="#charging" class="mobile-nav-link">Charging</a>
                                </li>
                                <li class="mobile-nav-item">
                                    <a href="#platforms" class="mobile-nav-link">Platforms</a>
                                </li>
                                <li class="mobile-nav-item">
                                    <a href="#support" class="mobile-nav-link">Support</a>
                                </li>
                                <li class="mobile-nav-item">
                                    <a href="#analysis" class="mobile-nav-link">Analytics</a>
                                </li>
                            </ul>
                        </div>

                        <!-- Mobile Header Actions -->
                        <div class="mobile-header-actions">
                            <!-- Mobile Language Selector -->
                            <div class="mobile-language-selector">
                                <div class="language-selector">
                                    <button class="language-btn" id="mobileLanguageBtn">
                                        <span class="language-text">EN</span>
                                        <i class="fas fa-chevron-down language-arrow"></i>
                                    </button>
                                    <div class="language-dropdown" id="mobileLanguageDropdown">
                                        <div class="language-option" data-lang="en">English</div>
                                        <div class="language-option" data-lang="es">Español</div>
                                        <div class="language-option" data-lang="fr">Français</div>
                                    </div>
                                </div>
                            </div>

                            <!-- Mobile Download App -->
                            <div class="mobile-download-app">
                                <div class="download-app">
                                    <button class="download-btn" id="mobileDownloadBtn">
                                        <i class="fas fa-download"></i>
                                    </button>
                                    <div class="qr-popup" id="mobileQrPopup">
                                        <div class="qr-content">
                                            <h4>Download Cephra App</h4>
                                            <div class="qr-code">
                                                <img src="images/qr.png" alt="QR Code - Download Cephra App" width="120" height="120" style="display: block; border-radius: 8px;" />
                                            </div>
                                            <p>Scan to download the Cephra mobile app</p>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Mobile Auth Buttons -->
                            <div class="mobile-auth-buttons">
                                <a href="Register_Panel.php" class="nav-link auth-link">Register</a>
                                <a href="login.php" class="nav-link auth-link">Login</a>
                            </div>
                        </div>
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

    <!-- Hero Section -->
    <section class="hero">
        <div class="hero-background">
            <div class="hero-overlay"></div>
        </div>
        <div class="container">
            <div class="hero-content">
                <h1 class="hero-title">
                    <span class="hero-title-main">Cephra</span>
                    <span class="hero-title-accent">Ultimate</span>
                    <span class="hero-title-sub">Charging Platform</span>
                </h1>
                <p class="hero-description">
                    An award-winning EV charging platform trusted by 50,000+ drivers.
                    Experience the future of electric vehicle charging with intelligent,
                    fast, and reliable charging solutions.
                </p>
                <div class="hero-actions">
                    <a href="dashboard.php" class="btn btn-cta">Start Charging</a>
                    <a href="#features" class="btn btn-outline">Learn More</a>
                </div>
            </div>
        </div>
    </section>

    <!-- Features Section -->
    <section class="features" id="features">
        <div class="container">
            <div class="section-header">
                <h2 class="section-title">Why Choose Cephra?</h2>
                <p class="section-description">Experience the next generation of EV charging technology</p>
            </div>

            <div class="features-grid">
                <!-- Feature 1: Smart Charging -->
                <div class="feature-card">
                    <div class="feature-icon">
                        <i class="fas fa-bolt"></i>
                    </div>
                    <h3 class="feature-title">Smart Charging</h3>
                    <p class="feature-description">
                        AI-powered charging optimization that adapts to your vehicle's needs
                        and grid conditions for maximum efficiency.
                    </p>
                    <a href="dashboard.php" class="feature-link">Experience Smart Charging →</a>
                </div>

                <!-- Feature 2: Real-time Monitoring -->
                <div class="feature-card">
                    <div class="feature-icon">
                        <i class="fas fa-chart-line"></i>
                    </div>
                    <h3 class="feature-title">Real-time Monitoring</h3>
                    <p class="feature-description">
                        Track your charging sessions, energy consumption, and costs in real-time
                        with detailed analytics and insights.
                    </p>
                    <a href="dashboard.php" class="feature-link">View Analytics →</a>
                </div>

                <!-- Feature 3: Rewards Program -->
                <div class="feature-card">
                    <div class="feature-icon">
                        <i class="fas fa-gift"></i>
                    </div>
                    <h3 class="feature-title">Cephra Rewards</h3>
                    <p class="feature-description">
                        Earn points on every charge and unlock exclusive benefits, discounts,
                        and premium features as you charge more.
                    </p>
                    <a href="dashboard.php" class="feature-link">Join Rewards →</a>
                </div>
            </div>
        </div>
    </section>

    <!-- Promotional Cards Section -->
    <section class="promotions" id="promotions">
        <div class="container">
            <div class="section-header">
                <h2 class="section-title">Exclusive Offers</h2>
                <p class="section-description">Limited-time promotions designed for EV enthusiasts</p>
            </div>

            <div class="promo-grid">
                <!-- Promo 1: Charging Rewards -->
                <div class="promo-card">
                    <div class="promo-header">
                        <div class="promo-icon">
                            <i class="fas fa-battery-full"></i>
                        </div>
                        <div class="promo-label">Limited Offer</div>
                    </div>
                    <h3 class="promo-title">13% Bonus Credits</h3>
                    <p class="promo-description">
                        Get 13% bonus credits on your first wallet top-up.
                        Perfect for frequent chargers looking to maximize savings.
                    </p>
                    <div class="promo-footer">
                        <a href="Register_Panel.php" class="btn btn-promo">Claim Offer</a>
                        <div class="promo-validity">Valid until Dec 31, 2024</div>
                    </div>
                </div>

                <!-- Promo 2: Premium Features -->
                <div class="promo-card">
                    <div class="promo-header">
                        <div class="promo-icon">
                            <i class="fas fa-crown"></i>
                        </div>
                        <div class="promo-label">Premium</div>
                    </div>
                    <h3 class="promo-title">Premium Access</h3>
                    <p class="promo-description">
                        Unlock priority charging, advanced analytics, and exclusive
                        station reservations with our premium membership.
                    </p>
                    <div class="promo-footer">
                        <a href="Register_Panel.php" class="btn btn-promo">Upgrade Now</a>
                        <div class="promo-validity">30-day free trial</div>
                    </div>
                </div>

                <!-- Promo 3: Community Contest -->
                <div class="promo-card">
                    <div class="promo-header">
                        <div class="promo-icon">
                            <i class="fas fa-trophy"></i>
                        </div>
                        <div class="promo-label">Contest</div>
                    </div>
                    <h3 class="promo-title">EV Champion 2025</h3>
                    <p class="promo-description">
                        Join our annual charging contest. Top chargers win premium
                        subscriptions, exclusive merchandise, and charging credits.
                    </p>
                    <div class="promo-footer">
                        <a href="Register_Panel.php" class="btn btn-promo">Join Contest</a>
                        <div class="promo-validity">Registration open</div>
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
                        <li><a href="#charging">Smart Charging</a></li>
                        <li><a href="#analysis">Analytics</a></li>
                        <li><a href="login.php">Login</a></li>
                    </ul>
                </div>

                <div class="footer-section">
                    <h4 class="footer-title">Support</h4>
                    <ul class="footer-links">
                        <li><a href="#support">Help Center</a></li>
                        <li><a href="#contact">Contact Us</a></li>
                        <li><a href="#status">System Status</a></li>
                        <li><a href="#api">API Documentation</a></li>
                    </ul>
                </div>

                <div class="footer-section">
                    <h4 class="footer-title">Company</h4>
                    <ul class="footer-links">
                        <li><a href="#about">About Us</a></li>
                        <li><a href="#careers">Careers</a></li>
                        <li><a href="#press">Press Kit</a></li>
                        <li><a href="#security">Security</a></li>
                    </ul>
                </div>
            </div>

            <div class="footer-bottom">
                <p>&copy; 2024 Cephra. All rights reserved. | <a href="#privacy">Privacy Policy</a> | <a href="#terms">Terms of Service</a></p>
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

        // Add animation on scroll for feature cards
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

        document.querySelectorAll('.feature-card, .promo-card').forEach(card => {
            observer.observe(card);
        });

        // Language Selector Functionality
        const languageBtn = document.getElementById('languageBtn');
        const languageDropdown = document.getElementById('languageDropdown');
        const languageOptions = document.querySelectorAll('.language-option');

        // Load saved language preference
        function loadLanguagePreference() {
            const savedLang = localStorage.getItem('selectedLanguage') || 'en';
            updateLanguageDisplay(savedLang);
        }

        // Update language display
        function updateLanguageDisplay(lang) {
            const languageText = document.querySelector('.language-text');
            const langMap = {
                'en': 'EN',
                'es': 'ES',
                'fr': 'FR'
            };
            languageText.textContent = langMap[lang] || 'EN';
            localStorage.setItem('selectedLanguage', lang);
        }

        // Toggle language dropdown
        languageBtn.addEventListener('click', function(e) {
            e.stopPropagation();
            languageDropdown.classList.toggle('show');
            languageBtn.classList.toggle('active');
        });

        // Handle language selection
        languageOptions.forEach(option => {
            option.addEventListener('click', function() {
                const selectedLang = this.dataset.lang;
                updateLanguageDisplay(selectedLang);
                languageDropdown.classList.remove('show');
                languageBtn.classList.remove('active');

                // Here you would typically make an API call to save to database
                console.log('Language changed to:', selectedLang);
                // TODO: Add database integration here
            });
        });

        // Close dropdown when clicking outside
        document.addEventListener('click', function(e) {
            if (!languageBtn.contains(e.target) && !languageDropdown.contains(e.target)) {
                languageDropdown.classList.remove('show');
                languageBtn.classList.remove('active');
            }
        });

        // QR Code Popup Functionality
        const downloadBtn = document.getElementById('downloadBtn');
        const qrPopup = document.getElementById('qrPopup');

        // Show QR popup on hover
        downloadBtn.addEventListener('mouseenter', function() {
            qrPopup.classList.add('show');
        });

        // Hide QR popup when mouse leaves both button and popup
        downloadBtn.addEventListener('mouseleave', function(e) {
            // Delay hiding to allow mouse to move to popup
            setTimeout(() => {
                if (!downloadBtn.matches(':hover') && !qrPopup.matches(':hover')) {
                    qrPopup.classList.remove('show');
                }
            }, 100);
        });

        // Hide QR popup when mouse leaves popup
        qrPopup.addEventListener('mouseleave', function() {
            qrPopup.classList.remove('show');
        });

        // Mobile Language Selector Functionality
        const mobileLanguageBtn = document.getElementById('mobileLanguageBtn');
        const mobileLanguageDropdown = document.getElementById('mobileLanguageDropdown');
        const mobileLanguageOptions = mobileLanguageDropdown.querySelectorAll('.language-option');

        // Toggle mobile language dropdown
        mobileLanguageBtn.addEventListener('click', function(e) {
            e.stopPropagation();
            mobileLanguageDropdown.classList.toggle('show');
            mobileLanguageBtn.classList.toggle('active');
        });

        // Handle mobile language selection
        mobileLanguageOptions.forEach(option => {
            option.addEventListener('click', function() {
                const selectedLang = this.dataset.lang;
                updateLanguageDisplay(selectedLang);
                mobileLanguageDropdown.classList.remove('show');
                mobileLanguageBtn.classList.remove('active');

                // Also update desktop language display
                const desktopLanguageText = document.querySelector('#languageBtn .language-text');
                if (desktopLanguageText) {
                    const langMap = { 'en': 'EN', 'es': 'ES', 'fr': 'FR' };
                    desktopLanguageText.textContent = langMap[selectedLang] || 'EN';
                }

                console.log('Language changed to:', selectedLang);
            });
        });

        // Mobile QR Code Popup Functionality
        const mobileDownloadBtn = document.getElementById('mobileDownloadBtn');
        const mobileQrPopup = document.getElementById('mobileQrPopup');

        // Show mobile QR popup on click
        mobileDownloadBtn.addEventListener('click', function(e) {
            e.stopPropagation();
            mobileQrPopup.classList.toggle('show');
        });

        // Hide mobile QR popup when clicking outside
        document.addEventListener('click', function(e) {
            if (!mobileDownloadBtn.contains(e.target) && !mobileQrPopup.contains(e.target)) {
                mobileQrPopup.classList.remove('show');
            }
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

        // Initialize language preference on page load
        document.addEventListener('DOMContentLoaded', function() {
            loadLanguagePreference();
        });
    </script>
</body>
</html>
