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

// Check if car is already linked and user has battery level
$isCarLinked = false;
$hasBatteryData = false;

if ($conn) {
    // Check if user has battery data (equivalent to car being linked)
    $stmt = $conn->prepare("SELECT COUNT(*) FROM battery_levels WHERE username = :username");
    $stmt->bindParam(':username', $username);
    $stmt->execute();
    $hasBatteryData = $stmt->fetchColumn() > 0;

    if ($hasBatteryData) {
        // Get battery level
        $stmt = $conn->prepare("SELECT battery_level FROM battery_levels WHERE username = :username");
        $stmt->bindParam(':username', $username);
        $stmt->execute();
        $result = $stmt->fetch(PDO::FETCH_ASSOC);
        $batteryLevel = (int)($result['battery_level'] ?? 0);

        if ($batteryLevel != -1) {
            // Car is linked and battery is initialized - redirect to dashboard
            header("Location: dashboard.php");
            exit();
        }
    }
}

// Handle form submissions
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (isset($_POST['link_car'])) {
        // Link car functionality
        if ($conn && !$hasBatteryData) {
            // Initialize battery to random level (15-50%) only if not already initialized
            $batteryLevel = 15 + rand(0, 35); // 15 to 50

            $stmt = $conn->prepare("INSERT INTO battery_levels (username, battery_level, initial_battery_level, battery_capacity_kwh) VALUES (:username, :battery_level, :initial_battery_level, 40.0)");
            $stmt->bindParam(':username', $username);
            $stmt->bindParam(':battery_level', $batteryLevel);
            $stmt->bindParam(':initial_battery_level', $batteryLevel);

            if ($stmt->execute()) {
                // Set car as linked in session
                $_SESSION['car_linked'] = true;

                // Redirect to dashboard
                header("Location: dashboard.php");
                exit();
            } else {
                $error = "Failed to link car. Please try again.";
            }
        }
    }
}
?>
<!DOCTYPE HTML>
<html>
<head>
    <title>Link Your Car - Cephra</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="icon" type="image/png" href="images/logo.png" />
    <link rel="stylesheet" href="assets/css/main.css" />
    <style>
        .link-container {
            text-align: center;
            padding: 20px;
        }
        .link-button {
            background: #4CAF50;
            color: white;
            padding: 15px 30px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
            margin: 20px 0;
        }
        .link-button:disabled {
            background: #cccccc;
            cursor: not-allowed;
        }
        .terms-checkbox {
            margin: 20px 0;
            font-size: 14px;
        }
        .nav-buttons {
            position: fixed;
            bottom: 20px;
            left: 50%;
            transform: translateX(-50%);
            display: flex;
            gap: 20px;
        }
        .nav-button {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            border: none;
            background: #007bff;
            color: white;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 20px;
        }
        .error-message {
            color: red;
            margin: 10px 0;
        }
    </style>
</head>
<body class="homepage is-preload">
    <div id="page-wrapper">
        <!-- Header -->
        <div id="header-wrapper">
            <!-- Header -->
            <header id="header">
                <div class="inner">
                    <!-- Logo -->
                    <h1>
                        <a href="dashboard.php" id="logo">Cephra</a>
                    </h1>
                    <!-- Nav -->
                    <nav id="nav">
                        <ul>
                            <li><a href="dashboard.php">Home</a></li>
                            <li class="current_page_item"><a href="link.php">Link</a></li>
                            <li><a href="right-sidebar.html">History</a></li>
                            <li><a href="no-sidebar.html">Profile</a></li>
                        </ul>
                    </nav>
                </div>
            </header>
        </div>

        <!-- Main Content -->
        <div id="main-wrapper">
            <div class="wrapper style1">
                <div class="inner">
                    <div class="link-container">
                        <h2>Link Your Electric Vehicle</h2>
                        <p>Connect your EV to start charging at Cephra stations</p>

                        <img src="images/ConnectCar.gif" alt="Connect Car" style="max-width: 100%; height: auto; margin: 20px 0;">

                        <form method="post" id="linkForm">
                            <div class="terms-checkbox">
                                <input type="checkbox" id="terms" name="terms" required>
                                <label for="terms">By linking, I agree to the <a href="#" onclick="showTerms(); return false;">Terms & Conditions</a></label>
                            </div>

                            <?php if (isset($error)): ?>
                                <div class="error-message"><?php echo htmlspecialchars($error); ?></div>
                            <?php endif; ?>

                            <button type="submit" name="link_car" class="link-button" id="linkBtn" disabled>
                                Link My Car
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Navigation Buttons -->
        <div class="nav-buttons">
            <button class="nav-button" onclick="window.location.href='dashboard.php'" title="Home">🏠</button>
            <button class="nav-button" onclick="window.location.href='ChargingPage.php'" title="Charge">🔋</button>
            <button class="nav-button" onclick="window.location.href='dashboard.php'" title="Profile">👤</button>
        </div>
    </div>

    <!-- Scripts -->
    <script src="assets/js/jquery.min.js"></script>
    <script src="assets/js/jquery.dropotron.min.js"></script>
    <script src="assets/js/browser.min.js"></script>
    <script src="assets/js/breakpoints.min.js"></script>
    <script src="assets/js/util.js"></script>
    <script src="assets/js/main.js"></script>

    <script>
        function showTerms() {
            const termsText = `CEPHRA EV LINKING TERMS AND CONDITIONS
Effective Date: <?php echo date('Y-m-d'); ?>
Version: 1.0

1. ACCEPTANCE OF TERMS
By linking your electric vehicle (EV) to the Cephra app (the "Service"), you agree to these Terms.

2. LINKING PURPOSE
Linking enables ticketing, charging session history, and status updates within the app.

3. DATA COLLECTED
Vehicle identifiers, session timestamps, kWh consumed, payment references, and diagnostic status necessary for Service delivery.

4. USER RESPONSIBILITIES
You confirm you are authorized to link the vehicle and will keep your account secure.

5. CONSENT TO COMMUNICATIONS
You consent to in-app notifications and transactional emails about charging and tickets.

6. PRIVACY
We process data per our Privacy Policy. Data is retained only as long as needed for the Service.

7. LIMITATIONS
The Service provides information "as is" and availability may vary by station and network conditions.

8. SECURITY
We employ reasonable safeguards, but you acknowledge inherent risks in networked systems.

9. UNLINKING
You may unlink your vehicle at any time from the app; some records may be retained for compliance.

10. LIABILITY
To the maximum extent permitted by law, Cephra is not liable for indirect or consequential damages.

11. GOVERNING LAW
These Terms are governed by the laws of Pasay City, Philippines.

12. CONTACT
Cephra Support — support@cephra.com | +63 2 8XXX XXXX

By checking "I agree", you confirm you have read and accept these Terms.`;

            // Create modal dialog
            const modal = document.createElement('div');
            modal.style.cssText = `
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0,0,0,0.8);
                display: flex;
                align-items: center;
                justify-content: center;
                z-index: 10000;
            `;

            const modalContent = document.createElement('div');
            modalContent.style.cssText = `
                background: white;
                padding: 20px;
                border-radius: 10px;
                max-width: 600px;
                max-height: 80vh;
                overflow-y: auto;
                position: relative;
            `;

            modalContent.innerHTML = `
                <h3>Terms and Conditions</h3>
                <pre style="white-space: pre-wrap; font-family: inherit; font-size: 14px; line-height: 1.5;">${termsText}</pre>
                <button onclick="this.closest('div').parentElement.remove()" style="margin-top: 20px; padding: 10px 20px; background: #007bff; color: white; border: none; border-radius: 5px; cursor: pointer;">OK</button>
            `;

            modal.appendChild(modalContent);
            document.body.appendChild(modal);
        }

        // Form validation
        document.getElementById('linkForm').addEventListener('submit', function(e) {
            const termsCheckbox = document.getElementById('terms');
            if (!termsCheckbox.checked) {
                e.preventDefault();
                alert('Please agree to the Terms & Conditions to continue.');
                return false;
            }
        });

        // Enable/disable button based on terms checkbox
        document.getElementById('terms').addEventListener('change', function() {
            const linkBtn = document.getElementById('linkBtn');
            if (this.checked) {
                linkBtn.disabled = false;
                linkBtn.style.background = '#4CAF50';
                linkBtn.style.cursor = 'pointer';
            } else {
                linkBtn.disabled = true;
                linkBtn.style.background = '#cccccc';
                linkBtn.style.cursor = 'not-allowed';
            }
        });

        // Initialize button state on page load
        document.addEventListener('DOMContentLoaded', function() {
            const termsCheckbox = document.getElementById('terms');
            const linkBtn = document.getElementById('linkBtn');
            if (!termsCheckbox.checked) {
                linkBtn.disabled = true;
                linkBtn.style.background = '#cccccc';
                linkBtn.style.cursor = 'not-allowed';
            }
        });
    </script>
</body>
</html>
