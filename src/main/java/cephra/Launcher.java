package cephra;
import java.awt.GraphicsEnvironment;

import cephra.db.CephraDB;

public final class Launcher {

	public static void main(String[] args) {
		
		try {
			System.out.println("Attempting to initialize database...");
			CephraDB.initializeDatabase();
			System.out.println("Database connection successful!");		  
			CephraDB.validateDatabaseIntegrity();
			cephra.Phone.Utilities.QueueFlow.refreshCountersFromDatabase();
		} catch (Exception e) {
			System.err.println("Database connection failed!");
			System.err.println("Error: " + e.getMessage());
			System.err.println("Continuing without database for API testing...");
		}
		javax.swing.SwingUtilities.invokeLater(() -> {
			java.awt.Rectangle screenBounds = GraphicsEnvironment
				.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice()
				.getDefaultConfiguration()
				.getBounds();

			// Launch Admin frame (it embeds the Login panel by default)
			cephra.Frame.Admin admin = new cephra.Frame.Admin();
			admin.setLocation(
				screenBounds.x + screenBounds.width - admin.getWidth(),
				screenBounds.y
			);
			admin.setVisible(true);

			// Keep Phone frame launching untouched
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