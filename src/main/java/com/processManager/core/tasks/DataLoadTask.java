package com.processManager.core.tasks;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.processManager.common.ProcessManagerException;
import com.processManager.domain.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import javax.activation.DataSource;
import javax.activation.URLDataSource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * Class in charge of executing a Data load.
 * 1) Identify the excel file to be processed based on DataLoadExecution
 * 2) USe DataLoadDefinition to understand the way of reading and transforming the information included in the excel file.
 * 3) Save the information in a new temp collection
 * 4) Provide information about errors and process status during the execution
 */
@Component
public class DataLoadTask extends ADataLoadTask {

    private static String TMP_COLLECTION = "tmp_{loadId}";

    /**
     * Start the process for putting a new data-set of performance data-points into CustomerData collection
     *
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws ProcessManagerException
     */
    private int processData() throws Exception {

        String tmpCollectionName = TMP_COLLECTION.replace("{loadId}", "" + dataLoadDetail.getLoadId());
        int recordsFound = 0;
        dataLoadDetail.setStatus(DataLoadStatuses.InProcess);
        dataLoadDetail.setLoadAt(new Date());
        dataLoadDetail.addLogLine("Starting the process to load data");
        dataLoadDetailRepository.save(dataLoadDetail);

        if (dataLoadDefinition.getType() == DataLoadType.FILE_XLS && dataLoadDetail.getDataFileName() != null && !dataLoadDetail.getDataFileName().isEmpty()) {
            // Creating a Workbook from an Excel file (.xls or .xlsx)
            DataSource source = new URLDataSource(new URL(dataLoadDetail.getDataFileName()));
            if (source == null)
                throw new ProcessManagerException("Invalid url (data source)");
            Workbook workbook = WorkbookFactory.create(source.getInputStream());
            if (workbook == null || workbook.getSheetAt(0) == null)
                throw new ProcessManagerException("Source can't be read as excel book");

            Sheet sheetToLoad = workbook.getSheetAt(0);
            Map<String, Integer> header = new HashMap<>();

            if (sheetToLoad != null) {
                try {
                    saveRowsToTempCollection(sheetToLoad, tmpCollectionName, header);
                    dataLoadDetail.setStatus(DataLoadStatuses.Finished);
                } catch (Exception ex) {
                    dataLoadDetail.setStatus(DataLoadStatuses.Error);
                    dataLoadDetail.addLogLine(ex.getMessage());
                }
            } else {
                dataLoadDetail.setStatus(DataLoadStatuses.Error);
                dataLoadDetail.addLogLine("No records to process in the file");
            }
        } else {
            dataLoadDetail.setStatus(DataLoadStatuses.Error);
            dataLoadDetail.addLogLine("dataLoadDefinition type is not valid or without source data to pull. Additional formats and source will be supported in the future.");
        }

        dataLoadDetail.addLogLine("Finishing the process to load data");
        dataLoadDetail.setFinishedAt(new Date());
        dataLoadDetailRepository.save(dataLoadDetail);

        return recordsFound;
    }

    /**
     * read the input file with performance data provided by the client and transform columns' names and data to the
     * required format. The result is saved in a temp collection.
     *
     * @param sheetToLoad
     * @param tmpCollectionName
     * @param header
     * @throws ProcessManagerException
     */
    private void saveRowsToTempCollection(Sheet sheetToLoad, String tmpCollectionName, Map<String, Integer> header) throws ProcessManagerException {
        Iterator<Row> rowIterator = sheetToLoad.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getRowNum() == 0) {
                header = getAndValidateHeaders(row);
                if (mongoTemplate.collectionExists(tmpCollectionName)) {
                    mongoTemplate.dropCollection(tmpCollectionName);
                }
                mongoTemplate.createCollection(tmpCollectionName);
            } else {

                DBObject obj = createCollectionRecord(row, header);
                if (obj != null) {
                    mongoTemplate.insert(obj, tmpCollectionName);
                }

            }
        }
    }

    private BasicDBObject createNewDBObject() {
        BasicDBObject obj = new BasicDBObject();
        obj.put("loadId", dataLoadDetail.getLoadId());
        obj.put("createdAt", new Date());
        obj.put("updatedAt", new Date());
        obj.put("loadType", dataLoadDefinition.getType());

        return obj;
    }

    /**
     * taking a row from an excel file and convert its content into a DBObject data. The process is
     * performed based on the settings configured on dataLoadDefinition collection
     *
     * @param row
     * @param header
     * @return
     */
    private DBObject createCollectionRecord(Row row, Map<String, Integer> header) {
        List<DataLoadColumn> columns = dataLoadDefinition.getColumns();
        BasicDBObject obj = createNewDBObject();

        for (DataLoadColumn column : columns) {

            try {

                String columnName = column.getSource().toLowerCase();
                int index = header.get(columnName).intValue();
                String attributeName = column.getTo();
                Cell cell = row.getCell(index);
                if (cell != null) {
                    String stringCellValue = (cell.getCellType().equals(CellType.NUMERIC) ? String.valueOf(cell.getNumericCellValue()) : cell.getStringCellValue());
                    logger.info("Column to resolve: " + columnName);

                    switch (column.getType()) {

                        case Date:
                            Date date = cell.getDateCellValue();
                            obj.put(attributeName, date);
                            break;

                        case Email:
                            obj.put(attributeName, stringCellValue);
                            break;

                        case Integer:

                            try {
                                if (stringCellValue != null && stringCellValue.indexOf(".") > 1) {
                                    stringCellValue = stringCellValue.substring(0, stringCellValue.indexOf("."));
                                }
                                obj.put(attributeName, resolveIntMapping(column, Integer.valueOf(stringCellValue).intValue()));
                            } catch (Exception ex) {
                                logger.warn("There is not numeric value at (%s,%s)", index, cell.getColumnIndex());
                                obj.put(attributeName, 0);
                            }

                            break;

                        case Phone:

                            String phone = stringCellValue.
                                    replace("(", "").
                                    replace(")", "").
                                    replace(" ", "").
                                    replace(".", "").
                                    replace("/", "").
                                    replace("-", "");
                            obj.put(attributeName, phone);
                            break;

                        case String:

                            Object mappedValue = resolveStringMapping(column, stringCellValue);
                            if (column.getConcatWith() != null && column.getConcatWith().size() > 0) {

                                String concatValue = "" + mappedValue;
                                for (String concatWith : column.getConcatWith()) {
                                    int concatWithIndex = header.get(concatWith.toLowerCase()).intValue();
                                    Cell concatCell = row.getCell(concatWithIndex);
                                    String concatCellValue = concatCell.getCellType().equals(CellType.NUMERIC) ? concatCell.getNumericCellValue() + "" : concatCell.getStringCellValue();
                                    if (concatCellValue != null && !concatCellValue.trim().isEmpty()) {
                                        concatValue += " " + concatCellValue;
                                    }
                                }
                                obj.put(attributeName, concatValue);
                            } else {
                                obj.put(attributeName, mappedValue);
                            }

                            break;

                        default:
                            obj.put(attributeName, resolveStringMapping(column, stringCellValue));
                            break;
                    }
                    resolveGeneratedValues(column, obj.get(attributeName), obj);
                } else {
                    obj.put(attributeName, null);
                }

            } catch (Exception ex) {
                logger.error("Error getting data from column: " + column.getSource().toLowerCase(), ex);
            }

        }
        return obj;
    }

    /**
     * resolve the value of a String cell using the mappings defined
     *
     * @param columnInfo
     * @param value
     * @return
     */
    private Object resolveStringMapping(DataLoadColumn columnInfo, String value) {

        List<DataLoadColumnMapping> mappings = columnInfo.getMappings();
        if (mappings == null || mappings.size() == 0) {
            return value;
        }

        Optional<DataLoadColumnMapping> result = mappings.stream().filter(dataLoadColumnMapping ->
                ((String) dataLoadColumnMapping.getValue()).equalsIgnoreCase(value)).findFirst();
        if (result.isPresent()) {
            return result.get().getMappingValue();
        } else {
            return value;
        }
    }

    /**
     * generate additional attributes based on column's definition
     *
     * @param columnInfo
     * @param value
     * @param obj
     */
    private void resolveGeneratedValues(DataLoadColumn columnInfo, Object value, BasicDBObject obj) {

        List<DataLoadGeneratedColumn> generatedAttributes = columnInfo.getGeneratedAttributes();
        if (generatedAttributes != null) {
            for (DataLoadGeneratedColumn generatedColumn : generatedAttributes) {
                List<DataLoadColumnMapping> mappings = generatedColumn.getMappings();
                if (mappings != null && mappings.size() > 0) {
                    Optional<DataLoadColumnMapping> result = mappings.stream().filter(
                            dataLoadColumnMapping -> dataLoadColumnMapping.getValue().toString().
                                    indexOf(value.toString()) > -1).findFirst();

                    if (result.isPresent()) {
                        obj.put(generatedColumn.getAttribute(), result.get().getMappingValue());
                    }
                }
            }
        }
    }

    /**
     * resolve the value of a integer cell using the mappings defined
     *
     * @param columnInfo
     * @param value
     * @return
     */
    private int resolveIntMapping(DataLoadColumn columnInfo, int value) {

        List<DataLoadColumnMapping> mappings = columnInfo.getMappings();
        if (mappings == null || mappings.size() == 0) {
            return value;
        }

        Optional<DataLoadColumnMapping> result = mappings.stream().filter(dataLoadColumnMapping ->
                ((int) dataLoadColumnMapping.getValue()) == value).findFirst();
        if (result.isPresent()) {
            return (int) result.get().getMappingValue();
        } else {
            return value;
        }
    }

    /**
     * get columns headers from the provided row
     *
     * @param row
     * @return
     */
    private Map<String, Integer> getAndValidateHeaders(Row row) throws ProcessManagerException {
        DataFormatter dataFormatter = new DataFormatter();
        Map<String, Integer> header = new HashMap<>();
        Iterator<Cell> cellIterator = row.cellIterator();

        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            String cellValue = dataFormatter.formatCellValue(cell);
            header.put(cellValue.toLowerCase(), new Integer(cell.getColumnIndex()));
        }

        List<DataLoadColumn> columns = dataLoadDefinition.getColumns();
        int i = 0;
        while (i < columns.size()) {
            String columnName = columns.get(i++).getSource().toLowerCase();
            if (!header.containsKey(columnName)) {
                throw new ProcessManagerException(String.format("Defined column %s is not part of the provided file header", columnName));
            }

        }

        return header;
    }

    @Override
    public Object call() throws Exception {

        return processData();

    }


}
