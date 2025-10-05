<?php
session_start();
if (!isset($_SESSION['username'])) {
    header("Location: index.php");
    exit();
}

require_once 'config/database.php';

$db = new Database();
$conn = $db->getConnection();

$username = $_SESSION['username'];

// Fetch current ticket and service info for the user
$currentTicket = null;
$currentService = null;

if ($conn) {
    $stmt = $conn->prepare("SELECT ticket_id, service_type FROM tickets WHERE username = :username AND status = 'active' LIMIT 1");
    $stmt->bindParam(':username', $username);
    $stmt->execute();
    $result = $stmt->fetch(PDO::FETCH_ASSOC);
    if ($result) {
        $currentTicket = $result['ticket_id'];
        $currentService = $result['service_type'];
    }
}

if (!$currentTicket) {
    echo json_encode(['error' => 'No active ticket found']);
    exit();
}

// Fetch battery level and estimated time (dummy calculation here)
$stmt = $conn->prepare("SELECT battery_level FROM battery_levels WHERE username = :username");
$stmt->bindParam(':username', $username);
$stmt->execute();
$batteryLevel = $stmt->fetchColumn();

$estimatedMinutes = 5; // Placeholder, replace with actual logic

?>

<!DOCTYPE html>
<html>
<head>
    <title>Queue Ticket</title>
    <link rel="stylesheet" href="assets/css/pages/queue_ticket_popup.css" />
</head>
<body>
    <div class="popup-overlay" id="popupOverlay">
        <div class="popup-container">
            <div class="popup-header">
                <h3>Queue Ticket</h3>
                <button class="close-btn" onclick="closePopup()">&times;</button>
            </div>
            <div class="popup-body">
                <div class="ticket-info">
                    <div class="info-row">
                        <span class="label">Ticket ID:</span>
                        <span class="value"><?php echo htmlspecialchars($currentTicket); ?></span>
                    </div>
                    <div class="info-row">
                        <span class="label">Service:</span>
                        <span class="value"><?php echo htmlspecialchars($currentService); ?></span>
                    </div>
                    <div class="info-row">
                        <span class="label">Battery:</span>
                        <span class="value"><?php echo htmlspecialchars($batteryLevel); ?>%</span>
                    </div>
                    <div class="info-row">
                        <span class="label">Wait Time:</span>
                        <span class="value"><?php echo htmlspecialchars($estimatedMinutes); ?> min</span>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        function closePopup() {
            document.getElementById('popupOverlay').style.display = 'none';
        }
        
        // Close popup when clicking outside
        document.getElementById('popupOverlay').addEventListener('click', function(e) {
            if (e.target === this) {
                closePopup();
            }
        });
        
        // Close popup with Escape key
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') {
                closePopup();
            }
        });
    </script>
</body>
</html>
