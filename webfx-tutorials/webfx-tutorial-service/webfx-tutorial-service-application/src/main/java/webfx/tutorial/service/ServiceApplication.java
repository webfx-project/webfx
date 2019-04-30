package webfx.tutorial.service;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import webfx.tutorial.service.services.alert.AlertService;
import webfx.tutorial.service.services.console.Console;
import webfx.tutorial.service.services.forecast.ForecastService;

/**
 * @author Bruno Salmon
 */
public class ServiceApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Console.log("Starting...");
        primaryStage.setTitle("Alert Application");
        Button btn = new Button();
        btn.setText("Trigger alert");
        btn.setOnAction(event -> ForecastService.getWeekForecast("Here").setHandler(ar ->
                        AlertService.alert(ar.result().iterator().next().getSkyState().toString())
                ));

        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}
