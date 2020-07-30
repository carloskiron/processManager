package com.processManager.core;

import com.processManager.common.ProcessManagerException;
import com.processManager.domain.ProcessStatus;

import java.util.concurrent.ExecutionException;

public interface IProcessManagerTasks {

    /**
     * Returns the status of a task
     *
     * @param processId
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    ProcessStatus getProcessStatus(String processId) throws ExecutionException, InterruptedException;

    /**
     * Load excel data into a database
     *
     * @param loadId
     * @return
     * @throws ProcessManagerException
     */
    String loadData(int loadId) throws ProcessManagerException;

}
