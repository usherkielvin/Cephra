<?php
// Mobile API for User Interface
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST");
header("Access-Control-Allow-Headers: Content-Type");

// Enable error reporting for debugging
error_reporting(E_ALL);
ini_set("display_errors", 1);

require_once "../config/database.php";

$db = (new Database())->getConnection();
if (!$db) {
    echo json_encode([
        "success" => false,
        "error" => "Database connection failed",
        "details" => "Check database configuration and XAMPP MySQL service"
    ]);
    exit();
}

$method = $_SERVER["REQUEST_METHOD"];

// Get action from POST data or query string
$action = "";
if ($method === "POST") {
    $action = $_POST["action"] ?? "";
} else {
    $action = $_GET["action"] ?? "";
}

try {
    switch ($action) {
        case "test":
            echo json_encode([
                "success" => true,
                "message" => "Mobile API is working",
                "timestamp" => date("Y-m-d H:i:s")
            ]);
            break;

        case "user-profile":
            // Get user profile data
            $username = $_GET["username"] ?? "";
            if (!$username) {
                echo json_encode([
                    "success" => false,
                    "error" => "Username required"
                ]);
                break;
            }
            
            $stmt = $db->prepare("
                SELECT 
                    username,
                    firstname,
                    lastname,
                    email,
                    created_at
                FROM users 
                WHERE username = ?
            ");
            $stmt->execute([$username]);
            $user = $stmt->fetch(PDO::FETCH_ASSOC);
            
            if ($user) {
                echo json_encode([
                    "success" => true,
                    "user" => $user
                ]);
            } else {
                echo json_encode([
                    "success" => false,
                    "error" => "User not found"
                ]);
            }
            break;

        case "user-history":
            // Get user charging history
            $username = $_GET["username"] ?? "";
            if (!$username) {
                echo json_encode([
                    "success" => false,
                    "error" => "Username required"
                ]);
                break;
            }
            
            $stmt = $db->prepare("
                SELECT 
                    ticket_id,
                    service_type,
                    status,
                    payment_status,
                    initial_battery_level,
                    created_at
                FROM queue_tickets 
                WHERE username = ?
                ORDER BY created_at DESC
            ");
            $stmt->execute([$username]);
            $history = $stmt->fetchAll(PDO::FETCH_ASSOC);
            
            echo json_encode([
                "success" => true,
                "history" => $history
            ]);
            break;

        case "available-bays":
            // Get available charging bays
            $stmt = $db->query("
                SELECT 
                    bay_number,
                    bay_type,
                    status
                FROM charging_bays 
                WHERE status = 'Available'
                ORDER BY bay_number
            ");
            $bays = $stmt->fetchAll(PDO::FETCH_ASSOC);
            
            echo json_encode([
                "success" => true,
                "bays" => $bays
            ]);
            break;

        case "create-ticket":
            if ($method !== "POST") {
                echo json_encode([
                    "success" => false,
                    "error" => "Method not allowed"
                ]);
                break;
            }
            
            $username = $_POST["username"] ?? "";
            $service_type = $_POST["service_type"] ?? "";
            $battery_level = $_POST["battery_level"] ?? 0;
            
            if (!$username || !$service_type) {
                echo json_encode([
                    "success" => false,
                    "error" => "Username and service type required"
                ]);
                break;
            }
            
            // Generate ticket ID
            $ticket_id = "TKT" . date("Ymd") . rand(1000, 9999);
            
            // Insert new ticket
            $stmt = $db->prepare("
                INSERT INTO queue_tickets (ticket_id, username, service_type, status, payment_status, initial_battery_level) 
                VALUES (?, ?, ?, 'Waiting', 'Pending', ?)
            ");
            $result = $stmt->execute([$ticket_id, $username, $service_type, $battery_level]);
            
            if ($result) {
                echo json_encode([
                    "success" => true,
                    "message" => "Ticket created successfully",
                    "ticket_id" => $ticket_id
                ]);
            } else {
                echo json_encode([
                    "success" => false,
                    "error" => "Failed to create ticket"
                ]);
            }
            break;

        case "register-user":
            if ($method !== "POST") {
                echo json_encode([
                    "success" => false,
                    "error" => "Method not allowed"
                ]);
                break;
            }
            
            $username = $_POST["username"] ?? "";
            $firstname = $_POST["firstname"] ?? "";
            $lastname = $_POST["lastname"] ?? "";
            $email = $_POST["email"] ?? "";
            $password = $_POST["password"] ?? "";
            
            if (!$username || !$firstname || !$lastname || !$email || !$password) {
                echo json_encode([
                    "success" => false,
                    "error" => "All fields are required"
                ]);
                break;
            }
            
            // Check if username already exists
            $stmt = $db->prepare("SELECT username FROM users WHERE username = ?");
            $stmt->execute([$username]);
            if ($stmt->fetch()) {
                echo json_encode([
                    "success" => false,
                    "error" => "Username already exists"
                ]);
                break;
            }
            
            // Insert new user
            $stmt = $db->prepare("
                INSERT INTO users (username, firstname, lastname, email, password) 
                VALUES (?, ?, ?, ?, ?)
            ");
            $result = $stmt->execute([$username, $firstname, $lastname, $email, $password]);
            
            if ($result) {
                echo json_encode([
                    "success" => true,
                    "message" => "User registered successfully"
                ]);
            } else {
                echo json_encode([
                    "success" => false,
                    "error" => "Failed to register user"
                ]);
            }
            break;

        default:
            echo json_encode([
                "success" => false,
                "error" => "Invalid action",
                "available_actions" => [
                    "test", "user-profile", "user-history", "available-bays", 
                    "create-ticket", "register-user"
                ]
            ]);
            break;
    }
} catch (Exception $e) {
    echo json_encode([
        "success" => false,
        "error" => "Server error: " . $e->getMessage()
    ]);
}
?>
