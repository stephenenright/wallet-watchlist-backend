#!/usr/bin/env bash
#
# setup-env.sh
#
# Prepares a local development environment for wallet-watchlist-backend.
# Currently ensures `just` (the task runner used by ./justfile) is installed.
#
# Usage:
#   ./scripts/setup-env.sh

set -euo pipefail

log() { printf '\033[1;34m==>\033[0m %s\n' "$*"; }
warn() { printf '\033[1;33mwarning:\033[0m %s\n' "$*" >&2; }
err() { printf '\033[1;31merror:\033[0m %s\n' "$*" >&2; }

install_just() {
    if command -v just >/dev/null 2>&1; then
        log "just already installed ($(just --version))"
        return 0
    fi

    log "just not found — installing..."

    if command -v brew >/dev/null 2>&1; then
        brew install just
    elif command -v cargo >/dev/null 2>&1; then
        cargo install just
    else
        # Fall back to the official prebuilt-binary installer.
        local bin_dir="${HOME}/.local/bin"
        mkdir -p "${bin_dir}"
        log "installing just to ${bin_dir} via just.systems installer"
        curl --proto '=https' --tlsv1.2 -sSf https://just.systems/install.sh \
            | bash -s -- --to "${bin_dir}"

        case ":${PATH}:" in
            *":${bin_dir}:"*) ;;
            *) warn "add ${bin_dir} to your PATH to use just (e.g. add 'export PATH=\"${bin_dir}:\$PATH\"' to your shell profile)" ;;
        esac
    fi

    if command -v just >/dev/null 2>&1; then
        log "just installed ($(just --version))"
    else
        err "just installation completed but 'just' is not on PATH; open a new shell or update PATH"
        return 1
    fi
}

main() {
    install_just
    log "environment setup complete"
}

main "$@"
