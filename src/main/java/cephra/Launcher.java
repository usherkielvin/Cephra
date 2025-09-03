package cephra;

import java.awt.GraphicsEnvironment;

public final class Launcher {

    public static void main(String[] args) {
        
        try {
            CephraDB.initializeDatabase();
            System.out.println("Database connection successful!");          
            CephraDB.validateDatabaseIntegrity();
            cephra.Phone.QueueFlow.refreshCountersFromDatabase();
        } catch (Exception e) {
            System.err.println("Database connection failed!");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        // Start embedded API server for Phone UI (runs on 0.0.0.0:8080)
        new Thread(() -> {
            try {
                System.out.println("Starting API server...");
                cephra.api.ApiServer.start(new String[]{});
                System.out.println("API server started successfully on port 8080");
            } catch (Throwable t) {
                System.err.println("Failed to start API server: " + t.getMessage());
                t.printStackTrace();
            }
        }, "api-server").start();

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