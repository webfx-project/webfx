package webfx.tutorial.customcontrol;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.tutorial.customcontrol.clock.Clock;
import webfx.tutorial.customcontrol.clock.ClockBuilder;

/**
 * @author Bruno Salmon
 */
public class CustomControlApplication extends Application {

    @Override
    public void start(Stage stage) {
        Clock clock = ClockBuilder.create()
                .secondsVisible(true)
                .secondColor(Color.RED)
                .skinType(Clock.ClockSkinType.YOTA2)
                .build();

        StackPane pane = new StackPane(clock);
        pane.setPadding(new Insets(10));

        stage.setTitle("Custom control");
        stage.setScene(new Scene(pane));
        stage.show();

        UiScheduler.schedulePeriodic(1000, () -> clock.setTime(System.currentTimeMillis() / 1000));
    }
}
