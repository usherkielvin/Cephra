@echo off
echo Checking for MySQL services and port conflicts...
echo ================================================

echo.
echo 1. Checking for running MySQL services...
sc query mysql
sc query "MySQL80"
sc query "MySQL57"
sc query "MySQL56"

echo.
echo 2. Checking what's using port 3306...
netstat -ano | findstr :3306

echo.
echo 3. Checking XAMPP MySQL status...
if exist "C:\xampp\mysql\bin\mysql.exe" (
    echo XAMPP MySQL found at C:\xampp\mysql\bin\mysql.exe
) else (
    echo XAMPP MySQL not found at default location
)

echo.
echo 4. Checking XAMPP MySQL data directory...
if exist "C:\xampp\mysql\data" (
    echo MySQL data directory exists
    dir "C:\xampp\mysql\data" | findstr "mysql"
) else (
    echo MySQL data directory not found
)

echo.
echo ================================================
echo If you see another MySQL service running above,
echo you need to stop it before starting XAMPP MySQL.
echo.
echo To stop Windows MySQL service, run as Administrator:
echo net stop mysql
echo net stop MySQL80
echo net stop MySQL57
echo net stop MySQL56
echo.
pause
