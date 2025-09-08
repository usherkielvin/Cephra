<?php
session_start();
if (!isset($_SESSION['username'])) {
	header("Location: index.php");
	exit();
}

require_once '../config/database.php';

$db = new Database();
$conn = $db->getConnection();

$username = $_SESSION['username'];
$user = null;
$memberSinceFormatted = '';
$batteryLevel = null;
if ($conn) {
	$stmt = $conn->prepare("SELECT username, firstname, lastname, email, created_at FROM users WHERE username = :username LIMIT 1");
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
		<link rel="stylesheet" href="assets/css/main.css" />
		<style>
			.profile-card { background:#fff; border-radius:8px; padding:16px; }
			.profile-row { display:flex; justify-content:space-between; padding:8px 0; border-bottom:1px solid #eee; }
			.profile-row:last-child { border-bottom:0; }
			.profile-label { color:#666; font-weight:700; }
			.panel-nav { display:flex; gap:12px; justify-content:center; margin-top:16px; }
			.panel-nav .button { min-width: 110px; text-align:center; }

		</style>
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
							<div class="profile-card">
								<div class="profile-row"><span class="profile-label">Username</span><span><?php echo htmlspecialchars($user['username'] ?? $username); ?></span></div>
								<div class="profile-row"><span class="profile-label">Name</span><span><?php echo htmlspecialchars(($user['firstname'] ?? '') . ' ' . ($user['lastname'] ?? '')); ?></span></div>
								<div class="profile-row"><span class="profile-label">Email</span><span><?php echo htmlspecialchars($user['email'] ?? ''); ?></span></div>
								<div class="profile-row"><span class="profile-label">Member Since</span><span><?php echo htmlspecialchars($memberSinceFormatted); ?></span></div>
								<div class="profile-row"><span class="profile-label">Battery</span><span><?php echo is_null($batteryLevel) ? '—' : ($batteryLevel . '%'); ?></span></div>
							</div>
							<div class="panel-nav">
								<a class="button alt" href="history.php">Prev: History</a>
								<a class="button" href="link.php">Next: Link</a>
							</div>
							<div style="margin-top:16px; text-align:center;">
								<button type="button" class="button alt" style="background:#464a52;" onclick="window.location.href='profile_logout.php'">Logout</button>
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
	</body>
</html>


