package cephra;

import cephra.db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class CephraDB {

    // Inner class to represent a user
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
            // Database is initialized through the connection
            System.out.println("Cephra MySQL database connected successfully.");
        } catch (SQLException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
        }
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
                    
                    // Insert the new battery level
                    try (PreparedStatement insertStmt = conn.prepareStatement(
                            "INSERT INTO battery_levels (username, battery_level) VALUES (?, ?)")) {
                        
                        insertStmt.setString(1, username);
                        insertStmt.setInt(2, batteryLevel);
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
}