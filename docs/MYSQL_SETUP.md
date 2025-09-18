# MySQL Setup Guide for Cephra

This guide will help you set up MySQL to work with your Cephra application.

## Step 1: Install MySQL Server

### Option A: Standalone MySQL
1. Download MySQL Server from the [official website](https://dev.mysql.com/downloads/mysql/)
2. Run the installer and follow the setup wizard
3. Choose "Developer Default" installation type
4. Set a root password (remember this for later)
5. Complete the installation and make sure the MySQL service is running

### Option B: XAMPP (Recommended for Development)
1. Download XAMPP from [Apache Friends](https://www.apachefriends.org/)
2. Install XAMPP with default settings
3. Start the MySQL service from XAMPP Control Panel
4. Default MySQL root password is empty (blank)

## Step 2: Configure Your Database Connection

### Java Application Configuration
1. Open `src/main/java/cephra/Database/DatabaseConnection.java`
2. Update the connection settings:
   ```java
   private static final String DB_HOST = "localhost";
   private static final String DB_PORT = "3306";
   private static final String DB_NAME = "cephradb";
   private static final String DB_USER = "root";
   private static final String DB_PASSWORD = "your_password_here"; // Update this
   ```

### PHP Web Interface Configuration
1. Open `config/database.php`
2. Update the connection settings:
   ```php
   $this->host = 'localhost';
   $this->db_name = 'cephradb';
   $this->username = 'root';
   $this->password = 'your_password_here'; // Update this
   ```

## Step 3: Download Required JAR Files

1. Download the MySQL Connector/J JAR file:
   - Go to: https://dev.mysql.com/downloads/connector/j/
   - Download the Platform Independent ZIP file
   - Extract the ZIP and find the JAR file (e.g., `mysql-connector-j-8.0.33.jar`)
   - Copy this JAR file to the `lib` folder in your Cephra project

2. Download HikariCP JAR file:
   - Go to: https://github.com/brettwooldridge/HikariCP/releases
   - Download the latest JAR file (e.g., `HikariCP-5.0.1.jar`)
   - Copy this JAR file to the `lib` folder in your Cephra project

## Step 4: Initialize the Database

### Method 1: Using phpMyAdmin (XAMPP)
1. Open phpMyAdmin in your browser: `http://localhost/phpmyadmin/`
2. Create a new database named `cephradb`
3. Import the SQL file: `src/main/resources/db/init.sql`

### Method 2: Using MySQL Command Line
1. Open MySQL command line or terminal
2. Connect to MySQL: `mysql -u root -p`
3. Create database: `CREATE DATABASE cephradb;`
4. Use database: `USE cephradb;`
5. Import schema: `SOURCE src/main/resources/db/init.sql;`

### Method 3: Using MySQL Workbench
1. Open MySQL Workbench
2. Connect to your MySQL server
3. Create a new schema named `cephradb`
4. Import the SQL file: `src/main/resources/db/init.sql`

## Step 5: Verify Installation

### Test Database Connection
1. Run the Java application: `mvn exec:java -Dexec.mainClass="cephra.Launcher"`
2. Check if the application connects successfully
3. Test the web interface: `http://localhost/cephra/Appweb/User/`

### Verify Tables Created
Connect to your database and verify these tables exist:
- `users`
- `battery_levels`
- `active_tickets`
- `otp_codes`
- `queue_tickets`
- `charging_history`
- `staff_records`
- `charging_bays`
- `payment_transactions`
- `system_settings`

## Troubleshooting

### Connection Errors
- **Error: Access denied for user 'root'@'localhost'**
  - Check your MySQL root password
  - Update password in configuration files
  - For XAMPP, default password is empty (blank)

- **Error: Unknown database 'cephradb'**
  - Create the database first
  - Import the initialization SQL file
  - Check database name spelling in config files

### Service Issues
- **MySQL service won't start**
  - Check if port 3306 is already in use
  - Restart your computer
  - Reinstall MySQL/XAMPP

- **Connection timeout**
  - Check firewall settings
  - Verify MySQL service is running
  - Check network connectivity

### Build Issues
- **ClassNotFoundException: com.mysql.cj.jdbc.Driver**
  - Ensure MySQL Connector/J JAR is in the `lib` folder
  - Check Maven dependencies in `pom.xml`
  - Run `mvn clean install` to refresh dependencies

## Database Configuration Details

### Character Set
The database is configured with:
- **Character Set**: `utf8mb4`
- **Collation**: `utf8mb4_unicode_ci`
- **Engine**: `InnoDB`

### Connection Pool Settings
- **Maximum Pool Size**: 10 connections
- **Minimum Idle**: 5 connections
- **Connection Timeout**: 30 seconds
- **Idle Timeout**: 10 minutes

## Security Considerations

### Development Environment
- Use empty password for root (XAMPP default)
- Access restricted to localhost only
- No external network access

### Production Environment
- Set strong passwords for all users
- Create dedicated database users with limited privileges
- Enable SSL connections
- Regular security updates

## Performance Optimization

### MySQL Configuration
- Increase `innodb_buffer_pool_size` for better performance
- Enable query cache for frequently accessed data
- Optimize table indexes for common queries

### Connection Management
- Use connection pooling (HikariCP)
- Close connections properly
- Monitor connection usage

## Backup and Recovery

### Creating Backups
```sql
-- Full database backup
mysqldump -u root -p cephradb > cephra_backup.sql

-- Specific table backup
mysqldump -u root -p cephradb users queue_tickets > tables_backup.sql
```

### Restoring Backups
```sql
-- Restore full database
mysql -u root -p cephradb < cephra_backup.sql

-- Restore specific tables
mysql -u root -p cephradb < tables_backup.sql
```

## Additional Resources

- [MySQL Official Documentation](https://dev.mysql.com/doc/)
- [MySQL Connector/J Documentation](https://dev.mysql.com/doc/connector-j/8.0/en/)
- [HikariCP Documentation](https://github.com/brettwooldridge/HikariCP)
- [XAMPP Documentation](https://www.apachefriends.org/docs/)