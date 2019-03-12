package com.controllers.windows.menu;

import com.tools.Constant;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Admin on 10.01.2019.
 */
public abstract class MenuController {

    private Stage stage;
    private Stage newWindow;

    public void initialize(Stage stage) throws IOException {
        stage.setOnHidden(event -> {
            Constant.getInstance().getLifecycleService().shutdown();
        });
        setStage(stage);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public void setNewWindow(Stage newWindow) {
        this.newWindow = newWindow;
    }

    public Stage getNewWindow() {
        return newWindow;
    }

    public boolean checkStatusCode(int statusCode) {
        switch (statusCode) {
            case 200:
                return true;
            case 201:
                return true;
            case 401:
                Constant.getAlert("Unauthorized: login or password incorrect!",
                        "Error code: " + statusCode, Alert.AlertType.ERROR);
                return false;
            case 404:
                Constant.getAlert("Error!", String.valueOf(statusCode), Alert.AlertType.ERROR);
            case 504:
                Constant.getAlert("Connection to the server is missing!",
                        "Error code: " + statusCode, Alert.AlertType.ERROR);
                return false;
            default:
                return false;
        }
    }

    public void initialize(Stage stage, Stage newWindow, boolean change) throws IOException {
    }

    public void initialize(Stage stage, Stage newWindow) throws IOException {
    }

}
