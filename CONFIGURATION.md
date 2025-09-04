# Environment Configuration Guide

This guide explains how to use the environment configuration system in the Foundry Agent Java application.

## Overview

The application supports configuration through multiple sources with the following priority order:

1. **Environment Variables** (highest priority)
2. **System Properties** (JVM -D arguments)
3. **Application Properties File** (`src/main/resources/application.properties`)
4. **Default Values** (lowest priority)

## Configuration Classes

### EnvironmentConfig.java

Located at `src/main/java/com/example/config/EnvironmentConfig.java`, this utility class provides methods to:

- Retrieve configuration values with fallback hierarchy
- Handle type conversion (String, int, boolean)
- Validate required configuration
- Debug configuration sources

### Key Methods

```java
// Get configuration with default fallback
String value = EnvironmentConfig.getConfig("key.name", "defaultValue");

// Get required configuration (throws exception if missing)
String required = EnvironmentConfig.getRequiredConfig("required.key");

// Type-safe getters
int port = EnvironmentConfig.getConfigAsInt("server.port", 8080);
boolean enabled = EnvironmentConfig.getConfigAsBoolean("feature.enabled", false);

// Debug configuration
EnvironmentConfig.printConfigurationInfo();
```

## Configuration Sources

### 1. Environment Variables

Environment variables are checked first and have the highest priority.

**Setting Environment Variables:**

**Windows PowerShell:**
```powershell
$env:SERVER_PORT = "9090"
$env:FEATURE_DEBUG_MODE = "true"
```

**Windows Command Prompt:**
```cmd
set SERVER_PORT=9090
set FEATURE_DEBUG_MODE=true
```

**Linux/macOS:**
```bash
export SERVER_PORT=9090
export FEATURE_DEBUG_MODE=true
```

### 2. System Properties

System properties are JVM arguments passed with the `-D` flag.

**Examples:**
```bash
java -Dserver.port=9090 -Dfeature.debug.mode=true -jar app.jar
mvn exec:java -Dserver.port=9090
```

### 3. Application Properties File

Default configuration is stored in `src/main/resources/application.properties`.

**Example content:**
```properties
app.name=Foundry Agent Application
server.port=8080
feature.debug.mode=false
api.timeout=30000
```

### 4. Default Values

Hardcoded fallback values provided in the code.

## Configuration Keys

### Application Configuration
- `app.name` - Application name
- `app.version` - Application version
- `app.environment` - Runtime environment (development, production, etc.)

### Server Configuration
- `server.host` - Server hostname
- `server.port` - Server port number

### Feature Flags
- `feature.debug.mode` - Enable debug mode
- `feature.experimental.enabled` - Enable experimental features

### Logging Configuration
- `logging.level` - Logging level
- `logging.file.enabled` - Enable file logging
- `logging.file.path` - Log file path

### API Configuration
- `api.timeout` - API timeout in milliseconds
- `api.retry.attempts` - Number of retry attempts
- `api.base.url` - Base API URL

### Azure Configuration
- `azure.client.id` - Azure Client ID
- `azure.client.secret` - Azure Client Secret
- `azure.tenant.id` - Azure Tenant ID
- `azure.subscription.id` - Azure Subscription ID

## Usage Examples

### Basic Configuration Retrieval

```java
import com.example.config.EnvironmentConfig;

public class MyService {
    private final String apiUrl;
    private final int timeout;
    private final boolean debugMode;
    
    public MyService() {
        this.apiUrl = EnvironmentConfig.getConfig("api.base.url", "https://localhost:8080");
        this.timeout = EnvironmentConfig.getConfigAsInt("api.timeout", 30000);
        this.debugMode = EnvironmentConfig.getConfigAsBoolean("feature.debug.mode", false);
    }
}
```

### Required Configuration

```java
public class DatabaseService {
    public DatabaseService() {
        // These will throw IllegalStateException if not configured
        String dbUrl = EnvironmentConfig.getRequiredConfig("database.url");
        String dbUser = EnvironmentConfig.getRequiredConfig("database.username");
        String dbPassword = EnvironmentConfig.getRequiredConfig("database.password");
    }
}
```

### Configuration Debugging

```java
public class App {
    public static void main(String[] args) {
        boolean debugMode = EnvironmentConfig.getConfigAsBoolean("feature.debug.mode", false);
        
        if (debugMode) {
            // This prints all configuration sources
            EnvironmentConfig.printConfigurationInfo();
        }
    }
}
```

## Running the Application with Configuration

### Using Maven Exec Plugin

```bash
# Default configuration
mvn exec:java

# With system properties
mvn exec:java -Dserver.port=9090 -Dfeature.debug.mode=true

# With command line arguments
mvn exec:java -Dexec.args="arg1 arg2"
```

### Using Built JAR

```bash
# Build the JAR
mvn package

# Run with default configuration
java -jar target/foundry-agent-app-1.0.0.jar

# Run with system properties
java -Dserver.port=9090 -Dfeature.debug.mode=true -jar target/foundry-agent-app-1.0.0.jar

# Run with environment variables (set before running)
export FEATURE_DEBUG_MODE=true
java -jar target/foundry-agent-app-1.0.0.jar
```

## Best Practices

1. **Use descriptive configuration keys** with dot notation (e.g., `server.port`, `database.url`)

2. **Provide sensible defaults** for all optional configuration

3. **Validate required configuration early** at application startup

4. **Use type-safe getters** instead of parsing strings manually

5. **Document configuration options** in your README or configuration guide

6. **Use environment-specific configuration files** for different deployment environments

7. **Never commit sensitive values** (passwords, API keys) to version control

8. **Use debug mode** to troubleshoot configuration issues

## Testing Configuration

The application includes comprehensive tests in `EnvironmentConfigTest.java` that demonstrate:

- Default value fallbacks
- System property override behavior
- Type conversion
- Required configuration validation
- Error handling for invalid values

Run tests with:
```bash
mvn test
```

## Environment-Specific Configuration

### Development
```properties
app.environment=development
feature.debug.mode=true
logging.level=DEBUG
server.port=8080
```

### Production
```properties
app.environment=production
feature.debug.mode=false
logging.level=WARN
server.port=80
```

### Testing
```properties
app.environment=testing
feature.debug.mode=true
logging.level=INFO
server.port=9090
```

## Troubleshooting

1. **Configuration not being picked up:**
   - Check the configuration key spelling
   - Verify the priority order (environment > system properties > properties file > defaults)
   - Use debug mode to see all configuration sources

2. **Type conversion errors:**
   - Check that numeric values are valid integers
   - Boolean values should be "true" or "false"
   - Invalid values will log warnings and use defaults

3. **Required configuration missing:**
   - Check that all required environment variables or system properties are set
   - Review the exception message for the missing configuration key

4. **Maven exec plugin not picking up environment variables:**
   - Use system properties with `-D` flags instead
   - Or build the JAR and run it directly with `java -jar`
