<?php
// Web Notification API for User Interface
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST");
header("Access-Control-Allow-Headers: Content-Type");

require_once "../config/database.php";

$db = (new Database())->getConnection();
if (!$db) {
    echo json_encode([
        "success" => false,
        "error" => "Database connection failed"
    ]);
    exit();
}

$method = $_SERVER["REQUEST_METHOD"];
$action = $_POST["action"] ?? $_GET["action"] ?? "";

try {
    switch ($action) {
        case "check-notifications":
            // Check for new notifications for the current user
            session_start();
            $username = $_SESSION['username'] ?? '';
            
            if (!$username) {
                echo json_encode([
                    "success" => false,
                    "error" => "User not logged in"
                ]);
                break;
            }
            
            // Get unread notifications
            $stmt = $db->prepare("
                SELECT id, notification_type, ticket_id, bay_number, message, created_at 
                FROM user_notifications 
                WHERE username = ? AND is_read = 0 
                ORDER BY created_at DESC 
                LIMIT 10
            ");
            $stmt->execute([$username]);
            $notifications = $stmt->fetchAll(PDO::FETCH_ASSOC);
            
            echo json_encode([
                "success" => true,
                "notifications" => $notifications
            ]);
            break;
            
        case "mark-read":
            // Mark notification as read
            $notificationId = $_POST["notification_id"] ?? '';
            if (!$notificationId) {
                echo json_encode([
                    "success" => false,
                    "error" => "Notification ID required"
                ]);
                break;
            }
            
            $stmt = $db->prepare("UPDATE user_notifications SET is_read = 1 WHERE id = ?");
            $result = $stmt->execute([$notificationId]);
            
            echo json_encode([
                "success" => $result,
                "message" => $result ? "Notification marked as read" : "Failed to mark notification as read"
            ]);
            break;
            
        case "create-notification":
            // Create a new notification (called by admin)
            $username = $_POST["username"] ?? '';
            $notificationType = $_POST["notification_type"] ?? '';
            $ticketId = $_POST["ticket_id"] ?? '';
            $bayNumber = $_POST["bay_number"] ?? '';
            $message = $_POST["message"] ?? '';
            
            if (!$username || !$notificationType) {
                echo json_encode([
                    "success" => false,
                    "error" => "Username and notification type required"
                ]);
                break;
            }
            
            $stmt = $db->prepare("
                INSERT INTO user_notifications (username, notification_type, ticket_id, bay_number, message, created_at, is_read) 
                VALUES (?, ?, ?, ?, ?, NOW(), 0)
            ");
            $result = $stmt->execute([$username, $notificationType, $ticketId, $bayNumber, $message]);
            
            echo json_encode([
                "success" => $result,
                "message" => $result ? "Notification created successfully" : "Failed to create notification"
            ]);
            break;
            
        default:
            echo json_encode([
                "success" => false,
                "error" => "Invalid action",
                "available_actions" => ["check-notifications", "mark-read", "create-notification"]
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
