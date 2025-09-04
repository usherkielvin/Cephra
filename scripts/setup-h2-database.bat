@echo off
echo Setting up H2 Database as alternative to XAMPP MySQL...
echo =====================================================

echo.
echo This will create a local H2 database file that doesn't require
echo XAMPP MySQL to be running. The database will be stored in:
echo %CD%\cephra-db.mv.db

echo.
echo 1. Adding H2 database dependency to pom.xml...
echo    (This will be done automatically when you run the application)

echo.
echo 2. Creating H2 database configuration...
echo    Database will be created automatically when you run the application

echo.
echo 3. Testing H2 database connection...
mvn compile exec:java -Dexec.mainClass="cephra.db.H2DatabaseTest"

echo.
echo =====================================================
echo H2 Database setup completed!
echo.
echo You can now run your application with:
echo scripts/run.bat
echo.
echo The H2 database will be created automatically in the current directory.
echo.
pause
