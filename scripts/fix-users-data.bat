@echo off
echo Fixing existing users data in Cephra Database...

REM Check if XAMPP MySQL is running
echo Checking XAMPP MySQL connection...
set XAMPP_MYSQL=C:\xampp\mysql\bin\mysql.exe

if exist "%XAMPP_MYSQL%" (
    echo Found XAMPP MySQL at %XAMPP_MYSQL%
) else (
    echo XAMPP MySQL not found at default location.
    echo Trying to use mysql from PATH...
    set XAMPP_MYSQL=mysql
)

REM Run the fix script
echo Applying user data fixes...
"%XAMPP_MYSQL%" -u root cephradb < scripts\fix-users-table.sql

if %ERRORLEVEL% EQU 0 (
    echo User data fix completed successfully!
    echo The firstname column error should now be resolved.
) else (
    echo Failed to fix user data. Please check that:
    echo 1. XAMPP is running and MySQL service is started
    echo 2. The database exists and has the users table
    echo 3. You may need to run scripts/init-database.bat first
)

pause
