package cephra;

import cephra.db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class CephraDB {

    // Inner class to represent a user
    @SuppressWarnings("unused")
    private static class User {
        String username;
        String email;
        String password;

        public User(String username, String email, String password) {
            this.username = username;
            this.email = email;
            this.password = password;
        }
    }

    // Current logged-in user
    private static User currentUser;
    
    // Method to initialize the database
    public static void initializeDatabase() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if required tables exist
            if (!tableExists(conn, "queue_tickets")) {
                System.err.println("Warning: queue_tickets table does not exist. Please run the database initialization script.");
            }
            if (!tableExists(conn, "users")) {
                System.err.println("Warning: users table does not exist. Please run the database initialization script.");
            }
            
            // Clean up any existing duplicate battery level entries
            cleanupDuplicateBatteryLevels();
            
            // Clean up any orphaned queue tickets (tickets in queue but already in history)
            cleanupOrphanedQueueTickets();
            
            System.out.println("Cephra MySQL database connected successfully.");
        } catch (SQLException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
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

    // Method to check if the given credentials are valid
    public static boolean validateLogin(String username, String password) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT username, email, password FROM users WHERE username = ? AND password = ?")) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Set the current user when login is successful
                    currentUser = new User(
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
    
    // Method to get the current logged-in username
    public static String getCurrentUsername() {
        return currentUser != null ? currentUser.username : "";
    }
    
    // Method to get the current logged-in user's email
    public static String getCurrentEmail() {
        return currentUser != null ? currentUser.email : "";
    }

    // Method to add a new user to the database
    public static boolean addUser(String username, String email, String password) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO users (username, email, password) VALUES (?, ?, ?)")) {
            
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);
            
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
        try (Connection conn = DatabaseConnection.getConnection();
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
        try (Connection conn = DatabaseConnection.getConnection();
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
        try (Connection conn = DatabaseConnection.getConnection();
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
    
    // Method to generate and store a new 6-digit OTP
    public static String generateAndStoreOTP() {
        Random random = new Random();
        String generatedOTP = String.format("%06d", random.nextInt(1000000));
        
        // If user is logged in, store OTP in database
        if (currentUser != null) {
            try (Connection conn = DatabaseConnection.getConnection();
                 // Delete any existing OTP for this email
                 PreparedStatement deleteStmt = conn.prepareStatement(
                         "DELETE FROM otp_codes WHERE email = ?");
                 // Insert new OTP
                 PreparedStatement insertStmt = conn.prepareStatement(
                         "INSERT INTO otp_codes (email, otp_code) VALUES (?, ?)")) {
                
                deleteStmt.setString(1, currentUser.email);
                deleteStmt.executeUpdate();
                
                insertStmt.setString(1, currentUser.email);
                insertStmt.setString(2, generatedOTP);
                insertStmt.executeUpdate();
                
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
        if (currentUser == null) {
            return null;
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT otp_code FROM otp_codes WHERE email = ? AND expires_at > NOW()")) {
            
            stmt.setString(1, currentUser.email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("otp_code");
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
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT battery_level FROM battery_levels WHERE username = ?")) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("battery_level");
                } else {
                    // Generate random battery level (15-50%) for new users
                    Random random = new Random();
                    int batteryLevel = 15 + random.nextInt(36); // 15 to 50
                    
                    // Insert the new battery level using ON DUPLICATE KEY UPDATE to prevent duplicates
                    try (PreparedStatement insertStmt = conn.prepareStatement(
                            "INSERT INTO battery_levels (username, battery_level) VALUES (?, ?) " +
                            "ON DUPLICATE KEY UPDATE battery_level = ?")) {
                        
                        insertStmt.setString(1, username);
                        insertStmt.setInt(2, batteryLevel);
                        insertStmt.setInt(3, batteryLevel);
                        insertStmt.executeUpdate();
                    }
                    
                    return batteryLevel;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting battery level: " + e.getMessage());
            e.printStackTrace();
            // Return a default value in case of error
            return 15;
        }
    }
    
    public static void setUserBatteryLevel(String username, int batteryLevel) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO battery_levels (username, battery_level) VALUES (?, ?) " +
                     "ON DUPLICATE KEY UPDATE battery_level = ?")) {
            
            stmt.setString(1, username);
            stmt.setInt(2, batteryLevel);
            stmt.setInt(3, batteryLevel);
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error setting battery level: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void chargeUserBatteryToFull(String username) {
        setUserBatteryLevel(username, 100);
    }
    
    // Method to clean up duplicate battery level entries
    public static void cleanupDuplicateBatteryLevels() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Delete duplicate entries, keeping only the most recent one for each user
            String cleanupSQL = 
                "DELETE b1 FROM battery_levels b1 " +
                "INNER JOIN battery_levels b2 " +
                "WHERE b1.id > b2.id AND b1.username = b2.username";
            
            try (PreparedStatement stmt = conn.prepareStatement(cleanupSQL)) {
                int deletedRows = stmt.executeUpdate();
                if (deletedRows > 0) {
                    System.out.println("Cleaned up " + deletedRows + " duplicate battery level entries");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error cleaning up duplicate battery levels: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Active ticket management methods
    public static boolean hasActiveTicket(String username) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT ticket_id FROM active_tickets WHERE username = ?")) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // If there's a result, user has an active ticket
            }
        } catch (SQLException e) {
            System.err.println("Error checking active ticket: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public static void setActiveTicket(String username, String ticketId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO active_tickets (username, ticket_id) VALUES (?, ?) " +
                     "ON DUPLICATE KEY UPDATE ticket_id = ?")) {
            
            stmt.setString(1, username);
            stmt.setString(2, ticketId);
            stmt.setString(3, ticketId);
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error setting active ticket: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void clearActiveTicket(String username) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM active_tickets WHERE username = ?")) {
            
            stmt.setString(1, username);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error clearing active ticket: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static String getActiveTicket(String username) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT ticket_id FROM active_tickets WHERE username = ?")) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("ticket_id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting active ticket: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    // Queue ticket management methods
    public static boolean addQueueTicket(String ticketId, String username, String serviceType, 
                                       String status, String paymentStatus, int initialBatteryLevel) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // First, ensure the user exists in the users table
            if (!userExists(username)) {
                // Create a temporary user if they don't exist
                addUser(username, username + "@cephra.com", "temp123");
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
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE queue_tickets SET status = ? WHERE ticket_id = ?")) {
            
            stmt.setString(1, status);
            stmt.setString(2, ticketId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating queue ticket status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean updateQueueTicketPayment(String ticketId, String paymentStatus, String referenceNumber) {
        try (Connection conn = DatabaseConnection.getConnection();
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
        try (Connection conn = DatabaseConnection.getConnection();
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
    
    // Charging history methods
    public static boolean addChargingHistory(String ticketId, String username, String serviceType,
                                           int initialBatteryLevel, int chargingTimeMinutes, 
                                           double totalAmount, String referenceNumber) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO charging_history (ticket_id, username, service_type, " +
                     "initial_battery_level, charging_time_minutes, total_amount, reference_number) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            
            stmt.setString(1, ticketId);
            stmt.setString(2, username);
            stmt.setString(3, serviceType);
            stmt.setInt(4, initialBatteryLevel);
            stmt.setInt(5, chargingTimeMinutes);
            stmt.setDouble(6, totalAmount);
            stmt.setString(7, referenceNumber);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding charging history: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public static List<Object[]> getChargingHistoryForUser(String username) {
        List<Object[]> history = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
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
    
    public static List<Object[]> getAllChargingHistory() {
        List<Object[]> history = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
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
    
    public static List<Object[]> getAllQueueTickets() {
        List<Object[]> tickets = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
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
        try (Connection conn = DatabaseConnection.getConnection();
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
        try (Connection conn = DatabaseConnection.getConnection();
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
        try (Connection conn = DatabaseConnection.getConnection();
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
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // 1. Add to charging history
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO charging_history (ticket_id, username, service_type, " +
                    "initial_battery_level, charging_time_minutes, total_amount, reference_number) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                
                stmt.setString(1, ticketId);
                stmt.setString(2, username);
                stmt.setString(3, serviceType);
                stmt.setInt(4, initialBatteryLevel);
                stmt.setInt(5, chargingTimeMinutes);
                stmt.setDouble(6, totalAmount);
                stmt.setString(7, referenceNumber);
                
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected <= 0) {
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
                stmt.setString(4, paymentMethod);
                stmt.setString(5, referenceNumber);
                
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected <= 0) {
                    conn.rollback();
                    return false;
                }
            }
            
            // 3. Update queue ticket payment status
            try (PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE queue_tickets SET payment_status = ?, reference_number = ? WHERE ticket_id = ?")) {
                
                stmt.setString(1, "Paid");
                stmt.setString(2, referenceNumber);
                stmt.setString(3, ticketId);
                
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected <= 0) {
                    conn.rollback();
                    return false;
                }
            }
            
            // 4. Add to admin history (if HistoryBridge is available)
            try {
                // Get the actual admin username who is currently logged in
                String adminUsername = getCurrentUsername();
                if (adminUsername == null || adminUsername.trim().isEmpty()) {
                    adminUsername = "Admin"; // Fallback if no admin logged in
                }
                
                Object[] historyRow = new Object[] {
                    ticketId,
                    username,
                    String.format("%.2f", (totalAmount / 100.0) * 40.0), // kWh calculation
                    String.format("%.2f", totalAmount),
                    adminUsername, // Use actual admin username instead of hardcoded "Admin"
                    java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a")),
                    referenceNumber
                };
                cephra.Admin.HistoryBridge.addRecord(historyRow);
            } catch (Throwable t) {
                // Ignore if HistoryBridge is not available
                System.out.println("Note: Could not add to admin history: " + t.getMessage());
            }
            
            conn.commit(); // Commit transaction
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error processing payment transaction: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
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
        try (Connection conn = DatabaseConnection.getConnection();
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
    
    public static List<Object[]> getAllStaff() {
        List<Object[]> staff = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
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
    
    // System settings methods
    public static String getSystemSetting(String settingKey) {
        try (Connection conn = DatabaseConnection.getConnection();
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
        try (Connection conn = DatabaseConnection.getConnection();
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
    
    // Method to clean up orphaned queue tickets (tickets in queue but already in history)
    private static void cleanupOrphanedQueueTickets() {
        try (Connection conn = DatabaseConnection.getConnection();
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

}