package com.processManager.controllers;

import com.processManager.common.ProcessManagerException;
import com.processManager.domain.ProcessStatus;

import java.util.concurrent.ExecutionException;

public interface IProcessManagerService {

    /**
     * Execute the data load process identified by loadId
     *
     * @param loadId
     * @return
     * @throws ProcessManagerException
     */
    String loadData(int loadId) throws ProcessManagerException;

    /**
     * Returns the status of the process identified by processId
     *
     * @param processId
     * @return
     * @throws Exception
     * @throws InterruptedException
     * @throws ExecutionException
     */
    ProcessStatus getProcessStatus(String processId) throws Exception, InterruptedException, ExecutionException;

}
