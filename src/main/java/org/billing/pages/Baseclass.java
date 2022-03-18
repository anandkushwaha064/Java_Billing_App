package org.billing.pages;

import javafx.scene.control.TextField;
import org.billing.styles.ChangeThemeInInputs;


public class Baseclass extends ChangeThemeInInputs {


    protected void setToDefaultStyle(TextField textField) {
        textField.setStyle(null);
    }

    public void numericTextField(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {

            for (int i = 0; i < newValue.length(); i++) {
                String character = String.valueOf(newValue.charAt(i));
                if (!character.matches("\\d*|[.]")) {
                    newValue = newValue.replace(character, "");
                }
            }
            textField.setText(newValue);
        });
        textField.setOnMouseClicked(event -> textField.selectAll());
    }

    public void numericTextFieldWithMinus(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {

            for (int i = 0; i < newValue.length(); i++) {
                String character = String.valueOf(newValue.charAt(i));
                if (!character.matches("\\d*|[.]|-")) {
                    newValue = newValue.replace(character, "");
                }
            }
            textField.setText(newValue);
        });
        textField.setOnMouseClicked(event -> textField.selectAll());
    }

    public void mobileNumberTextField(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {

            for (int i = 0; i < newValue.length(); i++) {
                String character = String.valueOf(newValue.charAt(i));
                if (!character.matches("\\d*")) {
                    newValue = newValue.replace(character, "");
                }
            }
            textField.setText(newValue);
        });
        textField.setOnMouseClicked(event -> textField.selectAll());
    }

    public void allSelectOnClick(TextField textField) {
        textField.setOnMouseClicked(event -> textField.selectAll());
    }


}
