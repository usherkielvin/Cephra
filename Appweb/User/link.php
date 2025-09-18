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

// Check if car is already linked and user has battery level
$isCarLinked = false;
$hasBatteryData = false;
$currentBatteryLevel = null;

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
        $batteryLevel = (int)($result['battery_level'] ?? -1);
        $currentBatteryLevel = $batteryLevel;

        if ($batteryLevel > 0) {
            // Car is linked and battery is initialized - keep Link panel visible
            $isCarLinked = true;
        }
    }
}

// Handle form submissions
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (isset($_POST['link_car'])) {
        // Link car functionality
        if ($conn) {
            // Initialize battery to random level (15-50%)
            $newBatteryLevel = 15 + rand(0, 35); // 15 to 50

            if (!$hasBatteryData) {
                // No record yet -> insert
                $stmt = $conn->prepare("INSERT INTO battery_levels (username, battery_level, initial_battery_level, battery_capacity_kwh) VALUES (:username, :battery_level, :initial_battery_level, 40.0)");
                $stmt->bindParam(':username', $username);
                $stmt->bindParam(':battery_level', $newBatteryLevel);
                $stmt->bindParam(':initial_battery_level', $newBatteryLevel);
            } else if ($currentBatteryLevel <= 0) {
                // Record exists but unlinked sentinel (-1) -> update
                $stmt = $conn->prepare("UPDATE battery_levels SET battery_level = :battery_level, initial_battery_level = :battery_level WHERE username = :username");
                $stmt->bindParam(':username', $username);
                $stmt->bindParam(':battery_level', $newBatteryLevel);
            } else {
                // Already linked -> nothing to do
                $stmt = null;
            }

            if ($stmt && $stmt->execute()) {
                $_SESSION['car_linked'] = true;
                header("Location: dashboard.php");
                exit();
            } elseif ($stmt) {
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
    <link rel="icon" type="image/png" href="images/logo.png?v=2" />
    <link rel="apple-touch-icon" href="images/logo.png?v=2" />
    <link rel="manifest" href="manifest.webmanifest" />
    <meta name="theme-color" content="#062635" />
    <link rel="stylesheet" href="css/main.css" />
    <link rel="stylesheet" href="css/pages/link.css" />
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
                        <a href="dashboard.php" id="logo" style="display:inline-flex;align-items:center;gap:8px;"><img src="images/logo.png" alt="Cephra" style="width:28px;height:28px;border-radius:6px;object-fit:cover;vertical-align:middle;" /><span>Cephra</span></a>
                    </h1>
                    <!-- Nav -->
                    <nav id="nav">
                        <ul>
                            <li><a href="dashboard.php">Home</a></li>
                            <li class="current_page_item"><a href="link.php">Link</a></li>
                            <li><a href="history.php">History</a></li>
                            <li><a href="profile.php">Profile</a></li>
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
                        <?php if (!empty($isCarLinked)): ?>
                            <h2>Your Vehicle is Linked</h2>
                            <p>Porsche connected. You're ready to charge.</p>
                            <img src="images/ads.png" alt="Porsche" style="max-width: 100%; height: auto; margin: 20px 0; border-radius:8px;">
                            <div style="display:flex; gap:12px; justify-content:center;">
                                <a href="dashboard.php" class="button">Go to Dashboard</a>
                                <a href="ChargingPage.php" class="button alt">Start Charging</a>
                            </div>
                        <?php else: ?>
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
                        <?php endif; ?>
                        <div class="panel-nav" style="display:flex; gap:12px; justify-content:center; margin-top:16px;">
                            <a class="button alt" href="profile.php">Prev: Profile</a>
                            <a class="button" href="history.php">Next: History</a>
                        </div>
                    </div>
                </div>
            </div>
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
        function showDialog(title, message) {
            const overlay = document.createElement('div');
            overlay.style.cssText = 'position:fixed;inset:0;background:rgba(0,0,0,0.6);display:flex;align-items:center;justify-content:center;z-index:10000;padding:16px;';
            const dialog = document.createElement('div');
            dialog.style.cssText = 'width:100%;max-width:360px;background:#fff;border-radius:12px;box-shadow:0 10px 30px rgba(0,0,0,0.25);overflow:hidden;';
            const header = document.createElement('div');
            header.style.cssText = 'background:#00c2ce;color:#fff;padding:12px 16px;font-weight:700';
            header.textContent = title || 'Notice';
            const body = document.createElement('div');
            body.style.cssText = 'padding:16px;color:#333;line-height:1.5;';
            body.textContent = message || '';
            const footer = document.createElement('div');
            footer.style.cssText = 'padding:12px 16px;display:flex;justify-content:flex-end;gap:8px;background:#f7f7f7;';
            const ok = document.createElement('button');
            ok.textContent = 'OK';
            ok.style.cssText = 'background:#00c2ce;color:#fff;border:0;padding:8px 14px;border-radius:8px;cursor:pointer;';
            ok.onclick = () => document.body.removeChild(overlay);
            footer.appendChild(ok);
            dialog.appendChild(header);
            dialog.appendChild(body);
            dialog.appendChild(footer);
            overlay.appendChild(dialog);
            document.body.appendChild(overlay);
        }
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
                <button onclick="this.closest('div').parentElement.remove(); document.getElementById('terms').checked = true;" style="margin-top: 20px; padding: 10px 20px; background: #007bff; color: white; border: none; border-radius: 5px; cursor: pointer;">I Agree</button>
            `;

            modal.appendChild(modalContent);
            document.body.appendChild(modal);
        }

        // Form validation
        document.getElementById('linkForm').addEventListener('submit', function(e) {
            const termsCheckbox = document.getElementById('terms');
            if (!termsCheckbox.checked) {
                e.preventDefault();
                showDialog('Terms Required', 'You must agree to the Terms & Conditions before linking your car.');
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
            // Ensure click triggers submission when enabled
            linkBtn.addEventListener('click', function(e) {
                if (!termsCheckbox.checked) {
                    e.preventDefault();
                    showDialog('Link Vehicle', 'Please agree to the Terms & Conditions to continue.');
                    return false;
                }
                // allow normal form submit
            });
        });
    </script>
</body>
</html>
