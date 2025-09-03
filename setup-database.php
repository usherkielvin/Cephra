<?php
// Database Setup Script for Cephra
echo "Setting up Cephra database...\n";

try {
    // Connect to MySQL without selecting a database
    $pdo = new PDO("mysql:host=localhost", "root", "");
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    
    // Create database if it doesn't exist
    $pdo->exec("CREATE DATABASE IF NOT EXISTS cephra");
    echo "✓ Database 'cephra' created/verified\n";
    
    // Select the database
    $pdo->exec("USE cephra");
    
    // Create users table
    $pdo->exec("CREATE TABLE IF NOT EXISTS users (
        id INT AUTO_INCREMENT PRIMARY KEY,
        username VARCHAR(50) NOT NULL UNIQUE,
        email VARCHAR(100) NOT NULL UNIQUE,
        password VARCHAR(100) NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    )");
    echo "✓ Users table created\n";
    
    // Create queue_tickets table
    $pdo->exec("CREATE TABLE IF NOT EXISTS queue_tickets (
        id INT AUTO_INCREMENT PRIMARY KEY,
        ticket_id VARCHAR(50) NOT NULL UNIQUE,
        username VARCHAR(50) NOT NULL,
        service_type VARCHAR(20) NOT NULL,
        initial_battery_level INT NOT NULL,
        status VARCHAR(20) NOT NULL DEFAULT 'Pending',
        payment_status VARCHAR(20) NOT NULL DEFAULT 'Pending',
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    )");
    echo "✓ Queue tickets table created\n";
    
    // Insert test user with correct password
    $stmt = $pdo->prepare("INSERT IGNORE INTO users (username, email, password) VALUES (?, ?, ?)");
    $stmt->execute(['dizon', 'dizon@cephra.com', '123']);
    echo "✓ Test user 'dizon' created with password '123'\n";
    
    // Insert demo ticket
    $stmt = $pdo->prepare("INSERT IGNORE INTO queue_tickets (ticket_id, username, service_type, initial_battery_level, status, payment_status) VALUES (?, ?, ?, ?, ?, ?)");
    $stmt->execute(['FCH001', 'dizon', 'Fast Charging', 30, 'Pending', 'Pending']);
    echo "✓ Demo ticket FCH001 created\n";
    
    echo "\n🎉 Database setup completed successfully!\n";
    echo "You can now test the login with:\n";
    echo "Username: dizon\n";
    echo "Password: 123\n";
    
} catch (PDOException $e) {
    echo "❌ Database setup failed: " . $e->getMessage() . "\n";
    echo "Make sure:\n";
    echo "1. XAMPP is running (Apache + MySQL)\n";
    echo "2. MySQL username is 'root' and password is empty\n";
}
?>
