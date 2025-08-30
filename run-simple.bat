@echo off
echo Compiling and running Cephra application...

REM Check if MySQL is running
echo Checking MySQL connection...
mysql --version > nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo WARNING: MySQL not found in PATH. Database features may not work correctly.
    echo Please run init-database.bat first if this is your first time running with MySQL.
    timeout /t 3
)

REM Create target directory if it doesn't exist
if not exist "target\classes" mkdir "target\classes"

REM Compile Java files with classpath
echo Compiling Java files...
javac -cp ".;lib/*" -d target\classes src\main\java\cephra\*.java src\main\java\cephra\Admin\*.java src\main\java\cephra\Phone\*.java src\main\java\cephra\Frame\*.java src\main\java\cephra\db\*.java

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
    echo Running application...
    java -cp "target\classes;lib/*" cephra.Launcher
) else (
    echo Compilation failed! Please check the errors above.
)

pause
