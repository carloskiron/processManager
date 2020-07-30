package com.processManager.core.tasks;

import com.processManager.common.ProcessManagerException;

import java.util.concurrent.Callable;

public interface IProcessTask extends Callable<Object> {

    void init(Object... args) throws ProcessManagerException;

    @Override
    Object call() throws Exception;

}
