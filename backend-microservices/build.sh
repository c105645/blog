#!/bin/bash
set -e

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"

echo ""
echo "================================================="
echo "  Blog Platform — Build All Services"
echo "================================================="
echo ""

SERVICES=("config-service" "userprofile-service" "post-service" "edge-service")

for SERVICE in "${SERVICES[@]}"; do
  echo ">>> Building $SERVICE..."
  cd "$ROOT_DIR/$SERVICE"
  chmod +x gradlew
  ./gradlew bootJar -x test --quiet
  echo "    ✓ $SERVICE JAR ready"
  echo ""
done

echo "================================================="
echo "  All services built successfully!"
echo "  Run './run.sh' to start the stack."
echo "================================================="
echo ""
