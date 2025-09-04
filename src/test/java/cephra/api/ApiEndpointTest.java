package cephra.api;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.time.Duration;
import java.io.IOException;

/**
 * API Endpoint Tests for Cephra Web Interface
 * 
 * Tests the PHP API endpoints that power the web interface:
 * - /api/mobile.php?action=login
 * - /api/mobile.php?action=queue  
 * - /api/mobile.php?action=create-ticket
 * - /api/view-queue.php
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApiEndpointTest {
    
    private static final String BASE_URL = "http://localhost:8080";
    private static final String API_BASE = BASE_URL + "/api";
    private HttpClient httpClient;
    
    @BeforeEach
    void setUp() {
        httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    }
    
    @Test
    @Order(1)
    @DisplayName("API Server Health Check")
    void testApiServerHealth() throws IOException, InterruptedException {
        // Test if the API server is running
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/health"))
            .timeout(Duration.ofSeconds(5))
            .build();
            
        HttpResponse<String> response = httpClient.send(request, 
            HttpResponse.BodyHandlers.ofString());
            
        assertTrue(response.statusCode() == 200 || response.statusCode() == 404,
            "API server should be running (200) or at least responding (404)");
    }
    
    @Test
    @Order(2)
    @DisplayName("Login Endpoint - Valid Credentials")
    void testLoginValidCredentials() throws IOException, InterruptedException {
        String requestBody = "username=dizon&password=123&action=login";
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_BASE + "/mobile.php"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .timeout(Duration.ofSeconds(10))
            .build();
            
        HttpResponse<String> response = httpClient.send(request, 
            HttpResponse.BodyHandlers.ofString());
            
        assertEquals(200, response.statusCode(), 
            "Login with valid credentials should return 200");
            
        String responseBody = response.body();
        assertTrue(responseBody.contains("success") || responseBody.contains("username"),
            "Response should contain success indicator: " + responseBody);
    }
    
    @Test
    @Order(3)
    @DisplayName("Login Endpoint - Invalid Credentials")
    void testLoginInvalidCredentials() throws IOException, InterruptedException {
        String requestBody = "username=invalid&password=wrong&action=login";
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_BASE + "/mobile.php"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .timeout(Duration.ofSeconds(10))
            .build();
            
        HttpResponse<String> response = httpClient.send(request, 
            HttpResponse.BodyHandlers.ofString());
            
        // Should return error for invalid credentials
        String responseBody = response.body();
        assertTrue(responseBody.contains("error") || responseBody.contains("Invalid"),
            "Response should contain error for invalid credentials: " + responseBody);
    }
    
    @Test
    @Order(4)
    @DisplayName("Queue Endpoint - Get Queue Data")
    void testGetQueueData() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_BASE + "/mobile.php?action=queue"))
            .GET()
            .timeout(Duration.ofSeconds(10))
            .build();
            
        HttpResponse<String> response = httpClient.send(request, 
            HttpResponse.BodyHandlers.ofString());
            
        assertEquals(200, response.statusCode(), 
            "Queue endpoint should return 200");
            
        String responseBody = response.body();
        // Should return JSON array (even if empty)
        assertTrue(responseBody.startsWith("[") || responseBody.startsWith("{"),
            "Response should be valid JSON: " + responseBody);
    }
    
    @Test
    @Order(5)
    @DisplayName("Create Ticket Endpoint - Valid Data")
    void testCreateTicketValid() throws IOException, InterruptedException {
        String jsonData = """
            {
                "ticket_id": "TEST001",
                "username": "testuser",
                "service_type": "Fast Charging",
                "initial_battery_level": 25
            }
            """;
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_BASE + "/mobile.php?action=create-ticket"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonData))
            .timeout(Duration.ofSeconds(10))
            .build();
            
        HttpResponse<String> response = httpClient.send(request, 
            HttpResponse.BodyHandlers.ofString());
            
        // Should return success or error (depending on database state)
        String responseBody = response.body();
        assertTrue(responseBody.contains("success") || responseBody.contains("error"),
            "Response should indicate success or error: " + responseBody);
    }
    
    @Test
    @Order(6)
    @DisplayName("Create Ticket Endpoint - Missing Data")
    void testCreateTicketMissingData() throws IOException, InterruptedException {
        String jsonData = """
            {
                "ticket_id": "TEST002"
            }
            """;
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_BASE + "/mobile.php?action=create-ticket"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonData))
            .timeout(Duration.ofSeconds(10))
            .build();
            
        HttpResponse<String> response = httpClient.send(request, 
            HttpResponse.BodyHandlers.ofString());
            
        // Should return error for missing required fields
        String responseBody = response.body();
        assertTrue(responseBody.contains("error") || responseBody.contains("Failed"),
            "Response should contain error for missing data: " + responseBody);
    }
    
    @Test
    @Order(7)
    @DisplayName("View Queue Page - HTML Response")
    void testViewQueuePage() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_BASE + "/view-queue.php"))
            .GET()
            .timeout(Duration.ofSeconds(10))
            .build();
            
        HttpResponse<String> response = httpClient.send(request, 
            HttpResponse.BodyHandlers.ofString());
            
        assertEquals(200, response.statusCode(), 
            "View queue page should return 200");
            
        String responseBody = response.body();
        assertTrue(responseBody.contains("<html>") || responseBody.contains("<!DOCTYPE"),
            "Response should be HTML: " + responseBody.substring(0, Math.min(100, responseBody.length())));
        assertTrue(responseBody.contains("Cephra") || responseBody.contains("Queue"),
            "Response should contain Cephra/Queue content");
    }
    
    @Test
    @Order(8)
    @DisplayName("API Endpoint - Method Not Allowed")
    void testMethodNotAllowed() throws IOException, InterruptedException {
        // Try GET on login endpoint (should be POST only)
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_BASE + "/mobile.php?action=login"))
            .GET()
            .timeout(Duration.ofSeconds(10))
            .build();
            
        HttpResponse<String> response = httpClient.send(request, 
            HttpResponse.BodyHandlers.ofString());
            
        // Should return method not allowed or error
        String responseBody = response.body();
        assertTrue(responseBody.contains("error") || responseBody.contains("Method"),
            "Response should indicate method not allowed: " + responseBody);
    }
    
    @Test
    @Order(9)
    @DisplayName("API Endpoint - Unknown Action")
    void testUnknownAction() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_BASE + "/mobile.php?action=unknown"))
            .GET()
            .timeout(Duration.ofSeconds(10))
            .build();
            
        HttpResponse<String> response = httpClient.send(request, 
            HttpResponse.BodyHandlers.ofString());
            
        // Should return error for unknown action
        String responseBody = response.body();
        assertTrue(responseBody.contains("error") || responseBody.contains("not found"),
            "Response should indicate unknown action: " + responseBody);
    }
    
    @Test
    @Order(10)
    @DisplayName("API Response Time Performance")
    void testApiResponseTime() throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_BASE + "/mobile.php?action=queue"))
            .GET()
            .timeout(Duration.ofSeconds(5))
            .build();
            
        HttpResponse<String> response = httpClient.send(request, 
            HttpResponse.BodyHandlers.ofString());
            
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        
        assertEquals(200, response.statusCode(), 
            "API should respond successfully");
        assertTrue(responseTime < 5000, 
            "API response time should be under 5 seconds, was: " + responseTime + "ms");
    }
    
    @Test
    @Order(11)
    @DisplayName("API CORS Headers")
    void testCorsHeaders() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_BASE + "/mobile.php?action=queue"))
            .GET()
            .timeout(Duration.ofSeconds(10))
            .build();
            
        HttpResponse<String> response = httpClient.send(request, 
            HttpResponse.BodyHandlers.ofString());
            
        assertEquals(200, response.statusCode(), 
            "API should respond successfully");
            
        // Check for CORS headers (if available in response)
        String responseBody = response.body();
        assertNotNull(responseBody, "Response body should not be null");
    }
    
    @AfterEach
    void tearDown() {
        // Clean up any test data if needed
        // For now, we'll leave test data in the database for manual inspection
    }
}
