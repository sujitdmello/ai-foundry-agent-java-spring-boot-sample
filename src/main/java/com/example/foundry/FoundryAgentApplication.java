package com.example.foundry;

import com.example.foundry.config.AgentConfiguration;
import com.example.foundry.service.AgentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Azure AI Foundry Agent Spring Boot Console Application
 * 
 * This application demonstrates how to create and interact with Azure AI Foundry agents
 * using the Azure AI Agents SDK in a Spring Boot environment.
 */
@SpringBootApplication
@EnableConfigurationProperties(AgentConfiguration.class)
public class FoundryAgentApplication implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(FoundryAgentApplication.class);
    
    private final AgentService agentService;
    private final AgentConfiguration agentConfig;
    
    @Value("${agent.skip-execution:false}")
    private boolean skipExecution;
    
    @Autowired
    public FoundryAgentApplication(AgentService agentService, AgentConfiguration agentConfig) {
        this.agentService = agentService;
        this.agentConfig = agentConfig;
    }
    
    public static void main(String[] args) {
        logger.info("=== Azure AI Foundry Agent Application Starting ===");
        SpringApplication.run(FoundryAgentApplication.class, args);
    }
    
    @Override
    public void run(String... args) throws Exception {
        if (skipExecution) {
            logger.info("Agent execution skipped due to configuration");
            return;
        }
        
        logger.info("=== Starting Foundry Agent Console Application ===");
        
        // Display configuration info
        displayApplicationInfo();
        
        // Execute the agent workflow
        agentService.executeAgentWorkflow();
        
        logger.info("=== Foundry Agent Application Completed ===");
    }
    
    private void displayApplicationInfo() {
        logger.info("Agent Name: {}", agentConfig.getAgentName());
        logger.info("Model Deployment: {}", agentConfig.getModelDeploymentName());
        
        // Only show endpoint if configured (don't show empty placeholders)
        if (agentConfig.getProjectEndpoint() != null && !agentConfig.getProjectEndpoint().trim().isEmpty()) {
            logger.info("Project Endpoint: {}", agentConfig.getProjectEndpoint());
        } else {
            logger.warn("Project Endpoint not configured. Please set PROJECT_ENDPOINT environment variable.");
        }
        
        logger.info("HTTP Logging Enabled: {}", agentConfig.isHttpLoggingEnabled());
    }
}
