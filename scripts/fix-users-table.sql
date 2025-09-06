-- Fix existing users table to add missing firstname and lastname data
-- This script should be run after the database schema is updated

USE cephradb;

-- Update existing users that have NULL firstname or lastname
UPDATE users 
SET firstname = 'John', lastname = 'Doe' 
WHERE firstname IS NULL OR lastname IS NULL OR firstname = '' OR lastname = '';

-- Specifically update the test users if they exist
UPDATE users 
SET firstname = 'John', lastname = 'Dizon' 
WHERE username = 'dizon' AND (firstname IS NULL OR firstname = '');

UPDATE users 
SET firstname = 'Earnest', lastname = 'Smith' 
WHERE username = 'earnest' AND (firstname IS NULL OR firstname = '');

-- Verify the updates
SELECT username, firstname, lastname, email FROM users;
