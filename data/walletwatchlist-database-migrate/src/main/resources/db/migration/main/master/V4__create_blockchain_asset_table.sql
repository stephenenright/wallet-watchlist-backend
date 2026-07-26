-- Blockchain assets represent a currency deployed on a specific blockchain.
-- Native assets (ETH on Ethereum, BTC on Bitcoin) have contract_address = NULL and is_native = true.
-- Tokens (USDC, USDT) have contract_address set and is_native = false.
CREATE TABLE blockchain_asset (
    id               UUID PRIMARY KEY,
    currency_id      UUID NOT NULL,
    blockchain_id    UUID NOT NULL,
    contract_address VARCHAR(255),
    is_native        BOOLEAN NOT NULL DEFAULT false,
    date_created     TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    date_updated     TIMESTAMP(6) WITH TIME ZONE,
    CONSTRAINT fk_blockchain_asset_currency FOREIGN KEY (currency_id) REFERENCES currency (id),
    CONSTRAINT fk_blockchain_asset_blockchain FOREIGN KEY (blockchain_id) REFERENCES blockchain (id),
    CONSTRAINT uq_blockchain_asset_currency_blockchain UNIQUE (currency_id, blockchain_id)
);

CREATE INDEX idx_blockchain_asset_currency_id ON blockchain_asset (currency_id);
CREATE INDEX idx_blockchain_asset_blockchain_id ON blockchain_asset (blockchain_id);
CREATE INDEX idx_blockchain_asset_contract_address ON blockchain_asset (contract_address);

-- Seed blockchain assets

-- Ethereum native + tokens
INSERT INTO blockchain_asset (id, currency_id, blockchain_id, contract_address, is_native, date_created, date_updated) VALUES
    ('30000000-0000-0000-0001-000000000001', '10000000-0000-0000-0000-000000000002', '20000000-0000-0000-0000-000000000001', NULL, true, NOW(), NOW()),  -- ETH on Ethereum
    ('30000000-0000-0000-0001-000000000002', '10000000-0000-0000-0000-000000000003', '20000000-0000-0000-0000-000000000001', '0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48', false, NOW(), NOW()),  -- USDC on Ethereum
    ('30000000-0000-0000-0001-000000000003', '10000000-0000-0000-0000-000000000004', '20000000-0000-0000-0000-000000000001', '0xdAC17F958D2ee523a2206206994597C13D831ec7', false, NOW(), NOW()),  -- USDT on Ethereum
    ('30000000-0000-0000-0001-000000000004', '10000000-0000-0000-0000-000000000006', '20000000-0000-0000-0000-000000000001', '0x6B175474E89094C44Da98b954EedeeCB5BE5f855', false, NOW(), NOW()),  -- DAI on Ethereum
    ('30000000-0000-0000-0001-000000000005', '10000000-0000-0000-0000-000000000008', '20000000-0000-0000-0000-000000000001', '0x2260FAC5E5542a773Aa44fBCfeDf7C193bc2C599', false, NOW(), NOW());  -- WBTC on Ethereum

-- Bitcoin native
INSERT INTO blockchain_asset (id, currency_id, blockchain_id, contract_address, is_native, date_created, date_updated) VALUES
    ('30000000-0000-0000-0003-000000000001', '10000000-0000-0000-0000-000000000001', '20000000-0000-0000-0000-000000000003', NULL, true, NOW(), NOW());  -- BTC on Bitcoin
