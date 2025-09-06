@echo off
echo Adding demo ticket FCH001 for user dizon...

REM Check if XAMPP MySQL is running
set XAMPP_MYSQL=C:\xampp\mysql\bin\mysql.exe

if exist "%XAMPP_MYSQL%" (
    echo Found XAMPP MySQL at %XAMPP_MYSQL%
) else (
    echo XAMPP MySQL not found. Trying mysql from PATH...
    set XAMPP_MYSQL=mysql
)

echo Adding demo ticket FCH001 to queue_tickets table...
"%XAMPP_MYSQL%" -u root cephradb < scripts\add-demo-ticket.sql

if %ERRORLEVEL% EQU 0 (
    echo Demo ticket FCH001 added successfully!
    echo The ticket should now appear in the admin queue table.
    echo - Ticket ID: FCH001
    echo - User: dizon
    echo - Service: Fast Charging
    echo - Status: Pending
) else (
    echo Failed to add demo ticket. Please check that:
    echo 1. XAMPP is running and MySQL service is started
    echo 2. The cephradb database exists
    echo 3. The queue_tickets table exists
)

pause
