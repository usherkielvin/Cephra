# 🧪 Cephra Testing Guide

This document explains how to test the Cephra EV charging management system components.

## 📋 Test Overview

The Cephra project includes testing for:

- **Java Application Testing** - Desktop application functionality
- **Web Interface Testing** - PHP web interface and API endpoints
- **Database Testing** - Data integrity and connection testing
- **Integration Testing** - End-to-end system testing

## 🚀 Quick Start

### Run Java Application Tests
```bash
# Build and test Java components
mvn clean test

# Run specific test classes
mvn test -Dtest=DatabaseConnectionTest
```

### Test Web Interface
```bash
# Start web server and test endpoints
# Open browser: http://localhost/cephra/Appweb/User/
# Test login with: dizon / 123
```

## 🔧 Java Application Testing

### Database Connection Testing
1. **Test Database Connection**
   - Launch the Java application
   - Check console for connection success messages
   - Verify database initialization completes

2. **Test Table Creation**
   - Run application first time
   - Check if all required tables are created
   - Verify data integrity constraints

### Admin Panel Testing
1. **Login Functionality**
   - Test admin login with valid credentials
   - Test login with invalid credentials
   - Verify session management

2. **Queue Management**
   - Create test queue tickets
   - Test queue status updates
   - Verify queue ordering and processing

3. **Staff Management**
   - Add new staff members
   - Test password reset functionality
   - Verify staff record management

### Customer Interface Testing
1. **User Registration**
   - Test new user registration
   - Verify email validation
   - Test duplicate username handling

2. **Queue Joining**
   - Test joining queue with different services
   - Verify battery level tracking
   - Test queue position updates

3. **Profile Management**
   - Test profile information updates
   - Verify battery level management
   - Test charging history viewing

## 🌐 Web Interface Testing

### API Endpoint Testing

#### User API (`/api/mobile.php`)
```php
// Test login endpoint
POST /api/mobile.php
Content-Type: application/json

{
    "action": "login",
    "username": "dizon",
    "password": "123"
}
```

#### Admin API (`/api/admin.php`)
```php
// Test admin login
POST /api/admin.php
Content-Type: application/json

{
    "action": "admin_login",
    "username": "admin",
    "password": "admin123"
}
```

### Web Interface Testing
1. **Customer Interface** (`/Appweb/User/`)
   - Test responsive design on mobile devices
   - Verify real-time queue updates
   - Test ticket creation and management
   - Check user authentication flow

2. **Admin Interface** (`/Appweb/Admin/`)
   - Test admin dashboard functionality
   - Verify queue management features
   - Test staff management operations
   - Check system monitoring capabilities

3. **Monitor Display** (`/Appweb/Monitor/`)
   - Test public display functionality
   - Verify queue status display
   - Test real-time updates
   - Check information accuracy

## 🗄️ Database Testing

### Data Integrity Testing
1. **User Data**
   - Test user registration data validation
   - Verify password hashing
   - Test user profile updates

2. **Queue Management**
   - Test queue ticket creation
   - Verify queue ordering logic
   - Test status updates and transitions

3. **Transaction Records**
   - Test charging history recording
   - Verify payment transaction logging
   - Test data consistency across tables

### Connection Testing
1. **Java Database Connection**
   - Test connection pooling
   - Verify connection timeout handling
   - Test reconnection after connection loss

2. **PHP Database Connection**
   - Test PDO connection handling
   - Verify error handling
   - Test transaction management

## 🔄 Integration Testing

### End-to-End Testing Scenarios

#### Scenario 1: Complete Charging Process
1. User registers via web interface
2. User joins queue via mobile app
3. Admin processes queue ticket
4. Charging session completes
5. Payment processed
6. History recorded

#### Scenario 2: Multi-User Queue Management
1. Multiple users join queue
2. Admin manages queue priorities
3. Bay assignments processed
4. Real-time updates across interfaces
5. Queue status synchronization

#### Scenario 3: System Recovery
1. Simulate database connection loss
2. Test automatic reconnection
3. Verify data consistency
4. Test error handling and recovery

## 🛠️ Test Data Setup

### Sample Test Users
```sql
-- Test customer user
INSERT INTO users (username, email, password, firstname, lastname) 
VALUES ('testuser', 'test@example.com', 'hashed_password', 'Test', 'User');

-- Test admin user
INSERT INTO staff_records (username, password, role) 
VALUES ('testadmin', 'hashed_password', 'admin');
```

### Sample Test Data
```sql
-- Test queue tickets
INSERT INTO queue_tickets (username, service_type, status, created_at) 
VALUES ('testuser', 'fast_charge', 'pending', NOW());

-- Test battery levels
INSERT INTO battery_levels (username, battery_level, initial_battery_level) 
VALUES ('testuser', 45, 45);
```

## 📊 Performance Testing

### Load Testing
1. **Concurrent Users**
   - Test with multiple simultaneous users
   - Verify system performance under load
   - Check database connection limits

2. **Queue Processing**
   - Test large queue processing
   - Verify response times
   - Check memory usage

### Response Time Testing
1. **API Response Times**
   - Measure endpoint response times
   - Test under various load conditions
   - Verify acceptable performance thresholds

2. **Database Query Performance**
   - Test complex query execution times
   - Verify index usage
   - Check query optimization

## 🐛 Bug Testing

### Common Issues to Test
1. **Network Connectivity**
   - Test with poor network conditions
   - Verify offline functionality
   - Test reconnection scenarios

2. **Data Validation**
   - Test with invalid input data
   - Verify error handling
   - Check data sanitization

3. **Edge Cases**
   - Test with empty databases
   - Verify null value handling
   - Test boundary conditions

## 📝 Test Documentation

### Test Results Recording
1. **Test Execution Log**
   - Record test execution times
   - Document test results
   - Track bug reports

2. **Performance Metrics**
   - Record response times
   - Track resource usage
   - Monitor system performance

### Bug Reporting
1. **Bug Report Template**
   - Description of the issue
   - Steps to reproduce
   - Expected vs actual behavior
   - System environment details

2. **Issue Tracking**
   - Assign priority levels
   - Track resolution status
   - Document fixes and workarounds

## 🔍 Automated Testing

### Unit Tests
```java
// Example Java unit test
@Test
public void testDatabaseConnection() {
    Connection conn = DatabaseConnection.getConnection();
    assertNotNull(conn);
    // Additional assertions
}
```

### API Tests
```php
// Example PHP API test
public function testLoginEndpoint() {
    $response = $this->post('/api/mobile.php', [
        'action' => 'login',
        'username' => 'testuser',
        'password' => 'testpass'
    ]);
    
    $this->assertEquals(200, $response->getStatusCode());
    // Additional assertions
}
```

## 📋 Testing Checklist

### Pre-Release Testing
- [ ] All Java components tested
- [ ] Web interface functionality verified
- [ ] Database operations tested
- [ ] API endpoints validated
- [ ] Performance benchmarks met
- [ ] Security vulnerabilities checked
- [ ] Cross-platform compatibility verified
- [ ] Documentation updated

### Post-Release Testing
- [ ] Production environment tested
- [ ] User acceptance testing completed
- [ ] Performance monitoring active
- [ ] Error logging functional
- [ ] Backup and recovery tested

## 🆘 Troubleshooting Test Issues

### Common Test Failures
1. **Database Connection Issues**
   - Check MySQL service status
   - Verify connection credentials
   - Test network connectivity

2. **Build Failures**
   - Check Java version compatibility
   - Verify Maven dependencies
   - Clean and rebuild project

3. **Web Interface Issues**
   - Check web server status
   - Verify PHP configuration
   - Test browser compatibility

## 📚 Additional Resources

- [Java Testing Best Practices](https://docs.oracle.com/javase/tutorial/java/javaOO/testing.html)
- [PHP Testing Guide](https://phpunit.readthedocs.io/)
- [MySQL Testing Documentation](https://dev.mysql.com/doc/mysql-test-suite/)
- [Web Testing Tools](https://developer.mozilla.org/en-US/docs/Learn/Tools_and_testing)