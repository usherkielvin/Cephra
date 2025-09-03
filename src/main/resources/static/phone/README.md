# Cephra Phone API Testing Guide

## 🚀 How to Test the Login Functionality

### 1. Start the Application
First, make sure your Cephra application is running. The API server should start on port 8080.

### 2. Access the Test Page
Open your web browser and navigate to:
```
http://localhost:8080/phone/test-login.html
```

### 3. Test Steps

#### Step 1: Test API Connection
- Click the **"Test API Connection"** button
- This tests if the server is responding to basic requests
- Expected result: ✅ API Connection Successful!

#### Step 2: Test Login with Known Credentials
- Use the pre-filled credentials: **Username: dizon, Password: 123**
- Click **"Test Login"** button
- Expected result: ✅ Login Successful!

#### Step 3: Test Queue Data
- Click **"Test Queue Data"** button
- This tests if you can fetch existing queue tickets
- Expected result: ✅ Queue Data Retrieved!

#### Step 4: Test Ticket Creation
- Click **"Test Create Ticket"** button
- This tests creating new tickets in the queue
- Expected result: ✅ Ticket Created Successfully!

#### Step 5: Quick Test
- Click **"Quick Test (dizon/123)"** button
- This runs all tests automatically with known working credentials
- Expected result: ✅ Quick Test Passed!

### 4. Console Logs
For detailed debugging:
1. Open Developer Tools (F12)
2. Go to Console tab
3. Watch for detailed logs of each API call

### 5. Expected Test Results

#### ✅ Successful Tests Should Show:
- API Connection: Working
- Login: Successful (dizon)
- Queue Data: Retrieved
- Ticket Creation: Working

#### ❌ Common Issues & Solutions:

**If API Connection Fails:**
- Check if the server is running on port 8080
- Verify no firewall is blocking the connection

**If Login Fails:**
- Verify the database connection is working
- Check if user "dizon" exists in the database
- Verify the password "123" is correct

**If Queue Data Fails:**
- Check database connection
- Verify queue_tickets table exists
- Check if there are any existing tickets

**If Ticket Creation Fails:**
- Verify database permissions
- Check if the ticket ID already exists
- Verify all required fields are being sent

### 6. Test Credentials

#### Working Test Account:
- **Username:** dizon
- **Password:** 123

#### Alternative Test Accounts:
You can also test with other credentials by changing the input fields.

### 7. API Endpoints Being Tested

- `POST /api/test-login` - Test connection
- `POST /api/login` - User authentication
- `GET /api/queue` - Fetch queue data
- `POST /api/create-ticket` - Create new tickets

### 8. Troubleshooting

If tests fail:
1. Check the browser console for error messages
2. Verify the server is running and accessible
3. Check database connectivity
4. Ensure all required tables exist
5. Verify the user credentials are correct

### 9. Next Steps

After successful testing:
1. Test the main phone interface: `http://localhost:8080/phone`
2. Try creating real tickets through the interface
3. Test the queue management system
4. Verify tickets appear in the admin panel

---

**Note:** This test page is for development and debugging purposes. Remove or secure it before production deployment.
