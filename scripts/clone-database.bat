@echo off
setlocal ENABLEDELAYEDEXPANSION
echo ========================================
echo Cephra - Clone MySQL Database
echo ========================================
echo.

REM ---- Configure connection (edit if needed) ----
set DB_HOST=localhost
set DB_PORT=3306
set DB_USER=root
set DB_PASS=
set SOURCE_DB=cephradb

REM ---- Resolve target DB name ----
set TARGET_DB=%1
if "%TARGET_DB%"=="" (
  for /f "tokens=1-4 delims=/ " %%a in ("%date%") do set d=%%d%%b%%c
  for /f "tokens=1-3 delims=:., " %%a in ("%time%") do set t=%%a%%b%%c
  set TARGET_DB=%SOURCE_DB%_clone_!d!!t!
)
set TARGET_DB=%TARGET_DB: =_%

echo Source DB : %SOURCE_DB%
echo Target DB : %TARGET_DB%
echo Host: %DB_HOST%  Port: %DB_PORT%
echo.

REM ---- Create target database ----
echo [1/4] Creating target database if not exists...
echo CREATE DATABASE IF NOT EXISTS `%TARGET_DB%` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci; > _createdb.sql
mysql -h %DB_HOST% -P %DB_PORT% -u %DB_USER% -p%DB_PASS% < _createdb.sql | cat > nul
del _createdb.sql > nul 2>&1
if errorlevel 1 (
  echo ERROR: Unable to create target database.
  exit /b 1
)

REM ---- Dump source schema + data ----
set backup_dir=backups
if not exist "%backup_dir%" mkdir "%backup_dir%"
set dump_file=%backup_dir%\%SOURCE_DB%_dump_for_%TARGET_DB%.sql
echo [2/4] Dumping source database to %dump_file% ...
mysqldump -h %DB_HOST% -P %DB_PORT% -u %DB_USER% -p%DB_PASS% ^
  --routines --triggers --events --single-transaction --set-gtid-purged=OFF ^
  %SOURCE_DB% > "%dump_file%"
if errorlevel 1 (
  echo ERROR: mysqldump failed.
  exit /b 1
)

REM ---- Import into target ----
echo [3/4] Importing into target database %TARGET_DB% ...
mysql -h %DB_HOST% -P %DB_PORT% -u %DB_USER% -p%DB_PASS% %TARGET_DB% < "%dump_file%"
if errorlevel 1 (
  echo ERROR: Import failed.
  exit /b 1
)

REM ---- Success ----
echo [4/4] Clone complete.
echo Created database: %TARGET_DB%
echo Dump file: %dump_file%
exit /b 0


