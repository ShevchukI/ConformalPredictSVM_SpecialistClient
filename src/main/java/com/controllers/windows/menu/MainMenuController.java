package com.controllers.windows.menu;

import com.controllers.windows.dataset.DataSetMenuController;
import com.hazelcast.core.HazelcastInstance;
import com.models.Dataset;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

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
//    @Autowired
//    HttpResponse response;
//
    private DataSetMenuController dataSetMenuController = new DataSetMenuController();
//
//    private Placeholder placeholder = new Placeholder();
//
    private WindowsController windowsController = new WindowsController();
//
//    private Encryptor encryptor = new Encryptor();
//
//    private int statusCode;

    @FXML
    private MenuBarController menuBarController;

    //    private Stage stage;
//
//    private List<Patient> patientList;
//
//    private MenuController menuController;
//
    @FXML
    private Pagination pagination_DataSet;
    //
//    private int rowsPerPage = 10;
//
//    @FXML
//    private ObservableList<Patient> patientObservableList;
//
    @FXML
    private TableView<Dataset> tableView_DataSetTable;
    @FXML
    private TableColumn<Dataset, Number> tableColumn_Number = new TableColumn<Dataset, Number>("#");
    @FXML
    private TableColumn tableColumn_Name;
    @FXML
    private TableColumn tableColumn_Description;
    @FXML
    private TableColumn tableColumn_Active;
//
//    @FXML
//    private TableColumn tableColumn_Telephone;
//
//    @FXML
//    private TableColumn tableColumn_Email;
//
    @FXML
    private Label label_Name;


//    @FXML
//    private Label label_Specialization;
//
//    @FXML
//    private Label label_Count;
//
    @FXML
    private Button button_View;
//

//
//    @FXML
//    private TextField textField_Search;
//
//    private int searchType;
//
//    private MainMenuController mainMenuController;
//
//    @FXML
//    public void initialize(Doctor doctor) {
//        this.doctor = doctor;
//        menuBarController.init(this);
//        label_Name.setText(doctor.getName());
//        label_Specialization.setText(doctor.getSpecialization().getName());
//    }
//
//    public void initialize(Doctor doctor, Stage stage, HazelcastInstance hazelcastInstance) {
//        userMap = hazelcastInstance.getMap("userMap");
//        stage.setOnHidden(event -> {
//            hazelcastInstance.getLifecycleService().shutdown();
//        });
//        setStage(stage);
//        setInstance(hazelcastInstance);
//
//        this.doctor = doctor;
//        menuBarController.init(this);
//        label_Name.setText(doctor.getName());
//        if (doctor.getSpecialization() == null) {
//            label_Specialization.setText("Empty!");
//        } else {
//            label_Specialization.setText(doctor.getSpecialization().getName());
//        }
//    }
//
    public void initialize(Stage stage, HazelcastInstance hazelcastInstance) throws IOException {
        userMap = hazelcastInstance.getMap("userMap");
        stage.setOnHidden(event -> {
            hazelcastInstance.getLifecycleService().shutdown();
        });
        setStage(stage);
        setInstance(hazelcastInstance);
        menuBarController.init(this);
        label_Name.setText(getMap().get("name").toString() + " " + getMap().get("surname").toString());

        button_View.disableProperty().bind(Bindings.isEmpty(tableView_DataSetTable.getSelectionModel().getSelectedItems()));


//        System.out.println(getMap().get("name"));
//        System.out.println(getMap().get("surname"));
//        System.out.println(getMap().get("login"));
//        System.out.println(getMap().get("password"));
//        label_Name.setText(getMap().get("surname").toString() + " " + getMap().get("name").toString());
//        label_Specialization.setText(getMap().get("specName").toString());
//
////        response = patientController.getAllPatient(encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(), getMap().get("login").toString()),
////                encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(), getMap().get("password").toString()));
//
//        response = patientController.findPatient(encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(), getMap().get("login").toString()),
//                encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(), getMap().get("password").toString()),
//                textField_Search.getText(), 0);
//
//        patientList = new Patient().listFromJson(response);
//
//        patientObservableList = FXCollections.observableList(patientList);
//
//        tableColumn_Number.setSortable(false);
//        tableColumn_Number.setCellValueFactory(column -> new ReadOnlyObjectWrapper<Number>(tableView_PatientTable.getItems().indexOf(column.getValue()) + 1));
//
//        tableColumn_Name.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
//        tableColumn_Surname.setCellValueFactory(new PropertyValueFactory<Patient, String>("surname"));
//        tableColumn_Address.setCellValueFactory(new PropertyValueFactory<Patient, String>("address"));
//        tableColumn_Telephone.setCellValueFactory(new PropertyValueFactory<Patient, String>("telephone"));
//        tableColumn_Email.setCellValueFactory(new PropertyValueFactory<Patient, String>("email"));
//
//        tableView_PatientTable.setItems(patientObservableList);
//
//        label_Count.setText(String.valueOf(patientObservableList.size()));
//
//        button_View.disableProperty().bind(Bindings.isEmpty(tableView_PatientTable.getSelectionModel().getSelectedItems()));
//
//        tableView_PatientTable.setRowFactory(tv -> {
//            TableRow<Patient> row = new TableRow<>();
//            row.setOnMouseClicked(event -> {
//                if (event.getClickCount() == 2 && (!row.isEmpty())) {
//                    try {
//                        viewDataSet();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            return row;
//        });
//
//       rowsPerPage = patientObservableList.size()/5;
//        pagination_Dataset.setMaxPageIndicatorCount(patientObservableList.size()/6);
//        pagination_Dataset.setPageFactory(this::createPage);
//

    }

    //
//
//    public void setStage(Stage stage) {
//        this.stage = stage;
//    }
//
//    public Stage getStage() {
//        return stage;
//    }
//
//    public void getPlaceholderAlert(ActionEvent event) {
//        placeholder.getAlert();
//    }
//
    public void addDataSet(ActionEvent event) throws IOException {
        windowsController.openNewModalWindow("dataset/addDataSetMenu.fxml", getStage(), getInstance(), dataSetMenuController,
                "Add new dataset", 740, 500);
    }

    //
    public void viewDataSet(ActionEvent event) throws IOException {
//        if (tableView_PatientTable.getSelectionModel().getSelectedItem() == null) {
//            System.out.println("ERROR!");
//        } else {
//            Patient patient = tableView_PatientTable.getSelectionModel().getSelectedItem();
//            windowsController.openWindowResizable("cardMenu.fxml", getStage(), getInstance(), cardMenuController, patient, "Card", 600, 640);
//        }
    }

    //

    //
//    public void getMap(ActionEvent event) {
//
//
//        System.out.println(getMap().get("key"));
//        System.out.println(getMap().get("vector"));
//        System.out.println(getMap().get("login") + " :: " + encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(), getMap().get("login").toString()));
//        System.out.println(getMap().get("password") + " :: " + encryptor.decrypt(getMap().get("key").toString(), getMap().get("vector").toString(), getMap().get("password").toString()));
//        System.out.println(getMap().size());
////        System.out.println("key: "+logmap.get("key"));
////        System.out.println("vector: "+logmap.get("vector"));
////
////        System.out.println(encryptor.decrypt(logmap.get("key"), logmap.get("vector"), logmap.get("login")));
//    }
//

//
//
//    private Node createPage(int pageIndex) {
//        int fromIndex = pageIndex * rowsPerPage;
//        int toIndex = Math.min(fromIndex + rowsPerPage, patientObservableList.size());
//        tableView_PatientTable.setItems(FXCollections.observableArrayList(patientObservableList.subList(fromIndex, toIndex)));
//        return tableView_PatientTable;
//    }
}
