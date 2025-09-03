console.log('Phone interface script starting...');

// Wait for page to load completely
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM loaded, setting up phone interface...');
    
    // Get all the elements we need
    var loginForm = document.getElementById('loginForm');
    var homePage = document.getElementById('homePage');
    var loginButton = document.getElementById('loginButton');
    var testButton = document.getElementById('testButton');
    var usernameInput = document.getElementById('username');
    var passwordInput = document.getElementById('password');
    var welcomeText = document.getElementById('welcome');
    var chargeBtn = document.getElementById('chargeBtn');
    var linkBtn = document.getElementById('linkBtn');
    var historyBtn = document.getElementById('historyBtn');
    var profileBtn = document.getElementById('profileBtn');
    
    console.log('Elements found:', {
        loginForm: !!loginForm,
        homePage: !!homePage,
        loginButton: !!loginButton,
        testButton: !!testButton,
        usernameInput: !!usernameInput,
        passwordInput: !!passwordInput
    });
    
    // Test API connection function
    function testApiConnection() {
        console.log('Testing API connection...');
        
        fetch('/api/test-login', {
            method: 'POST'
        })
        .then(function(response) {
            console.log('Test response received:', response.status);
            return response.json();
        })
        .then(function(data) {
            console.log('Test data:', data);
            alert('API Connection Test: ' + data.message);
        })
        .catch(function(error) {
            console.error('Test error:', error);
            alert('API Test Failed: ' + error.message);
        });
    }
    
    // Login function
    function doLogin() {
        console.log('Login function called');
        
        var username = usernameInput.value;
        var password = passwordInput.value;
        
        console.log('Login attempt for:', username);
        
        if (!username || !password) {
            alert('Please enter both username and password');
            return;
        }
        
        console.log('Sending login request...');
        
        fetch('/api/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: 'username=' + encodeURIComponent(username) + '&password=' + encodeURIComponent(password)
        })
        .then(function(response) {
            console.log('Login response received:', response.status);
            return response.json();
        })
        .then(function(data) {
            console.log('Login data:', data);
            
            if (data.success) {
                console.log('Login successful, showing home page');
                
                // Hide login form
                loginForm.style.display = 'none';
                
                // Show home page
                homePage.style.display = 'block';
                
                // Update welcome message
                welcomeText.textContent = 'Hi ' + username + '!';
                
                // Load queue data
                loadQueueData();
                
            } else {
                console.log('Login failed:', data.message);
                alert(data.message || 'Login failed');
            }
        })
        .catch(function(error) {
            console.error('Login error:', error);
            alert('Network error: ' + error.message);
        });
    }
    
    // Load queue data function
    function loadQueueData() {
        console.log('Loading queue data...');
        
        fetch('/api/queue')
        .then(function(response) {
            return response.json();
        })
        .then(function(data) {
            console.log('Queue data received:', data);
            
            var queueContainer = document.getElementById('queue');
            var html = '';
            
            if (data && data.length > 0) {
                for (var i = 0; i < data.length; i++) {
                    var ticket = data[i];
                    html += '<div class="queue-item">';
                    html += '<div class="queue-title">' + (ticket.ticket || '') + ' - ' + (ticket.status || '') + '</div>';
                    html += '<div class="queue-details">' + (ticket.customer || '') + ' • ' + (ticket.service || '') + ' • ' + (ticket.payment || '') + '</div>';
                    html += '</div>';
                }
            } else {
                html = '<div class="queue-item"><div class="queue-title">No queue items</div></div>';
            }
            
            queueContainer.innerHTML = html;
        })
        .catch(function(error) {
            console.error('Error loading queue:', error);
            document.getElementById('queue').innerHTML = '<div class="queue-item"><div class="queue-title">Error loading queue</div></div>';
        });
    }
    
    // Create new ticket function
    function createTicket(serviceType) {
        var username = usernameInput.value || 'guest';
        console.log('Creating ticket for service:', serviceType, 'user:', username);
        
        // Generate ticket ID based on service type
        var ticketId = generateTicketId(serviceType);
        
        var ticketData = {
            ticket_id: ticketId,
            username: username,
            service_type: serviceType,
            status: 'Pending',
            payment_status: 'Pending',
            initial_battery_level: 20 // Default battery level
        };
        
        console.log('Sending ticket creation request:', ticketData);
        
        // Send ticket creation request to API
        fetch('/api/create-ticket', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(ticketData)
        })
        .then(function(response) {
            console.log('Ticket creation response:', response.status);
            return response.json();
        })
        .then(function(data) {
            console.log('Ticket creation result:', data);
            if (data.success) {
                alert('Ticket ' + ticketId + ' created successfully!\nService: ' + serviceType + '\nStatus: Pending');
                // Reload queue data to show new ticket
                loadQueueData();
            } else {
                alert('Failed to create ticket: ' + (data.message || 'Unknown error'));
            }
        })
        .catch(function(error) {
            console.error('Ticket creation error:', error);
            alert('Error creating ticket: ' + error.message);
        });
    }
    
    // Generate ticket ID based on service type
    function generateTicketId(serviceType) {
        var timestamp = Date.now();
        var random = Math.floor(Math.random() * 1000);
        
        if (serviceType.toLowerCase().includes('fast')) {
            return 'FCH' + timestamp.toString().slice(-6) + random.toString().padStart(3, '0');
        } else if (serviceType.toLowerCase().includes('normal')) {
            return 'NCH' + timestamp.toString().slice(-6) + random.toString().padStart(3, '0');
        } else {
            return 'GEN' + timestamp.toString().slice(-6) + random.toString().padStart(3, '0');
        }
    }
    
    // Button click functions
    function showCharge() {
        console.log('Charge button clicked');
        
        // Show service selection dialog
        var serviceChoice = confirm('⚡ Charge Service\n\nSelect service type:\n\nOK = Fast Charging\nCancel = Normal Charging');
        
        if (serviceChoice !== null) {
            var serviceType = serviceChoice ? 'Fast Charging' : 'Normal Charging';
            createTicket(serviceType);
        }
    }
    
    function showLink() {
        console.log('Link button clicked');
        alert('🔗 Link Service\n\nConnecting your vehicle...\n\nVehicle linked successfully!');
    }
    
    function showHistory() {
        console.log('History button clicked');
        alert('📅 History\n\nShowing your charging history...\n\nNo previous sessions found.');
    }
    
    function showProfile() {
        console.log('Profile button clicked');
        alert('👤 Profile\n\nUsername: ' + (usernameInput.value || 'Guest') + '\nBattery Level: 20%\nVehicle: Tesla Model 3');
    }
    
    // Attach event listeners
    console.log('Attaching event listeners...');
    
    // Test button click
    testButton.addEventListener('click', testApiConnection);
    console.log('Test button listener attached');
    
    // Login button click
    loginButton.addEventListener('click', doLogin);
    console.log('Login button listener attached');
    
    // Enter key in username field
    usernameInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            passwordInput.focus();
        }
    });
    
    // Enter key in password field
    passwordInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            doLogin();
        }
    });
    
    // Navigation button clicks
    chargeBtn.addEventListener('click', showCharge);
    linkBtn.addEventListener('click', showLink);
    historyBtn.addEventListener('click', showHistory);
    profileBtn.addEventListener('click', showProfile);
    
    console.log('All event listeners attached successfully');
    console.log('Phone interface ready!');
});
