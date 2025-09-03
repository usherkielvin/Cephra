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
$request_uri = $_SERVER['REQUEST_URI'];

// Simple endpoint detection
if (strpos($request_uri, '/login') !== false) {
    $endpoint = 'login';
} elseif (strpos($request_uri, '/queue') !== false) {
    $endpoint = 'queue';
} elseif (strpos($request_uri, '/create-ticket') !== false) {
    $endpoint = 'create-ticket';
} else {
    $endpoint = 'unknown';
}

// Only show debug info if accessing the file directly
if ($endpoint === 'unknown') {
    echo json_encode([
        'endpoint' => $endpoint,
        'method' => $method,
        'uri' => $request_uri,
        'message' => 'Simple API working! Access with /login, /queue, or /create-ticket'
    ]);
    exit;
}

// Handle endpoints
switch ($endpoint) {
    case 'login':
        if ($method === 'POST') {
            $username = $_POST['username'] ?? '';
            $password = $_POST['password'] ?? '';
            
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
}
?>
