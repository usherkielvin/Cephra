@echo off
echo Setting up MySQL JDBC Driver...

REM Create lib directory if it doesn't exist
if not exist "lib" mkdir "lib"

REM Check if MySQL JDBC driver already exists
if exist "lib\mysql-connector-java-8.0.33.jar" (
    echo MySQL JDBC driver already exists.
    goto :end
)

REM Download MySQL JDBC driver
echo Downloading MySQL JDBC driver...
powershell -Command "& {Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar' -OutFile 'lib\mysql-connector-java-8.0.33.jar'}"

if exist "lib\mysql-connector-java-8.0.33.jar" (
    echo MySQL JDBC driver downloaded successfully!
) else (
    echo Failed to download MySQL JDBC driver.
    echo Please manually download mysql-connector-java-8.0.33.jar and place it in the lib folder.
)

:end
echo Setup complete!
pause
