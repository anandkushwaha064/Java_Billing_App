package org.billing.session;

import org.billing.configuration.EnvironmentConfig;
import org.billing.utility.CryptoUtils;
import org.billing.utility.ExcelUtil;
import org.billing.pages.MessageScreen;

import java.util.Map;

public abstract class Session {
    private static String AUTHKEY = "";

    public static boolean isLoggedIn() {
        if (!AUTHKEY.equals(null))
            return true;
        return false;
    }

    public static void endSession() {
        AUTHKEY = null;
    }

    public static boolean startSession(String userName, String password) {
        ExcelUtil excelUtil = new ExcelUtil();
        try {
            excelUtil.setWorkbook(EnvironmentConfig.adminDataFilePath, "UserCredential", EnvironmentConfig.getAdminFilePassword());
            Map<String, String> userMap = excelUtil.getRowDataMatchingDataId(userName);
            if (userMap.size() > 0 && CryptoUtils.encryptTheValue(password).equals(userMap.get("password"))) {
                AUTHKEY = CryptoUtils.encryptTheValue(userName + ":" + password);
                return true;
            }
        } catch (Exception e) {
            MessageScreen.showException("Exception Occurred while encrypting the password\nError : " + e.getLocalizedMessage());
        }
        return false;
    }
}


