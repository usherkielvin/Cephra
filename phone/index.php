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
            <div id="queue"></div>
            <div class="nav-buttons">
                <button class="nav-btn" onclick="createTicket('Fast Charging')">Fast Charge</button>
                <button class="nav-btn" onclick="createTicket('Normal Charging')">Normal Charge</button>
            </div>
        </div>
    </div>

    <script>
        function login() {
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;
            
            const formData = new FormData();
            formData.append('username', username);
            formData.append('password', password);
            
            fetch('http://localhost/cephra/api/login', {
                method: 'POST',
                body: formData
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    document.getElementById('loginForm').style.display = 'none';
                    document.getElementById('homePage').style.display = 'block';
                    document.getElementById('welcomeUser').textContent = username;
                    loadQueue();
                } else {
                    alert('Login failed: ' + data.error);
                }
            })
            .catch(error => alert('Error: ' + error.message));
        }
        
        function loadQueue() {
            fetch('http://localhost/cephra/api/queue')
            .then(response => response.json())
            .then(data => {
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
            })
            .catch(error => console.error('Error loading queue:', error));
        }
        
        function createTicket(serviceType) {
            const username = document.getElementById('username').value;
            const ticketData = {
                ticket_id: 'FCH' + Date.now(),
                username: username,
                service_type: serviceType,
                initial_battery_level: 25
            };
            
            fetch('http://localhost/cephra/api/create-ticket', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(ticketData)
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('Ticket created: ' + data.ticket_id);
                    loadQueue();
                } else {
                    alert('Error: ' + data.error);
                }
            })
            .catch(error => alert('Error: ' + error.message));
        }
    </script>
</body>
</html>
