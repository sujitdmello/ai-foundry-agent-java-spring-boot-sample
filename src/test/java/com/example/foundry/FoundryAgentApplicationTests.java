package com.example.foundry;

import com.example.foundry.config.AgentConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the Azure AI Foundry Agent application.
 */
@SpringBootTest
@TestPropertySource(properties = {
    "agent.project-endpoint=https://test.example.com/api/projects/test",
    "agent.model-deployment-name=test-model",
    "agent.agent-name=TestAgent",
    "agent.skip-execution=true"
})
class FoundryAgentApplicationTests {

    @Test
    void contextLoads() {
        // This test verifies that the Spring application context loads successfully
        assertTrue(true, "Application context should load without errors");
    }
    
    @Test
    void configurationTest() {
        AgentConfiguration config = new AgentConfiguration();
        config.setAgentName("TestAgent");
        config.setModelDeploymentName("gpt-4o");
        config.setHttpLoggingEnabled(true);
        
        assertEquals("TestAgent", config.getAgentName());
        assertEquals("gpt-4o", config.getModelDeploymentName());
        assertTrue(config.isHttpLoggingEnabled());
    }
}
