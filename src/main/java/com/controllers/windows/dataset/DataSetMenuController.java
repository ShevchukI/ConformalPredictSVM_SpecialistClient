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
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.BasicResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
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
    private TableView<List<String>> tableView_Object;
    @FXML
    private ChoiceBox<String> choiceBox_Activate;

    public void initialize(Stage stage) throws IOException {
        stage.setOnHidden(event -> {
            Constant.getInstance().getLifecycleService().shutdown();
        });
        setStage(stage);
        menuBarController.init(this);
        List<String> list = new ArrayList<String>();
        list.add("Enabled");
        list.add("Disabled");
        ObservableList<String> observableList = new ObservableListWrapper<String>(list);
        choiceBox_Activate.setItems(observableList);
        response = dataSetController.getDatasetById(Constant.getAuth(),
                Integer.parseInt(Constant.getMapByName("dataset").get("id").toString()));
        statusCode = response.getStatusLine().getStatusCode();
        if (checkStatusCode(statusCode)) {
            dataset = new Dataset().fromJson(response);
            if (dataset.isActive()) {
                dataset.setVisibleActive(true);
            } else {
                dataset.setVisibleActive(false);
            }
            textField_Name.setText(dataset.getName());
            textArea_Description.setText(dataset.getDescription());
            if (dataset.isActive()) {
                choiceBox_Activate.setValue(list.get(0));
            } else {
                choiceBox_Activate.setValue(list.get(1));
            }
        }
        button_Test.disableProperty().bind(Bindings.isEmpty(tableView_Configuration.getSelectionModel().getSelectedItems()));
//        File file = new File("F:\\dataSet.txt");
//        tableView_Object = readTabDelimitedFileIntoTable(file.toPath());

        response = dataSetController.getDatasetObjects(Constant.getAuth(), dataset.getId());
        String responseString = new BasicResponseHandler().handleResponse(response);
        File file = new File("C:\\aaaaaaaaaasc.txt");
        FileWriter writer = new FileWriter(file);
        writer.write(responseString);
        writer.flush();
        writer.close();
        tableView_Object = readTabDelimitedFileIntoTable(file.toPath());
        file.deleteOnExit();


    }

    public void save(ActionEvent event) throws IOException {
        response = dataSetController.changeDataset(Constant.getAuth(),
                new Dataset(dataset.getId(), textField_Name.getText(), textArea_Description.getText()));
        statusCode = response.getStatusLine().getStatusCode();
        if (!checkStatusCode(statusCode)) {
            getAlert(null, "Don`t save!", Alert.AlertType.ERROR);
        }
        if (!choiceBox_Activate.getSelectionModel().getSelectedItem().equals(dataset.getVisibleActive())) {
            response = dataSetController.changeActive(Constant.getAuth(), dataset.getId(), !dataset.isActive());
            statusCode = response.getStatusLine().getStatusCode();
            if (!checkStatusCode(statusCode)) {
                getAlert(null, "Active don`t change!", Alert.AlertType.ERROR);
            }
        }
    }


    public void backToMainMenu(ActionEvent event) throws IOException {
        windowsController.openWindowResizable("menu/mainMenu", getStage(), mainMenuController,
                "Main menu", 600, 640);
    }

    public void addConfiguration(ActionEvent event) throws IOException {
        Constant.getMapByName("dataset").put("name", dataset.getName());
        Constant.getMapByName("dataset").put("column", dataset.getColumns());
        windowsController.openNewModalWindow("dataset/changeConfigurationMenu", getStage(), changeConfigurationMenuController,
                "Add configuration", true, 870, 560);
    }

    public void fillObjectTable(TableView tableView) throws IOException {

//        response = dataSetController.getDatasetObjects(Constant.getAuth(), 289);
//        String responseString = new BasicResponseHandler().handleResponse(response);
//        String[] mainContent = responseString.split("\n");
////        String[] split = null;
//        ArrayList<String> arrayList = new ArrayList<>();
//        for(int i = 0; i < 2; i++){
////        for(int i = 0; i < mainContent.length; i++){
//            arrayList.add(mainContent[i]);
//
//           String[] split = mainContent[i].split(",");
////            for(int j = 0; j<split.length; j ++) {
////                TableColumn<String, String> column = new TableColumn<>("")
////            }
//            TableColumn<String, String> columnId = new TableColumn<>("ID");
//            tableView.getColumns().addAll(columnId);
//            columnId.setCellValueFactory(data -> new SimpleStringProperty(split[0]));
//            TableColumn<String, String> columnClass = new TableColumn<>("Class");
//            tableView.getColumns().addAll(columnId);
//            columnId.setCellValueFactory(data -> new SimpleStringProperty(split[1]));
//        }
//        ObservableList<String> details = FXCollections.observableArrayList(arrayList);
//
//        tableView.setItems(details);
//        List<String> list = new ArrayList<>();
//        list.add("String1");
//        list.add("String2");
//        list.add("String3");
//        list.add("String4");
//        list.add("String5");
//        list.add("String6");
//        ObservableList<String> details = FXCollections.observableArrayList(list);
//        for(int i = 0; i<list.size(); i++){
//            TableColumn<String, String> col = new TableColumn<>("Strings"+i);
//            tableView.getColumns().addAll(col);
//
//            col.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
//        }
//        tableView.setItems(details);
    }


    public TableView<List<String>> readTabDelimitedFileIntoTable(Path file) throws IOException {
//        TableView<List<String>> table = new TableView<>();
        Files.lines(file).map(line -> line.split(",")).forEach(values -> {
            // Add extra columns if necessary:
            ArrayList<String> listName = new ArrayList<>();
            String[] str = dataset.getColumns().split(",");
            for (int i = 0; i < str.length; i++) {
                listName.add(str[i]);
            }
            for (int i = tableView_Object.getColumns().size(); i < values.length; i++) {
                TableColumn<List<String>, String> col = new TableColumn<>(listName.get(i));
                col.setMinWidth(80);
                final int colIndex = i;
                col.setCellValueFactory(data -> {
                    List<String> rowValues = data.getValue();
                    String cellValue;
                    if (colIndex < rowValues.size()) {
                        cellValue = rowValues.get(colIndex);
                    } else {
                        cellValue = "";
                    }
                    return new ReadOnlyStringWrapper(cellValue);
                });
                tableView_Object.getColumns().add(col);
            }
            // add row:
            tableView_Object.getItems().add(Arrays.asList(values));
        });
        return tableView_Object;
    }

}
