#!/usr/bin/env bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
JAVA_HOME="$SCRIPT_DIR/jre"
JAVA="$JAVA_HOME/bin/java"

exec "$JAVA" -jar "$SCRIPT_DIR/app.jar" "$@"
