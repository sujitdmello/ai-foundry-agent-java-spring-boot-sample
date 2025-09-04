# Foundry Agent Spring Boot Console Application

This is a Spring Boot console application that integrates with Azure AI Foundry Agent services.

## Changes Made

The application has been converted from a plain Java application to a Spring Boot console application with the following improvements:

### Architecture Changes
- **Spring Boot Framework**: Now uses Spring Boot for dependency injection, configuration management, and application lifecycle
- **Configuration Management**: Uses Spring Boot's `@ConfigurationProperties` for type-safe configuration binding
- **Service Layer**: Business logic extracted into a dedicated `AgentService` class
- **Dependency Injection**: All dependencies are managed by Spring's IoC container

### Key Components
1. **Agent.java**: Main Spring Boot application class implementing `CommandLineRunner`
2. **AgentService.java**: Service class containing the agent workflow logic
3. **AgentConfiguration.java**: Configuration properties class for type-safe configuration
4. **HttpLoggingInterceptor.java**: Utility class for HTTP logging (unchanged)

## Configuration

The application uses Spring Boot's configuration system with the following properties:

### Agent Configuration
```properties
# Agent specific configuration
agent.project-endpoint=${PROJECT_ENDPOINT:}
agent.model-deployment-name=${MODEL_DEPLOYMENT_NAME:gpt-4o}
agent.agent-name=${AGENT_NAME:my-agent}
agent.instructions=${AGENT_INSTRUCTIONS:You are a helpful agent}
agent.http-logging-enabled=${HTTP_LOGGING_ENABLED:false}
agent.http-log-request-body=${HTTP_LOG_REQUEST_BODY:false}
agent.http-log-response-body=${HTTP_LOG_RESPONSE_BODY:false}
```

### Environment Variables
You can override any configuration using environment variables:
- `PROJECT_ENDPOINT`: Azure AI Project endpoint URL
- `MODEL_DEPLOYMENT_NAME`: AI model deployment name
- `AGENT_NAME`: Name for the agent instance
- `AGENT_INSTRUCTIONS`: System instructions for the agent
- `HTTP_LOGGING_ENABLED`: Enable/disable HTTP request logging

## Running the Application

### Option 1: Using Spring Boot Maven Plugin (Recommended)
```bash
mvn spring-boot:run
```

### Option 2: Using Maven with exec plugin
```bash
mvn compile exec:java
```

### Option 3: Build and run JAR
```bash
mvn clean package
java -jar target/foundry-agent-app-1.0.0.jar
```

### With Command Line Arguments
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--arg1=value1 --arg2=value2"
```

### With Environment Variables
```bash
# Windows
set PROJECT_ENDPOINT=your-endpoint-url
set MODEL_DEPLOYMENT_NAME=gpt-4o
mvn spring-boot:run

# Linux/macOS
export PROJECT_ENDPOINT=your-endpoint-url
export MODEL_DEPLOYMENT_NAME=gpt-4o
mvn spring-boot:run
```

## Development Features

### Configuration Validation
Spring Boot automatically validates configuration properties and provides helpful error messages for missing required values.

### Profiles
You can use Spring profiles for different environments:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### External Configuration
You can provide external configuration files:
```bash
java -jar target/foundry-agent-app-1.0.0.jar --spring.config.location=file:./config/
```

## Testing

Run tests with:
```bash
mvn test
```

## Building

Build the application:
```bash
mvn clean package
```

This creates an executable JAR file in the `target/` directory.

## Dependencies

The application includes:
- Spring Boot Starter
- Spring Boot Configuration Processor
- Azure AI Agents Persistent SDK
- Azure Identity SDK
- Azure Core HTTP Netty
- Reactor Netty HTTP
- Jackson for JSON processing

## Benefits of Spring Boot Migration

1. **Improved Configuration Management**: Type-safe configuration with validation
2. **Better Testing Support**: Spring Boot test utilities and auto-configuration
3. **Dependency Injection**: Cleaner, more maintainable code structure
4. **Production Features**: Health checks, metrics, and monitoring capabilities
5. **Auto-configuration**: Automatic setup of common components
6. **Profile Support**: Environment-specific configurations
7. **External Configuration**: Flexible configuration sources

## Legacy Support

The application maintains backward compatibility with the original configuration keys for a smooth transition.
