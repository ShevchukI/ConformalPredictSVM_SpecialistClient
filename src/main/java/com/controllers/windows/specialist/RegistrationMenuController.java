package com.controllers.windows.specialist;

import com.controllers.requests.SpecialistController;
import com.controllers.windows.menu.MenuController;
import com.controllers.windows.menu.WindowsController;
import com.models.SpecialistEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by Admin on 06.01.2019.
 */
public class RegistrationMenuController extends MenuController {

    @Autowired
    LoginMenuController loginMenuController;

    private WindowsController windowsController = new WindowsController();
    private SpecialistController specialistController = new SpecialistController();
    private int statusCode;

    @FXML
    private TextField textField_Name;
    @FXML
    private TextField textField_Surname;
    @FXML
    private TextField textField_Login;
    @FXML
    private PasswordField passwordField_Password;
    @FXML
    private PasswordField passwordField_ConfirmPassword;
    @FXML
    private Tooltip tooltip_Name;
    @FXML
    private Tooltip tooltip_Surname;
    @FXML
    private Tooltip tooltip_Login;
    @FXML
    private Tooltip tooltip_Password;
    @FXML
    private Tooltip tooltip_ConfirmPassword;

    private Tooltip tooltipError_Name = new Tooltip();
    private Tooltip tooltipError_Surname = new Tooltip();
    private Tooltip tooltipError_Login = new Tooltip();
    private Tooltip tooltipError_Password = new Tooltip();
    private Tooltip tooltipError_ConfirmPassword = new Tooltip();

    public void register(ActionEvent event) throws IOException {
        if (checkRegister()) {
            SpecialistEntity specialistEntity = new SpecialistEntity(textField_Name.getText(), textField_Surname.getText(),
                    textField_Login.getText(), passwordField_ConfirmPassword.getText());
            statusCode = specialistController.specialistRegistration(specialistEntity);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            if (checkStatusCode(statusCode)) {
                alert.setContentText("Congratulations, you are registered!");
                alert.showAndWait();
                windowsController.openWindow("specialist/loginMenu", getStage(), loginMenuController,
                        "Login menu", 350, 190);
            }
        }
    }


    public void cancel(ActionEvent event) {
        ButtonType ok = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert questionOfCancellation = new Alert(Alert.AlertType.WARNING, "Do you really want to leave?", ok, cancel);
        questionOfCancellation.setHeaderText(null);
        Optional<ButtonType> result = questionOfCancellation.showAndWait();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        if (result.orElse(cancel) == ok) {
            try {
                returnToLoginMenu();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void returnToLoginMenu() throws IOException {
        windowsController.openWindow("specialist/loginMenu", getStage(), loginMenuController,
                "Login menu", 350, 190);
    }

    public boolean checkRegister() {
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

        if (textField_Login.getText().equals("")) {
            tooltipError_Login.setText("You login is empty!");
            textField_Login.setTooltip(tooltipError_Login);
            textField_Login.setStyle("-fx-border-color: red");
        } else {
            textField_Login.setTooltip(tooltip_Login);
            textField_Login.setStyle("-fx-border-color: inherit");
        }

        if (passwordField_Password.getText().equals("")) {
            tooltipError_Password.setText("You password is empty!");
            passwordField_Password.setTooltip(tooltipError_Password);
            passwordField_Password.setStyle("-fx-border-color: red");
        } else {
            passwordField_Password.setTooltip(tooltip_Password);
            passwordField_Password.setStyle("-fx-border-color: inherit");
        }

        if (passwordField_ConfirmPassword.getText().equals("")) {
            tooltipError_ConfirmPassword.setText("You confirm password is empty");
            passwordField_ConfirmPassword.setTooltip(tooltipError_ConfirmPassword);
            passwordField_ConfirmPassword.setStyle("-fx-border-color: red");

        } else if (!passwordField_ConfirmPassword.getText().equals(passwordField_Password.getText())) {
            tooltipError_ConfirmPassword.setText("Your passwords do not match!");
            passwordField_ConfirmPassword.setTooltip(tooltipError_ConfirmPassword);
            passwordField_ConfirmPassword.setStyle("-fx-border-color: red");

        } else {
            passwordField_ConfirmPassword.setTooltip(tooltip_ConfirmPassword);
            passwordField_ConfirmPassword.setStyle("-fx-border-color: inherit");
        }

        if (textField_Name.getStyle().equals("-fx-border-color: inherit")
                && textField_Surname.getStyle().equals("-fx-border-color: inherit")
                && textField_Login.getStyle().equals("-fx-border-color: inherit")
                && passwordField_Password.getStyle().equals("-fx-border-color: inherit")
                && passwordField_ConfirmPassword.getStyle().equals("-fx-border-color: inherit")) {
            return true;
        } else {
            return false;
        }
    }
}
