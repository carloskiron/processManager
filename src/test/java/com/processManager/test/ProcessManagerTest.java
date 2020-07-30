package com.processManager.test;

import com.processManager.ProcessManagerServiceConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringJUnitConfig(ProcessManagerServiceConfig.class)
@TestPropertySource("/application-unitTest.properties")
@WebAppConfiguration
@SpringBootApplication
public class ProcessManagerTest {

    @org.junit.jupiter.api.BeforeEach
    public void setUp() {
        //set up your mock database to emulate database content without depending of a mongodb instance.
        //This is the point to create some DataLoadDefinitios
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() {
    }

    @org.junit.jupiter.api.Test
    public void testIncorrectDataLoadDefinition() {
        //TODO testIncorrectDataLoadDefinition
    }

    @org.junit.jupiter.api.Test
    public void testDataLoadExecutionDoesNotExists() {
        //TODO testDataLoadExecutionDoesNotExists
    }

    @org.junit.jupiter.api.Test
    public void testDataLoadExecutionInProcess() {
        //TODO testDataLoadExecutionInProcess
    }

    @org.junit.jupiter.api.Test
    public void testDataLoadExecutionWithErrors() {
        //TODO testDataLoadExecutionWithErrors
    }

    @org.junit.jupiter.api.Test
    public void testDataLoadExecutionCompletedWithoutErrors() {
        //TODO testDataLoadExecutionCompletedWithoutErrors
    }

    @org.junit.jupiter.api.Test
    public void testDataLoadExecutionFileDoesNotExistsOrIncorrectFormat() {
        //TODO testDataLoadExecutionFileDoesNotExistsOrIncorrectFormat
    }

}
