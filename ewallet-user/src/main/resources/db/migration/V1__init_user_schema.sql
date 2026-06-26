CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE users
(
    id                     UUID PRIMARY KEY             DEFAULT gen_random_uuid(),
    keycloak_user_id       VARCHAR(100) UNIQUE NOT NULL,
    phone_number_encrypted TEXT                NOT NULL,
    email_encrypted        TEXT                NOT NULL,
    phone_hash             VARCHAR(64) UNIQUE  NOT NULL,
    email_hash             VARCHAR(64) UNIQUE  NOT NULL,
    status                 VARCHAR(20)         NOT NULL DEFAULT 'ACTIVE',
    kyc_level              VARCHAR(10)         NOT NULL DEFAULT 'L0',
    created_at             TIMESTAMPTZ         NOT NULL DEFAULT NOW(),
    updated_at             TIMESTAMPTZ         NOT NULL DEFAULT NOW()
);

CREATE TABLE user_profiles
(
    id            UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    user_id       UUID        NOT NULL REFERENCES users (id),
    full_name     VARCHAR(255),
    date_of_birth DATE,
    gender        VARCHAR(20),
    avatar_url    TEXT,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE user_preferences
(
    id            UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    user_id       UUID        NOT NULL REFERENCES users (id),
    language      VARCHAR(10) NOT NULL DEFAULT 'vi',
    push_enabled  BOOLEAN     NOT NULL DEFAULT TRUE,
    email_enabled BOOLEAN     NOT NULL DEFAULT TRUE,
    sms_enabled   BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_users_keycloak_user_id ON users (keycloak_user_id);
CREATE INDEX idx_users_phone_hash ON users (phone_hash);
CREATE INDEX idx_users_email_hash ON users (email_hash);
CREATE INDEX idx_user_profiles_user_id ON user_profiles (user_id);
CREATE INDEX idx_user_preferences_user_id ON user_preferences (user_id);