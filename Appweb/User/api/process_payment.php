<?php
// Disable error display to prevent breaking JSON
ini_set('display_errors', 0);
ini_set('log_errors', 1);

session_start();
if (!isset($_SESSION['username'])) {
    http_response_code(401);
    echo json_encode(['success' => false, 'error' => 'Unauthorized']);
    exit();
}

require_once '../config/database.php';

header('Content-Type: application/json');

$db = new Database();
$conn = $db->getConnection();

if (!$conn) {
    echo json_encode(['success' => false, 'error' => 'Database connection failed']);
    exit();
}

$input = json_decode(file_get_contents('php://input'), true);
$username = $_SESSION['username'];
$ticket_id = $input['ticket_id'] ?? '';
$payment_method = $input['payment_method'] ?? '';
$amount = floatval($input['amount'] ?? 0);

if (!$ticket_id || !$payment_method || $amount <= 0) {
    echo json_encode(['success' => false, 'error' => 'Invalid parameters']);
    exit();
}

try {
    $conn->beginTransaction();
    
    // Get ticket details
    $stmt = $conn->prepare("SELECT * FROM queue_tickets WHERE ticket_id = ? AND username = ?");
    $stmt->execute([$ticket_id, $username]);
    $ticket = $stmt->fetch(PDO::FETCH_ASSOC);
    
    if (!$ticket) {
        $conn->rollback();
        echo json_encode(['success' => false, 'error' => 'Ticket not found']);
        exit();
    }
    
    $remaining_balance = null;
    
    // Handle different payment methods
    if ($payment_method === 'ewallet') {
        // Check wallet balance
        $stmt = $conn->prepare("SELECT balance FROM wallet_balance WHERE username = ? FOR UPDATE");
        $stmt->execute([$username]);
        $wallet = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if (!$wallet || $wallet['balance'] < $amount) {
            $conn->rollback();
            echo json_encode(['success' => false, 'error' => 'Insufficient wallet balance']);
            exit();
        }
        
        // Deduct from wallet
        $new_balance = $wallet['balance'] - $amount;
        $stmt = $conn->prepare("UPDATE wallet_balance SET balance = ?, updated_at = NOW() WHERE username = ?");
        $stmt->execute([$new_balance, $username]);
        
        // Record wallet transaction
        $reference = 'WT' . date('YmdHis') . rand(100, 999);
        $stmt = $conn->prepare("INSERT INTO wallet_transactions (username, transaction_type, amount, previous_balance, new_balance, description, reference_id, transaction_date) VALUES (?, 'PAYMENT', ?, ?, ?, 'Charging payment - E-Wallet', ?, NOW())");
        $stmt->execute([$username, $amount, $wallet['balance'], $new_balance, $reference]);
        
        $remaining_balance = $new_balance;
        
    } elseif ($payment_method === 'cash') {
        // For cash payment, no wallet deduction needed
        // Just record the payment transaction
        $reference = 'CASH' . date('YmdHis') . rand(100, 999);
    }
    
    // Update ticket payment status to 'paid'
    $stmt = $conn->prepare("UPDATE queue_tickets SET payment_status = 'paid', status = 'completed' WHERE ticket_id = ? AND username = ?");
    $stmt->execute([$ticket_id, $username]);
    
    // Record payment transaction
    $transaction_id = 'TXN' . date('YmdHis') . rand(100, 999);
    $stmt = $conn->prepare("INSERT INTO payment_transactions (username, ticket_id, amount, payment_method, transaction_status, processed_at) VALUES (?, ?, ?, ?, 'completed', NOW())");
    $stmt->execute([$username, $ticket_id, $amount, $payment_method]);
    
    // Update battery level to 100% when charging is complete and paid
    $stmt = $conn->prepare("UPDATE battery_levels SET battery_level = 100, last_updated = NOW() WHERE username = ?");
    $result = $stmt->execute([$username]);
    
    if ($result) {
        error_log("Payment: Successfully set battery to 100% for user $username");
    } else {
        error_log("Payment: Failed to set battery to 100% for user $username");
    }
    
    // Save to charging_history
    $stmt = $conn->prepare("INSERT INTO charging_history (ticket_id, username, service_type, status, payment_status, amount, payment_method, initial_battery_level, final_battery_level, start_time, end_time, created_at) VALUES (?, ?, ?, 'completed', 'paid', ?, ?, ?, ?, NOW(), NOW(), NOW())");
    $stmt->execute([
        $ticket_id,
        $username,
        $ticket['service_type'],
        $amount,
        $payment_method,
        $ticket['initial_battery_level'] ?? 0,
        100 // Charging is complete, battery is at 100%
    ]);
    
    // Also update active_tickets if exists (for admin system)
    $stmt = $conn->prepare("UPDATE active_tickets SET status = 'completed', payment_status = 'paid' WHERE ticket_id = ? AND username = ?");
    $stmt->execute([$ticket_id, $username]);
    
    $conn->commit();
    
    echo json_encode([
        'success' => true,
        'transaction_id' => $transaction_id,
        'remaining_balance' => $remaining_balance,
        'payment_method' => $payment_method,
        'message' => 'Payment processed successfully'
    ]);
    
} catch (Exception $e) {
    $conn->rollback();
    error_log("Payment processing error: " . $e->getMessage());
    echo json_encode(['success' => false, 'error' => 'Payment processing failed: ' . $e->getMessage()]);
}
?>
