#!/usr/bin/env python3
"""
Sync wallet assets and transactions from blockchain indexers.

Fetches:
- Token assets (quantities) for each wallet
- Last 20 transactions per wallet

Outputs SQL INSERT statements to stdout or a file.

Usage:
    python scripts/sync_wallet_data.py --alchemy-key YOUR_KEY > data-seed/local/V1006__seed_wallet_data.sql
    python scripts/sync_wallet_data.py --alchemy-key YOUR_KEY --output data-seed/local/V1006__seed_wallet_data.sql

Environment:
    ALCHEMY_API_KEY - Alchemy API key (alternative to --alchemy-key)
    Loads from .env.local if present.
"""

import argparse
import json
import os
from pathlib import Path
import sys
import uuid
from datetime import datetime, timezone
from decimal import Decimal
from typing import Optional
import urllib.request
import urllib.error


def load_env_file():
    """Load environment variables from .env.local file."""
    script_dir = Path(__file__).parent.parent
    env_file = script_dir / ".env.local"
    if env_file.exists():
        with open(env_file) as f:
            for line in f:
                line = line.strip()
                if line and not line.startswith("#") and "=" in line:
                    key, value = line.split("=", 1)
                    os.environ.setdefault(key.strip(), value.strip())


load_env_file()

# Wallet configurations matching V1004__seed_wallets.sql
WALLETS = {
    "ethereum": [
        {"id": "w0000000-0000-0000-0001-000000000001", "address": "0xd8dA6BF26964aF9D7eEd9e03E53415D37aA96045", "name": "Vitalik"},
        {"id": "w0000000-0000-0000-0001-000000000002", "address": "0xde0B295669a9FD93d5F28D9Ec85E40f4cb697BAe", "name": "ETH Foundation"},
        {"id": "w0000000-0000-0000-0001-000000000003", "address": "0xBE0eB53F46cd790Cd13851d5EFf43D12404d33E8", "name": "Binance Cold"},
    ],
}

# Blockchain asset IDs matching V1003__seed_blockchain_assets.sql
BLOCKCHAIN_ASSETS = {
    "ethereum": {
        "native": "a0000000-0000-0000-0001-000000000001",  # ETH
        "0xa0b86991c6218b36c1d19d4a2e9eb0ce3606eb48": "a0000000-0000-0000-0001-000000000002",  # USDC
        "0xdac17f958d2ee523a2206206994597c13d831ec7": "a0000000-0000-0000-0001-000000000003",  # USDT
        "0x6b175474e89094c44da98b954eescdecb5be5f855": "a0000000-0000-0000-0001-000000000004",  # DAI
        "0x2260fac5e5542a773aa44fbcfedf7c193bc2c599": "a0000000-0000-0000-0001-000000000005",  # WBTC
    },
}

ALCHEMY_NETWORKS = {
    "ethereum": "eth-mainnet",
}


def alchemy_request(network: str, method: str, params: list, api_key: str) -> dict:
    """Make a JSON-RPC request to Alchemy."""
    url = f"https://{ALCHEMY_NETWORKS[network]}.g.alchemy.com/v2/{api_key}"
    payload = {
        "jsonrpc": "2.0",
        "id": 1,
        "method": method,
        "params": params,
    }
    req = urllib.request.Request(
        url,
        data=json.dumps(payload).encode("utf-8"),
        headers={"Content-Type": "application/json"},
    )
    try:
        with urllib.request.urlopen(req, timeout=30) as resp:
            return json.loads(resp.read().decode("utf-8"))
    except urllib.error.HTTPError as e:
        print(f"-- HTTP Error {e.code}: {e.reason}", file=sys.stderr)
        return {"error": str(e)}
    except Exception as e:
        print(f"-- Request error: {e}", file=sys.stderr)
        return {"error": str(e)}


def get_native_balance(network: str, address: str, api_key: str) -> Optional[Decimal]:
    """Get native token balance (ETH/MATIC)."""
    result = alchemy_request(network, "eth_getBalance", [address, "latest"], api_key)
    if "result" in result:
        hex_balance = result["result"]
        wei = int(hex_balance, 16)
        return Decimal(wei) / Decimal(10**18)
    return None


def get_token_balances(network: str, address: str, api_key: str) -> list:
    """Get ERC-20 token balances using Alchemy's alchemy_getTokenBalances."""
    result = alchemy_request(network, "alchemy_getTokenBalances", [address], api_key)
    if "result" in result and "tokenBalances" in result["result"]:
        balances = []
        for tb in result["result"]["tokenBalances"]:
            if tb.get("tokenBalance") and tb["tokenBalance"] != "0x0":
                contract = tb["contractAddress"].lower()
                hex_balance = tb["tokenBalance"]
                # Assume 18 decimals for simplicity (would need metadata call for accuracy)
                wei = int(hex_balance, 16)
                balance = Decimal(wei) / Decimal(10**18)
                if balance > 0:
                    balances.append({"contract": contract, "balance": balance})
        return balances
    return []


def get_transactions(network: str, address: str, api_key: str, limit: int = 20) -> list:
    """Get recent transactions using Alchemy's alchemy_getAssetTransfers."""
    # Get incoming transfers
    incoming = alchemy_request(
        network,
        "alchemy_getAssetTransfers",
        [{
            "fromBlock": "0x0",
            "toBlock": "latest",
            "toAddress": address,
            "category": ["external", "erc20"],
            "maxCount": hex(limit),
            "order": "desc",
        }],
        api_key,
    )

    # Get outgoing transfers
    outgoing = alchemy_request(
        network,
        "alchemy_getAssetTransfers",
        [{
            "fromBlock": "0x0",
            "toBlock": "latest",
            "fromAddress": address,
            "category": ["external", "erc20"],
            "maxCount": hex(limit),
            "order": "desc",
        }],
        api_key,
    )

    transfers = []
    for result in [incoming, outgoing]:
        if "result" in result and "transfers" in result["result"]:
            transfers.extend(result["result"]["transfers"])

    # Sort by block number descending and take top N
    transfers.sort(key=lambda x: int(x.get("blockNum", "0x0"), 16), reverse=True)
    return transfers[:limit]


def generate_uuid() -> str:
    """Generate a new UUID."""
    return str(uuid.uuid4())


def sql_escape(value: str) -> str:
    """Escape single quotes for SQL."""
    return value.replace("'", "''")


def generate_sql(api_key: str) -> str:
    """Generate SQL INSERT statements for wallet data."""
    lines = []
    lines.append("-- Auto-generated wallet data from blockchain indexers.")
    lines.append(f"-- Generated at: {datetime.now(timezone.utc).isoformat()}")
    lines.append("")

    asset_inserts = []
    transaction_inserts = []
    activity_inserts = []
    wallet_updates = []

    for network, wallets in WALLETS.items():
        lines.append(f"-- {network.upper()} wallets")

        for wallet in wallets:
            wallet_id = wallet["id"]
            address = wallet["address"]
            name = wallet["name"]

            print(f"Fetching data for {name} ({address[:10]}...) on {network}...", file=sys.stderr)

            # Get native asset quantity
            native_quantity = get_native_balance(network, address, api_key)
            if native_quantity is not None and native_quantity > 0:
                blockchain_asset_id = BLOCKCHAIN_ASSETS[network]["native"]
                asset_row_id = generate_uuid()
                asset_inserts.append(
                    f"    ('{asset_row_id}', '{wallet_id}', '{blockchain_asset_id}', {native_quantity}, NOW(), NOW())"
                )

            # Get token asset quantities
            token_balances = get_token_balances(network, address, api_key)
            for tb in token_balances:
                contract = tb["contract"]
                if contract in BLOCKCHAIN_ASSETS.get(network, {}):
                    blockchain_asset_id = BLOCKCHAIN_ASSETS[network][contract]
                    asset_row_id = generate_uuid()
                    asset_inserts.append(
                        f"    ('{asset_row_id}', '{wallet_id}', '{blockchain_asset_id}', {tb['balance']}, NOW(), NOW())"
                    )

            # Get transactions
            transactions = get_transactions(network, address, api_key, limit=20)
            for tx in transactions:
                tx_id = generate_uuid()
                tx_hash = tx.get("hash", tx.get("uniqueId", ""))
                block_num = int(tx.get("blockNum", "0x0"), 16) if tx.get("blockNum") else "NULL"
                from_addr = sql_escape(tx.get("from", ""))
                to_addr = sql_escape(tx.get("to", "")) if tx.get("to") else "NULL"
                value = Decimal(str(tx.get("value", 0))) if tx.get("value") else Decimal(0)

                # Determine asset ID
                asset_id = "NULL"
                if tx.get("category") == "external":
                    asset_id = f"'{BLOCKCHAIN_ASSETS[network]['native']}'"
                elif tx.get("rawContract", {}).get("address"):
                    contract = tx["rawContract"]["address"].lower()
                    if contract in BLOCKCHAIN_ASSETS.get(network, {}):
                        asset_id = f"'{BLOCKCHAIN_ASSETS[network][contract]}'"

                to_addr_sql = f"'{to_addr}'" if to_addr != "NULL" else "NULL"
                block_num_sql = block_num if block_num != "NULL" else "NULL"

                transaction_inserts.append(
                    f"    ('{tx_id}', '{wallet_id}', '{sql_escape(tx_hash)}', {block_num_sql}, '{from_addr}', {to_addr_sql}, {value}, {asset_id}, NULL, 'SUCCESS', NOW(), NOW(), NOW())"
                )

                # Generate activity
                activity_id = generate_uuid()
                is_incoming = tx.get("to", "").lower() == address.lower()
                activity_type = "TRANSFER_IN" if is_incoming else "TRANSFER_OUT"
                asset_symbol = tx.get("asset", "ETH")
                summary = f"{'Received' if is_incoming else 'Sent'} {value} {asset_symbol}"

                activity_inserts.append(
                    f"    ('{activity_id}', '{wallet_id}', '{tx_id}', '{activity_type}', '{sql_escape(summary)}', NULL, NOW(), NOW(), NOW())"
                )

            # Update wallet sync status
            wallet_updates.append(
                f"UPDATE wallet SET sync_status = 'SYNCED', date_last_synced = NOW(), date_updated = NOW() WHERE id = '{wallet_id}';"
            )

    # Output asset inserts
    if asset_inserts:
        lines.append("")
        lines.append("-- Wallet assets")
        lines.append("INSERT INTO wallet_asset (id, wallet_id, blockchain_asset_id, quantity, date_created, date_updated) VALUES")
        lines.append(",\n".join(asset_inserts) + ";")

    # Output transaction inserts
    if transaction_inserts:
        lines.append("")
        lines.append("-- Wallet transactions (last 20 per wallet)")
        lines.append("INSERT INTO wallet_transaction (id, wallet_id, tx_hash, block_number, from_address, to_address, value, blockchain_asset_id, gas_used, status, date_occurred, date_created, date_updated) VALUES")
        lines.append(",\n".join(transaction_inserts) + ";")

    # Output activity inserts
    if activity_inserts:
        lines.append("")
        lines.append("-- Wallet activities")
        lines.append("INSERT INTO wallet_activity (id, wallet_id, transaction_id, activity_type, summary, value_usd, date_occurred, date_created, date_updated) VALUES")
        lines.append(",\n".join(activity_inserts) + ";")

    # Output wallet updates
    if wallet_updates:
        lines.append("")
        lines.append("-- Update wallet sync status")
        lines.extend(wallet_updates)

    return "\n".join(lines)


def main():
    parser = argparse.ArgumentParser(description="Sync wallet data from blockchain indexers")
    parser.add_argument("--alchemy-key", help="Alchemy API key")
    parser.add_argument("--output", "-o", help="Output file (default: stdout)")
    args = parser.parse_args()

    api_key = args.alchemy_key or os.environ.get("ALCHEMY_API_KEY")
    if not api_key:
        print("Error: Alchemy API key required. Use --alchemy-key or set ALCHEMY_API_KEY", file=sys.stderr)
        sys.exit(1)

    sql = generate_sql(api_key)

    if args.output:
        with open(args.output, "w") as f:
            f.write(sql)
        print(f"Generated: {args.output}", file=sys.stderr)
    else:
        print(sql)


if __name__ == "__main__":
    main()
