@echo off
echo Fixing XAMPP MySQL Issues...
echo ============================

REM Run as administrator check
net session >nul 2>&1
if %errorLevel% == 0 (
    echo Running as Administrator - Good!
) else (
    echo WARNING: This script should be run as Administrator
    echo Right-click and "Run as Administrator"
    pause
    exit /b 1
)

echo.
echo 1. Stopping any running MySQL services...
net stop mysql >nul 2>&1
net stop MySQL80 >nul 2>&1
net stop MySQL57 >nul 2>&1
net stop MySQL56 >nul 2>&1
echo MySQL services stopped.

echo.
echo 2. Checking XAMPP MySQL installation...
set XAMPP_PATH=C:\xampp
if not exist "%XAMPP_PATH%\mysql\bin\mysql.exe" (
    echo ERROR: XAMPP MySQL not found at %XAMPP_PATH%
    echo Please make sure XAMPP is installed correctly.
    pause
    exit /b 1
)

echo.
echo 3. Backing up MySQL data directory...
if exist "%XAMPP_PATH%\mysql\data" (
    if not exist "%XAMPP_PATH%\mysql\data_backup" (
        echo Creating backup of MySQL data...
        xcopy "%XAMPP_PATH%\mysql\data" "%XAMPP_PATH%\mysql\data_backup" /E /I /H /Y >nul
        echo Backup created at %XAMPP_PATH%\mysql\data_backup
    ) else (
        echo Backup already exists
    )
)

echo.
echo 4. Checking for corrupted MySQL files...
if exist "%XAMPP_PATH%\mysql\data\mysql\ib_logfile0" (
    echo Removing potentially corrupted log files...
    del "%XAMPP_PATH%\mysql\data\mysql\ib_logfile0" >nul 2>&1
    del "%XAMPP_PATH%\mysql\data\mysql\ib_logfile1" >nul 2>&1
    del "%XAMPP_PATH%\mysql\data\mysql\ibdata1" >nul 2>&1
)

echo.
echo 5. Checking for port conflicts...
netstat -ano | findstr :3306
if %errorLevel% == 0 (
    echo WARNING: Port 3306 is still in use!
    echo You may need to restart your computer or kill the process manually.
) else (
    echo Port 3306 is available.
)

echo.
echo 6. Attempting to start XAMPP MySQL...
echo Starting MySQL service...
"%XAMPP_PATH%\mysql\bin\mysqld.exe" --defaults-file="%XAMPP_PATH%\mysql\bin\my.ini" --standalone

echo.
echo ============================
echo MySQL fix completed!
echo.
echo If MySQL still doesn't start:
echo 1. Restart your computer
echo 2. Try running XAMPP as Administrator
echo 3. Check Windows Event Viewer for more details
echo.
pause
