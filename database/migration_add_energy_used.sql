-- Migration script to add energy_used column to charging_history table
-- Run this script to update existing databases

-- Add energy_used column if it doesn't exist
ALTER TABLE charging_history 
ADD COLUMN IF NOT EXISTS energy_used DECIMAL(10,2) NOT NULL DEFAULT 0.0 
COMMENT 'Energy consumed in kWh';

-- Update existing records with calculated energy_used values
-- Based on battery levels: energy_used = (100 - initial_battery_level) / 100 * 40.0
UPDATE charging_history 
SET energy_used = ((100 - initial_battery_level) / 100.0) * 40.0
WHERE energy_used = 0.0;

-- Verify the update
SELECT ticket_id, username, initial_battery_level, energy_used, total_amount, reference_number 
FROM charging_history 
ORDER BY completed_at DESC 
LIMIT 10;
