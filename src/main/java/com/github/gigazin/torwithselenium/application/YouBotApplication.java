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
 * @version 1.0.0-bt1
 * @since 10/15/2023
 */
public class YouBotApplication extends Application {

    /**
     * Creates and launches the application main stage.
     *
     * @author gigazin
     * @since 10/15/2023
     * @param stage The stage of the application.
     * @throws IOException Thrown if the application fails to create the stage.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(YouBotApplication.class.getResource("youbot-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 420);
        stage.setTitle("YouBot Application");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}