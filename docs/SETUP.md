# Cephra Setup Guide

This guide provides quick setup instructions for the Cephra EV charging management system.

## 🚀 Quick Setup

### 1. Database Setup
- Create MySQL database named `cephradb`
- Import the SQL from `src/main/resources/db/init.sql`
- Update database credentials in configuration files if needed

### 2. Java Application Setup
- Ensure Java JDK 24+ is installed
- Run `mvn clean compile` to build the project
- Launch with `mvn exec:java -Dexec.mainClass="cephra.Launcher"`

### 3. Web Interface Setup
- Place `Appweb/` folder in your web server directory (e.g., `htdocs/cephra/`)
- Ensure PHP and MySQL are running
- Update database credentials in `config/database.php` if needed

### 4. Test the System
- **Java Application**: Launch the main application
- **Web Interface**: Open `http://localhost/cephra/Appweb/User/` in your browser
- **Test Login**: Use `dizon` / `123` for testing

## 📁 File Structure
```
cephra/
├── src/main/java/cephra/          # Java source code
├── Appweb/                        # Web interface
│   ├── User/                      # Customer interface
│   ├── Admin/                     # Admin interface
│   └── Monitor/                   # Display monitor
├── config/                        # Database configuration
├── docs/                          # Documentation
└── pom.xml                        # Maven configuration
```

## 🔧 Configuration Files

### Database Connection (Java)
```java
// src/main/java/cephra/Database/DatabaseConnection.java
private static final String DB_HOST = "localhost";
private static final String DB_PORT = "3306";
private static final String DB_NAME = "cephradb";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "";
```

### Database Connection (PHP)
```php
// config/database.php
$this->host = 'localhost';
$this->db_name = 'cephradb';
$this->username = 'root';
$this->password = '';
```

## 🌐 Web Interface Access Points

- **Customer Interface**: `http://localhost/cephra/Appweb/User/`
- **Admin Interface**: `http://localhost/cephra/Appweb/Admin/`
- **Monitor Display**: `http://localhost/cephra/Appweb/Monitor/`

## 📱 Features

### Java Application
- ✅ Admin panel for station management
- ✅ Customer mobile interface
- ✅ Display monitor for public viewing
- ✅ Queue management and tracking
- ✅ Payment processing
- ✅ Staff management

### Web Interface
- ✅ Mobile-responsive design
- ✅ Real-time queue updates
- ✅ User authentication
- ✅ Ticket creation and management
- ✅ Fast/Normal charging options
- ✅ Cross-platform compatibility

## 🗄️ Database Tables

The system uses the following main tables:
- `users` - User accounts and profiles
- `queue_tickets` - Charging queue management
- `charging_history` - Transaction records
- `battery_levels` - User battery tracking
- `staff_records` - Employee management
- `charging_bays` - Station bay management
- `payment_transactions` - Payment records
- `system_settings` - Configuration settings

## 🔍 Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Ensure MySQL service is running
   - Check database credentials in config files
   - Verify database `cephradb` exists

2. **Java Application Won't Start**
   - Check Java version (JDK 24+ required)
   - Run `mvn clean compile` to rebuild
   - Verify database connection settings

3. **Web Interface Not Loading**
   - Ensure web server (Apache/Nginx) is running
   - Check PHP is enabled
   - Verify file permissions

4. **Build Errors**
   - Run `mvn clean install` to clean build
   - Check Maven version (3.11.0+ required)
   - Verify all dependencies are available

## 📚 Additional Documentation

- [Main README](README.md) - Complete project overview
- [MySQL Setup](MYSQL_SETUP.md) - Detailed database setup
- [XAMPP Setup](XAMPP_SETUP.md) - XAMPP-specific setup
- [Testing Guide](TESTING.md) - Testing procedures

## 🆘 Support

For additional help:
- Check the troubleshooting section above
- Review the detailed documentation in `docs/` folder
- Create an issue in the project repository