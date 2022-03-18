package org.billing.data;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.billing.configuration.EnvironmentConfig;
import org.billing.pagepojo.ScrapPojo;
import org.billing.styles.ChangeThemeInInputs;
import org.billing.utility.CommonUtils;

import java.text.DecimalFormat;

public class Scrap extends ChangeThemeInInputs {
    private TextField scrapName = new TextField();
    private TextField quantity = new TextField();
    private ComboBox<String> quantityType = new ComboBox<>();
    private TextField rate = new TextField();
    private Label total = new Label();
    private Button addRow = new Button("+");
    private Button removeRow = new Button("-");


    public Scrap(String scrapName, String quantity, String rate, String total) {
        this.scrapName.setText(scrapName);
        this.quantity.setText(quantity);
        this.rate.setText(rate);
        this.total.setText(total);
        updateTotal();
        setQuantityTypes();
    }

    public Scrap(String scrapName, String quantity, String rate) {
        this.scrapName.setText(scrapName);
        this.quantity.setText(quantity);
        this.rate.setText(rate);
        this.total.setText(String.valueOf(Double.parseDouble(quantity) * Double.parseDouble(rate)));
        updateTotal();
        setQuantityTypes();
    }

    public Scrap() {
        this.quantity.setText("0.0");
        this.rate.setText("0.0");
        this.total.setText("0.0");
        updateTotal();
        setQuantityTypes();
    }

    public boolean isValidQuantityRate() {
        if (CommonUtils.isValidNumber(this.quantity.getText()) && CommonUtils.isValidNumber(this.rate.getText())) {
            return true;
        }
        return false;
    }

    public boolean isDetailsCorrect() {
        if (CommonUtils.isValidString(this.getScrapName().getText()) && isValidQuantityRate()) {
            return true;
        }
        return false;
    }


    public void updateTotal() {
        if (isDetailsCorrect()) {
            setTotal(Double.parseDouble(getQuantity().getText()) * Double.parseDouble(getRate().getText()));
        }
    }

    public ScrapPojo getScrapPojo(String id) {
        boolean isDetailsCorrect = true;
        ScrapPojo scrapPojo = new ScrapPojo();
        scrapPojo.setId(id);
        String scrap = this.scrapName.getText();

        if (!CommonUtils.isValidString(scrap)) {
            setInvalidTextStyle(this.scrapName);
            isDetailsCorrect = false;
            return null;
        } else {
            scrapPojo.setProductName(scrap);
            setValidTextStyle(this.scrapName);
        }

        String quantity = this.quantity.getText();
        if (!CommonUtils.isValidNumber(quantity)) {
            setInvalidTextStyle(this.quantity);
            isDetailsCorrect = false;
            return null;
        } else {
            scrapPojo.setQuantity(quantity);
            setValidTextStyle(this.quantity);
        }


        String quantityType = this.quantityType.getValue();
        System.out.println(quantityType);
        if (!CommonUtils.isValidString(quantityType)) {
            setInvalidTextStyle(this.quantityType);
            isDetailsCorrect = false;
            return null;
        } else {
            scrapPojo.setQuantityType(quantityType);
            setValidTextStyle(this.quantityType);
        }

        String rate = this.rate.getText();
        if (!CommonUtils.isValidNumber(rate)) {
            setInvalidTextStyle(this.rate);
            isDetailsCorrect = false;
            return null;
        } else {
            scrapPojo.setRate(rate);
            setValidTextStyle(this.rate);
        }

        if (CommonUtils.isValidNumber(quantity) && CommonUtils.isValidNumber(rate)) {
            String totalAmount = String.valueOf(Double.parseDouble(quantity) * Double.parseDouble(rate));
            scrapPojo.setTotal(totalAmount);
        }
        return (isDetailsCorrect) ? scrapPojo : null;
    }


    public TextField getScrapName() {
        return scrapName;
    }

    public void setScrapName(TextField scrapName) {
        this.scrapName = scrapName;
    }

    public TextField getQuantity() {
        return quantity;
    }

    public void setQuantity(TextField quantity) {
        this.quantity = quantity;
    }

    public ComboBox<String> getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(ComboBox<String> quantityType) {
        this.quantityType = quantityType;
    }

    public void setQuantityTypes() {
//        quantityType.setPromptText("Select");
        ObservableList<String> list = quantityType.getItems();
        list.addAll(EnvironmentConfig.getQuantityTypes());
    }

    public TextField getRate() {
        return rate;
    }

    public void setRate(TextField rate) {
        this.rate = rate;
    }

    public Label getTotal() {
        return total;
    }

    public void setTotal(Double total) {

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        if (isDetailsCorrect()) {
            this.total.setText(df.format(total));
        }
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
