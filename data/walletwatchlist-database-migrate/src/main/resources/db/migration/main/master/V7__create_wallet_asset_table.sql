-- Wallet assets per blockchain asset.
CREATE TABLE wallet_asset (
    id                  UUID PRIMARY KEY,
    wallet_id           UUID NOT NULL,
    blockchain_asset_id UUID NOT NULL,
    quantity            NUMERIC(36, 18) NOT NULL,
    date_created        TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    date_updated        TIMESTAMP(6) WITH TIME ZONE,
    CONSTRAINT fk_wallet_asset_wallet FOREIGN KEY (wallet_id) REFERENCES wallet (id),
    CONSTRAINT fk_wallet_asset_blockchain_asset FOREIGN KEY (blockchain_asset_id) REFERENCES blockchain_asset (id),
    CONSTRAINT uq_wallet_asset_wallet_blockchain_asset UNIQUE (wallet_id, blockchain_asset_id)
);

CREATE INDEX idx_wallet_asset_wallet_id ON wallet_asset (wallet_id);
CREATE INDEX idx_wallet_asset_blockchain_asset_id ON wallet_asset (blockchain_asset_id);

-- Seed wallet assets (ETH holdings)
INSERT INTO wallet_asset (id, wallet_id, blockchain_asset_id, quantity, date_created, date_updated) VALUES
    ('5d1d27b4-ea53-48b0-b611-ad8e1b601af7', '40000000-0000-0000-0001-000000000001', '30000000-0000-0000-0001-000000000001', 6.631771748663559991, NOW(), NOW()),
    ('b33d43f6-b6d2-4527-af4a-eccb9263a43e', '40000000-0000-0000-0001-000000000002', '30000000-0000-0000-0001-000000000001', 8774.487015807047266412, NOW(), NOW()),
    ('a7cf67e5-2ae0-41ff-a4a8-0763914b60c7', '40000000-0000-0000-0001-000000000003', '30000000-0000-0000-0001-000000000001', 1996008.385778844801255683, NOW(), NOW());
