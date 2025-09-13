# 🌐 Cephra Appweb Structure

## 📁 New Folder Organization

The appweb has been reorganized into three main sections:

### 🔧 **Appweb/Admin**
Contains all administrative interface files:
- **Admin Interface:** `index.php`, `login.php`, `logout.php`
- **API Files:** `api/admin.php`, `api/admin-clean.php`, `api/admin-test.php`
- **Tools:** Database fixers, API testers, connection tools
- **Assets:** CSS, JavaScript files for admin interface
- **Config:** Database configuration

**Key Files:**
- `index.php` - Main admin dashboard
- `api/admin-clean.php` - Clean API without authentication
- `database-fixer.php` - Database connection fixer
- `api-tester.php` - API endpoint tester

### 👥 **Appweb/User**
Contains all user-facing website files:
- **User Interface:** `index.php`, `profile.php`, `history.php`
- **Charging:** `ChargingPage.php`, `queue_ticket_popup.php`
- **Registration:** `Register_Panel.php`, `register_script.js`
- **Assets:** CSS, JavaScript, images, webfonts
- **Config:** Database configuration

**Key Files:**
- `index.php` - Main user dashboard
- `profile.php` - User profile management
- `history.php` - Charging history
- `ChargingPage.php` - Charging interface

### 📊 **Appweb/Monitor**
Contains monitoring interface files:
- **Monitor Interface:** `index.php`
- **Monitor Manifest:** `monitor.webmanifest`

## 🔗 **Access URLs**

### Admin Interface
- **Main Admin:** `http://localhost/Cephra/Appweb/Admin/`
- **API Test:** `http://localhost/Cephra/Appweb/Admin/api-tester.php`
- **Database Fix:** `http://localhost/Cephra/Appweb/Admin/database-fixer.php`

### User Interface
- **Main User:** `http://localhost/Cephra/Appweb/User/`
- **Profile:** `http://localhost/Cephra/Appweb/User/profile.php`
- **History:** `http://localhost/Cephra/Appweb/User/history.php`

### Monitor Interface
- **Monitor:** `http://localhost/Cephra/Appweb/Monitor/`

## 🔧 **Configuration**

Each folder has its own `config/database.php` file for database connections.

## 📋 **Migration Notes**

- All files have been copied (not moved) from the original `mobileweb` folder
- Original `mobileweb` folder remains intact for backup
- File paths in PHP files may need updating for the new structure
- Database connections should work with the new config files

## 🚀 **Next Steps**

1. Test all interfaces with the new URLs
2. Update any hardcoded file paths in PHP files
3. Test database connections in each folder
4. Remove original `mobileweb` folder if everything works correctly
