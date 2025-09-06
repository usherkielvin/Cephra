-- Add missing firstname and lastname columns to existing users table
USE cephradb;

-- Check if columns already exist before adding them
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'cephradb' 
     AND TABLE_NAME = 'users' 
     AND COLUMN_NAME = 'firstname') = 0,
    'ALTER TABLE users ADD COLUMN firstname VARCHAR(50) NOT NULL DEFAULT "User"',
    'SELECT "firstname column already exists" as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'cephradb' 
     AND TABLE_NAME = 'users' 
     AND COLUMN_NAME = 'lastname') = 0,
    'ALTER TABLE users ADD COLUMN lastname VARCHAR(50) NOT NULL DEFAULT "User"',
    'SELECT "lastname column already exists" as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Update existing users with proper names
UPDATE users SET firstname = 'John', lastname = 'Dizon' WHERE username = 'dizon';
UPDATE users SET firstname = 'Earnest', lastname = 'Smith' WHERE username = 'earnest';

-- Show the updated table structure
DESCRIBE users;

-- Show current users
SELECT username, firstname, lastname, email FROM users;
