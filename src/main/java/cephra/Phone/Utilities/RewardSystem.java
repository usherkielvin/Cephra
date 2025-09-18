package cephra.Phone.Utilities;

import java.sql.*;
import java.util.*;


public class RewardSystem {
    
    // Reward system configuration
    private static final double POINTS_PER_PHP = 0.05; // 0.05 points per 1 PHP spent
    private static final int MIN_POINTS_EARNED = 1; // Minimum 1 point per transaction
    
    /**
     * Get user's current points balance
     * @param username The username to get points for
     * @return Current points balance, or 0 if user not found
     */
    public static int getUserPoints(String username) {
        if (username == null || username.trim().isEmpty()) {
            System.err.println("RewardSystem: Invalid username provided");
            return 0;
        }
        
        try (Connection conn = cephra.Database.DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.err.println("RewardSystem: Could not establish database connection");
                return 0;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT total_points FROM user_points WHERE username = ?")) {
                stmt.setString(1, username);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int points = rs.getInt("total_points");
                        // System.out.println("RewardSystem: Retrieved " + points + " points for user " + username);
                        return points;
                    } else {
                        // User not found in user_points table, create entry
                        createUserPointsEntry(username);
                        System.out.println("RewardSystem: Created new user points entry for user " + username);
                        return 0;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("RewardSystem: Error getting user points for " + username + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Add points to user's account (typically from payments)
     * @param username The username to add points to
     * @param pointsToAdd Number of points to add
     * @param description Description of why points were earned
     * @param referenceId Reference ID (e.g., ticket_id for payment-based points)
     * @return true if successful, false otherwise
     */
    public static boolean addPoints(String username, int pointsToAdd, String description, String referenceId) {
        if (username == null || username.trim().isEmpty() || pointsToAdd <= 0) {
            System.err.println("RewardSystem: Invalid parameters for adding points");
            return false;
        }
        
        try (Connection conn = cephra.Database.DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.err.println("RewardSystem: Could not establish database connection");
                return false;
            }
            
            conn.setAutoCommit(false); // Start transaction
            
            try {
                // Get current points and update
                int currentPoints = getUserPoints(username);
                int newPoints = currentPoints + pointsToAdd;
                
                // Update user_points table
                try (PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO user_points (username, total_points, lifetime_earned) " +
                        "VALUES (?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "total_points = total_points + ?, " +
                        "lifetime_earned = lifetime_earned + ?, " +
                        "last_updated = CURRENT_TIMESTAMP")) {
                    
                    stmt.setString(1, username);
                    stmt.setInt(2, pointsToAdd);
                    stmt.setInt(3, pointsToAdd);
                    stmt.setInt(4, pointsToAdd);
                    stmt.setInt(5, pointsToAdd);
                    
                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected <= 0) {
                        System.err.println("RewardSystem: Failed to update user points");
                        conn.rollback();
                        return false;
                    }
                }
                
                // Add transaction record
                try (PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO reward_transactions " +
                        "(username, transaction_type, points_change, total_points_after, description, reference_id) " +
                        "VALUES (?, 'EARNED', ?, ?, ?, ?)")) {
                    
                    stmt.setString(1, username);
                    stmt.setInt(2, pointsToAdd);
                    stmt.setInt(3, newPoints);
                    stmt.setString(4, description);
                    stmt.setString(5, referenceId);
                    
                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected <= 0) {
                        System.err.println("RewardSystem: Failed to record reward transaction");
                        conn.rollback();
                        return false;
                    }
                }
                
                conn.commit();
                System.out.println("RewardSystem: Successfully added " + pointsToAdd + " points to " + username + 
                                 ". New total: " + newPoints);
                return true;
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("RewardSystem: Error adding points to " + username + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Add points based on payment amount (0.05 points per PHP)
     * @param username The username to add points to
     * @param paymentAmount Amount paid in PHP
     * @param ticketId Ticket ID for reference
     * @return true if successful, false otherwise
     */
    public static boolean addPointsForPayment(String username, double paymentAmount, String ticketId) {
        if (paymentAmount <= 0) {
            System.out.println("RewardSystem: No points to add for zero payment amount");
            return true; // Not an error, just no points earned
        }
        
        double pointsEarnedDecimal = paymentAmount * POINTS_PER_PHP;
        int pointsEarned = Math.max((int) Math.round(pointsEarnedDecimal), MIN_POINTS_EARNED);
        
        String description = "Payment reward: ₱" + String.format("%.2f", paymentAmount) + 
                           " @ " + POINTS_PER_PHP + " points per PHP";
        
        boolean success = addPoints(username, pointsEarned, description, ticketId);
        
        if (success && pointsEarned > 0) {
            // Show notification to user
            javax.swing.SwingUtilities.invokeLater(() -> {
                javax.swing.JOptionPane.showMessageDialog(null, 
                    "You earned " + pointsEarned + " points for your ₱" + String.format("%.2f", paymentAmount) + " payment!\n" +
                    "(Rate: " + POINTS_PER_PHP + " points per PHP)",
                    "Points Earned", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            });
        }
        
        return success;
    }
    
    /**
     * Spend points (for purchases)
     * @param username The username to spend points from
     * @param pointsToSpend Number of points to spend
     * @param description Description of purchase
     * @param itemId Item ID for reference
     * @return true if successful, false if insufficient points
     */
    public static boolean spendPoints(String username, int pointsToSpend, String description, String itemId) {
        if (username == null || username.trim().isEmpty() || pointsToSpend <= 0) {
            System.err.println("RewardSystem: Invalid parameters for spending points");
            return false;
        }
        
        try (Connection conn = cephra.Database.DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.err.println("RewardSystem: Could not establish database connection");
                return false;
            }
            
            conn.setAutoCommit(false); // Start transaction
            
            try {
                // Check current points
                int currentPoints = getUserPoints(username);
                if (currentPoints < pointsToSpend) {
                    System.out.println("RewardSystem: Insufficient points for " + username + 
                                     ". Required: " + pointsToSpend + ", Available: " + currentPoints);
                    conn.rollback();
                    return false;
                }
                
                int newPoints = currentPoints - pointsToSpend;
                
                // Update user_points table
                try (PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE user_points SET " +
                        "total_points = total_points - ?, " +
                        "lifetime_spent = lifetime_spent + ?, " +
                        "updated_at = CURRENT_TIMESTAMP " +
                        "WHERE username = ?")) {
                    
                    stmt.setInt(1, pointsToSpend);
                    stmt.setInt(2, pointsToSpend);
                    stmt.setString(3, username);
                    
                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected <= 0) {
                        System.err.println("RewardSystem: Failed to update user points for spending");
                        conn.rollback();
                        return false;
                    }
                }
                
                // Add transaction record
                try (PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO reward_transactions " +
                        "(username, transaction_type, points_change, total_points_after, description, reference_id) " +
                        "VALUES (?, 'SPENT', ?, ?, ?, ?)")) {
                    
                    stmt.setString(1, username);
                    stmt.setInt(2, -pointsToSpend); // Negative for spending
                    stmt.setInt(3, newPoints);
                    stmt.setString(4, description);
                    stmt.setString(5, itemId);
                    
                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected <= 0) {
                        System.err.println("RewardSystem: Failed to record spending transaction");
                        conn.rollback();
                        return false;
                    }
                }
                
                conn.commit();
                System.out.println("RewardSystem: Successfully spent " + pointsToSpend + " points from " + username + 
                                 ". New total: " + newPoints);
                return true;
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("RewardSystem: Error spending points from " + username + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Get user's reward transaction history
     * @param username The username to get history for
     * @param limit Maximum number of transactions to return (0 for all)
     * @return List of transaction records
     */
    public static List<Map<String, Object>> getTransactionHistory(String username, int limit) {
        List<Map<String, Object>> transactions = new ArrayList<>();
        
        if (username == null || username.trim().isEmpty()) {
            System.err.println("RewardSystem: Invalid username for transaction history");
            return transactions;
        }
        
        try (Connection conn = cephra.Database.DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.err.println("RewardSystem: Could not establish database connection");
                return transactions;
            }
            
            String query = "SELECT * FROM reward_transactions WHERE username = ? ORDER BY transaction_date DESC";
            if (limit > 0) {
                query += " LIMIT " + limit;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> transaction = new HashMap<>();
                        transaction.put("id", rs.getInt("id"));
                        transaction.put("transaction_type", rs.getString("transaction_type"));
                        transaction.put("points_change", rs.getInt("points_change"));
                        transaction.put("total_points_after", rs.getInt("total_points_after"));
                        transaction.put("description", rs.getString("description"));
                        transaction.put("reference_id", rs.getString("reference_id"));
                        transaction.put("transaction_date", rs.getTimestamp("transaction_date"));
                        
                        transactions.add(transaction);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("RewardSystem: Error getting transaction history for " + username + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        return transactions;
    }
    
    /**
     * Create a new wallet balance entry if it doesn't exist
     * @param username The username to create entry for
     * @return true if successful, false otherwise
     */
    private static boolean createUserPointsEntry(String username) {
        try (Connection conn = cephra.Database.DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.err.println("RewardSystem: Could not establish database connection");
                return false;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO user_points (username, total_points, lifetime_earned, lifetime_spent) " +
                    "VALUES (?, 0, 0, 0)")) {
                
                stmt.setString(1, username);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.err.println("RewardSystem: Error creating user points entry for " + username + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Get reward system statistics for a user
     * @param username The username to get stats for
     * @return Map containing user statistics
     */
    public static Map<String, Object> getUserStats(String username) {
        Map<String, Object> stats = new HashMap<>();
        
        if (username == null || username.trim().isEmpty()) {
            System.err.println("RewardSystem: Invalid username for user stats");
            return stats;
        }
        
        try (Connection conn = cephra.Database.DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.err.println("RewardSystem: Could not establish database connection");
                return stats;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT points, created_at " +
                    "FROM wallet_balance WHERE username = ?")) {
                
                stmt.setString(1, username);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        stats.put("total_points", rs.getInt("points"));
                        stats.put("member_since", rs.getTimestamp("created_at"));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("RewardSystem: Error getting user stats for " + username + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        return stats;
    }
}
