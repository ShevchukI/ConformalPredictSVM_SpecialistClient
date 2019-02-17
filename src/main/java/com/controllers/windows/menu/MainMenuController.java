package com.controllers.windows.menu;

import com.controllers.requests.DataSetController;
import com.controllers.windows.dataset.AddDataSetMenuController;
import com.controllers.windows.dataset.DataSetMenuController;
import com.models.Dataset;
import com.models.DatasetPage;
import com.tools.Constant;
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


    private ObservableList<Dataset> allDatasetObservableList;
    private ObservableList<Dataset> myDatasetObservableList;
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

    public void initialize(Stage stage) throws IOException {
        stage.setOnHidden(event -> {
            Constant.getInstance().getLifecycleService().shutdown();
        });
        setStage(stage);
        Constant.getMapByName("dataset").remove("id");
        menuBarController.init(this);
        label_Name.setText(Constant.getMapByName("user").get("name").toString() + " " + Constant.getMapByName("user").get("surname").toString());
        pageIndx = 1;
        pageIndx = Integer.parseInt(Constant.getMapByName("misc").get("pageIndex").toString());
        tableColumn_AllNumber.setSortable(false);
        tableColumn_AllNumber.setCellValueFactory(column -> new ReadOnlyObjectWrapper<Number>((tableView_AllDataSetTable.getItems().
                indexOf(column.getValue()) + 1) + (pageIndx - 1) * objectOnPage));
        tableColumn_AllName.setCellValueFactory(new PropertyValueFactory<Dataset, String>("name"));
        tableColumn_AllName.setSortable(false);
        tableColumn_AllDescription.setCellValueFactory(new PropertyValueFactory<Dataset, String>("description"));
        tableColumn_AllDescription.setSortable(false);

        tableColumn_AllActive.setCellValueFactory(new PropertyValueFactory<Dataset, String>("visibleActive"));
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
        tableColumn_MyActive.setCellValueFactory(new PropertyValueFactory<Dataset, String>("visibleActive"));
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
        windowsController.openNewModalWindow("dataset/addDataSetMenu", getStage(), addDataSetMenuController,
                "Add new dataset", 740, 500);
    }

    public void viewDataSet(ActionEvent event) throws IOException {
        if (tab_All.isSelected() && !tableView_AllDataSetTable.getSelectionModel().getSelectedItems().isEmpty()) {
            Constant.getMapByName("dataset").put("id", tableView_AllDataSetTable.getSelectionModel().getSelectedItem().getId());
            windowsController.openWindowResizable("dataset/dataSetMenu", getStage(),
                    dataSetMenuController, "Dataset menu", 800, 640);
        }
        if (tab_My.isSelected() && !tableView_MyDataSetTable.getSelectionModel().getSelectedItems().isEmpty()) {
            Constant.getMapByName("dataset").put("id", tableView_MyDataSetTable.getSelectionModel().getSelectedItem().getId());
            windowsController.openWindowResizable("dataset/dataSetMenu", getStage(),
                    dataSetMenuController, "Dataset menu", 800, 640);
        }
    }

    public void viewDataSet() throws IOException {
        if (tab_All.isSelected() && !tableView_AllDataSetTable.getSelectionModel().getSelectedItems().isEmpty()) {
            Constant.getMapByName("dataset").put("id", tableView_AllDataSetTable.getSelectionModel().getSelectedItem().getId());
            windowsController.openWindowResizable("dataset/dataSetMenu", getStage(),
                    dataSetMenuController, "Dataset menu", 800, 640);
        }
        if (tab_My.isSelected() && !tableView_MyDataSetTable.getSelectionModel().getSelectedItems().isEmpty()) {
            Constant.getMapByName("dataset").put("id", tableView_MyDataSetTable.getSelectionModel().getSelectedItem().getId());
            windowsController.openWindowResizable("dataset/dataSetMenu", getStage(),
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
            response = dataSetController.getDataSetAllPage(Constant.getAuth(),
                    pageIndx, objectOnPage);
            allDatasetObservableList = getOListAfterFillPage(pageIndx, response, allDatasetObservableList, pagination_AllDataSet, tableView_AllDataSetTable, label_AllCount);
//            getAllPage(pageIndx, objectOnPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tableView_AllDataSetTable;
    }

    private Node createMyPage(int pageIndex) {
        try {
            pageIndx = pageIndex + 1;
            response = dataSetController.getSpecialistDataSetAllPage(Constant.getAuth(),
                    pageIndx, objectOnPage);
            myDatasetObservableList = getOListAfterFillPage(pageIndx, response, myDatasetObservableList, pagination_MyDataSet, tableView_MyDataSetTable, label_MyCount);
//            getMyPage(pageIndx, objectOnPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tableView_MyDataSetTable;
    }


    public void getAllPage(int pageIndx, int objectOnPage) throws IOException {
//        response = dataSetController.getDataSetAllPage(Constant.getAuth(),
//                pageIndx, objectOnPage);
//        statusCode = response.getStatusLine().getStatusCode();
//        if (checkStatusCode(statusCode)) {
//            datasetPage = new DatasetPage().fromJson(response);
//            allDatasetObservableList = FXCollections.observableList(datasetPage.getDatasetEntities());
//            for (Dataset dataset : datasetPage.getDatasetEntities()) {
//                if (dataset.isActive()) {
//                    dataset.setVisibleActive("Enabled");
//                } else {
//                    dataset.setVisibleActive("Disabled");
//                }
//            }
//        }
//        if (allDatasetObservableList.isEmpty()) {
//            pagination_AllDataSet.setPageCount(1);
//            pagination_AllDataSet.setCurrentPageIndex(1);
//        } else {
//            pagination_AllDataSet.setPageCount(datasetPage.getNumberOfPages());
//            pagination_AllDataSet.setCurrentPageIndex(pageIndx - 1);
//        }
//        tableView_AllDataSetTable.setItems(allDatasetObservableList);
//        label_AllCount.setText(String.valueOf(allDatasetObservableList.size()));
    }

    public void getMyPage(int pageIndx, int objectOnPage) throws IOException {
//        response = dataSetController.getSpecialistDataSetAllPage(Constant.getAuth(),
//                pageIndx, objectOnPage);
//        statusCode = response.getStatusLine().getStatusCode();
//        if (checkStatusCode(statusCode)) {
//            datasetPage = new DatasetPage().fromJson(response);
//            myDatasetObservableList = FXCollections.observableList(datasetPage.getDatasetEntities());
//            for (Dataset dataset : datasetPage.getDatasetEntities()) {
//                if (dataset.isActive()) {
//                    dataset.setVisibleActive("Enabled");
//                } else {
//                    dataset.setVisibleActive("Disabled");
//                }
//            }
//        }
//        if (myDatasetObservableList.isEmpty()) {
//            pagination_MyDataSet.setPageCount(1);
//            pagination_MyDataSet.setCurrentPageIndex(1);
//        } else {
//            pagination_MyDataSet.setPageCount(datasetPage.getNumberOfPages());
//            pagination_MyDataSet.setCurrentPageIndex(pageIndx - 1);
//        }
//        tableView_MyDataSetTable.setItems(myDatasetObservableList);
//        label_MyCount.setText(String.valueOf(myDatasetObservableList.size()));
    }

    public void changeActive(ActionEvent event) throws IOException {
        if (tab_All.isSelected()) {
            changeActive(tableView_AllDataSetTable, tableView_MyDataSetTable, myDatasetObservableList);
        }
        if (tab_My.isSelected()) {
            changeActive(tableView_MyDataSetTable, tableView_AllDataSetTable, allDatasetObservableList);
        }
    }

    public ObservableList getOListAfterFillPage(int pageIndx, HttpResponse response, ObservableList<Dataset> list,
                                                Pagination pagination, TableView<Dataset> tableView, Label label_Count) throws IOException {

        statusCode = response.getStatusLine().getStatusCode();
        if (checkStatusCode(statusCode)) {
            datasetPage = new DatasetPage().fromJson(response);
            list = FXCollections.observableList(datasetPage.getDatasetEntities());
            for (Dataset dataset : datasetPage.getDatasetEntities()) {
                if (dataset.isActive()) {
                    dataset.setVisibleActive(true);
                } else {
                    dataset.setVisibleActive(false);
                }
            }
        }
        if (list.isEmpty()) {
            pagination.setPageCount(1);
            pagination.setCurrentPageIndex(1);
        } else {
            pagination.setPageCount(datasetPage.getNumberOfPages());
            pagination.setCurrentPageIndex(pageIndx - 1);
        }
        tableView.setItems(list);
        label_Count.setText(String.valueOf(list.size()));
        return list;
    }

    public void changeActive(TableView<Dataset> firstTable, TableView<Dataset> secondTable, ObservableList<Dataset> secondList) throws IOException {
        int id = firstTable.getSelectionModel().getSelectedItem().getId();
        if (firstTable.getSelectionModel().getSelectedItem().isActive()) {
            response = dataSetController.changeActive(Constant.getAuth(),
                    firstTable.getSelectionModel().getSelectedItem().getId(),
                    false);
            statusCode = response.getStatusLine().getStatusCode();
            if (checkStatusCode(statusCode)) {
                firstTable.getSelectionModel().getSelectedItem().setActive(false);
                firstTable.getSelectionModel().getSelectedItem().setVisibleActive(false);
            }
        } else {
            response = dataSetController.changeActive(Constant.getAuth(),
                    firstTable.getSelectionModel().getSelectedItem().getId(),
                    true);
            statusCode = response.getStatusLine().getStatusCode();
            if (checkStatusCode(statusCode)) {
                firstTable.getSelectionModel().getSelectedItem().setActive(true);
                firstTable.getSelectionModel().getSelectedItem().setVisibleActive(true);
            }
        }
        for (Dataset dataset : secondList) {
            if (dataset.getId() == id) {
                dataset.setActive(firstTable.getSelectionModel().getSelectedItem().isActive());
                dataset.setVisibleActive(firstTable.getSelectionModel().getSelectedItem().isActive());
            }
        }
        firstTable.refresh();
        secondTable.refresh();
    }


}
