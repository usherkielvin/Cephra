// Cephra Phone Interface JavaScript

// Get the current host and path to build correct API URLs
const currentHost = window.location.protocol + '//' + window.location.host;
const apiBase = currentHost + '/cephra/api/mobile.php';

console.log('API Base URL:', apiBase);

let queueRefreshInterval; // For real-time updates

// Login function
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
        return response.text(); // Get raw response first
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

// Start real-time queue updates
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

// Load queue data
function loadQueue() {
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

// Update queue display
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

// Update last updated timestamp
function updateLastUpdated() {
    const now = new Date();
    const timeString = now.toLocaleTimeString();
    document.getElementById('lastUpdated').textContent = timeString;
}

// Create new ticket
function createTicket(serviceType) {
    const username = document.getElementById('username').value;
    
    // Use FormData instead of JSON for better compatibility
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

// Logout function
function logout() {
    // Clear any intervals
    if (queueRefreshInterval) {
        clearInterval(queueRefreshInterval);
    }
    
    // Reset display
    document.getElementById('homePage').style.display = 'none';
    document.getElementById('loginForm').style.display = 'block';
    document.getElementById('username').value = '';
    document.getElementById('password').value = '';
    
    console.log('Logged out successfully');
}

// Add logout button event listener when page loads
document.addEventListener('DOMContentLoaded', function() {
    // Add logout button if it doesn't exist
    if (!document.getElementById('logoutBtn')) {
        const logoutBtn = document.createElement('button');
        logoutBtn.id = 'logoutBtn';
        logoutBtn.textContent = 'Logout';
        logoutBtn.className = 'nav-btn';
        logoutBtn.style.background = 'linear-gradient(135deg, #dc3545 0%, #c82333 100%)';
        logoutBtn.onclick = logout;
        
        // Insert logout button after nav-buttons
        const navButtons = document.querySelector('.nav-buttons');
        if (navButtons) {
            navButtons.parentNode.insertBefore(logoutBtn, navButtons.nextSibling);
        }
    }
});

// Clean up interval when page is closed
window.addEventListener('beforeunload', () => {
    if (queueRefreshInterval) {
        clearInterval(queueRefreshInterval);
    }
});

// Utility function to show messages
function showMessage(message, type = 'success') {
    const messageDiv = document.createElement('div');
    messageDiv.className = type === 'success' ? 'success-message' : 'error-message';
    messageDiv.textContent = message;
    
    // Insert at top of home page
    const homePage = document.getElementById('homePage');
    homePage.insertBefore(messageDiv, homePage.firstChild);
    
    // Remove after 5 seconds
    setTimeout(() => {
        if (messageDiv.parentNode) {
            messageDiv.parentNode.removeChild(messageDiv);
        }
    }, 5000);
}

// Add loading state to buttons
function setLoadingState(button, isLoading) {
    if (isLoading) {
        button.classList.add('loading');
        button.disabled = true;
        button.textContent = 'Loading...';
    } else {
        button.classList.remove('loading');
        button.disabled = false;
        button.textContent = button.dataset.originalText || button.textContent;
    }
}
