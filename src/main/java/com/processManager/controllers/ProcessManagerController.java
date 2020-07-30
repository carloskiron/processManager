package com.processManager.controllers;

import com.processManager.common.ProcessManagerException;
import com.processManager.core.IProcessManagerTasks;
import com.processManager.domain.ProcessStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
public class ProcessManagerController implements IProcessManagerService {

    private static Logger logger = LoggerFactory.getLogger(ProcessManagerController.class);

    @Autowired
    private IProcessManagerTasks performanceTasks;

    @Override
    @RequestMapping(value = "${mapping.prefix}/loadData/{loadId}", method = RequestMethod.PUT)
    public String loadData(@PathVariable("loadId") int loadId) throws ProcessManagerException {
        return performanceTasks.loadData(loadId);
    }

    @Override
    @RequestMapping("${mapping.prefix}/getProcessStatus")
    public ProcessStatus getProcessStatus(@RequestParam(value = "processId", defaultValue = "") String processId)
            throws Exception, InterruptedException, ExecutionException {
        return performanceTasks.getProcessStatus(processId);
    }

    @RequestMapping("${mapping.prefix}/live")
    public boolean check() {
        return true;
    }
}
