package com.processManager.domain;

import java.util.ArrayList;
import java.util.List;

public class DataLoadColumn {
    private String jsonSourceExp;
    private String source;
    private String to;
    private List<String> concatWith = new ArrayList<>();
    private ColumnTypes type;
    private Object fixedValue;
    private List<DataLoadColumnMapping> mappings;
    private List<DataLoadGeneratedColumn> generatedAttributes;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<DataLoadGeneratedColumn> getGeneratedAttributes() {
        return generatedAttributes;
    }

    public void setGeneratedAttributes(List<DataLoadGeneratedColumn> generatedAttributes) {
        this.generatedAttributes = generatedAttributes;
    }

    public Object getFixedValue() {
        return fixedValue;
    }

    public void setFixedValue(Object fixedValue) {
        this.fixedValue = fixedValue;
    }

    public List<String> getConcatWith() {
        return concatWith;
    }

    public void setConcatWith(List<String> concatWith) {
        this.concatWith = concatWith;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public ColumnTypes getType() {
        return type;
    }

    public void setType(ColumnTypes type) {
        this.type = type;
    }

    public List<DataLoadColumnMapping> getMappings() {
        return mappings;
    }

    public void setMappings(List<DataLoadColumnMapping> mappings) {
        this.mappings = mappings;
    }

    public String getJsonSourceExp() {
        return jsonSourceExp;
    }

    public void setJsonSourceExp(String jsonSourceExp) {
        this.jsonSourceExp = jsonSourceExp;
    }
}
