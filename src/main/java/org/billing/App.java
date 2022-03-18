package org.billing;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.billing.configuration.EnvironmentConfig;
import org.billing.pages.Dashboard;
import org.billing.pages.Login;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {
    private Scene scene;
    private Login login = new Login();

    public App() {

    }

    @Override
    public void start(Stage stage) {
        EnvironmentConfig.configureProject(stage);
        login.loadLoginPage();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}