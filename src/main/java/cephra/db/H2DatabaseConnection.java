package cephra.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * H2 Database connection manager for Cephra (alternative to MySQL)
 * Use this when XAMPP MySQL is not working
 */
public class H2DatabaseConnection {
    
    private static final String DB_URL = "jdbc:h2:./cephra-db;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    
    static {
        initializeDatabase();
    }
    
    /**
     * Initialize the H2 database and create tables
     */
    private static void initializeDatabase() {
        try {
            // Load H2 driver
            Class.forName("org.h2.Driver");
            
            // Create connection and tables
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 Statement stmt = conn.createStatement()) {
                
                // Create tables if they don't exist
                createTables(stmt);
                
                System.out.println("H2 Database initialized successfully");
            }
            
        } catch (Exception e) {
            System.err.println("Failed to initialize H2 database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Create all necessary tables
     */
    private static void createTables(Statement stmt) throws SQLException {
        // Users table
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS users (
                id INT AUTO_INCREMENT PRIMARY KEY,
                username VARCHAR(50) NOT NULL UNIQUE,
                email VARCHAR(100) NOT NULL UNIQUE,
                password VARCHAR(100) NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """);
        
        // Battery levels table
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS battery_levels (
                id INT AUTO_INCREMENT PRIMARY KEY,
                username VARCHAR(50) NOT NULL UNIQUE,
                battery_level INT NOT NULL DEFAULT 0,
                last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
            )
        """);
        
        // Queue tickets table
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS queue_tickets (
                id INT AUTO_INCREMENT PRIMARY KEY,
                ticket_id VARCHAR(50) NOT NULL UNIQUE,
                username VARCHAR(50) NOT NULL,
                service_type VARCHAR(50) NOT NULL,
                status VARCHAR(20) NOT NULL DEFAULT 'Pending',
                payment_status VARCHAR(20) NOT NULL DEFAULT '',
                reference_number VARCHAR(20),
                initial_battery_level INT NOT NULL,
                battery_capacity_kwh DECIMAL(5,2) NOT NULL DEFAULT 40.0,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
            )
        """);
        
        // Staff records table
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS staff_records (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                username VARCHAR(50) NOT NULL UNIQUE,
                email VARCHAR(100) NOT NULL UNIQUE,
                status VARCHAR(20) NOT NULL DEFAULT 'Active',
                password VARCHAR(100) NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """);
        
        // Charging bays table
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS charging_bays (
                id INT AUTO_INCREMENT PRIMARY KEY,
                bay_number VARCHAR(10) NOT NULL UNIQUE,
                bay_type VARCHAR(20) NOT NULL,
                status VARCHAR(20) NOT NULL DEFAULT 'Available',
                current_ticket_id VARCHAR(50),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """);
        
        // Insert initial data
        insertInitialData(stmt);
    }
    
    /**
     * Insert initial data
     */
    private static void insertInitialData(Statement stmt) throws SQLException {
        // Insert test user if not exists
        stmt.execute("""
            INSERT INTO users (username, email, password) 
            SELECT 'testuser', 'test@cephra.com', '1234'
            WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'testuser')
        """);
        
        // Insert admin staff if not exists
        stmt.execute("""
            INSERT INTO staff_records (name, username, email, status, password) 
            SELECT 'Admin User', 'admin', 'admin@cephra.com', 'Active', 'admin123'
            WHERE NOT EXISTS (SELECT 1 FROM staff_records WHERE username = 'admin')
        """);
        
        // Insert charging bays if not exist
        stmt.execute("""
            INSERT INTO charging_bays (bay_number, bay_type, status) 
            SELECT 'Bay-1', 'Fast', 'Available'
            WHERE NOT EXISTS (SELECT 1 FROM charging_bays WHERE bay_number = 'Bay-1')
        """);
        
        stmt.execute("""
            INSERT INTO charging_bays (bay_number, bay_type, status) 
            SELECT 'Bay-2', 'Fast', 'Available'
            WHERE NOT EXISTS (SELECT 1 FROM charging_bays WHERE bay_number = 'Bay-2')
        """);
    }
    
    /**
     * Get a database connection
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    /**
     * Close the database (not needed for H2 file-based database)
     */
    public static void closeDatabase() {
        // H2 file-based database doesn't need explicit closing
    }
}
