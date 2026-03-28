#!/bin/bash
set -e

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
COMPOSE_DIR="$ROOT_DIR/blog-deployments/docker"

echo ""
echo "================================================="
echo "  Blog Platform — Start All Services"
echo "================================================="
echo ""

# Check JARs have been built
SERVICES=("config-service" "userprofile-service" "post-service" "edge-service")
for SERVICE in "${SERVICES[@]}"; do
  JAR_COUNT=$(find "$ROOT_DIR/$SERVICE/build/libs" -name "*.jar" 2>/dev/null | wc -l)
  if [ "$JAR_COUNT" -eq 0 ]; then
    echo "  ERROR: No JAR found for $SERVICE."
    echo "  Please run './build.sh' first."
    echo ""
    exit 1
  fi
done

echo "  Starting all containers..."
echo ""
cd "$COMPOSE_DIR"
docker compose up --build -d

echo ""
echo "================================================="
echo "  Stack is up!"
echo ""
echo "  Edge Gateway      →  http://localhost:9000"
echo "  UserProfile API   →  http://localhost:9010"
echo "  Post API          →  http://localhost:9020"
echo "  Config Server     →  http://localhost:8888"
echo "  RabbitMQ UI       →  http://localhost:15672  (guest / guest)"
echo ""
echo "  Swagger UI:"
echo "    UserProfile  →  http://localhost:9010/swagger-ui/index.html"
echo "    Post         →  http://localhost:9020/swagger-ui/index.html"
echo ""
echo "  Logs:  docker compose logs -f <service-name>"
echo "  Stop:  docker compose down"
echo "================================================="
echo ""
