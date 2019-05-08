package webfx.tutorial.css;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * @author Bruno Salmon
 */
public class CssApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Css");

        Region r = new Region();
        r.setId("region");

        Scene scene = new Scene(new StackPane(r), 300, 250);
        scene.getStylesheets().add("webfx/tutorial/css/css/app.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
