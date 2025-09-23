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
		<link rel="stylesheet" href="css/vantage-style.css" />
		<link rel="stylesheet" href="assets/css/fontawesome-all.min.css" />
		<link rel="stylesheet" href="assets/css/pages/history.css" />
	</head>
	<body class="homepage is-preload">
		<div id="page-wrapper">
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
								<li><a href="dashboard.php" class="nav-link">Dashboard</a></li>
								<li><a href="link.php" class="nav-link">Link</a></li>
								<li class="current_page_item"><a href="history.php" class="nav-link">History</a></li>
								<li><a href="profile.php" class="nav-link">Profile</a></li>
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

			<div id="main-wrapper">
				<div class="wrapper style1">
					<div class="inner">
						<section class="container box feature2">
							<header class="major">
								<h2>Transaction History</h2>
							</header>

							<!-- Search and Filter Section -->
							<div class="section-header">
								<h3>Transaction History</h3>
								<p>Search and filter your transaction records</p>
							</div>
							<div class="search-filter-section">
								<div class="search-bar">
									<input type="text" id="searchInput" class="search-input" placeholder="Search transactions...">
									<i class="fas fa-search search-icon"></i>
								</div>
								<div class="filter-controls">
									<select id="statusFilter" class="filter-select">
										<option value="all">All Status</option>
										<option value="completed">Completed</option>
										<option value="pending">Pending</option>
										<option value="failed">Failed</option>
									</select>
									<select id="methodFilter" class="filter-select">
										<option value="all">All Methods</option>
										<option value="cash">Cash</option>
										<option value="gcash">GCash</option>
									</select>
									<button id="clearFilters" class="filter-button">Clear</button>
								</div>
							</div>

							<!-- History Table -->
							<div class="history-table-container">
								<?php if (empty($payments)): ?>
									<div class="no-data">
										<h3>No Transactions Yet</h3>
										<p>You haven't made any payments. Start by linking your account.</p>
									</div>
								<?php else: ?>
									<table class="history-table">
										<thead>
											<tr>
												<th>Transaction ID</th>
												<th>Status</th>
												<th>Amount</th>
												<th>Method</th>
												<th>Reference</th>
												<th>Date</th>
												<th>Action</th>
											</tr>
										</thead>
										<tbody>
											<?php foreach ($payments as $p): ?>
												<?php
													$when = $p['processed_at'] ?? '';
													$whenFmt = $when;
													try {
														$dt = new DateTime($when);
														$whenFmt = $dt->format('M d, Y h:i A');
													} catch (Exception $e) {}
												?>
												<tr data-status="<?php echo htmlspecialchars(strtolower($p['transaction_status'] ?? '')); ?>" data-method="<?php echo htmlspecialchars(strtolower($p['payment_method'] ?? '')); ?>" data-date="<?php echo htmlspecialchars($p['processed_at'] ?? ''); ?>">
													<td><?php echo htmlspecialchars($p['ticket_id']); ?></td>
													<td>
														<span class="status-badge status-<?php echo htmlspecialchars(strtolower($p['transaction_status'] ?? '')); ?>">
															<?php echo htmlspecialchars($p['transaction_status'] ?? ''); ?>
														</span>
													</td>
													<td>₱<?php echo number_format((float)($p['amount'] ?? 0), 2); ?></td>
													<td><?php echo htmlspecialchars($p['payment_method'] ?? ''); ?></td>
													<td><?php echo htmlspecialchars($p['reference_number'] ?? ''); ?></td>
													<td><?php echo htmlspecialchars($whenFmt); ?></td>
													<td>
														<button class="action-button" onclick="showDetails('<?php echo htmlspecialchars($p['ticket_id']); ?>')">
															<i class="fas fa-eye"></i>
														</button>
													</td>
												</tr>
											<?php endforeach; ?>
										</tbody>
									</table>
								<?php endif; ?>
							</div>

							<!-- Transaction Details Modal -->
							<div id="transactionModal" class="modal">
								<div class="modal-content">
									<div class="modal-header">
										<h3>Transaction Details</h3>
										<button class="modal-close" onclick="closeModal()">
											<i class="fas fa-times"></i>
										</button>
									</div>
									<div class="modal-body" id="modalBody">
										<!-- Transaction details will be loaded here -->
									</div>
								</div>
							</div>

						</section>
					</div>
				</div>
			</div>

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
								<li><a href="#team">Our Team</a></li>
							</ul>
						</div>
					</div>

					<div class="footer-bottom">
						<p>&copy; 2025 Cephra. All rights reserved. | <a href="#privacy">Privacy Policy</a> | <a href="#terms">Terms of Service</a></p>
					</div>
				</div>
			</footer>
		</div>
		<!-- Scripts -->
		<script src="assets/js/jquery.min.js"></script>
		<script src="assets/js/jquery.dropotron.min.js"></script>
		<script src="assets/js/browser.min.js"></script>
		<script src="assets/js/breakpoints.min.js"></script>
		<script src="assets/js/util.js"></script>
		<script src="assets/js/main.js"></script>
		<script>
			// Mobile menu toggle
			document.getElementById('mobileMenuToggle').addEventListener('click', function() {
				const nav = document.querySelector('.nav');
				nav.classList.toggle('mobile-menu-open');
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

			// Filter functionality
			function applyFilters() {
				const statusFilter = document.getElementById('statusFilter').value;
				const methodFilter = document.getElementById('methodFilter').value;
				const rows = document.querySelectorAll('.history-table tbody tr');

				rows.forEach(row => {
					let showRow = true;

					// Status filter
					if (statusFilter !== 'all') {
						const rowStatus = row.getAttribute('data-status');
						if (rowStatus !== statusFilter) {
							showRow = false;
						}
					}

					// Method filter
					if (methodFilter !== 'all') {
						const rowMethod = row.getAttribute('data-method');
						if (rowMethod !== methodFilter) {
							showRow = false;
						}
					}

					// Show/hide row
					row.style.display = showRow ? '' : 'none';
				});
			}

			// Clear filters
			function clearFilters() {
				document.getElementById('statusFilter').value = 'all';
				document.getElementById('methodFilter').value = 'all';
				document.getElementById('dateFilter').value = '';
				applyFilters();
			}

			// Search functionality
			function applySearch() {
				const searchTerm = document.getElementById('searchInput').value.toLowerCase();
				const rows = document.querySelectorAll('.history-table tbody tr');

				rows.forEach(row => {
					const text = row.textContent.toLowerCase();
					const showRow = text.includes(searchTerm);
					row.style.display = showRow ? '' : 'none';
				});
			}

			// Show details function with modal
			function showDetails(ticketId) {
				// Find the transaction data
				const rows = document.querySelectorAll('.history-table tbody tr');
				let transactionData = null;

				rows.forEach(row => {
					if (row.cells[0].textContent === ticketId) {
						transactionData = {
							id: row.cells[0].textContent,
							status: row.cells[1].textContent,
							amount: row.cells[2].textContent,
							method: row.cells[3].textContent,
							reference: row.cells[4].textContent,
							date: row.cells[5].textContent
						};
					}
				});

				if (transactionData) {
					const modalBody = document.getElementById('modalBody');
					modalBody.innerHTML = `
						<div class="transaction-details">
							<div class="detail-row">
								<span class="detail-label">Transaction ID:</span>
								<span class="detail-value">${transactionData.id}</span>
							</div>
							<div class="detail-row">
								<span class="detail-label">Status:</span>
								<span class="detail-value">${transactionData.status}</span>
							</div>
							<div class="detail-row">
								<span class="detail-label">Amount:</span>
								<span class="detail-value">${transactionData.amount}</span>
							</div>
							<div class="detail-row">
								<span class="detail-label">Payment Method:</span>
								<span class="detail-value">${transactionData.method}</span>
							</div>
							<div class="detail-row">
								<span class="detail-label">Reference Number:</span>
								<span class="detail-value">${transactionData.reference}</span>
							</div>
							<div class="detail-row">
								<span class="detail-label">Date & Time:</span>
								<span class="detail-value">${transactionData.date}</span>
							</div>
						</div>
					`;

					// Show modal
					const modal = document.getElementById('transactionModal');
					modal.classList.add('show');
					document.body.classList.add('modal-open');
				}
			}

			// Close modal function
			function closeModal() {
				const modal = document.getElementById('transactionModal');
				modal.classList.remove('show');
				document.body.classList.remove('modal-open');
			}

			// Close modal when clicking outside
			document.getElementById('transactionModal').addEventListener('click', function(e) {
				if (e.target === this) {
					closeModal();
				}
			});

			// Event listeners for filters and search
			document.getElementById('statusFilter').addEventListener('change', applyFilters);
			document.getElementById('methodFilter').addEventListener('change', applyFilters);
			document.getElementById('clearFilters').addEventListener('click', clearFilters);
			document.getElementById('searchInput').addEventListener('input', applySearch);

			// Initialize filters on page load
			document.addEventListener('DOMContentLoaded', function() {
				applyFilters();
			});
		</script>
	</body>
</html>


