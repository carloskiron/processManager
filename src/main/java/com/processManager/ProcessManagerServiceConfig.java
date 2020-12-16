package com.processManager;

import com.processManager.common.IRequestHelper;
import com.processManager.common.RequestHelper;
import com.processManager.core.IProcessManagerTasks;
import com.processManager.core.ProcessManagerTasks;
import com.processManager.core.tasks.ADataLoadTask;
import com.processManager.core.tasks.DataLoadTask;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

@Configuration
@EnableMongoRepositories("com.processManager")
@ComponentScan("com.processManager")
@EnableAspectJAutoProxy
public class ProcessManagerServiceConfig {

    @Bean
    MongoTransactionManager transactionManager(MongoDbFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Bean
    public VelocityEngine velocityEngineService() {

        VelocityEngine ve = new VelocityEngine();
        ve.init(getVelocityProperties());

        return ve;
    }

    @Bean
    public ADataLoadTask getDataLoadTask() {
        return new DataLoadTask();
    }

    @Bean
    public IProcessManagerTasks getProcessManagerTasks() {
        return new ProcessManagerTasks();
    }

    @Bean
    public IRequestHelper getRequestHelper() {
        return new RequestHelper();
    }

    private Properties getVelocityProperties() {
        Properties properties = new Properties();
        properties.setProperty("resource.loader", "class");
        properties
                .setProperty("class.resource.loader.class",
                        "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        return properties;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(java.time.Duration.ofMinutes(1))
                .setReadTimeout(java.time.Duration.ofMinutes(2))
                .build();
    }


}
