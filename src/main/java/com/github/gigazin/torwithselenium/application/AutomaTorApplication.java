package com.github.gigazin.torwithselenium.application;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.Objects;

/**
 * The application "main" class where the stage is created and launched.
 *
 * @author gigazin
 * @version 1.0.0-bt2
 * @since 1.0.0-bt1
 */
public class AutomaTorApplication extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

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
        Parent root = FXMLLoader.load(Objects.requireNonNull(AutomaTorApplication.class.getResource("automator-view.fxml")));
        Scene scene = new Scene(root, 900, 600);
        scene.setFill(Color.TRANSPARENT);
        stage.setTitle("AutomaTor Application");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        makeDraggable(root, stage);
        stage.show();
    }

    public void makeDraggable(Parent root, Stage stage) {
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                xOffset = mouseEvent.getSceneX();
                yOffset = mouseEvent.getSceneY();
            }
        });

        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.setX(mouseEvent.getScreenX() - xOffset);
                stage.setY(mouseEvent.getScreenY() - yOffset);
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}