@echo off
echo =====================================================
echo Cephra API Endpoint Tests
echo =====================================================
echo.

REM Check if Java is available
java -version > nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 21 or higher
    pause
    exit /b 1
)

REM Check if Maven is available
mvn -version > nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven 3.6 or higher
    pause
    exit /b 1
)

echo Starting API tests...
echo.

REM Run Java unit tests
echo 1. Running Java Unit Tests...
echo ================================
mvn test -Dtest=ApiEndpointTest
if %ERRORLEVEL% NEQ 0 (
    echo WARNING: Some Java tests failed
    echo.
) else (
    echo Java tests completed successfully
    echo.
)

REM Check if API server is running
echo 2. Checking API Server Status...
echo ================================
curl -s http://localhost:8080/api/health > nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo WARNING: API server may not be running
    echo Please start the application with: scripts/run.bat
    echo.
) else (
    echo API server is running
    echo.
)

REM Run PHP tests
echo 3. Running PHP API Tests...
echo ================================
echo Opening PHP test page in browser...
start http://localhost:8080/api/test-endpoints.php
echo.
echo PHP tests are running in your browser
echo Check the results in the opened page
echo.

REM Run JavaScript tests
echo 4. JavaScript Integration Tests...
echo ================================
echo Opening JavaScript test page...
start http://localhost:8080/phone/?test=true
echo.
echo JavaScript tests are running in your browser
echo Check the browser console for results
echo.

echo =====================================================
echo Test Summary
echo =====================================================
echo.
echo Tests completed:
echo - Java Unit Tests: Check Maven output above
echo - PHP API Tests: Check browser window
echo - JavaScript Tests: Check browser console
echo.
echo For detailed results, check:
echo - Maven test reports: target/surefire-reports/
echo - PHP test page: http://localhost:8080/api/test-endpoints.php
echo - JavaScript tests: Browser console
echo.
pause
