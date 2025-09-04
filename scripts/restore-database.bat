@echo off
echo ========================================
echo Cephra Database Restore Script
echo ========================================
echo.

if "%~1"=="" (
    echo Usage: restore-database.bat [backup_file.sql]
    echo.
    echo Available backup files:
    if exist "backups\*.sql" (
        dir /b "backups\*.sql"
    ) else (
        echo No backup files found in backups directory
    )
    echo.
    pause
    exit /b 1
)

set backup_file=%~1

echo [1/4] Checking if backup file exists...
if not exist "%backup_file%" (
    echo Error: Backup file not found: %backup_file%
    pause
    exit /b 1
)

echo Backup file: %backup_file%
echo.

echo [2/4] Stopping MySQL service...
net stop mysql
if %errorlevel% neq 0 (
    echo Warning: Could not stop MySQL service. It might already be stopped.
)

echo.
echo [3/4] Waiting for MySQL to stop...
timeout /t 3 /nobreak > nul

echo.
echo [4/4] Starting MySQL service...
net start mysql
if %errorlevel% neq 0 (
    echo Error: Failed to start MySQL service
    pause
    exit /b 1
)

echo.
echo [5/5] Restoring database from backup...
echo This will overwrite existing data!
echo.
set /p confirm="Are you sure you want to restore from backup? (y/N): "
if /i not "%confirm%"=="y" (
    echo Operation cancelled.
    pause
    exit /b 1
)

echo.
echo Restoring database...
mysql -u root < "%backup_file%"
if %errorlevel% neq 0 (
    echo Error: Failed to restore database
    pause
    exit /b 1
)

echo.
echo ========================================
echo Database restore completed successfully!
echo ========================================
echo.
pause
