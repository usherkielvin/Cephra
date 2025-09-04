# 🧪 Cephra API Testing Guide

This document explains how to test the Cephra API endpoints and web interface functionality.

## 📋 Test Overview

The Cephra project includes comprehensive testing for:

- **Java Unit Tests** - Test API endpoint connectivity and responses
- **PHP API Tests** - Test server-side API functionality
- **JavaScript Integration Tests** - Test frontend API integration
- **Performance Tests** - Test API response times and performance

## 🚀 Quick Start

### Run All Tests
```bash
# Run the complete test suite
scripts/run-tests.bat
```

### Run Individual Tests
```bash
# Java unit tests only
mvn test

# PHP tests only
# Open: http://localhost/cephra/api/test-endpoints.php

# JavaScript tests only
# Open: http://localhost/cephra/mobileweb/?test=true
```

## 📁 Test Structure

```
src/test/java/cephra/api/
└── ApiEndpointTest.java          # Java unit tests

api/
└── test-endpoints.php            # PHP API tests

mobileweb/
└── test-api-integration.js       # JavaScript integration tests

scripts/
└── run-tests.bat                 # Test runner script
```

## 🔧 Test Types

### 1. Java Unit Tests (`ApiEndpointTest.java`)

**Purpose**: Test API endpoint connectivity and basic functionality

**Tests Include**:
- ✅ API Server Health Check
- ✅ Login Endpoint (Valid/Invalid Credentials)
- ✅ Queue Data Retrieval
- ✅ Ticket Creation
- ✅ Error Handling
- ✅ Response Time Performance
- ✅ CORS Headers

**Run Command**:
```bash
mvn test -Dtest=ApiEndpointTest
```

### 2. PHP API Tests (`test-endpoints.php`)

**Purpose**: Test server-side API functionality with real database

**Tests Include**:
- ✅ Database Connection
- ✅ Login Authentication
- ✅ Queue Data Operations
- ✅ Ticket Creation/Validation
- ✅ Error Handling
- ✅ HTML Page Rendering

**Access**: `http://localhost/cephra/api/test-endpoints.php`

### 3. JavaScript Integration Tests (`test-api-integration.js`)

**Purpose**: Test frontend API integration from browser perspective

**Tests Include**:
- ✅ API Connectivity
- ✅ Login Flow
- ✅ Queue Data Flow
- ✅ Ticket Creation Flow
- ✅ Error Handling
- ✅ CORS Headers
- ✅ Performance Testing

**Access**: `http://localhost/cephra/mobileweb/?test=true`

## 🎯 API Endpoints Tested

### Authentication
- `POST /api/mobile.php?action=login`
  - Valid credentials: `dizon` / `123`
  - Invalid credentials handling
  - Missing credentials handling

### Queue Management
- `GET /api/mobile.php?action=queue`
  - Retrieve all queue tickets
  - Data structure validation
  - Empty queue handling

### Ticket Operations
- `POST /api/mobile.php?action=create-ticket`
  - Valid ticket creation
  - Invalid data handling
  - Missing field validation

### Web Interface
- `GET /api/view-queue.php`
  - HTML page rendering
  - Content validation
  - Database integration

## 📊 Test Results

### Success Criteria
- ✅ All API endpoints respond correctly
- ✅ Authentication works with valid credentials
- ✅ Error handling works for invalid requests
- ✅ Response times under 5 seconds
- ✅ CORS headers present for web integration

### Test Reports
- **Java Tests**: Maven surefire reports in `target/surefire-reports/`
- **PHP Tests**: HTML report in browser
- **JavaScript Tests**: Console output and JSON report

## 🔧 Test Configuration

### Prerequisites
1. **Java 21+** - For running unit tests
2. **Maven 3.6+** - For building and testing
3. **PHP 8+** - For API server
4. **MySQL** - For database tests
5. **Web Server** - XAMPP or similar

### Environment Setup
1. Start the application: `scripts/run.bat`
2. Java Swing application is ready to run
3. Verify database connection
4. Run tests: `scripts/run-tests.bat`

## 🐛 Troubleshooting

### Common Issues

#### API Server Not Running
```
ERROR: API server may not be running
```
**Solution**: Start the application with `scripts/run.bat`

#### Database Connection Failed
```
Database connection failed
```
**Solution**: 
1. Check MySQL is running
2. Run `scripts/init-database.bat`
3. Verify database credentials in `config/database.php`

#### Tests Failing
```
Some tests failed
```
**Solution**:
1. Check API server logs
2. Verify database has test data
3. Check network connectivity
4. Review test output for specific errors

### Debug Mode
Enable debug logging in API files:
```php
error_reporting(E_ALL);
ini_set('display_errors', 1);
```

## 📈 Performance Testing

### Response Time Benchmarks
- **Login**: < 1 second
- **Queue Data**: < 2 seconds
- **Ticket Creation**: < 3 seconds
- **Page Loading**: < 5 seconds

### Load Testing
For production deployment, consider:
- Multiple concurrent users
- Database connection pooling
- API rate limiting
- Caching strategies

## 🔄 Continuous Integration

### Automated Testing
The test suite can be integrated into CI/CD pipelines:

```bash
# CI Pipeline Example
mvn clean test
# Run PHP tests
curl http://localhost/cephra/api/test-endpoints.php
# Run JavaScript tests
npm test  # If using Node.js
```

### Test Coverage
Current test coverage includes:
- ✅ API endpoint functionality
- ✅ Database operations
- ✅ Error handling
- ✅ Performance metrics
- ✅ Frontend integration

## 📝 Adding New Tests

### Java Tests
1. Add test methods to `ApiEndpointTest.java`
2. Use JUnit 5 annotations
3. Follow naming convention: `test[Feature][Scenario]`

### PHP Tests
1. Add test methods to `test-endpoints.php`
2. Use descriptive test names
3. Include both success and failure scenarios

### JavaScript Tests
1. Add test methods to `test-api-integration.js`
2. Use async/await for API calls
3. Include performance measurements

## 🎉 Test Success

When all tests pass, you'll see:
- ✅ Java tests: "BUILD SUCCESS"
- ✅ PHP tests: "All tests passed! Your API is working correctly."
- ✅ JavaScript tests: "All tests passed! Your API integration is working correctly."

## 📞 Support

For testing issues:
1. Check the troubleshooting section above
2. Review test output and error messages
3. Verify all prerequisites are installed
4. Check API server and database status

---

**Happy Testing!** 🧪✨
