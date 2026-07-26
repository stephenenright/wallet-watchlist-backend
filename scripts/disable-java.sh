#!/usr/bin/env bash
# Temporarily removes Java from PATH for the current shell session.
# Usage: source scripts/disable-java.sh
#
# To restore, start a new shell or re-export your original PATH.

if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
    echo "This script must be sourced, not executed."
    echo "Usage: source $0"
    exit 1
fi

# Remove JAVA_HOME/bin and common Java paths from PATH
export PATH=$(echo "$PATH" | tr ':' '\n' | grep -v -E '(java|jdk|jre|/Library/Java)' | tr '\n' ':' | sed 's/:$//')

# Unset JAVA_HOME
unset JAVA_HOME

echo "Java disabled for this shell session."
echo "PATH entries containing 'java', 'jdk', 'jre' have been removed."
which java 2>/dev/null && echo "Warning: java still found at $(which java)" || echo "java command not found (success)"
