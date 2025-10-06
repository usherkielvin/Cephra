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
	$stmt = $conn->prepare("SELECT pt.ticket_id, pt.amount, pt.payment_method, pt.reference_number, pt.transaction_status, pt.processed_at, pt.plate_number, ch.service_type FROM payment_transactions pt LEFT JOIN charging_history ch ON pt.ticket_id = ch.ticket_id WHERE pt.username = :username ORDER BY pt.processed_at DESC LIMIT 20");
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
            <?php include __DIR__ . '/partials/header.php'; ?>

			<!-- History Section -->
			<section class="history-section" style="padding: 100px 0; background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);">
				<div class="container">
                    <div class="section-header" style="text-align: center; margin-bottom: 60px;">
                        <h2 class="section-title" data-i18n="historyTitle" style="font-size: 2.5rem; font-weight: 700; margin-bottom: 1rem; background: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text;">Transaction History</h2>
                        <p class="section-description" data-i18n="historySubtitle" style="font-size: 1.2rem; color: rgba(26, 32, 44, 0.8); max-width: 600px; margin: 0 auto;">View and manage your payment transaction records</p>
					</div>

					<!-- Search and Filter Section -->
					<div class="search-filter-section" style="background: white; border-radius: 20px; padding: 2rem; border: 1px solid rgba(26, 32, 44, 0.1); box-shadow: 0 5px 15px rgba(0, 194, 206, 0.1); margin-bottom: 2rem;">
                        <div class="search-bar" style="position: relative; flex: 1; max-width: 400px; margin-bottom: 1rem;">
                            <input type="text" id="searchInput" class="search-input" placeholder="Search transactions..." data-i18n="searchPlaceholder" style="width: 100%; padding: 0.75rem 1rem 0.75rem 2.5rem; border: 1px solid rgba(26, 32, 44, 0.1); border-radius: 8px; font-size: 1rem; transition: border-color 0.3s ease; background: rgba(248, 250, 252, 0.5);">
							<i class="fas fa-search search-icon" style="position: absolute; left: 0.75rem; top: 50%; transform: translateY(-50%); color: rgba(26, 32, 44, 0.5); font-size: 0.9rem;"></i>
						</div>
						<div class="filter-controls" style="display: flex; gap: 1rem; align-items: center; flex-wrap: wrap;">
                            <select id="statusFilter" class="filter-select" style="padding: 0.75rem 1rem; border: 1px solid rgba(26, 32, 44, 0.1); border-radius: 8px; font-size: 1rem; background: rgba(248, 250, 252, 0.5); color: #1a202c; transition: all 0.3s ease; min-width: 120px;">
                                <option value="all" data-i18n="filterAllChargeTypes">All Charge Types</option>
                                <option value="normal" data-i18n="filterNormalCharge">Normal Charge</option>
                                <option value="fast" data-i18n="filterFastCharge">Fast Charge</option>
							</select>
                            <select id="methodFilter" class="filter-select" style="padding: 0.75rem 1rem; border: 1px solid rgba(26, 32, 44, 0.1); border-radius: 8px; font-size: 1rem; background: rgba(248, 250, 252, 0.5); color: #1a202c; transition: all 0.3s ease; min-width: 120px;">
                                <option value="all" data-i18n="filterAllMethods">All Methods</option>
                                <option value="cash" data-i18n="filterCash">Cash</option>
                                <option value="gcash" data-i18n="filterGCash">GCash</option>
							</select>
                            <button id="clearFilters" class="filter-button" data-i18n="clearFilters" style="background: transparent; color: #00c2ce; border: 2px solid #00c2ce; padding: 0.75rem 1.5rem; border-radius: 25px; cursor: pointer; font-weight: 600; transition: all 0.3s ease;">Clear Filters</button>
						</div>
					</div>

					<!-- History Table -->
					<div class="history-table-container" style="background: white; border-radius: 20px; padding: 2rem; border: 1px solid rgba(26, 32, 44, 0.1); box-shadow: 0 5px 15px rgba(0, 194, 206, 0.1); overflow-y: auto; overflow-x: hidden; position: relative; max-height: 600px;">
						<style>
							.history-table-container::-webkit-scrollbar {
								width: 8px;
							}
							.history-table-container::-webkit-scrollbar-track {
								background: rgba(26, 32, 44, 0.1);
								border-radius: 4px;
							}
							.history-table-container::-webkit-scrollbar-thumb {
								background: #00c2ce;
								border-radius: 4px;
							}
							.history-table-container::-webkit-scrollbar-thumb:hover {
								background: #009cb4;
							}
							/* Natural table sizing */
							.history-table {
								width: 100%;
								min-width: unset;
							}
							/* Mobile responsiveness */
							@media (max-width: 600px) {
								.history-table-container {
									padding: 1rem;
									border-radius: 15px;
									max-height: 500px;
								}
								.history-table {
									font-size: 0.8rem;
								}
								.history-table th,
								.history-table td {
									padding: 0.75rem 0.5rem;
									white-space: nowrap;
								}
								.history-table th {
									font-size: 0.75rem;
								}
							}
						</style>
                        <?php if (empty($payments)): ?>
                            <div class="no-data" style="text-align: center; padding: 3rem 2rem; color: rgba(26, 32, 44, 0.7);">
                                <h3 data-i18n="noTransactions" style="font-size: 1.5rem; margin-bottom: 0.5rem; color: #1a202c;">No Transactions Yet</h3>
                                <p data-i18n="noTransactionsHint" style="font-size: 1rem; margin: 0;">You haven't made any payments. Start by linking your account.</p>
                            </div>
						<?php else: ?>
							<table class="history-table" style="width: 100%; border-collapse: collapse; margin: 0; font-size: 0.9rem; background: transparent; table-layout: auto;">
								<thead>
									<tr>
                                        <th data-i18n="thTransactionId" style="background: #00c2ce; color: white; padding: 1rem 0.75rem; text-align: left; font-weight: 600; font-size: 0.85rem; text-transform: uppercase; letter-spacing: 0.5px; border: none; position: sticky; top: 0; z-index: 10;">Transaction ID</th>
                                        <th data-i18n="thStatus" style="background: #00c2ce; color: white; padding: 1rem 0.75rem; text-align: left; font-weight: 600; font-size: 0.85rem; text-transform: uppercase; letter-spacing: 0.5px; border: none; position: sticky; top: 0; z-index: 10;">Status</th>
                                        <th data-i18n="thAmount" style="background: #00c2ce; color: white; padding: 1rem 0.75rem; text-align: left; font-weight: 600; font-size: 0.85rem; text-transform: uppercase; letter-spacing: 0.5px; border: none; position: sticky; top: 0; z-index: 10;">Amount</th>
                                        <th data-i18n="thMethod" style="background: #00c2ce; color: white; padding: 1rem 0.75rem; text-align: left; font-weight: 600; font-size: 0.85rem; text-transform: uppercase; letter-spacing: 0.5px; border: none; position: sticky; top: 0; z-index: 10;">Method</th>
                                        <th data-i18n="thPlateNumber" style="background: #00c2ce; color: white; padding: 1rem 0.75rem; text-align: left; font-weight: 600; font-size: 0.85rem; text-transform: uppercase; letter-spacing: 0.5px; border: none; position: sticky; top: 0; z-index: 10;">Plate Number</th>
                                        <th data-i18n="thReference" style="background: #00c2ce; color: white; padding: 1rem 0.75rem; text-align: left; font-weight: 600; font-size: 0.85rem; text-transform: uppercase; letter-spacing: 0.5px; border: none; position: sticky; top: 0; z-index: 10;">Reference</th>
                                        <th data-i18n="thDate" style="background: #00c2ce; color: white; padding: 1rem 0.75rem; text-align: left; font-weight: 600; font-size: 0.85rem; text-transform: uppercase; letter-spacing: 0.5px; border: none; position: sticky; top: 0; z-index: 10;">Date</th>
                                        <th data-i18n="thActions" style="background: #00c2ce; color: white; padding: 1rem 0.75rem; text-align: left; font-weight: 600; font-size: 0.85rem; text-transform: uppercase; letter-spacing: 0.5px; border: none; position: sticky; top: 0; z-index: 10;">Actions</th>
									</tr>
								</thead>
								<tbody>
									<?php foreach ($payments as $payment): ?>
										<tr data-status="<?php echo strtolower($payment['transaction_status']); ?>" data-method="<?php echo strtolower($payment['payment_method']); ?>" data-charge-type="<?php echo strtolower(explode(' ', $payment['service_type'])[0]); ?>">
											<td style="padding: 1rem 0.75rem; border-bottom: 1px solid rgba(26, 32, 44, 0.1); vertical-align: middle; background: transparent; transition: all 0.2s ease;"><?php echo htmlspecialchars($payment['ticket_id']); ?></td>
											<td style="padding: 1rem 0.75rem; border-bottom: 1px solid rgba(26, 32, 44, 0.1); vertical-align: middle; background: transparent; transition: all 0.2s ease;">
												<span class="status-badge status-<?php echo strtolower($payment['transaction_status']); ?>">
													<?php echo htmlspecialchars(ucfirst($payment['transaction_status'])); ?>
												</span>
											</td>
											<td style="padding: 1rem 0.75rem; border-bottom: 1px solid rgba(26, 32, 44, 0.1); vertical-align: middle; background: transparent; transition: all 0.2s ease;">₱<?php echo number_format($payment['amount'], 2); ?></td>
											<td style="padding: 1rem 0.75rem; border-bottom: 1px solid rgba(26, 32, 44, 0.1); vertical-align: middle; background: transparent; transition: all 0.2s ease;"><?php echo htmlspecialchars(ucfirst($payment['payment_method'])); ?></td>
											<td style="padding: 1rem 0.75rem; border-bottom: 1px solid rgba(26, 32, 44, 0.1); vertical-align: middle; background: transparent; transition: all 0.2s ease;"><?php echo htmlspecialchars($payment['plate_number'] ?: 'N/A'); ?></td>
											<td style="padding: 1rem 0.75rem; border-bottom: 1px solid rgba(26, 32, 44, 0.1); vertical-align: middle; background: transparent; transition: all 0.2s ease;"><?php echo htmlspecialchars($payment['reference_number'] ?: 'N/A'); ?></td>
											<td style="padding: 1rem 0.75rem; border-bottom: 1px solid rgba(26, 32, 44, 0.1); vertical-align: middle; background: transparent; transition: all 0.2s ease;"><?php echo htmlspecialchars(date('M d, Y H:i', strtotime($payment['processed_at']))); ?></td>
                                            <td style="padding: 1rem 0.75rem; border-bottom: 1px solid rgba(26, 32, 44, 0.1); vertical-align: middle; background: transparent; transition: all 0.2s ease;">
                                                <button class="action-button" onclick="showDetails('<?php echo htmlspecialchars($payment['ticket_id']); ?>')" data-i18n="view" style="background: linear-gradient(135deg, #6c757d 0%, #495057 100%); color: white; border: none; border-radius: 6px; padding: 0.5rem 0.75rem; cursor: pointer; transition: all 0.3s ease; font-size: 0.8rem;">View</button>
                                            </td>
										</tr>
									<?php endforeach; ?>
								</tbody>
							</table>
						<?php endif; ?>
					</div>
				</div>
			</section>

			<!-- Transaction Details Modal -->
            <div id="transactionModal" class="modal">
				<div class="modal-content">
					<div class="modal-header">
                        <h3 data-i18n="modalTitle">Transaction Details</h3>
						<button class="modal-close" onclick="closeModal()">&times;</button>
					</div>
					<div class="modal-body" id="modalBody">
						<!-- Transaction details will be populated here -->
					</div>
				</div>
			</div>

		<!-- Footer -->
		<?php include __DIR__ . '/partials/footer.php'; ?>
		
		</div>

        <!-- Scripts -->
		<script src="assets/js/jquery.min.js"></script>
		<script src="assets/js/jquery.dropotron.min.js"></script>
		<script src="assets/js/browser.min.js"></script>
		<script src="assets/js/breakpoints.min.js"></script>
		<script src="assets/js/util.js"></script>
		<script src="assets/js/main.js"></script>
        <script src="assets/js/i18n.js"></script>
		<style>
			/* Modal Styles */
			.modal {
				display: none;
				position: fixed;
				z-index: 1000;
				left: 0;
				top: 0;
				width: 100%;
				height: 100%;
				background-color: rgba(0, 0, 0, 0.5);
			}

			.modal.show {
				display: flex;
				align-items: center;
				justify-content: center;
			}

			.modal-content {
				background: white;
				border-radius: 15px;
				padding: 0;
				width: 90%;
				max-width: 500px;
				max-height: 80vh;
				overflow-y: auto;
			}

			.modal-header {
				background: #00c2ce;
				color: white;
				padding: 1rem 1.5rem;
				display: flex;
				justify-content: space-between;
				align-items: center;
			}

			.modal-header h3 {
				margin: 0;
				font-size: 1.25rem;
			}

			.modal-close {
				background: none;
				border: none;
				color: white;
				font-size: 1.5rem;
				cursor: pointer;
				padding: 0;
			}

			.modal-close:hover {
				background: none;
				border: none;
			}

			.modal-body {
				padding: 1.5rem;
			}

			.transaction-details {
				display: flex;
				flex-direction: column;
				gap: 0.75rem;
			}

			.detail-row {
				display: flex;
				justify-content: space-between;
				align-items: center;
				padding: 0.5rem 0;
				border-bottom: 1px solid #eee;
			}

			.detail-row:last-child {
				border-bottom: none;
			}

			.detail-label {
				font-weight: 600;
				color: #333;
				font-size: 0.9rem;
			}

			.detail-value {
				color: #666;
				text-align: right;
			}

			/* Mobile responsiveness for modal */
			@media (max-width: 600px) {
				.modal-content {
					width: 95%;
					max-height: 90vh;
				}

				.modal-header {
					padding: 0.75rem 1rem;
				}

				.modal-header h3 {
					font-size: 1.1rem;
				}

				.modal-body {
					padding: 1rem;
				}

				.detail-row {
					flex-direction: column;
					align-items: flex-start;
					gap: 0.25rem;
				}

				.detail-value {
					text-align: left;
				}
			}

			@media (max-width: 370px) {
				.history-table-container {
					padding: 0.5rem;
					border-radius: 10px;
					max-height: 350px;
					overflow-x: auto;
				}
				.history-table {
					font-size: 0.7rem;
					min-width: 200px;
				}
				.history-table th,
				.history-table td {
					padding: 0.5rem 0.25rem;
					white-space: nowrap;
				}
				.modal-content {
					width: 98%;
					max-height: 95vh;
				}
				.modal-header {
					padding: 0.5rem 0.75rem;
				}
				.modal-header h3 {
					font-size: 1rem;
				}
				.modal-body {
					padding: 0.75rem;
				}
				.detail-row {
					padding: 0.5rem 0;
				}
				.detail-label,
				.detail-value {
					font-size: 0.8rem;
				}
			}
		</style>

		<script>
			// Mobile Menu Toggle
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
				mobileNavLinks.forEach(link => link.addEventListener('click', closeMobileMenu));

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

			// Filter functionality
			function applyFilters() {
				const statusFilter = document.getElementById('statusFilter').value;
				const methodFilter = document.getElementById('methodFilter').value;
				const rows = document.querySelectorAll('.history-table tbody tr');

				rows.forEach(row => {
					let showRow = true;

					// Charge type filter
					if (statusFilter !== 'all') {
						const rowChargeType = row.getAttribute('data-charge-type');
						if (rowChargeType !== statusFilter) {
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
							plate: row.cells[4].textContent,
							reference: row.cells[5].textContent,
							date: row.cells[6].textContent
						};
					}
				});

                if (transactionData) {
					const modalBody = document.getElementById('modalBody');
                    const d = window.CephraI18n ? window.CephraI18n.dict[(window.CephraI18n.getLang&&window.CephraI18n.getLang())||'en'] : null;
                    const label = (k, fallback) => d && d[k] ? d[k] : fallback;
                    modalBody.innerHTML = `
                        <div class="transaction-details">
                            <div class="detail-row">
                                <span class="detail-label" data-i18n="modalId">${label('modalId','Transaction ID:')}</span>
                                <span class="detail-value">${transactionData.id}</span>
                            </div>
                            <div class="detail-row">
                                <span class="detail-label" data-i18n="modalStatus">${label('modalStatus','Status:')}</span>
                                <span class="detail-value">${transactionData.status}</span>
                            </div>
                            <div class="detail-row">
                                <span class="detail-label" data-i18n="modalAmount">${label('modalAmount','Amount:')}</span>
                                <span class="detail-value">${transactionData.amount}</span>
                            </div>
                            <div class="detail-row">
                                <span class="detail-label" data-i18n="modalMethod">${label('modalMethod','Payment Method:')}</span>
                                <span class="detail-value">${transactionData.method}</span>
                            </div>
                            <div class="detail-row">
                                <span class="detail-label" data-i18n="modalPlate">${label('modalPlate','Plate Number:')}</span>
                                <span class="detail-value">${transactionData.plate}</span>
                            </div>
                            <div class="detail-row">
                                <span class="detail-label" data-i18n="modalReference">${label('modalReference','Reference Number:')}</span>
                                <span class="detail-value">${transactionData.reference}</span>
                            </div>
                            <div class="detail-row">
                                <span class="detail-label" data-i18n="modalDate">${label('modalDate','Date & Time:')}</span>
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
				initMobileMenu();
				applyFilters();
                if (window.CephraI18n && window.CephraI18n.apply){ window.CephraI18n.apply(); }
			});
		</script>
	</body>
</html>


