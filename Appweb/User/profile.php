<?php
session_start();
if (!isset($_SESSION['username'])) {
	header("Location: index.php");
	exit();
}

require_once 'config/database.php';

$db = new Database();
$conn = $db->getConnection();

$username = $_SESSION['username'];
$user = null;
$memberSinceFormatted = '';
$profilePicture = null;
$successMessage = '';
$errorMessage = '';

// Handle form submissions
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
	if (isset($_POST['update_profile'])) {
		// Handle profile update
		$firstname = trim($_POST['firstname'] ?? '');
		$lastname = trim($_POST['lastname'] ?? '');
		$email = trim($_POST['email'] ?? '');
		$newUsername = trim($_POST['username'] ?? '');

		if (empty($firstname) || empty($lastname) || empty($email) || empty($newUsername)) {
			$errorMessage = 'All fields are required.';
		} elseif (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
			$errorMessage = 'Please enter a valid email address.';
		} else {
			try {
				// Check if username is already taken (if different from current)
				if ($newUsername !== $username) {
					$stmt = $conn->prepare("SELECT id FROM users WHERE username = :username AND username != :current_username");
					$stmt->bindParam(':username', $newUsername);
					$stmt->bindParam(':current_username', $username);
					$stmt->execute();
					if ($stmt->rowCount() > 0) {
						$errorMessage = 'Username is already taken.';
					}
				}

				if (empty($errorMessage)) {
					// Update user profile
					$stmt = $conn->prepare("UPDATE users SET firstname = :firstname, lastname = :lastname, email = :email, username = :username WHERE username = :current_username");
					$stmt->bindParam(':firstname', $firstname);
					$stmt->bindParam(':lastname', $lastname);
					$stmt->bindParam(':email', $email);
					$stmt->bindParam(':username', $newUsername);
					$stmt->bindParam(':current_username', $username);
					$stmt->execute();

					// Update session username if changed
					if ($newUsername !== $username) {
						$_SESSION['username'] = $newUsername;
						$username = $newUsername;
					}

					$successMessage = 'Profile updated successfully!';
				}
			} catch (Exception $e) {
				$errorMessage = 'Failed to update profile. Please try again.';
			}
		}
	} elseif (isset($_POST['upload_photo'])) {
		// Handle profile photo upload
		if (isset($_FILES['profile_photo']) && $_FILES['profile_photo']['error'] === UPLOAD_ERR_OK) {
			$file = $_FILES['profile_photo'];
			$allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif'];
			$maxSize = 5 * 1024 * 1024; // 5MB

			if (!in_array($file['type'], $allowedTypes)) {
				$errorMessage = 'Please upload a valid image file (JPEG, PNG, or GIF).';
			} elseif ($file['size'] > $maxSize) {
				$errorMessage = 'Image file size must be less than 5MB.';
			} else {
				// Create uploads directory if it doesn't exist
				$uploadDir = 'uploads/profile_pictures/';
				if (!is_dir($uploadDir)) {
					mkdir($uploadDir, 0755, true);
				}

				// Generate unique filename
				$extension = pathinfo($file['name'], PATHINFO_EXTENSION);
				$filename = $username . '_profile_' . time() . '.' . $extension;
				$uploadPath = $uploadDir . $filename;

				if (move_uploaded_file($file['tmp_name'], $uploadPath)) {
					// Update database with new profile picture
					$stmt = $conn->prepare("UPDATE users SET profile_picture = :profile_picture WHERE username = :username");
					$stmt->bindParam(':profile_picture', $filename);
					$stmt->bindParam(':username', $username);
					$stmt->execute();

					$successMessage = 'Profile photo updated successfully!';
				} else {
					$errorMessage = 'Failed to upload image. Please try again.';
				}
			}
		} else {
			$errorMessage = 'Please select an image to upload.';
		}
	}
}

if ($conn) {
	$stmt = $conn->prepare("SELECT username, firstname, lastname, email, profile_picture, created_at FROM users WHERE username = :username LIMIT 1");
	$stmt->bindParam(':username', $username);
	$stmt->execute();
	$user = $stmt->fetch(PDO::FETCH_ASSOC) ?: null;

	if ($user && !empty($user['created_at'])) {
		try {
			$dt = new DateTime($user['created_at']);
			$memberSinceFormatted = $dt->format('M d, Y h:i A');
		} catch (Exception $e) {
			$memberSinceFormatted = $user['created_at'];
		}
	}

	$profilePicture = $user['profile_picture'] ?? null;
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Profile - Cephra</title>
    <link rel="icon" type="image/png" href="images/logo.png?v=2" />
    <link rel="apple-touch-icon" href="images/logo.png?v=2" />
    <link rel="manifest" href="manifest.webmanifest" />
    <meta name="theme-color" content="#1a1a2e" />
    <link rel="stylesheet" href="css/vantage-style.css" />
    <link rel="stylesheet" href="assets/css/fontawesome-all.min.css" />
    <link rel="stylesheet" href="assets/css/pages/profile.css" />
</head>
<body>
    <!-- Header -->
    <header class="header">
        <div class="container">
                <div class="header-content">
						<!-- Logo -->
						<div class="logo">
							<img src="images/logo.png" alt="Cephra" class="logo-img">
							<span class="logo-text">CEPHRA</span>
						</div>

						<!-- Navigation -->
						<nav class="nav">
							<ul class="nav-list">
								<li><a href="dashboard.php" class="nav-link">Dashboard</a></li>
								<li><a href="link.php" class="nav-link">Link</a></li>
								<li><a href="history.php" class="nav-link">History</a></li>
								<li class="current_page_item"><a href="profile.php" class="nav-link">Profile</a></li>
							</ul>
						</nav>

						<!-- Header Actions -->
						<div class="header-actions">
							<div class="auth-buttons">
								<a href="dashboard.php" class="nav-link auth-link">Back</a>
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

    <!-- Profile Section -->
    <section class="profile-section" style="padding: 100px 0; background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);">
        <div class="container">
            <div class="section-header" style="text-align: center; margin-bottom: 60px;">
                <h2 class="section-title" style="font-size: 2.5rem; font-weight: 700; margin-bottom: 1rem; background: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text;">Your Profile</h2>
                <p class="section-description" style="font-size: 1.2rem; color: rgba(26, 32, 44, 0.8); max-width: 600px; margin: 0 auto;">Manage your account information and settings</p>
            </div>

            <!-- Success/Error Messages -->
            <?php if (!empty($successMessage)): ?>
                <div class="alert alert-success" style="background: #d4edda; color: #155724; border: 1px solid #c3e6cb; border-radius: 8px; padding: 1rem; margin-bottom: 2rem; text-align: center;">
                    <?php echo htmlspecialchars($successMessage); ?>
                </div>
            <?php endif; ?>
            <?php if (!empty($errorMessage)): ?>
                <div class="alert alert-error" style="background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; border-radius: 8px; padding: 1rem; margin-bottom: 2rem; text-align: center;">
                    <?php echo htmlspecialchars($errorMessage); ?>
                </div>
            <?php endif; ?>

            <div class="profile-container" style="display: grid; grid-template-columns: 1fr 2fr; gap: 2rem; margin-bottom: 2rem;">
                <style>
                    @media (max-width: 768px) {
                        .profile-container {
                            grid-template-columns: 1fr !important;
                            gap: 1rem !important;
                        }
                        .profile-photo-section, .profile-info-section {
                            padding: 1.5rem !important;
                        }
                        .profile-photo {
                            width: 120px !important;
                            height: 120px !important;
                        }
                        .form-row {
                            grid-template-columns: 1fr !important;
                            gap: 0 !important;
                        }
                        .form-actions {
                            flex-direction: column !important;
                            align-items: center !important;
                        }
                        .btn-primary, .btn-reset-password, .btn-back {
                            width: 100% !important;
                            max-width: 300px !important;
                        }
                    }
                </style>
                <!-- Profile Photo Section -->
                <div class="profile-photo-section" style="background: white; border-radius: 20px; padding: 2rem; border: 1px solid rgba(26, 32, 44, 0.1); box-shadow: 0 5px 15px rgba(0, 194, 206, 0.1);">
                    <div class="photo-container" style="text-align: center; position: relative;">
                        <!-- Profile Photo -->
                        <div class="profile-photo" style="width: 150px; height: 150px; border-radius: 50%; margin: 0 auto 1rem; border: 4px solid #00c2ce; position: relative; overflow: visible;">
                            <?php if ($profilePicture && file_exists('uploads/profile_pictures/' . $profilePicture)): ?>
                                <img src="uploads/profile_pictures/<?php echo htmlspecialchars($profilePicture); ?>" alt="Profile Photo" id="profilePhoto" style="width: 100%; height: 100%; object-fit: cover; border-radius: 50%;">
                            <?php else: ?>
                                <div class="photo-placeholder" style="width: 100%; height: 100%; background: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%); display: flex; align-items: center; justify-content: center; color: white; font-size: 3rem; border-radius: 50%;">
                                    <i class="fas fa-user"></i>
                                </div>
                            <?php endif; ?>
                            <!-- Online Status Indicator (Bottom Left) -->
                            <div style="position: absolute; bottom: 10px; left: 10px; width: 20px; height: 20px; background: #4CAF50; border: 3px solid white; border-radius: 50%; z-index: 10;"></div>
                            <!-- Camera Icon for Photo Upload (Bottom Right, Overlapping Photo) -->
                            <button type="button" onclick="document.getElementById('profilePhotoInput').click()" style="position: absolute; bottom: 5px; right: 5px; width: 40px; height: 40px; background: rgba(0, 194, 206, 0.9); color: white; border: 3px solid white; border-radius: 50%; cursor: pointer; font-size: 1rem; display: flex; align-items: center; justify-content: center; transition: all 0.3s ease; z-index: 20;">
                                <i class="fas fa-camera"></i>
                            </button>
                        </div>

                        <!-- User Info -->
                        <div style="margin-bottom: 1.5rem;">
                            <h3 style="margin: 0; font-size: 1.5rem; font-weight: 700; color: #1a202c;"><?php echo htmlspecialchars($user['firstname'] ?? '') . ' ' . htmlspecialchars($user['lastname'] ?? ''); ?></h3>
                            <p style="margin: 0.5rem 0 0 0; color: rgba(26, 32, 44, 0.7); font-size: 0.9rem;">@<?php echo htmlspecialchars($username); ?></p>
                            <div style="display: inline-flex; align-items: center; gap: 0.5rem; margin-top: 0.5rem;">
                                <div style="padding: 0.25rem 0.75rem; background: #4CAF50; color: white; border-radius: 15px; font-size: 0.8rem; font-weight: 600;">
                                    <i class="fas fa-shield-alt"></i> Verified
                                </div>
                                <div style="padding: 0.25rem 0.75rem; background: #2196F3; color: white; border-radius: 15px; font-size: 0.8rem; font-weight: 600;">
                                    <i class="fas fa-leaf"></i> Eco Driver
                                </div>
                            </div>
                        </div>

                        <!-- Account Statistics -->
                        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; margin-bottom: 1.5rem;">
                            <div style="text-align: center; padding: 1rem; background: rgba(76, 175, 80, 0.05); border-radius: 10px; border: 1px solid rgba(76, 175, 80, 0.2);">
                                <div style="font-size: 1.5rem; font-weight: 700; color: #4CAF50; margin-bottom: 0.25rem;">156h</div>
                                <div style="font-size: 0.8rem; color: rgba(26, 32, 44, 0.7);">Total Charging</div>
                            </div>
                            <div style="text-align: center; padding: 1rem; background: rgba(33, 150, 243, 0.05); border-radius: 10px; border: 1px solid rgba(33, 150, 243, 0.2);">
                                <div style="font-size: 1.5rem; font-weight: 700; color: #2196F3; margin-bottom: 0.25rem;">847kg</div>
                                <div style="font-size: 0.8rem; color: rgba(26, 32, 44, 0.7);">CO₂ Saved</div>
                            </div>
                        </div>

                        <!-- Charging Streak -->
                        <div style="margin-bottom: 1.5rem; text-align: center;">
                            <div style="display: inline-flex; align-items: center; gap: 0.5rem; padding: 1rem 3rem; background: linear-gradient(135deg, #FF9800 0%, #F57C00 100%); color: white; border-radius: 20px; font-weight: 600;">
                                <i class="fas fa-fire"></i>
                                <span>7 Day Streak</span>
                            </div>
                        </div>



                    </div>
                </div>

                <!-- Profile Information Section -->
                <div class="profile-info-section" style="background: white; border-radius: 20px; padding: 2rem; border: 1px solid rgba(26, 32, 44, 0.1); box-shadow: 0 5px 15px rgba(0, 194, 206, 0.1);">
                    <form method="POST" class="profile-form">
                        <div class="form-group" style="margin-bottom: 1.5rem;">
                            <label for="username" style="display: block; margin-bottom: 0.5rem; font-weight: 600; color: #1a202c;">Username</label>
                            <input type="text" id="username" name="username" value="<?php echo htmlspecialchars($user['username'] ?? $username); ?>" required style="width: 100%; padding: 0.75rem; border: 1px solid rgba(26, 32, 44, 0.1); border-radius: 8px; font-size: 1rem; transition: border-color 0.3s ease;">
                        </div>

                        <div class="form-row" style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; margin-bottom: 1.5rem;">
                            <div class="form-group">
                                <label for="firstname" style="display: block; margin-bottom: 0.5rem; font-weight: 600; color: #1a202c;">First Name</label>
                                <input type="text" id="firstname" name="firstname" value="<?php echo htmlspecialchars($user['firstname'] ?? ''); ?>" required style="width: 100%; padding: 0.75rem; border: 1px solid rgba(26, 32, 44, 0.1); border-radius: 8px; font-size: 1rem; transition: border-color 0.3s ease;">
                            </div>
                            <div class="form-group">
                                <label for="lastname" style="display: block; margin-bottom: 0.5rem; font-weight: 600; color: #1a202c;">Last Name</label>
                                <input type="text" id="lastname" name="lastname" value="<?php echo htmlspecialchars($user['lastname'] ?? ''); ?>" required style="width: 100%; padding: 0.75rem; border: 1px solid rgba(26, 32, 44, 0.1); border-radius: 8px; font-size: 1rem; transition: border-color 0.3s ease;">
                            </div>
                        </div>

                        <div class="form-group" style="margin-bottom: 1.5rem;">
                            <label for="email" style="display: block; margin-bottom: 0.5rem; font-weight: 600; color: #1a202c;">Email</label>
                            <input type="email" id="email" name="email" value="<?php echo htmlspecialchars($user['email'] ?? ''); ?>" required style="width: 100%; padding: 0.75rem; border: 1px solid rgba(26, 32, 44, 0.1); border-radius: 8px; font-size: 1rem; transition: border-color 0.3s ease;">
                        </div>

                        <div class="form-group" style="margin-bottom: 1.5rem;">
                            <label style="display: block; margin-bottom: 0.5rem; font-weight: 600; color: #1a202c;">Member Since</label>
                            <div class="info-display" style="padding: 0.75rem; background: rgba(0, 194, 206, 0.1); border-radius: 8px; color: #1a202c;"><?php echo htmlspecialchars($memberSinceFormatted); ?></div>
                        </div>

                        <div class="form-actions" style="display: flex; gap: 1rem; justify-content: center; margin-top: 2rem;">
                            <button type="submit" name="update_profile" class="btn-primary" style="background: transparent; color: #00c2ce; border: 2px solid #00c2ce; padding: 0.75rem 1.5rem; border-radius: 25px; cursor: pointer; font-weight: 600; transition: all 0.3s ease;">
                                <i class="fas fa-save"></i> Update Profile
                            </button>
                            <button type="button" onclick="window.location.href='forgot_password.php'" class="btn-reset-password" style="background: transparent; color: #FF9800; border: 2px solid #FF9800; padding: 0.75rem 1.5rem; border-radius: 25px; cursor: pointer; font-weight: 600; transition: all 0.3s ease;">
                                <i class="fas fa-key"></i> Reset Password
                            </button>
                            <button type="button" onclick="window.location.href='dashboard.php'" class="btn-back" style="background: transparent; color: #00c2ce; border: 2px solid #00c2ce; padding: 0.75rem 1.5rem; border-radius: 25px; cursor: pointer; font-weight: 600; transition: all 0.3s ease;">
                                <i class="fas fa-arrow-left"></i> Back
                            </button>
                        </div>
                    </form>
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
                        <li><a href="link.php">Link</a></li>
                        <li><a href="history.php">History</a></li>
                    </ul>
                </div>

                <div class="footer-section">
                    <h4 class="footer-title">Support</h4>
                    <ul class="footer-links">
                        <li><a href="#support">Help Center</a></li>
                        <li><a href="#contact">Contact Us</a></li>
                    </ul>
                </div>

                <div class="footer-section">
                    <h4 class="footer-title">Company</h4>
                    <ul class="footer-links">
                        <li><a href="#about">About Us</a></li>
                        <li><a href="#careers">Our Team</a></li>
                    </ul>
                </div>
            </div>

            <div class="footer-bottom">
                <p>&copy; 2025 Cephra. All rights reserved. | <a href="#privacy">Privacy Policy</a> | <a href="#terms">Terms of Service</a></p>
            </div>
        </div>
    </footer>

    <script>
        // Function to open Monitor Web in new tab
        window.openMonitorWeb = function() {
            const monitorUrl = '../Monitor/';
            window.open(monitorUrl, '_blank', 'noopener,noreferrer');
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

        document.querySelectorAll('.profile-container, .alert').forEach(card => {
            observer.observe(card);
        });

        // Initialize dashboard features
        $(document).ready(function() {
            initMobileMenu(); // Initialize mobile menu functionality

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
            document.querySelectorAll('.profile-container, .alert').forEach(el => {
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
