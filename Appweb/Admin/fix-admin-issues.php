<?php
// Fix script for web admin interface issues
session_start();

// Enable error reporting
error_reporting(E_ALL);
ini_set('display_errors', 1);

echo "<h1>🔧 Cephra Web Admin Issue Fixer</h1>";
echo "<div style='font-family: Arial, sans-serif; max-width: 1000px; margin: 0 auto; padding: 20px;'>";

require_once 'config/database.php';

// Issue 1: Database Connection Problems
echo "<h2>🔍 Issue 1: Database Connection Problems</h2>";

try {
    $db = new Database();
    $conn = $db->getConnection();
    
    if (!$conn) {
        echo "<div style='color: red; padding: 10px; background: #f8d7da; border-radius: 5px; margin: 10px 0;'>";
        echo "❌ <strong>CRITICAL:</strong> Database connection failed!<br>";
        echo "This is likely causing the 'Recent Activity' loading issue.<br><br>";
        echo "<strong>Solutions:</strong><br>";
        echo "1. Start XAMPP Control Panel<br>";
        echo "2. Start MySQL service<br>";
        echo "3. Check if port 3306 is available<br>";
        echo "4. Verify database 'cephradb' exists<br>";
        echo "</div>";
    } else {
        echo "<div style='color: green; padding: 10px; background: #d4edda; border-radius: 5px; margin: 10px 0;'>";
        echo "✅ Database connection is working!<br>";
        echo "Recent activity loading should work properly.<br>";
        echo "</div>";
        
        // Test recent activity query
        try {
            $stmt = $conn->query("
                SELECT 
                    'ticket' as type,
                    CONCAT('Ticket ', ticket_id, ' - ', username, ' (', service_type, ')') as description,
                    'fa-ticket-alt' as icon,
                    created_at
                FROM queue_tickets 
                ORDER BY created_at DESC 
                LIMIT 10
            ");
            $recent_activity = $stmt->fetchAll(PDO::FETCH_ASSOC);
            
            if (count($recent_activity) > 0) {
                echo "<div style='color: green; margin: 10px 0;'>";
                echo "✅ Recent activity query working - Found " . count($recent_activity) . " activities<br>";
                echo "</div>";
            } else {
                echo "<div style='color: orange; margin: 10px 0;'>";
                echo "⚠️ Recent activity query working but no data found<br>";
                echo "</div>";
            }
        } catch (Exception $e) {
            echo "<div style='color: red; margin: 10px 0;'>";
            echo "❌ Recent activity query failed: " . $e->getMessage() . "<br>";
            echo "</div>";
        }
    }
} catch (Exception $e) {
    echo "<div style='color: red; padding: 10px; background: #f8d7da; border-radius: 5px; margin: 10px 0;'>";
    echo "❌ Database error: " . $e->getMessage();
    echo "</div>";
}

// Issue 2: JavaScript/AJAX Problems
echo "<h2>🔍 Issue 2: JavaScript/AJAX Problems</h2>";

echo "<div style='background: #f8f9fa; padding: 15px; border-radius: 5px; margin: 10px 0;'>";
echo "<strong>Common JavaScript Issues:</strong><br>";
echo "1. <strong>Navbar buttons not switching:</strong> Check browser console for JavaScript errors<br>";
echo "2. <strong>AJAX requests failing:</strong> Check Network tab in developer tools<br>";
echo "3. <strong>Recent activity stuck loading:</strong> Usually database connection issue<br>";
echo "4. <strong>Panel switching not working:</strong> JavaScript event listeners not attached<br>";
echo "</div>";

// Test API endpoints directly
echo "<h3>🧪 Testing API Endpoints</h3>";

$baseUrl = 'http://' . $_SERVER['HTTP_HOST'] . dirname($_SERVER['REQUEST_URI']);
$apiUrl = $baseUrl . '/../api/admin.php';

$endpoints = [
    'dashboard' => $apiUrl . '?action=dashboard',
    'queue' => $apiUrl . '?action=queue',
    'bays' => $apiUrl . '?action=bays',
    'users' => $apiUrl . '?action=users'
];

foreach ($endpoints as $name => $url) {
    echo "<div style='margin: 10px 0; padding: 10px; border: 1px solid #ddd; border-radius: 5px;'>";
    echo "<strong>Testing {$name} endpoint:</strong><br>";
    echo "URL: <code>{$url}</code><br>";
    
    // Test with cURL
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_TIMEOUT, 10);
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
    
    $response = curl_exec($ch);
    $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    $error = curl_error($ch);
    curl_close($ch);
    
    if ($error) {
        echo "<span style='color: red;'>❌ cURL Error: {$error}</span><br>";
    } elseif ($httpCode === 200) {
        $data = json_decode($response, true);
        if ($data && isset($data['success'])) {
            echo "<span style='color: green;'>✅ Working - HTTP {$httpCode}</span><br>";
            if (isset($data['error'])) {
                echo "<span style='color: orange;'>⚠️ API Error: {$data['error']}</span><br>";
            }
        } else {
            echo "<span style='color: orange;'>⚠️ Invalid JSON response</span><br>";
            echo "<pre style='background: #f8f9fa; padding: 10px; border-radius: 3px; font-size: 12px;'>" . htmlspecialchars(substr($response, 0, 200)) . "</pre>";
        }
    } else {
        echo "<span style='color: red;'>❌ HTTP Error: {$httpCode}</span><br>";
    }
    echo "</div>";
}

// Issue 3: Session Problems
echo "<h2>🔍 Issue 3: Session Problems</h2>";

if (session_status() === PHP_SESSION_ACTIVE) {
    echo "<div style='color: green; padding: 10px; background: #d4edda; border-radius: 5px; margin: 10px 0;'>";
    echo "✅ PHP sessions are active<br>";
    echo "Session ID: " . session_id() . "<br>";
    echo "</div>";
} else {
    echo "<div style='color: red; padding: 10px; background: #f8d7da; border-radius: 5px; margin: 10px 0;'>";
    echo "❌ PHP sessions are not active!<br>";
    echo "This will cause authentication issues.<br>";
    echo "</div>";
}

// Issue 4: File Permissions
echo "<h2>🔍 Issue 4: File Permissions</h2>";

$criticalFiles = [
    '../api/admin.php' => 'Admin API endpoint',
    '../config/database.php' => 'Database configuration',
    'js/admin.js' => 'Admin JavaScript',
    'css/admin.css' => 'Admin CSS',
    'index.php' => 'Admin dashboard'
];

foreach ($criticalFiles as $file => $description) {
    if (file_exists($file)) {
        if (is_readable($file)) {
            echo "<div style='color: green; margin: 5px 0;'>✅ {$description}: Readable</div>";
        } else {
            echo "<div style='color: red; margin: 5px 0;'>❌ {$description}: Not readable</div>";
        }
    } else {
        echo "<div style='color: red; margin: 5px 0;'>❌ {$description}: File not found</div>";
    }
}

// Issue 5: Browser-specific Issues
echo "<h2>🔍 Issue 5: Browser-specific Issues</h2>";

echo "<div style='background: #fff3cd; padding: 15px; border-radius: 5px; margin: 10px 0;'>";
echo "<strong>Browser Troubleshooting:</strong><br>";
echo "1. <strong>Clear browser cache:</strong> Ctrl+F5 or Cmd+Shift+R<br>";
echo "2. <strong>Disable browser extensions:</strong> Try incognito/private mode<br>";
echo "3. <strong>Check JavaScript console:</strong> F12 → Console tab<br>";
echo "4. <strong>Check Network tab:</strong> Look for failed requests<br>";
echo "5. <strong>Try different browser:</strong> Chrome, Firefox, Edge<br>";
echo "</div>";

// Quick Fixes
echo "<h2>🛠️ Quick Fixes</h2>";

echo "<div style='background: #e7f3ff; padding: 15px; border-radius: 5px; margin: 10px 0;'>";
echo "<strong>Step-by-step fixes:</strong><br><br>";

echo "<strong>1. Fix Recent Activity Loading:</strong><br>";
echo "• Ensure XAMPP MySQL is running<br>";
echo "• Check database 'cephradb' exists<br>";
echo "• Verify table 'queue_tickets' has data<br>";
echo "• Clear browser cache and refresh<br><br>";

echo "<strong>2. Fix Navbar Button Switching:</strong><br>";
echo "• Open browser developer tools (F12)<br>";
echo "• Check Console tab for JavaScript errors<br>";
echo "• Verify admin.js is loading properly<br>";
echo "• Check if jQuery is loaded<br><br>";

echo "<strong>3. Fix API Connection Issues:</strong><br>";
echo "• Test API endpoints directly (see above)<br>";
echo "• Check PHP error logs<br>";
echo "• Verify file permissions<br>";
echo "• Ensure proper URL paths<br>";
echo "</div>";

// Test Results Summary
echo "<h2>📊 Test Results Summary</h2>";

$issues = [];
$fixes = [];

// Check database
if (!$conn) {
    $issues[] = "Database connection failed";
    $fixes[] = "Start XAMPP MySQL service";
} else {
    $fixes[] = "Database connection working";
}

// Check API
$testApi = true;
foreach ($endpoints as $name => $url) {
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_TIMEOUT, 5);
    $response = curl_exec($ch);
    $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);
    
    if ($httpCode !== 200) {
        $testApi = false;
        break;
    }
}

if (!$testApi) {
    $issues[] = "API endpoints not responding";
    $fixes[] = "Check API file permissions and PHP configuration";
} else {
    $fixes[] = "API endpoints working";
}

if (empty($issues)) {
    echo "<div style='color: green; padding: 15px; background: #d4edda; border-radius: 5px; margin: 10px 0;'>";
    echo "🎉 <strong>All systems appear to be working!</strong><br>";
    echo "If you're still experiencing issues, try:<br>";
    echo "• Clear browser cache<br>";
    echo "• Check browser console for errors<br>";
    echo "• Try a different browser<br>";
    echo "</div>";
} else {
    echo "<div style='color: red; padding: 15px; background: #f8d7da; border-radius: 5px; margin: 10px 0;'>";
    echo "⚠️ <strong>Issues found:</strong><br>";
    foreach ($issues as $issue) {
        echo "• {$issue}<br>";
    }
    echo "</div>";
}

echo "<div style='color: blue; padding: 15px; background: #d1ecf1; border-radius: 5px; margin: 10px 0;'>";
echo "<strong>Recommended fixes:</strong><br>";
foreach ($fixes as $fix) {
    echo "• {$fix}<br>";
}
echo "</div>";

echo "<h2>🔗 Quick Actions</h2>";
echo "<div style='margin: 10px 0;'>";
echo "<a href='login.php' style='margin-right: 10px; padding: 10px 20px; background: #007bff; color: white; text-decoration: none; border-radius: 5px;'>🔐 Admin Login</a>";
echo "<a href='index.php' style='margin-right: 10px; padding: 10px 20px; background: #28a745; color: white; text-decoration: none; border-radius: 5px;'>📊 Admin Dashboard</a>";
echo "<a href='test-connection.php' style='margin-right: 10px; padding: 10px 20px; background: #17a2b8; color: white; text-decoration: none; border-radius: 5px;'>🧪 Connection Test</a>";
echo "</div>";

echo "</div>";
?>
