package com.controllers.windows.dataset;

import com.controllers.windows.menu.MenuController;
import com.hazelcast.core.HazelcastInstance;
import com.tools.Constant;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ChangeConfigurationMenuController extends MenuController {

    @FXML
    private Label label_DatasetName;

    public void initialize(Stage stage, HazelcastInstance hazelcastInstance, Stage newWindow, boolean change){
        userMap = hazelcastInstance.getMap("userMap");
        stage.setOnHidden(event -> {
            hazelcastInstance.getLifecycleService().shutdown();
            getMap().remove("datasetName");
        });
        setStage(stage);
        setInstance(hazelcastInstance);
        setNewWindow(newWindow);
        label_DatasetName.setText(getMap().get("datasetName").toString());
        if (!change) {

        }
        String[] str = Constant.getAuth();
        System.out.println(str[0]);
        System.out.println(str[1]);
    }
}
