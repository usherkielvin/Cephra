-- Complete Plate Number Setup Script
-- This script adds plate_number columns to all required tables in the Cephra database
-- Run this script to set up the complete plate number functionality

-- Start transaction to ensure all changes are applied together
START TRANSACTION;

-- Add plate_number column to users table (main storage with unique constraint)
ALTER TABLE users 
ADD COLUMN plate_number VARCHAR(10) DEFAULT NULL UNIQUE;

-- Add plate_number column to payment_transactions table
ALTER TABLE payment_transactions 
ADD COLUMN plate_number VARCHAR(10) DEFAULT NULL;

-- Add plate_number column to queue_tickets table
ALTER TABLE queue_tickets 
ADD COLUMN plate_number VARCHAR(10) DEFAULT NULL;

-- Add plate_number column to active_tickets table
ALTER TABLE active_tickets 
ADD COLUMN plate_number VARCHAR(10) DEFAULT NULL;

-- Create indexes for better performance on plate number lookups
CREATE INDEX idx_users_plate_number ON users(plate_number);
CREATE INDEX idx_payment_transactions_plate_number ON payment_transactions(plate_number);
CREATE INDEX idx_queue_tickets_plate_number ON queue_tickets(plate_number);
CREATE INDEX idx_active_tickets_plate_number ON active_tickets(plate_number);

-- Optional: Update existing records with plate numbers from users table
-- These statements will populate existing transaction/ticket records with plate numbers
-- if the user already has a plate number assigned

-- Uncomment the following UPDATE statements if you want to populate existing records:

-- UPDATE payment_transactions pt 
-- JOIN users u ON pt.username = u.username 
-- SET pt.plate_number = u.plate_number 
-- WHERE pt.plate_number IS NULL AND u.plate_number IS NOT NULL;

-- UPDATE queue_tickets qt 
-- JOIN users u ON qt.username = u.username 
-- SET qt.plate_number = u.plate_number 
-- WHERE qt.plate_number IS NULL AND u.plate_number IS NOT NULL;

-- UPDATE active_tickets at 
-- JOIN users u ON at.username = u.username 
-- SET at.plate_number = u.plate_number 
-- WHERE at.plate_number IS NULL AND u.plate_number IS NOT NULL;

-- Commit all changes
COMMIT;

-- Verify the changes were applied successfully
SELECT 'Setup completed successfully!' AS status;

-- Show the table structures to confirm columns were added
DESCRIBE users;
DESCRIBE payment_transactions;
DESCRIBE queue_tickets;
DESCRIBE active_tickets;