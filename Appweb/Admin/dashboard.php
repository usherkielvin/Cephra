<?php
session_start();
if (!isset($_SESSION['admin_logged_in']) || $_SESSION['admin_logged_in'] !== true) {
    header("Location: index.php");
    exit();
}

require_once 'config/database.php';
$db = new Database();
$conn = $db->getConnection();

// Get admin username
$admin_username = $_SESSION['admin_username'] ?? 'Admin';

// Re-validate staff status; if deactivated, force logout
if ($conn && isset($_SESSION['admin_username'])) {
    try {
        $stmt = $conn->prepare("SELECT status FROM staff_records WHERE username = ?");
        $stmt->execute([$_SESSION['admin_username']]);
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
        if (!$row || strcasecmp($row['status'] ?? '', 'Active') !== 0) {
            session_unset();
            session_destroy();
            header("Location: index.php?deactivated=1");
            exit();
        }
    } catch (Exception $e) {
        // On error, default to safe behavior: logout
        session_unset();
        session_destroy();
        header("Location: index.php");
        exit();
    }
}
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
    <style>
        .lang-dropdown { position: absolute; right: 10px; top: 60px; background:#fff; color:#0b1e29; border:1px solid rgba(0,0,0,0.1); border-radius:8px; box-shadow:0 8px 20px rgba(0,0,0,0.15); min-width:160px; display:none; z-index:1000; }
        .lang-dropdown.open { display:block; }
        .lang-dropdown .lang-item { padding:10px 12px; cursor:pointer; display:flex; align-items:center; gap:8px; }
        .lang-dropdown .lang-item:hover { background:#f1f7fa; }
        .header-right { position: relative; }
    </style>
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
                    <span id="nav-dashboard">Dashboard</span>
                </li>
                <li data-panel="queue">
                    <i class="fas fa-list-alt"></i>
                    <span id="nav-queue">Queue Management</span>
                </li>
                <li data-panel="bays">
                    <i class="fas fa-battery-half"></i>
                    <span id="nav-bays">Charging Bays</span>
                </li>
                <li data-panel="users">
                    <i class="fas fa-users"></i>
                    <span id="nav-users">User Management</span>
                </li>
                <li data-panel="staff">
                    <i class="fas fa-user-tie"></i>
                    <span id="nav-staff">Staff Management</span>
                </li>
                <li data-panel="analytics">
                    <i class="fas fa-chart-bar"></i>
                    <span id="nav-analytics">Analytics</span>
                </li>
                <li data-panel="transactions">
                    <i class="fas fa-receipt"></i>
                    <span id="nav-transactions">Transaction History</span>
                </li>
                <li data-panel="settings">
                    <i class="fas fa-cog"></i>
                    <span id="nav-settings">Settings</span>
                </li>
            </ul>
            <div class="sidebar-footer">
                <a href="logout.php" class="logout-btn">
                    <i class="fas fa-sign-out-alt"></i>
                    <span id="nav-logout">Logout</span>
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
                    <div class="current-time" id="current-time"></div>
                    <div class="header-quick">
                        <button class="header-link header-lang" type="button" title="Language">
                            <span>ENG</span>
                        </button>
                        <div id="langMenu" class="lang-dropdown">
                            <div class="lang-item" data-lang="EN">English</div>
                            <div class="lang-item" data-lang="PH">Filipino</div>
                            <div class="lang-item" data-lang="CEB">Bisaya</div>
                            <div class="lang-item" data-lang="ES">Español</div>
                            <div class="lang-item" data-lang="ZH">中文 (Chinese)</div>
                        </div>
                    </div>
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
                    <h2 id="queue-header">Queue Management</h2>
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
                            <option value="Pending">Pending</option>
                            <option value="Waiting">Waiting</option>
                            <option value="Charging">Charging</option>
                            <option value="Complete">Complete</option>
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
                    <h2 id="bays-header">Charging Bays Management</h2>
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
                    <h2 id="users-header">User Management</h2>
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
                                    <th id="th-users-username">Username</th>
                                    <th id="th-users-firstname">First Name</th>
                                    <th id="th-users-lastname">Last Name</th>
                                    <th id="th-users-email">Email</th>
                                    <th id="th-users-created">Created</th>
                                    <th id="th-users-actions">Actions</th>
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
                    <h2 id="analytics-header">Analytics & Reports</h2>
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
                    <h2 id="transactions-header">Transaction History</h2>
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
                <div class="transactions-filters" style="position:relative; display:flex; gap:10px; align-items:center;">
                    <input type="text" id="tx-search" placeholder="Search transactions..." style="flex:1; min-width:220px; padding:8px 10px; border:1px solid rgba(0,0,0,0.15); border-radius:8px;" />
                    <button class="btn btn-primary" id="tx-filter-btn" type="button">
                        <i class="fas fa-filter"></i> Filter
                    </button>
                    <div id="tx-filter-menu" class="tx-filter-menu" style="position:absolute; right:0; top:42px; background:#fff; color:#0b1e29; border:1px solid rgba(0,0,0,0.1); border-radius:10px; box-shadow:0 8px 20px rgba(0,0,0,0.15); padding:12px; display:none; z-index:1000; min-width:240px;">
                        <div style="font-weight:600; margin-bottom:8px;">Filters</div>
                        <label style="display:flex; align-items:center; gap:8px; margin-bottom:6px;"><input type="checkbox" id="tx-fast" checked /> Fast Charging</label>
                        <label style="display:flex; align-items:center; gap:8px; margin-bottom:10px;"><input type="checkbox" id="tx-normal" checked /> Normal Charging</label>
                        <div style="display:flex; gap:8px; align-items:center; margin-bottom:10px;">
                            <input type="date" id="tx-from" style="flex:1; padding:6px 8px; border:1px solid rgba(0,0,0,0.15); border-radius:8px;" />
                            <input type="date" id="tx-to" style="flex:1; padding:6px 8px; border:1px solid rgba(0,0,0,0.15); border-radius:8px;" />
                        </div>
                        <div style="display:flex; gap:8px; justify-content:flex-end;">
                            <button type="button" class="btn btn-secondary" id="tx-filter-cancel">Close</button>
                            <button type="button" class="btn btn-primary" id="tx-filter-apply">Apply</button>
                        </div>
                    </div>
                </div>
                    <div class="transactions-table-container">
                        <table class="transactions-table" id="transactions-table">
                            <thead>
                                <tr>
                                    <th id="th-tx-ticket">Ticket</th>
                                    <th id="th-tx-user">User</th>
                                    <th id="th-tx-kwh">kWh</th>
                                    <th id="th-tx-total">Total</th>
                                    <th id="th-tx-datetime">Date and Time</th>
                                    <th id="th-tx-reference">Reference</th>
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

            <!-- Staff Management Panel -->
            <div class="panel" id="staff-panel">
                <div class="panel-header">
                    <h2 id="staff-header">Staff Management</h2>
                    <div class="panel-actions">
                        <button class="btn btn-primary" onclick="if (adminPanel) adminPanel.loadStaffData()">
                            <i class="fas fa-sync-alt"></i> Refresh
                        </button>
                        <button class="btn btn-success" onclick="showAddStaffModal()">
                            <i class="fas fa-plus"></i> Add Staff
                        </button>
                    </div>
                </div>
                <div class="users-container">
                    <div class="users-table-container">
                        <table class="users-table" id="staff-table">
                            <thead>
                                <tr>
                                    <th id="th-staff-name">Name</th>
                                    <th id="th-staff-username">Username</th>
                                    <th id="th-staff-email">Email</th>
                                    <th id="th-staff-status">Status</th>
                                    <th id="th-staff-created">Created</th>
                                    <th id="th-staff-actions">Actions</th>
                                </tr>
                            </thead>
                            <tbody id="staff-tbody">
                                <tr>
                                    <td colspan="6" class="loading">
                                        <i class="fas fa-spinner fa-spin"></i> Loading staff data...
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
                    <h2 id="settings-header">System Settings</h2>
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

    <div class="modal" id="add-staff-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Add New Staff</h3>
                <button class="modal-close">&times;</button>
            </div>
            <div class="modal-body">
                <form id="add-staff-form">
                    <div class="form-group">
                        <label for="staff-name">Name</label>
                        <input type="text" id="staff-name" required />
                    </div>
                    <div class="form-group">
                        <label for="staff-username">Username</label>
                        <input type="text" id="staff-username" required />
                    </div>
                    <div class="form-group">
                        <label for="staff-email">Email</label>
                        <input type="email" id="staff-email" required />
                    </div>
                    <div class="form-group">
                        <label for="staff-password">Password</label>
                        <input type="password" id="staff-password" required />
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button class="btn btn-secondary" onclick="closeModal('add-staff-modal')">Cancel</button>
                <button class="btn btn-primary" onclick="addStaff()">Add Staff</button>
            </div>
        </div>
    </div>

    <script src="js/admin-fixed.js?v=<?php echo time(); ?>"></script>
    <script>
        // Mini language panel logic
        (function(){
            const btn = document.querySelector('.header-lang');
            const menu = document.getElementById('langMenu');
            const label = btn ? btn.querySelector('span') : null;
            const i18n = {
                EN: {
                    'nav-dashboard':'Dashboard','nav-queue':'Queue Management','nav-bays':'Charging Bays','nav-users':'User Management','nav-staff':'Staff Management','nav-analytics':'Analytics','nav-transactions':'Transaction History','nav-settings':'Settings','nav-logout':'Logout',
                        'queue-header':'Queue Management','bays-header':'Charging Bays Management','users-header':'User Management','analytics-header':'Analytics & Reports','transactions-header':'Transaction History','staff-header':'Staff Management','settings-header':'System Settings',
                    'th-tx-ticket':'Ticket','th-tx-user':'User','th-tx-kwh':'kWh','th-tx-total':'Total','th-tx-datetime':'Date and Time','th-tx-reference':'Reference',
                    'th-users-username':'Username','th-users-firstname':'First Name','th-users-lastname':'Last Name','th-users-email':'Email','th-users-created':'Created','th-users-actions':'Actions',
                    'th-staff-name':'Name','th-staff-username':'Username','th-staff-email':'Email','th-staff-status':'Status','th-staff-created':'Created','th-staff-actions':'Actions'
                },
                CEB: {
                        'nav-dashboard':'Dashboard','nav-queue':'Pagdumala sa Pila','nav-bays':'Mga Bay sa Charging','nav-users':'Pagdumala sa User','nav-staff':'Pagdumala sa Staff','nav-analytics':'Analitika','nav-transactions':'Kasaysayan sa Transaksiyon','nav-settings':'Mga Setting','nav-logout':'Gawas',
                        'queue-header':'Pagdumala sa Pila','bays-header':'Pagdumala sa mga Charging Bay','users-header':'Pagdumala sa User','analytics-header':'Analitika ug mga Report','transactions-header':'Kasaysayan sa Transaksiyon','staff-header':'Pagdumala sa Staff','settings-header':'Mga Setting',
                    'th-tx-ticket':'Tiket','th-tx-user':'Tig-gamit','th-tx-kwh':'kWh','th-tx-total':'Kinatibuk-an','th-tx-datetime':'Petsa ug Oras','th-tx-reference':'Referencia',
                    'th-users-username':'Username','th-users-firstname':'Ngalan','th-users-lastname':'Apelyido','th-users-email':'Email','th-users-created':'Gibuhat','th-users-actions':'Aksyon',
                    'th-staff-name':'Ngalan','th-staff-username':'Username','th-staff-email':'Email','th-staff-status':'Status','th-staff-created':'Gibuhat','th-staff-actions':'Aksyon'
                    },
                PH: {
                    'nav-dashboard':'Dashboard','nav-queue':'Pamamahala ng Pila','nav-bays':'Mga Charging Bay','nav-users':'Pamamahala ng User','nav-staff':'Pamamahala ng Staff','nav-analytics':'Analytics','nav-transactions':'Kasaysayan ng Transaksyon','nav-settings':'Mga Setting',
                        'nav-logout':'Logout','queue-header':'Pamamahala ng Pila','bays-header':'Pamamahala ng Charging Bay','users-header':'Pamamahala ng User','analytics-header':'Analytics at Ulat','transactions-header':'Kasaysayan ng Transaksyon','staff-header':'Pamamahala ng Staff','settings-header':'Mga Setting',
                    'th-tx-ticket':'Ticket','th-tx-user':'User','th-tx-kwh':'kWh','th-tx-total':'Kabuuan','th-tx-datetime':'Petsa at Oras','th-tx-reference':'Sanggunian',
                    'th-users-username':'Username','th-users-firstname':'Pangalan','th-users-lastname':'Apelyido','th-users-email':'Email','th-users-created':'Nagawa','th-users-actions':'Mga Gawain',
                    'th-staff-name':'Pangalan','th-staff-username':'Username','th-staff-email':'Email','th-staff-status':'Katayuan','th-staff-created':'Nagawa','th-staff-actions':'Mga Gawain'
                },
                ES: {
                    'nav-dashboard':'Panel','nav-queue':'Gestión de Cola','nav-bays':'Bahías de Carga','nav-users':'Gestión de Usuarios','nav-staff':'Gestión de Personal','nav-analytics':'Analítica','nav-transactions':'Historial de Transacciones','nav-settings':'Configuración',
                        'nav-logout':'Salir','queue-header':'Gestión de Cola','bays-header':'Gestión de Bahías de Carga','users-header':'Gestión de Usuarios','analytics-header':'Analítica y Reportes','transactions-header':'Historial de Transacciones','staff-header':'Gestión de Personal','settings-header':'Configuración',
                    'th-tx-ticket':'Ticket','th-tx-user':'Usuario','th-tx-kwh':'kWh','th-tx-total':'Total','th-tx-datetime':'Fecha y Hora','th-tx-reference':'Referencia',
                    'th-users-username':'Usuario','th-users-firstname':'Nombre','th-users-lastname':'Apellido','th-users-email':'Correo','th-users-created':'Creado','th-users-actions':'Acciones',
                    'th-staff-name':'Nombre','th-staff-username':'Usuario','th-staff-email':'Correo','th-staff-status':'Estado','th-staff-created':'Creado','th-staff-actions':'Acciones'
                    },
                ZH: {
                        'nav-dashboard':'仪表盘','nav-queue':'队列管理','nav-bays':'充电车位','nav-users':'用户管理','nav-staff':'员工管理','nav-analytics':'数据分析','nav-transactions':'交易记录','nav-settings':'系统设置','nav-logout':'退出',
                        'queue-header':'队列管理','bays-header':'充电车位管理','users-header':'用户管理','analytics-header':'数据分析与报表','transactions-header':'交易记录','staff-header':'员工管理','settings-header':'系统设置',
                    'th-tx-ticket':'票号','th-tx-user':'用户','th-tx-kwh':'千瓦时','th-tx-total':'总计','th-tx-datetime':'日期时间','th-tx-reference':'参考号',
                    'th-users-username':'用户名','th-users-firstname':'名','th-users-lastname':'姓','th-users-email':'邮箱','th-users-created':'创建时间','th-users-actions':'操作',
                    'th-staff-name':'姓名','th-staff-username':'用户名','th-staff-email':'邮箱','th-staff-status':'状态','th-staff-created':'创建时间','th-staff-actions':'操作'
                }
            };

            function applyLang(code){
                const dict = i18n[code] || i18n.EN;
                Object.keys(dict).forEach(id => {
                    const el = document.getElementById(id);
                    if (el) el.textContent = dict[id];
                });
            }
            if (!btn || !menu) return;
            btn.addEventListener('click', (e) => {
                e.stopPropagation();
                menu.classList.toggle('open');
            });
            document.addEventListener('click', () => menu.classList.remove('open'));
            menu.addEventListener('click', (e) => {
                const item = e.target.closest('.lang-item');
                if (!item) return;
                const code = item.getAttribute('data-lang') || 'EN';
                if (label) label.textContent = code;
                menu.classList.remove('open');
                // Persist selection (optional)
                try { localStorage.setItem('admin_lang', code); } catch(_) {}
                applyLang(code);
            });
            // Load persisted selection
            try {
                const saved = localStorage.getItem('admin_lang');
                if (saved && label) label.textContent = saved;
                applyLang(saved || 'EN');
            } catch(_) {}
        })();
    </script>
    <script>
        // Transactions search + filter mini panel
        (function(){
            const search = document.getElementById('tx-search');
            const btn = document.getElementById('tx-filter-btn');
            const menu = document.getElementById('tx-filter-menu');
            const applyBtn = document.getElementById('tx-filter-apply');
            const cancelBtn = document.getElementById('tx-filter-cancel');
            const tbody = document.getElementById('transactions-tbody');
            if (!tbody) return;

            function getRowText(tr){
                return Array.from(tr.querySelectorAll('td')).map(td => (td.textContent||'').toLowerCase()).join(' ');
            }
            function passType(tr){
                // Transactions table does not have a service type column; infer from ticket prefix
                const ticket = (tr.children[0]?.textContent||'').toUpperCase();
                const fastOn = document.getElementById('tx-fast')?.checked;
                const normalOn = document.getElementById('tx-normal')?.checked;
                if (ticket.startsWith('FCH')) return !!fastOn;   // Fast Charging
                if (ticket.startsWith('NCH')) return !!normalOn; // Normal Charging
                // If unknown ticket format, don't filter it out
                return true;
            }
            function passDate(tr){
                const from = document.getElementById('tx-from')?.value;
                const to = document.getElementById('tx-to')?.value;
                const dateText = tr.children[4]?.textContent || '';
                const d = new Date(dateText);
                if (from && d < new Date(from)) return false;
                if (to && d > new Date(to + 'T23:59:59')) return false;
                return true;
            }
            function applyFilters(){
                const q = (search?.value||'').toLowerCase();
                Array.from(tbody.querySelectorAll('tr')).forEach(tr => {
                    if (tr.classList.contains('loading')) return;
                    const show = getRowText(tr).includes(q) && passType(tr) && passDate(tr);
                    tr.style.display = show ? '' : 'none';
                });
            }

            search?.addEventListener('input', applyFilters);
            btn?.addEventListener('click', (e)=>{ e.stopPropagation(); menu.style.display = (menu.style.display==='block'?'none':'block'); });
            applyBtn?.addEventListener('click', ()=>{ menu.style.display='none'; applyFilters(); });
            cancelBtn?.addEventListener('click', ()=>{ menu.style.display='none'; });
            document.addEventListener('click', ()=>{ if(menu) menu.style.display='none'; });
        })();
    </script>
    <script>
        // Queue Management: status filter (Pending, Waiting, Charging, Complete)
        (function(){
            const statusSel = document.getElementById('status-filter');
            const tbody = document.getElementById('queue-tbody');
            if (!statusSel || !tbody) return;

            function normalize(v){
                const x = (v||'').toLowerCase().trim();
                if (x === 'completed') return 'complete';
                if (x === 'processing' || x === 'in progress') return 'charging';
                return x;
            }
            function rowStatus(tr){
                // Primary: status column
                const statusCell = tr.children[4];
                let s = normalize(statusCell ? statusCell.textContent : '');
                if (s) return s;
                // Fallback: scan entire row text
                const txt = (tr.textContent||'').toLowerCase();
                if (txt.includes('pending')) return 'pending';
                if (txt.includes('waiting')) return 'waiting';
                if (txt.includes('charging') || txt.includes('in progress') || txt.includes('processing')) return 'charging';
                if (txt.includes('complete') || txt.includes('completed')) return 'complete';
                return '';
            }
            function applyStatusFilter() {
                const value = normalize(statusSel.value || '');
                Array.from(tbody.querySelectorAll('tr')).forEach(tr => {
                    if (tr.classList.contains('loading')) return;
                    const statusText = rowStatus(tr);
                    const show = !value || statusText === value;
                    tr.style.display = show ? '' : 'none';
                });
            }

            statusSel.addEventListener('change', applyStatusFilter);
            // Re-apply filter whenever table rows are refreshed
            const observer = new MutationObserver(applyStatusFilter);
            observer.observe(tbody, { childList: true });
            // Initial
            applyStatusFilter();
        })();
    </script>
    <script>
        // Remove rows where Payment column equals "Paid"
        (function(){
            const tbody = document.getElementById('queue-tbody');
            if (!tbody) return;

            function isPaidRow(tr){
                // Payment column is the 6th column (0-based index 5) per table header
                const paymentCell = tr.children[5];
                if (!paymentCell) return false;
                const txt = (paymentCell.textContent || '').trim().toLowerCase();
                return txt === 'paid' || txt === 'paid.'; // tolerate trailing dot
            }

            function removePaidQueueRows(){
                Array.from(tbody.querySelectorAll('tr')).forEach(tr => {
                    if (tr.classList.contains('loading')) return;
                    try {
                        if (isPaidRow(tr)) {
                            // Remove the row from DOM to avoid it appearing in filters/search
                            tr.remove();
                        }
                    } catch(e){
                        // ignore individual row errors
                    }
                });
            }

            // Observe table body for new rows and remove paid ones automatically
            const obs = new MutationObserver((mutations)=>{
                // small debounce to batch multiple mutations
                removePaidQueueRows();
            });
            obs.observe(tbody, { childList: true, subtree: false });

            // Run once on initial load
            removePaidQueueRows();

            // Expose for manual triggering (e.g., from console)
            try{ window.removePaidQueueRows = removePaidQueueRows; } catch(_){}
        })();
    </script>
</body>
</html>
