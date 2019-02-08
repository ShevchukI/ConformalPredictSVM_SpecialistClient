package com.controllers.windows.specialist;

import com.controllers.requests.SpecialistController;
import com.controllers.windows.menu.MenuController;
import com.hazelcast.core.HazelcastInstance;
import com.models.Specialist;
import com.tools.Encryptor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
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

    private Tooltip tooltipError_CurrentPassword = new Tooltip();
    private Tooltip tooltipError_NewPassword = new Tooltip();
    private Tooltip tooltipError_ConfirmPassword = new Tooltip();
    private Tooltip tooltipError_Name = new Tooltip();
    private Tooltip tooltipError_Surname = new Tooltip();
    private SpecialistController specialistController = new SpecialistController();
    private int statusCode;

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
    public void initialize(Stage stage, HazelcastInstance hazelcastInstance, Stage newWindow, boolean change) throws IOException {
        userMap = hazelcastInstance.getMap("userMap");
        stage.setOnHidden(event -> {
            hazelcastInstance.getLifecycleService().shutdown();
        });
        setStage(stage);
        setInstance(hazelcastInstance);
        setNewWindow(newWindow);
        if (change) {
            textField_Name.setText(getMap().get("name").toString());
            textField_Surname.setText(getMap().get("surname").toString());
        }
    }

    public void savePassword(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        if (checkPasswords()) {
            if (passwordField_CurrentPassword.getText().equals(new Encryptor().decrypt(getMap().get("key").toString(), getMap().get("vector").toString(),
                    getMap().get("password").toString()))) {
                if (passwordField_NewPassword.getText().equals(passwordField_ConfirmPassword.getText())) {
                    response = specialistController.changePassword(new Encryptor().decrypt(getMap().get("key").toString(),
                            getMap().get("vector").toString(), getMap().get("login").toString()),
                            new Encryptor().decrypt(getMap().get("key").toString(), getMap().get("vector").toString(),
                                    getMap().get("password").toString()), passwordField_ConfirmPassword.getText());
                    statusCode = response.getStatusLine().getStatusCode();
                    if (checkStatusCode(statusCode)) {
                        getMap().put("password", new Encryptor().encrypt(getMap().get("key").toString(), getMap().get("vector").toString(),
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
            Specialist specialist = new Specialist(textField_Name.getText(), textField_Surname.getText());
            response = specialistController.changeName(new Encryptor().decrypt(getMap().get("key").toString(),
                    getMap().get("vector").toString(), getMap().get("login").toString()),
                    new Encryptor().decrypt(getMap().get("key").toString(), getMap().get("vector").toString(),
                            getMap().get("password").toString()), specialist);
            statusCode = response.getStatusLine().getStatusCode();
            if (checkStatusCode(statusCode)) {
                alert.setContentText("Information changed!");
                alert.showAndWait();
                getMap().put("name", specialist.getName());
                getMap().put("surname", specialist.getSurname());
            }
            getNewWindow().close();
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
