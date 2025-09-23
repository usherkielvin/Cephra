# Cephra Monitor with Real-time Updates

This is the Monitor web application for Cephra with real-time updates using WebSockets.

## Features

- Real-time updates of bay status and queue information
- Automatic fallback to polling if WebSockets are not supported or connection fails
- Visual connection status indicator
- Responsive design for all device sizes
- Text-to-speech announcements for status changes
- Light/dark theme toggle
- Fullscreen mode for display monitors
- Progressive Web App (PWA) support

## Setup Instructions

### 1. Install Dependencies

First, install the required PHP dependencies using Composer:

```bash
cd /path/to/Cephra/Appweb/Monitor
composer install
```

### 2. Start the WebSocket Server

Start the WebSocket server in a terminal:

```bash
cd /path/to/Cephra/Appweb/Monitor
php bin/server.php
```

You should see output indicating that the server has started on port 8080.

### 3. Configure Your Web Server

Make sure your web server (Apache, Nginx, etc.) is configured to serve the Monitor application.

### 4. Access the Monitor

Open your web browser and navigate to the Monitor application URL. The application will automatically connect to the WebSocket server for real-time updates.

## Connection Status Indicator

The connection status is displayed in the toolbar:

- **Green dot**: Connected to WebSocket server (real-time updates)
- **Red dot**: Disconnected, using polling fallback
- **Gray dot**: Connecting or status unknown

## Troubleshooting

### WebSocket Connection Issues

- Ensure the WebSocket server is running
- Check that port 8080 is not blocked by a firewall
- Verify that your browser supports WebSockets
- Check the browser console for error messages

### Fallback Mechanism

If WebSockets are not available or the connection fails after multiple attempts, the application will automatically fall back to polling the API every 3 seconds.

## Development Notes

- The WebSocket server uses the Ratchet PHP library
- The server broadcasts updates only when data has changed
- The client automatically attempts to reconnect if the connection is lost