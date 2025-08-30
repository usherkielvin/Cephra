package cephra.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * H2 Database connection test utility for Cephra
 */
public class H2DatabaseTest {
    
    public static void main(String[] args) {
        System.out.println("Testing Cephra H2 Database Connection...");
        System.out.println("========================================");
        
        try {
            // Test basic connection
            System.out.println("1. Testing basic connection...");
            Connection conn = H2DatabaseConnection.getConnection();
            System.out.println("✓ H2 Database connection successful!");
            
            // Test if tables exist
            System.out.println("\n2. Testing database structure...");
            Statement stmt = conn.createStatement();
            
            // Check if tables exist
            ResultSet rs = stmt.executeQuery("SHOW TABLES");
            int tableCount = 0;
            while (rs.next()) {
                tableCount++;
                System.out.println("  - Table: " + rs.getString(1));
            }
            
            if (tableCount > 0) {
                System.out.println("✓ Found " + tableCount + " tables in database");
            } else {
                System.out.println("✗ No tables found");
            }
            
            // Test a simple query
            System.out.println("\n3. Testing sample query...");
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM users");
            if (rs.next()) {
                int userCount = rs.getInt("count");
                System.out.println("✓ Users table accessible - " + userCount + " users found");
            } else {
                System.out.println("✗ Could not query users table");
            }
            
            // Test staff records
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM staff_records");
            if (rs.next()) {
                int staffCount = rs.getInt("count");
                System.out.println("✓ Staff records table accessible - " + staffCount + " staff found");
            }
            
            conn.close();
            System.out.println("\n========================================");
            System.out.println("H2 Database test completed successfully!");
            System.out.println("Database file: cephra-db.mv.db");
            
        } catch (Exception e) {
            System.err.println("\n========================================");
            System.err.println("H2 Database connection failed!");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
