-- Application users.
CREATE TABLE app_user (
    id           UUID PRIMARY KEY,
    first_name   VARCHAR(255) NOT NULL,
    last_name    VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL
);

CREATE UNIQUE INDEX uq_app_user_email ON app_user (email);

ALTER TABLE app_user ADD COLUMN date_created TIMESTAMP(6) WITH TIME ZONE NOT NULL DEFAULT NOW();
ALTER TABLE app_user ADD COLUMN date_updated TIMESTAMP(6) WITH TIME ZONE;

-- Seed users
INSERT INTO app_user (id, first_name, last_name, email, date_created, date_updated) VALUES
    ('00000000-0000-0000-0000-000000000001', 'John', 'Doe', 'john.doe@example.com', NOW(), NOW()),
    ('00000000-0000-0000-0000-000000000002', 'Jane', 'Smith', 'jane.smith@example.com', NOW(), NOW()),
    ('00000000-0000-0000-0000-000000000003', 'Bob', 'Wilson', 'bob.wilson@example.com', NOW(), NOW());
