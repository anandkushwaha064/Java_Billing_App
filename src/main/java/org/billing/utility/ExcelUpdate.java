package org.billing.utility;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.aspectj.util.FileUtil;
import org.billing.configuration.EnvironmentConfig;
import org.billing.pages.MessageScreen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelUpdate {

    private static Workbook workbook;
    private static Sheet sheet;
    private static FileInputStream inputStream;
    private static final String dataFile = EnvironmentConfig.databaseFileBackup;

    public static void createWorkBook() {

        try {
            File file = new File(EnvironmentConfig.dataBaseFilePath);
            File dataBackupFile = new File(dataFile);

            if (file.getTotalSpace() == 0) {
                file.delete();
                if (dataBackupFile.exists())
                    FileUtil.copyFile(dataBackupFile, file);
            } else if (!file.exists()) {
                if (dataBackupFile.exists())
                    FileUtil.copyFile(dataBackupFile, file);
            }

            inputStream = new FileInputStream(file);
            workbook = WorkbookFactory.create(inputStream);
            String dataSheet = "Database";
            sheet = workbook.getSheet(dataSheet);
            if (sheet.getLastRowNum() <= 0) {
                Row row = sheet.createRow(0);
                CellStyle style = workbook.createCellStyle();
                style.setFillBackgroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
                row.setRowStyle(style);
                row.createCell(0).setCellValue("Se. no.");
                row.createCell(1).setCellValue("Customer Name");
                row.createCell(2).setCellValue("DateTime");
                row.createCell(3).setCellValue("Products");
                row.createCell(4).setCellValue("Scraps");
                row.createCell(5).setCellValue("Total Cost of Products");
                row.createCell(6).setCellValue("Total Cost of Scraps");
                row.createCell(7).setCellValue("Customer Deposit Amount");
                row.createCell(8).setCellValue("Old Credit");
                row.createCell(9).setCellValue("Discount");
                row.createCell(10).setCellValue("MobileNumber");
                row.createCell(11).setCellValue("Address");
                row.createCell(12).setCellValue("SubTotal");
                row.createCell(13).setCellValue("Paid");
            }

        } catch (Exception exception) {
            MessageScreen.showException(exception.getLocalizedMessage());
            exception.printStackTrace();
        }
    }


    public static boolean saveWorkBook() {
        boolean isSaved = false;
        File outputFile = new File(EnvironmentConfig.dataBaseFilePath);
        try {
            inputStream.close();
            if (outputFile.exists())
                outputFile.delete();
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
            isSaved = true;
        } catch (IOException | EncryptedDocumentException ex) {
            MessageScreen.showException("Please close file : '" + outputFile.getName() + "'");
        }
        return isSaved;
    }

    public static Row createNewRow() {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        Cell cell = row.createCell(0);
        cell.setCellValue(sheet.getLastRowNum());
        return row;
    }


    public static Row createNewRow(int rowId) {
        Row row = sheet.createRow(rowId);
        Cell cell = row.createCell(0);
        cell.setCellValue(sheet.getLastRowNum());
        return row;
    }

    public static int getRowNumberMatchingDataId(String dataId) {
        int totalRows = sheet.getLastRowNum();
        Row row;
        for (int i = 1; i <= totalRows; i++) {
            row = sheet.getRow(i);
            if (row != null) {
                if (getStringCellValue(row.getCell(0)).equalsIgnoreCase(dataId)) {
                    sheet.removeRow(row);
                    return i;
                }
            } else {
                MessageScreen.showException("While updating Row : Row is null for Row number : " + i);
            }
        }
        MessageScreen.showMessage("Data ID : " + dataId + " Not found");
        return -1;
    }

    public static boolean deleteRowMatchingByDataId(String dataId) {
        int totalRows = sheet.getLastRowNum();
        Row row;
        for (int i = 1; i <= totalRows; i++) {
            row = sheet.getRow(i);
            if (row != null) {
                if (getStringCellValue(row.getCell(0)).equalsIgnoreCase(dataId)) {
                    Cell cell = row.getCell(0);
                    cell.setCellValue("");
                    return true;
                }
            } else {
                MessageScreen.showException("While deleting Row : Row is null for Row number : " + i);
            }
        }
        MessageScreen.showMessage("Data ID : " + dataId + " Not found");
        return false;
    }

    private static String getStringCellValue(Cell cell) {
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
}
