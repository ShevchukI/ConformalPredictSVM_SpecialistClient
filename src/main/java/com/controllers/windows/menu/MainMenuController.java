package com.controllers.windows.menu;

import com.controllers.requests.DataSetController;
import com.controllers.windows.dataSet.AddDataSetMenuController;
import com.controllers.windows.dataSet.DataSetMenuController;
import com.models.DataSet;
import com.models.DataSetPage;
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


public class MainMenuController extends MenuController {
    @Autowired
    HttpResponse response;

    private ObservableList<DataSet> allDataSetObservableList;
    private ObservableList<DataSet> myDataSetObservableList;
    private AddDataSetMenuController addDataSetMenuController;
    private WindowsController windowsController;
    private DataSetMenuController dataSetMenuController;
    private DataSetPage dataSetPage;
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
    private TableView<DataSet> tableView_AllDataSetTable;
    @FXML
    private TableView<DataSet> tableView_MyDataSetTable;
    @FXML
    private TableColumn<DataSet, Number> tableColumn_AllNumber;
    @FXML
    private TableColumn tableColumn_AllName;
    @FXML
    private TableColumn tableColumn_AllDescription;
    @FXML
    private TableColumn tableColumn_AllOwner;
    @FXML
    private TableColumn tableColumn_AllActive;
    @FXML
    private TableColumn<DataSet, Number> tableColumn_MyNumber;
    @FXML
    private TableColumn tableColumn_MyName;
    @FXML
    private TableColumn tableColumn_MyDescription;
    @FXML
    private TableColumn tableColumn_MyOwner;
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
        addDataSetMenuController = new AddDataSetMenuController();
        windowsController = new WindowsController();
        dataSetMenuController = new DataSetMenuController();
        Constant.getMapByName(Constant.getDataSetMapName()).remove("id");
        menuBarController.init(this);

        label_Name.setText(Constant.getMapByName(Constant.getUserMapName()).get("name").toString() + " " + Constant.getMapByName(Constant.getUserMapName()).get("surname").toString());
        allPageIndex = 1;
        allPageIndex = Integer.parseInt(Constant.getMapByName(Constant.getMiscellaneousMapName()).get("pageIndexAllDataSet").toString());
        setSettingColumnTable(allPageIndex, tableView_AllDataSetTable, tableColumn_AllNumber,
                tableColumn_AllName, tableColumn_AllDescription, tableColumn_AllOwner, tableColumn_AllActive);
        pagination_AllDataSet.setPageFactory(this::createAllPage);

        myPageIndex = 1;
        myPageIndex = Integer.parseInt(Constant.getMapByName(Constant.getMiscellaneousMapName()).get("pageIndexMyDataSet").toString());
        setSettingColumnTable(myPageIndex, tableView_MyDataSetTable, tableColumn_MyNumber,
                tableColumn_MyName, tableColumn_MyDescription, tableColumn_MyOwner, tableColumn_MyActive);
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
        windowsController.openNewModalWindow("dataSet/addDataSetMenu", getStage(), addDataSetMenuController,
                "Add new dataSet", 740, 500);
    }

    public void viewDataSet(ActionEvent event) throws IOException {
        viewDataSet();
    }

    public void viewDataSet() throws IOException {
        if (tab_All.isSelected() && !tableView_AllDataSetTable.getSelectionModel().getSelectedItems().isEmpty()) {
            Constant.getMapByName(Constant.getDataSetMapName()).put("id", tableView_AllDataSetTable.getSelectionModel().getSelectedItem().getId());
            Constant.getMapByName(Constant.getMiscellaneousMapName()).put("pageIndexAllDataset", allPageIndex);
            windowsController.openWindowResizable("dataSet/dataSetMenu", getStage(),
                    dataSetMenuController, "DataSet menu", 800, 640);
        }
        if (tab_My.isSelected() && !tableView_MyDataSetTable.getSelectionModel().getSelectedItems().isEmpty()) {
            Constant.getMapByName(Constant.getDataSetMapName()).put("id", tableView_MyDataSetTable.getSelectionModel().getSelectedItem().getId());
            Constant.getMapByName(Constant.getMiscellaneousMapName()).put("pageIndexMyDataset", myPageIndex);
            windowsController.openWindowResizable("dataSet/dataSetMenu", getStage(),
                    dataSetMenuController, "DataSet menu", 800, 640);
        }
    }

    private Node createAllPage(int pageIndex) {
        try {
            allPageIndex = pageIndex + 1;
            response = DataSetController.getDataSetAllPage(allPageIndex);
            allDataSetObservableList = getOListAfterFillPage(allPageIndex, response,
                    allDataSetObservableList, pagination_AllDataSet, tableView_AllDataSetTable, label_AllCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Constant.getMapByName("misc").put("pageIndexAllDataSet", allPageIndex);
        return tableView_AllDataSetTable;
    }

    private Node createMyPage(int pageIndex) {
        try {
            myPageIndex = pageIndex + 1;
            response = DataSetController.getSpecialistDataSetAllPage(myPageIndex);
            myDataSetObservableList = getOListAfterFillPage(myPageIndex, response, myDataSetObservableList, pagination_MyDataSet, tableView_MyDataSetTable, label_MyCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Constant.getMapByName(Constant.getMiscellaneousMapName()).put("pageIndexMyDataSet", myPageIndex);
        return tableView_MyDataSetTable;
    }

    public void changeActive(ActionEvent event) throws IOException {
        if (tab_All.isSelected()) {
            changeActive(tableView_AllDataSetTable, tableView_MyDataSetTable, myDataSetObservableList);
        }
        if (tab_My.isSelected()) {
            changeActive(tableView_MyDataSetTable, tableView_AllDataSetTable, allDataSetObservableList);
        }
    }

    public ObservableList getOListAfterFillPage(int pageIndex, HttpResponse response, ObservableList<DataSet> list,
                                                Pagination pagination, TableView<DataSet> tableView, Label label_Count) throws IOException {

        setStatusCode(response.getStatusLine().getStatusCode());
        if (checkStatusCode(getStatusCode())) {
            dataSetPage = new DataSetPage().fromJson(response);
            list = FXCollections.observableList(dataSetPage.getDataSetEntities());
        }
        if (list.isEmpty()) {
            pagination.setPageCount(1);
            pagination.setCurrentPageIndex(1);
        } else {
            pagination.setPageCount(dataSetPage.getNumberOfPages());
            pagination.setCurrentPageIndex(pageIndex - 1);
        }
        tableView.setItems(list);
        label_Count.setText(String.valueOf(list.size()));
        return list;
    }

    public void changeActive(TableView<DataSet> firstTable, TableView<DataSet> secondTable, ObservableList<DataSet> secondList) throws IOException {
        int id = firstTable.getSelectionModel().getSelectedItem().getId();
        if (firstTable.getSelectionModel().getSelectedItem().isActive()) {
            response = DataSetController.changeActive(firstTable.getSelectionModel().getSelectedItem().getId(),
                    false);
            setStatusCode(response.getStatusLine().getStatusCode());
            if (checkStatusCode(getStatusCode())) {
                firstTable.getSelectionModel().getSelectedItem().setActive(false);
            }
        } else {
            response = DataSetController.changeActive(firstTable.getSelectionModel().getSelectedItem().getId(),
                    true);
            setStatusCode(response.getStatusLine().getStatusCode());
            if (checkStatusCode(getStatusCode())) {
                firstTable.getSelectionModel().getSelectedItem().setActive(true);
            }
        }
        for (DataSet dataSet : secondList) {
            if (dataSet.getId() == id) {
                dataSet.setActive(firstTable.getSelectionModel().getSelectedItem().isActive());
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
            response = DataSetController.getDataSetAllPage(allPageIndex);
            allDataSetObservableList = getOListAfterFillPage(allPageIndex, response, allDataSetObservableList, pagination_AllDataSet, tableView_AllDataSetTable, label_AllCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            response = DataSetController.getSpecialistDataSetAllPage(myPageIndex);
            myDataSetObservableList = getOListAfterFillPage(myPageIndex, response, myDataSetObservableList, pagination_MyDataSet, tableView_MyDataSetTable, label_MyCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(TableView<DataSet> tableView) throws IOException {
        boolean result = Constant.questionOkCancel("Do you really want to delete dataSet: "
                + tableView.getSelectionModel().getSelectedItem().getName()
                + "?");
        if (result) {
            response = DataSetController.deleteDataSet(tableView.getSelectionModel().getSelectedItem().getId());
            setStatusCode(response.getStatusLine().getStatusCode());
            if (checkStatusCode(getStatusCode())) {
                Constant.getAlert(null, tableView.getSelectionModel().getSelectedItem().getName()
                        + " deleted!", Alert.AlertType.INFORMATION);
            } else if (getStatusCode() == 400) {
                Constant.getAlert(null, "You can not delete someone else's dataSet!", Alert.AlertType.ERROR);
            }
        }
    }

    private void setSettingColumnTable(int pageIndex, TableView tableView,
                                       TableColumn<DataSet, Number> columnNumber,
                                       TableColumn columnName,
                                       TableColumn columnDescription, TableColumn columnOwner, TableColumn columnActive) {
        columnNumber.setSortable(false);
        columnNumber.setCellValueFactory(column -> new ReadOnlyObjectWrapper<Number>((tableView.getItems().
                indexOf(column.getValue()) + 1) + (pageIndex - 1) * Constant.getObjectOnPage()));
        columnName.setCellValueFactory(new PropertyValueFactory<DataSet, String>("name"));
        columnName.setSortable(false);
        columnDescription.setCellValueFactory(new PropertyValueFactory<DataSet, String>("description"));
        columnDescription.setSortable(false);
        columnOwner.setSortable(true);
        columnOwner.setCellValueFactory(new PropertyValueFactory<DataSet, String>("owner"));
        columnActive.setCellValueFactory(new PropertyValueFactory<DataSet, String>("visibleActive"));
        columnActive.setSortable(false);
        tableView.setRowFactory(tv -> {
            TableRow<DataSet> row = new TableRow<>();
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
