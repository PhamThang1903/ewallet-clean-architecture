CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE transfers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    source_wallet_id UUID NOT NULL,
    target_wallet_id UUID NOT NULL,
    amount NUMERIC(20, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'VND',
    description VARCHAR(500),
    idempotency_key VARCHAR(128) UNIQUE NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    current_step VARCHAR(50) NOT NULL DEFAULT 'VALIDATE',
    failure_reason TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_transfer_source_wallet ON transfers(source_wallet_id, created_at DESC);
CREATE INDEX idx_transfer_target_wallet ON transfers(target_wallet_id, created_at DESC);
CREATE INDEX idx_transfer_status ON transfers(status);
CREATE INDEX idx_transfer_idempotency_key ON transfers(idempotency_key);