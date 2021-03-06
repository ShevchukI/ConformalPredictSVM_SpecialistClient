package com.controllers.windows.specialist;

import com.controllers.windows.menu.MenuController;
import com.models.ModelDeveloper;
import com.tools.Constant;
import com.tools.Encryptor;
import com.tools.GlobalMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import org.apache.http.HttpResponse;

import java.io.IOException;

/**
 * Created by Admin on 29.01.2019.
 */
public class ChangeInfoMenuController extends MenuController {

    private Tooltip tooltipError_CurrentPassword;
    private Tooltip tooltipError_NewPassword;
    private Tooltip tooltipError_ConfirmPassword;
    private ModelDeveloper modelDeveloper;

    @FXML
    private PasswordField passwordField_CurrentPassword;
    @FXML
    private PasswordField passwordField_NewPassword;
    @FXML
    private PasswordField passwordField_ConfirmPassword;
    @FXML
    private Tooltip tooltip_CurrentPassword;
    @FXML
    private Tooltip tooltip_NewPassword;
    @FXML
    private Tooltip tooltip_ConfirmPassword;
    @FXML
    private Button button_Ok;
    @FXML
    private Button button_Cancel;


    @FXML
    public void initialize(Stage stage, Stage newWindow, boolean change) throws IOException {
        setStage(stage);
        setNewWindow(newWindow);
        modelDeveloper = new ModelDeveloper();
        tooltipError_CurrentPassword = new Tooltip();
        tooltipError_NewPassword = new Tooltip();
        tooltipError_ConfirmPassword = new Tooltip();
        button_Ok.setGraphic(Constant.okIcon());
        button_Cancel.setGraphic(Constant.cancelIcon());
    }

    public void savePassword(ActionEvent event) throws IOException {
        if (checkPasswords()) {
            if (passwordField_CurrentPassword.getText().equals(Constant.getAuth()[0])) {
                if (passwordField_NewPassword.getText().equals(passwordField_ConfirmPassword.getText())) {
                    HttpResponse response = modelDeveloper.changePassword(passwordField_ConfirmPassword.getText());
                    setStatusCode(response.getStatusLine().getStatusCode());
                    if (checkStatusCode(getStatusCode())) {
                        GlobalMap.getUserMap().put(Constant.PASSWORD,
                                Encryptor.encrypt(GlobalMap.getKeyMap().get(Constant.KEY),
                                        GlobalMap.getKeyMap().get(Constant.VECTOR),
                                        passwordField_ConfirmPassword.getText()));
                        Constant.getAlert(null, "Password changed!", Alert.AlertType.INFORMATION);
                        getNewWindow().close();
                    }
                }
            } else {
                Constant.getAlert(null, "Error! Current password incorrect, please try again.", Alert.AlertType.ERROR);
            }
        }
    }

    public void cancel(ActionEvent event) {
        getNewWindow().close();
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
}
