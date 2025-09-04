# Azure AI Foundry Agent Demo

A Spring Boot console application demonstrating how to create and interact with Azure AI Foundry agents using the Azure AI Agents SDK.

## Features

- 🤖 **Azure AI Foundry Integration**: Create and interact with AI agents using the official Azure AI Agents SDK
- 🏗️ **Spring Boot Framework**: Modern Spring Boot application with dependency injection and configuration management
- 📝 **Type-safe Configuration**: Externalized configuration with support for environment variables and property files
- 📡 **HTTP Request Logging**: Optional detailed logging of HTTP requests and responses for debugging
- 🔧 **Easy Setup**: Simple configuration and quick start with minimal dependencies
- ✅ **Production Ready**: Configurable logging levels and proper resource management

## Prerequisites

- **Java 17** or higher
- **Maven 3.6** or higher
- **Azure AI Foundry Project** with an active deployment

## Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/example/azure-ai-foundry-agent.git
cd azure-ai-foundry-agent
```

### 2. Configure Your Azure AI Foundry Endpoint

Set your Azure AI Foundry project endpoint as an environment variable:

```bash
# Linux/macOS
export PROJECT_ENDPOINT="https://your-project.your-region.services.ai.azure.com/api/projects/your-project-name"

# Windows PowerShell
$env:PROJECT_ENDPOINT="https://your-project.your-region.services.ai.azure.com/api/projects/your-project-name"

# Windows Command Prompt
set PROJECT_ENDPOINT=https://your-project.your-region.services.ai.azure.com/api/projects/your-project-name
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

Or build and run the JAR:

```bash
mvn clean package
java -jar target/azure-ai-foundry-agent-1.0.0.jar
```

## Configuration

The application supports multiple configuration methods with the following priority:

1. **Environment Variables** (highest priority)
2. **System Properties** (JVM `-D` arguments)
3. **Application Properties File** (`application.properties`)
4. **Default Values** (lowest priority)

### Available Configuration Options

| Property | Environment Variable | Default | Description |
|----------|---------------------|---------|-------------|
| `agent.project-endpoint` | `PROJECT_ENDPOINT` | *(required)* | Your Azure AI Foundry project endpoint |
| `agent.model-deployment-name` | `MODEL_DEPLOYMENT_NAME` | `gpt-4o` | Model deployment name |
| `agent.agent-name` | `AGENT_NAME` | `DemoAgent` | Name for the AI agent |
| `agent.instructions` | `AGENT_INSTRUCTIONS` | *default instructions* | System instructions for the agent |
| `agent.http-logging-enabled` | `HTTP_LOGGING_ENABLED` | `false` | Enable detailed HTTP request/response logging |

### Example Configuration

**Environment Variables:**
```bash
export PROJECT_ENDPOINT="https://your-project.services.ai.azure.com/api/projects/your-project"
export MODEL_DEPLOYMENT_NAME="gpt-4o"
export AGENT_NAME="MyDemoAgent"
export HTTP_LOGGING_ENABLED="true"
```

**application.properties:**
```properties
agent.project-endpoint=${PROJECT_ENDPOINT:}
agent.model-deployment-name=gpt-4o
agent.agent-name=MyDemoAgent
agent.instructions=You are a helpful AI assistant specialized in data analysis.
agent.http-logging-enabled=false
```

## HTTP Request Logging

Enable detailed HTTP logging to see all API interactions:

```bash
export HTTP_LOGGING_ENABLED=true
mvn spring-boot:run
```

This will display:
- Request methods and URLs
- Request/response headers (sensitive headers are masked)
- Response status codes
- Timestamps for debugging

## Application Structure

```
src/main/java/com/example/foundry/
├── FoundryAgentApplication.java     # Main Spring Boot application
├── config/
│   └── AgentConfiguration.java     # Configuration properties
├── service/
│   └── AgentService.java          # Agent business logic
└── util/
    └── HttpLoggingInterceptor.java # HTTP logging utility
```

### Key Components

- **FoundryAgentApplication**: Main entry point with Spring Boot configuration
- **AgentConfiguration**: Type-safe configuration binding from properties
- **AgentService**: Core service that handles agent creation and interactions
- **HttpLoggingInterceptor**: Custom HTTP policy for detailed request/response logging

## Example Usage

The application demonstrates:

1. **Agent Creation**: Creates an AI agent with custom instructions
2. **Thread Management**: Creates a conversation thread
3. **Message Interaction**: Sends a message and processes the response
4. **Resource Cleanup**: Proper cleanup of HTTP resources

## Development

### Running Tests

```bash
mvn test
```

### Building

```bash
mvn clean package
```

### Running with Different Profiles

```bash
# Development profile with debug logging
mvn spring-boot:run -Dspring.profiles.active=dev

# With custom configuration
mvn spring-boot:run -DPROJECT_ENDPOINT="your-endpoint" -DHTTP_LOGGING_ENABLED=true
```

## Troubleshooting

### Common Issues

1. **Missing PROJECT_ENDPOINT**: Ensure your Azure AI Foundry project endpoint is properly configured
2. **Authentication Issues**: Verify your Azure credentials and project access
3. **Network Connectivity**: Check firewall settings if running in corporate environments

### Enable Debug Logging

```bash
# Enable debug logging for the application
export LOGGING_LEVEL_COM_EXAMPLE_FOUNDRY=DEBUG

# Or set in application.properties
logging.level.com.example.foundry=DEBUG
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Resources

- [Azure AI Foundry Documentation](https://docs.microsoft.com/azure/ai-foundry/)
- [Azure AI Agents SDK Documentation](https://docs.microsoft.com/azure/ai-agents/)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)

## Support

For issues and questions:
- Check the [troubleshooting section](#troubleshooting)
- Review [Azure AI Foundry documentation](https://docs.microsoft.com/azure/ai-foundry/)
- Create an issue in this repository
