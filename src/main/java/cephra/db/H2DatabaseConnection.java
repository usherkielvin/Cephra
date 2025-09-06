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
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cephradb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    static {
        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
        }
    }
    
    /**
     * Initialize the H2 database and create tables
     */
    private static void initializeDatabase() {
        try {
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
                firstname VARCHAR(50) NOT NULL,
                lastname VARCHAR(50) NOT NULL,
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
                current_username VARCHAR(50),
                start_time TIMESTAMP,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """);
        
        // Waiting grid table
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS waiting_grid (
                id INT AUTO_INCREMENT PRIMARY KEY,
                slot_number INT NOT NULL UNIQUE,
                ticket_id VARCHAR(50),
                username VARCHAR(50),
                service_type VARCHAR(20),
                initial_battery_level INT,
                position_in_queue INT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """);
        
        // Charging grid table
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS charging_grid (
                id INT AUTO_INCREMENT PRIMARY KEY,
                bay_number VARCHAR(10) NOT NULL UNIQUE,
                ticket_id VARCHAR(50),
                username VARCHAR(50),
                service_type VARCHAR(20),
                initial_battery_level INT,
                start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
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
        // Insert all charging bays (1-8)
        String[] bayNumbers = {"Bay-1", "Bay-2", "Bay-3", "Bay-4", "Bay-5", "Bay-6", "Bay-7", "Bay-8"};
        String[] bayTypes = {"Fast", "Fast", "Fast", "Normal", "Normal", "Normal", "Normal", "Normal"};
        
        for (int i = 0; i < bayNumbers.length; i++) {
            stmt.execute(String.format("""
                INSERT INTO charging_bays (bay_number, bay_type, status) 
                SELECT '%s', '%s', 'Available'
                WHERE NOT EXISTS (SELECT 1 FROM charging_bays WHERE bay_number = '%s')
                """, bayNumbers[i], bayTypes[i], bayNumbers[i]));
        }
        
        // Insert waiting grid slots (1-10)
        for (int i = 1; i <= 10; i++) {
            stmt.execute(String.format("""
                INSERT INTO waiting_grid (slot_number, ticket_id, username, service_type, initial_battery_level, position_in_queue) 
                SELECT %d, NULL, NULL, NULL, NULL, NULL
                WHERE NOT EXISTS (SELECT 1 FROM waiting_grid WHERE slot_number = %d)
                """, i, i));
        }
        
        // Insert charging grid slots (Bay-1 to Bay-8)
        for (String bayNumber : bayNumbers) {
            stmt.execute(String.format("""
                INSERT INTO charging_grid (bay_number, ticket_id, username, service_type, initial_battery_level) 
                SELECT '%s', NULL, NULL, NULL, NULL
                WHERE NOT EXISTS (SELECT 1 FROM charging_grid WHERE bay_number = '%s')
                """, bayNumber, bayNumber));
        }
        
        System.out.println("✓ Initial data inserted: 8 charging bays, 10 waiting slots, 8 charging grid slots");
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
