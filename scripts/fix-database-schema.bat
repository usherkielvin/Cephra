@echo off
echo Adding missing columns to Cephra Database...

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

REM Run the schema fix script
echo Adding missing firstname and lastname columns...
"%XAMPP_MYSQL%" -u root cephradb < scripts\add-missing-columns.sql

if %ERRORLEVEL% EQU 0 (
    echo Database schema fix completed successfully!
    echo The firstname and lastname columns have been added.
    echo You can now use the Java application without errors.
) else (
    echo Failed to fix database schema. Please check that:
    echo 1. XAMPP is running and MySQL service is started
    echo 2. The cephradb database exists
    echo 3. You have proper MySQL permissions
)

pause
