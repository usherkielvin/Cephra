# 🔋 Cephra - EV Charging Queue Management System

A comprehensive Java-based Electric Vehicle (EV) charging station queue management system with multi-interface support for administrators, customers, and display monitors.

![Java](https://img.shields.io/badge/Java-21-orange)
![Maven](https://img.shields.io/badge/Maven-3.11.0-blue)
![Swing](https://img.shields.io/badge/Swing-GUI-green)
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
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

Cephra is a modern EV charging station management system designed to streamline the queue process for electric vehicle charging. The system provides three distinct interfaces:

- **Admin Panel**: Complete management interface for station operators
- **Customer Mobile Interface**: User-friendly mobile app for customers
- **Display Monitor**: Public display showing queue status and information

## ✨ Features

### 🔧 Admin Panel Features
- **Dashboard Management**: Real-time overview of charging stations
- **Queue Management**: Monitor and manage customer queues
- **Staff Records**: Employee management and tracking
- **History Tracking**: Complete transaction and service history
- **Bay Management**: Individual charging bay control and monitoring
- **Business Analytics**: Performance metrics and reporting

### 📱 Customer Mobile Interface
- **User Registration**: Simple account creation process
- **Queue Joining**: Easy queue entry with real-time updates
- **Service Selection**: Choose from available charging services
- **Profile Management**: Personal information and preferences
- **History View**: Personal charging history and receipts
- **QR Code Linking**: Quick station connection via QR codes

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

## 📸 Screenshots

*[Screenshots will be added here]*

## 🚀 Installation

### Prerequisites

- **Java 21** or higher
- **Maven 3.6+** for building
- **NetBeans IDE** (recommended for development)

### Quick Start

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/cephra.git
   cd cephra
   ```

2. **Build the project**
   ```bash
   mvn clean compile
   ```

3. **Run the application**
   ```bash
   # Using Maven
   mvn exec:java -Dexec.mainClass="cephra.Launcher"
   
   # Or using the provided batch files
   run.bat
   ```

### Alternative Installation Methods

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

### Configuration

The application automatically launches all three interfaces:
- Admin Panel: Right side of screen
- Display Monitor: Top-left corner  
- Customer Mobile: Center of screen

### Navigation

- **Escape Key**: Exit the application
- **Mouse Drag**: Move undecorated windows
- **Button Navigation**: Use on-screen buttons for interface switching

## ⚙️ Configuration

### Window Positioning

The application automatically positions windows:
- Admin panel: Right side of screen
- Monitor: Top-left corner
- Mobile interface: Center of screen

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
│       │       │   └── Login.java
│       │       ├── Phone/           # Mobile interface components
│       │       │   ├── home.java
│       │       │   ├── Register.java
│       │       │   ├── Profile.java
│       │       │   └── serviceoffered.java
│       │       ├── Frame/           # Main frame containers
│       │       │   ├── Admin.java
│       │       │   ├── Monitor.java
│       │       │   └── Phone.java
│       │       └── Launcher.java    # Application entry point
│       └── resources/
│           └── cephra/
│               └── Photos/          # Application images and icons
├── target/                          # Compiled classes and JAR
├── pom.xml                          # Maven configuration
├── run.bat                          # Windows run script
└── README.md                        # This file
```

## 🛠️ Technologies Used

- **Java 21**: Core programming language
- **Java Swing**: GUI framework
- **Maven**: Build and dependency management
- **NetBeans**: IDE and form designer
- **Git**: Version control

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

### Adding New Features

1. Create new Java classes in appropriate packages
2. Design forms using NetBeans GUI designer
3. Update navigation logic in existing panels
4. Test thoroughly before committing

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
- **Contributors** who help improve the project

---

**Made with ❤️ for the EV charging community**

*Last updated: August 2025*
