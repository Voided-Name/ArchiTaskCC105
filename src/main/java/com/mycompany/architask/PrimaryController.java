package com.mycompany.architask;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javax.swing.JOptionPane;
import javafx.scene.control.Label;
import org.mindrot.jbcrypt.BCrypt;

public class PrimaryController {

    @FXML
    private TextField rFName;
    @FXML
    private TextField rLName;
    @FXML
    private TextField rEmail;
    @FXML
    private TextField rUName;
    @FXML
    private PasswordField rPassword;
    @FXML
    private PasswordField rCPassword;
    @FXML
    private ChoiceBox<String> rUType;
    @FXML
    private Label emailWarn;
    @FXML
    private Label usernameWarn;
    @FXML
    private Label passwordWarn;
    @FXML
    private Label cPasswordWarn;
    @FXML
    private Label genWarn;

    @FXML
    private void initialize() {
        rUType.getItems().addAll("Senior Architect", "Architect", "Admin");
        App.controlFocus(rFName);
        App.controlFocus(rFName);
        App.controlFocus(rLName);
        App.controlFocus(rEmail);
        emailControlFocus(rEmail);
        App.controlFocus(rUName);
        App.controlFocus(rPassword);
        App.controlFocus(rCPassword);
        App.controlFocus(rUType);
    }

    private void emailControlFocus(TextField email) {
        email.focusedProperty().addListener((obs, unFocus, Focus) -> {
            if (Focus) {

            } else {
                emailWarn.setText("");
            }
        });
    }

    @FXML
    private void switchToLogin() throws IOException {
        App.setRoot("login");
    }

    private void folderSetup() {
    }

    private void clearStyles() {
        rFName.setStyle("");
        rLName.setStyle("");
        rEmail.setStyle("");
        rUName.setStyle("");
        rPassword.setStyle("");
        rCPassword.setStyle("");
        rUType.setStyle("");
    }

    private boolean emailChecker(String email) {
        int atPosition = email.indexOf('@');
        int perPosition = email.indexOf('.');

        return perPosition > atPosition;
    }

    @FXML
    private void btnRegister() throws IOException {
        emailWarn.setText("");
        usernameWarn.setText("");
        cPasswordWarn.setText("");
        genWarn.setText("");

        boolean notBlank = false, validEmail = false, validUsername = false, validPassword = false;
        String fName = rFName.getText().trim();
        String lName = rLName.getText().trim();
        String email = rEmail.getText().trim();
        String uName = rUName.getText().trim();
        String password = rPassword.getText();
        String cPassword = rCPassword.getText();
        String uType = rUType.getValue();
        int uTypeInt = 0;

        if (fName.isBlank() || lName.isBlank() || email.isBlank() || uName.isBlank() ||
                password.isBlank() || cPassword.isBlank() || uType == null) {

            if (fName.isBlank()) {
                rFName.setStyle("-fx-border-color: red; -fx-border-width: 2;");
            }
            if (lName.isBlank()) {
                rLName.setStyle("-fx-border-color: red; -fx-border-width: 2;");
            }
            if (email.isBlank()) {
                rEmail.setStyle("-fx-border-color: red; -fx-border-width: 2;");
            }
            if (uName.isBlank()) {
                rUName.setStyle("-fx-border-color: red; -fx-border-width: 2;");
            }
            if (password.isBlank()) {
                rPassword.setStyle("-fx-border-color: red; -fx-border-width: 2;");
            }
            if (cPassword.isBlank()) {
                rCPassword.setStyle("-fx-border-color: red; -fx-border-width: 2;");
            }
            if (rUType.getValue() == null || rUType.getValue().isBlank()) {
                rUType.setStyle("-fx-border-color: red; -fx-border-width: 2;");
            }
            genWarn.setText("Empty Field/s");
        } else {
            clearStyles();
            notBlank = true;
        }

        if (!emailChecker(email)) {
            emailWarn.setText("Invalid Email Format!");
            rEmail.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        } else if (App.checkExistEmail(email)) {
            emailWarn.setText("Email Already In Use!");
            rEmail.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        } else {
            validEmail = true;
        }

        if (App.doesUNameExist(uName)) {
            usernameWarn.setText("Username Exists! Try Another");
            rUName.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        } else if (!(uName.indexOf(' ') == -1)) {
            usernameWarn.setText("Don't Use Whitespaces for your Username!");
            rUName.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        } else if (uName.length() > 30) {
            usernameWarn.setText("Username Length Exceedss the Limit (30)");
            rUName.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        } else {
            validUsername = true;
        }

        if (!password.equals(cPassword)) {
            rCPassword.setStyle("-fx-border-color: red; -fx-border-width: 2;");
            cPasswordWarn.setText("Passwords Do Not Match!");
        } else if (password.length() < 8) {
            rPassword.setStyle("-fx-border-color: red; -fx-border-width: 2;");
            rCPassword.setStyle("-fx-border-color: red; -fx-border-width: 2;");
            cPasswordWarn.setText("Password Should at Least be 8 characters");
        } else {
            validPassword = true;
        }

        if (uType.equals("Senior Architect")) {
            uTypeInt = 0;
        } else if (uType.equals("Architect")) {
            uTypeInt = 1;
        } else {
            uTypeInt = 2;

        }

        if (validPassword && validEmail && validUsername && notBlank) {
            password = BCrypt.hashpw(password, BCrypt.gensalt());
            App.registerSQL(fName, lName, email, uName, password, uTypeInt);
            JOptionPane.showMessageDialog(null, "Succesful Registration");
            rFName.setText("");
            rLName.setText("");
            rEmail.setText("");
            rUName.setText("");
            rPassword.setText("");
            rCPassword.setText("");
            rUType.setValue(null);
        }

        System.out.println(fName);
        System.out.println(lName);
        System.out.println(email);
        System.out.println(uName);
        System.out.println(password);
        System.out.println(cPassword);
        System.out.println(uType);
    }

}
