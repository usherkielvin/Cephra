package cephra.Phone.Utilities;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Background charging manager that handles charging independently of UI panels
 * Charging continues even when user switches panels or logs out
 * Also monitors for admin-triggered charging starts globally
 */
public class ChargingManager {
    
    // Singleton instance
    private static ChargingManager instance;
    
    // Track charging sessions for different users
    private static final ConcurrentHashMap<String, ChargingSession> activeSessions = new ConcurrentHashMap<>();
    
    // Global monitoring timer to detect admin changes
    private Timer globalMonitorTimer;
    private final ConcurrentHashMap<String, String> lastKnownUserStatus = new ConcurrentHashMap<>();
    
    // Charging session data
    private static class ChargingSession {
        Timer chargingTimer;
        String chargingType;
        long startTime;
        int startingBatteryLevel;
        boolean completionNotificationSent;
        
        ChargingSession(String type, int startLevel) {
            this.chargingType = type;
            this.startingBatteryLevel = startLevel;
            this.startTime = System.currentTimeMillis();
            this.completionNotificationSent = false;
        }
    }
    
    private ChargingManager() {
        // Private constructor for singleton
        startGlobalMonitoring();
    }
    
    public static ChargingManager getInstance() {
        if (instance == null) {
            instance = new ChargingManager();
        }
        return instance;
    }
    
    /**
     * Start global monitoring for admin changes across all users
     * This runs independently and checks for admin-triggered charging
     */
    private void startGlobalMonitoring() {
        globalMonitorTimer = new Timer(2000, new ActionListener() { // Check every 2 seconds
            @Override
            public void actionPerformed(ActionEvent e) {
                monitorAllUsersForChargingStarts();
            }
        });
        globalMonitorTimer.setRepeats(true);
        globalMonitorTimer.start();
        System.out.println("ChargingManager: Started global monitoring for admin changes");
    }
    
    /**
     * Monitor all users to detect when admin sets status to 'Charging'
     */
    private void monitorAllUsersForChargingStarts() {
        try {
            // Get all users from database who have tickets
            java.util.List<String> activeUsers = getActiveUsers();
            
            for (String username : activeUsers) {
                if (username == null || username.trim().isEmpty()) continue;
                
                String currentStatus = cephra.Database.CephraDB.getUserCurrentTicketStatus(username);
                String lastStatus = lastKnownUserStatus.get(username);
                
                // Check if admin just changed status to 'Charging'
                if ("Charging".equals(currentStatus) && !"Charging".equals(lastStatus)) {
                    // Admin just started charging for this user!
                    if (!isCharging(username)) {
                        String ticketId = getActiveTicketForUser(username);
                        String serviceType = getServiceTypeForTicket(ticketId);
                        
                        if (serviceType != null) {
                            System.out.println("ChargingManager: Auto-detected admin started charging for " + username + " (" + serviceType + ")");
                            startCharging(username, serviceType);
                        }
                    }
                }
                
                // Update last known status
                lastKnownUserStatus.put(username, currentStatus != null ? currentStatus : "");
            }
        } catch (Exception ex) {
            System.err.println("ChargingManager: Error in global monitoring: " + ex.getMessage());
        }
    }
    
    /**
     * Get list of users with active tickets
     */
    private java.util.List<String> getActiveUsers() {
        java.util.List<String> users = new java.util.ArrayList<>();
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection()) {
            // Get users from both active_tickets and queue_tickets
            String query = "SELECT DISTINCT username FROM (" +
                          "SELECT username FROM active_tickets " +
                          "UNION " +
                          "SELECT username FROM queue_tickets WHERE status IN ('Waiting', 'Pending', 'In Progress', 'Charging')" +
                          ") AS all_users";
            
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(query);
                 java.sql.ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String username = rs.getString("username");
                    if (username != null && !username.trim().isEmpty()) {
                        users.add(username);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("ChargingManager: Error getting active users: " + e.getMessage());
        }
        return users;
    }
    
    /**
     * Get active ticket ID for user
     */
    private String getActiveTicketForUser(String username) {
        try {
            String activeTicketId = cephra.Database.CephraDB.getActiveTicket(username);
            if (activeTicketId != null && !activeTicketId.isEmpty()) {
                return activeTicketId;
            }
            return cephra.Database.CephraDB.getQueueTicketForUser(username);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Get service type for a ticket
     */
    private String getServiceTypeForTicket(String ticketId) {
        if (ticketId == null) return null;
        
        try (java.sql.Connection conn = cephra.Database.DatabaseConnection.getConnection()) {
            // Try active_tickets first
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(
                    "SELECT service_type FROM active_tickets WHERE ticket_id = ?")) {
                stmt.setString(1, ticketId);
                try (java.sql.ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("service_type");
                    }
                }
            }
            
            // Try queue_tickets
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(
                    "SELECT service_type FROM queue_tickets WHERE ticket_id = ?")) {
                stmt.setString(1, ticketId);
                try (java.sql.ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("service_type");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("ChargingManager: Error getting service type: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Start background charging for a user
     */
    public void startCharging(String username, String serviceType) {
        if (username == null || username.isEmpty()) {
            return;
        }
        
        // Stop any existing charging for this user
        stopCharging(username);
        
        try {
            int currentBatteryLevel = cephra.Database.CephraDB.getUserBatteryLevel(username);
            if (currentBatteryLevel >= 100) {
                System.out.println("ChargingManager: User " + username + " already fully charged");
                return;
            }
            
            // Create charging session
            ChargingSession session = new ChargingSession(serviceType, currentBatteryLevel);
            
            // Calculate charging speed
            int batteryRemaining = 100 - currentBatteryLevel;
            int totalChargingTimeMs;
            
            if (serviceType.toLowerCase().contains("fast")) {
                totalChargingTimeMs = 30 * 1000; // 30 seconds
            } else {
                totalChargingTimeMs = 60 * 1000; // 1 minute
            }
            
            // Calculate timer delay per 1% increment
            int timerDelayMs = totalChargingTimeMs / batteryRemaining;
            
            // Create charging timer
            session.chargingTimer = new Timer(timerDelayMs, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateCharging(username);
                }
            });
            
            // Start charging
            activeSessions.put(username, session);
            session.chargingTimer.start();
            
            System.out.println("ChargingManager: Started " + serviceType + " charging for " + username + 
                             " from " + currentBatteryLevel + "% (background mode)");
            
        } catch (Exception e) {
            System.err.println("ChargingManager: Error starting charging for " + username + ": " + e.getMessage());
        }
    }
    
    /**
     * Update charging progress (called by timer)
     */
    private void updateCharging(String username) {
        ChargingSession session = activeSessions.get(username);
        if (session == null) {
            return;
        }
        
        try {
            int currentBattery = cephra.Database.CephraDB.getUserBatteryLevel(username);
            
            if (currentBattery < 100) {
                // Increment battery by 1%
                int newBatteryLevel = currentBattery + 1;
                cephra.Database.CephraDB.setUserBatteryLevel(username, newBatteryLevel);
                
                System.out.println("ChargingManager: " + username + " - Battery: " + newBatteryLevel + "% (" + session.chargingType + ")");
                
                // Check if charging is complete
                if (newBatteryLevel >= 100) {
                    completeCharging(username);
                }
            } else {
                completeCharging(username);
            }
        } catch (Exception e) {
            System.err.println("ChargingManager: Error updating charging for " + username + ": " + e.getMessage());
            completeCharging(username);
        }
    }
    
    /**
     * Complete charging process
     */
    private void completeCharging(String username) {
        ChargingSession session = activeSessions.get(username);
        if (session == null) {
            return;
        }
        
        try {
            // Stop timer
            if (session.chargingTimer != null && session.chargingTimer.isRunning()) {
                session.chargingTimer.stop();
            }
            
            // Update database ticket status to Complete
            String activeTicketId = cephra.Database.CephraDB.getActiveTicket(username);
            String queueTicketId = cephra.Database.CephraDB.getQueueTicketForUser(username);
            String currentTicketId = (activeTicketId != null && !activeTicketId.isEmpty()) ? activeTicketId : queueTicketId;
            
            if (currentTicketId != null) {
                cephra.Database.CephraDB.updateQueueTicketStatus(currentTicketId, "Complete");
                System.out.println("ChargingManager: Updated ticket " + currentTicketId + " status to Complete");
            }
            
            // Send completion notification (only once)
            if (currentTicketId != null && !session.completionNotificationSent) {
                cephra.Phone.Utilities.NotificationManager.addFullChargeNotification(username, currentTicketId);
                session.completionNotificationSent = true;
                System.out.println("ChargingManager: Sent completion notification for " + username + " ticket " + currentTicketId);
            }
            
            System.out.println("ChargingManager: Charging completed for " + username + " - Battery: 100%");
            
        } catch (Exception e) {
            System.err.println("ChargingManager: Error completing charging for " + username + ": " + e.getMessage());
        } finally {
            // Remove session
            activeSessions.remove(username);
        }
    }
    
    /**
     * Stop charging for a user
     */
    public void stopCharging(String username) {
        ChargingSession session = activeSessions.get(username);
        if (session != null) {
            if (session.chargingTimer != null && session.chargingTimer.isRunning()) {
                session.chargingTimer.stop();
            }
            activeSessions.remove(username);
            System.out.println("ChargingManager: Stopped charging for " + username);
        }
    }
    
    /**
     * Check if user is currently charging
     */
    public boolean isCharging(String username) {
        return activeSessions.containsKey(username);
    }
    
    /**
     * Get charging type for user (if charging)
     */
    public String getChargingType(String username) {
        ChargingSession session = activeSessions.get(username);
        return session != null ? session.chargingType : null;
    }
    
    /**
     * Stop all charging sessions (cleanup)
     */
    public void stopAllCharging() {
        for (String username : activeSessions.keySet()) {
            stopCharging(username);
        }
    }
    
    /**
     * Stop global monitoring (cleanup)
     */
    public void stopGlobalMonitoring() {
        if (globalMonitorTimer != null && globalMonitorTimer.isRunning()) {
            globalMonitorTimer.stop();
            System.out.println("ChargingManager: Stopped global monitoring");
        }
    }
}