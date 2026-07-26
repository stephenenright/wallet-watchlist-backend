-- Currencies (abstract concept: BTC, ETH, USDC, etc.).
CREATE TABLE currency (
    id           UUID PRIMARY KEY,
    symbol       VARCHAR(255) NOT NULL,
    name         VARCHAR(255) NOT NULL,
    date_created TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    date_updated TIMESTAMP(6) WITH TIME ZONE,
    CONSTRAINT uq_currency_symbol UNIQUE (symbol)
);

-- Seed currencies
INSERT INTO currency (id, symbol, name, date_created, date_updated) VALUES
    ('10000000-0000-0000-0000-000000000001', 'BTC', 'Bitcoin', NOW(), NOW()),
    ('10000000-0000-0000-0000-000000000002', 'ETH', 'Ethereum', NOW(), NOW()),
    ('10000000-0000-0000-0000-000000000003', 'USDC', 'USD Coin', NOW(), NOW()),
    ('10000000-0000-0000-0000-000000000004', 'USDT', 'Tether', NOW(), NOW()),
    ('10000000-0000-0000-0000-000000000006', 'DAI', 'Dai Stablecoin', NOW(), NOW()),
    ('10000000-0000-0000-0000-000000000008', 'WBTC', 'Wrapped Bitcoin', NOW(), NOW());
