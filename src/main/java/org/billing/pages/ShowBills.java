package org.billing.pages;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.billing.configuration.EnvironmentConfig;
import org.billing.data.DatafileHandler;
import org.billing.pagepojo.BillPojo;
import org.billing.pagepojo.TransactionsPojo;
import org.billing.pdf.CreateSlipPDF;
import org.billing.session.Session;
import org.billing.utility.*;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

public class ShowBills extends Baseclass implements Initializable {

    @FXML
    private Button showBills;

    @FXML
    private Button Dashboard;

    @FXML
    private Button close;

    @FXML
    private TextField customerNameTextField;

    @FXML
    private DatePicker transactionStart;

    @FXML
    private DatePicker transactionEnd;

    @FXML
    private Button searchButton;

    @FXML
    private TableView<BillPojo> billsTable;

    @FXML
    private TableColumn<BillPojo, String> SeNo;

    @FXML
    private TableColumn<BillPojo, String> CustomerName;

    @FXML
    private TableColumn<BillPojo, String> Date;

    @FXML
    private TableColumn<BillPojo, String> transactionTime;

    @FXML
    private TableColumn<BillPojo, String> productCost;

    @FXML
    private TableColumn<BillPojo, String> scrapCost;

    @FXML
    private TableColumn<BillPojo, String> totalCost;

    @FXML
    private TableColumn<BillPojo, String> customerDeposit;

    @FXML
    private TableColumn<BillPojo, String> oldAmount;

    @FXML
    private TableColumn<BillPojo, String> mobileNumber;

    @FXML
    private TableColumn<BillPojo, String> paid;

    @FXML
    private TableColumn<BillPojo, String> discount;

    @FXML
    private TableColumn<BillPojo, Button> print;

    @FXML
    private TableColumn<BillPojo, Button> delete;

    @FXML
    private Label SearchSummary;

    private final DecimalFormat decimalFormat = new DecimalFormat();

    public void loadShowBills() {
        Stage stage = EnvironmentConfig.getStage();
        if (!Session.isLoggedIn()) {
            System.exit(0);
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(EnvironmentConfig.getFxmlFilePath("ShowBills.fxml")));
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setMaximized(false);
            stage.setMaximized(true);
            EnvironmentConfig.configureDashboard();
        } catch (Exception exception) {
            exception.printStackTrace();
            MessageScreen.showException("Error Occurred While loading ShowBills Page, Make sure Dashboard page Exist in repository : \n" + exception.getMessage());
            stage.close();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Menus(showBills, Dashboard, close).setAction();
        SystemUtils.convertDatePicker(transactionStart);
        SystemUtils.convertDatePicker(transactionEnd);
        billTable();
        filterBills();

        searchButton.setOnMouseClicked(event -> filterBills());

        customerNameTextField.setOnKeyReleased(event -> filterBills());

        transactionStart.setOnMouseExited(event -> filterBills());
        transactionEnd.setOnMouseExited(event -> filterBills());
    }

    public void billTable() {
        SeNo.setCellValueFactory(new PropertyValueFactory<>("SeNo"));
        CustomerName.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
        Date.setCellValueFactory(new PropertyValueFactory<>("Date"));
        transactionTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        productCost.setCellValueFactory(new PropertyValueFactory<>("productCost"));
        scrapCost.setCellValueFactory(new PropertyValueFactory<>("scrapCost"));
        totalCost.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        customerDeposit.setCellValueFactory(new PropertyValueFactory<>("gotAmount"));
        paid.setCellValueFactory(new PropertyValueFactory<>("paid"));
        discount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        oldAmount.setCellValueFactory(new PropertyValueFactory<>("oldAmount"));
        mobileNumber.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));
        print.setCellValueFactory(new PropertyValueFactory<>("printButton"));
        delete.setCellValueFactory(new PropertyValueFactory<>("delete"));
    }

    public void filterBills() {
        decimalFormat.setMaximumFractionDigits(2);
        ObservableList<BillPojo> billsPojo = FXCollections.observableArrayList();
        try {

            ExcelUtil datafile = DatafileHandler.getDataFileAccess();
            List<Map<String, String>> billFromExcel;
            billFromExcel = datafile.getAllRows();

            int count = 0;
            Double totalCostOfProduct = 0.0;
            Double totalCostOfScrap = 0.0;
            Double totalCostOfOldCredit = 0.0;
            Double Discount = 0.0;
            Double Deposit = 0.0;
            Double total = 0.0;

            assert billFromExcel != null;
            for (Map<String, String> billData : billFromExcel) {

                if (filter(billData)) {
                    totalCostOfProduct += Double.valueOf(billData.get("Total Cost of Products"));
                    totalCostOfScrap += Double.valueOf(billData.get("Total Cost of Scraps"));
                    totalCostOfOldCredit += Double.valueOf(billData.get("Old Credit"));
                    Discount += Double.valueOf(billData.get("Discount"));
                    Deposit += Double.valueOf(billData.get("Customer Deposit Amount"));
                    if (billData.get("Paid").equals("No"))
                        total += Double.valueOf(billData.get("Customer Deposit Amount"));
                    BillPojo billPojo = getBillPojo(billData, ++count);
                    billsPojo.add(billPojo);
                }
            }

//            Double ExactTotal = (totalCostOfProduct + totalCostOfOldCredit - Discount) - (totalCostOfScrap + Deposit) + total;

            String summary = "[ " + decimalFormat.format(totalCostOfProduct) + " + "
                    + decimalFormat.format(totalCostOfOldCredit) + " + "
                    + decimalFormat.format(Discount) + " ] - [ "
                    + decimalFormat.format(totalCostOfScrap) + " + "
                    + decimalFormat.format(Deposit) + " ] ";
///**/                    + decimalFormat.format(total) + " = "
//                    + decimalFormat.format(ExactTotal);


            // End of the List Table
            SearchSummary.setText(summary);


        } catch (Exception e) {
            MessageScreen.showMessage("Do not have any Bills");
        }
        billsTable.setItems(billsPojo);
    }


    public boolean filter(Map<String, String> billData) {
        String customerName = customerNameTextField.getText();
        LocalDate startDate = transactionStart.getValue();
        LocalDate endDate = transactionEnd.getValue();

        LocalDate localDate = SystemUtils.getLocalDateFromString(SystemUtils.getDateTime(billData.get("DateTime"))[0]);

        boolean shouldAdd = true;

        if (CommonUtils.isValidString(customerName) && !billData.get("Customer Name").toLowerCase().contains(customerName.toLowerCase()))
            shouldAdd = false;

        if (startDate != null && endDate != null) {
            boolean startDateIsBefore = startDate.isBefore(localDate);
            boolean startDateIsEqual = startDate.isEqual(localDate);
            boolean endDateIsAfter = endDate.isAfter(localDate);
            boolean endDateIsEqual = endDate.isEqual(localDate);

            if (!((startDateIsBefore || startDateIsEqual) && (endDateIsAfter || endDateIsEqual)))
                shouldAdd = false;

        } else if (startDate != null && endDate == null) {
            boolean startDateIsBefore = startDate.isBefore(localDate);
            boolean startDateIsEqual = startDate.isEqual(localDate);
            if (!(startDateIsBefore || startDateIsEqual))
                shouldAdd = false;

        } else if (startDate == null && endDate != null) {
            boolean endDateIsAfter = endDate.isAfter(localDate);
            boolean endDateIsEqual = endDate.isEqual(localDate);
            if (!(endDateIsAfter || endDateIsEqual))
                shouldAdd = false;
        }

        return shouldAdd;
    }

    public BillPojo getBillPojo(Map<String, String> billData, int count) {

        Button goButton = new Button("Go");

        Button printButton = new Button("Print");
        printButton.setOnMouseClicked(event -> {
            createPrintablePdf(billData);
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnMouseClicked(event -> {
            if (DatafileHandler.deleteBill(billData.get("Se. no.")))
                filterBills();
        });

        String datetime = billData.get("DateTime");
        String[] date0time1 = SystemUtils.getDateTime(billData.get("DateTime"));
        date0time1[1] = date0time1[1].trim();

        return new BillPojo(String.valueOf(count), billData.get("Customer Name"), date0time1[0], date0time1[1], decimalFormat.format(CommonUtils.convertToNumeric(billData.get("Total Cost of Products"))), decimalFormat.format(CommonUtils.convertToNumeric(billData.get("Total Cost of Scraps"))), decimalFormat.format(CommonUtils.convertToNumeric(billData.get("SubTotal"))), decimalFormat.format(CommonUtils.convertToNumeric(billData.get("Customer Deposit Amount"))), decimalFormat.format(CommonUtils.convertToNumeric(billData.get("Old Credit"))), billData.get("Discount"), billData.get("Paid"), billData.get("MobileNumber"), printButton, deleteButton);
    }

    public void createPrintablePdf(Map<String, String> billData) {
        final JsonUtils jsonUtils = new JsonUtils();
        final TransactionsPojo transactionsPojo = new TransactionsPojo();
        transactionsPojo.setCustomerName(billData.get("Customer Name"));
        transactionsPojo.setAddress(billData.get("Address"));
        transactionsPojo.setTransactionDate(billData.get("DateTime"));
        transactionsPojo.setProducts(jsonUtils.getProductsPojo(billData.get("Products")));
        transactionsPojo.setScraps(jsonUtils.getScrapsPojo(billData.get("Scraps")));
        transactionsPojo.setTotalOfProduct(CommonUtils.convertToNumeric(billData.get("Total Cost of Products")));
        transactionsPojo.setTotalOfScrap(CommonUtils.convertToNumeric(billData.get("Total Cost of Scraps")));
        transactionsPojo.setDiscount(CommonUtils.convertToNumeric(billData.get("Discount")));
        transactionsPojo.setDeposit(CommonUtils.convertToNumeric(billData.get("Customer Deposit Amount")));
        transactionsPojo.setOldCredit(CommonUtils.convertToNumeric(billData.get("Old Credit")));
        transactionsPojo.setSubTotal(CommonUtils.convertToNumeric(billData.get("SubTotal")));
        transactionsPojo.setMobileNumber(billData.get("MobileNumber"));
        CreateSlipPDF createSlipPDF = new CreateSlipPDF();
        createSlipPDF.createPdf(transactionsPojo);

    }

}

