-- Cephra Mobile Web Database Setup
-- Run this script in your local XAMPP MySQL to create the required tables

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS cephra_db;
USE cephra_db;

-- Users table for authentication
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    firstname VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Queue tickets table for charging requests
CREATE TABLE IF NOT EXISTS queue_tickets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id VARCHAR(20) UNIQUE NOT NULL,
    username VARCHAR(50) NOT NULL,
    service_type ENUM('Normal Charging', 'Fast Charging') NOT NULL,
    status ENUM('Pending', 'Complete', 'Cancelled') DEFAULT 'Pending',
    payment_status ENUM('Pending', 'Paid', 'Failed') DEFAULT 'Pending',
    priority INT DEFAULT 0,
    initial_battery_level INT DEFAULT 20,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

-- Active tickets table for current charging sessions
CREATE TABLE IF NOT EXISTS active_tickets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id VARCHAR(20) UNIQUE NOT NULL,
    username VARCHAR(50) NOT NULL,
    service_type ENUM('Normal Charging', 'Fast Charging') NOT NULL,
    bay_number INT,
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estimated_end_time TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE,
    FOREIGN KEY (ticket_id) REFERENCES queue_tickets(ticket_id) ON DELETE CASCADE
);

-- Battery levels table for user battery data
CREATE TABLE IF NOT EXISTS battery_levels (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    battery_level INT NOT NULL CHECK (battery_level >= 0 AND battery_level <= 100),
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

-- Charging bays table for bay management
CREATE TABLE IF NOT EXISTS charging_bays (
    id INT AUTO_INCREMENT PRIMARY KEY,
    bay_number INT UNIQUE NOT NULL,
    bay_type ENUM('Normal', 'Fast') NOT NULL,
    status ENUM('Available', 'Occupied', 'Maintenance') DEFAULT 'Available',
    current_ticket_id VARCHAR(20),
    current_username VARCHAR(50),
    start_time TIMESTAMP NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (current_ticket_id) REFERENCES queue_tickets(ticket_id) ON DELETE SET NULL,
    FOREIGN KEY (current_username) REFERENCES users(username) ON DELETE SET NULL
);

-- Charging history table for completed sessions
CREATE TABLE IF NOT EXISTS charging_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id VARCHAR(20) NOT NULL,
    username VARCHAR(50) NOT NULL,
    service_type ENUM('Normal Charging', 'Fast Charging') NOT NULL,
    bay_number INT,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    initial_battery_level INT NOT NULL,
    final_battery_level INT DEFAULT 100,
    energy_used DECIMAL(10,2),
    total_cost DECIMAL(10,2),
    payment_method ENUM('Cash', 'GCash', 'Credit Card') NOT NULL,
    reference_number VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

-- Insert sample charging bays
INSERT IGNORE INTO charging_bays (bay_number, bay_type, status) VALUES
(1, 'Normal', 'Available'),
(2, 'Normal', 'Available'),
(3, 'Normal', 'Available'),
(4, 'Fast', 'Available'),
(5, 'Fast', 'Available');

-- Insert sample user for testing (password: test123)
INSERT IGNORE INTO users (firstname, lastname, username, email, password) VALUES
('John', 'Doe', 'testuser', 'test@example.com', 'test123');

-- Insert sample battery level for test user
INSERT IGNORE INTO battery_levels (username, battery_level) VALUES
('testuser', 25);

-- Create indexes for better performance
CREATE INDEX idx_queue_tickets_username ON queue_tickets(username);
CREATE INDEX idx_queue_tickets_status ON queue_tickets(status);
CREATE INDEX idx_active_tickets_username ON active_tickets(username);
CREATE INDEX idx_charging_bays_status ON charging_bays(status);
CREATE INDEX idx_charging_history_username ON charging_history(username);
CREATE INDEX idx_charging_history_ticket_id ON charging_history(ticket_id);

-- Show tables created
SHOW TABLES;
