# 🔋 Cephra - EV Charging Queue Management System

A comprehensive Java-based Electric Vehicle (EV) charging station queue management system with **web-based phone interface improvements** for enhanced mobile accessibility.

![Java](https://img.shields.io/badge/Java-24-orange)
![Maven](https://img.shields.io/badge/Maven-3.11.0-blue)
![Swing](https://img.shields.io/badge/Swing-GUI-green)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue)
![Web](https://img.shields.io/badge/Web-PHP%20%2B%20HTML5-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [System Architecture](#system-architecture)
- [Installation](#installation)
- [Usage](#usage)
- [Configuration](#configuration)
- [Project Structure](#project-structure)
- [Technologies Used](#technologies-used)
- [Web Interface (Phone Improvement)](#web-interface-phone-improvement)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

Cephra is a modern EV charging station management system built in **Java** with three main interfaces:

- **Admin Panel**: Complete management interface for station operators
- **Customer Mobile Interface**: User-friendly mobile app for customers
- **Display Monitor**: Public display showing queue status and information

**🆕 NEW: Web-Based Phone Interface** - Enhanced mobile accessibility through modern web technologies

## ✨ Features

### 🔧 Admin Panel Features (Java)
- **Dashboard Management**: Real-time overview of charging stations with configurable pricing
- **Queue Management**: Monitor and manage customer queues with status tracking
- **Staff Records**: Employee management with password reset capabilities
- **History Tracking**: Complete transaction and service history with detailed analytics
- **Bay Management**: Individual charging bay control and monitoring
- **Business Analytics**: Performance metrics and reporting
- **Payment Processing**: Integrated payment handling with multiple methods

### 📱 Customer Mobile Interface (Java + Web Enhancement)
- **User Registration**: Simple account creation with email verification
- **Queue Joining**: Easy queue entry with real-time updates and notifications
- **Service Selection**: Choose from available charging services (Fast/Normal)
- **Profile Management**: Personal information and battery level tracking
- **History View**: Personal charging history and digital receipts
- **QR Code Linking**: Quick station connection via QR codes
- **Battery Monitoring**: Real-time battery level display and management

### 📺 Display Monitor (Java)
- **Queue Display**: Real-time queue status and updates
- **Station Information**: Current availability and wait times
- **Public Announcements**: Important notifications and updates

### 🌐 Web Interface Improvements (Phone Enhancement)
- **Mobile-First Design** - Responsive web interface for phones
- **Real-Time Updates** - Live queue updates every 5 seconds
- **Cross-Platform Access** - Works on any device with a web browser
- **Enhanced User Experience** - Modern UI with animations and gradients

## 🏗️ System Architecture

The application follows a **dual-architecture approach**:

### **Primary System (Java)**
```
Cephra/
├── Admin Panel (1000x750)     # Java Swing management interface
├── Customer Mobile (350x750)   # Java Swing mobile app interface  
└── Display Monitor (1000x750)  # Java Swing public display interface
```

### **Web Enhancement (Phone Interface)**
```
cephra/ (XAMPP Web Server)
├── mobileweb/                  # Web-based mobile interface
│   ├── index.php              # Main phone interface
│   ├── styles.css             # Modern styling
│   └── script.js              # Interactive functionality
├── api/                       # PHP API backend
│   └── mobile.php             # API endpoints
└── config/                    # Database configuration
    └── database.php           # MySQL connection
```

### **Database Architecture**
- **MySQL Database**: Robust data persistence with transaction support
- **Real-time Updates**: Live synchronization between Java and web interfaces
- **Data Integrity**: Comprehensive error handling and validation
- **Unified Data**: Both Java and web systems share the same database

## 🚀 Installation

### **Prerequisites**
- **Java JDK 24** or higher
- **Maven 3.11.0** or higher
- **MySQL 8.0+** or **XAMPP** (includes MySQL)
- **Git** (for cloning the repository)

### **Quick Setup (XAMPP)**

1. **Install XAMPP**
   ```bash
   # Download from https://www.apachefriends.org/
   # Install and start MySQL service
   ```

2. **Clone Repository**
   ```bash
   git clone https://github.com/your-repo/cephra.git
   cd cephra
   ```

3. **Database Setup**
   ```bash
   # Create database 'cephradb' in phpMyAdmin
   # Import: src/main/resources/db/init.sql
   ```

4. **Build Project**
   ```bash
   mvn clean compile
   ```

5. **Run Application**
   ```bash
   mvn exec:java -Dexec.mainClass="cephra.Launcher"
   ```

### **Manual Setup**

1. **Database Configuration**
   - Create MySQL database: `cephradb`
   - Update connection settings in `src/main/java/cephra/Database/DatabaseConnection.java`
   - Import database schema from `src/main/resources/db/init.sql`

2. **Web Interface Setup**
   - Copy `Appweb/` folder to your web server directory
   - Update database credentials in `config/database.php`
   - Access via `http://localhost/cephra/`

## 📱 Usage

### **Admin Panel**
- Launch the Java application
- Login with admin credentials
- Manage queues, staff, and station operations
- Monitor real-time charging activities

### **Customer Interface**
- **Java Mobile App**: Launch customer interface
- **Web Interface**: Visit `http://localhost/cephra/mobileweb/`
- Register account or login with existing credentials
- Join queue and select charging services

### **Display Monitor**
- Launch monitor interface for public display
- Shows real-time queue status and station information

## ⚙️ Configuration

### **Database Settings**
```java
// src/main/java/cephra/Database/DatabaseConnection.java
private static final String DB_HOST = "localhost";
private static final String DB_PORT = "3306";
private static final String DB_NAME = "cephradb";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "";
```

### **Web Configuration**
```php
// config/database.php
$this->host = 'localhost';
$this->db_name = 'cephradb';
$this->username = 'root';
$this->password = '';
```

## 📁 Project Structure

```
Cephra/
├── src/main/java/cephra/          # Java source code
│   ├── Admin/                     # Admin panel components
│   ├── Phone/                     # Customer mobile interface
│   ├── Database/                  # Database connection & management
│   └── Launcher.java             # Main application entry point
├── src/main/resources/            # Resources and assets
│   ├── db/                       # Database initialization scripts
│   └── Cephra Images/            # Application images and icons
├── Appweb/                       # Web interface components
│   ├── User/                     # Customer web interface
│   ├── Admin/                    # Admin web interface
│   └── Monitor/                  # Display monitor web interface
├── config/                       # Configuration files
├── docs/                         # Project documentation
├── target/                       # Maven build output
└── pom.xml                       # Maven project configuration
```

## 🛠️ Technologies Used

### **Backend**
- **Java 24** - Core application logic
- **Java Swing** - Desktop GUI framework
- **Maven** - Build and dependency management
- **MySQL** - Database management system
- **HikariCP** - Database connection pooling

### **Web Interface**
- **PHP 8.0+** - Server-side web development
- **HTML5** - Modern web markup
- **CSS3** - Advanced styling and animations
- **JavaScript** - Client-side interactivity
- **jQuery** - JavaScript library for DOM manipulation

### **Development Tools**
- **NetBeans IDE** - Java development environment
- **XAMPP** - Local development server
- **Git** - Version control system

## 🌐 Web Interface (Phone Improvement)

The web interface provides enhanced mobile accessibility:

### **Features**
- **Responsive Design** - Adapts to any screen size
- **Real-Time Updates** - Live queue status every 5 seconds
- **Modern UI** - Clean, intuitive interface with animations
- **Cross-Platform** - Works on iOS, Android, and desktop browsers

### **Access Points**
- **Customer Interface**: `http://localhost/cephra/Appweb/User/`
- **Admin Interface**: `http://localhost/cephra/Appweb/Admin/`
- **Monitor Display**: `http://localhost/cephra/Appweb/Monitor/`

### **API Endpoints**
- `POST /api/mobile.php` - Mobile API endpoints
- `POST /api/admin.php` - Admin API endpoints
- Real-time queue updates and user management

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### **Development Guidelines**
- Follow Java coding standards
- Write comprehensive tests
- Update documentation for new features
- Ensure cross-platform compatibility

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

For support and questions:
- Create an issue in the GitHub repository
- Check the documentation in the `docs/` folder
- Review the setup guides for common issues

---

**Cephra** - Modern EV charging station management made simple and accessible.