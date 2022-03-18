package org.billing.configuration;

import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.billing.data.ReadDetailsFromDataFile;
import org.billing.pages.MessageScreen;
import org.billing.session.Session;
import org.billing.utility.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnvironmentConfig {

    private static Stage stage;
    // Directory Path
    public static final String projectDirPath = System.getProperty("user.dir");

    public static String adminDataFilePath;
    public static String databaseFileBackup;
    public static String logoImagePath;
    public static String logoImagePathForSlip;
    public static final String encryptionKey = "mJPb%rJe-Q8PCsG_#6bCDasdfk5esasese$xqHx3bVDhR!PHes3";

    public static String homeDirectoryPath = "";

    // Pages Path
    public static final String resources = "org/billing/pages/";
    public static String dataBaseFolderPath;
    public static String dataBaseFilePath;

    // Project Information
    private static String projectTitle = "Laxmi Enterprises Billing";
    private static String projectIconPath;
    private static List<String> quantityTypes = new ArrayList<>();

    public static String shopName = null;
    public static String shopAddress = null;
    public static String mobileNumber = null;
    private static String username = "Laxmi";
    private static String password = "";
    private static String dataFilePassword = "";
    private static final String adminFilePassword = "kMDsvX3ET/Frp+FF3mzjOA==";


    public static String getAdminFilePassword() {
        return adminFilePassword;
    }
    // Need to be remove

    public static String dataId = null;

    public static String getDataId() {
        return dataId;
    }

    public static void setDataId(String dd) {
        dataId = dd;
    }

    public static final String systemDateFormat = "dd-MMM-yyyy";
    public static final String systemTimeFormat = "HH:mm:ss";

    public static String getFxmlFilePath(String pageName) {
        return resources + pageName;
    }

    public void setDefaultPathsAndInfo() throws IOException {
        setHomeDirectoryPath();
        dataBaseFolderPath = homeDirectoryPath + "LaxmiEnterprisesData";
        dataBaseFilePath = dataBaseFolderPath + "/Data.xlsx";
        File file = new File(dataBaseFolderPath);
        if (!file.exists())
            file.mkdir();

        adminDataFilePath = getResourcesPath("DataFiles/LaxmiEnterpriseData.xlsx");
        databaseFileBackup = getResourcesPath("DataFiles/DataBackUp.xlsx");

        logoImagePath = getResourcesPath("images/Logo.jpeg");
        logoImagePathForSlip = getResourcesPath("images/LogoForSlip.jpg");
        projectIconPath = getResourcesPath("images/icon.png");

        EnvironmentConfig.setBillingPDFInfo();
        EnvironmentConfig.setQuantityTypes();
    }


    public String getResourcesPath(String name) {
        URL url = getClass().getClassLoader().getResource(name);
        try {
            return url.toURI().getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDataFilePassword() {
        if (Session.isLoggedIn())
            return dataFilePassword;
        return null;
    }

    public static Stage getStage() {
        return stage;
    }

    public static String setHomeDirectoryPath() {
        homeDirectoryPath = System.getProperty("user.home").replace("\\", "/") + "/";
        return homeDirectoryPath;
    }

    public static void setStage(Stage stage) {
        EnvironmentConfig.stage = stage;
    }

    public static void setBillingPDFInfo() {
        Map<String, String> details = ReadDetailsFromDataFile.readShopDetails();
        shopName = details.get("shopName");
        projectTitle = details.get("shopName");
        shopAddress = details.get("shopAddress");
        mobileNumber = details.get("mobileNumber1") + ", " + details.get("mobileNumber2");
    }


    public static void configureProject(Stage stage) {
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.initStyle(StageStyle.DECORATED);
        setStage(stage);
        setProjectIconPath();
        setProjectTitle();
    }

    public static void setQuantityTypes() {
        quantityTypes = ReadDetailsFromDataFile.read("QuantityTypes");
    }

    public static List<String> getQuantityTypes() {
        return quantityTypes;
    }

    public static void configureDashboard() {
        stage.resizableProperty().setValue(Boolean.TRUE);
        stage.setMaximized(true);
    }

    private static void setProjectTitle() {
        if (CommonUtils.isValidString(EnvironmentConfig.projectTitle))
            EnvironmentConfig.stage.setTitle(EnvironmentConfig.projectTitle);
        else
            MessageScreen.showAlertMessage("Path provided to set Project Icon is not valid : " + projectIconPath);
    }

    private static void setProjectIconPath() {
        if (CommonUtils.isValidString(projectIconPath)) {
            File file = new File(projectIconPath);
            if (file.exists()) {
//                stage.getIcons().add(new Image(""));
            } else {
                MessageScreen.showAlertMessage("Icon is not available in Location : " + projectIconPath);
            }
        } else
            MessageScreen.showAlertMessage("Path provided to set Project Icon is not valid : " + projectIconPath);
    }

    private static void setScreenHeightAndWidth(Stage stage) {
        stage.setHeight(SystemUtils.screenHeight);
        stage.setWidth(SystemUtils.screenWidth);
    }

}
