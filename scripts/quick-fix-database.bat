@echo off
echo Quick Fix: Adding missing columns to Cephra Database...

REM Check if XAMPP MySQL is running
set XAMPP_MYSQL=C:\xampp\mysql\bin\mysql.exe

if exist "%XAMPP_MYSQL%" (
    echo Found XAMPP MySQL at %XAMPP_MYSQL%
) else (
    echo XAMPP MySQL not found. Trying mysql from PATH...
    set XAMPP_MYSQL=mysql
)

echo Adding firstname and lastname columns to users table...
"%XAMPP_MYSQL%" -u root cephradb -e "ALTER TABLE users ADD COLUMN firstname VARCHAR(50) NOT NULL DEFAULT 'User';"
"%XAMPP_MYSQL%" -u root cephradb -e "ALTER TABLE users ADD COLUMN lastname VARCHAR(50) NOT NULL DEFAULT 'User';"

echo Updating existing users with proper names...
"%XAMPP_MYSQL%" -u root cephradb -e "UPDATE users SET firstname = 'John', lastname = 'Dizon' WHERE username = 'dizon';"
"%XAMPP_MYSQL%" -u root cephradb -e "UPDATE users SET firstname = 'Earnest', lastname = 'Smith' WHERE username = 'earnest';"

echo Database fix completed! You can now:
echo 1. Register new users (username for login, firstname for display)
echo 2. Login with username
echo 3. See "Hello [firstname]" on dashboard

pause
