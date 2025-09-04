<!DOCTYPE html>
<html>
<head>
    <title>Cephra Database - Queue Tickets</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
        .container { max-width: 800px; margin: 0 auto; background: white; padding: 20px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        h1 { color: #333; text-align: center; }
        .ticket { background: #f8f9fa; padding: 15px; margin: 10px 0; border-radius: 8px; border-left: 4px solid #007bff; }
        .ticket-id { font-weight: bold; color: #007bff; font-size: 18px; }
        .ticket-details { margin: 10px 0; color: #666; }
        .status { display: inline-block; padding: 4px 8px; border-radius: 4px; font-size: 12px; font-weight: bold; }
        .status-pending { background: #fff3cd; color: #856404; }
        .status-in-progress { background: #d1ecf1; color: #0c5460; }
        .status-completed { background: #d4edda; color: #155724; }
        .no-tickets { text-align: center; color: #666; font-style: italic; padding: 40px; }
        .refresh-btn { background: #28a745; color: white; border: none; padding: 10px 20px; border-radius: 5px; cursor: pointer; margin: 20px 0; }
        .refresh-btn:hover { background: #218838; }
        .db-info { background: #e9ecef; padding: 15px; border-radius: 5px; margin: 20px 0; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🎫 Cephra Database - Queue Tickets</h1>
        
        <div class="db-info">
            <strong>Database:</strong> cephra (Real Database)<br>
            <strong>Table:</strong> queue_tickets<br>
            <strong>Last Updated:</strong> <span id="lastUpdate">Loading...</span>
        </div>
        
        <button class="refresh-btn" onclick="loadQueue()">🔄 Refresh Queue</button>
        
        <div id="queueContainer">
            <div class="no-tickets">Loading queue tickets from Cephra database...</div>
        </div>
    </div>

    <script>
        function loadQueue() {
            const container = document.getElementById('queueContainer');
            container.innerHTML = '<div class="no-tickets">Loading...</div>';
            
            fetch('api/mobile.php?action=queue')
            .then(response => response.json())
            .then(data => {
                console.log('Cephra database queue data:', data);
                displayQueue(data);
                updateLastUpdate();
            })
            .catch(error => {
                console.error('Error loading queue from Cephra database:', error);
                container.innerHTML = '<div class="no-tickets">Error loading queue data from Cephra database</div>';
            });
        }
        
        function displayQueue(tickets) {
            const container = document.getElementById('queueContainer');
            
            if (!tickets || tickets.length === 0) {
                container.innerHTML = '<div class="no-tickets">No tickets found in Cephra database queue</div>';
                return;
            }
            
            const ticketsHtml = tickets.map(ticket => {
                const statusClass = getStatusClass(ticket.status);
                return `
                    <div class="ticket">
                        <div class="ticket-id">${ticket.ticket_id}</div>
                        <div class="ticket-details">
                            <strong>User:</strong> ${ticket.username}<br>
                            <strong>Service:</strong> ${ticket.service_type}<br>
                            <strong>Status:</strong> <span class="status ${statusClass}">${ticket.status}</span><br>
                            <strong>Payment:</strong> <span class="status ${getStatusClass(ticket.payment_status)}">${ticket.payment_status}</span>
                        </div>
                    </div>
                `;
            }).join('');
            
            container.innerHTML = ticketsHtml;
        }
        
        function getStatusClass(status) {
            switch(status.toLowerCase()) {
                case 'pending': return 'status-pending';
                case 'in progress': return 'status-in-progress';
                case 'completed': return 'status-completed';
                default: return 'status-pending';
            }
        }
        
        function updateLastUpdate() {
            const now = new Date();
            document.getElementById('lastUpdate').textContent = now.toLocaleString();
        }
        
        // Load queue when page loads
        loadQueue();
        
        // Auto-refresh every 10 seconds
        setInterval(loadQueue, 10000);
    </script>
</body>
</html>
