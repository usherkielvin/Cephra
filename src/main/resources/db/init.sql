-- Cephra Database Initialization Script
-- This script creates all necessary tables for the Cephra charging station management system

-- Drop existing tables if they exist (for clean initialization)
DROP TABLE IF EXISTS payment_transactions;
DROP TABLE IF EXISTS charging_history;
DROP TABLE IF EXISTS queue_tickets;
DROP TABLE IF EXISTS active_tickets;
DROP TABLE IF EXISTS battery_levels;
DROP TABLE IF EXISTS otp_codes;
DROP TABLE IF EXISTS charging_bays;
DROP TABLE IF EXISTS staff_records;
DROP TABLE IF EXISTS system_settings;
DROP TABLE IF EXISTS users;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Battery levels table (tracks user battery levels)
CREATE TABLE IF NOT EXISTS battery_levels (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    battery_level INT NOT NULL CHECK (battery_level >= 0 AND battery_level <= 100),
    initial_battery_level INT NOT NULL CHECK (initial_battery_level >= 0 AND initial_battery_level <= 100),
    battery_capacity_kwh DECIMAL(5,2) NOT NULL DEFAULT 40.0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

-- Active tickets table (tracks currently active charging sessions)
CREATE TABLE IF NOT EXISTS active_tickets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    ticket_id VARCHAR(50) NOT NULL UNIQUE,
    service_type VARCHAR(20) NOT NULL, -- 'Fast', 'Normal'
    initial_battery_level INT NOT NULL CHECK (initial_battery_level >= 0 AND initial_battery_level <= 100),
    current_battery_level INT NOT NULL CHECK (current_battery_level >= 0 AND current_battery_level <= 100),
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estimated_completion_time TIMESTAMP NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'Active', -- 'Active', 'Completed', 'Cancelled'
    bay_number VARCHAR(10), -- Reference to charging bay
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

-- OTP codes table (for password reset functionality)
CREATE TABLE IF NOT EXISTS otp_codes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    otp_code VARCHAR(6) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    used BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

-- Queue tickets table (tracks tickets waiting for charging)
CREATE TABLE IF NOT EXISTS queue_tickets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id VARCHAR(50) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL,
    service_type VARCHAR(20) NOT NULL, -- 'Fast', 'Normal'
    initial_battery_level INT NOT NULL CHECK (initial_battery_level >= 0 AND initial_battery_level <= 100),
    status VARCHAR(20) NOT NULL DEFAULT 'Pending', -- 'Pending', 'In Progress', 'Completed', 'Cancelled'
    payment_status VARCHAR(20) NOT NULL DEFAULT 'Pending', -- 'Pending', 'Paid'
    reference_number VARCHAR(20),
    priority INT DEFAULT 0, -- For queue ordering
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

-- Charging history table (tracks completed charging sessions)
CREATE TABLE IF NOT EXISTS charging_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id VARCHAR(50) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL,
    service_type VARCHAR(20) NOT NULL, -- 'Fast', 'Normal'
    initial_battery_level INT NOT NULL CHECK (initial_battery_level >= 0 AND initial_battery_level <= 100),
    final_battery_level INT NOT NULL DEFAULT 100 CHECK (final_battery_level >= 0 AND final_battery_level <= 100),
    charging_time_minutes INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    reference_number VARCHAR(20) NOT NULL,
    served_by VARCHAR(50) DEFAULT 'Admin',
    completed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

-- Staff records table
CREATE TABLE IF NOT EXISTS staff_records (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL DEFAULT 'Active', -- 'Active', 'Inactive'
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Charging bays table
CREATE TABLE IF NOT EXISTS charging_bays (
    id INT AUTO_INCREMENT PRIMARY KEY,
    bay_number VARCHAR(10) NOT NULL UNIQUE,
    bay_type VARCHAR(20) NOT NULL, -- 'Fast', 'Normal'
    status VARCHAR(20) NOT NULL DEFAULT 'Available', -- 'Available', 'Occupied', 'Maintenance'
    current_ticket_id VARCHAR(50),
    current_username VARCHAR(50),
    start_time TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Payment transactions table
CREATE TABLE IF NOT EXISTS payment_transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(20) DEFAULT 'Cash',
    reference_number VARCHAR(20) NOT NULL,
    transaction_status VARCHAR(20) NOT NULL DEFAULT 'Completed',
    processed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

-- System settings table
CREATE TABLE IF NOT EXISTS system_settings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    setting_key VARCHAR(50) NOT NULL UNIQUE,
    setting_value TEXT NOT NULL,
    description TEXT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert initial test users (using INSERT IGNORE to prevent duplicates)
INSERT IGNORE INTO users (username, email, password) VALUES 
('testuser', 'test@cephra.com', '1234'),
('dizon', 'dizon@cephra.com', '1234'),
('earnest', 'earnest@cephra.com', '1234');

-- Insert initial staff records (using INSERT IGNORE to prevent duplicates)
INSERT IGNORE INTO staff_records (name, username, email, status, password) VALUES 
('Admin User', 'admin', 'admin@cephra.com', 'Active', 'admin123'),
('Staff Member', 'staff', 'staff@cephra.com', 'Active', 'staff123');

-- Insert initial charging bays (8 total bays to match Queue.java expectations)
INSERT IGNORE INTO charging_bays (bay_number, bay_type, status) VALUES 
('Bay-1', 'Fast', 'Available'),
('Bay-2', 'Fast', 'Available'),
('Bay-3', 'Fast', 'Available'),
('Bay-4', 'Normal', 'Available'),
('Bay-5', 'Normal', 'Available'),
('Bay-6', 'Normal', 'Available'),
('Bay-7', 'Normal', 'Available'),
('Bay-8', 'Normal', 'Available');

-- Insert system settings
INSERT IGNORE INTO system_settings (setting_key, setting_value, description) VALUES 
('rate_per_kwh', '15.0', 'Charging rate per kWh in pesos'),
('minimum_fee', '50.0', 'Minimum charging fee in pesos'),
('fast_charge_speed', '0.8', 'Minutes per 1% charge for fast charging'),
('normal_charge_speed', '1.6', 'Minutes per 1% charge for normal charging'),
('platform_commission', '0.18', 'Platform commission rate (18%)'),
('battery_capacity_kwh', '40.0', 'Standard battery capacity in kWh');

-- Create indexes for better performance
CREATE INDEX idx_queue_tickets_username ON queue_tickets(username);
CREATE INDEX idx_queue_tickets_status ON queue_tickets(status);
CREATE INDEX idx_queue_tickets_priority ON queue_tickets(priority);
CREATE INDEX idx_charging_history_username ON charging_history(username);
CREATE INDEX idx_charging_history_ticket ON charging_history(ticket_id);
CREATE INDEX idx_payment_transactions_ticket ON payment_transactions(ticket_id);
CREATE INDEX idx_battery_levels_username ON battery_levels(username);
CREATE INDEX idx_active_tickets_username ON active_tickets(username);
CREATE INDEX idx_active_tickets_bay ON active_tickets(bay_number);
CREATE INDEX idx_active_tickets_status ON active_tickets(status);
CREATE INDEX idx_charging_bays_status ON charging_bays(status);
CREATE INDEX idx_charging_bays_type ON charging_bays(bay_type);
CREATE INDEX idx_charging_bays_ticket ON charging_bays(current_ticket_id);

-- Create triggers for automatic updates
DELIMITER //

-- Trigger to update battery_levels when active_tickets is updated
CREATE TRIGGER update_battery_levels_on_active_ticket_update
AFTER UPDATE ON active_tickets
FOR EACH ROW
BEGIN
    IF NEW.current_battery_level != OLD.current_battery_level THEN
        UPDATE battery_levels 
        SET battery_level = NEW.current_battery_level,
            last_updated = CURRENT_TIMESTAMP
        WHERE username = NEW.username;
    END IF;
END//

-- Trigger to clean up active_tickets when charging is completed
CREATE TRIGGER cleanup_active_ticket_on_completion
AFTER INSERT ON charging_history
FOR EACH ROW
BEGIN
    DELETE FROM active_tickets WHERE ticket_id = NEW.ticket_id;
END//

-- Trigger to update charging bay status when active ticket is created
CREATE TRIGGER update_bay_status_on_active_ticket_insert
AFTER INSERT ON active_tickets
FOR EACH ROW
BEGIN
    IF NEW.bay_number IS NOT NULL THEN
        UPDATE charging_bays 
        SET status = 'Occupied',
            current_ticket_id = NEW.ticket_id,
            current_username = NEW.username,
            start_time = NEW.start_time,
            updated_at = CURRENT_TIMESTAMP
        WHERE bay_number = NEW.bay_number;
    END IF;
END//

-- Trigger to free charging bay when active ticket is deleted
CREATE TRIGGER free_bay_on_active_ticket_delete
AFTER DELETE ON active_tickets
FOR EACH ROW
BEGIN
    IF OLD.bay_number IS NOT NULL THEN
        UPDATE charging_bays 
        SET status = 'Available',
            current_ticket_id = NULL,
            current_username = NULL,
            start_time = NULL,
            updated_at = CURRENT_TIMESTAMP
        WHERE bay_number = OLD.bay_number;
    END IF;
END//

-- Trigger to update queue ticket status when moved to active
CREATE TRIGGER update_queue_status_on_active_ticket_insert
AFTER INSERT ON active_tickets
FOR EACH ROW
BEGIN
    UPDATE queue_tickets 
    SET status = 'In Progress',
        updated_at = CURRENT_TIMESTAMP
    WHERE ticket_id = NEW.ticket_id;
END//

DELIMITER ;

-- Insert demo data for testing (using INSERT IGNORE to prevent duplicates)
INSERT IGNORE INTO battery_levels (username, battery_level, initial_battery_level, battery_capacity_kwh) VALUES
('testuser', 25, 25, 40.0),
('dizon', 30, 30, 40.0),
('earnest', 35, 35, 40.0);

-- Insert demo queue ticket (using INSERT IGNORE to prevent duplicates)
INSERT IGNORE INTO queue_tickets (ticket_id, username, service_type, initial_battery_level, status, payment_status, priority) VALUES
('FCH001', 'dizon', 'Fast', 30, 'Pending', 'Pending', 1);

COMMIT;