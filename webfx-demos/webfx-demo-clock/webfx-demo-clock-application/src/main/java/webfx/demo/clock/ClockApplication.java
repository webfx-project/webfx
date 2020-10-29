package webfx.demo.clock;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.stage.Stage;
import webfx.demo.clock.hansolo.Clock;
import webfx.demo.clock.hansolo.skins.ClockSkin;
import webfx.demo.clock.hansolo.skins.DBClockSkin;
import webfx.demo.clock.hansolo.skins.MorphingClockSkin;
import webfx.demo.clock.hansolo.skins.TileClockSkin;

/**
 * @author Bruno Salmon
 */
public class ClockApplication extends Application {

    private final BorderPane borderPane = new BorderPane();
    private CheckBox discreteCheckbox = new CheckBox("Discrete");
    private Clock clock;

    @Override
    public void start(Stage stage) {
        ToggleGroup skinGroup = new ToggleGroup();

        ToggleButton skin1Button = new ToggleButton("Skin 1");
        skin1Button.setToggleGroup(skinGroup);
        skin1Button.setOnAction(e -> createYota2Clock());

        ToggleButton skin2Button = new ToggleButton("Skin 2");
        skin2Button.setToggleGroup(skinGroup);
        skin2Button.setOnAction(e -> createDBClock());

        ToggleButton skin3Button = new ToggleButton("Skin 3");
        skin3Button.setToggleGroup(skinGroup);
        skin3Button.setOnAction(e -> createTileClock());

        ToggleButton skin4Button = new ToggleButton("Skin 4");
        skin4Button.setToggleGroup(skinGroup);
        skin4Button.setOnAction(e -> createMorphingClock());

        skin1Button.fire();

        HBox.setMargin(discreteCheckbox, new Insets(0, 0, 0, 10));
        discreteCheckbox.setAlignment(Pos.CENTER);
        HBox hBox = new HBox(skin1Button, skin2Button, skin3Button, skin4Button, discreteCheckbox);
        hBox.setAlignment(Pos.CENTER);
        borderPane.setTop(hBox);

        borderPane.setBackground(new Background(new BackgroundFill(LinearGradient.valueOf("linear-gradient(to right, #43cea2, #185a9d)"), null, null)));
        discreteCheckbox.setTextFill(Color.WHITE);

        stage.setTitle("Custom control");
        stage.setScene(new Scene(borderPane, 600, 600));
        stage.show();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                clock.setTimeMs(System.currentTimeMillis());
            }
        }.start();
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

    private void createMorphingClock() {
        createClock();
        clock.setMinuteColor(Color.BLUE);
        clock.setHourColor(Color.GREEN);
        clock.setSkin(new MorphingClockSkin(clock));
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

    private void createTileClock() {
        createClock();
        clock.setBackgroundPaint(Color.rgb(42,42,42));
        clock.setHourColor(Color.rgb(238, 238, 238));
        clock.setMinuteColor(Color.rgb(238, 238, 238));
        //clock.setSecondColor(Color.rgb(238, 238, 238));
        clock.setKnobColor(Color.rgb(238, 238, 238));
        clock.setHourTickMarkColor(Color.rgb(238, 238, 238));
        clock.setMinuteTickMarkColor(Color.rgb(238, 238, 238));
        clock.setDateColor(Color.rgb(238, 238, 238));
        clock.setDateVisible(false);
        //clock.setSecondsVisible(false);
        clock.setTextVisible(false);
        clock.setTextColor(Color.rgb(238, 238, 238));
        clock.setTitleVisible(true);
        clock.setTitleColor(Color.rgb(238, 238, 238));
        clock.setSkin(new TileClockSkin(clock));
    }
}
