package com.controllers.windows.dataset;

import com.controllers.requests.ConfigurationController;
import com.controllers.requests.DataSetController;
import com.controllers.windows.menu.MenuController;
import com.models.Dataset;
import com.models.Predict;
import com.tools.Constant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Admin on 22.02.2019.
 */
public class DetailsResultMenuController extends MenuController {


    @Autowired
    HttpResponse response;

    private int statusCode;
    private int configId;
    private ConfigurationController configurationController = new ConfigurationController();
    private ArrayList<Predict> predictArrayList;
    private ObservableList<Predict> predicts;
    private DataSetController dataSetController = new DataSetController();

    @FXML
    private TextField textField_FileName;
    @FXML
    private TableView tableView_Results;
    @FXML
    private TableColumn tableColumn_Id;
    @FXML
    private TableColumn tableColumn_RealClass;
    @FXML
    private TableColumn tableColumn_PredictClass;
    @FXML
    private TableColumn tableColumn_Confidence;
    @FXML
    private TableColumn tableColumn_Credibility;
    @FXML
    private TableColumn tableColumn_AlphaPositive;
    @FXML
    private TableColumn tableColumn_AlphaNegative;
    @FXML
    private TableColumn tableColumn_Parameters;


    @FXML
    public void initialize(Stage stage, Stage newWindow) throws IOException {
        setStage(stage);
        setNewWindow(newWindow);
        configId = Integer.parseInt(Constant.getMapByName("misc").get("configurationId").toString());
        response = configurationController.getDetaildeResult(Constant.getAuth(), configId);
        statusCode = response.getStatusLine().getStatusCode();
        if (checkStatusCode(statusCode)) {
            predictArrayList = Constant.getPredictListFromJson(response);
            predicts = FXCollections.observableArrayList(predictArrayList);
            tableView_Results.setItems(predicts);
            for (Predict predict : predictArrayList) {
                predict.setVisibleParameters(predict.getDatasetObjectsEntity().getParams());
            }
        }
        tableColumn_Id.setCellValueFactory(new PropertyValueFactory<Predict, Integer>("id"));
        tableColumn_RealClass.setCellValueFactory(new PropertyValueFactory<Predict, Integer>("realClass"));
        tableColumn_PredictClass.setCellValueFactory(new PropertyValueFactory<Predict, Integer>("predictClass"));
        tableColumn_Confidence.setCellValueFactory(new PropertyValueFactory<Predict, Double>("confidence"));
        tableColumn_Credibility.setCellValueFactory(new PropertyValueFactory<Predict, Double>("credibility"));
        tableColumn_AlphaPositive.setCellValueFactory(new PropertyValueFactory<Predict, Double>("alphaPositive"));
        tableColumn_AlphaNegative.setCellValueFactory(new PropertyValueFactory<Predict, Double>("alphaNegative"));
        tableColumn_Parameters.setCellValueFactory(new PropertyValueFactory<Predict, String>("visibleParameters"));
    }

    public void chooseDirectory(ActionEvent event) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(getNewWindow());
        if (file != null) {
            textField_FileName.setText(file.getAbsolutePath() + Constant.getMapByName("dataset").get("name"));
        }
    }

    public void cancel(ActionEvent event) {
        getNewWindow().close();
    }

    public void save(ActionEvent event) throws IOException {
        int datasetId = Integer.parseInt(Constant.getMapByName("dataset").get("id").toString());
        response = dataSetController.getDatasetById(Constant.getAuth(), datasetId);
        statusCode = response.getStatusLine().getStatusCode();
        if(checkStatusCode(statusCode)){
            Dataset dataset = new Dataset().fromJson(response);
            String[] column = dataset.getColumns().split(",");
            Constant.printTableAndMatrix(textField_FileName.getText(), predictArrayList);
        }
    }
}
