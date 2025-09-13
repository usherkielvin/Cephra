# 🌐 Cephra Web Application

## 📁 Clean Production Structure

```
Appweb/
├── Admin/           # Admin Dashboard
│   ├── api/         # Admin APIs
│   │   ├── admin.php           # Main admin API (with auth)
│   │   ├── admin-clean.php     # Clean admin API (no auth)
│   │   └── admin-complete.php  # Complete admin API (all features)
│   ├── config/      # Database configuration
│   ├── css/         # Admin stylesheets
│   ├── images/      # Admin images (logo, etc.)
│   ├── js/          # Admin JavaScript
│   ├── index.php    # Admin dashboard
│   ├── login.php    # Admin login
│   └── logout.php   # Admin logout
├── User/            # User Interface
│   ├── api/         # User APIs
│   ├── assets/      # User assets (CSS, JS, fonts)
│   ├── config/      # Database configuration
│   ├── css/         # User stylesheets
│   ├── images/      # User images
│   ├── *.php        # User pages (index, profile, history, etc.)
│   ├── *.js         # User scripts
│   └── sw.js        # Service worker (PWA)
├── Monitor/         # Live Monitor
│   ├── api/         # Monitor API
│   │   └── monitor.php         # Monitor data API
│   ├── index.php    # Monitor interface
│   └── monitor.webmanifest
└── README.md        # This file
```

## 🚀 Quick Access URLs

### **Admin Interface**
- **Login:** `http://localhost/Cephra/Appweb/Admin/login.php`
- **Dashboard:** `http://localhost/Cephra/Appweb/Admin/index.php`

### **User Interface**
- **Home:** `http://localhost/Cephra/Appweb/User/index.php`
- **Dashboard:** `http://localhost/Cephra/Appweb/User/dashboard.php`

### **Monitor Interface**
- **Live Monitor:** `http://localhost/Cephra/Appweb/Monitor/index.php`

## 🔧 Setup Instructions

1. **Start XAMPP:**
   - Start Apache and MySQL services
   - Ensure MySQL is running on port 3306

2. **Database Setup:**
   - Database will be created automatically when first accessed
   - Tables will be created by the APIs as needed

3. **Access Applications:**
   - Use the URLs above to access each interface
   - Admin login: username=`admin`, password=`admin123`

## 📋 Default Credentials

### **Admin Login**
- **Username:** `admin`
- **Password:** `admin123`

### **User Registration**
- Users can register through the User interface
- No default user credentials (registration required)

## 🛠️ Core Features

### **Admin Dashboard**
- ✅ Dashboard with real-time statistics
- ✅ Queue management with ticket processing
- ✅ Bay management (Available/Occupied/Maintenance)
- ✅ User management (add/delete users)
- ✅ Settings configuration (pricing)
- ✅ Monitor button (opens Monitor web in new tab)
- ✅ Auto-refresh every 30 seconds

### **User Interface**
- ✅ User registration and login
- ✅ Dashboard with personal statistics
- ✅ Queue ticket creation
- ✅ Charging bay booking
- ✅ History tracking
- ✅ Profile management
- ✅ PWA support (installable)

### **Monitor Interface**
- ✅ Live bay status monitoring (8 bays)
- ✅ Real-time queue display
- ✅ Bay announcer (voice announcements)
- ✅ Fullscreen mode
- ✅ Theme switching (light/dark)
- ✅ Auto-refresh every 3 seconds

## 🔌 API Endpoints

### **Admin APIs**
- `Admin/api/admin.php` - Main admin API (with authentication)
- `Admin/api/admin-clean.php` - Clean admin API (no authentication)
- `Admin/api/admin-complete.php` - Complete admin API (all features)

### **User APIs**
- `User/api/mobile.php` - Mobile user API

### **Monitor APIs**
- `Monitor/api/monitor.php` - Monitor data API (bays + queue)

## 🗣️ Bay Announcer Features

### **Voice Announcements**
- **Bay Status Changes:** "Bay X is now available/occupied/maintenance"
- **Waiting Tickets:** "Ticket TKT001" (simple announcement)
- **Bay Range:** Only announces bays 1-8
- **One-time:** Each ticket announced only once
- **Female Voice:** Optimized speech settings

### **Announcer Controls**
- Single "Bay Announcer" checkbox (checked by default)
- Controls all voice announcements
- Real-time updates every 3 seconds

## 📱 Mobile Support

- ✅ Responsive design for all interfaces
- ✅ PWA support for User interface
- ✅ Touch-friendly controls
- ✅ Mobile-optimized layouts
- ✅ Service worker for offline functionality

## 🎨 Themes

- ✅ Dark theme (default)
- ✅ Light theme (toggle available in Monitor)
- ✅ Consistent styling across all interfaces
- ✅ Enhanced "Available" status (larger, bold text)

## 🔄 Real-time Updates

- ✅ **Admin:** Auto-refresh every 30 seconds
- ✅ **Monitor:** Auto-refresh every 3 seconds
- ✅ **User:** Real-time updates as needed
- ✅ Live status updates
- ✅ Voice announcements for changes

## 🚨 Troubleshooting

### **Common Issues**
1. **Database Connection Failed**
   - Check XAMPP MySQL service is running
   - Verify MySQL is on port 3306

2. **Admin Login Issues**
   - Use: username=`admin`, password=`admin123`
   - Check database has `staff_records` table

3. **Monitor Not Showing Data**
   - Check Monitor API is accessible
   - Verify database has `charging_bays` table

4. **Bay Announcer Not Working**
   - Check browser supports Web Speech API
   - Ensure "Bay Announcer" checkbox is checked
   - Check browser console for errors

### **File Structure**
- All unnecessary test files and checkers have been removed
- Only essential flow files remain
- Clean, production-ready structure

## 📞 Support

For technical support:
1. Check the troubleshooting section above
2. Verify XAMPP services are running
3. Check browser console for errors
4. Ensure all URLs are accessible

---

**Last Updated:** December 2024  
**Version:** 2.0.0 (Clean Production Version)