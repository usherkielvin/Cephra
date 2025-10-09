<?php
/**
 * Simple API Test Script
 */

require_once 'config/database.php';

echo "Testing Monitor API...\n";

try {
    $database = new Database();
    $conn = $database->getConnection();
    
    if (!$conn) {
        throw new Exception('Database connection failed');
    }
    
    echo "✓ Database connection successful\n";
    
    // Test the same query that the API uses
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
            COALESCE(cb.current_ticket_id, 
                (SELECT cg.ticket_id FROM charging_grid cg 
                 WHERE cg.bay_number = cb.bay_number LIMIT 1)
            ) AS current_ticket_id,
            cb.start_time,
            u.plate_number
        FROM charging_bays cb
        LEFT JOIN users u ON cb.current_username = u.username
        ORDER BY cb.bay_number
    ");
    
    $bays = $stmt->fetchAll(PDO::FETCH_ASSOC);
    echo "✓ Bays query successful - Found " . count($bays) . " bays\n";
    
    // Test queue query
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
    echo "✓ Queue query successful - Found " . count($queue) . " waiting tickets\n";
    
    echo "\nSample Bay Data:\n";
    foreach (array_slice($bays, 0, 3) as $bay) {
        echo "- Bay {$bay['bay_number']}: {$bay['status']} ({$bay['bay_type']})\n";
    }
    
    echo "\nSample Queue Data:\n";
    foreach (array_slice($queue, 0, 3) as $ticket) {
        echo "- Ticket {$ticket['ticket_id']}: {$ticket['username']} ({$ticket['service_type']})\n";
    }
    
    echo "\n✓ All API queries working correctly!\n";
    echo "The Monitor should now display real data.\n";
    
} catch (Exception $e) {
    echo "✗ Error: " . $e->getMessage() . "\n";
}
?>
