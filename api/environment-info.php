<?php
require_once '../config/database.php';

// Create database instance to get environment info
$db = new Database();
$envInfo = $db->getEnvironmentInfo();

// Set JSON header
header('Content-Type: application/json');

// Return environment information
echo json_encode([
    'status' => 'success',
    'environment' => $envInfo['environment'],
    'database_info' => [
        'host' => $envInfo['host'],
        'database' => $envInfo['database'],
        'username' => $envInfo['username']
    ],
    'server_info' => [
        'http_host' => $_SERVER['HTTP_HOST'] ?? 'unknown',
        'server_name' => $_SERVER['SERVER_NAME'] ?? 'unknown',
        'request_uri' => $_SERVER['REQUEST_URI'] ?? 'unknown'
    ],
    'timestamp' => date('Y-m-d H:i:s')
], JSON_PRETTY_PRINT);
?>
