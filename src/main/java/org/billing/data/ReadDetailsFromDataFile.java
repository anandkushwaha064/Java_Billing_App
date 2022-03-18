package org.billing.data;

import org.billing.configuration.EnvironmentConfig;
import org.billing.utility.ExcelUtil;

import java.util.List;
import java.util.Map;

public class ReadDetailsFromDataFile {
    static final ExcelUtil excelUtil = new ExcelUtil();

    public static List<String> read(String details) {
        excelUtil.setWorkbook(EnvironmentConfig.adminDataFilePath, "DashboardDetails", EnvironmentConfig.getAdminFilePassword());
        return excelUtil.getRowDataMatchingHeading(details);
    }

    public static Map<String, String> readShopDetails() {
        excelUtil.setWorkbook(EnvironmentConfig.adminDataFilePath, "ShopDetails", EnvironmentConfig.getAdminFilePassword());
        return excelUtil.getRowDataMatchingDataId("English");
    }
}
