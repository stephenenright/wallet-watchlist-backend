-- Watched wallets linking users to wallets they are watching.
CREATE TABLE watched_wallet (
    id           UUID PRIMARY KEY,
    watcher_id   UUID NOT NULL,
    wallet_id    UUID NOT NULL,
    label        VARCHAR(255),
    status       VARCHAR(32) NOT NULL DEFAULT 'ACTIVE',
    date_created TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    date_updated TIMESTAMP(6) WITH TIME ZONE,
    CONSTRAINT fk_watched_wallet_watcher FOREIGN KEY (watcher_id) REFERENCES app_user (id),
    CONSTRAINT fk_watched_wallet_wallet FOREIGN KEY (wallet_id) REFERENCES wallet (id),
    CONSTRAINT uq_watched_wallet_watcher_wallet UNIQUE (watcher_id, wallet_id)
);

CREATE INDEX idx_watched_wallet_watcher_id ON watched_wallet (watcher_id);
CREATE INDEX idx_watched_wallet_wallet_id ON watched_wallet (wallet_id);
CREATE INDEX idx_watched_wallet_status ON watched_wallet (status);

-- Seed watched wallets

-- John Doe watches Ethereum and Bitcoin wallets
INSERT INTO watched_wallet (id, watcher_id, wallet_id, label, status, date_created, date_updated) VALUES
    ('50000000-0000-0000-0001-000000000001', '00000000-0000-0000-0000-000000000001', '40000000-0000-0000-0001-000000000001', 'Vitalik', 'ACTIVE', NOW(), NOW()),
    ('50000000-0000-0000-0001-000000000002', '00000000-0000-0000-0000-000000000001', '40000000-0000-0000-0001-000000000002', 'ETH Foundation', 'ACTIVE', NOW(), NOW()),
    ('50000000-0000-0000-0001-000000000003', '00000000-0000-0000-0000-000000000001', '40000000-0000-0000-0003-000000000001', 'Satoshi Genesis', 'ACTIVE', NOW(), NOW());

-- Jane Smith watches Ethereum and Bitcoin wallets
INSERT INTO watched_wallet (id, watcher_id, wallet_id, label, status, date_created, date_updated) VALUES
    ('50000000-0000-0000-0002-000000000001', '00000000-0000-0000-0000-000000000002', '40000000-0000-0000-0001-000000000003', 'Binance ETH', 'ACTIVE', NOW(), NOW()),
    ('50000000-0000-0000-0002-000000000002', '00000000-0000-0000-0000-000000000002', '40000000-0000-0000-0003-000000000002', 'Binance BTC', 'ACTIVE', NOW(), NOW());

-- Bob Wilson watches Bitcoin wallets + Vitalik
INSERT INTO watched_wallet (id, watcher_id, wallet_id, label, status, date_created, date_updated) VALUES
    ('50000000-0000-0000-0003-000000000001', '00000000-0000-0000-0000-000000000003', '40000000-0000-0000-0003-000000000001', 'Genesis Block', 'ACTIVE', NOW(), NOW()),
    ('50000000-0000-0000-0003-000000000002', '00000000-0000-0000-0000-000000000003', '40000000-0000-0000-0003-000000000003', 'Bitfinex Cold', 'ACTIVE', NOW(), NOW()),
    ('50000000-0000-0000-0003-000000000003', '00000000-0000-0000-0000-000000000003', '40000000-0000-0000-0001-000000000001', 'Vitalik Tracker', 'ACTIVE', NOW(), NOW());
