<?php
session_start();
if (!isset($_SESSION['admin_logged_in']) || $_SESSION['admin_logged_in'] !== true) {
    header("Location: login.php");
    exit();
}

require_once 'config/database.php';
$db = new Database();
$conn = $db->getConnection();

// Get admin username
$admin_username = $_SESSION['admin_username'] ?? 'Admin';
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cephra Admin Panel</title>
    <link rel="icon" type="image/png" href="images/logo.png" />
    <link rel="stylesheet" href="css/admin.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
    <div class="admin-container">
        <!-- Sidebar -->
        <nav class="sidebar">
            <div class="sidebar-header">
                <img src="images/logo.png" alt="Cephra" class="admin-logo" />
                <h2>Cephra Admin</h2>
            </div>
            <div class="admin-user">
                <i class="fas fa-user-shield"></i>
                <span><?php echo htmlspecialchars($admin_username); ?></span>
            </div>
            <ul class="sidebar-menu">
                <li class="active" data-panel="dashboard">
                    <i class="fas fa-tachometer-alt"></i>
                    <span>Dashboard</span>
                </li>
                <li data-panel="queue">
                    <i class="fas fa-list-alt"></i>
                    <span>Queue Management</span>
                </li>
                <li data-panel="bays">
                    <i class="fas fa-battery-half"></i>
                    <span>Charging Bays</span>
                </li>
                <li data-panel="users">
                    <i class="fas fa-users"></i>
                    <span>User Management</span>
                </li>
                <li data-panel="analytics">
                    <i class="fas fa-chart-bar"></i>
                    <span>Analytics</span>
                </li>
                <li data-panel="transactions">
                    <i class="fas fa-receipt"></i>
                    <span>Transaction History</span>
                </li>
                <li data-panel="settings">
                    <i class="fas fa-cog"></i>
                    <span>Settings</span>
                </li>
            </ul>
            <div class="sidebar-footer">
                <a href="logout.php" class="logout-btn">
                    <i class="fas fa-sign-out-alt"></i>
                    <span>Logout</span>
                </a>
            </div>
        </nav>

        <!-- Main Content -->
        <main class="main-content">
            <header class="top-header">
                <div class="header-left">
                    <button class="sidebar-toggle">
                        <i class="fas fa-bars"></i>
                    </button>
                    <h1 id="page-title">Dashboard</h1>
                </div>
                <div class="header-right">
                    <button class="monitor-btn" onclick="openMonitorWeb()" title="Open Monitor Web">
                        <i class="fas fa-desktop"></i>
                        <span>Monitor</span>
                    </button>
                    <div class="status-indicator">
                        <span class="status-dot online"></span>
                        <span>System Online</span>
                    </div>
                    <div class="current-time" id="current-time"></div>
                </div>
            </header>

            <!-- Dashboard Panel -->
            <div class="panel active" id="dashboard-panel">
                <div class="stats-grid">
                    <div class="stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-users"></i>
                        </div>
                        <div class="stat-content">
                            <h3 id="total-users">0</h3>
                            <p>Total Users</p>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-list-alt"></i>
                        </div>
                        <div class="stat-content">
                            <h3 id="queue-count">0</h3>
                            <p>Queue Tickets</p>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-battery-half"></i>
                        </div>
                        <div class="stat-content">
                            <h3 id="active-bays">0</h3>
                            <p>Active Bays</p>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon">
                            <i class="fas fa-peso-sign"></i>
                        </div>
                        <div class="stat-content">
                            <h3 id="revenue-today">₱0</h3>
                            <p>Overall Revenue</p>
                        </div>
                    </div>
                </div>

                <div class="dashboard-grid">
                    <div class="dashboard-card">
                        <h3>Recent Activity</h3>
                        <div class="activity-list" id="recent-activity">
                            <div class="activity-item">
                                <i class="fas fa-spinner fa-spin"></i>
                                <span>Loading recent activity...</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Queue Management Panel -->
            <div class="panel" id="queue-panel">
                <div class="panel-header">
                    <h2>Queue Management</h2>
                    <div class="panel-actions">
                        <button class="btn btn-primary" onclick="refreshQueue()">
                            <i class="fas fa-sync-alt"></i> Refresh
                        </button>
                    </div>
                </div>
                <div class="queue-container">
                    <div class="queue-filters">
                        <select id="status-filter">
                            <option value="">All Status</option>
                            <option value="Waiting">Waiting</option>
                            <option value="Processing">Processing</option>
                            <option value="Completed">Completed</option>
                            <option value="Cancelled">Cancelled</option>
                        </select>
                        <select id="service-filter">
                            <option value="">All Services</option>
                            <option value="Fast Charging">Fast Charging</option>
                            <option value="Normal Charging">Normal Charging</option>
                        </select>
                    </div>
                    <div class="queue-table-container">
                        <table class="queue-table" id="queue-table">
                            <thead>
                                <tr>
                                    <th>Ticket ID</th>
                                    <th>Username</th>
                                    <th>Service Type</th>
                                    <th>Priority</th>
                                    <th>Status</th>
                                    <th>Payment</th>
                                    <th>Created</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody id="queue-tbody">
                                <tr>
                                    <td colspan="8" class="loading">
                                        <i class="fas fa-spinner fa-spin"></i> Loading queue data...
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- Charging Bays Panel -->
            <div class="panel" id="bays-panel">
                <div class="panel-header">
                    <h2>Charging Bays Management</h2>
                    <div class="panel-actions">
                        <button class="btn btn-primary" onclick="refreshBays()">
                            <i class="fas fa-sync-alt"></i> Refresh
                        </button>
                    </div>
                </div>
                <div class="bays-grid" id="bays-grid">
                    <div class="loading">
                        <i class="fas fa-spinner fa-spin"></i> Loading bays data...
                    </div>
                </div>
            </div>

            <!-- User Management Panel -->
            <div class="panel" id="users-panel">
                <div class="panel-header">
                    <h2>User Management</h2>
                    <div class="panel-actions">
                        <button class="btn btn-primary" onclick="refreshUsers()">
                            <i class="fas fa-sync-alt"></i> Refresh
                        </button>
                        <button class="btn btn-success" onclick="showAddUserModal()">
                            <i class="fas fa-plus"></i> Add User
                        </button>
                    </div>
                </div>
                <div class="users-container">
                    <div class="users-table-container">
                        <table class="users-table" id="users-table">
                            <thead>
                                <tr>
                                    <th>Username</th>
                                    <th>First Name</th>
                                    <th>Last Name</th>
                                    <th>Email</th>
                                    <th>Created</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody id="users-tbody">
                                <tr>
                                    <td colspan="6" class="loading">
                                        <i class="fas fa-spinner fa-spin"></i> Loading users data...
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- Analytics Panel -->
            <div class="panel" id="analytics-panel">
                <div class="panel-header">
                    <h2>Analytics & Reports</h2>
                    <div class="panel-actions">
                        <div id="analytics-range-selector"></div>
                    </div>
                </div>
                <div class="analytics-container">
                    <div class="analytics-grid">
                        <div class="analytics-card">
                            <h3>Daily Revenue</h3>
                            <canvas id="revenue-chart"></canvas>
                        </div>
                        <div class="analytics-card">
                            <h3>Service Usage</h3>
                            <canvas id="service-chart"></canvas>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Transaction History Panel -->
            <div class="panel" id="transactions-panel">
                <div class="panel-header">
                    <h2>Transaction History</h2>
                    <div class="panel-actions">
                        <button class="btn btn-primary" onclick="refreshTransactions()">
                            <i class="fas fa-sync-alt"></i> Refresh
                        </button>
                        <button class="btn btn-secondary" onclick="exportTransactions()">
                            <i class="fas fa-download"></i> Export
                        </button>
                    </div>
                </div>
                <div class="transactions-container">
                    <div class="transactions-filters">
                        <select id="transaction-type-filter">
                            <option value="">All Transactions</option>
                            <option value="payment">Payment Transactions</option>
                            <option value="charging">Charging History</option>
                        </select>
                        <select id="transaction-status-filter">
                            <option value="">All Status</option>
                            <option value="Completed">Completed</option>
                            <option value="Pending">Pending</option>
                            <option value="Cancelled">Cancelled</option>
                        </select>
                        <input type="date" id="transaction-date-from" placeholder="From Date">
                        <input type="date" id="transaction-date-to" placeholder="To Date">
                        <button class="btn btn-primary" onclick="filterTransactions()">
                            <i class="fas fa-filter"></i> Filter
                        </button>
                    </div>
                    <div class="transactions-table-container">
                        <table class="transactions-table" id="transactions-table">
                            <thead>
                                <tr>
                                    <th>Ticket</th>
                                    <th>User</th>
                                    <th>kWh</th>
                                    <th>Total</th>
                                    <th>Date and Time</th>
                                    <th>Reference</th>
                                </tr>
                            </thead>
                            <tbody id="transactions-tbody">
                                <tr>
                                    <td colspan="6" class="loading">
                                        <i class="fas fa-spinner fa-spin"></i> Loading transaction data...
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- Settings Panel -->
            <div class="panel" id="settings-panel">
                <div class="panel-header">
                    <h2>System Settings</h2>
                </div>
                <div class="settings-container">
                    <div class="settings-section">
                        <h3>Pricing Configuration</h3>
                        <div class="settings-form">
                            <div class="form-group">
                                <label for="min-fee">Minimum Fee (₱)</label>
                                <input type="number" id="min-fee" step="0.01" min="0" />
                            </div>
                            <div class="form-group">
                                <label for="kwh-per-peso">kWh per Peso</label>
                                <input type="number" id="kwh-per-peso" step="0.01" min="0" />
                            </div>
                            <button class="btn btn-primary" onclick="saveBusinessSettings()">
                                <i class="fas fa-save"></i> Save Settings
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>

    <!-- Loading Overlay -->
    <div id="loading-overlay" class="loading-overlay">
        <div class="loading-spinner">
            <div class="loader">
                <div class="jimu-primary-loading"></div>
            </div>
        </div>
    </div>

    <!-- Modals -->
    <div class="modal" id="ticket-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Ticket Details</h3>
                <button class="modal-close">&times;</button>
            </div>
            <div class="modal-body" id="ticket-modal-body">
                <!-- Ticket details will be loaded here -->
            </div>
            <div class="modal-footer">
                <button class="btn btn-secondary" onclick="closeModal('ticket-modal')">Close</button>
            </div>
        </div>
    </div>

    <div class="modal" id="add-user-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Add New User</h3>
                <button class="modal-close">&times;</button>
            </div>
            <div class="modal-body">
                <form id="add-user-form">
                    <div class="form-group">
                        <label for="new-username">Username</label>
                        <input type="text" id="new-username" required />
                    </div>
                    <div class="form-group">
                        <label for="new-firstname">First Name</label>
                        <input type="text" id="new-firstname" required />
                    </div>
                    <div class="form-group">
                        <label for="new-lastname">Last Name</label>
                        <input type="text" id="new-lastname" required />
                    </div>
                    <div class="form-group">
                        <label for="new-email">Email</label>
                        <input type="email" id="new-email" required />
                    </div>
                    <div class="form-group">
                        <label for="new-password">Password</label>
                        <input type="password" id="new-password" required />
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button class="btn btn-secondary" onclick="closeModal('add-user-modal')">Cancel</button>
                <button class="btn btn-primary" onclick="addUser()">Add User</button>
            </div>
        </div>
    </div>

    <script src="js/admin-fixed.js?v=<?php echo time(); ?>"></script>
</body>
</html>
