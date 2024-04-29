package com.mycompany.architask;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import java.sql.SQLException;

public class SecondaryController {
    @FXML
    private TextField lUName;
    @FXML
    private PasswordField lPassword;
    @FXML
    private Label loginWarn;

    @FXML
    private void initialize() {
        App.controlFocus(lUName);
        App.controlFocus(lPassword);
    }

    @FXML
    private void switchToSignUp() throws IOException {
        App.setRoot("signup");
    }

    @FXML
    private void scanQr() {
        new QrCapture();
    }

    @FXML
    private void login() throws IOException {
        loginWarn.setText("");

        // input
        String uName = lUName.getText();
        String password = lPassword.getText();

        // input validation, if blank highlight the affected fields
        if (uName.isBlank() || password.isBlank()) {
            if (uName.isBlank()) {
                lUName.setStyle("-fx-border-color: red; -fx-border-width: 2;");
            } else if (password.isBlank()) {
                lPassword.setStyle("-fx-border-color: red; -fx-border-width: 2;");
            }
            loginWarn.setText("Empty Field/s");
        }

        // App.loginSQL returns a boolean of whether the credentials inputted exists in
        // the system and is valid
        if (App.loginSQL(uName, password)) {
            int uType;
            try {
                App.currentUser(uName);
            } catch (SQLException err) {
                err.printStackTrace();
            }
            if (App.getUserType() == 2) {
                App.setRoot("admin");
            } else {
                App.setRoot("dashboard");
            }
            App.setResizable(true);
            App.setMaximized(true);
        } else {
            loginWarn.setText("Invalid Username/Password!");
        }
    }
}
