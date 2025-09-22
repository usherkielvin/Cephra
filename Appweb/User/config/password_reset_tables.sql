-- Password Reset Tokens Table
-- Run this SQL in your MySQL database to create the necessary table

CREATE TABLE IF NOT EXISTS `password_reset_tokens` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `reset_code` varchar(6) NOT NULL,
  `temp_token` varchar(64) DEFAULT NULL,
  `expires_at` datetime NOT NULL,
  `used` tinyint(1) NOT NULL DEFAULT 0,
  `password_updated` tinyint(1) NOT NULL DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `reset_code` (`reset_code`),
  KEY `temp_token` (`temp_token`),
  KEY `expires_at` (`expires_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Add indexes for better performance
CREATE INDEX idx_email_used ON password_reset_tokens(email, used);
CREATE INDEX idx_temp_token_used ON password_reset_tokens(temp_token, used);
CREATE INDEX idx_expires_at ON password_reset_tokens(expires_at);

-- Optional: Add a cleanup job to remove expired tokens
-- You can run this query periodically (e.g., daily) to clean up expired tokens:
-- DELETE FROM password_reset_tokens WHERE expires_at < NOW() OR (used = 1 AND password_updated = 1);
