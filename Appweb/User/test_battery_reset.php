<?php
session_start();
if (!isset($_SESSION['username'])) {
    header('Location: index.php');
    exit();
}

require_once 'config/database.php';

$username = $_SESSION['username'];
$db = new Database();
$conn = $db->getConnection();

if (!$conn) {
    die('Database connection failed');
}

// Get current battery level
$stmt = $conn->prepare("SELECT battery_level FROM battery_levels WHERE username = :username LIMIT 1");
$stmt->bindParam(':username', $username);
$stmt->execute();
$row = $stmt->fetch(PDO::FETCH_ASSOC);
$currentLevel = $row ? $row['battery_level'] : 'Not found';

// Set battery to 100% for testing
$stmt = $conn->prepare("UPDATE battery_levels SET battery_level = 100 WHERE username = :username");
$stmt->bindParam(':username', $username);
$stmt->execute();

echo "<h2>Battery Reset Test</h2>";
echo "<p>Current battery level: $currentLevel%</p>";
echo "<p>Battery set to 100% for testing</p>";
echo "<p><a href='profile_logout.php'>Click here to logout and test battery reset</a></p>";
echo "<p><a href='dashboard.php'>Back to Dashboard</a></p>";

// Show recent error logs
echo "<h3>Recent Logout Logs:</h3>";
echo "<pre>";
$logFile = '/var/log/apache2/error.log'; // Adjust path as needed
if (file_exists($logFile)) {
    $logs = file_get_contents($logFile);
    $lines = explode("\n", $logs);
    $recentLogs = array_slice($lines, -20); // Last 20 lines
    foreach ($recentLogs as $line) {
        if (strpos($line, 'Logout:') !== false) {
            echo htmlspecialchars($line) . "\n";
        }
    }
} else {
    echo "Log file not found. Check your server's error log for logout messages.";
}
echo "</pre>";
?>
