package com.controllers.windows.dataSet;

import com.controllers.requests.DataSetController;
import com.controllers.requests.IllnessController;
import com.controllers.windows.menu.MenuController;
import com.models.DataSet;
import com.models.ParameterSingleObject;
import com.models.Predict;
import com.tools.Constant;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 06.02.2019.
 */
public class DiagnosticMenuController extends MenuController {

    @Autowired
    HttpResponse response;

    private DataSet dataSet;
//    private ConfigurationEntity configurationEntity;
    private int statusCode;
    private int dataSetId;
    private int configurationId;
    private String[] columns;
    private DataSetController dataSetController = new DataSetController();
//    private ConfigurationController configurationController = new ConfigurationController();
    private IllnessController illnessController = new IllnessController();
    private List<Predict> predictList = new ArrayList<>();
    private ObservableList<Predict> predicts;
    private Predict predict;

    @FXML
    private CheckBox checkBox_Significance;
    @FXML
    private Slider slider_Significance;
    @FXML
    private TextField textField_Significance;
    @FXML
    private ScrollPane scrollPane_Data;
    @FXML
    private GridPane gridPane_Data;
    @FXML
    private TableView<Predict> tableView_Result;
    @FXML
    private TableColumn tableColumn_Class;
    @FXML
    private TableColumn tableColumn_Credibility;
    @FXML
    private StackPane stackPane_Table;
    @FXML
    private StackPane stackPane_Progress;
    @FXML
    private Button button_Run;
    @FXML
    private Button button_Ok;

    @FXML
    public void initialize(Stage stage, Stage newWindow) throws IOException {
        setStage(stage);
        setNewWindow(newWindow);
        stackPane_Table.setVisible(true);
        stackPane_Progress.setVisible(false);
        tableColumn_Class.setSortable(false);
        tableColumn_Class.setCellValueFactory(new PropertyValueFactory<Predict, String>("visibleClass"));
        tableColumn_Credibility.setSortable(false);
        tableColumn_Credibility.setCellValueFactory(new PropertyValueFactory<Predict, String>("visibleCredibility"));
        NumberFormat formatter = new DecimalFormat("#0.00");
        slider_Significance.disableProperty().bind(checkBox_Significance.selectedProperty().not());
        textField_Significance.disableProperty().bind(checkBox_Significance.selectedProperty().not());
        slider_Significance.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                textField_Significance.setText(String.valueOf(formatter.format(Double.parseDouble(String.valueOf(new_val))).replace(",", ".")));
            }
        });
        textField_Significance.setText(String.valueOf(formatter.format(Double.parseDouble(String.valueOf(slider_Significance.getValue()))).replace(",", ".")));
        scrollPane_Data.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane_Data.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        dataSetId = Integer.parseInt(Constant.getMapByName("dataSet").get("id").toString());
        configurationId = Integer.parseInt(Constant.getMapByName("misc").get("configurationId").toString());
        createFields(dataSetId);
    }


    public void runDiagnostic(ActionEvent event) throws IOException {
        tableView_Result.setOpacity(0);
        stackPane_Progress.setVisible(true);
        button_Run.setDisable(true);
        ParameterSingleObject parameterSingleObject = new ParameterSingleObject("", 0);
        for (int i = 2; i < columns.length; i++) {
            TextField textField = (TextField) gridPane_Data.lookup("#parameter" + i);
            if (!textField.getText().equals("")) {
                parameterSingleObject.setParams(parameterSingleObject.getParams() + textField.getText());
            } else {
                parameterSingleObject.setParams(parameterSingleObject.getParams() + 0);
            }
            if (i != columns.length - 1) {
                parameterSingleObject.setParams(parameterSingleObject.getParams() + ",");
            }

        }
        if (checkBox_Significance.isSelected()) {
            parameterSingleObject.setSignificance((100 - Double.parseDouble(textField_Significance.getText())) / 100);
        } else {
            parameterSingleObject.setSignificance(100);
        }
        System.out.println(parameterSingleObject.toString());
        response = illnessController.startSingleTest(Constant.getAuth(), configurationId, parameterSingleObject);
        statusCode = response.getStatusLine().getStatusCode();
        System.out.println("First request: " + statusCode);
        if (checkStatusCode(statusCode)) {
            int processId = Integer.parseInt(Constant.responseToString(response));
            Thread calculation = new Thread(new Runnable() {
                @Override
                public void run() {
//                    button_Run.setDisable(true);
                    predict = new Predict();
                    predict.setPredictClass(0);
//                    double progress = 0;
                    while (predict.getPredictClass() == 0) {
                        try {
                            response = illnessController.resultSingleTest(Constant.getAuth(), processId);
                            statusCode = response.getStatusLine().getStatusCode();
                            System.out.println("Second request: " + statusCode);
                            if (statusCode == 200) {
                                predict = new Predict().fromJson(response);
                                System.out.println(predict.getRealClass() + " : " + predict.getPredictClass() + " : " + predict.getCredibility());
                                if (predict.getPredictClass() != 0) {
                                    if (predict.getRealClass() == predict.getPredictClass()) {
                                        switch (predict.getPredictClass()) {
                                            case 1:
                                                predict.setVisibleClass("Positive");
                                                break;
                                            case -1:
                                                predict.setVisibleClass("Negative");
                                                break;
                                            default:
                                                break;
                                        }
//                                        predict.setVisibleClass(String.valueOf(predict.getPredictClass()));
//                                        predict.setVisibleCredibility(String.valueOf(predict.getCredibility() * 100) + "%");
                                        predict.setVisibleConfidence(String.valueOf(predict.getConfidence() * 100) + "%");
                                    } else {
                                        predict.setVisibleClass("Uncertain");
                                        predict.setVisibleConfidence("");
                                    }
                                    predictList.clear();
                                    predictList.add(predict);
                                    predicts = FXCollections.observableArrayList(predictList);
                                    tableView_Result.setItems(predicts);
//                                    stackPane_Table.setVisible(true);
                                    stackPane_Progress.setVisible(false);
                                    tableView_Result.setOpacity(100);

                                }
                                Thread.sleep(1000 * 1);
                            } else {
                                return;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

//                        button_Run.setDisable(false);
                    }
                    button_Run.setDisable(false);
                    button_Ok.setDisable(false);
                }
            });
            calculation.start();
//            response = illnessController.resultSingleTest(Constant.getAuth(), processId);
//            statusCode = response.getStatusLine().getStatusCode();
//            System.out.println("Second request: " + statusCode);
//            if (checkStatusCode(statusCode)) {
//                Predict predict = new Predict().fromJson(response);
//                if (predict.getRealClass() == predict.getPredictClass()) {
//                    predict.setVisibleClass(String.valueOf(predict.getPredictClass()));
//                    predict.setVisibleCredibility(String.valueOf(predict.getCredibility() * 100) + "%");
//                } else {
//                    predict.setVisibleClass("Uncertain");
//                    predict.setVisibleCredibility("");
//                }
//                predictList.clear();
//                predictList.add(predict);
//                predicts = FXCollections.observableArrayList(predictList);
//                tableView_Result.setItems(predicts);
//                System.out.println(predict.getRealClass() + " : " + predict.getPredictClass() + " : " + predict.getCredibility());
            }
        }

    public void cancel(ActionEvent event) {
        getNewWindow().close();
    }


    public void setSignificanceValue(ActionEvent event) {
        setSignificanceValue();
    }

    public void setSignificanceValue() {
        slider_Significance.setValue(Double.parseDouble(textField_Significance.getText()));
    }

    private void createFields(int dataSetId) throws IOException {
        response = dataSetController.getDataSetById(Constant.getAuth(), dataSetId);
        statusCode = response.getStatusLine().getStatusCode();
        if (checkStatusCode(statusCode)) {
            dataSet = new DataSet().fromJson(response);
            columns = dataSet.getColumns().split(",");
            for (int i = 2; i < columns.length; i++) {
                Label label = new Label(columns[i]);
                TextField textField = new TextField();
                textField.setId("parameter" + i);
                textField.setMaxWidth(100.0);
                gridPane_Data.add(label, 0, i);
                gridPane_Data.setHalignment(label, HPos.LEFT);
                gridPane_Data.add(textField, 1, i);
                gridPane_Data.setHalignment(textField, HPos.RIGHT);

                gridPane_Data.setMargin(label, new Insets(14, 5, 10, 14));
                gridPane_Data.setMargin(textField, new Insets(14, 14, 10, 5));
            }
        }
    }
}

