package webfx.demos.gauge;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import webfx.platform.shared.services.scheduler.Scheduler;

import java.util.Random;

/**
 * @author Bruno Salmon
 */
public class GaugeApplication extends Application {

    private static final Random RND = new Random();
    private Gauge gauge;

    @Override
    public void start(Stage stage) {
        gauge = GaugeBuilder.create()
                .skinType(Gauge.SkinType.MODERN)
                .prefSize(400, 400)
                .sectionTextVisible(true)
                .title("MODERN")
                .unit("UNIT")
                .threshold(85)
                .thresholdVisible(true)
                .animated(true)
                //.interactive(true)
                .build();
        StackPane root = new StackPane(gauge);
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        Scene scene = new Scene(root,800, 600);
        stage.setScene(scene);
        stage.setTitle("JavaFx Modern Gauge");
        stage.show();
        Scheduler.schedulePeriodic(1500, () -> gauge.setValue(RND.nextDouble() * gauge.getRange() + gauge.getMinValue()));
    }

}
