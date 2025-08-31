# 🔋 Cephra - EV Charging Queue Management System

A comprehensive Java-based Electric Vehicle (EV) charging station queue management system with multi-interface support for administrators, customers, and display monitors.

![Java](https://img.shields.io/badge/Java-21-orange)
![Maven](https://img.shields.io/badge/Maven-3.11.0-blue)
![Swing](https://img.shields.io/badge/Swing-GUI-green)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [System Architecture](#system-architecture)
- [Screenshots](#screenshots)
- [Installation](#installation)
- [Usage](#usage)
- [Configuration](#configuration)
- [Project Structure](#project-structure)
- [Technologies Used](#technologies-used)
- [Recent Updates](#recent-updates)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

Cephra is a modern EV charging station management system designed to streamline the queue process for electric vehicle charging. The system provides three distinct interfaces:

- **Admin Panel**: Complete management interface for station operators
- **Customer Mobile Interface**: User-friendly mobile app for customers
- **Display Monitor**: Public display showing queue status and information

## ✨ Features

### 🔧 Admin Panel Features
- **Dashboard Management**: Real-time overview of charging stations with configurable pricing
- **Queue Management**: Monitor and manage customer queues with status tracking
- **Staff Records**: Employee management with password reset capabilities
- **History Tracking**: Complete transaction and service history with detailed analytics
- **Bay Management**: Individual charging bay control and monitoring
- **Business Analytics**: Performance metrics and reporting
- **Payment Processing**: Integrated payment handling with multiple methods

### 📱 Customer Mobile Interface
- **User Registration**: Simple account creation with email verification
- **Queue Joining**: Easy queue entry with real-time updates and notifications
- **Service Selection**: Choose from available charging services (Fast/Normal)
- **Profile Management**: Personal information and battery level tracking
- **History View**: Personal charging history and digital receipts
- **QR Code Linking**: Quick station connection via QR codes
- **Battery Monitoring**: Real-time battery level display and management

### 📺 Display Monitor
- **Queue Display**: Real-time queue status and updates
- **Station Information**: Current availability and wait times
- **Public Announcements**: Important notifications and updates

## 🏗️ System Architecture

The application follows a modular architecture with three main components:

```
Cephra/
├── Admin Panel (1000x750)     # Management interface
├── Customer Mobile (350x750)   # Mobile app interface  
└── Display Monitor (1000x750)  # Public display interface
```

### Database Architecture
- **MySQL Database**: Robust data persistence with transaction support
- **Real-time Updates**: Live synchronization between all interfaces
- **Data Integrity**: Comprehensive error handling and validation

## 📸 Screenshots

*[Screenshots will be added here]*

## 🚀 Installation

### Prerequisites

- **Java 21** or higher
- **Maven 3.6+** for building
- **MySQL 8.0+** for database
- **XAMPP** (recommended for easy MySQL setup)

### Database Setup

#### Option 1: Using XAMPP (Recommended)
1. **Install XAMPP**
   - Download from [XAMPP official website](https://www.apachefriends.org/)
   - Install and start MySQL service

2. **Initialize Database**
   ```bash
   # Run the database initialization script
   init-database.bat
   ```

#### Option 2: Manual MySQL Setup
1. **Install MySQL Server**
   - Download from [MySQL official website](https://dev.mysql.com/downloads/mysql/)
   - Create database: `cephradb`
   - Default credentials: username `root` with empty password

2. **Initialize Schema**
   ```bash
   # Run the SQL script manually
   mysql -u root -p < src/main/resources/db/init.sql
   ```

### Quick Start

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/cephra.git
   cd cephra
   ```

2. **Setup Database**
   ```bash
   # Initialize database (if using XAMPP)
   init-database.bat
   
   # Or test database connection
   test-database-connection.bat
   ```

3. **Build and Run**
   ```bash
   # Using Maven
   mvn clean compile
   mvn exec:java -Dexec.mainClass="cephra.Launcher"
   
   # Or using the provided batch files
   run.bat
   ```

### Alternative Installation Methods

#### Using Simple Compilation
```bash
# Simple compilation without Maven
run-simple.bat
```

#### Using NetBeans IDE
1. Open NetBeans IDE
2. Select `File` → `Open Project`
3. Navigate to the Cephra project folder
4. Open the project and run `Launcher.java`

#### Using JAR File
```bash
# Build JAR
mvn clean package

# Run JAR
java -jar target/cephra-1.0-SNAPSHOT.jar
```

## 🎮 Usage

### Starting the Application

The application launches three interfaces simultaneously:

1. **Admin Panel**: Appears on the right side of the screen
2. **Display Monitor**: Appears in the top-left corner
3. **Customer Mobile**: Appears centered on screen

### Admin Panel Usage
- **Login**: Use default credentials (admin/admin123)
- **Dashboard**: Configure pricing and view system status
- **Queue**: Manage customer tickets and process payments
- **History**: View transaction history and analytics
- **Staff**: Manage employee accounts and permissions

### Customer Mobile Usage
- **Register**: Create new account with email verification
- **Join Queue**: Select service type and join the queue
- **Monitor Status**: Track your position in the queue
- **View History**: Access charging history and receipts

### Navigation
- **Escape Key**: Exit the application
- **Mouse Drag**: Move undecorated windows
- **Button Navigation**: Use on-screen buttons for interface switching

## ⚙️ Configuration

### Database Configuration
Edit `src/main/java/cephra/db/DatabaseConnection.java` to modify:
- Database URL
- Username and password
- Connection pool settings

### Window Positioning
The application automatically positions windows:
- Admin panel: Right side of screen
- Monitor: Top-left corner
- Mobile interface: Center of screen

### Pricing Configuration
Configure charging rates in the Admin Dashboard:
- Minimum fee settings
- Rate per hour (RPH)
- Service type pricing

## 📁 Project Structure

```
Cephra/
├── src/
│   └── main/
│       ├── java/
│       │   └── cephra/
│       │       ├── Admin/           # Admin panel components
│       │       │   ├── Dashboard.java
│       │       │   ├── Queue.java
│       │       │   ├── History.java
│       │       │   ├── Bay.java
│       │       │   ├── StaffRecord.java
│       │       │   ├── Login.java
│       │       │   └── AdminRegister.java
│       │       ├── Phone/           # Mobile interface components
│       │       │   ├── home.java
│       │       │   ├── Register.java
│       │       │   ├── Profile.java
│       │       │   ├── serviceoffered.java
│       │       │   ├── phonehistory.java
│       │       │   ├── PorscheTaycan.java
│       │       │   └── Transition.java
│       │       ├── Frame/           # Main frame containers
│       │       │   ├── Admin.java
│       │       │   ├── Monitor.java
│       │       │   └── Phone.java
│       │       ├── db/              # Database components
│       │       │   ├── DatabaseConnection.java
│       │       │   └── DatabaseTest.java
│       │       ├── CephraDB.java    # Database operations
│       │       └── Launcher.java    # Application entry point
│       └── resources/
│           ├── db/
│           │   └── init.sql         # Database schema
│           └── cephra/
│               └── Photos/          # Application images and icons
├── target/                          # Compiled classes and JAR
├── pom.xml                          # Maven configuration
├── *.bat                           # Windows batch scripts
├── *.md                            # Documentation files
└── README.md                        # This file
```

## 🛠️ Technologies Used

- **Java 21**: Core programming language
- **Java Swing**: GUI framework
- **Maven**: Build and dependency management
- **MySQL**: Database management system
- **XAMPP**: Development environment
- **Git**: Version control

## 🆕 Recent Updates

### Code Quality Improvements
- ✅ **Fixed unused lambda parameters** across all components
- ✅ **Removed unused variables** and dead code
- ✅ **Improved code organization** and structure
- ✅ **Enhanced error handling** and validation

### Project Cleanup
- ✅ **Removed compiled .class files** from source control
- ✅ **Cleaned IDE-specific files** and temporary files
- ✅ **Optimized project structure** for better maintainability
- ✅ **Updated documentation** and setup instructions

### Database Enhancements
- ✅ **Improved database operations** with better error handling
- ✅ **Enhanced transaction management** for data integrity
- ✅ **Added comprehensive logging** for debugging
- ✅ **Optimized query performance** and connection management

## 🔧 Development

### Building from Source

```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Create JAR
mvn package

# Install to local repository
mvn install
```

### Code Style

- Follow Java naming conventions
- Use meaningful variable and method names
- Add comments for complex logic
- Maintain consistent indentation
- Use underscore `_` for unused lambda parameters

### Adding New Features

1. Create new Java classes in appropriate packages
2. Design forms using NetBeans GUI designer
3. Update navigation logic in existing panels
4. Test thoroughly before committing
5. Update documentation for new features

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/AmazingFeature`)
3. **Commit** your changes (`git commit -m 'Add some AmazingFeature'`)
4. **Push** to the branch (`git push origin feature/AmazingFeature`)
5. **Open** a Pull Request

### Contribution Guidelines

- Ensure code compiles without errors
- Test all functionality before submitting
- Update documentation for new features
- Follow existing code style and patterns
- Fix any unused parameter warnings
- Maintain clean project structure

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

For support and questions:

- **Issues**: Create an issue on GitHub
- **Email**: [your-email@example.com]
- **Documentation**: Check the project wiki

## 🙏 Acknowledgments

- **NetBeans Community** for the excellent IDE
- **Java Swing Team** for the GUI framework
- **Maven Community** for build tools
- **MySQL Community** for database support
- **Contributors** who help improve the project

---

**Made with ❤️ for the EV charging community**

*Last updated: December 2024*
