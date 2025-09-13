<?php
// Complete Admin API with All Panel Functions
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

// Handle preflight requests
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

require_once '../config/database.php';

try {
    $db = new Database();
    $conn = $db->getConnection();
    
    if (!$conn) {
        throw new Exception('Database connection failed');
    }
    
    $action = $_GET['action'] ?? $_POST['action'] ?? '';
    
    switch ($action) {
        case 'dashboard':
            handleDashboard($conn);
            break;
        case 'queue':
            handleQueue($conn);
            break;
        case 'bays':
            handleBays($conn);
            break;
        case 'users':
            handleUsers($conn);
            break;
        case 'settings':
            handleSettings($conn);
            break;
        case 'process-ticket':
            handleProcessTicket($conn);
            break;
        case 'set-bay-maintenance':
            handleSetBayMaintenance($conn);
            break;
        case 'set-bay-available':
            handleSetBayAvailable($conn);
            break;
        case 'add-user':
            handleAddUser($conn);
            break;
        case 'save-settings':
            handleSaveSettings($conn);
            break;
        case 'ticket-details':
            handleTicketDetails($conn);
            break;
        default:
            sendResponse(false, 'Invalid action');
    }
    
} catch (Exception $e) {
    sendResponse(false, 'Server error: ' . $e->getMessage());
}

function handleDashboard($conn) {
    try {
        // Get total users
        $stmt = $conn->query("SELECT COUNT(*) as count FROM users");
        $totalUsers = $stmt->fetch(PDO::FETCH_ASSOC)['count'];
        
        // Get queue count
        $stmt = $conn->query("SELECT COUNT(*) as count FROM queue_tickets WHERE status IN ('Waiting', 'Processing')");
        $queueCount = $stmt->fetch(PDO::FETCH_ASSOC)['count'];
        
        // Get active bays
        $stmt = $conn->query("SELECT COUNT(*) as count FROM charging_bays WHERE status = 'Occupied'");
        $activeBays = $stmt->fetch(PDO::FETCH_ASSOC)['count'];
        
        // Get today's revenue
        $stmt = $conn->query("SELECT SUM(amount) as revenue FROM queue_tickets WHERE DATE(created_at) = CURDATE() AND payment_status = 'Paid'");
        $revenueToday = $stmt->fetch(PDO::FETCH_ASSOC)['revenue'] ?? 0;
        
        // Get recent activity
        $stmt = $conn->query("
            SELECT 
                'New ticket' as description,
                'ticket' as icon,
                created_at as time
            FROM queue_tickets 
            WHERE DATE(created_at) = CURDATE()
            ORDER BY created_at DESC 
            LIMIT 10
        ");
        $recentActivity = $stmt->fetchAll(PDO::FETCH_ASSOC);
        
        sendResponse(true, 'Dashboard data loaded', [
            'stats' => [
                'total_users' => $totalUsers,
                'queue_count' => $queueCount,
                'active_bays' => $activeBays,
                'revenue_today' => $revenueToday
            ],
            'recent_activity' => $recentActivity
        ]);
        
    } catch (Exception $e) {
        sendResponse(false, 'Failed to load dashboard data: ' . $e->getMessage());
    }
}

function handleQueue($conn) {
    try {
        $stmt = $conn->query("
            SELECT 
                ticket_id,
                username,
                service_type,
                status,
                payment_status,
                created_at
            FROM queue_tickets 
            ORDER BY created_at DESC
        ");
        $queue = $stmt->fetchAll(PDO::FETCH_ASSOC);
        
        sendResponse(true, 'Queue data loaded', ['queue' => $queue]);
        
    } catch (Exception $e) {
        sendResponse(false, 'Failed to load queue data: ' . $e->getMessage());
    }
}

function handleBays($conn) {
    try {
        // Ensure charging_bays table exists
        $conn->exec("
            CREATE TABLE IF NOT EXISTS charging_bays (
                bay_number INT PRIMARY KEY,
                bay_type VARCHAR(50) DEFAULT 'Fast Charge',
                status VARCHAR(20) DEFAULT 'Available',
                current_username VARCHAR(50),
                current_ticket_id VARCHAR(20),
                start_time TIMESTAMP NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        ");
        
        // Insert sample bays if they don't exist
        $stmt = $conn->query("SELECT COUNT(*) as count FROM charging_bays");
        $count = $stmt->fetch(PDO::FETCH_ASSOC)['count'];
        
        if ($count == 0) {
            for ($i = 1; $i <= 8; $i++) {
                $conn->exec("INSERT INTO charging_bays (bay_number, bay_type, status) VALUES ($i, 'Fast Charge', 'Available')");
            }
        }
        
        $stmt = $conn->query("
            SELECT 
                bay_number,
                bay_type,
                status,
                current_username,
                current_ticket_id,
                start_time
            FROM charging_bays 
            ORDER BY bay_number
        ");
        $bays = $stmt->fetchAll(PDO::FETCH_ASSOC);
        
        sendResponse(true, 'Bays data loaded', ['bays' => $bays]);
        
    } catch (Exception $e) {
        sendResponse(false, 'Failed to load bays data: ' . $e->getMessage());
    }
}

function handleUsers($conn) {
    try {
        $stmt = $conn->query("
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
        
        sendResponse(true, 'Users data loaded', ['users' => $users]);
        
    } catch (Exception $e) {
        sendResponse(false, 'Failed to load users data: ' . $e->getMessage());
    }
}

function handleSettings($conn) {
    try {
        // Ensure settings table exists
        $conn->exec("
            CREATE TABLE IF NOT EXISTS settings (
                id INT PRIMARY KEY AUTO_INCREMENT,
                setting_key VARCHAR(50) UNIQUE,
                setting_value VARCHAR(255),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        ");
        
        // Get settings
        $stmt = $conn->query("SELECT setting_key, setting_value FROM settings");
        $settings = [];
        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            $settings[$row['setting_key']] = $row['setting_value'];
        }
        
        // Set defaults if not exists
        if (!isset($settings['fast_charge_price'])) {
            $conn->exec("INSERT INTO settings (setting_key, setting_value) VALUES ('fast_charge_price', '50')");
            $settings['fast_charge_price'] = '50';
        }
        if (!isset($settings['normal_charge_price'])) {
            $conn->exec("INSERT INTO settings (setting_key, setting_value) VALUES ('normal_charge_price', '30')");
            $settings['normal_charge_price'] = '30';
        }
        
        sendResponse(true, 'Settings loaded', ['settings' => $settings]);
        
    } catch (Exception $e) {
        sendResponse(false, 'Failed to load settings: ' . $e->getMessage());
    }
}

function handleProcessTicket($conn) {
    try {
        $ticketId = $_POST['ticket_id'] ?? '';
        
        if (empty($ticketId)) {
            sendResponse(false, 'Ticket ID is required');
            return;
        }
        
        // Update ticket status
        $stmt = $conn->prepare("UPDATE queue_tickets SET status = 'Processing' WHERE ticket_id = ?");
        $stmt->execute([$ticketId]);
        
        if ($stmt->rowCount() > 0) {
            sendResponse(true, 'Ticket processed successfully');
        } else {
            sendResponse(false, 'Ticket not found');
        }
        
    } catch (Exception $e) {
        sendResponse(false, 'Failed to process ticket: ' . $e->getMessage());
    }
}

function handleSetBayMaintenance($conn) {
    try {
        $bayNumber = $_POST['bay_number'] ?? '';
        
        if (empty($bayNumber)) {
            sendResponse(false, 'Bay number is required');
            return;
        }
        
        $stmt = $conn->prepare("UPDATE charging_bays SET status = 'Maintenance', current_username = NULL, current_ticket_id = NULL, start_time = NULL WHERE bay_number = ?");
        $stmt->execute([$bayNumber]);
        
        if ($stmt->rowCount() > 0) {
            sendResponse(true, 'Bay set to maintenance mode');
        } else {
            sendResponse(false, 'Bay not found');
        }
        
    } catch (Exception $e) {
        sendResponse(false, 'Failed to set bay to maintenance: ' . $e->getMessage());
    }
}

function handleSetBayAvailable($conn) {
    try {
        $bayNumber = $_POST['bay_number'] ?? '';
        
        if (empty($bayNumber)) {
            sendResponse(false, 'Bay number is required');
            return;
        }
        
        $stmt = $conn->prepare("UPDATE charging_bays SET status = 'Available', current_username = NULL, current_ticket_id = NULL, start_time = NULL WHERE bay_number = ?");
        $stmt->execute([$bayNumber]);
        
        if ($stmt->rowCount() > 0) {
            sendResponse(true, 'Bay set to available');
        } else {
            sendResponse(false, 'Bay not found');
        }
        
    } catch (Exception $e) {
        sendResponse(false, 'Failed to set bay to available: ' . $e->getMessage());
    }
}

function handleAddUser($conn) {
    try {
        $username = $_POST['username'] ?? '';
        $firstname = $_POST['firstname'] ?? '';
        $lastname = $_POST['lastname'] ?? '';
        $email = $_POST['email'] ?? '';
        $password = $_POST['password'] ?? '';
        
        if (empty($username) || empty($firstname) || empty($lastname) || empty($email) || empty($password)) {
            sendResponse(false, 'All fields are required');
            return;
        }
        
        // Check if username or email already exists
        $stmt = $conn->prepare("SELECT COUNT(*) as count FROM users WHERE username = ? OR email = ?");
        $stmt->execute([$username, $email]);
        $count = $stmt->fetch(PDO::FETCH_ASSOC)['count'];
        
        if ($count > 0) {
            sendResponse(false, 'Username or email already exists');
            return;
        }
        
        $stmt = $conn->prepare("INSERT INTO users (username, firstname, lastname, email, password) VALUES (?, ?, ?, ?, ?)");
        $stmt->execute([$username, $firstname, $lastname, $email, $password]);
        
        sendResponse(true, 'User added successfully');
        
    } catch (Exception $e) {
        sendResponse(false, 'Failed to add user: ' . $e->getMessage());
    }
}

function handleSaveSettings($conn) {
    try {
        $fastPrice = $_POST['fast_charge_price'] ?? '';
        $normalPrice = $_POST['normal_charge_price'] ?? '';
        
        if (empty($fastPrice) || empty($normalPrice)) {
            sendResponse(false, 'Both prices are required');
            return;
        }
        
        // Update or insert fast charge price
        $stmt = $conn->prepare("INSERT INTO settings (setting_key, setting_value) VALUES ('fast_charge_price', ?) ON DUPLICATE KEY UPDATE setting_value = ?");
        $stmt->execute([$fastPrice, $fastPrice]);
        
        // Update or insert normal charge price
        $stmt = $conn->prepare("INSERT INTO settings (setting_key, setting_value) VALUES ('normal_charge_price', ?) ON DUPLICATE KEY UPDATE setting_value = ?");
        $stmt->execute([$normalPrice, $normalPrice]);
        
        sendResponse(true, 'Settings saved successfully');
        
    } catch (Exception $e) {
        sendResponse(false, 'Failed to save settings: ' . $e->getMessage());
    }
}

function handleTicketDetails($conn) {
    try {
        $ticketId = $_GET['ticket_id'] ?? '';
        
        if (empty($ticketId)) {
            sendResponse(false, 'Ticket ID is required');
            return;
        }
        
        $stmt = $conn->prepare("SELECT * FROM queue_tickets WHERE ticket_id = ?");
        $stmt->execute([$ticketId]);
        $ticket = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if ($ticket) {
            sendResponse(true, 'Ticket details loaded', ['ticket' => $ticket]);
        } else {
            sendResponse(false, 'Ticket not found');
        }
        
    } catch (Exception $e) {
        sendResponse(false, 'Failed to load ticket details: ' . $e->getMessage());
    }
}

function sendResponse($success, $message, $data = null) {
    $response = [
        'success' => $success,
        'message' => $message
    ];
    
    if ($data !== null) {
        $response = array_merge($response, $data);
    }
    
    echo json_encode($response);
    exit();
}
?>
