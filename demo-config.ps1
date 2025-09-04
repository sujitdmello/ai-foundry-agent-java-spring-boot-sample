# Final Environment Variables Demo Script

Write-Host "===== Foundry Agent Application Environment Configuration Demo =====" -ForegroundColor Green
Write-Host ""

Write-Host "1. Default Configuration (from application.properties):" -ForegroundColor Yellow
java -jar target/foundry-agent-app-1.0.0.jar
Write-Host ""

Write-Host "2. Override with System Properties (dot notation):" -ForegroundColor Yellow
java "-Dfeature.debug.mode=true" "-Dserver.port=9090" "-Dapp.environment=production" -jar target/foundry-agent-app-1.0.0.jar
Write-Host ""

Write-Host "3. Demonstrating Required Configuration Error:" -ForegroundColor Yellow
java "-DTEST_MODE=required_config_test" -jar target/foundry-agent-app-1.0.0.jar
Write-Host ""

Write-Host "4. Setting Environment Variables (for external processes):" -ForegroundColor Yellow
Write-Host "Note: Java primarily uses system properties, but environment variables"
Write-Host "      are available through System.getenv() and our configuration utility"
Write-Host "      supports both with dot notation preference."
Write-Host ""

Write-Host "===== Configuration Hierarchy (Priority Order) =====" -ForegroundColor Cyan
Write-Host "1. Environment Variables (highest priority)"
Write-Host "2. System Properties (-D flags)"
Write-Host "3. Application Properties File"
Write-Host "4. Default Values (lowest priority)"
Write-Host ""

Write-Host "Demo completed! Check the debug output above to see configuration sources." -ForegroundColor Green
