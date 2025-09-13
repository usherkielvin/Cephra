<?php
// Test version of admin API without authentication
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST');
header('Access-Control-Allow-Headers: Content-Type');

// Enable error reporting for debugging
error_reporting(E_ALL);
ini_set('display_errors', 1);

echo "<!-- Admin API Test Version -->\n";

require_once 'config/database.php';

$db = (new Database())->getConnection();
if (!$db) {
    echo json_encode(['error' => 'Database connection failed', 'details' => 'Check database configuration']);
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

// Log the request for debugging
error_log("Admin API Test - Action: $action, Method: $method");

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
                'recent_activity' => $recent_activity,
                'debug' => 'Test version - no authentication required'
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
                'queue' => $queue,
                'debug' => 'Test version - no authentication required'
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
                'bays' => $bays,
                'debug' => 'Test version - no authentication required'
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
                'users' => $users,
                'debug' => 'Test version - no authentication required'
            ]);
            break;

        case 'test':
            // Simple test endpoint
            echo json_encode([
                'success' => true,
                'message' => 'Admin API test endpoint working',
                'timestamp' => date('Y-m-d H:i:s'),
                'method' => $method,
                'action' => $action
            ]);
            break;

        default:
            echo json_encode([
                'error' => 'Invalid action',
                'available_actions' => [
                    'dashboard', 'queue', 'bays', 'users', 'test'
                ],
                'debug' => 'Test version - no authentication required'
            ]);
            break;
    }
} catch (Exception $e) {
    echo json_encode([
        'error' => 'Server error: ' . $e->getMessage(),
        'debug' => 'Test version - no authentication required'
    ]);
}
?>
