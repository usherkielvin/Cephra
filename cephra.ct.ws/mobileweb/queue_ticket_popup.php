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
    <link rel="stylesheet" href="css/pages/queue_ticket_popup.css" />
</head>
<body>
    <div id="queuePopup">
        <h2>Your Queue Ticket</h2>
        <div class="info"><strong>Ticket ID:</strong> <?php echo htmlspecialchars($currentTicket); ?></div>
        <div class="info"><strong>Service:</strong> <?php echo htmlspecialchars($currentService); ?></div>
        <div class="info"><strong>Battery Level:</strong> <?php echo htmlspecialchars($batteryLevel); ?>%</div>
        <div class="info"><strong>Estimated Wait Time:</strong> <?php echo htmlspecialchars($estimatedMinutes); ?> minutes</div>
        <button onclick="closePopup()">Close</button>
    </div>

    <script>
        function closePopup() {
            document.getElementById('queuePopup').style.display = 'none';
        }
    </script>
</body>
</html>
