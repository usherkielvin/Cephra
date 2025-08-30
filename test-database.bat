@echo off
echo Testing Cephra Database Connection...
echo =====================================

REM Compile and run the database test
echo Compiling database test...
javac -cp "target/classes;target/dependency/*" src/main/java/cephra/db/DatabaseTest.java

if %ERRORLEVEL% EQU 0 (
    echo Running database test...
    java -cp "target/classes;target/dependency/*" cephra.db.DatabaseTest
) else (
    echo Failed to compile. Trying with Maven...
    mvn compile exec:java -Dexec.mainClass="cephra.db.DatabaseTest"
)

echo.
echo =====================================
echo Database test completed.
pause
