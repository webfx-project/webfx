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
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import webfx.demo.enzoclocks.circlespacker.CirclesPackerPane;
import webfx.demo.enzoclocks.settings.BackgroundMenuPane;
import webfx.demo.enzoclocks.settings.ClockSetting;
import webfx.demo.enzoclocks.settings.SvgButtonPaths;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.shared.services.resource.ResourceService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public final class EnzoClocksApplication extends Application {

    private final List<ClockSetting> clockSettings = new ArrayList<>();
    private final CirclesPackerPane circlesPackerPane = new CirclesPackerPane();

    @Override
    public void start(Stage stage) {
        addClocks(
                new ClockSetting("America/Los_Angeles", "San Francisco", Clock.Design.BOSCH),
                new ClockSetting("America/New_York", null, Clock.Design.IOS6),
                new ClockSetting("Europe/Berlin", null, Clock.Design.DB),
                new ClockSetting("Australia/Sydney", null, Clock.Design.BRAUN)
        );
        Insets buttonsMargin = new Insets(10);
        PlusButton plusButton = new PlusButton(Color.GREEN.brighter());
        plusButton.setOnAction(e -> addClock(ClockSetting.createRandom(clockSettings)));
        plusButton.setMaxSize(100, 100);
        StackPane.setAlignment(plusButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(plusButton, buttonsMargin);
        Pane gearPane = createSVGButton(SvgButtonPaths.getGearPath(), Color.GRAY);
        gearPane.setCursor(Cursor.HAND);
        StackPane.setAlignment(gearPane, Pos.BOTTOM_LEFT);
        StackPane.setMargin(gearPane, buttonsMargin);
        Pane root = new StackPane(circlesPackerPane, plusButton, gearPane);
        BackgroundMenuPane backgroundMenuPane = new BackgroundMenuPane(root);
        gearPane.setOnMouseClicked(e -> root.getChildren().add(backgroundMenuPane));

        updateClockTimes();
        UiScheduler.schedulePeriodic(20, this::updateClockTimes);

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(ResourceService.toUrl("/eu/hansolo/enzo/clock/clock.css", getClass()));
        stage.setTitle("Enzo Clocks");
        stage.setScene(scene);
        stage.show();
    }

    private Pane createSVGButton(String svgPath, Paint fill) {
        SVGPath path = new SVGPath();
        path.setContent(svgPath);
        path.setFill(fill);
        // We now embed the svg path in a pane. The reason is for a better click experience. Because in JavaFx (not in
        // the browser), the clicking area is only the filled shape, not the empty space in that shape. So when clicking
        // on a gear icon on a mobile for example, even if globally our finger covers the icon, the final click point
        // may be in this empty space, making the button not reacting, leading to a frustrating experience.
        Pane pane = new Pane(path); // Will act as the mouse click area covering the entire surface
        // The pane needs to be reduced to the svg path size (which we can get using the layout bounds).
        path.sceneProperty().addListener((observableValue, scene, t1) -> { // This postpone is necessary only when running in the browser, not in standard JavaFx
            Bounds b = path.getLayoutBounds(); // Bounds computation should be correct now even in the browser
            pane.setMaxSize(b.getWidth(), b.getHeight());
        });
        pane.setCursor(Cursor.HAND);
        return pane;
    }

    private int discreteSecond;

    private void updateClockTimes() {
        if (!clockSettings.isEmpty()) {
            LocalTime[] clockLocalTimes = clockSettings.stream().map(s -> LocalTime.now(s.getZoneId())).toArray(LocalTime[]::new);
            boolean secondElapsed = clockLocalTimes[0].getSecond() != discreteSecond;
            for (int i = 0; i < clockSettings.size(); i++) {
                Clock clock = clockSettings.get(i).getClock();
                if (!clock.isDiscreteSecond() || secondElapsed)
                    clock.setTime(clockLocalTimes[i]);
            }
            if (secondElapsed)
                discreteSecond = clockLocalTimes[0].getSecond();
        }
    }

    private void addClocks(ClockSetting... clockSettings) {
        Arrays.stream(clockSettings).forEach(this::addClock);
    }

    private void addClock(ClockSetting clockSetting) {
        clockSettings.add(clockSetting);
        circlesPackerPane.getChildren().add(clockSetting.embedClock());
        clockSetting.setOnRemoveRequested(() -> {
            clockSettings.remove(clockSetting);
            circlesPackerPane.getChildren().remove(clockSetting.getContainer());
        });
    }
}
