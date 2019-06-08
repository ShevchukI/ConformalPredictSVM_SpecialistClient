package com.controllers.windows.dataSet;

//import com.controllers.requests.IllnessController;
import com.controllers.windows.menu.MenuController;
import com.models.DataSet;
import com.models.ParameterSingleObject;
import com.models.Predict;
import com.tools.Constant;
import com.tools.HazelCastMap;
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

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 06.02.2019.
 */
public class DiagnosticMenuController extends MenuController {


    private DataSet dataSet;
//    private IllnessController illnessController;
    private int dataSetId;
    private int configurationId;
    private String[] columns;
    private List<Predict> predictList;
    private ObservableList<Predict> predicts;
    private Predict predict;


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
    public void initialize(Stage stage, Stage newWindow) throws IOException {
        newWindow.setOnHidden(event -> {
            HazelCastMap.getDataSetMap().clear();
        });
        stage.setOnHidden(event -> {
            HazelCastMap.getInstance().getLifecycleService().shutdown();
        });
        setStage(stage);
        setNewWindow(newWindow);
//        illnessController = new IllnessController();
        predict = new Predict();
        predictList = new ArrayList<>();
        stackPane_Table.setVisible(true);
        stackPane_Progress.setVisible(false);
        tableColumn_Class.setSortable(false);
        tableColumn_Class.setCellValueFactory(new PropertyValueFactory<Predict, String>("visibleClass"));
        tableColumn_Credibility.setSortable(false);
        tableColumn_Credibility.setCellValueFactory(new PropertyValueFactory<Predict, String>("visibleConfidence"));
        scrollPane_Data.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane_Data.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        dataSetId = HazelCastMap.getDataSetMap().get(1).getId();
//        dataSetId = Integer.parseInt(HazelCastMap.getMapByName(HazelCastMap.getDataSetMapName()).get("id").toString());
        configurationId = Integer.parseInt(HazelCastMap.getMapByName(HazelCastMap.getMiscellaneousMapName()).get("configurationId").toString());
        createFields(dataSetId);
    }


    public void runDiagnostic(ActionEvent event) throws IOException {
        tableView_Result.setOpacity(0);
        stackPane_Progress.setVisible(true);
        button_Run.setDisable(true);
        ParameterSingleObject objectParameters = new ParameterSingleObject("", 0);
        for (int i = 2; i < columns.length; i++) {
            TextField textField = (TextField) gridPane_Data.lookup("#parameter" + i);
            if (!textField.getText().equals("")) {
                objectParameters.setParams(objectParameters.getParams() + textField.getText());
            } else {
                objectParameters.setParams(objectParameters.getParams() + 0);
            }
            if (i != columns.length - 1) {
                objectParameters.setParams(objectParameters.getParams() + ",");
            }
        }
        HttpResponse response = predict.startSingleTest(configurationId, objectParameters);
        setStatusCode(response.getStatusLine().getStatusCode());
        if (checkStatusCode(getStatusCode())) {
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
                            HttpResponse response = predict.resultSingleTest(processId);
                            setStatusCode(response.getStatusLine().getStatusCode());
                            if (getStatusCode() == 200) {
                                predict = new Predict().fromJson(response);
                                System.out.println(predict.getRealClass() + " : " + predict.getPredictClass() + " : " + predict.getConfidence() + " Sig: " + objectParameters.getSignificance());
                                if (predict.getPredictClass() != 0) {
//                                        if (predict.getRealClass() == predict.getPredictClass()) {
                                    NumberFormat formatter = new DecimalFormat("#00.00");
                                    predict.setVisibleConfidence(String.valueOf(formatter.format(predict.getConfidence() * 100)) + "%");
//                                        } else {
//                                            predict.setVisibleConfidence("");
//                                        }
                                    predictList.clear();
                                    predictList.add(predict);
                                    predicts = FXCollections.observableArrayList(predictList);
                                    tableView_Result.setItems(predicts);
                                    stackPane_Progress.setVisible(false);
                                    tableView_Result.setOpacity(100);

                                }
//                                if (predict.getPredictClass() != 0) {
//                                    if (predict.getRealClass() == predict.getPredictClass() || predict.getRealClass() == 0) {
////                                        switch (predict.getPredictClass()) {
////                                            case 1:
////                                                predict.setVisibleClass("Positive");
////                                                break;
////                                            case -1:
////                                                predict.setVisibleClass("Negative");
////                                                break;
////                                            default:
////                                                break;
////                                        }
//                                        NumberFormat formatter = new DecimalFormat("#00.00");
////                                        predict.setVisibleConfidence(String.valueOf(predict.getConfidence() * 100) + "%");
//                                        predict.setVisibleConfidence(String.valueOf(formatter.format(predict.getConfidence() * 100)) + "%");
//                                    } else {
////                                        predict.setVisibleClass("Uncertain");
//                                        predict.setVisibleConfidence("");
//                                    }
////
////                                    predict.setVisibleConfidence(String.valueOf(predict.getConfidence() * 100) + "%");
//                                    predictList.clear();
//                                    predictList.add(predict);
//                                    predicts = FXCollections.observableArrayList(predictList);
//                                    tableView_Result.setItems(predicts);
//                                    stackPane_Progress.setVisible(false);
//                                    tableView_Result.setOpacity(100);
//                                }
                                Thread.sleep(1000 * 1);
                            } else {
                                return;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    button_Run.setDisable(false);
                }
            });
            calculation.start();
        }
    }

    public void cancel(ActionEvent event) {
        getNewWindow().close();
    }


    private void createFields(int dataSetId) throws IOException {
        HttpResponse response = dataSet.getDataSetById(dataSetId);
        setStatusCode(response.getStatusLine().getStatusCode());
        if (checkStatusCode(getStatusCode())) {
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

