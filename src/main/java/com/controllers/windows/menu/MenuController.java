package com.controllers.windows.menu;

import com.tools.Constant;
import com.tools.Placeholder;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by Admin on 10.01.2019.
 */
public abstract class MenuController {

//    @Autowired
//    protected HazelcastInstance instance;

//    @Autowired
//    protected IMap<String, String> userMap;


    private Placeholder placeholder = new Placeholder();
    private Stage stage;
    private Stage newWindow;

    public void initialize(Stage stage) throws IOException {
//        userMap = Hazelcast.getHazelcastInstanceByName("mainInstance").getMap("userMap");
        stage.setOnHidden(event -> {
            Constant.getInstance().getLifecycleService().shutdown();
//            Hazelcast.getHazelcastInstanceByName("mainSpecialistInstance").getLifecycleService().shutdown();
        });
        setStage(stage);
//        setInstance(hazelcastInstance);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

//    public HazelcastInstance getInstance() {
//        return instance;
////    }

//    public void setInstance(HazelcastInstance hazelcastInstance) {
//        instance = hazelcastInstance;
//    }

//    public IMap getMap() {
//        return userMap;
//    }

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

//    public void fillMap(Specialist specialistFromJson, String login, String password) {
//        Specialist specialist = specialistFromJson;
//        String key;
//        String vector;
//        key = new Encryptor().genRandString();
//        vector = new Encryptor().genRandString();
//        getMap().put("key", key);
//        getMap().put("vector", vector);
//        getMap().put("login", new Encryptor().encrypt(key, vector, login));
//        getMap().put("password", new Encryptor().encrypt(key, vector, password));
//        getMap().put("id", specialist.getId());
//        getMap().put("name", specialist.getName());
//        getMap().put("surname", specialist.getSurname());
//        getMap().put("pageIndex", "1");
//    }

    public void getPlaceholderAlert(ActionEvent event) {
        placeholder.getAlert();
    }

    public void getAlert(String header, String content, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public boolean questionOkCancel(String questionText){
        ButtonType ok = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert questionOfCancellation = new Alert(Alert.AlertType.WARNING, questionText, ok, cancel);
        questionOfCancellation.setHeaderText(null);
        Optional<ButtonType> result = questionOfCancellation.showAndWait();
        if(result.orElse(cancel) == ok){
            return true;
        } else {
            return false;
        }
    }

    public void initialize(Stage stage, Stage newWindow, boolean change) throws IOException {
    }

    public void initialize(Stage stage, Stage newWindow) throws IOException {
    }

}
