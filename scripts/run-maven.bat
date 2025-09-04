@echo off
echo Compiling and running Cephra application with Maven...

REM Compile the application with Maven
echo Compiling application...
mvn clean compile

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed. Please check the errors above.
    pause
    exit /b 1
)

echo Compilation successful!

REM Run the application with Maven
echo Running application...
mvn exec:java -Dexec.mainClass="cephra.Launcher"

pause
