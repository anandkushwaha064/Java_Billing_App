package org.billing.styles;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class ChangeThemeInInputs {

    protected void setInvalidTextStyle(TextField textField) {
        textField.setStyle("-fx-background-color : rgb(247, 115, 82);");
    }
    protected void setInvalidTextStyle(ComboBox comboBox) {
        comboBox.setStyle("-fx-background-color : rgb(247, 115, 82);");
    }

    protected void setInvalidTextStyle(DatePicker datePicker) {
        datePicker.setStyle("-fx-background-color : rgb(247, 115, 82);");
    }

    protected void setValidTextStyle(TextField textField) {
        textField.setStyle(null);
    }

    protected void setValidTextStyle(DatePicker datePicker) {
        datePicker.setStyle(null);
    }
    protected void setValidTextStyle(ComboBox ComboBox) {
        ComboBox.setStyle(null);
    }


}
