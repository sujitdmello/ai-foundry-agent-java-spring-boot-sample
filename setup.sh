#!/bin/bash

# Azure AI Foundry Agent Demo - Setup Script

echo "🚀 Setting up Azure AI Foundry Agent Demo..."

# Check Java version
echo "Checking Java version..."
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 17 or higher."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ Java 17 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi
echo "✅ Java version check passed"

# Check Maven
echo "Checking Maven..."
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven 3.6 or higher."
    exit 1
fi
echo "✅ Maven check passed"

# Check for PROJECT_ENDPOINT
echo "Checking configuration..."
if [ -z "$PROJECT_ENDPOINT" ]; then
    echo "⚠️  PROJECT_ENDPOINT environment variable is not set."
    echo "Please set it to your Azure AI Foundry project endpoint:"
    echo ""
    echo "export PROJECT_ENDPOINT=\"https://your-project.your-region.services.ai.azure.com/api/projects/your-project-name\""
    echo ""
    echo "You can still build the project, but you'll need to set this before running."
else
    echo "✅ PROJECT_ENDPOINT is configured"
fi

# Build the project
echo "Building the project..."
if mvn clean compile; then
    echo "✅ Build successful!"
else
    echo "❌ Build failed!"
    exit 1
fi

echo ""
echo "🎉 Setup completed successfully!"
echo ""
echo "To run the application:"
echo "  mvn spring-boot:run"
echo ""
echo "To run with HTTP logging enabled:"
echo "  HTTP_LOGGING_ENABLED=true mvn spring-boot:run"
echo ""
echo "For more information, see README.md"
