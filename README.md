# 🔋 Cephra - EV Charging Queue Management System

A comprehensive Java-based Electric Vehicle (EV) charging station queue management system with **web-based phone interface improvements** for enhanced mobile accessibility.

![Java](https://img.shields.io/badge/Java-21-orange)
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
├── phone/                      # Web-based phone interface
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

- **Java 21** or higher (Primary System)
- **Maven 3.6+** for building Java application
- **MySQL 8.0+** for database
- **XAMPP** for web interface (optional enhancement)

### **Primary System Setup (Java)**

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/cephra.git
   cd cephra
   ```

2. **Setup Database**
   ```bash
   # Initialize database
   init-database.bat
   ```

3. **Build and Run Java Application**
   ```bash
   # Using Maven
   mvn clean compile
   mvn exec:java -Dexec.mainClass="cephra.Launcher"
   
   # Or using provided batch files
   run.bat
   ```

### **Web Interface Setup (Optional Enhancement)**

1. **Install XAMPP**
   - Download from [XAMPP official website](https://www.apachefriends.org/)
   - Start Apache and MySQL services

2. **Copy Web Files**
   ```bash
   # Copy to XAMPP htdocs folder
   C:\xampp\htdocs\cephra\
   ```

3. **Access Web Interface**
   ```
   Phone Interface: http://localhost/cephra/phone/
   Queue Monitor:  http://localhost/cephra/view-queue.php
   ```

## 🎮 Usage

### **Starting the Java Application**

The Java application launches three interfaces simultaneously:

1. **Admin Panel**: Appears on the right side of the screen
2. **Display Monitor**: Appears in the top-left corner
3. **Customer Mobile**: Appears centered on screen

### **Java Interface Usage**

#### **Admin Panel**
- **Login**: Use default credentials (admin/admin123)
- **Dashboard**: Configure pricing and view system status
- **Queue**: Manage customer tickets and process payments
- **History**: View transaction history and analytics
- **Staff**: Manage employee accounts and permissions

#### **Customer Mobile (Java)**
- **Register**: Create new account with email verification
- **Join Queue**: Select service type and join the queue
- **Monitor Status**: Track your position in the queue
- **View History**: Access charging history and receipts

#### **Display Monitor**
- **Queue Display**: Real-time queue status and updates
- **Station Information**: Current availability and wait times
- **Public Announcements**: Important notifications and updates

### **Web Interface Usage (Phone Enhancement)**

#### **Mobile Access**
- **Login** with username: `dizon`, password: `123`
- **View Live Queue** - Real-time updates every 5 seconds
- **Create Tickets** - Fast Charging or Normal Charging
- **Mobile Optimized** - Works perfectly on phones

#### **Cross-Platform Benefits**
- **Any Device** - Works on phones, tablets, computers
- **No Installation** - Access via web browser
- **Real-Time Sync** - Same data as Java application
- **Enhanced UX** - Modern web interface design

### **Navigation**
- **Escape Key**: Exit the Java application
- **Mouse Drag**: Move undecorated windows
- **Button Navigation**: Use on-screen buttons for interface switching
- **Web Browser**: Access web interface from any device

## ⚙️ Configuration

### **Java System Configuration**

#### **Database Configuration**
Edit `src/main/java/cephra/db/DatabaseConnection.java` to modify:
- Database URL
- Username and password
- Connection pool settings

#### **Window Positioning**
The Java application automatically positions windows:
- Admin panel: Right side of screen
- Monitor: Top-left corner
- Mobile interface: Center of screen

#### **Pricing Configuration**
Configure charging rates in the Admin Dashboard:
- Minimum fee settings
- Rate per hour (RPH)
- Service type pricing

### **Web Interface Configuration**

#### **Database Connection**
Edit `config/database.php` to modify:
- Database host and name
- Username and password
- Connection parameters

#### **API Endpoints**
Modify `api/mobile.php` to:
- Add new API functions
- Change data handling
- Enhance security features

## 📁 Project Structure

```
Cephra/
├── 📁 src/                     # Java Source Code (Primary System)
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
├── 📁 cephra/                  # Web Interface (Phone Enhancement)
│   ├── 📁 phone/               # Web-based phone interface
│   │   ├── index.php           # Main phone interface
│   │   ├── styles.css          # Modern styling
│   │   └── script.js           # Interactive functionality
│   ├── 📁 api/                 # PHP API backend
│   │   └── mobile.php          # API endpoints
│   ├── 📁 config/              # Configuration
│   │   └── database.php        # Database connection
│   ├── view-queue.php          # Queue monitoring page
│   └── index.php               # Main landing page
├── 📁 target/                  # Compiled Java classes and JAR
├── pom.xml                     # Maven configuration
├── *.bat                       # Windows batch scripts
├── *.md                        # Documentation files
└── README.md                   # This file
```

## 🛠️ Technologies Used

### **Primary System (Java)**
- **Java 21**: Core programming language
- **Java Swing**: GUI framework
- **Maven**: Build and dependency management
- **MySQL**: Database management system

### **Web Enhancement (Phone Interface)**
- **PHP 8+**: Server-side logic and API
- **HTML5**: Modern web markup
- **CSS3**: Advanced styling and animations
- **JavaScript ES6+**: Interactive functionality
- **XAMPP**: Local development environment

## 🌐 Web Interface (Phone Improvement)

### **Purpose**
The web interface serves as an **enhancement** to the Java-based phone interface, providing:

- **Better Mobile Experience** - Modern web design optimized for phones
- **Cross-Platform Access** - Works on any device with a web browser
- **Enhanced User Interface** - Beautiful gradients, animations, and responsive design
- **Real-Time Updates** - Live queue synchronization with the Java system

### **Key Features**
- **Mobile-First Design** - Responsive interface that works perfectly on phones
- **Real-Time Queue Updates** - Live data synchronization every 5 seconds
- **Modern UI/UX** - Professional styling with hover effects and animations
- **API Integration** - Seamless connection to the same database as Java system

### **API Endpoints**
```
POST /api/mobile.php?action=login          # User authentication
GET  /api/mobile.php?action=queue          # Get queue data
POST /api/mobile.php?action=create-ticket  # Create new tickets
```

### **Mobile Access**
- **Local Network**: `http://[YOUR_IP]/cephra/phone/`
- **Same Database**: Shares data with Java application
- **Real-Time Sync**: Updates automatically with Java system

## 🔧 Development

### **Java Development**

#### **Building from Source**
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

#### **Code Style**
- Follow Java naming conventions
- Use meaningful variable and method names
- Add comments for complex logic
- Maintain consistent indentation
- Use underscore `_` for unused lambda parameters

### **Web Interface Development**

#### **File Separation Benefits**
- **Easier Maintenance** - Each file has a single purpose
- **Faster Development** - Edit CSS without touching HTML
- **Better Organization** - Clear file structure
- **Team Collaboration** - Different developers can work on different files

#### **Development Workflow**
1. **Edit HTML** - Modify structure in `phone/index.html`
2. **Style Changes** - Update design in `phone/styles.css`
3. **Add Features** - Enhance functionality in `phone/script.js`
4. **Test** - Refresh browser to see changes
5. **Deploy** - Copy to production when ready

## 🐛 Troubleshooting

### **Java System Issues**
- **Compilation errors**: Check Java version and Maven installation
- **Database connection**: Verify MySQL is running and credentials are correct
- **Window positioning**: Check screen resolution and graphics settings

### **Web Interface Issues**
- **"Page not found"**: Check XAMPP is running and files are in correct location
- **Database connection failed**: Verify MySQL is running and credentials are correct
- **Login not working**: Check database tables and user data exist
- **Mobile access issues**: Ensure both devices are on same WiFi network

### **Debug Tools**
- **Java Console**: Check application logs and error messages
- **Browser Console**: Check JavaScript errors and API responses
- **Database Check**: Verify data exists in MySQL tables

## 📈 Future Enhancements

### **Java System**
- **Enhanced GUI**: Modern look and feel improvements
- **Advanced Analytics**: Business intelligence and reporting
- **Payment Integration**: Online payment processing
- **Multi-language Support**: Internationalization

### **Web Interface**
- **Push Notifications**: Real-time alerts for users
- **Advanced Mobile Features**: Offline support, PWA capabilities
- **Admin Dashboard**: Web-based administration panel
- **API Expansion**: More endpoints for enhanced functionality

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/AmazingFeature`)
3. **Commit** your changes (`git commit -m 'Add some AmazingFeature'`)
4. **Push** to the branch (`git push origin feature/AmazingFeature`)
5. **Open** a Pull Request

### **Contribution Guidelines**

- Ensure Java code compiles without errors
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
- **PHP Community** for web enhancement capabilities
- **Contributors** who help improve the project

---

**Made with ❤️ for the EV charging community**

**Cephra - Where Java Meets Modern Web Technology** 🚀✨

*Last updated: December 2024*
