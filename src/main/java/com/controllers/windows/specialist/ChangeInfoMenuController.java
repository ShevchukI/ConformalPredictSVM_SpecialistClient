package com.controllers.windows.specialist;

import com.controllers.requests.SpecialistController;
import com.controllers.windows.menu.MenuController;
import com.models.DataSet;
import com.models.SpecialistEntity;
import com.tools.Constant;
import com.tools.Encryptor;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by Admin on 29.01.2019.
 */
public class ChangeInfoMenuController extends MenuController {

    @Autowired
    HttpResponse response;

    private Tooltip tooltipError_CurrentPassword;
    private Tooltip tooltipError_NewPassword;
    private Tooltip tooltipError_ConfirmPassword;
    private Tooltip tooltipError_Name;
    private Tooltip tooltipError_Surname;
    private ObservableList<DataSet> allDataSetObservableList;
    private ObservableList<DataSet> myDataSetObservableList;

    @FXML
    private PasswordField passwordField_CurrentPassword;
    @FXML
    private PasswordField passwordField_NewPassword;
    @FXML
    private PasswordField passwordField_ConfirmPassword;
    @FXML
    private TextField textField_Name;
    @FXML
    private TextField textField_Surname;
    @FXML
    private Tooltip tooltip_CurrentPassword;
    @FXML
    private Tooltip tooltip_NewPassword;
    @FXML
    private Tooltip tooltip_ConfirmPassword;
    @FXML
    private Tooltip tooltip_Name;
    @FXML
    private Tooltip tooltip_Surname;

    @FXML
    public void initialize(Stage stage, Stage newWindow, boolean change) throws IOException {
        stage.setOnHidden(event -> {
            Constant.getInstance().getLifecycleService().shutdown();
        });
        setStage(stage);
        setNewWindow(newWindow);
        tooltipError_CurrentPassword = new Tooltip();
        tooltipError_NewPassword = new Tooltip();
        tooltipError_ConfirmPassword = new Tooltip();
        tooltipError_Name = new Tooltip();
        tooltipError_Surname = new Tooltip();
        if (change) {
            textField_Name.setText(Constant.getMapByName(Constant.getUserMapName()).get("name").toString());
            textField_Surname.setText(Constant.getMapByName(Constant.getUserMapName()).get("surname").toString());
        }
    }

    public void savePassword(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        if (checkPasswords()) {
            if (passwordField_CurrentPassword.getText().equals(Constant.getAuth()[0])) {
                if (passwordField_NewPassword.getText().equals(passwordField_ConfirmPassword.getText())) {
                    response = SpecialistController.changePassword(passwordField_ConfirmPassword.getText());
                    setStatusCode(response.getStatusLine().getStatusCode());
                    if (checkStatusCode(getStatusCode())) {
                        Constant.getMapByName(Constant.getUserMapName()).put("password", new Encryptor().encrypt(Constant.getMapByName(Constant.getKeyMapName()).get("key").toString(),
                                Constant.getMapByName(Constant.getKeyMapName()).get("vector").toString(),
                                passwordField_ConfirmPassword.getText().toString()));
                        alert.setContentText("Password changed!");
                        alert.showAndWait();
                        getNewWindow().close();
                    }
                }
            } else {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setContentText("Error! Current password incorrect, please try again.");
                alert.showAndWait();
            }
        }

    }

    public void cancel(ActionEvent event) {
        getNewWindow().close();
    }

    public void saveName(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        if (checkNames()) {
            SpecialistEntity specialistEntity = new SpecialistEntity(textField_Name.getText(), textField_Surname.getText());
            response = SpecialistController.changeName(specialistEntity);
            setStatusCode(response.getStatusLine().getStatusCode());
            if (checkStatusCode(getStatusCode())) {
                alert.setContentText("Information changed!");
                alert.showAndWait();
                Constant.getMapByName(Constant.getUserMapName()).put("name", specialistEntity.getName());
                Constant.getMapByName(Constant.getUserMapName()).put("surname", specialistEntity.getSurname());
                Label label_Name = (Label)getStage().getScene().lookup("#label_Name");
                label_Name.setText(Constant.getMapByName(Constant.getUserMapName()).get("name").toString() + " "
                        + Constant.getMapByName(Constant.getUserMapName()).get("surname").toString());
                getNewWindow().close();
                TableView<DataSet> tableView_All = (TableView)getStage().getScene().lookup("#tableView_AllDataSetTable");
                TableView<DataSet> tableView_My = (TableView)getStage().getScene().lookup("#tableView_MyDataSetTable");
                allDataSetObservableList = tableView_All.getItems();
                for(DataSet dataSet:allDataSetObservableList){
                    if(dataSet.getSpecialistEntity().getId() == Integer.parseInt(Constant.getMapByName(Constant.getUserMapName()).get("id").toString())){
                        dataSet.getSpecialistEntity().setName(Constant.getMapByName(Constant.getUserMapName()).get("name").toString());
                        dataSet.getSpecialistEntity().setSurname(Constant.getMapByName(Constant.getUserMapName()).get("surname").toString());
                    }
                }
//                tableView_All.getItems().clear();
//                tableView_All.setItems(allDataSetObservableList);
                tableView_All.refresh();

                myDataSetObservableList = tableView_My.getItems();
                for(DataSet dataSet:myDataSetObservableList){
                    if(dataSet.getSpecialistEntity().getId() == Integer.parseInt(Constant.getMapByName(Constant.getUserMapName()).get("id").toString())){
                        dataSet.getSpecialistEntity().setName(Constant.getMapByName(Constant.getUserMapName()).get("name").toString());
                        dataSet.getSpecialistEntity().setSurname(Constant.getMapByName(Constant.getUserMapName()).get("surname").toString());
                    }
                }
//                tableView_My.getItems().clear();
//                tableView_My.setItems(myDataSetObservableList);
                tableView_My.refresh();

//                tableView_All.getItems().clear();
//                response = dataSetController.getSpecialistDataSetAllPage(Constant.getAuth(),
//                        Integer.parseInt(Constant.getMapByName("misc").get("pageIndexAllDataset").toString()),
//                        Integer.parseInt(Constant.getMapByName("misc").get("objectOnPage").toString()));
//                statusCode = response.getStatusLine().getStatusCode();
//                if(checkStatusCode(statusCode)){
//                    dataSetPage = new DataSetPage().fromJson(response);
//                    allDataSetObservableList = FXCollections.observableList(dataSetPage.getDataSetEntities());
//                    tableView_All.setItems(allDataSetObservableList);
//                    tableView_All.refresh();
//                }
//
//                tableView_My.getItems().clear();
//                response = dataSetController.getSpecialistDataSetAllPage(Constant.getAuth(),
//                        Integer.parseInt(Constant.getMapByName("misc").get("pageIndexMyDataset").toString()),
//                        Integer.parseInt(Constant.getMapByName("misc").get("objectOnPage").toString()));
//                statusCode = response.getStatusLine().getStatusCode();
//                if(checkStatusCode(statusCode)){
//                    dataSetPage = new DataSetPage().fromJson(response);
//                    myDataSetObservableList = FXCollections.observableList(dataSetPage.getDataSetEntities());
//                    tableView_My.setItems(myDataSetObservableList);
//                    tableView_My.refresh();
//                }
            }
        }
    }

    public boolean checkPasswords() {
        if (passwordField_CurrentPassword.getText().equals("")) {
            tooltipError_CurrentPassword.setText("You name is empty!");
            passwordField_CurrentPassword.setTooltip(tooltipError_CurrentPassword);
            passwordField_CurrentPassword.setStyle("-fx-border-color: red");
        } else {
            passwordField_CurrentPassword.setTooltip(tooltip_CurrentPassword);
            passwordField_CurrentPassword.setStyle("-fx-border-color: inherit");
        }
        if (passwordField_NewPassword.getText().equals("")) {
            tooltipError_NewPassword.setText("You surname is empty!");
            passwordField_NewPassword.setTooltip(tooltipError_NewPassword);
            passwordField_NewPassword.setStyle("-fx-border-color: red");
        } else {
            passwordField_NewPassword.setTooltip(tooltip_NewPassword);
            passwordField_NewPassword.setStyle("-fx-border-color: inherit");
        }
        if (passwordField_ConfirmPassword.getText().equals("")) {
            tooltipError_ConfirmPassword.setText("You login is empty!");
            passwordField_ConfirmPassword.setTooltip(tooltipError_ConfirmPassword);
            passwordField_ConfirmPassword.setStyle("-fx-border-color: red");
        } else {
            passwordField_ConfirmPassword.setTooltip(tooltip_ConfirmPassword);
            passwordField_ConfirmPassword.setStyle("-fx-border-color: inherit");
        }
        if (passwordField_CurrentPassword.getStyle().equals("-fx-border-color: inherit")
                && passwordField_NewPassword.getStyle().equals("-fx-border-color: inherit")
                && passwordField_ConfirmPassword.getStyle().equals("-fx-border-color: inherit")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkNames() {
        if (textField_Name.getText().equals("")) {
            tooltipError_Name.setText("You name is empty!");
            textField_Name.setTooltip(tooltipError_Name);
            textField_Name.setStyle("-fx-border-color: red");
        } else {
            textField_Name.setTooltip(tooltip_Name);
            textField_Name.setStyle("-fx-border-color: inherit");
        }
        if (textField_Surname.getText().equals("")) {
            tooltipError_Surname.setText("You surname is empty!");
            textField_Surname.setTooltip(tooltipError_Surname);
            textField_Surname.setStyle("-fx-border-color: red");
        } else {
            textField_Surname.setTooltip(tooltip_Surname);
            textField_Surname.setStyle("-fx-border-color: inherit");
        }
        if (textField_Name.getStyle().equals("-fx-border-color: inherit")
                && textField_Surname.getStyle().equals("-fx-border-color: inherit")) {
            return true;
        } else {
            return false;
        }
    }
}
