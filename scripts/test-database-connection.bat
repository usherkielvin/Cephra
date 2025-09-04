@echo off
echo Testing Cephra Database Connection...

REM Test database connection and table structure
C:\xampp\mysql\bin\mysql.exe -u root cephradb -e "SELECT 'Database Connection Test' as Test; SELECT COUNT(*) as 'Total Tables' FROM information_schema.tables WHERE table_schema = 'cephradb'; SELECT 'Charging History Records:' as Info; SELECT COUNT(*) as 'History Count' FROM charging_history; SELECT 'Queue Tickets:' as Info; SELECT COUNT(*) as 'Queue Count' FROM queue_tickets; SELECT 'Users:' as Info; SELECT COUNT(*) as 'User Count' FROM users;"

if %ERRORLEVEL% EQU 0 (
    echo Database connection test completed successfully!
) else (
    echo Database connection test failed!
)

pause
