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
								<li><a href="rewards.php" class="nav-link">Rewards</a></li>
								<li><a href="wallet.php" class="nav-link">Wallet</a></li>
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

        <!-- Mobile Menu -->
        <div class="mobile-menu" id="mobileMenu">
            <div class="mobile-menu-content">
                <ul class="mobile-nav-list">
                    <li><a href="dashboard.php" class="mobile-nav-link">Dashboard</a></li>
                    <li><a href="link.php" class="mobile-nav-link">Link</a></li>
                    <li><a href="history.php" class="mobile-nav-link">History</a></li>
                    <li><a href="profile.php" class="mobile-nav-link">Profile</a></li>
                    <li><a href="rewards.php" class="mobile-nav-link">Rewards</a></li>
                    <li><a href="wallet.php" class="mobile-nav-link">Wallet</a></li>
                </ul>
                <div class="mobile-header-actions">
                    <a href="dashboard.php" class="mobile-auth-link">Back</a>
                </div>
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
                            <?php if ($profilePicture): ?>
                                <?php if (strpos($profilePicture, 'data:image') === 0): ?>
                                    <!-- Data URI format from Java app -->
                                    <img src="<?php echo htmlspecialchars($profilePicture); ?>" alt="Profile Photo" id="profilePhoto" style="width: 100%; height: 100%; object-fit: cover; border-radius: 50%;">
                                <?php elseif (strpos($profilePicture, 'iVBORw0KGgo') === 0): ?>
                                    <!-- Raw Base64 format from old Java app -->
                                    <img src="data:image/png;base64,<?php echo htmlspecialchars($profilePicture); ?>" alt="Profile Photo" id="profilePhoto" style="width: 100%; height: 100%; object-fit: cover; border-radius: 50%;">
                                <?php elseif (file_exists('uploads/profile_pictures/' . $profilePicture)): ?>
                                    <!-- File path from web upload -->
                                    <img src="uploads/profile_pictures/<?php echo htmlspecialchars($profilePicture); ?>" alt="Profile Photo" id="profilePhoto" style="width: 100%; height: 100%; object-fit: cover; border-radius: 50%;">
                                <?php else: ?>
                                    <!-- Fallback placeholder -->
                                    <div class="photo-placeholder" style="width: 100%; height: 100%; background: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%); display: flex; align-items: center; justify-content: center; color: white; font-size: 3rem; border-radius: 50%;">
                                        <i class="fas fa-user"></i>
                                    </div>
                                <?php endif; ?>
                            <?php else: ?>
                                <!-- No profile picture -->
                                <div class="photo-placeholder" style="width: 100%; height: 100%; background: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%); display: flex; align-items: center; justify-content: center; color: white; font-size: 3rem; border-radius: 50%;">
                                    <i class="fas fa-user"></i>
                                </div>
                            <?php endif; ?>
                            <!-- Online Status Indicator (Bottom Left) -->
                            <div style="position: absolute; bottom: 10px; left: 10px; width: 20px; height: 20px; background: #4CAF50; border: 3px solid white; border-radius: 50%; z-index: 10;"></div>
                            <!-- Camera Icon for Photo Upload (Bottom Right, Overlapping Photo) -->
                            <button type="button" onclick="openProfileUploadModal()" style="position: absolute; bottom: 5px; right: 5px; width: 40px; height: 40px; background: rgba(0, 194, 206, 0.9); color: white; border: 3px solid white; border-radius: 50%; cursor: pointer; font-size: 1rem; display: flex; align-items: center; justify-content: center; transition: all 0.3s ease; z-index: 20;">
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
                            <button type="button" onclick="window.location.href='index.php'" class="btn-back" style="background: transparent; color: #00c2ce; border: 2px solid #00c2ce; padding: 0.75rem 1.5rem; border-radius: 25px; cursor: pointer; font-weight: 600; transition: all 0.3s ease;">
                                <i class="fas fa-arrow-left"></i> Logout
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>

    <!-- Profile Photo Upload Modal -->
    <div id="profileUploadModal" class="modal">
        <div class="modal-overlay" onclick="closeProfileUploadModal()"></div>
        <div class="modal-content" style="max-width: 500px; width: 90%; max-height: 90vh; overflow-y: auto;">
            <div class="modal-header" style="text-align: center; padding: 1.5rem; border-bottom: 1px solid rgba(26, 32, 44, 0.1);">
                <h3 style="margin: 0; font-size: 1.5rem; font-weight: 700; color: #1a202c;">Update Profile Photo</h3>
                <button class="modal-close" onclick="closeProfileUploadModal()" style="position: absolute; top: 1rem; right: 1rem; background: none; border: none; font-size: 1.5rem; cursor: pointer; color: #6c757d;">&times;</button>
            </div>
            <form method="POST" enctype="multipart/form-data" id="profileUploadForm">
                <div class="modal-body" style="padding: 2rem; display: flex; flex-direction: column; align-items: center;">
                    <div class="crop-container" style="margin-bottom: 2rem; text-align: center;">
                        <div style="position: relative; display: inline-block; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 20px rgba(0, 194, 206, 0.15);">
                            <canvas id="cropCanvas" width="250" height="250" style="display: block; background: #f8fafc;"></canvas>
                            <div id="cropOverlay" style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; pointer-events: none; background: rgba(0, 194, 206, 0.1); display: none;"></div>
                        </div>
                        <p style="text-align: center; margin-top: 1rem; color: #1a202c; font-size: 0.9rem; font-weight: 500;">Drag to select crop area</p>
                        <div id="cropInstructions" style="text-align: center; margin-top: 0.5rem; color: rgba(26, 32, 44, 0.6); font-size: 0.8rem; display: none;">Click "Apply Crop" to confirm your selection</div>
                    </div>
                    <div class="form-group" style="width: 100%; margin-bottom: 2rem;">
                        <input type="file" id="profilePhotoInput" name="profile_photo" accept="image/*" required style="width: 100%; padding: 0.75rem; border: 2px dashed rgba(0, 194, 206, 0.3); border-radius: 8px; font-size: 1rem; text-align: center; cursor: pointer; transition: all 0.3s ease;" onchange="loadImageForCrop(event)">
                        <small style="color: rgba(26, 32, 44, 0.6); display: block; margin-top: 0.25rem; text-align: center;">Supported: JPEG, PNG, GIF (Max 5MB)</small>
                    </div>
                    <div class="crop-controls" style="display: flex; gap: 1rem; justify-content: center; margin-bottom: 2rem;">
                        <button type="button" id="cropBtn" class="crop-btn" onclick="cropImage()" style="display: none; background: #00c2ce; color: white; border: none; padding: 0.75rem 1.5rem; border-radius: 25px; cursor: pointer; font-weight: 600; transition: all 0.3s ease; box-shadow: 0 4px 15px rgba(0, 194, 206, 0.3);">
                            <i class="fas fa-crop"></i> Apply Crop
                        </button>
                        <button type="button" class="crop-btn reset-btn" onclick="resetCrop()" style="display: none; background: transparent; color: #ff6b6b; border: 2px solid #ff6b6b; padding: 0.75rem 1.5rem; border-radius: 25px; cursor: pointer; font-weight: 600; transition: all 0.3s ease;">
                            <i class="fas fa-undo"></i> Reset
                        </button>
                    </div>
                    <div class="form-actions" style="display: flex; gap: 1rem; justify-content: center; width: 100%;">
                        <button type="submit" name="upload_photo" class="btn-primary" style="background: #00c2ce; color: white; border: none; padding: 0.75rem 2rem; border-radius: 25px; cursor: pointer; font-weight: 600; transition: all 0.3s ease; flex: 1; max-width: 200px; box-shadow: 0 4px 15px rgba(0, 194, 206, 0.3);">
                            <i class="fas fa-upload"></i> Upload Photo
                        </button>
                        <button type="button" onclick="closeProfileUploadModal()" class="btn-cancel" style="background: transparent; color: #6c757d; border: 2px solid #6c757d; padding: 0.75rem 2rem; border-radius: 25px; cursor: pointer; font-weight: 600; transition: all 0.3s ease; flex: 1; max-width: 200px;">
                            Cancel
                        </button>
                    </div>
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

    <script>
        // Function to open Monitor Web in new tab
        window.openMonitorWeb = function() {
            const monitorUrl = '../Monitor/';
            window.open(monitorUrl, '_blank', 'noopener,noreferrer');
        };

        // Mobile Menu Toggle Functionality
        function initMobileMenu() {
            const mobileMenuToggle = document.getElementById('mobileMenuToggle');
            if (!mobileMenuToggle) return; // Exit if no mobile menu toggle

            const mobileMenu = document.getElementById('mobileMenu');
            if (!mobileMenu) return; // Exit if no mobile menu

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

        // Profile Upload Modal Functions
        function openProfileUploadModal() {
            console.log('Camera button clicked - opening modal');
            const modal = document.getElementById('profileUploadModal');
            if (!modal) {
                console.error('Modal element not found');
                return;
            }
            modal.style.display = 'flex';
            modal.classList.add('show');
            document.body.style.overflow = 'hidden';
            // Reset form and crop state
            const form = document.getElementById('profileUploadForm');
            if (form) {
                form.reset();
            }
            resetCrop();
            document.getElementById('cropBtn').style.display = 'none';
            document.querySelector('.reset-btn').style.display = 'none';
        }

        function closeProfileUploadModal() {
            document.getElementById('profileUploadModal').style.display = 'none';
            document.getElementById('profileUploadModal').classList.remove('show');
            document.body.style.overflow = '';
            // Reset crop state
            resetCrop();
            document.getElementById('cropBtn').style.display = 'none';
        }

        let originalImage = null;
        let cropCanvas = null;
        let cropCtx = null;
        let isDragging = false;
        let startX, startY, endX, endY;
        let cropApplied = false;
        let animationFrameId = null;

        function loadImageForCrop(event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    const img = new Image();
                    img.onload = function() {
                        originalImage = img;
                        cropCanvas = document.getElementById('cropCanvas');
                        cropCtx = cropCanvas.getContext('2d');
                        cropApplied = false;
                        resetCrop();
                        drawImage();
                        document.getElementById('cropBtn').style.display = 'none'; // Hide initially
                        document.querySelector('.reset-btn').style.display = 'none';
                        document.getElementById('cropInstructions').style.display = 'block';
                        document.getElementById('cropOverlay').style.display = 'block';
                    };
                    img.src = e.target.result;
                };
                reader.readAsDataURL(file);
            }
        }

        function drawImage() {
            if (!cropCanvas || !cropCtx || !originalImage) return;

            const canvasSize = 250;
            const imgSize = Math.min(originalImage.width, originalImage.height);
            const scale = canvasSize / imgSize;
            const scaledWidth = originalImage.width * scale;
            const scaledHeight = originalImage.height * scale;
            const x = (canvasSize - scaledWidth) / 2;
            const y = (canvasSize - scaledHeight) / 2;

            cropCtx.clearRect(0, 0, canvasSize, canvasSize);
            cropCtx.drawImage(originalImage, x, y, scaledWidth, scaledHeight);

            // Draw crop rectangle with enhanced styling
            if (startX !== undefined && endX !== undefined) {
                const width = Math.abs(endX - startX);
                const height = Math.abs(endY - startY);
                const rectX = Math.min(startX, endX);
                const rectY = Math.min(startY, endY);

                // Draw semi-transparent overlay outside crop area
                cropCtx.fillStyle = 'rgba(0, 0, 0, 0.4)';
                cropCtx.fillRect(0, 0, canvasSize, canvasSize);

                // Clear the crop area
                cropCtx.clearRect(rectX, rectY, width, height);
                cropCtx.drawImage(originalImage, x, y, scaledWidth, scaledHeight);

                // Draw crop border
                cropCtx.strokeStyle = '#00c2ce';
                cropCtx.lineWidth = 3;
                cropCtx.strokeRect(rectX, rectY, width, height);

                // Draw corner handles
                const handleSize = 8;
                cropCtx.fillStyle = '#00c2ce';
                // Top-left
                cropCtx.fillRect(rectX - handleSize/2, rectY - handleSize/2, handleSize, handleSize);
                // Top-right
                cropCtx.fillRect(rectX + width - handleSize/2, rectY - handleSize/2, handleSize, handleSize);
                // Bottom-left
                cropCtx.fillRect(rectX - handleSize/2, rectY + height - handleSize/2, handleSize, handleSize);
                // Bottom-right
                cropCtx.fillRect(rectX + width - handleSize/2, rectY + height - handleSize/2, handleSize, handleSize);

                // Draw grid lines inside crop area
                cropCtx.strokeStyle = 'rgba(255, 255, 255, 0.5)';
                cropCtx.lineWidth = 1;
                const gridSize = Math.min(width, height) / 3;
                for (let i = 1; i < 3; i++) {
                    // Vertical lines
                    cropCtx.beginPath();
                    cropCtx.moveTo(rectX + (width * i / 3), rectY);
                    cropCtx.lineTo(rectX + (width * i / 3), rectY + height);
                    cropCtx.stroke();
                    // Horizontal lines
                    cropCtx.beginPath();
                    cropCtx.moveTo(rectX, rectY + (height * i / 3));
                    cropCtx.lineTo(rectX + width, rectY + (height * i / 3));
                    cropCtx.stroke();
                }
            }
        }

        function cropImage() {
            if (!cropCanvas || !cropCtx || !originalImage) return;

            const canvasSize = 250;
            const imgSize = Math.min(originalImage.width, originalImage.height);
            const scale = canvasSize / imgSize;
            const scaledWidth = originalImage.width * scale;
            const scaledHeight = originalImage.height * scale;
            const x = (canvasSize - scaledWidth) / 2;
            const y = (canvasSize - scaledHeight) / 2;

            if (startX === undefined || endX === undefined) {
                // No crop selection, use center square
                const size = Math.min(scaledWidth, scaledHeight);
                startX = x + (scaledWidth - size) / 2;
                startY = y + (scaledHeight - size) / 2;
                endX = startX + size;
                endY = startY + size;
            }

            const cropWidth = Math.abs(endX - startX);
            const cropHeight = Math.abs(endY - startY);
            const cropSize = Math.min(cropWidth, cropHeight);
            const cropX = Math.min(startX, endX);
            const cropY = Math.min(startY, endY);

            // Create cropped image
            const croppedCanvas = document.createElement('canvas');
            croppedCanvas.width = cropSize;
            croppedCanvas.height = cropSize;
            const croppedCtx = croppedCanvas.getContext('2d');

            // Calculate source coordinates
            const sourceX = (cropX - x) / scale;
            const sourceY = (cropY - y) / scale;
            const sourceSize = cropSize / scale;

            croppedCtx.drawImage(originalImage, sourceX, sourceY, sourceSize, sourceSize, 0, 0, cropSize, cropSize);

            // Convert to blob and set as form data
            croppedCanvas.toBlob(function(blob) {
                const fileInput = document.getElementById('profilePhotoInput');
                const file = new File([blob], 'cropped_image.png', { type: 'image/png' });
                const dt = new DataTransfer();
                dt.items.add(file);
                fileInput.files = dt.files;

                // Show success feedback
                cropApplied = true;
                document.getElementById('cropBtn').innerHTML = '<i class="fas fa-check"></i> Crop Applied';
                document.getElementById('cropBtn').style.background = '#4CAF50';
                document.getElementById('cropInstructions').textContent = 'Crop applied successfully! Ready to upload.';
                document.getElementById('cropInstructions').style.color = '#4CAF50';

                setTimeout(() => {
                    document.getElementById('cropBtn').innerHTML = '<i class="fas fa-crop"></i> Apply Crop';
                    document.getElementById('cropBtn').style.background = '#00c2ce';
                }, 2000);
            });
        }

        function resetCrop() {
            startX = startY = endX = endY = undefined;
            cropApplied = false;
            drawImage();
            document.getElementById('cropInstructions').textContent = 'Click "Apply Crop" to confirm your selection';
            document.getElementById('cropInstructions').style.color = 'rgba(26, 32, 44, 0.6)';
            document.getElementById('cropBtn').style.display = 'none';
            document.querySelector('.reset-btn').style.display = 'none';

            // Add visual feedback for reset
            const resetBtn = document.querySelector('.reset-btn');
            resetBtn.style.transform = 'scale(1.1)';
            setTimeout(() => {
                resetBtn.style.transform = 'scale(1)';
            }, 150);
        }

        // Enhanced mouse events for cropping
        document.getElementById('cropCanvas').addEventListener('mousedown', function(e) {
            isDragging = true;
            const rect = cropCanvas.getBoundingClientRect();
            startX = e.clientX - rect.left;
            startY = e.clientY - rect.top;
            endX = startX;
            endY = startY;
            drawImage();
        });

        document.getElementById('cropCanvas').addEventListener('mousemove', function(e) {
            if (!isDragging) return;
            const rect = cropCanvas.getBoundingClientRect();
            endX = Math.max(0, Math.min(250, e.clientX - rect.left));
            endY = Math.max(0, Math.min(250, e.clientY - rect.top));

            // Throttle drawing with requestAnimationFrame
            if (animationFrameId) {
                cancelAnimationFrame(animationFrameId);
            }
            animationFrameId = requestAnimationFrame(() => {
                drawImage();
                animationFrameId = null;
            });
        });

        document.getElementById('cropCanvas').addEventListener('mouseup', function() {
            isDragging = false;
            // Ensure minimum crop size
            if (startX !== undefined && endX !== undefined) {
                const width = Math.abs(endX - startX);
                const height = Math.abs(endY - startY);
                if (width < 50 || height < 50) {
                    // Reset if crop area is too small
                    resetCrop();
                    alert('Please select a larger crop area (minimum 50x50 pixels)');
                } else {
                    // Show crop controls only when a valid crop area is selected
                    document.getElementById('cropBtn').style.display = 'inline-block';
                    document.querySelector('.reset-btn').style.display = 'inline-block';
                }
            }
        });

        document.getElementById('cropCanvas').addEventListener('mouseleave', function() {
            isDragging = false;
        });

        // Close modal on escape key
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') {
                closeProfileUploadModal();
            }
        });

        // Initialize profile page features
        document.addEventListener('DOMContentLoaded', function() {
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
            document.addEventListener('click', function(e) {
                // Close modal when clicking outside
                if (e.target.classList.contains('modal-overlay')) {
                    closeProfileUploadModal();
                }
            });

            // Add keyboard support for modal
            document.addEventListener('keydown', function(e) {
                if (e.key === 'Escape') {
                    closeProfileUploadModal();
                }
            });
        });

    </script>
</body>
</html>
