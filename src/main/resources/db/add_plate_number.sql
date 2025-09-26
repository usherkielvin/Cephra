-- Add plate_number column to users table
-- This script adds the plate_number column to support unique randomized plate numbers for each user
-- Plate numbers will have format: 3 random letters + 4 random digits (e.g., XYZ1234, ABC5678)

ALTER TABLE users 
ADD COLUMN plate_number VARCHAR(10) DEFAULT NULL UNIQUE;

-- Add index for better performance on plate number lookups
CREATE INDEX idx_users_plate_number ON users(plate_number);

-- Optional: Update existing users with random plate numbers
-- Uncomment the following lines if you want to generate plate numbers for existing users immediately
-- UPDATE users SET plate_number = CONCAT('NBH', LPAD(FLOOR(RAND() * 10000), 4, '0')) WHERE plate_number IS NULL;

COMMIT;