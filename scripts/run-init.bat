@echo off
echo Initializing Cephra Database...
echo.

REM Check if MySQL is running
mysql -u root -p -e "SELECT 1;" >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: MySQL is not running or not accessible.
    echo Please start XAMPP MySQL service first.
    pause
    exit /b 1
)

echo MySQL is running. Initializing database...
echo.

REM Run the initialization script
mysql -u root -p cephradb < src\main\resources\db\init.sql

if %errorlevel% equ 0 (
    echo.
    echo Database initialization completed successfully!
    echo All tables have been created and populated with initial data.
) else (
    echo.
    echo Error: Database initialization failed.
    echo Please check your MySQL connection and try again.
)

echo.
pause
