#!/bin/sh
echo "Running Branch naming check..."
# Enforce Git Branch Naming Convention
BRANCH_NAME=$(git rev-parse --abbrev-ref HEAD)

# Allowed branch patterns (modify as needed)
BRANCH_PATTERN="^(feature|bugfix|hotfix|release|chore|docs)/[a-z0-9_\-]+$|^(main|develop|staging)$"

if ! echo "$BRANCH_NAME" | grep -E -q "$BRANCH_PATTERN"; then
    echo "❌ ERROR: Branch name '$BRANCH_NAME' does not follow naming convention!"
    echo "✅ Allowed formats: feature/xxx, bugfix/xxx, hotfix/xxx, release/xxx, chore/xxx, docs/xxx, main, develop, staging"
    exit 1
fi