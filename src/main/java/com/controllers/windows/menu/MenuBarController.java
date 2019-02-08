package com.controllers.windows.menu;

import com.controllers.windows.dataset.DataSetMenuController;
import com.controllers.windows.specialist.ChangeInfoMenuController;
import com.controllers.windows.specialist.LoginMenuController;
import javafx.event.ActionEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by Admin on 07.01.2019.
 */
public class MenuBarController extends MenuController {

    //
    @Autowired
    LoginMenuController loginMenuController;
    //
    @Autowired
    ChangeInfoMenuController changeInfoMenuController;


    private WindowsController windowsController = new WindowsController();
    //
//
//    private Placeholder placeholder = new Placeholder();
//
//
    private MenuController menuController;

    private DataSetMenuController dataSetMenuController;

    //
//
    public void init(MenuController menuController) {
        this.menuController = menuController;
    }


    //
    public void closeApplication(ActionEvent event) {
        menuController.getStage().setOnHiding(event1 -> {
            menuController.getInstance().shutdown();
        });
        menuController.getStage().close();
    }

    //
//    public void getPlaceholderAlert(ActionEvent event) {
//        placeholder.getAlert();
//    }
//
    public void signOut(ActionEvent event) throws IOException {
        windowsController.openWindow("specialist/loginMenu.fxml", menuController.getStage(), menuController.getInstance(), loginMenuController,
                "Login menu", 350, 190);
    }

    //
    public void changeName(ActionEvent event) throws IOException {
        windowsController.openNewModalWindow("specialist/changeName.fxml", menuController.getStage(), menuController.getInstance(),
                changeInfoMenuController, "Change name and surname", true, 400, 200);
    }

    //
    public void changePassword(ActionEvent event) throws IOException {
        windowsController.openNewModalWindow("specialist/changePassword.fxml", menuController.getStage(), menuController.getInstance(),
                changeInfoMenuController, "Change password", false, 400, 200);

    }

    public void about(ActionEvent event) {
        getPlaceholderAlert(event);
    }
}
