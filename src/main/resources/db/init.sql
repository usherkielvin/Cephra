-- Cephra Database Initialization Script
-- This script creates all necessary tables for the Cephra charging station management system

-- Drop existing tables if they exist (for clean initialization)
DROP TABLE IF EXISTS reward_transactions;
DROP TABLE IF EXISTS user_points;
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
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    car_index INT DEFAULT NULL, -- Car image index (0-9 for c1.png to c10.png)
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
    payment_method VARCHAR(20) DEFAULT 'Cash', -- 'Cash', 'Online'
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
    energy_used DECIMAL(10,2) NOT NULL DEFAULT 0.0, -- Energy consumed in kWh
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

-- Wallet balance table
CREATE TABLE IF NOT EXISTS wallet_balance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    balance DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

-- Wallet transactions table
CREATE TABLE IF NOT EXISTS wallet_transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    transaction_type VARCHAR(20) NOT NULL, -- 'TOP_UP', 'PAYMENT', 'REFUND'
    amount DECIMAL(10,2) NOT NULL,
    previous_balance DECIMAL(10,2) NOT NULL,
    new_balance DECIMAL(10,2) NOT NULL,
    description VARCHAR(255) NOT NULL,
    reference_id VARCHAR(50), -- ticket_id for payments, transaction_id for topups
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
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
INSERT IGNORE INTO users (firstname, lastname, username, email, password) VALUES 
('John', 'Dizon', 'dizon', 'dizon@cephra.com', '1234'),
('Earnest', 'Smith', 'earnest', 'earnest@cephra.com', '1234');

-- Insert initial staff records (using INSERT IGNORE to prevent duplicates)
INSERT IGNORE INTO staff_records (name, username, email, status, password) VALUES 
('Admin User', 'admin', 'admin@cephra.com', 'Active', 'admin123'),
('Staff Member', 'staff', 'staff@cephra.com', 'Active', 'staff123');

-- Insert initial charging bays (8 total bays to match Queue.java expectations)
-- Fast Charging Bays: Bay-1, Bay-2, Bay-3
-- Normal Charging Bays: Bay-4, Bay-5, Bay-6, Bay-7, Bay-8
-- Clear existing data first to ensure clean state
DELETE FROM charging_bays;
INSERT INTO charging_bays (bay_number, bay_type, status) VALUES 
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
CREATE INDEX idx_wallet_balance_username ON wallet_balance(username);
CREATE INDEX idx_wallet_transactions_username ON wallet_transactions(username);
CREATE INDEX idx_wallet_transactions_type ON wallet_transactions(transaction_type);
CREATE INDEX idx_wallet_transactions_date ON wallet_transactions(transaction_date);
CREATE INDEX idx_user_points_username ON user_points(username);
CREATE INDEX idx_reward_transactions_username ON reward_transactions(username);
CREATE INDEX idx_reward_transactions_type ON reward_transactions(transaction_type);
CREATE INDEX idx_reward_transactions_date ON reward_transactions(transaction_date);

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
('dizon', 30, 30, 40.0),
('earnest', 35, 35, 40.0);

-- Insert initial wallet balances for demo users
INSERT IGNORE INTO wallet_balance (username, balance) VALUES
('dizon', 1000.00),
('earnest', 500.00);

-- User points table for reward system
CREATE TABLE IF NOT EXISTS user_points (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    total_points INT NOT NULL DEFAULT 0,
    lifetime_earned INT NOT NULL DEFAULT 0,
    lifetime_spent INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

-- Reward transactions table for tracking point activities
CREATE TABLE IF NOT EXISTS reward_transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    transaction_type VARCHAR(20) NOT NULL, -- 'EARNED', 'SPENT', 'EXPIRED', 'BONUS'
    points_change INT NOT NULL, -- Positive for earned, negative for spent
    total_points_after INT NOT NULL,
    description VARCHAR(255) NOT NULL,
    reference_id VARCHAR(50), -- ticket_id for payment-based points, item_id for purchases
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

-- Insert initial user points for demo users
INSERT IGNORE INTO user_points (username, total_points, lifetime_earned, lifetime_spent) VALUES
('dizon', 69, 69, 0),
('earnest', 0, 0, 0);

-- Insert demo queue ticket (using INSERT IGNORE to prevent duplicates)
-- INSERT IGNORE INTO queue_tickets (ticket_id, username, service_type, initial_battery_level, status, payment_status, priority) VALUES
-- ('FCH001', 'dizon', 'Fast', 30, 'Pending', 'Pending', 1);

-- Create waiting grid table to track tickets in waiting slots
CREATE TABLE IF NOT EXISTS waiting_grid (
    id INT AUTO_INCREMENT PRIMARY KEY,
    slot_number INT NOT NULL UNIQUE, -- 1-10 for waiting grid slots
    ticket_id VARCHAR(50),
    username VARCHAR(50),
    service_type VARCHAR(20), -- 'Fast', 'Normal'
    initial_battery_level INT,
    position_in_queue INT, -- Order in waiting queue
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

-- Create charging grid table to track tickets in charging bays
CREATE TABLE IF NOT EXISTS charging_grid (
    id INT AUTO_INCREMENT PRIMARY KEY,
    bay_number VARCHAR(10) NOT NULL UNIQUE, -- Bay-1 to Bay-8
    ticket_id VARCHAR(50),
    username VARCHAR(50),
    service_type VARCHAR(20), -- 'Fast', 'Normal'
    initial_battery_level INT,
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

-- Insert initial waiting grid slots (empty)
-- Waiting Grid Slots: 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 (10 total slots)
-- Clear existing data first to ensure clean state
DELETE FROM waiting_grid;
INSERT INTO waiting_grid (slot_number, ticket_id, username, service_type, initial_battery_level, position_in_queue) VALUES
(1, NULL, NULL, NULL, NULL, NULL),
(2, NULL, NULL, NULL, NULL, NULL),
(3, NULL, NULL, NULL, NULL, NULL),
(4, NULL, NULL, NULL, NULL, NULL),
(5, NULL, NULL, NULL, NULL, NULL),
(6, NULL, NULL, NULL, NULL, NULL),
(7, NULL, NULL, NULL, NULL, NULL),
(8, NULL, NULL, NULL, NULL, NULL),
(9, NULL, NULL, NULL, NULL, NULL),
(10, NULL, NULL, NULL, NULL, NULL);

-- Insert initial charging grid slots (empty)
-- Charging Grid Bays: Bay-1, Bay-2, Bay-3, Bay-4, Bay-5, Bay-6, Bay-7, Bay-8 (8 total bays)
-- Clear existing data first to ensure clean state
DELETE FROM charging_grid;
INSERT INTO charging_grid (bay_number, ticket_id, username, service_type, initial_battery_level) VALUES
('Bay-1', NULL, NULL, NULL, NULL),
('Bay-2', NULL, NULL, NULL, NULL),
('Bay-3', NULL, NULL, NULL, NULL),
('Bay-4', NULL, NULL, NULL, NULL),
('Bay-5', NULL, NULL, NULL, NULL),
('Bay-6', NULL, NULL, NULL, NULL),
('Bay-7', NULL, NULL, NULL, NULL),
('Bay-8', NULL, NULL, NULL, NULL);

-- Verify bay numbering system is correct
-- This section ensures all bays and slots are properly numbered and incrementing

-- Verify charging bays are numbered 1-8
SELECT '=== CHARGING BAYS VERIFICATION ===' as verification_type;
SELECT bay_number, bay_type, status FROM charging_bays ORDER BY bay_number;
SELECT COUNT(*) as total_charging_bays FROM charging_bays;

-- Verify waiting grid slots are numbered 1-10  
SELECT '=== WAITING GRID VERIFICATION ===' as verification_type;
SELECT slot_number, ticket_id FROM waiting_grid ORDER BY slot_number;
SELECT COUNT(*) as total_waiting_slots FROM waiting_grid;

-- Verify charging grid bays are numbered Bay-1 to Bay-8
SELECT '=== CHARGING GRID VERIFICATION ===' as verification_type;
SELECT bay_number, ticket_id FROM charging_grid ORDER BY bay_number;
SELECT COUNT(*) as total_charging_grid_bays FROM charging_grid;

-- Final verification summary
SELECT '=== BAY NUMBERING SUMMARY ===' as summary_type;
SELECT 
    (SELECT COUNT(*) FROM charging_bays) as charging_bays_count,
    (SELECT COUNT(*) FROM waiting_grid) as waiting_slots_count,
    (SELECT COUNT(*) FROM charging_grid) as charging_grid_count;

COMMIT;