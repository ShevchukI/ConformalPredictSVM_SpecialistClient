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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.http.HttpResponse;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Admin on 12.02.2019.
 */
public class DataSetMenuController extends MenuController {

    private MainMenuController mainMenuController;
    private WindowsController windowsController;
    private DataSet dataSet;
    private ChangeConfigurationMenuController changeConfigurationMenuController;
    private int objectOnPage;
    private int allPageIndex;
    private int myPageIndex;
    private String dataSetObject;
    private ObservableList<ConfigurationEntity> allConfigurationObservableList;
    private ObservableList<ConfigurationEntity> myConfigurationObservableList;
    private ConfigurationPage configurationPage;
    private File file;
    private File fileBuf;
    private ArrayList<String> filterList;
    private boolean changeFile;

    @FXML
    private MenuBarController menuBarController;
    @FXML
    private TextField textField_Name;
    @FXML
    private TextArea textArea_Description;

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
    private AnchorPane anchorPane_Table;
    @FXML
    private Button button_Change;
    @FXML
    private StackPane stackPane_Change;
    @FXML
    private TextField textField_FileName;
    @FXML
    private Button button_Choose;
    @FXML
    private TextArea textArea_Content;
    @FXML
    private Button button_Add;
    @FXML
    private Button button_View;
    @FXML
    private Button button_Delete;
    @FXML
    private Button button_Activate;
    @FXML
    private Button button_Ok;
    @FXML
    private Button button_Cancel;
    @FXML
    private Button button_Back;
    @FXML
    private TextArea textArea_Error;
    @FXML
    private MenuItem menuItem_AllChangeActive;

    public void initialize(Stage stage) throws IOException {
        stage.setOnHidden(event -> {
            Constant.getInstance().getLifecycleService().shutdown();
        });
        changePane(false);
        setStage(stage);
        mainMenuController = new MainMenuController();
        windowsController = new WindowsController();
        changeConfigurationMenuController = new ChangeConfigurationMenuController();
        objectOnPage = 30;
        filterList = new ArrayList<String>();
        menuBarController.init(this);
        List<String> list = new ArrayList<String>();
        list.add("Enabled");
        list.add("Disabled");
        filterList.add("*.csv");
        filterList.add("*.txt");
        ObservableList<String> observableList = new ObservableListWrapper<String>(list);
        choiceBox_Activate.setItems(observableList);
        HttpResponse response = DataSetController.getDataSetById(Integer.parseInt(Constant.getMapByName(Constant.getDataSetMapName()).get("id").toString()));
        setStatusCode(response.getStatusLine().getStatusCode());
        if (checkStatusCode(getStatusCode())) {
            dataSet = new DataSet().fromJson(response);
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
        button_View.disableProperty()
                .bind(Bindings.isEmpty(tableView_AllConfiguration.getSelectionModel().getSelectedItems())
                        .and(Bindings.isEmpty(tableView_MyConfiguration.getSelectionModel().getSelectedItems())));

        response = DataSetController.getDataSetObjects(dataSet.getId());
        dataSetObject = Constant.responseToString(response);
        tableView_Object = fillTableFromString(dataSetObject);

        allPageIndex = 1;
        allPageIndex = Integer.parseInt(Constant.getMapByName(Constant.getMiscellaneousMapName()).get("pageIndexAllConfiguration").toString());
        setSettingColumnTable(allPageIndex, tableView_AllConfiguration, tableColumn_AllNumber, tableColumn_AllName, tableColumn_AllOwner, tableColumn_AllActive);
        pagination_AllConfiguration.setPageFactory(this::createAllPage);

        myPageIndex = 1;
        myPageIndex = Integer.parseInt(Constant.getMapByName(Constant.getMiscellaneousMapName()).get("pageIndexAllConfiguration").toString());
        setSettingColumnTable(myPageIndex, tableView_MyConfiguration, tableColumn_MyNumber, tableColumn_MyName, tableColumn_MyOwner, tableColumn_MyActive);
        pagination_MyConfiguration.setPageFactory(this::createMyPage);

        choiceBox_Activate.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        if (choiceBox_Activate.getSelectionModel().getSelectedIndex() == 0
                                && (tableView_AllConfiguration.getSelectionModel().getSelectedItem()!=null)
                                || tableView_MyConfiguration.getSelectionModel().getSelectedItem()!=null){
                            button_Activate.setDisable(false);
                            menuItem_AllChangeActive.setDisable(false);
                        } else {
                            menuItem_AllChangeActive.setDisable(true);
                            button_Activate.setDisable(true);
                        }
                    }
                }
        );
        if (choiceBox_Activate.getSelectionModel().getSelectedIndex() == 0){
            button_Activate.setDisable(false);
            menuItem_AllChangeActive.setDisable(false);
        } else {
            menuItem_AllChangeActive.setDisable(true);
            button_Activate.setDisable(true);
        }
        changeFile = false;

        button_Add.setGraphic(Constant.addIcon());
        button_View.setGraphic(Constant.infoIcon());
        button_Change.setGraphic(Constant.editIcon());
        button_Ok.setGraphic(Constant.okIcon());
        button_Cancel.setGraphic(Constant.cancelIcon());
        button_Back.setGraphic(Constant.returnIcon());
        button_Delete.setGraphic(Constant.deleteIcon());

    }

    private Node createAllPage(int pageIndex) {
        allPageIndex = pageIndex + 1;
        createPage(allPageIndex, true, tableView_AllConfiguration, allConfigurationObservableList,
                pagination_AllConfiguration);
        return tableView_AllConfiguration;
    }

    private Node createMyPage(int pageIndex) {
        myPageIndex = pageIndex + 1;
        createPage(myPageIndex, false, tableView_MyConfiguration, myConfigurationObservableList,
                pagination_MyConfiguration);
        return tableView_MyConfiguration;
    }

    private void createPage(int tablePageIndex, boolean allPage, TableView<ConfigurationEntity> tableView,
                            ObservableList<ConfigurationEntity> list, Pagination pagination) {
        try {
            HttpResponse response = ConfigurationController.getConfigurationAllPage(dataSet.getId(), tablePageIndex, allPage);
            list = getOListAfterFillPage(tablePageIndex, response,
                    list, pagination,
                    tableView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList getOListAfterFillPage(int pageIndx, HttpResponse response, ObservableList<ConfigurationEntity> list,
                                                Pagination pagination, TableView<ConfigurationEntity> tableView) throws IOException {

        setStatusCode(response.getStatusLine().getStatusCode());
        if (checkStatusCode(getStatusCode())) {
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
        return list;
    }

    public void viewConfiguration() throws IOException {
        if (tab_AllConfiguration.isSelected()) {
            Constant.getMapByName(Constant.getMiscellaneousMapName()).put("configurationId",
                    tableView_AllConfiguration.getSelectionModel().getSelectedItem().getId());
        }
        if (tab_MyConfiguration.isSelected()) {
            Constant.getMapByName(Constant.getMiscellaneousMapName()).put("configurationId",
                    tableView_MyConfiguration.getSelectionModel().getSelectedItem().getId());
        }
        Constant.getMapByName(Constant.getDataSetMapName()).put("name", dataSet.getName());
        Constant.getMapByName(Constant.getDataSetMapName()).put("column", dataSet.getColumns());
        windowsController.openNewModalWindow("dataSet/changeConfigurationMenu", getStage(), changeConfigurationMenuController,
                "Add configuration", true, 870, 540);
    }


    public void save(ActionEvent event) throws IOException {
        HttpResponse response = DataSetController.changeDataSet(new DataSet(dataSet.getId(), textField_Name.getText(), textArea_Description.getText(), dataSet.getColumns()));
        setStatusCode(response.getStatusLine().getStatusCode());
        if (!checkStatusCode(getStatusCode())) {
            Constant.getAlert(null, "Don`t save!", Alert.AlertType.ERROR);
        }
        if (!choiceBox_Activate.getSelectionModel().getSelectedItem().equals(dataSet.getVisibleActive())) {
            response = DataSetController.changeActive(dataSet.getId(), !dataSet.isActive());
            setStatusCode(response.getStatusLine().getStatusCode());
            if (!checkStatusCode(getStatusCode())) {
                Constant.getAlert(null, "Active don`t change!", Alert.AlertType.ERROR);
            }
        }
        if(changeFile) {
            response = DataSetController.addObjectsToDataSet(fileBuf, dataSet.getId());
            setStatusCode(response.getStatusLine().getStatusCode());
            if (!checkStatusCode(getStatusCode())) {
                Constant.getAlert(null, "Objects don`t saved!", Alert.AlertType.ERROR);
            }
        }
        changeFile = false;
    }


    public void backToMainMenu(ActionEvent event) throws IOException {
        windowsController.openWindowResizable("menu/mainMenu", getStage(), mainMenuController,
                "Main menu", 600, 640);
    }

    public void addConfiguration(ActionEvent event) throws IOException {
        Constant.getMapByName(Constant.getDataSetMapName()).put("name", dataSet.getName());
        Constant.getMapByName(Constant.getDataSetMapName()).put("column", dataSet.getColumns());
        windowsController.openNewModalWindow("dataSet/changeConfigurationMenu", getStage(), changeConfigurationMenuController,
                "Add configuration", false, 870, 540);
    }

    public TableView<List<String>> fillTableFromString(String string) {
        ArrayList<String> listName = new ArrayList<>();
        String[] str = dataSet.getColumns().split(",");
        for (int i = 0; i < str.length; i++) {
            listName.add(str[i]);
        }
        String[] lines = string.split("\n");
        for (String line : lines) {
            String[] content = line.split(",");
            for (int i = tableView_Object.getColumns().size(); i < content.length; i++) {
                TableColumn<List<String>, String> col = new TableColumn<>(listName.get(i));
                if(i == 0){
                    col.setSortable(false);
                }
                col.setMinWidth(listName.get(i).length() * 10);
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
                indexOf(column.getValue()) + 1) + (pageIndex - 1) * Constant.getObjectOnPage()));
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
        if (tab_AllConfiguration.isSelected()
                && tableView_AllConfiguration.getSelectionModel().getSelectedItem()!=null) {
            activateConfiguration(allPageIndex, myPageIndex, true, tableView_AllConfiguration,
                    tableView_MyConfiguration, allConfigurationObservableList,
                    myConfigurationObservableList, pagination_AllConfiguration,
                    pagination_MyConfiguration);
        }
        if (tab_MyConfiguration.isSelected()
                && tableView_MyConfiguration.getSelectionModel().getSelectedItem()!=null) {
            activateConfiguration(myPageIndex, allPageIndex, false, tableView_MyConfiguration,
                    tableView_AllConfiguration, myConfigurationObservableList,
                    allConfigurationObservableList, pagination_MyConfiguration,
                    pagination_AllConfiguration);
        }
    }

    public void activateConfiguration(int firstPageIndex, int secondPageIndex, boolean allPage,
                                      TableView<ConfigurationEntity> firstTable,
                                      TableView<ConfigurationEntity> secondTable,
                                      ObservableList<ConfigurationEntity> firstList,
                                      ObservableList<ConfigurationEntity> secondList,
                                      Pagination firstPagination, Pagination secondPagination) throws IOException {
        int configId = firstTable.getSelectionModel().getSelectedItem().getId();
        HttpResponse response = ConfigurationController.activateConfiguration(configId);
        setStatusCode(response.getStatusLine().getStatusCode());
        if (checkStatusCode(getStatusCode())) {
            createPage(firstPageIndex, allPage, firstTable, firstList,
                    firstPagination);
            createPage(secondPageIndex, !allPage, secondTable, secondList,
                    secondPagination);
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
        int configId = tableView.getSelectionModel().getSelectedItem().getId();
        HttpResponse response = ConfigurationController.deleteConfiguration(configId);
        setStatusCode(response.getStatusLine().getStatusCode());
        if (checkStatusCode(getStatusCode())) {
            createPage(allPageIndex, true, tableView_AllConfiguration, allConfigurationObservableList,
                    pagination_AllConfiguration);
            createPage(myPageIndex, false, tableView_MyConfiguration, myConfigurationObservableList,
                    pagination_MyConfiguration);
        }
    }

    public void tablePaneActivity(boolean activity) {
        tableView_Object.setDisable(!activity);
        anchorPane_Table.setDisable(!activity);
        anchorPane_Table.setVisible(activity);
        button_Change.setDisable(!activity);
        button_Change.setVisible(activity);
    }

    public void changePaneActivity(boolean activity) {
        stackPane_Change.setDisable(!activity);
        stackPane_Change.setVisible(activity);
        textField_FileName.setDisable(!activity);
        textField_FileName.setVisible(activity);
        button_Choose.setDisable(!activity);
        button_Choose.setVisible(activity);
        textArea_Content.setDisable(!activity);
        textArea_Content.setVisible(activity);
        button_Ok.setDisable(!activity);
        button_Ok.setVisible(activity);
        button_Cancel.setDisable(!activity);
        button_Cancel.setVisible(activity);
    }

    public void changePane(boolean activity) {
        tablePaneActivity(!activity);
        changePaneActivity(activity);
    }

    public void changeDataSetObject(ActionEvent event) {
        changePane(true);
        textArea_Content.clear();
        textArea_Content.setText(dataSetObject);
    }

    public void ok(ActionEvent event) throws IOException {
        int error = 0;
        textArea_Error.setStyle("-fx-border-color: inherit");
        textArea_Error.clear();
        textArea_Error.setVisible(false);
        String fileName = "file.txt";
        fileBuf = new File(fileName);
        FileWriter writer = new FileWriter(fileBuf.getAbsolutePath());

        String[] mainContent = textArea_Content.getText().split("\n");
        textArea_Content.clear();
        String[][] content = new String[mainContent.length][3];
        for (int i = 0; i < mainContent.length; i++) {
            String[] split = mainContent[i].split(",");
            content[i][0] = split[0];
            content[i][1] = mainContent[i].substring(split[0].length() + 1, mainContent[i].length());
            content[i][2] = split[1];
            if(!checkObjectFormat(mainContent[i])){
                error++;
            }
            if (!checkObjectLength(tableView_Object.getColumns().size(), content[i][0] + "," + content[i][1])) {
                error++;
            }
            if (!checkObjectClass(content, i)) {
                error++;
            }
        }
        for (int i = 0; i < mainContent.length; i++) {
            for (int j = i + 1; j < mainContent.length; j++) {
                if (content[i][1].equals(content[j][1])) {
                    content[j][0] = null;
                }
            }
            if (content[i][0] != null) {
                textArea_Content.appendText(content[i][0] + "," + content[i][1] + "\n");
                writer.write(content[i][0] + "," + content[i][1] + "\r\n");
            }
        }
        writer.flush();
        writer.close();
        if (error != 0) {
            textArea_Error.setStyle("-fx-border-color: red");
            textArea_Error.setStyle("-fx-text-fill: red");
            textArea_Error.setVisible(true);
        } else {
            tableView_Object.getItems().clear();
            tableView_Object = fillTableFromString(textArea_Content.getText());
            tableView_Object.refresh();
            dataSetObject = textArea_Content.getText();
            changeFile = true;
            changePane(false);
        }
    }

    private boolean checkObjectFormat(String object) {
        String[] obj = object.split(",");
        for (int i = 2; i<obj.length; i++){
            if(!obj[i].matches("[0-9]{1,3}")){
                textArea_Error.appendText("Object error! Object [" + object + "] has invalid characters!\n");
                return false;
            }
        }
        return true;
    }

    public void cancel(ActionEvent event) {
        changePane(false);
    }

    private boolean checkObjectLength(int columnsCount, String object) {
        String[] obj = object.split(",");
        if (columnsCount > obj.length) {
            textArea_Error.appendText("Object error! Object [" + object + "] lacks " + (columnsCount - obj.length) + "  feature!\n");
            return false;
        } else if (columnsCount < obj.length) {
            textArea_Error.appendText("Object error! Object [" + object + "] have " + (obj.length - columnsCount) + " excessive feature!\n");
            return false;
        } else {
            return true;
        }
    }

    private boolean checkObjectClass(String[][] content, int i) {
        if (!content[i][2].equals("1") && !content[i][2].equals("-1")) {
            textArea_Error.appendText("Class error! Current: " + content[i][2] + " needed: 1 or -1:\n"
                    + "Object: " + content[i][0] + "," + content[i][1] + ";\n");
            return false;
        } else {
            return true;
        }
    }

    public void chooseFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Files", filterList);
        fileChooser.getExtensionFilters().add(extensionFilter);
        try {
            file = fileChooser.showOpenDialog(getNewWindow());
            if (fileChooser != null) {
                textField_FileName.setText(file.getAbsolutePath());
            }
        } catch (IllegalStateException e1) {
            System.out.println("File not chosen! Application crash!");
        } catch (Exception e) {
            e.getStackTrace();
        }
        if (textField_FileName.getText() != null) {
            fillUpData(textField_FileName.getText());
        }
    }

    public void fillUpData(ActionEvent event) {
        fillUpData(textField_FileName.getText());
    }

    public void fillUpData(String fileName) {
        try {
            FileReader input = new FileReader(fileName);
            BufferedReader bufRead = new BufferedReader(input);
            String line;
            while ((line = bufRead.readLine()) != null) {
                textArea_Content.appendText(line + "\n");
            }
        } catch (FileNotFoundException e1) {
            textArea_Error.appendText("File not found!");
            textArea_Error.setStyle("-fx-border-color: red");
            textArea_Error.setStyle("-fx-text-fill: red");
            textArea_Error.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
