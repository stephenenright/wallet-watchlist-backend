#!/usr/bin/env bash
# Generates a Postman collection from the OpenAPI spec.
# Requires: curl, npx (Node.js), jq
#
# Usage: ./scripts/generate-postman-collection.sh
#
# The app must be running on localhost:8081.

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
DOCS_DIR="$PROJECT_DIR/docs"
OPENAPI_FILE="$DOCS_DIR/openapi.json"
POSTMAN_FILE="$DOCS_DIR/postman-collection.json"
POSTMAN_TEMP="$DOCS_DIR/postman-collection-temp.json"

mkdir -p "$DOCS_DIR"

echo "Fetching OpenAPI spec from http://localhost:8081/v3/api-docs..."
curl -s http://localhost:8081/v3/api-docs -o "$OPENAPI_FILE"

if [ ! -s "$OPENAPI_FILE" ]; then
    echo "Error: Failed to fetch OpenAPI spec. Is the app running?"
    exit 1
fi

echo "Converting to Postman collection..."
npx --yes openapi-to-postmanv2 -s "$OPENAPI_FILE" -o "$POSTMAN_TEMP" -p

echo "Customizing request parameters..."
# Use jq to:
# 1. Set status=ACTIVE for wallet list endpoint
# 2. Remove filters from watched wallet list endpoint
jq '
def update_request:
  if .name == "List all wallets" then
    .request.url.query = [.request.url.query[] | if .key == "status" then .value = "ACTIVE" else . end]
  elif .name == "List all watched wallets" then
    .request.url.query = [.request.url.query[] | select(.key == "page" or .key == "size")]
  else
    .
  end;

def walk_items:
  if type == "array" then
    map(walk_items)
  elif type == "object" then
    if has("request") then
      update_request
    else
      with_entries(.value |= walk_items)
    end
  else
    .
  end;

.item |= walk_items
' "$POSTMAN_TEMP" > "$POSTMAN_FILE"

rm -f "$POSTMAN_TEMP"

echo "Done!"
echo "  OpenAPI spec: $OPENAPI_FILE"
echo "  Postman collection: $POSTMAN_FILE"
