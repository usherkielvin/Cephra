@echo off
echo Compiling and running Cephra application with MySQL support...

REM Check if MySQL is running
echo Checking MySQL connection...
mysql --version > nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo WARNING: MySQL not found in PATH. Database features may not work correctly.
    echo Please run scripts/init-database.bat first if this is your first time running with MySQL.
    timeout /t 3
)

REM Compile and run the application using Maven
echo Compiling application with Maven...
call mvn clean compile

if %ERRORLEVEL% EQU 0 (
    echo Running application...
    call mvn exec:java -Dexec.mainClass="cephra.Launcher"
) else (
    echo Compilation failed! Please check the errors above.
)

pause
