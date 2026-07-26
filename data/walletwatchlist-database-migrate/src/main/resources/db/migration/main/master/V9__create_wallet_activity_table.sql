-- Interpreted wallet activities (user-friendly view).
CREATE TABLE wallet_activity (
    id             UUID PRIMARY KEY,
    wallet_id      UUID NOT NULL,
    transaction_id UUID,
    activity_type  VARCHAR(50) NOT NULL,
    summary        VARCHAR(500) NOT NULL,
    value_usd      NUMERIC(18, 2),
    date_occurred  TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    date_created   TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    date_updated   TIMESTAMP(6) WITH TIME ZONE,
    CONSTRAINT fk_wallet_activity_wallet FOREIGN KEY (wallet_id) REFERENCES wallet (id),
    CONSTRAINT fk_wallet_activity_transaction FOREIGN KEY (transaction_id) REFERENCES wallet_transaction (id)
);

CREATE INDEX idx_wallet_activity_wallet_id ON wallet_activity (wallet_id);
CREATE INDEX idx_wallet_activity_date_occurred ON wallet_activity (date_occurred);
CREATE INDEX idx_wallet_activity_type ON wallet_activity (activity_type);

-- Seed wallet activities with USD values
INSERT INTO wallet_activity (id, wallet_id, transaction_id, activity_type, summary, value_usd, date_occurred, date_created, date_updated) VALUES
    ('d8b6d237-82f1-4411-9ee8-05580639745b', '40000000-0000-0000-0001-000000000001', '3106a6b5-e2f5-4b43-ae16-523c530cf430', 'TRANSFER_IN', 'Received 2.5 ETH from 0x7b0d...a044', 8000.00, NOW(), NOW(), NOW()),
    ('dba8be7f-36ee-4c11-abb7-2986d13acad1', '40000000-0000-0000-0001-000000000001', 'cf4d3487-450a-489e-9c98-4d73b373b402', 'TRANSFER_IN', 'Received 1.0 ETH from 0x644c...b544', 3200.00, NOW(), NOW(), NOW()),
    ('5b1aca17-5a70-4b85-91d3-b57c66818176', '40000000-0000-0000-0001-000000000002', '5b8aca17-5a70-4b85-91d3-b57c66818176', 'TRANSFER_IN', 'Received 100 ETH from 0x644c...b544', 320000.00, NOW(), NOW(), NOW()),
    ('010ce022-972c-41f8-9f48-0160d6e01446', '40000000-0000-0000-0001-000000000003', '010be022-972c-41f8-9f48-0160d6e01446', 'TRANSFER_IN', 'Received 1 ETH from 0xf82d...40aa', 3200.00, NOW(), NOW(), NOW());
