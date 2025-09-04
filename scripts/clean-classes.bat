@echo off
echo Cleaning up class files from source directory...
del /s /q "src\*.class" 2>nul
echo Class files cleaned from source directory.
echo.
echo Note: Class files in target/ directory are normal Maven build artifacts.
echo They will be automatically hidden by .gitignore and .gitattributes.
pause
