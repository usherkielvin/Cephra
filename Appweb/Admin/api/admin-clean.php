<?php
// Clean Admin API - No Authentication Required for Testing
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST");
header("Access-Control-Allow-Headers: Content-Type");

// Error reporting
error_reporting(E_ALL);
ini_set("display_errors", 1);

require_once __DIR__ . "/../config/database.php";

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
        case "dashboard":
            // Get dashboard statistics
            $stats = [];
            
            // Total users
            $stmt = $db->query("SELECT COUNT(*) as count FROM users");
            $stats["total_users"] = $stmt->fetch(PDO::FETCH_ASSOC)["count"];
            
            // Queue count
            $stmt = $db->query("SELECT COUNT(*) as count FROM queue_tickets WHERE status IN ('Waiting', 'Processing')");
            $stats["queue_count"] = $stmt->fetch(PDO::FETCH_ASSOC)["count"];
            
            // Active bays
            $stmt = $db->query("SELECT COUNT(*) as count FROM charging_bays WHERE status = 'Occupied'");
            $stats["active_bays"] = $stmt->fetch(PDO::FETCH_ASSOC)["count"];
            
            // Total revenue from payment_transactions table
            $stmt = $db->query("SELECT COALESCE(SUM(amount), 0) as total_revenue FROM payment_transactions");
            $stats["revenue_today"] = $stmt->fetch(PDO::FETCH_ASSOC)["total_revenue"];
            
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
                "success" => true,
                "stats" => $stats,
                "recent_activity" => $recent_activity
            ]);
            break;

        case "queue":
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
                "success" => true,
                "queue" => $queue
            ]);
            break;

        case "bays":
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
                "success" => true,
                "bays" => $bays
            ]);
            break;

        case "users":
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
                "success" => true,
                "users" => $users
            ]);
            break;

        case "set-bay-maintenance":
            $bay_number = $_POST["bay_number"] ?? "";
            if (!$bay_number) {
                echo json_encode(["success" => false, "message" => "Bay number required"]);
                break;
            }
            // Update charging_bays status to Maintenance
            $stmt = $db->prepare("UPDATE charging_bays SET status = 'Maintenance' WHERE bay_number = ?");
            $stmt->execute([$bay_number]);
            $rows_affected = $stmt->rowCount();
            if ($rows_affected == 0) {
                echo json_encode(["success" => false, "message" => "No bay found with that number"]);
            } else {
                echo json_encode(["success" => true]);
            }
            break;

        case "set-bay-available":
            $bay_number = $_POST["bay_number"] ?? "";
            if (!$bay_number) {
                echo json_encode(["success" => false, "message" => "Bay number required"]);
                break;
            }
            // Update charging_bays status to Available
            $stmt = $db->prepare("UPDATE charging_bays SET status = 'Available' WHERE bay_number = ?");
            $stmt->execute([$bay_number]);
            $rows_affected = $stmt->rowCount();
            if ($rows_affected == 0) {
                echo json_encode(["success" => false, "message" => "No bay found with that number"]);
            } else {
                echo json_encode(["success" => true]);
            }
            break;

        case "add-user":
            $username = $_POST["username"] ?? "";
            $firstname = $_POST["firstname"] ?? "";
            $lastname = $_POST["lastname"] ?? "";
            $email = $_POST["email"] ?? "";
            $password = $_POST["password"] ?? "";

            if (!$username || !$firstname || !$lastname || !$email || !$password) {
                echo json_encode(["success" => false, "message" => "All fields are required"]);
                break;
            }

            // Check if username already exists
            $stmt = $db->prepare("SELECT COUNT(*) as count FROM users WHERE username = ?");
            $stmt->execute([$username]);
            if ($stmt->fetch(PDO::FETCH_ASSOC)["count"] > 0) {
                echo json_encode(["success" => false, "message" => "Username already exists"]);
                break;
            }

            // Insert new user
            $stmt = $db->prepare("INSERT INTO users (username, firstname, lastname, email, password, created_at) VALUES (?, ?, ?, ?, ?, NOW())");
            $stmt->execute([$username, $firstname, $lastname, $email, $password]);

            if ($stmt->rowCount() > 0) {
                echo json_encode(["success" => true]);
            } else {
                echo json_encode(["success" => false, "message" => "Failed to add user"]);
            }
            break;

        case "delete-user":
            $username = $_POST["username"] ?? "";
            if (!$username) {
                echo json_encode(["success" => false, "message" => "Username is required"]);
                break;
            }
            // Delete user from users table
            $stmt = $db->prepare("DELETE FROM users WHERE username = ?");
            $stmt->execute([$username]);
            $rows_affected = $stmt->rowCount();
            if ($rows_affected == 0) {
                echo json_encode(["success" => false, "message" => "User not found"]);
            } else {
                echo json_encode(["success" => true]);
            }
            break;

        case "analytics":
            // Get range parameter (day, week, month)
            $range = $_GET["range"] ?? "week";
            $days = 7; // default to week

            switch ($range) {
                case "day":
                    $days = 1;
                    break;
                case "week":
                    $days = 7;
                    break;
                case "month":
                    $days = 30;
                    break;
            }

            try {
                // Get revenue data based on range
                $stmt = $db->prepare("
                    SELECT
                        DATE(processed_at) as date,
                        SUM(amount) as revenue
                    FROM payment_transactions
                    WHERE processed_at >= DATE_SUB(CURDATE(), INTERVAL ? DAY)
                    GROUP BY DATE(processed_at)
                    ORDER BY DATE(processed_at)
                ");
                $stmt->execute([$days]);
                $revenue_data = $stmt->fetchAll(PDO::FETCH_ASSOC);

                // Get service usage data from charging_history based on range
                $stmt = $db->prepare("
                    SELECT
                        DATE(completed_at) as date,
                        COUNT(*) as service_count
                    FROM charging_history
                    WHERE completed_at >= DATE_SUB(CURDATE(), INTERVAL ? DAY)
                    GROUP BY DATE(completed_at)
                    ORDER BY DATE(completed_at)
                ");
                $stmt->execute([$days]);
                $service_data = $stmt->fetchAll(PDO::FETCH_ASSOC);

                echo json_encode([
                    "success" => true,
                    "revenue_data" => $revenue_data,
                    "service_data" => $service_data
                ]);
            } catch (Exception $e) {
                echo json_encode([
                    "success" => false,
                    "error" => "Database error: " . $e->getMessage()
                ]);
            }
            break;

        case "ticket-details":
            $ticket_id = $_GET["ticket_id"] ?? "";
            if (!$ticket_id) {
                echo json_encode(["success" => false, "message" => "Ticket ID required"]);
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
                echo json_encode(["success" => true, "ticket" => $ticket]);
            } else {
                echo json_encode(["success" => false, "message" => "Ticket not found"]);
            }
            break;

        case "process-ticket":
            $ticket_id = $_POST["ticket_id"] ?? "";
            if (!$ticket_id) {
                echo json_encode(["success" => false, "message" => "Ticket ID required"]);
                break;
            }

            // Get current ticket status
            $stmt = $db->prepare("SELECT status FROM queue_tickets WHERE ticket_id = ?");
            $stmt->execute([$ticket_id]);
            $ticket = $stmt->fetch(PDO::FETCH_ASSOC);

            if (!$ticket) {
                echo json_encode(["success" => false, "message" => "Ticket not found"]);
                break;
            }

            $current_status = $ticket["status"];

            // Define the process cycle: Pending -> Waiting -> Charging -> Complete
            $process_cycle = [
                "Pending" => "Waiting",
                "Waiting" => "Charging",
                "Charging" => "Complete",
                "Complete" => "Complete" // Stay at Complete
            ];

            $new_status = $process_cycle[$current_status] ?? $current_status;

            // Update the status if it changed
            if ($new_status !== $current_status) {
                $stmt = $db->prepare("UPDATE queue_tickets SET status = ? WHERE ticket_id = ?");
                $stmt->execute([$new_status, $ticket_id]);
                echo json_encode(["success" => true, "new_status" => $new_status]);
            } else {
                echo json_encode(["success" => true, "message" => "Ticket is already at final process stage"]);
            }
            break;

        case "mark-as-paid":
            $ticket_id = $_POST["ticket_id"] ?? "";
            if (!$ticket_id) {
                echo json_encode(["success" => false, "message" => "Ticket ID required"]);
                break;
            }

            // Update status to Complete and payment_status to "Paid"
            $stmt = $db->prepare("UPDATE queue_tickets SET status = 'Complete', payment_status = 'Paid' WHERE ticket_id = ?");
            $stmt->execute([$ticket_id]);

            echo json_encode(["success" => true]);
            break;

        case "progress-to-waiting":
            $ticket_id = $_POST["ticket_id"] ?? "";
            if (!$ticket_id) {
                echo json_encode(["success" => false, "message" => "Ticket ID required"]);
                break;
            }

            $stmt = $db->prepare("UPDATE queue_tickets SET status = 'Waiting' WHERE ticket_id = ? AND status = 'Pending'");
            $stmt->execute([$ticket_id]);
            $rows_affected = $stmt->rowCount();

            if ($rows_affected > 0) {
                echo json_encode(["success" => true, "new_status" => "Waiting"]);
            } else {
                echo json_encode(["success" => false, "message" => "Ticket not found or not in Pending status"]);
            }
            break;

        case "progress-to-charging":
            $ticket_id = $_POST["ticket_id"] ?? "";
            if (!$ticket_id) {
                echo json_encode(["success" => false, "message" => "Ticket ID required"]);
                break;
            }

            // Get ticket info
            $stmt = $db->prepare("SELECT username, service_type FROM queue_tickets WHERE ticket_id = ? AND status = 'Waiting'");
            $stmt->execute([$ticket_id]);
            $ticket = $stmt->fetch(PDO::FETCH_ASSOC);

            if (!$ticket) {
                echo json_encode(["success" => false, "message" => "Ticket not found or not in Waiting status"]);
                break;
            }

            // Find available bay
            $stmt = $db->prepare("SELECT bay_number FROM charging_bays WHERE status = 'Available' ORDER BY bay_number LIMIT 1");
            $stmt->execute();
            $bay = $stmt->fetch(PDO::FETCH_ASSOC);

            if (!$bay) {
                echo json_encode(["success" => false, "message" => "No available bays"]);
                break;
            }

            try {
                $db->beginTransaction();

                // Update ticket to Charging
                $stmt = $db->prepare("UPDATE queue_tickets SET status = 'Charging' WHERE ticket_id = ?");
                $stmt->execute([$ticket_id]);

                // Update bay to Occupied
                $stmt = $db->prepare("UPDATE charging_bays SET status = 'Occupied', current_ticket_id = ?, current_username = ?, start_time = NOW() WHERE bay_number = ?");
                $stmt->execute([$ticket_id, $ticket["username"], $bay["bay_number"]]);

                $db->commit();
                echo json_encode(["success" => true, "new_status" => "Charging", "bay_number" => $bay["bay_number"]]);
            } catch (Exception $e) {
                $db->rollBack();
                echo json_encode(["success" => false, "message" => "Error assigning ticket to bay: " . $e->getMessage()]);
            }
            break;

        case "progress-to-complete":
            $ticket_id = $_POST["ticket_id"] ?? "";
            if (!$ticket_id) {
                echo json_encode(["success" => false, "message" => "Ticket ID required"]);
                break;
            }

            $stmt = $db->prepare("UPDATE queue_tickets SET status = 'Complete' WHERE ticket_id = ? AND status = 'Charging'");
            $stmt->execute([$ticket_id]);
            $rows_affected = $stmt->rowCount();

            if ($rows_affected > 0) {
                // Update bay to Available when ticket is completed
                $stmt = $db->prepare("UPDATE charging_bays SET status = 'Available', current_ticket_id = NULL, current_username = NULL, start_time = NULL WHERE current_ticket_id = ?");
                $stmt->execute([$ticket_id]);

                echo json_encode(["success" => true, "new_status" => "Complete"]);
            } else {
                echo json_encode(["success" => false, "message" => "Ticket not found or not in Charging status"]);
            }
            break;

        case "mark-payment-paid":
            $ticket_id = $_POST["ticket_id"] ?? "";
            if (!$ticket_id) {
                echo json_encode(["success" => false, "message" => "Ticket ID required"]);
                break;
            }

            // Get current ticket information
            $stmt = $db->prepare("SELECT status, payment_method FROM queue_tickets WHERE ticket_id = ?");
            $stmt->execute([$ticket_id]);
            $ticket = $stmt->fetch(PDO::FETCH_ASSOC);

            if (!$ticket) {
                echo json_encode(["success" => false, "message" => "Ticket not found"]);
                break;
            }

            // Check if charging is complete before allowing payment
            if ($ticket["status"] !== "Complete") {
                echo json_encode(["success" => false, "message" => "Cannot mark as paid until charging is complete. Current status: " . $ticket["status"]]);
                break;
            }

            // Check if payment method is Cash
            if ($ticket["payment_method"] !== "Cash") {
                echo json_encode(["success" => false, "message" => "This ticket is not marked for cash payment. Payment method: " . $ticket["payment_method"] . ". Only cash payments can be marked as paid by admin."]);
                break;
            }

            // Mark payment as paid and remove from queue (like Java version)
            $stmt = $db->prepare("UPDATE queue_tickets SET payment_status = 'Paid' WHERE ticket_id = ?");
            $stmt->execute([$ticket_id]);

            // Insert payment transaction record
            $stmt = $db->prepare("INSERT INTO payment_transactions (ticket_id, amount, processed_at) VALUES (?, 0, NOW())");
            $stmt->execute([$ticket_id]);

            // Remove ticket from queue (like Java version)
            $stmt = $db->prepare("DELETE FROM queue_tickets WHERE ticket_id = ?");
            $stmt->execute([$ticket_id]);

            echo json_encode(["success" => true, "message" => "Payment marked as paid and ticket removed from queue"]);
            break;

        case "progress-next-ticket":
            // Find the next Waiting ticket and assign it to an available bay (like Java nextNormalTicket/nextFastTicket)
            $stmt = $db->query("SELECT ticket_id, username, service_type FROM queue_tickets WHERE status = 'Waiting' ORDER BY created_at ASC LIMIT 1");
            $ticket = $stmt->fetch(PDO::FETCH_ASSOC);

            if (!$ticket) {
                echo json_encode(["success" => false, "message" => "No waiting tickets available to assign"]);
                break;
            }

            $ticket_id = $ticket["ticket_id"];
            $username = $ticket["username"];

            // Find available bay
            $stmt = $db->prepare("SELECT bay_number FROM charging_bays WHERE status = 'Available' ORDER BY bay_number LIMIT 1");
            $stmt->execute();
            $bay = $stmt->fetch(PDO::FETCH_ASSOC);

            if (!$bay) {
                echo json_encode(["success" => false, "message" => "No available bays"]);
                break;
            }

            try {
                $db->beginTransaction();

                // Update ticket to Charging
                $stmt = $db->prepare("UPDATE queue_tickets SET status = 'Charging' WHERE ticket_id = ?");
                $stmt->execute([$ticket_id]);

                // Update bay to Occupied
                $stmt = $db->prepare("UPDATE charging_bays SET status = 'Occupied', current_ticket_id = ?, current_username = ?, start_time = NOW() WHERE bay_number = ?");
                $stmt->execute([$ticket_id, $username, $bay["bay_number"]]);

                $db->commit();
                echo json_encode(["success" => true, "ticket_id" => $ticket_id, "bay_number" => $bay["bay_number"], "new_status" => "Charging"]);
            } catch (Exception $e) {
                $db->rollBack();
                echo json_encode(["success" => false, "message" => "Error assigning ticket to bay: " . $e->getMessage()]);
            }
            break;

        case "settings":
            // Get pricing settings
            $stmt = $db->query("SELECT fast_charge_price, normal_charge_price FROM pricing_settings ORDER BY id DESC LIMIT 1");
            $settings = $stmt->fetch(PDO::FETCH_ASSOC);

            if (!$settings) {
                $settings = ["fast_charge_price" => 50, "normal_charge_price" => 30];
            }

            echo json_encode(["success" => true, "settings" => $settings]);
            break;

        case "save-settings":
            $fast_price = $_POST["fast_charge_price"] ?? "";
            $normal_price = $_POST["normal_charge_price"] ?? "";

            if (!$fast_price || !$normal_price) {
                echo json_encode(["success" => false, "message" => "Both prices are required"]);
                break;
            }

            // Insert or update pricing settings
            $stmt = $db->prepare("INSERT INTO pricing_settings (fast_charge_price, normal_charge_price, updated_at) VALUES (?, ?, NOW()) ON DUPLICATE KEY UPDATE fast_charge_price = VALUES(fast_charge_price), normal_charge_price = VALUES(normal_charge_price), updated_at = NOW()");
            $stmt->execute([$fast_price, $normal_price]);

            echo json_encode(["success" => true]);
            break;

        default:
            echo json_encode([
                "success" => false,
                "error" => "Invalid action",
                "available_actions" => ["dashboard", "queue", "bays", "users", "add-user", "delete-user", "set-bay-maintenance", "set-bay-available", "analytics", "ticket-details", "process-ticket", "mark-as-paid", "progress-next-ticket", "settings", "save-settings"]
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
