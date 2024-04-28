/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.architask;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.sql.*;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
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
import javafx.scene.layout.HBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView;
import javafx.scene.control.Dialog;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.nio.file.*;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TableColumn;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.stage.Popup;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import java.util.Vector;
import javafx.util.Callback;
import javafx.scene.control.TextInputDialog;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
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
    static ArrayList<java.sql.Date> projectDueArr = new ArrayList<java.sql.Date>();
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
    static Double imageMultiplier;

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
    Label lblStatus;
    @FXML
    Label lblTaskStatus;
    @FXML
    Label lblTaskDue;
    @FXML
    Rectangle line;
    @FXML
    Text taskTitle;
    @FXML
    Label taskDetails;
    @FXML
    TextArea taskDetailsArea;
    @FXML
    HBox chatHBox;
    @FXML
    HBox chatTextHBox;
    @FXML
    HBox chatBtnHBox;
    @FXML
    ImageView projectImageView;
    @FXML
    Button btnImage;
    @FXML
    ListView<String> chatList;
    @FXML
    TextField chatField;
    @FXML
    Label username;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        uiSetup();
        projectDetails.setWrapText(true);
        username.setText(App.getUserNow());
        // try {
        // App.setResizable(true);
        // App.setMaximized(true);
        // } catch (IOException err) {
        // err.printStackTrace();
        // }
    }

    /*
     * ==============================
     * Initial setup functions
     * ==============================
     * This section includes methods for the initial ui setups such as
     * bindings and listeners.
     */
    private void uiSetup() {
        contextMenuSetup();
        tableSetup();

        // VBox min height
        // mainVBox.minHeightProperty()
        // .bind(Bindings.createDoubleBinding(
        // () -> projectDetailsArea.getHeight() +
        // projectTitle.getLayoutBounds().getHeight()
        // + deliverTable.getHeight(),
        // projectDetailsArea.heightProperty(),
        // projectTitle.layoutBoundsProperty(),
        // deliverTable.heightProperty()));

        // projectImage.fitWidthProperty().bind(mainScroll.prefWidthProperty().multiply(0.9));
        if (projectImageView.getImage() == null) {
            projectImageView.setVisible(false);
            projectImageView.setManaged(false);
        }
        imageMultiplier = 1.0;

        // ProjectList and ProjectDetails UI Setup
        projectList.setShowRoot(false);
        projectList.setRoot(projectListRoot);
        projectList.setPrefWidth(200);

        // former colorFolding(); changed to wrapfolding instead since changing the
        // color or font style is too buggy
        wrapFolding();

        projectTitle.wrappingWidthProperty().bind(mainScroll.widthProperty().multiply(0.9));
        taskTitle.wrappingWidthProperty().bind(mainScroll.widthProperty().multiply(0.9));
        projectDetails.prefWidthProperty().bind(mainVBox.widthProperty().multiply(0.8));
        taskDetails.prefWidthProperty().bind(mainVBox.widthProperty().multiply(0.9));
        line.widthProperty().bind(mainVBox.widthProperty().multiply(0.9));

        mainScroll.widthProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("ScrollPane Width: " + newVal);
            if (mainScroll.widthProperty().multiply(0.7).get() > 900) {
                projectImageView.setFitWidth(900);
                projectImageView.setFitHeight(projectImageView.fitWidthProperty().get() * imageMultiplier);
            } else {
                projectImageView.setFitWidth(mainScroll.widthProperty().multiply(0.7).get());
                projectImageView.setFitHeight(mainScroll.widthProperty().multiply(0.7).get() * imageMultiplier);
            }
            mainScroll.requestLayout();
        });

        line.widthProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Line Width: " + newVal);
        });

        projectImageView.fitWidthProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("ImageView Fit Width: " + newVal);
        });

        // chat functionalty hidden, to be added if there is enough time

        projectDetailsArea.setWrapText(true);
        taskDetailsArea.setWrapText(true);
        projectDetailsArea.prefWidthProperty().bind(mainScroll.widthProperty().multiply(0.9));
        taskDetailsArea.prefWidthProperty().bind(mainScroll.widthProperty().multiply(0.9));
        chatHBox.prefWidthProperty().bind(mainScroll.widthProperty().multiply(0.9));
        chatHBox.setAlignment(Pos.CENTER);
        chatTextHBox.prefWidthProperty().bind(mainScroll.widthProperty().multiply(0.9));
        chatTextHBox.setAlignment(Pos.CENTER);
        chatBtnHBox.prefWidthProperty().bind(mainScroll.widthProperty().multiply(0.9));

        chatList.prefWidthProperty().bind(mainScroll.widthProperty().divide(3));

        taskDetailsArea.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue) {
                int taskIndex = deliverTable.getSelectionModel().getSelectedIndex();
                int taskId = Integer.parseInt((data.get(taskIndex))[0]);
                int taskIndexPar = taskIdArr.indexOf(taskId);
                String text = taskDetailsArea.getText();
                int indexOfLastChar = 0;

                taskDetails.setVisible(true);
                taskDetails.setManaged(true);
                taskDetailsArea.setManaged(false);
                taskDetailsArea.setVisible(false);
                taskDetails.setText(taskDetailsArea.getText());
                taskDetails.setWrapText(true);

                taskDetailsArr.set(taskIndexPar, taskDetails.getText());
            }
        });

        taskDetailsArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE || (event.getCode() == KeyCode.ENTER) && !event.isShiftDown()) {
                int taskIndex = deliverTable.getSelectionModel().getSelectedIndex();
                int taskId = Integer.parseInt((data.get(taskIndex))[0]);
                int taskIndexPar = taskIdArr.indexOf(taskId);
                String text = taskDetailsArea.getText();
                int indexOfLastChar = 0;

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

                taskDetails.setVisible(true);
                taskDetails.setManaged(true);
                taskDetailsArea.setManaged(false);
                taskDetailsArea.setVisible(false);
                taskDetails.setText(taskDetailsArea.getText());
                taskDetails.setWrapText(true);

                taskDetailsArr.set(taskIndexPar, taskDetails.getText());
                App.editTaskDetails(taskId, taskDetails.getText());
            } else if (event.getCode() == KeyCode.ENTER && event.isShiftDown()) {
                int caretPosition = taskDetailsArea.getCaretPosition();
                String currentText = taskDetailsArea.getText();
                String newText = currentText.substring(0, caretPosition) + "\n" + currentText.substring(caretPosition);
                taskDetailsArea.setText(newText);
                taskDetailsArea.positionCaret(caretPosition + 1);
            }
        });
        projectDetailsArea.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue) {
                int[] indexes = getSelectedIndex();
                int selectedIndex = indexes[1];
                String text = projectDetailsArea.getText();
                int indexOfLastChar = 0;

                projectDetails.setVisible(true);
                projectDetails.setManaged(true);
                projectDetailsArea.setVisible(false);
                projectDetailsArea.setManaged(false);
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
            } else {
                projectDetailsArea.positionCaret(projectDetailsArea.getText().length());
            }
        });
        projectDetailsArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE || (event.getCode() == KeyCode.ENTER) && !event.isShiftDown()) {
                int[] indexes = getSelectedIndex();
                int selectedIndex = indexes[1];
                String text = projectDetailsArea.getText();
                int indexOfLastChar = 0;

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

                projectDetails.setVisible(true);
                projectDetails.setManaged(true);
                projectDetailsArea.setVisible(false);
                projectDetailsArea.setManaged(false);
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

            } else if (event.getCode() == KeyCode.ENTER && event.isShiftDown()) {
                int caretPosition = projectDetailsArea.getCaretPosition();
                String currentText = projectDetailsArea.getText();
                String newText = currentText.substring(0, caretPosition) + "\n" + currentText.substring(caretPosition);
                projectDetailsArea.setText(newText);
                projectDetailsArea.positionCaret(caretPosition + 1);
            }

        });
        projectDetailsArea.setVisible(false);
        projectDetailsArea.setManaged(false);

        deliverTable.setOnMouseClicked(event -> {
            TreeItem<String> selected = projectList.getSelectionModel().getSelectedItem();

            if (selected.getParent() != projectListRoot) {
                if (deliverTable.getSelectionModel().getFocusedIndex() != -1) {
                    selectTask();
                }
            }
        });

        deliverTable.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                TreeItem<String> selected = projectList.getSelectionModel().getSelectedItem();

                if (selected.getParent() != projectListRoot) {
                    if (deliverTable.getSelectionModel().getFocusedIndex() != -1) {
                        taskTitle.setManaged(true);
                        taskDetails.setManaged(true);
                        lblTaskDue.setManaged(true);
                        lblTaskStatus.setManaged(true);
                        taskTitle.setVisible(true);
                        taskDetails.setVisible(true);
                        lblTaskDue.setVisible(true);
                        lblTaskStatus.setVisible(true);
                    }
                }
            }
        });

        setupProjectTreeView();
        getAllTasks();
    }

    private void saveImage(String name) {
        TreeItem<String> selected = projectList.getSelectionModel().getSelectedItem();
        int indexes[] = getSelectedIndex();

        if (selected.getParent() == projectListRoot) {
            int projectId = projectIdArr.get(indexes[1]);
            projectImageArr.set(indexes[1], name);
            App.editProjectImage(projectId, name);
            updateImage();
        }
    }

    private void updateImage() {
        TreeItem<String> selected = projectList.getSelectionModel().getSelectedItem();
        int indexes[] = getSelectedIndex();

        if (selected.getParent() == projectListRoot) {
            if (projectImageArr.get(indexes[1]) == "") {
                btnImage.setManaged(true);
                btnImage.setVisible(true);
                projectImageView.setManaged(false);
                projectImageView.setVisible(false);
            } else {
                System.out.println("This runs");
                btnImage.setManaged(false);
                btnImage.setVisible(false);
                projectImageView.setManaged(true);
                projectImageView.setVisible(true);

                System.out.println(projectImageArr.get(indexes[1]));
                Image image = new Image(
                        "file:///C:/Users/Nash/Documents/01_Docs/School/BSIT_2_B/FinalProject(2nd-Year)/ArchiTaskResources/img/"
                                + projectImageArr.get(indexes[1]));
                if (image.isError()) {
                    System.out.println(image.getException().getMessage());
                    System.out.println(image.getException().getCause());

                }
                Double width = image.getWidth();
                Double height = image.getHeight();
                imageMultiplier = height / width;
                System.out.println("Width: " + width);
                System.out.println("Height: " + height);
                System.out.println("Multiplier: " + imageMultiplier);

                projectImageView.setPreserveRatio(false);
                projectImageView.setImage(image);
                // projectImageView.setFitWidth(mainScroll.widthProperty().multiply(0.7).get());

                if (mainScroll.widthProperty().multiply(0.7).get() > 900) {
                    projectImageView.setFitWidth(900);
                    projectImageView.setFitHeight(projectImageView.fitWidthProperty().get() * imageMultiplier);
                } else {
                    projectImageView.setFitWidth(mainScroll.widthProperty().multiply(0.7).get());
                    projectImageView.setFitHeight(mainScroll.widthProperty().multiply(0.7).get() * imageMultiplier);
                }

                mainScroll.requestLayout();
            }
        } else {
            btnImage.setManaged(false);
            btnImage.setVisible(false);
            projectImageView.setManaged(false);
            projectImageView.setVisible(false);
        }
    }

    @FXML
    private void setImage(MouseEvent event) {
        FileChooser imageChoose = new FileChooser();
        imageChoose.setTitle("Choose Project Image");
        imageChoose.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg"));
        File selectedFile = imageChoose.showOpenDialog(projectImageView.getScene().getWindow());
        if (selectedFile != null) {
            try {
                Path dest = new File(
                        "C:/Users/Nash/Documents/01_Docs/School/BSIT_2_B/FinalProject(2nd-Year)/ArchiTaskResources/img/"
                                + selectedFile.getName())
                        .toPath();
                Files.copy(selectedFile.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
                projectImageView.setImage(new Image(dest.toUri().toString()));
                if (!projectImageView.isVisible()) {
                    projectImageView.setManaged(true);
                    projectImageView.setVisible(true);
                }
                saveImage(selectedFile.getName());
            } catch (IOException e) {
                e.printStackTrace(); // Handle the case where the image could not be saved or loaded
            }
        }

        projectImageView.setPreserveRatio(true);
        Double width = projectImageView.getFitWidth();
        Double height = projectImageView.getFitHeight();
        imageMultiplier = width / height;
        projectImageView.setPreserveRatio(false);
    }

    private void contextMenuSetup() {
        ContextMenu mItableMenu = new ContextMenu();
        MenuItem mIadd = new MenuItem("Add");
        MenuItem mIdelete = new MenuItem("Delete");
        MenuItem mIedit = new MenuItem("Edit");
        mItableMenu.getItems().addAll(mIadd, mIdelete, mIedit);
        deliverTable.setContextMenu(mItableMenu);

        mIedit.setOnAction((event) -> {
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
    }

    // not yet fully functional used to be named color folding changed in favor of
    // just wrapping
    private void wrapFolding() {
        projectList.setCellFactory(tv -> {
            TreeCell<String> treeCell = new TreeCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                    }
                    setWrapText(true);
                }
            };
            treeCell.prefWidthProperty().bind(projectList.widthProperty().subtract(5));
            return treeCell;
        });
    }

    private void setupProjectTreeView() {
        ResultSet rs = App.getProjects();
        int projectId, deliverableProjectId, deliverableId, deliverableLeadId;
        String projectTitle, projectDetails, projectImage, deliverableTitle, deliverableDetails, deliverableStatus,
                deliverableLead;
        String[] rowBuffer;
        java.sql.Date deliverableDue, projectDue;

        try {
            while (rs.next()) {
                // geting current project, deliverable, and task information
                projectId = rs.getInt("projectId");
                projectTitle = rs.getString("projectTitle");
                projectDetails = rs.getString("projectDetails");
                projectImage = rs.getString("projectImage");
                projectDue = rs.getDate("projectDue");

                // parallel arrays for reference
                projectTitleArr.add(projectTitle);
                projectIdArr.add(projectId);
                projectDetailsArr.add(projectDetails);
                projectImageArr.add(projectImage);
                projectDueArr.add(projectDue);

                projectBranches.add(new TreeItem<String>(projectTitle));
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }

        for (TreeItem<String> projectBranch : projectBranches) {
            projectListRoot.getChildren().add(projectBranch);
        }

        rs = App.getDeliverables();
        try {
            ResultSetMetaData rsmd = rs.getMetaData();

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
                deliverableDueArr.add(deliverableDue);
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }

        if (deliverableIdArr.size() > 0) {

            int currentProjectId = deliverableProjectIdArr.get(0);

            for (int x = 0; x < deliverableProjectIdArr.size(); x++) {
                if (deliverableProjectIdArr.get(x) == currentProjectId) {
                    projectLeafsBuffer.add(new TreeItem<String>(deliverableTitleArr.get(x)));
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

    /*
     * ==============================
     * End of Initial setup functions
     * ==============================
     */

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
        }
        return taskUnderProject;
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

    @FXML
    public void editLead() {
        int[] indexes = getSelectedIndex();
        int delIndexParallel = getDelIndexInPar(indexes[1], indexes[0]);
        int idOfArchs = 0;

        Vector<ArrayList<Object>> archNames = new Vector<>();
        archNames = App.getArchNames();

        ArrayList<Integer> userIdParallel = archNamesToIdArr(archNames);
        ArrayList<String> archNamesArr = archNamesToArr(archNames);

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
        }
        return deliverUnderProject;
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
        projectIdArr.add(App.newProject(projectName));
        projectTitleArr.add(projectName);
        projectDetailsArr.add("Double Click to Edit");
        projectImageArr.add("");
        projectDueArr.add(new java.sql.Date((new java.util.Date()).getTime()));
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

        int condition = indexes[0];
        TreeItem<String> selected = projectList.getSelectionModel().getSelectedItem();
        if (condition == -1) {
            projectTitle.setText(selected.getValue());
        } else {
            projectTitle.setText(selected.getParent().getValue() + " > " + selected.getValue());
        }
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

    @FXML
    private void editTaskTitle(MouseEvent event) {
        if (event.getClickCount() == 2) {
            TextInputDialog dialog = new TextInputDialog(taskTitle.getText());
            dialog.setTitle("Change Task Name");
            dialog.setHeaderText(null);
            dialog.setContentText("Task Name:");
            dialog.getDialogPane().setStyle("-fx-background-color: white;-fx-border-color: #E8B631");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(inpt -> changeTaskName(inpt));
        }
    }

    private void changeTaskName(String name) {
        int taskIndex = deliverTable.getSelectionModel().getSelectedIndex();
        int taskId = Integer.parseInt((data.get(taskIndex))[0]);
        int taskIndexPar = taskIdArr.indexOf(taskId);

        taskTitleArr.set(taskIndexPar, name);
        App.editTaskTitle(taskId, name);
        taskTitle.setText(name);
        refreshTable();
    }

    @FXML
    private void editTaskDetails(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Double layoutHeight = taskDetails.getHeight();
            taskDetails.setVisible(false);
            taskDetails.setManaged(false);
            taskDetailsArea.setVisible(true);
            taskDetailsArea.setManaged(true);

            taskDetailsArea.setText(taskDetails.getText());
            taskDetailsArea.setMinHeight(layoutHeight + 20);
            taskDetailsArea.setMaxHeight(layoutHeight + 20);
            taskDetailsArea.requestFocus();
        }
    }

    @FXML
    private void editProjectDetails(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Double layoutHeight = projectDetails.getHeight();
            projectDetails.setVisible(false);
            projectDetails.setManaged(false);
            projectDetailsArea.setVisible(true);
            projectDetailsArea.setManaged(true);

            projectDetailsArea.setText(projectDetails.getText());
            projectDetailsArea.setMinHeight(layoutHeight + 20);
            projectDetailsArea.setMaxHeight(layoutHeight + 20);
            projectDetailsArea.requestFocus();
            // mainScroll.setVvalue(mainScroll.getVmin());
        }
    }

    private void colorStatus() {
        if (lblStatus.getText().equals("TODO")) {
            lblStatus.setStyle("-fx-border-color:  #ee5622; -fx-text-fill: #ee5622");
        } else if (lblStatus.getText().equals("ONGOING")) {
            lblStatus.setStyle("-fx-border-color:   #edab24; -fx-text-fill: #edab24");
        } else {
            lblStatus.setStyle("-fx-border-color:  #26eb79; -fx-text-fill: #26eb79");
        }

        if (lblTaskStatus.getText().equals("TODO")) {
            lblTaskStatus.setStyle("-fx-border-color:  #ee5622; -fx-text-fill: #ee5622");
        } else if (lblTaskStatus.getText().equals("ONGOING")) {
            lblTaskStatus.setStyle("-fx-border-color:   #edab24; -fx-text-fill: #edab24");
        } else {
            lblTaskStatus.setStyle("-fx-border-color:  #26eb79; -fx-text-fill: #26eb79");
        }
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
            int delIndexParallel = getDelIndexInPar(indexes[1], indexes[0]);
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

    private String getStatus() {
        ArrayList<String> statuses = new ArrayList<>();
        statuses.add("TODO");
        statuses.add("ONGOING");
        statuses.add("DONE");

        Dialog<Integer> dialogLead = new Dialog<>();
        ComboBox<String> cbox = new ComboBox<String>(FXCollections.observableArrayList(statuses));
        dialogLead.setTitle("CHANGE STATUS");
        dialogLead.setHeaderText(null);
        dialogLead.setTitle("Status:");
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
            return statuses.get(index);
        }
        return "cancel";
    }

    private Optional<LocalDate> getDate(java.sql.Date currentDate) {
        Dialog<LocalDate> dialogDelDate = new Dialog<>();
        DatePicker datepicker = new DatePicker();
        datepicker.setValue(currentDate.toLocalDate());

        dialogDelDate.getDialogPane().setContent(datepicker);
        dialogDelDate.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialogDelDate.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return datepicker.getValue();
            }
            return null;
        });

        Optional<LocalDate> result = dialogDelDate.showAndWait();
        return result;
    }

    @FXML
    public void editTaskStatus() {
        int taskIndex = deliverTable.getSelectionModel().getSelectedIndex();
        int taskId = Integer.parseInt((data.get(taskIndex))[0]);
        int taskIndexPar = taskIdArr.indexOf(taskId);

        String status = getStatus();

        if (status != "cancel") {
            taskStatusArr.set(taskIndexPar, status);
            lblTaskStatus.setText(status);
            colorStatus();
            App.updateTaskStatus(status, taskId);
            refreshTable();
        }
        deliverTable.getSelectionModel().select(taskIndex);
    }

    @FXML
    public void editTaskDue() {
        int taskIndex = deliverTable.getSelectionModel().getSelectedIndex();
        int taskId = Integer.parseInt((data.get(taskIndex))[0]);
        int taskIndexPar = taskIdArr.indexOf(taskId);

        Optional<LocalDate> result = getDate(taskDueArr.get(taskIndexPar));

        if (result.isPresent()) {
            java.sql.Date date = java.sql.Date.valueOf(result.get());
            taskDueArr.set(taskIndexPar, date);
            lblTaskDue.setText(dateToString(taskDueArr.get(taskIndexPar)));
            App.updateTaskDue(date, taskId);
            refreshTable();
        }
        deliverTable.getSelectionModel().select(taskIndex);
    }

    @FXML
    public void addChat() {
        int taskIndex = deliverTable.getSelectionModel().getSelectedIndex();
        int taskId = Integer.parseInt((data.get(taskIndex))[0]);

        String input = chatField.getText();
        String chat = App.getUserNow() + ": " + input;

        chatField.setText("");
        chatList.getItems().add(chat);
        App.addChat(taskId, chat);
    }

    @FXML
    public void addChatEnter(javafx.scene.input.KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            int taskIndex = deliverTable.getSelectionModel().getSelectedIndex();
            int taskId = Integer.parseInt((data.get(taskIndex))[0]);

            String input = chatField.getText();
            String chat = App.getUserNow() + ": " + input;

            chatField.setText("");
            chatList.getItems().add(chat);
            App.addChat(taskId, chat);
        }
    }

    @FXML
    public void editDelStatus() {
        int[] indexes = getSelectedIndex();
        int delIndexParallel = getDelIndexInPar(indexes[1], indexes[0]);
        String status = getStatus();

        if (status != "cancel") {
            deliverableStatusArr.set(delIndexParallel, status);
            lblStatus.setText(status);
            colorStatus();
            App.updateDelStatus(status, deliverableIdArr.get(delIndexParallel));
        }
    }

    @FXML
    public void editDueDate() {
        int[] indexes = getSelectedIndex();
        if (indexes[0] == -1) {
            Optional<LocalDate> result = getDate(projectDueArr.get(indexes[1]));
            if (result.isPresent()) {
                java.sql.Date date = java.sql.Date.valueOf(result.get());
                projectDueArr.set(indexes[1], date);
                lblDue.setText(dateToString(projectDueArr.get(indexes[1])));
                App.updateProjectDue(date, projectIdArr.get(indexes[1]));
            }
        } else {
            int delIndexParallel = getDelIndexInPar(indexes[1], indexes[0]);

            Optional<LocalDate> result = getDate(deliverableDueArr.get(delIndexParallel));

            if (result.isPresent()) {
                java.sql.Date date = java.sql.Date.valueOf(result.get());
                deliverableDueArr.set(delIndexParallel, date);
                lblDue.setText(dateToString(deliverableDueArr.get(delIndexParallel)));
                App.updateDelDue(date, deliverableIdArr.get(delIndexParallel));
            }
        }
    }

    private void selectTask() {
        int taskIndex = deliverTable.getSelectionModel().getSelectedIndex();
        int taskId = Integer.parseInt((data.get(taskIndex))[0]);
        int taskIndexPar = taskIdArr.indexOf(taskId);

        lblTaskDue.setManaged(true);
        lblTaskDue.setVisible(true);
        lblTaskStatus.setManaged(true);
        lblTaskStatus.setVisible(true);
        chatHBox.setManaged(true);
        chatHBox.setVisible(true);
        chatTextHBox.setManaged(true);
        chatTextHBox.setVisible(true);
        chatBtnHBox.setManaged(true);
        chatBtnHBox.setVisible(true);

        chatList.getItems().clear();
        chatList.getItems().addAll(App.getChats(taskId));

        taskTitle.setText(taskTitleArr.get(taskIndexPar));
        taskDetails.setText(taskDetailsArr.get(taskIndexPar));
        lblTaskStatus.setText(taskStatusArr.get(taskIndexPar));
        lblTaskDue.setText(dateToString(taskDueArr.get(taskIndexPar)));
        colorStatus();
    }

    @FXML
    private void selectProject() {
        if (projectListRoot.getChildren().size() == 0) {
            mainVBox.setVisible(false);
            return;
        } else {
            mainVBox.setVisible(true);
        }
        if (projectDetailsArea.isVisible()) {
            if (prevSelectedPrimed) {
                int[] indexes = prevSelected;
                int selectedIndex = indexes[1];
                String text = projectDetailsArea.getText();

                projectDetails.setManaged(true);
                projectDetails.setVisible(true);
                projectDetailsArea.setManaged(false);
                projectDetailsArea.setVisible(false);
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

        chatHBox.setManaged(false);
        chatHBox.setVisible(false);
        chatTextHBox.setManaged(false);
        chatTextHBox.setVisible(false);
        chatBtnHBox.setManaged(false);
        chatBtnHBox.setVisible(false);

        if (selectedIndexParent == -1) {
            lblLead.setVisible(false);
            lblLead.setManaged(false);
            lblStatus.setManaged(false);
            lblStatus.setVisible(false);
            lblDue.setText(dateToString(projectDueArr.get(indexes[1])));
            deliverCol.setText("Deliverable");
            deliverCol.prefWidthProperty().bind(deliverTable.widthProperty().divide(4));
            deliverStatusCol.prefWidthProperty().bind(deliverTable.widthProperty().divide(4));
            deliverLeadCol.prefWidthProperty().bind(deliverTable.widthProperty().divide(4));
            deliverDueCol.prefWidthProperty().bind(deliverTable.widthProperty().divide(4));
            if (deliverTable.getColumns().size() < 5) {
                deliverTable.getColumns().add(deliverLeadCol);
            }
            updateImage();

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
            updateImage();

            lblStatus.setVisible(true);
            lblStatus.setManaged(true);
            String status = deliverableStatusArr.get(delIndexParallel);
            lblStatus.setText(status);
            colorStatus();

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
            }
        }
        taskTitle.setManaged(false);
        taskDetails.setManaged(false);
        taskDetailsArea.setManaged(false);
        lblTaskStatus.setManaged(false);
        lblTaskDue.setManaged(false);

        taskTitle.setVisible(false);
        taskDetails.setVisible(false);
        taskDetailsArea.setVisible(false);
        lblTaskStatus.setVisible(false);
        lblTaskDue.setVisible(false);

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

}
