@echo off
echo Initializing Cephra MySQL Database with XAMPP...

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

REM Create database and tables
echo Creating database and tables...
"%XAMPP_MYSQL%" -u root < src\main\resources\db\init.sql

if %ERRORLEVEL% EQU 0 (
    echo Database initialization completed successfully!
) else (
    echo Failed to initialize database. Please check that:
    echo 1. XAMPP is running and MySQL service is started
    echo 2. You have started the MySQL service in the XAMPP Control Panel
    echo 3. The MySQL port (3306) is not being used by another application
)

pause