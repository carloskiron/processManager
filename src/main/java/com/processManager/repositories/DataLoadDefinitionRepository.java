package com.processManager.repositories;

import com.processManager.domain.DataLoadDefinition;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DataLoadDefinitionRepository extends MongoRepository<DataLoadDefinition, String> {

    /**
     * get an specific DataLoadDefinition
     *
     * @param definitionCode
     * @return
     */
    DataLoadDefinition findOneByDefinitionCode(int definitionCode);

}
