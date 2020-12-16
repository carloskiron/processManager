package com.processManager.test;

import com.amazonaws.services.dynamodbv2.document.Expected;
import com.processManager.ProcessManagerServiceConfig;
import com.processManager.common.ProcessManagerException;
import com.processManager.core.IProcessManagerTasks;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringJUnitConfig(ProcessManagerServiceConfig.class)
@TestPropertySource("/application-unitTest.properties")
@WebAppConfiguration
@SpringBootApplication
public class ProcessManagerTest {

    @Autowired
    IProcessManagerTasks processManagerTasks;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @org.junit.jupiter.api.BeforeEach
    public void setUp() {
        //set up your mock database to emulate database content without depending of a mongodb instance.
        //This is the point to create some DataLoadDefinitions
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() {
    }

    @org.junit.jupiter.api.Test()
    public void testIncorrectDataLoadDefinition() throws ProcessManagerException {
        String str = processManagerTasks.loadData(12);
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
