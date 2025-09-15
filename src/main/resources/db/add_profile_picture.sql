-- Add profile picture support to users table
-- This script adds a profile_picture column to store Base64 encoded profile images

USE cephradb;

-- Add profile_picture column if it doesn't exist
SET @col_exists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = 'cephradb'
    AND TABLE_NAME = 'users'
    AND COLUMN_NAME = 'profile_picture'
);

SET @sql = IF(@col_exists = 0, 
    'ALTER TABLE users ADD COLUMN profile_picture LONGTEXT NULL AFTER email', 
    'SELECT "Column profile_picture already exists" AS message'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Show the updated table structure
DESCRIBE users;
