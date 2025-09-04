package cephra.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Database connection test utility for Cephra
 */
public class DatabaseTest {
    
    public static void main(String[] args) {
        System.out.println("Testing Cephra Database Connection...");
        System.out.println("=====================================");
        
        try {
            // Test basic connection
            System.out.println("1. Testing basic connection...");
            Connection conn = DatabaseConnection.getConnection();
            System.out.println("✓ Database connection successful!");
            
            // Test if database exists and has tables
            System.out.println("\n2. Testing database structure...");
            Statement stmt = conn.createStatement();
            
            // Check if cephradb database exists
            ResultSet rs = stmt.executeQuery("SHOW DATABASES LIKE 'cephradb'");
            if (rs.next()) {
                System.out.println("✓ Database 'cephradb' exists");
            } else {
                System.out.println("✗ Database 'cephradb' not found - run scripts/init-database.bat");
            }
            
            // Check if tables exist
            rs = stmt.executeQuery("SHOW TABLES");
            int tableCount = 0;
            while (rs.next()) {
                tableCount++;
                System.out.println("  - Table: " + rs.getString(1));
            }
            
            if (tableCount > 0) {
                System.out.println("✓ Found " + tableCount + " tables in database");
            } else {
                System.out.println("✗ No tables found - run scripts/init-database.bat");
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
            
            conn.close();
            System.out.println("\n=====================================");
            System.out.println("Database test completed successfully!");
            
        } catch (Exception e) {
            System.err.println("\n=====================================");
            System.err.println("Database connection failed!");
            System.err.println("Error: " + e.getMessage());
            System.err.println("\nTroubleshooting steps:");
            System.err.println("1. Make sure XAMPP is running");
            System.err.println("2. Start MySQL service in XAMPP Control Panel");
            System.err.println("3. Check if port 3306 is available");
            System.err.println("4. Run scripts/init-database.bat to create database");
            e.printStackTrace();
        }
    }
}
