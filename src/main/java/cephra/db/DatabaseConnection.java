package cephra.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Database connection manager for Cephra using HikariCP connection pool
 */
public class DatabaseConnection {
    
    private static HikariDataSource dataSource;
    
    // Database configuration for XAMPP MySQL
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "cephradb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // XAMPP MySQL default has no password
    
    static {
        initializeDataSource();
    }
    
    /**
     * Initialize the database connection pool
     */
    private static void initializeDataSource() {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME);
            config.setUsername(DB_USER);
            config.setPassword(DB_PASSWORD);
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            
            // Connection pool settings
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setConnectionTimeout(30000);
            
            // Auto-create database if it doesn't exist
            config.addDataSourceProperty("createDatabaseIfNotExist", "true");
            config.addDataSourceProperty("useSSL", "false");
            config.addDataSourceProperty("serverTimezone", "UTC");
            
            dataSource = new HikariDataSource(config);
            
            System.out.println("Database connection pool initialized successfully");
        } catch (Exception e) {
            System.err.println("Failed to initialize database connection pool: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Get a connection from the pool
     * @return Connection object
     * @throws SQLException if connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            initializeDataSource();
        }
        return dataSource.getConnection();
    }
    
    /**
     * Close the connection pool
     */
    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}