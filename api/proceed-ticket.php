<?php
// Proceed a pending ticket: assign an available bay and mark as In Progress
// Input: POST ticket_id
// Output: JSON { success, bay_number, status }

ini_set('display_errors', 1);
error_reporting(E_ALL);

header('Content-Type: application/json');

require_once __DIR__ . '/../config/database.php';

try {
    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        http_response_code(405);
        echo json_encode(['error' => 'Method not allowed']);
        exit();
    }

    $ticketId = isset($_POST['ticket_id']) ? trim($_POST['ticket_id']) : '';
    if ($ticketId === '') {
        http_response_code(400);
        echo json_encode(['error' => 'ticket_id is required']);
        exit();
    }

    $db = new Database();
    $conn = $db->getConnection();
    if (!$conn) {
        http_response_code(500);
        echo json_encode(['error' => 'Database connection failed']);
        exit();
    }

    // Verify ticket exists and is pending
    $stmt = $conn->prepare("SELECT username, service_type, status FROM active_tickets WHERE ticket_id = :ticket_id LIMIT 1");
    $stmt->bindParam(':ticket_id', $ticketId);
    $stmt->execute();
    $ticket = $stmt->fetch(PDO::FETCH_ASSOC);
    if (!$ticket) {
        http_response_code(404);
        echo json_encode(['error' => 'Ticket not found']);
        exit();
    }

    if (strtolower($ticket['status']) !== 'pending') {
        echo json_encode(['error' => 'Ticket is not pending', 'status' => $ticket['status']]);
        exit();
    }

    // Pick an available bay based on service type
    $bayType = (stripos($ticket['service_type'], 'Fast') !== false) ? 'Fast' : 'Normal';
    $stmt = $conn->prepare("SELECT bay_number FROM charging_bays WHERE bay_type = :bayType AND status = 'Available' LIMIT 1");
    $stmt->bindParam(':bayType', $bayType);
    $stmt->execute();
    $bay = $stmt->fetch(PDO::FETCH_ASSOC);
    if (!$bay) {
        echo json_encode(['error' => 'No available bay']);
        exit();
    }
    $bayNumber = $bay['bay_number'];

    // Mark bay as Occupied and update active_tickets to In Progress
    $conn->beginTransaction();

    $stmt = $conn->prepare("UPDATE charging_bays SET status = 'Occupied' WHERE bay_number = :bay");
    $stmt->bindParam(':bay', $bayNumber);
    $stmt->execute();

    $stmt = $conn->prepare("UPDATE active_tickets SET status = 'In Progress', bay_number = :bay WHERE ticket_id = :ticket_id");
    $stmt->bindParam(':bay', $bayNumber);
    $stmt->bindParam(':ticket_id', $ticketId);
    $stmt->execute();

    // Reflect in queue_tickets as well
    $stmt = $conn->prepare("UPDATE queue_tickets SET status = 'In Progress' WHERE ticket_id = :ticket_id");
    $stmt->bindParam(':ticket_id', $ticketId);
    $stmt->execute();

    $conn->commit();

    echo json_encode(['success' => true, 'bay_number' => $bayNumber, 'status' => 'In Progress']);
    exit();
} catch (Exception $e) {
    if (isset($conn) && $conn->inTransaction()) {
        $conn->rollBack();
    }
    http_response_code(500);
    echo json_encode(['error' => 'Server error', 'details' => $e->getMessage()]);
    exit();
}
?>


