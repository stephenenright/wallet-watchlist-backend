-- Wallets on blockchain networks.
CREATE TABLE wallet (
    id                 UUID PRIMARY KEY,
    address            VARCHAR(255) NOT NULL,
    blockchain_id      UUID NOT NULL,
    status             VARCHAR(50) NOT NULL,
    sync_status        VARCHAR(50) NOT NULL,
    sync_retry_count   INTEGER NOT NULL DEFAULT 0,
    balance_usd        NUMERIC(18, 2),
    date_last_synced   TIMESTAMP(6) WITH TIME ZONE,
    date_last_activity TIMESTAMP(6) WITH TIME ZONE,
    date_created       TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    date_updated       TIMESTAMP(6) WITH TIME ZONE,
    CONSTRAINT fk_wallet_blockchain FOREIGN KEY (blockchain_id) REFERENCES blockchain (id),
    CONSTRAINT uq_wallet_address_blockchain UNIQUE (address, blockchain_id)
);

CREATE INDEX idx_wallet_blockchain_id ON wallet (blockchain_id);
CREATE INDEX idx_wallet_status ON wallet (status);
CREATE INDEX idx_wallet_sync_status ON wallet (sync_status);

-- Seed wallets

-- Ethereum wallets (balance_usd based on ~$3,200 per ETH)
INSERT INTO wallet (id, address, blockchain_id, status, sync_status, sync_retry_count, balance_usd, date_last_synced, date_last_activity, date_created, date_updated) VALUES
    ('40000000-0000-0000-0001-000000000001', '0xd8dA6BF26964aF9D7eEd9e03E53415D37aA96045', '20000000-0000-0000-0000-000000000001', 'ACTIVE', 'SYNCED', 0, 21221.67, NOW(), NOW(), NOW(), NOW()),  -- Vitalik Buterin
    ('40000000-0000-0000-0001-000000000002', '0xde0B295669a9FD93d5F28D9Ec85E40f4cb697BAe', '20000000-0000-0000-0000-000000000001', 'ACTIVE', 'SYNCED', 0, 28078358.45, NOW(), NOW(), NOW(), NOW()),  -- Ethereum Foundation
    ('40000000-0000-0000-0001-000000000003', '0xBE0eB53F46cd790Cd13851d5EFf43D12404d33E8', '20000000-0000-0000-0000-000000000001', 'ACTIVE', 'SYNCED', 0, 6387226834.49, NOW(), NOW(), NOW(), NOW());  -- Binance Cold Wallet

-- Bitcoin wallets (no balance data seeded yet)
INSERT INTO wallet (id, address, blockchain_id, status, sync_status, sync_retry_count, balance_usd, date_last_synced, date_last_activity, date_created, date_updated) VALUES
    ('40000000-0000-0000-0003-000000000001', '1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa', '20000000-0000-0000-0000-000000000003', 'ACTIVE', 'SYNCED', 0, NULL, NOW(), NOW(), NOW(), NOW()),  -- Satoshi Genesis
    ('40000000-0000-0000-0003-000000000002', '34xp4vRoCGJym3xR7yCVPFHoCNxv4Twseo', '20000000-0000-0000-0000-000000000003', 'ACTIVE', 'SYNCED', 0, NULL, NOW(), NOW(), NOW(), NOW()),  -- Binance Cold
    ('40000000-0000-0000-0003-000000000003', 'bc1qgdjqv0av3q56jvd82tkdjpy7gdp9ut8tlqmgrpmv24sq90ecnvqqjwvw97', '20000000-0000-0000-0000-000000000003', 'ACTIVE', 'SYNCED', 0, NULL, NOW(), NOW(), NOW(), NOW());  -- Bitfinex Cold
