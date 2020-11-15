package webfx.demo.medusaclock;

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
import webfx.demo.medusaclock.hansolo.Clock;
import webfx.demo.medusaclock.hansolo.skins.ClockSkin;
import webfx.demo.medusaclock.hansolo.skins.DBClockSkin;
import webfx.demo.medusaclock.hansolo.skins.MorphingClockSkin;
import webfx.demo.medusaclock.hansolo.skins.TileClockSkin;

/**
 * @author Bruno Salmon
 */
public final class MedusaClockApplication extends Application {

    private final BorderPane borderPane = new BorderPane();
    private final CheckBox discreteCheckbox = new CheckBox("Discrete");
    private final ToggleGroup skinGroup = new ToggleGroup();
    private Clock clock;

    @Override
    public void start(Stage stage) {
        HBox hBox = new HBox(
                createSkinButton("Skin 1", this::createYota2Clock),
                createSkinButton("Skin 2", this::createDBClock),
                createSkinButton("Skin 3", this::createTileClock),
                createSkinButton("Skin 4", this::createMorphingClock),
                discreteCheckbox);
        hBox.setAlignment(Pos.CENTER);
        HBox.setMargin(discreteCheckbox, new Insets(0, 0, 0, 10));
        discreteCheckbox.setAlignment(Pos.CENTER);
        discreteCheckbox.setTextFill(Color.WHITE);
        borderPane.setTop(hBox);

        borderPane.setBackground(new Background(new BackgroundFill(LinearGradient.valueOf("linear-gradient(to right, #43cea2, #185a9d)"), null, null)));

        stage.setTitle("Medusa clock");
        stage.setScene(new Scene(borderPane, 600, 600));
        stage.show();

        createYota2Clock();
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                clock.setTimeMs(System.currentTimeMillis());
            }
        }.start();
    }

    private ToggleButton createSkinButton(String text, Runnable actionRunnable) {
        ToggleButton skinButton = new ToggleButton(text);
        skinButton.setToggleGroup(skinGroup);
        skinButton.setOnAction(e -> actionRunnable.run());
        return skinButton;
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
