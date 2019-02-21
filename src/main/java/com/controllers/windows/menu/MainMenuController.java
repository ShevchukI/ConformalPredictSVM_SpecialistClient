package com.controllers.windows.menu;

import com.controllers.requests.DataSetController;
import com.controllers.windows.dataset.AddDataSetMenuController;
import com.controllers.windows.dataset.DataSetMenuController;
import com.models.Dataset;
import com.models.DatasetPage;
import com.tools.Constant;
import javafx.beans.binding.Bindings;
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
    private int allPageIndex;
    private int myPageIndex;

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
    @FXML
    private Button button_ChangeActive;
    @FXML
    private Button button_Delete;

    public void initialize(Stage stage) throws IOException {
        stage.setOnHidden(event -> {
            Constant.getInstance().getLifecycleService().shutdown();
        });
        setStage(stage);
        Constant.getMapByName("dataset").remove("id");
        menuBarController.init(this);

        label_Name.setText(Constant.getMapByName("user").get("name").toString() + " " + Constant.getMapByName("user").get("surname").toString());
        allPageIndex = 1;
        allPageIndex = Integer.parseInt(Constant.getMapByName("misc").get("pageIndexAllDataset").toString());
        setSettingColumnTable(allPageIndex, tableView_AllDataSetTable, tableColumn_AllNumber,
                tableColumn_AllName, tableColumn_AllDescription, tableColumn_AllActive);
        pagination_AllDataSet.setPageFactory(this::createAllPage);

        myPageIndex = 1;
        myPageIndex = Integer.parseInt(Constant.getMapByName("misc").get("pageIndexMyDataset").toString());
        setSettingColumnTable(myPageIndex, tableView_MyDataSetTable, tableColumn_MyNumber,
                tableColumn_MyName, tableColumn_MyDescription, tableColumn_MyActive);
        pagination_MyDataSet.setPageFactory(this::createMyPage);

        button_View.disableProperty()
                .bind(Bindings.isEmpty(tableView_AllDataSetTable.getSelectionModel().getSelectedItems())
                        .and(Bindings.isEmpty(tableView_MyDataSetTable.getSelectionModel().getSelectedItems())));
        button_ChangeActive.disableProperty()
                .bind(Bindings.isEmpty(tableView_AllDataSetTable.getSelectionModel().getSelectedItems())
                        .and(Bindings.isEmpty(tableView_MyDataSetTable.getSelectionModel().getSelectedItems())));
        button_Delete.disableProperty()
                .bind(Bindings.isEmpty(tableView_AllDataSetTable.getSelectionModel().getSelectedItems())
                        .and(Bindings.isEmpty(tableView_MyDataSetTable.getSelectionModel().getSelectedItems())));
    }


    public void addDataSet(ActionEvent event) throws IOException {
        windowsController.openNewModalWindow("dataset/addDataSetMenu", getStage(), addDataSetMenuController,
                "Add new dataset", 740, 500);
    }

    public void viewDataSet(ActionEvent event) throws IOException {
        viewDataSet();
    }

    public void viewDataSet() throws IOException {
        if (tab_All.isSelected() && !tableView_AllDataSetTable.getSelectionModel().getSelectedItems().isEmpty()) {
            Constant.getMapByName("dataset").put("id", tableView_AllDataSetTable.getSelectionModel().getSelectedItem().getId());
            Constant.getMapByName("misc").put("pageIndexAllDataset", allPageIndex);
            windowsController.openWindowResizable("dataset/dataSetMenu", getStage(),
                    dataSetMenuController, "Dataset menu", 800, 640);
        }
        if (tab_My.isSelected() && !tableView_MyDataSetTable.getSelectionModel().getSelectedItems().isEmpty()) {
            Constant.getMapByName("dataset").put("id", tableView_MyDataSetTable.getSelectionModel().getSelectedItem().getId());
            Constant.getMapByName("misc").put("pageIndexMyDataset", myPageIndex);
            windowsController.openWindowResizable("dataset/dataSetMenu", getStage(),
                    dataSetMenuController, "Dataset menu", 800, 640);
        }
    }

    private Node createAllPage(int pageIndex) {
        try {
            allPageIndex = pageIndex + 1;
            response = dataSetController.getDataSetAllPage(Constant.getAuth(),
                    allPageIndex, objectOnPage);
            allDatasetObservableList = getOListAfterFillPage(allPageIndex, response, allDatasetObservableList, pagination_AllDataSet, tableView_AllDataSetTable, label_AllCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tableView_AllDataSetTable;
    }

    private Node createMyPage(int pageIndex) {
        try {
            myPageIndex = pageIndex + 1;
            response = dataSetController.getSpecialistDataSetAllPage(Constant.getAuth(),
                    myPageIndex, objectOnPage);
            myDatasetObservableList = getOListAfterFillPage(myPageIndex, response, myDatasetObservableList, pagination_MyDataSet, tableView_MyDataSetTable, label_MyCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tableView_MyDataSetTable;
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


    public void delete(ActionEvent event) throws IOException {
        if (tab_All.isSelected()) {
            delete(tableView_AllDataSetTable);
        }
        if (tab_My.isSelected()) {
            delete(tableView_MyDataSetTable);
        }
        try {
            response = dataSetController.getDataSetAllPage(Constant.getAuth(),
                    allPageIndex, objectOnPage);
            allDatasetObservableList = getOListAfterFillPage(allPageIndex, response, allDatasetObservableList, pagination_AllDataSet, tableView_AllDataSetTable, label_AllCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            response = dataSetController.getSpecialistDataSetAllPage(Constant.getAuth(),
                    myPageIndex, objectOnPage);
            myDatasetObservableList = getOListAfterFillPage(myPageIndex, response, myDatasetObservableList, pagination_MyDataSet, tableView_MyDataSetTable, label_MyCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(TableView<Dataset> tableView) throws IOException {
        boolean result = questionOkCancel("Do you really want to delete dataset: "
                + tableView.getSelectionModel().getSelectedItem().getName()
                + "?");
        if (result) {
            response = dataSetController.deleteDataset(Constant.getAuth(), tableView.getSelectionModel().getSelectedItem().getId());
            statusCode = response.getStatusLine().getStatusCode();
            if (checkStatusCode(statusCode)) {
                getAlert(null, tableView.getSelectionModel().getSelectedItem().getName()
                        + " deleted!", Alert.AlertType.INFORMATION);
            } else if (statusCode == 400) {
                getAlert(null, "You can not delete someone else's dataset!", Alert.AlertType.ERROR);
            }
        }
    }

    private void setSettingColumnTable(int pageIndex, TableView tableView,
                                       TableColumn<Dataset, Number> columnNumber,
                                       TableColumn columnName,
                                       TableColumn columnDescription, TableColumn columnActive) {
        columnNumber.setSortable(false);
        columnNumber.setCellValueFactory(column -> new ReadOnlyObjectWrapper<Number>((tableView.getItems().
                indexOf(column.getValue()) + 1) + (pageIndex - 1) * objectOnPage));
        columnName.setCellValueFactory(new PropertyValueFactory<Dataset, String>("name"));
        columnName.setSortable(false);
        columnDescription.setCellValueFactory(new PropertyValueFactory<Dataset, String>("description"));
        columnDescription.setSortable(false);
        columnActive.setCellValueFactory(new PropertyValueFactory<Dataset, String>("visibleActive"));
        columnActive.setSortable(false);
        tableView.setRowFactory(tv -> {
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
    }
}
