package cephra;

import java.awt.GraphicsEnvironment;

public final class Launcher {

    public static void main(String[] args) {
        // Test database connection first
        System.out.println("Testing Cephra Database Connection...");
        System.out.println("=====================================");
        
        try {
            // Initialize database connection
            CephraDB.initializeDatabase();
            System.out.println("✓ Database connection successful!");
            System.out.println("✓ Cephra application starting...");
            System.out.println("=====================================");
        } catch (Exception e) {
            System.err.println("✗ Database connection failed!");
            System.err.println("Error: " + e.getMessage());
            System.err.println("\nTroubleshooting steps:");
            System.err.println("1. Make sure XAMPP is running");
            System.err.println("2. Start MySQL service in XAMPP Control Panel");
            System.err.println("3. Check if port 3306 is available");
            System.err.println("4. Run init-database.bat to create database");
            System.err.println("5. Or run setup-h2-database.bat for H2 database");
            e.printStackTrace();
            System.exit(1); // Exit if database connection fails
        }

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

            // Monitor will be created when Queue panel is opened

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

    private Launcher() {}///
}