<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST');
header('Access-Control-Allow-Headers: Content-Type');

// Simple admin API key check
if (!defined('ADMIN_API_KEY')) {
    $envKey = getenv('ADMIN_API_KEY');
    define('ADMIN_API_KEY', $envKey !== false ? $envKey : 'CHANGE_ME_ADMIN_KEY');
}

function _get_header($name) {
    $key = 'HTTP_' . strtoupper(str_replace('-', '_', $name));
    return $_SERVER[$key] ?? '';
}

function require_admin_key() {
    $headerKey = _get_header('X-API-KEY');
    $queryKey = $_GET['key'] ?? $_POST['key'] ?? '';
    $provided = $headerKey ?: $queryKey;
    if (!$provided || $provided !== ADMIN_API_KEY) {
        http_response_code(401);
        echo json_encode(['error' => 'Unauthorized']);
        exit;
    }
}

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
                    session_start();
                    $_SESSION['username'] = $username;
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

        case 'register':
            if ($method === 'POST') {
                $firstname = $_POST['firstname'] ?? '';
                $lastname = $_POST['lastname'] ?? '';
                $username = $_POST['username'] ?? '';
                $email = $_POST['email'] ?? '';
                $password = $_POST['password'] ?? '';

                if (!$firstname || !$lastname || !$username || !$email || !$password) {
                    echo json_encode(['error' => 'Missing required fields']);
                    exit;
                }

                // Check if username already exists
                $stmt = $db->prepare("SELECT id FROM users WHERE username = ?");
                $stmt->execute([$username]);
                if ($stmt->rowCount() > 0) {
                    echo json_encode(['error' => 'Username already exists']);
                    exit;
                }

                // Check if email already exists
                $stmt = $db->prepare("SELECT id FROM users WHERE email = ?");
                $stmt->execute([$email]);
                if ($stmt->rowCount() > 0) {
                    echo json_encode(['error' => 'Email already registered']);
                    exit;
                }

                // Insert new user
                $stmt = $db->prepare("INSERT INTO users (firstname, lastname, username, email, password) VALUES (?, ?, ?, ?, ?)");
                $result = $stmt->execute([$firstname, $lastname, $username, $email, $password]);

                if ($result) {
                    echo json_encode(['success' => true, 'message' => 'User registered successfully']);
                } else {
                    echo json_encode(['error' => 'Failed to register user']);
                }
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

        case 'monitor':
            // Public snapshot for web monitor (no admin key required)
            if ($method === 'GET') {
                // Bays snapshot
                $baysStmt = $db->query("SELECT bay_number, bay_type, status, current_ticket_id, current_username, start_time FROM charging_bays ORDER BY bay_number");
                $bays = $baysStmt->fetchAll(PDO::FETCH_ASSOC);

                // Waiting queue snapshot (top 20)
                $queueStmt = $db->query("SELECT ticket_id, username, service_type, status, payment_status, created_at FROM queue_tickets WHERE status='Waiting' ORDER BY created_at ASC LIMIT 20");
                $queue = $queueStmt->fetchAll(PDO::FETCH_ASSOC);

                echo json_encode(['bays' => $bays, 'queue' => $queue]);
            } else {
                echo json_encode(['error' => 'Method not allowed']);
            }
            break;

        default:
            // Show available actions
            echo json_encode([
                'message' => 'Cephra Database API',
                'available_actions' => ['login', 'register', 'queue', 'create-ticket', 'monitor', 'admin-list-bays', 'admin-list-queue', 'admin-set-bay', 'admin-close-ticket'],
                'usage' => 'Add ?action=ACTION or POST action=ACTION'
            ]);
            break;

        case 'admin-list-bays':
            require_admin_key();
            $stmt = $db->query("SELECT bay_number, bay_type, status, current_ticket_id, current_username, start_time, updated_at FROM charging_bays ORDER BY bay_number");
            echo json_encode($stmt->fetchAll(PDO::FETCH_ASSOC));
            break;

        case 'admin-list-queue':
            require_admin_key();
            $stmt = $db->query("SELECT ticket_id, username, service_type, status, payment_status, priority, created_at FROM queue_tickets ORDER BY created_at DESC");
            echo json_encode($stmt->fetchAll(PDO::FETCH_ASSOC));
            break;

        case 'admin-set-bay':
            require_admin_key();
            if ($method !== 'POST') { echo json_encode(['error' => 'Method not allowed']); break; }
            $bay = $_POST['bay_number'] ?? '';
            $status = $_POST['status'] ?? '';
            $ticket = $_POST['ticket_id'] ?? null;
            $username = $_POST['username'] ?? null;
            if (!$bay || !$status) { echo json_encode(['error' => 'Missing bay_number or status']); break; }
            if ($status === 'Occupied') {
                $stmt = $db->prepare("UPDATE charging_bays SET status='Occupied', current_ticket_id=?, current_username=?, start_time=COALESCE(start_time, CURRENT_TIMESTAMP), updated_at=CURRENT_TIMESTAMP WHERE bay_number=?");
                $ok = $stmt->execute([$ticket, $username, $bay]);
            } else {
                $stmt = $db->prepare("UPDATE charging_bays SET status='Available', current_ticket_id=NULL, current_username=NULL, start_time=NULL, updated_at=CURRENT_TIMESTAMP WHERE bay_number=?");
                $ok = $stmt->execute([$bay]);
            }
            echo json_encode(['success' => (bool)$ok]);
            break;

        case 'admin-close-ticket':
            require_admin_key();
            if ($method !== 'POST') { echo json_encode(['error' => 'Method not allowed']); break; }
            $ticketId = $_POST['ticket_id'] ?? '';
            $finalLevel = intval($_POST['final_battery_level'] ?? 100);
            $totalAmount = floatval($_POST['total_amount'] ?? 0);
            $reference = $_POST['reference_number'] ?? ('REF' . time());
            $servedBy = $_POST['served_by'] ?? 'Admin';
            if (!$ticketId) { echo json_encode(['error' => 'Missing ticket_id']); break; }

            $db->beginTransaction();
            try {
                $stmt = $db->prepare("SELECT username, service_type, initial_battery_level, bay_number FROM active_tickets WHERE ticket_id=? FOR UPDATE");
                $stmt->execute([$ticketId]);
                $row = $stmt->fetch(PDO::FETCH_ASSOC);
                if (!$row) { throw new Exception('Active ticket not found'); }

                $ins = $db->prepare("INSERT INTO charging_history (ticket_id, username, service_type, initial_battery_level, final_battery_level, charging_time_minutes, total_amount, reference_number, served_by) VALUES (?,?,?,?,?,?,?, ?,?)");
                $ins->execute([$ticketId, $row['username'], $row['service_type'], $row['initial_battery_level'], $finalLevel, 0, $totalAmount, $reference, $servedBy]);

                $del = $db->prepare("DELETE FROM active_tickets WHERE ticket_id=?");
                $del->execute([$ticketId]);

                if (!empty($row['bay_number'])) {
                    $upd = $db->prepare("UPDATE charging_bays SET status='Available', current_ticket_id=NULL, current_username=NULL, start_time=NULL, updated_at=CURRENT_TIMESTAMP WHERE bay_number=?");
                    $upd->execute([$row['bay_number']]);
                }

                $db->commit();
                echo json_encode(['success' => true]);
            } catch (Exception $ex) {
                $db->rollBack();
                echo json_encode(['error' => $ex->getMessage()]);
            }
            break;
    }
} catch (Exception $e) {
    error_log("Cephra API Error: " . $e->getMessage());
    echo json_encode(['error' => $e->getMessage()]);
}
?>
