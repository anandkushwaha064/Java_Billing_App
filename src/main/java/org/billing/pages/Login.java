package org.billing.pages;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.billing.configuration.EnvironmentConfig;
import org.billing.utility.CommonUtils;
import javafx.scene.control.*;
import org.billing.session.Session;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Login extends Baseclass implements Initializable {

    private Dashboard dashboard = new Dashboard();

    @FXML
    private Button loginButton;
    @FXML
    private Button closeButton;
    @FXML
    private TextField userNameTextFiled;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label usernameErrorMessage;
    @FXML
    private Label passwordErrorMessage;

    public void loadLoginPage() {
        Stage stage = EnvironmentConfig.getStage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(EnvironmentConfig.getFxmlFilePath("Login.fxml")));
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent, 700, 500);
            stage.setScene(scene);
        } catch (Exception exception) {
            MessageScreen.showException("Error Occurred While loading Login Page, Make sure Login page Exist in repository : \n" + exception.getMessage());
            stage.close();
        }
    }

    @FXML
    private void onCloseButtonClick(MouseEvent actionEvent) {
        System.exit(0);
    }

    @FXML
    private void onLoginButtonClick(ActionEvent actionEvent) throws IOException {
        String username = userNameTextFiled.getText();
        String password = passwordField.getText();
        if (!CommonUtils.isValidString(username)) {
            usernameErrorMessage.setText("Enter Username ");
            setInvalidTextStyle(userNameTextFiled);
        }
        if (!CommonUtils.isValidString(password)) {
            passwordErrorMessage.setText("Enter Password");
            setInvalidTextStyle(passwordField);
        }
        if (CommonUtils.isValidString(username) && CommonUtils.isValidString(password)) {
            usernameErrorMessage.setText("");
            passwordErrorMessage.setText("");

            setToDefaultStyle(userNameTextFiled);
            setToDefaultStyle(passwordField);

            boolean isCorrect = Session.startSession(username, password);
            if (isCorrect) {
                dashboard.showDashboard();
                System.out.println("User LoggedIn");
            } else {
                setInvalidTextStyle(userNameTextFiled);
                setInvalidTextStyle(passwordField);
                passwordErrorMessage.setText("Invalid User ID or Password");
            }
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        userNameTextFiled.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                usernameErrorMessage.setText("");
                setToDefaultStyle(userNameTextFiled);
            }
        });

        passwordField.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                passwordErrorMessage.setText("");
                setToDefaultStyle(passwordField);
            }
        });
    }

}
