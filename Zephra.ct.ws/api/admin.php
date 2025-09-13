<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST');
header('Access-Control-Allow-Headers: Content-Type');

session_start();

// Check if admin is logged in
if (!isset($_SESSION['admin_logged_in']) || $_SESSION['admin_logged_in'] !== true) {
    http_response_code(401);
    echo json_encode(['error' => 'Unauthorized access']);
    exit();
}

require_once '../config/database.php';

$db = (new Database())->getConnection();
if (!$db) {
    echo json_encode(['error' => 'Database connection failed']);
    exit();
}

$method = $_SERVER['REQUEST_METHOD'];

// Get action from POST data or query string
$action = '';
if ($method === 'POST') {
    $action = $_POST['action'] ?? '';
} else {
    $action = $_GET['action'] ?? '';
}

try {
    switch ($action) {
        case 'dashboard':
            // Get dashboard statistics
            $stats = [];
            
            // Total users
            $stmt = $db->query("SELECT COUNT(*) as count FROM users");
            $stats['total_users'] = $stmt->fetch(PDO::FETCH_ASSOC)['count'];
            
            // Queue count
            $stmt = $db->query("SELECT COUNT(*) as count FROM queue_tickets WHERE status IN ('Waiting', 'Processing')");
            $stats['queue_count'] = $stmt->fetch(PDO::FETCH_ASSOC)['count'];
            
            // Active bays
            $stmt = $db->query("SELECT COUNT(*) as count FROM charging_bays WHERE status = 'Occupied'");
            $stats['active_bays'] = $stmt->fetch(PDO::FETCH_ASSOC)['count'];
            
            // Today's revenue (placeholder)
            $stats['revenue_today'] = 0;
            
            // Recent activity from actual database records
            $stmt = $db->query("
                SELECT 
                    'ticket' as type,
                    CONCAT('Ticket ', ticket_id, ' - ', username, ' (', service_type, ')') as description,
                    'fa-ticket-alt' as icon,
                    created_at
                FROM queue_tickets 
                ORDER BY created_at DESC 
                LIMIT 10
            ");
            $recent_activity = $stmt->fetchAll(PDO::FETCH_ASSOC);
            
            echo json_encode([
                'success' => true,
                'stats' => $stats,
                'recent_activity' => $recent_activity
            ]);
            break;

        case 'queue':
            // Get queue tickets
            $stmt = $db->query("
                SELECT 
                    ticket_id,
                    username,
                    service_type,
                    status,
                    payment_status,
                    initial_battery_level,
                    created_at
                FROM queue_tickets 
                ORDER BY created_at DESC
            ");
            $queue = $stmt->fetchAll(PDO::FETCH_ASSOC);
            
            echo json_encode([
                'success' => true,
                'queue' => $queue
            ]);
            break;

        case 'bays':
            // Get charging bays
            $stmt = $db->query("
                SELECT 
                    bay_number,
                    bay_type,
                    status,
                    current_ticket_id,
                    current_username,
                    start_time
                FROM charging_bays 
                ORDER BY bay_number
            ");
            $bays = $stmt->fetchAll(PDO::FETCH_ASSOC);
            
            echo json_encode([
                'success' => true,
                'bays' => $bays
            ]);
            break;

        case 'users':
            // Get users
            $stmt = $db->query("
                SELECT 
                    username,
                    firstname,
                    lastname,
                    email,
                    created_at
                FROM users 
                ORDER BY created_at DESC
            ");
            $users = $stmt->fetchAll(PDO::FETCH_ASSOC);
            
            echo json_encode([
                'success' => true,
                'users' => $users
            ]);
            break;

        case 'ticket-details':
            $ticket_id = $_GET['ticket_id'] ?? '';
            if (!$ticket_id) {
                echo json_encode(['error' => 'Ticket ID required']);
                break;
            }
            
            $stmt = $db->prepare("
                SELECT 
                    ticket_id,
                    username,
                    service_type,
                    status,
                    payment_status,
                    initial_battery_level,
                    created_at
                FROM queue_tickets 
                WHERE ticket_id = ?
            ");
            $stmt->execute([$ticket_id]);
            $ticket = $stmt->fetch(PDO::FETCH_ASSOC);
            
            if ($ticket) {
                echo json_encode([
                    'success' => true,
                    'ticket' => $ticket
                ]);
            } else {
                echo json_encode(['error' => 'Ticket not found']);
            }
            break;

        case 'process-ticket':
            if ($method !== 'POST') {
                echo json_encode(['error' => 'Method not allowed']);
                break;
            }
            
            $ticket_id = $_POST['ticket_id'] ?? '';
            if (!$ticket_id) {
                echo json_encode(['error' => 'Ticket ID required']);
                break;
            }
            
            // Update ticket status to Processing
            $stmt = $db->prepare("UPDATE queue_tickets SET status = 'Processing' WHERE ticket_id = ?");
            $result = $stmt->execute([$ticket_id]);
            
            if ($result) {
                echo json_encode(['success' => true, 'message' => 'Ticket processed successfully']);
            } else {
                echo json_encode(['error' => 'Failed to process ticket']);
            }
            break;

        case 'set-bay-maintenance':
            if ($method !== 'POST') {
                echo json_encode(['error' => 'Method not allowed']);
                break;
            }
            
            $bay_number = $_POST['bay_number'] ?? '';
            if (!$bay_number) {
                echo json_encode(['error' => 'Bay number required']);
                break;
            }
            
            // Set bay to maintenance
            $stmt = $db->prepare("UPDATE charging_bays SET status = 'Maintenance' WHERE bay_number = ?");
            $result = $stmt->execute([$bay_number]);
            
            if ($result) {
                echo json_encode(['success' => true, 'message' => 'Bay set to maintenance mode']);
            } else {
                echo json_encode(['error' => 'Failed to set bay to maintenance']);
            }
            break;

        case 'set-bay-available':
            if ($method !== 'POST') {
                echo json_encode(['error' => 'Method not allowed']);
                break;
            }
            
            $bay_number = $_POST['bay_number'] ?? '';
            if (!$bay_number) {
                echo json_encode(['error' => 'Bay number required']);
                break;
            }
            
            // Set bay to available
            $stmt = $db->prepare("UPDATE charging_bays SET status = 'Available' WHERE bay_number = ?");
            $result = $stmt->execute([$bay_number]);
            
            if ($result) {
                echo json_encode(['success' => true, 'message' => 'Bay set to available']);
            } else {
                echo json_encode(['error' => 'Failed to set bay to available']);
            }
            break;

        case 'add-user':
            if ($method !== 'POST') {
                echo json_encode(['error' => 'Method not allowed']);
                break;
            }
            
            $username = $_POST['username'] ?? '';
            $firstname = $_POST['firstname'] ?? '';
            $lastname = $_POST['lastname'] ?? '';
            $email = $_POST['email'] ?? '';
            $password = $_POST['password'] ?? '';
            
            if (!$username || !$firstname || !$lastname || !$email || !$password) {
                echo json_encode(['error' => 'All fields are required']);
                break;
            }
            
            // Check if username already exists
            $stmt = $db->prepare("SELECT username FROM users WHERE username = ?");
            $stmt->execute([$username]);
            if ($stmt->fetch()) {
                echo json_encode(['error' => 'Username already exists']);
                break;
            }
            
            // Insert new user
            $stmt = $db->prepare("
                INSERT INTO users (username, firstname, lastname, email, password) 
                VALUES (?, ?, ?, ?, ?)
            ");
            $result = $stmt->execute([$username, $firstname, $lastname, $email, $password]);
            
            if ($result) {
                echo json_encode(['success' => true, 'message' => 'User added successfully']);
            } else {
                echo json_encode(['error' => 'Failed to add user']);
            }
            break;

        case 'delete-user':
            if ($method !== 'POST') {
                echo json_encode(['error' => 'Method not allowed']);
                break;
            }
            
            $username = $_POST['username'] ?? '';
            if (!$username) {
                echo json_encode(['error' => 'Username required']);
                break;
            }
            
            // Delete user
            $stmt = $db->prepare("DELETE FROM users WHERE username = ?");
            $result = $stmt->execute([$username]);
            
            if ($result) {
                echo json_encode(['success' => true, 'message' => 'User deleted successfully']);
            } else {
                echo json_encode(['error' => 'Failed to delete user']);
            }
            break;

        case 'settings':
            // Get current settings (placeholder)
            $settings = [
                'fast_charge_price' => 5,
                'normal_charge_price' => 3
            ];
            
            echo json_encode([
                'success' => true,
                'settings' => $settings
            ]);
            break;

        case 'save-settings':
            if ($method !== 'POST') {
                echo json_encode(['error' => 'Method not allowed']);
                break;
            }
            
            $fast_charge_price = $_POST['fast_charge_price'] ?? '';
            $normal_charge_price = $_POST['normal_charge_price'] ?? '';
            
            if (!$fast_charge_price || !$normal_charge_price) {
                echo json_encode(['error' => 'Both pricing values are required']);
                break;
            }
            
            // Save settings (placeholder - you might want to create a settings table)
            echo json_encode(['success' => true, 'message' => 'Settings saved successfully']);
            break;


        default:
            echo json_encode([
                'error' => 'Invalid action',
                'available_actions' => [
                    'dashboard', 'queue', 'bays', 'users', 'ticket-details',
                    'process-ticket', 'set-bay-maintenance', 'set-bay-available',
                    'add-user', 'delete-user', 'settings', 'save-settings'
                ]
            ]);
            break;
    }
} catch (Exception $e) {
    echo json_encode(['error' => 'Server error: ' . $e->getMessage()]);
}
?>
