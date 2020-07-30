package com.processManager.domain;

import java.util.Date;

public class DataLoadExecution extends BaseObject {

    private int loadId;
    private int definitionCode;
    private String dataFileName;
    private String collection;
    private String loadLog = "";
    private DataLoadStatuses status;
    private String userEmail;
    private Date loadAt;
    private Date finishedAt;

    public int getLoadId() {
        return loadId;
    }

    public void setLoadId(int loadId) {
        this.loadId = loadId;
    }

    public int getDefinitionCode() {
        return definitionCode;
    }

    public void setDefinitionCode(int definitionCode) {
        this.definitionCode = definitionCode;
    }

    public String getDataFileName() {
        return dataFileName;
    }

    public void setDataFileName(String dataFileName) {
        this.dataFileName = dataFileName;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getLoadLog() {
        return loadLog;
    }

    public void setLoadLog(String loadLog) {
        this.loadLog = loadLog;
    }

    public DataLoadStatuses getStatus() {
        return status;
    }

    public void setStatus(DataLoadStatuses status) {
        this.status = status;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Date getLoadAt() {
        return loadAt;
    }

    public void setLoadAt(Date loadAt) {
        this.loadAt = loadAt;
    }

    public Date getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Date finishedAt) {
        this.finishedAt = finishedAt;
    }

    public void addLogLine(String logLine) {
        loadLog += new Date() + ": " + logLine + " ";
    }

}
