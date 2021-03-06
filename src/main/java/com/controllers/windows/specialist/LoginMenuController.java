package com.controllers.windows.specialist;

import com.controllers.windows.menu.MainMenuController;
import com.controllers.windows.menu.MenuController;
import com.controllers.windows.menu.WindowsController;
import com.models.ModelDeveloper;
import com.models.SpecialistEntity;
import com.tools.Constant;
import com.tools.GlobalMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
    private ModelDeveloper modelDeveloper;

    @FXML
    private TextField textField_Login;
    @FXML
    private PasswordField passwordField_Password;
    @FXML
    private Button button_SignIn;

    public void initialize(Stage stage){
        setStage(stage);
        mainMenuController = new MainMenuController();
        windowsController = new WindowsController();
        button_SignIn.setGraphic(Constant.signInIcon());
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
            modelDeveloper = new ModelDeveloper(textField_Login.getText(), passwordField_Password.getText());
            HttpResponse response = modelDeveloper.authorization();
            setStatusCode(response.getStatusLine().getStatusCode());
            if(checkStatusCode(getStatusCode())){
                GlobalMap.fillMap(authorization);
                GlobalMap.getSpecialistMap().put(1, new SpecialistEntity().fromJson(response));
                windowsController.openWindowResizable("menu/mainMenu", getStage(),
                        mainMenuController, "Main menu", 1100, 640);
            }
        }
    }
}
