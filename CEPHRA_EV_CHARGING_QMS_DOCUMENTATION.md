# CEPHRA EV CHARGING STATION QUEUE MANAGEMENT SYSTEM
## Data Structures & Algorithms Final Project Documentation

**Course:** Data Structures and Algorithms  
**Institution:** National University - Mall of Asia (NU MOA)  
**Project Type:** Queue Management System (QMS) Final Project  
**Development Team:** Cephra QMS Development Team  

---

## I. INTRODUCTION OF QMS FOR CEPHRA EV CHARGING STATION

### Background: Why EVs and QMS are Needed

The global shift towards sustainable transportation has accelerated the adoption of Electric Vehicles (EVs). With increasing EV ownership, the demand for efficient charging infrastructure has become critical. Traditional gas stations operate on a "fill and go" model, but EV charging requires significantly more time, creating the need for sophisticated queue management systems.

**Key Challenges Addressed:**
- **Time Management**: EV charging takes 30 minutes to several hours, unlike the 5-minute gasoline fill-up
- **Resource Optimization**: Limited charging bays must be efficiently allocated among multiple users
- **Customer Experience**: Users need real-time updates on queue status, wait times, and charging progress
- **Payment Integration**: Seamless payment processing for different charging services and durations
- **Priority Management**: Emergency situations and low battery levels require priority handling

### Short Explanation of Cephra and its Purpose

**Cephra** is a comprehensive EV Charging Station Queue Management System that bridges traditional desktop applications with modern web interfaces. Built with Java Swing for robust desktop functionality and enhanced with a mobile-optimized web interface, Cephra provides a complete solution for managing EV charging stations, customer queues, and payment processing.

**Core Purpose:**
- **Dual Interface System**: Combines Java desktop applications with modern web technology
- **Real-time Management**: Live queue updates and station monitoring
- **Mobile-First Design**: Accessible from any device with internet connection
- **Complete EV Ecosystem**: From customer registration to payment processing
- **Scalable Architecture**: Built to handle multiple charging stations and high customer volumes

**Key Features:**
- Real-time queue management with priority handling
- Dynamic bay allocation and monitoring
- Integrated payment processing with multiple methods
- Battery level tracking and charging optimization
- Comprehensive admin dashboard for station management
- Mobile-responsive web interface for customer convenience

---

## II. TRANSACTIONS HANDLED BY THE QMS

### 1. User Registration

**Purpose**: Create and manage customer accounts for EV charging services

**Process Flow:**
```
Registration Form → Validation → Database Storage → Email Verification → Account Activation
```

**Key Components:**
- **Form Fields**: First Name, Last Name, Username, Email, Password
- **Validation**: Email format, password strength, username uniqueness
- **Database Integration**: MySQL storage with user profile management
- **Security**: Password hashing and session management

**Implementation Details:**
- **Java Interface**: `Register.java` with Swing GUI components
- **Web Interface**: `Register_Panel.php` with responsive design
- **Database**: `users` table with comprehensive user information
- **API Integration**: RESTful endpoints for registration validation

### 2. Queue Number Generation

**Purpose**: Generate unique ticket identifiers for charging queue management

**Algorithm Implementation:**
- **Fast Charging**: FCH001, FCH002, FCH003... (Priority-based numbering)
- **Normal Charging**: NCH001, NCH002, NCH003... (Sequential numbering)
- **Priority System**: Battery level < 20% receives priority status

**Data Structures Used:**
- **ArrayList**: Dynamic ticket storage and management
- **Counter Variables**: `nextFastNumber`, `nextNormalNumber` for sequential generation
- **Priority Queue**: Battery level-based priority assignment

**Code Implementation:**
```java
private static int nextFastNumber = 1;   // FCH001, FCH002, ...
private static int nextNormalNumber = 1; // NCH001, NCH002, ...

public static String generatePriorityTicketIdForService(String service, int batteryLevel) {
    int priority = (batteryLevel < 20) ? 1 : 0;
    String prefix = service.contains("Fast") ? "FCH" : "NCH";
    int number = service.contains("Fast") ? nextFastNumber++ : nextNormalNumber++;
    return String.format("%s%03d", prefix, number);
}
```

### 3. Queue Monitoring

**Purpose**: Real-time tracking of queue status, wait times, and customer positions

**Monitoring Features:**
- **Live Queue Display**: Real-time updates of queue status
- **Position Tracking**: Current position in queue for each customer
- **Wait Time Estimation**: Calculated based on service type and current queue length
- **Status Updates**: Pending → Waiting → In Progress → Complete

**Data Structures:**
- **ArrayList<Entry>**: Queue entries with customer information
- **HashMap**: Battery information mapping and user data storage
- **Boolean Arrays**: Bay availability and occupation tracking

**Real-time Updates:**
- **WebSocket Integration**: Live synchronization between Java and web interfaces
- **Database Polling**: Regular updates from MySQL database
- **Admin Dashboard**: Comprehensive monitoring interface

### 4. Charging Slot Assignment

**Purpose**: Intelligent allocation of charging bays based on availability and service requirements

**Bay Management System:**
- **Fast Charging Bays**: 3 bays (Bays 1-3) for rapid charging
- **Normal Charging Bays**: 5 bays (Bays 4-8) for standard charging
- **Availability Tracking**: Real-time bay status monitoring
- **Automatic Assignment**: Next available bay allocation

**Assignment Algorithm:**
```java
public static boolean isFastChargingAvailable() {
    for (int i = 0; i < fastChargingAvailable.length; i++) {
        if (fastChargingAvailable[i] && !fastChargingOccupied[i]) {
            return true;
        }
    }
    return false;
}
```

**Priority Handling:**
- **Low Battery Priority**: Battery < 20% gets immediate bay assignment
- **Service Type Matching**: Fast charging requests to fast bays, normal to normal bays
- **Load Balancing**: Even distribution across available bays

### 5. Payment Processing

**Purpose**: Secure and efficient payment handling for charging services

**Payment Methods:**
- **Wallet System**: Pre-loaded balance for seamless transactions
- **Online Payment**: Credit card and digital wallet integration
- **Cash Payment**: Traditional payment method support

**Payment Flow:**
```
Service Selection → Amount Calculation → Payment Method Selection → Transaction Processing → Receipt Generation
```

**Amount Calculation:**
- **Base Rate**: Service-specific pricing (Fast vs Normal charging)
- **Time-based**: Duration of charging session
- **Battery Level**: Initial and target battery levels
- **Dynamic Pricing**: Peak/off-peak rate adjustments

**Security Features:**
- **Transaction Validation**: Duplicate payment prevention
- **Database Transactions**: ACID compliance for payment integrity
- **Receipt Generation**: Digital receipts with transaction details

### 6. Charging Session Tracking

**Purpose**: Monitor charging progress, battery levels, and session completion

**Tracking Features:**
- **Battery Level Monitoring**: Real-time battery percentage tracking
- **Charging Progress**: Visual progress indicators
- **Time Estimation**: Remaining charging time calculation
- **Session History**: Complete charging session records

**Battery Management:**
```java
public static int getUserBatteryLevel(String username) {
    // Retrieve current battery level from database
    // Calculate remaining range and charging time
    // Update display with real-time information
}
```

**Progress Indicators:**
- **Visual Progress Bars**: Battery level representation
- **Time Countdown**: Estimated completion time
- **Status Updates**: Charging, Paused, Complete, Error states

### 7. Completion and Exit

**Purpose**: Proper session termination and bay release for next customer

**Completion Process:**
- **Automatic Detection**: Battery full or user-initiated stop
- **Payment Verification**: Ensure payment completion
- **Bay Release**: Free up charging bay for next customer
- **Receipt Generation**: Final transaction summary
- **History Recording**: Session data archival

**Exit Workflow:**
```
Charging Complete → Payment Verification → Bay Release → Receipt Generation → Queue Update → Next Customer
```

**Data Cleanup:**
- **Ticket Removal**: Clear completed tickets from active queue
- **Bay Status Update**: Mark bay as available
- **Database Updates**: Record session completion
- **Statistics Update**: Update charging statistics and analytics

---

## III. SCREEN LAYOUT

### 1. Registration Screen

**Desktop Interface (Java Swing):**
- **Layout**: 350x750 mobile simulation window
- **Components**: Form fields, validation messages, navigation buttons
- **Design**: Modern mobile app aesthetic with touch-friendly controls

**Web Interface (Responsive Design):**
- **Layout**: Full-screen responsive design
- **Components**: Multi-step registration form with validation
- **Features**: Real-time validation, email verification, terms acceptance

**Key Elements:**
- First Name, Last Name input fields
- Username with uniqueness validation
- Email with format verification
- Password with strength indicators
- Terms and Conditions checkbox
- Registration submit button

### 2. Queue Display Board

**Public Display Monitor:**
- **Size**: 1000x750 full-screen display
- **Purpose**: Public queue status for all customers
- **Content**: Current queue, wait times, bay availability

**Display Elements:**
- **Queue List**: Customer names, ticket numbers, service types
- **Wait Times**: Estimated waiting time for each position
- **Bay Status**: Available/Occupied status for all charging bays
- **System Messages**: Announcements and notifications

**Real-time Updates:**
- **Live Refresh**: Automatic updates every 30 seconds
- **Color Coding**: Status-based color indicators
- **Sound Notifications**: Audio alerts for queue updates

### 3. Payment Interface

**Mobile Payment Screen:**
- **Layout**: Clean, secure payment form
- **Payment Methods**: Wallet, Credit Card, Digital Wallets
- **Amount Display**: Clear pricing breakdown
- **Security Indicators**: SSL, encryption status

**Payment Components:**
- **Service Summary**: Charging type, duration, amount
- **Payment Method Selection**: Radio buttons for payment options
- **Amount Confirmation**: Total amount with tax breakdown
- **Submit Button**: Secure payment processing
- **Receipt Preview**: Transaction summary before confirmation

### 4. Charging Status Screen

**Customer Interface:**
- **Progress Display**: Visual charging progress bar
- **Battery Level**: Real-time battery percentage
- **Time Remaining**: Estimated completion time
- **Bay Information**: Assigned bay number and status

**Status Indicators:**
- **Charging**: Active charging with progress animation
- **Paused**: Temporarily stopped charging
- **Complete**: Charging finished, ready for exit
- **Error**: Technical issues requiring attention

**Interactive Elements:**
- **Pause/Resume**: Control charging session
- **Emergency Stop**: Immediate charging termination
- **Help Button**: Contact support for assistance

### 5. Admin Dashboard (Management Interface)

**Admin Panel Layout:**
- **Size**: 1000x750 comprehensive management interface
- **Sections**: Queue management, bay control, payment monitoring, analytics

**Dashboard Components:**
- **Queue Management Table**: All active and pending tickets
- **Bay Status Grid**: Visual representation of all charging bays
- **Payment Monitoring**: Transaction status and history
- **Analytics Dashboard**: Usage statistics and performance metrics

**Management Features:**
- **Ticket Processing**: Approve, reject, or modify queue entries
- **Bay Control**: Manually assign or release charging bays
- **Payment Override**: Handle payment disputes or refunds
- **System Settings**: Configure charging rates and policies

---

## IV. SOURCE CODE

### Brief Description of Code Structure

**Project Architecture:**
```
Cephra/
├── src/main/java/cephra/          # Core Java Application
│   ├── Admin/                     # Admin Panel Components
│   ├── Database/                  # Database Connection & Operations
│   ├── Frame/                     # Main Application Windows
│   ├── Phone/                     # Customer Mobile Interface
│   └── Launcher.java             # Application Entry Point
├── Appweb/                        # Web Interface Components
│   ├── Admin/                     # Admin Web Interface
│   ├── Monitor/                   # Queue Monitor Web Interface
│   └── User/                      # Customer Web Interface
└── resources/                     # Application Resources
    ├── db/                        # Database Schema Files
    └── cephra/Cephra Images/      # Application Images & Icons
```

**Technology Stack:**
- **Backend**: Java 24, Java Swing, MySQL 8.0+, HikariCP
- **Frontend**: PHP 8+, HTML5, CSS3, JavaScript, Bootstrap
- **Database**: MySQL with connection pooling
- **Build Tool**: Maven 3.11.0
- **Testing**: JUnit 5

### Data Structures Used

#### 1. ArrayList for Queue Management
```java
private static final List<Entry> entries = new ArrayList<Entry>();

public static final class Entry {
    public final String ticketId;
    public final String customerName;
    public final String serviceName;
    public final String status;
    public final String payment;
    public final String action;
    public final int initialBatteryPercent;
    public final double batteryCapacityKWh;
}
```

#### 2. HashMap for Battery Information
```java
private static final Map<String, BatteryInfo> ticketBattery = new HashMap<>();

public static class BatteryInfo {
    public final int initialPercent;
    public final double capacityKWh;
    
    public BatteryInfo(int initialPercent, double capacityKWh) {
        this.initialPercent = initialPercent;
        this.capacityKWh = capacityKWh;
    }
}
```

#### 3. Boolean Arrays for Bay Management
```java
public static boolean[] fastChargingAvailable = {true, true, true}; // Bays 1-3
public static boolean[] normalChargingAvailable = {true, true, true, true, true}; // Bays 4-8

public static boolean[] fastChargingOccupied = {false, false, false};
public static boolean[] normalChargingOccupied = {false, false, false, false, false};
```

#### 4. PreparedStatement for Database Operations
```java
public static boolean validateLogin(String username, String password) {
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(
                 "SELECT username, email, password FROM users WHERE username = ? AND password = ?")) {
        
        stmt.setString(1, username);
        stmt.setString(2, password);
        
        try (ResultSet rs = stmt.executeQuery()) {
            return rs.next(); // Returns true if user exists
        }
    } catch (SQLException e) {
        System.err.println("Error validating login: " + e.getMessage());
        return false;
    }
}
```

### Important Code Snippets

#### 1. Queue Management Algorithm (FIFO with Priority)
```java
public static void addCurrentToAdminAndStore(String customerName) {
    String service = currentServiceName;
    final int initialBatteryPercent = CephraDB.getUserBatteryLevel(customerName);
    
    // Generate ticket ID with priority consideration
    if (currentTicketId == null || currentTicketId.length() == 0) {
        currentTicketId = generatePriorityTicketIdForService(service, initialBatteryPercent);
    }
    
    // Determine status based on battery level priority
    final String status = (initialBatteryPercent < 20) ? "Waiting" : "Pending";
    final double batteryCapacityKWh = 40.0; // 40kWh capacity
    
    // Store in memory list
    entries.add(new Entry(currentTicketId, customerName, service, status, "", "", 
                         initialBatteryPercent, batteryCapacityKWh));
    
    // Update database
    CephraDB.addQueueTicket(currentTicketId, customerName, service, status, "", initialBatteryPercent);
}
```

#### 2. Bay Allocation Algorithm
```java
public static boolean isFastChargingAvailable() {
    for (int i = 0; i < fastChargingAvailable.length; i++) {
        if (fastChargingAvailable[i] && !fastChargingOccupied[i]) {
            return true;
        }
    }
    return false;
}

public static int assignFastChargingBay() {
    for (int i = 0; i < fastChargingAvailable.length; i++) {
        if (fastChargingAvailable[i] && !fastChargingOccupied[i]) {
            fastChargingOccupied[i] = true;
            return i + 1; // Bay numbers start from 1
        }
    }
    return -1; // No bay available
}
```

#### 3. Payment Processing with Database Transactions
```java
public static boolean processPaymentTransaction(String ticketId, String username, String serviceType,
                                              int initialBatteryLevel, int chargingTimeMinutes, 
                                              double totalAmount, String paymentMethod, String referenceNumber) {
    Connection conn = null;
    try {
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false); // Start transaction
        
        // Update queue ticket status
        PreparedStatement updateTicket = conn.prepareStatement(
            "UPDATE queue_tickets SET payment_status = 'Paid' WHERE ticket_id = ?");
        updateTicket.setString(1, ticketId);
        updateTicket.executeUpdate();
        
        // Insert payment record
        PreparedStatement insertPayment = conn.prepareStatement(
            "INSERT INTO payments (ticket_id, username, amount, payment_method, reference_number, payment_date) " +
            "VALUES (?, ?, ?, ?, ?, NOW())");
        insertPayment.setString(1, ticketId);
        insertPayment.setString(2, username);
        insertPayment.setDouble(3, totalAmount);
        insertPayment.setString(4, paymentMethod);
        insertPayment.setString(5, referenceNumber);
        insertPayment.executeUpdate();
        
        // Update charging session
        PreparedStatement updateSession = conn.prepareStatement(
            "UPDATE charging_sessions SET status = 'In Progress' WHERE ticket_id = ?");
        updateSession.setString(1, ticketId);
        updateSession.executeUpdate();
        
        conn.commit(); // Commit transaction
        return true;
        
    } catch (SQLException e) {
        if (conn != null) {
            try { conn.rollback(); } catch (SQLException ex) {}
        }
        System.err.println("Payment transaction failed: " + e.getMessage());
        return false;
    } finally {
        if (conn != null) {
            try { conn.close(); } catch (SQLException e) {}
        }
    }
}
```

#### 4. Battery Level Validation Algorithm
```java
public static boolean canStartCharging(String username) {
    int batteryLevel = getUserBatteryLevel(username);
    
    // Prevent charging if battery is already full
    if (batteryLevel >= 100) {
        return false;
    }
    
    // Check if user has an active ticket
    if (hasActiveTicket(username)) {
        return false;
    }
    
    // Check if car is linked
    if (!AppState.isCarLinked) {
        return false;
    }
    
    return true;
}

public static int calculateChargingTime(int initialBatteryLevel, String serviceType) {
    int targetBatteryLevel = 100; // Charge to full
    int batteryDifference = targetBatteryLevel - initialBatteryLevel;
    
    // Fast charging: 1% per minute, Normal charging: 1% per 2 minutes
    if (serviceType.contains("Fast")) {
        return batteryDifference * 1; // minutes
    } else {
        return batteryDifference * 2; // minutes
    }
}
```

#### 5. Ticket Generation Algorithm
```java
public static String generatePriorityTicketIdForService(String service, int batteryLevel) {
    int priority = (batteryLevel < 20) ? 1 : 0;
    String prefix = service.contains("Fast") ? "FCH" : "NCH";
    int number;
    
    if (service.contains("Fast")) {
        number = nextFastNumber++;
    } else {
        number = nextNormalNumber++;
    }
    
    return String.format("%s%03d", prefix, number);
}

public static void bumpCounterForService(String service, String ticketId) {
    if (service.contains("Fast")) {
        nextFastNumber = Math.max(nextFastNumber, extractNumberFromTicket(ticketId) + 1);
    } else {
        nextNormalNumber = Math.max(nextNormalNumber, extractNumberFromTicket(ticketId) + 1);
    }
}
```

---

## CONCLUSION

The Cephra EV Charging Station Queue Management System successfully demonstrates the practical implementation of core data structures and algorithms in a real-world application. The system effectively manages the complex requirements of EV charging queue management through:

**Key Achievements:**
- **Efficient Queue Management**: FIFO algorithm with priority handling for emergency situations
- **Dynamic Resource Allocation**: Intelligent bay assignment based on availability and service requirements
- **Real-time Monitoring**: Live updates and synchronization across multiple interfaces
- **Secure Payment Processing**: Transaction integrity with database ACID compliance
- **Scalable Architecture**: Modular design supporting multiple charging stations and high user volumes

**Technical Implementation:**
- **Data Structures**: ArrayList for dynamic queue management, HashMap for data mapping, Boolean arrays for bay tracking
- **Algorithms**: Priority-based queue management, resource allocation, and real-time status updates
- **Integration**: Seamless Java desktop and web interface integration
- **Performance**: Optimized database operations with connection pooling and transaction management

This project showcases the practical application of computer science fundamentals in solving real-world problems, demonstrating the importance of efficient data structures and algorithms in modern software development.

---

**Document Version:** 1.0  
**Last Updated:** December 2024  
**Project Status:** Complete  
**Academic Institution:** National University - Mall of Asia (NU MOA)
