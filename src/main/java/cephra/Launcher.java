package cephra;

import java.awt.GraphicsEnvironment;

public final class Launcher {



    public static void main(String[] args) {
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

            cephra.Frame.Monitor monitor = new cephra.Frame.Monitor();
            monitor.setVisible(true);

            cephra.Frame.Phone phone = new cephra.Frame.Phone();
            
            // Get taskbar height (typically 40-50 pixels on Windows)
            int taskbarHeight = 30;
            
            phone.setLocation(
                screenBounds.x + (screenBounds.width - phone.getWidth()) / 2,  // Center horizontally
                screenBounds.y + screenBounds.height - phone.getHeight() - taskbarHeight  // Above taskbar
            );
            phone.setVisible(true);
            phone.toFront();
            phone.requestFocus();
        });
    }

    private Launcher() {}
}