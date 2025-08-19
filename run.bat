@echo off
echo Compiling and running Cephra application...
"C:\Program Files\NetBeans-21\netbeans\java\maven\bin\mvn.cmd" clean compile
"C:\Program Files\NetBeans-21\netbeans\java\maven\bin\mvn.cmd" exec:java
pause
