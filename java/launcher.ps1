$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot

Write-Host "Starting Postgres and the application."
Write-Host "Swagger UI: http://localhost:8080/swagger-ui.html"
docker compose up --build
