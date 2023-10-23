package com.github.gigazin.torwithselenium.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The application "main" class where the stage is created and launched.
 *
 * @author gigazin
 * @version 1.0.0-bt2
 * @since 1.0.0-bt1
 */
public class AutomaTorApplication extends Application {

    /**
     * Creates and launches the application main stage.
     *
     * @author gigazin
     * @since 1.0.0-bt1
     * @param stage The stage of the application.
     * @throws IOException Thrown if the application fails to create the stage.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AutomaTorApplication.class.getResource("automator-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        stage.setTitle("AutomaTor Application");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}