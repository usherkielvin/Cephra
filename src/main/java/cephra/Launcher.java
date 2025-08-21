package cephra;

public final class Launcher {

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(() -> {
			cephra.Frame.Admin admin = new cephra.Frame.Admin();
			cephra.Frame.Phone phone = new cephra.Frame.Phone();
			cephra.Frame.Monitor monitor = new cephra.Frame.Monitor();

			java.awt.Rectangle screenBounds = java.awt.GraphicsEnvironment
				.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice()
				.getDefaultConfiguration()
				.getBounds();

					
		admin.setLocation(
			screenBounds.x + screenBounds.width - admin.getWidth(),
			screenBounds.y
		);
		admin.setVisible(true);					
		monitor.setVisible(true);					
		phone.setLocationRelativeTo(null);
		phone.setVisible(true);
		phone.toFront();
		phone.requestFocus();
		});
	}

	private Launcher() {}
}