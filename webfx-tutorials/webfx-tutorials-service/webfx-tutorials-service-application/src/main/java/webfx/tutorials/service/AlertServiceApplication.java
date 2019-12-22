package webfx.tutorials.service;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import webfx.tutorials.service.services.alert.AlertService;

/**
 * @author Bruno Salmon
 */
public class AlertServiceApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Alert Application");
        Button btn = new Button();
        btn.setText("Trigger alert");
        btn.setOnAction(event -> AlertService.alert("Alert displayed using a cross platform service!"));

        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}
