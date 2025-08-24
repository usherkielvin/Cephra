package cephra;

import java.awt.GraphicsEnvironment;

public final class Launcher {

    // Configuration flags to control which frames are visible
    private static final boolean SHOW_ADMIN = true;    // Set to false to hide Admin frame
    private static final boolean SHOW_MONITOR = true;  // Set to false to hide Monitor frame
    private static final boolean SHOW_PHONE = true;    // Set to false to hide Phone frame

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            java.awt.Rectangle screenBounds = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .getBounds();

            // Create and show Admin frame if enabled
            if (SHOW_ADMIN) {
                cephra.Frame.Admin admin = new cephra.Frame.Admin();
                admin.setLocation(
                    screenBounds.x + screenBounds.width - admin.getWidth(),
                    screenBounds.y
                );
                admin.setVisible(true);
            }

            // Create and show Monitor frame if enabled
            if (SHOW_MONITOR) {
                cephra.Frame.Monitor monitor = new cephra.Frame.Monitor();
                monitor.setVisible(true);
            }

            // Create and show Phone frame if enabled
            if (SHOW_PHONE) {
                cephra.Frame.Phone phone = new cephra.Frame.Phone();
                phone.setLocationRelativeTo(null);
                phone.setVisible(true);
                phone.toFront();
                phone.requestFocus();
            }
        });
    }

    private Launcher() {}
}