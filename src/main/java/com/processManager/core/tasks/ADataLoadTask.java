package com.processManager.core.tasks;

import com.processManager.common.ProcessManagerException;
import com.processManager.domain.DataLoadDefinition;
import com.processManager.domain.DataLoadExecution;
import com.processManager.domain.DataLoadStatuses;
import com.processManager.repositories.DataLoadDefinitionRepository;
import com.processManager.repositories.DataLoadExecutionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.servlet.http.HttpServletRequest;

public abstract class ADataLoadTask implements IProcessTask {

    @Autowired
    protected DataLoadDefinitionRepository dataLoadDefinitionRepository;

    @Autowired
    protected DataLoadExecutionRepository dataLoadDetailRepository;

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Autowired
    protected RestTemplateBuilder restTemplateBuilder;

    @Autowired
    HttpServletRequest request;

    protected DataLoadExecution dataLoadDetail;

    protected DataLoadDefinition dataLoadDefinition;

    protected static Logger logger = LoggerFactory.getLogger(ADataLoadTask.class);

    @Override
    public void init(Object... args) throws ProcessManagerException {

        if (args == null || args.length == 0) {
            throw new ProcessManagerException("LoadId must be provided during init call");
        }

        int loadId = (int) args[0];
        dataLoadDetail = dataLoadDetailRepository.findOneByLoadId(loadId);
        if (dataLoadDetail == null) {
            throw new ProcessManagerException("Data load detail should be provided");
        } else if (dataLoadDetail.getStatus() == DataLoadStatuses.Finished) {
            throw new ProcessManagerException("Data load was already executed (Finished status)");
        } else if (dataLoadDetail.getStatus() == DataLoadStatuses.InProcess) {
            throw new ProcessManagerException("Data load is currently in process (InProcess status)");
        }
        dataLoadDefinition = dataLoadDefinitionRepository.findOneByDefinitionCode(dataLoadDetail.getDefinitionCode());
        if (dataLoadDefinition == null) {
            throw new ProcessManagerException(String.format("Data load definition %s should exists", dataLoadDetail.getDefinitionCode()));
        }

    }

    private String getTokenFromRequest() {
        String token = request.getHeader("Authorization");
        return token;
    }
}
