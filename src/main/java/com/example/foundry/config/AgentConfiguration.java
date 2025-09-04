package com.example.foundry.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the Azure AI Foundry Agent application.
 * 
 * This class provides type-safe configuration binding from application.properties
 * and environment variables to strongly typed Java properties.
 */
@ConfigurationProperties(prefix = "agent")
public class AgentConfiguration {
    
    private String projectEndpoint;
    private String modelDeploymentName = "gpt-4.1";
    private String agentName = "my-agent";
    private String instructions = "You are a helpful AI assistant that can run math problems and create visualizations.";
    private boolean httpLoggingEnabled = false;
    private boolean httpLogRequestBody = false;
    private boolean httpLogResponseBody = false;
    
    // Getters and setters
    public String getProjectEndpoint() {
        return projectEndpoint;
    }
    
    public void setProjectEndpoint(String projectEndpoint) {
        this.projectEndpoint = projectEndpoint;
    }
    
    public String getModelDeploymentName() {
        return modelDeploymentName;
    }
    
    public void setModelDeploymentName(String modelDeploymentName) {
        this.modelDeploymentName = modelDeploymentName;
    }
    
    public String getAgentName() {
        return agentName;
    }
    
    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
    
    public String getInstructions() {
        return instructions;
    }
    
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    
    public boolean isHttpLoggingEnabled() {
        return httpLoggingEnabled;
    }
    
    public void setHttpLoggingEnabled(boolean httpLoggingEnabled) {
        this.httpLoggingEnabled = httpLoggingEnabled;
    }
    
    public boolean isHttpLogRequestBody() {
        return httpLogRequestBody;
    }
    
    public void setHttpLogRequestBody(boolean httpLogRequestBody) {
        this.httpLogRequestBody = httpLogRequestBody;
    }
    
    public boolean isHttpLogResponseBody() {
        return httpLogResponseBody;
    }
    
    public void setHttpLogResponseBody(boolean httpLogResponseBody) {
        this.httpLogResponseBody = httpLogResponseBody;
    }
}
