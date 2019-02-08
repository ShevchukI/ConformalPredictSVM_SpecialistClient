package com.controllers.windows.menu;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.models.Specialist;
import com.tools.Encryptor;
import com.tools.Placeholder;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by Admin on 10.01.2019.
 */
public abstract class MenuController {

    @Autowired
    protected HazelcastInstance instance;

    @Autowired
    protected IMap<String, String> userMap;

    private Placeholder placeholder = new Placeholder();
    private Stage stage;
    private Stage newWindow;

    public void initialize(Stage stage, HazelcastInstance hazelcastInstance) throws IOException {
        userMap = hazelcastInstance.getMap("userMap");
        stage.setOnHidden(event -> {
            hazelcastInstance.getLifecycleService().shutdown();
        });
        setStage(stage);
        setInstance(hazelcastInstance);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public HazelcastInstance getInstance() {
        return instance;
    }

    public void setInstance(HazelcastInstance hazelcastInstance) {
        instance = hazelcastInstance;
    }

    public IMap getMap() {
        return userMap;
    }

    public void setNewWindow(Stage newWindow) {
        this.newWindow = newWindow;
    }

    public Stage getNewWindow() {
        return newWindow;
    }

    public boolean checkStatusCode(int statusCode) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        switch (statusCode) {
            case 200:
                return true;
            case 201:
                return true;
            case 401:
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setHeaderText("Unauthorized: login or password incorrect!");
                alert.setContentText("Error code: " + statusCode);
                alert.showAndWait();
                return false;
            case 504:
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setHeaderText("Connection to the server is missing!");
                alert.setContentText("Error code: " + statusCode);
                alert.showAndWait();
                return false;
            default:
                return false;
        }
    }

    public void fillMap(Specialist specialistFromJson, String login, String password) {
        Specialist specialist = specialistFromJson;
        String key;
        String vector;
        key = new Encryptor().genRandString();
        vector = new Encryptor().genRandString();
        getMap().put("key", key);
        getMap().put("vector", vector);
        getMap().put("login", new Encryptor().encrypt(key, vector, login));
        getMap().put("password", new Encryptor().encrypt(key, vector, password));
        getMap().put("id", specialist.getId());
        getMap().put("name", specialist.getName());
        getMap().put("surname", specialist.getSurname());
    }

    public void getPlaceholderAlert(ActionEvent event) {
        placeholder.getAlert();
    }

    public void initialize(Stage stage, HazelcastInstance hazelcastInstance, Stage newWindow, boolean change) throws IOException {
    }

    public void initialize(Stage stage, HazelcastInstance hazelcastInstance, Stage newWindow) {
    }
}
