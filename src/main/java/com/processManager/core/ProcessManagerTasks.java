package com.processManager.core;

import com.processManager.common.ProcessManagerException;
import com.processManager.core.tasks.DataLoadTask;
import com.processManager.core.tasks.IProcessTask;
import com.processManager.domain.ProcessStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Helper class in charge of managing the execution of specific process manager tasks
 */
@Service
public class ProcessManagerTasks implements IProcessManagerTasks {

    @Autowired
    private ObjectFactory<DataLoadTask> integratePerformanceDataTaskFactory;

    private static Logger logger = LoggerFactory.getLogger(ProcessManagerTasks.class);

    private static final ExecutorService threadpool = Executors.newCachedThreadPool();
    private static HashMap<String, Future<Object>> executors;

    public ProcessManagerTasks() {
        executors = new HashMap<String, Future<Object>>();
    }

    @Override
    public ProcessStatus getProcessStatus(String processId) throws ExecutionException, InterruptedException {

        ProcessStatus processStatus = new ProcessStatus();

        if (executors.containsKey(processId)) {

            Future<Object> future = executors.get(processId);

            if (future.isDone()) {
                processStatus.setStatus(true);
                processStatus.setResult(future.get());

            } else {
                processStatus.setStatus(false);
            }

        } else {
            processStatus.setError(true);
            processStatus.setErrorMessage(String.format("No execution under id: %s", processId));
        }

        return processStatus;
    }

    @Override
    public String loadData(int loadId) throws ProcessManagerException {

        IProcessTask task = integratePerformanceDataTaskFactory.getObject();
        task.init(loadId);
        Future<Object> future = threadpool.submit(task);
        logger.debug(String.format("Task is submitted for processing performance data of loadId=%s", loadId));
        String processId = loadId + "_" + (new Date()).getTime();
        executors.put(processId, future);
        return processId;
    }

    /**
     * method to clean the map of executors
     */
    @Scheduled(fixedDelay = 60000, initialDelay = 60000)
    public void cleanMap() {

        String token = "_";
        int millisecondsOfdifference = 120000;
        Object[] ids = executors.keySet().toArray();

        for (int i = 0; i < ids.length; i++) {
            String id = (String) ids[i];
            if (executors.get(id).isDone()) {
                int pos = id.lastIndexOf(token);
                if (pos > 0) {
                    long timeStampExecutor = new Long(id.substring(pos + 1, id.length())).longValue();
                    long now = new Date().getTime();
                    if ((now - timeStampExecutor) >= millisecondsOfdifference) {
                        executors.remove(id);
                    }

                }
            }

        }

        logger.debug("Cleaning un used executors");

    }
}
