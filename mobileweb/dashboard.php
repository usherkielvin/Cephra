<?php
session_start();
if (!isset($_SESSION['username'])) {
    header("Location: index.php");
    exit();
}
require_once '../config/database.php';
$db = new Database();
$conn = $db->getConnection();
if ($conn) {
    $username = $_SESSION['username'];
    $stmt = $conn->prepare("SELECT firstname FROM users WHERE username = :username");
    $stmt->bindParam(':username', $username);
    $stmt->execute();
    $user = $stmt->fetch(PDO::FETCH_ASSOC);
$firstname = $user ? $user['firstname'] : 'User';

echo "<!-- DEBUG: Session username: " . htmlspecialchars($_SESSION['username']) . " -->";
echo "<!-- DEBUG: Fetched firstname: " . htmlspecialchars($firstname) . " -->";

} else {
    $firstname = 'User';
}
?>
<!DOCTYPE HTML>
<!--
	ZeroFour by HTML5 UP
	html5up.net | @ajlkn
	Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)
-->
<html>
	<head>
		<title>Cephra</title>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
		<link rel="icon" type="image/png" href="images/logo.png" />
		<link rel="stylesheet" href="assets/css/main.css" />
	</head>
	<body class="homepage is-preload">
		<div id="page-wrapper">

			<!-- Header -->
				<div id="header-wrapper">
					<!-- Header -->
						<header id="header">
							<div class="inner">

								<!-- Logo -->
									<h1>
									
									<a href="dashboard.php" id="logo">Cephra</a>
									</h1>

								<!-- Nav -->
									<nav id="nav">
										<ul>
											<li class="current_page_item"><a href="dashboard.php">Home</a></li>
											<li><a href="link.php">Link</a></li>
											<li><a href="right-sidebar.html">History</a></li>
											<li><a href="no-sidebar.html">Profile</a></li>
										</ul>
									</nav>

							</div>
						</header>

					<div class="container">
                        <h2 class="greeting">Hello, <?php echo htmlspecialchars($firstname ?? 'User'); ?></h2>
						<!-- Banner -->
							<div id="banner">
								<h2>
								<a href="#" class="image featured"><img src="images/ads.png" alt="" /></a>
								</h2>
								<p>Every tap starts your power. With Cephra, charging isn’t just energy it’s freedom. </p>
								<a href="ChargingPage.php" id="start-charging" class="button large">Start Charging</a>
							</div>
					</div>
				</div>

			<!-- Main Wrapper -->
				<div id="main-wrapper">
					<div class="wrapper style1">
						<div class="inner">

							

							<!-- Feature 2 -->
								<section class="container box feature2">
									<div class="row">
										<div class="col-6 col-12-medium">
											<section>
												<header class="major">
													<h2>Rewards</h2>
													<p>Earn points every charge with Cephra Rewards and unlock perks made for you.</p>
												</header>
												<p><a href="#" class="image featured"><img src="images/ads.png" alt="" /></a></p>
												<footer>
													<a href="#" class="button medium alt icon solid fa-info-circle">Rewards</a>
												</footer>
											</section>
										</div>
										<div class="col-6 col-12-medium">
											<section>
												<header class="major">
													<h2>Wallet</h2>
													<p>Securely store, manage, and use your balance with ease through Cephra Wallet.</p>
												</header>
												<p><a href="#" class="image featured"><img src="images/ads.png" alt="" /></a></p>
												<footer>
													<a href="#" class="button medium alt icon solid fa-info-circle">Wait, what?</a>
												</footer>
											</section>
										</div>
									</div>
								</section>

							</div>
					</div>
					
			<!-- Footer Wrapper -->
				<div id="footer-wrapper">
					<footer id="footer" class="container">
						<div class="row">


								<!-- About -->
									<section>
										<h2><strong>Cephra</strong> by CephraCorp</h2>
										<p>Hi! This is <strong>Cephra</strong>, a free, fully responsive site for charging your
										electric vehicle. its built on HTML5 and CSS3 </a>.
										</p>
									</section>

								<!-- Contact -->
									<section>
										<h2>Get in touch</h2>
										<div>
											<div class="row">
												<div class="col-6 col-12-small">
													<dl class="contact">
														<dt>Twitter</dt>
														<dd><a href="https://www.x.com/@Cephra">@Cephra</a></dd>
														<dt>Facebook</dt>
														<dd><a href="https://www.facebook.com/CephraCorp">facebook.com/CephraCorp</a></dd>
														<dt>WWW</dt>
														<dd><a href="https://www.cephra.com">CephraCorp.tld</a></dd>
														<dt>Email</dt>
														<dd><a href="mailto:admin@cephra.com">admin@Cephra.gmail.com</a></dd>
													</dl>
												</div>
												<div class="col-6 col-12-small">
													<dl class="contact">
														<dt>Address</dt>
														<dd>
															Coral Way St.<br />
															Moa Complex, Pasay City<br />
															PH
														</dd>
														<dt>Phone</dt>
														<dd>(000) 000-0000</dd>
													</dl>
												</div>
											</div>
										</div>
									</section>

							</div>
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