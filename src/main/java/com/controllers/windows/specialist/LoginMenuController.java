package com.controllers.windows.specialist;

import com.controllers.requests.SpecialistController;
import com.controllers.windows.menu.MainMenuController;
import com.controllers.windows.menu.MenuController;
import com.controllers.windows.menu.WindowsController;
import com.models.SpecialistEntity;
import com.tools.Constant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.http.HttpResponse;

import java.io.IOException;

/**
 * Created by Admin on 06.01.2019.
 */
public class LoginMenuController extends MenuController {

    private MainMenuController mainMenuController;
    private WindowsController windowsController;

    @FXML
    private TextField textField_Login;
    @FXML
    private PasswordField passwordField_Password;

    public void initialize(Stage stage){
        stage.setOnHidden(event -> {
            Constant.getInstance().getLifecycleService().shutdown();
        });
        setStage(stage);
        mainMenuController = new MainMenuController();
        windowsController = new WindowsController();
    }

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
            String[] authorization = new String[2];
            authorization[0] = textField_Login.getText();
            authorization[1] = passwordField_Password.getText();
            HttpResponse response = SpecialistController.specialistAuthorization(authorization);
            setStatusCode(response.getStatusLine().getStatusCode());
            if(checkStatusCode(getStatusCode())){
                Constant.fillMap(new SpecialistEntity().fromJson(response), textField_Login.getText(), passwordField_Password.getText());
                windowsController.openWindowResizable("menu/mainMenu", getStage(),
                        mainMenuController, "Main menu", 600, 640);
            }
        }
    }

}
