<?php
// Test the progress-next-ticket API directly
header("Content-Type: text/plain");

echo "=== TESTING PROGRESS-NEXT-TICKET API ===\n\n";

// Simulate the API call
$_SERVER['REQUEST_METHOD'] = 'POST';
$_POST['action'] = 'progress-next-ticket';

echo "Calling API with action: progress-next-ticket\n\n";

// Include the API file
ob_start();
include 'api/admin-clean.php';
$output = ob_get_clean();

echo "=== API RESPONSE ===\n";
echo $output . "\n\n";

echo "=== TEST COMPLETE ===\n";
?>
