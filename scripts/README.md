# Cephra Scripts

This folder contains all the batch scripts for managing the Cephra EV Charging Queue Management System.

## 🚀 Application Scripts

### Main Application
- **`run.bat`** - Main application launcher (starts all three interfaces)
- **`run-maven.bat`** - Run application via Maven
- **`run-simple.bat`** - Simple run script
- **`run-init.bat`** - Initialize and run application

## 🗄️ Database Scripts

### Database Management
- **`init-database.bat`** - Initialize MySQL database with schema
- **`backup-database.bat`** - Create database backup
- **`restore-database.bat`** - Restore database from backup
- **`test-database.bat`** - Test database connection
- **`test-database-connection.bat`** - Advanced connection testing

### MySQL Setup
- **`setup-mysql-driver.bat`** - Install MySQL JDBC drivers
- **`check-mysql-services.bat`** - Check MySQL service status
- **`fix-mysql.bat`** - Fix common MySQL issues

### Alternative Database
- **`setup-h2-database.bat`** - Setup H2 database (MySQL alternative)

## 🧹 Development Scripts

### Build Management
- **`clean-classes.bat`** - Clean compiled Java classes

## 📋 Usage Instructions

### First Time Setup
1. Run `setup-mysql-driver.bat` to install MySQL drivers
2. Run `init-database.bat` to create the database
3. Run `run.bat` to start the application

### Daily Usage
- Use `run.bat` to start the application
- Use `backup-database.bat` for regular backups

### Troubleshooting
- Use `check-mysql-services.bat` if database connection fails
- Use `fix-mysql.bat` for common MySQL issues
- Use `test-database-connection.bat` to diagnose connection problems

## ⚠️ Important Notes

- All scripts assume you're running from the project root directory
- MySQL must be installed and running for database scripts to work
- Some scripts require administrator privileges
- Always backup your database before running restore scripts

## 🔧 Customization

You can modify these scripts to:
- Change database connection parameters
- Add additional setup steps
- Customize backup locations
- Modify application startup parameters
