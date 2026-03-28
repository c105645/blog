#!/usr/bin/env bash
# =============================================================================
# seed.sh — Run seed_data.sql against the blog platform's PostgreSQL instance
# =============================================================================
#
# USAGE:
#   ./seed.sh                      # Docker Compose (default)
#   ./seed.sh local                # Local PostgreSQL on localhost:5432
#   ./seed.sh <host> [port]        # Custom host/port
#
# The script must be run from the same directory as seed_data.sql.
# =============================================================================

set -euo pipefail

# ── Config ────────────────────────────────────────────────────────────────────
DB_NAME="blogdb"
DB_USER="user"
DB_PASS="password"
DB_PORT="${2:-5432}"
CONTAINER="blog-postgres"         # docker-compose container name
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
SQL_FILE="$SCRIPT_DIR/seed_data.sql"

# ── Colour helpers ────────────────────────────────────────────────────────────
GREEN='\033[0;32m'; YELLOW='\033[1;33m'; RED='\033[0;31m'; NC='\033[0m'
info()  { echo -e "${GREEN}[seed]${NC} $*"; }
warn()  { echo -e "${YELLOW}[seed]${NC} $*"; }
error() { echo -e "${RED}[seed]${NC} $*" >&2; exit 1; }

# ── Pre-flight ────────────────────────────────────────────────────────────────
[[ -f "$SQL_FILE" ]] || error "seed_data.sql not found at $SQL_FILE"

MODE="${1:-docker}"

# ── Run ───────────────────────────────────────────────────────────────────────
case "$MODE" in

  docker)
    info "Targeting Docker container: $CONTAINER"
    # Verify container is running
    if ! docker ps --format '{{.Names}}' | grep -q "^${CONTAINER}$"; then
      error "Container '$CONTAINER' is not running. Start it with: docker-compose up -d blog-postgres"
    fi

    info "Waiting for PostgreSQL to be ready..."
    until docker exec "$CONTAINER" pg_isready -U "$DB_USER" -d "$DB_NAME" -q; do
      sleep 1
    done

    info "Running seed script inside container..."
    docker exec -i "$CONTAINER" \
      env PGPASSWORD="$DB_PASS" \
      psql -U "$DB_USER" -d "$DB_NAME" \
      < "$SQL_FILE"
    ;;

  local)
    info "Targeting local PostgreSQL on localhost:$DB_PORT"
    command -v psql &>/dev/null || error "'psql' not found. Install PostgreSQL client tools."

    until PGPASSWORD="$DB_PASS" psql -h localhost -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -c '\q' 2>/dev/null; do
      warn "PostgreSQL not ready, retrying in 2s..."
      sleep 2
    done

    info "Running seed script..."
    PGPASSWORD="$DB_PASS" psql \
      -h localhost -p "$DB_PORT" \
      -U "$DB_USER" -d "$DB_NAME" \
      -f "$SQL_FILE"
    ;;

  *)
    # Treat first arg as custom host
    HOST="$MODE"
    info "Targeting custom host: $HOST:$DB_PORT"
    command -v psql &>/dev/null || error "'psql' not found. Install PostgreSQL client tools."

    info "Running seed script..."
    PGPASSWORD="$DB_PASS" psql \
      -h "$HOST" -p "$DB_PORT" \
      -U "$DB_USER" -d "$DB_NAME" \
      -f "$SQL_FILE"
    ;;
esac

info "✅ Seed complete."
