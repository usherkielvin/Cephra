@echo off
echo Compiling and running Cephra application with MySQL support...

REM Check if MySQL is running
echo Checking MySQL connection...
mysql --version > nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo WARNING: MySQL not found in PATH. Database features may not work correctly.
    echo Please run init-database.bat first if this is your first time running with MySQL.
    timeout /t 3
)

REM Compile and run the application
echo Compiling application...
javac -cp "src/main/java;src/main/resources;lib/*" src/main/java/cephra/Launcher.java

echo Running application...
java -cp "src/main/java;src/main/resources;lib/*" cephra.Launcher

pause
