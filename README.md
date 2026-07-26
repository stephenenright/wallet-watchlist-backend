# wallet-watchlist-backend
A sample backend for a wallet watchlist application. The functionality currently implemented is:

The functionality is based on the requirements in this assignment.
[Backend_Home_Task.pdf](Backend_Home_Task.pdf)


To run the application locally without the need to install dependencies or a JRE cd to the ./bin directory and follow 
the instructions found here:

[How to run the api](bin/README.md)


Screen Recording of the Functionality:
https://www.dropbox.com/scl/fi/wa63k6rtd1plqsyy0dd33/screen-recording.mov?rlkey=lzgtc8y4dbpo4oh6ox3is65mb&st=2bmz27qe&dl=0


Postman collection is available here:
[postman-collection.json](docs/postman-collection.json)


## Tech Stack
* Spring Boot
* Spring + Spring Data JPA
* H2 Database to simplify running the application.
* Also, a postgres db can be used by updating the configuration.


## Service Urls

| Service | URL | Description |
|---------|-----|-------------|
| API Base | http://localhost:8081/api | Base URL for all API endpoints |
| Swagger UI | http://localhost:8081/swagger-ui/index.html | Interactive API documentation |
| OpenAPI Spec | http://localhost:8081/v3/api-docs | OpenAPI 3.0 JSON specification |
| Currencies | http://localhost:8081/api/currencies | Currency management |
| Blockchains | http://localhost:8081/api/blockchains | Blockchain network management |
| Blockchain Assets | http://localhost:8081/api/blockchain-assets | Assets deployed on blockchains |
| Wallets | http://localhost:8081/api/wallets | Wallet management |
| Watched Wallets | http://localhost:8081/api/watched-wallets | User watched wallet management |
| Health Check | http://localhost:8081/api/health | Service health status |



## Architecture Decisions and Tradeoffs
* A Monolithic application was chosen due to the short time frame to implement the assignment.
* In a real application we can see this as a microservice architecture with different services for:
  * Auth
  * User Management
  * Wallet Management
  * Asset Management
  * User Registration
  * Wallet Sync Service.
* Since we can split the application into different services in the future, we have created separate packages for that possibility.
* I made a design decision to store the wallet balance and most recent transactions history, to have a snapshot of data. This is also used to mock new wallets.
* In a real application we can use a blockchain indexer to view more of the data from the wallet frontend, but also cache balances and recent transactions.
* We have omitted background / cron jobs since they are not required for the assignment. The background jobs can also be used to sync a cached subset of data.
* Sorting and filtering are not supported on the APIs.
* For flexible filtering, we can use RSQL (https://github.com/jirutka/rsql-parser) and integrate it with JPA to automatically generate the queries.
* We could consider soft delete in the future for wallets.
* The Wallet and the WatchedWallet are stored in different models as a wallet could be watched by many users.
* We can support a watcher (user) watching a subset of assets in the future.


## Just Tasks

This project uses [just](https://github.com/casey/just) as a command runner. Run `just` to see all available tasks.

| Task | Description |
|------|-------------|
| `just` | Lists all available tasks |
| `just infra-up` | Starts the local infrastructure stack (Postgres) in Docker |
| `just infra-down` | Stops the local infrastructure stack |
| `just build` | Cleans and builds the project with Gradle |
| `just format` | Formats code using Spotless |
| `just start-services` | Starts the web API server on port 8081 |
| `just kill-services` | Stops the web API server |
| `just new-migration <desc>` | Generates a timestamped Flyway migration file (e.g., `just new-migration add wallets table`) |
| `just sync-wallet-data` | Syncs wallet data from blockchain indexers (requires `ALCHEMY_API_KEY` env var) |
| `just sync-wallet-data-key <key>` | Syncs wallet data with an explicit Alchemy API key |
| `just db-clean` | Drops and recreates the database (Postgres only) |
| `just dist` | Builds a distribution package with fat JAR and bundled JRE into `bin/` |
| `just gen-postman-col` | Generates a Postman collection from the OpenAPI spec (app must be running)
