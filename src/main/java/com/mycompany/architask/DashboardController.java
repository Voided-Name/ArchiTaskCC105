/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.architask;

import java.net.URL;
import java.util.ResourceBundle;
import java.sql.*;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TreeView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.TextBoundsType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextInputDialog;
import java.util.ArrayList;
import java.util.Optional;

/**
 * FXML Controller class
 *
 * @author Nash
 */
public class DashboardController implements Initializable {
    static TreeItem<String> projectListRoot = new TreeItem<String>("Root");
    static ArrayList<ArrayList<TreeItem<String>>> projectLeafs = new ArrayList<ArrayList<TreeItem<String>>>();
    static ArrayList<TreeItem<String>> projectLeafsBuffer = new ArrayList<TreeItem<String>>();
    static ArrayList<TreeItem<String>> projectBranches = new ArrayList<TreeItem<String>>();
    static ArrayList<Integer> projectIdArr = new ArrayList<Integer>();
    static ArrayList<String> projectTitleArr = new ArrayList<String>();
    static ArrayList<String> projectDetailsArr = new ArrayList<String>();
    static ArrayList<String> projectImageArr = new ArrayList<String>();
    static ArrayList<Integer> deliverableProjectIdArr = new ArrayList<Integer>();
    static ArrayList<String> deliverableDetailsArr = new ArrayList<String>();
    static ArrayList<String> deliverableTitleArr = new ArrayList<String>();

    // FXML IDs
    @FXML
    TreeView<String> projectList;
    @FXML
    ScrollPane mainScroll;
    @FXML
    Text projectTitle;
    @FXML
    Text projectDetails;
    @FXML
    TextArea projectDetailsArea;
    @FXML
    VBox mainVBox;
    @FXML
    AnchorPane mainAnchorPane;
    @FXML
    TableView<String> deliverTable;
    @FXML
    TableColumn<String, String> deliverCol;
    @FXML
    TableColumn<String, String> deliverStatusCol;
    @FXML
    TableColumn<String, String> deliverDueCol;
    @FXML
    TableColumn<String, String> deliverLeadCol;
    @FXML
    TableColumn<String, String> deliverArchsCol;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        uiSetup();
    }
    // <TableView fx:id="deliverTable" prefHeight="200.0" prefWidth="200.0">
    // <columns>
    // <TableColumn fx:id="deliverCol" prefWidth="68.79998779296875"
    // text="Deliverable" />
    // <TableColumn fx:id="deliverStatusCol" prefWidth="75.0" text="Status" />
    // <TableColumn fx:id="deliverDueCol" prefWidth="81.60004252195358" text="Due
    // Date" />
    // <TableColumn fx:id="deliverLeadCol" prefWidth="75.0" text="Lead" />
    // <TableColumn fx:id="deliverArchsCol" prefWidth="75.0" text="Architects" />
    // </columns>
    // </TableView>

    private void uiSetup() {
        deliverCol.prefWidthProperty().bind(deliverTable.widthProperty().divide(5));
        deliverStatusCol.prefWidthProperty().bind(deliverTable.widthProperty().divide(5));
        deliverArchsCol.prefWidthProperty().bind(deliverTable.widthProperty().divide(5));
        deliverLeadCol.prefWidthProperty().bind(deliverTable.widthProperty().divide(5));
        deliverDueCol.prefWidthProperty().bind(deliverTable.widthProperty().divide(5));
        mainAnchorPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.S && event.isControlDown()) {
                Alert confirmSave = new Alert(AlertType.CONFIRMATION, "Save Changes?");
                confirmSave.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        System.out.println("This Runs");
                        App.saveToSQL(projectIdArr, projectTitleArr, projectDetailsArr, projectImageArr);
                    }
                });

            }
        });
        projectList.setShowRoot(false);
        projectList.setRoot(projectListRoot);
        projectTitle.wrappingWidthProperty().bind(mainScroll.widthProperty().multiply(0.9));
        projectDetails.wrappingWidthProperty().bind(mainScroll.widthProperty().multiply(0.9));
        projectDetails.setBoundsType(TextBoundsType.VISUAL);
        projectDetailsArea.setWrapText(true);
        projectDetailsArea.prefWidthProperty().bind(projectDetails.wrappingWidthProperty());
        // projectDetailsArea.prefHeightProperty().bind(Bindings.createDoubleBinding(
        // () -> projectDetails.getLayoutBounds().getHeight(),
        // projectDetails.layoutBoundsProperty()));
        projectDetailsArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                int[] indexes = getSelectedIndex();
                int selectedIndex = indexes[1];

                mainVBox.getChildren().get(2).setVisible(true);
                mainVBox.getChildren().get(2).setManaged(true);
                mainVBox.getChildren().get(1).setVisible(false);
                mainVBox.getChildren().get(1).setManaged(false);
                projectDetails.setText(projectDetailsArea.getText());

                if (projectList.getSelectionModel().getSelectedItem().getParent() == projectListRoot) {
                    projectDetailsArr.set(selectedIndex, projectDetails.getText());
                } else {
                    deliverableDetailsArr.set(selectedIndex, projectDetails.getText());
                }
            }
        });
        mainVBox.getChildren().get(1).setVisible(false);
        mainVBox.getChildren().get(1).setManaged(false);

        setupProjectTreeView();
    }

    private void setupProjectTreeView() {
        ResultSet rs = App.getProjects();
        int projectId, deliverableProjectId;
        String projectTitle, projectDetails, projectImage, deliverableTitle, deliverableDetails;

        try {
            while (rs.next()) {
                // geting current project, deliverable, and task information
                projectId = rs.getInt("projectId");
                projectTitle = rs.getString("projectTitle");
                projectDetails = rs.getString("projectDetails");
                projectImage = rs.getString("projectImage");

                // parallel arrays for reference
                projectTitleArr.add(projectTitle);
                projectIdArr.add(projectId);
                projectDetailsArr.add(projectDetails);
                projectImageArr.add(projectDetails);

                projectBranches.add(new TreeItem<String>(projectTitle));
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }

        for (TreeItem<String> projectBranch : projectBranches) {
            projectListRoot.getChildren().add(projectBranch);
            System.out.println("Added to projectListRoot: " + projectBranch);
        }

        rs = App.getDeliverables();
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            System.out.println(rsmd.getColumnCount());

            while (rs.next()) {
                deliverableProjectId = rs.getInt("projectId");
                deliverableTitle = rs.getString("deliverableTitle");
                deliverableDetails = rs.getString("deliverableDetails");
                deliverableProjectIdArr.add(deliverableProjectId);
                deliverableTitleArr.add(deliverableTitle);
                deliverableDetailsArr.add(deliverableDetails);
                System.out.println("Deliverable Title: " + deliverableTitle);
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }

        int currentProjectId = deliverableProjectIdArr.get(0);
        System.out.println("Current Project Id: " + currentProjectId);
        for (int x = 0; x < deliverableProjectIdArr.size(); x++) {
            System.out.println(
                    "Deliverable Project Id: " + deliverableProjectIdArr + ", Current Id: " + currentProjectId);
            if (deliverableProjectIdArr.get(x) == currentProjectId) {
                projectLeafsBuffer.add(new TreeItem<String>(deliverableTitleArr.get(x)));
                System.out.println("Added to Project Leafs Buffer " + deliverableTitleArr.get(x));
            } else {
                for (TreeItem<String> projectLeaf : projectLeafsBuffer) {
                    projectBranches.get(projectIdArr.indexOf(currentProjectId)).getChildren().add(projectLeaf);
                }
                projectLeafsBuffer.clear();
                projectLeafsBuffer.add(new TreeItem<String>(deliverableTitleArr.get(x)));
                currentProjectId = deliverableProjectIdArr.get(x);
            }
        }
        for (TreeItem<String> projectLeaf : projectLeafsBuffer) {
            projectBranches.get(projectIdArr.indexOf(currentProjectId)).getChildren().add(projectLeaf);
        }

        projectList.getSelectionModel().selectFirst();
        selectProject();

    }

    @FXML
    private void cursorHand() {
        App.cursorHand();
    }

    @FXML
    private void cursorDefault() {
        App.cursorDefault();
    }

    @FXML
    private void projectAdd() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("New Project");
        dialog.setHeaderText(null);
        dialog.setContentText("Name:");
        dialog.getDialogPane().setStyle("-fx-background-color: white;-fx-border-color: #E8B631");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(inpt -> addProject(inpt));
    }

    private void addProject(String projectName) {
        for (TreeItem<String> project : projectListRoot.getChildren()) {
            String projectStr = project.getValue();
            if (projectStr.toLowerCase().equals(projectName.toLowerCase())) {
                Alert alertExistProject = new Alert(AlertType.WARNING);
                alertExistProject.setTitle("Project Not Created");
                alertExistProject.setHeaderText(null);
                alertExistProject.setContentText("Project Already Exists!");
                alertExistProject.showAndWait();
                return;
            }
        }
        projectListRoot.getChildren().add(new TreeItem<String>(projectName));
    }

    private void changeProjectName(String projectName) {
        TreeItem<String> selectedProject = projectList.getSelectionModel().getSelectedItem();
        int[] indexes = getSelectedIndex();

        TreeItem<String> selectedProjectParent = selectedProject.getParent();
        if (selectedProjectParent == projectListRoot) {
            for (TreeItem<String> project : projectListRoot.getChildren()) {
                String projectStr = project.getValue();
                if (projectStr.toLowerCase().equals(projectName.toLowerCase())) {
                    if (!projectStr.equals(selectedProject.getValue())) {
                        Alert alertExistProject = new Alert(AlertType.WARNING);
                        alertExistProject.setTitle("Project Not Created");
                        alertExistProject.setHeaderText(null);
                        alertExistProject.setContentText("Project Already Exists!");
                        alertExistProject.showAndWait();
                        return;
                    }
                }
            }
        } else {
            for (TreeItem<String> project : selectedProjectParent.getChildren()) {
                String projectStr = project.getValue();
                if (projectStr.toLowerCase().equals(projectName.toLowerCase())) {
                    if (!projectStr.equals(selectedProject.getValue())) {
                        Alert alertExistProject = new Alert(AlertType.WARNING);
                        alertExistProject.setTitle("Project Not Created");
                        alertExistProject.setHeaderText(null);
                        alertExistProject.setContentText("Deliverable Already Exists!");
                        alertExistProject.showAndWait();
                        return;
                    }
                }
            }
        }

        if (indexes[0] == -1) {
            projectTitleArr.set(indexes[1], projectName);
        } else {
            deliverableTitleArr.set(indexes[1], projectName);
        }

        selectedProject.setValue(projectName);
        projectViewUpdate();
    }

    private void projectViewUpdate() {
        projectTitle.setText(projectList.getSelectionModel().getSelectedItem().getValue());
    }

    @FXML
    private void selectProject() {
        int[] indexes = getSelectedIndex();
        TreeItem<String> selected = projectList.getSelectionModel().getSelectedItem();
        int selectedIndexParent = indexes[0];
        int selectedIndex = indexes[1];

        if (selectedIndexParent == -1) {
            projectTitle.setText(selected.getValue());
            projectDetails.setText(projectDetailsArr.get(selectedIndex));
        } else {
            projectTitle.setText(selected.getValue());
            projectDetails.setText(getSelectedDeliverableDetails(selectedIndex, selectedIndexParent));
        }

    }

    private int[] getSelectedIndex() {
        TreeItem<String> selected = projectList.getSelectionModel().getSelectedItem();
        TreeItem<String> selectedParent = new TreeItem<String>();
        int[] indexes = new int[2];
        int selectedIndex = 0;
        int selectedIndexParent = 0;

        if (selected.getParent() == projectListRoot) {
            for (TreeItem<String> projectTreeItem : projectListRoot.getChildren()) {
                if (projectTreeItem == selected) {
                    break;
                }
                selectedIndex++;
            }
            projectTitle.setText(selected.getValue());
            projectDetails.setText(projectDetailsArr.get(selectedIndex));
            indexes[0] = -1;
            indexes[1] = selectedIndex;
            return indexes;
        } else {
            selectedParent = selected.getParent();
            for (TreeItem<String> projectTreeItem : projectListRoot.getChildren()) {
                if (projectTreeItem == selectedParent) {
                    break;
                }
                selectedIndexParent++;
            }
            for (TreeItem<String> deliverableTreeItem : selectedParent.getChildren()) {
                if (deliverableTreeItem == selected) {
                    break;
                }
                selectedIndex++;
            }
            projectTitle.setText(selected.getValue());
            projectDetails.setText(getSelectedDeliverableDetails(selectedIndex, selectedIndexParent));
            indexes[0] = selectedIndexParent;
            indexes[1] = selectedIndex;
            return indexes;
        }
    }

    private String getSelectedDeliverableDetails(int selectedIndex, int parentIndex) {
        int projectId = projectIdArr.get(parentIndex);

        int index = 0;
        for (int x = 0; x < deliverableTitleArr.size(); x++) {
            if (projectId == deliverableProjectIdArr.get(x)) {
                if (index == selectedIndex) {
                    return deliverableDetailsArr.get(x);
                } else {
                    index++;
                }
            }
        }

        return "";
    }

    @FXML
    private void editTitleDouble(MouseEvent event) {
        if (event.getClickCount() == 2) {
            TreeItem<String> selectedProject = projectList.getSelectionModel().getSelectedItem();

            TreeItem<String> selectedProjectParent = selectedProject.getParent();
            if (selectedProjectParent == projectListRoot) {
                TextInputDialog dialog = new TextInputDialog(projectTitle.getText());
                dialog.setTitle("Change Project Name");
                dialog.setHeaderText(null);
                dialog.setContentText("Project Name:");
                dialog.getDialogPane().setStyle("-fx-background-color: white;-fx-border-color: #E8B631");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(inpt -> changeProjectName(inpt));
            } else {
                TextInputDialog dialog = new TextInputDialog(projectTitle.getText());
                dialog.setTitle("Change Deliverable Name");
                dialog.setHeaderText(null);
                dialog.setContentText("Deliverable Name:");
                dialog.getDialogPane().setStyle("-fx-background-color: white;-fx-border-color: #E8B631");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(inpt -> changeProjectName(inpt));
            }
        }
    }

    @FXML
    private void editProjectDetails(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Double layoutHeight = projectDetails.getLayoutBounds().getHeight();
            System.out.println("Project Details: " + projectDetails.getLayoutBounds().getHeight());
            System.out.println("Project Area: " + projectDetailsArea.getHeight());
            mainVBox.getChildren().get(2).setVisible(false);
            mainVBox.getChildren().get(2).setManaged(false);
            mainVBox.getChildren().get(1).setVisible(true);
            mainVBox.getChildren().get(1).setManaged(true);

            projectDetailsArea.setText(projectDetails.getText());
            projectDetailsArea.setMinHeight(layoutHeight + 20);
            projectDetailsArea.setMaxHeight(layoutHeight + 20);
            System.out.println("Project Details: " + projectDetails.getLayoutBounds().getHeight());
            System.out.println("Project Area: " + projectDetailsArea.getHeight());
            projectDetailsArea.requestFocus();
        }

    }

}
