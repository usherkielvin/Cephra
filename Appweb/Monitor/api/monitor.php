<?php
// Monitor API for Live Monitor Web
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

// Handle preflight requests
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

require_once '../../Admin/config/database.php';

try {
    $db = new Database();
    $conn = $db->getConnection();
    
    if (!$conn) {
        throw new Exception('Database connection failed');
    }
    
    // Get bays data with effective status using charging_grid tickets
    $stmt = $conn->query("
        SELECT
            cb.bay_number,
            cb.bay_type,
            CASE
                WHEN cb.status = 'Available'
                     AND EXISTS (SELECT 1 FROM charging_grid cg
                                 WHERE cg.bay_number = cb.bay_number
                                   AND cg.ticket_id IS NOT NULL)
                THEN 'Occupied'
                ELSE cb.status
            END AS status,
            cb.current_username,
            COALESCE(cb.current_ticket_id, (SELECT cg.ticket_id FROM charging_grid cg WHERE cg.bay_number = cb.bay_number LIMIT 1)) AS current_ticket_id,
            cb.start_time,
            u.plate_number
        FROM charging_bays cb
        LEFT JOIN users u ON cb.current_username = u.username
        ORDER BY cb.bay_number
    ");
    $bays = $stmt->fetchAll(PDO::FETCH_ASSOC);
    
    // Get queue data (waiting tickets)
    $stmt = $conn->query("
        SELECT 
            ticket_id,
            username,
            service_type,
            created_at
        FROM queue_tickets 
        WHERE status = 'Waiting'
        ORDER BY created_at ASC
    ");
    $queue = $stmt->fetchAll(PDO::FETCH_ASSOC);
    
    // Get waiting_grid (ordered by position/slot)
    $stmt = $conn->query("
        SELECT 
            slot_number,
            ticket_id,
            username,
            service_type,
            initial_battery_level,
            position_in_queue
        FROM waiting_grid
        ORDER BY COALESCE(position_in_queue, slot_number) ASC
    ");
    $waiting_grid = $stmt->fetchAll(PDO::FETCH_ASSOC);
    
    // Get charging_grid (bay assignments)
    $stmt = $conn->query("
        SELECT 
            bay_number,
            ticket_id,
            username,
            service_type,
            start_time
        FROM charging_grid
        ORDER BY bay_number
    ");
    $charging_grid = $stmt->fetchAll(PDO::FETCH_ASSOC);
    
    // Get recent activity (last 10 tickets)
    $stmt = $conn->query("
        SELECT 
            ticket_id,
            username,
            service_type,
            status,
            created_at
        FROM queue_tickets 
        ORDER BY created_at DESC 
        LIMIT 10
    ");
    $recent_activity = $stmt->fetchAll(PDO::FETCH_ASSOC);
    
    // Get statistics
    $stats = [
        'total_bays' => count($bays),
        'available_bays' => count(array_filter($bays, function($bay) { return $bay['status'] === 'Available'; })),
        'occupied_bays' => count(array_filter($bays, function($bay) { return $bay['status'] === 'Occupied'; })),
        'maintenance_bays' => count(array_filter($bays, function($bay) { return $bay['status'] === 'Maintenance'; })),
        'queue_count' => count($queue),
        'timestamp' => date('Y-m-d H:i:s')
    ];
    
    sendResponse(true, 'Monitor data loaded', [
        'bays' => $bays,
        'queue' => $queue,
        'waiting_grid' => $waiting_grid,
        'charging_grid' => $charging_grid,
        'recent_activity' => $recent_activity,
        'stats' => $stats
    ]);
    
} catch (Exception $e) {
    sendResponse(false, 'Monitor error: ' . $e->getMessage());
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
