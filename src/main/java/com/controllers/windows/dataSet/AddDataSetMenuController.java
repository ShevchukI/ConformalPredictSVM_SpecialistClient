package com.controllers.windows.dataSet;

import com.controllers.windows.menu.MainMenuController;
import com.controllers.windows.menu.MenuBarController;
import com.controllers.windows.menu.MenuController;
import com.controllers.windows.menu.WindowsController;
import com.models.DataSet;
import com.tools.Constant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Admin on 07.02.2019.
 */
public class AddDataSetMenuController extends MenuController {

    private MainMenuController mainMenuController;
    private ArrayList<String> filterList;
    private File file;
    private WindowsController windowsController;
    private DataSet dataSet;

    @FXML
    private TextField textField_FileName;
    @FXML
    private TextField textField_DataSetName;
    @FXML
    private TextArea textArea_Description;
    @FXML
    private TextArea textArea_Columns;
    @FXML
    private TextArea textArea_Content;
    @FXML
    private MenuBarController menuBarController;
    @FXML
    private TextArea textArea_Error;

    public void initialize(Stage stage, Stage newWindow) {
//        stage.setOnHidden(event -> {
//            HazelCastMap.getInstance().getLifecycleService().shutdown();
//        });
        setStage(stage);
        setNewWindow(newWindow);
        menuBarController.init(this);
        mainMenuController = new MainMenuController();
        windowsController = new WindowsController();
        dataSet = new DataSet();
        textArea_Error.setEditable(false);
        textArea_Error.setVisible(false);
        textArea_Error.setWrapText(true);
        filterList = new ArrayList<String>();
        filterList.add("*.csv");
        filterList.add("*.txt");
        textField_FileName.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                String fileName = file.getName().substring(0, file.getName().length() - 4);
                textField_DataSetName.setText(fileName);
            } catch (NullPointerException e) {
                System.out.println(e);
            }
        });
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
            textArea_Content.clear();
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
        int error = 0;
        textArea_Error.setStyle("-fx-border-color: inherit");
        textArea_Error.clear();
        textArea_Error.setVisible(false);
        try {
            if (!file.exists()) {
                textArea_Error.appendText("File not found!");
                textArea_Error.setStyle("-fx-border-color: red");
                textArea_Error.setStyle("-fx-text-fill: red");
                textArea_Error.setVisible(true);
                error++;
            }
        } catch (NullPointerException e) {
            textArea_Error.appendText("File not found!");
            textArea_Error.setStyle("-fx-border-color: red");
            textArea_Error.setStyle("-fx-text-fill: red");
            textArea_Error.setVisible(true);
            error++;
            return;
        }
        if (!checkColumns(textArea_Columns.getText())) {
            error++;
        }
        String fileName = "file.txt";
        File fileBuf = new File(fileName);
        FileWriter writer = new FileWriter(fileBuf.getAbsolutePath());
//        FileReader input = new FileReader(textField_FileName.getText());
//        BufferedReader bufRead = new BufferedReader(input);
//        String line = bufRead.readLine();
//        textArea_Columns.setText(line);
        String[] mainContent = textArea_Content.getText().split("\n");
        textArea_Content.clear();
        String[][] content = new String[mainContent.length][3];
        for (int i = 0; i < mainContent.length; i++) {
            String[] split = mainContent[i].split(",");
            content[i][0] = split[0];
            content[i][1] = mainContent[i].substring(split[0].length() + 1, mainContent[i].length());
            content[i][2] = split[1];
            if (!checkObjectLength(textArea_Columns.getText(), content[i][0] + "," + content[i][1])) {
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
        mainContent = textArea_Content.getText().split("\n");
        if (error != 0) {
            textArea_Error.setStyle("-fx-border-color: red");
            textArea_Error.setStyle("-fx-text-fill: red");
            textArea_Error.setVisible(true);
        } else {
            int statusCode = dataSet.addNew(textField_DataSetName.getText(),
                    textArea_Description.getText(),
                    textArea_Columns.getText(), fileBuf);

            if (checkStatusCode(statusCode)) {
                fileBuf.delete();
            }
            Constant.getAlert(null, "Data Set saved!", Alert.AlertType.INFORMATION);
            windowsController.openWindowResizable("menu/mainMenu", getStage(), mainMenuController, "Main menu", 600, 640);
            getNewWindow().close();
        }
    }

    public void cancel(ActionEvent event) {
        getNewWindow().close();
    }

    public void fillUpData(ActionEvent event) {
        fillUpData(textField_FileName.getText());
    }

    public void fillUpData(String fileName) {
        try {
            FileReader input = new FileReader(fileName);
            BufferedReader bufRead = new BufferedReader(input);
            String line = bufRead.readLine();
            textArea_Columns.setText(line);
            while ((line = bufRead.readLine()) != null) {
                line = line.replaceAll("M", "-1");
                line = line.replaceAll("B", "1");
                textArea_Content.appendText(line + "\n");
            }
        } catch (FileNotFoundException e1) {
            textArea_Error.appendText("File not found!");
            textArea_Error.setStyle("-fx-border-color: red");
            textArea_Error.setStyle("-fx-text-fill: red");
            textArea_Error.setVisible(true);
            System.out.println("file not found!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkColumns(String columns) {
        if (columns.isEmpty()) {
            textArea_Error.appendText("Columns error! Columns is empty!\n");
            return false;
        } else {
            return true;
        }
    }

    public boolean checkObjectLength(String columns, String object) {
        String[] col = columns.split(",");
        String[] obj = object.split(",");
        if (col.length > obj.length) {
            textArea_Error.appendText("Object error! Object [" + object + "] lacks " + (col.length - obj.length) + "  feature!\n");
            return false;
        } else if (col.length < obj.length) {
            textArea_Error.appendText("Object error! Object [" + object + "] have " + (obj.length - col.length) + " excessive feature!\n");
            return false;
        } else {
            return true;
        }
    }

    public boolean checkObjectClass(String[][] content, int i) {
        if (content[i][2].equals("M")) {
            content[i][2] = content[i][2].replace("M", "-1");
        } else if (content[i][2].equals("B")) {
            content[i][2] = content[i][2].replace("B", "1");
        }
        if (!content[i][2].equals("1") && !content[i][2].equals("-1")) {
            textArea_Error.appendText("Class error! Current: " + content[i][2] + " needed: 1 or -1:\n"
                    + "Object: " + content[i][0] + "," + content[i][1] + ";\n");
            return false;
        } else {
            return true;
        }
    }
}
