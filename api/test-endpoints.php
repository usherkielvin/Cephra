<?php
/**
 * API Endpoint Test Suite for Cephra Web Interface
 * 
 * This file tests all the API endpoints to ensure they work correctly.
 * Run this file to verify your API is functioning properly.
 */

// Enable error reporting
error_reporting(E_ALL);
ini_set('display_errors', 1);

// Include the database connection
require_once '../config/database.php';

class ApiEndpointTester {
    private $db;
    private $baseUrl;
    private $testResults = [];
    
    public function __construct() {
        $this->db = (new Database())->getConnection();
        $this->baseUrl = 'http://localhost:8080/api';
    }
    
    /**
     * Run all API endpoint tests
     */
    public function runAllTests() {
        echo "<h1>🧪 Cephra API Endpoint Tests</h1>\n";
        echo "<div style='font-family: Arial, sans-serif; max-width: 800px; margin: 0 auto;'>\n";
        
        $this->testDatabaseConnection();
        $this->testLoginEndpoint();
        $this->testQueueEndpoint();
        $this->testCreateTicketEndpoint();
        $this->testViewQueuePage();
        $this->testErrorHandling();
        
        $this->displayResults();
        echo "</div>\n";
    }
    
    /**
     * Test database connection
     */
    private function testDatabaseConnection() {
        echo "<h2>📊 Database Connection Test</h2>\n";
        
        if ($this->db) {
            try {
                $stmt = $this->db->query("SELECT COUNT(*) as count FROM users");
                $result = $stmt->fetch(PDO::FETCH_ASSOC);
                $this->addResult("Database Connection", "PASS", "Connected successfully. Users table has {$result['count']} records.");
            } catch (Exception $e) {
                $this->addResult("Database Connection", "FAIL", "Error: " . $e->getMessage());
            }
        } else {
            $this->addResult("Database Connection", "FAIL", "Database connection failed");
        }
    }
    
    /**
     * Test login endpoint
     */
    private function testLoginEndpoint() {
        echo "<h2>🔐 Login Endpoint Tests</h2>\n";
        
        // Test valid login
        $this->testLoginRequest('dizon', '123', 'Valid Login');
        
        // Test invalid login
        $this->testLoginRequest('invalid', 'wrong', 'Invalid Login');
        
        // Test missing credentials
        $this->testLoginRequest('', '', 'Missing Credentials');
    }
    
    private function testLoginRequest($username, $password, $testName) {
        $data = [
            'username' => $username,
            'password' => $password,
            'action' => 'login'
        ];
        
        $response = $this->makePostRequest('/mobile.php', $data);
        $result = json_decode($response, true);
        
        if ($testName === 'Valid Login') {
            if (isset($result['success']) && $result['success']) {
                $this->addResult($testName, "PASS", "Login successful for user: {$result['username']}");
            } else {
                $this->addResult($testName, "FAIL", "Expected success but got: " . $response);
            }
        } else {
            if (isset($result['error'])) {
                $this->addResult($testName, "PASS", "Correctly returned error: {$result['error']}");
            } else {
                $this->addResult($testName, "FAIL", "Expected error but got: " . $response);
            }
        }
    }
    
    /**
     * Test queue endpoint
     */
    private function testQueueEndpoint() {
        echo "<h2>📋 Queue Endpoint Tests</h2>\n";
        
        $response = $this->makeGetRequest('/mobile.php?action=queue');
        $result = json_decode($response, true);
        
        if (is_array($result)) {
            $this->addResult("Get Queue Data", "PASS", "Retrieved " . count($result) . " queue tickets");
        } else {
            $this->addResult("Get Queue Data", "FAIL", "Invalid response: " . $response);
        }
    }
    
    /**
     * Test create ticket endpoint
     */
    private function testCreateTicketEndpoint() {
        echo "<h2>🎫 Create Ticket Endpoint Tests</h2>\n";
        
        // Test valid ticket creation
        $ticketData = [
            'ticket_id' => 'TEST' . time(),
            'username' => 'testuser',
            'service_type' => 'Fast Charging',
            'initial_battery_level' => 25
        ];
        
        $response = $this->makeJsonPostRequest('/mobile.php?action=create-ticket', $ticketData);
        $result = json_decode($response, true);
        
        if (isset($result['success']) && $result['success']) {
            $this->addResult("Create Valid Ticket", "PASS", "Ticket created: {$result['ticket_id']}");
        } else {
            $this->addResult("Create Valid Ticket", "FAIL", "Failed to create ticket: " . $response);
        }
        
        // Test invalid ticket creation (missing data)
        $invalidData = ['ticket_id' => 'INVALID'];
        $response = $this->makeJsonPostRequest('/mobile.php?action=create-ticket', $invalidData);
        $result = json_decode($response, true);
        
        if (isset($result['error'])) {
            $this->addResult("Create Invalid Ticket", "PASS", "Correctly rejected invalid data: {$result['error']}");
        } else {
            $this->addResult("Create Invalid Ticket", "FAIL", "Should have rejected invalid data: " . $response);
        }
    }
    
    /**
     * Test view queue page
     */
    private function testViewQueuePage() {
        echo "<h2>📄 View Queue Page Test</h2>\n";
        
        $response = $this->makeGetRequest('/view-queue.php');
        
        if (strpos($response, '<html>') !== false && strpos($response, 'Cephra') !== false) {
            $this->addResult("View Queue Page", "PASS", "Page loads correctly with HTML content");
        } else {
            $this->addResult("View Queue Page", "FAIL", "Page did not load correctly: " . substr($response, 0, 200));
        }
    }
    
    /**
     * Test error handling
     */
    private function testErrorHandling() {
        echo "<h2>⚠️ Error Handling Tests</h2>\n";
        
        // Test unknown action
        $response = $this->makeGetRequest('/mobile.php?action=unknown');
        $result = json_decode($response, true);
        
        if (isset($result['error'])) {
            $this->addResult("Unknown Action", "PASS", "Correctly returned error: {$result['error']}");
        } else {
            $this->addResult("Unknown Action", "FAIL", "Should have returned error: " . $response);
        }
        
        // Test wrong HTTP method
        $response = $this->makeGetRequest('/mobile.php?action=login');
        $result = json_decode($response, true);
        
        if (isset($result['error']) && strpos($result['error'], 'Method') !== false) {
            $this->addResult("Wrong HTTP Method", "PASS", "Correctly rejected GET for login: {$result['error']}");
        } else {
            $this->addResult("Wrong HTTP Method", "FAIL", "Should have rejected GET for login: " . $response);
        }
    }
    
    /**
     * Make a POST request with form data
     */
    private function makePostRequest($endpoint, $data) {
        $url = $this->baseUrl . $endpoint;
        $postData = http_build_query($data);
        
        $context = stream_context_create([
            'http' => [
                'method' => 'POST',
                'header' => 'Content-Type: application/x-www-form-urlencoded',
                'content' => $postData,
                'timeout' => 10
            ]
        ]);
        
        return file_get_contents($url, false, $context);
    }
    
    /**
     * Make a POST request with JSON data
     */
    private function makeJsonPostRequest($endpoint, $data) {
        $url = $this->baseUrl . $endpoint;
        $jsonData = json_encode($data);
        
        $context = stream_context_create([
            'http' => [
                'method' => 'POST',
                'header' => 'Content-Type: application/json',
                'content' => $jsonData,
                'timeout' => 10
            ]
        ]);
        
        return file_get_contents($url, false, $context);
    }
    
    /**
     * Make a GET request
     */
    private function makeGetRequest($endpoint) {
        $url = $this->baseUrl . $endpoint;
        
        $context = stream_context_create([
            'http' => [
                'method' => 'GET',
                'timeout' => 10
            ]
        ]);
        
        return file_get_contents($url, false, $context);
    }
    
    /**
     * Add a test result
     */
    private function addResult($test, $status, $message) {
        $this->testResults[] = [
            'test' => $test,
            'status' => $status,
            'message' => $message
        ];
        
        $color = $status === 'PASS' ? 'green' : 'red';
        echo "<div style='margin: 10px 0; padding: 10px; border-left: 4px solid {$color}; background: #f8f9fa;'>\n";
        echo "<strong>{$test}:</strong> <span style='color: {$color};'>{$status}</span><br>\n";
        echo "<small>{$message}</small>\n";
        echo "</div>\n";
    }
    
    /**
     * Display final results
     */
    private function displayResults() {
        echo "<h2>📊 Test Results Summary</h2>\n";
        
        $passed = 0;
        $failed = 0;
        
        foreach ($this->testResults as $result) {
            if ($result['status'] === 'PASS') {
                $passed++;
            } else {
                $failed++;
            }
        }
        
        $total = $passed + $failed;
        $passRate = $total > 0 ? round(($passed / $total) * 100, 1) : 0;
        
        echo "<div style='background: #e9ecef; padding: 20px; border-radius: 8px; margin: 20px 0;'>\n";
        echo "<h3>Summary</h3>\n";
        echo "<p><strong>Total Tests:</strong> {$total}</p>\n";
        echo "<p><strong>Passed:</strong> <span style='color: green;'>{$passed}</span></p>\n";
        echo "<p><strong>Failed:</strong> <span style='color: red;'>{$failed}</span></p>\n";
        echo "<p><strong>Pass Rate:</strong> {$passRate}%</p>\n";
        echo "</div>\n";
        
        if ($failed === 0) {
            echo "<div style='background: #d4edda; color: #155724; padding: 15px; border-radius: 5px; margin: 20px 0;'>\n";
            echo "<strong>🎉 All tests passed! Your API is working correctly.</strong>\n";
            echo "</div>\n";
        } else {
            echo "<div style='background: #f8d7da; color: #721c24; padding: 15px; border-radius: 5px; margin: 20px 0;'>\n";
            echo "<strong>⚠️ Some tests failed. Please check the issues above.</strong>\n";
            echo "</div>\n";
        }
    }
}

// Run the tests
$tester = new ApiEndpointTester();
$tester->runAllTests();
?>
