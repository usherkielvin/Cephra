# 🔋 Cephra - EV Charging Queue Management System

A comprehensive Java-based Electric Vehicle (EV) charging station queue management system with **mobile web interface improvements** for enhanced mobile accessibility.

![Java](https://img.shields.io/badge/Java-21-orange)
![Maven](https://img.shields.io/badge/Maven-3.11.0-blue)
![Swing](https://img.shields.io/badge/Swing-GUI-green)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue)
![Web](https://img.shields.io/badge/Web-PHP%20%2B%20HTML5-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

## 📁 Project Structure

```
Cephra/
├── 📁 src/                     # Java Source Code (Primary System)
│   └── main/java/cephra/       # Main application code
├── 📁 mobileweb/               # Mobile web interface
├── 📁 api/                     # PHP API backend
├── 📁 config/                  # Configuration files
├── 📁 scripts/                 # Batch scripts for setup and management
├── 📁 docs/                    # Documentation files
├── 📁 dev/                     # Development tools and VS Code settings
├── 📁 target/                  # Compiled Java classes
├── pom.xml                     # Maven configuration
└── api/view-queue.php          # Queue monitoring page
```

## 🚀 Quick Start

### 1. Setup Database
```bash
scripts/init-database.bat
```

### 2. Run Application
```bash
scripts/run.bat
```

### 3. Access Web Interface
- Mobile Web Interface: `http://localhost/cephra/mobileweb/`
- Queue Monitor: `http://localhost/cephra/api/view-queue.php`

## 📚 Documentation

- **[Complete Documentation](docs/README.md)** - Full project documentation
- **[Setup Guide](docs/SETUP.md)** - PHP web interface setup
- **[MySQL Setup](docs/MYSQL_SETUP.md)** - Database configuration
- **[XAMPP Setup](docs/XAMPP_SETUP.md)** - Web server setup
- **[Scripts Guide](scripts/README.md)** - Batch scripts documentation
- **[Development Setup](dev/README.md)** - VS Code and development tools

## ✨ Features

### 🔧 Admin Panel (Java)
- Dashboard Management with real-time overview
- Queue Management with status tracking
- Staff Records and employee management
- History Tracking with detailed analytics
- Bay Management and monitoring
- Payment Processing integration

### 📱 Customer Mobile Interface
- **Java Interface**: Desktop mobile app (350x750)
- **Web Interface**: Modern mobile-optimized web UI
- User Registration and profile management
- Queue joining with real-time updates
- Service selection (Fast/Normal charging)
- Battery monitoring and history tracking

### 📺 Display Monitor (Java)
- Real-time queue status display
- Station information and availability
- Public announcements and notifications

## 🛠️ Technologies Used

- **Java 21** - Core application
- **Java Swing** - Desktop GUI
- **Maven** - Build management
- **MySQL** - Database
- **PHP 8+** - Web interface
- **HTML5/CSS3/JavaScript** - Modern web UI

## 🎮 Usage

### Java Application
The application launches three interfaces simultaneously:
1. **Admin Panel** - Right side of screen
2. **Display Monitor** - Top-left corner  
3. **Customer Mobile** - Center of screen

### Web Interface
- **Login**: `dizon` / `123`
- **Mobile Optimized** - Works on any device
- **Real-Time Updates** - Live queue synchronization

## 🔧 Configuration

- **Database**: Edit `config/database.php`
- **Java System**: Edit `src/main/java/cephra/db/DatabaseConnection.java`
- **Scripts**: See `scripts/README.md` for customization

## 📞 Support

For detailed setup instructions and troubleshooting, see the documentation in the `docs/` folder.

---

**Made with ❤️ for the EV charging community**

**Cephra - Where Java Meets Modern Web Technology** 🚀✨
