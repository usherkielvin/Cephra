<?php
// Debug script to check tickets in database
header("Content-Type: text/plain");

require_once "config/database.php";

try {
    $db = (new Database())->getConnection();

    if (!$db) {
        echo "ERROR: Database connection failed\n";
        exit();
    }

    echo "=== DATABASE CONNECTION SUCCESSFUL ===\n\n";

    // Check all tickets
    echo "=== ALL TICKETS ===\n";
    $stmt = $db->query("SELECT ticket_id, username, status, payment_status, created_at FROM queue_tickets ORDER BY created_at DESC");
    $allTickets = $stmt->fetchAll(PDO::FETCH_ASSOC);

    if (empty($allTickets)) {
        echo "No tickets found in database!\n";
    } else {
        foreach ($allTickets as $ticket) {
            echo "Ticket ID: {$ticket['ticket_id']}, Username: {$ticket['username']}, Status: {$ticket['status']}, Payment Status: {$ticket['payment_status']}, Created: {$ticket['created_at']}\n";
        }
    }

    echo "\n=== TICKETS THAT CAN BE PROGRESSSED (Pending/Waiting/Charging) ===\n";
    $stmt = $db->query("SELECT ticket_id, username, status, created_at FROM queue_tickets WHERE status IN ('Pending', 'Waiting', 'Charging') ORDER BY created_at ASC");
    $progressableTickets = $stmt->fetchAll(PDO::FETCH_ASSOC);

    if (empty($progressableTickets)) {
        echo "No tickets available to progress! This is why the API is failing.\n";
        echo "You need to create some tickets with status 'Pending', 'Waiting', or 'Charging'.\n";
    } else {
        foreach ($progressableTickets as $ticket) {
            echo "Ticket ID: {$ticket['ticket_id']}, Username: {$ticket['username']}, Status: {$ticket['status']}, Created: {$ticket['created_at']}\n";
        }
    }

    echo "\n=== TICKET STATUS COUNTS ===\n";
    $stmt = $db->query("SELECT status, COUNT(*) as count FROM queue_tickets GROUP BY status");
    $statusCounts = $stmt->fetchAll(PDO::FETCH_ASSOC);

    foreach ($statusCounts as $count) {
        echo "{$count['status']}: {$count['count']}\n";
    }

} catch (Exception $e) {
    echo "ERROR: " . $e->getMessage() . "\n";
}
?>
