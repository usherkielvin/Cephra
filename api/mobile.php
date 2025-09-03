<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST');
header('Access-Control-Allow-Headers: Content-Type');

require_once '../config/database.php';

$db = (new Database())->getConnection();
if (!$db) {
    echo json_encode(['error' => 'Database connection failed']);
    exit;
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
        case 'login':
            if ($method === 'POST') {
                $username = $_POST['username'] ?? '';
                $password = $_POST['password'] ?? '';
                
                // Check credentials against your actual users table
                $stmt = $db->prepare("SELECT username FROM users WHERE username = ? AND password = ?");
                $stmt->execute([$username, $password]);
                
                if ($stmt->rowCount() > 0) {
                    echo json_encode(['success' => true, 'username' => $username]);
                } else {
                    echo json_encode(['error' => 'Invalid credentials']);
                }
            } else {
                echo json_encode(['error' => 'Method not allowed']);
            }
            break;
            
        case 'queue':
            if ($method === 'GET') {
                // Get tickets from your actual queue_tickets table
                $stmt = $db->query("SELECT ticket_id, username, service_type, status, payment_status FROM queue_tickets ORDER BY created_at DESC");
                $tickets = $stmt->fetchAll(PDO::FETCH_ASSOC);
                echo json_encode($tickets);
            } else {
                echo json_encode(['error' => 'Method not allowed']);
            }
            break;
            
        case 'create-ticket':
            if ($method === 'POST') {
                $data = json_decode(file_get_contents('php://input'), true);
                
                // Insert into your actual queue_tickets table
                $stmt = $db->prepare("INSERT INTO queue_tickets (ticket_id, username, service_type, status, payment_status, initial_battery_level) VALUES (?, ?, ?, ?, ?, ?)");
                
                $result = $stmt->execute([
                    $data['ticket_id'],
                    $data['username'],
                    $data['service_type'],
                    'Pending',
                    'Pending',
                    $data['initial_battery_level'] ?? 20
                ]);
                
                if ($result) {
                    echo json_encode(['success' => true, 'ticket_id' => $data['ticket_id']]);
                } else {
                    echo json_encode(['error' => 'Failed to create ticket']);
                }
            } else {
                echo json_encode(['error' => 'Method not allowed']);
            }
            break;
            
        default:
            // Show available actions
            echo json_encode([
                'message' => 'Cephra Database API',
                'available_actions' => ['login', 'queue', 'create-ticket'],
                'usage' => 'Add ?action=ACTION or POST action=ACTION'
            ]);
    }
} catch (Exception $e) {
    error_log("Cephra API Error: " . $e->getMessage());
    echo json_encode(['error' => $e->getMessage()]);
}
?>
