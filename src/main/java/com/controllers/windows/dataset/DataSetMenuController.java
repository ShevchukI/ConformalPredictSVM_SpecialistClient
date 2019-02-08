package com.controllers.windows.dataset;

import com.controllers.windows.menu.MenuBarController;
import com.controllers.windows.menu.MenuController;
import com.hazelcast.core.HazelcastInstance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Admin on 07.02.2019.
 */
public class DataSetMenuController extends MenuController {

    private ArrayList<String> filterList = new ArrayList<String>();
    private File file;

    @FXML
    private TextField textField_FileName;
    @FXML
    private TextField textField_DatasetName;
    @FXML
    private TextArea textArea_Description;
    @FXML
    private TextArea textArea_Columns;
    @FXML
    private TextArea textArea_Content;
    @FXML
    private MenuBarController menuBarController;

    public void initialize(Stage stage, HazelcastInstance hazelcastInstance, Stage newWindow) {
        userMap = hazelcastInstance.getMap("userMap");
        stage.setOnHidden(event -> {
            hazelcastInstance.getLifecycleService().shutdown();
        });
        setStage(stage);
        setInstance(hazelcastInstance);
        setNewWindow(newWindow);
        menuBarController.init(this);

        filterList.add("*.csv");
        filterList.add("*.txt");

        textField_FileName.textProperty().addListener((observable, oldValue, newValue) -> {
            String fileName = file.getName().substring(0, file.getName().length() - 4);
            textField_DatasetName.setText(fileName);
        });
    }

    public void chooseFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Files", filterList);
        fileChooser.getExtensionFilters().add(extensionFilter);
        file = fileChooser.showOpenDialog(null);
        try {
            if (fileChooser != null) {
                textField_FileName.setText(file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        if (textField_FileName.getText() != null) {
            fillUpData(textField_FileName.getText());
        }

        try {
            System.out.println(file.getAbsolutePath());
            System.out.println(file.getPath());
            System.out.println(file.getName());
        } catch (NullPointerException e) {
            System.out.println("file not choose!");
        }

    }

    public void save(ActionEvent event) throws IOException {
        File file1 = new File("F:\\testsingDataset.txt");
        FileWriter writer = new FileWriter(file1);
        String[] content = textArea_Content.getText().split("\n");
        for (int i = 0; i<content.length; i++){
            writer.write(content[i]+"\r\n");
        }
        writer.flush();
        writer.close();
        System.out.println("save");
    }

    public void cancel(ActionEvent event) {
        getNewWindow().close();
    }

    public void fillUpData(String fileName) {
        try {
            FileReader input = new FileReader(fileName);
            BufferedReader bufRead = new BufferedReader(input);
            String line = bufRead.readLine();
            textArea_Columns.setText(line);
            while ((line = bufRead.readLine()) != null) {
                textArea_Content.appendText(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
