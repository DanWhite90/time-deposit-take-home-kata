#!/usr/bin/env sh
cd "$(dirname "$0")"

echo "Starting Postgres and the application."
echo "Swagger UI: http://localhost:8080/swagger-ui.html"
docker compose up --build
