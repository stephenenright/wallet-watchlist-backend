-- Cached wallet transactions.
CREATE TABLE wallet_transaction (
    id                  UUID PRIMARY KEY,
    wallet_id           UUID NOT NULL,
    tx_hash             VARCHAR(255) NOT NULL,
    block_number        BIGINT,
    from_address        VARCHAR(255) NOT NULL,
    to_address          VARCHAR(255),
    tx_value            NUMERIC(36, 18),
    blockchain_asset_id UUID,
    gas_used            NUMERIC(36, 18),
    status              VARCHAR(50) NOT NULL,
    date_occurred       TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    date_created        TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    date_updated        TIMESTAMP(6) WITH TIME ZONE,
    CONSTRAINT fk_wallet_transaction_wallet FOREIGN KEY (wallet_id) REFERENCES wallet (id),
    CONSTRAINT fk_wallet_transaction_blockchain_asset FOREIGN KEY (blockchain_asset_id) REFERENCES blockchain_asset (id),
    CONSTRAINT uq_wallet_transaction_wallet_hash UNIQUE (wallet_id, tx_hash)
);

CREATE INDEX idx_wallet_transaction_wallet_id ON wallet_transaction (wallet_id);
CREATE INDEX idx_wallet_transaction_date_occurred ON wallet_transaction (date_occurred);
CREATE INDEX idx_wallet_transaction_status ON wallet_transaction (status);

-- Seed wallet transactions
INSERT INTO wallet_transaction (id, wallet_id, tx_hash, block_number, from_address, to_address, tx_value, blockchain_asset_id, gas_used, status, date_occurred, date_created, date_updated) VALUES
    ('3106a6b5-e2f5-4b43-ae16-523c530cf430', '40000000-0000-0000-0001-000000000001', '0x7ddb1393ffbcc3842c082fd4c6494f5d26c97bb34dad66e2e47ebf7690564fa7', 25597322, '0x7b0d132c6bf845d5a072b6cc9201546f0af8a044', '0xd8da6bf26964af9d7eed9e03e53415d37aa96045', 2.5, '30000000-0000-0000-0001-000000000001', 21000, 'SUCCESS', NOW(), NOW(), NOW()),
    ('cf4d3487-450a-489e-9c98-4d73b373b402', '40000000-0000-0000-0001-000000000001', '0xfcdfcf74f10ff797937a67eec00f4025ad9e2fc03b04b345efa788fd84d45fc4', 25597005, '0x644cc87ba4976cdbbc465768c24720b76022b544', '0xd8da6bf26964af9d7eed9e03e53415d37aa96045', 1.0, '30000000-0000-0000-0001-000000000001', 21000, 'SUCCESS', NOW(), NOW(), NOW()),
    ('e0a885fb-d4d2-4ed1-9da9-2de903cba9ae', '40000000-0000-0000-0001-000000000001', '0x3e0ca84ed1388f16b5a37c66d0ac72a9142e224fef3fca76968cb778a67b1a30', 25590385, '0x7e5f4552091a69125d5dfcb7b8c2659029395bdf', '0xd8da6bf26964af9d7eed9e03e53415d37aa96045', 0.5, '30000000-0000-0000-0001-000000000001', 21000, 'SUCCESS', NOW(), NOW(), NOW()),
    ('5b8aca17-5a70-4b85-91d3-b57c66818176', '40000000-0000-0000-0001-000000000002', '0x90156b19900c04e63babcc1db16bfa0675658ebec11d6e430e5416a79f01a41e', 25598156, '0x644cc87ba4976cdbbc465768c24720b76022b544', '0xde0b295669a9fd93d5f28d9ec85e40f4cb697bae', 100.0, '30000000-0000-0000-0001-000000000001', 21000, 'SUCCESS', NOW(), NOW(), NOW()),
    ('9136e201-6c1f-435c-a07c-3b94fb9ec3a6', '40000000-0000-0000-0001-000000000002', '0x650aaaa39823ce85da2027c4a9ffef2cf413e8eb89cc227461b3a83f7322f191', 25280882, '0x8e764fd73d58e662d2059ee2ba593c031d5164ca', '0xde0b295669a9fd93d5f28d9ec85e40f4cb697bae', 0.001, '30000000-0000-0000-0001-000000000001', 21000, 'SUCCESS', NOW(), NOW(), NOW()),
    ('010be022-972c-41f8-9f48-0160d6e01446', '40000000-0000-0000-0001-000000000003', '0xa4829a965da6d4c5aeb535acabf27d8d396438994d4d174f0c15a368791ee96f', 25565547, '0xf82d598ce6fce9f2d46f046fc46653e3284040aa', '0xbe0eb53f46cd790cd13851d5eff43d12404d33e8', 1.0, '30000000-0000-0000-0001-000000000001', 21000, 'SUCCESS', NOW(), NOW(), NOW()),
    ('840e8d99-8511-490e-964e-6d40a4222f9f', '40000000-0000-0000-0001-000000000003', '0xa3296baf09fc07bbe278efb11a787b1fff0978b835fc81bad7316698fc2a569b', 25545185, '0x260b364fe0d3d37e6fd3cda0fa50926a06c54cea', '0xbe0eb53f46cd790cd13851d5eff43d12404d33e8', 10.0, '30000000-0000-0000-0001-000000000002', 65000, 'SUCCESS', NOW(), NOW(), NOW());
