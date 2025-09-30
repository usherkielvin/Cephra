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
    <title>Our Team - Cephra</title>
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

        .team-hero {
            background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
            padding: 120px 0 80px;
            text-align: center;
        }

        .leadership-section {
            padding: 80px 0;
            background: white;
        }

        .leadership-grid {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 3rem;
            margin-bottom: 4rem;
        }

        .team-section {
            padding: 80px 0;
            background: var(--bg-secondary);
        }

        .team-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 2rem;
        }

        .team-member-card {
            background: white;
            border-radius: 20px;
            overflow: hidden;
            border: 1px solid var(--border-color);
            transition: all 0.3s ease;
            position: relative;
        }

        .team-member-card:hover {
            transform: translateY(-10px);
            box-shadow: 0 20px 40px var(--shadow-medium);
            border-color: var(--primary-color);
        }

        .member-photo {
            width: 150px;
            height: 150px;
            background: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            position: relative;
            overflow: hidden;
            border-radius: 50%;
            margin: 20px auto 0;
        }

        .member-photo img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .member-photo-placeholder {
            font-size: 4rem;
            color: white;
        }

        .member-info {
            padding: 1.5rem;
            text-align: center;
        }

        .member-name {
            font-size: 1.3rem;
            font-weight: 700;
            color: var(--text-primary);
            margin-bottom: 0.5rem;
        }

        .member-role {
            font-size: 1rem;
            font-weight: 600;
            color: var(--primary-color);
            margin-bottom: 1rem;
        }

        .member-bio {
            color: var(--text-secondary);
            line-height: 1.6;
            font-size: 0.9rem;
        }

        .member-social {
            position: absolute;
            top: 10px;
            right: 10px;
            opacity: 0;
            transition: opacity 0.3s ease;
        }

        .team-member-card:hover .member-social {
            opacity: 1;
        }

        .social-links {
            display: flex;
            gap: 0.5rem;
        }

        .social-link {
            width: 35px;
            height: 35px;
            background: rgba(255, 255, 255, 0.9);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            color: var(--primary-color);
            text-decoration: none;
            transition: all 0.3s ease;
        }

        .social-link:hover {
            background: var(--primary-color);
            color: white;
            transform: scale(1.1);
        }

        .department-filter {
            display: flex;
            justify-content: center;
            gap: 1rem;
            margin-bottom: 3rem;
            flex-wrap: wrap;
        }

        .filter-btn {
            padding: 0.75rem 1.5rem;
            border: 2px solid var(--border-color);
            background: white;
            border-radius: 25px;
            cursor: pointer;
            font-weight: 600;
            color: var(--text-secondary);
            transition: all 0.3s ease;
        }

        .filter-btn:hover,
        .filter-btn.active {
            border-color: var(--primary-color);
            color: var(--primary-color);
            background: rgba(0, 194, 206, 0.05);
        }

        .join-section {
            padding: 80px 0;
            background: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%);
            color: white;
            text-align: center;
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
            background: white;
            color: var(--primary-color);
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(255, 255, 255, 0.3);
        }

        .btn-secondary {
            background: var(--primary-color);
            color: white;
            border: 2px solid var(--primary-color);
        }

        .btn-secondary:hover {
            background: white;
            color: var(--primary-color);
            transform: translateY(-2px);
        }

        @media (max-width: 768px) {
            .team-hero {
                padding: 100px 0 60px;
            }

            .leadership-grid {
                grid-template-columns: 1fr;
                gap: 2rem;
            }

            .team-grid {
                grid-template-columns: 1fr;
            }

            .department-filter {
                gap: 0.5rem;
            }

            .filter-btn {
                padding: 0.5rem 1rem;
                font-size: 0.9rem;
            }

            .member-photo {
                width: 150px;
                height: 150px;
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

    <!-- Team Hero -->
    <section class="team-hero">
        <div class="container">
            <h1 style="font-size: clamp(2.5rem, 6vw, 4rem); font-weight: 900; margin-bottom: 1rem; background: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text;">Meet Our Team</h1>
            <p style="font-size: 1.3rem; color: var(--text-secondary); max-width: 600px; margin: 0 auto 2rem;">The passionate individuals driving Cephra's mission to revolutionize electric vehicle charging.</p>
            <div style="display: flex; gap: 1rem; justify-content: center; flex-wrap: wrap;">

                <a href="#leadership" class="btn btn-secondary">Our Team</a>
            </div>
        </div>
    </section>

    <!-- Leadership Section -->
    <section class="leadership-section" id="leadership">
        <div class="container">
            <div class="section-header" style="text-align: center; margin-bottom: 60px;">
                <h2 style="font-size: 2.5rem; font-weight: 700; margin-bottom: 1rem; background: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text;">Our Team</h2>
                <p style="font-size: 1.2rem; color: var(--text-secondary);">Visionary leaders guiding our mission</p>
            </div>

            <div class="leadership-grid">
                <div class="team-member-card">
                    <div class="member-photo">
                        <img src="images/team pictures/default.png" alt="Usher Kielvin Ponce" />
                    </div>
                    <div class="member-info">
                        <h3 class="member-name">Usher Kielvin Ponce</h3>
                        <div class="member-role">Project Lead, Backend Developer & EV Technology Enthusiast</div>
                        <p class="member-bio">- Backend logic & project coordination</p>
                        <p class="member-bio">- Manages EV charging system flow</p>
                    </div>
                    <div class="member-social">
                        <div class="social-links">
                            <a href="https://github.com/usherkielvin" class="social-link"><i class="fab fa-github"></i></a>
                        </div>
                    </div>
                </div>

                <div class="team-member-card">
                    <div class="member-photo">
                        <img src="images/team pictures/delacruz.jpeg" alt="Mark Dwayne P. Dela Cruz" />
                    </div>
                    <div class="member-info">
                        <h3 class="member-name">Mark Dwayne P. Dela Cruz</h3>
                        <div class="member-role">Web Interface & User Experience</div>
                        <p class="member-bio">- Designs and develops mobile web interface for customers</p>
                        <p class="member-bio">- Focuses on intuitive, responsive layouts</p>

                    </div>
                    <div class="member-social">
                        <div class="social-links">
                            <a href="https://github.com/cwossant" class="social-link"><i class="fab fa-github"></i></a>
                        </div>
                    </div>
                </div>

                <div class="team-member-card">
                    <div class="member-photo">
                        <img src="images/team pictures/default.png" alt="Dizon S. Dizon" />
                    </div>
                    <div class="member-info">
                        <h3 class="member-name">Dizon S. Dizon</h3>
                        <div class="member-role">Backend Development & Database Architecture</div>
                         <p class="member-bio">- Expert in Java backend development</p>
                        <p class="member-bio">- Handles server-side logic and database design</p>
                    </div>
                    <div class="member-social">
                        <div class="social-links">
                            <a href="https://github.com/Dizon-69" class="social-link"><i class="fab fa-github"></i></a>
                        </div>
                    </div>
                </div>

                <div class="team-member-card">
                    <div class="member-photo">
                        <img src="images/team pictures/kenji.jpeg" alt="Kenji A. Hizon" />
                    </div>
                    <div class="member-info">
                        <h3 class="member-name">Kenji A. Hizon</h3>
                        <div class="member-role">Desktop Application Interface Developer</div>
                         <p class="member-bio">- Specializes in Java Swing GUI</p>
                        <p class="member-bio">- Focuses on building an intuitive and functional desktop interface</p>
                    </div>
                    <div class="member-social">
                        <div class="social-links">
                            <a href="https://github.com/froshitech" class="social-link"><i class="fab fa-github"></i></a>
                        </div>
                    </div>
                </div>
                
            </div>
        </div>
    </section>

   

    <!-- Join Section -->
    <section class="join-section">
        <div class="container">
            <h2 style="font-size: 2.5rem; font-weight: 700; margin-bottom: 1rem;">Join Our Mission</h2>
            <p style="font-size: 1.2rem; margin-bottom: 2rem; opacity: 0.9;">We're always looking for talented individuals who share our passion for sustainable transportation.</p>
            <div style="display: flex; gap: 1rem; justify-content: center; flex-wrap: wrap;">
                <a href="contact_us.php" class="btn btn-primary">Get in Touch</a>
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

        // Department Filter Functionality
        function initDepartmentFilter() {
            const filterButtons = document.querySelectorAll('.filter-btn');
            const teamMembers = document.querySelectorAll('.team-member-card');

            filterButtons.forEach(button => {
                button.addEventListener('click', function() {
                    // Remove active class from all buttons
                    filterButtons.forEach(btn => btn.classList.remove('active'));
                    // Add active class to clicked button
                    this.classList.add('active');

                    const filterValue = this.getAttribute('data-filter');

                    teamMembers.forEach(member => {
                        if (filterValue === 'all' || member.getAttribute('data-department') === filterValue) {
                            member.style.display = 'block';
                            setTimeout(() => {
                                member.style.opacity = '1';
                                member.style.transform = 'scale(1)';
                            }, 10);
                        } else {
                            member.style.opacity = '0';
                            member.style.transform = 'scale(0.8)';
                            setTimeout(() => {
                                member.style.display = 'none';
                            }, 300);
                        }
                    });
                });
            });
        }

        // Add scroll effect to header
        window.addEventListener('scroll', function() {
            const header = document.querySelector('.header');
            if (window.scrollY > 100) {
                header.classList.add('scrolled');
            } else {
                header.classList.remove('scrolled');
            }
        });

        // Initialize on load
        document.addEventListener('DOMContentLoaded', function() {
            initMobileMenu();
            initDepartmentFilter();

            // Add initial animation styles
            const teamMembers = document.querySelectorAll('.team-member-card');
            teamMembers.forEach(member => {
                member.style.transition = 'all 0.3s ease';
            });
        });
    </script>
</body>
</html>
