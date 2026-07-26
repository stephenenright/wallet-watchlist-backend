# Local Stack

Local development services for `wallet-watchlist-backend`, run with Docker
Compose. Separate from `ci-cd/` (which serves the `walletwatchlist-test`
database on port `5451` for CI).

## Postgres

| Setting  | Value |
|----------|-------|
| Image    | `postgres:alpine` (latest stable) |
| Host port | `5450` |
| Database | `walletwatchlist` |
| User     | `postgres` |
| Password | `postgres` |

JDBC URL: `jdbc:postgresql://localhost:5450/walletwatchlist`

This matches the local Flyway target (`cleanDb`) in
`data/walletwatchlist-database-migrate/build.gradle`.

## Usage

```bash
# Start (from this directory)
docker compose -f local-stack/docker-compose.yml up -d

# Stop
docker compose -f local-stack/docker-compose.yml down

# Stop and wipe the database volume
docker compose -f local-stack/docker-compose.yml down -v
```

Data persists in the named volume `walletwatchlist-postgres-data` across
restarts.
