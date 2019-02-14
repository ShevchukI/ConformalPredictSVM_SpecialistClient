package com.controllers.windows.menu;

import com.controllers.requests.DataSetController;
import com.controllers.windows.dataset.AddDataSetMenuController;
import com.controllers.windows.dataset.DataSetMenuController;
import com.hazelcast.core.HazelcastInstance;
import com.models.Dataset;
import com.models.DatasetPage;
import com.tools.Encryptor;
import javafx.beans.property.ReadOnlyObjectWrapper;
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

/**
 * Created by Admin on 07.01.2019.
 */
public class MainMenuController extends MenuController {
    //
//    @Autowired
//    RegistrationMenuController registrationMenuController;
//
//    @Autowired
//    AddPatientAndCardMenuController addPatientAndCardMenuController;
//
//    @Autowired
//    CardMenuController cardMenuController;
//
    @Autowired
    HttpResponse response;


    private ObservableList<Dataset> datasetObservableList;
    private AddDataSetMenuController addDataSetMenuController = new AddDataSetMenuController();
    private WindowsController windowsController = new WindowsController();
    private DataSetController dataSetController = new DataSetController();
    private DataSetMenuController dataSetMenuController = new DataSetMenuController();
    private int statusCode;
    private DatasetPage datasetPage;
    private int objectOnPage = 30;
    private int pageIndx;

    @FXML
    private MenuBarController menuBarController;
    @FXML
    private Tab tab_All;
    @FXML
    private Tab tab_My;
    @FXML
    private Pagination pagination_AllDataSet;
    @FXML
    private Pagination pagination_MyDataSet;
    @FXML
    private TableView<Dataset> tableView_AllDataSetTable;
    @FXML
    private TableView<Dataset> tableView_MyDataSetTable;
    @FXML
    private TableColumn<Dataset, Number> tableColumn_AllNumber = new TableColumn<Dataset, Number>("#");
    @FXML
    private TableColumn tableColumn_AllName;
    @FXML
    private TableColumn tableColumn_AllDescription;
    @FXML
    private TableColumn tableColumn_AllActive;
    @FXML
    private TableColumn<Dataset, Number> tableColumn_MyNumber = new TableColumn<Dataset, Number>("#");
    @FXML
    private TableColumn tableColumn_MyName;
    @FXML
    private TableColumn tableColumn_MyDescription;
    @FXML
    private TableColumn tableColumn_MyActive;
    @FXML
    private Label label_AllCount;
    @FXML
    private Label label_MyCount;
    @FXML
    private Label label_Name;
    @FXML
    private Button button_View;

    public void initialize(Stage stage, HazelcastInstance hazelcastInstance) throws IOException {
        userMap = hazelcastInstance.getMap("userMap");
        stage.setOnHidden(event -> {
            hazelcastInstance.getLifecycleService().shutdown();
        });
        setStage(stage);
        setInstance(hazelcastInstance);
        getMap().remove("datasetId");
        menuBarController.init(this);
        label_Name.setText(getMap().get("name").toString() + " " + getMap().get("surname").toString());
        pageIndx = Integer.parseInt(getMap().get("pageIndex").toString());
        tableColumn_AllNumber.setSortable(false);
        tableColumn_AllNumber.setCellValueFactory(column -> new ReadOnlyObjectWrapper<Number>((tableView_AllDataSetTable.getItems().
                indexOf(column.getValue()) + 1) + (pageIndx - 1) * objectOnPage));
        tableColumn_AllName.setCellValueFactory(new PropertyValueFactory<Dataset, String>("name"));
        tableColumn_AllName.setSortable(false);
        tableColumn_AllDescription.setCellValueFactory(new PropertyValueFactory<Dataset, String>("description"));
        tableColumn_AllDescription.setSortable(false);

        tableColumn_AllActive.setCellValueFactory(new PropertyValueFactory<Dataset, String>("active"));
        tableColumn_AllActive.setSortable(false);

////        button_View.disableProperty().bind(Bindings.isEmpty(tableView_AllDataSetTable.getSelectionModel().getSelectedItems()).or(Bindings.isEmpty(tableView_MyDataSetTable.getSelectionModel().getSelectedItems())));
//        button_View.disableProperty().bind(Bindings.isEmpty(tableView_MyDataSetTable.getSelectionModel().getSelectedItems()));

        tableView_AllDataSetTable.setRowFactory(tv -> {
            TableRow<Dataset> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    try {
                        viewDataSet();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
        pagination_AllDataSet.setPageFactory(this::createAllPage);

        tableColumn_MyNumber.setSortable(false);
        tableColumn_MyNumber.setCellValueFactory(column -> new ReadOnlyObjectWrapper<Number>((tableView_MyDataSetTable.getItems().
                indexOf(column.getValue()) + 1) + (pageIndx - 1) * objectOnPage));
        tableColumn_MyName.setCellValueFactory(new PropertyValueFactory<Dataset, String>("name"));
        tableColumn_MyName.setSortable(false);
        tableColumn_MyDescription.setCellValueFactory(new PropertyValueFactory<Dataset, String>("description"));
        tableColumn_MyDescription.setSortable(false);
        tableColumn_MyActive.setCellValueFactory(new PropertyValueFactory<Dataset, String>("active"));
        tableColumn_MyActive.setSortable(false);
        tableView_MyDataSetTable.setRowFactory(tv -> {
            TableRow<Dataset> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    try {
                        viewDataSet();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
        pagination_MyDataSet.setPageFactory(this::createMyPage);
//        button_View.disableProperty().bind(Bindings.isEmpty(tableView_AllDataSetTable.getSelectionModel().getSelectedItems()));
//        button_View.disableProperty().bind(Bindings.isEmpty(tableView_AllDataSetTable.getSelectionModel().getSelectedItems()).or(Bindings.isEmpty(tableView_MyDataSetTable.getSelectionModel().getSelectedItems())));

    }


    public void addDataSet(ActionEvent event) throws IOException {
        windowsController.openNewModalWindow("dataset/addDataSetMenu.fxml", getStage(), getInstance(), addDataSetMenuController,
                "Add new dataset", 740, 500);
    }

    public void viewDataSet(ActionEvent event) throws IOException {
        if (tab_All.isSelected() && !tableView_AllDataSetTable.getSelectionModel().getSelectedItems().isEmpty()) {
            getMap().put("datasetId", tableView_AllDataSetTable.getSelectionModel().getSelectedItem().getId());
            windowsController.openWindowResizable("dataset/dataSetMenu.fxml", getStage(), getInstance(),
                    dataSetMenuController, "Dataset menu", 800, 640);
        }
        if (tab_My.isSelected() && !tableView_MyDataSetTable.getSelectionModel().getSelectedItems().isEmpty()) {
            getMap().put("datasetId", tableView_MyDataSetTable.getSelectionModel().getSelectedItem().getId());
            windowsController.openWindowResizable("dataset/dataSetMenu.fxml", getStage(), getInstance(),
                    dataSetMenuController, "Dataset menu", 800, 640);
        }
    }

    public void viewDataSet() throws IOException {
        if (tab_All.isSelected() && !tableView_AllDataSetTable.getSelectionModel().getSelectedItems().isEmpty()) {
            getMap().put("datasetId", tableView_AllDataSetTable.getSelectionModel().getSelectedItem().getId());
            windowsController.openWindowResizable("dataset/dataSetMenu.fxml", getStage(), getInstance(),
                    dataSetMenuController, "Dataset menu", 800, 640);
        }
        if (tab_My.isSelected() && !tableView_MyDataSetTable.getSelectionModel().getSelectedItems().isEmpty()) {
            getMap().put("datasetId", tableView_MyDataSetTable.getSelectionModel().getSelectedItem().getId());
            windowsController.openWindowResizable("dataset/dataSetMenu.fxml", getStage(), getInstance(),
                    dataSetMenuController, "Dataset menu", 800, 640);
        }
    }

    public void updateActive(ActionEvent event) {
        tableView_AllDataSetTable.refresh();
        System.out.println("refresh all");
        tableView_MyDataSetTable.refresh();
        System.out.println("refresh my");

    }

    private Node createAllPage(int pageIndex) {
        try {
            pageIndx = pageIndex + 1;
            getAllPage(pageIndx, objectOnPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tableView_AllDataSetTable;
    }

    private Node createMyPage(int pageIndex) {
        try {
            pageIndx = pageIndex + 1;
            getMyPage(pageIndx, objectOnPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tableView_MyDataSetTable;
    }


    public void getAllPage(int pageIndx, int objectOnPage) throws IOException {
        response = dataSetController.getDataSetAllPage(new Encryptor().decrypt(getMap().get("key").toString(),
                getMap().get("vector").toString(), getMap().get("login").toString()),
                new Encryptor().decrypt(getMap().get("key").toString(),
                        getMap().get("vector").toString(), getMap().get("password").toString()),
                pageIndx, objectOnPage);
        statusCode = response.getStatusLine().getStatusCode();
        if (checkStatusCode(statusCode)) {
            datasetPage = new DatasetPage().fromJson(response);
            datasetObservableList = FXCollections.observableList(datasetPage.getDatasetEntities());
        }
        if (datasetObservableList.isEmpty()) {
            pagination_AllDataSet.setPageCount(1);
            pagination_AllDataSet.setCurrentPageIndex(1);
        } else {
            pagination_AllDataSet.setPageCount(datasetPage.getNumberOfPages());
            pagination_AllDataSet.setCurrentPageIndex(pageIndx - 1);
        }
        tableView_AllDataSetTable.setItems(datasetObservableList);
        label_AllCount.setText(String.valueOf(datasetObservableList.size()));
    }

    public void getMyPage(int pageIndx, int objectOnPage) throws IOException {
        response = dataSetController.getSpecialistDataSetAllPage(new Encryptor().decrypt(getMap().get("key").toString(),
                getMap().get("vector").toString(), getMap().get("login").toString()),
                new Encryptor().decrypt(getMap().get("key").toString(),
                        getMap().get("vector").toString(), getMap().get("password").toString()),
                pageIndx, objectOnPage);
        statusCode = response.getStatusLine().getStatusCode();
        if (checkStatusCode(statusCode)) {
            datasetPage = new DatasetPage().fromJson(response);
            datasetObservableList = FXCollections.observableList(datasetPage.getDatasetEntities());
        }
        if (datasetObservableList.isEmpty()) {
            pagination_MyDataSet.setPageCount(1);
            pagination_MyDataSet.setCurrentPageIndex(1);
        } else {
            pagination_MyDataSet.setPageCount(datasetPage.getNumberOfPages());
            pagination_MyDataSet.setCurrentPageIndex(pageIndx - 1);
        }
        tableView_MyDataSetTable.setItems(datasetObservableList);
        label_MyCount.setText(String.valueOf(datasetObservableList.size()));
    }

    public void changeActive(ActionEvent event) throws IOException {
        if (tab_All.isSelected()) {
            if (tableView_AllDataSetTable.getSelectionModel().getSelectedItem().isActive()) {
                response = dataSetController.changeActive(new Encryptor().decrypt(getMap().get("key").toString(),
                        getMap().get("vector").toString(), getMap().get("login").toString()),
                        new Encryptor().decrypt(getMap().get("key").toString(), getMap().get("vector").toString(),
                                getMap().get("password").toString()),
                        tableView_AllDataSetTable.getSelectionModel().getSelectedItem().getId(),
                        false
                );
                statusCode = response.getStatusLine().getStatusCode();
                if (checkStatusCode(statusCode)) {
                    tableView_AllDataSetTable.getSelectionModel().getSelectedItem().setActive(false);
                }
            } else {
                response = dataSetController.changeActive(new Encryptor().decrypt(getMap().get("key").toString(),
                        getMap().get("vector").toString(), getMap().get("login").toString()),
                        new Encryptor().decrypt(getMap().get("key").toString(), getMap().get("vector").toString(),
                                getMap().get("password").toString()),
                        tableView_AllDataSetTable.getSelectionModel().getSelectedItem().getId(),
                        true
                );
                statusCode = response.getStatusLine().getStatusCode();
                if (checkStatusCode(statusCode)) {
                    tableView_AllDataSetTable.getSelectionModel().getSelectedItem().setActive(true);
                }
            }
            tableView_AllDataSetTable.refresh();
            for (Dataset dataset : datasetObservableList) {
                if (tableView_AllDataSetTable.getSelectionModel().getSelectedItem().getId() == dataset.getId()) {
                    dataset.setActive(tableView_AllDataSetTable.getSelectionModel().getSelectedItem().isActive());
                }
            }
            tableView_MyDataSetTable.refresh();
        }


        if (tab_My.isSelected()) {
            if (tableView_MyDataSetTable.getSelectionModel().getSelectedItem().isActive()) {
                response = dataSetController.changeActive(new Encryptor().decrypt(getMap().get("key").toString(),
                        getMap().get("vector").toString(), getMap().get("login").toString()),
                        new Encryptor().decrypt(getMap().get("key").toString(), getMap().get("vector").toString(),
                                getMap().get("password").toString()),
                        tableView_MyDataSetTable.getSelectionModel().getSelectedItem().getId(),
                        false
                );
                statusCode = response.getStatusLine().getStatusCode();
                if (checkStatusCode(statusCode)) {
                    tableView_MyDataSetTable.getSelectionModel().getSelectedItem().setActive(false);
                }
            } else {
                response = dataSetController.changeActive(new Encryptor().decrypt(getMap().get("key").toString(),
                        getMap().get("vector").toString(), getMap().get("login").toString()),
                        new Encryptor().decrypt(getMap().get("key").toString(), getMap().get("vector").toString(),
                                getMap().get("password").toString()),
                        tableView_MyDataSetTable.getSelectionModel().getSelectedItem().getId(),
                        true
                );
                statusCode = response.getStatusLine().getStatusCode();
                if (checkStatusCode(statusCode)) {
                    tableView_MyDataSetTable.getSelectionModel().getSelectedItem().setActive(true);
                }
            }
            tableView_MyDataSetTable.refresh();
            for (Dataset dataset : datasetObservableList) {
                if (tableView_MyDataSetTable.getSelectionModel().getSelectedItem().getId() == dataset.getId()) {
                    dataset.setActive(tableView_MyDataSetTable.getSelectionModel().getSelectedItem().isActive());
                }
            }
            tableView_AllDataSetTable.refresh();
        }
    }
}
