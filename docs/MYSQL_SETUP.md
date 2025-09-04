# MySQL Setup Guide for Cephra

This guide will help you set up MySQL to work with your Cephra application.

## Step 1: Install MySQL Server

1. Download MySQL Server from the [official website](https://dev.mysql.com/downloads/mysql/)
2. Run the installer and follow the setup wizard
3. Choose "Developer Default" installation type
4. Set a root password (remember this for later)
5. Complete the installation and make sure the MySQL service is running

## Step 2: Configure Your Database Connection

1. Open `src/main/java/cephra/db/DatabaseConnection.java`
2. Update the password field with your MySQL root password:
   ```java
   private static final String DB_PASSWORD = "your_password_here"; // Replace with your MySQL root password
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

1. Run the `scripts/init-database.bat` script:
   ```
   scripts/init-database.bat
   ```
2. When prompted, enter your MySQL root password

## Step 5: Run the Application

1. Run the application using the updated `scripts/run.bat` script:
   ```
   scripts/run.bat
   ```

## Troubleshooting

If you encounter any issues:

1. **Connection errors**: Make sure MySQL service is running and the password in `DatabaseConnection.java` is correct
2. **ClassNotFoundException**: Ensure the JAR files are in the `lib` folder
3. **Database not found**: Run the `scripts/init-database.bat` script again to create the database

## Manual Database Setup (if needed)

If the automatic setup doesn't work, you can manually set up the database:

1. Open MySQL Command Line Client
2. Enter your root password
3. Run the following commands:
   ```sql
   CREATE DATABASE IF NOT EXISTS cephradb;
   USE cephradb;
   
   CREATE TABLE IF NOT EXISTS users (
       id INT AUTO_INCREMENT PRIMARY KEY,
       username VARCHAR(50) NOT NULL UNIQUE,
       email VARCHAR(100) NOT NULL UNIQUE,
       password VARCHAR(100) NOT NULL,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );
   
   INSERT INTO users (username, email, password) VALUES 
   ('dizon', 'dizon@cephra.com', '1234'),
   ('testuser', 'test@cephra.com', '1234');
   ```