<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Cephra EV Charging Platform</title>
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
                        <li><a href="#promotions" class="nav-link">Offers</a></li>
                        <li><a href="#features" class="nav-link">About</a></li>
                    </ul>
                </nav>

                <!-- Auth and Feature Buttons -->
                <div class="header-actions">
                    <!-- Auth Buttons (left within the right group) -->
                    <div class="auth-buttons">
                        <a href="Register_Panel.php" class="nav-link auth-link">Register</a>
                        <a href="login.php" class="nav-link auth-link">Login</a>
                    </div>

                    <!-- Separator -->
                    <span class="header-separator">|</span>

                    <!-- Language Selector -->
                    <div class="language-selector">
                        <button class="language-btn" id="languageBtn">
                            <span class="language-text">EN</span>
                            <i class="fas fa-chevron-down language-arrow"></i>
                        </button>
                        <div class="language-dropdown" id="languageDropdown">
                            <div class="language-option" data-lang="en">English</div>
                            <div class="language-option" data-lang="fil">Filipino</div>
                            <div class="language-option" data-lang="ceb">Bisaya</div>
                            <div class="language-option" data-lang="zh">中文</div>
                        </div>
                    </div>

                    <!-- Download App Button (furthest right) -->
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
                                    <a href="#promotions" class="mobile-nav-link">Offers</a>
                                </li>
                                <li class="mobile-nav-item">
                                    <a href="#features" class="mobile-nav-link">About</a>
                                </li>
                            </ul>
                        </div>

                        <!-- Mobile Header Actions -->
                        <div class="mobile-header-actions" style="display:flex;gap:16px;align-items:center;justify-content:center;flex-wrap:wrap;">
                            <!-- Mobile Language Selector -->
                            <div class="mobile-language-selector">
                                <div class="language-selector">
                                    <button class="language-btn" id="mobileLanguageBtn">
                                        <span class="language-text">EN</span>
                                        <i class="fas fa-chevron-down language-arrow"></i>
                                    </button>
                                    <div class="language-dropdown" id="mobileLanguageDropdown">
                                        <div class="language-option" data-lang="en">English</div>
                                        <div class="language-option" data-lang="fil">Filipino</div>
                                        <div class="language-option" data-lang="ceb">Bisaya</div>
                                        <div class="language-option" data-lang="zh">中文</div>
                                    </div>
                                </div>
                            </div>

						<!-- Actions row: Download + Auth in one row on small screens -->
						<div class="mobile-actions-row" style="display:flex;gap:16px;align-items:center;justify-content:center;margin-top:12px;">
							<!-- Mobile Download App -->
							<div class="mobile-download-app" style="display:flex;align-items:center;">
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
							<div class="mobile-auth-buttons" style="display:flex;gap:12px;align-items:center;">
								<a href="Register_Panel.php" class="nav-link auth-link">Register</a>
								<a href="login.php" class="nav-link auth-link">Login</a>
							</div>
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
                    <a href="register_panel.php" class="btn btn-cta">Start Charging</a>
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
                </div>
            </div>
        </div>
    </section>

    <!-- Charging Types Section -->
    <section class="charging-types" id="charging">
        <div class="container">
            <div class="section-header">
                <h2 class="section-title">Choose Your Charging Speed</h2>
                <p class="section-description">Select the perfect charging option for your needs</p>
            </div>

            <div class="charging-grid">
                <!-- Charging Type 1: Normal Charging -->
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
                </div>

                <!-- Charging Type 2: Fast Charging -->
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
                </div>

                <!-- Charging Type 3: Priority Charging -->
                <div class="charging-card">
                    <div class="charging-icon">
                        <i class="fas fa-rocket"></i>
                    </div>
                    <h3 class="charging-title">Priority Charging</h3>
                    <p class="charging-description">
                        When your battery drops below 20%, priority charging automatically activates
                        for fire protection and vehicle safety. This ensures your EV gets immediate
                        attention with maximum charging speed and guaranteed availability, protecting
                        both your vehicle and ensuring your safety on the road.
                    </p>
                    <div class="charging-specs">
                        <span class="spec-item">Priority</span>
                        <span class="spec-item">10-30 minutes</span>
                    </div>
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
                        <div class="promo-label">Rewards</div>
                    </div>
                    <h3 class="promo-title">Cephra Rewards</h3>
                    <p class="promo-description">
                        Get Cephra credits on your first charge with us.
                        Perfect for frequent chargers looking to maximize savings.
                    </p>
                    <div class="promo-footer">
                        <a href="Register_Panel.php" class="btn btn-promo">Claim now</a>
                        <div class="promo-validity">Valid until Dec 31, 2025</div>
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
                    <h3 class="promo-title">Fast Charging</h3>
                    <p class="promo-description">
                        Unlock priority charging, advanced charging technology, and
                         quality service for our premium clients.
                    </p>
                    <div class="promo-footer">
                        <a href="Register_Panel.php" class="btn btn-promo">Book Now</a>
                        <div class="promo-validity">Try Now</div>
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
		<?php include __DIR__ . '/partials/footer.php'; ?>

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

        document.querySelectorAll('.feature-card, .promo-card, .charging-card').forEach(card => {
            observer.observe(card);
        });

        // Language Selector Functionality
        const languageBtn = document.getElementById('languageBtn');
        const languageDropdown = document.getElementById('languageDropdown');
        const languageOptions = document.querySelectorAll('.language-option');

        // Simple i18n dictionary covering all visible strings on this page
        const i18n = {
            en: {
                TitleMain: 'Cephra',
                TitleAccent: 'Ultimate',
                TitleSub: 'Charging Platform',
                HeroDesc: 'An award-winning EV charging platform trusted by 50,000+ drivers. Experience the future of electric vehicle charging with intelligent, fast, and reliable charging solutions.',
                Charging: 'Charging',
                Offers: 'Offers',
                About: 'About',
                Register: 'Register',
                Login: 'Login',
                StartCharging: 'Start Charging',
                LearnMore: 'Learn More',
                WhyChoose: 'Why Choose Cephra?',
                WhyDesc: 'Experience the next generation of EV charging technology',
                SmartCharging: 'Smart Charging',
                SmartLink: 'Experience Smart Charging →',
                RealTime: 'Real-time Monitoring',
                RealTimeLink: 'View Analytics →',
                Rewards: 'Cephra Rewards',
                RewardsLink: 'Join Rewards →',
                ChooseSpeed: 'Choose Your Charging Speed',
                ChooseDesc: 'Select the perfect charging option for your needs',
                NormalCharging: 'Normal Charging',
                NormalDesc: "Perfect for regular charging when you're not in a rush. Ideal for everyday use, overnight charging at home, or during extended parking periods. Provides steady, reliable charging without putting stress on your vehicle's battery.",
                NormalSpec1: '3-7 kW',
                NormalSpec2: '2-3 hours',
                NormalLink: 'Start Normal Charging →',
                FastCharging: 'Fast Charging',
                FastDesc: 'When time is of the essence, fast charging delivers rapid power to get you back on the road quickly. Perfect for lunch breaks, shopping stops, or quick top-ups during long drives. Our advanced fast charging technology provides optimal charging curves to maximize efficiency while protecting your battery health.',
                FastSpec1: '50-150 kW',
                FastSpec2: '20-40 minutes',
                FastLink: 'Start Fast Charging →',
                PriorityCharging: 'Priority Charging',
                PriorityDesc: 'When your battery drops below 20%, priority charging automatically activates for fire protection and vehicle safety. This ensures your EV gets immediate attention with maximum charging speed and guaranteed availability, protecting both your vehicle and ensuring your safety on the road.',
                PrioritySpec1: 'Priority',
                PrioritySpec2: '10-30 minutes',
                PriorityLink: 'Get Priority Access →',
                ExclusiveOffers: 'Exclusive Offers',
                ExclusiveDesc: 'Limited-time promotions designed for EV enthusiasts',
                RewardsPromo: 'Cephra Rewards',
                RewardsDesc: 'Get Cephra credits on your first charge with us. Perfect for frequent chargers looking to maximize savings.',
                ClaimNow: 'Claim now',
                PremiumTitle: 'Fast Charging',
                PremiumDesc: 'Unlock priority charging, advanced charging technology, and quality service for our premium clients.',
                BookNow: 'Book Now',
                TryNow: 'Try Now',
                ContestTitle: 'EV Champion 2025',
                ContestDesc: 'Join our annual charging contest. Top chargers win premium subscriptions, exclusive merchandise, and charging credits.',
                JoinContest: 'Join Contest',
                RegistrationOpen: 'Registration open',
                ValidUntil: 'Valid until',
                Platform: 'Platform',
                Support: 'Support',
                Company: 'Company'
            },
            fil: {
                TitleMain: 'Cephra',
                TitleAccent: 'Ultimate',
                TitleSub: 'Charging Platform',
                HeroDesc: 'Gantimpalang EV charging platform na pinagkakatiwalaan ng 50,000+ na drayber. Damhin ang kinabukasan ng pagkarga—matalino, mabilis, at maaasahan.',
                Charging: 'Karga',
                Offers: 'Alok',
                About: 'Tungkol',
                Register: 'Magrehistro',
                Login: 'Mag-login',
                StartCharging: 'Simulan ang Pagkarga',
                LearnMore: 'Alamin Pa',
                WhyChoose: 'Bakit Piliin ang Cephra?',
                WhyDesc: 'Maraming-bagong henerasyon ng teknolohiya sa EV charging',
                SmartCharging: 'Matalinong Pagkarga',
                SmartLink: 'Subukan ang Matalinong Pagkarga →',
                RealTime: 'Real-time na Pagsubaybay',
                RealTimeLink: 'Tingnan ang Analytics →',
                Rewards: 'Cephra Rewards',
                RewardsLink: 'Sumali sa Rewards →',
                ChooseSpeed: 'Pumili ng Bilis ng Pagkarga',
                ChooseDesc: 'Piliin ang tamang opsyon para sa iyong pangangailangan',
                NormalCharging: 'Normal na Pagkarga',
                NormalDesc: 'Perpekto para sa regular na pagkarga kapag hindi nagmamadali. Mainam para sa araw‑araw na gamit, overnight sa bahay, o habang naka-park nang matagal. Nagbibigay ng tuluy‑tuloy at maaasahang pagkarga nang hindi pinapahirapan ang baterya.',
                NormalSpec1: '3-7 kW',
                NormalSpec2: '2-3 oras',
                NormalLink: 'Simulan ang Normal na Pagkarga →',
                FastCharging: 'Mabilis na Pagkarga',
                FastDesc: 'Kapag mahalaga ang oras, ang mabilis na pagkarga ang magbabalik sa iyo agad sa kalsada. Perpekto para sa tanghalian, pamimili, o mabilisang top‑up sa mahabang biyahe. Ang aming advanced tech ay nagbibigay ng optimal na charging curve para sa kahusayan at proteksyon ng baterya.',
                FastSpec1: '50-150 kW',
                FastSpec2: '20-40 minuto',
                FastLink: 'Simulan ang Mabilis na Pagkarga →',
                PriorityCharging: 'Prayoridad na Pagkarga',
                PriorityDesc: 'Kapag bumaba sa 20% ang baterya, awtomatikong naa-activate ang prayoridad na pagkarga para sa proteksyon at kaligtasan. Tinitiyak nito ang agarang atensyon, pinakamataas na bilis, at garantisadong availability.',
                PrioritySpec1: 'Prayoridad',
                PrioritySpec2: '10-30 minuto',
                PriorityLink: 'Kunin ang Prayoridad →',
                ExclusiveOffers: 'Espesyal na Alok',
                ExclusiveDesc: 'Limitadong oras na alok para sa mga EV enthusiast',
                RewardsPromo: 'Cephra Rewards',
                RewardsDesc: 'Kumuha ng Cephra credits sa unang karga. Perpekto para sa madalas mag-charge na nais makatipid.',
                ClaimNow: 'Kunin Ngayon',
                PremiumTitle: 'Mabilis na Pagkarga',
                PremiumDesc: 'I-unlock ang prayoridad na pagkarga, advanced na teknolohiya, at dekalidad na serbisyo para sa premium na kliyente.',
                BookNow: 'Mag-book Ngayon',
                TryNow: 'Subukan Ngayon',
                ContestTitle: 'EV Champion 2025',
                ContestDesc: 'Sumali sa aming taunang charging contest. Manalo ng premium subscription, eksklusibong merchandise, at charging credits.',
                JoinContest: 'Sumali',
                RegistrationOpen: 'Bukas ang Rehistrasyon',
                ValidUntil: 'Bisa hanggang',
                Platform: 'Plataporma',
                Support: 'Suporta',
                Company: 'Kumpanya'
            },
            ceb: {
                TitleMain: 'Cephra',
                TitleAccent: 'Ultimate',
                TitleSub: 'Charging Platform',
                HeroDesc: 'Gipasaligan nga EV charging platform sa kapin 50,000 ka drayber. Masinati ang kaugmaon sa pag‑charge—maalamon, paspas, ug kasaligan.',
                Charging: 'Pag-charge',
                Offers: 'Mga Alok',
                About: 'Mahitungod',
                Register: 'Magparehistro',
                Login: 'Sulod',
                StartCharging: 'Sugdi ang Pag-charge',
                LearnMore: 'Hibalo Dugang',
                WhyChoose: 'Ngano Pilion ang Cephra?',
                WhyDesc: 'Masunod nga henerasyon sa teknolohiya sa EV charging',
                SmartCharging: 'Maayong Pag-charge',
                SmartLink: 'Sulayi ang Maayong Pag-charge →',
                RealTime: 'Real-time nga Pagbantay',
                RealTimeLink: 'Tan-awa ang Analitika →',
                Rewards: 'Cephra Rewards',
                RewardsLink: 'Apil sa Rewards →',
                ChooseSpeed: 'Pilia ang Imong Kadalikyat sa Pag-charge',
                ChooseDesc: 'Pilia ang husto nga kapilian para sa imong panginahanglan',
                NormalCharging: 'Ordinaryong Pag-charge',
                NormalDesc: 'Angay para sa kanunay nga pag-charge kung dili nagdali. Maayo para sa adlaw-adlaw nga gamit, overnight sa balay, o dugay nga pag-parking. Naghatag og lig-on ug kasaligan nga pag-charge nga dili makastress sa baterya.',
                NormalSpec1: '3-7 kW',
                NormalSpec2: '2-3 oras',
                NormalLink: 'Sugdi ang Ordinaryong Pag-charge →',
                FastCharging: 'Paspas nga Pag-charge',
                FastDesc: 'Kung hinahanglan ang kadali, paspas nga pag-charge ang mobalik kanimo sa dalan dayon. Angay para sa paniudto, pamalit, o dali nga top‑up sa taas nga biyahe. Ang among abanteng teknolohiya naghatag og optimal nga curve para sa kahusayan ug proteksyon sa baterya.',
                FastSpec1: '50-150 kW',
                FastSpec2: '20-40 minutos',
                FastLink: 'Sugdi ang Paspas nga Pag-charge →',
                PriorityCharging: 'Prayoridad nga Pag-charge',
                PriorityDesc: 'Kung moubos sa 20% ang baterya, awtomatikong mo-activate ang prayoridad nga pag-charge para sa kaluwasan. Siguradong dali nga pagtagad, pinakataas nga tulin, ug garantiya sa pagkab-ot.',
                PrioritySpec1: 'Prayoridad',
                PrioritySpec2: '10-30 minutos',
                PriorityLink: 'Kuhaa ang Prayoridad →',
                ExclusiveOffers: 'Eksklusibong mga Alok',
                ExclusiveDesc: 'Limitado nga promosyon para sa mga EV enthusiasts',
                RewardsPromo: 'Cephra Rewards',
                RewardsDesc: 'Kuhaa ang Cephra credits sa imong unang pag-charge uban kanamo. Angay para sa kanunay nga magpa-charge nga gusto makatipid.',
                ClaimNow: 'Kuhaa karon',
                PremiumTitle: 'Paspas nga Pag-charge',
                PremiumDesc: 'Ablihi ang prayoridad nga pag-charge, abanteng teknolohiya, ug kalidad nga serbisyo para sa among premium nga kliyente.',
                BookNow: 'Mag-book Karon',
                TryNow: 'Sulayi Karon',
                ContestTitle: 'EV Champion 2025',
                ContestDesc: 'Apila ang among tinuig nga kontest sa pag-charge. Ang mga labing aktibo makadaog og premium subscription, espesyal nga paninda, ug charging credits.',
                JoinContest: 'Apil sa Kontest',
                RegistrationOpen: 'Abli ang Rehistrasyon',
                ValidUntil: 'Balido hangtod',
                Platform: 'Plataporma',
                Support: 'Tabang',
                Company: 'Kompanya'
            },
            zh: {
                TitleMain: 'Cephra',
                TitleAccent: 'Ultimate',
                TitleSub: 'Charging Platform',
                HeroDesc: '屡获殊荣的电动车充电平台，已被 50,000+ 车主信赖。体验智能、快速、稳定的下一代充电方案。',
                Charging: '充电',
                Offers: '优惠',
                About: '关于',
                Register: '注册',
                Login: '登录',
                StartCharging: '开始充电',
                LearnMore: '了解更多',
                WhyChoose: '为什么选择 Cephra？',
                WhyDesc: '体验新一代电动车充电技术',
                SmartCharging: '智能充电',
                SmartLink: '体验智能充电 →',
                RealTime: '实时监控',
                RealTimeLink: '查看分析 →',
                Rewards: 'Cephra 积分',
                RewardsLink: '加入积分 →',
                ChooseSpeed: '选择你的充电速度',
                ChooseDesc: '选择最适合你的充电方式',
                NormalCharging: '普通充电',
                NormalDesc: '适合日常不赶时间的充电。非常适合日常使用、在家过夜充电，或长时间停车期间使用。提供稳定可靠的电力，不会给电池带来压力。',
                NormalSpec1: '3-7 kW',
                NormalSpec2: '2-3 小时',
                NormalLink: '开始普通充电 →',
                FastCharging: '快速充电',
                FastDesc: '当时间紧迫时，快速充电可让您迅速回到路上。适合午餐、购物或长途行驶中的快速补电。我们的先进充电技术提供最佳充电曲线，在保证效率的同时保护电池健康。',
                FastSpec1: '50-150 kW',
                FastSpec2: '20-40 分钟',
                FastLink: '开始快速充电 →',
                PriorityCharging: '优先充电',
                PriorityDesc: '当电量低于 20% 时，优先充电会自动启动，保障消防与行车安全。确保您的车辆立即获得关注，提供最大充电速度与可用性，保护车辆并保障您的行车安全。',
                PrioritySpec1: '优先',
                PrioritySpec2: '10-30 分钟',
                PriorityLink: '获取优先权 →',
                ExclusiveOffers: '专属优惠',
                ExclusiveDesc: '为电动车爱好者打造的限时优惠',
                RewardsPromo: 'Cephra 积分',
                RewardsDesc: '首次充电即可获得 Cephra 积分。非常适合经常充电的用户，帮助最大化节省。',
                ClaimNow: '立即领取',
                PremiumTitle: '快速充电',
                PremiumDesc: '解锁优先充电、先进充电技术，以及面向高端客户的优质服务。',
                BookNow: '立即预订',
                TryNow: '立即体验',
                ContestTitle: 'EV 冠军 2025',
                ContestDesc: '参加我们的年度充电比赛。优胜者将获得高级订阅、独家礼品和充电积分。',
                JoinContest: '参加比赛',
                RegistrationOpen: '报名开启',
                ValidUntil: '有效期至',
                Platform: '平台',
                Support: '支持',
                Company: '公司'
            }
        };

        // Load saved language preference
        function loadLanguagePreference() {
            const savedLang = localStorage.getItem('selectedLanguage') || 'en';
            updateLanguageDisplay(savedLang);
        }

        // Update language display
        function updateLanguageDisplay(lang) {
            const languageText = document.querySelector('.language-text');
            const langMap = { 'en': 'EN', 'fil': 'Fil', 'ceb': 'Bisaya', 'zh': '中文' };
            languageText.textContent = langMap[lang] || 'EN';
            localStorage.setItem('selectedLanguage', lang);

            // Apply translations
            const t = i18n[lang] || i18n.en;
            // Top nav
            document.querySelectorAll('.nav-list .nav-link')[0].textContent = t.Charging;
            document.querySelectorAll('.nav-list .nav-link')[1].textContent = t.Offers;
            document.querySelectorAll('.nav-list .nav-link')[2].textContent = t.About;
            // Auth
            const auth = document.querySelectorAll('.auth-buttons .auth-link');
            if (auth[0]) auth[0].textContent = t.Register;
            if (auth[1]) auth[1].textContent = t.Login;
            // Hero CTA
            const ctas = document.querySelectorAll('.hero-actions a');
            if (ctas[0]) ctas[0].textContent = t.StartCharging;
            if (ctas[1]) ctas[1].textContent = t.LearnMore;
            // Hero heading/description
            const heroMain = document.querySelector('.hero-title-main');
            const heroAccent = document.querySelector('.hero-title-accent');
            const heroSub = document.querySelector('.hero-title-sub');
            const heroDesc = document.querySelector('.hero-description');
            if (heroMain) heroMain.textContent = t.TitleMain;
            if (heroAccent) heroAccent.textContent = t.TitleAccent;
            if (heroSub) heroSub.textContent = t.TitleSub;
            if (heroDesc) heroDesc.textContent = t.HeroDesc;
            // Section headers
            const whyTitle = document.querySelector('#features .section-title');
            const whyDesc = document.querySelector('#features .section-description');
            if (whyTitle) whyTitle.textContent = t.WhyChoose;
            if (whyDesc) whyDesc.textContent = t.WhyDesc;
            // Feature cards
            const featureTitles = document.querySelectorAll('.features .feature-title');
            const featureLinks = document.querySelectorAll('.features .feature-link');
            if (featureTitles[0]) featureTitles[0].textContent = t.SmartCharging;
            if (featureLinks[0]) featureLinks[0].textContent = t.SmartLink;
            if (featureTitles[1]) featureTitles[1].textContent = t.RealTime;
            if (featureLinks[1]) featureLinks[1].textContent = t.RealTimeLink;
            if (featureTitles[2]) featureTitles[2].textContent = t.Rewards;
            if (featureLinks[2]) featureLinks[2].textContent = t.RewardsLink;
            // Charging types section
            const chargingHeader = document.querySelector('#charging .section-title');
            const chargingDesc = document.querySelector('#charging .section-description');
            if (chargingHeader) chargingHeader.textContent = t.ChooseSpeed;
            if (chargingDesc) chargingDesc.textContent = t.ChooseDesc;
            const chargingTitles = document.querySelectorAll('.charging-card .charging-title');
            const chargingLinks = document.querySelectorAll('.charging-card .charging-link');
            const chargingDescs = document.querySelectorAll('.charging-card .charging-description');
            const chargingSpecsLeft = document.querySelectorAll('.charging-card .charging-specs .spec-item:first-child');
            const chargingSpecsRight = document.querySelectorAll('.charging-card .charging-specs .spec-item:last-child');
            if (chargingTitles[0]) chargingTitles[0].textContent = t.NormalCharging;
            if (chargingDescs[0]) chargingDescs[0].textContent = t.NormalDesc;
            if (chargingSpecsLeft[0]) chargingSpecsLeft[0].textContent = t.NormalSpec1;
            if (chargingSpecsRight[0]) chargingSpecsRight[0].textContent = t.NormalSpec2;
            if (chargingLinks[0]) chargingLinks[0].textContent = t.NormalLink;
            if (chargingTitles[1]) chargingTitles[1].textContent = t.FastCharging;
            if (chargingDescs[1]) chargingDescs[1].textContent = t.FastDesc;
            if (chargingSpecsLeft[1]) chargingSpecsLeft[1].textContent = t.FastSpec1;
            if (chargingSpecsRight[1]) chargingSpecsRight[1].textContent = t.FastSpec2;
            if (chargingLinks[1]) chargingLinks[1].textContent = t.FastLink;
            if (chargingTitles[2]) chargingTitles[2].textContent = t.PriorityCharging;
            if (chargingDescs[2]) chargingDescs[2].textContent = t.PriorityDesc;
            if (chargingSpecsLeft[2]) chargingSpecsLeft[2].textContent = t.PrioritySpec1;
            if (chargingSpecsRight[2]) chargingSpecsRight[2].textContent = t.PrioritySpec2;
            if (chargingLinks[2]) chargingLinks[2].textContent = t.PriorityLink;
            // Promotions
            const promoHeader = document.querySelector('#promotions .section-title');
            const promoDesc = document.querySelector('#promotions .section-description');
            if (promoHeader) promoHeader.textContent = t.ExclusiveOffers;
            if (promoDesc) promoDesc.textContent = t.ExclusiveDesc;
            const promoTitles = document.querySelectorAll('.promo-card .promo-title');
            if (promoTitles[0]) promoTitles[0].textContent = t.RewardsPromo;
            if (promoTitles[1]) promoTitles[1].textContent = t.PremiumTitle;
            if (promoTitles[2]) promoTitles[2].textContent = t.ContestTitle;
            const promoDescriptions = document.querySelectorAll('.promo-card .promo-description');
            if (promoDescriptions[0]) promoDescriptions[0].textContent = t.RewardsDesc;
            if (promoDescriptions[1]) promoDescriptions[1].textContent = t.PremiumDesc;
            if (promoDescriptions[2]) promoDescriptions[2].textContent = t.ContestDesc;
            const promoBtns = document.querySelectorAll('.promo-card .btn-promo');
            if (promoBtns[0]) promoBtns[0].textContent = t.ClaimNow;
            if (promoBtns[1]) promoBtns[1].textContent = t.BookNow;
            if (promoBtns[2]) promoBtns[2].textContent = t.JoinContest;
            const promoValidity = document.querySelectorAll('.promo-validity');
            if (promoValidity[0]) promoValidity[0].textContent = `${t.ValidUntil} Dec 31, 2025`;
            if (promoValidity[1]) promoValidity[1].textContent = t.TryNow;
            if (promoValidity[2]) promoValidity[2].textContent = t.RegistrationOpen;
            // Footer headings
            const footerTitles = document.querySelectorAll('.footer .footer-title');
            if (footerTitles[0]) footerTitles[0].textContent = t.Platform;
            if (footerTitles[1]) footerTitles[1].textContent = t.Support;
            if (footerTitles[2]) footerTitles[2].textContent = t.Company;
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
                    const langMap = { 'en': 'EN', 'ceb': 'Bisaya', 'zh': '中文' };
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
