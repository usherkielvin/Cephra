# Cephra PHP Setup Guide

## Quick Setup

### 1. Database Setup
- Create MySQL database named `cephra`
- Import the SQL from `src/main/resources/db/init.sql`
- Update database credentials in `config/database.php` if needed

### 2. Web Server Setup
- Place files in your web server directory (e.g., `htdocs/cephra/`)
- Ensure PHP and MySQL are running

### 3. Test the System
- Open `http://localhost/cephra/phone/` in your browser
- Login with: `dizon` / `123`
- Create tickets and view queue

## File Structure
```
cephra/
├── config/
│   └── database.php          # Database connection
├── api/
│   └── index.php            # API endpoints
└── phone/
    └── index.php            # Phone interface
```

## API Endpoints
- `POST /api/login` - User authentication
- `GET /api/queue` - Get queue tickets
- `POST /api/create-ticket` - Create new ticket

## Database Tables Used
- `queue_tickets` - Stores charging tickets
- `users` - User accounts (optional)

## Features
- ✅ Simple login system
- ✅ View queue tickets
- ✅ Create new tickets
- ✅ Fast/Normal charging options
- ✅ Real-time queue updates
