<?php
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

// Function to ensure user has a plate number (like Java system)
function ensureUserHasPlateNumber($conn, $username) {
    try {
        // Check if user already has a plate number
        $stmt = $conn->prepare("SELECT plate_number FROM users WHERE username = ?");
        $stmt->execute([$username]);
        $user = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if ($user && $user['plate_number'] && !empty(trim($user['plate_number']))) {
            return $user['plate_number'];
        }
        
        // Generate a unique plate number if user doesn't have one
        $plate_number = generateUniquePlateNumber($conn);
        
        // Update user with the new plate number
        $stmt = $conn->prepare("UPDATE users SET plate_number = ? WHERE username = ?");
        $stmt->execute([$plate_number, $username]);
        
        return $plate_number;
    } catch (Exception $e) {
        error_log("Failed to ensure plate number for user $username: " . $e->getMessage());
        return null;
    }
}

// Function to generate a unique plate number
function generateUniquePlateNumber($conn) {
    // Generate plate number format: ABC-1234 (like Java system)
    $letters = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];
    $attempts = 0;
    $max_attempts = 100;
    
    do {
        $letter1 = $letters[array_rand($letters)];
        $letter2 = $letters[array_rand($letters)];
        $letter3 = $letters[array_rand($letters)];
        $numbers = sprintf('%04d', rand(0, 9999));
        
        $plate_number = $letter1 . $letter2 . $letter3 . '-' . $numbers;
        
        // Check if plate number is unique
        $stmt = $conn->prepare("SELECT COUNT(*) FROM users WHERE plate_number = ?");
        $stmt->execute([$plate_number]);
        $count = $stmt->fetchColumn();
        
        if ($count == 0) {
            return $plate_number;
        }
        
        $attempts++;
    } while ($attempts < $max_attempts);
    
    // Fallback: use timestamp-based plate number
    return 'WEB-' . date('Ymd') . '-' . substr(uniqid(), -4);
}

$username = $_SESSION['username'];
$input = json_decode(file_get_contents('php://input'), true);
$action = $input['action'] ?? '';

// Debug logging
error_log("Status update API called - Username: $username, Action: $action");

if ($action === 'test_status') {
    // Test endpoint to check current status for debugging
    $test_username = $_POST['username'] ?? $username;
    
    $stmt_queue = $conn->prepare("SELECT ticket_id, status, payment_status, created_at FROM queue_tickets WHERE username = :username ORDER BY created_at DESC LIMIT 1");
    $stmt_queue->bindParam(':username', $test_username);
    $stmt_queue->execute();
    $queue_ticket = $stmt_queue->fetch(PDO::FETCH_ASSOC);
    
    $stmt_active = $conn->prepare("SELECT ticket_id, status, bay_number, created_at FROM active_tickets WHERE username = :username ORDER BY created_at DESC LIMIT 1");
    $stmt_active->bindParam(':username', $test_username);
    $stmt_active->execute();
    $active_ticket = $stmt_active->fetch(PDO::FETCH_ASSOC);
    
    echo json_encode([
        'success' => true,
        'test_username' => $test_username,
        'session_username' => $username,
        'queue_ticket' => $queue_ticket,
        'active_ticket' => $active_ticket,
        'charging_grid' => $conn->query("SELECT bay_number, ticket_id FROM charging_grid WHERE username = '$test_username'")->fetchAll(PDO::FETCH_ASSOC)
    ]);
    exit();
}

if ($action === 'get_status') {
    // Fetch latest charging status from both queue_tickets and active_tickets
    $stmt_queue = $conn->prepare("SELECT ticket_id, status, payment_status, created_at FROM queue_tickets WHERE username = :username ORDER BY created_at DESC LIMIT 1");
    $stmt_queue->bindParam(':username', $username);
    $stmt_queue->execute();
    $queue_ticket = $stmt_queue->fetch(PDO::FETCH_ASSOC);

    $stmt_active = $conn->prepare("SELECT ticket_id, status, bay_number, created_at FROM active_tickets WHERE username = :username ORDER BY created_at DESC LIMIT 1");
    $stmt_active->bindParam(':username', $username);
    $stmt_active->execute();
    $active_ticket = $stmt_active->fetch(PDO::FETCH_ASSOC);

    // Determine current status based on ticket states
    $status_text = 'Connected';
    $background_class = 'connected-bg';
    $button_text = 'Charge Now';
    $button_href = 'ChargingPage.php';
    
    // Debug logging
    error_log("Initial status for $username: $status_text");

    // Determine which ticket is more recent and prioritize it
    $use_queue_ticket = false;
    $use_active_ticket = false;
    
    if ($queue_ticket && $active_ticket) {
        // Compare timestamps to see which is more recent
        $queue_time = strtotime($queue_ticket['created_at']);
        $active_time = strtotime($active_ticket['created_at']);
        $use_queue_ticket = ($queue_time >= $active_time);
        $use_active_ticket = !$use_queue_ticket;
        
        error_log("Both tickets found for $username - Queue: {$queue_ticket['created_at']}, Active: {$active_ticket['created_at']}, Using: " . ($use_queue_ticket ? 'queue' : 'active'));
    } elseif ($queue_ticket) {
        $use_queue_ticket = true;
        error_log("Only queue ticket found for $username");
    } elseif ($active_ticket) {
        $use_active_ticket = true;
        error_log("Only active ticket found for $username");
    }

    // Check active_tickets for charging states
    if ($use_active_ticket && $active_ticket) {
        $ticket_status = strtolower($active_ticket['status']);
        if ($ticket_status === 'charging' || $ticket_status === 'active' || $ticket_status === 'processing') {
            $background_class = 'charging-bg';
            
            // Try to get bay number from charging_grid first (most accurate)
            $stmt_charging_grid = $conn->prepare("SELECT bay_number FROM charging_grid WHERE username = :username AND ticket_id IS NOT NULL");
            $stmt_charging_grid->bindParam(':username', $username);
            $stmt_charging_grid->execute();
            $charging_grid_bay = $stmt_charging_grid->fetch(PDO::FETCH_ASSOC);
            
            if ($charging_grid_bay && $charging_grid_bay['bay_number']) {
                $bay_number = str_replace('Bay-', 'Bay ', $charging_grid_bay['bay_number']);
            } else {
                // Fallback to active_tickets bay_number or TBD
                $fallback_bay = $active_ticket['bay_number'] ?? 'TBD';
                $bay_number = str_replace('Bay-', 'Bay ', $fallback_bay);
            }
            
            $status_text = 'Charging at ' . $bay_number;
            $button_text = 'Check Monitor';
            $button_href = '../Monitor/index.php';
            
            // Debug logging
            error_log("Status updated to charging (from active_tickets) for $username: $status_text");
            
            // Add ticket information for active_tickets charging status
            $ticket_info = [
                'ticket_id' => $active_ticket['ticket_id'],
                'status' => $active_ticket['status']
            ];
        }
    }
    // Check queue_tickets for all states (including charging)
    if ($use_queue_ticket && $queue_ticket) {
        $queue_status = strtolower($queue_ticket['status']);
        $payment_status = strtolower($queue_ticket['payment_status'] ?? '');
        $ticket_id = $queue_ticket['ticket_id'];

        error_log("Processing queue ticket for $username - Original status: {$queue_ticket['status']}, Payment status: {$queue_ticket['payment_status']}, Lowercase status: $queue_status, Ticket ID: $ticket_id");

        // Check if payment is completed - if so, show as connected
        if (in_array($payment_status, ['paid', 'completed', 'success'])) {
            $status_text = 'Connected';
            $background_class = 'connected-bg';
            $button_text = 'Charge Now';
            $button_href = 'ChargingPage.php';
            error_log("Status updated to connected (payment completed) for $username: $status_text");
        } elseif ($queue_status === 'charging') {
            $background_class = 'charging-bg';

            // Try to get bay number from charging_grid first (most accurate)
            $stmt_charging_grid = $conn->prepare("SELECT bay_number FROM charging_grid WHERE username = :username AND ticket_id IS NOT NULL");
            $stmt_charging_grid->bindParam(':username', $username);
            $stmt_charging_grid->execute();
            $charging_grid_bay = $stmt_charging_grid->fetch(PDO::FETCH_ASSOC);

            if ($charging_grid_bay && $charging_grid_bay['bay_number']) {
                $bay_number = str_replace('Bay-', 'Bay ', $charging_grid_bay['bay_number']);
            } else {
                // Fallback to TBD if no bay found
                $bay_number = 'TBD';
            }

            $status_text = 'Charging at ' . $bay_number;
            $button_text = 'Check Monitor';
            $button_href = '../Monitor/index.php';
            error_log("Status updated to charging (from queue_tickets) for $username: $status_text");
        } elseif ($queue_status === 'pending') {
            $background_class = 'queue-pending-bg';
            $status_text = 'Queue Pending';
            $button_text = 'Check Monitor';
            $button_href = '../Monitor/index.php';
            error_log("Status updated to queue pending for $username: $status_text");
        } elseif ($queue_status === 'waiting') {
            $background_class = 'waiting-bg';
            $status_text = 'Waiting';
            $button_text = 'Check Monitor';
            $button_href = '../Monitor/index.php';
            error_log("Status updated to waiting for $username: $status_text");
        } elseif ($queue_status === 'in progress' || $queue_status === 'in_progress') {
            $background_class = 'waiting-bg';
            $status_text = 'In Progress';
            $button_text = 'Check Monitor';
            $button_href = '../Monitor/index.php';
            error_log("Status updated to in progress for $username: $status_text");
        } elseif ($queue_status === 'complete') {
            $background_class = 'queue-pending-bg';
            $status_text = 'Pending Payment';
            $button_text = 'Pay Now';
            $button_href = '../Monitor/index.php';
            error_log("Status updated to pending payment for $username: $status_text");
        }

        // Add ticket information to the response for queue states
        $ticket_info = [
            'ticket_id' => $ticket_id,
            'status' => $queue_ticket['status']
        ];
    }

    // Get current battery level
    $stmt_battery = $conn->prepare("SELECT battery_level FROM battery_levels WHERE username = :username ORDER BY last_updated DESC LIMIT 1");
    $stmt_battery->bindParam(':username', $username);
    $stmt_battery->execute();
    $battery_row = $stmt_battery->fetch(PDO::FETCH_ASSOC);
    $battery_level = $battery_row ? $battery_row['battery_level'] . '%' : '100%';

    // Check for pending notifications (like Java MY_TURN notification)
    $notification = null;
    $notifications_file = 'notifications.json';
    if (file_exists($notifications_file)) {
        $notifications = json_decode(file_get_contents($notifications_file), true) ?: [];
        if (isset($notifications[$username])) {
            $notification = $notifications[$username];
            // Remove notification after sending it (one-time notification)
            unset($notifications[$username]);
            file_put_contents($notifications_file, json_encode($notifications));
        }
    }

    // Final debug logging
    error_log("Final status for $username: $status_text, Background: $background_class, Button: $button_text");
    
    echo json_encode([
        'success' => true,
        'status_text' => $status_text,
        'background_class' => $background_class,
        'button_text' => $button_text,
        'button_href' => $button_href,
        'battery_level' => $battery_level,
        'notification' => $notification,
        'ticket_info' => $ticket_info ?? null,
        'timestamp' => time()
    ]);
} elseif ($action === 'confirm_charging') {
    $ticket_id = $input['ticket_id'] ?? '';
    
    if (!$ticket_id) {
        echo json_encode(['success' => false, 'error' => 'Ticket ID required']);
        exit();
    }
    
    try {
        // Move ticket from queue_tickets to active_tickets (like Java system)
        $conn->beginTransaction();
        
        // Get ticket details from queue_tickets
        $stmt = $conn->prepare("SELECT username, service_type, bay_number, initial_battery_level FROM queue_tickets WHERE ticket_id = ?");
        $stmt->execute([$ticket_id]);
        $ticket = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if (!$ticket) {
            $conn->rollback();
            echo json_encode(['success' => false, 'error' => 'Ticket not found']);
            exit();
        }
        
        // Ensure user has a plate number (like Java system)
        $plate_number = ensureUserHasPlateNumber($conn, $ticket['username']);
        
        // Insert into active_tickets with plate number
        if ($plate_number) {
            $stmt = $conn->prepare("INSERT INTO active_tickets (ticket_id, username, service_type, status, bay_number, initial_battery_level, plate_number, start_time) VALUES (?, ?, ?, 'charging', ?, ?, ?, NOW())");
            $stmt->execute([
                $ticket_id,
                $ticket['username'],
                $ticket['service_type'],
                $ticket['bay_number'],
                $ticket['initial_battery_level'],
                $plate_number
            ]);
        } else {
            $stmt = $conn->prepare("INSERT INTO active_tickets (ticket_id, username, service_type, status, bay_number, initial_battery_level, start_time) VALUES (?, ?, ?, 'charging', ?, ?, NOW())");
            $stmt->execute([
                $ticket_id,
                $ticket['username'],
                $ticket['service_type'],
                $ticket['bay_number'],
                $ticket['initial_battery_level']
            ]);
        }
        
        // Remove from queue_tickets
        $stmt = $conn->prepare("DELETE FROM queue_tickets WHERE ticket_id = ?");
        $stmt->execute([$ticket_id]);
        
        $conn->commit();
        
        echo json_encode([
            'success' => true,
            'message' => 'Ticket status updated to charging'
        ]);
        
    } catch (Exception $e) {
        $conn->rollback();
        echo json_encode(['success' => false, 'error' => 'Failed to update ticket status']);
    }
} else {
    echo json_encode(['success' => false, 'error' => 'Invalid action']);
}
?>
