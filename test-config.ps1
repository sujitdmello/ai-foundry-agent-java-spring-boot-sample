# PowerShell script to test environment variable configuration

Write-Host "Testing Foundry Agent Application with Environment Variables" -ForegroundColor Green
Write-Host ""

Write-Host "===== Test 1: Default Configuration =====" -ForegroundColor Yellow
mvn -q exec:java
Write-Host ""

Write-Host "===== Test 2: With Environment Variables =====" -ForegroundColor Yellow
$env:FEATURE_DEBUG_MODE = "true"
$env:SERVER_PORT = "9090" 
$env:APP_ENVIRONMENT = "testing"
mvn -q exec:java
Write-Host ""

Write-Host "===== Test 3: Building and Running JAR with Environment Variables =====" -ForegroundColor Yellow
mvn -q package
if ($LASTEXITCODE -eq 0) {
    java -Dfeature.debug.mode=true -Dserver.port=9090 -jar target/foundry-agent-app-1.0.0.jar
}
Write-Host ""

Write-Host "===== Test 4: Testing Required Configuration (Should Fail) =====" -ForegroundColor Yellow
Write-Host "This demonstrates what happens when a required config is missing:"
$env:TEST_MODE = "required_config_test"
mvn -q exec:java
Write-Host ""

Write-Host "Environment variables test completed!" -ForegroundColor Green
