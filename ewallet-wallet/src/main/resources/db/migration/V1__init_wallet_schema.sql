CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE wallets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    wallet_number VARCHAR(20) UNIQUE NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'VND',
    balance NUMERIC(20, 2) NOT NULL DEFAULT 0,
    frozen_balance NUMERIC(20, 2) NOT NULL DEFAULT 0,
    wallet_type VARCHAR(20) NOT NULL DEFAULT 'main',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    version BIGINT NOT NULL DEFAULT 0,
    create_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    update_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT balance_non_negative CHECK ( balance > 0 ),
    CONSTRAINT frozen_non_negative CHECK ( frozen_balance > 0 )
);

CREATE TABLE wallet_transactions (
    id UUID NOT NULL DEFAULT gen_random_uuid(),
    idempotency_key VARCHAR(128) UNIQUE NOT NULL,
    wallet_id UUID NOT NULL,
    type VARCHAR(30) NOT NULL,
    category VARCHAR(30) NOT NULL,
    amount NUMERIC(20, 2) NOT NULL,
    balance_before NUMERIC(20, 2) NOT NULL,
    balance_after NUMERIC(20, 2) NOT NULL,
    reference_id UUID,
    description VARCHAR(500),
    metadata JSONB,
    status VARCHAR(20) NOT NULL  DEFAULT 'COMPLETED',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id, created_at)
) PARTITION BY RANGE (created_at);

CREATE TABLE wallet_transactions_2026_06 PARTITION OF wallet_transactions FOR VALUES FROM ('2026-06-01') TO ('2026-07-01');
CREATE TABLE wallet_transactions_2026_07 PARTITION OF wallet_transactions FOR VALUES FROM ('2026-07-01') TO ('2026-08-01');
CREATE INDEX idx_wallets_user_id ON wallets(user_id);
CREATE INDEX idx_transactions_wallet_id_created ON wallet_transactions(wallet_id, created_at DESC);
CREATE INDEX idx_transactions_reference_id ON wallet_transactions(reference_id);