<!DOCTYPE html>
<html>
<head>
    <title>Database Test - Cephra</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .success { color: green; }
        .error { color: red; }
        .info { color: blue; }
        table { border-collapse: collapse; width: 100%; margin: 20px 0; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>
    <h1>🔍 Cephra Database Test</h1>
    
    <?php
    require_once 'config/database.php';
    
    echo "<h2>Database Connection Test</h2>";
    
    $db = (new Database())->getConnection();
    if ($db) {
        echo "<p class='success'>✅ Database connection successful!</p>";
        
        // Test users table
        echo "<h3>Users Table</h3>";
        try {
            $stmt = $db->query("SELECT username, email, created_at FROM users");
            $users = $stmt->fetchAll(PDO::FETCH_ASSOC);
            
            if (count($users) > 0) {
                echo "<table>";
                echo "<tr><th>Username</th><th>Email</th><th>Created</th></tr>";
                foreach ($users as $user) {
                    echo "<tr>";
                    echo "<td>{$user['username']}</td>";
                    echo "<td>{$user['email']}</td>";
                    echo "<td>{$user['created_at']}</td>";
                    echo "</tr>";
                }
                echo "</table>";
            } else {
                echo "<p class='info'>No users found in database</p>";
            }
        } catch (Exception $e) {
            echo "<p class='error'>Error reading users: " . $e->getMessage() . "</p>";
        }
        
        // Test queue_tickets table
        echo "<h3>Queue Tickets Table</h3>";
        try {
            $stmt = $db->query("SELECT ticket_id, username, service_type, status, payment_status FROM queue_tickets");
            $tickets = $stmt->fetchAll(PDO::FETCH_ASSOC);
            
            if (count($tickets) > 0) {
                echo "<table>";
                echo "<tr><th>Ticket ID</th><th>Username</th><th>Service</th><th>Status</th><th>Payment</th></tr>";
                foreach ($tickets as $ticket) {
                    echo "<tr>";
                    echo "<td>{$ticket['ticket_id']}</td>";
                    echo "<td>{$ticket['username']}</td>";
                    echo "<td>{$ticket['service_type']}</td>";
                    echo "<td>{$ticket['status']}</td>";
                    echo "<td>{$ticket['payment_status']}</td>";
                    echo "</tr>";
                }
                echo "</table>";
            } else {
                echo "<p class='info'>No tickets found in database</p>";
            }
        } catch (Exception $e) {
            echo "<p class='error'>Error reading tickets: " . $e->getMessage() . "</p>";
        }
        
    } else {
        echo "<p class='error'>❌ Database connection failed!</p>";
        echo "<p>Please check:</p>";
        echo "<ul>";
        echo "<li>XAMPP is running (Apache + MySQL)</li>";
        echo "<li>MySQL username is 'root' and password is empty</li>";
        echo "<li>Database 'cephra' exists</li>";
        echo "</ul>";
    }
    ?>
    
    <h2>Quick Actions</h2>
    <p><a href="setup-database.php" target="_blank">🔧 Run Database Setup</a></p>
    <p><a href="phone/" target="_blank">📱 Test Phone Interface</a></p>
    <p><a href="api/login" target="_blank">🔑 Test Login API</a></p>
</body>
</html>
