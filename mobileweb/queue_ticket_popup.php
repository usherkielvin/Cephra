<?php
session_start();
if (!isset($_SESSION['username'])) {
    header("Location: index.php");
    exit();
}

require_once '../config/database.php';

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
    <style>
        #queuePopup {
            position: fixed;
            top: 20%;
            left: 50%;
            transform: translate(-50%, -20%);
            background: white;
            border: 2px solid #007bff;
            border-radius: 10px;
            padding: 20px;
            width: 300px;
            z-index: 10000;
            box-shadow: 0 0 10px rgba(0,0,0,0.5);
        }
        #queuePopup h2 {
            margin-top: 0;
            color: #007bff;
            text-align: center;
        }
        #queuePopup .info {
            margin: 10px 0;
            font-size: 16px;
            text-align: center;
        }
        #queuePopup button {
            display: block;
            margin: 15px auto 0;
            padding: 10px 20px;
            background: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        #queuePopup button:hover {
            background: #0056b3;
        }
    </style>
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
