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
    
    // Get bays data
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
