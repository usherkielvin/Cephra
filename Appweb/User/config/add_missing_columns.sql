-- Add missing columns to otp_codes table
ALTER TABLE otp_codes
ADD COLUMN temp_token VARCHAR(64) NULL AFTER used,
ADD COLUMN password_updated TINYINT(1) NOT NULL DEFAULT 0 AFTER temp_token,
ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER password_updated;

-- Add indexes for better performance
CREATE INDEX idx_temp_token_used ON otp_codes(temp_token, used);
CREATE INDEX idx_username_used ON otp_codes(username, used);
