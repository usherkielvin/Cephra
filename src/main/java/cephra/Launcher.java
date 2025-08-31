package cephra;

import java.awt.GraphicsEnvironment;

public final class Launcher {

    public static void main(String[] args) {
        
        try {
            CephraDB.initializeDatabase();
            System.out.println("Database connection successful!");
            
            CephraDB.validateDatabaseIntegrity();
            
            // Initialize ticket counters from database and history
            cephra.Phone.QueueFlow.refreshCountersFromDatabase();
            System.out.println("Ticket counters initialized from database and history!");
            
            System.out.println("Cephra application starting...");
        } catch (Exception e) {
            System.err.println("✗ Database connection failed!");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
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