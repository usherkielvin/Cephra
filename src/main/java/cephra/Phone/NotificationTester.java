package cephra.Phone;

/**
 * Utility class to test notification history functionality
 * This adds sample notifications for testing purposes
 */
public class NotificationTester {
    
    public static void addSampleNotifications() {
        try {
            String currentUser = "TestUser"; // Always use TestUser for testing
            try {
                String dbUser = cephra.CephraDB.getCurrentUsername();
                if (dbUser != null && !dbUser.trim().isEmpty()) {
                    currentUser = dbUser;
                }
            } catch (Exception e) {
                System.out.println("Could not get current username from DB, using TestUser");
            }
            
            System.out.println("Adding sample notifications for user: " + currentUser);
            System.out.println("Current notifications count before adding: " + NotificationHistoryManager.getNotificationCountForUser(currentUser));
            
            // Add some sample notifications
            System.out.println("Adding Full Charge notification...");
            NotificationHistoryManager.addFullChargeNotification(currentUser, "T001");
            
            Thread.sleep(100); // Small delay to ensure different timestamps
            
            System.out.println("Adding My Turn notification...");
            NotificationHistoryManager.addMyTurnNotification(currentUser, "T002", "Bay 3");
            
            Thread.sleep(100);
            
            System.out.println("Adding Ticket Waiting notification...");
            NotificationHistoryManager.addTicketWaitingNotification(currentUser, "T003");
            
            Thread.sleep(100);
            
            NotificationHistoryManager.addTicketPendingNotification(currentUser, "T004");
            
            Thread.sleep(100);
            
            NotificationHistoryManager.addEmailNotification(currentUser, "12345");
            
            Thread.sleep(100);
            
            NotificationHistoryManager.addAdminUpdateNotification(
                currentUser, 
                "System maintenance scheduled", 
                "The charging system will undergo maintenance tonight from 10 PM to 2 AM. Please plan accordingly."
            );
            
            System.out.println("Sample notifications added successfully!");
            System.out.println("Total notifications count after adding: " + NotificationHistoryManager.getNotificationCountForUser(currentUser));
            
        } catch (Exception e) {
            System.err.println("Error adding sample notifications: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void clearAllNotifications() {
        NotificationHistoryManager.clearAllNotifications();
        System.out.println("All notifications cleared.");
    }
}
