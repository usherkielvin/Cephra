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
            phone.setLocationRelativeTo(null);
            phone.setVisible(true);
            phone.toFront();
            phone.requestFocus();
        });
    }

    private Launcher() {}
}