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
            
            // Overall revenue (all time)
            $stmt = $db->query("SELECT SUM(amount) as revenue FROM payment_transactions");
            $revenue = $stmt->fetch(PDO::FETCH_ASSOC)['revenue'];
            $stats['revenue_today'] = $revenue ? (float)$revenue : 0;
            
            // Today's revenue for comparison
            $stmt = $db->query("SELECT SUM(amount) as revenue FROM payment_transactions WHERE DATE(processed_at) = CURDATE()");
            $today_revenue = $stmt->fetch(PDO::FETCH_ASSOC)['revenue'];
            $stats['revenue_today_only'] = $today_revenue ? (float)$today_revenue : 0;
            
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

        case 'progress-to-waiting':
            if ($method !== 'POST') {
                echo json_encode(['error' => 'Method not allowed']);
                break;
            }
            
            $ticket_id = $_POST['ticket_id'] ?? '';
            if (!$ticket_id) {
                echo json_encode(['error' => 'Ticket ID required']);
                break;
            }
            
            // Update ticket status to Waiting
            $stmt = $db->prepare("UPDATE queue_tickets SET status = 'Waiting' WHERE ticket_id = ?");
            $result = $stmt->execute([$ticket_id]);
            
            if ($result) {
                echo json_encode(['success' => true, 'message' => 'Ticket moved to waiting status']);
            } else {
                echo json_encode(['error' => 'Failed to move ticket to waiting']);
            }
            break;

        case 'progress-to-charging':
            if ($method !== 'POST') {
                echo json_encode(['error' => 'Method not allowed']);
                break;
            }
            
            $ticket_id = $_POST['ticket_id'] ?? '';
            if (!$ticket_id) {
                echo json_encode(['error' => 'Ticket ID required']);
                break;
            }
            
            // Log the attempt
            error_log("Admin API: Attempting to progress ticket {$ticket_id} to charging");
            
            // Get ticket service type to determine bay type
            $service_stmt = $db->prepare("SELECT service_type FROM queue_tickets WHERE ticket_id = ?");
            $service_stmt->execute([$ticket_id]);
            $ticket_data = $service_stmt->fetch(PDO::FETCH_ASSOC);
            
            if (!$ticket_data) {
                error_log("Admin API: Ticket {$ticket_id} not found in database");
                echo json_encode(['error' => 'Ticket not found']);
                break;
            }
            
            $service_type = $ticket_data['service_type'] ?? '';
            $is_fast_charging = (stripos($service_type, 'fast') !== false) || (stripos($ticket_id, 'FCH') !== false);
            
            error_log("Admin API: Ticket {$ticket_id} - Service: {$service_type}, Is Fast: " . ($is_fast_charging ? 'Yes' : 'No'));
            
            // First, let's check what bays are actually available
            $debug_stmt = $db->query("SELECT bay_number, status FROM charging_bays ORDER BY CAST(bay_number AS UNSIGNED) ASC");
            $all_bays = $debug_stmt->fetchAll(PDO::FETCH_ASSOC);
            error_log("Admin API: All bays: " . json_encode($all_bays));
            
            // Ensure Bay-3 exists and is properly configured
            $bay3_check = $db->query("SELECT COUNT(*) as count FROM charging_bays WHERE bay_number = 'Bay-3'");
            $bay3_exists = $bay3_check->fetch(PDO::FETCH_ASSOC)['count'];
            
            if ($bay3_exists == 0) {
                error_log("Admin API: Bay-3 does not exist - creating it");
                $create_bay3 = $db->prepare("INSERT INTO charging_bays (bay_number, bay_type, status, current_ticket_id, current_username, start_time) VALUES ('Bay-3', 'Fast', 'Available', NULL, NULL, NULL)");
                $create_result = $create_bay3->execute();
                if ($create_result) {
                    error_log("Admin API: Successfully created Bay-3");
                } else {
                    error_log("Admin API: Failed to create Bay-3");
                }
            }
            
            // Find appropriate bay based on service type
            if ($is_fast_charging) {
                // Fast charging: only use bays 1-3
                error_log("Admin API: Searching for FCH bays 1-3 with status 'AVAILABLE'");
                
                // Database uses 'Bay-1', 'Bay-2', 'Bay-3' format
                $bay_stmt = $db->query("
                    SELECT bay_number 
                    FROM charging_bays 
                    WHERE TRIM(UPPER(status)) = 'AVAILABLE' 
                    AND bay_type = 'Fast'
                    ORDER BY bay_number ASC 
                    LIMIT 1
                ");
                
                // Debug: Check what bays 1-3 look like specifically
                $debug_fast_stmt = $db->query("
                    SELECT bay_number, status, TRIM(UPPER(status)) as trimmed_status, CAST(bay_number AS UNSIGNED) as bay_num
                    FROM charging_bays 
                    WHERE CAST(bay_number AS UNSIGNED) BETWEEN 1 AND 3
                    ORDER BY CAST(bay_number AS UNSIGNED) ASC
                ");
                $fast_bays = $debug_fast_stmt->fetchAll(PDO::FETCH_ASSOC);
                error_log("Admin API: Fast bays 1-3 details: " . json_encode($fast_bays));
                
                // Test the exact query that should find Bay-3
                $test_stmt = $db->query("
                    SELECT bay_number, status, bay_type,
                           CASE WHEN TRIM(UPPER(status)) = 'AVAILABLE' THEN 'MATCH' ELSE 'NO_MATCH' END as status_check,
                           CASE WHEN bay_type = 'Fast' THEN 'FAST_TYPE' ELSE 'NORMAL_TYPE' END as type_check
                    FROM charging_bays 
                    WHERE bay_number = 'Bay-3'
                ");
                $bay3_test = $test_stmt->fetch(PDO::FETCH_ASSOC);
                error_log("Admin API: Bay-3 test: " . json_encode($bay3_test));
                
                // Force Bay-3 to be available for FCH tickets
                error_log("Admin API: Ensuring Bay-3 is available for FCH tickets");
                $force_bay3 = $db->prepare("UPDATE charging_bays SET status = 'Available', current_ticket_id = NULL, current_username = NULL, start_time = NULL WHERE bay_number = 'Bay-3'");
                $force_result = $force_bay3->execute();
                if ($force_result) {
                    error_log("Admin API: Successfully forced Bay-3 to Available status");
                }
                
                // Retry the bay search after ensuring Bay-3 is available
                $bay_stmt = $db->query("
                    SELECT bay_number 
                    FROM charging_bays 
                    WHERE TRIM(UPPER(status)) = 'AVAILABLE' 
                    AND bay_type = 'Fast'
                    ORDER BY bay_number ASC 
                    LIMIT 1
                ");
                $available_bay = $bay_stmt->fetch(PDO::FETCH_ASSOC);
                error_log("Admin API: After Bay-3 fix - Bay search result: " . json_encode($available_bay));
                
            } else {
                // Normal charging: only use bays 4-8
                error_log("Admin API: Searching for NCH bays 4-8 with status 'AVAILABLE'");
                $bay_stmt = $db->query("
                    SELECT bay_number 
                    FROM charging_bays 
                    WHERE TRIM(UPPER(status)) = 'AVAILABLE' 
                    AND bay_type = 'Normal'
                    ORDER BY bay_number ASC 
                    LIMIT 1
                ");
            }
            
            $available_bay = $bay_stmt->fetch(PDO::FETCH_ASSOC);
            error_log("Admin API: Query result for {$bay_type} bays: " . json_encode($available_bay));
            
            if (!$available_bay) {
                $bay_type = $is_fast_charging ? 'fast charging' : 'normal charging';
                error_log("Admin API: No available {$bay_type} bays for ticket {$ticket_id}");
                
                // Try alternative status values
                $alt_statuses = ['Available', 'available', 'AVAILABLE', ' Available ', ' available '];
                foreach ($alt_statuses as $alt_status) {
                    if ($is_fast_charging) {
                        $alt_stmt = $db->query("
                            SELECT bay_number 
                            FROM charging_bays 
                            WHERE status = '{$alt_status}' 
                            AND bay_type = 'Fast'
                            ORDER BY bay_number ASC 
                            LIMIT 1
                        ");
                    } else {
                        $alt_stmt = $db->query("
                            SELECT bay_number 
                            FROM charging_bays 
                            WHERE status = '{$alt_status}' 
                            AND bay_type = 'Normal'
                            ORDER BY bay_number ASC 
                            LIMIT 1
                        ");
                    }
                    
                    $alt_bay = $alt_stmt->fetch(PDO::FETCH_ASSOC);
                    if ($alt_bay) {
                        error_log("Admin API: Found bay {$alt_bay['bay_number']} with status '{$alt_status}'");
                        $available_bay = $alt_bay;
                        break;
                    }
                }
                
                if (!$available_bay) {
                    // Check if charging_bays table has any data at all
                    $check_stmt = $db->query("SELECT COUNT(*) as count FROM charging_bays");
                    $bay_count = $check_stmt->fetch(PDO::FETCH_ASSOC)['count'];
                    
                    if ($bay_count == 0) {
                        error_log("Admin API: charging_bays table is empty!");
                        echo json_encode(['error' => 'Charging bays table is empty. Please initialize the database.']);
                    } else {
                        echo json_encode(['error' => "No available {$bay_type} bays. Please wait for a {$bay_type} bay to become available."]);
                    }
                    break;
                }
            }
            
            $bay_number = $available_bay['bay_number'];
            error_log("Admin API: Found available bay {$bay_number} for ticket {$ticket_id}");
            
            // Validate bay assignment is correct for service type
            if ($is_fast_charging && !in_array($bay_number, ['Bay-1', 'Bay-2', 'Bay-3'])) {
                error_log("Admin API: ERROR - Fast charging ticket {$ticket_id} assigned to invalid bay {$bay_number} (should be Bay-1, Bay-2, or Bay-3)");
                echo json_encode(['error' => "Invalid bay assignment: Fast charging ticket assigned to bay {$bay_number}"]);
                break;
            } elseif (!$is_fast_charging && !in_array($bay_number, ['Bay-4', 'Bay-5', 'Bay-6', 'Bay-7', 'Bay-8'])) {
                error_log("Admin API: ERROR - Normal charging ticket {$ticket_id} assigned to invalid bay {$bay_number} (should be Bay-4 to Bay-8)");
                echo json_encode(['error' => "Invalid bay assignment: Normal charging ticket assigned to bay {$bay_number}"]);
                break;
            }
            
            // Start transaction
            $db->beginTransaction();
            
            try {
                // Update ticket status to Charging
                $stmt = $db->prepare("UPDATE queue_tickets SET status = 'Charging' WHERE ticket_id = ?");
                $result = $stmt->execute([$ticket_id]);
                
                if (!$result) {
                    throw new Exception('Failed to update ticket status');
                }
                
                // Get ticket details for username
                $ticket_stmt = $db->prepare("SELECT username FROM queue_tickets WHERE ticket_id = ?");
                $ticket_stmt->execute([$ticket_id]);
                $ticket_data = $ticket_stmt->fetch(PDO::FETCH_ASSOC);
                
                if (!$ticket_data) {
                    throw new Exception('Ticket not found');
                }
                
                // Assign bay to ticket
                $bay_stmt = $db->prepare("UPDATE charging_bays SET status = 'Occupied', current_ticket_id = ?, current_username = ?, start_time = NOW() WHERE bay_number = ?");
                $bay_result = $bay_stmt->execute([$ticket_id, $ticket_data['username'], $bay_number]);
                
                if (!$bay_result) {
                    throw new Exception('Failed to assign bay');
                }
                
                // Verify the bay assignment was successful
                $verify_stmt = $db->prepare("SELECT bay_number, status, current_ticket_id, current_username FROM charging_bays WHERE bay_number = ?");
                $verify_stmt->execute([$bay_number]);
                $verify_result = $verify_stmt->fetch(PDO::FETCH_ASSOC);
                error_log("Admin API: Bay {$bay_number} verification: " . json_encode($verify_result));
                
                // Verify the ticket status was updated
                $ticket_verify_stmt = $db->prepare("SELECT ticket_id, status FROM queue_tickets WHERE ticket_id = ?");
                $ticket_verify_stmt->execute([$ticket_id]);
                $ticket_verify_result = $ticket_verify_stmt->fetch(PDO::FETCH_ASSOC);
                error_log("Admin API: Ticket {$ticket_id} verification: " . json_encode($ticket_verify_result));
                
                $db->commit();
                
                error_log("Admin API: Successfully assigned ticket {$ticket_id} to bay {$bay_number}");
                
                echo json_encode([
                    'success' => true, 
                    'message' => 'Ticket assigned to charging bay',
                    'bay_number' => $bay_number,
                    'ticket_status' => 'Charging',
                    'bay_status' => 'Occupied'
                ]);
                
            } catch (Exception $e) {
                $db->rollback();
                error_log("Admin API: Error assigning ticket {$ticket_id} to bay: " . $e->getMessage());
                echo json_encode(['error' => 'Failed to assign ticket to bay: ' . $e->getMessage()]);
            }
            break;

        case 'progress-to-complete':
            if ($method !== 'POST') {
                echo json_encode(['error' => 'Method not allowed']);
                break;
            }
            
            $ticket_id = $_POST['ticket_id'] ?? '';
            if (!$ticket_id) {
                echo json_encode(['error' => 'Ticket ID required']);
                break;
            }
            
            // Start transaction
            $db->beginTransaction();
            
            try {
                // Get ticket details
                $ticket_stmt = $db->prepare("SELECT * FROM queue_tickets WHERE ticket_id = ?");
                $ticket_stmt->execute([$ticket_id]);
                $ticket = $ticket_stmt->fetch(PDO::FETCH_ASSOC);
                
                if (!$ticket) {
                    throw new Exception('Ticket not found');
                }
                
                // Use existing reference number from Java admin if available, otherwise generate new one
                $reference_number = $ticket['reference_number'] ?? '';
                if (empty($reference_number)) {
                    // Generate reference number (8-digit random number like Java admin)
                    $reference_number = str_pad(mt_rand(10000000, 99999999), 8, '0', STR_PAD_LEFT);
                }
                
                // Calculate payment amount using Java admin billing system
                $ratePerKwh = 15.0; // ₱15.00 per kWh (configurable from database)
                $minimumFee = 50.0; // ₱50.00 minimum fee (configurable from database)
                $fastMultiplier = 1.25; // 1.25x multiplier for fast charging (configurable from database)
                $batteryCapacityKwh = 40.0; // 40kWh battery capacity
                
                // Calculate energy used based on battery levels (like Java admin)
                $initialBatteryLevel = $ticket['initial_battery_level'] ?? 50;
                $usedFraction = (100.0 - $initialBatteryLevel) / 100.0;
                $energyUsed = $usedFraction * $batteryCapacityKwh;
                
                // Determine service multiplier
                $multiplier = 1.0; // Default for normal charging
                if (stripos($ticket['service_type'], 'fast') !== false) {
                    $multiplier = $fastMultiplier; // Apply fast charging premium
                }
                
                // Calculate gross amount (like Java admin)
                $grossAmount = $energyUsed * $ratePerKwh * $multiplier;
                $amount = max($grossAmount, $minimumFee * $multiplier); // Apply minimum fee with multiplier
                
                // Get the bay number from charging_bays table BEFORE freeing it
                $bay_stmt = $db->prepare("SELECT bay_number FROM charging_bays WHERE current_ticket_id = ?");
                $bay_stmt->execute([$ticket_id]);
                $bay_data = $bay_stmt->fetch(PDO::FETCH_ASSOC);
                $bay_number = $bay_data ? $bay_data['bay_number'] : 0;
                
                // Update ticket status to Complete, set payment to Pending, and save reference number
                $stmt = $db->prepare("UPDATE queue_tickets SET status = 'Complete', payment_status = 'Pending', reference_number = ? WHERE ticket_id = ?");
                $result = $stmt->execute([$reference_number, $ticket_id]);
                
                if (!$result) {
                    throw new Exception('Failed to update ticket status');
                }
                
                // Free up the bay
                $bay_stmt = $db->prepare("UPDATE charging_bays SET status = 'Available', current_ticket_id = NULL, current_username = NULL, start_time = NULL WHERE current_ticket_id = ?");
                $bay_result = $bay_stmt->execute([$ticket_id]);
                
                if (!$bay_result) {
                    throw new Exception('Failed to free bay');
                }
                
                // Add to charging history
                try {
                    $history_stmt = $db->prepare("
                        INSERT INTO charging_history (
                            ticket_id, username, service_type, initial_battery_level,
                            final_battery_level, charging_time_minutes, energy_used, 
                            total_amount, reference_number, completed_at
                        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())
                    ");
                    // Reference number already determined above
                    $history_result = $history_stmt->execute([
                        $ticket_id, 
                        $ticket['username'], 
                        $ticket['service_type'], 
                        $initialBatteryLevel,
                        100, // final_battery_level (always 100% when completed)
                        0, // charging_time_minutes (not calculated yet)
                        $energyUsed, // energy_used (calculated from battery levels)
                        $amount, // total_amount (calculated using Java admin billing system)
                        $reference_number
                    ]);
                    
                    if (!$history_result) {
                        throw new Exception('Failed to add to charging history');
                    }
                } catch (Exception $history_error) {
                    // Log the error but don't fail the transaction
                    error_log("Charging history insertion failed for ticket $ticket_id: " . $history_error->getMessage());
                    // Continue with the transaction - bay is already freed and ticket marked complete
                }
                
                $db->commit();
                
                echo json_encode([
                    'success' => true, 
                    'message' => 'Ticket marked as complete',
                    'reference_number' => $reference_number
                ]);
                
            } catch (Exception $e) {
                $db->rollback();
                echo json_encode(['error' => 'Failed to complete ticket: ' . $e->getMessage()]);
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
                
                // Check charging_bays table
                $stmt = $db->query("SHOW TABLES LIKE 'charging_bays'");
                if ($stmt->rowCount() > 0) {
                    $columns = $db->query("DESCRIBE charging_bays")->fetchAll(PDO::FETCH_ASSOC);
                    $tables_info['charging_bays'] = $columns;
                    
                    // Get sample data
                    $sample_data = $db->query("SELECT * FROM charging_bays LIMIT 3")->fetchAll(PDO::FETCH_ASSOC);
                    $tables_info['charging_bays_sample'] = $sample_data;
                }
                
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
                    
                    // Get sample data with specific column order
                    $history_data = $db->query("SELECT ticket_id, username, service_type, initial_battery_level, charging_time_minutes, energy_used, total_amount, reference_number, completed_at FROM charging_history ORDER BY completed_at DESC LIMIT 3")->fetchAll(PDO::FETCH_ASSOC);
                    $tables_info['charging_history_sample'] = $history_data;
                    
                    // Get numeric indexed data to debug column order
                    $history_numeric = $db->query("SELECT ticket_id, username, service_type, initial_battery_level, charging_time_minutes, energy_used, total_amount, reference_number, completed_at FROM charging_history ORDER BY completed_at DESC LIMIT 3")->fetchAll(PDO::FETCH_NUM);
                    $tables_info['charging_history_numeric'] = $history_numeric;
                }
                
                // Check queue_tickets table
                $stmt = $db->query("SHOW TABLES LIKE 'queue_tickets'");
                if ($stmt->rowCount() > 0) {
                    $columns = $db->query("DESCRIBE queue_tickets")->fetchAll(PDO::FETCH_ASSOC);
                    $tables_info['queue_tickets'] = $columns;
                    
                    // Get current tickets
                    $tickets_data = $db->query("SELECT * FROM queue_tickets ORDER BY created_at DESC LIMIT 5")->fetchAll(PDO::FETCH_ASSOC);
                    $tables_info['queue_tickets_sample'] = $tickets_data;
                }
                
                // Check users table
                $stmt = $db->query("SHOW TABLES LIKE 'users'");
                if ($stmt->rowCount() > 0) {
                    $columns = $db->query("DESCRIBE users")->fetchAll(PDO::FETCH_ASSOC);
                    $tables_info['users'] = $columns;
                    
                    // Get current users
                    $users_data = $db->query("SELECT username, firstname, lastname FROM users LIMIT 5")->fetchAll(PDO::FETCH_ASSOC);
                    $tables_info['users_sample'] = $users_data;
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

        case 'mark-payment-paid':
            if ($method !== 'POST') {
                echo json_encode(['error' => 'Method not allowed']);
                break;
            }
            
            $ticket_id = $_POST['ticket_id'] ?? '';
            if (!$ticket_id) {
                echo json_encode(['error' => 'Ticket ID required']);
                break;
            }
            
            // Debug: Log the request
            error_log("Payment Debug - Starting payment for ticket: $ticket_id");
            
            // Start transaction
            $db->beginTransaction();
            
            try {
                // Get ticket details
                $ticket_stmt = $db->prepare("SELECT * FROM queue_tickets WHERE ticket_id = ?");
                $ticket_stmt->execute([$ticket_id]);
                $ticket = $ticket_stmt->fetch(PDO::FETCH_ASSOC);
                
                if (!$ticket) {
                    throw new Exception('Ticket not found');
                }
                
                // Debug: Log ticket data
                error_log("Payment Debug - Ticket Data: " . json_encode($ticket));
                
                // Use existing reference number from Java admin if available, otherwise generate new one
                $reference_number = $ticket['reference_number'] ?? '';
                if (empty($reference_number)) {
                    // Generate reference number (8-digit random number like Java admin)
                    $reference_number = str_pad(mt_rand(10000000, 99999999), 8, '0', STR_PAD_LEFT);
                    error_log("Payment Debug - Generated new reference number: $reference_number");
                } else {
                    error_log("Payment Debug - Using existing reference number from Java admin: $reference_number");
                }
                
                // Get bay number if ticket was assigned to a bay
                $bay_stmt = $db->prepare("SELECT bay_number FROM charging_bays WHERE current_ticket_id = ?");
                $bay_stmt->execute([$ticket_id]);
                $bay_data = $bay_stmt->fetch(PDO::FETCH_ASSOC);
                $bay_number = $bay_data ? $bay_data['bay_number'] : 0;
                
                // Calculate payment amount using Java admin billing system
                $ratePerKwh = 15.0; // ₱15.00 per kWh (configurable from database)
                $minimumFee = 50.0; // ₱50.00 minimum fee (configurable from database)
                $fastMultiplier = 1.25; // 1.25x multiplier for fast charging (configurable from database)
                $batteryCapacityKwh = 40.0; // 40kWh battery capacity
                
                // Calculate energy used based on battery levels (like Java admin)
                $initialBatteryLevel = $ticket['initial_battery_level'] ?? 50;
                $usedFraction = (100.0 - $initialBatteryLevel) / 100.0;
                $energyUsed = $usedFraction * $batteryCapacityKwh;
                
                // Determine service multiplier
                $multiplier = 1.0; // Default for normal charging
                if (stripos($ticket['service_type'], 'fast') !== false) {
                    $multiplier = $fastMultiplier; // Apply fast charging premium
                }
                
                // Calculate gross amount (like Java admin)
                $grossAmount = $energyUsed * $ratePerKwh * $multiplier;
                $amount = max($grossAmount, $minimumFee * $multiplier); // Apply minimum fee with multiplier
                
                // Reference number already determined above
                
                // Check if username exists in users table
                error_log("Payment Debug - Checking user: " . $ticket['username']);
                $user_check_stmt = $db->prepare("SELECT username FROM users WHERE username = ?");
                $user_check_stmt->execute([$ticket['username']]);
                $user_exists = $user_check_stmt->fetch();
                
                if (!$user_exists) {
                    error_log("Payment Debug - User not found: " . $ticket['username']);
                    throw new Exception('User ' . $ticket['username'] . ' does not exist in users table');
                }
                
                error_log("Payment Debug - User exists: " . $ticket['username']);
                
                // Insert payment transaction
                error_log("Payment Debug - Creating payment transaction for amount: $amount");
                $payment_stmt = $db->prepare("
                    INSERT INTO payment_transactions (
                        ticket_id, username, amount, payment_method, 
                        reference_number, transaction_status, processed_at
                    ) VALUES (?, ?, ?, ?, ?, 'Completed', NOW())
                ");
                $payment_result = $payment_stmt->execute([
                    $ticket_id,
                    $ticket['username'],
                    $amount,
                    $ticket['payment_method'] ?? 'Cash',
                    $reference_number
                ]);
                
                if (!$payment_result) {
                    $error_info = $payment_stmt->errorInfo();
                    error_log("Payment Debug - Payment transaction failed: " . json_encode($error_info));
                    throw new Exception('Failed to create payment transaction: ' . $error_info[2]);
                }
                
                error_log("Payment Debug - Payment transaction created successfully");
                
                // Update queue_tickets table with reference number before moving to history
                $update_ref_stmt = $db->prepare("UPDATE queue_tickets SET reference_number = ? WHERE ticket_id = ?");
                $update_ref_stmt->execute([$reference_number, $ticket_id]);
                error_log("Payment Debug - Updated queue_tickets with reference number: $reference_number");
                
                // Add to charging history
                try {
                    $history_stmt = $db->prepare("
                        INSERT INTO charging_history (
                            ticket_id, username, service_type, initial_battery_level,
                            final_battery_level, charging_time_minutes, energy_used, 
                            total_amount, reference_number, completed_at
                        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())
                    ");
                    $history_result = $history_stmt->execute([
                        $ticket_id, 
                        $ticket['username'], 
                        $ticket['service_type'], 
                        $initialBatteryLevel,
                        100, // final_battery_level (always 100% when completed)
                        0, // charging_time_minutes (not calculated yet)
                        $energyUsed, // energy_used (calculated from battery levels)
                        $amount, // total_amount (calculated using Java admin billing system)
                        $reference_number
                    ]);
                    
                    if (!$history_result) {
                        throw new Exception('Failed to add to charging history');
                    }
                } catch (Exception $history_error) {
                    // Log the error but don't fail the transaction
                    error_log("Charging history insertion failed for ticket $ticket_id: " . $history_error->getMessage());
                }
                
                // Clear active ticket (if exists) - like Java admin
                try {
                    $active_stmt = $db->prepare("DELETE FROM active_tickets WHERE ticket_id = ?");
                    $active_stmt->execute([$ticket_id]);
                } catch (Exception $e) {
                    // Don't fail if active_tickets table doesn't exist
                    error_log("Payment Debug - Active tickets table may not exist: " . $e->getMessage());
                }
                
                // Clear charging bays - like Java admin
                $bay_stmt = $db->prepare("UPDATE charging_bays SET current_ticket_id = NULL, current_username = NULL, status = 'Available', start_time = NULL WHERE current_ticket_id = ?");
                $bay_stmt->execute([$ticket_id]);
                
                // Clear charging grid - like Java admin
                try {
                    $grid_stmt = $db->prepare("UPDATE charging_grid SET ticket_id = NULL, username = NULL, service_type = NULL, initial_battery_level = NULL, start_time = NULL WHERE ticket_id = ?");
                    $grid_stmt->execute([$ticket_id]);
                } catch (Exception $e) {
                    // Don't fail if charging_grid table doesn't exist
                    error_log("Payment Debug - Charging grid table may not exist: " . $e->getMessage());
                }
                
                // Update user's battery level to 100% when charging is completed - like Java admin
                try {
                    $battery_stmt = $db->prepare("UPDATE battery_levels SET battery_level = 100, last_updated = NOW() WHERE username = ?");
                    $battery_stmt->execute([$ticket['username']]);
                } catch (Exception $e) {
                    // Don't fail if battery_levels table doesn't exist
                    error_log("Payment Debug - Battery levels table may not exist: " . $e->getMessage());
                }
                
                // Remove ticket from queue_tickets table (move to history) - like Java admin
                $delete_stmt = $db->prepare("DELETE FROM queue_tickets WHERE ticket_id = ?");
                $delete_result = $delete_stmt->execute([$ticket_id]);
                
                if (!$delete_result) {
                    throw new Exception('Failed to remove ticket from queue');
                }
                
                $db->commit();
                
                echo json_encode([
                    'success' => true,
                    'message' => 'Payment processed and ticket moved to history',
                    'amount' => $amount,
                    'reference_number' => $reference_number
                ]);
                
            } catch (Exception $e) {
                $db->rollback();
                echo json_encode(['error' => 'Failed to process payment: ' . $e->getMessage()]);
            }
            break;


        default:
            echo json_encode([
                'error' => 'Invalid action',
                'available_actions' => [
                    'dashboard', 'queue', 'bays', 'users', 'ticket-details',
                    'process-ticket', 'progress-to-waiting', 'progress-to-charging', 'progress-to-complete',
                    'mark-payment-paid', 'set-bay-maintenance', 'set-bay-available',
                    'add-user', 'delete-user', 'settings', 'save-settings', 'analytics', 'transactions', 'progress-next-ticket'
                ]
            ]);
            break;
    }
} catch (Exception $e) {
    echo json_encode(['error' => 'Server error: ' . $e->getMessage()]);
}
?>
