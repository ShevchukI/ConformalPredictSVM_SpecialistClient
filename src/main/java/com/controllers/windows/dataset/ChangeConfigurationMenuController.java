package com.controllers.windows.dataset;

import com.controllers.requests.SVMParameterController;
import com.controllers.windows.menu.MenuController;
import com.models.Configuration;
import com.models.SVMParameter;
import com.tools.Constant;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;

public class ChangeConfigurationMenuController extends MenuController {

    @Autowired
    HttpResponse response;

    private ArrayList<SVMParameter> kernelTypes = new ArrayList<>();
    private ObservableList<SVMParameter> observableKernelTypes = FXCollections.observableArrayList();
    private int statusCode;

    @FXML
    private Label label_DatasetName;
    @FXML
    private TextField textField_Name;
    @FXML
    private RadioButton radioButton_CSVC;
    @FXML
    private RadioButton radioButton_NUSVC;
    @FXML
    private TextField textField_C;
    @FXML
    private TextField textField_Weight;
    @FXML
    private TextField textField_NU;
    @FXML
    private ComboBox<SVMParameter> comboBox_KernelType;
    @FXML
    private TextField textField_Gamma;
    @FXML
    private TextField textField_Coef;
    @FXML
    private TextField textField_Degree;
    @FXML
    private TextField textField_Epsilon;
    @FXML
    private CheckBox checkBox_Shrinking;
    @FXML
    private CheckBox checkBox_Propability;
    @FXML
    private TableView tableView_RegionMatrix;
    @FXML
    private TableColumn column_Significance;
    @FXML
    private TableColumn column_PredictClassTT;
    @FXML
    private TableColumn column_PredictClassTF;
    @FXML
    private TableColumn column_PredictClassFF;
    @FXML
    private TableColumn column_PredictClassFT;
    @FXML
    private TableColumn column_Empty;
    @FXML
    private TableColumn column_Uncertain;

    public void initialize(Stage stage, Stage newWindow, boolean change) throws IOException {
        stage.setOnHidden(event -> {
            Constant.getMapByName("dataset").remove("name");
            Constant.getMapByName("dataset").remove("column");
            Constant.getInstance().getLifecycleService().shutdown();
        });

        setStage(stage);
        setNewWindow(newWindow);
        label_DatasetName.setText(Constant.getMapByName("dataset").get("name").toString());
        response = new SVMParameterController().getAllKernel(Constant.getAuth());
        statusCode = response.getStatusLine().getStatusCode();
        if (checkStatusCode(statusCode)) {
            kernelTypes = Constant.fillKernelType(response);
        }
        observableKernelTypes.addAll(kernelTypes);
        comboBox_KernelType.setItems(observableKernelTypes);
        switch (kernelTypes.get(0).getName()) {
            case "LINEAR":
                textField_Gamma.setDisable(true);
                textField_Degree.setDisable(true);
                textField_Coef.setDisable(true);
                break;
            case "POLY":
                textField_Gamma.setDisable(false);
                textField_Degree.setDisable(false);
                textField_Coef.setDisable(false);
                break;
            case "RBF":
                textField_Gamma.setDisable(false);
                textField_Degree.setDisable(true);
                textField_Coef.setDisable(true);
                break;
            case "SIGMOID":
                textField_Gamma.setDisable(false);
                textField_Degree.setDisable(true);
                textField_Coef.setDisable(false);
                break;
            default:
                break;
        }
        comboBox_KernelType.setCellFactory(new Callback<ListView<SVMParameter>, ListCell<SVMParameter>>() {
            @Override
            public ListCell<SVMParameter> call(ListView<SVMParameter> p) {
                ListCell cell = new ListCell<SVMParameter>() {
                    @Override
                    protected void updateItem(SVMParameter item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText("");
                        } else {
                            setText(item.getName());
                        }
                    }
                };
                return cell;
            }
        });
        comboBox_KernelType.setButtonCell(new ListCell<SVMParameter>() {
            @Override
            protected void updateItem(SVMParameter t, boolean bln) {
                super.updateItem(t, bln);
                if (bln) {
                    setText("");
                } else {
                    setText(t.getName());
                }
            }
        });
        comboBox_KernelType.setVisibleRowCount(5);
        comboBox_KernelType.getSelectionModel().select(0);
        textField_C.disableProperty().bind(radioButton_CSVC.selectedProperty().not());
        textField_Weight.disableProperty().bind(radioButton_CSVC.selectedProperty().not());

        textField_NU.disableProperty().bind(radioButton_NUSVC.selectedProperty().not());
        int columnCount = Constant.getCountSplitString(Constant.getMapByName("dataset").get("column").toString(), ",")-2;
        double defaultGamma = 1.0/columnCount;
        textField_Gamma.setText(String.valueOf(defaultGamma));
        comboBox_KernelType.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        switch (comboBox_KernelType.getValue().getName()) {
                            case "LINEAR":
                                textField_Gamma.setDisable(true);
                                textField_Degree.setDisable(true);
                                textField_Coef.setDisable(true);
                                break;
                            case "POLY":
                                textField_Gamma.setDisable(false);
                                textField_Degree.setDisable(false);
                                textField_Coef.setDisable(false);
                                break;
                            case "RBF":
                                textField_Gamma.setDisable(false);
                                textField_Degree.setDisable(true);
                                textField_Coef.setDisable(true);
                                break;
                            case "SIGMOID":
                                textField_Gamma.setDisable(false);
                                textField_Degree.setDisable(true);
                                textField_Coef.setDisable(false);
                                break;
                            default:
                                break;
                        }
//                        if (comboBox_KernelType.getValue().getName().equals("LINEAR")) {
//                            textField_Gamma.setDisable(true);
//                            textField_Degree.setDisable(true);
//                            textField_Coef.setDisable(true);
//                        } else if (comboBox_KernelType.getValue().getName().equals("POLY")) {
//                            textField_Gamma.setDisable(false);
//                            textField_Degree.setDisable(false);
//                            textField_Coef.setDisable(false);
//                        } else if (comboBox_KernelType.getValue().getName().equals("RBF")) {
//                            textField_Gamma.setDisable(false);
//                            textField_Degree.setDisable(true);
//                            textField_Coef.setDisable(true);
//                        } else if (comboBox_KernelType.getValue().getName().equals("SIGMOID")) {
//                            textField_Gamma.setDisable(false);
//                            textField_Degree.setDisable(true);
//                            textField_Coef.setDisable(false);
//                        }
                    }
                }
        );

//        if (!change) {
//
//        }
    }


    public void save(ActionEvent event) {
        int probability;
        if(checkBox_Propability.isSelected()){
            probability = 1;
        } else {
            probability = 0;
        }
        int svm = 1;
        if(radioButton_CSVC.isSelected()){
            svm = 1;
        }
        if(radioButton_NUSVC.isSelected()){
            svm = 4;
        }
        double testPart = 0.3;
        Configuration configuration = new Configuration(textField_Name.getText(),
                Integer.parseInt(textField_C.getText()),
                Integer.parseInt(textField_Degree.getText()),
                Double.parseDouble(textField_Epsilon.getText()),
                Double.parseDouble(textField_Gamma.getText()),
                comboBox_KernelType.getSelectionModel().getSelectedItem().getId(),
                Double.parseDouble(textField_NU.getText()),
                probability,
                svm,
                testPart);

        System.out.println(configuration.toString());
    }
}
