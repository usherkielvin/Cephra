# Cephra CT.WS - Web Interface

A complete web-based interface for the Cephra Electric Vehicle Charging Station Management System.

## 🚀 Quick Setup

1. **Run Setup Script**: Visit `http://localhost/Cephra/cephra.ct.ws/setup_cephra_ct_ws.php`
2. **Verify Database**: Ensure all tables are created and admin user exists
3. **Access Interfaces**: Use the provided URLs to access different parts of the system

## 📁 Directory Structure

```
cephra.ct.ws/
├── mobileweb/                 # Main web interface
│   ├── index.php             # Home page
│   ├── login.php             # User login
│   ├── dashboard.php         # User dashboard
│   ├── admin/                # Admin panel
│   │   ├── login.php         # Admin login
│   │   ├── index.php         # Admin dashboard
│   │   └── css/admin.css     # Admin styles
│   ├── monitor/              # Queue monitor
│   └── config/database.php   # Database configuration
├── setup_database.sql        # Database initialization
└── setup_cephra_ct_ws.php    # Setup verification script
```

## 🔗 Access URLs

### Main Interface
- **Home**: `http://localhost/Cephra/cephra.ct.ws/mobileweb/index.php`
- **Login**: `http://localhost/Cephra/cephra.ct.ws/mobileweb/login.php`
- **Register**: `http://localhost/Cephra/cephra.ct.ws/mobileweb/Register_Panel.php`

### Admin Panel
- **Admin Login**: `http://localhost/Cephra/cephra.ct.ws/mobileweb/admin/login.php`
- **Admin Dashboard**: `http://localhost/Cephra/cephra.ct.ws/mobileweb/admin/index.php`

### Monitor
- **Queue Monitor**: `http://localhost/Cephra/cephra.ct.ws/mobileweb/monitor/index.php`

## 🗄️ Database

- **Database**: `cephradb`
- **Host**: `localhost`
- **Username**: `root`
- **Password**: (empty)

### Required Tables
- `users` - User accounts
- `queue_tickets` - Charging requests
- `charging_bays` - Charging stations
- `staff_records` - Admin accounts
- `system_settings` - Configuration

## 👥 Default Accounts

### Admin Account
- **Username**: `admin`
- **Password**: `admin123`
- **Status**: `Active`

### Test User Account
- **Username**: `dizon`
- **Password**: `1234`
- **Email**: `dizon@cephra.com`

## 🔧 Features

### User Interface
- ✅ User registration and login
- ✅ Queue management
- ✅ Real-time updates
- ✅ Mobile responsive design
- ✅ Charging history
- ✅ Profile management

### Admin Interface
- ✅ Staff authentication
- ✅ Queue management
- ✅ Bay monitoring
- ✅ User administration
- ✅ System settings
- ✅ Analytics dashboard

### Monitor Interface
- ✅ Real-time queue display
- ✅ Public information display
- ✅ Station status updates

## 🛠️ Installation Steps

1. **Copy Files**: All files are already copied to `cephra.ct.ws/`
2. **Database Setup**: Run `setup_database.sql` in phpMyAdmin
3. **Verification**: Run `setup_cephra_ct_ws.php` to verify installation
4. **Access**: Use the provided URLs to access the interfaces

## 🔒 Security

- Session-based authentication
- Prepared statements (SQL injection protection)
- Input validation and sanitization
- CSRF protection
- Secure password handling

## 📱 Mobile Support

- Responsive design for all screen sizes
- Touch-friendly interface
- Progressive Web App (PWA) features
- Offline capability

## 🔄 Integration

This web interface integrates seamlessly with:
- Java admin application
- Database system
- Real-time updates
- Mobile devices

## 🆘 Troubleshooting

### Database Issues
- Ensure XAMPP MySQL is running
- Check database connection in `config/database.php`
- Verify all tables exist using setup script

### Login Issues
- Check admin credentials in `staff_records` table
- Verify user accounts in `users` table
- Clear browser cache and cookies

### Performance Issues
- Check database indexes
- Monitor server resources
- Optimize queries if needed

## 📞 Support

For technical support or questions about the Cephra CT.WS installation, please contact the development team.

---

**Cephra CT.WS** - Complete Web Interface for Electric Vehicle Charging Station Management