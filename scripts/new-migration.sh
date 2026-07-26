#!/usr/bin/env bash
#
# new-migration.sh
#
# Generates a new Flyway migration file whose version is the current Unix
# timestamp in milliseconds, so migrations sort chronologically.
#
# Produces: V<epoch-millis>__<description>.sql
#   e.g.    V1721678400000__add_wallets_table.sql
#
# Usage:
#   ./scripts/new-migration.sh <description>
#   ./scripts/new-migration.sh add wallets table
#   ./scripts/new-migration.sh "add wallets table"

set -euo pipefail

log() { printf '\033[1;34m==>\033[0m %s\n' "$*"; }
err() { printf '\033[1;31merror:\033[0m %s\n' "$*" >&2; }

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(dirname "${SCRIPT_DIR}")"
MIGRATION_DIR="${REPO_ROOT}/data/walletwatchlist-database-migrate/src/main/resources/db/migration/main/master"

usage() {
    cat >&2 <<EOF
Usage: $(basename "$0") <description>

Creates a Flyway migration V<epoch-millis>__<description>.sql in:
  ${MIGRATION_DIR#"${REPO_ROOT}/"}

Examples:
  $(basename "$0") add wallets table
  $(basename "$0") "add wallets table"
EOF
    exit 2
}

# Current Unix time in milliseconds, portable across macOS (BSD date), Linux
# (GNU date), and environments where only python3/perl are available.
current_millis() {
    local ms
    if command -v gdate >/dev/null 2>&1; then
        gdate +%s%3N
        return
    fi
    ms="$(date +%s%3N 2>/dev/null || true)"
    if [[ "${ms}" =~ ^[0-9]+$ ]]; then
        echo "${ms}"
        return
    fi
    if command -v python3 >/dev/null 2>&1; then
        python3 -c 'import time; print(int(time.time() * 1000))'
        return
    fi
    if command -v perl >/dev/null 2>&1; then
        perl -MTime::HiRes -e 'printf("%d\n", Time::HiRes::time() * 1000)'
        return
    fi
    # Last resort: whole seconds promoted to milliseconds.
    echo "$(( $(date +%s) * 1000 ))"
}

main() {
    [[ $# -ge 1 ]] || usage

    local description="$*"

    # Normalize the description into a Flyway-safe slug:
    # lowercase, non-alphanumeric runs -> '_', trim leading/trailing '_'.
    local slug
    slug="$(printf '%s' "${description}" \
        | tr '[:upper:]' '[:lower:]' \
        | sed -E 's/[^a-z0-9]+/_/g; s/^_+//; s/_+$//')"

    if [[ -z "${slug}" ]]; then
        err "description '${description}' produced an empty name after normalization"
        exit 1
    fi

    if [[ ! -d "${MIGRATION_DIR}" ]]; then
        err "migration directory not found: ${MIGRATION_DIR}"
        exit 1
    fi

    local version
    version="$(current_millis)"

    local filename="V${version}__${slug}.sql"
    local filepath="${MIGRATION_DIR}/${filename}"

    if [[ -e "${filepath}" ]]; then
        err "migration already exists: ${filepath}"
        exit 1
    fi

    cat > "${filepath}" <<EOF
-- ${filename}
-- ${description}
-- Generated: $(date -u '+%Y-%m-%dT%H:%M:%SZ')

EOF

    log "created ${filepath#"${REPO_ROOT}/"}"
}

main "$@"
