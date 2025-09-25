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
├── 📁 Appweb/                       # Web Interface Components
│   ├── Admin/                      # Admin web interface
│   ├── Monitor/                    # Queue monitor web interface
│   └── User/                       # Customer web interface
│       ├── assets/                 # CSS, JS, and web assets
│       ├── images/                 # Web interface images
│       ├── api/                    # PHP API endpoints
│       ├── config/                 # Database and configuration files
│       ├── css/                    # Stylesheets
│       ├── ChargingPage.php       # Main charging interface
│       ├── dashboard.php          # User dashboard
│       ├── link.php               # Vehicle linking interface
│       └── *.php                  # Other PHP pages
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

## 👥 Development Team – Cephra QMS

### 👨‍💻 Project Lead
**Usher Kielvin Ponce** – Project Lead, Backend Developer & EV Technology Enthusiast

- Backend logic & project coordination
- Manages EV charging system flow

### 🎨 UI/UX & Web Developer
**Mark Dwayne P. Dela Cruz** – Web Interface & User Experience

- Designs and develops mobile web interface for customers
- Focuses on intuitive, responsive layouts

### 🔧 Backend Developer
**Dizon S. Dizon** – Backend Development & Database Architecture

- Expert in Java backend development
- Handles server-side logic and database design

### 💻 Frontend Developer (Java Swing)
**Kenji A. Hizon** – Desktop Application Interface Developer

- Specializes in Java Swing GUI
- Focuses on building an intuitive and functional desktop interface

## 🔄 API Documentation

### Core Endpoints

#### Mobile API (`api/mobile.php`)
- **GET** `/api/mobile.php?action=get_queue` - Retrieve current queue status
- **POST** `/api/mobile.php?action=join_queue` - Join charging queue
- **POST** `/api/mobile.php?action=leave_queue` - Leave current queue
- **GET** `/api/mobile.php?action=get_history` - Get user's charging history

#### Web Interface APIs
- **POST** `/api/check_email.php` - Email validation for registration
- **POST** `/api/forgot_password.php` - Password reset functionality
- **GET** `/api/view-queue.php` - Public queue monitoring

### Authentication
- Session-based authentication for web interface
- Username/password validation
- Secure password hashing with bcrypt

## 🧪 Testing

### Automated Testing
- Unit tests for Java components
- Integration tests for database operations
- API endpoint testing with Postman collections

### Manual Testing Guidelines
- See `docs/TESTING.md` for comprehensive testing procedures
- User acceptance testing for new features
- Cross-browser compatibility testing

## 🚀 Deployment

### Production Setup
1. **Server Requirements**
   - Apache/Nginx web server
   - PHP 8.0+ with PDO extension
   - MySQL 8.0+ database
   - Java 21 runtime for admin panel

2. **Security Configuration**
   - SSL certificate installation
   - Database connection encryption
   - Secure session management
   - Input validation and sanitization

3. **Performance Optimization**
   - Database query optimization
   - Caching implementation
   - CDN for static assets
   - Load balancing for high traffic

## 📊 Monitoring & Analytics

### System Monitoring
- Real-time queue length tracking
- Charging station utilization reports
- Payment transaction analytics
- User activity logs

### Performance Metrics
- Average queue wait times
- Charging session completion rates
- System uptime and availability
- User satisfaction scores

## 🔐 Security Features

### Data Protection
- Encrypted database connections
- Secure password storage (bcrypt hashing)
- XSS protection in web forms
- CSRF protection for API endpoints

### Access Control
- Role-based access (Admin, Staff, Customer)
- Session timeout management
- IP-based restrictions for admin panel
- Audit logging for sensitive operations

## 🐛 Troubleshooting

### Common Issues
- **Database Connection Errors**: Check MySQL credentials in `config/database.php`
- **Java Application Won't Start**: Verify Java 21 installation and Maven dependencies
- **Web Interface Not Loading**: Ensure Apache is running and files are in correct directory
- **Queue Sync Issues**: Check WebSocket connections and API endpoints

### Debug Mode
- Enable debug logging in Java application
- PHP error reporting in web interface
- Database query logging for performance issues

## 📈 Roadmap

### Upcoming Features
- [ ] Mobile app development (React Native)
- [ ] Advanced analytics dashboard
- [ ] Multi-language support (i18n)
- [ ] Integration with third-party EV APIs
- [ ] Automated testing suite expansion
- [ ] Cloud deployment support

### Version History
- **v1.0.0** - Initial release with core functionality
- **v1.1.0** - Web interface enhancements
- **v1.2.0** - API improvements and bug fixes
- **v2.0.0** - Planned major update with mobile app

## 🤝 Contributing Guidelines

### Code Standards
- Java: Follow Google Java Style Guide
- PHP: PSR-12 coding standards
- HTML/CSS: Consistent indentation and semantic markup
- Commit messages: Clear, descriptive, and prefixed with type

### Development Workflow
1. Create feature branch from `main`
2. Implement changes with proper testing
3. Submit pull request with detailed description
4. Code review and approval process
5. Merge to main branch

## 📞 Contact & Support

### Development Team
- **Project Lead**: Usher Kielvin Ponce
- **Web Developer**: Mark Dwayne P. Dela Cruz
- **Backend Developer**: Dizon S. Dizon
- **Java Developer**: Kenji A. Hizon

### Support Channels
- **GitHub Issues**: Bug reports and feature requests
- **Documentation**: Comprehensive guides in `docs/` folder
- **Email**: support@cephra-project.com (placeholder)

## 🙏 Acknowledgments

### Open Source Libraries
- **Java Swing** - Desktop GUI framework
- **MySQL Connector/J** - Database connectivity
- **PHP PDO** - Database abstraction layer
- **Font Awesome** - Icon library
- **Bootstrap** - CSS framework

### Community Support
- **EV Enthusiasts** - For valuable feedback and testing
- **Open Source Community** - For tools and inspiration
- **Educational Institutions** - For providing development environment

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

**Made with ❤️ for the EV charging community**

**Cephra - Where Java Meets Modern Web Technology** 🚀✨

*Empowering the future of electric vehicle charging infrastructure through innovative software solutions.*
