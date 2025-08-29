-- Cephra Database Initialization Script

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS cephradb;
USE cephradb;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Battery levels table
CREATE TABLE IF NOT EXISTS battery_levels (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    battery_level INT NOT NULL DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

-- Active tickets table
CREATE TABLE IF NOT EXISTS active_tickets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    ticket_id VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

-- OTP table for password reset
CREATE TABLE IF NOT EXISTS otp_codes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    otp_code VARCHAR(6) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP DEFAULT (CURRENT_TIMESTAMP + INTERVAL 10 MINUTE),
    FOREIGN KEY (email) REFERENCES users(email) ON DELETE CASCADE
);

-- Queue tickets table (for persistent storage)
CREATE TABLE IF NOT EXISTS queue_tickets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id VARCHAR(50) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL,
    service_type VARCHAR(50) NOT NULL, -- 'Fast Charging' or 'Normal Charging'
    status VARCHAR(20) NOT NULL DEFAULT 'Pending', -- 'Pending', 'Waiting', 'Charging', 'Completed'
    payment_status VARCHAR(20) NOT NULL DEFAULT '', -- '', 'Paid'
    reference_number VARCHAR(20),
    initial_battery_level INT NOT NULL,
    battery_capacity_kwh DECIMAL(5,2) NOT NULL DEFAULT 40.0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

-- Charging history table
CREATE TABLE IF NOT EXISTS charging_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL,
    service_type VARCHAR(50) NOT NULL,
    initial_battery_level INT NOT NULL,
    final_battery_level INT NOT NULL DEFAULT 100,
    charging_time_minutes INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    reference_number VARCHAR(20) NOT NULL,
    served_by VARCHAR(50) DEFAULT 'Cephra',
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

-- Insert initial test users
INSERT INTO users (username, email, password) VALUES 
('testuser', 'test@cephra.com', '1234');

-- Insert initial staff records
INSERT INTO staff_records (name, username, email, status, password) VALUES 
('Admin User', 'admin', 'admin@cephra.com', 'Active', 'admin123'),
('Staff Member', 'staff', 'staff@cephra.com', 'Active', 'staff123');

-- Insert initial charging bays
INSERT INTO charging_bays (bay_number, bay_type, status) VALUES 
('Bay-1', 'Fast', 'Available'),
('Bay-2', 'Fast', 'Available'),
('Bay-3', 'Fast', 'Available'),
('Bay-4', 'Normal', 'Available'),
('Bay-5', 'Normal', 'Available');

-- Insert system settings
INSERT INTO system_settings (setting_key, setting_value, description) VALUES 
('rate_per_kwh', '15.0', 'Charging rate per kWh in pesos'),
('minimum_fee', '50.0', 'Minimum charging fee in pesos'),
('fast_charge_speed', '0.8', 'Minutes per 1% charge for fast charging'),
('normal_charge_speed', '1.6', 'Minutes per 1% charge for normal charging'),
('platform_commission', '0.18', 'Platform commission rate (18%)');

-- Create indexes for better performance
CREATE INDEX idx_queue_tickets_username ON queue_tickets(username);
CREATE INDEX idx_queue_tickets_status ON queue_tickets(status);
CREATE INDEX idx_charging_history_username ON charging_history(username);
CREATE INDEX idx_charging_history_ticket ON charging_history(ticket_id);
CREATE INDEX idx_payment_transactions_ticket ON payment_transactions(ticket_id);
CREATE INDEX idx_battery_levels_username ON battery_levels(username);
CREATE INDEX idx_active_tickets_username ON active_tickets(username);