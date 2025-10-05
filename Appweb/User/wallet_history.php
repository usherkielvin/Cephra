<?php
session_start();
if (!isset($_SESSION['username'])) {
    header("Location: index.php");
    exit();
}
// Ensure correct local timezone for displaying times
date_default_timezone_set('Asia/Manila');
require_once 'config/database.php';
$db = new Database();
$conn = $db->getConnection();

$transactions = [];
$balance = 0.00;
if ($conn) {
    $username = $_SESSION['username'];
    $stmt = $conn->prepare("SELECT balance FROM wallet_balance WHERE username = :u");
    $stmt->bindParam(':u', $username);
    $stmt->execute();
    $row = $stmt->fetch(PDO::FETCH_ASSOC);
    $balance = $row ? (float)$row['balance'] : 0.00;

    $stmt = $conn->prepare("SELECT transaction_type, amount, description, reference_id, transaction_date, previous_balance, new_balance FROM wallet_transactions WHERE username = :u ORDER BY transaction_date DESC LIMIT 100");
    $stmt->bindParam(':u', $username);
    $stmt->execute();
    $transactions = $stmt->fetchAll(PDO::FETCH_ASSOC);
}
?>
<!DOCTYPE HTML>
<html>
    <head>
        <title>Wallet History - Cephra</title>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
        <link rel="icon" type="image/png" href="images/logo.png?v=2" />
        <link rel="apple-touch-icon" href="images/logo.png?v=2" />
        <link rel="manifest" href="manifest.webmanifest" />
        <meta name="theme-color" content="#062635" />
        <link rel="stylesheet" href="css/vantage-style.css" />
        <link rel="stylesheet" href="assets/css/fontawesome-all.min.css" />
    </head>
    <body class="homepage is-preload">
        <div id="page-wrapper">
            <?php include __DIR__ . '/partials/header.php'; ?>

            <section class="history-section" style="padding: 100px 0; background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);">
                <div class="container">
                    <div class="section-header" style="text-align: center; margin-bottom: 60px;">
                        <h2 class="section-title" style="font-size: 2.5rem; font-weight: 700; margin-bottom: 1rem; background: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text;">Wallet Transactions</h2>
                        <p class="section-description" style="font-size: 1.2rem; color: rgba(26, 32, 44, 0.8); max-width: 600px; margin: 0 auto;">Top-ups and deductions overview</p>
                    </div>

                    <div class="history-table-container" style="background: white; border-radius: 20px; padding: 2rem; border: 1px solid rgba(26, 32, 44, 0.1); box-shadow: 0 5px 15px rgba(0, 194, 206, 0.1); overflow-y: auto; overflow-x: hidden; position: relative; max-height: 600px;">
                        <?php if (empty($transactions)): ?>
                            <div class="no-data" style="text-align: center; padding: 3rem 2rem; color: rgba(26, 32, 44, 0.7);">
                                <h3 style="font-size: 1.5rem; margin-bottom: 0.5rem; color: #1a202c;">No Wallet Transactions</h3>
                                <p style="font-size: 1rem; margin: 0;">Your top-ups and deductions will appear here.</p>
                            </div>
                        <?php else: ?>
                            <table class="history-table" style="width: 100%; border-collapse: collapse; margin: 0; font-size: 0.9rem; background: transparent; table-layout: auto;">
                                <thead>
                                    <tr>
                                        <th style="background: #00c2ce; color: white; padding: 1rem 0.75rem; text-align: left; font-weight: 600; font-size: 0.85rem; text-transform: uppercase; letter-spacing: 0.5px; position: sticky; top: 0; z-index: 10;">Date</th>
                                        <th style="background: #00c2ce; color: white; padding: 1rem 0.75rem; text-align: left; font-weight: 600; font-size: 0.85rem; text-transform: uppercase; letter-spacing: 0.5px; position: sticky; top: 0; z-index: 10;">Type</th>
                                        <th style="background: #00c2ce; color: white; padding: 1rem 0.75rem; text-align: left; font-weight: 600; font-size: 0.85rem; text-transform: uppercase; letter-spacing: 0.5px; position: sticky; top: 0; z-index: 10;">Description</th>
                                        <th style="background: #00c2ce; color: white; padding: 1rem 0.75rem; text-align: right; font-weight: 600; font-size: 0.85rem; text-transform: uppercase; letter-spacing: 0.5px; position: sticky; top: 0; z-index: 10;">Amount</th>
                                        <th style="background: #00c2ce; color: white; padding: 1rem 0.75rem; text-align: right; font-weight: 600; font-size: 0.85rem; text-transform: uppercase; letter-spacing: 0.5px; position: sticky; top: 0; z-index: 10;">Balance</th>
                                        <th style="background: #00c2ce; color: white; padding: 1rem 0.75rem; text-align: left; font-weight: 600; font-size: 0.85rem; text-transform: uppercase; letter-spacing: 0.5px; position: sticky; top: 0; z-index: 10;">Reference</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <?php foreach ($transactions as $t): ?>
                                        <?php $isPositive = ($t['amount'] >= 0); ?>
                                        <tr>
                                            <td style="padding: 1rem 0.75rem; border-bottom: 1px solid rgba(26, 32, 44, 0.1);"><?php echo htmlspecialchars(date('M d, Y H:i', strtotime($t['transaction_date']))); ?></td>
                                            <td style="padding: 1rem 0.75rem; border-bottom: 1px solid rgba(26, 32, 44, 0.1);"><?php echo htmlspecialchars(ucfirst(strtolower($t['transaction_type']))); ?></td>
                                            <td style="padding: 1rem 0.75rem; border-bottom: 1px solid rgba(26, 32, 44, 0.1);"><?php echo htmlspecialchars($t['description']); ?></td>
                                            <td style="padding: 1rem 0.75rem; border-bottom: 1px solid rgba(26, 32, 44, 0.1); text-align: right;" class="<?php echo $isPositive ? 'positive' : 'negative'; ?>"><?php echo ($isPositive ? '+' : '-') . '₱' . number_format(abs($t['amount']), 2); ?></td>
                                            <td style="padding: 1rem 0.75rem; border-bottom: 1px solid rgba(26, 32, 44, 0.1); text-align: right;">₱<?php echo number_format((float)$t['new_balance'], 2); ?></td>
                                            <td style="padding: 1rem 0.75rem; border-bottom: 1px solid rgba(26, 32, 44, 0.1);"><?php echo htmlspecialchars($t['reference_id'] ?? ''); ?></td>
                                        </tr>
                                    <?php endforeach; ?>
                                </tbody>
                            </table>
                        <?php endif; ?>
                    </div>
                </div>
            </section>

            <footer class="footer">
                <div class="container">
                    <div class="footer-content">
                        <div class="footer-section">
                            <div class="footer-logo">
                                <img src="images/logo.png" alt="Cephra" class="footer-logo-img" />
                                <span class="footer-logo-text">CEPHRA</span>
                            </div>
                            <p class="footer-description">Your ultimate electric vehicle charging platform, powering the future of sustainable transportation.</p>
                        </div>
                    </div>
                    <div class="footer-bottom">
                        <p>&copy; 2025 Cephra. All rights reserved.</p>
                    </div>
                </div>
            </footer>
        </div>
    </body>
</html>


