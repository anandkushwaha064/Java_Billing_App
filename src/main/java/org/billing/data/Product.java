package org.billing.data;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.billing.configuration.EnvironmentConfig;
import org.billing.pagepojo.ProductPojo;
import org.billing.styles.ChangeThemeInInputs;
import org.billing.utility.CommonUtils;

import java.text.DecimalFormat;

public class Product extends ChangeThemeInInputs {

    private TextField productName = new TextField();
    private TextField quantity = new TextField();
    private ComboBox<String> quantityType = new ComboBox<>();
    private TextField rate = new TextField();
    private TextField gst = new TextField();
    private Label total = new Label();
    private Button addRow = new Button("+");
    private Button removeRow = new Button("-");


    public Product(String productName, String quantity, String rate, String gst, String total) {
        this.productName.setText(productName);
        this.quantity.setText(quantity);
        this.rate.setText(rate);
        this.gst.setText(gst);
        this.total.setText(total);
        updateTotal();
        setQuantityTypes();
    }

    public Product(String productName, String quantity, String rate, String gst) {
        this.productName.setText(productName);
        this.quantity.setText(quantity);
        this.rate.setText(rate);
        this.gst.setText(gst);
        this.total.setText(String.valueOf(Double.parseDouble(quantity) * Double.parseDouble(rate)));
        updateTotal();
        setQuantityTypes();
    }


    public Product() {
        this.quantity.setText("0.0");
        this.rate.setText("0.0");
        this.total.setText("0.0");
        this.gst.setText("0.0");
        updateTotal();
        setQuantityTypes();
    }

    public boolean isValidQuantityRate() {
        if (CommonUtils.isValidNumberNotZero(this.quantity.getText()) && CommonUtils.isValidNumberNotZero(this.rate.getText()) ) {
            return true;
        }
        return false;
    }

    public boolean isDetailsCorrect() {
        if (CommonUtils.isValidString(this.getProductName().getText()) && isValidQuantityRate()) {
            return true;
        }
        return false;
    }


    public void updateTotal() {
        if (isDetailsCorrect()) {
            Double gstValue = CommonUtils.convertToNumeric(getGst().getText());
            if (gstValue != 0) {
                setTotal(CommonUtils.convertToNumeric(getQuantity().getText()) *
                        CommonUtils.convertToNumeric(getRate().getText()) *
                        (gstValue + 100) / 100);
            } else {
                setTotal(CommonUtils.convertToNumeric(getQuantity().getText()) *
                        CommonUtils.convertToNumeric(getRate().getText()));
            }
        }
    }


    public ProductPojo getProductPojo(String id) {
        Boolean isDetailsCorrect = true;
        ProductPojo productPojo = new ProductPojo();
        productPojo.setId(id);
        String product = this.productName.getText();
        if (!CommonUtils.isValidString(product)) {
            setInvalidTextStyle(this.productName);
            isDetailsCorrect = false;
            return null;
        } else {
            setValidTextStyle(this.productName);
            productPojo.setProductName(product);
        }

        String quantity = this.quantity.getText();
        if (!CommonUtils.isValidNumber(quantity)) {
            setInvalidTextStyle(this.quantity);
            isDetailsCorrect = false;
            return null;
        } else {
            productPojo.setQuantity(quantity);
            setValidTextStyle(this.quantity);
        }

        String quantityType = this.quantityType.getValue();
        System.out.println(quantityType);
        if (!CommonUtils.isValidString(quantityType)) {
            setInvalidTextStyle(this.quantityType);
            isDetailsCorrect = false;
            return null;
        } else {
            productPojo.setQuantityType(quantityType);
            setValidTextStyle(this.quantityType);
        }

        String rate = this.rate.getText();
        if (!CommonUtils.isValidNumber(rate)) {
            setInvalidTextStyle(this.rate);
            isDetailsCorrect = false;
            return null;
        } else {
            productPojo.setRate(rate);
            setValidTextStyle(this.rate);
        }

        String gst = this.gst.getText();
        if (!CommonUtils.isValidNumber(gst)) {
            setInvalidTextStyle(this.gst);
            isDetailsCorrect = false;
            return null;
        } else {
            productPojo.setGst(gst);
            setValidTextStyle(this.gst);
        }

        updateTotal();
        productPojo.setTotal(total.getText());

        return (isDetailsCorrect) ? productPojo : null;
    }

    public TextField getProductName() {
        return productName;
    }

    public void setProductName(TextField productName) {
        this.productName = productName;
    }

    public TextField getQuantity() {
        return quantity;
    }

    public void setQuantity(TextField quantity) {
        this.quantity = quantity;
    }

    public void setQuantityTypes() {
        ObservableList<String> list = quantityType.getItems();
        list.addAll(EnvironmentConfig.getQuantityTypes());
    }

    public ComboBox<String> getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(ComboBox<String> quantityType) {
        this.quantityType = quantityType;
    }


    public TextField getRate() {
        return rate;
    }

    public void setRate(TextField rate) {
        this.rate = rate;
    }

    public TextField getGst() {
        return gst;
    }

    public void setGst(TextField gst) {
        this.gst = gst;
    }

    public Label getTotal() {
        return total;
    }

    public Double getTotalValue() {
        return CommonUtils.convertToNumeric(total.getText());
    }

    public void setTotal(Double total) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        this.total.setText(df.format(total));
    }

    public Button getAddRow() {
        return addRow;
    }

    public void setAddRow(Button addRow) {
        this.addRow = addRow;
    }

    public Button getRemoveRow() {
        return removeRow;
    }

    public void setRemoveRow(Button removeRow) {
        this.removeRow = removeRow;
    }
}
