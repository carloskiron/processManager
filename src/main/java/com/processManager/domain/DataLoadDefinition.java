package com.processManager.domain;

import java.util.List;

public class DataLoadDefinition extends BaseObject {

    private int definitionCode;
    private DataLoadType type;
    private Object[] locationsFilter;

    private List<DataLoadColumn> columns;

    public int getDefinitionCode() {
        return definitionCode;
    }

    public Object[] getLocationsFilter() {
        return locationsFilter;
    }

    public void setLocationsFilter(Object[] locationsFilter) {
        this.locationsFilter = locationsFilter;
    }

    public void setDefinitionCode(int definitionCode) {
        this.definitionCode = definitionCode;
    }

    public List<DataLoadColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<DataLoadColumn> columns) {
        this.columns = columns;
    }

    public DataLoadType getType() {
        return type;
    }

    public void setType(DataLoadType type) {
        this.type = type;
    }
}
