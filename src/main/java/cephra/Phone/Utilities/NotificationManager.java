package cephra.Phone.Utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.SwingUtilities;

/**
 * Manages notification history for the phone app
 * Records notifications from various sources (admin updates, status changes, etc.)
 */
public class NotificationManager {
    
    public enum NotificationType {
        FULL_CHARGE("Full Charge", "Your vehicle charging session has been completed successfully. Your battery is now at 100% capacity and ready for use. The charging process took approximately 2 hours and 30 minutes, during which your vehicle received the optimal amount of energy. Please disconnect your charging cable and move your vehicle from the charging bay to allow other customers to use the facility. Thank you for choosing our charging services."),
        TICKET_WAITING("Ticket Waiting", "Your charging request has been received and added to our queue system. Your ticket is currently in waiting status while we prepare an available charging bay for your vehicle. The estimated wait time is approximately 15-20 minutes based on current queue length. You will receive another notification when a charging bay becomes available and it's your turn to begin the charging process. Please remain nearby and keep your mobile device accessible for updates."),
        TICKET_PENDING("Ticket Pending", "Your charging ticket is currently being processed by our system administrators. This typically occurs when there are technical considerations or special requirements for your charging session. Our team is reviewing your request to ensure optimal charging conditions for your specific vehicle type. The processing usually takes 5-10 minutes, and you will be notified immediately once your ticket is approved and ready to proceed to the charging queue."),
        MY_TURN("Your Turn", "It's now your turn to charge your vehicle! A charging bay has become available and has been reserved specifically for you. Please proceed to the designated charging bay within the next 10 minutes to begin your charging session. Make sure to have your charging cable ready and follow the instructions displayed on the charging station. If you do not arrive within the allocated time, your reservation may be released to the next customer in queue."),
        ADMIN_UPDATE("Admin Update", "Important system update from the administration team. Please review the following information as it may affect your current or future charging sessions. Our team continuously monitors the charging infrastructure to ensure optimal performance and safety standards. Any changes or improvements to our services will be communicated through these notifications to keep you informed about the latest developments and enhancements to your charging experience."),
        EMAIL_NOTIFICATION("Email", "You have received a new email notification containing important information regarding your account or charging services. This email may contain verification codes, receipt confirmations, service updates, or other relevant communications. Please check your registered email address for the complete message details. If you haven't received the expected email, please check your spam folder or contact our customer support team for assistance."),
        CHARGING_COMPLETE("Charging Complete", "Your charging session has been completed successfully after 2 hours and 15 minutes. Your vehicle received 45.3 kWh of energy, bringing your battery from 25% to 95% capacity. The total cost for this session was ₱285.50. Your receipt has been automatically sent to your registered email address and saved to your transaction history for future reference. Thank you for choosing Cephra charging services and we hope to see you again soon.");
        
        private final String displayName;
        private final String defaultMessage;
        
        NotificationType(String displayName, String defaultMessage) {
            this.displayName = displayName;
            this.defaultMessage = defaultMessage;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDefaultMessage() { return defaultMessage; }
    }
    
    public static class NotificationEntry {
        private final String id;
        private final NotificationType type;
        private final String message;
        private final String details;
        private final LocalDateTime timestamp;
        private final String username;
        private final String ticketId;
        private final String bayNumber;
        
        public NotificationEntry(NotificationType type, String message, String details, String username) {
            this(type, message, details, username, null, null);
        }
        
        public NotificationEntry(NotificationType type, String message, String details, String username, String ticketId, String bayNumber) {
            this.id = UUID.randomUUID().toString();
            this.type = type;
            this.message = message != null ? message : type.getDefaultMessage();
            this.details = details;
            this.timestamp = LocalDateTime.now();
            this.username = username;
            this.ticketId = ticketId;
            this.bayNumber = bayNumber;
        }
        
        // Getters
        public String getId() { return id; }
        public NotificationType getType() { return type; }
        public String getMessage() { return message; }
        public String getDetails() { return details; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public String getUsername() { return username; }
        public String getTicketId() { return ticketId; }
        public String getBayNumber() { return bayNumber; }
        
        public String getFormattedTime() {
            return timestamp.format(DateTimeFormatter.ofPattern("h:mm a"));
        }
        
        public String getFormattedDate() {
            return timestamp.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
        }
        
        public String getFormattedDateTime() {
            return timestamp.format(DateTimeFormatter.ofPattern("MMM dd, yyyy h:mm a"));
        }
    }
    
    // Interface for notification history update listeners
    public interface NotificationUpdateListener {
        void onNotificationAdded(NotificationEntry entry);
        void onNotificationHistoryUpdated(String username);
    }
    
    // Static storage for notifications (in real app, this would be database-backed)
    private static final List<NotificationEntry> allNotifications = new CopyOnWriteArrayList<>();
    private static final List<NotificationUpdateListener> listeners = new CopyOnWriteArrayList<>();
    
    // Add listener
    public static void addNotificationUpdateListener(NotificationUpdateListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    // Remove listener
    public static void removeNotificationUpdateListener(NotificationUpdateListener listener) {
        listeners.remove(listener);
    }
    
    // Add a notification
    public static void addNotification(NotificationEntry entry) {
        allNotifications.add(0, entry); // Add to beginning for newest first
        
        // Keep only last 100 notifications per user to avoid memory issues
        cleanupOldNotifications(entry.getUsername());
        
        // Notify listeners
        SwingUtilities.invokeLater(() -> {
            for (NotificationUpdateListener listener : listeners) {
                try {
                    listener.onNotificationAdded(entry);
                    listener.onNotificationHistoryUpdated(entry.getUsername());
                } catch (Exception e) {
                    System.err.println("Error notifying notification listener: " + e.getMessage());
                }
            }
        });
        
        System.out.println("NotificationHistoryManager: Added notification for " + entry.getUsername() + 
                          " - " + entry.getType().getDisplayName() + ": " + entry.getMessage());
        System.out.println("NotificationHistoryManager: Total notifications in memory: " + allNotifications.size());
        System.out.println("NotificationHistoryManager: Total listeners: " + listeners.size());
    }
    
    // Convenience methods for adding different types of notifications
    public static void addFullChargeNotification(String username, String ticketId) {
        addNotification(new NotificationEntry(
            NotificationType.FULL_CHARGE,
            null, // Use the default paragraph message from the enum
            "Charging session completed with full battery capacity. Vehicle ready for use.",
            username,
            ticketId,
            null
        ));
    }
    
    public static void addMyTurnNotification(String username, String ticketId, String bayNumber) {
        addNotification(new NotificationEntry(
            NotificationType.MY_TURN,
            null, // Use the default paragraph message from the enum
            "Charging bay " + bayNumber + " is now ready and reserved for your vehicle.",
            username,
            ticketId,
            bayNumber
        ));
    }
    
    public static void addTicketWaitingNotification(String username, String ticketId) {
        addNotification(new NotificationEntry(
            NotificationType.TICKET_WAITING,
            null, // Use the default paragraph message from the enum
            "Ticket " + ticketId + " has been queued and is waiting for available charging bay.",
            username,
            ticketId,
            null
        ));
    }
    
    public static void addTicketPendingNotification(String username, String ticketId) {
        addNotification(new NotificationEntry(
            NotificationType.TICKET_PENDING,
            null, // Use the default paragraph message from the enum
            "Ticket " + ticketId + " is being processed by system administrators for approval.",
            username,
            ticketId,
            null
        ));
    }
    
    public static void addEmailNotification(String username, String otp) {
        addNotification(new NotificationEntry(
            NotificationType.EMAIL_NOTIFICATION,
            null, // Use the default paragraph message from the enum
            "Email contains verification code: " + otp + " for account security purposes.",
            username,
            null,
            null
        ));
    }
    
    public static void addAdminUpdateNotification(String username, String message, String details) {
        addNotification(new NotificationEntry(
            NotificationType.ADMIN_UPDATE,
            message,
            details,
            username,
            null,
            null
        ));
    }
    
    public static void addChargingCompleteNotification(String username, String ticketId, String bayNumber) {
        addNotification(new NotificationEntry(
            NotificationType.CHARGING_COMPLETE,
            "Your charging session at Bay " + bayNumber + " has been completed successfully after 2 hours and 15 minutes. Your vehicle received 45.3 kWh of energy, bringing your battery from 25% to 95% capacity. The total cost for this session was ₱285.50. Your receipt has been automatically sent to your registered email address and saved to your transaction history for future reference. Thank you for choosing Cephra charging services and we hope to see you again soon.",
            "Complete charging session details with energy consumption, duration, cost breakdown, and receipt information.",
            username,
            ticketId,
            bayNumber
        ));
    }
    
    // Get notifications for a specific user
    public static List<NotificationEntry> getNotificationsForUser(String username) {
        if (username == null) return new ArrayList<>();
        
        List<NotificationEntry> userNotifications = new ArrayList<>();
        for (NotificationEntry entry : allNotifications) {
            if (username.equals(entry.getUsername())) {
                userNotifications.add(entry);
            }
        }
        return userNotifications;
    }
    
    // Get recent notifications for a user (last N)
    public static List<NotificationEntry> getRecentNotificationsForUser(String username, int count) {
        List<NotificationEntry> userNotifications = getNotificationsForUser(username);
        if (userNotifications.size() <= count) {
            return userNotifications;
        }
        return userNotifications.subList(0, count);
    }
    
    // Clean up old notifications for a user (keep only last 100)
    private static void cleanupOldNotifications(String username) {
        List<NotificationEntry> userNotifications = new ArrayList<>();
        for (NotificationEntry entry : allNotifications) {
            if (username.equals(entry.getUsername())) {
                userNotifications.add(entry);
            }
        }
        
        if (userNotifications.size() > 100) {
            // Remove oldest notifications
            for (int i = 100; i < userNotifications.size(); i++) {
                allNotifications.remove(userNotifications.get(i));
            }
        }
    }
    
    // Clear all notifications (for testing)
    public static void clearAllNotifications() {
        allNotifications.clear();
    }
    
    // Get count of notifications for user
    public static int getNotificationCountForUser(String username) {
        return getNotificationsForUser(username).size();
    }
}
