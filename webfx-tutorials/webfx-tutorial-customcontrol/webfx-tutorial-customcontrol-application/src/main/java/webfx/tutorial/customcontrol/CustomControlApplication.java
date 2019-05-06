package webfx.tutorial.customcontrol;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import webfx.platform.client.services.uischeduler.AnimationFramePass;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.tutorial.customcontrol.clock.Clock;
import webfx.tutorial.customcontrol.clock.skins.ClockSkin;
import webfx.tutorial.customcontrol.clock.skins.DBClockSkin;

/**
 * @author Bruno Salmon
 */
public class CustomControlApplication extends Application {

    private final BorderPane borderPane = new BorderPane();
    private CheckBox discreteCheckbox = new CheckBox("Discrete");
    private Clock clock;

    @Override
    public void start(Stage stage) {
        ToggleGroup skinGroup = new ToggleGroup();

        ToggleButton yota2Button = new ToggleButton("Yota2");
        yota2Button.setToggleGroup(skinGroup);
        yota2Button.setOnAction(e -> createYota2Clock());

        ToggleButton dbButton = new ToggleButton("DB");
        dbButton.setToggleGroup(skinGroup);
        dbButton.setOnAction(e -> createDBClock());

        discreteCheckbox.setSelected(true);

        createYota2Clock();

        borderPane.setTop(new HBox(10, yota2Button, dbButton, discreteCheckbox));

        stage.setTitle("Custom control");
        stage.setScene(new Scene(borderPane));
        stage.show();

        UiScheduler.schedulePeriodicInAnimationFrame(() -> clock.setTimeMs(System.currentTimeMillis()), AnimationFramePass.PROPERTY_CHANGE_PASS);
    }

    private void createClock() {
        clock = new Clock();
        clock.setSecondsVisible(true);
        clock.setSecondColor(Color.RED);
        clock.discreteSecondsProperty().bind(discreteCheckbox.selectedProperty());
        BorderPane.setMargin(clock, new Insets(10));
        borderPane.setCenter(clock);
    }

    private void createDBClock() {
        createClock();
        clock.setSkin(new DBClockSkin(clock));
    }

    private void createYota2Clock() {
        createClock();
        clock.setBackgroundPaint(Color.rgb(40, 42, 48));
        clock.setHourTickMarkColor(Color.rgb(255, 255, 255));
        clock.setMinuteTickMarkColor(Color.rgb(255, 255, 255, 0.5));
        clock.setHourColor(Color.WHITE);
        clock.setMinuteColor(Color.WHITE);
        clock.setKnobColor(Color.WHITE);
        clock.setTextColor(Color.rgb(255, 255, 255, 0.5));
        clock.setDateColor(Color.rgb(255, 255, 255));
        clock.setSkin(new ClockSkin(clock));
    }
}
