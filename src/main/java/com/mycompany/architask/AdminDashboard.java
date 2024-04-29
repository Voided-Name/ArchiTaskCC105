/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.architask;

import javafx.scene.control.ContextMenu;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import java.io.IOException;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableCell;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ButtonType;
import java.io.File;
import java.util.Optional;

/**
 *
 * @author Nash
 */
public class AdminDashboard implements Initializable {
  @FXML
  AnchorPane adminPane;
  @FXML
  VBox adminCenter;
  @FXML
  TableView<String[]> adminTable;
  @FXML
  TableColumn<String[], String> adminTableId;
  @FXML
  TableColumn<String[], String> adminTableUsername;
  @FXML
  TableColumn<String[], String> adminTableFName;
  @FXML
  TableColumn<String[], String> adminTableLName;
  @FXML
  TableColumn<String[], String> adminTableEmail;
  @FXML
  TableColumn<String[], String> adminTableUType;

  static ObservableList<String[]> data = FXCollections.observableArrayList();

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    adminCenter.prefWidthProperty().bind(adminPane.widthProperty());
    adminCenter.prefHeightProperty().bind(adminPane.heightProperty());
    adminTable.prefHeightProperty().bind(adminCenter.heightProperty().multiply(0.8));
    adminTableId.setVisible(false);
    fillAdminTable();
  }

  private void fillAdminTable() {
    ArrayList<ArrayList<String>> cells;
    cells = App.adminTableValues();
    System.out.println(cells.size());
    adminTableUsername.prefWidthProperty().bind(adminTable.widthProperty().divide(5));
    adminTableFName.prefWidthProperty().bind(adminTable.widthProperty().divide(5));
    adminTableLName.prefWidthProperty().bind(adminTable.widthProperty().divide(5));
    adminTableUType.prefWidthProperty().bind(adminTable.widthProperty().divide(5));
    adminTableEmail.prefWidthProperty().bind(adminTable.widthProperty().divide(5));
    adminTableId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));
    adminTableUsername.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));
    adminTableFName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2]));
    adminTableLName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[3]));
    adminTableEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[4]));
    adminTableUType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[5]));
    adminTable.setItems(data);
    adminTable.setMinHeight(200);
    for (ArrayList<String> cell : cells) {
      data.add(cell.toArray(new String[0]));
    }
  }

  // <Button
  // onMouseClick="#adminEdit"fx:id="adminEditUser"alignment="CENTER"mnemonicParsing="false"prefHeight="46.0"prefWidth="94.0"text="Edit
  // Type"/><Button
  // onMouseClick="#adminArchive"fx:id="adminArchiveUser"alignment="CENTER"mnemonicParsing="false"prefHeight="46.0"prefWidth="94.0"text="Archive
  // User"/><Button
  // onMouseClick="#adminGenerate"fx:id="adminGenerate"alignment="CENTER"contentDisplay="CENTER"mnemonicParsing="false"prefHeight="46.0"prefWidth="94.0"text="Generate
  // Report"textAlignment="CENTER"wrapText="true"/>

  // @FXML
  // public void editLead() {
  // int[] indexes = getSelectedIndex();
  // int delIndexParallel = getDelIndexInPar(indexes[1], indexes[0]);
  // int idOfArchs = 0;
  //
  // Vector<ArrayList<Object>> archNames = new Vector<>();
  // archNames = App.getArchNames();
  //
  // ArrayList<Integer> userIdParallel = archNamesToIdArr(archNames);
  // ArrayList<String> archNamesArr = archNamesToArr(archNames);
  //
  // Dialog<Integer> dialogLead = new Dialog<>();
  // ComboBox<String> cbox = new
  // ComboBox<String>(FXCollections.observableArrayList(archNamesArr));
  // dialogLead.setTitle("Set Deliverable Lead");
  // dialogLead.setHeaderText(null);
  // dialogLead.setTitle("Choose Architect:");
  // dialogLead.getDialogPane().setContent(cbox);
  // dialogLead.getDialogPane().getButtonTypes().addAll(ButtonType.OK,
  // ButtonType.CANCEL);
  // cbox.getSelectionModel().selectFirst();
  //
  // dialogLead.setResultConverter(dialogButton -> {
  // if (dialogButton == ButtonType.OK) {
  // return cbox.getSelectionModel().getSelectedIndex();
  // }
  // return null;
  // });
  //
  // Optional<Integer> result = dialogLead.showAndWait();
  //
  // if (result.isPresent()) {
  // int index = Integer.valueOf(result.get());
  // String archName = archNamesArr.get(index);
  // deliverableLeadArr.set(delIndexParallel, archName);
  // deliverableLeadIdArr.set(delIndexParallel, userIdParallel.get(index));
  // lblLead.setText(archName);
  // App.updateDelLead(userIdParallel.get(index),
  // deliverableIdArr.get(delIndexParallel));
  // }
  // }

  // dialogLead.setTitle("Set Deliverable Lead");
  // dialogLead.setHeaderText(null);
  // dialogLead.setTitle("Choose Architect:");
  // dialogLead.getDialogPane().setContent(cbox);
  // dialogLead.getDialogPane().getButtonTypes().addAll(ButtonType.OK,
  // ComboBox<String> cbox = new
  // ComboBox<String>(FXCollections.observableArrayList(archNamesArr));
  @FXML
  public void adminEdit() {
    int selectedIndex = adminTable.getSelectionModel().getSelectedIndex();
    int userId = Integer.parseInt((data.get(selectedIndex))[0]);
    Dialog<Integer> getUserType = new Dialog<>();
    getUserType.setTitle("Set User Type");
    getUserType.setHeaderText(null);
    getUserType.setTitle("Choose New User Type: ");
    ArrayList<String> userTypes = new ArrayList<>();
    userTypes.add("Senior Architect");
    userTypes.add("Architect");
    userTypes.add("Admin");
    ComboBox<String> cbox = new ComboBox<String>(FXCollections.observableArrayList(userTypes));
    getUserType.getDialogPane().setContent(cbox);
    getUserType.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
    cbox.getSelectionModel().selectFirst();

    getUserType.setResultConverter(dialogButton -> {
      if (dialogButton == ButtonType.OK) {
        return cbox.getSelectionModel().getSelectedIndex();
      }
      return null;
    });

    Optional<Integer> result = getUserType.showAndWait();

    if (result.isPresent()) {
      int index = Integer.valueOf(result.get());
      App.adminEdit(userId, index);
      data.clear();
      fillAdminTable();
    }

    ContextMenu usernameMenu = new ContextMenu();
    MenuItem logout = new MenuItem("Log Out");
    usernameMenu.getItems().add(logout);
    adminTable.setContextMenu(usernameMenu);

    logout.setOnAction((event) -> {
      try {
        data.clear();

        File remFile = new File(
            "C:/Users/Nash/Documents/01_Docs/School/BSIT_2_B/FinalProject(2nd-Year)/ArchiTaskResources/Generated/"
                + "remFile.txt");
        if (remFile.exists()) {
          remFile.delete();
        }

        App.setRoot("login");
        App.setResizable(false);
        App.setSceneSize(600, 400);
      } catch (IOException err) {
        err.printStackTrace();
      }
    });
  }

  @FXML
  public void adminArchive() {
    int selectedIndex = adminTable.getSelectionModel().getSelectedIndex();
    int userId = Integer.parseInt((data.get(selectedIndex))[0]);
    Dialog<Integer> archiveAccount = new Dialog<>();
    archiveAccount.setTitle("Archive Account");
    archiveAccount.setHeaderText(null);
    archiveAccount.setTitle("Are you sure?");
    archiveAccount.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

    archiveAccount.setResultConverter(dialogButton -> {
      if (dialogButton == ButtonType.OK) {
        App.adminArchive(userId);
        data.clear();
        fillAdminTable();
      }
      return null;
    });

    archiveAccount.showAndWait();
  }

  @FXML
  public void adminGenerate() {
    DashboardController.generateMD();
  }

}
