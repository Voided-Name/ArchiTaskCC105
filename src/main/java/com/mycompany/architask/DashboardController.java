/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.architask;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.sql.*;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.control.TreeView;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView;
import javafx.scene.control.Dialog;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TableColumn;
import javafx.stage.Popup;
import java.util.Vector;
import javafx.util.Callback;
import javafx.scene.control.TextInputDialog;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.binding.Bindings;
import java.util.ArrayList;
import java.time.LocalDate;
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
    static ArrayList<Integer> deliverableIdArr = new ArrayList<Integer>();
    static ArrayList<String> deliverableTitleArr = new ArrayList<String>();
    static ArrayList<String> deliverableDetailsArr = new ArrayList<String>();
    static ArrayList<String> deliverableStatusArr = new ArrayList<String>();
    static ArrayList<String> deliverableLeadArr = new ArrayList<String>();
    static ArrayList<Integer> deliverableLeadIdArr = new ArrayList<Integer>();
    static ArrayList<java.sql.Date> deliverableDueArr = new ArrayList<java.sql.Date>();
    static ArrayList<String> deliverableArchsArr = new ArrayList<String>();
    static ArrayList<Integer> newProjectIds = new ArrayList<Integer>();
    static ObservableList<String[]> data = FXCollections.observableArrayList();
    static boolean gotNextId = false;
    static int nextProjectId = 0;
    static int[] prevSelected;
    static boolean prevSelectedPrimed = false;
    static ArrayList<Integer> taskDeliverableIdArr = new ArrayList<Integer>();
    static ArrayList<Integer> taskIdArr = new ArrayList<Integer>();
    static ArrayList<String> taskTitleArr = new ArrayList<String>();
    static ArrayList<String> taskDetailsArr = new ArrayList<String>();
    static ArrayList<String> taskStatusArr = new ArrayList<String>();
    static ArrayList<java.sql.Date> taskDueArr = new ArrayList<java.sql.Date>();

    // FXML IDs
    @FXML
    TreeView<String> projectList;
    @FXML
    ScrollPane mainScroll;
    @FXML
    Text projectTitle;
    @FXML
    Label projectDetails;
    @FXML
    TextArea projectDetailsArea;
    @FXML
    VBox mainVBox;
    @FXML
    AnchorPane mainAnchorPane;
    @FXML
    TableView<String[]> deliverTable;
    @FXML
    TableColumn<String[], String> deliverId;
    @FXML
    TableColumn<String[], String> deliverCol;
    @FXML
    TableColumn<String[], String> deliverStatusCol;
    @FXML
    TableColumn<String[], String> deliverDueCol;
    @FXML
    TableColumn<String[], String> deliverLeadCol;
    @FXML
    Label lblDue;
    @FXML
    Label lblLead;
    @FXML
    Rectangle line;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        uiSetup();
        projectDetails.setWrapText(true);
        // try {
        // App.setResizable(true);
        // App.setMaximized(true);
        // } catch (IOException err) {
        // err.printStackTrace();
        // }
    }

    private void archiveTask() {
        int deltableIndex = 0;
        int id = 0;
        int index = 0;

        deltableIndex = deliverTable.getSelectionModel().getSelectedIndex();
        id = Integer.parseInt((data.get(deltableIndex))[0]);
        App.archiveTask(id);
        index = taskIdArr.indexOf(id);

        taskDeliverableIdArr.remove(index);
        taskIdArr.remove(index);
        taskTitleArr.remove(index);
        taskDetailsArr.remove(index);
        taskStatusArr.remove(index);
        taskDueArr.remove(index);

        refreshTable();
    }

    private void archiveDeliverable(int treeIndex) {
        TreeItem<String> selectedProject = projectList.getSelectionModel().getSelectedItem();
        int deltableIndex = 0;
        int id = 0;
        int index = 0;

        if (treeIndex == -1) {
            selectedProject = projectList.getSelectionModel().getSelectedItem();
            deltableIndex = deliverTable.getSelectionModel().getSelectedIndex();
            id = Integer.parseInt((data.get(deltableIndex))[0]);
            App.archiveDeliverable(id);
            index = deliverableIdArr.indexOf(id);
        } else {
            id = deliverableIdArr.get(treeIndex);
            App.archiveDeliverable(deliverableIdArr.get(treeIndex));
            index = treeIndex;
        }

        int delIndexInArrs = 0;
        for (TreeItem<String> project : projectListRoot.getChildren()) {
            if (project == selectedProject) {
                for (int x = 0; x < project.getChildren().size(); x++) {
                    if (deliverableIdArr.get(delIndexInArrs) == id) {
                        project.getChildren().remove(x);
                    }
                    delIndexInArrs++;
                }
                break;
            }
            delIndexInArrs = delIndexInArrs + project.getChildren().size();
        }

        deliverableProjectIdArr.remove(index);
        deliverableIdArr.remove(index);
        deliverableTitleArr.remove(index);
        deliverableDetailsArr.remove(index);
        deliverableStatusArr.remove(index);
        deliverableLeadIdArr.remove(index);
        deliverableLeadArr.remove(index);
        deliverableDueArr.remove(index);

        if (treeIndex == -1) {
            refreshTable();
        }
    }

    private void contextMenuSetup() {
        ContextMenu mItableMenu = new ContextMenu();
        MenuItem mIadd = new MenuItem("Add");
        MenuItem mIdelete = new MenuItem("Delete");
        MenuItem mIedit = new MenuItem("Edit");
        mItableMenu.getItems().addAll(mIadd, mIdelete, mIedit);
        deliverTable.setContextMenu(mItableMenu);

        mIedit.setOnAction((event) -> {
            Alert dialog = new Alert(AlertType.CONFIRMATION, "Open Deliverable");
            dialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    int deltableIndex = deliverTable.getSelectionModel().getSelectedIndex();
                    int id = Integer.parseInt((data.get(deltableIndex))[0]);
                    TreeItem<String> selectedProject = projectList.getSelectionModel().getSelectedItem();
                    int delIndexInArrs = 0;

                    for (TreeItem<String> project : projectListRoot.getChildren()) {
                        if (project == selectedProject) {
                            for (int x = 0; x < project.getChildren().size(); x++) {
                                if (deliverableIdArr.get(delIndexInArrs) == id) {
                                    projectList.getSelectionModel().select(project.getChildren().get(x));
                                }
                                delIndexInArrs++;
                            }
                            break;
                        }
                        delIndexInArrs = delIndexInArrs + project.getChildren().size();
                    }
                    selectProject();
                }
            });
        });

        mIdelete.setOnAction((event) -> {
            TreeItem<String> selected = projectList.getSelectionModel().getSelectedItem();
            Alert dialog = new Alert(AlertType.CONFIRMATION, "");

            if (selected.getParent() == projectListRoot) {
                dialog.setTitle("New Deliverable");
            } else {
                dialog.setTitle("New Task");
            }

            dialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK && deliverTable.getSelectionModel().getSelectedIndex() != -1) {
                    if (selected.getParent() == projectListRoot) {
                        archiveDeliverable(-1);
                    } else {
                        archiveTask();
                    }
                }
            });
        });

        mIadd.setOnAction((event) -> {
            TreeItem<String> selected = projectList.getSelectionModel().getSelectedItem();
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setHeaderText(null);
            dialog.setContentText("Name:");
            dialog.getDialogPane().setStyle("-fx-background-color: white;-fx-border-color: #E8B631");

            if (selected.getParent() == projectListRoot) {
                dialog.setTitle("New Deliverable");
            } else {
                dialog.setTitle("New Task");
            }

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(inpt -> addToTable(inpt));
        });
    }

    private void tableSetup() {
        deliverCol.prefWidthProperty().bind(deliverTable.widthProperty().divide(4));
        Label tableTooltipDeliver = new Label("");
        tableTooltipDeliver.setTooltip(new Tooltip("Right-Click to Edit Table"));
        deliverCol.setGraphic(tableTooltipDeliver);
        deliverStatusCol.prefWidthProperty().bind(deliverTable.widthProperty().divide(4));
        deliverLeadCol.prefWidthProperty().bind(deliverTable.widthProperty().divide(4));
        deliverDueCol.prefWidthProperty().bind(deliverTable.widthProperty().divide(4));
        deliverId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));
        deliverCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));
        deliverStatusCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2]));
        deliverDueCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[3]));
        deliverLeadCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[4]));
        deliverTable.setItems(data);
        deliverTable.setMinHeight(200);
        deliverTable.setFocusTraversable(false);
    }

    // not yet fully functional
    private void colorFolding() {
        projectList.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
            @Override
            public TreeCell<String> call(TreeView<String> param) {
                return new TreeCell<String>() {
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setStyle("");
                        } else {
                            setText(item);
                            TreeItem<String> currentItem = getTreeItem();
                            int depth = getDepth(currentItem) - 1;
                            switch (depth) {
                                case 0:
                                    getStyleClass().add("project");
                            }
                        }
                    }

                    private int getDepth(TreeItem<String> item) {
                        int depth = 0;
                        while (item != null && item.getParent() != null) {
                            depth++;
                            item = item.getParent();
                        }
                        return depth;
                    }
                };

            }
        });
    }

    private void uiSetup() {
        contextMenuSetup();
        tableSetup();

        // VBox min height
        mainVBox.minHeightProperty()
                .bind(Bindings.createDoubleBinding(
                        () -> projectDetailsArea.getHeight() + projectTitle.getLayoutBounds().getHeight()
                                + deliverTable.getHeight(),
                        projectDetailsArea.heightProperty(),
                        projectTitle.layoutBoundsProperty(),
                        deliverTable.heightProperty()));

        // ProjectList and ProjectDetails UI Setup
        projectList.setShowRoot(false);
        projectList.setRoot(projectListRoot);
        colorFolding();
        projectTitle.wrappingWidthProperty().bind(mainScroll.widthProperty().multiply(0.9));
        projectDetails.prefWidthProperty().bind(mainVBox.widthProperty().multiply(0.9));
        line.widthProperty().bind(mainVBox.widthProperty().multiply(0.9));
        projectDetailsArea.setWrapText(true);
        projectDetailsArea.prefWidthProperty().bind(mainScroll.widthProperty());
        // projectDetailsArea.prefHeightProperty().bind(Bindings.createDoubleBinding(
        // () -> projectDetails.getLayoutBounds().getHeight(),
        // projectDetails.layoutBoundsProperty()));
        projectDetailsArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE || (event.getCode() == KeyCode.ENTER) && !event.isShiftDown()) {
                int[] indexes = getSelectedIndex();
                int selectedIndex = indexes[1];
                String text = projectDetailsArea.getText();
                int indexOfLastChar = 0;
                System.out.println("debug 1");

                if (event.getCode() == KeyCode.ENTER) {
                    for (int x = text.length() - 1; x > 0; x--) {
                        if (Character.isWhitespace(text.charAt(x))) {
                            indexOfLastChar = x;
                            break;
                        }
                    }

                    text = text.substring(0, indexOfLastChar);

                    projectDetailsArea.setText(text);
                }
                System.out.println("debug 2");

                mainVBox.getChildren().get(3).setVisible(true);
                mainVBox.getChildren().get(3).setManaged(true);
                mainVBox.getChildren().get(2).setVisible(false);
                mainVBox.getChildren().get(2).setManaged(false);
                projectDetails.setText(projectDetailsArea.getText());
                projectDetails.setWrapText(true);

                System.out.println("debug 3");

                if (projectList.getSelectionModel().getSelectedItem().getParent() == projectListRoot) {
                    projectDetailsArr.set(selectedIndex, projectDetails.getText());
                    App.editProjectDetails(projectIdArr.get(selectedIndex), projectDetails.getText());
                    System.out.println("debug 4");
                } else {
                    deliverableDetailsArr.set(selectedIndex, projectDetails.getText());
                    int delId = deliverableIdArr.get(getDelIndexInPar(indexes[1], indexes[0]));
                    App.editDeliverableDetails(delId, projectDetails.getText());
                }

            } else if (event.getCode() == KeyCode.ENTER && event.isShiftDown()) {
                int caretPosition = projectDetailsArea.getCaretPosition();
                String currentText = projectDetailsArea.getText();
                String newText = currentText.substring(0, caretPosition) + "\n" + currentText.substring(caretPosition);
                projectDetailsArea.setText(newText);
                projectDetailsArea.positionCaret(caretPosition + 1);
            }

        });
        mainVBox.getChildren().get(2).setVisible(false);
        mainVBox.getChildren().get(2).setManaged(false);

        setupProjectTreeView();
        getAllTasks();
    }

    private void getAllTasks() {
        int taskDeliverableId;
        int taskId;
        String taskTitle;
        String taskDetails;
        String taskStatus;
        java.sql.Date taskDue;

        ResultSet rs = App.getTasks();
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            System.out.println(rsmd.getColumnCount());

            while (rs.next()) {
                taskDeliverableId = rs.getInt("deliverableid");
                taskId = rs.getInt("taskid");
                taskTitle = rs.getString("tasktitle");
                taskDetails = rs.getString("taskdetails");
                taskStatus = rs.getString("status");
                taskDue = rs.getDate("taskdue");

                taskDeliverableIdArr.add(taskDeliverableId);
                taskIdArr.add(taskId);
                taskTitleArr.add(taskTitle);
                taskDetailsArr.add(taskDetails);
                taskStatusArr.add(taskStatus);
                taskDueArr.add(taskDue);
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }
        App.closeConnection();
    }

    private void setupProjectTreeView() {
        ResultSet rs = App.getProjects();
        int projectId, deliverableProjectId, deliverableId, deliverableLeadId;
        String projectTitle, projectDetails, projectImage, deliverableTitle, deliverableDetails, deliverableStatus,
                deliverableLead;
        String[] rowBuffer;
        java.sql.Date deliverableDue;

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
                deliverableId = rs.getInt("deliverableId");
                deliverableTitle = rs.getString("deliverableTitle");
                deliverableDetails = rs.getString("deliverableDetails");
                deliverableStatus = rs.getString("deliverableStatus");
                deliverableLeadId = rs.getInt("deliverableLead");
                deliverableDue = rs.getDate("deliverableDue");

                deliverableProjectIdArr.add(deliverableProjectId);
                deliverableIdArr.add(deliverableId);
                deliverableTitleArr.add(deliverableTitle);
                deliverableDetailsArr.add(deliverableDetails);
                deliverableStatusArr.add(deliverableStatus);
                deliverableLeadIdArr.add(deliverableLeadId);
                deliverableLeadArr.add(App.getLead(deliverableLeadId));
                System.out.println(App.getLead(deliverableLeadId));
                deliverableDueArr.add(deliverableDue);
                System.out.println("Deliverable Title: " + deliverableTitle);
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }

        if (deliverableIdArr.size() > 0) {

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
                        System.out.println(projectIdArr);
                        projectBranches.get(projectIdArr.indexOf(currentProjectId)).getChildren().add(projectLeaf);
                    }
                    projectLeafsBuffer.clear();
                    projectLeafsBuffer.add(new TreeItem<String>(deliverableTitleArr.get(x)));
                    currentProjectId = deliverableProjectIdArr.get(x);
                }
            }

            for (TreeItem<String> projectLeaf : projectLeafsBuffer) {
                System.out.println("Current project id: " + currentProjectId);
                System.out.println("Project leaf: " + projectLeaf);
                System.out.println("Project IDs: " + projectIdArr);
                projectBranches.get(projectIdArr.indexOf(currentProjectId)).getChildren().add(projectLeaf);
            }
        }

        projectList.getSelectionModel().selectFirst();
        selectProject();
        App.closeConnection();
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

    @FXML
    private void btnDelete() {
        int[] indexes = getSelectedIndex();
        String msg = "Project";
        if (!(indexes[0] == -1)) {
            msg = "Deliverable";

        }
        TreeItem<String> selected = projectList.getSelectionModel().getSelectedItem();
        Alert dialog = new Alert(AlertType.CONFIRMATION, "Delete " + msg + "(" + selected.getValue() + ")");
        dialog.showAndWait().ifPresent(response -> {
            if (indexes[0] == -1) {
                System.out.println("Index id to delete: " + indexes[1]);
                System.out.println("ProjectIdArr length: " + projectIdArr.size());
                App.archiveProject(projectIdArr.get(indexes[1]));
                projectIdArr.remove(indexes[1]);
                projectTitleArr.remove(indexes[1]);
                projectDetailsArr.remove(indexes[1]);
                projectImageArr.remove(indexes[1]);
                projectListRoot.getChildren().remove(selected);
                projectList.getSelectionModel().selectFirst();
                selectProject();
            } else {
                int delIndexParallel = getDelIndexInPar(indexes[1], indexes[0]);
                archiveDeliverable(delIndexParallel);
                projectListRoot.getChildren().remove(selected);
                TreeItem<String> parent = selected.getParent();
                parent.getChildren().remove(selected);
                projectList.getSelectionModel().select(parent);
                selectProject();
            }
        });
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
        projectIdArr.add(App.newProject(projectName));
        projectTitleArr.add(projectName);
        projectDetailsArr.add("Double Click to Edit");
        projectImageArr.add("");
    }

    private void addToTable(String name) {
        TreeItem<String> selected = projectList.getSelectionModel().getSelectedItem();
        int indexes[] = getSelectedIndex();

        if (indexes[0] == -1) {
            int leafIndex = 0;

            if (deliverTable.getItems().size() == 0) {
                leafIndex = 0;
            } else {
                leafIndex = deliverTable.getItems().size();
            }

            int deliverId = 0;

            projectListRoot.getChildren().get(indexes[1]).getChildren().add(leafIndex,
                    new TreeItem<String>(name));

            deliverId = App.addDeliverable(projectIdArr.get(indexes[1]), name);
            deliverableProjectIdArr.add(leafIndex, projectIdArr.get(indexes[1]));
            deliverableIdArr.add(leafIndex, deliverId);
            deliverableTitleArr.add(leafIndex, name);
            deliverableDetailsArr.add(leafIndex, "Double Click to Edit");
            deliverableStatusArr.add(leafIndex, "TODO");
            deliverableLeadArr.add(leafIndex, "");
            deliverableLeadIdArr.add(leafIndex, 0);
            deliverableDueArr.add(leafIndex, new java.sql.Date((new java.util.Date()).getTime()));
        } else {
            int delIndexParallel = getDelIndexInPar(indexes[0], indexes[1]);
            int leafIndex = 0;

            if (deliverTable.getItems().size() == 0) {
                leafIndex = 0;
            } else {
                leafIndex = deliverTable.getItems().size();
            }

            int taskId = 0;

            taskId = App.addTask(deliverableIdArr.get(delIndexParallel), name);
            taskDeliverableIdArr.add(leafIndex, deliverableIdArr.get(delIndexParallel));
            taskIdArr.add(leafIndex, taskId);
            taskTitleArr.add(leafIndex, name);
            taskDetailsArr.add(leafIndex, name);
            taskStatusArr.add(leafIndex, "TODO");
            taskDueArr.add(leafIndex, new java.sql.Date((new java.util.Date()).getTime()));
        }
        refreshTable();
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
            int id = projectIdArr.get(indexes[1]);
            App.editProjectTitle(id, projectName);
        } else {
            deliverableTitleArr.set(indexes[1], projectName);
            int delId = deliverableIdArr.get(getDelIndexInPar(indexes[1], indexes[0]));
            App.editDeliverableTitle(delId, projectName);
        }

        selectedProject.setValue(projectName);
        projectViewUpdate(indexes[0]);
    }

    private void projectViewUpdate(int condition) {
        TreeItem<String> selected = projectList.getSelectionModel().getSelectedItem();
        if (condition == -1) {
            projectTitle.setText(selected.getValue());
        } else {
            projectTitle.setText(selected.getParent().getValue() + " > " + selected.getValue());
        }
    }

    private ArrayList<ArrayList<String>> getTasksUnderDeliverable(int indexDeliver) {
        ArrayList<ArrayList<String>> taskUnderProject = new ArrayList<ArrayList<String>>();
        int deliverableId = deliverableIdArr.get(indexDeliver);

        int index = 0;
        for (int x = 0; x < taskIdArr.size(); x++) {
            if (taskDeliverableIdArr.get(x) == deliverableId) {
                ArrayList<String> buffer = new ArrayList<String>();
                buffer.add(taskIdArr.get(x).toString());
                buffer.add(taskTitleArr.get(x));
                buffer.add(taskStatusArr.get(x));
                buffer.add(taskDueArr.get(x).toString());
                taskUnderProject.add(buffer);
                index++;
            }
        }

        for (ArrayList<String> task : taskUnderProject) {
            System.out.println(task.toString());
        }
        return taskUnderProject;
    }

    private ArrayList<ArrayList<String>> getDeliverablesUnderProject(int indexProject) {
        ArrayList<ArrayList<String>> deliverUnderProject = new ArrayList<ArrayList<String>>();
        int projectId = projectIdArr.get(indexProject);

        int index = 0;
        for (int x = 0; x < deliverableIdArr.size(); x++) {
            if (deliverableProjectIdArr.get(x) == projectId) {
                ArrayList<String> buffer = new ArrayList<String>();
                buffer.add(deliverableIdArr.get(x).toString());
                buffer.add(deliverableTitleArr.get(x));
                buffer.add(deliverableStatusArr.get(x));
                buffer.add(deliverableDueArr.get(x).toString());
                buffer.add(deliverableLeadArr.get(x));
                deliverUnderProject.add(buffer);
                index++;
            }
        }

        for (ArrayList<String> deliverable : deliverUnderProject) {
            System.out.println(deliverable.toString());
        }
        return deliverUnderProject;
    }

    public String dateToString(java.sql.Date date) {
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("MMMM dd, yyyy");
        return format.format(date);
    }

    private static ArrayList<String> archNamesToArr(Vector<ArrayList<Object>> archNames) {
        ArrayList<String> archNamesRet = new ArrayList<>();
        for (ArrayList<Object> instanceOfVec : archNames) {
            archNamesRet.add(instanceOfVec.get(2) + ", " + instanceOfVec.get(1));
        }

        return archNamesRet;
    }

    private static ArrayList<Integer> archNamesToIdArr(Vector<ArrayList<Object>> archNames) {
        ArrayList<Integer> archIdsRet = new ArrayList<>();
        for (ArrayList<Object> instanceOfVec : archNames) {
            archIdsRet.add((int) instanceOfVec.get(0));
        }

        return archIdsRet;
    }

    @FXML
    public void editLead() {
        System.out.println("Editing Lead");
        int[] indexes = getSelectedIndex();
        int delIndexParallel = getDelIndexInPar(indexes[0], indexes[1]);
        int idOfArchs = 0;

        Vector<ArrayList<Object>> archNames = new Vector<>();
        archNames = App.getArchNames();

        ArrayList<Integer> userIdParallel = archNamesToIdArr(archNames);
        ArrayList<String> archNamesArr = archNamesToArr(archNames);
        System.out.println(archNames);
        System.out.println(archNamesArr);

        Dialog<Integer> dialogLead = new Dialog<>();
        ComboBox<String> cbox = new ComboBox<String>(FXCollections.observableArrayList(archNamesArr));
        dialogLead.setTitle("Set Deliverable Lead");
        dialogLead.setHeaderText(null);
        dialogLead.setTitle("Choose Architect:");
        dialogLead.getDialogPane().setContent(cbox);
        dialogLead.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        cbox.getSelectionModel().selectFirst();

        dialogLead.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return cbox.getSelectionModel().getSelectedIndex();
            }
            return null;
        });

        Optional<Integer> result = dialogLead.showAndWait();

        if (result.isPresent()) {
            int index = Integer.valueOf(result.get());
            String archName = archNamesArr.get(index);
            deliverableLeadArr.set(delIndexParallel, archName);
            deliverableLeadIdArr.set(delIndexParallel, userIdParallel.get(index));
            lblLead.setText(archName);
            App.updateDelLead(userIdParallel.get(index), deliverableIdArr.get(delIndexParallel));
        }
    }

    @FXML
    public void editDueDate() {
        int[] indexes = getSelectedIndex();
        int delIndexParallel = getDelIndexInPar(indexes[0], indexes[1]);

        Dialog<LocalDate> dialogDelDate = new Dialog<>();
        DatePicker datepicker = new DatePicker();
        datepicker.setValue((deliverableDueArr.get(delIndexParallel)).toLocalDate());

        dialogDelDate.getDialogPane().setContent(datepicker);
        dialogDelDate.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialogDelDate.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return datepicker.getValue();
            }
            return null;
        });

        Optional<LocalDate> result = dialogDelDate.showAndWait();

        if (result.isPresent()) {
            java.sql.Date date = java.sql.Date.valueOf(result.get());
            deliverableDueArr.set(delIndexParallel, date);
            lblDue.setText(dateToString(deliverableDueArr.get(delIndexParallel)));
            App.updateDelDue(date, deliverableIdArr.get(delIndexParallel));
        }
    }

    @FXML
    private void selectProject() {
        System.out.println(projectListRoot.getChildren().size());
        if (projectListRoot.getChildren().size() == 0) {
            System.out.println("should be false");
            mainVBox.setVisible(false);
            return;
        } else {
            mainVBox.setVisible(true);
        }
        if (mainVBox.getChildren().get(2).isVisible()) {
            if (prevSelectedPrimed) {
                int[] indexes = prevSelected;
                int selectedIndex = indexes[1];
                String text = projectDetailsArea.getText();

                mainVBox.getChildren().get(3).setVisible(true);
                mainVBox.getChildren().get(3).setManaged(true);
                mainVBox.getChildren().get(2).setVisible(false);
                mainVBox.getChildren().get(2).setManaged(false);
                projectDetails.setText(projectDetailsArea.getText());
                projectDetails.setWrapText(true);

                if (projectList.getSelectionModel().getSelectedItem().getParent() == projectListRoot) {
                    projectDetailsArr.set(selectedIndex, projectDetails.getText());
                    App.editProjectDetails(projectIdArr.get(selectedIndex), projectDetails.getText());
                } else {
                    deliverableDetailsArr.set(selectedIndex, projectDetails.getText());
                    int delId = deliverableIdArr.get(getDelIndexInPar(indexes[1], indexes[0]));
                    App.editDeliverableDetails(delId, projectDetails.getText());
                }
            }
        }

        int[] indexes = getSelectedIndex();
        TreeItem<String> selected = projectList.getSelectionModel().getSelectedItem();
        int selectedIndexParent = indexes[0];
        int selectedIndex = indexes[1];

        if (selectedIndexParent == -1) {
            lblDue.setVisible(false);
            lblDue.setManaged(false);
            lblLead.setVisible(false);
            lblLead.setManaged(false);
            deliverCol.setText("Deliverable");
            deliverCol.prefWidthProperty().bind(deliverTable.widthProperty().divide(4));
            deliverStatusCol.prefWidthProperty().bind(deliverTable.widthProperty().divide(4));
            deliverLeadCol.prefWidthProperty().bind(deliverTable.widthProperty().divide(4));
            deliverDueCol.prefWidthProperty().bind(deliverTable.widthProperty().divide(4));
            if (deliverTable.getColumns().size() < 5) {
                deliverTable.getColumns().add(deliverLeadCol);
            }

            ArrayList<ArrayList<String>> deliverUnderProject = getDeliverablesUnderProject(selectedIndex);
            String[][] buffer;

            projectTitle.setText(selected.getValue());
            projectDetails.setText(projectDetailsArr.get(selectedIndex));
            data.clear();

            for (int x = 0; x < deliverUnderProject.size(); x++) {
                data.add(deliverUnderProject.get(x).toArray(new String[0]));
            }
        } else {
            int delIndexParallel = getDelIndexInPar(selectedIndex, selectedIndexParent);
            lblDue.setVisible(true);
            lblDue.setManaged(true);
            lblDue.setText(dateToString(deliverableDueArr.get(delIndexParallel)));

            lblLead.setVisible(true);
            lblLead.setManaged(true);
            String leadName = (deliverableLeadArr.get(delIndexParallel).equals("")) ? "Lead Name"
                    : deliverableLeadArr.get(delIndexParallel);
            lblLead.setText(leadName);

            deliverCol.prefWidthProperty().bind(deliverTable.widthProperty().divide(3));
            deliverStatusCol.prefWidthProperty().bind(deliverTable.widthProperty().divide(3));
            deliverDueCol.prefWidthProperty().bind(deliverTable.widthProperty().divide(3));
            projectTitle.setText(selected.getParent().getValue() + " > " + selected.getValue());
            projectDetails.setText(deliverableDetailsArr.get(delIndexParallel));
            deliverCol.setText("Task");
            if (deliverTable.getColumns().size() == 5) {
                deliverTable.getColumns().remove(4);
            }

            ArrayList<ArrayList<String>> taskUnderDeliver = getTasksUnderDeliverable(selectedIndex);
            String[][] buffer;
            data.clear();

            for (int x = 0; x < taskUnderDeliver.size(); x++) {
                data.add(taskUnderDeliver.get(x).toArray(new String[0]));
                System.out.println("Task Under Deliver Id: " + taskUnderDeliver.get(x).toArray(new String[0]));
            }
        }
        prevSelected = getSelectedIndex();
        prevSelectedPrimed = true;
    }

    private void refreshTable() {
        int[] indexes = getSelectedIndex();
        if (indexes[0] == -1) {
            ArrayList<ArrayList<String>> deliverUnderProject = getDeliverablesUnderProject(getSelectedIndex()[1]);
            data.clear();

            for (int x = 0; x < deliverUnderProject.size(); x++) {
                data.add(deliverUnderProject.get(x).toArray(new String[0]));
            }
        } else {
            int delIndexParallel = getDelIndexInPar(indexes[1], indexes[0]);
            ArrayList<ArrayList<String>> taskUnderDeliver = getTasksUnderDeliverable(delIndexParallel);
            data.clear();

            for (int x = 0; x < taskUnderDeliver.size(); x++) {
                data.add(taskUnderDeliver.get(x).toArray(new String[0]));
            }
        }
    }

    private int getDeliverableIndex(int i) {
        TreeItem<String> selected = projectList.getSelectionModel().getSelectedItem();
        int index = 0;

        for (TreeItem<String> projectTreeItem : projectListRoot.getChildren()) {
            if (projectTreeItem == selected) {
                break;
            }
            for (TreeItem<String> deliverableTreeItem : projectTreeItem.getChildren()) {
                index++;
            }
        }
        return index + i;
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
            indexes[0] = selectedIndexParent;
            indexes[1] = selectedIndex;
            return indexes;
        }
    }

    private int getDelIndexInPar(int selectedIndex, int parentIndex) {
        int projectId = projectIdArr.get(parentIndex);

        int index = 0;
        for (int x = 0; x < deliverableTitleArr.size(); x++) {
            if (projectId == deliverableProjectIdArr.get(x)) {
                if (index == selectedIndex) {
                    return x;
                } else {
                    index++;
                }
            }
        }

        return 0;
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
                TextInputDialog dialog = new TextInputDialog(selectedProject.getValue());
                dialog.setTitle("Change Deliverable Name");
                dialog.setHeaderText(null);
                dialog.setContentText("Deliverable Name:");
                dialog.getDialogPane().setStyle("-fx-background-color: white;-fx-border-color: #E8B631");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(inpt -> changeProjectName(inpt));
            }
        }
    }

    /**
     * Helper to set table height based on content
     *
     * @param table        - the table in context
     * @param rowHeight    - the height of a single row, alternatively could use
     *                     table.fixedCellSizeProperty()
     * @param headerHeight - the height of the table header
     * @param margin       - a value for the margins
     */
    public void tableHeightHelper(TableView<?> table, int headerHeight, int margin) {
        int rowHeight = table.getItems().size();
        table.prefHeightProperty().bind(Bindings.max(2, Bindings.size(table.getItems()))
                .multiply(rowHeight)
                .add(headerHeight)
                .add(margin));
        table.minHeightProperty().bind(table.prefHeightProperty());
        table.maxHeightProperty().bind(table.prefHeightProperty());
    }

    @FXML
    private void editProjectDetails(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Double layoutHeight = projectDetails.getHeight();
            System.out.println("Project Details: " + projectDetails.getHeight());
            System.out.println("Project Area: " + projectDetailsArea.getHeight());
            mainVBox.getChildren().get(3).setVisible(false);
            mainVBox.getChildren().get(3).setManaged(false);
            mainVBox.getChildren().get(2).setVisible(true);
            mainVBox.getChildren().get(2).setManaged(true);

            projectDetailsArea.setText(projectDetails.getText());
            projectDetailsArea.setMinHeight(layoutHeight + 20);
            projectDetailsArea.setMaxHeight(layoutHeight + 20);
            System.out.println("Project Details: " + projectDetails.getHeight());
            System.out.println("Project Area: " + projectDetailsArea.getHeight());
            projectDetailsArea.requestFocus();
            mainScroll.setVvalue(mainScroll.getVmin());
        }
    }
}
