# Setting Up Cephra with XAMPP MySQL

This guide will help you set up the Cephra application with XAMPP's MySQL database.

## Prerequisites
- XAMPP installed on your system (includes MySQL)
- Java JDK 8 or higher

## Quick Setup Steps

### 1. Start XAMPP MySQL Service
1. Open XAMPP Control Panel
2. Start the MySQL service by clicking the "Start" button next to MySQL
3. Make sure the MySQL service is running (status should be green)

### 2. Initialize the Database
1. Run the `init-database.bat` file in the Cephra root directory
2. This will create the necessary database and tables in XAMPP's MySQL

### 3. Run the Application
1. Run the `run.bat` file to start the Cephra application
2. The application will now connect to the XAMPP MySQL database

## Troubleshooting

### If Database Initialization Fails
- Make sure XAMPP's MySQL service is running
- Check that port 3306 is not being used by another application
- If you've set a password for your XAMPP MySQL root user, update it in:
  `src\main\java\cephra\db\DatabaseConnection.java`

### If Application Fails to Connect
- Verify MySQL service is running in XAMPP Control Panel
- Check the database connection settings in `DatabaseConnection.java`
- Default settings (should work with standard XAMPP installation):
  - Host: localhost
  - Port: 3306
  - Database: cephradb
  - User: root
  - Password: (empty)