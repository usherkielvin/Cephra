# Cephra Admin Web Panel

A comprehensive web-based admin interface for the Cephra charging station management system.

## Features

### 🔐 Authentication
- Secure admin login with session management
- Uses staff_records table from database
- Session-based authentication with automatic logout

### 📊 Dashboard
- Real-time system statistics
- Recent activity monitoring
- System status indicators
- Revenue tracking

### 🎫 Queue Management
- View all queue tickets
- Filter by status and service type
- Process tickets directly from the interface
- View detailed ticket information

### 🔋 Charging Bays Management
- Monitor all charging bays
- Set bays to maintenance mode
- View bay status and current users
- Real-time bay status updates

### 👥 User Management
- View all registered users
- Add new users
- Delete user accounts
- User activity tracking

### 📈 Analytics & Reports
- Revenue analytics
- Service usage statistics
- Performance metrics
- Custom reporting

### ⚙️ System Settings
- Configure pricing for services
- System configuration options
- Admin preferences

## Installation

1. **Place Files**: Copy the admin folder to your web server directory
2. **Database**: Ensure your database has the required tables
3. **Permissions**: Set proper file permissions for web access
4. **Access**: Navigate to `http://your-domain/admin/`

## File Structure

```
admin/
├── index.php          # Main admin dashboard
├── login.php          # Admin login page
├── logout.php         # Logout handler
├── css/
│   └── admin.css      # Admin panel styles
├── js/
│   └── admin.js       # Admin panel functionality
└── README.md          # This file

api/
└── admin.php          # Admin API endpoints
```

## API Endpoints

### Authentication Required
All endpoints require admin authentication via session.

### Dashboard
- `GET /api/admin.php?action=dashboard` - Get dashboard statistics

### Queue Management
- `GET /api/admin.php?action=queue` - Get all queue tickets
- `GET /api/admin.php?action=ticket-details&ticket_id=X` - Get ticket details
- `POST /api/admin.php` with `action=process-ticket&ticket_id=X` - Process ticket

### Bay Management
- `GET /api/admin.php?action=bays` - Get all charging bays
- `POST /api/admin.php` with `action=set-bay-maintenance&bay_number=X` - Set bay to maintenance
- `POST /api/admin.php` with `action=set-bay-available&bay_number=X` - Set bay to available

### User Management
- `GET /api/admin.php?action=users` - Get all users
- `POST /api/admin.php` with `action=add-user&...` - Add new user
- `POST /api/admin.php` with `action=delete-user&username=X` - Delete user

### Settings
- `GET /api/admin.php?action=settings` - Get current settings
- `POST /api/admin.php` with `action=save-settings&...` - Save settings

## Usage

### Accessing the Admin Panel
1. Navigate to `http://your-domain/admin/`
2. Login with your staff credentials from the database
3. Use the sidebar to navigate between sections

### Managing Queue
1. Go to "Queue Management"
2. View all tickets with their status
3. Use filters to find specific tickets
4. Click "Process" to handle tickets
5. View detailed ticket information

### Managing Bays
1. Go to "Charging Bays"
2. View all bay statuses
3. Set bays to maintenance when needed
4. Monitor current users and ticket assignments

### Adding Users
1. Go to "User Management"
2. Click "Add User" button
3. Fill in user details
4. Save to create new user account

## Security Features

- Session-based authentication
- CSRF protection
- Input validation
- SQL injection prevention
- XSS protection

## Browser Compatibility

- Chrome 60+
- Firefox 55+
- Safari 12+
- Edge 79+

## Mobile Responsive

The admin panel is fully responsive and works on:
- Desktop computers
- Tablets
- Mobile phones

## Integration

The admin web panel integrates seamlessly with:
- Java admin application
- Mobile web interface
- Database system
- API endpoints

## Troubleshooting

### Login Issues
- Verify staff credentials in staff_records table
- Check session configuration
- Ensure proper file permissions

### Data Not Loading
- Check database connection
- Verify API endpoints
- Check browser console for errors

### Styling Issues
- Clear browser cache
- Check CSS file permissions
- Verify file paths

## Support

For technical support or feature requests, please contact the development team.

## License

This admin panel is part of the Cephra charging station management system.
