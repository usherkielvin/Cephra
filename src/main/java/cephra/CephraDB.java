package cephra;

import java.sql.*;
import java.util.*;
import java.time.*;
import java.time.format.*;
import java.awt.*;
import javax.swing.*;

/**
 * CephraDB - Main database management class for the Cephra charging station system.
 * 
 * This class provides comprehensive database operations including:
 * - User authentication and management
 * - Battery level tracking
 * - Queue ticket management
 * - Charging history and payment processing
 * - Staff management
 * - System settings
 * 
 * All database operations are performed using prepared statements for security
 * and the class includes comprehensive error handling and logging.
 */
public class CephraDB {

    private static class User {
        final String username;
        final String email;
        @SuppressWarnings("unused")
        final String password;

        public User(String username, String email, String password) {
            this.username = username;
            this.email = email;
            this.password = password;
        }
    }
    private static User currentPhoneUser;
    private static User currentAdminUser;
    
    public static void initializeDatabase() {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection()) {
            System.out.println("Initializing Cephra Database...");
            
            // Check if all required tables exist
            String[] requiredTables = {
                "users", "battery_levels", "active_tickets", "otp_codes",
                "queue_tickets", "charging_history", "staff_records", 
                "charging_bays", "payment_transactions", "system_settings"
            };
            
            boolean allTablesExist = true;
            for (String tableName : requiredTables) {
                if (!tableExists(conn, tableName)) {
                    System.err.println("Warning: " + tableName + " table does not exist.");
                    allTablesExist = false;
                }
            }
            
            if (!allTablesExist) {
                printDatabaseInitInstructions();
            } else {
                System.out.println("All  database tables are present.");
            }
            
            // Clean up any existing duplicate battery level entries
            cleanupDuplicateBatteryLevels();
            
            // Also clean up duplicates for all users to prevent future issues
            cleanupAllDuplicateBatteryLevels();
            
            // Clean up any orphaned queue tickets (tickets in queue but already in history)
            cleanupOrphanedQueueTickets();
            
            // Clean up admin users from users table (they should be in staff_records)
            cleanupAdminFromUsersTable();
            
            // Synchronize bay status with database
            cephra.Admin.BayManagement.synchronizeBayStatusWithDatabase();
            
            // Print current bay status for debugging
            cephra.Admin.BayManagement.printCurrentBayStatus();
            
            // Verify database connection and basic functionality
            verifyDatabaseConnection(conn);
            
            System.out.println("Cephra MySQL database connected successfully.");
        } catch (SQLException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            System.err.println("Please ensure:");
            System.err.println("1. XAMPP MySQL service is running");
            System.err.println("2. Database 'cephradb' exists");
            System.err.println("3. All tables are created using init.sql");
            e.printStackTrace();
        }
    }
    
    // Helper method to check if a table exists
    private static boolean tableExists(Connection conn, String tableName) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = ?")) {
            stmt.setString(1, tableName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking if table exists: " + e.getMessage());
        }
        return false;
    }

    // Method to check if the given credentials are valid (for phone users)
    public static boolean validateLogin(String username, String password) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT username, email, password FROM users WHERE username = ? AND password = ?")) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Set the current phone user when login is successful
                    currentPhoneUser = new User(
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password")
                    );
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error validating login: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    // Method to get the current logged-in username (phone user)
    public static String getCurrentUsername() {
        return currentPhoneUser != null ? currentPhoneUser.username : "";
    }
    
    // Method to get the current logged-in user's email (phone user)
    public static String getCurrentEmail() {
        return currentPhoneUser != null ? currentPhoneUser.email : "";
    }
    
    // Method to get the current logged-in user's firstname (phone user)
    public static String getCurrentFirstname() {
        return getCurrentUserField("firstname");
    }
    
    // Method to get the current logged-in user's lastname (phone user)
    public static String getCurrentLastname() {
        return getCurrentUserField("lastname");
    }
    
    // Helper method to get a specific field from the current user
    private static String getCurrentUserField(String fieldName) {
        if (currentPhoneUser == null || currentPhoneUser.username == null) {
            return "";
        }
        
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT " + fieldName + " FROM users WHERE username = ?")) {
            
            stmt.setString(1, currentPhoneUser.username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(fieldName);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting current user's " + fieldName + ": " + e.getMessage());
            e.printStackTrace();
        }
        return "";
    }
    
    // Method to logout the current phone user
    public static void logoutCurrentUser() {
        System.out.println("CephraDB: Logging out user " + (currentPhoneUser != null ? currentPhoneUser.username : "null"));
        currentPhoneUser = null;
    }
    
    // Method to check if any user is currently logged in
    public static boolean isUserLoggedIn() {
        return currentPhoneUser != null;
    }
    
    // Method to get the current admin username
    public static String getCurrentAdminUsername() {
        return currentAdminUser != null ? currentAdminUser.username : "";
    }
    
    // Method to get the current admin email
    public static String getCurrentAdminEmail() {
        return currentAdminUser != null ? currentAdminUser.email : "";
    }

    // Method to add a new user to the database
    public static boolean addUser(String firstname, String lastname, String username, String email, String password) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO users (firstname, lastname, username, email, password) VALUES (?, ?, ?, ?, ?)")) {
            
            stmt.setString(1, firstname);
            stmt.setString(2, lastname);
            stmt.setString(3, username);
            stmt.setString(4, email);
            stmt.setString(5, password);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            // Check if it's a duplicate key error
            if (e.getMessage().contains("Duplicate entry")) {
                System.err.println("User already exists: " + e.getMessage());
                return false;
            }
            System.err.println("Error adding user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Method to check if a user exists
    public static boolean userExists(String username) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT username FROM users WHERE username = ?")) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // If there's a result, user exists
            }
        } catch (SQLException e) {
            System.err.println("Error checking if user exists: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Method to find a user by email
    public static User findUserByEmail(String email) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT username, email, password FROM users WHERE email = ?")) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by email: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    // Method to update a user's password
    public static boolean updateUserPassword(String email, String newPassword) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE users SET password = ? WHERE email = ?")) {
            
            stmt.setString(1, newPassword);
            stmt.setString(2, email);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating password: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Method to get a user's current password by email
    public static String getUserPasswordByEmail(String email) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT password FROM users WHERE email = ?")) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting user password: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    // Method to generate and store a new 6-digit OTP
    public static String generateAndStoreOTP() {
        Random random = new Random();
        String generatedOTP = String.format("%06d", random.nextInt(1000000));
        
        // Determine which email to use for OTP storage
        String emailToUse = null;
        if (currentPhoneUser != null) {
            emailToUse = currentPhoneUser.email;
        } else if (cephra.Phone.AppSessionState.userEmailForReset != null) {
            emailToUse = cephra.Phone.AppSessionState.userEmailForReset;
        }
        
        // Store OTP in database if we have an email
        if (emailToUse != null) {
            try (Connection conn = cephra.db.DatabaseConnection.getConnection();
                 PreparedStatement findUserStmt = conn.prepareStatement(
                         "SELECT username FROM users WHERE email = ?")) {
                
                findUserStmt.setString(1, emailToUse);
                ResultSet userRs = findUserStmt.executeQuery();
                
                if (userRs.next()) {
                    String username = userRs.getString("username");
                    
                    // Delete any existing OTP for this username
                    try (PreparedStatement deleteStmt = conn.prepareStatement(
                            "DELETE FROM otp_codes WHERE username = ?")) {
                        deleteStmt.setString(1, username);
                        deleteStmt.executeUpdate();
                    }
                    
                    // Insert new OTP with expiration (15 minutes from now)
                    try (PreparedStatement insertStmt = conn.prepareStatement(
                            "INSERT INTO otp_codes (username, otp_code, expires_at) VALUES (?, ?, DATE_ADD(NOW(), INTERVAL 15 MINUTE))")) {
                        insertStmt.setString(1, username);
                        insertStmt.setString(2, generatedOTP);
                        insertStmt.executeUpdate();
                    }
                    
                    System.out.println("Generated and stored OTP: " + generatedOTP + " for user: " + username);
                }
                
            } catch (SQLException e) {
                System.err.println("Error storing OTP: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        System.out.println("Generated OTP: " + generatedOTP);
        return generatedOTP;
    }

    // Method to get the stored OTP
    public static String getGeneratedOTP() {
        // Determine which email to use for OTP retrieval
        String emailToUse = null;
        if (currentPhoneUser != null) {
            emailToUse = currentPhoneUser.email;
        } else if (cephra.Phone.AppSessionState.userEmailForReset != null) {
            emailToUse = cephra.Phone.AppSessionState.userEmailForReset;
        }
        
        if (emailToUse == null) {
            return null;
        }
        
        try (Connection conn = cephra.db.DatabaseConnection.getConnection()) {
            // First find username from email
            PreparedStatement findUserStmt = conn.prepareStatement(
                    "SELECT username FROM users WHERE email = ?");
            
            findUserStmt.setString(1, emailToUse);
            ResultSet userRs = findUserStmt.executeQuery();
            
            if (userRs.next()) {
                String username = userRs.getString("username");
                
                // Now get OTP using username
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT otp_code FROM otp_codes WHERE username = ? AND expires_at > NOW()");
                
                stmt.setString(1, username);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("otp_code");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting OTP: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    // Battery management methods
    public static int getUserBatteryLevel(String username) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT battery_level FROM battery_levels WHERE username = ? ORDER BY id DESC LIMIT 1")) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Return the most recent stored battery level (no randomization)
                    int batteryLevel = rs.getInt("battery_level");
                    System.out.println("CephraDB: Retrieved battery level for " + username + ": " + batteryLevel + "%");
                    return batteryLevel;
                } else {
                    // No battery level found - return -1 to indicate no battery initialized yet
                    System.out.println("CephraDB: No battery level found for " + username + " - returning -1");
                    return -1;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting battery level: " + e.getMessage());
            e.printStackTrace();
            // Return -1 to indicate no battery initialized
            return -1;
        }
    }
    
    public static void setUserBatteryLevel(String username, int batteryLevel) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection()) {
            // First, delete ALL existing battery level entries for this user
            try (PreparedStatement deleteStmt = conn.prepareStatement(
                    "DELETE FROM battery_levels WHERE username = ?")) {
                
                deleteStmt.setString(1, username);
                int deletedRows = deleteStmt.executeUpdate();
                if (deletedRows > 0) {
                    System.out.println("CephraDB: Deleted " + deletedRows + " old battery level entries for " + username);
                }
            }
            
            // Then insert the new battery level entry
            try (PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO battery_levels (username, battery_level, initial_battery_level, battery_capacity_kwh) VALUES (?, ?, ?, ?)")) {
                
                insertStmt.setString(1, username);
                insertStmt.setInt(2, batteryLevel);
                insertStmt.setInt(3, batteryLevel); // initial_battery_level same as current
                insertStmt.setDouble(4, 40.0); // default battery capacity
                
                int rowsAffected = insertStmt.executeUpdate();
                System.out.println("CephraDB: Set new battery level for " + username + " to " + batteryLevel + "% (rows affected: " + rowsAffected + ")");
            }
            
        } catch (SQLException e) {
            System.err.println("Error setting battery level: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void chargeUserBatteryToFull(String username) {
        setUserBatteryLevel(username, 100);
    }
    
    // Method to check for duplicate battery level entries for a user
    public static void checkDuplicateBatteryLevels(String username) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM battery_levels WHERE username = ?")) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count > 1) {
                        System.err.println("CephraDB: WARNING - Found " + count + " battery level entries for user " + username);
                        logBatteryLevelDetails(conn, username);
                    } else {
                        System.out.println("CephraDB: Found " + count + " battery level entry for user " + username);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking duplicate battery levels: " + e.getMessage());
        }
    }
    
    // Helper method to log battery level details
    private static void logBatteryLevelDetails(Connection conn, String username) {
        try (PreparedStatement detailStmt = conn.prepareStatement(
                "SELECT id, battery_level, initial_battery_level FROM battery_levels WHERE username = ? ORDER BY id")) {
            detailStmt.setString(1, username);
            try (ResultSet detailRs = detailStmt.executeQuery()) {
                while (detailRs.next()) {
                    System.err.println("CephraDB: Entry ID " + detailRs.getInt("id") + 
                                     " - battery_level: " + detailRs.getInt("battery_level") + 
                                     "%, initial_battery_level: " + detailRs.getInt("initial_battery_level") + "%");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error logging battery level details: " + e.getMessage());
        }
    }
    
    // Method to clean up duplicate battery level entries
    public static void cleanupDuplicateBatteryLevels() {
        cleanupBatteryLevels(false);
    }
    
    // Method to clean up all duplicate battery level entries for all users
    public static void cleanupAllDuplicateBatteryLevels() {
        cleanupBatteryLevels(true);
    }
    
    // Consolidated method to clean up battery level duplicates
    private static void cleanupBatteryLevels(boolean allUsers) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection()) {
            String cleanupSQL = allUsers ? 
                "DELETE b1 FROM battery_levels b1 " +
                "LEFT JOIN (" +
                "    SELECT username, MAX(id) as max_id " +
                "    FROM battery_levels " +
                "    GROUP BY username" +
                ") b2 ON b1.username = b2.username AND b1.id = b2.max_id " +
                "WHERE b2.max_id IS NULL" :
                "DELETE b1 FROM battery_levels b1 " +
                "INNER JOIN battery_levels b2 " +
                "WHERE b1.id > b2.id AND b1.username = b2.username";
            
            try (PreparedStatement stmt = conn.prepareStatement(cleanupSQL)) {
                int deletedRows = stmt.executeUpdate();
                if (deletedRows > 0) {
                    String message = allUsers ? 
                        "Cleaned up " + deletedRows + " old duplicate battery level entries for all users" :
                        "Cleaned up " + deletedRows + " duplicate battery level entries";
                    System.out.println(message);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error cleaning up duplicate battery levels: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Active ticket management methods
    public static boolean hasActiveTicket(String username) {
        return executeActiveTicketQuery(username, "SELECT ticket_id FROM active_tickets WHERE username = ?") != null;
    }
    
    // Helper method for active ticket queries
    private static String executeActiveTicketQuery(String username, String query) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection()) {
            if (!tableExists(conn, "active_tickets")) {
                System.err.println("Warning: active_tickets table does not exist.");
                printDatabaseInitInstructions();
                return null;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next() ? rs.getString("ticket_id") : null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error executing active ticket query: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public static void setActiveTicket(String username, String ticketId) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection()) {
            // First check if the active_tickets table exists
            if (!tableExists(conn, "active_tickets")) {
                System.err.println("Warning: active_tickets table does not exist.");
                printDatabaseInitInstructions();
                return; // Cannot set active ticket if table doesn't exist
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO active_tickets (username, ticket_id, service_type, initial_battery_level, current_battery_level, status) VALUES (?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE ticket_id = ?, service_type = ?, initial_battery_level = ?, current_battery_level = ?, status = ?")) {
                
                // Get user's current battery level
                int batteryLevel = getUserBatteryLevel(username);
                
                stmt.setString(1, username);
                stmt.setString(2, ticketId);
                stmt.setString(3, "Normal"); // Default service type
                stmt.setInt(4, batteryLevel);
                stmt.setInt(5, batteryLevel);
                stmt.setString(6, "Active");
                stmt.setString(7, ticketId);
                stmt.setString(8, "Normal");
                stmt.setInt(9, batteryLevel);
                stmt.setInt(10, batteryLevel);
                stmt.setString(11, "Active");
                
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error setting active ticket: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Overloaded method to set active ticket with full details
    public static void setActiveTicket(String username, String ticketId, String serviceType, int initialBatteryLevel, String bayNumber) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection()) {
            // First check if the active_tickets table exists
            if (!tableExists(conn, "active_tickets")) {
                System.err.println("Warning: active_tickets table does not exist.");
                printDatabaseInitInstructions();
                return; // Cannot set active ticket if table doesn't exist
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO active_tickets (username, ticket_id, service_type, initial_battery_level, current_battery_level, bay_number, status) VALUES (?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE service_type = ?, initial_battery_level = ?, current_battery_level = ?, bay_number = ?, status = ?")) {
                
                stmt.setString(1, username);
                stmt.setString(2, ticketId);
                stmt.setString(3, serviceType);
                stmt.setInt(4, initialBatteryLevel);
                stmt.setInt(5, initialBatteryLevel);
                stmt.setString(6, bayNumber);
                stmt.setString(7, "Active");
                stmt.setString(8, serviceType);
                stmt.setInt(9, initialBatteryLevel);
                stmt.setInt(10, initialBatteryLevel);
                stmt.setString(11, bayNumber);
                stmt.setString(12, "Active");
                
                stmt.executeUpdate();
                
                // Mark the bay as occupied in the Bay class
              
            }
        } catch (SQLException e) {
            System.err.println("Error setting active ticket with details: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void clearActiveTicket(String username) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection()) {
            // First check if the active_tickets table exists
            if (!tableExists(conn, "active_tickets")) {
                System.err.println("Warning: active_tickets table does not exist.");
                printDatabaseInitInstructions();
                return; // Cannot clear active ticket if table doesn't exist
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM active_tickets WHERE username = ?")) {
                
                stmt.setString(1, username);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error clearing active ticket: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Method to clear active ticket by ticket ID
    public static void clearActiveTicketByTicketId(String ticketId) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection()) {
            // First check if the active_tickets table exists
            if (!tableExists(conn, "active_tickets")) {
                System.err.println("Warning: active_tickets table does not exist.");
                printDatabaseInitInstructions();
                return; // Cannot clear active ticket if table doesn't exist
            }
            

            
            // Now delete the active ticket
            try (PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM active_tickets WHERE ticket_id = ?")) {
                
                stmt.setString(1, ticketId);
                stmt.executeUpdate();
            }
            
            // Mark the bay as free in the Bay class
          
        } catch (SQLException e) {
            System.err.println("Error clearing active ticket by ticket ID: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static String getActiveTicket(String username) {
        return executeActiveTicketQuery(username, "SELECT ticket_id FROM active_tickets WHERE username = ?");
    }
    
    // Queue ticket management methods
    public static boolean addQueueTicket(String ticketId, String username, String serviceType, 
                                       String status, String paymentStatus, int initialBatteryLevel) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection()) {
            // First, ensure the user exists in the users table
            if (!userExists(username)) {
                // Create a temporary user if they don't exist
                addUser("Temp", "User", username, username + "@cephra.com", "temp123");
            }
            
            // Check if ticket already exists
            try (PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT ticket_id FROM queue_tickets WHERE ticket_id = ?")) {
                checkStmt.setString(1, ticketId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        System.err.println("Ticket " + ticketId + " already exists in database. Skipping insertion.");
                        return false; // Ticket already exists
                    }
                }
            }
            
            // Now insert the queue ticket
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO queue_tickets (ticket_id, username, service_type, status, " +
                    "payment_status, initial_battery_level) VALUES (?, ?, ?, ?, ?, ?)")) {
                
                stmt.setString(1, ticketId);
                stmt.setString(2, username);
                stmt.setString(3, serviceType);
                stmt.setString(4, status);
                stmt.setString(5, paymentStatus);
                stmt.setInt(6, initialBatteryLevel);
                
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error adding queue ticket: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean updateQueueTicketStatus(String ticketId, String status) {
        // Validate input parameters
        if (ticketId == null || ticketId.trim().isEmpty()) {
            System.err.println("CephraDB: Invalid ticket ID for status update");
            return false;
        }
        if (status == null || status.trim().isEmpty()) {
            System.err.println("CephraDB: Invalid status for ticket " + ticketId);
            return false;
        }
        
        try (Connection conn = cephra.db.DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.err.println("CephraDB: Could not establish database connection for status update");
                return false;
            }
            
            // First check if the ticket exists
            try (PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT ticket_id FROM queue_tickets WHERE ticket_id = ?")) {
                checkStmt.setString(1, ticketId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        System.err.println("CephraDB: Ticket " + ticketId + " not found in queue_tickets table");
                        return false;
                    }
                }
            }
            
            // Update the status
            try (PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE queue_tickets SET status = ? WHERE ticket_id = ?")) {
                
                stmt.setString(1, status);
                stmt.setString(2, ticketId);
                
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("CephraDB: Successfully updated status to '" + status + "' for ticket " + ticketId);
                    return true;
                } else {
                    System.err.println("CephraDB: No rows affected when updating status for ticket " + ticketId);
                    return false;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error updating queue ticket status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean updateQueueTicketPayment(String ticketId, String paymentStatus, String referenceNumber) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE queue_tickets SET payment_status = ?, reference_number = ? WHERE ticket_id = ?")) {
            
            stmt.setString(1, paymentStatus);
            stmt.setString(2, referenceNumber);
            stmt.setString(3, ticketId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating queue ticket payment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean removeQueueTicket(String ticketId) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM queue_tickets WHERE ticket_id = ?")) {
            
            stmt.setString(1, ticketId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error removing queue ticket: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /** Check if ticket is already in charging history (already processed) */
    public static boolean isTicketInChargingHistory(String ticketId) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM charging_history WHERE ticket_id = ?")) {
            
            stmt.setString(1, ticketId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking if ticket is in charging history: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    // Charging history methods
    public static boolean addChargingHistory(String ticketId, String username, String serviceType,
                                           int initialBatteryLevel, int chargingTimeMinutes, 
                                           double totalAmount, String referenceNumber) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO charging_history (ticket_id, username, service_type, " +
                     "initial_battery_level, final_battery_level, charging_time_minutes, total_amount, reference_number, served_by) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            
            stmt.setString(1, ticketId);
            stmt.setString(2, username);
            stmt.setString(3, serviceType);
            stmt.setInt(4, initialBatteryLevel);
            stmt.setInt(5, 100); // Final battery level is always 100% when completed
            stmt.setInt(6, chargingTimeMinutes);
            stmt.setDouble(7, totalAmount);
            stmt.setString(8, referenceNumber);
            
            // Get the actual admin username who is currently logged in
            String adminUsername = getCurrentAdminUsername();
            if (adminUsername == null || adminUsername.trim().isEmpty()) {
                adminUsername = "Admin"; // Fallback if no admin logged in
            }
            stmt.setString(9, adminUsername);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding charging history: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public static java.util.List<Object[]> getChargingHistoryForUser(String username) {
        java.util.List<Object[]> history = new ArrayList<>();
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT ticket_id, username, service_type, initial_battery_level, charging_time_minutes, " +
                     "total_amount, reference_number, completed_at FROM charging_history " +
                     "WHERE username = ? ORDER BY completed_at DESC")) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("ticket_id"),
                        rs.getString("username"),
                        rs.getString("service_type"),
                        rs.getInt("initial_battery_level"),
                        rs.getInt("charging_time_minutes"),
                        rs.getDouble("total_amount"),
                        rs.getString("reference_number"),
                        rs.getTimestamp("completed_at")
                    };
                    history.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting charging history: " + e.getMessage());
            e.printStackTrace();
        }
        return history;
    }
    
    public static java.util.List<Object[]> getAllChargingHistory() {
        java.util.List<Object[]> history = new ArrayList<>();
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT ticket_id, username, service_type, initial_battery_level, charging_time_minutes, " +
                     "total_amount, reference_number, completed_at FROM charging_history " +
                     "ORDER BY completed_at DESC")) {
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("ticket_id"),
                        rs.getString("username"),
                        rs.getString("service_type"),
                        rs.getInt("initial_battery_level"),
                        rs.getInt("charging_time_minutes"),
                        rs.getDouble("total_amount"),
                        rs.getString("reference_number"),
                        rs.getTimestamp("completed_at")
                    };
                    history.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting all charging history: " + e.getMessage());
            e.printStackTrace();
        }
        return history;
    }
    
    public static java.util.List<Object[]> getAllQueueTickets() {
        java.util.List<Object[]> tickets = new ArrayList<>();
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT ticket_id, reference_number, username, service_type, status, payment_status " +
                     "FROM queue_tickets ORDER BY created_at DESC")) {
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("ticket_id"),
                        rs.getString("reference_number"),
                        rs.getString("username"),
                        rs.getString("service_type"),
                        rs.getString("status"),
                        rs.getString("payment_status")
                    };
                    tickets.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting all queue tickets: " + e.getMessage());
            e.printStackTrace();
        }
        return tickets;
    }
    
    // Payment transaction methods
        public static boolean addPaymentTransaction(String ticketId, String username, double amount, 
                                               String paymentMethod, String referenceNumber) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO payment_transactions (ticket_id, username, amount, " +
                     "payment_method, reference_number) VALUES (?, ?, ?, ?, ?)")) {
            
            stmt.setString(1, ticketId);
            stmt.setString(2, username);
            stmt.setDouble(3, amount);
            stmt.setString(4, paymentMethod);
            stmt.setString(5, referenceNumber);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding payment transaction: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Method to get payment method for a specific ticket
    public static String getPaymentMethodForTicket(String ticketId) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT payment_method FROM payment_transactions WHERE ticket_id = ?")) {
            
            stmt.setString(1, ticketId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("payment_method");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting payment method for ticket: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    // Method to check if payment has already been processed for a ticket
    public static boolean isPaymentAlreadyProcessed(String ticketId) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM charging_history WHERE ticket_id = ?")) {
            
            stmt.setString(1, ticketId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0; // If count > 0, payment already exists
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking if payment already processed: " + e.getMessage());
            e.printStackTrace();
        }
        return false; // Default to false if error occurs
    }
    
    // Method to process payment transaction with all related database operations in a single transaction
    public static boolean processPaymentTransaction(String ticketId, String username, String serviceType,
                                                  int initialBatteryLevel, int chargingTimeMinutes, 
                                                  double totalAmount, String paymentMethod, String referenceNumber) {
        // Validate input parameters
        if (ticketId == null || ticketId.trim().isEmpty()) {
            System.err.println("CephraDB: Invalid ticket ID for payment transaction");
            return false;
        }
        if (username == null || username.trim().isEmpty()) {
            System.err.println("CephraDB: Invalid username for payment transaction");
            return false;
        }
        if (serviceType == null || serviceType.trim().isEmpty()) {
            System.err.println("CephraDB: Invalid service type for payment transaction");
            return false;
        }
        if (totalAmount < 0) {
            System.err.println("CephraDB: Invalid amount for payment transaction: " + totalAmount);
            return false;
        }
        
        Connection conn = null;
        try {
            conn = cephra.db.DatabaseConnection.getConnection();
            if (conn == null) {
                System.err.println("CephraDB: Could not establish database connection for payment transaction");
                return false;
            }
            conn.setAutoCommit(false); // Start transaction
            
            // 1. Add to charging history
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO charging_history (ticket_id, username, service_type, " +
                    "initial_battery_level, final_battery_level, charging_time_minutes, total_amount, reference_number, served_by) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                
                stmt.setString(1, ticketId);
                stmt.setString(2, username);
                stmt.setString(3, serviceType);
                stmt.setInt(4, initialBatteryLevel);
                stmt.setInt(5, 100); // Final battery level is always 100% when completed
                stmt.setInt(6, chargingTimeMinutes);
                stmt.setDouble(7, totalAmount);
                stmt.setString(8, referenceNumber != null ? referenceNumber : "");
                
                // Get the actual admin username who is currently logged in
                String adminUsername = getCurrentAdminUsername();
                if (adminUsername == null || adminUsername.trim().isEmpty()) {
                    adminUsername = "Admin"; // Fallback if no admin logged in
                }
                stmt.setString(9, adminUsername);
                
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected <= 0) {
                    System.err.println("CephraDB: Failed to insert charging history for ticket " + ticketId);
                    conn.rollback();
                    return false;
                }
            }
            
            // 2. Add payment transaction record
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO payment_transactions (ticket_id, username, amount, " +
                    "payment_method, reference_number) VALUES (?, ?, ?, ?, ?)")) {
                
                stmt.setString(1, ticketId);
                stmt.setString(2, username);
                stmt.setDouble(3, totalAmount);
                stmt.setString(4, paymentMethod != null ? paymentMethod : "Cash");
                stmt.setString(5, referenceNumber != null ? referenceNumber : "");
                
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected <= 0) {
                    System.err.println("CephraDB: Failed to insert payment transaction for ticket " + ticketId);
                    conn.rollback();
                    return false;
                }
            }
            
            // 3. Update queue ticket payment status
            try (PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE queue_tickets SET payment_status = ?, reference_number = ? WHERE ticket_id = ?")) {
                
                stmt.setString(1, "Paid");
                stmt.setString(2, referenceNumber != null ? referenceNumber : "");
                stmt.setString(3, ticketId);
                
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected <= 0) {
                    System.err.println("CephraDB: Failed to update queue ticket payment status for ticket " + ticketId);
                    conn.rollback();
                    return false;
                }
            }
            
            // 3.5. Clear active ticket (if exists)
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM active_tickets WHERE ticket_id = ?")) {
                
                stmt.setString(1, ticketId);
                stmt.executeUpdate(); // Don't fail if no active ticket exists
            }
            
            // 3.5.1. Clear charging bays and charging grid for this ticket
            try (PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE charging_bays SET current_ticket_id = NULL, current_username = NULL, status = 'Available', start_time = NULL WHERE current_ticket_id = ?")) {
                
                stmt.setString(1, ticketId);
                int bayRowsUpdated = stmt.executeUpdate();
                if (bayRowsUpdated > 0) {
                    System.out.println("CephraDB: Cleared charging bay for ticket " + ticketId + " - rows updated: " + bayRowsUpdated);
                }
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE charging_grid SET ticket_id = NULL, username = NULL, service_type = NULL, initial_battery_level = NULL, start_time = NULL WHERE ticket_id = ?")) {
                
                stmt.setString(1, ticketId);
                int gridRowsUpdated = stmt.executeUpdate();
                if (gridRowsUpdated > 0) {
                    System.out.println("CephraDB: Cleared charging grid for ticket " + ticketId + " - rows updated: " + gridRowsUpdated);
                }
            }
            
            // 3.6. Remove ticket from queue_tickets table (move to history)
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM queue_tickets WHERE ticket_id = ?")) {
                
                stmt.setString(1, ticketId);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("CephraDB: Removed ticket " + ticketId + " from queue_tickets table (moved to history)");
                }
            }
            
            // 3.7. Update user's battery level to 100% when charging is completed
            try (PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE battery_levels SET battery_level = 100 WHERE username = ?")) {
                
                stmt.setString(1, username);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("CephraDB: Updated battery level to 100% for user " + username + " after charging completion");
                } else {
                    // If no battery level record exists, create one with 100%
                    try (PreparedStatement insertStmt = conn.prepareStatement(
                            "INSERT INTO battery_levels (username, battery_level, initial_battery_level, battery_capacity_kwh) VALUES (?, 100, 100, 40.0)")) {
                        
                        insertStmt.setString(1, username);
                        insertStmt.executeUpdate();
                        System.out.println("CephraDB: Created battery level record with 100% for user " + username + " after charging completion");
                    }
                }
                
                // Verify the battery level was updated correctly
                int verifyBattery = getUserBatteryLevel(username);
                System.out.println("CephraDB: Verified battery level after update: " + verifyBattery + "% for user " + username);
            }
            
            // 4. Add reward points for all payments (1 PHP = 0.05 points)
            if (totalAmount > 0) {
                try {
                    // Use static method to add points globally
                    cephra.Phone.Rewards.addPointsForPaymentGlobally(totalAmount);
                    System.out.println("CephraDB: Added points for payment of " + totalAmount + " PHP to user " + username);
                } catch (Exception pointsEx) {
                    System.err.println("CephraDB: Error adding points for payment: " + pointsEx.getMessage());
                    // Don't fail the transaction if points addition fails
                }
            }
            
            // 5. Add to admin history (if HistoryBridge is available)
            addToAdminHistory(ticketId, username, totalAmount, referenceNumber);
            
            conn.commit(); // Commit transaction
            System.out.println("CephraDB: Successfully committed payment transaction for ticket " + ticketId);
            
            // Clear charging bay and grid after successful payment
            cephra.Admin.BayManagement.clearChargingBayForCompletedTicket(ticketId);
            
            // Verify that the ticket was added to charging history
            try (PreparedStatement verifyStmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM charging_history WHERE ticket_id = ?")) {
                verifyStmt.setString(1, ticketId);
                try (ResultSet rs = verifyStmt.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        System.out.println("CephraDB: Verification - ticket " + ticketId + " found in charging_history: " + count + " records");
                    }
                }
            } catch (Exception verifyEx) {
                System.err.println("CephraDB: Error verifying ticket in charging_history: " + verifyEx.getMessage());
            }
            
            // Notify phone history that a new entry has been added
            try {
                cephra.Phone.UserHistoryManager.notifyHistoryUpdate(username);
                System.out.println("CephraDB: Notified phone history for user: " + username);
            } catch (Exception e) {
                System.err.println("CephraDB: Error notifying phone history: " + e.getMessage());
            }
            
            // Refresh admin history table to show the new completed ticket
            try {
                cephra.Admin.HistoryBridge.refreshHistoryTable();
                System.out.println("CephraDB: Refreshed admin history table after payment completion");
            } catch (Exception e) {
                System.err.println("CephraDB: Error refreshing admin history table: " + e.getMessage());
            }
            
            // Refresh Porsche screen to show updated 100% battery level
            refreshPorscheScreen(username);
            
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error processing payment transaction: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("CephraDB: Rolled back payment transaction for ticket " + ticketId);
                } catch (SQLException rollbackEx) {
                    System.err.println("Error rolling back transaction: " + rollbackEx.getMessage());
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
    
    // Staff management methods
    public static boolean addStaff(String name, String username, String email, String password) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO staff_records (name, username, email, password) VALUES (?, ?, ?, ?)")) {
            
            stmt.setString(1, name);
            stmt.setString(2, username);
            stmt.setString(3, email);
            stmt.setString(4, password);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding staff: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public static java.util.List<Object[]> getAllStaff() {
        java.util.List<Object[]> staff = new ArrayList<>();
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT name, username, email, status, password FROM staff_records ORDER BY name")) {
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("status"),
                        rs.getString("password")
                    };
                    staff.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting staff: " + e.getMessage());
            e.printStackTrace();
        }
        return staff;
    }
    
    public static boolean removeStaff(String username) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM staff_records WHERE username = ?")) {
            
            stmt.setString(1, username);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error removing staff: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean resetStaffPassword(String username, String newPassword) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE staff_records SET password = ? WHERE username = ?")) {
            
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error resetting staff password: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Method to validate staff login credentials
    public static boolean validateStaffLogin(String username, String password) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT username, email, password FROM staff_records WHERE username = ? AND password = ? AND status = 'Active'")) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Set the current admin user when login is successful
                    currentAdminUser = new User(
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password")
                    );
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error validating staff login: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    // System settings methods
    public static String getSystemSetting(String settingKey) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT setting_value FROM system_settings WHERE setting_key = ?")) {
            
            stmt.setString(1, settingKey);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("setting_value");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting system setting: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public static boolean updateSystemSetting(String settingKey, String settingValue) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE system_settings SET setting_value = ? WHERE setting_key = ?")) {
            
            stmt.setString(1, settingValue);
            stmt.setString(2, settingKey);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating system setting: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Method to verify database connection and basic functionality
    private static void verifyDatabaseConnection(Connection conn) {
        try {
            // Test basic query
            try (PreparedStatement stmt = conn.prepareStatement("SELECT 1")) {
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Database connection verified successfully.");
                    }
                }
            }
            
            // Check if admin staff exists in staff_records, create if not
            try (PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT username FROM staff_records WHERE username = 'admin'")) {
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("Creating default admin staff...");
                        addStaff("Admin User", "admin", "admin@cephra.com", "admin123");
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error verifying database connection: " + e.getMessage());
        }
    }
    
    // Method to provide database initialization instructions
    public static void printDatabaseInitInstructions() {
        System.err.println("================================================");
        System.err.println("DATABASE INITIALIZATION REQUIRED");
        System.err.println("================================================");
        System.err.println("Some required database tables are missing.");
        System.err.println("Please follow these steps to initialize the database:");
        System.err.println("");
        System.err.println("1. Start XAMPP Control Panel");
        System.err.println("2. Start the MySQL service");
        System.err.println("3. Run one of the following commands:");
        System.err.println("   - Double-click: scripts/init-database.bat");
        System.err.println("   - Or manually: mysql -u root -p < src/main/resources/db/init.sql");
        System.err.println("");
        System.err.println("After initialization, restart the application.");
        System.err.println("================================================");
    }
    
    // Method to clean up orphaned queue tickets (tickets in queue but already in history)
    private static void cleanupOrphanedQueueTickets() {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE qt FROM queue_tickets qt " +
                     "INNER JOIN charging_history ch ON qt.ticket_id = ch.ticket_id " +
                     "WHERE qt.payment_status = 'Paid'")) {
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cleaned up " + rowsAffected + " orphaned queue tickets that were already in history.");
            }
        } catch (SQLException e) {
            System.err.println("Error cleaning up orphaned queue tickets: " + e.getMessage());
        }
    }
    
    // Method to clean up admin users from users table (they should be in staff_records)
    private static void cleanupAdminFromUsersTable() {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection()) {
            // Remove admin and testuser from users table
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM users WHERE username IN ('admin', 'testuser')")) {
                
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Cleaned up " + rowsAffected + " admin/testuser entries from users table.");
                }
            }
            
            // Also remove any admin users with old password (1234) from staff_records
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM staff_records WHERE username = 'admin' AND password = '1234'")) {
                
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Cleaned up " + rowsAffected + " admin entries with old password from staff_records.");
                }
            }
            
            // Ensure admin exists in staff_records with correct password
            try (PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT username FROM staff_records WHERE username = 'admin' AND password = 'admin123'")) {
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        // Admin doesn't exist with correct password, create it
                        try (PreparedStatement insertStmt = conn.prepareStatement(
                                "INSERT INTO staff_records (name, username, email, status, password) VALUES (?, ?, ?, ?, ?)")) {
                            
                            insertStmt.setString(1, "Admin User");
                            insertStmt.setString(2, "admin");
                            insertStmt.setString(3, "admin@cephra.com");
                            insertStmt.setString(4, "Active");
                            insertStmt.setString(5, "admin123");
                            
                            int rowsAffected = insertStmt.executeUpdate();
                            if (rowsAffected > 0) {
                                System.out.println("Created admin user with correct password in staff_records.");
                            }
                        }
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error cleaning up admin from users table: " + e.getMessage());
        }
    }
    
    // Helper method to add record to admin history
    private static void addToAdminHistory(String ticketId, String username, double totalAmount, String referenceNumber) {
        try {
            String adminUsername = getCurrentAdminUsername();
            if (adminUsername == null || adminUsername.trim().isEmpty()) {
                adminUsername = "Admin"; // Fallback if no admin logged in
            }
            
            Object[] historyRow = new Object[] {
                ticketId,
                username,
                String.format("%.2f", (totalAmount / 100.0) * 40.0), // kWh calculation
                String.format("%.2f", totalAmount),
                adminUsername,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a")),
                referenceNumber != null ? referenceNumber : ""
            };
            cephra.Admin.HistoryBridge.addRecord(historyRow);
        } catch (Throwable t) {
            // Ignore if HistoryBridge is not available
            System.out.println("Note: Could not add to admin history: " + t.getMessage());
        }
    }
    
    // Helper method to refresh Porsche screen
    private static void refreshPorscheScreen(String username) {
        try {
            SwingUtilities.invokeLater(() -> {
                Window[] windows = Window.getWindows();
                for (Window window : windows) {
                    if (window instanceof cephra.Frame.Phone) {
                        cephra.Frame.Phone phoneFrame = (cephra.Frame.Phone) window;
                        if (phoneFrame.isVisible()) {
                            Component[] components = phoneFrame.getContentPane().getComponents();
                            for (Component comp : components) {
                                if (comp instanceof cephra.Phone.PorscheTaycan) {
                                    cephra.Phone.PorscheTaycan porschePanel = (cephra.Phone.PorscheTaycan) comp;
                                    porschePanel.refreshBatteryDisplay();
                                    System.out.println("CephraDB: Refreshed Porsche screen to show 100% battery for user " + username);
                                    return;
                                }
                            }
                            
                            // If PorscheTaycan is not found in current components, try to find it recursively
                            try {
                                Component currentPanel = findPorscheTaycanPanel(phoneFrame.getContentPane());
                                if (currentPanel instanceof cephra.Phone.PorscheTaycan) {
                                    cephra.Phone.PorscheTaycan porschePanel = (cephra.Phone.PorscheTaycan) currentPanel;
                                    porschePanel.refreshBatteryDisplay();
                                    System.out.println("CephraDB: Refreshed Porsche screen (found recursively) to show 100% battery for user " + username);
                                    return;
                                }
                            } catch (Exception panelEx) {
                                System.err.println("CephraDB: Could not refresh current panel: " + panelEx.getMessage());
                            }
                        }
                    }
                }
                System.out.println("CephraDB: Porsche screen not found for refresh - user may not be on Porsche screen");
            });
        } catch (Exception e) {
            System.err.println("CephraDB: Error refreshing Porsche screen: " + e.getMessage());
        }
    }
    
    // Helper method to find PorscheTaycan panel recursively in a container
    private static Component findPorscheTaycanPanel(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof cephra.Phone.PorscheTaycan) {
                return comp;
            }
            if (comp instanceof Container) {
                Component found = findPorscheTaycanPanel((Container) comp);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
    
    // Method to validate and ensure database integrity
    public static void validateDatabaseIntegrity() {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection()) {
            System.out.println("Validating database integrity...");
            
            // Check if all required tables exist
            String[] requiredTables = {
                "users", "battery_levels", "active_tickets", "otp_codes",
                "queue_tickets", "charging_history", "staff_records", 
                "charging_bays", "payment_transactions", "system_settings",
                "wallet_balance", "wallet_transactions"
            };
            
            boolean allTablesExist = true;
            for (String tableName : requiredTables) {
                if (!tableExists(conn, tableName)) {
                    System.err.println("❌ Missing table: " + tableName);
                    allTablesExist = false;
                } else {
                    System.out.println("✅ Table exists: " + tableName);
                }
            }
            
            if (!allTablesExist) {
                printDatabaseInitInstructions();
                return;
            }
            
            // Check if charging bays are properly configured
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM charging_bays WHERE bay_type IN ('Fast', 'Normal')")) {
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int bayCount = rs.getInt(1);
                        System.out.println("✅ Found " + bayCount + " charging bays configured");
                        if (bayCount < 8) {
                            System.err.println("⚠️  Warning: Expected at least 8 charging bays, found " + bayCount);
                        }
                    }
                }
            }
            
            // Check if system settings are configured
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM system_settings")) {
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int settingCount = rs.getInt(1);
                        System.out.println("✅ Found " + settingCount + " system settings configured");
                    }
                }
            }
            
            // Check if there are any records in key tables
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM users")) {
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int userCount = rs.getInt(1);
                        System.out.println("✅ Found " + userCount + " users in database");
                    }
                }
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM charging_bays")) {
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int bayCount = rs.getInt(1);
                        System.out.println("✅ Found " + bayCount + " charging bays in database");
                    }
                }
            }
            
            System.out.println("✅ Database integrity validation completed successfully.");
            
        } catch (SQLException e) {
            System.err.println("❌ Error validating database integrity: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // ========================= WALLET METHODS =========================
    
    /**
     * Gets the current wallet balance for a user
     * @param username the username to get balance for
     * @return the current balance, or 0.00 if user not found
     */
    public static double getUserWalletBalance(String username) {
        if (username == null || username.trim().isEmpty()) {
            return 0.00;
        }
        
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT balance FROM wallet_balance WHERE username = ?")) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("balance");
                } else {
                    // Create wallet balance entry if it doesn't exist
                    createWalletBalanceEntry(username, 0.00);
                    return 0.00;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting user wallet balance: " + e.getMessage());
            e.printStackTrace();
            return 0.00;
        }
    }
    
    /**
     * Creates a wallet balance entry for a new user
     * @param username the username
     * @param initialBalance the initial balance (default 0.00)
     * @return true if successful
     */
    private static boolean createWalletBalanceEntry(String username, double initialBalance) {
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT IGNORE INTO wallet_balance (username, balance) VALUES (?, ?)")) {
            
            stmt.setString(1, username);
            stmt.setDouble(2, initialBalance);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating wallet balance entry: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Updates user wallet balance and adds transaction record
     * @param username the username
     * @param amount the amount to add (positive) or deduct (negative)
     * @param transactionType the type of transaction (TOP_UP, PAYMENT, REFUND)
     * @param description the transaction description
     * @param referenceId the reference ID (ticket ID for payments, transaction ID for topups)
     * @return true if successful
     */
    public static boolean updateWalletBalance(String username, double amount, String transactionType, 
                                            String description, String referenceId) {
        if (username == null || username.trim().isEmpty()) {
            System.err.println("Invalid username for wallet balance update");
            return false;
        }
        
        Connection conn = null;
        try {
            conn = cephra.db.DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // Get current balance
            double currentBalance = getUserWalletBalance(username);
            double newBalance = currentBalance + amount;
            
            if (newBalance < 0 && transactionType.equals("PAYMENT")) {
                System.err.println("Insufficient wallet balance for payment. Current: " + currentBalance + ", Required: " + Math.abs(amount));
                return false;
            }
            
            // Update wallet balance
            try (PreparedStatement updateStmt = conn.prepareStatement(
                    "UPDATE wallet_balance SET balance = ?, updated_at = CURRENT_TIMESTAMP WHERE username = ?")) {
                
                updateStmt.setDouble(1, newBalance);
                updateStmt.setString(2, username);
                
                int rowsUpdated = updateStmt.executeUpdate();
                if (rowsUpdated == 0) {
                    // Create wallet balance entry if it doesn't exist
                    try (PreparedStatement insertStmt = conn.prepareStatement(
                            "INSERT INTO wallet_balance (username, balance) VALUES (?, ?)")) {
                        insertStmt.setString(1, username);
                        insertStmt.setDouble(2, newBalance);
                        insertStmt.executeUpdate();
                    }
                }
            }
            
            // Add wallet transaction record
            try (PreparedStatement transStmt = conn.prepareStatement(
                    "INSERT INTO wallet_transactions (username, transaction_type, amount, previous_balance, new_balance, description, reference_id) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                
                transStmt.setString(1, username);
                transStmt.setString(2, transactionType);
                transStmt.setDouble(3, amount);
                transStmt.setDouble(4, currentBalance);
                transStmt.setDouble(5, newBalance);
                transStmt.setString(6, description);
                transStmt.setString(7, referenceId);
                
                transStmt.executeUpdate();
            }
            
            // Note: No automatic transaction limit - preserve all wallet history
            // The Wallet panel preview uses LIMIT 5 in query, but WalletHistory shows all
            
            conn.commit(); // Commit transaction
            System.out.println("Wallet balance updated successfully for " + username + ": " + currentBalance + " → " + newBalance);
            return true;
            
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback on error
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error rolling back wallet transaction: " + rollbackEx.getMessage());
            }
            System.err.println("Error updating wallet balance: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); // Reset auto-commit
                    conn.close();
                }
            } catch (SQLException closeEx) {
                System.err.println("Error closing connection: " + closeEx.getMessage());
            }
        }
    }
    
    // History retention: We no longer auto-delete wallet transactions here.
    // If retention is needed in the future, implement an archival strategy instead.
    
    /**
     * Gets ALL wallet transactions for a user (for WalletHistory panel)
     * @param username the username
     * @return list of all wallet transaction records
     */
    public static java.util.List<Object[]> getAllWalletTransactionHistory(String username) {
        java.util.List<Object[]> transactions = new ArrayList<>();
        
        if (username == null || username.trim().isEmpty()) {
            return transactions;
        }
        
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT transaction_type, amount, new_balance, description, reference_id, transaction_date " +
                     "FROM wallet_transactions WHERE username = ? ORDER BY transaction_date DESC")) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("transaction_type"),
                        rs.getDouble("amount"),
                        rs.getDouble("new_balance"),
                        rs.getString("description"),
                        rs.getString("reference_id"),
                        rs.getTimestamp("transaction_date")
                    };
                    transactions.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting all wallet transaction history: " + e.getMessage());
            e.printStackTrace();
        }
        
        return transactions;
    }
    
    /**
     * Gets the latest wallet transactions for a user (up to 5 for Wallet panel preview)
     * @param username the username
     * @return list of wallet transaction records
     */
    public static java.util.List<Object[]> getWalletTransactionHistory(String username) {
        java.util.List<Object[]> transactions = new ArrayList<>();
        
        if (username == null || username.trim().isEmpty()) {
            return transactions;
        }
        
        try (Connection conn = cephra.db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT transaction_type, amount, new_balance, description, reference_id, transaction_date " +
                     "FROM wallet_transactions WHERE username = ? ORDER BY transaction_date DESC LIMIT 5")) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("transaction_type"),
                        rs.getDouble("amount"),
                        rs.getDouble("new_balance"),
                        rs.getString("description"),
                        rs.getString("reference_id"),
                        rs.getTimestamp("transaction_date")
                    };
                    transactions.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting wallet transaction history: " + e.getMessage());
            e.printStackTrace();
        }
        
        return transactions;
    }
    
    /**
     * Checks if user has sufficient wallet balance for a payment
     * @param username the username
     * @param amount the amount required
     * @return true if balance is sufficient
     */
    public static boolean hasSufficientWalletBalance(String username, double amount) {
        double currentBalance = getUserWalletBalance(username);
        return currentBalance >= amount;
    }
    
    /**
     * Processes wallet payment for a ticket
     * @param username the username
     * @param ticketId the ticket ID
     * @param amount the payment amount
     * @return true if payment successful
     */
    public static boolean processWalletPayment(String username, String ticketId, double amount) {
        if (!hasSufficientWalletBalance(username, amount)) {
            return false;
        }
        
        String description = "Payment for ticket " + ticketId;
        return updateWalletBalance(username, -amount, "PAYMENT", description, ticketId);
    }
    
    /**
     * Processes wallet top-up
     * @param username the username
     * @param amount the top-up amount
     * @param method the top-up method (e.g., "Quick Amount A", "Custom Amount")
     * @return true if top-up successful
     */
    public static boolean processWalletTopUp(String username, double amount, String method) {
        String description = "Wallet top-up via " + method;
        String referenceId = "TOPUP_" + System.currentTimeMillis();
        return updateWalletBalance(username, amount, "TOP_UP", description, referenceId);
    }

}
