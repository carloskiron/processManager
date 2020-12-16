# Process Manager

The process manager is a REST service able to execute asynchronous operations. These operations are built through the implementation of **IProcessTask** interface. IProcessTask is a callable object so it can be executed as an independent thread using Java thread pools.

The main motivation for having this component is to have a standarized framework to implement process-oriented tasks that can take seconds, minutes, or hours to finish. 

The diagrams below illustrate the concepts involved and the high level architecture of the service:

**Concepts**

![Concepts](https://github.com/carloskiron/processManager/blob/master/diagrams/Process%20Manager.PNG)

**HL Architecture**

![HL Architecture](https://github.com/carloskiron/processManager/blob/master/diagrams/Architecture.PNG)

**Architecture decisions**
* Scalability through asynchronous operations and thread pools to isolate executions.
* Docker container to be platform-agnostic and to provide vertical scalability.
* Authentication based on AWS Cognito. Ready to be deployed through an API gateway.
* IoC to avoid direct dependencies between internal components/classes.
* Repositories to perform data operations.
* Spring Boot with embedded tomcat to be deployed as Micro Service.
* Logging capability through slf4j.

**Use case: Data Loader**

*Context:* This is a process able to load data points saved in excel files into mongodb collections. Files can be hosted on Amazon S3. The process should load files based on specific definitions, rules, validations, and required data transformations. These definitions are configured through DataLoadDefinition objects.

![Data Loader](https://github.com/carloskiron/processManager/blob/master/diagrams/Data%20Loader.PNG)

**DataLoadDefinition:** Provides a way of defining the structure of a specific file and the rules to be applied during the process of loading data. 

*Example below:*

```
{ 
    "definitionCode" : NumberInt(22), 
    "columns" : [
        {
            "source" : "Local Employee ID", 
            "to" : "fileNumber", 
            "type" : "Integer"
        }, 
        {
            "source" : "Location", 
            "to" : "companyLocationCode", 
            "type" : "String", 
            "mappings" : [
                {
                    "value" : "ORD2", 
                    "mappingValue" : NumberInt(231)
                }
            ]
        }, 
        {
            "source" : "Job Code (Label)", 
            "to" : "jobPositionDescription", 
            "type" : "String", 
            "generatedAttributes" : [
                {
                    "attribute" : "jobPositionCode", 
                    "mappings" : [
                        {
                            "value" : "201 - Utility Worker", 
                            "mappingValue" : NumberInt(22)
                        }, 
                        {
                            "value" : "245 - Food Service Employee", 
                            "mappingValue" : NumberInt(223)
                        }, 
                        {
                            "value" : "252 - Airline Bev And Equip Assemble", 
                            "mappingValue" : NumberInt(222)
                        }
                    ]
                }
            ]
        }, 
        {
            "source" : "First Name", 
            "to" : "payrollName", 
            "type" : "String", 
            "concatWith" : [
                "Middle Name", 
                "Last Name"
            ]
        }, 
        {
            "source" : "Hire/Rehire Date", 
            "to" : "hireDate", 
            "type" : "Date"
        }, 
        {
            "source" : "Termination Date", 
            "to" : "terminationDate", 
            "type" : "Date"
        }, 
        {
            "source" : "Sub Event", 
            "to" : "reasonCode", 
            "type" : "String"
        }, 
        {
            "source" : "Gender", 
            "to" : "gender", 
            "type" : "String"
        }
    ], 
    "type" : "FILE_XLS"
}

```
**DataLoadExecution:** its main goal is to provide details about the load to be performed. Information like the source file and the load definition to apply are defined through this entity. A data load execution with status “Created” represents a load that is ready to be executed. DataLoadExecution is updated during the process to keep up to date information in terms of status and logging. 

*Example below:*
```
{ 
    "loadId" : NumberInt(130), 
    "definitionCode" : NumberInt(22), 
    "dataFileName" : "https://xxxxxxxxxx.s3-us-west-2.amazonaws.com/22/data.xls", 
    "collection" : "customerData", 
    "loadLog" : "", 
    "status" : "Finished", 
    "userEmail" : "cmonvel@xxxxx.com", 
    "loadAt" : ISODate("2020-03-20T03:11:05.064+0000"), 
    "finishedAt" : ISODate("2020-03-20T03:11:24.993+0000"), 
}
```

**Execution**

To start a new data load:
* PUT CALL to http://HOST/loadData/LOAD_ID/
  * LOAD_ID is the unique identifier of the DataLoadExecution object to use.
  * A new processId is returned. It can be used to know the status of the process.

To know the status of a current execution:
* GET CALL to http://HOST/getProcessStatus?processId=XXXXXX


**Stack**

* Spring Boot (https://spring.io/projects/spring-boot)
* Java
* Maven
* MongoDB
* Docker
* AWS ECS
* Amazon Cognito

**Global Settings**

Database connection and other settings can be set up in the following .properties:

* resources/application-default.properties (development) 
* resources/application-sand.properties (Sandbox)
* resources/application-prod.properties  (Production)
* resources/application-unitTest.properties (Test cases). 

It's highly recommended to use ENV variables to set up sensitive information like
connection strings and API keys.  

**Security**

Authentication was implemented using Amazon Cognito. It should be configured using the settings below: 

* AUTH_SERVICE_URL=https://HOST.auth.us-east-1.amazoncognito.com
* CLIENT_ID=XXXXXXXXX
* CLIENT_SECRET=YYYYYYYYY

**Unit tests**

src/test/java/com/processManager/test

No unit tests have been completed yet. You will see a list of possible tests to fully confirm the readiness of the data-loader example provided here.

**Deployment**

The service can be deployed on any container-based service with Docker support. A **Docker file** and **build spec file** are included as part of this distribution to easily deploy it on AWS ECS through CodeDeploy or Code Pipeline

* AWS ECS https://aws.amazon.com/ecs/

MongoDB database can be set up using MongoDB Atlas:
https://www.mongodb.com/cloud/atlas

**Further Development and Improvements**
* DataLoader able to process additional file formats and data sources.
* Full unit test coverage. Including mongodb in memory mockup.
* Additional column data types and transformations.
* UI for defining DataLoad definitions and monitoring process executions.
