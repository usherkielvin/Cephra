<?php
header('Content-Type: application/json');

echo json_encode([
    'request_uri' => $_SERVER['REQUEST_URI'],
    'script_name' => $_SERVER['SCRIPT_NAME'],
    'path_info' => $_SERVER['PATH_INFO'] ?? 'null',
    'query_string' => $_SERVER['QUERY_STRING'] ?? 'null',
    'method' => $_SERVER['REQUEST_METHOD'],
    'server_name' => $_SERVER['SERVER_NAME'],
    'http_host' => $_SERVER['HTTP_HOST'],
    'document_root' => $_SERVER['DOCUMENT_ROOT'],
    'current_file' => __FILE__
]);
?>
