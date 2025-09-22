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
$batteryLevel = null;
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

	// fetch current battery level
	$stmt = $conn->prepare("SELECT battery_level FROM battery_levels WHERE username = :username LIMIT 1");
	$stmt->bindParam(':username', $username);
	$stmt->execute();
	$bl = $stmt->fetch(PDO::FETCH_ASSOC) ?: null;
	$batteryLevel = isset($bl['battery_level']) ? (int)$bl['battery_level'] : null;
}
?>
<!DOCTYPE HTML>
<html>
	<head>
		<title>Profile - Cephra</title>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
		<link rel="icon" type="image/png" href="images/logo.png?v=2" />
		<link rel="apple-touch-icon" href="images/logo.png?v=2" />
		<link rel="manifest" href="manifest.webmanifest" />
		<meta name="theme-color" content="#062635" />
		<link rel="stylesheet" href="css/main.css" />
		<link rel="stylesheet" href="css/pages/profile.css" />
	</head>
	<body class="homepage is-preload">
		<div id="page-wrapper">
			<div id="header-wrapper">
				<header id="header">
					<div class="inner">
						<h1><a href="dashboard.php" id="logo" style="display:inline-flex;align-items:center;gap:8px;"><img src="images/logo.png" alt="Cephra" style="width:28px;height:28px;border-radius:6px;object-fit:cover;vertical-align:middle;" /><span>Cephra</span></a></h1>
						<nav id="nav">
							<ul>
								<li><a href="dashboard.php">Home</a></li>
								<li><a href="link.php">Link</a></li>
								<li><a href="history.php">History</a></li>
								<li class="current_page_item"><a href="profile.php">Profile</a></li>
							</ul>
						</nav>
					</div>
				</header>
			</div>

			<div id="main-wrapper">
				<div class="wrapper style1">
					<div class="inner">
						<section class="container box feature2">
							<header class="major">
								<h2>Your Profile</h2>
							</header>

							<!-- Success/Error Messages -->
							<?php if (!empty($successMessage)): ?>
								<div class="alert alert-success">
									<?php echo htmlspecialchars($successMessage); ?>
								</div>
							<?php endif; ?>
							<?php if (!empty($errorMessage)): ?>
								<div class="alert alert-error">
									<?php echo htmlspecialchars($errorMessage); ?>
								</div>
							<?php endif; ?>

							<div class="profile-container">
								<!-- Profile Photo Section -->
								<div class="profile-photo-section">
									<div class="photo-container">
										<div class="profile-photo">
											<?php if ($profilePicture && file_exists('uploads/profile_pictures/' . $profilePicture)): ?>
												<img src="uploads/profile_pictures/<?php echo htmlspecialchars($profilePicture); ?>" alt="Profile Photo" id="profilePhoto">
											<?php else: ?>
												<div class="photo-placeholder">
													<i class="fas fa-user"></i>
												</div>
											<?php endif; ?>
										</div>
										<form method="POST" enctype="multipart/form-data" class="photo-upload-form">
											<input type="file" id="profilePhotoInput" name="profile_photo" accept="image/*" style="display: none;">
											<button type="button" class="btn-change-photo" onclick="document.getElementById('profilePhotoInput').click()">
												<i class="fas fa-camera"></i> Change Photo
											</button>
											<button type="submit" name="upload_photo" class="btn-upload-photo" style="display: none;" id="uploadBtn">
												Upload
											</button>
										</form>
									</div>
								</div>

								<!-- Profile Information Section -->
								<div class="profile-info-section">
									<form method="POST" class="profile-form">
										<div class="form-group">
											<label for="username">Username</label>
											<input type="text" id="username" name="username" value="<?php echo htmlspecialchars($user['username'] ?? $username); ?>" required>
										</div>

										<div class="form-row">
											<div class="form-group">
												<label for="firstname">First Name</label>
												<input type="text" id="firstname" name="firstname" value="<?php echo htmlspecialchars($user['firstname'] ?? ''); ?>" required>
											</div>
											<div class="form-group">
												<label for="lastname">Last Name</label>
												<input type="text" id="lastname" name="lastname" value="<?php echo htmlspecialchars($user['lastname'] ?? ''); ?>" required>
											</div>
										</div>

										<div class="form-group">
											<label for="email">Email</label>
											<input type="email" id="email" name="email" value="<?php echo htmlspecialchars($user['email'] ?? ''); ?>" required>
										</div>

										<div class="form-group">
											<label>Member Since</label>
											<div class="info-display"><?php echo htmlspecialchars($memberSinceFormatted); ?></div>
										</div>

										<div class="form-group">
											<label>Battery Level</label>
											<div class="info-display battery-display">
												<?php echo is_null($batteryLevel) ? '—' : ($batteryLevel . '%'); ?>
												<?php if (!is_null($batteryLevel)): ?>
													<div class="battery-bar">
														<div class="battery-fill" style="width: <?php echo $batteryLevel; ?>%"></div>
													</div>
												<?php endif; ?>
											</div>
										</div>

										<div class="form-actions">
											<button type="submit" name="update_profile" class="btn-primary">
												<i class="fas fa-save"></i> Update Profile
											</button>
											<button type="button" class="btn-logout" onclick="window.location.href='profile_logout.php'">
												<i class="fas fa-sign-out-alt"></i> Logout
											</button>
										</div>
									</form>
								</div>
							</div>

						</section>
					</div>
				</div>
			</div>

			<div id="footer-wrapper">
				<footer id="footer" class="container">
					<div class="row">
						<div class="col-12">
							<div id="copyright">
								<ul class="menu">
									<li>&copy; Cephra. All rights reserved</li><li>Design: <a href="http://html5up.net">Cephra Designer</a></li>
								</ul>
							</div>
						</div>
					</div>
				</footer>
			</div>
		</div>

		<!-- Scripts -->
		<script src="assets/js/jquery.min.js"></script>
		<script src="assets/js/jquery.dropotron.min.js"></script>
		<script src="assets/js/browser.min.js"></script>
		<script src="assets/js/breakpoints.min.js"></script>
		<script src="assets/js/util.js"></script>
		<script src="assets/js/main.js"></script>

		<script>
			// Profile photo preview functionality
			document.getElementById('profilePhotoInput').addEventListener('change', function(e) {
				const file = e.target.files[0];
				if (file) {
					const reader = new FileReader();
					reader.onload = function(e) {
						document.getElementById('profilePhoto').src = e.target.result;
					};
					reader.readAsDataURL(file);
					document.getElementById('uploadBtn').style.display = 'inline-block';
				}
			});
		</script>
	</body>
</html>
