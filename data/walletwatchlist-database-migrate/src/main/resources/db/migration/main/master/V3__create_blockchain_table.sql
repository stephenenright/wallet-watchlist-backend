-- Blockchain networks.
CREATE TABLE blockchain (
    id              UUID PRIMARY KEY,
    code            VARCHAR(50) NOT NULL,
    name            VARCHAR(255) NOT NULL,
    mainnet         VARCHAR(255) NOT NULL,
    native_currency VARCHAR(50) NOT NULL,
    date_created    TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    date_updated    TIMESTAMP(6) WITH TIME ZONE,
    CONSTRAINT uq_blockchain_code UNIQUE (code)
);

-- Seed blockchains
INSERT INTO blockchain (id, code, name, mainnet, native_currency, date_created, date_updated) VALUES
    ('20000000-0000-0000-0000-000000000001', 'ETHEREUM', 'Ethereum', 'mainnet', 'ETH', NOW(), NOW()),
    ('20000000-0000-0000-0000-000000000003', 'BITCOIN', 'Bitcoin', 'mainnet', 'BTC', NOW(), NOW()),
    ('20000000-0000-0000-0000-000000000004', 'ARBITRUM', 'Arbitrum One', 'mainnet', 'ETH', NOW(), NOW()),
    ('20000000-0000-0000-0000-000000000005', 'OPTIMISM', 'Optimism', 'mainnet', 'ETH', NOW(), NOW()),
    ('20000000-0000-0000-0000-000000000006', 'BASE', 'Base', 'mainnet', 'ETH', NOW(), NOW());
