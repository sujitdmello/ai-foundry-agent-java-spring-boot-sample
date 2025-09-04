@echo off
REM Azure AI Foundry Agent Demo - Setup Script for Windows

echo 🚀 Setting up Azure AI Foundry Agent Demo...

REM Check Java version
echo Checking Java version...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java is not installed. Please install Java 17 or higher.
    exit /b 1
)
echo ✅ Java version check passed

REM Check Maven
echo Checking Maven...
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Maven is not installed. Please install Maven 3.6 or higher.
    exit /b 1
)
echo ✅ Maven check passed

REM Check for PROJECT_ENDPOINT
echo Checking configuration...
if "%PROJECT_ENDPOINT%"=="" (
    echo ⚠️  PROJECT_ENDPOINT environment variable is not set.
    echo Please set it to your Azure AI Foundry project endpoint:
    echo.
    echo $env:PROJECT_ENDPOINT="https://your-project.your-region.services.ai.azure.com/api/projects/your-project-name"
    echo.
    echo You can still build the project, but you'll need to set this before running.
) else (
    echo ✅ PROJECT_ENDPOINT is configured
)

REM Build the project
echo Building the project...
mvn clean compile
if %errorlevel% neq 0 (
    echo ❌ Build failed!
    exit /b 1
)
echo ✅ Build successful!

echo.
echo 🎉 Setup completed successfully!
echo.
echo To run the application:
echo   mvn spring-boot:run
echo.
echo To run with HTTP logging enabled:
echo   set HTTP_LOGGING_ENABLED=true && mvn spring-boot:run
echo.
echo For more information, see README.md
