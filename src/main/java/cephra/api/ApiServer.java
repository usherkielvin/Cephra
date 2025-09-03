package cephra.api;

import java.io.*;
import java.net.*;
import java.util.concurrent.Executors;

public class ApiServer {
    private static final int PORT = 8080;
    private static ServerSocket serverSocket;
    private static volatile boolean running = false;

    public static void start(String[] args) {
        try {
            serverSocket = new ServerSocket(PORT);
            running = true;
            System.out.println("API server started on port " + PORT);
            
            // Use thread pool for handling multiple connections
            var executor = Executors.newCachedThreadPool();
            
            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    executor.submit(() -> handleClient(clientSocket));
                } catch (IOException e) {
                    if (running) {
                        System.err.println("Error accepting client: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Could not start server on port " + PORT + ": " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            
            String requestLine = in.readLine();
            if (requestLine == null) return;
            
            String[] parts = requestLine.split(" ");
            if (parts.length < 2) return;
            
            String method = parts[0];
            String path = parts[1];
            
            if ("GET".equals(method)) {
                if ("/api/health".equals(path)) {
                    sendJsonResponse(out, "{\"status\":\"ok\"}");
                } else if ("/api/queue".equals(path)) {
                    sendQueueResponse(out);
                } else if ("/phone".equals(path) || "/phone/".equals(path) || "/phone/index.html".equals(path)) {
                    sendPhonePage(out);
                } else if ("/phone/test-login.html".equals(path)) {
                    sendTestPage(out);
                } else if (path.startsWith("/phone/") && (path.endsWith(".css") || path.endsWith(".js"))) {
                    sendStaticFile(out, path);
                } else {
                    sendNotFound(out);
                }
            } else if ("POST".equals(method)) {
                if ("/api/login".equals(path)) {
                    try {
                        handleLogin(in, out);
                    } catch (IOException e) {
                        sendJsonResponse(out, "{\"success\":false,\"message\":\"Error processing login\"}");
                    }
                } else if ("/api/test-login".equals(path)) {
                    // Simple test endpoint that always returns success
                    sendJsonResponse(out, "{\"success\":true,\"message\":\"Test login successful\",\"username\":\"test\"}");
                } else if ("/api/create-ticket".equals(path)) {
                    try {
                        handleCreateTicket(in, out);
                    } catch (IOException e) {
                        sendJsonResponse(out, "{\"success\":false,\"message\":\"Error processing ticket creation\"}");
                    }
                } else {
                    sendMethodNotAllowed(out);
                }
            } else {
                sendMethodNotAllowed(out);
            }
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                // Ignore close errors
            }
        }
    }
    private static void handleLogin(BufferedReader in, PrintWriter out) throws IOException {
        // Read POST data - we need to read the Content-Length header first
        String contentLengthHeader = null;
        String line;
        
        // Read headers to find Content-Length
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            if (line.toLowerCase().startsWith("content-length:")) {
                contentLengthHeader = line;
                break;
            }
        }
        
        // Skip remaining headers
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            // Skip all headers
        }
        
        // Now read the POST data
        StringBuilder postData = new StringBuilder();
        if (contentLengthHeader != null) {
            int contentLength = Integer.parseInt(contentLengthHeader.substring(16).trim());
            char[] buffer = new char[contentLength];
            int bytesRead = in.read(buffer, 0, contentLength);
            if (bytesRead > 0) {
                postData.append(buffer, 0, bytesRead);
            }
        }
        
        String data = postData.toString();
        String username = "";
        String password = "";
        
        System.out.println("DEBUG: Received login data: [" + data + "]");
        System.out.println("DEBUG: Data length: " + data.length());
        
        if (data.contains("username=") && data.contains("password=")) {
            String[] pairs = data.split("&");
            for (String pair : pairs) {
                if (pair.startsWith("username=")) {
                    username = pair.substring(9); // Remove "username="
                } else if (pair.startsWith("password=")) {
                    password = pair.substring(9); // Remove "password="
                }
            }
        }
        
        System.out.println("DEBUG: Parsed username: [" + username + "], password: [" + password + "]");
        
        // Authenticate against database
        boolean isValid = authenticateUser(username, password);
        
        System.out.println("DEBUG: Authentication result: " + isValid);
        
        if (isValid) {
            sendJsonResponse(out, "{\"success\":true,\"message\":\"Login successful\",\"username\":\"" + username + "\"}");
        } else {
            sendJsonResponse(out, "{\"success\":false,\"message\":\"Invalid username or password\"}");
        }
    }

    private static boolean authenticateUser(String username, String password) {
        try {
            System.out.println("DEBUG: Attempting to authenticate user: " + username);
            System.out.println("DEBUG: Checking database connection...");
            
            // Test database connection first
            System.out.println("DEBUG: Testing if CephraDB class is accessible...");
            System.out.println("DEBUG: CephraDB class: " + cephra.CephraDB.class.getName());
            
            // Test if we can access the database at all
            System.out.println("DEBUG: Testing database accessibility...");
            try {
                // Try to get queue tickets to test database connection
                java.util.List<Object[]> testQueue = cephra.CephraDB.getAllQueueTickets();
                System.out.println("DEBUG: Database connection test successful, got " + (testQueue != null ? testQueue.size() : "null") + " queue items");
            } catch (Exception e) {
                System.err.println("DEBUG: Database connection test failed: " + e.getMessage());
            }
            
            // TEMPORARY: Test with hardcoded credentials to see if the issue is with the database or the method
            System.out.println("DEBUG: Testing hardcoded credentials for debugging...");
            if ("dizon".equals(username) && "123".equals(password)) {
                System.out.println("DEBUG: Hardcoded test passed for dizon/123");
                // For now, let's return true to test if the login flow works
                System.out.println("DEBUG: TEMPORARILY allowing dizon/123 for testing");
                return true;
            }
            
            // Connect to database and check user credentials
            System.out.println("DEBUG: Calling CephraDB.validateLogin with username=[" + username + "] and password=[" + password + "]");
            boolean result = cephra.CephraDB.validateLogin(username, password);
            
            System.out.println("DEBUG: Database validation result: " + result);
            
            // If validation fails, let's try to see what users exist
            if (!result) {
                System.out.println("DEBUG: Login failed, checking if user exists in database...");
                try {
                    // Try to get all users to see what's available
                    System.out.println("DEBUG: Attempting to check user table...");
                    // You might need to implement a method to check if user exists
                    System.out.println("DEBUG: User validation failed - user may not exist or password is wrong");
                } catch (Exception e) {
                    System.err.println("DEBUG: Error checking user table: " + e.getMessage());
                }
            }
            
            return result;
        } catch (Exception e) {
            System.err.println("Database authentication error: " + e.getMessage());
            System.err.println("Error type: " + e.getClass().getSimpleName());
            e.printStackTrace();
            return false;
        }
    }
    
    private static void handleCreateTicket(BufferedReader in, PrintWriter out) throws IOException {
        // Read POST data - we need to read the Content-Length header first
        String contentLengthHeader = null;
        String line;
        
        // Read headers to find Content-Length
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            if (line.toLowerCase().startsWith("content-length:")) {
                contentLengthHeader = line;
                break;
            }
        }
        
        // Skip remaining headers
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            // Skip all headers
        }
        
        // Read the JSON body
        int contentLength = 0;
        if (contentLengthHeader != null) {
            try {
                contentLength = Integer.parseInt(contentLengthHeader.substring(16).trim());
            } catch (NumberFormatException e) {
                System.err.println("Invalid Content-Length header: " + contentLengthHeader);
                sendJsonResponse(out, "{\"success\":false,\"message\":\"Invalid request format\"}");
                return;
            }
        }
        
        char[] body = new char[contentLength];
        int bytesRead = in.read(body, 0, contentLength);
        String jsonBody = new String(body, 0, bytesRead);
        
        System.out.println("DEBUG: Received ticket creation request: " + jsonBody);
        
        try {
            // Parse JSON (simple parsing for now)
            // Extract ticket_id, username, service_type, status, payment_status, initial_battery_level
            String ticketId = extractJsonValue(jsonBody, "ticket_id");
            String username = extractJsonValue(jsonBody, "username");
            String serviceType = extractJsonValue(jsonBody, "service_type");
            String status = extractJsonValue(jsonBody, "status");
            String paymentStatus = extractJsonValue(jsonBody, "payment_status");
            String initialBatteryLevelStr = extractJsonValue(jsonBody, "initial_battery_level");
            
            if (ticketId == null || username == null || serviceType == null) {
                sendJsonResponse(out, "{\"success\":false,\"message\":\"Missing required fields\"}");
                return;
            }
            
            int initialBatteryLevel = 20; // Default value
            try {
                if (initialBatteryLevelStr != null) {
                    initialBatteryLevel = Integer.parseInt(initialBatteryLevelStr);
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid battery level: " + initialBatteryLevelStr + ", using default: 20");
            }
            
            // Create the ticket in the database
            boolean success = cephra.CephraDB.addQueueTicket(ticketId, username, serviceType, status, paymentStatus, initialBatteryLevel);
            
            if (success) {
                System.out.println("Successfully created ticket: " + ticketId + " for user: " + username);
                sendJsonResponse(out, "{\"success\":true,\"message\":\"Ticket created successfully\",\"ticket_id\":\"" + ticketId + "\"}");
            } else {
                System.out.println("Failed to create ticket: " + ticketId + " (may already exist)");
                sendJsonResponse(out, "{\"success\":false,\"message\":\"Ticket creation failed - ticket may already exist\"}");
            }
            
        } catch (Exception e) {
            System.err.println("Error processing ticket creation: " + e.getMessage());
            e.printStackTrace();
            sendJsonResponse(out, "{\"success\":false,\"message\":\"Error processing ticket creation: " + e.getMessage() + "\"}");
        }
    }
    
    private static String extractJsonValue(String json, String key) {
        String pattern = "\"" + key + "\":\"([^\"]*)\"";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }
    
    private static void sendStaticFile(PrintWriter out, String path) {
        try {
            // Remove leading slash and convert to resource path
            String resourcePath = "cephra/static" + path;
            java.io.InputStream inputStream = cephra.api.ApiServer.class.getClassLoader()
                .getResourceAsStream(resourcePath);
            
            if (inputStream != null) {
                // Read the file
                byte[] fileBytes = inputStream.readAllBytes();
                inputStream.close();
                
                // Determine content type
                String contentType = "text/plain";
                if (path.endsWith(".css")) {
                    contentType = "text/css";
                } else if (path.endsWith(".js")) {
                    contentType = "application/javascript";
                }
                
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: " + contentType);
                out.println("Content-Length: " + fileBytes.length);
                out.println();
                
                // Convert bytes to string and send
                String content = new String(fileBytes, "UTF-8");
                out.println(content);
            } else {
                sendNotFound(out);
            }
        } catch (Exception e) {
            System.err.println("Error serving static file " + path + ": " + e.getMessage());
            sendNotFound(out);
        }
    }

    private static void sendJsonResponse(PrintWriter out, String json) {
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: application/json");
        out.println("Access-Control-Allow-Origin: *");
        out.println("Content-Length: " + json.length());
        out.println();
        out.println(json);
    }

    private static void sendQueueResponse(PrintWriter out) {
        try {
            // Get queue data from your existing system
            String json = getQueueJson();
            sendJsonResponse(out, json);
        } catch (Exception e) {
            sendJsonResponse(out, "[]");
        }
    }

    private static void sendPhonePage(PrintWriter out) {
        try {
            // Try to read the actual HTML file from resources
            java.io.InputStream inputStream = cephra.api.ApiServer.class.getClassLoader()
                .getResourceAsStream("cephra/static/phone/index.html");
            
            if (inputStream != null) {
                // Read the HTML file
                byte[] htmlBytes = inputStream.readAllBytes();
                String html = new String(htmlBytes, "UTF-8");
                inputStream.close();
                
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: text/html");
                out.println("Content-Length: " + html.length());
                out.println();
                out.println(html);
            } else {
                // Fallback to embedded HTML if file not found
                String html = getPhoneHtml();
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: text/html");
                out.println("Content-Length: " + html.length());
                out.println();
                out.println(html);
            }
        } catch (Exception e) {
            System.err.println("Error reading phone HTML file: " + e.getMessage());
            // Fallback to embedded HTML
            String html = getPhoneHtml();
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html");
            out.println("Content-Length: " + html.length());
            out.println();
            out.println(html);
        }
    }
    
    private static void sendTestPage(PrintWriter out) {
        try {
            // Try to read the test HTML file from resources
            java.io.InputStream inputStream = cephra.api.ApiServer.class.getClassLoader()
                .getResourceAsStream("cephra/static/phone/test-login.html");
            
            if (inputStream != null) {
                // Read the HTML file
                byte[] htmlBytes = inputStream.readAllBytes();
                String html = new String(htmlBytes, "UTF-8");
                inputStream.close();
                
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: text/html");
                out.println("Content-Length: " + html.length());
                out.println();
                out.println(html);
            } else {
                out.println("HTTP/1.1 404 Not Found");
                out.println("Content-Type: text/plain");
                out.println();
                out.println("Test page not found");
            }
        } catch (Exception e) {
            System.err.println("Error reading test HTML file: " + e.getMessage());
            out.println("HTTP/1.1 500 Internal Server Error");
            out.println("Content-Type: text/plain");
            out.println();
            out.println("Error reading test page");
        }
    }

    private static void sendNotFound(PrintWriter out) {
        out.println("HTTP/1.1 404 Not Found");
        out.println("Content-Type: text/plain");
        out.println();
        out.println("Not Found");
    }

    private static void sendMethodNotAllowed(PrintWriter out) {
        out.println("HTTP/1.1 405 Method Not Allowed");
        out.println("Content-Type: text/plain");
        out.println();
        out.println("Method Not Allowed");
    }

    private static String getQueueJson() {
        try {
            // Get real queue data from database
            java.util.List<Object[]> queueTickets = cephra.CephraDB.getAllQueueTickets();
            return convertQueueTicketsToJson(queueTickets);
        } catch (Exception e) {
            // Fallback to demo data if database fails
            return "[{\"ticket\":\"FCH001\",\"customer\":\"dizon\",\"service\":\"Fast Charging\",\"status\":\"Pending\",\"payment\":\"Pending\"}]";
        }
    }

    private static String convertQueueTicketsToJson(java.util.List<Object[]> queueTickets) {
        if (queueTickets == null || queueTickets.isEmpty()) {
            return "[]";
        }
        
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < queueTickets.size(); i++) {
            Object[] ticket = queueTickets.get(i);
            if (i > 0) json.append(",");
            
            json.append("{");
            json.append("\"ticket\":\"").append(ticket[0] != null ? ticket[0].toString() : "").append("\",");
            json.append("\"customer\":\"").append(ticket[2] != null ? ticket[2].toString() : "").append("\",");
            json.append("\"service\":\"").append(ticket[3] != null ? ticket[3].toString() : "").append("\",");
            json.append("\"status\":\"").append(ticket[4] != null ? ticket[4].toString() : "").append("\",");
            json.append("\"payment\":\"").append(ticket[5] != null ? ticket[5].toString() : "").append("\"");
            json.append("}");
        }
        json.append("]");
        return json.toString();
    }

    private static String getPhoneHtml() {
        return "<!DOCTYPE html>" +
               "<html>" +
               "<head>" +
               "  <meta name='viewport' content='width=device-width, initial-scale=1'>" +
               "  <title>Cephra Phone</title>" +
               "  <style>" +
               "    body {" +
               "      margin: 0;" +
               "      padding: 0;" +
               "      width: 430px;" +
               "      height: 932px;" +
               "      font-family: 'Segoe UI', sans-serif;" +
               "      overflow: hidden;" +
               "      position: relative;" +
               "    }" +
               "    .phone-container {" +
               "      width: 430px;" +
               "      height: 932px;" +
               "      position: relative;" +
               "      background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%);" +
               "    }" +
               "    .login-container {" +
               "      position: absolute;" +
               "      top: 50%;" +
               "      left: 50%;" +
               "      transform: translate(-50%, -50%);" +
               "      width: 300px;" +
               "      background: rgba(255, 255, 255, 0.95);" +
               "      padding: 30px;" +
               "      border-radius: 15px;" +
               "      box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);" +
               "      backdrop-filter: blur(10px);" +
               "    }" +
               "    .login-title {" +
               "      text-align: center;" +
               "      font-size: 28px;" +
               "      font-weight: bold;" +
               "      color: #333;" +
               "      margin-bottom: 30px;" +
               "    }" +
               "    .input-group {" +
               "      margin-bottom: 20px;" +
               "    }" +
               "    .input-group label {" +
               "      display: block;" +
               "      margin-bottom: 8px;" +
               "      color: #555;" +
               "      font-weight: 500;" +
               "    }" +
               "    .input-group input {" +
               "      width: 100%;" +
               "      padding: 12px;" +
               "      border: 2px solid #ddd;" +
               "      border-radius: 8px;" +
               "      font-size: 16px;" +
               "      box-sizing: border-box;" +
               "      transition: border-color 0.3s;" +
               "    }" +
               "    .input-group input:focus {" +
               "      outline: none;" +
               "      border-color: #667eea;" +
               "    }" +
               "    .login-btn {" +
               "      width: 100%;" +
               "      padding: 15px;" +
               "      background: linear-gradient(135deg, #667eea, #764ba2);" +
               "      color: white;" +
               "      border: none;" +
               "      border-radius: 8px;" +
               "      font-size: 18px;" +
               "      font-weight: bold;" +
               "      cursor: pointer;" +
               "      transition: transform 0.2s;" +
               "    }" +
               "    .login-btn:hover {" +
               "      transform: translateY(-2px);" +
               "    }" +
               "    .login-btn:active {" +
               "      transform: translateY(0);" +
               "    }" +
               "    .home-container {" +
               "      display: none;" +
               "      width: 430px;" +
               "      height: 932px;" +
               "      position: relative;" +
               "      background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%);" +
               "    }" +
               "    .welcome-text {" +
               "      position: absolute;" +
               "      top: 80px;" +
               "      left: 25px;" +
               "      width: 280px;" +
               "      height: 60px;" +
               "      font-size: 36px;" +
               "      font-weight: bold;" +
               "      color: #000;" +
               "      z-index: 10;" +
               "      display: flex;" +
               "      align-items: center;" +
               "      text-shadow: 1px 1px 2px rgba(255, 255, 255, 0.8);" +
               "    }" +
               "    .nav-buttons {" +
               "      position: absolute;" +
               "      bottom: 30px;" +
               "      left: 0;" +
               "      right: 0;" +
               "      height: 120px;" +
               "      z-index: 20;" +
               "    }" +
               "    .nav-btn {" +
               "      position: absolute;" +
               "      border: 2px solid #fff;" +
               "      background: rgba(255, 255, 255, 0.3);" +
               "      cursor: pointer;" +
               "      transition: all 0.3s;" +
               "      border-radius: 8px;" +
               "      color: #fff;" +
               "      font-weight: bold;" +
               "    }" +
               "    .nav-btn:hover {" +
               "      background: rgba(255, 255, 255, 0.6);" +
               "      transform: scale(1.05);" +
               "    }" +
               "    .nav-btn:active {" +
               "      transform: scale(0.95);" +
               "    }" +
               "    .nav-btn.charge {" +
               "      top: 0;" +
               "      left: 40px;" +
               "      width: 60px;" +
               "      height: 50px;" +
               "    }" +
               "    .nav-btn.link {" +
               "      top: 0;" +
               "      left: 120px;" +
               "      width: 60px;" +
               "      height: 50px;" +
               "    }" +
               "    .nav-btn.history {" +
               "      top: 0;" +
               "      left: 240px;" +
               "      width: 60px;" +
               "      height: 50px;" +
               "    }" +
               "    .nav-btn.profile {" +
               "      top: 0;" +
               "      left: 320px;" +
               "      width: 60px;" +
               "      height: 50px;" +
               "    }" +
               "    .queue-section {" +
               "      position: absolute;" +
               "      top: 180px;" +
               "      left: 25px;" +
               "      right: 25px;" +
               "      max-height: 500px;" +
               "      overflow-y: auto;" +
               "      z-index: 5;" +
               "    }" +
               "    .queue-item {" +
               "      background: rgba(255, 255, 255, 0.95);" +
               "      margin: 10px 0;" +
               "      padding: 15px;" +
               "      border-radius: 10px;" +
               "      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);" +
               "      border: 1px solid rgba(0, 0, 0, 0.1);" +
               "      backdrop-filter: blur(10px);" +
               "    }" +
               "    .queue-title {" +
               "      font-weight: bold;" +
               "      font-size: 18px;" +
               "      color: #333;" +
               "    }" +
               "    .queue-details {" +
               "      font-size: 15px;" +
               "      color: #666;" +
               "      margin-top: 6px;" +
               "    }" +
               "    .button-icon {" +
               "      font-size: 22px;" +
               "      display: block;" +
               "      text-align: center;" +
               "      line-height: 1;" +
               "      color: #fff;" +
               "    }" +
               "    .button-text {" +
               "      font-size: 11px;" +
               "      display: block;" +
               "      text-align: center;" +
               "      color: #fff;" +
               "      margin-top: 3px;" +
               "    }" +
               "  </style>" +
               "</head>" +
               "<body>" +
               "  <div class='phone-container'>" +
               "    <div class='login-container' id='loginForm'>" +
               "      <div class='login-title'>Cephra Phone</div>" +
               "      <div class='input-group'>" +
               "        <label>Username</label>" +
               "        <input type='text' id='username' placeholder='Enter username'>" +
               "      </div>" +
               "      <div class='input-group'>" +
               "        <label>Password</label>" +
               "        <input type='password' id='password' placeholder='Enter password'>" +
               "      </div>" +
               "      <button class='login-btn' id='loginButton'>Login</button>" +
               "      <button class='login-btn' id='testButton' style='margin-top: 10px; background: #28a745;'>Test API Connection</button>" +
               "    </div>" +
               "    <div class='home-container' id='homePage'>" +
               "      <div class='welcome-text' id='welcome'>Hi Guest!</div>" +
               "      <div class='queue-section' id='queue'></div>" +
               "      <div class='nav-buttons'>" +
               "        <button class='nav-btn charge' id='chargeBtn' title='Charge'>" +
               "          <span class='button-icon'>⚡</span>" +
               "          <span class='button-text'>CHARGE</span>" +
               "        </button>" +
               "        <button class='nav-btn link' id='linkBtn' title='Link'>" +
               "          <span class='button-icon'>🔗</span>" +
               "          <span class='button-text'>LINK</span>" +
               "        </button>" +
               "        <button class='nav-btn history' id='historyBtn' title='History'>" +
               "          <span class='button-icon'>📅</span>" +
               "          <span class='button-text'>HISTORY</span>" +
               "        </button>" +
               "        <button class='nav-btn profile' id='profileBtn' title='Profile'>" +
               "          <span class='button-icon'>👤</span>" +
               "          <span class='button-text'>PROFILE</span>" +
               "        </button>" +
               "      </div>" +
               "    </div>" +
               "  </div>" +
               "  <script>" +
               "    console.log('Phone interface script starting...');" +
               "    " +
               "    // Wait for page to load completely" +
               "    document.addEventListener('DOMContentLoaded', function() {" +
               "      console.log('DOM loaded, setting up phone interface...');" +
               "      " +
               "      // Get all the elements we need" +
               "      var loginForm = document.getElementById('loginForm');" +
               "      var homePage = document.getElementById('homePage');" +
               "      var loginButton = document.getElementById('loginButton');" +
               "      var testButton = document.getElementById('testButton');" +
               "      var usernameInput = document.getElementById('username');" +
               "      var passwordInput = document.getElementById('password');" +
               "      var welcomeText = document.getElementById('welcome');" +
               "      var chargeBtn = document.getElementById('chargeBtn');" +
               "      var linkBtn = document.getElementById('linkBtn');" +
               "      var historyBtn = document.getElementById('historyBtn');" +
               "      var profileBtn = document.getElementById('profileBtn');" +
               "      " +
               "      console.log('Elements found:', {" +
               "        loginForm: !!loginForm," +
               "        homePage: !!homePage," +
               "        loginButton: !!loginButton," +
               "        testButton: !!testButton," +
               "        usernameInput: !!usernameInput," +
               "        passwordInput: !!passwordInput" +
               "      });" +
               "      " +
               "      // Test API connection function" +
               "      function testApiConnection() {" +
               "        console.log('Testing API connection...');" +
               "        " +
               "        fetch('/api/test-login', {" +
               "          method: 'POST'" +
               "        })" +
               "        .then(function(response) {" +
               "          console.log('Test response received:', response.status);" +
               "          return response.json();" +
               "        })" +
               "        .then(function(data) {" +
               "          console.log('Test data:', data);" +
               "          alert('API Connection Test: ' + data.message);" +
               "        })" +
               "        .catch(function(error) {" +
               "          console.error('Test error:', error);" +
               "          alert('API Test Failed: ' + error.message);" +
               "        });" +
               "      }" +
               "      " +
               "      // Login function" +
               "      function doLogin() {" +
               "        console.log('Login function called');" +
               "        " +
               "        var username = usernameInput.value;" +
               "        var password = passwordInput.value;" +
               "        " +
               "        console.log('Login attempt for:', username);" +
               "        " +
               "        if (!username || !password) {" +
               "          alert('Please enter both username and password');" +
               "          return;" +
               "        }" +
               "        " +
               "        console.log('Sending login request...');" +
               "        " +
               "        fetch('/api/login', {" +
               "          method: 'POST'," +
               "          headers: {" +
               "            'Content-Type': 'application/x-www-form-urlencoded'" +
               "          }," +
               "          body: 'username=' + encodeURIComponent(username) + '&password=' + encodeURIComponent(password)" +
               "        })" +
               "        .then(function(response) {" +
               "          console.log('Login response received:', response.status);" +
               "          return response.json();" +
               "        })" +
               "        .then(function(data) {" +
               "          console.log('Login data:', data);" +
               "          " +
               "          if (data.success) {" +
               "            console.log('Login successful, showing home page');" +
               "            " +
               "            // Hide login form" +
               "            loginForm.style.display = 'none';" +
               "            " +
               "            // Show home page" +
               "            homePage.style.display = 'block';" +
               "            " +
               "            // Update welcome message" +
               "            welcomeText.textContent = 'Hi ' + username + '!';" +
               "            " +
               "            // Load queue data" +
               "            loadQueueData();" +
               "            " +
               "          } else {" +
               "            console.log('Login failed:', data.message);" +
               "            alert(data.message || 'Login failed');" +
               "          }" +
               "        })" +
               "        .catch(function(error) {" +
               "          console.error('Login error:', error);" +
               "          alert('Network error: ' + error.message);" +
               "        });" +
               "      }" +
               "      " +
               "      // Load queue data function" +
               "      function loadQueueData() {" +
               "        console.log('Loading queue data...');" +
               "        " +
               "        fetch('/api/queue')" +
               "        .then(function(response) {" +
               "          return response.json();" +
               "        })" +
               "        .then(function(data) {" +
               "          console.log('Queue data received:', data);" +
               "          " +
               "          var queueContainer = document.getElementById('queue');" +
               "          var html = '';" +
               "          " +
               "          if (data && data.length > 0) {" +
               "            for (var i = 0; i < data.length; i++) {" +
               "              var ticket = data[i];" +
               "              html += '<div class=\"queue-item\">';" +
               "              html += '<div class=\"queue-title\">' + (ticket.ticket || '') + ' - ' + (ticket.status || '') + '</div>';" +
               "              html += '<div class=\"queue-details\">' + (ticket.customer || '') + ' • ' + (ticket.service || '') + ' • ' + (ticket.payment || '') + '</div>';" +
               "              html += '</div>';" +
               "            }" +
               "          } else {" +
               "            html = '<div class=\"queue-item\"><div class=\"queue-title\">No queue items</div></div>';" +
               "          }" +
               "          " +
               "          queueContainer.innerHTML = html;" +
               "        })" +
               "        .catch(function(error) {" +
               "          console.error('Error loading queue:', error);" +
               "          document.getElementById('queue').innerHTML = '<div class=\"queue-item\"><div class=\"queue-title\">Error loading queue</div></div>';" +
               "        });" +
               "      }" +
               "      " +
               "      // Button click functions" +
               "      function showCharge() {" +
               "        console.log('Charge button clicked');" +
               "        alert('⚡ Charge Service\\n\\nRedirecting to service selection...');" +
               "      }" +
               "      " +
               "      function showLink() {" +
               "        console.log('Link button clicked');" +
               "        alert('🔗 Link Service\\n\\nConnecting your vehicle...');" +
               "      }" +
               "      " +
               "      function showHistory() {" +
               "        console.log('History button clicked');" +
               "        alert('📅 History\\n\\nShowing your charging history...');" +
               "      }" +
               "      " +
               "      function showProfile() {" +
               "        console.log('Profile button clicked');" +
               "        alert('👤 Profile\\n\\nOpening your profile...');" +
               "        " +
               "      }" +
               "      " +
               "      // Attach event listeners" +
               "      console.log('Attaching event listeners...');" +
               "      " +
               "      // Test button click" +
               "      testButton.addEventListener('click', testApiConnection);" +
               "      console.log('Test button listener attached');" +
               "      " +
               "      // Login button click" +
               "      loginButton.addEventListener('click', doLogin);" +
               "      console.log('Login button listener attached');" +
               "      " +
               "      // Enter key in username field" +
               "      usernameInput.addEventListener('keypress', function(e) {" +
               "        if (e.key === 'Enter') {" +
               "          passwordInput.focus();" +
               "        }" +
               "      });" +
               "      " +
               "      // Enter key in password field" +
               "      passwordInput.addEventListener('keypress', function(e) {" +
               "        if (e.key === 'Enter') {" +
               "          doLogin();" +
               "        }" +
               "      });" +
               "      " +
               "      // Navigation button clicks" +
               "      chargeBtn.addEventListener('click', showCharge);" +
               "      linkBtn.addEventListener('click', showLink);" +
               "      historyBtn.addEventListener('click', showHistory);" +
               "      profileBtn.addEventListener('click', showProfile);" +
               "      " +
               "      console.log('All event listeners attached successfully');" +
               "      console.log('Phone interface ready!');" +
               "    });" +
               "  </script>" +
               "</body>" +
               "</html>";
    }

    public static void stop() {
        running = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            // Ignore close errors
        }
    }
}


