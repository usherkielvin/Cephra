# 🔋 Cephra - EV Charging Queue Management System

![Java](https://img.shields.io/badge/Java-21-orange)
![Maven](https://img.shields.io/badge/Maven-3.11.0-blue)
![Swing](https://img.shields.io/badge/Swing-GUI-green)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue)
![Web](https://img.shields.io/badge/Web-PHP%20%2B%20HTML5-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

## 🎯 Introduction

**Cephra** is a comprehensive Electric Vehicle (EV) charging station queue management system that bridges the gap between traditional desktop applications and modern web interfaces. Built with Java Swing for robust desktop functionality and enhanced with a mobile-optimized web interface, Cephra provides a complete solution for managing EV charging stations, customer queues, and payment processing.

### Why Cephra?
- **Dual Interface**: Seamlessly combines Java desktop applications with modern web technology
- **Real-time Management**: Live queue updates and station monitoring
- **Mobile-First Web Design**: Accessible from any device with internet connection
- **Complete EV Ecosystem**: From customer registration to payment processing
- **Scalable Architecture**: Built to handle multiple charging stations and high customer volumes

## ✨ Features

### 🔧 Admin Panel (Java Desktop)
- **📊 Dashboard Management** - Real-time overview of all charging stations
- **📋 Queue Management** - Live status tracking and customer management
- **👥 Staff Records** - Complete employee management system
- **📈 History Tracking** - Detailed analytics and reporting
- **🔌 Bay Management** - Monitor and control charging bays
- **💳 Payment Processing** - Integrated payment system with transaction history

### 📱 Customer Mobile Interface
- **🖥️ Java Desktop App** - Native 350x750 mobile interface simulation
- **🌐 Web Interface** - Modern, responsive mobile-optimized web UI
- **👤 User Registration** - Complete profile management with firstname/lastname support
- **⏳ Queue Joining** - Real-time queue updates and notifications
- **⚡ Service Selection** - Choose between Fast Charging and Normal Charging
- **🔋 Battery Monitoring** - Track battery levels and charging history
- **📱 Cross-Platform** - Works on desktop, tablet, and mobile devices

### 📺 Display Monitor (Java)
- **📺 Real-time Display** - Public queue status and station information
- **🔔 Notifications** - Announcements and system alerts
- **📊 Live Statistics** - Current queue length and wait times
- **🎨 Customizable Interface** - Adjustable display settings

### 🌐 Web Integration
- **🔗 API Endpoints** - RESTful API for mobile web integration
- **📱 Mobile Responsive** - Optimized for all screen sizes
- **🔄 Real-time Sync** - Live synchronization between Java and web interfaces
- **🔐 Secure Authentication** - User login and session management

## 📁 Project Structure

```
Cephra/
├── 📁 src/                          # Java Source Code (Primary System)
│   ├── main/java/cephra/
│   │   ├── Admin/                   # Admin panel components
│   │   ├── Phone/                   # Customer mobile interface
│   │   ├── Frame/                   # Main application frames
│   │   ├── db/                      # Database connection classes
│   │   └── CephraDB.java           # Core database operations
│   └── resources/
│       ├── db/init.sql             # Database schema and initial data
│       └── cephra/Photos/          # Application images and icons
├── 📁 mobileweb/                    # Mobile Web Interface
│   ├── assets/                     # CSS, JS, and web assets
│   ├── images/                     # Web interface images
│   ├── ChargingPage.php           # Main charging interface
│   ├── dashboard.php              # User dashboard
│   └── charge_action.php          # Charging request processing
├── 📁 api/                         # PHP API Backend
│   ├── index.php                  # Main API endpoint
│   ├── mobile.php                 # Mobile API
│   ├── view-queue.php             # Queue monitoring
│   └── .php-preview-router.php    # Development router
├── 📁 config/                      # Configuration Files
│   ├── database.php               # Database configuration
│   └── nb-configuration.xml       # NetBeans project settings
├── 📁 database/                    # Database Files
│   ├── cephra-db.mv.db           # H2 database file
│   └── cephra-db.trace.db        # H2 trace file
├── 📁 scripts/                     # Setup and Management Scripts
│   ├── init-database.bat         # Database initialization
│   ├── run.bat                   # Application launcher
│   ├── fix-database-schema.bat   # Database repair tools
│   └── *.sql                     # SQL scripts
├── 📁 docs/                        # Documentation
│   ├── SETUP.md                  # Setup instructions
│   ├── MYSQL_SETUP.md            # MySQL configuration
│   ├── XAMPP_SETUP.md            # Web server setup
│   └── TESTING.md                # Testing guidelines
├── 📁 target/                      # Maven Build Output
├── 📁 vscode/                      # VS Code Configuration
├── pom.xml                        # Maven project configuration
├── README.md                      # This file
└── TODO.md                        # Project tasks and roadmap
```

## 🚀 Installation & Setup

### Prerequisites
- **Java 21** or higher
- **Maven 3.11.0** or higher
- **MySQL 8.0+** or **XAMPP** (for web interface)
- **Windows 10/11** (batch scripts are Windows-specific)

### Quick Setup (Recommended)

1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourusername/cephra.git
   cd cephra
   ```

2. **Initialize Database**
   ```bash
   scripts/init-database.bat
   ```

3. **Run Application**
   ```bash
   scripts/run.bat
   ```

4. **Access Web Interface**
   - Mobile Web: `http://localhost/cephra/mobileweb/`
   - Queue Monitor: `http://localhost/cephra/api/view-queue.php`

### Manual Setup

#### Database Setup
1. **MySQL Configuration**
   ```bash
   # Edit database settings
   config/database.php
   ```

2. **Initialize Schema**
   ```bash
   # Run SQL initialization
   scripts/init-database.bat
   ```

#### Java Application
1. **Compile with Maven**
   ```bash
   mvn clean compile
   ```

2. **Run Application**
   ```bash
   mvn exec:java -Dexec.mainClass="cephra.Launcher"
   ```

#### Web Interface Setup
1. **XAMPP Setup**
   - Install XAMPP
   - Copy project to `htdocs/cephra/`
   - Start Apache and MySQL services

2. **Database Connection**
   - Update `config/database.php` with your MySQL credentials
   - Run `scripts/init-database.bat`

### Default Login Credentials
- **Username**: `dizon`
- **Password**: `123`

## 🛠️ Technologies Used

### Backend
- **Java 21** - Core application logic
- **Java Swing** - Desktop GUI framework
- **Maven** - Build and dependency management
- **MySQL 8.0+** - Primary database
- **H2 Database** - Embedded database for development

### Frontend
- **PHP 8+** - Web interface backend
- **HTML5/CSS3** - Modern web markup and styling
- **JavaScript** - Interactive web components
- **Bootstrap** - Responsive web framework
- **Font Awesome** - Icon library

### Development Tools
- **NetBeans** - Java IDE integration
- **VS Code** - Web development environment
- **Git** - Version control
- **XAMPP** - Local web server stack

## 🎮 Usage

### Java Application Launch
The application launches three synchronized interfaces:
1. **Admin Panel** (Right side) - Station management and monitoring
2. **Display Monitor** (Top-left) - Public queue display
3. **Customer Mobile** (Center) - Customer interface simulation

### Web Interface Access
- **Mobile Web**: Access from any device with internet connection
- **Real-time Updates**: Live synchronization with Java application
- **Responsive Design**: Optimized for mobile, tablet, and desktop

### Key Workflows
1. **Customer Registration** → **Queue Joining** → **Service Selection** → **Payment Processing**
2. **Admin Monitoring** → **Queue Management** → **Payment Verification** → **History Tracking**

## 🔧 Configuration

### Database Settings
- **Java**: `src/main/java/cephra/db/DatabaseConnection.java`
- **Web**: `config/database.php`

### Application Settings
- **Maven**: `pom.xml`
- **NetBeans**: `config/nb-configuration.xml`
- **VS Code**: `vscode/` folder

### Scripts Customization
- See `scripts/README.md` for detailed script documentation
- Modify batch files for custom deployment scenarios

## 📚 Documentation

- **[Complete Setup Guide](docs/SETUP.md)** - Comprehensive installation instructions
- **[MySQL Configuration](docs/MYSQL_SETUP.md)** - Database setup and optimization
- **[XAMPP Setup](docs/XAMPP_SETUP.md)** - Web server configuration
- **[Testing Guidelines](docs/TESTING.md)** - Testing procedures and best practices
- **[Scripts Documentation](scripts/README.md)** - Batch scripts reference

## 🤝 Contributing

We welcome contributions! Please see our contributing guidelines:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📞 Support

- **Documentation**: Check the `docs/` folder for detailed guides
- **Issues**: Report bugs and request features via GitHub Issues
- **Scripts**: Use the provided batch scripts for common tasks

## 👨‍💻 Author/Team

### Project Lead
**Usher Kielvin Ponce** - Project Lead, Backend Developer & EV Technology Enthusiast

- 🌐 **Portfolio**: [yourportfolio.com](https://yourportfolio.com)
- 💼 **LinkedIn**: [linkedin.com/in/yourprofile](https://linkedin.com/in/yourprofile)
- 🐙 **GitHub**: [github.com/yourusername](https://github.com/yourusername)
- 📧 **Email**: your.email@example.com

### Development Team

#### 🎨 UI/UX Designer
**Mark Dwayne Dela Cruz** - User Interface & User Experience Design
- Specializes in mobile-first responsive design
- Creates intuitive user interfaces for both Java and web applications

#### 🔧 Backend Developer
**Dizon S. Dizon** - Backend Development & Database Architecture
- Expert in Java backend development and database design
- Handles server-side logic and data management

#### 💻 Frontend Developer
**Kenji** - Frontend Development & Web Technologies
- Specializes in modern web technologies (HTML5, CSS3, JavaScript)
- Creates responsive and interactive user interfaces

### Team Specializations
- **Java Development** - Desktop applications and enterprise solutions
- **Web Development** - Modern PHP, HTML5, CSS3, and JavaScript
- **Database Design** - MySQL optimization and schema design
- **EV Technology** - Electric vehicle charging infrastructure
- **UI/UX Design** - Mobile-first responsive design

### Acknowledgments
- **EV Community** - For inspiration and feedback
- **Open Source Contributors** - For the amazing tools and libraries
- **Beta Testers** - For helping refine the user experience

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

**Made with ❤️ for the EV charging community**

**Cephra - Where Java Meets Modern Web Technology** 🚀✨

*Empowering the future of electric vehicle charging infrastructure through innovative software solutions.*