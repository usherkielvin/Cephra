@echo off
echo ========================================
echo Cephra Database Backup Script
echo ========================================
echo.

set backup_dir=backups
set timestamp=%date:~-4,4%-%date:~-10,2%-%date:~-7,2%_%time:~0,2%-%time:~3,2%-%time:~6,2%
set timestamp=%timestamp: =0%
set backup_file=%backup_dir%\cephradb_backup_%timestamp%.sql

echo [1/3] Creating backup directory...
if not exist "%backup_dir%" mkdir "%backup_dir%"

echo.
echo [2/3] Creating database backup...
echo Backup file: %backup_file%
echo.

mysqldump -u root --databases cephradb > "%backup_file%"
if %errorlevel% neq 0 (
    echo Error: Failed to create backup
    echo Make sure MySQL is running and accessible
    pause
    exit /b 1
)

echo.
echo [3/3] Backup completed successfully!
echo Backup saved to: %backup_file%
echo.
echo You can restore this backup later using:
echo mysql -u root -p ^< "%backup_file%"
echo.
pause
