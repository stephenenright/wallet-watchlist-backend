set shell := ["bash", "-uc"]

local_compose := "local-stack/docker-compose.yml"
server_port := "8081"

# List available tasks
default:
    @just --list

# Start the local infrastructure stack (Postgres) in the background
infra-up:
    docker compose -f {{local_compose}} up -d

# Stop the local infrastructure stack
infra-down:
    docker compose -f {{local_compose}} down

# Clean and build the project
build:
    ./gradlew clean build

# Format code with Spotless
format:
    ./gradlew spotlessApply

# Start the web API server
start-services:
    ./gradlew :services:wallet-web-api:bootRun

# Kill the web API server
kill-services:
    @pkill -f "wallet-web-api" 2>/dev/null || echo "No server running"
    @lsof -ti:{{server_port}} | xargs -r kill -9 2>/dev/null || true

# Generate a timestamped Flyway migration: just new-migration add wallets table
new-migration +description:
    ./scripts/new-migration.sh {{description}}

# Sync wallet data from blockchain indexers (requires ALCHEMY_API_KEY env var or --alchemy-key)
sync-wallet-data:
    python3 scripts/sync_wallet_data.py --output data-seed/local/V1006__seed_wallet_data.sql

# Sync wallet data with explicit API key
sync-wallet-data-key key:
    python3 scripts/sync_wallet_data.py --alchemy-key {{key}} --output data-seed/local/V1006__seed_wallet_data.sql

# Clean the database (drop and recreate)
db-clean:
    docker exec walletwatchlist-postgres-local psql -U postgres -c "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = 'walletwatchlist' AND pid <> pg_backend_pid();"
    docker exec walletwatchlist-postgres-local psql -U postgres -c "DROP DATABASE IF EXISTS walletwatchlist;"
    docker exec walletwatchlist-postgres-local psql -U postgres -c "CREATE DATABASE walletwatchlist;"

# Build distribution package with fat JAR and bundled JRE
dist:
    ./gradlew :services:wallet-web-api:dist

# Generate Postman collection from OpenAPI spec (app must be running)
gen-postman-col:
    ./scripts/generate-postman-collection.sh
