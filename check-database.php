<?php
// Check Database Connection and Tables
echo "🔍 Checking Cephra Database Connection...\n\n";

try {
    require_once 'config/database.php';
    
    $db = (new Database())->getConnection();
    if (!$db) {
        echo "❌ Database connection failed\n";
        exit;
    }
    
    echo "✅ Database connection successful!\n";
    
    // Check which database we're connected to
    $stmt = $db->query("SELECT DATABASE() as current_db");
    $result = $stmt->fetch(PDO::FETCH_ASSOC);
    echo "📊 Current Database: " . $result['current_db'] . "\n\n";
    
    // Check if queue_tickets table exists
    $stmt = $db->query("SHOW TABLES LIKE 'queue_tickets'");
    $tableExists = $stmt->rowCount() > 0;
    
    if ($tableExists) {
        echo "✅ queue_tickets table exists\n";
        
        // Count tickets in queue_tickets table
        $stmt = $db->query("SELECT COUNT(*) as ticket_count FROM queue_tickets");
        $result = $stmt->fetch(PDO::FETCH_ASSOC);
        echo "📋 Total tickets in queue_tickets: " . $result['ticket_count'] . "\n";
        
        // Show all tickets
        echo "\n📋 All tickets in queue_tickets table:\n";
        $stmt = $db->query("SELECT ticket_id, username, service_type, status, created_at FROM queue_tickets ORDER BY created_at DESC");
        $tickets = $stmt->fetchAll(PDO::FETCH_ASSOC);
        
        if (count($tickets) > 0) {
            foreach ($tickets as $ticket) {
                echo "- {$ticket['ticket_id']}: {$ticket['username']} • {$ticket['service_type']} • {$ticket['status']} • {$ticket['created_at']}\n";
            }
        } else {
            echo "No tickets found\n";
        }
        
    } else {
        echo "❌ queue_tickets table does NOT exist\n";
        
        // Show what tables do exist
        echo "\n📋 Available tables:\n";
        $stmt = $db->query("SHOW TABLES");
        $tables = $stmt->fetchAll(PDO::FETCH_COLUMN);
        
        if (count($tables) > 0) {
            foreach ($tables as $table) {
                echo "- $table\n";
            }
        } else {
            echo "No tables found\n";
        }
    }
    
    // Check users table
    echo "\n👥 Checking users table:\n";
    $stmt = $db->query("SHOW TABLES LIKE 'users'");
    $usersTableExists = $stmt->rowCount() > 0;
    
    if ($usersTableExists) {
        echo "✅ users table exists\n";
        $stmt = $db->query("SELECT COUNT(*) as user_count FROM users");
        $result = $stmt->fetch(PDO::FETCH_ASSOC);
        echo "👤 Total users: " . $result['user_count'] . "\n";
    } else {
        echo "❌ users table does NOT exist\n";
    }
    
} catch (Exception $e) {
    echo "❌ Error: " . $e->getMessage() . "\n";
}
?>
