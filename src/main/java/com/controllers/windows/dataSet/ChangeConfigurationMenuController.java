package com.controllers.windows.dataSet;

import com.controllers.requests.SVMParameterController;
import com.controllers.windows.menu.MenuController;
import com.controllers.windows.menu.WindowsController;
import com.models.ConfigurationEntity;
import com.models.ConfusionMatrixRow;
import com.models.Model;
import com.models.SVMParameter;
import com.tools.Constant;
import com.tools.HazelCastMap;
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

    private Model model;
    private DiagnosticMenuController diagnosticMenuController;
    private DataSetMenuController dataSetMenuController;
    private WindowsController windowsController;
    private ArrayList<SVMParameter> kernelTypes;
    private ObservableList<SVMParameter> observableKernelTypes;
    private int columnCount;
    private int configId;
    private int processId;
    private boolean change;
    private Model oldModel;
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
            if (HazelCastMap.getInstance().getLifecycleService().isRunning()) {
//                HazelCastMap.getMapByName(HazelCastMap.getDataSetMapName()).remove("name");
//                HazelCastMap.getMapByName(HazelCastMap.getDataSetMapName()).remove("column");
                HazelCastMap.getMapByName(HazelCastMap.getMiscellaneousMapName()).remove("configurationId");
                HazelCastMap.getInstance().getLifecycleService().shutdown();
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
        model = new Model();
        label_DataSetName.setText(HazelCastMap.getDataSetMap().get(1).getName());
//        label_DataSetName.setText(HazelCastMap.getMapByName(HazelCastMap.getDataSetMapName()).get("name").toString());
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

        columnCount = Constant.getCountSplitString(HazelCastMap.getDataSetMap().get(1).getColumns(), ",") - 2;
//        columnCount = Constant.getCountSplitString(HazelCastMap.getMapByName(HazelCastMap.getDataSetMapName()).get("column").toString(), ",") - 2;

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
            model = fillConfiguration();
            if (model == null) {
                return;
            }
            response = model.createConfiguration(model,
                    Integer.parseInt(String.valueOf(HazelCastMap.getDataSetMap().get(1).getId())));
//                    Integer.parseInt(HazelCastMap.getMapByName(HazelCastMap.getDataSetMapName()).get("id").toString()));
            setStatusCode(response.getStatusLine().getStatusCode());
            if (checkStatusCode(getStatusCode())) {
                configId = Integer.parseInt(Constant.responseToString(response));
                HazelCastMap.getMapByName(HazelCastMap.getMiscellaneousMapName()).put("configurationId", configId);
            }

        } else {

            configId = Integer.parseInt(HazelCastMap.getMapByName(HazelCastMap.getMiscellaneousMapName()).get("configurationId").toString());
            try {
                response = model.getConfusionMatrix(configId);
                confusionMatrixRowList = new ConfusionMatrixRow().listFromJson(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            confusionMatrixRowObservableList.clear();
            confusionMatrixRowObservableList.addAll(confusionMatrixRowList);
            response = model.getModel(Integer.parseInt(HazelCastMap.getMapByName(HazelCastMap.getMiscellaneousMapName()).get("configurationId").toString()));
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
                oldModel = fillConfiguration();
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
        model = fillConfiguration();
        if (model == null) {
            return;
        }
        HttpResponse response = model.changeModel(model, configId);
        setStatusCode(response.getStatusLine().getStatusCode());
        if (checkStatusCode(getStatusCode())) {
            Constant.getAlert(null, "Model saved!", Alert.AlertType.INFORMATION);
            windowsController.openWindowResizable("dataSet/dataSetMenu", getStage(),
                    dataSetMenuController, "DataSet menu", 800, 640);
            getNewWindow().close();
        } else if(getStatusCode()==400){
            Constant.getAlert(null, "You can`t change this model!", Alert.AlertType.ERROR);
        }
    }

    public void run(ActionEvent event) throws IOException {
        model = fillConfiguration();
        if (model == null) {
            return;
        }
        HttpResponse response = model.changeModel(model, configId);
        setStatusCode(response.getStatusLine().getStatusCode());
        if (checkStatusCode(getStatusCode())) {
            response = model.startGenerateConfiguration(configId);
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
                                HttpResponse response = model.getProgress(processId);
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
                            HttpResponse response = model.getConfusionMatrix(configId);
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
                HttpResponse response = Model.deleteModel(configId);
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
                HttpResponse response = model.changeModel(oldModel, configId);
                setStatusCode(response.getStatusLine().getStatusCode());
                if (checkStatusCode(getStatusCode())) {
                    response = model.startGenerateConfiguration(configId);
                }
            }
            windowsController.openWindowResizable("dataSet/dataSetMenu", getStage(),
                    dataSetMenuController, "DataSet menu", 800, 640);
            getNewWindow().close();
        }
    }

    public Model fillConfiguration() {
        Model model = new Model();
        model.setName(textField_Name.getText());
        model.setEps(Constant.getSvmEps());
        model.setKernelParameter(comboBox_KernelType.getSelectionModel().getSelectedItem().getId());
//        NumberFormat formatter = new DecimalFormat("#0.000000000");
//        String[] gammaNumber = textField_Gamma.getText().split(".");
//        if(gammaNumber[1].length()>=9){
//            textField_Gamma.setText(formatter.format(textField_Gamma.getText()));
//        }
        if(textField_Gamma.getText().matches("[0-9]{1,3}[.]?[0-9]{1,20}")){
            model.setTestPart(Double.parseDouble(textField_Gamma.getText()));
            textField_Gamma.setStyle("-fx-border-color: inherit");
        } else {
            textField_Gamma.setStyle("-fx-border-color: red");
            return null;
        }
        if(textField_Degree.getText().matches("([0-9]{1,3})|([0-9]{1,3}[.]?[0-9]{1,9})")){
            model.setTestPart(Double.parseDouble(textField_Degree.getText()));
            textField_Degree.setStyle("-fx-border-color: inherit");
        } else {
            textField_Degree.setStyle("-fx-border-color: red");
            return null;
        }
        if (textField_ToTest.getText().matches("[0][.][0-9]{1,9}")) {
            model.setTestPart(Double.parseDouble(textField_ToTest.getText()));
            textField_ToTest.setStyle("-fx-border-color: inherit");
        } else {
            textField_ToTest.setStyle("-fx-border-color: red");
            return null;
        }
        model.setSvmParameter(1);
        if (textField_Gamma.isDisable()) {
            model.setGamma(Constant.getSvmGamma(columnCount));
        } else {
            model.setGamma(Double.parseDouble(textField_Gamma.getText()));
        }
        if (textField_Degree.isDisable()) {
            model.setDegree(Constant.getSvmDegree());
        } else {
            model.setDegree(Integer.parseInt(textField_Degree.getText()));
        }
        model.setC(Constant.getSvmC());
        model.setNu(Constant.getSvmNu());
        model.setProbability(0);
        return model;
    }

    private boolean checkOldConfiguration() {
        Model model = fillConfiguration();
        if (model.getKernelParameter() == oldModel.getKernelParameter()
                && model.getSvmParameter() == oldModel.getSvmParameter()
                && model.getC() == oldModel.getC()
                && model.getDegree() == oldModel.getDegree()
                && model.getEps() == oldModel.getEps()
                && model.getGamma() == oldModel.getGamma()
                && model.getNu() == oldModel.getNu()
                && model.getProbability() == oldModel.getProbability()
                && model.getTestPart() == oldModel.getTestPart()) {
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


