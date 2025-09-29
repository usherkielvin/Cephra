<?php
session_start();
if (!isset($_SESSION['username'])) {
    header("Location: index.php");
    exit();
}

require_once 'config/database.php';

$db = new Database();
$conn = $db->getConnection();

// Function to process image and remove white background
function process_image($image_path) {
    $processed_dir = 'images/processed/';
    if (!is_dir($processed_dir)) {
        mkdir($processed_dir, 0755, true);
    }

    $processed_path = $processed_dir . basename($image_path, '.' . pathinfo($image_path, PATHINFO_EXTENSION)) . '.png';

    if (file_exists($processed_path)) {
        return $processed_path;
    }

    // Load the image
    $image_info = getimagesize($image_path);
    if (!$image_info) {
        return $image_path; // Return original if can't load
    }

    $mime = $image_info['mime'];
    switch ($mime) {
        case 'image/jpeg':
            $src_image = imagecreatefromjpeg($image_path);
            break;
        case 'image/png':
            $src_image = imagecreatefrompng($image_path);
            break;
        case 'image/gif':
            $src_image = imagecreatefromgif($image_path);
            break;
        default:
            return $image_path;
    }

    if (!$src_image) {
        return $image_path;
    }

    $width = imagesx($src_image);
    $height = imagesy($src_image);

    // Create new image with alpha channel
    $new_image = imagecreatetruecolor($width, $height);
    imagealphablending($new_image, false);
    imagesavealpha($new_image, true);

    // Define white color (adjust fuzziness if needed)
    $white = imagecolorallocate($new_image, 255, 255, 255);

    // Copy pixels, making white transparent
    for ($x = 0; $x < $width; $x++) {
        for ($y = 0; $y < $height; $y++) {
            $rgb = imagecolorat($src_image, $x, $y);
            $colors = imagecolorsforindex($src_image, $rgb);
            $r = $colors['red'];
            $g = $colors['green'];
            $b = $colors['blue'];

            // If color is close to white (within 10), make transparent
            if ($r > 245 && $g > 245 && $b > 245) {
                $alpha = 127; // Fully transparent
            } else {
                $alpha = 0; // Opaque
            }

            $color = imagecolorallocatealpha($new_image, $r, $g, $b, $alpha);
            imagesetpixel($new_image, $x, $y, $color);
        }
    }

    // Save as PNG
    imagepng($new_image, $processed_path);
    imagedestroy($src_image);
    imagedestroy($new_image);

    return $processed_path;
}

$username = $_SESSION['username'];

// Check car_index from users table
$carIndex = null;
if ($conn) {
    $stmt = $conn->prepare("SELECT car_index FROM users WHERE username = :username");
    $stmt->bindParam(':username', $username);
    $stmt->execute();
    $result = $stmt->fetch(PDO::FETCH_ASSOC);
    $carIndex = $result['car_index'] ?? null;
}

// Handle form submissions
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (isset($_POST['link_car'])) {
        // Link car functionality
        if ($conn) {
            // Generate random car_index between 0-8 to match vehicle specs
            $newCarIndex = rand(0, 8);
            $stmt = $conn->prepare("UPDATE users SET car_index = :car_index WHERE username = :username");
            $stmt->bindParam(':car_index', $newCarIndex);
            $stmt->bindParam(':username', $username);
            if ($stmt->execute()) {
                $_SESSION['car_linked'] = true;
                header("Location: link.php"); // Refresh to show "With Car"
                exit();
            } else {
                $error = "Failed to link car. Please try again.";
            }
        }
    }
}

// Fetch battery level from database
$db_battery_level = null;
if ($conn && $username) {
    $stmt_battery = $conn->prepare("SELECT battery_level FROM battery_levels WHERE username = :username ORDER BY last_updated DESC LIMIT 1");
    $stmt_battery->bindParam(':username', $username);
    $stmt_battery->execute();
    $battery_row = $stmt_battery->fetch(PDO::FETCH_ASSOC);
    $db_battery_level = $battery_row ? $battery_row['battery_level'] . '%' : null;
}

// Vehicle data based on car_index
$vehicle_data = null;
if ($carIndex !== null && $carIndex >= 0 && $carIndex <= 8) {
    // Real EV models
    $models = [
        0 => 'Audi q8 etron',
        1 => 'Nissan leaf',
        2 => 'Tesla x',
        3 => 'Lotus Spectre',
        4 => 'BYD Seagull',
        5 => 'Hyundai',
        6 => 'Porsche Taycan',
        7 => 'BYD Tang',
        8 => 'omada e5'
    ];

    // Car images
    $car_images = [
        0 => 'images/audiq8etron.jpg',
        1 => 'images/nissanleaf_.jpg',
        2 => 'images/teslamodelx.jpeg',
        3 => 'images/lotuseltre.png',
        4 => 'images/bydseagull.jpg',
        5 => 'images/default.png',
        6 => 'images/porschetaycan.jpg',
        7 => 'images/bydtang.jpg',
        8 => 'images/omodae5.png'
    ];

    // Realistic vehicle specs based on model
    $vehicle_specs = [
        0 => ['range' => '450 km', 'time_to_full' => '6h 0m', 'battery_level' => '80%', 'hp' => 300], // Audi q8 etron
        1 => ['range' => '220 km', 'time_to_full' => '8h 0m', 'battery_level' => '72%', 'hp' => 150], // Nissan leaf
        2 => ['range' => '400 km', 'time_to_full' => '7h 0m', 'battery_level' => '85%', 'hp' => 450], // Tesla x
        3 => ['range' => '500 km', 'time_to_full' => '5h 0m', 'battery_level' => '90%', 'hp' => 600], // Lotus Spectre
        4 => ['range' => '300 km', 'time_to_full' => '5h 0m', 'battery_level' => '75%', 'hp' => 200], // BYD Seagull
        5 => ['range' => '484 km', 'time_to_full' => '7h 20m', 'battery_level' => '95%', 'hp' => 400], // Hyundai
        6 => ['range' => '400 km', 'time_to_full' => '6h 0m', 'battery_level' => '85%', 'hp' => 500], // Porsche Taycan
        7 => ['range' => '400 km', 'time_to_full' => '7h 0m', 'battery_level' => '80%', 'hp' => 350], // BYD Tang
        8 => ['range' => '350 km', 'time_to_full' => '6h 0m', 'battery_level' => '78%', 'hp' => 250] // omada e5
    ];

    $vehicle_data = [
        'model' => $models[$carIndex],
        'status' => 'Connected & Charging',
        'range' => $vehicle_specs[$carIndex]['range'],
        'time_to_full' => $vehicle_specs[$carIndex]['time_to_full'],
        'battery_level' => $db_battery_level ?? $vehicle_specs[$carIndex]['battery_level'],
        'image' => $car_images[$carIndex]
    ];

    // Calculate range and time_to_full based on battery level
    $battery_level_str = $vehicle_data['battery_level'];
    $battery_level_num = floatval(str_replace('%', '', $battery_level_str));

    // Get max range from specs (assuming it's the max at 100%)
    $max_range_km = intval(str_replace(' km', '', $vehicle_specs[$carIndex]['range']));

    // Parse max charge time from specs
    $time_str = $vehicle_specs[$carIndex]['time_to_full'];
    preg_match('/(\d+)h\s*(\d+)m/', $time_str, $matches);
    $max_charge_time_hours = 0;
    if ($matches) {
        $hours = intval($matches[1]);
        $mins = intval($matches[2]);
        $max_charge_time_hours = $hours + $mins / 60;
    }

    // Calculate current range
    $current_range_km = round($max_range_km * ($battery_level_num / 100));
    $vehicle_data['range'] = $current_range_km . ' km';

    // Calculate time to full
    $time_to_full_hours = $max_charge_time_hours * ((100 - $battery_level_num) / 100);
    $hours_full = floor($time_to_full_hours);
    $mins_full = round(($time_to_full_hours - $hours_full) * 60);
    $vehicle_data['time_to_full'] = $hours_full . 'h ' . $mins_full . 'm';
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
    <link rel="stylesheet" href="css/vantage-style.css" />
    <link rel="stylesheet" href="assets/css/fontawesome-all.min.css" />
    <link rel="stylesheet" href="assets/css/pages/link.css" />
</head>
<body class="homepage is-preload">
    <div id="page-wrapper">
        <?php include __DIR__ . '/partials/header.php'; ?>

        <!-- Link Section -->
        <section class="link-section" style="padding: 100px 0; background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);">
            <div class="container">
                <div class="section-header" style="text-align: center; margin-bottom: 60px;">
                    <h2 class="section-title" style="font-size: 2.5rem; font-weight: 700; margin-bottom: 1rem; background: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text;">Link Your Car</h2>
                    <p class="section-description" style="font-size: 1.2rem; color: rgba(26, 32, 44, 0.8); max-width: 600px; margin: 0 auto;">Connect your electric vehicle to start charging at Cephra stations</p>
                </div>

                <div class="link-container" style="background: white; border-radius: 20px; padding: 2rem; border: 1px solid rgba(26, 32, 44, 0.1); box-shadow: 0 5px 15px rgba(0, 194, 206, 0.1); max-width: 600px; margin: 0 auto;">
                    <?php if (is_null($carIndex)): ?>
                        <!-- No Car Design -->
                        <h3 style="text-align: center; margin-bottom: 1rem; color: #1a202c;">Link Your Electric Vehicle</h3>
                        <p style="text-align: center; margin-bottom: 2rem; color: rgba(26, 32, 44, 0.7);">Connect your EV to start charging at Cephra stations</p>
                        <img src="images/ConnectCar.gif" alt="Connect Car" style="max-width: 100%; height: auto; margin: 20px 0; border-radius:8px; display: block; margin-left: auto; margin-right: auto;">
                        <form method="post" id="linkForm">
                            <div class="terms-checkbox" style="margin-bottom: 1rem;">
                                <input type="checkbox" id="terms" name="terms" required>
                                <label for="terms">By linking, I agree to the <a href="#" onclick="showTerms(); return false;">Terms & Conditions</a></label>
                            </div>
                            <?php if (isset($error)): ?>
                                <div class="error-message" style="color: red; margin-bottom: 1rem;"><?php echo htmlspecialchars($error); ?></div>
                            <?php endif; ?>
                            <button type="submit" name="link_car" class="link-button" id="linkBtn" disabled style="width: 100%; padding: 0.75rem; background: #cccccc; color: white; border: none; border-radius: 8px; cursor: not-allowed; transition: all 0.3s ease;">
                                Link My Car
                            </button>
                        </form>
                    <?php else: ?>
                        <!-- With Car Design -->
                        <h3 style="text-align: center; margin-bottom: 1rem; color: #1a202c;">Your Vehicle is Linked</h3>
                        <p style="text-align: center; margin-bottom: 2rem; color: rgba(26, 32, 44, 0.7);"><?php echo htmlspecialchars($vehicle_data['model']); ?> connected. You're ready to charge.</p>
                        <img src="<?php echo htmlspecialchars($vehicle_data['image']); ?>" alt="<?php echo htmlspecialchars($vehicle_data['model']); ?>" style="max-width: 100%; height: auto; margin: 20px 0; border-radius:8px; display: block; margin-left: auto; margin-right: auto;">
                        <div class="car-details" style="margin-top: 2rem;">
                            <div class="detail-row" style="margin-bottom: 1rem;">
                                <span class="detail-label">Car Model:</span>
                                <span class="detail-value"><?php echo htmlspecialchars($vehicle_data['model']); ?></span>
                            </div>
                            <div class="detail-row" style="margin-bottom: 1rem;">
                                <span class="detail-label">Performance:</span>
                                <span class="detail-value"><?php echo $vehicle_specs[$carIndex]['hp']; ?> HP</span>
                            </div>
                            <div class="detail-row" style="margin-bottom: 1rem;">
                                <span class="detail-label">Kms Remaining:</span>
                                <span class="detail-value"><?php echo htmlspecialchars($vehicle_data['range']); ?></span>
                            </div>
                            <div class="detail-row" style="margin-bottom: 1rem;">
                                <span class="detail-label">Time to Charge:</span>
                                <span class="detail-value"><?php echo htmlspecialchars($vehicle_data['time_to_full']); ?></span>
                            </div>
                            <div class="detail-row" style="margin-bottom: 1rem;">
                                <span class="detail-label">Battery Level:</span>
                                <div class="progress-bar" style="width: 100%; background: #e0e0e0; border-radius: 10px; overflow: hidden;">
                                    <div class="progress-fill" style="height: 20px; background: linear-gradient(135deg, #00c2ce 0%, #0e3a49 100%); width: <?php echo $battery_level_num; ?>%; transition: width 0.3s ease;"></div>
                                </div>
                                <span class="progress-text" style="display: block; text-align: center; margin-top: 0.5rem;"><?php echo htmlspecialchars($vehicle_data['battery_level']); ?></span>
                            </div>
                        </div>
                        <div style="display:flex; gap:12px; justify-content:center; margin-top: 2rem;">
                            <a href="dashboard.php" class="button">Go to Dashboard</a>
                            <a href="ChargingPage.php" class="button alt">Start Charging</a>
                        </div>
                    <?php endif; ?>
                </div>
            </div>
        </section>
        
		<!-- Footer -->
		<?php include __DIR__ . '/partials/footer.php'; ?>
    </div>

    <!-- Scripts -->
    <script src="assets/js/jquery.min.js"></script>
    <script src="assets/js/jquery.dropotron.min.js"></script>
    <script src="assets/js/browser.min.js"></script>
    <script src="assets/js/breakpoints.min.js"></script>
    <script src="assets/js/util.js"></script>
    <script src="assets/js/main.js"></script>

    <script>
        // Mobile menu toggle
        document.getElementById('mobileMenuToggle').addEventListener('click', function() {
            const nav = document.querySelector('.nav');
            nav.classList.toggle('mobile-menu-open');
            this.classList.toggle('active');
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
    </script>
</body>
</html>
