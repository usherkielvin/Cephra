// Fixed Admin Panel JavaScript with Proper API Connections
class AdminPanel {
    constructor() {
        this.currentPanel = 'dashboard';
        this.refreshInterval = null;
        this.analyticsRange = 'week'; // default range
        this.init();
    }

    init() {
        this.setupEventListeners();
        this.updateCurrentTime();
        this.loadDashboardData();
        this.startAutoRefresh();
        this.startTimeUpdate();
        this.setupAnalyticsRangeSelector();
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
        const sidebar = document.querySelector('.sidebar');
        const mainContent = document.querySelector('.main-content');

        if (sidebarToggle && sidebar) {
            sidebarToggle.addEventListener('click', (e) => {
                e.stopPropagation();
                sidebar.classList.toggle('active');
            });
        }

        // Close sidebar when clicking on main content background
        if (mainContent && sidebar) {
            mainContent.addEventListener('click', () => {
                if (sidebar.classList.contains('active')) {
                    sidebar.classList.remove('active');
                }
            });
        }

        // Prevent sidebar clicks from closing the sidebar
        if (sidebar) {
            sidebar.addEventListener('click', (e) => {
                e.stopPropagation();
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
            'transactions': 'Transaction History',
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
            case 'transactions':
                this.loadTransactions();
                break;
            case 'settings':
                this.loadBusinessData();
                break;
        }
    }

    async loadDashboardData() {
        try {
            const response = await fetch('api/admin.php?action=dashboard');
            const data = await response.json();

            if (data.success) {
                this.updateDashboardStats(data.stats);
                this.updateRecentActivity(data.recent_activity);
            } else {
                this.showError(data.error || 'Failed to load dashboard data');
            }
        } catch (error) {
            console.error('Error loading dashboard data:', error);
            this.showError('Failed to load dashboard data - check console for details');
        }
    }

    updateDashboardStats(stats) {
        document.getElementById('total-users').textContent = stats.total_users || 0;
        document.getElementById('queue-count').textContent = stats.queue_count || 0;
        document.getElementById('active-bays').textContent = stats.active_bays || 0;
        document.getElementById('revenue-today').textContent = `₱${this.formatCurrency(stats.revenue_today || 0)}`;
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
            const response = await fetch('api/admin.php?action=queue');
            const data = await response.json();

            if (data.success) {
                this.updateQueueTable(data.queue);
            } else {
                this.showError(data.error || 'Failed to load queue data');
            }
        } catch (error) {
            console.error('Error loading queue data:', error);
            this.showError('Failed to load queue data - check console for details');
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
                    <button class="btn btn-primary btn-sm" onclick="if (adminPanel) adminPanel.viewTicket('${ticket.ticket_id}')">
                        <i class="fas fa-eye"></i> View
                    </button>
                ${ticket.status !== 'Complete' ? `
                    <button class="btn btn-success" onclick="if (adminPanel) adminPanel.proceedTicket('${ticket.ticket_id}', '${ticket.status}')">
                        <i class="fas fa-play"></i> Process
                    </button>
                ` : `
                    <button class="btn btn-warning" onclick="if (adminPanel) adminPanel.markAsPaid('${ticket.ticket_id}')">
                        <i class="fas fa-credit-card"></i> Mark as Paid
                    </button>
                `}
                </td>
            </tr>
        `).join('');

        // Auto-remove paid tickets after a delay
        this.autoRemovePaidTickets();
    }

    async loadBaysData() {
        try {
            const response = await fetch('api/admin.php?action=bays');
            const data = await response.json();

            if (data.success) {
                this.updateBaysGrid(data.bays);
            } else {
                this.showError(data.error || 'Failed to load bays data');
            }
        } catch (error) {
            console.error('Error loading bays data:', error);
            this.showError('Failed to load bays data - check console for details');
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
                    <span class="bay-number">${bay.bay_number}</span>
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
                        <button class="btn btn-warning btn-sm" onclick="if (adminPanel) adminPanel.setBayMaintenance('${bay.bay_number}')">
                            <i class="fas fa-tools"></i> Maintenance
                        </button>
                    ` : bay.status === 'Maintenance' ? `
                        <button class="btn btn-success btn-sm" onclick="if (adminPanel) adminPanel.setBayAvailable('${bay.bay_number}')">
                            <i class="fas fa-check"></i> Available
                        </button>
                    ` : ''}
                </div>
            </div>
        `).join('');
    }

    async loadUsersData() {
        try {
            const response = await fetch('api/admin.php?action=users');
            const data = await response.json();

            if (data.success) {
                this.updateUsersTable(data.users);
            } else {
                this.showError(data.error || 'Failed to load users data');
            }
        } catch (error) {
            console.error('Error loading users data:', error);
            this.showError('Failed to load users data - check console for details');
        }
    }

    updateUsersTable(users) {
        const tbody = document.getElementById('users-tbody');
        if (!users || users.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" class="loading"><i class="fas fa-info-circle"></i> No users found</td></tr>';
            return;
        }

        tbody.innerHTML = users.map(user => `
            <tr id="user-row-${user.username}">
                <td>${user.username}</td>
                <td>${user.firstname}</td>
                <td>${user.lastname}</td>
                <td>${user.email}</td>
                <td>${this.formatDateTime(user.created_at)}</td>
                <td>
                    <button class="btn btn-danger btn-sm" onclick="if (adminPanel) adminPanel.deleteUser('${user.username}')">
                        <i class="fas fa-trash"></i> Delete
                    </button>
                </td>
            </tr>
        `).join('');
    }

    async deleteUser(username) {
        if (!confirm(`Are you sure you want to delete user "${username}"? This action cannot be undone.`)) {
            return;
        }

        try {
            const response = await fetch('api/admin.php', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `action=delete-user&username=${username}`
            });
            const data = await response.json();

            if (data.success) {
                this.showSuccess('User deleted successfully');
                // Remove the user row from the table
                const userRow = document.getElementById(`user-row-${username}`);
                if (userRow) {
                    userRow.remove();
                }
            } else {
                this.showError(data.message || 'Failed to delete user');
            }
        } catch (error) {
            console.error('Error deleting user:', error);
            this.showError('Failed to delete user');
        }
    }

    setupAnalyticsRangeSelector() {
        const container = document.getElementById('analytics-range-selector');
        if (!container) return;

        container.innerHTML = `
            <button id="range-day" class="range-btn">Day</button>
            <button id="range-week" class="range-btn active">Week</button>
            <button id="range-month" class="range-btn">Month</button>
        `;

        container.querySelectorAll('.range-btn').forEach(btn => {
            btn.addEventListener('click', (e) => {
                container.querySelectorAll('.range-btn').forEach(b => b.classList.remove('active'));
                e.target.classList.add('active');
                this.analyticsRange = e.target.id.replace('range-', '');
                this.loadAnalyticsData();
            });
        });
    }

    async loadAnalyticsData() {
        try {
            console.log('Loading analytics data for range:', this.analyticsRange);
            const response = await fetch(`api/admin.php?action=analytics&range=${this.analyticsRange}`);
            const data = await response.json();

            console.log('Analytics API response:', data);

            if (data.success) {
                if (data.revenue_data && data.revenue_data.length > 0) {
                    console.log('Rendering revenue chart with', data.revenue_data.length, 'data points');
                    const filledRevenueData = this.fillMissingDates(data.revenue_data, 'revenue');
                    this.renderRevenueChart(filledRevenueData);
                } else {
                    console.log('No revenue data available');
                }
                if (data.service_data && data.service_data.length > 0) {
                    console.log('Rendering service chart with', data.service_data.length, 'data points');
                    const filledServiceData = this.fillMissingDates(data.service_data, 'service_count');
                    this.renderServiceChart(filledServiceData);
                } else {
                    console.log('No service data available');
                }
            } else {
                console.error('Analytics API returned error:', data);
                this.showError('Failed to load analytics data');
            }
        } catch (error) {
            console.error('Error loading analytics data:', error);
            this.showError('Failed to load analytics data - check console for details');
        }
    }

    // Format date for chart display (remove year, show as M/D)
    formatDateForChart(dateString) {
        const date = new Date(dateString);
        const month = date.getMonth() + 1; // getMonth() returns 0-11
        const day = date.getDate();
        return `${month}/${day}`;
    }

    // Fill missing dates with zero values to ensure all days are shown
    fillMissingDates(data, valueKey) {
        if (!data || data.length === 0) return data;

        // Determine date range based on analytics range
        const today = new Date();
        let startDate = new Date();
        
        switch (this.analyticsRange) {
            case 'day':
                startDate.setDate(today.getDate() - 1); // Show yesterday and today
                break;
            case 'week':
                startDate.setDate(today.getDate() - 7); // Show last 7 days
                break;
            case 'month':
                startDate.setDate(today.getDate() - 30); // Show last 30 days
                break;
            default:
                startDate.setDate(today.getDate() - 7);
        }

        // Create a map of existing data
        const dataMap = new Map();
        data.forEach(item => {
            dataMap.set(item.date, parseFloat(item[valueKey]) || 0);
        });

        // Fill missing dates with zero values
        const filledData = [];
        const currentDate = new Date(startDate);
        
        while (currentDate <= today) {
            const dateString = currentDate.toISOString().split('T')[0]; // YYYY-MM-DD format
            const value = dataMap.get(dateString) || 0;
            
            filledData.push({
                date: dateString,
                [valueKey]: value
            });
            
            currentDate.setDate(currentDate.getDate() + 1);
        }

        return filledData;
    }

    renderRevenueChart(revenueData) {
        const ctx = document.getElementById('revenue-chart').getContext('2d');

        // Prepare data for chart with proper date formatting
        const labels = revenueData.map(item => this.formatDateForChart(item.date));
        const dataPoints = revenueData.map(item => parseFloat(item.revenue));

        if (this.revenueChart) {
            this.revenueChart.destroy();
        }

        this.revenueChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Revenue (₱)',
                    data: dataPoints,
                    borderColor: 'rgba(75, 192, 192, 1)',
                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                    fill: true,
                    tension: 0.3,
                    pointRadius: 4,
                    pointHoverRadius: 6,
                }]
            },
            options: {
                responsive: true,
                scales: {
                    x: {
                        title: {
                            display: true,
                            text: 'Date'
                        }
                    },
                    y: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: 'Revenue (₱)'
                        }
                    }
                },
                plugins: {
                    legend: {
                        display: true,
                        position: 'top',
                    },
                    tooltip: {
                        enabled: true,
                        mode: 'nearest',
                        intersect: false,
                    }
                }
            }
        });
    }

    renderServiceChart(serviceData) {
        const ctx = document.getElementById('service-chart').getContext('2d');

        // Prepare data for chart with proper date formatting
        const labels = serviceData.map(item => this.formatDateForChart(item.date));
        const dataPoints = serviceData.map(item => parseInt(item.service_count));

        if (this.serviceChart) {
            this.serviceChart.destroy();
        }

        this.serviceChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Service Count',
                    data: dataPoints,
                    borderColor: 'rgba(255, 159, 64, 1)',
                    backgroundColor: 'rgba(255, 159, 64, 0.2)',
                    fill: true,
                    tension: 0.3,
                    pointRadius: 4,
                    pointHoverRadius: 6,
                }]
            },
            options: {
                responsive: true,
                scales: {
                    x: {
                        title: {
                            display: true,
                            text: 'Date'
                        }
                    },
                    y: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: 'Number of Services'
                        }
                    }
                },
                plugins: {
                    legend: {
                        display: true,
                        position: 'top',
                    },
                    tooltip: {
                        enabled: true,
                        mode: 'nearest',
                        intersect: false,
                    }
                }
            }
        });
    }

    async loadBusinessData() {
        try {
            const response = await fetch('api/admin.php?action=business-settings');
            const data = await response.json();

            if (data.success) {
                document.getElementById('min-fee').value = data.settings.min_fee || 0;
                document.getElementById('kwh-per-peso').value = data.settings.kwh_per_peso || 0;
            }
        } catch (error) {
            console.error('Error loading business data:', error);
        }
    }

    async viewTicket(ticketId) {
        try {
            const response = await fetch(`api/admin.php?action=ticket-details&ticket_id=${ticketId}`);
            const data = await response.json();

            if (data.success) {
                this.showTicketModal(data.ticket);
            } else {
                this.showError('Failed to load ticket details');
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
            const response = await fetch('api/admin.php', {
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

    async processPayment(ticketId) {
        try {
            const response = await fetch('api/admin.php', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `action=mark-payment-paid&ticket_id=${ticketId}`
            });
            const data = await response.json();

            if (data.success) {
                this.showSuccess('Payment processed successfully');
                this.loadQueueData();
            } else {
                this.showError(data.message || 'Failed to process payment');
            }
        } catch (error) {
            console.error('Error processing payment:', error);
            this.showError('Failed to process payment');
        }
    }

    async markAsPaid(ticketId) {
        try {
            const response = await fetch('api/admin.php', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `action=mark-payment-paid&ticket_id=${ticketId}`
            });
            const data = await response.json();

            if (data.success) {
                this.showSuccess('Ticket marked as paid successfully');
                this.loadQueueData();
            } else {
                this.showError(data.message || 'Failed to mark ticket as paid');
            }
        } catch (error) {
            console.error('Error marking ticket as paid:', error);
            this.showError('Failed to mark ticket as paid');
        }
    }

    async proceedTicket(ticketId, currentStatus) {
        try {
            let action = '';

            switch (currentStatus.toLowerCase()) {
                case 'pending':
                    action = 'progress-to-waiting';
                    break;
                case 'waiting':
                    action = 'progress-to-charging';
                    break;
                case 'charging':
                    action = 'progress-to-complete';
                    break;
                case 'complete':
                    // For complete tickets, show message that PayPop will be displayed to customer
                    this.showInfo('Charging completed! PayPop will be displayed to the customer for payment.');
                    this.loadQueueData();
                    return;
                default:
                    this.showError('Invalid ticket status for progression');
                    return;
            }

            const response = await fetch('api/admin.php', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `action=${action}&ticket_id=${ticketId}`
            });
            
            if (!response.ok) {
                throw new Error(`HTTP ${response.status}: ${response.statusText}`);
            }
            
            const data = await response.json();

            if (data.success) {
                this.showSuccess(`Ticket ${ticketId} progressed successfully`);
                this.loadQueueData();
                this.loadBaysData(); // Refresh bays in case assignment happened
            } else {
                console.error('API Error:', data);
                this.showError(data.error || data.message || 'Failed to progress ticket');
            }
        } catch (error) {
            console.error('Error progressing ticket:', error);
            this.showError('Failed to progress ticket');
        }
    }

    autoRemovePaidTickets() {
        // Auto-remove paid tickets after a delay to allow for visual feedback
        setTimeout(() => {
            const tbody = document.getElementById('queue-tbody');
            if (!tbody) return;

            const rows = tbody.querySelectorAll('tr');
            rows.forEach(row => {
                const cells = row.querySelectorAll('td');
                if (cells.length >= 5) {
                    const paymentStatus = cells[4].textContent.trim().toLowerCase();
                    if (paymentStatus === 'paid') {
                        console.log('Auto-removing paid ticket row');
                        row.remove();
                    }
                }
            });

            // Update counters after removal
            this.updateQueueCounters();
        }, 2000); // 2 second delay
    }

    updateQueueCounters() {
        // Update the queue counters in the UI
        const tbody = document.getElementById('queue-tbody');
        if (!tbody) return;

        let waiting = 0;
        let charging = 0;
        let paid = 0;

        const rows = tbody.querySelectorAll('tr');
        rows.forEach(row => {
            const cells = row.querySelectorAll('td');
            if (cells.length >= 5) {
                const status = cells[3].textContent.trim().toLowerCase();
                const payment = cells[4].textContent.trim().toLowerCase();

                if (status === 'waiting') waiting++;
                else if (status === 'charging') charging++;
                if (payment === 'paid') paid++;
            }
        });

        // Update counter elements if they exist
        const waitingEl = document.getElementById('waiting-count');
        const chargingEl = document.getElementById('charging-count');
        const paidEl = document.getElementById('paid-count');

        if (waitingEl) waitingEl.textContent = waiting;
        if (chargingEl) chargingEl.textContent = charging;
        if (paidEl) paidEl.textContent = paid;
    }

    async setBayMaintenance(bayNumber) {
        if (!confirm(`Are you sure you want to set ${bayNumber} to maintenance mode?`)) {
            return;
        }

        try {
            const response = await fetch('api/admin.php', {
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
            const response = await fetch('api/admin.php', {
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
            const response = await fetch('api/admin.php', {
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

    async saveBusinessSettings() {
        const minFee = parseFloat(document.getElementById('min-fee').value);
        const kwhPerPeso = parseFloat(document.getElementById('kwh-per-peso').value);

        if (isNaN(minFee) || isNaN(kwhPerPeso) || minFee < 0 || kwhPerPeso <= 0) {
            this.showError('Please enter valid business values (min fee >= 0, kWh per peso > 0)');
            return;
        }

        try {
            const response = await fetch('api/admin.php', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `action=save-business-settings&min_fee=${minFee}&kwh_per_peso=${kwhPerPeso}`
            });
            const data = await response.json();

            if (data.success) {
                this.showSuccess('Business settings updated successfully');
            } else {
                this.showError(data.message || 'Failed to update settings');
            }
        } catch (error) {
            console.error('Error updating business settings:', error);
            this.showError('Failed to update business settings');
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

    // Transaction History Methods
    async loadTransactions() {
        try {
            const response = await fetch('api/admin.php?action=transactions');
            const data = await response.json();
            
            if (data.success) {
                // Store transaction data for export
                this.transactionData = data.transactions;
                this.displayTransactions(data.transactions);
                if (data.warnings && data.warnings.length > 0) {
                    console.warn('Transaction loading warnings:', data.warnings);
                }
                if (data.count !== undefined) {
                    console.log(`Loaded ${data.count} transactions`);
                }
            } else {
                console.error('Failed to load transactions:', data.error);
                this.showError('Failed to load transaction data: ' + (data.error || 'Unknown error'));
            }
        } catch (error) {
            console.error('Error loading transactions:', error);
            this.showError('Error loading transaction data: ' + error.message);
        }
    }

    displayTransactions(transactions) {
        const tbody = document.getElementById('transactions-tbody');
        if (!tbody) return;

        if (transactions.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="8" class="no-data">
                        <i class="fas fa-inbox"></i>
                        No transactions found
                    </td>
                </tr>
            `;
            return;
        }

        tbody.innerHTML = transactions.map(transaction => {
            const date = new Date(transaction.transaction_date);
            // Format date without seconds
            const formattedDate = date.toLocaleDateString() + ' ' + 
                date.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
            const amount = parseFloat(transaction.total_amount || 0).toFixed(2);
            const energy = parseFloat(transaction.energy_kwh || 0).toFixed(2);
            
            return `
                <tr>
                    <td>${transaction.ticket_id || 'N/A'}</td>
                    <td>${transaction.username || 'N/A'}</td>
                    <td>${energy} kWh</td>
                    <td>₱${amount}</td>
                    <td>${formattedDate}</td>
                    <td>${transaction.reference_number || 'N/A'}</td>
                </tr>
            `;
        }).join('');
    }

    async filterTransactions() {
        const typeFilter = document.getElementById('transaction-type-filter')?.value || '';
        const statusFilter = document.getElementById('transaction-status-filter')?.value || '';
        const dateFrom = document.getElementById('transaction-date-from')?.value || '';
        const dateTo = document.getElementById('transaction-date-to')?.value || '';

        try {
            let url = 'api/admin.php?action=transactions';
            const params = new URLSearchParams();
            
            if (typeFilter) params.append('type', typeFilter);
            if (statusFilter) params.append('status', statusFilter);
            if (dateFrom) params.append('date_from', dateFrom);
            if (dateTo) params.append('date_to', dateTo);
            
            if (params.toString()) {
                url += '&' + params.toString();
            }

            const response = await fetch(url);
            const data = await response.json();
            
            if (data.success) {
                // Store filtered transaction data for export
                this.transactionData = data.transactions;
                this.displayTransactions(data.transactions);
            } else {
                console.error('Failed to filter transactions:', data.error);
                this.showError('Failed to filter transaction data');
            }
        } catch (error) {
            console.error('Error filtering transactions:', error);
            this.showError('Error filtering transaction data');
        }
    }

    exportTransactions() {
        // Export transactions using the raw data instead of HTML table content
        if (!this.transactionData || this.transactionData.length === 0) {
            this.showError('No transactions to export');
            return;
        }

        let csv = 'Ticket,User,kWh,Total,Date and Time,Reference\n';
        
        this.transactionData.forEach(transaction => {
            const date = new Date(transaction.transaction_date);
            // Format date for CSV export
            const formattedDate = date.toLocaleDateString('en-US', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit'
            }) + ' ' + date.toLocaleTimeString('en-US', {
                hour: '2-digit',
                minute: '2-digit',
                hour12: false
            });
            
            const amount = parseFloat(transaction.total_amount || 0).toFixed(2);
            const energy = parseFloat(transaction.energy_kwh || 0).toFixed(2);
            
            const rowData = [
                `"${transaction.ticket_id || 'N/A'}"`,
                `"${transaction.username || 'N/A'}"`,
                `"${energy}"`,
                `"${amount}"`,
                `"${formattedDate}"`,
                `"${transaction.reference_number || 'N/A'}"`
            ];
            
            csv += rowData.join(',') + '\n';
        });

        // Download CSV
        const blob = new Blob([csv], { type: 'text/csv' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `transactions_${new Date().toISOString().split('T')[0]}.csv`;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
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
            let hours = now.getHours();
            const minutes = now.getMinutes();
            const ampm = hours >= 12 ? 'PM' : 'AM';
            
            hours = hours % 12;
            hours = hours ? hours : 12;
            
            const formattedTime = `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')} ${ampm}`;
            timeElement.textContent = formattedTime;
        }
    }

    startTimeUpdate() {
        setInterval(() => {
            this.updateCurrentTime();
        }, 60000);
    }

    startAutoRefresh() {
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

    formatCurrency(amount) {
        return Math.round(amount).toLocaleString();
    }

    formatDateTime(dateString) {
        const date = new Date(dateString);
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const day = date.getDate().toString().padStart(2, '0');
        const year = date.getFullYear();

        let hours = date.getHours();
        const minutes = date.getMinutes();
        const ampm = hours >= 12 ? 'PM' : 'AM';

        hours = hours % 12;
        hours = hours ? hours : 12;

        return `${month}/${day}/${year}, ${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')} ${ampm}`;
    }
}

// Open Monitor Web in new tab
function openMonitorWeb() {
    const monitorUrl = '../Monitor/';
    window.open(monitorUrl, '_blank', 'noopener,noreferrer');
}

// Global functions for button onclick handlers
function showLoadingSpinner() {
    const overlay = document.getElementById('loading-overlay');
    if (overlay) {
        overlay.style.display = 'flex';
        overlay.style.position = 'fixed';
        overlay.style.top = '0';
        overlay.style.left = '0';
        overlay.style.width = '100%';
        overlay.style.height = '100%';
        overlay.style.background = 'rgba(0, 0, 0, 0.5)';
        overlay.style.zIndex = '9999';
        overlay.style.alignItems = 'center';
        overlay.style.justifyContent = 'center';
        console.log('Loading overlay shown with inline styles and refreshing text');
        return overlay;
    } else {
        console.error('Loading overlay element not found');
        return null;
    }
}

function hideLoadingSpinner() {
    const overlay = document.getElementById('loading-overlay');
    if (overlay) {
        overlay.style.display = 'none';
        console.log('Loading overlay hidden');
    }
}

function refreshQueue() {
    console.log('Refresh Queue clicked');
    const overlay = showLoadingSpinner();
    if (!overlay) return;

    if (adminPanel) {
        console.log('Calling adminPanel.loadQueueData()');
        adminPanel.loadQueueData()
            .then(() => {
                console.log('loadQueueData completed successfully');
            })
            .catch((error) => {
                console.error('loadQueueData failed:', error);
            })
            .finally(() => {
                console.log('Hiding overlay after loadQueueData');
                setTimeout(() => hideLoadingSpinner(), 1300); // Extended delay to 1.3 seconds
            });
    } else {
        console.log('adminPanel not found, using fallback timeout');
        setTimeout(() => hideLoadingSpinner(), 1500);
    }
}

function refreshBays() {
    console.log('Refresh Bays clicked');
    const overlay = showLoadingSpinner();
    if (!overlay) return;

    if (adminPanel) {
        console.log('Calling adminPanel.loadBaysData()');
        adminPanel.loadBaysData()
            .then(() => {
                console.log('loadBaysData completed successfully');
            })
            .catch((error) => {
                console.error('loadBaysData failed:', error);
            })
            .finally(() => {
                console.log('Hiding overlay after loadBaysData');
                setTimeout(() => hideLoadingSpinner(), 1300); // Extended delay to 1.3 seconds
            });
    } else {
        console.log('adminPanel not found, using fallback timeout');
        setTimeout(() => hideLoadingSpinner(), 1500);
    }
}

function refreshUsers() {
    console.log('Refresh Users clicked');
    const overlay = showLoadingSpinner();
    if (!overlay) return;

    if (adminPanel) {
        console.log('Calling adminPanel.loadUsersData()');
        adminPanel.loadUsersData()
            .then(() => {
                console.log('loadUsersData completed successfully');
            })
            .catch((error) => {
                console.error('loadUsersData failed:', error);
            })
            .finally(() => {
                console.log('Hiding overlay after loadUsersData');
                setTimeout(() => hideLoadingSpinner(), 1300); // Extended delay to 1.3 seconds
            });
    } else {
        console.log('adminPanel not found, using fallback timeout');
        setTimeout(() => hideLoadingSpinner(), 1500);
    }
}

function processNextTicket() {
    if (adminPanel) {
        console.log('Starting processNextTicket...');
        // Call the new API to progress the next ticket
        fetch('api/admin.php?action=progress-next-ticket', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            }
        })
        .then(response => {
            console.log('Response status:', response.status);
            console.log('Response ok:', response.ok);
            return response.text().then(text => {
                console.log('Raw response:', text);
                try {
                    return JSON.parse(text);
                } catch (e) {
                    console.error('JSON parse error:', e);
                    throw new Error('Invalid JSON response: ' + text);
                }
            });
        })
        .then(data => {
            console.log('Parsed data:', data);
            if (data.success) {
                adminPanel.showSuccess(`Ticket ${data.ticket_id} assigned to Bay ${data.bay_number} (${data.new_status})`);
                adminPanel.loadQueueData();
                adminPanel.loadBaysData(); // Refresh bays to show updated status
            } else {
                adminPanel.showError(data.message || 'Failed to progress ticket');
            }
        })
        .catch(error => {
            console.error('Error progressing ticket:', error);
            adminPanel.showError('Failed to progress ticket: ' + error.message);
        });
    }
}

function showAddUserModal() {
    if (adminPanel) {
        adminPanel.showAddUserModal();
    }
}

function addUser() {
    if (adminPanel) {
        adminPanel.addUser();
    }
}

function saveBusinessSettings() {
    if (adminPanel) {
        adminPanel.saveBusinessSettings();
    }
}

function closeModal(modalId) {
    if (adminPanel) {
        adminPanel.closeModal(modalId);
    }
}

function processTicket(ticketId) {
    if (adminPanel) {
        adminPanel.processTicket(ticketId);
    }
}

// Transaction History Functions
function refreshTransactions() {
    if (adminPanel) {
        adminPanel.loadTransactions();
    }
}

function filterTransactions() {
    if (adminPanel) {
        adminPanel.filterTransactions();
    }
}

function exportTransactions() {
    if (adminPanel) {
        adminPanel.exportTransactions();
    }
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