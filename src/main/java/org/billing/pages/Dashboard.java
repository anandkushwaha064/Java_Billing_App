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
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.billing.configuration.EnvironmentConfig;
import org.billing.data.DatafileHandler;
import org.billing.data.Product;
import org.billing.data.Scrap;
import org.billing.pagepojo.ProductPojo;
import org.billing.pagepojo.ScrapPojo;
import org.billing.pagepojo.TransactionsPojo;
import org.billing.pdf.CreateSlipPDF;
import org.billing.session.Session;
import org.billing.utility.*;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

public class Dashboard extends Baseclass implements Initializable {

    @FXML
    private Button showBills;

    @FXML
    private Button Dashboard;

    @FXML
    private Button close;

    @FXML
    private TableView<Product> ProductListTable;

    @FXML
    private TableColumn<Product, TextField> ProductName;

    @FXML
    private TableColumn<Product, TextField> ProductQuantity;

    @FXML
    private TableColumn<Product, ComboBox<String>> ProductQuantityType;

    @FXML
    private TableColumn<Product, TextField> ProductRate;

    @FXML
    private TableColumn<Product, TextField> gst;

    @FXML
    private TableColumn<Product, Label> ProductTotal;

    @FXML
    private TableColumn<Product, Button> addProductRow;

    @FXML
    private TableColumn<Product, Button> removeProductRow;

    @FXML
    private TableView<Scrap> ScrapListTable;

    @FXML
    private TableColumn<Scrap, TextField> ScrapName;

    @FXML
    private TableColumn<Scrap, TextField> ScrapQuantity;

    @FXML
    private TableColumn<Scrap, ComboBox<String>> ScrapQuantityType;

    @FXML
    private TableColumn<Scrap, TextField> ScrapRate;

    @FXML
    private TableColumn<Scrap, Label> ScrapTotal;

    @FXML
    private TableColumn<Scrap, Button> addScrapRow;

    @FXML
    private TableColumn<Scrap, Button> removeScrapRow;

    @FXML
    private Label productTotalLabel;

    @FXML
    private Label numberOfProducts;

    @FXML
    private Label scrapTotalLabel;

    @FXML
    private Label numberOfScraps;

    @FXML
    private Label subTotalLabel;

    @FXML
    private TextField customerNameTextField;

    @FXML
    private DatePicker transactionDate;

    @FXML
    private Button printButton;

    @FXML
    private Button saveButton;

    @FXML
    private TextField customerAmount;

    @FXML
    private TextField oldCredit;

    @FXML
    private TextField Discount;

    @FXML
    private Label totalLabel;

    @FXML
    private Label summaryLabel;

    @FXML
    private TextField mobileNumber;

    @FXML
    private TextField address;

    @FXML
    private CheckBox paidCheckbox;

    private ObservableList<Product> products;
    private ObservableList<Scrap> scraps;
    private Double totalOfProduct = 0.0;
    private Double totalOfScrap = 0.0;
    private Double customerDeposit = 0.0;
    private Double customerOldCredit = 0.0;
    private Double discount = 0.0;
    private Double subTotal = 0.0;
    private String dataId = null;
    private final DecimalFormat df = new DecimalFormat();

    public void showDashboard() {
        Stage stage = EnvironmentConfig.getStage();
        if (!Session.isLoggedIn()) {
            System.exit(0);
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(EnvironmentConfig.getFxmlFilePath("Dashboard.fxml")));
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setMaximized(false);
            stage.setMaximized(true);
            EnvironmentConfig.configureDashboard();
        } catch (Exception exception) {
            exception.printStackTrace();
            MessageScreen.showException("Error Occurred While loading Dashboard Page, Make sure Dashboard page Exist in repository : \n" + exception.getMessage());
            stage.close();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDashboard();
        SystemUtils.convertDatePicker(transactionDate);
        new Menus(showBills, Dashboard, close).setAction();
    }

    public void loadDashboard() {
        String summary = "";
        String mobNumber = "";
        String customerAddress = "";
        setListener();

        df.setMaximumFractionDigits(2);

        products = FXCollections.observableArrayList();
        scraps = FXCollections.observableArrayList();

        if (EnvironmentConfig.getDataId() != null) {
            Map<String, String> billData = DatafileHandler.getDataFileAccess().getRowDataMatchingDataId(EnvironmentConfig.getDataId());
            dataId = String.copyValueOf(EnvironmentConfig.getDataId().toCharArray());
            EnvironmentConfig.setDataId(null);

            if (billData.size() > 0) {
                customerNameTextField.setText(billData.get("Customer Name"));
                String[] dateTime = SystemUtils.getDateTime(billData.get("DateTime"));
                if (dateTime != null)
                    transactionDate.setValue(SystemUtils.getLocalDateFromString(dateTime[0]));
                customerDeposit = Double.parseDouble(billData.get("Customer Deposit Amount"));
                customerOldCredit = Double.parseDouble(billData.get("Old Credit"));
                mobNumber = billData.get("MobileNumber");
                customerAddress = billData.get("Address");

                totalOfProduct = Double.parseDouble(billData.get("Total Cost of Products"));
                totalOfScrap = Double.parseDouble(billData.get("Total Cost of Scraps"));
                subTotal = Double.parseDouble(billData.get("SubTotal"));

                summary = "[" + df.format(totalOfProduct) + " + " + df.format(customerOldCredit) + "] - [" + df.format(totalOfScrap) + "+" + df.format(customerDeposit) + "]";

                JsonUtils jsonUtils = new JsonUtils();
                final List<ProductPojo> productsData = jsonUtils.getProductsPojo(billData.get("Products"));
                for (final ProductPojo data : productsData) {
//                    addNewProductFromData(data);
                }

                final List<ScrapPojo> scrapsData = jsonUtils.getScrapsPojo(billData.get("Scraps"));
                for (final ScrapPojo data : scrapsData) {
//                    addNewScrapFromData(data);
                }

                productTable();
                scrapsTable();
            }
        } else {
            customerNameTextField.setText("");
            transactionDate.setValue(LocalDate.now());
            totalOfProduct = 0.0;
            totalOfScrap = 0.0;
            subTotal = 0.0;
            discount = 0.0;
            summary = "";
            mobNumber = "";
            customerAddress = "";
            customerOldCredit = 0.0;
            customerDeposit = 0.0;
            productTable();
            scrapsTable();
            addNewScrap(null);
            addNewProduct(null);
        }

        summaryLabel.setText(summary);
        mobileNumber.setText(mobNumber);
        address.setText(customerAddress);
        Discount.setText(discount.toString());
        oldCredit.setText(df.format(customerOldCredit));
        customerAmount.setText(df.format(customerDeposit));
        scrapTotalLabel.setText(df.format(totalOfScrap));
        productTotalLabel.setText(df.format(totalOfProduct));
        subTotalLabel.setText(df.format(totalOfProduct - totalOfScrap));
        totalLabel.setText(df.format(subTotal));
    }

    public void addNewProductFromData(final Map<String, String> data) {
        Product newProduct = new Product(data.get("name"), data.get("quantity"), data.get("rate"), data.get("gst"), data.get("total"));
        products.add(newProduct);
        setCommonInProduct(newProduct);
    }

    public void addNewProduct(final Product product) {
        Product newProduct = null;
        if (product == null) {
            newProduct = new Product();
            products.add(newProduct);
        } else {
            int index = products.indexOf(product);
            if (index == -1) {
                products.add(product);
            } else {
                newProduct = new Product();
                products.add(index + 1, newProduct);
            }
        }
        if (newProduct != null) {
            newProduct.updateTotal();
            setCommonInProduct(newProduct);
        }
    }

    public void setCommonInProduct(Product product) {
        numberOfProducts.setText(String.valueOf(products.size()));
        numericTextField(product.getQuantity());
        numericTextField(product.getRate());
        numericTextField(product.getGst());
        product.getAddRow().setFocusTraversable(false);
        product.getRemoveRow().setFocusTraversable(false);

        product.getAddRow().setOnMouseClicked(event -> addNewProduct(product));

        product.getAddRow().setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                product.updateTotal();
            }
        });

        product.getRemoveRow().setOnMouseClicked(event -> {
            if (products.size() > 1) {
                products.remove(product);
            }
            updateProductCalculation();
            numberOfScraps.setText(String.valueOf(scraps.size()));
        });

        product.getAddRow().setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                product.updateTotal();
            }
        });

        product.getRate().setOnKeyReleased(event -> {
//            setTotal(product);
            product.updateTotal();
            updateProductCalculation();
        });

        product.getQuantity().setOnKeyReleased(event -> {
//            setTotal(product);

            product.updateTotal();
            updateProductCalculation();
        });

        product.getGst().setOnKeyReleased(event -> {
//            setTotal(product);
            product.updateTotal();
            updateProductCalculation();
        });
    }

    public void removeEmptyProduct() {
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            if (!product.isValidQuantityRate() && products.size() > 1) {
                products.remove(product);
                i--;
            }
        }
    }

    public boolean updateProductCalculation() {
        boolean isDetailsCorrect = true;
        totalOfProduct = 0.0;

        for (Product product : products) {
            if ((!CommonUtils.isValidString(product.getProductName().getText()))) {
                setInvalidTextStyle(product.getProductName());
            } else {
                setValidTextStyle(product.getProductName());
            }

            isDetailsCorrect = product.isDetailsCorrect();
            product.updateTotal();
            Double aDouble = product.getTotalValue();
            totalOfProduct = totalOfProduct + aDouble;
        }

        numberOfProducts.setText(String.valueOf(products.size()));
        setTotalLabels();
        return isDetailsCorrect;
    }

    public void productTable() {
        ProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        ProductQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        ProductQuantityType.setCellValueFactory(new PropertyValueFactory<>("quantityType"));
        ProductRate.setCellValueFactory(new PropertyValueFactory<>("Rate"));
        gst.setCellValueFactory(new PropertyValueFactory<>("gst"));
        ProductTotal.setCellValueFactory(new PropertyValueFactory<>("Total"));
        addProductRow.setCellValueFactory(new PropertyValueFactory<>("addRow"));
        removeProductRow.setCellValueFactory(new PropertyValueFactory<>("removeRow"));
        ProductListTable.setItems(products);
    }

    public void addNewScrapFromData(final Map<String, String> data) {
        Scrap newScrap = new Scrap(data.get("name"), data.get("quantity"), data.get("rate"), data.get("total"));
        scraps.add(newScrap);
        setCommonInScrap(newScrap);
    }

    public void addNewScrap(final Scrap scrap) {
        Scrap newScrap = null;
        if (scrap == null) {
            newScrap = new Scrap();
            scraps.add(newScrap);
        } else {
            int index = scraps.indexOf(scrap);
            if (index == -1) {
                scraps.add(scrap);
            } else {
                newScrap = new Scrap();
                scraps.add(index + 1, newScrap);
            }
        }
        if (newScrap != null) {
            newScrap.updateTotal();
            setCommonInScrap(newScrap);
        }
    }

    public void setCommonInScrap(Scrap scrap) {
        numberOfScraps.setText(String.valueOf(scraps.size()));
        numericTextField(scrap.getQuantity());
        numericTextField(scrap.getRate());
        scrap.getAddRow().setFocusTraversable(false);
        scrap.getRemoveRow().setFocusTraversable(false);

        scrap.getAddRow().setOnMouseClicked(event -> addNewScrap(scrap));

        scrap.getAddRow().setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                scrap.updateTotal();
            }
        });

        scrap.getRemoveRow().setOnMouseClicked(event -> {

            scraps.remove(scrap);
            updateScrapCalculation();
        });

        scrap.getAddRow().setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                scrap.updateTotal();
            }
        });

        scrap.getRate().setOnKeyReleased(event -> {
            setTotal(scrap);
            updateScrapCalculation();
        });

        scrap.getQuantity().setOnKeyReleased(event -> {
            setTotal(scrap);
            updateScrapCalculation();
        });

    }

    public void setTotal(Scrap scrap) {
        String quantityValue = scrap.getQuantity().getText();
        String rateValue = scrap.getRate().getText();
        if (CommonUtils.isValidNumber(quantityValue) && CommonUtils.isValidNumber(rateValue)) {
            scrap.getTotal().setText(String.valueOf(Double.parseDouble(quantityValue) * Double.parseDouble(rateValue)));
        } else {
            scrap.getTotal().setText("0.0");
        }

    }

    public void removeEmptyScraps() {
        for (int i = 0; i < scraps.size(); i++) {
            Scrap scrap = scraps.get(i);
            if (!scrap.isValidQuantityRate()) {
                scraps.remove(scrap);
                i--;
            }
        }

    }

    public boolean updateScrapCalculation() {
        boolean isDetailsCorrect = true;
        totalOfScrap = 0.0;
        for (Scrap scrap : scraps) {
            if ((!CommonUtils.isValidString(scrap.getScrapName().getText()))) {
                setInvalidTextStyle(scrap.getScrapName());
            } else {
                setValidTextStyle(scrap.getScrapName());
            }

            if (scrap.isDetailsCorrect()) {
                Double total = Double.parseDouble(scrap.getQuantity().getText()) * Double.parseDouble(scrap.getRate().getText());
                totalOfScrap = totalOfScrap + total;
                scrap.setTotal(total);
            } else
                isDetailsCorrect = false;
        }

        setTotalLabels();
        numberOfScraps.setText(String.valueOf(scraps.size()));
        return isDetailsCorrect;
    }

    public void scrapsTable() {
        ScrapName.setCellValueFactory(new PropertyValueFactory<>("scrapName"));
        ScrapQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        ScrapQuantityType.setCellValueFactory(new PropertyValueFactory<>("quantityType"));
        ScrapRate.setCellValueFactory(new PropertyValueFactory<>("Rate"));
        ScrapTotal.setCellValueFactory(new PropertyValueFactory<>("Total"));
        addScrapRow.setCellValueFactory(new PropertyValueFactory<>("addRow"));
        removeScrapRow.setCellValueFactory(new PropertyValueFactory<>("removeRow"));
        ScrapListTable.setItems(scraps);
    }

    public TransactionsPojo isFromDetailsCorrect() {
        List<ProductPojo> productPojoList = new ArrayList<>();
        List<ScrapPojo> scrapPojoList = new ArrayList<>();

        boolean shouldFormSubmit = true;
        TransactionsPojo transactionsPojo = new TransactionsPojo();

        String customerName = customerNameTextField.getText();
        if (CommonUtils.isValidString(customerName)) {
            transactionsPojo.setCustomerName(customerName);
            setValidTextStyle(customerNameTextField);
        } else {
            shouldFormSubmit = false;
            setInvalidTextStyle(customerNameTextField);
        }

        String date = SystemUtils.getDateInSystemFormat(transactionDate.getValue());
        if (CommonUtils.isValidString(date)) {
            transactionsPojo.setTransactionDateTime(SystemUtils.getDateWithCurrentTime(transactionDate.getValue()));
            setValidTextStyle(transactionDate);
        } else {
            shouldFormSubmit = false;
            setInvalidTextStyle(transactionDate);
        }

        removeEmptyProduct();
        boolean isProductDetailsCorrect = updateProductCalculation();
        if (isProductDetailsCorrect) {
            int i = 1;
            for (Product product : products) {
                productPojoList.add(product.getProductPojo(String.valueOf(i++)));
            }
        }
        transactionsPojo.setProducts(productPojoList);
        shouldFormSubmit = shouldFormSubmit && isProductDetailsCorrect;


        removeEmptyScraps();
        boolean isScrapsDetailsCorrect = updateScrapCalculation();
        if (isScrapsDetailsCorrect) {
            int i = 1;
            for (Scrap scrap : scraps) {
                scrapPojoList.add(scrap.getScrapPojo(String.valueOf(i++)));
            }
        }
        transactionsPojo.setScraps(scrapPojoList);
        shouldFormSubmit = shouldFormSubmit && isScrapsDetailsCorrect;


        String customerDepositAmount = customerAmount.getText();
        if (CommonUtils.isValidNumber(customerDepositAmount)) {
            transactionsPojo.setDeposit(CommonUtils.convertToNumeric(customerDepositAmount));
            setValidTextStyle(customerAmount);
        } else {
            shouldFormSubmit = false;
            setInvalidTextStyle(customerAmount);
        }

        String discountText = Discount.getText();
        if (CommonUtils.isValidNumber(discountText)) {
            transactionsPojo.setDiscount(CommonUtils.convertToNumeric(discountText));
            setValidTextStyle(Discount);
        } else {
            shouldFormSubmit = false;
            setInvalidTextStyle(Discount);
        }

        String oldCustomerCredit = oldCredit.getText();
        if (CommonUtils.isValidNumber(oldCustomerCredit)) {
            transactionsPojo.setOldCredit(CommonUtils.convertToNumeric(oldCustomerCredit));
            setValidTextStyle(oldCredit);
        } else {
            shouldFormSubmit = false;
            setInvalidTextStyle(oldCredit);
        }

        String number = mobileNumber.getText();
        if (CommonUtils.isValidString(number)) {
            if (CommonUtils.isValidMobileNumber(number)) {
                transactionsPojo.setMobileNumber(number);
                setValidTextStyle(mobileNumber);
            } else {
                shouldFormSubmit = false;
                setInvalidTextStyle(mobileNumber);
            }
        } else
            transactionsPojo.setMobileNumber("");

        transactionsPojo.setPaid(paidCheckbox.isSelected() ? "Yes" : "No");

        setTotalLabels();

        String customerAddress = address.getText();
        transactionsPojo.setAddress(CommonUtils.isValidString(customerAddress) ? customerAddress : "");

        transactionsPojo.setTotalOfProduct(totalOfProduct);
        transactionsPojo.setTotalOfScrap(totalOfScrap);
        transactionsPojo.setSubTotal(subTotal);


        System.out.println(transactionsPojo);
        System.out.println(shouldFormSubmit);

        return (shouldFormSubmit) ? transactionsPojo : null;
    }

    public void setPrintButtonClicked() {
        TransactionsPojo transactionsPojo = isFromDetailsCorrect();
        if (transactionsPojo != null) {
            handleTransactionData(transactionsPojo);
            CreateSlipPDF slipPDF = new CreateSlipPDF();
            slipPDF.createPdf(transactionsPojo);
            System.out.println(slipPDF.fullFilePath);
        } else
            MessageScreen.showAlertMessage("Please fill all Details correctly");
    }

    public void setSaveButtonClicked() {
        TransactionsPojo transactionsPojo = isFromDetailsCorrect();
        if (transactionsPojo != null) {
            handleTransactionData(transactionsPojo);
        } else
            MessageScreen.showAlertMessage("Please fill all Details correctly");
    }

    public void handleTransactionData(TransactionsPojo transactionsPojo) {
        if (DatafileHandler.updateRow(transactionsPojo, dataId)) {
            MessageScreen.showMessage("Transaction has been successfully updated");
        } else if (DatafileHandler.insertRow(transactionsPojo)) {
            MessageScreen.showMessage("Transaction has been successfully inserted");
        }
        loadDashboard();

    }

    public void setListener() {
        numericTextField(customerAmount);
        numericTextField(Discount);
        numericTextFieldWithMinus(oldCredit);
        mobileNumberTextField(mobileNumber);
        numericTextField(Discount);

        Discount.setOnKeyReleased(event -> setTotalLabels());
        customerAmount.setOnKeyReleased(event -> setTotalLabels());
        oldCredit.setOnKeyReleased(event -> setTotalLabels());
        saveButton.setOnMouseClicked(event -> setSaveButtonClicked());
        printButton.setOnMouseClicked(event -> setPrintButtonClicked());

    }

    public void setTotalLabels() {
        customerDeposit = CommonUtils.convertToNumeric(customerAmount.getText());
        customerOldCredit = CommonUtils.convertToNumeric(oldCredit.getText());
        discount = CommonUtils.convertToNumeric(Discount.getText());
        subTotal = (totalOfProduct + customerOldCredit) - (totalOfScrap + customerDeposit + discount);
        summaryLabel.setText("[" + df.format(totalOfProduct) + " + " + df.format(customerOldCredit) + "] - [" + df.format(totalOfScrap) + "+" + df.format(customerDeposit) + "+" + df.format(discount) + "]");
        productTotalLabel.setText(df.format(totalOfProduct));
        scrapTotalLabel.setText(df.format(totalOfScrap));
        subTotalLabel.setText(df.format(totalOfProduct) + " - " + df.format(totalOfScrap) + " = " + df.format(totalOfProduct - totalOfScrap));
        totalLabel.setText(df.format(subTotal));
    }
}

