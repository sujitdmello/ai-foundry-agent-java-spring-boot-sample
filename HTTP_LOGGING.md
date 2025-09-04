# HTTP Request/Response Logging

This application now includes comprehensive HTTP logging capabilities to display OpenAI API requests and responses directly in the console. This is extremely useful for debugging, monitoring, and understanding the communication between your application and the OpenAI/Azure AI endpoints.

## Features

- **Complete Request Logging**: Method, URL, headers, and request body
- **Complete Response Logging**: Status code, headers, and response body  
- **Security-Aware**: Automatically masks authorization headers and API keys
- **Pretty-Printed JSON**: All JSON payloads are formatted for easy reading
- **Configurable**: Fine-grained control over what gets logged
- **Production-Safe**: Disabled by default, can be enabled per environment

## Configuration Options

You can control HTTP logging using environment variables, system properties, or the `application.properties` file:

### Environment Variables (Recommended)
```bash
# Enable/disable HTTP logging
HTTP_LOGGING_ENABLED=true

# Control request body logging (default: true)
HTTP_LOG_REQUEST_BODY=true

# Control response body logging (default: true)  
HTTP_LOG_RESPONSE_BODY=true
```

### System Properties
```bash
java -DHTTP_LOGGING_ENABLED=true -DHTTP_LOG_REQUEST_BODY=false -jar app.jar
```

### Application Properties File
```properties
# HTTP Logging Configuration
HTTP_LOGGING_ENABLED=false
HTTP_LOG_REQUEST_BODY=true
HTTP_LOG_RESPONSE_BODY=true
```

## Example Output

When enabled, you'll see detailed logs like this:

```
========== OpenAI API REQUEST ==========
Timestamp: 2025-09-02T10:57:31.3366498
Method: POST
URL: https://hrbaif.services.ai.azure.com/api/projects/multiAgents/assistants?api-version=2025-05-15-preview
Headers:
  Date: Tue, 02 Sep 2025 14:57:28 GMT
  Authorization: ***MASKED***
  Content-Length: 116
  Content-Type: application/json
  x-ms-client-request-id: 03f49d74-b7e9-48c4-9120-91425fa84282
  Accept: application/json
  User-Agent: azsdk-java-azure-ai-agents-persistent/1.0.0-beta.2
Request Body:
{
  "model" : "gpt-4.1",
  "name" : "my-agent", 
  "instructions" : "You are a helpful agent",
  "tools" : [ {
    "type" : "code_interpreter"
  } ]
}
=======================================

========== OpenAI API RESPONSE ==========
Timestamp: 2025-09-02T10:57:32.9940901
Status Code: 200
Headers:
  Date: Tue, 02 Sep 2025 14:57:32 GMT
  X-Request-ID: 9ef36c4b58f2360849d81bd4cd77873a
  Content-Length: 436
  Content-Type: application/json
Response Body:
{
  "id" : "asst_V4dEDCHrKqp4tfUeXJ7GsLAR",
  "object" : "assistant", 
  "created_at" : 1756825053,
  "name" : "my-agent",
  "model" : "gpt-4.1",
  "instructions" : "You are a helpful agent",
  "tools" : [ {
    "type" : "code_interpreter"
  } ]
}
==========================================
```

## Quick Start

### Enable Logging for Development
```bash
# Set environment variable
export HTTP_LOGGING_ENABLED=true  # On Linux/Mac
$env:HTTP_LOGGING_ENABLED="true"  # On Windows PowerShell

# Run your application
mvn exec:java -Dexec.mainClass="com.microsoft.ise.Agent"
```

### Disable Request Bodies for Large Payloads
```bash
export HTTP_LOGGING_ENABLED=true
export HTTP_LOG_REQUEST_BODY=false
```

### Disable Response Bodies for Large Responses
```bash
export HTTP_LOGGING_ENABLED=true  
export HTTP_LOG_RESPONSE_BODY=false
```

## Use Cases

- **API Debugging**: See exactly what requests are being sent and what responses are received
- **Rate Limiting Analysis**: Monitor response headers for rate limit information
- **Token Usage Tracking**: View usage data in response headers
- **Error Diagnosis**: Capture full error responses for troubleshooting
- **Performance Analysis**: Monitor request/response times and sizes
- **Compliance Auditing**: Log API interactions for compliance requirements

## Security Considerations

- Authorization headers and API keys are automatically masked as `***MASKED***`
- Consider disabling body logging in production for sensitive data
- Log output may contain sensitive information - secure your log files appropriately
- Body logging can generate large amounts of log data

## Implementation Details

The HTTP logging is implemented using Azure SDK's `HttpPipelinePolicy` interface, which intercepts all HTTP requests and responses at the transport layer. This ensures complete capture of the actual network traffic without affecting application logic.

The implementation is in:
- `HttpLoggingInterceptor.java` - The logging pipeline policy
- `EnvironmentConfig.java` - Configuration management
- `Agent.java` - Integration with Azure AI client
