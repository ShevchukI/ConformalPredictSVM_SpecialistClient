package com.controllers.windows.menu;

import com.controllers.windows.specialist.ChangeInfoMenuController;
import com.controllers.windows.specialist.LoginMenuController;
import com.tools.Constant;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.io.IOException;

/**
 * Created by Admin on 07.01.2019.
 */
public class MenuBarController extends MenuController {

    private LoginMenuController loginMenuController;
    private ChangeInfoMenuController changeInfoMenuController;
    private WindowsController windowsController;

    private MenuController menuController;

    public void init(MenuController menuController) {
        this.menuController = menuController;
        loginMenuController = new LoginMenuController();
        changeInfoMenuController = new ChangeInfoMenuController();
        windowsController = new WindowsController();
    }

    public void closeApplication(ActionEvent event) {
        menuController.getStage().close();
    }

    public void signOut(ActionEvent event) throws IOException {
        windowsController.openWindow("specialist/loginMenu", menuController.getStage(), loginMenuController,
                "Login menu", 350, 250);
    }

    public void changeName(ActionEvent event) throws IOException {
        windowsController.openNewModalWindow("specialist/changeName", menuController.getStage(),
                changeInfoMenuController, "Change name and surname", true, 400, 200);
    }

    public void changePassword(ActionEvent event) throws IOException {
        windowsController.openNewModalWindow("specialist/changePassword", menuController.getStage(),
                changeInfoMenuController, "Change password", false, 400, 200);

    }

    public void about(ActionEvent event) {
       Constant.getAlert(null, "Client: Peryite\nServer: DayRo", Alert.AlertType.INFORMATION);
    }
}
