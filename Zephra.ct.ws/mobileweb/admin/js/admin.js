// Admin Panel JavaScript
class AdminPanel {
    constructor() {
        this.currentPanel = 'dashboard';
        this.refreshInterval = null;
        this.init();
    }

    init() {
        this.setupEventListeners();
        this.updateCurrentTime();
        this.loadDashboardData();
        this.startAutoRefresh();
    }

    setupEventListeners() {
        // Sidebar navigation
        document.querySelectorAll('.sidebar-menu li').forEach(item => {
            item.addEventListener('click', (e) => {
                const panel = e.currentTarget.dataset.panel;
                this.switchPanel(panel);
            });
        });

        // Sidebar toggle for mobile
        const sidebarToggle = document.querySelector('.sidebar-toggle');
        if (sidebarToggle) {
            sidebarToggle.addEventListener('click', () => {
                document.querySelector('.sidebar').classList.toggle('active');
            });
        }

        // Modal close buttons
        document.querySelectorAll('.modal-close').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const modal = e.target.closest('.modal');
                this.closeModal(modal.id);
            });
        });

        // Close modal on outside click
        document.querySelectorAll('.modal').forEach(modal => {
            modal.addEventListener('click', (e) => {
                if (e.target === modal) {
                    this.closeModal(modal.id);
                }
            });
        });

        // Queue filters
        const statusFilter = document.getElementById('status-filter');
        const serviceFilter = document.getElementById('service-filter');
        
        if (statusFilter) {
            statusFilter.addEventListener('change', () => this.filterQueue());
        }
        if (serviceFilter) {
            serviceFilter.addEventListener('change', () => this.filterQueue());
        }
    }

    switchPanel(panelName) {
        // Update sidebar active state
        document.querySelectorAll('.sidebar-menu li').forEach(item => {
            item.classList.remove('active');
        });
        document.querySelector(`[data-panel="${panelName}"]`).classList.add('active');

        // Update panel visibility
        document.querySelectorAll('.panel').forEach(panel => {
            panel.classList.remove('active');
        });
        document.getElementById(`${panelName}-panel`).classList.add('active');

        // Update page title
        const titles = {
            'dashboard': 'Dashboard',
            'queue': 'Queue Management',
            'bays': 'Charging Bays',
            'users': 'User Management',
            'analytics': 'Analytics & Reports',
            'settings': 'System Settings'
        };
        document.getElementById('page-title').textContent = titles[panelName];

        this.currentPanel = panelName;

        // Load panel-specific data
        switch (panelName) {
            case 'dashboard':
                this.loadDashboardData();
                break;
            case 'queue':
                this.loadQueueData();
                break;
            case 'bays':
                this.loadBaysData();
                break;
            case 'users':
                this.loadUsersData();
                break;
            case 'analytics':
                this.loadAnalyticsData();
                break;
            case 'settings':
                this.loadSettingsData();
                break;
        }
    }

    async loadDashboardData() {
        try {
            const response = await fetch('../api/admin.php?action=dashboard');
            const data = await response.json();

            if (data.success) {
                this.updateDashboardStats(data.stats);
                this.updateRecentActivity(data.recent_activity);
            }
        } catch (error) {
            console.error('Error loading dashboard data:', error);
            this.showError('Failed to load dashboard data');
        }
    }

    updateDashboardStats(stats) {
        document.getElementById('total-users').textContent = stats.total_users || 0;
        document.getElementById('queue-count').textContent = stats.queue_count || 0;
        document.getElementById('active-bays').textContent = stats.active_bays || 0;
        document.getElementById('revenue-today').textContent = `$${stats.revenue_today || 0}`;
    }

    updateRecentActivity(activities) {
        const container = document.getElementById('recent-activity');
        if (!activities || activities.length === 0) {
            container.innerHTML = '<div class="activity-item"><i class="fas fa-info-circle"></i><span>No recent activity</span></div>';
            return;
        }

        container.innerHTML = activities.map(activity => `
            <div class="activity-item">
                <i class="fas fa-${activity.icon || 'info-circle'}"></i>
                <span>${activity.description}</span>
                <small>${this.formatDateTime(activity.time || activity.created_at)}</small>
            </div>
        `).join('');
    }

    async loadQueueData() {
        try {
            const response = await fetch('../api/admin.php?action=queue');
            const data = await response.json();

            if (data.success) {
                this.updateQueueTable(data.queue);
            }
        } catch (error) {
            console.error('Error loading queue data:', error);
            this.showError('Failed to load queue data');
        }
    }

    updateQueueTable(queue) {
        const tbody = document.getElementById('queue-tbody');
        if (!queue || queue.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" class="loading"><i class="fas fa-info-circle"></i> No tickets in queue</td></tr>';
            return;
        }

        tbody.innerHTML = queue.map(ticket => `
            <tr>
                <td>${ticket.ticket_id}</td>
                <td>${ticket.username}</td>
                <td>${ticket.service_type}</td>
                <td><span class="status-badge ${ticket.status.toLowerCase()}">${ticket.status}</span></td>
                <td><span class="status-badge ${ticket.payment_status.toLowerCase()}">${ticket.payment_status}</span></td>
                <td>${this.formatDateTime(ticket.created_at)}</td>
                <td>
                    <button class="btn btn-primary btn-sm" onclick="adminPanel.viewTicket('${ticket.ticket_id}')">
                        <i class="fas fa-eye"></i> View
                    </button>
                    ${ticket.status === 'Waiting' ? `
                        <button class="btn btn-success btn-sm" onclick="adminPanel.processTicket('${ticket.ticket_id}')">
                            <i class="fas fa-play"></i> Process
                        </button>
                    ` : ''}
                </td>
            </tr>
        `).join('');
    }

    async loadBaysData() {
        try {
            const response = await fetch('../api/admin.php?action=bays');
            const data = await response.json();

            if (data.success) {
                this.updateBaysGrid(data.bays);
            }
        } catch (error) {
            console.error('Error loading bays data:', error);
            this.showError('Failed to load bays data');
        }
    }

    updateBaysGrid(bays) {
        const container = document.getElementById('bays-grid');
        if (!bays || bays.length === 0) {
            container.innerHTML = '<div class="loading"><i class="fas fa-info-circle"></i> No bays available</div>';
            return;
        }

        container.innerHTML = bays.map(bay => `
            <div class="bay-card ${bay.status.toLowerCase()}">
                <div class="bay-header">
                    <span class="bay-number">Bay ${bay.bay_number}</span>
                    <span class="bay-status ${bay.status.toLowerCase()}">${bay.status}</span>
                </div>
                <div class="bay-info">
                    <div class="bay-info-item">
                        <span class="bay-info-label">Type:</span>
                        <span class="bay-info-value">${bay.bay_type}</span>
                    </div>
                    <div class="bay-info-item">
                        <span class="bay-info-label">Current User:</span>
                        <span class="bay-info-value">${bay.current_username || 'None'}</span>
                    </div>
                    <div class="bay-info-item">
                        <span class="bay-info-label">Ticket ID:</span>
                        <span class="bay-info-value">${bay.current_ticket_id || 'None'}</span>
                    </div>
                    <div class="bay-info-item">
                        <span class="bay-info-label">Start Time:</span>
                        <span class="bay-info-value">${bay.start_time ? this.formatDateTime(bay.start_time) : 'N/A'}</span>
                    </div>
                </div>
                <div class="bay-actions" style="margin-top: 15px;">
                    ${bay.status === 'Available' ? `
                        <button class="btn btn-warning btn-sm" onclick="adminPanel.setBayMaintenance(${bay.bay_number})">
                            <i class="fas fa-tools"></i> Maintenance
                        </button>
                    ` : bay.status === 'Maintenance' ? `
                        <button class="btn btn-success btn-sm" onclick="adminPanel.setBayAvailable(${bay.bay_number})">
                            <i class="fas fa-check"></i> Available
                        </button>
                    ` : ''}
                </div>
            </div>
        `).join('');
    }

    async loadUsersData() {
        try {
            const response = await fetch('../api/admin.php?action=users');
            const data = await response.json();

            if (data.success) {
                this.updateUsersTable(data.users);
            }
        } catch (error) {
            console.error('Error loading users data:', error);
            this.showError('Failed to load users data');
        }
    }

    updateUsersTable(users) {
        const tbody = document.getElementById('users-tbody');
        if (!users || users.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" class="loading"><i class="fas fa-info-circle"></i> No users found</td></tr>';
            return;
        }

        tbody.innerHTML = users.map(user => `
            <tr>
                <td>${user.username}</td>
                <td>${user.firstname}</td>
                <td>${user.lastname}</td>
                <td>${user.email}</td>
                <td>${this.formatDateTime(user.created_at)}</td>
                <td>
                    <button class="btn btn-primary btn-sm" onclick="adminPanel.viewUser('${user.username}')">
                        <i class="fas fa-eye"></i> View
                    </button>
                    <button class="btn btn-danger btn-sm" onclick="adminPanel.deleteUser('${user.username}')">
                        <i class="fas fa-trash"></i> Delete
                    </button>
                </td>
            </tr>
        `).join('');
    }

    async loadAnalyticsData() {
        // Placeholder for analytics data
        console.log('Loading analytics data...');
    }

    async loadSettingsData() {
        try {
            const response = await fetch('../api/admin.php?action=settings');
            const data = await response.json();

            if (data.success) {
                document.getElementById('fast-charge-price').value = data.settings.fast_charge_price || 0;
                document.getElementById('normal-charge-price').value = data.settings.normal_charge_price || 0;
            }
        } catch (error) {
            console.error('Error loading settings data:', error);
        }
    }

    async viewTicket(ticketId) {
        try {
            const response = await fetch(`../api/admin.php?action=ticket-details&ticket_id=${ticketId}`);
            const data = await response.json();

            if (data.success) {
                this.showTicketModal(data.ticket);
            }
        } catch (error) {
            console.error('Error loading ticket details:', error);
            this.showError('Failed to load ticket details');
        }
    }

    showTicketModal(ticket) {
        const modalBody = document.getElementById('ticket-modal-body');
        modalBody.innerHTML = `
            <div class="ticket-details">
                <div class="detail-row">
                    <strong>Ticket ID:</strong> ${ticket.ticket_id}
                </div>
                <div class="detail-row">
                    <strong>Username:</strong> ${ticket.username}
                </div>
                <div class="detail-row">
                    <strong>Service Type:</strong> ${ticket.service_type}
                </div>
                <div class="detail-row">
                    <strong>Status:</strong> <span class="status-badge ${ticket.status.toLowerCase()}">${ticket.status}</span>
                </div>
                <div class="detail-row">
                    <strong>Payment Status:</strong> <span class="status-badge ${ticket.payment_status.toLowerCase()}">${ticket.payment_status}</span>
                </div>
                <div class="detail-row">
                    <strong>Battery Level:</strong> ${ticket.initial_battery_level}%
                </div>
                <div class="detail-row">
                    <strong>Created:</strong> ${this.formatDateTime(ticket.created_at)}
                </div>
            </div>
        `;
        this.showModal('ticket-modal');
    }

    async processTicket(ticketId) {
        if (!confirm('Are you sure you want to process this ticket?')) {
            return;
        }

        try {
            const response = await fetch('../api/admin.php', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `action=process-ticket&ticket_id=${ticketId}`
            });
            const data = await response.json();

            if (data.success) {
                this.showSuccess('Ticket processed successfully');
                this.loadQueueData();
            } else {
                this.showError(data.message || 'Failed to process ticket');
            }
        } catch (error) {
            console.error('Error processing ticket:', error);
            this.showError('Failed to process ticket');
        }
    }

    async setBayMaintenance(bayNumber) {
        if (!confirm(`Are you sure you want to set Bay ${bayNumber} to maintenance mode?`)) {
            return;
        }

        try {
            const response = await fetch('../api/admin.php', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `action=set-bay-maintenance&bay_number=${bayNumber}`
            });
            const data = await response.json();

            if (data.success) {
                this.showSuccess('Bay set to maintenance mode');
                this.loadBaysData();
            } else {
                this.showError(data.message || 'Failed to set bay to maintenance');
            }
        } catch (error) {
            console.error('Error setting bay to maintenance:', error);
            this.showError('Failed to set bay to maintenance');
        }
    }

    async setBayAvailable(bayNumber) {
        if (!confirm(`Are you sure you want to set Bay ${bayNumber} to available?`)) {
            return;
        }

        try {
            const response = await fetch('../api/admin.php', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `action=set-bay-available&bay_number=${bayNumber}`
            });
            const data = await response.json();

            if (data.success) {
                this.showSuccess('Bay set to available');
                this.loadBaysData();
            } else {
                this.showError(data.message || 'Failed to set bay to available');
            }
        } catch (error) {
            console.error('Error setting bay to available:', error);
            this.showError('Failed to set bay to available');
        }
    }

    showAddUserModal() {
        document.getElementById('add-user-form').reset();
        this.showModal('add-user-modal');
    }

    async addUser() {
        const form = document.getElementById('add-user-form');
        const formData = new FormData(form);
        
        const userData = {
            username: document.getElementById('new-username').value,
            firstname: document.getElementById('new-firstname').value,
            lastname: document.getElementById('new-lastname').value,
            email: document.getElementById('new-email').value,
            password: document.getElementById('new-password').value
        };

        if (!userData.username || !userData.firstname || !userData.lastname || !userData.email || !userData.password) {
            this.showError('Please fill in all fields');
            return;
        }

        try {
            const response = await fetch('../api/admin.php', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `action=add-user&${new URLSearchParams(userData).toString()}`
            });
            const data = await response.json();

            if (data.success) {
                this.showSuccess('User added successfully');
                this.closeModal('add-user-modal');
                this.loadUsersData();
            } else {
                this.showError(data.message || 'Failed to add user');
            }
        } catch (error) {
            console.error('Error adding user:', error);
            this.showError('Failed to add user');
        }
    }

    async savePricingSettings() {
        const fastPrice = document.getElementById('fast-charge-price').value;
        const normalPrice = document.getElementById('normal-charge-price').value;

        if (!fastPrice || !normalPrice) {
            this.showError('Please enter both pricing values');
            return;
        }

        try {
            const response = await fetch('../api/admin.php', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `action=save-settings&fast_charge_price=${fastPrice}&normal_charge_price=${normalPrice}`
            });
            const data = await response.json();

            if (data.success) {
                this.showSuccess('Settings saved successfully');
            } else {
                this.showError(data.message || 'Failed to save settings');
            }
        } catch (error) {
            console.error('Error saving settings:', error);
            this.showError('Failed to save settings');
        }
    }

    filterQueue() {
        const statusFilter = document.getElementById('status-filter').value;
        const serviceFilter = document.getElementById('service-filter').value;
        
        // Simple client-side filtering
        const rows = document.querySelectorAll('#queue-tbody tr');
        rows.forEach(row => {
            const status = row.cells[3].textContent.trim();
            const service = row.cells[2].textContent.trim();
            
            let showRow = true;
            
            if (statusFilter && status !== statusFilter) {
                showRow = false;
            }
            
            if (serviceFilter && service !== serviceFilter) {
                showRow = false;
            }
            
            row.style.display = showRow ? '' : 'none';
        });
    }

    showModal(modalId) {
        document.getElementById(modalId).classList.add('active');
    }

    closeModal(modalId) {
        document.getElementById(modalId).classList.remove('active');
    }

    showSuccess(message) {
        this.showNotification(message, 'success');
    }

    showError(message) {
        this.showNotification(message, 'error');
    }

    showNotification(message, type) {
        // Create notification element
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.innerHTML = `
            <i class="fas fa-${type === 'success' ? 'check-circle' : 'exclamation-triangle'}"></i>
            <span>${message}</span>
        `;
        
        // Add styles
        notification.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            background: ${type === 'success' ? '#d4edda' : '#f8d7da'};
            color: ${type === 'success' ? '#155724' : '#721c24'};
            padding: 15px 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            z-index: 3000;
            display: flex;
            align-items: center;
            gap: 10px;
            animation: slideIn 0.3s ease;
        `;
        
        document.body.appendChild(notification);
        
        // Remove after 3 seconds
        setTimeout(() => {
            notification.style.animation = 'slideOut 0.3s ease';
            setTimeout(() => {
                if (notification.parentNode) {
                    notification.parentNode.removeChild(notification);
                }
            }, 300);
        }, 3000);
    }

    updateCurrentTime() {
        const timeElement = document.getElementById('current-time');
        if (timeElement) {
            const now = new Date();
            timeElement.textContent = now.toLocaleString();
        }
    }

    startAutoRefresh() {
        // Refresh current panel data every 30 seconds
        this.refreshInterval = setInterval(() => {
            switch (this.currentPanel) {
                case 'dashboard':
                    this.loadDashboardData();
                    break;
                case 'queue':
                    this.loadQueueData();
                    break;
                case 'bays':
                    this.loadBaysData();
                    break;
                case 'users':
                    this.loadUsersData();
                    break;
            }
        }, 30000);
    }

    formatDateTime(dateString) {
        const date = new Date(dateString);
        return date.toLocaleString();
    }
}

// Global functions for button onclick handlers
function refreshQueue() {
    adminPanel.loadQueueData();
}

function refreshBays() {
    adminPanel.loadBaysData();
}

function refreshUsers() {
    adminPanel.loadUsersData();
}

function processNextTicket() {
    // Find the first waiting ticket and process it
    const waitingRows = document.querySelectorAll('#queue-tbody tr');
    for (let row of waitingRows) {
        if (row.style.display !== 'none' && row.cells[3].textContent.trim() === 'Waiting') {
            const ticketId = row.cells[0].textContent.trim();
            adminPanel.processTicket(ticketId);
            break;
        }
    }
}

function showAddUserModal() {
    adminPanel.showAddUserModal();
}

function addUser() {
    adminPanel.addUser();
}

function savePricingSettings() {
    adminPanel.savePricingSettings();
}

function closeModal(modalId) {
    adminPanel.closeModal(modalId);
}

// Initialize admin panel when DOM is loaded
let adminPanel;
document.addEventListener('DOMContentLoaded', function() {
    adminPanel = new AdminPanel();
});

// Add CSS animations
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from {
            transform: translateX(100%);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
    
    @keyframes slideOut {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(100%);
            opacity: 0;
        }
    }
`;
document.head.appendChild(style);
