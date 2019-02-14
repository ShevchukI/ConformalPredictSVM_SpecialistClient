package com.controllers.windows.dataset;

import com.controllers.requests.DataSetController;
import com.controllers.windows.menu.MainMenuController;
import com.controllers.windows.menu.MenuBarController;
import com.controllers.windows.menu.MenuController;
import com.controllers.windows.menu.WindowsController;
import com.models.Dataset;
import com.sun.javafx.collections.ObservableListWrapper;
import com.tools.Constant;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 12.02.2019.
 */
public class DataSetMenuController extends MenuController {
    @Autowired
    HttpResponse response;
    @Autowired
    MainMenuController mainMenuController;

//    private ObservableList observableList;
    private WindowsController windowsController = new WindowsController();
    private Dataset dataset;
    private DataSetController dataSetController = new DataSetController();
    private ChangeConfigurationMenuController changeConfigurationMenuController = new ChangeConfigurationMenuController();
    int statusCode;

    @FXML
    private MenuBarController menuBarController;
    @FXML
    private TextField textField_Name;
    @FXML
    private TextArea textArea_Description;
    @FXML
    private Button button_Test;
    @FXML
    private TableView tableView_Configuration;
    @FXML
    private ChoiceBox<Boolean> choiceBox_Activate;

    public void initialize(Stage stage) throws IOException {
        stage.setOnHidden(event -> {
            Constant.getInstance().getLifecycleService().shutdown();
        });
        setStage(stage);
        menuBarController.init(this);
        List<Boolean> list = new ArrayList<Boolean>();
        list.add(true);
        list.add(false);
        ObservableList<Boolean> observableList = new ObservableListWrapper<Boolean>(list);
        choiceBox_Activate.setItems(observableList);
        response = dataSetController.getDatasetById(Constant.getAuth(),
                Integer.parseInt(Constant.getMap().get("datasetId").toString()));
        statusCode = response.getStatusLine().getStatusCode();
        if (checkStatusCode(statusCode)) {
            dataset = new Dataset().fromJson(response);
            textField_Name.setText(dataset.getName());
            textArea_Description.setText(dataset.getDescription());
            choiceBox_Activate.setValue(dataset.isActive());
        }
        button_Test.disableProperty().bind(Bindings.isEmpty(tableView_Configuration.getSelectionModel().getSelectedItems()));
    }

    public void save(ActionEvent event) throws IOException {
        response = dataSetController.changeDataset(Constant.getAuth(),
                new Dataset(dataset.getId(), textField_Name.getText(), textArea_Description.getText(),
                        choiceBox_Activate.getSelectionModel().getSelectedItem().booleanValue()));
        statusCode = response.getStatusLine().getStatusCode();
        if(checkStatusCode(statusCode)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Saved!");
            alert.showAndWait();
        } else {
            System.out.println(statusCode);
        }
    }


    public void backToMainMenu(ActionEvent event) throws IOException {
        windowsController.openWindowResizable("menu/mainMenu.fxml", getStage(), mainMenuController,
                "Main menu", 600, 640);
    }

    public void addConfiguration(ActionEvent event) throws IOException {
        Constant.getMap().put("datasetName", dataset.getName());
        windowsController.openNewModalWindow("dataset/changeConfigurationMenu.fxml", getStage(), changeConfigurationMenuController,
                "Add configuration", true, 870, 560);
    }
}
