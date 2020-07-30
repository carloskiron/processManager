package com.processManager.domain;

import java.util.List;

public class DataLoadGeneratedColumn {
    private String attribute;
    private List<DataLoadColumnMapping> mappings;

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public List<DataLoadColumnMapping> getMappings() {
        return mappings;
    }

    public void setMappings(List<DataLoadColumnMapping> mappings) {
        this.mappings = mappings;
    }
}
