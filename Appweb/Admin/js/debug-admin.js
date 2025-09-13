// Debug script for web admin interface issues
console.log('🔧 Cephra Admin Debug Script Loaded');

// Debug function to test admin panel functionality
function debugAdminPanel() {
    console.log('🔍 Starting admin panel debug...');
    
    // Test 1: Check if admin panel is initialized
    if (typeof adminPanel !== 'undefined') {
        console.log('✅ AdminPanel class is initialized');
        console.log('Current panel:', adminPanel.currentPanel);
    } else {
        console.error('❌ AdminPanel class is not initialized');
        console.log('This is likely causing navbar button switching issues');
    }
    
    // Test 2: Check if jQuery is loaded
    if (typeof $ !== 'undefined') {
        console.log('✅ jQuery is loaded');
    } else {
        console.error('❌ jQuery is not loaded');
        console.log('This will cause AJAX requests to fail');
    }
    
    // Test 3: Check API endpoints
    const apiEndpoints = [
        '../api/admin.php?action=dashboard',
        '../api/admin.php?action=queue',
        '../api/admin.php?action=bays',
        '../api/admin.php?action=users'
    ];
    
    console.log('🧪 Testing API endpoints...');
    apiEndpoints.forEach(async (endpoint) => {
        try {
            const response = await fetch(endpoint);
            const data = await response.json();
            
            if (data.success) {
                console.log(`✅ ${endpoint} - Working`);
            } else {
                console.warn(`⚠️ ${endpoint} - API Error:`, data.error);
            }
        } catch (error) {
            console.error(`❌ ${endpoint} - Failed:`, error.message);
        }
    });
    
    // Test 4: Check DOM elements
    const criticalElements = [
        '.sidebar-menu li',
        '.panel',
        '#recent-activity',
        '#queue-tbody',
        '#bays-grid',
        '#users-tbody'
    ];
    
    console.log('🔍 Checking DOM elements...');
    criticalElements.forEach(selector => {
        const elements = document.querySelectorAll(selector);
        if (elements.length > 0) {
            console.log(`✅ ${selector} - Found ${elements.length} elements`);
        } else {
            console.warn(`⚠️ ${selector} - No elements found`);
        }
    });
    
    // Test 5: Check event listeners
    console.log('🔍 Checking event listeners...');
    const sidebarItems = document.querySelectorAll('.sidebar-menu li');
    sidebarItems.forEach((item, index) => {
        const panel = item.dataset.panel;
        console.log(`Sidebar item ${index + 1}: ${panel}`);
        
        // Test click functionality
        item.addEventListener('click', function() {
            console.log(`🖱️ Sidebar item clicked: ${panel}`);
        });
    });
    
    // Test 6: Check for JavaScript errors
    window.addEventListener('error', function(e) {
        console.error('🚨 JavaScript Error:', e.message, 'at', e.filename, ':', e.lineno);
    });
    
    // Test 7: Check recent activity loading
    console.log('🔍 Testing recent activity loading...');
    const recentActivityContainer = document.getElementById('recent-activity');
    if (recentActivityContainer) {
        const loadingElement = recentActivityContainer.querySelector('.fa-spinner');
        if (loadingElement) {
            console.warn('⚠️ Recent activity is still loading - this indicates an API issue');
        } else {
            console.log('✅ Recent activity has loaded content');
        }
    }
    
    console.log('🔧 Debug complete! Check the console for any issues.');
}

// Auto-run debug when page loads
document.addEventListener('DOMContentLoaded', function() {
    console.log('📄 DOM loaded, running admin debug...');
    
    // Wait a bit for admin panel to initialize
    setTimeout(() => {
        debugAdminPanel();
    }, 1000);
});

// Add debug button to page
function addDebugButton() {
    const debugBtn = document.createElement('button');
    debugBtn.innerHTML = '🔧 Debug Admin';
    debugBtn.style.cssText = `
        position: fixed;
        top: 10px;
        right: 10px;
        z-index: 9999;
        background: #007bff;
        color: white;
        border: none;
        padding: 10px 15px;
        border-radius: 5px;
        cursor: pointer;
        font-size: 12px;
    `;
    debugBtn.onclick = debugAdminPanel;
    document.body.appendChild(debugBtn);
}

// Add debug button when DOM is ready
document.addEventListener('DOMContentLoaded', addDebugButton);

// Export debug function globally
window.debugAdminPanel = debugAdminPanel;

// Common fixes for admin issues
window.fixAdminIssues = function() {
    console.log('🛠️ Applying common fixes...');
    
    // Fix 1: Re-initialize admin panel if needed
    if (typeof adminPanel === 'undefined') {
        console.log('🔧 Re-initializing admin panel...');
        try {
            adminPanel = new AdminPanel();
            console.log('✅ Admin panel re-initialized');
        } catch (error) {
            console.error('❌ Failed to re-initialize admin panel:', error);
        }
    }
    
    // Fix 2: Refresh current panel data
    if (typeof adminPanel !== 'undefined') {
        console.log('🔄 Refreshing current panel data...');
        switch (adminPanel.currentPanel) {
            case 'dashboard':
                adminPanel.loadDashboardData();
                break;
            case 'queue':
                adminPanel.loadQueueData();
                break;
            case 'bays':
                adminPanel.loadBaysData();
                break;
            case 'users':
                adminPanel.loadUsersData();
                break;
        }
    }
    
    // Fix 3: Clear any loading states
    const loadingElements = document.querySelectorAll('.fa-spinner');
    loadingElements.forEach(element => {
        element.style.display = 'none';
    });
    
    console.log('✅ Common fixes applied');
};

// Add fix button
document.addEventListener('DOMContentLoaded', function() {
    const fixBtn = document.createElement('button');
    fixBtn.innerHTML = '🛠️ Fix Issues';
    fixBtn.style.cssText = `
        position: fixed;
        top: 50px;
        right: 10px;
        z-index: 9999;
        background: #28a745;
        color: white;
        border: none;
        padding: 10px 15px;
        border-radius: 5px;
        cursor: pointer;
        font-size: 12px;
    `;
    fixBtn.onclick = window.fixAdminIssues;
    document.body.appendChild(fixBtn);
});

console.log('🔧 Admin debug script ready! Use debugAdminPanel() or fixAdminIssues() in console.');
