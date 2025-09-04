package com.example.foundry.service;

import com.azure.ai.agents.persistent.MessagesClient;
import com.azure.ai.agents.persistent.PersistentAgentsAdministrationClient;
import com.azure.ai.agents.persistent.PersistentAgentsClient;
import com.azure.ai.agents.persistent.PersistentAgentsClientBuilder;
import com.azure.ai.agents.persistent.RunsClient;
import com.azure.ai.agents.persistent.ThreadsClient;
import com.azure.ai.agents.persistent.models.*;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.http.policy.HttpLogOptions;
import com.azure.core.http.rest.PagedIterable;
import com.example.foundry.config.AgentConfiguration;
import com.example.foundry.util.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class that handles Azure AI Foundry agent operations.
 * 
 * This service demonstrates how to:
 * - Create an AI agent with specific instructions
 * - Create a thread for conversation
 * - Send messages and process responses
 * - Handle image outputs from the agent
 */
@Service
public class AgentService {
    
    private static final Logger logger = LoggerFactory.getLogger(AgentService.class);
    
    private final AgentConfiguration config;
    private PersistentAgentsClient agentsClient;
    private PersistentAgentsAdministrationClient administrationClient;
    private ThreadsClient threadsClient;
    private MessagesClient messagesClient;
    private RunsClient runsClient;
    
    @Autowired
    public AgentService(AgentConfiguration config) {
        this.config = config;
    }
    
    /**
     * Execute the complete agent workflow including creation, interaction, and cleanup.
     */
    public void executeAgentWorkflow() {
        logger.info("=== Starting Foundry Agent Workflow ===");
        
        try {
            // Validate configuration
            validateConfiguration();
            
            // Initialize the Azure AI client
            initializeClient();
            
            // Create an agent
            PersistentAgent agent = createAgent();
            
            // Create a thread for conversation
            PersistentAgentThread thread = createThread();
            
            // Send a message and get response
            String message = "Hi, Agent! Draw a graph for a line with a slope of 4 and y-intercept of 9.";
            sendMessageAndProcessResponse(agent, thread, message);
            
        } catch (Exception e) {
            logger.error("Error in agent workflow: {}", e.getMessage(), e);
            throw new RuntimeException("Agent workflow failed", e);
        } finally {
            // Clean up resources
            cleanup();
        }
        
        logger.info("Agent workflow completed!");
    }
    
    private void validateConfiguration() {
        if (config.getProjectEndpoint() == null || config.getProjectEndpoint().trim().isEmpty()) {
            throw new IllegalStateException(
                "PROJECT_ENDPOINT environment variable is required. " +
                "Please set it to your Azure AI Foundry project endpoint."
            );
        }
    }
    
    private void initializeClient() {
        logger.debug("Initializing Azure AI Agents client...");
        
        PersistentAgentsClientBuilder builder = new PersistentAgentsClientBuilder()
                .endpoint(config.getProjectEndpoint())
                .credential(new DefaultAzureCredentialBuilder().build());
        
        // Configure HTTP logging if enabled
        if (config.isHttpLoggingEnabled()) {
            HttpLogOptions logOptions = new HttpLogOptions()
                    .setLogLevel(HttpLogDetailLevel.BODY_AND_HEADERS);
            builder.httpLogOptions(logOptions);
            
            // Add custom interceptor for enhanced logging
            builder.addPolicy(new HttpLoggingInterceptor(config.isHttpLogRequestBody()));
        }
        
        this.agentsClient = builder.buildClient();
        this.administrationClient = agentsClient.getPersistentAgentsAdministrationClient();
        this.threadsClient = agentsClient.getThreadsClient();
        this.messagesClient = agentsClient.getMessagesClient();
        this.runsClient = agentsClient.getRunsClient();
        logger.debug("Azure AI Agents client initialized successfully");
    }
    
    private PersistentAgent createAgent() {
        logger.debug("About to create agent...");
        
        CreateAgentOptions options = new CreateAgentOptions(config.getModelDeploymentName())
                .setName(config.getAgentName())
                .setInstructions(config.getInstructions())
                .setTools(List.of(new CodeInterpreterToolDefinition()));
        
        PersistentAgent agent = administrationClient.createAgent(options);
        
        logger.debug("Agent object: {}", agent);
        logger.debug("Agent is null: {}", agent == null);
        if (agent != null) {
            logger.debug("Agent ID: {}", agent.getId());
            logger.info("Agent created successfully: {}", agent.getId());
        }
        
        return agent;
    }
    
    private PersistentAgentThread createThread() {
        PersistentAgentThread thread = threadsClient.createThread();
        logger.info("Thread created successfully: {}", thread.getId());
        return thread;
    }
    
    private void sendMessageAndProcessResponse(PersistentAgent agent, PersistentAgentThread thread, String messageText) {
        // Create and send message
        ThreadMessage message = messagesClient.createMessage(
            thread.getId(),
            MessageRole.USER,
            messageText);
        logger.info("Message created successfully: {}", message.getId());
        
        // Create and monitor run
        CreateRunOptions runOptions = new CreateRunOptions(thread.getId(), agent.getId());
        ThreadRun run = runsClient.createRun(runOptions);
        logger.info("Run created successfully: {}", run.getId());
        
        // Wait for the run to complete
        waitForRunCompletion(thread.getId(), run.getId());
        
        // Get and display messages
        displayMessages(thread);
    }
    
    private void waitForRunCompletion(String threadId, String runId) {
        logger.info("Waiting for run to complete...");
        
        ThreadRun run;
        int maxAttempts = 30; // Maximum number of polling attempts
        int attemptCount = 0;
        long pollIntervalMs = 2000; // Poll every 2 seconds
        
        do {
            try {
                Thread.sleep(pollIntervalMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread was interrupted while waiting for run completion", e);
            }
            
            run = runsClient.getRun(threadId, runId);
            RunStatus status = run.getStatus();
            logger.debug("Run status: {}", status);
            
            attemptCount++;
            
            if (status == RunStatus.COMPLETED) {
                logger.info("Run completed successfully after {} attempts", attemptCount);
                return;
            } else if (status == RunStatus.FAILED || status == RunStatus.CANCELLED || status == RunStatus.EXPIRED) {
                String errorMessage = String.format("Run failed with status: %s", status);
                if (run.getLastError() != null) {
                    errorMessage += ". Error: " + run.getLastError().getMessage();
                }
                throw new RuntimeException(errorMessage);
            }
            
            if (attemptCount >= maxAttempts) {
                throw new RuntimeException("Run did not complete within the expected time. Status: " + status);
            }
            
        } while (run.getStatus() == RunStatus.QUEUED || 
                 run.getStatus() == RunStatus.IN_PROGRESS || 
                 run.getStatus() == RunStatus.REQUIRES_ACTION);
    }
    
    private void displayMessages(PersistentAgentThread thread) {
        PagedIterable<ThreadMessage> messages = messagesClient.listMessages(thread.getId());
        
        for (ThreadMessage message : messages) {
            String timestamp = message.getCreatedAt().toString();
            String role = message.getRole().toString().toLowerCase();
            logger.info("{} - {} :", timestamp, role);
            
            for (MessageContent content : message.getContent()) {
                if (content instanceof MessageTextContent textContent) {
                    logger.info("{}", textContent.getText().getValue());
                } else if (content instanceof MessageImageFileContent imageContent) {
                    String fileId = imageContent.getImageFile().getFileId();
                    logger.info("Image from ID: {}", fileId);
                    logger.debug("{}", content);
                }
            }
        }
    }
    
    private void cleanup() {
        if (agentsClient != null) {
            logger.info("Cleaning up HTTP client resources...");
            try {
                // Clean up threads
                if (threadsClient != null) {
                    PagedIterable<PersistentAgentThread> threads = threadsClient.listThreads();
                    for (PersistentAgentThread thread : threads) {
                        threadsClient.deleteThread(thread.getId());
                        logger.debug("Thread deleted: {}", thread.getId());
                    }
                }

                // Clean up agents
                if (administrationClient != null) {
                    PagedIterable<PersistentAgent> agents = administrationClient.listAgents();
                    for (PersistentAgent agent : agents) {
                        administrationClient.deleteAgent(agent.getId());
                        logger.debug("Agent deleted: {}", agent.getId());
                    }
                }
            } catch (Exception e) {
                logger.warn("Error during cleanup: {}", e.getMessage());
            }
        }
    }
}
