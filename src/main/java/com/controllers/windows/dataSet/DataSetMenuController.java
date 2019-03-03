package com.controllers.windows.dataSet;

import com.controllers.requests.ConfigurationController;
import com.controllers.requests.DataSetController;
import com.controllers.windows.menu.MainMenuController;
import com.controllers.windows.menu.MenuBarController;
import com.controllers.windows.menu.MenuController;
import com.controllers.windows.menu.WindowsController;
import com.models.ConfigurationEntity;
import com.models.ConfigurationPage;
import com.models.DataSet;
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

    private WindowsController windowsController = new WindowsController();
    private DataSet dataSet;
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
    private TableColumn tableColumn_AllOwner;
    @FXML
    private TableColumn tableColumn_AllActive;
    @FXML
    private TableColumn<ConfigurationEntity, Number> tableColumn_MyNumber;
    @FXML
    private TableColumn tableColumn_MyName;
    @FXML
    private TableColumn tableColumn_MyOwner;
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
        response = dataSetController.getDataSetById(Constant.getAuth(),
                Integer.parseInt(Constant.getMapByName("dataSet").get("id").toString()));
        statusCode = response.getStatusLine().getStatusCode();
        if (checkStatusCode(statusCode)) {
            dataSet = new DataSet().fromJson(response);
//            if (dataSet.isActive()) {
//                dataSet.setVisibleActive(true);
//            } else {
//                dataSet.setVisibleActive(false);
//            }
            textField_Name.setText(dataSet.getName());
            textArea_Description.setText(dataSet.getDescription());
            if (dataSet.isActive()) {
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

        response = dataSetController.getDataSetObjects(Constant.getAuth(), dataSet.getId());
        String responseString = Constant.responseToString(response);
        tableView_Object = fillTableFromResponseString(responseString);

        allPageIndex = 1;
        allPageIndex = Integer.parseInt(Constant.getMapByName("misc").get("pageIndexAllConfiguration").toString());
        setSettingColumnTable(allPageIndex, tableView_AllConfiguration, tableColumn_AllNumber, tableColumn_AllName, tableColumn_AllOwner, tableColumn_AllActive);
        pagination_AllConfiguration.setPageFactory(this::createAllPage);

        myPageIndex = 1;
        myPageIndex = Integer.parseInt(Constant.getMapByName("misc").get("pageIndexAllConfiguration").toString());
        setSettingColumnTable(myPageIndex, tableView_MyConfiguration, tableColumn_MyNumber, tableColumn_MyName, tableColumn_MyOwner, tableColumn_MyActive);
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
                            ObservableList<ConfigurationEntity> list, Pagination pagination, Label labelCount) {
        try {
            response = configurationController.getConfigurationAllPage(Constant.getAuth(),
                    dataSet.getId(), tablePageIndex, objectOnPage, allPage);
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
//    public ObservableList getOListAfterFillPage(int pageIndx, HttpResponse response, ObservableList<ConfigurationEntity> list,
//                                                Pagination pagination, TableView<ConfigurationEntity> tableView, Label label_Count) throws IOException {
//
//        statusCode = response.getStatusLine().getStatusCode();
//        if (checkStatusCode(statusCode)) {
//            configurationPage = new ConfigurationPage().fromJson(response);
//            list = FXCollections.observableList(configurationPage.getDatasetConfigurationEntities());
////            for (ConfigurationEntity configurationEntity : configurationPage.getDatasetConfigurationEntities()) {
////                if (configurationEntity.isActive()) {
////                    configurationEntity.setVisibleActive(true);
////                } else {
////                    configurationEntity.setVisibleActive(false);
////                }
////            }
//        }
//        if (list.isEmpty()) {
//            pagination.setPageCount(1);
//            pagination.setCurrentPageIndex(1);
//        } else {
//            pagination.setPageCount(configurationPage.getNumberOfPages());
//            pagination.setCurrentPageIndex(pageIndx - 1);
//        }
//        tableView.setItems(list);
//        label_Count.setText(String.valueOf(list.size()));
//        return list;
//    }

    public void viewConfiguration() throws IOException {
        if (tab_AllConfiguration.isSelected()) {
            Constant.getMapByName("misc").put("configurationId",
                    tableView_AllConfiguration.getSelectionModel().getSelectedItem().getId());
        }
        if (tab_MyConfiguration.isSelected()) {
            Constant.getMapByName("misc").put("configurationId",
                    tableView_MyConfiguration.getSelectionModel().getSelectedItem().getId());
        }
        Constant.getMapByName("dataSet").put("name", dataSet.getName());
        Constant.getMapByName("dataSet").put("column", dataSet.getColumns());
        windowsController.openNewModalWindow("dataSet/changeConfigurationMenu", getStage(), changeConfigurationMenuController,
                "Add configuration", true, 870, 540);
    }


    public void save(ActionEvent event) throws IOException {
        response = dataSetController.changeDataSet(Constant.getAuth(),
                new DataSet(dataSet.getId(), textField_Name.getText(), textArea_Description.getText(), dataSet.getColumns()));
        statusCode = response.getStatusLine().getStatusCode();
        if (!checkStatusCode(statusCode)) {
            Constant.getAlert(null, "Don`t save!", Alert.AlertType.ERROR);
        }
        if (!choiceBox_Activate.getSelectionModel().getSelectedItem().equals(dataSet.getVisibleActive())) {
            response = dataSetController.changeActive(Constant.getAuth(), dataSet.getId(), !dataSet.isActive());
            statusCode = response.getStatusLine().getStatusCode();
            if (!checkStatusCode(statusCode)) {
                Constant.getAlert(null, "Active don`t change!", Alert.AlertType.ERROR);
            }
        }
    }


    public void backToMainMenu(ActionEvent event) throws IOException {
        windowsController.openWindowResizable("menu/mainMenu", getStage(), mainMenuController,
                "Main menu", 600, 640);
    }

    public void addConfiguration(ActionEvent event) throws IOException {
        Constant.getMapByName("dataSet").put("name", dataSet.getName());
        Constant.getMapByName("dataSet").put("column", dataSet.getColumns());
        windowsController.openNewModalWindow("dataSet/changeConfigurationMenu", getStage(), changeConfigurationMenuController,
                "Add configuration", false, 870, 540);
    }

    public TableView<List<String>> fillTableFromResponseString(String responseString) {
        ArrayList<String> listName = new ArrayList<>();
        String[] str = dataSet.getColumns().split(",");
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

    public void setSettingColumnTable(int pageIndex, TableView tableView,
                                      TableColumn<ConfigurationEntity, Number> columnNumber,
                                      TableColumn columnName,
                                      TableColumn columnOwner,
                                      TableColumn columnActive) {
        columnNumber.setSortable(false);
        columnNumber.setCellValueFactory(column -> new ReadOnlyObjectWrapper<Number>((tableView.getItems().
                indexOf(column.getValue()) + 1) + (pageIndex - 1) * objectOnPage));
        columnName.setCellValueFactory(new PropertyValueFactory<ConfigurationEntity, String>("name"));
        columnName.setSortable(false);
        columnOwner.setCellValueFactory(new PropertyValueFactory<ConfigurationEntity, String>("owner"));
        columnActive.setCellValueFactory(new PropertyValueFactory<ConfigurationEntity, String>("visibleActive"));
        columnActive.setSortable(false);
        tableView.setRowFactory(tv -> {
            TableRow<DataSet> row = new TableRow<>();
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
            activateConfiguration(allPageIndex, myPageIndex, true, tableView_AllConfiguration,
                    tableView_MyConfiguration, allConfigurationObservableList,
                    myConfigurationObservableList, pagination_AllConfiguration,
                    pagination_MyConfiguration, label_AllCount, label_MyCount);
        }
        if (tab_MyConfiguration.isSelected()) {
            activateConfiguration(myPageIndex, allPageIndex, false, tableView_MyConfiguration,
                    tableView_AllConfiguration, myConfigurationObservableList,
                    allConfigurationObservableList, pagination_MyConfiguration,
                    pagination_AllConfiguration, label_MyCount, label_AllCount);
        }
    }

    public void activateConfiguration(int firstPageIndex, int secondPageIndex, boolean allPage,
                                      TableView<ConfigurationEntity> firstTable,
                                      TableView<ConfigurationEntity> secondTable,
                                      ObservableList<ConfigurationEntity> firstList,
                                      ObservableList<ConfigurationEntity> secondList,
                                      Pagination firstPagination, Pagination secondPagination,
                                      Label firstCount, Label secondCount) throws IOException {
        int configId = firstTable.getSelectionModel().getSelectedItem().getId();
        response = configurationController.activateConfiguration(Constant.getAuth(), configId);
        statusCode = response.getStatusLine().getStatusCode();
        if (checkStatusCode(statusCode)) {
            createPage(firstPageIndex, allPage, firstTable, firstList,
                    firstPagination, firstCount);
            createPage(secondPageIndex, !allPage, secondTable, secondList,
                    secondPagination, secondCount);
        } else {
            System.out.println(statusCode + " : " + configId);
        }
    }


    public void deleteConfiguration(ActionEvent event) throws IOException {
        boolean result;
        if (tab_AllConfiguration.isSelected()) {
            result = Constant.questionOkCancel("Do you really want to delete configuration: "
                    + tableView_AllConfiguration.getSelectionModel().getSelectedItem().getName() + "?");
            if (result) {
                deleteConfiguration(tableView_AllConfiguration);
            }
        }
        if (tab_MyConfiguration.isSelected()) {
            result = Constant.questionOkCancel("Do you really want to delete configuration: "
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
