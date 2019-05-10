package com.controllers.windows.dataSet;

import com.controllers.requests.ConfigurationController;
import com.controllers.requests.SVMParameterController;
import com.controllers.windows.menu.MenuController;
import com.controllers.windows.menu.WindowsController;
import com.models.Configuration;
import com.models.ConfigurationEntity;
import com.models.ConfusionMatrixRow;
import com.models.SVMParameter;
import com.tools.Constant;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChangeConfigurationMenuController extends MenuController {

    private DiagnosticMenuController diagnosticMenuController;
    private DataSetMenuController dataSetMenuController;
    private WindowsController windowsController;
    private ArrayList<SVMParameter> kernelTypes;
    private ObservableList<SVMParameter> observableKernelTypes;
    private int columnCount;
    private int configId;
    private int processId;
    private boolean change;
    private Configuration oldConfiguration;
    private List<ConfusionMatrixRow> confusionMatrixRowList;
    private ObservableList<ConfusionMatrixRow> confusionMatrixRowObservableList;
    @FXML
    private Label label_DataSetName;
    @FXML
    private TextField textField_Name;
    @FXML
    private ComboBox<SVMParameter> comboBox_KernelType;
    @FXML
    private TextField textField_Gamma;
    @FXML
    private TextField textField_Degree;
    @FXML
    private TextField textField_ToTest;

    @FXML
    private ProgressIndicator progressIndicator_Progress;
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

    @FXML
    private Button button_Run;
    @FXML
    private Button button_Save;
    @FXML
    private Button button_Cancel;



    public void initialize(Stage stage, Stage newWindow, boolean change) throws IOException {
        stage.setOnHidden(event -> {
            if (Constant.getInstance().getLifecycleService().isRunning()) {
                Constant.getMapByName(Constant.getDataSetMapName()).remove("name");
                Constant.getMapByName(Constant.getDataSetMapName()).remove("column");
                Constant.getMapByName(Constant.getMiscellaneousMapName()).remove("configurationId");
                Constant.getInstance().getLifecycleService().shutdown();
            }
        });
        setStage(stage);
        setNewWindow(newWindow);
        DataSetMenuController dataSetMenuController = new DataSetMenuController();
        DiagnosticMenuController diagnosticMenuController = new DiagnosticMenuController();
        windowsController = new WindowsController();
        kernelTypes = new ArrayList<>();
        observableKernelTypes = FXCollections.observableArrayList();
        confusionMatrixRowList = new ArrayList<ConfusionMatrixRow>();
        confusionMatrixRowObservableList = FXCollections.observableArrayList();
        this.change = change;
        label_DataSetName.setText(Constant.getMapByName(Constant.getDataSetMapName()).get("name").toString());
        HttpResponse response = new SVMParameterController().getAllKernel();
        setStatusCode(response.getStatusLine().getStatusCode());
        if (checkStatusCode(getStatusCode())) {
            kernelTypes = Constant.fillKernelType(response);
        }
        observableKernelTypes.addAll(kernelTypes);
        comboBox_KernelType.setItems(observableKernelTypes);
        rulesOfDisable(kernelTypes.get(0).getName());
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
        comboBox_KernelType.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        rulesOfDisable(comboBox_KernelType.getValue().getName());
                    }
                }
        );

        comboBox_KernelType.setVisibleRowCount(5);

        columnCount = Constant.getCountSplitString(Constant.getMapByName(Constant.getDataSetMapName()).get("column").toString(), ",") - 2;

        textField_Gamma.setText(String.valueOf(Constant.getSvmGamma(columnCount)));

        progressIndicator_Progress.setProgress(0);

        column_Significance.setSortable(false);
        column_Significance.setCellValueFactory(new PropertyValueFactory<ConfusionMatrixRow, Double>("epsilon"));

        column_PredictClassTT.setSortable(false);
        column_PredictClassTT.setCellValueFactory(new PropertyValueFactory<ConfusionMatrixRow, Integer>("activePredictedActive"));

        column_PredictClassTF.setSortable(false);
        column_PredictClassTF.setCellValueFactory(new PropertyValueFactory<ConfusionMatrixRow, Integer>("activePredictedInactive"));

        column_PredictClassFF.setSortable(false);
        column_PredictClassFF.setCellValueFactory(new PropertyValueFactory<ConfusionMatrixRow, Integer>("inactivePredictedInactive"));

        column_PredictClassFT.setSortable(false);
        column_PredictClassFT.setCellValueFactory(new PropertyValueFactory<ConfusionMatrixRow, Integer>("inactivePredictedActive"));

        column_Empty.setSortable(false);
        column_Empty.setCellValueFactory(new PropertyValueFactory<ConfusionMatrixRow, Integer>("emptyPredictions"));

        column_Uncertain.setSortable(false);
        column_Uncertain.setCellValueFactory(new PropertyValueFactory<ConfusionMatrixRow, Integer>("uncertainPredictions"));

        tableView_RegionMatrix.setItems(confusionMatrixRowObservableList);

        if (!this.change) {
            rulesOfDisable(kernelTypes.get(0).getName());
            comboBox_KernelType.getSelectionModel().select(0);
            Configuration configuration = fillConfiguration();
            if (configuration == null) {
                return;
            }
            response = ConfigurationController.createConfiguration(configuration,
                    Integer.parseInt(Constant.getMapByName(Constant.getDataSetMapName()).get("id").toString()));
            setStatusCode(response.getStatusLine().getStatusCode());
            if (checkStatusCode(getStatusCode())) {
                configId = Integer.parseInt(Constant.responseToString(response));
                Constant.getMapByName(Constant.getMiscellaneousMapName()).put("configurationId", configId);
            }

        } else {

            configId = Integer.parseInt(Constant.getMapByName(Constant.getMiscellaneousMapName()).get("configurationId").toString());
            try {
                response = ConfigurationController.getConfusionMatrix(configId);
                confusionMatrixRowList = new ConfusionMatrixRow().listFromJson(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            confusionMatrixRowObservableList.clear();
            confusionMatrixRowObservableList.addAll(confusionMatrixRowList);
            response = ConfigurationController.getConfiguration(Integer.parseInt(Constant.getMapByName(Constant.getMiscellaneousMapName()).get("configurationId").toString()));
            setStatusCode(response.getStatusLine().getStatusCode());
            if (checkStatusCode(getStatusCode())) {
                ConfigurationEntity configurationEntity = new ConfigurationEntity().fromJson(response);
                for (int i = 0; i < observableKernelTypes.size(); i++) {
                    if (configurationEntity.getKernelParameter().getId() == observableKernelTypes.get(i).getId()) {
                        comboBox_KernelType.getSelectionModel().select(i);
                        break;
                    }
                }
                textField_Name.setText(configurationEntity.getName());
                textField_Degree.setText(String.valueOf(configurationEntity.getDegree()));
                textField_Gamma.setText(String.valueOf(configurationEntity.getGamma()));
                textField_ToTest.setText(String.valueOf(configurationEntity.getTestPart()));
                oldConfiguration = fillConfiguration();
            }
        }

        button_Run.setGraphic(Constant.runIcon());
        button_Save.setGraphic(Constant.okIcon());
        button_Cancel.setGraphic(Constant.cancelIcon());
    }

    public void rulesOfDisable(String string) {
        switch (string) {
            case "LINEAR":
                textField_Gamma.setDisable(true);
                textField_Degree.setDisable(true);
                break;
            case "POLY":
                textField_Gamma.setDisable(false);
                textField_Degree.setDisable(false);
                break;
            case "RBF":
                textField_Gamma.setDisable(false);
                textField_Degree.setDisable(true);
                break;
            case "SIGMOID":
                textField_Gamma.setDisable(false);
                textField_Degree.setDisable(true);
                break;
            default:
                break;
        }
    }

    public void save(ActionEvent event) throws IOException {
        Configuration configuration = fillConfiguration();
        if (configuration == null) {
            return;
        }
        HttpResponse response = ConfigurationController.changeConfiguration(configuration, configId);
        setStatusCode(response.getStatusLine().getStatusCode());
        if (checkStatusCode(getStatusCode())) {
            Constant.getAlert(null, "Configuration saved!", Alert.AlertType.INFORMATION);
            windowsController.openWindowResizable("dataSet/dataSetMenu", getStage(),
                    dataSetMenuController, "DataSet menu", 800, 640);
            getNewWindow().close();
        } else if(getStatusCode()==400){
            Constant.getAlert(null, "You can`t change this model!", Alert.AlertType.ERROR);
        }
    }

    public void run(ActionEvent event) throws IOException {
        Configuration configuration = fillConfiguration();
        if (configuration == null) {
            return;
        }
        HttpResponse response = ConfigurationController.changeConfiguration(configuration, configId);
        setStatusCode(response.getStatusLine().getStatusCode());
        if (checkStatusCode(getStatusCode())) {
            response = ConfigurationController.startGenerateConfiguration(configId);
            setStatusCode(response.getStatusLine().getStatusCode());
            if (checkStatusCode(getStatusCode())) {
                processId = Integer.parseInt(Constant.responseToString(response));
                Thread calculation = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        button_Run.setDisable(true);
                        double progress = 0;
                        while (progress != 1) {
                            try {
                                HttpResponse response = ConfigurationController.getProgress(processId);
                                progress = Double.parseDouble(Constant.responseToString(response)) / 100;
                                System.out.println(progress);
                                progressIndicator_Progress.setProgress(progress);
                                Thread.sleep(1000 * 1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        button_Run.setDisable(false);
                        try {
                            HttpResponse response = ConfigurationController.getConfusionMatrix(configId);
                            confusionMatrixRowList = new ConfusionMatrixRow().listFromJson(response);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        confusionMatrixRowObservableList.clear();
                        confusionMatrixRowObservableList.addAll(confusionMatrixRowList);
                        tableView_RegionMatrix.refresh();
                    }
                });
                calculation.start();
            }
        }
    }

    public void cancel(ActionEvent event) throws IOException {
        boolean result;
        if (!change) {
            result = Constant.questionOkCancel("Do you really want to leave without save configuration?");
            if (result) {
                HttpResponse response = ConfigurationController.deleteConfiguration(configId);
                setStatusCode(response.getStatusLine().getStatusCode());
                if (checkStatusCode(getStatusCode())) {
                    windowsController.openWindowResizable("dataSet/dataSetMenu", getStage(),
                            dataSetMenuController, "DataSet menu", 800, 640);
                    getNewWindow().close();
                }
            } else {
                return;
            }
        } else {
            if(checkOldConfiguration()) {
                HttpResponse response = ConfigurationController.changeConfiguration(oldConfiguration, configId);
                setStatusCode(response.getStatusLine().getStatusCode());
                if (checkStatusCode(getStatusCode())) {
                    response = ConfigurationController.startGenerateConfiguration(configId);
                }
            }
            windowsController.openWindowResizable("dataSet/dataSetMenu", getStage(),
                    dataSetMenuController, "DataSet menu", 800, 640);
            getNewWindow().close();
        }
    }

    public Configuration fillConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setName(textField_Name.getText());
        configuration.setEps(Constant.getSvmEps());
        configuration.setKernelParameter(comboBox_KernelType.getSelectionModel().getSelectedItem().getId());
//        NumberFormat formatter = new DecimalFormat("#0.000000000");
//        String[] gammaNumber = textField_Gamma.getText().split(".");
//        if(gammaNumber[1].length()>=9){
//            textField_Gamma.setText(formatter.format(textField_Gamma.getText()));
//        }
        if(textField_Gamma.getText().matches("[0-9]{1,3}[.]?[0-9]{1,20}")){
            configuration.setTestPart(Double.parseDouble(textField_Gamma.getText()));
            textField_Gamma.setStyle("-fx-border-color: inherit");
        } else {
            textField_Gamma.setStyle("-fx-border-color: red");
            return null;
        }
        if(textField_Degree.getText().matches("([0-9]{1,3})|([0-9]{1,3}[.]?[0-9]{1,9})")){
            configuration.setTestPart(Double.parseDouble(textField_Degree.getText()));
            textField_Degree.setStyle("-fx-border-color: inherit");
        } else {
            textField_Degree.setStyle("-fx-border-color: red");
            return null;
        }
        if (textField_ToTest.getText().matches("[0][.][0-9]{1,9}")) {
            configuration.setTestPart(Double.parseDouble(textField_ToTest.getText()));
            textField_ToTest.setStyle("-fx-border-color: inherit");
        } else {
            textField_ToTest.setStyle("-fx-border-color: red");
            return null;
        }
        configuration.setSvmParameter(1);
        if (textField_Gamma.isDisable()) {
            configuration.setGamma(Constant.getSvmGamma(columnCount));
        } else {
            configuration.setGamma(Double.parseDouble(textField_Gamma.getText()));
        }
        if (textField_Degree.isDisable()) {
            configuration.setDegree(Constant.getSvmDegree());
        } else {
            configuration.setDegree(Integer.parseInt(textField_Degree.getText()));
        }
        configuration.setC(Constant.getSvmC());
        configuration.setNu(Constant.getSvmNu());
        configuration.setProbability(0);
        return configuration;
    }

    private boolean checkOldConfiguration() {
        Configuration configuration = fillConfiguration();
        if (configuration.getKernelParameter() == oldConfiguration.getKernelParameter()
                && configuration.getSvmParameter() == oldConfiguration.getSvmParameter()
                && configuration.getC() == oldConfiguration.getC()
                && configuration.getDegree() == oldConfiguration.getDegree()
                && configuration.getEps() == oldConfiguration.getEps()
                && configuration.getGamma() == oldConfiguration.getGamma()
                && configuration.getNu() == oldConfiguration.getNu()
                && configuration.getProbability() == oldConfiguration.getProbability()
                && configuration.getTestPart() == oldConfiguration.getTestPart()) {
            return false;
        } else {
            return true;
        }
    }

    public void diagnosticSingleObject(ActionEvent event) throws IOException {
        windowsController.openNewModalWindow("dataSet/diagnosticMenu", getNewWindow(), diagnosticMenuController,
                "Diagnostic object", 670, 500);
    }

    public void showDetails(ActionEvent event) throws IOException {
        windowsController.openNewResizableModalWindow("dataSet/detailsResultMenu", getNewWindow(), diagnosticMenuController,
                "Details", 670, 500);
    }
}


