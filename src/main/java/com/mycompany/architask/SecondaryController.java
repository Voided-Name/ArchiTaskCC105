package com.mycompany.architask;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import java.sql.SQLException;
import javafx.scene.control.CheckBox;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Scanner;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;

public class SecondaryController {
    @FXML
    private TextField lUName;
    @FXML
    private PasswordField lPassword;
    @FXML
    private Label loginWarn;
    @FXML
    CheckBox rememberMe;

    @FXML
    private void initialize() {
        File remFile = new File(
                "C:/Users/Nash/Documents/01_Docs/School/BSIT_2_B/FinalProject(2nd-Year)/ArchiTaskResources/Generated/"
                        + "remFile.txt");
        if (remFile.exists()) {
            String data = "";
            try {
                Scanner reader = new Scanner(remFile);
                if (reader.hasNextLine()) {
                    data = reader.nextLine();

                    try {
                        App.rememberMe(decrypt(data));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                reader.close();
            } catch (FileNotFoundException err) {
                err.printStackTrace();
            }

        }
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
   public void forgotPassword(){
       Email email = EmailBuilder.startingBlank().from("architask", "architaskk@gmail.com").to("jet", "zairaganda10@gmail.com").withPlainText("hehe boi").buildEmail();
       Mailer mailer = MailerBuilder.withSMTPServer("smtp.gmail.com", 587, "architaskk@gmail.com", "aslu uwtb cjdy imtm").buildMailer();
       mailer.sendMail(email);   
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
            if (rememberMe.isSelected()) {
                File remFile = new File(
                        "C:/Users/Nash/Documents/01_Docs/School/BSIT_2_B/FinalProject(2nd-Year)/ArchiTaskResources/Generated/"
                                + "remFile.txt");
                FileWriter writer = new FileWriter(
                        "C:/Users/Nash/Documents/01_Docs/School/BSIT_2_B/FinalProject(2nd-Year)/ArchiTaskResources/Generated/"
                                + "remFile.txt");
                writer.flush();

                try {
                    System.out.println(encrypt(uName));
                    writer.write(encrypt(uName));
                } catch (Exception err) {
                    err.printStackTrace();
                }
                writer.close();
            } else {
                File remFile = new File(
                        "C:/Users/Nash/Documents/01_Docs/School/BSIT_2_B/FinalProject(2nd-Year)/ArchiTaskResources/Generated/"
                                + "remFile.txt");
                if (remFile.exists()) {
                    remFile.delete();
                }
            }

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

    private static String encrypt(String text) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec("jetangelogaspars".getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptedBytes = cipher.doFinal(text.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedText) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec("jetangelogaspars".getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }
}
