package org.billing.utility;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.billing.pages.MessageScreen;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


public class ExcelUtil {

    private Workbook wb;
    private Sheet sheet;
    private int headerRowNumber = 0;

    public void setWorkbook(String path, String sheetName, String password) {
        try {
            wb = WorkbookFactory.create(new FileInputStream(new File(path)), CryptoUtils.decryptTheValue(password));
            sheet = wb.getSheet(sheetName);
        } catch (IOException e) {
            MessageScreen.showException("Error occurred while reading the excel file. Path: " + path);
        } catch (InvalidFormatException e) {
            MessageScreen.showException("Error occurred while reading the excel file. File format should be xls or xlsx Path: " + path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setWorkbook(String path, String sheetName) {
        try {
            wb = WorkbookFactory.create(new FileInputStream(path));
            sheet = wb.getSheet(sheetName);
        } catch (IOException e) {
            MessageScreen.showException("Error occurred while reading the excel file. Path: " + path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSheet(String sheetName) {
        sheet = wb.getSheet(sheetName);
        if (sheet == null)
            throw new RuntimeException("Excel sheet '" + sheetName + "' is not found");
    }

    public String getCurrentSheet() {
        if (sheet != null)
            return sheet.getSheetName();
        else
            return null;
    }

    private List<String> getColumnNames(Sheet sheet) {
        final Row row = sheet.getRow(0);
        final List<String> columnValues = new ArrayList<>();
        final int firstCellNum = row.getFirstCellNum();
        final int lastCellNum = row.getLastCellNum();
        for (int i = firstCellNum; i < lastCellNum; i++) {
            final Cell cell = row.getCell(i);
            columnValues.add(getStringCellValue(cell));
        }
        return columnValues;
    }


    public Map<String, String> getRowDataMatchingDataId(String dataID) {
        final List<String> rowData = new ArrayList<>();
        final Map<String, String> rowVal = new LinkedHashMap<>();

        final List<String> columnNames = getColumnNames(sheet);
        final int totalRows = sheet.getLastRowNum();
        final Row headerRow = sheet.getRow(0);

        final int firstCellNum = headerRow.getFirstCellNum();
        final int lastCellNum = headerRow.getLastCellNum();

        for (int i = 0; i < totalRows; i++) {
            final Row row = sheet.getRow(i);
            if (row == null)
                continue;
            final String testLinkID = getStringCellValue(row.getCell(0));
            if (dataID.equalsIgnoreCase(testLinkID)) {
                for (int j = firstCellNum; j < lastCellNum; j++) {
                    final Cell cell = row.getCell(j);
                    rowData.add(getStringCellValue(cell));
                    if (!"".equals(columnNames.get(j)))
                        rowVal.put(columnNames.get(j), rowData.get(j).trim());
                }
                break;
            }
        }
        return rowVal;
    }


    public List<String> getRowDataMatchingHeading(String dataID) {
        final List<String> rowData = new ArrayList<>();

        final int totalRows = sheet.getLastRowNum();

        for (int i = 0; i <= totalRows; i++) {
            final Row row = sheet.getRow(i);
            if (row == null)
                continue;
            final String testLinkID = getStringCellValue(row.getCell(0));
            if (dataID.equalsIgnoreCase(testLinkID)) {
                for (int j = 1; j < row.getLastCellNum(); j++) {
                    final Cell cell = row.getCell(j);
                    rowData.add(getStringCellValue(cell));
                }
                break;
            }
        }
        return rowData;
    }


    public List<Map<String, String>> getAllRows() {
        final List<Map<String, String>> retList = new LinkedList<>();

        Object value;
        final List<String> coulmnNames = getColumnNames(sheet);
        final int totalRows = sheet.getLastRowNum();
        final Row row = sheet.getRow(0);
        final int firstCellNum = row.getFirstCellNum();
        final int lastCellNum = row.getLastCellNum();
//        System.out.println(sheet.getLastRowNum());
        for (int i = 1; i <= totalRows; i++) {
            final Row rows = sheet.getRow(i);
            if (rows != null) {
                final String cell0 = getStringCellValue(rows.getCell(0));
                if ((!"".equalsIgnoreCase(cell0))) {
                    final List<String> rowData = new ArrayList<>();
                    final Map<String, String> rowVal = new LinkedHashMap<>();
                    for (int j = firstCellNum; j < lastCellNum; j++) {
                        final Cell cell = rows.getCell(j);
                        rowData.add(getStringCellValue(cell));
                        rowVal.put(coulmnNames.get(j), rowData.get(j).trim());
                    }
                    retList.add(rowVal);
                }
            }
        }
        return retList;
    }

    private String getStringCellValue(Cell cell) {
        String retVal = null;
        if (cell == null
                || cell.getCellType() == CellType.BLANK) {
            retVal = "";
        } else if (cell.getCellType() == CellType.NUMERIC) {
            final Double val = cell.getNumericCellValue();
            Object value = val.intValue();
            retVal = value.toString();
        } else if (cell.getCellType() == CellType.STRING) {
            retVal = cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.FORMULA) {
            try {
                retVal = cell.getStringCellValue();
            } catch (Exception exception) {
                retVal = String.valueOf(cell.getNumericCellValue());
            }
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            final boolean booleanVal = cell.getBooleanCellValue();
            retVal = String.valueOf(booleanVal);
        } else if (DateUtil.isCellDateFormatted(cell)) {
            retVal = cell.getDateCellValue().toString();
        } else if (cell.getCellType() == CellType.ERROR) {
            throw new RuntimeException(" Cell Type is not supported ");
        }
        return retVal;
    }

    public void setHeaderRowNumber(int headerRowNumber) {
        this.headerRowNumber = headerRowNumber;
    }


    public Workbook getWb() {
        return wb;
    }


}
