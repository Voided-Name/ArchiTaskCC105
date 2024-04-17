package com.mycompany.architask;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.*;
import javax.swing.JOptionPane;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.Cursor;
import java.util.ArrayList;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Connection conn;
    private static String userNow;
    private static int userTypeNow;
    private static int userId;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("login"), 600, 400);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    public static void saveToSQL(ArrayList<Integer> projectIdArr, ArrayList<String> projectTitleArr,
            ArrayList<String> projectDetailsArr, ArrayList<String> projectImageArr) {
        openConnection();

        for (int x = 0; x < projectIdArr.size(); x++) {
            String query = "UPDATE tblprojectinfo SET projectTitle = ?, projectDetails = ?, projectImage = ? WHERE projectId = ?";
            System.out.println("Query: " + projectTitleArr.get(x) + ", " + projectDetailsArr.get(x) + ", "
                    + projectImageArr.get(x));

            try {
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, projectTitleArr.get(x));
                pst.setString(2, projectDetailsArr.get(x));
                pst.setString(3, projectImageArr.get(x));
                pst.setInt(4, projectIdArr.get(x));
                pst.executeUpdate();
            } catch (SQLException err) {
                err.printStackTrace();
            }
        }

        for (int x = 0; x < projectIdArr.size(); x++) {
            String query = "UPDATE tbldeliverables SET projectTitle = ?, projectDetails = ?, projectImage = ? WHERE projectId = ?";
            System.out.println("Query: " + projectTitleArr.get(x) + ", " + projectDetailsArr.get(x) + ", "
                    + projectImageArr.get(x));

            try {
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, projectTitleArr.get(x));
                pst.setString(2, projectDetailsArr.get(x));
                pst.setString(3, projectImageArr.get(x));
                pst.setInt(4, projectIdArr.get(x));
                pst.executeUpdate();
            } catch (SQLException err) {
                err.printStackTrace();
            }
        }

        closeConnection();
    }

    // cursor functions
    static void cursorHand() {
        scene.setCursor(Cursor.HAND);
    }

    public static void cursorDefault() {
        scene.setCursor(Cursor.DEFAULT);
    }

    // scene functions
    static void sceneResizable(boolean bool) {
        Stage stage = (Stage) scene.getWindow();

        stage.setResizable(bool);
    }

    static void setSceneSize(int width, int height) throws IOException {
        Stage stage = (Stage) scene.getWindow();

        stage.setMinWidth(width);
        stage.setMinHeight(height);
        stage.sizeToScene();
    }

    static void setFullScreen() throws IOException {
        Stage stage = (Stage) scene.getWindow();

        stage.setFullScreen(true);
    }

    static void setMaximized(boolean bool) throws IOException {
        Stage stage = (Stage) scene.getWindow();

        stage.setMaximized(bool);
    }

    static void setResizable(boolean bool) throws IOException {
        Stage stage = (Stage) scene.getWindow();

        stage.setResizable(bool);
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    // stored variables
    static String getUserNow() {
        return userNow;
    }

    // queries
    static ResultSet getProjects() {
        ResultSet rs = null;
        if (userTypeNow == 0) {
            String query = "SELECT projectId, projectTitle, projectDetails, projectImage FROM tblprojectinfo";

            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
            } catch (SQLException err) {
                err.printStackTrace();
            }
        } else if (userTypeNow == 1) {
            String query = "SELECT title, id FROM tblprojects JOIN tbluserproject ON tblprojects.id = tbluserproject.idProject WHERE tbluserproject.idUser = ?";

            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, userId);
                rs = stmt.executeQuery();
            } catch (SQLException err) {
                err.printStackTrace();
            }
        }

        return rs;
    }

    static ResultSet getDeliverables() {
        ResultSet rs = null;
        if (userTypeNow == 0) {
            String query = "SELECT projectId, deliverableId, deliverableTitle, deliverableDetails, deliverableStatus, deliverableLead, deliverableDue FROM tbldeliverables ORDER BY projectId";
            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
            } catch (SQLException err) {
                err.printStackTrace();
            }
        } else if (userTypeNow == 1) {
            String query = "SELECT title, id FROM tblprojects JOIN tbluserproject ON tblprojects.id = tbluserproject.idProject WHERE tbluserproject.idUser = ?";

            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, userId);
                rs = stmt.executeQuery();
            } catch (SQLException err) {
                err.printStackTrace();
            }
        }

        return rs;
    }

    static void registerSQL(String fName, String lName, String email, String username, String password, int userType) {
        openConnection();

        String query = "INSERT INTO tbluseraccount (fname, lname, email, username, password, utype) Values (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, fName);
            stmt.setString(2, lName);
            stmt.setString(3, email);
            stmt.setString(4, username);
            stmt.setString(5, password);
            stmt.setInt(6, userType);

            int rows = stmt.executeUpdate();
            System.out.println("Rows Update: " + rows);
        } catch (SQLException err) {
            System.out.println(err);
        }

        closeConnection();
    }

    static boolean loginSQL(String username, String password) {
        openConnection();
        boolean logged = false;

        String query = "SELECT password FROM tbluseraccount WHERE username = ?";
        String hashedpassword = "";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                hashedpassword = rs.getString("password");
                return BCrypt.checkpw(password, hashedpassword);
            } else {
                return false;
            }

        } catch (SQLException err) {
            System.out.println(err);
        }

        closeConnection();
        return logged;
    }

    static void currentUser(String username) throws SQLException {
        String query = "SELECT id, utype  FROM tbluseraccount WHERE username = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        userId = rs.getInt("id");
        userTypeNow = rs.getInt("utype");
        userNow = username;
    }

    static boolean doesUNameExist(String username) {
        boolean exists = false;
        String query = "SELECT EXISTS(SELECT username FROM tbluseraccount WHERE username = ?  ) As 'exists'";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);

            try {
                ResultSet rs = stmt.executeQuery();
                rs.next();
                exists = rs.getBoolean("exists");
            } catch (SQLException e) {
                System.out.println(e);
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }

        return exists;
    }

    static int getNextProjectId() {
        openConnection();
        String query = "SELECT MAX(projectId) from project";
        int nextId = 0;

        try {
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                nextId = rs.getInt("projectId");
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }
        closeConnection();
        return nextId + 1;
    }

    static boolean checkExistEmail(String email) {
        boolean exists = false;
        String query = "SELECT EXISTS(SELECT email FROM tbluseraccount WHERE email = ?  ) As 'exists'";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);

            try {
                ResultSet rs = stmt.executeQuery();
                rs.next();
                exists = rs.getBoolean("exists");
            } catch (SQLException e) {
                System.out.println(e);
            }
        } catch (SQLException err) {
            System.out.println(err);
        }

        return exists;
    };

    public static void main(String[] args) {
        openConnection();
        closeConnection();
        launch();
    }

    public static void openConnection() {
        String url = "jdbc:mysql://localhost:3306/architask?useSSL=false";
        String user = "root";
        String password = "";

        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Unable to Connect! Closing the Program", "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.out.println("SQLException: " + ex.getMessage());
            return;
        }
    }

    public static void closeConnection() {
        try {
            conn.close();
        } catch (SQLException err) {
            err.printStackTrace();
        }
    }

    protected static void controlFocus(Control control) {
        control.focusedProperty().addListener((obs, unFocus, Focus) -> {
            if (Focus) {

            } else {
                if (control instanceof TextField) {
                    if (!((TextField) control).getText().isBlank()) {
                        control.setStyle("");
                    }
                } else if (control instanceof PasswordField) {
                    if (!((PasswordField) control).getText().isBlank()) {
                        control.setStyle("");
                    }
                } else if (control instanceof ChoiceBox) {
                    if (!(control == null) || !((String) ((ChoiceBox<?>) control).getValue()).isBlank()) {
                        control.setStyle("");
                    }
                }
            }
        });
    }

}
