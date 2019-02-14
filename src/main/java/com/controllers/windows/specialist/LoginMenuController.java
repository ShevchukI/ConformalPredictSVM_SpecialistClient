package com.controllers.windows.specialist;

import com.controllers.requests.SpecialistController;
import com.controllers.windows.menu.MainMenuController;
import com.controllers.windows.menu.MenuController;
import com.controllers.windows.menu.WindowsController;
import com.models.Specialist;
import com.tools.Constant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by Admin on 06.01.2019.
 */
public class LoginMenuController extends MenuController {
    @Autowired
    RegistrationMenuController registrationMenuController;
    @Autowired
    MainMenuController mainMenuController;
    @Autowired
    HttpResponse response;

//    private String key;
//    private String vector;
    private SpecialistController specialistController = new SpecialistController();
//    private Encryptor encryptor = new Encryptor();
    private int statusCode;
    private WindowsController windowsController = new WindowsController();

    @FXML
    private TextField textField_Login;
    @FXML
    private PasswordField passwordField_Password;

    public void signIn(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        if (textField_Login.getText().equals("")) {
            alert.setContentText("Login is empty!");
            alert.showAndWait();
        } else if (passwordField_Password.getText().equals("")) {
            alert.setContentText("Password is empty!");
            alert.showAndWait();
        } else {
            response = specialistController.specialistAuthorization(textField_Login.getText(), passwordField_Password.getText());
            statusCode = response.getStatusLine().getStatusCode();
            if(checkStatusCode(statusCode)){
                Constant.fillMap(new Specialist().fromJson(response), textField_Login.getText(), passwordField_Password.getText(), getMap());
//                fillMap(new Specialist().fromJson(response), textField_Login.getText(), passwordField_Password.getText());
                windowsController.openWindowResizable("menu/mainMenu.fxml", getStage(), getInstance(),
                        mainMenuController, "Main menu", 600, 640);
            }
//            if (statusCode == 200) {
//                Specialist specialist = new Specialist().fromJson(response);
//                key = encryptor.genRandString();
//                vector = encryptor.genRandString();
//                getMap().put("key", key);
//                getMap().put("vector", vector);
//                getMap().put("login", encryptor.encrypt(key, vector, textField_Login.getText()));
//                getMap().put("password", encryptor.encrypt(key, vector, passwordField_Password.getText()));
//                getMap().put("id", specialist.getId());
//                getMap().put("name", specialist.getName());
//                getMap().put("surname", specialist.getSurname());
//
//                windowsController.openWindowResizable("mainMenu.fxml", getStage(), getInstance(),
//                        mainMenuController, "Main menu", 600, 640);
//            } else {
//
//                alert.setHeaderText("Status code: " + statusCode);
//                alert.setContentText("Login or password incorrect!");
//                alert.showAndWait();
//            }
        }
    }

    public void signUp(ActionEvent event) throws IOException {
        windowsController.openWindow("specialist/registrationMenu.fxml", getStage(), getInstance(),
                registrationMenuController,"Registration", 408, 400);
    }

}
