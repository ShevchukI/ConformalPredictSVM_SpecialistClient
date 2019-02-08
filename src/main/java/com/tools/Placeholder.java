package com.tools;

import javafx.scene.control.Alert;

/**
 * Created by Admin on 07.01.2019.
 */
public class Placeholder {

    private final static String PLACEHOLDER = "PLACEHOLDER";

    public Placeholder() {
    }

    public void getAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(PLACEHOLDER);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
