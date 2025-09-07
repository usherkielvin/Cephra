@echo off
echo Seeding waiting_grid and charging_grid tables in cephradb...

REM Locate mysql client (XAMPP default first, else PATH)
set XAMPP_MYSQL=C:\xampp\mysql\bin\mysql.exe
if exist "%XAMPP_MYSQL%" (
    set MYSQL_CMD="%XAMPP_MYSQL%"
) else (
    echo XAMPP MySQL not found at %XAMPP_MYSQL%. Using mysql from PATH.
    set MYSQL_CMD=mysql
)

%MYSQL_CMD% -u root cephradb < scripts\seed-grids.sql
if %ERRORLEVEL% EQU 0 (
    echo Seed completed successfully.
) else (
    echo Seed failed. Ensure MySQL is running and database cephradb exists.
)

pause


