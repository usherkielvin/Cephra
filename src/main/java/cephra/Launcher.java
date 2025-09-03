package cephra;

import java.awt.GraphicsEnvironment;

public final class Launcher {

    public static void main(String[] args) {
        
        try {
            System.out.println("Attempting to initialize database...");
            CephraDB.initializeDatabase();
            System.out.println("Database connection successful!");          
            CephraDB.validateDatabaseIntegrity();
            cephra.Phone.QueueFlow.refreshCountersFromDatabase();
        } catch (Exception e) {
            System.err.println("Database connection failed!");
            System.err.println("Error: " + e.getMessage());
            System.err.println("Continuing without database for API testing...");
            // Don't exit - let the API server start for testing
        }

        System.out.println("Basic Java functionality test passed!");
        
        // Start embedded API server for Phone UI (runs on 0.0.0.0:8080)
        new Thread(() -> {
            try {
                System.out.println("Starting API server...");
                System.out.println("Checking if port 8080 is available...");
                
                // Test if port is available
                try (java.net.ServerSocket testSocket = new java.net.ServerSocket(8080)) {
                    testSocket.close();
                    System.out.println("Port 8080 is available");
                } catch (Exception e) {
                    System.err.println("Port 8080 is already in use!");
                    System.err.println("Please close any other applications using port 8080");
                    return;
                }
                
                System.out.println("Testing if ApiServer class can be loaded...");
                Class.forName("cephra.api.ApiServer");
                System.out.println("ApiServer class loaded successfully!");
                
                cephra.api.ApiServer.start(new String[]{});
                System.out.println("API server started successfully on port 8080");
            } catch (Throwable t) {
                System.err.println("Failed to start API server: " + t.getMessage());
                System.err.println("Error type: " + t.getClass().getSimpleName());
                t.printStackTrace();
            }
        }, "api-server").start();
        
        // Wait a moment for the server to start, then test it
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Wait 2 seconds
                System.out.println("Testing API server connection...");
                
                java.net.URL url = new java.net.URL("http://localhost:8080/api/health");
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    System.out.println("✅ API server is responding! Response code: " + responseCode);
                } else {
                    System.err.println("❌ API server responded with code: " + responseCode);
                }
                
            } catch (Exception e) {
                System.err.println("❌ API server test failed: " + e.getMessage());
            }
        }, "api-test").start();

        javax.swing.SwingUtilities.invokeLater(() -> {
            java.awt.Rectangle screenBounds = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .getBounds();

            cephra.Frame.Admin admin = new cephra.Frame.Admin();
            admin.setLocation(
                screenBounds.x + screenBounds.width - admin.getWidth(),
                screenBounds.y
            );
            admin.setVisible(true);

            cephra.Frame.Phone phone = new cephra.Frame.Phone();
            int taskbarHeight = 30;
            phone.setLocation(
                screenBounds.x + (screenBounds.width - phone.getWidth()) / 2,
                screenBounds.y + screenBounds.height - phone.getHeight() - taskbarHeight
            );
            phone.setVisible(true);
            phone.toFront();
            phone.requestFocus();
        });
    }

    private Launcher() {}
}