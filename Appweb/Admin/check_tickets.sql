SELECT ticket_id, username, status, created_at FROM queue_tickets WHERE status IN ('Pending', 'Waiting', 'Charging') ORDER BY created_at ASC LIMIT 5;
