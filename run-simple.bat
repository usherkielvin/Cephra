@echo off
echo Compiling and running Cephra application...
javac -cp . src/main/java/cephra/MainFrame.java src/main/java/cephra/AdminPanels/AdminLogin.java
java -cp src/main/java cephra.MainFrame
pause
