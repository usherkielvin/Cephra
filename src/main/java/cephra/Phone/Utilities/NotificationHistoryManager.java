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
public class NotificationHistoryManager {
    
    public enum NotificationType {
        FULL_CHARGE("Full Charge", "Your car is now fully charged"),
        TICKET_WAITING("Ticket Waiting", "Your ticket is now waiting"),
        TICKET_PENDING("Ticket Pending", "Your ticket is now pending"),
        MY_TURN("Your Turn", "Please go to your charging bay"),
        ADMIN_UPDATE("Admin Update", "Update from admin"),
        EMAIL_NOTIFICATION("Email", "New email notification");
        
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
            "Your car is now fully charged",
            "Charging completed successfully. Please remove your vehicle from the bay.",
            username,
            ticketId,
            null
        ));
    }
    
    public static void addMyTurnNotification(String username, String ticketId, String bayNumber) {
        addNotification(new NotificationEntry(
            NotificationType.MY_TURN,
            "Please go to your charging bay \"" + bayNumber + "\" now",
            "It's your turn! Please proceed to bay " + bayNumber + " to start charging.",
            username,
            ticketId,
            bayNumber
        ));
    }
    
    public static void addTicketWaitingNotification(String username, String ticketId) {
        addNotification(new NotificationEntry(
            NotificationType.TICKET_WAITING,
            "Your ticket \"" + ticketId + "\" is now waiting",
            "Your charging request has been queued and is waiting for an available bay.",
            username,
            ticketId,
            null
        ));
    }
    
    public static void addTicketPendingNotification(String username, String ticketId) {
        addNotification(new NotificationEntry(
            NotificationType.TICKET_PENDING,
            "Your ticket \"" + ticketId + "\" is now pending",
            "Your charging request is being processed by our team.",
            username,
            ticketId,
            null
        ));
    }
    
    public static void addEmailNotification(String username, String otp) {
        addNotification(new NotificationEntry(
            NotificationType.EMAIL_NOTIFICATION,
            "New email with OTP: " + otp,
            "You have received a new email with verification code.",
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
