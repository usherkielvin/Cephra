<?php
// Enable error reporting for debugging
error_reporting(E_ALL);
ini_set('display_errors', 1);

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST');
header('Access-Control-Allow-Headers: Content-Type');

require_once '../config/database.php';

// Debug: Log the request
error_log("API Request: " . $_SERVER['REQUEST_METHOD'] . " " . $_SERVER['REQUEST_URI']);

$db = (new Database())->getConnection();
if (!$db) {
    http_response_code(500);
    echo json_encode(['error' => 'Database connection failed']);
    exit;
}

$method = $_SERVER['REQUEST_METHOD'];

// Fix endpoint detection - parse the URL path properly
$request_uri = $_SERVER['REQUEST_URI'];
$path_parts = explode('/', trim($request_uri, '/'));
$endpoint = end($path_parts);

// Debug: Log the endpoint detection
error_log("Request URI: " . $request_uri);
error_log("Path parts: " . print_r($path_parts, true));
error_log("Detected endpoint: " . $endpoint);

try {
    switch ($endpoint) {
        case 'login':
            if ($method === 'POST') {
                $username = $_POST['username'] ?? '';
                $password = $_POST['password'] ?? '';
                
                error_log("Login attempt: username=$username, password=$password");
                
                // Check credentials against database
                $stmt = $db->prepare("SELECT username FROM users WHERE username = ? AND password = ?");
                $stmt->execute([$username, $password]);
                
                if ($stmt->rowCount() > 0) {
                    echo json_encode(['success' => true, 'username' => $username]);
                } else {
                    http_response_code(401);
                    echo json_encode(['error' => 'Invalid credentials']);
                }
            } else {
                http_response_code(405);
                echo json_encode(['error' => 'Method not allowed']);
            }
            break;
            
        case 'queue':
            if ($method === 'GET') {
                $stmt = $db->query("SELECT ticket_id, username, service_type, status, payment_status FROM queue_tickets ORDER BY created_at DESC");
                $tickets = $stmt->fetchAll(PDO::FETCH_ASSOC);
                echo json_encode($tickets);
            } else {
                http_response_code(405);
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
                http_response_code(405);
                echo json_encode(['error' => 'Method not allowed']);
            }
            break;
            
        default:
            http_response_code(404);
            echo json_encode(['error' => 'Endpoint not found: ' . $endpoint . ' (URI: ' . $request_uri . ')']);
    }
} catch (Exception $e) {
    error_log("API Error: " . $e->getMessage());
    http_response_code(500);
    echo json_encode(['error' => $e->getMessage()]);
}
?>
