@echo off
echo Compiling and running Cephra application...
javac -cp "src/main/java;src/main/resources" src/main/java/cephra/Launcher.java
java -cp "src/main/java;src/main/resources" cephra.Launcher
pause
