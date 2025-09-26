-- Add missing plate_number columns to transaction/ticket tables
-- This script only adds columns that don't already exist

-- Start transaction
START TRANSACTION;

-- Add plate_number column to payment_transactions table if it doesn't exist
-- We'll use a conditional approach to avoid duplicate column errors

-- Check and add to payment_transactions
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE table_name = 'payment_transactions' 
     AND column_name = 'plate_number' 
     AND table_schema = DATABASE()) = 0,
    'ALTER TABLE payment_transactions ADD COLUMN plate_number VARCHAR(10) DEFAULT NULL',
    'SELECT "plate_number column already exists in payment_transactions" as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check and add to queue_tickets
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE table_name = 'queue_tickets' 
     AND column_name = 'plate_number' 
     AND table_schema = DATABASE()) = 0,
    'ALTER TABLE queue_tickets ADD COLUMN plate_number VARCHAR(10) DEFAULT NULL',
    'SELECT "plate_number column already exists in queue_tickets" as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check and add to active_tickets
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE table_name = 'active_tickets' 
     AND column_name = 'plate_number' 
     AND table_schema = DATABASE()) = 0,
    'ALTER TABLE active_tickets ADD COLUMN plate_number VARCHAR(10) DEFAULT NULL',
    'SELECT "plate_number column already exists in active_tickets" as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Add indexes only if they don't exist (MySQL will show warning if index exists, but won't fail)
CREATE INDEX IF NOT EXISTS idx_payment_transactions_plate_number ON payment_transactions(plate_number);
CREATE INDEX IF NOT EXISTS idx_queue_tickets_plate_number ON queue_tickets(plate_number);
CREATE INDEX IF NOT EXISTS idx_active_tickets_plate_number ON active_tickets(plate_number);

-- Commit changes
COMMIT;

-- Verify the setup
SELECT 'Plate number columns added successfully!' AS status;

-- Show which tables have the plate_number column
SELECT 
    table_name,
    column_name,
    data_type,
    is_nullable
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE table_schema = DATABASE() 
  AND column_name = 'plate_number'
ORDER BY table_name;