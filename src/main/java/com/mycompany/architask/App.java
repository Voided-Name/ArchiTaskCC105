package com.mycompany.architask;

import javafx.application.Application;
import java.util.Vector;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.*;
import javax.swing.JOptionPane;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
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
    private static String userNow = "NaeBerber";
    private static int userTypeNow = 0;
    private static int userId = 1;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("login"), 600, 400);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        stage.getIcons()
                .add(new Image(App.class.getResourceAsStream("/img/logo_2.jpg")));
        stage.setMaximized(false);
    }

    public static int getUserType() {
        return userTypeNow;
    }

    // cursor functions
    static void cursorHand() {
        scene.setCursor(Cursor.HAND);
    }

    public static Vector<ArrayList<Object>> getArchNames() {
        // TODO check access to project first
        Vector<ArrayList<Object>> archNames = new Vector<>();
        openConnection();
        try {
            String query = "SELECT id, fname, lname, username FROM tbluseraccount";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                ArrayList<Object> buffer = new ArrayList<>();
                buffer.add(rs.getInt("id"));
                buffer.add(rs.getString("fname"));
                buffer.add(rs.getString("lname"));
                buffer.add(rs.getString("username"));

                archNames.add(buffer);
            }
            closeConnection();
        } catch (SQLException err) {
            err.printStackTrace();
        }
        return archNames;
    }

    public static void updateDelDue(java.sql.Date due, int delId) {
        openConnection();
        String query = "UPDATE tbldeliverables SET deliverableDue = ? WHERE deliverableId = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setDate(1, due);
            pst.setInt(2, delId);
            int x = pst.executeUpdate();
            System.out.println("Num of affected rows: " + x + " delId: " + delId);
        } catch (SQLException err) {
            err.printStackTrace();
        }
        closeConnection();
    }

    public static void updateProjectDue(java.sql.Date due, int projectId) {
        openConnection();
        String query = "UPDATE tblprojectinfo SET projectDue = ? WHERE projectId = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setDate(1, due);
            pst.setInt(2, projectId);
            int x = pst.executeUpdate();
        } catch (SQLException err) {
            err.printStackTrace();
        }
        closeConnection();
    }

    public static void updateDelStatus(String delStatus, int delId) {
        openConnection();
        String query = "UPDATE tbldeliverables SET deliverableStatus = ? WHERE deliverableId = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, delStatus);
            pst.setInt(2, delId);
            int x = pst.executeUpdate();
        } catch (SQLException err) {
            err.printStackTrace();
        }
        closeConnection();
    }

    public static void updateTaskStatus(String taskStatus, int taskId) {
        openConnection();
        String query = "UPDATE tbltasks SET status = ? WHERE taskId = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, taskStatus);
            pst.setInt(2, taskId);
            int x = pst.executeUpdate();
        } catch (SQLException err) {
            err.printStackTrace();
        }
        closeConnection();
    }

    public static void updateTaskDue(java.sql.Date taskDue, int taskId) {
        openConnection();
        String query = "UPDATE tbltasks SET taskDue = ? WHERE taskId = ?";
        System.out.println(query);
        System.out.println(taskDue);
        System.out.println(taskId);
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setDate(1, taskDue);
            pst.setInt(2, taskId);
            int x = pst.executeUpdate();
        } catch (SQLException err) {
            err.printStackTrace();
        }
        closeConnection();
    }

    public static void updateDelLead(int leadId, int delId) {
        openConnection();
        String query = "UPDATE tbldeliverables SET deliverableLead = ? WHERE deliverableId = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, leadId);
            pst.setInt(2, delId);
            int x = pst.executeUpdate();
        } catch (SQLException err) {
            err.printStackTrace();
        }
        closeConnection();
    }

    public static void archiveProject(int projectId) {
        openConnection();
        try {
            System.out.println("Archive Project id : " + projectId);
            ArrayList<Integer> deliverableIds = new ArrayList<>();
            String query = "SELECT deliverableId FROM tbldeliverables WHERE projectid = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, projectId);
            ResultSet rsSelect = pst.executeQuery();

            while (rsSelect.next()) {
                archiveDeliverable(rsSelect.getInt("deliverableId"));
            }

            if (conn.isClosed()) {
                openConnection();
            }

            query = "SELECT * FROM tblprojectinfo WHERE projectid = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, projectId);
            rsSelect = pst.executeQuery();
            rsSelect.next();
            String projectTitle = rsSelect.getString("projectTitle");
            String projectDetails = rsSelect.getString("projectDetails");
            String projectImage = rsSelect.getString("projectImage");
            java.sql.Date projectDue = rsSelect.getDate("projectDue");

            query = "DELETE FROM tblprojectinfo WHERE projectid = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, projectId);
            pst.executeUpdate();

            query = "INSERT INTO tblprojectinfoarchive (projectid, projecttitle, projectdetails, projectimage, projectdue) VALUES (?, ?, ?, ?, ?)";
            pst = conn.prepareStatement(query);
            pst.setInt(1, projectId);
            pst.setString(2, projectTitle);
            pst.setString(3, projectDetails);
            pst.setString(4, projectImage);
            pst.setDate(5, projectDue);
            pst.executeUpdate();
        } catch (SQLException err) {
            err.printStackTrace();
        }
        closeConnection();
    }

    public static String getLead(int projectLeadId) {
        if (projectLeadId == 0) {
            return "";
        }

        String lname = "";
        String fname = "";
        String lead = "";

        openConnection();
        try {
            String query = "SELECT lname, fname FROM tbluseraccount WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, projectLeadId);
            ResultSet rs = pst.executeQuery();
            rs.next();
            lname = rs.getString("lname");
            fname = rs.getString("fname");
            lead = lname + ", " + fname;
        } catch (SQLException err) {
            err.printStackTrace();
        }
        closeConnection();

        return lead;
    }

    public static void cursorDefault() {
        scene.setCursor(Cursor.DEFAULT);
    }

    public static int addTask(int deliverableId, String taskName) {
        int taskId = 0;
        openConnection();
        String query = "INSERT INTO tbltasks (deliverableId, taskTitle, taskDetails, status, taskDue) VALUES (?,?,'Double Click to Edit', 'TODO', NOW())";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, deliverableId);
            pst.setString(2, taskName);
            pst.executeUpdate();
        } catch (SQLException err) {
            err.printStackTrace();
        }
        query = "SELECT MAX(taskId) AS maxid FROM tbltasks";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            rs.next();
            taskId = rs.getInt("maxid");

        } catch (SQLException err) {
            err.printStackTrace();
        }
        closeConnection();
        return taskId;
    }

    public static int addDeliverable(int projectId, String deliverableName) {
        int deliverId = 0;
        openConnection();
        String query = "INSERT INTO tbldeliverables (projectid, deliverabletitle, deliverableDetails,deliverableStatus, deliverableDue, deliverablelead) VALUES (?,?,'Double Click to Edit', 'TODO', NOW(), 0)";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, projectId);
            pst.setString(2, deliverableName);
            pst.executeUpdate();
        } catch (SQLException err) {
            err.printStackTrace();
        }
        query = "SELECT MAX(deliverableid) AS maxid FROM tbldeliverables";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            rs.next();
            deliverId = rs.getInt("maxid");

        } catch (SQLException err) {
            err.printStackTrace();
        }
        closeConnection();
        return deliverId;
    }

    public static int newProject(String projectName) {
        openConnection();
        int newId = 0;

        String query = "INSERT INTO tblprojectinfo (projectTitle, projectDetails, projectImage, projectDue) VALUES (?, 'Double Click to Edit', '', NOW())";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, projectName);
            pst.executeUpdate();
        } catch (SQLException err) {
            err.printStackTrace();
        }

        query = "SELECT MAX(projectId) as maxid FROM tblprojectinfo";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            rs.next();
            newId = rs.getInt("maxid");
        } catch (SQLException err) {
            err.printStackTrace();
        }

        closeConnection();
        return newId;
    }

    public static void archiveTask(int id) {
        openConnection();
        try {
            String query = "SELECT * FROM tbltasks WHERE taskId = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            rs.next();

            int taskId = rs.getInt("taskId");
            int taskDeliverableId = rs.getInt("deliverableId");
            String taskTitle = rs.getString("taskTitle");
            String taskDetails = rs.getString("taskDetails");
            String taskStatus = rs.getString("status");
            java.sql.Date taskDue = rs.getDate("taskDue");

            query = "DELETE FROM tbltasks WHERE taskId = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            pst.executeUpdate();

            query = "INSERT INTO tbltasksarchive (taskId, deliverableId, taskTitle, taskDetails, status, taskDue ) VALUES (?, ?, ?, ?, ?, ?)";
            pst = conn.prepareStatement(query);
            pst.setInt(1, taskId);
            pst.setInt(2, taskDeliverableId);
            pst.setString(3, taskTitle);
            pst.setString(4, taskDetails);
            pst.setString(5, taskStatus);
            pst.setDate(6, taskDue);
            pst.executeUpdate();
        } catch (SQLException err) {
            err.printStackTrace();
        }
        closeConnection();
    }

    public static void archiveDeliverable(int id) {
        openConnection();
        try {
            String query = "SELECT taskId FROM tbltasks WHERE deliverableId = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rsSelect = pst.executeQuery();

            while (rsSelect.next()) {
                archiveTask(rsSelect.getInt("taskId"));
            }

            if (conn.isClosed()) {
                openConnection();
            }

            System.out.println("Id sent from dashboard: " + id);
            query = "SELECT * FROM tbldeliverables WHERE deliverableId = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            rs.next();

            int deliverableId = rs.getInt("deliverableId");
            String deliverableTitle = rs.getString("deliverableTitle");
            String deliverableStatus = rs.getString("deliverableStatus");
            int deliverableProjectId = rs.getInt("projectId");
            String deliverableDetails = rs.getString("deliverableDetails");
            int deliverableLead = rs.getInt("deliverableLead");
            java.sql.Date deliverableDue = rs.getDate("deliverableDue");

            query = "DELETE FROM tbldeliverables WHERE deliverableId = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            pst.executeUpdate();

            query = "INSERT INTO tbldeliverablesarchive (deliverableId, projectId, deliverableTitle, deliverableDetails, deliverableStatus, deliverableLead, deliverableDue) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pst = conn.prepareStatement(query);
            pst.setInt(1, deliverableId);
            pst.setInt(2, deliverableProjectId);
            pst.setString(3, deliverableTitle);
            pst.setString(4, deliverableDetails);
            pst.setString(5, deliverableStatus);
            pst.setInt(6, deliverableLead);
            pst.setDate(7, deliverableDue);
            pst.executeUpdate();
        } catch (SQLException err) {
            err.printStackTrace();
        }
        closeConnection();
    }

    public static void editProjectTitle(int projectId, String projectName) {
        openConnection();
        String query = "Update tblprojectinfo SET projectTitle = ? WHERE projectId = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, projectName);
            pst.setInt(2, projectId);
            pst.executeUpdate();
        } catch (SQLException err) {
            err.printStackTrace();
        }
        closeConnection();
    }

    public static ArrayList<String> getChats(int taskId) {
        ArrayList<String> messages = new ArrayList<>();
        openConnection();
        String query = "SELECT message FROM tbltaskschats WHERE taskId = ? ORDER BY id";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, taskId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                messages.add(rs.getString("message"));
            }
        } catch (SQLException err) {
        }
        closeConnection();
        return messages;
    }

    public static void addChat(int taskId, String chat) {
        openConnection();
        String query = "INSERT INTO tbltaskschats (taskId, message) VALUES (?, ?)";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, taskId);
            pst.setString(2, chat);
            pst.executeUpdate();
        } catch (SQLException err) {
        }
        closeConnection();
    }

    public static void editProjectImage(int projectId, String imageName) {
        openConnection();
        String query = "Update tblprojectinfo SET projectImage = ? WHERE projectId = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, imageName);
            pst.setInt(2, projectId);
            pst.executeUpdate();
        } catch (SQLException err) {
            err.printStackTrace();
        }
        closeConnection();
    }

    public static void editDeliverableDetails(int deliverableId, String deliverableDetail) {
        openConnection();
        System.out.println(deliverableId);
        System.out.println(deliverableDetail);
        String query = "UPDATE tbldeliverables SET deliverableDetails = ? WHERE deliverableId = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, deliverableDetail);
            pst.setInt(2, deliverableId);
            pst.executeUpdate();
        } catch (SQLException err) {
            err.printStackTrace();
        }
        closeConnection();
    }

    public static void editTaskDetails(int taskId, String taskDetails) {
        openConnection();
        String query = "UPDATE tbltasks SET taskDetails = ? WHERE taskId = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, taskDetails);
            pst.setInt(2, taskId);
            pst.executeUpdate();
        } catch (SQLException err) {
            err.printStackTrace();
        }
        closeConnection();
    }

    public static void editDeliverableTitle(int deliverableId, String deliverableName) {
        openConnection();
        System.out.println(deliverableId);
        System.out.println(deliverableName);
        String query = "UPDATE tbldeliverables SET deliverableTitle = ? WHERE deliverableId = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, deliverableName);
            pst.setInt(2, deliverableId);
            pst.executeUpdate();
        } catch (SQLException err) {
            err.printStackTrace();
        }
        closeConnection();
    }

    public static void editTaskTitle(int taskId, String taskName) {
        openConnection();
        String query = "UPDATE tbltasks SET taskTitle = ? WHERE taskId = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, taskName);
            pst.setInt(2, taskId);
            pst.executeUpdate();
        } catch (SQLException err) {
            err.printStackTrace();
        }
        closeConnection();
    }

    public static void editProjectDetails(int projectId, String projectDetails) {
        openConnection();
        System.out.println("Project id: " + projectId);
        String query = "Update tblprojectinfo SET projectDetails = ? WHERE projectId = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, projectDetails);
            pst.setInt(2, projectId);
            pst.executeUpdate();
        } catch (SQLException err) {
            err.printStackTrace();
        }
        closeConnection();
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

    public static void adminEdit(int userId, int userType) {
        openConnection();
        String query = "UPDATE tbluseraccount SET utype = ? WHERE id = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, userType);
            pst.setInt(2, userId);
            pst.executeUpdate();
        } catch (SQLException err) {
            err.printStackTrace();
        }
        closeConnection();
    }

    public static void adminArchive(int userId) {
        openConnection();
        String query = "DELETE FROM tbluseraccount WHERE id = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, userId);
            pst.executeUpdate();
        } catch (SQLException err) {
            err.printStackTrace();
        }
        closeConnection();
    }

    public static ArrayList<ArrayList<String>> adminTableValues() {
        ArrayList<ArrayList<String>> cells = new ArrayList<>();
        openConnection();
        String query = "SELECT  id, fname, lname, email, username, utype FROM tbluseraccount";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                ArrayList<String> buffer = new ArrayList<>();
                buffer.add(Integer.toString(rs.getInt("id")));
                buffer.add(rs.getString("username"));
                buffer.add(rs.getString("fname"));
                buffer.add(rs.getString("lname"));
                buffer.add(rs.getString("email"));
                buffer.add(Integer.toString(rs.getInt("utype")));
                cells.add(buffer);
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }
        closeConnection();
        return cells;
    }

    // queries
    static ResultSet getProjects() {
        openConnection();
        ResultSet rs = null;
        String query = "SELECT projectId, projectTitle, projectDetails, projectImage, projectDue FROM tblprojectinfo";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
        } catch (SQLException err) {
            err.printStackTrace();
        }

        return rs;
    }

    static ResultSet getDeliverables() {
        ResultSet rs = null;
        String query = "SELECT projectId, deliverableId, deliverableTitle, deliverableDetails, deliverableStatus, deliverableLead, deliverableDue FROM tbldeliverables ORDER BY projectId";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
        } catch (SQLException err) {
            err.printStackTrace();
        }

        return rs;
    }

    static ResultSet getTasks() {
        openConnection();
        ResultSet rs = null;
        String query = "SELECT taskId, deliverableId, taskTitle, taskDetails, status, taskDue FROM tbltasks ORDER BY deliverableId";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
        } catch (SQLException err) {
            err.printStackTrace();
        }

        return rs;
    }

    static void sendQr(String qrData) {
        System.out.println(qrData);
        openConnection();
        String query = "SELECT projectId FROM tblprojectinfo WHERE projectQr = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, qrData);
            ResultSet rs = pst.executeQuery();
            if (rs.isBeforeFirst()) {
                try {
                    setRoot("dashboard");
                    userTypeNow = 3;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }

        closeConnection();
    }

    static void setQr(int projectId, String qrData) {
        System.out.println(projectId);
        System.out.println(qrData);
        openConnection();
        String query = "UPDATE TABLE tblprojectinfo SET projectQr = ? WHERE = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, qrData);
            pst.setInt(2, projectId);
            pst.executeUpdate();
        } catch (SQLException err) {
            err.printStackTrace();
        }
        closeConnection();
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
        openConnection();
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

        closeConnection();
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
                        control.setStyle(
                                "-fx-border-color: #0a2342;-fx-background-color: transparent;-fx-border-width: 0 0 1 0;");
                    }
                } else if (control instanceof PasswordField) {
                    if (!((PasswordField) control).getText().isBlank()) {
                        control.setStyle(
                                "-fx-border-color: #0a2342;-fx-background-color: transparent;-fx-border-width: 0 0 1 0;");
                    }
                } else if (control instanceof ChoiceBox) {
                    if (!(control == null) || !((String) ((ChoiceBox<?>) control).getValue()).isBlank()) {
                        control.setStyle(
                                "-fx-border-color: #0a2342;-fx-background-color: transparent;-fx-border-width: 0 0 1 0;");
                    }
                }
            }
        });
    }

}
