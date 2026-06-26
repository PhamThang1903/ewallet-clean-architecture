ALTER TABLE users
ADD COLUMN phone_verified BOOLEAN NOT NULL DEFAULT FALSE,
ADD COLUMN email_verified BOOLEAN NOT NULL DEFAULT FALSE;

CREATE INDEX idx_users_phone_verified ON users(phone_verified);
CREATE INDEX idx_users_email_verified ON users(email_verified);