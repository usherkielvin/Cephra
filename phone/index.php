<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cephra Phone</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: Arial, sans-serif; background: #f0f0f0; padding: 20px; }
        .container { max-width: 400px; margin: 0 auto; background: white; border-radius: 10px; padding: 20px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        .login-form { display: block; }
        .home-page { display: none; }
        input, button { width: 100%; padding: 10px; margin: 10px 0; border: 1px solid #ddd; border-radius: 5px; }
        button { background: #007bff; color: white; cursor: pointer; }
        button:hover { background: #0056b3; }
        .queue-item { background: #f8f9fa; padding: 10px; margin: 10px 0; border-radius: 5px; border: 1px solid #dee2e6; }
        .nav-buttons { display: flex; gap: 10px; margin-top: 20px; }
        .nav-btn { flex: 1; padding: 15px; background: #28a745; color: white; border: none; border-radius: 5px; cursor: pointer; }
        .queue-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; }
        .refresh-info { font-size: 12px; color: #666; }
        .last-updated { font-size: 11px; color: #999; text-align: right; margin-top: 10px; }
    </style>
</head>
<body>
    <div class="container">
        <div id="loginForm" class="login-form">
            <h2>Login</h2>
            <input type="text" id="username" placeholder="Username" value="dizon">
            <input type="password" id="password" placeholder="Password" value="123">
            <button onclick="login()">Login</button>
        </div>
        
        <div id="homePage" class="home-page">
            <h2>Welcome <span id="welcomeUser"></span>!</h2>
            
            <div class="queue-header">
                <h3>Live Queue</h3>
                <div class="refresh-info">Auto-refresh every 5s</div>
            </div>
            
            <div id="queue"></div>
            
            <div class="nav-buttons">
                <button class="nav-btn" onclick="createTicket('Fast Charging')">Fast Charge</button>
                <button class="nav-btn" onclick="createTicket('Normal Charging')">Normal Charge</button>
            </div>
            
            <div class="last-updated">
                Last updated: <span id="lastUpdated">Never</span>
            </div>
        </div>
    </div>

    <script>
        // Get the current host and path to build correct API URLs
        const currentHost = window.location.protocol + '//' + window.location.host;
        const apiBase = currentHost + '/cephra/api/mobile.php';
        
        let queueRefreshInterval; // For real-time updates
        
        console.log('API Base URL:', apiBase);
        
        function login() {
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;
            
            const formData = new FormData();
            formData.append('action', 'login');
            formData.append('username', username);
            formData.append('password', password);
            
            console.log('Sending login request to:', apiBase);
            
            fetch(apiBase, {
                method: 'POST',
                body: formData
            })
            .then(response => {
                console.log('Response status:', response.status);
                console.log('Response headers:', response.headers);
                return response.text();
            })
            .then(text => {
                console.log('Raw response:', text);
                try {
                    const data = JSON.parse(text);
                    console.log('Parsed response:', data);
                    if (data.success) {
                        document.getElementById('loginForm').style.display = 'none';
                        document.getElementById('homePage').style.display = 'block';
                        document.getElementById('welcomeUser').textContent = username;
                        
                        // Start real-time queue updates
                        startQueueUpdates();
                        
                        // Load initial queue
                        loadQueue();
                    } else {
                        alert('Login failed: ' + (data.error || 'Unknown error'));
                    }
                } catch (e) {
                    console.error('JSON parse error:', e);
                    alert('Error: Invalid response from server. Check console for details.');
                }
            })
            .catch(error => {
                console.error('Fetch error:', error);
                alert('Error: ' + error.message);
            });
        }
        
        function startQueueUpdates() {
            // Clear any existing interval
            if (queueRefreshInterval) {
                clearInterval(queueRefreshInterval);
            }
            
            // Start auto-refresh every 5 seconds
            queueRefreshInterval = setInterval(() => {
                loadQueue();
            }, 5000);
            
            console.log('Started real-time queue updates (every 5 seconds)');
        }
        
        function loadQueue() {
            console.log('Loading queue from:', apiBase + '?action=queue');
            
            fetch(apiBase + '?action=queue')
            .then(response => response.text())
            .then(text => {
                console.log('Queue raw response:', text);
                try {
                    const data = JSON.parse(text);
                    console.log('Queue parsed response:', data);
                    updateQueueDisplay(data);
                    updateLastUpdated();
                } catch (e) {
                    console.error('Queue JSON parse error:', e);
                    document.getElementById('queue').innerHTML = '<div class="queue-item">Error loading queue</div>';
                }
            })
            .catch(error => console.error('Error loading queue:', error));
        }
        
        function updateQueueDisplay(data) {
            const queueDiv = document.getElementById('queue');
            if (data.length > 0) {
                queueDiv.innerHTML = data.map(ticket => 
                    `<div class="queue-item">
                        <strong>${ticket.ticket_id}</strong> - ${ticket.status}<br>
                        ${ticket.username} • ${ticket.service_type} • ${ticket.payment_status}
                    </div>`
                ).join('');
            } else {
                queueDiv.innerHTML = '<div class="queue-item">No tickets in queue</div>';
            }
        }
        
        function updateLastUpdated() {
            const now = new Date();
            const timeString = now.toLocaleTimeString();
            document.getElementById('lastUpdated').textContent = timeString;
        }
        
        function createTicket(serviceType) {
            const username = document.getElementById('username').value;
            
            const formData = new FormData();
            formData.append('action', 'create-ticket');
            formData.append('ticket_id', 'FCH' + Date.now());
            formData.append('username', username);
            formData.append('service_type', serviceType);
            formData.append('initial_battery_level', '25');
            
            console.log('Creating ticket via:', apiBase);
            console.log('Ticket data:', {
                action: 'create-ticket',
                ticket_id: 'FCH' + Date.now(),
                username: username,
                service_type: serviceType,
                initial_battery_level: '25'
            });
            
            fetch(apiBase, {
                method: 'POST',
                body: formData
            })
            .then(response => response.text())
            .then(text => {
                console.log('Create ticket raw response:', text);
                try {
                    const data = JSON.parse(text);
                    console.log('Create ticket parsed response:', data);
                    if (data.success) {
                        alert('Ticket created: ' + data.ticket_id);
                        // Queue will auto-refresh in 5 seconds, or refresh immediately
                        loadQueue();
                    } else {
                        alert('Error: ' + (data.error || 'Unknown error'));
                    }
                } catch (e) {
                    console.error('Create ticket JSON parse error:', e);
                    alert('Error: Invalid response from server');
                }
            })
            .catch(error => alert('Error: ' + error.message));
        }
        
        // Clean up interval when page is closed
        window.addEventListener('beforeunload', () => {
            if (queueRefreshInterval) {
                clearInterval(queueRefreshInterval);
            }
        });
    </script>
</body>
</html>
