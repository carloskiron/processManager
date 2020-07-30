package com.processManager.repositories;

import com.processManager.domain.DataLoadExecution;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DataLoadExecutionRepository extends MongoRepository<DataLoadExecution, String> {

    /**
     * get an specific DataLoadDetail
     *
     * @param loadId
     * @return
     */
    DataLoadExecution findOneByLoadId(int loadId);

}
