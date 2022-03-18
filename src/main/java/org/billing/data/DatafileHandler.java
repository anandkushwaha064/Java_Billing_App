package org.billing.data;

import org.apache.poi.ss.usermodel.*;
import org.billing.configuration.EnvironmentConfig;
import org.billing.pagepojo.TransactionsPojo;
import org.billing.utility.ExcelUtil;
import org.billing.pages.MessageScreen;
import org.billing.session.Session;
import org.billing.utility.ExcelUpdate;

public class DatafileHandler {
    public static boolean updateRow(TransactionsPojo transactionsPojo, String dataId) {
        boolean rowUpdated;
        ExcelUpdate excelUpdate = new ExcelUpdate();
        excelUpdate.createWorkBook();

        int rowNum = excelUpdate.getRowNumberMatchingDataId(dataId);
        if (rowNum != -1) {
            try {
                Row row = excelUpdate.createNewRow(rowNum);
                row.createCell(1).setCellValue(transactionsPojo.getCustomerName());
                row.createCell(2).setCellValue(transactionsPojo.getTransactionDateTime());
                row.createCell(3).setCellValue(transactionsPojo.getProducts());
                row.createCell(4).setCellValue(transactionsPojo.getScraps());
                row.createCell(5).setCellValue(transactionsPojo.getTotalOfProduct().toString());
                row.createCell(6).setCellValue(transactionsPojo.getTotalOfScrap().toString());
                row.createCell(7).setCellValue(transactionsPojo.getCustomerAmount().toString());
                row.createCell(8).setCellValue(transactionsPojo.getOldCredit().toString());
                row.createCell(9).setCellValue(transactionsPojo.getDiscount().toString());
                row.createCell(10).setCellValue(transactionsPojo.getMobileNumber());
                row.createCell(11).setCellValue(transactionsPojo.getAddress());
                row.createCell(12).setCellValue(transactionsPojo.getSubTotal().toString());
                row.createCell(13).setCellValue(transactionsPojo.getPaid());
                rowUpdated = true;
            } catch (Exception exception) {
                rowUpdated = false;
                MessageScreen.showException("Exception Occurred while creating new Row in the DataSheet");
            }
        } else {
            rowUpdated = false;
            MessageScreen.showMessage(dataId + " : Not find");
        }
        return rowUpdated && excelUpdate.saveWorkBook();
    }

    public static boolean insertRow(TransactionsPojo transactionsPojo) {
        boolean rowUpdated;
        ExcelUpdate excelUpdate = new ExcelUpdate();
        excelUpdate.createWorkBook();
        try {
            Row row = excelUpdate.createNewRow();
            row.createCell(1).setCellValue(transactionsPojo.getCustomerName());
            row.createCell(2).setCellValue(transactionsPojo.getTransactionDateTime());
            row.createCell(3).setCellValue(transactionsPojo.getProducts());
            row.createCell(4).setCellValue(transactionsPojo.getScraps());
            row.createCell(5).setCellValue(transactionsPojo.getTotalOfProduct().toString());
            row.createCell(6).setCellValue(transactionsPojo.getTotalOfScrap().toString());
            row.createCell(7).setCellValue(transactionsPojo.getCustomerAmount().toString());
            row.createCell(8).setCellValue(transactionsPojo.getOldCredit().toString());
            row.createCell(9).setCellValue(transactionsPojo.getDiscount().toString());
            row.createCell(10).setCellValue(transactionsPojo.getMobileNumber());
            row.createCell(11).setCellValue(transactionsPojo.getAddress());
            row.createCell(12).setCellValue(transactionsPojo.getSubTotal().toString());
            row.createCell(13).setCellValue(transactionsPojo.getPaid());
            rowUpdated = true;
        } catch (Exception exception) {
            rowUpdated = false;
            MessageScreen.showException("Exception Occurred while creating new Row in the DataSheet");
        }
        return rowUpdated && excelUpdate.saveWorkBook();
    }

    public static ExcelUtil getDataFileAccess() {
        if (Session.isLoggedIn()) {
            ExcelUtil excelUtil = new ExcelUtil();
            excelUtil.setWorkbook(EnvironmentConfig.dataBaseFilePath, "Database");
            return excelUtil;
        } else {
            System.exit(0);
            return null;
        }
    }

    public static boolean deleteBill(String dataId) {

//    public static void main(String[] args) {
        boolean rowUpdated = false;
        ExcelUpdate excelUpdate = new ExcelUpdate();
        excelUpdate.createWorkBook();
        try {
            rowUpdated = excelUpdate.deleteRowMatchingByDataId(dataId);
        } catch (Exception exception) {
            rowUpdated = false;
            MessageScreen.showException("Exception Occurred while creating new Row in the DataSheet");
        }
        return rowUpdated && excelUpdate.saveWorkBook();
    }


}
