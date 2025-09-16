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
            
            // Today's revenue
            $stmt = $db->query("SELECT SUM(amount) as revenue FROM payment_transactions WHERE DATE(processed_at) = CURDATE()");
            $revenue = $stmt->fetch(PDO::FETCH_ASSOC)['revenue'];
            $stats['revenue_today'] = $revenue ? (float)$revenue : 0;
            
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

        case 'business-settings':
            // Get current business settings from database
            try {
                $stmt = $db->prepare("SELECT setting_value FROM system_settings WHERE setting_key = ?");
                
                // Get minimum fee
                $stmt->execute(['minimum_fee']);
                $minFeeRow = $stmt->fetch(PDO::FETCH_ASSOC);
                $minFee = $minFeeRow ? floatval($minFeeRow['setting_value']) : 50.0;
                
                // Get rate per kWh
                $stmt->execute(['rate_per_kwh']);
                $rateRow = $stmt->fetch(PDO::FETCH_ASSOC);
                $ratePerKwh = $rateRow ? floatval($rateRow['setting_value']) : 15.0;
                
                $settings = [
                    'min_fee' => $minFee,
                    'kwh_per_peso' => $ratePerKwh
                ];
                
                echo json_encode([
                    'success' => true,
                    'settings' => $settings
                ]);
            } catch (Exception $e) {
                echo json_encode([
                    'success' => false,
                    'error' => 'Failed to load business settings: ' . $e->getMessage()
                ]);
            }
            break;

        case 'save-business-settings':
            if ($method !== 'POST') {
                echo json_encode(['error' => 'Method not allowed']);
                break;
            }

            $min_fee = $_POST['min_fee'] ?? '';
            $kwh_per_peso = $_POST['kwh_per_peso'] ?? '';

            if ($min_fee === '' || $kwh_per_peso === '') {
                echo json_encode(['error' => 'Both business values are required']);
                break;
            }

            // Validate values
            $min_fee = floatval($min_fee);
            $kwh_per_peso = floatval($kwh_per_peso);

            if ($min_fee < 0 || $kwh_per_peso <= 0) {
                echo json_encode(['error' => 'Invalid business values (min fee >= 0, kWh per peso > 0)']);
                break;
            }

            try {
                // Update minimum fee in system_settings table
                $stmt = $db->prepare("UPDATE system_settings SET setting_value = ? WHERE setting_key = 'minimum_fee'");
                $stmt->execute([$min_fee]);
                
                // Update rate per kWh in system_settings table
                $stmt = $db->prepare("UPDATE system_settings SET setting_value = ? WHERE setting_key = 'rate_per_kwh'");
                $stmt->execute([$kwh_per_peso]);
                
                echo json_encode(['success' => true, 'message' => 'Business settings updated successfully']);
            } catch (Exception $e) {
                echo json_encode(['error' => 'Failed to save business settings: ' . $e->getMessage()]);
            }
            break;

        case 'analytics':
            // Get range parameter (day, week, month)
            $range = $_GET['range'] ?? 'week';
            $interval = match($range) {
                'day' => 'INTERVAL 1 DAY',
                'week' => 'INTERVAL 7 DAY',
                'month' => 'INTERVAL 30 DAY',
                default => 'INTERVAL 7 DAY'
            };

            // Get revenue data
            $stmt = $db->query("
                SELECT
                    DATE(processed_at) as date,
                    SUM(amount) as revenue
                FROM payment_transactions
                WHERE processed_at >= DATE_SUB(CURDATE(), $interval)
                GROUP BY DATE(processed_at)
                ORDER BY DATE(processed_at)
            ");
            $revenue_data = $stmt->fetchAll(PDO::FETCH_ASSOC);

            // Get service usage data (charging sessions)
            $stmt = $db->query("
                SELECT
                    DATE(completed_at) as date,
                    COUNT(*) as service_count
                FROM charging_history
                WHERE completed_at >= DATE_SUB(CURDATE(), $interval)
                GROUP BY DATE(completed_at)
                ORDER BY DATE(completed_at)
            ");
            $service_data = $stmt->fetchAll(PDO::FETCH_ASSOC);

            echo json_encode([
                'success' => true,
                'revenue_data' => $revenue_data,
                'service_data' => $service_data
            ]);
            break;

        case 'debug-tables':
            // Debug endpoint to check table structure
            try {
                $tables_info = [];
                
                // Check payment_transactions table
                $stmt = $db->query("SHOW TABLES LIKE 'payment_transactions'");
                if ($stmt->rowCount() > 0) {
                    $columns = $db->query("DESCRIBE payment_transactions")->fetchAll(PDO::FETCH_ASSOC);
                    $tables_info['payment_transactions'] = $columns;
                }
                
                // Check charging_history table
                $stmt = $db->query("SHOW TABLES LIKE 'charging_history'");
                if ($stmt->rowCount() > 0) {
                    $columns = $db->query("DESCRIBE charging_history")->fetchAll(PDO::FETCH_ASSOC);
                    $tables_info['charging_history'] = $columns;
                }
                
                echo json_encode([
                    'success' => true,
                    'tables' => $tables_info
                ]);
            } catch (Exception $e) {
                echo json_encode([
                    'success' => false,
                    'error' => 'Debug error: ' . $e->getMessage()
                ]);
            }
            break;

        case 'transactions':
            // Get transaction history ONLY from charging_history table
            $type_filter = $_GET['type'] ?? '';
            $status_filter = $_GET['status'] ?? '';
            $date_from = $_GET['date_from'] ?? '';
            $date_to = $_GET['date_to'] ?? '';
            
            $transactions = [];
            $errors = [];
            
            try {
                // Check if charging_history table exists
                $tables_check = $db->query("SHOW TABLES LIKE 'charging_history'");
                $charging_table_exists = $tables_check->rowCount() > 0;
                
                if (!$charging_table_exists) {
                    echo json_encode([
                        'success' => false,
                        'error' => 'charging_history table not found in database'
                    ]);
                    break;
                }
                
                // Check if required columns exist, if not create them
                $columns_check = $db->query("SHOW COLUMNS FROM charging_history LIKE 'energy_used'");
                if ($columns_check->rowCount() == 0) {
                    $db->exec("ALTER TABLE charging_history ADD COLUMN energy_used DECIMAL(10,2) NOT NULL DEFAULT 0.0 COMMENT 'Energy consumed in kWh' AFTER charging_time_minutes");
                }
                
                $columns_check = $db->query("SHOW COLUMNS FROM charging_history LIKE 'reference_number'");
                if ($columns_check->rowCount() == 0) {
                    $db->exec("ALTER TABLE charging_history ADD COLUMN reference_number VARCHAR(50) DEFAULT NULL COMMENT 'Transaction reference number' AFTER total_amount");
                }
                
                // Build WHERE clause for filters
                $where_conditions = [];
                $params = [];
                
                if ($type_filter && $type_filter !== '') {
                    $where_conditions[] = "ch.service_type = ?";
                    $params[] = $type_filter;
                }
                
                if ($status_filter && $status_filter !== '') {
                    $where_conditions[] = "ch.status = ?";
                    $params[] = $status_filter;
                }
                
                if ($date_from && $date_from !== '') {
                    $where_conditions[] = "ch.completed_at >= ?";
                    $params[] = $date_from . ' 00:00:00';
                }
                
                if ($date_to && $date_to !== '') {
                    $where_conditions[] = "ch.completed_at <= ?";
                    $params[] = $date_to . ' 23:59:59';
                }
                
                $where_clause = '';
                if (!empty($where_conditions)) {
                    $where_clause = 'WHERE ' . implode(' AND ', $where_conditions);
                }
                
                // Get charging history ONLY from charging_history table - ONLY 6 COLUMNS
                $sql = "
                    SELECT 
                        ch.ticket_id,
                        ch.username,
                        ch.energy_used as energy_kwh,
                        ch.total_amount,
                        ch.reference_number,
                        ch.completed_at as transaction_date
                    FROM charging_history ch
                    $where_clause
                    ORDER BY ch.completed_at DESC
                    LIMIT 100
                ";
                
                $stmt = $db->prepare($sql);
                $stmt->execute($params);
                $transactions = $stmt->fetchAll(PDO::FETCH_ASSOC);
                
                // Update missing energy_used values
                $db->exec("UPDATE charging_history SET energy_used = ((100 - initial_battery_level) / 100.0) * 40.0 WHERE energy_used = 0.0 OR energy_used IS NULL");
                
                // Update missing reference_number values
                $db->exec("UPDATE charging_history SET reference_number = CONCAT('REF', ticket_id, '_', UNIX_TIMESTAMP(NOW())) WHERE reference_number IS NULL OR reference_number = '' OR reference_number = 'N/A'");
                
                // Sort by transaction date
                usort($transactions, function($a, $b) {
                    $dateA = $a['transaction_date'] ?? '1970-01-01';
                    $dateB = $b['transaction_date'] ?? '1970-01-01';
                    return strtotime($dateB) - strtotime($dateA);
                });
                
                // Limit to 100 total
                $transactions = array_slice($transactions, 0, 100);
                
                $response = [
                    'success' => true,
                    'data' => $transactions,
                    'transactions' => $transactions,
                    'count' => count($transactions)
                ];
                
                if (!empty($errors)) {
                    $response['warnings'] = $errors;
                }
                
                echo json_encode($response);
                
            } catch (Exception $e) {
                echo json_encode([
                    'success' => false,
                    'error' => 'Charging history error: ' . $e->getMessage()
                ]);
            }
            break;

        case 'progress-next-ticket':
            if ($method !== 'POST') {
                echo json_encode(['error' => 'Method not allowed']);
                break;
            }
            
            // Find the next waiting ticket
            $stmt = $db->query("
                SELECT ticket_id, username, service_type 
                FROM queue_tickets 
                WHERE status = 'Waiting' 
                ORDER BY created_at ASC 
                LIMIT 1
            ");
            $next_ticket = $stmt->fetch(PDO::FETCH_ASSOC);
            
            if ($next_ticket) {
                // Update ticket status to Processing
                $update_stmt = $db->prepare("UPDATE queue_tickets SET status = 'Processing' WHERE ticket_id = ?");
                $result = $update_stmt->execute([$next_ticket['ticket_id']]);
                
                if ($result) {
                    echo json_encode([
                        'success' => true,
                        'message' => 'Next ticket processed successfully',
                        'ticket' => $next_ticket
                    ]);
                } else {
                    echo json_encode(['error' => 'Failed to process next ticket']);
                }
            } else {
                echo json_encode(['error' => 'No waiting tickets found']);
            }
            break;


        default:
            echo json_encode([
                'error' => 'Invalid action',
                'available_actions' => [
                    'dashboard', 'queue', 'bays', 'users', 'ticket-details',
                    'process-ticket', 'set-bay-maintenance', 'set-bay-available',
                    'add-user', 'delete-user', 'settings', 'save-settings', 'analytics', 'transactions', 'progress-next-ticket'
                ]
            ]);
            break;
    }
} catch (Exception $e) {
    echo json_encode(['error' => 'Server error: ' . $e->getMessage()]);
}
?>
