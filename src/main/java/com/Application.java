package com;

import com.controllers.windows.menu.WindowsController;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        new WindowsController().start(stage);
    }

}
