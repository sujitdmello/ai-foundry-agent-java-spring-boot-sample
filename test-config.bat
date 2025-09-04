@echo off
echo Testing Foundry Agent Application with Environment Variables
echo.

echo ===== Test 1: Default Configuration =====
mvn -q exec:java
echo.

echo ===== Test 2: With Environment Variables =====
set FEATURE_DEBUG_MODE=true
set SERVER_PORT=9090
set APP_ENVIRONMENT=testing
mvn -q exec:java
echo.

echo ===== Test 3: With System Properties =====
mvn -q exec:java -Djava.system.properties="feature.debug.mode=true"
echo.

echo Environment variables test completed!
