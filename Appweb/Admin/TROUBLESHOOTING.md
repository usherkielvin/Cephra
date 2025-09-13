# 🔧 Cephra Web Admin Interface Troubleshooting Guide

## 🚨 Common Issues & Solutions

### Issue 1: Recent Activity Keeps Loading
**Symptoms:** The "Recent Activity" section shows a spinning loader indefinitely.

**Causes & Solutions:**
1. **Database Connection Failed**
   - ✅ **Solution:** Start XAMPP MySQL service
   - ✅ **Check:** Ensure database `cephradb` exists
   - ✅ **Verify:** Username `root` has access

2. **API Endpoint Not Responding**
   - ✅ **Solution:** Check `mobileweb/api/admin.php` file exists
   - ✅ **Verify:** File permissions are correct
   - ✅ **Test:** Visit `mobileweb/api/admin.php?action=dashboard` directly

3. **No Data in Database**
   - ✅ **Solution:** Add some test data to `queue_tickets` table
   - ✅ **Check:** Ensure tables exist with proper structure

### Issue 2: Navbar Buttons Not Switching Panels
**Symptoms:** Clicking sidebar buttons doesn't change the active panel.

**Causes & Solutions:**
1. **JavaScript Errors**
   - ✅ **Solution:** Open browser console (F12) and check for errors
   - ✅ **Fix:** Clear browser cache and refresh
   - ✅ **Check:** Ensure `admin.js` is loading properly

2. **jQuery Not Loaded**
   - ✅ **Solution:** Verify jQuery is included before admin.js
   - ✅ **Check:** Network tab shows jQuery loading successfully

3. **Event Listeners Not Attached**
   - ✅ **Solution:** Use debug script to re-initialize admin panel
   - ✅ **Fix:** Call `fixAdminIssues()` in browser console

### Issue 3: API Calls Failing
**Symptoms:** Network requests return errors or timeouts.

**Causes & Solutions:**
1. **PHP Configuration Issues**
   - ✅ **Solution:** Check PHP error logs
   - ✅ **Verify:** PHP extensions are enabled (PDO, MySQL)

2. **Database Connection Issues**
   - ✅ **Solution:** Test database connection directly
   - ✅ **Check:** MySQL service is running on port 3306

3. **File Permission Issues**
   - ✅ **Solution:** Ensure web server can read PHP files
   - ✅ **Check:** File permissions are set correctly

## 🛠️ Diagnostic Tools

### 1. Connection Test
Visit: `mobileweb/admin/test-connection.php`
- Tests database connection
- Verifies API endpoints
- Checks file permissions
- Validates session status

### 2. Issue Fixer
Visit: `mobileweb/admin/fix-admin-issues.php`
- Comprehensive diagnostic
- Step-by-step solutions
- API endpoint testing
- Browser troubleshooting

### 3. JavaScript Debug
- Open browser console (F12)
- Look for debug messages
- Use `debugAdminPanel()` function
- Call `fixAdminIssues()` for quick fixes

## 🔍 Step-by-Step Troubleshooting

### Step 1: Check Database
```bash
# Start XAMPP
# Open XAMPP Control Panel
# Start MySQL service
# Check if port 3306 is listening
```

### Step 2: Verify Database Structure
```sql
-- Connect to MySQL
USE cephradb;

-- Check if tables exist
SHOW TABLES;

-- Check queue_tickets table
SELECT COUNT(*) FROM queue_tickets;

-- Check users table
SELECT COUNT(*) FROM users;
```

### Step 3: Test API Endpoints
```bash
# Test dashboard endpoint
curl "http://localhost/cephra/mobileweb/api/admin.php?action=dashboard"

# Test queue endpoint
curl "http://localhost/cephra/mobileweb/api/admin.php?action=queue"
```

### Step 4: Check Browser Console
1. Open admin interface
2. Press F12 to open developer tools
3. Go to Console tab
4. Look for red error messages
5. Check Network tab for failed requests

### Step 5: Clear Browser Cache
- Press Ctrl+F5 (Windows) or Cmd+Shift+R (Mac)
- Or clear browser cache manually
- Try incognito/private mode

## 🚀 Quick Fixes

### Fix 1: Restart Everything
1. Stop XAMPP services
2. Start XAMPP services
3. Clear browser cache
4. Refresh admin interface

### Fix 2: Re-initialize Admin Panel
1. Open browser console (F12)
2. Type: `fixAdminIssues()`
3. Press Enter
4. Check if issues are resolved

### Fix 3: Check File Permissions
```bash
# Ensure files are readable
chmod 644 mobileweb/admin/*.php
chmod 644 mobileweb/api/*.php
chmod 644 mobileweb/config/*.php
```

### Fix 4: Database Reset
```sql
-- If database is corrupted
DROP DATABASE cephradb;
CREATE DATABASE cephradb;
-- Re-run database setup scripts
```

## 📋 Pre-flight Checklist

Before reporting issues, check:

- [ ] XAMPP is running
- [ ] MySQL service is started
- [ ] Database `cephradb` exists
- [ ] Admin is logged in
- [ ] Browser console shows no errors
- [ ] API endpoints respond correctly
- [ ] File permissions are correct
- [ ] Browser cache is cleared

## 🆘 Emergency Recovery

If nothing works:

1. **Complete Reset:**
   ```bash
   # Stop XAMPP
   # Delete database
   # Restart XAMPP
   # Recreate database
   # Re-run setup scripts
   ```

2. **Browser Reset:**
   - Clear all browser data
   - Disable extensions
   - Try different browser

3. **File Reset:**
   - Re-download admin files
   - Check file integrity
   - Verify file permissions

## 📞 Support Information

- **Admin Interface:** `mobileweb/admin/`
- **API Endpoints:** `mobileweb/api/admin.php`
- **Database Config:** `mobileweb/config/database.php`
- **Debug Tools:** `mobileweb/admin/test-connection.php`

## 🔗 Useful Links

- [Connection Test](test-connection.php)
- [Issue Fixer](fix-admin-issues.php)
- [Admin Login](login.php)
- [Admin Dashboard](index.php)

---

**Remember:** Most issues are caused by database connection problems or JavaScript errors. Always check the browser console first!
