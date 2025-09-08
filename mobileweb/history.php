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

$payments = [];
if ($conn) {
	$stmt = $conn->prepare("SELECT ticket_id, amount, payment_method, reference_number, transaction_status, processed_at FROM payment_transactions WHERE username = :username ORDER BY processed_at DESC LIMIT 20");
	$stmt->bindParam(':username', $username);
	$stmt->execute();
	$payments = $stmt->fetchAll(PDO::FETCH_ASSOC) ?: [];
}
?>
<!DOCTYPE HTML>
<html>
	<head>
		<title>History - Cephra</title>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
		<link rel="icon" type="image/png" href="images/logo.png?v=2" />
		<link rel="apple-touch-icon" href="images/logo.png?v=2" />
		<link rel="manifest" href="manifest.webmanifest" />
		<meta name="theme-color" content="#062635" />
		<link rel="stylesheet" href="css/main.css" />
		<link rel="stylesheet" href="css/pages/history.css" />
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
								<li class="current_page_item"><a href="history.php">History</a></li>
								<li><a href="profile.php">Profile</a></li>
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
								<h2>Payment History</h2>
								<p>Your 20 most recent payments</p>
							</header>
							<div class="history-list">
								<?php if (empty($payments)): ?>
									<div class="history-item">No payments yet.</div>
								<?php else: foreach ($payments as $p): ?>
									<?php
										$when = $p['processed_at'] ?? '';
										$whenFmt = $when;
										try {
											$dt = new DateTime($when);
											$whenFmt = $dt->format('M d, Y h:i A');
										} catch (Exception $e) {}
									?>
									<div class="history-item">
										<strong><?php echo htmlspecialchars($p['ticket_id']); ?></strong>
										<div class="history-meta">
											₱<?php echo number_format((float)($p['amount'] ?? 0), 2); ?> •
											<?php echo htmlspecialchars($p['payment_method'] ?? ''); ?> •
											Ref: <?php echo htmlspecialchars($p['reference_number'] ?? ''); ?> •
											<?php echo htmlspecialchars($p['transaction_status'] ?? ''); ?> •
											<?php echo htmlspecialchars($whenFmt); ?>
										</div>
									</div>
								<?php endforeach; endif; ?>
							</div>
							<div class="panel-nav">
								<a class="button alt" href="link.php">Prev: Link</a>
								<a class="button" href="profile.php">Next: Profile</a>
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


