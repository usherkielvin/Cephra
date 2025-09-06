-- Add demo ticket FCH001 for user dizon as requested
USE cephradb;

-- Insert demo ticket FCH001 for user dizon with Fast Charging service and Pending status
INSERT IGNORE INTO queue_tickets (ticket_id, username, service_type, status, payment_status, initial_battery_level, priority) VALUES
('FCH001', 'dizon', 'Fast', 'Pending', 'Pending', 30, 1);

-- Verify the ticket was added
SELECT ticket_id, username, service_type, status, payment_status, initial_battery_level, created_at 
FROM queue_tickets 
WHERE ticket_id = 'FCH001';

-- Show all queue tickets
SELECT ticket_id, username, service_type, status, payment_status, initial_battery_level, created_at 
FROM queue_tickets 
ORDER BY created_at DESC;
