-- Add plate_number column to transaction and ticket tables
-- This script adds the plate_number column to multiple tables for better tracking and reporting
-- Plate numbers will have format: 3 random letters + 4 random digits (e.g., XYZ1234, ABC5678)

-- Add plate_number to payment_transactions table
ALTER TABLE payment_transactions 
ADD COLUMN plate_number VARCHAR(10) DEFAULT NULL;

-- Add plate_number to queue_tickets table
ALTER TABLE queue_tickets 
ADD COLUMN plate_number VARCHAR(10) DEFAULT NULL;

-- Add plate_number to active_tickets table
ALTER TABLE active_tickets 
ADD COLUMN plate_number VARCHAR(10) DEFAULT NULL;

-- Add indexes for better performance on plate number lookups
CREATE INDEX idx_payment_transactions_plate_number ON payment_transactions(plate_number);
CREATE INDEX idx_queue_tickets_plate_number ON queue_tickets(plate_number);
CREATE INDEX idx_active_tickets_plate_number ON active_tickets(plate_number);

-- Optional: Update existing records with plate numbers from users table
-- This will populate the plate_number column for existing records based on username
-- Uncomment the following lines if you want to populate existing records:

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

COMMIT;