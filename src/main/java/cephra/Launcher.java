package cephra;
import cephra.Database.CephraDB;
public final class Launcher {

	public static void main(String[] args) {
		
		try {
			CephraDB.initializeDatabase();
			CephraDB.validateDatabaseIntegrity();
			cephra.Phone.Utilities.QueueFlow.refreshCountersFromDatabase();
		} catch (Exception e) {
			System.err.println("Database connection failed: " + e.getMessage());
		}
		javax.swing.SwingUtilities.invokeLater(() -> {
			
			cephra.Frame.Admin admin = new cephra.Frame.Admin();
			java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
			java.awt.Dimension screenSize = toolkit.getScreenSize();
			admin.setLocation(screenSize.width - admin.getWidth(), 0);
			admin.setVisible(true);
			
			cephra.Frame.Phone phone = new cephra.Frame.Phone();
			phone.setVisible(true);
			phone.toFront();
			phone.requestFocus();
		});
	}
	private Launcher() {}
}