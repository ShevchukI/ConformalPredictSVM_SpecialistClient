package com.controllers.windows.dataset;

import com.controllers.windows.menu.MenuController;
import com.tools.Constant;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ChangeConfigurationMenuController extends MenuController {

    @FXML
    private Label label_DatasetName;

    public void initialize(Stage stage, Stage newWindow, boolean change){
        stage.setOnHidden(event -> {
            Constant.getMap().remove("datasetName");
            Constant.getInstance().getLifecycleService().shutdown();
        });
        setStage(stage);
        setNewWindow(newWindow);
        label_DatasetName.setText(Constant.getMap().get("datasetName").toString());
        if (!change) {

        }
        String[] str = Constant.getAuth();
        System.out.println(str[0]);
        System.out.println(str[1]);
    }
}
