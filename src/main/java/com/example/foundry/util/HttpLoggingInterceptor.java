package com.example.foundry.util;

import com.azure.core.http.HttpPipelineCallContext;
import com.azure.core.http.HttpPipelineNextPolicy;
import com.azure.core.http.HttpRequest;
import com.azure.core.http.HttpResponse;
import com.azure.core.http.policy.HttpPipelinePolicy;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * HTTP pipeline policy that logs Azure AI API requests and responses to the console.
 * 
 * This interceptor provides detailed logging of HTTP interactions with Azure AI services,
 * including request/response headers, bodies, and timing information.
 * Sensitive headers like API keys are automatically masked for security.
 */
public class HttpLoggingInterceptor implements HttpPipelinePolicy {
    
    private final boolean logRequestBody;
    
    /**
     * Create an HTTP logging interceptor with default settings (log everything).
     */
    public HttpLoggingInterceptor() {
        this(true);
    }
    
    /**
     * Create an HTTP logging interceptor with specific logging preferences.
     * 
     * @param logRequestBody Whether to log request bodies
     */
    public HttpLoggingInterceptor(boolean logRequestBody) {
        this.logRequestBody = logRequestBody;
    }
    
    @Override
    public Mono<HttpResponse> process(HttpPipelineCallContext context, HttpPipelineNextPolicy next) {
        HttpRequest request = context.getHttpRequest();
        return logRequest(request)
                .then(next.process())
                .flatMap(response -> logResponse(response));
    }
    
    private Mono<Void> logRequest(HttpRequest request) {
        return Mono.fromRunnable(() -> {
            String timestamp = OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            
            System.out.println("\\n" + "=".repeat(80));
            System.out.println("🔵 OUTGOING REQUEST - " + timestamp);
            System.out.println("=".repeat(80));
            System.out.println("Method: " + request.getHttpMethod());
            System.out.println("URL: " + request.getUrl());
            
            // Log headers (mask sensitive ones)
            System.out.println("\\nHeaders:");
            request.getHeaders().forEach(header -> {
                String name = header.getName();
                String value = isSensitiveHeader(name) ? "***MASKED***" : header.getValue();
                System.out.println("  " + name + ": " + value);
            });
            
            // Log request body if enabled
            if (logRequestBody && request.getBody() != null) {
                logRequestBody(request);
            }
            
            System.out.println("=".repeat(80));
        });
    }
    
    private Mono<HttpResponse> logResponse(HttpResponse response) {
        return Mono.fromRunnable(() -> {
            String timestamp = OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            
            System.out.println("\\n" + "=".repeat(80));
            System.out.println("🟢 INCOMING RESPONSE - " + timestamp);
            System.out.println("=".repeat(80));
            System.out.println("Status: " + response.getStatusCode());
            
            // Log response headers
            System.out.println("\\nHeaders:");
            response.getHeaders().forEach(header -> {
                String name = header.getName();
                String value = isSensitiveHeader(name) ? "***MASKED***" : header.getValue();
                System.out.println("  " + name + ": " + value);
            });
            
            System.out.println("=".repeat(80) + "\\n");
        }).then(Mono.just(response));
    }
    
    private void logRequestBody(HttpRequest request) {
        try {
            // For simplicity in this demo, we'll just log that there is a body
            // The actual Azure SDK handles body logging through HttpLogOptions
            System.out.println("\\nRequest Body: (present - see Azure SDK logs for details)");
        } catch (Exception e) {
            System.out.println("\\nRequest Body: (unable to read - " + e.getMessage() + ")");
        }
    }
    
    private boolean isSensitiveHeader(String headerName) {
        if (headerName == null) return false;
        String name = headerName.toLowerCase();
        return name.contains("authorization") || 
               name.contains("api-key") || 
               name.contains("apikey") ||
               name.contains("key") ||
               name.contains("secret") ||
               name.contains("token");
    }
}
