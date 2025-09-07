-- Seed waiting_grid and charging_grid tables
-- Safe to run multiple times; uses CREATE IF NOT EXISTS and clears rows first

-- Ensure waiting_grid exists
CREATE TABLE IF NOT EXISTS waiting_grid (
    id INT AUTO_INCREMENT PRIMARY KEY,
    slot_number INT NOT NULL UNIQUE,
    ticket_id VARCHAR(50),
    username VARCHAR(50),
    service_type VARCHAR(20),
    initial_battery_level INT,
    position_in_queue INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Ensure charging_grid exists
CREATE TABLE IF NOT EXISTS charging_grid (
    id INT AUTO_INCREMENT PRIMARY KEY,
    bay_number VARCHAR(10) NOT NULL UNIQUE,
    ticket_id VARCHAR(50),
    username VARCHAR(50),
    service_type VARCHAR(20),
    initial_battery_level INT,
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Clear and seed waiting grid (10 empty slots)
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

-- Clear and seed charging grid (8 empty bays)
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

-- Summary checks (optional; harmless if client discards result sets)
SELECT 'waiting_grid slots' AS info, COUNT(*) AS count FROM waiting_grid;
SELECT 'charging_grid bays' AS info, COUNT(*) AS count FROM charging_grid;


