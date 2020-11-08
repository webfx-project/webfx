/*
 * Copyright (c) 2015 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package webfx.demo.enzoclocks;

import eu.hansolo.enzo.clock.Clock;
import eu.hansolo.enzo.clock.ClockBuilder;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;
import webfx.demo.enzoclocks.circlespacker.CirclesPackerPane;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.shared.services.resource.ResourceService;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public final class EnzoClocksApplication extends Application {

    private final static List<String> AVAILABLE_ZONE_IDS = ZoneId.getAvailableZoneIds().stream().filter(z -> !zoneShortName(z).equals(zoneShortName(z).toUpperCase())).collect(Collectors.toList());

    private final List<ClockSetting> clockSettings = new ArrayList<>(Arrays.asList(
            new ClockSetting("America/Los_Angeles", "San Francisco", Clock.Design.BOSCH),
            new ClockSetting("America/New_York", null, Clock.Design.IOS6),
            new ClockSetting("Europe/Berlin", null, Clock.Design.DB),
            new ClockSetting("Australia/Sydney", null, Clock.Design.BRAUN)
    ));
    private final CirclesPackerPane circlesPackerPane = new CirclesPackerPane(clockSettings.stream().map(s -> s.clock).toArray(Node[]::new));

    class ClockSetting {
        ZoneId zoneId;
        String text;
        Clock.Design design;
        boolean discreteSecond;
        Clock clock;

        public ClockSetting(String zoneName, String text, Clock.Design design) {
            this.zoneId = ZoneId.of(zoneName);
            this.text = text != null ? text : zoneShortName(zoneName);
            this.design = design;
            discreteSecond = true;
            clock = ClockBuilder.create()
                    .design(design)
                    .text(this.text)
                    .autoNightMode(true)
                    .discreteSecond(discreteSecond)
                    .time(LocalTime.now(zoneId))
                    .build();
            clock.setOnMouseClicked(e -> removeClock(this));
        }
    }

    private static String zoneShortName(String zoneName) {
        return zoneName.substring(zoneName.lastIndexOf('/') + 1).replace("_", " ");
    }

    private static int clampRandom(double random, int n) {
        return (int) Math.floor(random * n);
    }

    private void addClock(ClockSetting clockSetting) {
        clockSettings.add(clockSetting);
        circlesPackerPane.getChildren().add(clockSetting.clock);
    }

    private void removeClock(ClockSetting clockSetting) {
        clockSettings.remove(clockSetting);
        circlesPackerPane.getChildren().remove(clockSetting.clock);
    }

    @Override
    public void start(Stage stage) {
        LedButton plusButton = new LedButton(Color.GREEN.brighter(), true);
        plusButton.setOnAction(e -> addClock(Stream.generate(Math::random).map(r -> AVAILABLE_ZONE_IDS.get(clampRandom(r, AVAILABLE_ZONE_IDS.size()))).filter(z -> clockSettings.stream().noneMatch(s -> s.zoneId.toString().equals(z))).map(z -> new ClockSetting(z, null, Clock.Design.values()[clampRandom(Math.random(), Clock.Design.values().length)])).findFirst().orElse(null)));
        plusButton.setMaxSize(100, 100);
        StackPane.setAlignment(plusButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(plusButton, new Insets(10));
        Pane root = new StackPane(circlesPackerPane, plusButton);
        // Purple love: #753a88, #cc2b5e
        // Cherry: #eb3349, #f45c43
        // Kashmir: #614385, #516395
        // Virgin America: #7b4397, #dc2430
        // Endless river: #43cea2, #185a9d
        // Dusk: #ffd89b → #19547b
        // Relay: #3a1c71 → #d76d77 → #ffaf7b
        // Celestial: #c33764, #1d2671
        // Aubergine: #aa076b, #61045f
        // Bluelago: #0052d4, #4364f7, #6fb1fc
        // JShine: #12c2e9, #c471ed, #f64f59
        // Flare: #f12711, #f5af19
        // Ultra Violet:  #654ea3, #eaafc8
        // Rastafari: #1e9600, #fff200, #ff0000
        // Blue Raspberry: #00b4db, #0083b0
        // Sublime Vivid: #fc466b, #3f5efb
        // Lawrencium: #0f0c29, #302b63, #24243e
        // Argon: to top left, #03001e, #7303c0, #ec38bc, #fdeff9
        // Summer: #22c1c3, #fdbb2d
        // Visions of Grandeur: #000046, #1cb5e0
        root.setBackground(new Background(new BackgroundFill(LinearGradient.valueOf("to top left, #000046, #1cb5e0"), null, null)));

        updateClockTimes();
        UiScheduler.schedulePeriodic(20, this::updateClockTimes);

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(ResourceService.toUrl("/eu/hansolo/enzo/clock/clock.css", getClass()));
        stage.setTitle("Enzo Clocks");
        stage.setScene(scene);
        stage.show();
    }

    private int discreteSecond;

    private void updateClockTimes() {
        if (clockSettings.isEmpty())
            return;
        LocalTime[] clockLocalTimes = clockSettings.stream().map(s -> LocalTime.now(s.zoneId)).toArray(LocalTime[]::new);
        boolean secondElapsed = clockLocalTimes[0].getSecond() != discreteSecond;
        for (int i = 0; i < clockSettings.size(); i++) {
            Clock clock = clockSettings.get(i).clock;
            if (!clock.isDiscreteSecond() || secondElapsed)
                clock.setTime(clockLocalTimes[i]);
        }
        if (secondElapsed)
            discreteSecond = clockLocalTimes[0].getSecond();
    }

    static final class LedButton extends Region {

        private final Circle ledCentre = new Circle();
        private final Line hLine = new Line(), vLine = new Line();
        private final Paint pressedFill, releasedFill;

        LedButton(Color ledColor, Boolean plus) {
            pressedFill = ledColor.brighter();
            releasedFill = ledColor;
            ledCentre.setFill(releasedFill);
            ledCentre.setOnMousePressed(e ->  ledCentre.setFill(pressedFill));
            ledCentre.setOnMouseReleased(e -> ledCentre.setFill(releasedFill));

            Color lineColor = Color.IVORY;
            hLine.setStroke(lineColor);
            hLine.setStrokeLineCap(StrokeLineCap.ROUND);
            vLine.setStroke(lineColor);
            vLine.setStrokeLineCap(StrokeLineCap.ROUND);
            Node sign = plus == Boolean.TRUE ? new Group(hLine, vLine) : hLine;
            getChildren().setAll(ledCentre, sign);
            sign.setMouseTransparent(true);
            if (plus == null)
                sign.setVisible(false);
        }

        final void setOnAction(EventHandler<ActionEvent> actionHandler) {
            ledCentre.setOnMouseClicked(e -> actionHandler.handle(new ActionEvent(this, this)));
        }

        @Override public void layoutChildren() {
            double width = getWidth();
            double height = getHeight();
            double radius = Math.min(width, height) / 2;
            ledCentre.setRadius(radius);
            double lineLength = 0.4 * radius;
            hLine.setStartX(width / 2 - lineLength);
            hLine.setEndX(width / 2 + lineLength);
            hLine.setStrokeWidth(0.2 * radius);
            vLine.setStartY(width / 2 - lineLength);
            vLine.setEndY(width / 2 + lineLength);
            vLine.setStrokeWidth(0.2 * radius);
            for (Node child : getChildren())
                if (!(child instanceof Group))
                    layoutInArea(child, 0, 0, width, height, 0 , HPos.CENTER, VPos.CENTER);
            layoutInArea(hLine, 0, 0, width, height, 0 , HPos.CENTER, VPos.CENTER);
            layoutInArea(vLine, 0, 0, width, height, 0 , HPos.CENTER, VPos.CENTER);
        }
    }

}
