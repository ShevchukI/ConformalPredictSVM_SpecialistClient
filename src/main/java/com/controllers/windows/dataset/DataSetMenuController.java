package com.controllers.windows.dataset;

import com.controllers.requests.ConfigurationController;
import com.controllers.requests.DataSetController;
import com.controllers.windows.menu.MainMenuController;
import com.controllers.windows.menu.MenuBarController;
import com.controllers.windows.menu.MenuController;
import com.controllers.windows.menu.WindowsController;
import com.models.ConfigurationEntity;
import com.models.ConfigurationPage;
import com.models.Dataset;
import com.sun.javafx.collections.ObservableListWrapper;
import com.tools.Constant;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
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
    private ConfigurationController configurationController = new ConfigurationController();
    private ChangeConfigurationMenuController changeConfigurationMenuController = new ChangeConfigurationMenuController();
    int statusCode;
    private int objectOnPage = 30;
    private int allPageIndex;
    private int myPageIndex;
    private ObservableList<ConfigurationEntity> allConfigurationObservableList;
    private ObservableList<ConfigurationEntity> myConfigurationObservableList;
    private ConfigurationPage configurationPage;

    @FXML
    private MenuBarController menuBarController;
    @FXML
    private TextField textField_Name;
    @FXML
    private TextArea textArea_Description;
    @FXML
    private Button button_View;
    @FXML
    private Button button_Delete;
    @FXML
    private Button button_Activate;
    @FXML
    private Tab tab_AllConfiguration;
    @FXML
    private Tab tab_MyConfiguration;
    @FXML
    private TableView<ConfigurationEntity> tableView_AllConfiguration;
    @FXML
    private TableView<ConfigurationEntity> tableView_MyConfiguration;
    @FXML
    private TableView<List<String>> tableView_Object;
    @FXML
    private ChoiceBox<String> choiceBox_Activate;
    @FXML
    private TableColumn<ConfigurationEntity, Number> tableColumn_AllNumber;
    @FXML
    private TableColumn tableColumn_AllName;
    @FXML
    private TableColumn tableColumn_AllActive;
    @FXML
    private TableColumn<ConfigurationEntity, Number> tableColumn_MyNumber;
    @FXML
    private TableColumn tableColumn_MyName;
    @FXML
    private TableColumn tableColumn_MyActive;
    @FXML
    private Pagination pagination_AllConfiguration;
    @FXML
    private Pagination pagination_MyConfiguration;
    @FXML
    private Label label_AllCount;
    @FXML
    private Label label_MyCount;

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
        button_Delete.disableProperty()
                .bind(Bindings.isEmpty(tableView_AllConfiguration.getSelectionModel().getSelectedItems())
                        .and(Bindings.isEmpty(tableView_MyConfiguration.getSelectionModel().getSelectedItems())));
        button_Activate.disableProperty()
                .bind(Bindings.isEmpty(tableView_AllConfiguration.getSelectionModel().getSelectedItems())
                        .and(Bindings.isEmpty(tableView_MyConfiguration.getSelectionModel().getSelectedItems())));
        button_View.disableProperty()
                .bind(Bindings.isEmpty(tableView_AllConfiguration.getSelectionModel().getSelectedItems())
                        .and(Bindings.isEmpty(tableView_MyConfiguration.getSelectionModel().getSelectedItems())));

        response = dataSetController.getDatasetObjects(Constant.getAuth(), dataset.getId());
        String responseString = Constant.responseToString(response);
        tableView_Object = fillTableFromResponseString(responseString);

        allPageIndex = 1;
        allPageIndex = Integer.parseInt(Constant.getMapByName("misc").get("pageIndexAllConfiguration").toString());
        setSettingColumnTable(allPageIndex, tableView_AllConfiguration, tableColumn_AllNumber, tableColumn_AllName, tableColumn_AllActive);
        pagination_AllConfiguration.setPageFactory(this::createAllPage);

        myPageIndex = 1;
        myPageIndex = Integer.parseInt(Constant.getMapByName("misc").get("pageIndexAllConfiguration").toString());
        setSettingColumnTable(myPageIndex, tableView_MyConfiguration, tableColumn_MyNumber, tableColumn_MyName, tableColumn_MyActive);
        pagination_MyConfiguration.setPageFactory(this::createMyPage);

    }

    private Node createAllPage(int pageIndex) {
        allPageIndex = pageIndex + 1;
        createPage(allPageIndex, true, tableView_AllConfiguration, allConfigurationObservableList,
                pagination_AllConfiguration, label_AllCount);
        return tableView_AllConfiguration;
    }

    private Node createMyPage(int pageIndex) {
        myPageIndex = pageIndex + 1;
        createPage(myPageIndex, false, tableView_MyConfiguration, myConfigurationObservableList,
                pagination_MyConfiguration, label_MyCount);
        return tableView_MyConfiguration;
    }

    private void createPage(int tablePageIndex, boolean allPage, TableView<ConfigurationEntity> tableView,
                            ObservableList list, Pagination pagination, Label labelCount) {
        try {
            response = configurationController.getConfigurationAllPage(Constant.getAuth(),
                    dataset.getId(), tablePageIndex, objectOnPage, allPage);
            list = getOListAfterFillPage(tablePageIndex, response,
                    list, pagination,
                    tableView, labelCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList getOListAfterFillPage(int pageIndx, HttpResponse response, ObservableList<ConfigurationEntity> list,
                                                Pagination pagination, TableView<ConfigurationEntity> tableView, Label label_Count) throws IOException {

        statusCode = response.getStatusLine().getStatusCode();
        if (checkStatusCode(statusCode)) {
            configurationPage = new ConfigurationPage().fromJson(response);
            list = FXCollections.observableList(configurationPage.getDatasetConfigurationEntities());
            for (ConfigurationEntity configurationEntity : configurationPage.getDatasetConfigurationEntities()) {
                if (configurationEntity.isActive()) {
                    configurationEntity.setVisibleActive(true);
                } else {
                    configurationEntity.setVisibleActive(false);
                }
            }
        }
        if (list.isEmpty()) {
            pagination.setPageCount(1);
            pagination.setCurrentPageIndex(1);
        } else {
            pagination.setPageCount(configurationPage.getNumberOfPages());
            pagination.setCurrentPageIndex(pageIndx - 1);
        }
        tableView.setItems(list);
        label_Count.setText(String.valueOf(list.size()));
        return list;
    }

    public void viewConfiguration() throws IOException {
        if (tab_AllConfiguration.isSelected()) {
            Constant.getMapByName("misc").put("configurationId",
                    tableView_AllConfiguration.getSelectionModel().getSelectedItem().getId());
        }
        if (tab_MyConfiguration.isSelected()) {
            Constant.getMapByName("misc").put("configurationId",
                    tableView_MyConfiguration.getSelectionModel().getSelectedItem().getId());
        }
        Constant.getMapByName("dataset").put("name", dataset.getName());
        Constant.getMapByName("dataset").put("column", dataset.getColumns());
        windowsController.openNewModalWindow("dataset/changeConfigurationMenu", getStage(), changeConfigurationMenuController,
                "Add configuration", true, 870, 540);
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
                "Add configuration", false, 870, 540);
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


//    public TableView<List<String>> readTabDelimitedFileIntoTable(Path file) throws IOException {
////        TableView<List<String>> table = new TableView<>();
//
//        Files.lines(file).map(line -> line.split(",")).forEach(values -> {
//            // Add extra columns if necessary:
//            ArrayList<String> listName = new ArrayList<>();
//            String[] str = dataset.getColumns().split(",");
//            for (int i = 0; i < str.length; i++) {
//                listName.add(str[i]);
//            }
//            for (int i = tableView_Object.getColumns().size(); i < values.length; i++) {
//                TableColumn<List<String>, String> col = new TableColumn<>(listName.get(i));
//                col.setMinWidth(80);
//                final int colIndex = i;
//                col.setCellValueFactory(data -> {
//                    List<String> rowValues = data.getValue();
//                    String cellValue;
//                    if (colIndex < rowValues.size()) {
//                        cellValue = rowValues.get(colIndex);
//                    } else {
//                        cellValue = "";
//                    }
//                    return new ReadOnlyStringWrapper(cellValue);
//                });
//                tableView_Object.getColumns().add(col);
//            }
//            // add row:
//            tableView_Object.getItems().add(Arrays.asList(values));
//        });
//        return tableView_Object;
//    }

    public TableView<List<String>> fillTableFromResponseString(String responseString) {
        ArrayList<String> listName = new ArrayList<>();
        String[] str = dataset.getColumns().split(",");
        for (int i = 0; i < str.length; i++) {
            listName.add(str[i]);
        }
        String[] lines = responseString.split("\n");
        for (String line : lines) {
            // Add extra columns if necessary:
            String[] content = line.split(",");
            for (int i = tableView_Object.getColumns().size(); i < content.length; i++) {
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
                    col.setStyle("-fx-alignment: CENTER");
                    return new ReadOnlyStringWrapper(cellValue);
                });
                tableView_Object.getColumns().add(col);
            }
            // add row:
            tableView_Object.getItems().add(Arrays.asList(content));
        }
        return tableView_Object;
    }

    public void setSettingColumnTable(int pageIndex, TableView tableView, TableColumn<ConfigurationEntity, Number> columnNumber, TableColumn columnName,
                                      TableColumn columnActive) {
        columnNumber.setSortable(false);
        columnNumber.setCellValueFactory(column -> new ReadOnlyObjectWrapper<Number>((tableView.getItems().
                indexOf(column.getValue()) + 1) + (pageIndex - 1) * objectOnPage));
        columnName.setCellValueFactory(new PropertyValueFactory<ConfigurationEntity, String>("name"));
        columnName.setSortable(false);
        columnActive.setCellValueFactory(new PropertyValueFactory<ConfigurationEntity, String>("visibleActive"));
        columnActive.setSortable(false);
        tableView.setRowFactory(tv -> {
            TableRow<Dataset> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    try {
                        viewConfiguration();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
    }

    public void activateConfiguration(ActionEvent event) throws IOException {
        if (tab_AllConfiguration.isSelected()) {
            activateConfiguration(allPageIndex, true, tableView_AllConfiguration,
                    allConfigurationObservableList, pagination_AllConfiguration, label_AllCount);
        }
        if (tab_MyConfiguration.isSelected()) {
            activateConfiguration(myPageIndex, false, tableView_MyConfiguration,
                    myConfigurationObservableList, pagination_MyConfiguration, label_MyCount);
        }
    }

    public void activateConfiguration(int tablePageIndex, boolean allPage, TableView<ConfigurationEntity> tableView,
                                      ObservableList list, Pagination pagination, Label labelCount) throws IOException {
        int configId = tableView.getSelectionModel().getSelectedItem().getId();
        response = configurationController.activateConfiguration(Constant.getAuth(), configId);
        statusCode = response.getStatusLine().getStatusCode();
        if (checkStatusCode(statusCode)) {
            createPage(tablePageIndex, allPage, tableView, list, pagination, labelCount);
        } else {
            System.out.println(statusCode);
        }
    }

    public void deleteConfiguration(ActionEvent event) throws IOException {
        boolean result;
        if (tab_AllConfiguration.isSelected()) {
            result = questionOkCancel("Do you really want to delete configuration: "
                    + tableView_AllConfiguration.getSelectionModel().getSelectedItem().getName() + "?");
            if (result) {
                deleteConfiguration(tableView_AllConfiguration);
            }
        }
        if (tab_MyConfiguration.isSelected()) {
            result = questionOkCancel("Do you really want to delete configuration: "
                    + tableView_MyConfiguration.getSelectionModel().getSelectedItem().getName() + "?");
            if (result) {
                deleteConfiguration(tableView_MyConfiguration);
            }
        }
    }

    public void deleteConfiguration(TableView<ConfigurationEntity> tableView) throws IOException {
        int cofigId = tableView.getSelectionModel().getSelectedItem().getId();
        response = configurationController.deleteConfiguration(Constant.getAuth(), cofigId);
        statusCode = response.getStatusLine().getStatusCode();
        if (checkStatusCode(statusCode)) {
            createPage(allPageIndex, true, tableView_AllConfiguration, allConfigurationObservableList,
                    pagination_AllConfiguration, label_AllCount);
            createPage(myPageIndex, false, tableView_MyConfiguration, myConfigurationObservableList,
                    pagination_MyConfiguration, label_MyCount);
        }
    }
}
