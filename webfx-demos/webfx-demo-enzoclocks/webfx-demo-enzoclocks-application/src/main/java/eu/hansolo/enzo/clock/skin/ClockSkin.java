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

package eu.hansolo.enzo.clock.skin;

import eu.hansolo.enzo.clock.Clock;
import eu.hansolo.enzo.fonts.Fonts;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.*;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import webfx.kit.util.properties.Properties;
import webfx.kit.util.properties.Unregisterable;

import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;


/**
 * User: hansolo
 * Date: 31.10.12
 * Time: 14:18
 */
public class ClockSkin extends SkinBase<Clock> implements Skin<Clock> {
    private static final boolean STYLE_WITH_CSS = false; // Turned to false (hardcoded style) as WebFx doesn't support CSS yet

    private static final double PREFERRED_WIDTH = 200;
    private static final double PREFERRED_HEIGHT = 200;
    private static final double MINIMUM_WIDTH = 50;
    private static final double MINIMUM_HEIGHT = 50;
    private static final double MAXIMUM_WIDTH = 1024;
    private static final double MAXIMUM_HEIGHT = 1024;
    private Pane pane;
    private String nightDayStyleClass;
    private Region background;
    private Canvas logoLayer;
    private GraphicsContext ctx;
    private Pane minutePointer;
    private Pane minutePointerFlour;
    private Pane hourPointer;
    private Pane hourPointerFlour;
    private Pane secondPointer;
    private Pane centerKnob;
    private Pane foreground;
    private double size;
    private double hourPointerWidthFactor;
    private double hourPointerHeightFactor;
    private double minutePointerWidthFactor;
    private double minutePointerHeightFactor;
    private double secondPointerWidthFactor;
    private double secondPointerHeightFactor;
    private double majorTickWidthFactor;
    private double majorTickHeightFactor;
    private double minorTickWidthFactor;
    private double minorTickHeightFactor;
    private double majorTickOffset;
    private double minorTickOffset;
    private Rotate hourRotate;
    private Rotate minuteRotate;
    private Rotate secondRotate;
    private List<Region> ticks;
    private List<Text> tickLabels;
    private Group tickMarkGroup;
    private Group tickLabelGroup;
    private Group pointerGroup;
    private Group secondPointerGroup;
    private Font tickLabelFont;
    private DoubleProperty currentMinuteAngle;
    private DoubleProperty minuteAngle;
    private Timeline timeline;


    // ******************** Constructors **************************************
    public ClockSkin(final Clock CONTROL) {
        super(CONTROL);
        Clock clock = getSkinnable();
        if (clock.isAutoNightMode()) checkForNight(clock.getTime());
        nightDayStyleClass = clock.isNightMode() ? "night-mode" : "day-mode";

        hourPointerWidthFactor = 0.04;
        hourPointerHeightFactor = 0.55;
        minutePointerWidthFactor = 0.04;
        minutePointerHeightFactor = 0.4;
        secondPointerWidthFactor = 0.075;
        secondPointerHeightFactor = 0.46;

        majorTickWidthFactor = 0.04;
        majorTickHeightFactor = 0.12;
        minorTickWidthFactor = 0.01;
        minorTickHeightFactor = 0.05;

        majorTickOffset = 0.018;
        minorTickOffset = 0.05;

        tickLabelFont = Fonts.bebasNeue(12);
        minuteAngle = new SimpleDoubleProperty(0);
        currentMinuteAngle = new SimpleDoubleProperty(0);

        minuteRotate = new Rotate();
        hourRotate = new Rotate();
        secondRotate = new Rotate();

        ticks = new ArrayList<>(60);
        tickLabels = new ArrayList<>(12);

        timeline = new Timeline();

        init();
        initGraphics();
        registerListeners();
        updateTime();
    }


    // ******************** Initialization ************************************
    private void init() {
        Clock clock = getSkinnable();
        if (Double.compare(clock.getPrefWidth(), 0.0) <= 0 || Double.compare(clock.getPrefHeight(), 0.0) <= 0 ||
                Double.compare(clock.getWidth(), 0.0) <= 0 || Double.compare(clock.getHeight(), 0.0) <= 0) {
            if (clock.getPrefWidth() > 0 && clock.getPrefHeight() > 0) {
                clock.setPrefSize(clock.getPrefWidth(), clock.getPrefHeight());
            } else {
                clock.setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
            }
        }

        if (Double.compare(clock.getMinWidth(), 0.0) <= 0 || Double.compare(clock.getMinHeight(), 0.0) <= 0) {
            clock.setMinSize(MINIMUM_WIDTH, MINIMUM_HEIGHT);
        }

        if (Double.compare(clock.getMaxWidth(), 0.0) <= 0 || Double.compare(clock.getMaxHeight(), 0.0) <= 0) {
            clock.setMaxSize(MAXIMUM_WIDTH, MAXIMUM_HEIGHT);
        }
    }

    private void initGraphics() {
        Clock clock = getSkinnable();
        pane = new Pane();

        background = new Region();
        if (Clock.Design.IOS6 == clock.getDesign()) {
            styleBackground("background-ios6");
        } else if (Clock.Design.DB == clock.getDesign()) {
            styleBackground("background-db");
        } else if (Clock.Design.BRAUN == clock.getDesign()) {
            styleBackground("background-braun");
        } else if (Clock.Design.BOSCH == clock.getDesign()) {
            styleBackground("background-bosch");
        }

        logoLayer = new Canvas(PREFERRED_WIDTH, PREFERRED_HEIGHT);
        ctx = logoLayer.getGraphicsContext2D();

        String majorTickStyleClass;
        String minorTickStyleClass;
        if (Clock.Design.IOS6 == clock.getDesign()) {
            majorTickStyleClass = "major-tick-ios6";
            minorTickStyleClass = "minor-tick-ios6";
        } else if (Clock.Design.DB == clock.getDesign()) {
            majorTickStyleClass = "major-tick-db";
            minorTickStyleClass = "minor-tick-db";
        } else if (Clock.Design.BOSCH == clock.getDesign()) {
            majorTickStyleClass = "major-tick-bosch";
            minorTickStyleClass = "minor-tick-bosch";
        } else {
            majorTickStyleClass = "major-tick-braun";
            minorTickStyleClass = "minor-tick-braun";
        }

        int tickLabelCounter = 1;
        for (double angle = 0; angle < 360; angle += 6) {
            Region tick = new Region();
            if (angle % 30 == 0) {
                styleMajorTick(tick, majorTickStyleClass);
                Text tickLabel = new Text(Integer.toString(tickLabelCounter));
                styleTickLabel(tickLabel, "tick-label-braun");
                tickLabels.add(tickLabel);
                tickLabelCounter++;
            } else {
                styleMinorTick(tick, minorTickStyleClass);
            }
            ticks.add(tick);
        }

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.rgb(0, 0, 0, 0.65));
        dropShadow.setRadius(1.5);
        dropShadow.setBlurType(BlurType.TWO_PASS_BOX);
        dropShadow.setOffsetY(1);

        tickMarkGroup = new Group();
        tickMarkGroup.setEffect(dropShadow);
        tickMarkGroup.getChildren().setAll(ticks);

        tickLabelGroup = new Group();
        tickLabelGroup.setEffect(dropShadow);
        tickLabelGroup.getChildren().setAll(tickLabels);
        tickLabelGroup.setOpacity(Clock.Design.BRAUN == clock.getDesign() ? 1 : 0);

        minutePointer = new Pane();
        if (Clock.Design.IOS6 == clock.getDesign()) {
            styleHourPointer(minutePointer, "hour-pointer-ios6");
        } else if (Clock.Design.DB == clock.getDesign()) {
            styleHourPointer(minutePointer, "hour-pointer-db");
        } else if (Clock.Design.BRAUN == clock.getDesign()) {
            styleHourPointer(minutePointer, "hour-pointer-braun");
        } else if (Clock.Design.BOSCH == clock.getDesign()) {
            styleHourPointer(minutePointer, "hour-pointer-bosch");
        }
        minutePointer.getTransforms().setAll(minuteRotate);

        minutePointerFlour = new Pane();
        styleHourPointerFlour(minutePointerFlour, "hour-pointer-braun-flour");
        if (Clock.Design.BRAUN == clock.getDesign()) {
            minutePointerFlour.setOpacity(1);
        } else {
            minutePointerFlour.setOpacity(0);
        }
        minutePointerFlour.getTransforms().setAll(minuteRotate);

        hourPointer = new Pane();
        if (Clock.Design.IOS6 == clock.getDesign()) {
            styleMinutePointer(hourPointer, "minute-pointer-ios6");
        } else if (Clock.Design.DB == clock.getDesign()) {
            styleMinutePointer(hourPointer, "minute-pointer-db");
        } else if (Clock.Design.BRAUN == clock.getDesign()) {
            styleMinutePointer(hourPointer, "minute-pointer-braun");
        } else if (Clock.Design.BOSCH == clock.getDesign()) {
            styleMinutePointer(hourPointer, "minute-pointer-bosch");
        }
        hourPointer.getTransforms().setAll(hourRotate);

        hourPointerFlour = new Pane();
        styleMinutePointerFlour(hourPointerFlour, "minute-pointer-braun-flour");
        if (Clock.Design.BRAUN == clock.getDesign()) {
            hourPointerFlour.setOpacity(1);
        } else {
            hourPointerFlour.setOpacity(0);
        }
        hourPointerFlour.getTransforms().setAll(hourRotate);

        DropShadow pointerShadow = new DropShadow();
        pointerShadow.setColor(Color.rgb(0, 0, 0, 0.45));
        pointerShadow.setRadius(12);
        pointerShadow.setBlurType(BlurType.TWO_PASS_BOX);
        pointerShadow.setOffsetY(6);

        pointerGroup = new Group();
        pointerGroup.setEffect(pointerShadow);
        pointerGroup.getChildren().setAll(hourPointerFlour, hourPointer, minutePointerFlour, minutePointer);

        secondPointer = new Pane();
        secondPointer.setOpacity(1);
        if (Clock.Design.IOS6 == clock.getDesign()) {
            styleSecondPointer(secondPointer, "second-pointer-ios6");
        } else if (Clock.Design.DB == clock.getDesign()) {
            styleSecondPointer(secondPointer, "second-pointer-db");
        } else if (Clock.Design.BRAUN == clock.getDesign()) {
            styleSecondPointer(secondPointer, "second-pointer-braun");
        } else if (Clock.Design.BOSCH == clock.getDesign()) {
            secondPointer.setOpacity(0);
        }
        secondPointer.getTransforms().setAll(secondRotate);

        InnerShadow secondPointerInnerShadow = new InnerShadow();
        secondPointerInnerShadow.setColor(Color.rgb(0, 0, 0, 0.3));
        secondPointerInnerShadow.setRadius(1);
        secondPointerInnerShadow.setBlurType(BlurType.TWO_PASS_BOX);
        secondPointerInnerShadow.setOffsetY(-1);

        InnerShadow secondPointerInnerHighlight = new InnerShadow();
        secondPointerInnerHighlight.setColor(Color.rgb(255, 255, 255, 0.3));
        secondPointerInnerHighlight.setRadius(1);
        secondPointerInnerHighlight.setBlurType(BlurType.TWO_PASS_BOX);
        secondPointerInnerHighlight.setOffsetY(1);
        secondPointerInnerHighlight.setInput(secondPointerInnerShadow);

        DropShadow secondPointerShadow = new DropShadow();
        secondPointerShadow.setColor(Color.rgb(0, 0, 0, 0.45));
        secondPointerShadow.setRadius(12);
        secondPointerShadow.setBlurType(BlurType.TWO_PASS_BOX);
        secondPointerShadow.setOffsetY(6);
        secondPointerShadow.setInput(secondPointerInnerHighlight);

        secondPointerGroup = new Group();
        secondPointerGroup.setEffect(secondPointerShadow);
        secondPointerGroup.getChildren().setAll(secondPointer);
        secondPointerGroup.setOpacity(clock.isSecondPointerVisible() ? 1 : 0);

        centerKnob = new Pane();
        if (Clock.Design.IOS6 == clock.getDesign()) {
            styleCenterKnob(centerKnob, "center-knob-ios6");
        } else if (Clock.Design.DB == clock.getDesign()) {
            styleCenterKnob(centerKnob, "center-knob-db");
        } else if (Clock.Design.BRAUN == clock.getDesign()) {
            styleCenterKnob(centerKnob, "center-knob-braun");
        } else if (Clock.Design.BOSCH == clock.getDesign()) {
            styleCenterKnob(centerKnob, "center-knob-bosch");
        }

        foreground = new Pane();
        if (Clock.Design.IOS6 == clock.getDesign()) {
            styleForeground(foreground, "foreground-ios6");
        } else if (Clock.Design.DB == clock.getDesign()) {
            styleForeground(foreground, "foreground-db");
        } else if (Clock.Design.BRAUN == clock.getDesign()) {
            styleForeground(foreground, "foreground-braun");
        } else if (Clock.Design.BOSCH == clock.getDesign()) {
            styleForeground(foreground, "foreground-bosch");
        }
        foreground.setOpacity(clock.isHighlightVisible() ? 1 : 0);

        pane.getChildren().setAll(background, logoLayer, tickMarkGroup, tickLabelGroup, pointerGroup, secondPointerGroup, centerKnob, foreground);

        getChildren().setAll(pane);

        updateDesign();
    }

    private void registerListeners() {
        minuteRotate.angleProperty().bind(currentMinuteAngle);
        minuteAngle.addListener(observable -> moveMinutePointer(minuteAngle.get()));

        Clock clock = getSkinnable();
        clock.widthProperty().addListener(observable -> handleControlPropertyChanged("RESIZE"));
        clock.heightProperty().addListener(observable -> handleControlPropertyChanged("RESIZE"));
        clock.secondPointerVisibleProperty().addListener(observable -> handleControlPropertyChanged("SECOND_POINTER_VISIBLE"));
        clock.nightModeProperty().addListener(observable -> handleControlPropertyChanged("DESIGN"));
        clock.designProperty().addListener(observable -> handleControlPropertyChanged("DESIGN"));
        clock.highlightVisibleProperty().addListener(observable -> handleControlPropertyChanged("DESIGN"));
        clock.timeProperty().addListener(observable -> handleControlPropertyChanged("TIME"));
        clock.textProperty().addListener(observable -> handleControlPropertyChanged("TEXT"));
        clock.runningProperty().addListener(observable -> handleControlPropertyChanged("RUNNING"));
    }


    // ******************** Methods *******************************************
    protected void handleControlPropertyChanged(final String PROPERTY) {
        if ("RESIZE".equals(PROPERTY)) {
            resize();
        } else if ("DESIGN".equals(PROPERTY)) {
            updateDesign();
        } else if ("SECOND_POINTER_VISIBLE".equals(PROPERTY)) {
            secondPointerGroup.setOpacity(getSkinnable().isSecondPointerVisible() ? 1 : 0);
        } else if ("TIME".equals(PROPERTY)) {
            updateTime();
        } else if ("TEXT".equals(PROPERTY)) {
            updateDesign();
        }
    }

    private void updateTime() {
        Clock clock = getSkinnable();
        LocalTime TIME = clock.getTime();
        // Seconds
        if (clock.isDiscreteSecond()) {
            secondRotate.setAngle(TIME.getSecond() * 6);
        } else {
            secondRotate.setAngle(TIME.getSecond() * 6 + TIME.get(ChronoField.MILLI_OF_SECOND) * 0.006);
        }
        // Minutes
        minuteAngle.set(TIME.getMinute() * 6);
        // Hours                
        hourRotate.setAngle(0.5 * (60 * TIME.getHour() + TIME.getMinute()));

        if (clock.isAutoNightMode()) checkForNight(TIME);
    }

    private void checkForNight(final LocalTime TIME) {
        int hour = TIME.getHour();
        int minute = TIME.getMinute();

        Clock clock = getSkinnable();
        if (0 <= hour && minute >= 0 && hour <= 5 && minute <= 59 || 17 <= hour && minute <= 59 && hour <= 23 && minute <= 59) {
            clock.setNightMode(true);
        } else {
            clock.setNightMode(false);
        }
    }

    private void moveMinutePointer(final double ANGLE) {
        /*
        final KeyValue kv = new KeyValue(currentMinuteAngle, ANGLE, Interpolator.SPLINE(0.5, 0.4, 0.4, 1.0));
        final KeyFrame kf = new KeyFrame(Duration.millis(200), kv);
        timeline = new Timeline();
        timeline.getKeyFrames().add(kf);
        timeline.play();
        */
        KeyValue kv = new KeyValue(currentMinuteAngle, ANGLE, Interpolator.SPLINE(0.5, 0.4, 0.4, 1.0));
        if ((int) currentMinuteAngle.get() == 354 && ANGLE == 0) {
            kv = new KeyValue(currentMinuteAngle, 360, Interpolator.SPLINE(0.5, 0.4, 0.4, 1.0));
        } else if ((int) currentMinuteAngle.get() == 0 && ANGLE == 354) {
            kv = new KeyValue(currentMinuteAngle, -6, Interpolator.SPLINE(0.5, 0.4, 0.4, 1.0));
        }
        final KeyFrame kf = new KeyFrame(Duration.millis(200), kv);
        timeline = new Timeline();
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(event -> {
            if ((int) currentMinuteAngle.get() == 360) {
                currentMinuteAngle.set(0);
            } else if ((int) currentMinuteAngle.get() == -6) {
                currentMinuteAngle.set(354);
            }
        });
        timeline.play();
    }


    // ******************** Drawing related ***********************************    
    private void drawLogoLayer() {
        ctx.clearRect(0, 0, size, size);
        if (Clock.Design.BOSCH == getSkinnable().getDesign()) {
            ctx.setFill(getSkinnable().isNightMode() ? Color.rgb(240, 240, 240) : Color.rgb(10, 10, 10));
            ctx.fillRect(size * 0.5 - 1, size * 0.18, 2, size * 0.27);
            ctx.fillRect(size * 0.5 - 1, size * 0.55, 2, size * 0.27);
            ctx.fillRect(size * 0.18, size * 0.5 - 1, size * 0.27, 2);
            ctx.fillRect(size * 0.55, size * 0.5 - 1, size * 0.27, 2);
        }
        if (getSkinnable().getText().isEmpty()) return;
        ctx.setFill(getSkinnable().isNightMode() ? Color.WHITE : Color.BLACK);
        ctx.setFont(Fonts.robotoMedium(size * 0.05));
        ctx.setTextBaseline(VPos.CENTER);
        ctx.setTextAlign(TextAlignment.CENTER);
        ctx.fillText(getSkinnable().getText(), size * 0.5, size * 0.675, size * 0.8);
    }

    private void updateDesign() {
        Clock clock = getSkinnable();
        boolean wasRunning = clock.isRunning();
        if (wasRunning) {
            clock.setRunning(false);
        }

        // Set day or night mode
        nightDayStyleClass = clock.isNightMode() ? "night-mode" : "day-mode";
        // Set Styles for each component
        if (Clock.Design.IOS6 == clock.getDesign()) {
            styleBackground("background-ios6");
            int index = 0;
            for (double angle = 0; angle < 360; angle += 6) {
                Region tick = ticks.get(index);
                if (angle % 30 == 0) {
                    styleMajorTick(tick, "major-tick-ios6");
                } else {
                    styleMinorTick(tick, "minor-tick-ios6");
                }
                ticks.add(tick);
                index++;
            }
            styleHourPointer(minutePointer, "hour-pointer-ios6");
            styleMinutePointer(hourPointer, "minute-pointer-ios6");
            styleSecondPointer(secondPointer, "second-pointer-ios6");
            styleCenterKnob(centerKnob, "center-knob-ios6");
            styleForeground(foreground, "foreground-ios6");
        } else if (Clock.Design.BRAUN == clock.getDesign()) {
            nightDayStyleClass = clock.isNightMode() ? "night-mode-braun" : "day-mode-braun";
            styleBackground("background-braun");
            int index = 0;
            for (double angle = 0; angle < 360; angle += 6) {
                Region tick = ticks.get(index);
                if (angle % 30 == 0) {
                    styleMajorTick(tick, "major-tick-braun");
                } else {
                    styleMinorTick(tick, "minor-tick-braun");
                }
                index++;
            }
            for (index = 0; index < 12; index++) {
                styleTickLabel(tickLabels.get(index), "tick-label-braun");
            }
            styleHourPointer(minutePointer, "hour-pointer-braun");
            styleMinutePointer(hourPointer, "minute-pointer-braun");
            styleSecondPointer(secondPointer, "second-pointer-braun");
            styleCenterKnob(centerKnob, "center-knob-braun");
            styleForeground(foreground, "foreground-braun");
        } else if (Clock.Design.BOSCH == clock.getDesign()) {
            nightDayStyleClass = clock.isNightMode() ? "night-mode-bosch" : "day-mode-bosch";
            styleBackground("background-bosch");
            int index = 0;
            for (double angle = 0; angle < 360; angle += 6) {
                Region tick = ticks.get(index);
                if (angle % 30 == 0) {
                    styleMajorTick(tick, "major-tick-bosch");
                } else {
                    styleMinorTick(tick, "minor-tick-bosch");
                }
                ticks.add(tick);
                index++;
            }
            styleHourPointer(minutePointer, "hour-pointer-bosch");
            styleMinutePointer(hourPointer, "minute-pointer-bosch");
            styleSecondPointer(secondPointer, "second-pointer-bosch");
            styleCenterKnob(centerKnob, "center-knob-bosch");
            styleForeground(foreground, "foreground-bosch");
        } else {
            styleBackground("background-db");
            int index = 0;
            for (double angle = 0; angle < 360; angle += 6) {
                Region tick = ticks.get(index);
                if (angle % 30 == 0) {
                    styleMajorTick(tick, "major-tick-db");
                } else {
                    styleMinorTick(tick, "minor-tick-db");
                }
                ticks.add(tick);
                index++;
            }
            styleHourPointer(minutePointer, "hour-pointer-db");
            styleMinutePointer(hourPointer, "minute-pointer-db");
            styleSecondPointer(secondPointer, "second-pointer-db");
            styleCenterKnob(centerKnob, "center-knob-db");
            styleForeground(foreground, "foreground-db");
        }
        tickLabelGroup.setOpacity(Clock.Design.BRAUN == clock.getDesign() ? 1 : 0);
        foreground.setOpacity(clock.isHighlightVisible() ? 1 : 0);
        hourPointerFlour.setOpacity(Clock.Design.BRAUN == clock.getDesign() ? 1 : 0);
        minutePointerFlour.setOpacity(Clock.Design.BRAUN == clock.getDesign() ? 1 : 0);
        secondPointer.setOpacity(Clock.Design.BOSCH == clock.getDesign() ? 0 : 1);

        resize();

        if (wasRunning) {
            clock.setRunning(true);
        }
    }

    private void resize() {
        Clock clock = getSkinnable();
        size = Math.min(clock.getWidth(), clock.getHeight());

        logoLayer.setWidth(size);
        logoLayer.setHeight(size);

        if (size > 0) {
            pane.setMaxSize(size, size);

            background.setPrefSize(size, size);

            if (Clock.Design.IOS6 == clock.getDesign()) {
                hourPointerWidthFactor = 0.04;
                hourPointerHeightFactor = 0.55;
                minutePointerWidthFactor = 0.04;
                minutePointerHeightFactor = 0.4;
                secondPointerWidthFactor = 0.075;
                secondPointerHeightFactor = 0.46;
                majorTickWidthFactor = 0.04;
                majorTickHeightFactor = 0.12;
                minorTickWidthFactor = 0.01;
                minorTickHeightFactor = 0.05;
                majorTickOffset = 0.018;
                minorTickOffset = 0.05;
                minuteRotate.setPivotX(size * 0.5 * hourPointerWidthFactor);
                minuteRotate.setPivotY(size * 0.76 * hourPointerHeightFactor);
                hourRotate.setPivotX(size * 0.5 * minutePointerWidthFactor);
                hourRotate.setPivotY(size * 0.66 * minutePointerHeightFactor);
                secondRotate.setPivotX(size * 0.5 * secondPointerWidthFactor);
                secondRotate.setPivotY(size * 0.7341040462 * secondPointerHeightFactor);
            } else if (Clock.Design.BRAUN == clock.getDesign()) {
                hourPointerWidthFactor = 0.105;
                hourPointerHeightFactor = 0.485;
                minutePointerWidthFactor = 0.105;
                minutePointerHeightFactor = 0.4;
                secondPointerWidthFactor = 0.09;
                secondPointerHeightFactor = 0.53;
                majorTickWidthFactor = 0.015;
                majorTickHeightFactor = 0.045;
                minorTickWidthFactor = 0.0075;
                minorTickHeightFactor = 0.0225;
                majorTickOffset = 0.012;
                minorTickOffset = 0.02;
                minuteRotate.setPivotX(size * 0.5 * hourPointerWidthFactor);
                minuteRotate.setPivotY(size * 0.895 * hourPointerHeightFactor);
                hourRotate.setPivotX(size * 0.5 * minutePointerWidthFactor);
                hourRotate.setPivotY(size * 0.87 * minutePointerHeightFactor);
                secondRotate.setPivotX(size * 0.5 * secondPointerWidthFactor);
                secondRotate.setPivotY(size * 0.8125 * secondPointerHeightFactor);
            } else if (Clock.Design.BOSCH == clock.getDesign()) {
                hourPointerWidthFactor = 0.04;
                hourPointerHeightFactor = 0.54;
                minutePointerWidthFactor = 0.04;
                minutePointerHeightFactor = 0.38;
                secondPointerWidthFactor = 0.09;
                secondPointerHeightFactor = 0.53;
                majorTickWidthFactor = 0.02;
                majorTickHeightFactor = 0.145;
                minorTickWidthFactor = 0.006;
                minorTickHeightFactor = 0.07;
                majorTickOffset = 0.005;
                minorTickOffset = 0.04;
                minuteRotate.setPivotX(size * 0.5 * hourPointerWidthFactor);
                minuteRotate.setPivotY(size * 0.8240740741 * hourPointerHeightFactor);
                hourRotate.setPivotX(size * 0.5 * minutePointerWidthFactor);
                hourRotate.setPivotY(size * 0.75 * minutePointerHeightFactor);
                secondRotate.setPivotX(size * 0.5 * secondPointerWidthFactor);
                secondRotate.setPivotY(size * 0.8125 * secondPointerHeightFactor);
            } else {
                hourPointerWidthFactor = 0.04;
                hourPointerHeightFactor = 0.47;
                minutePointerWidthFactor = 0.055;
                minutePointerHeightFactor = 0.33;
                secondPointerWidthFactor = 0.1;
                secondPointerHeightFactor = 0.455;
                majorTickWidthFactor = 0.04;
                majorTickHeightFactor = 0.12;
                minorTickWidthFactor = 0.025;
                minorTickHeightFactor = 0.04;
                majorTickOffset = 0.018;
                minorTickOffset = 0.06;
                minuteRotate.setPivotX(size * 0.5 * hourPointerWidthFactor);
                minuteRotate.setPivotY(size * hourPointerHeightFactor);
                hourRotate.setPivotX(size * 0.5 * minutePointerWidthFactor);
                hourRotate.setPivotY(size * minutePointerHeightFactor);
                secondRotate.setPivotX(size * 0.5 * secondPointerWidthFactor);
                secondRotate.setPivotY(size * secondPointerHeightFactor);
            }

            drawLogoLayer();

            double radius = 0.4;
            double sinValue;
            double cosValue;
            int index = 0;
            for (double angle = 0; angle < 360; angle += 6) {
                sinValue = Math.sin(Math.toRadians(angle));
                cosValue = Math.cos(Math.toRadians(angle));
                Region tick = ticks.get(index);
                if (angle % 30 == 0) {
                    //tick.setPrefWidth(size * majorTickWidthFactor);
                    //tick.setPrefHeight(size * majorTickHeightFactor);
                    resizeRelocate(tick, size * 0.5 + ((size * (radius + majorTickOffset) * sinValue) - (size * (majorTickWidthFactor) * 0.5)),
                            size * 0.5 + ((size * (radius + majorTickOffset) * cosValue) - (size * (majorTickHeightFactor) * 0.5)),
                            size * majorTickWidthFactor,
                            size * majorTickHeightFactor
                    );
                } else {
                    //tick.setPrefWidth(size * minorTickWidthFactor);
                    //tick.setPrefHeight(size * minorTickHeightFactor);
                    resizeRelocate(tick, size * 0.5 + ((size * (radius + minorTickOffset) * sinValue) - (size * (minorTickWidthFactor) * 0.5)),
                            size * 0.5 + ((size * (radius + minorTickOffset) * cosValue) - (size * (minorTickHeightFactor) * 0.5)),
                            size * minorTickWidthFactor,
                            size * minorTickHeightFactor
                    );
                }
                tick.getTransforms().setAll(new Rotate(-angle, tick.getPrefWidth() / 2, tick.getPrefHeight() / 2));
                //tick.setRotate(-angle);
                index++;
            }

            if (Clock.Design.BRAUN == clock.getDesign()) {
                int tickLabelCounter = 0;
                //tickLabelFont = Font.loadFont(getClass().getResourceAsStream("/eu/hansolo/enzo/fonts/helvetica.ttf"), (0.075 * size));
                tickLabelFont = Font.font("Bebas Neue", FontWeight.THIN, FontPosture.REGULAR, 0.09 * size);
                for (double angle = 0; angle < 360; angle += 30.0) {
                    double x = 0.34 * size * Math.sin(Math.toRadians(150 - angle));
                    double y = 0.34 * size * Math.cos(Math.toRadians(150 - angle));
                    Text tickLabel = tickLabels.get(tickLabelCounter);
                    tickLabel.setFont(tickLabelFont);
                    tickLabel.setX(size * 0.5 + x - tickLabel.getLayoutBounds().getWidth() * 0.5);
                    tickLabel.setY(size * 0.5 + y);
                    tickLabel.setTextOrigin(VPos.CENTER);
                    tickLabel.setTextAlignment(TextAlignment.CENTER);
                    tickLabelCounter++;
                }
            }

            double minutePointerWidth = size * hourPointerWidthFactor;
            double minutePointerHeight = size * hourPointerHeightFactor;
            //minutePointer.setPrefSize(minutePointerWidth, minutePointerHeight);
            if (Clock.Design.IOS6 == clock.getDesign()) {
                resizeRelocate(minutePointer, size * 0.5 - (minutePointerWidth * 0.5), size * 0.5 - (minutePointerHeight) + (minutePointerHeight * 0.24), minutePointerWidth, minutePointerHeight);
            } else if (Clock.Design.BRAUN == clock.getDesign()) {
                resizeRelocate(minutePointer, size * 0.5 - (minutePointerWidth * 0.5), size * 0.5 - (minutePointerHeight) + (minutePointerHeight * 0.108), minutePointerWidth, minutePointerHeight);
                resizeRelocate(minutePointerFlour, size * 0.5 - (minutePointerWidth * 0.5), size * 0.5 - (minutePointerHeight) + (minutePointerHeight * 0.108), minutePointerWidth, minutePointerHeight);
            } else if (Clock.Design.BOSCH == clock.getDesign()) {
                resizeRelocate(minutePointer, size * 0.5 - (minutePointerWidth * 0.5), size * 0.5 - (minutePointerHeight) + (minutePointerHeight * 0.1759259259), minutePointerWidth, minutePointerHeight);
            } else {
                resizeRelocate(minutePointer, size * 0.5 - (minutePointerWidth * 0.5), size * 0.5 - minutePointerHeight, minutePointerWidth, minutePointerHeight);
            }

            double hourPointerWidth = size * minutePointerWidthFactor;
            double hourPointerHeight = size * minutePointerHeightFactor;
            //hourPointer.setPrefSize(hourPointerWidth, hourPointerHeight);
            if (Clock.Design.IOS6 == clock.getDesign()) {
                resizeRelocate(hourPointer, size * 0.5 - (hourPointerWidth * 0.5), size * 0.5 - hourPointerHeight + (hourPointerHeight * 0.34), hourPointerWidth, hourPointerHeight);
            } else if (Clock.Design.BRAUN == clock.getDesign()) {
                resizeRelocate(hourPointer, size * 0.5 - (hourPointerWidth * 0.5), size * 0.5 - hourPointerHeight + (hourPointerHeight * 0.128), hourPointerWidth, hourPointerHeight);
                resizeRelocate(hourPointerFlour, size * 0.5 - (hourPointerWidth * 0.5), size * 0.5 - hourPointerHeight + (hourPointerHeight * 0.128), hourPointerWidth, hourPointerHeight);
            } else if (Clock.Design.BOSCH == clock.getDesign()) {
                resizeRelocate(hourPointer, size * 0.5 - (hourPointerWidth * 0.5), size * 0.5 - hourPointerHeight + (hourPointerHeight * 0.25), hourPointerWidth, hourPointerHeight);
            } else {
                resizeRelocate(hourPointer, size * 0.5 - (hourPointerWidth * 0.5), size * 0.5 - hourPointerHeight, hourPointerWidth, hourPointerHeight);
            }

            double secondPointerWidth = size * secondPointerWidthFactor;
            double secondPointerHeight = size * secondPointerHeightFactor;
            //secondPointer.setPrefSize(secondPointerWidth, secondPointerHeight);
            if (Clock.Design.IOS6 == clock.getDesign()) {
                resizeRelocate(secondPointer, size * 0.5 - (secondPointerWidth * 0.5), size * 0.5 - (secondPointerHeight) + (secondPointerHeight * 0.2658959538), secondPointerWidth, secondPointerHeight);
            } else if (Clock.Design.BRAUN == clock.getDesign()) {
                resizeRelocate(secondPointer, size * 0.5 - (secondPointerWidth * 0.5), size * 0.5 - secondPointerHeight + (secondPointerHeight * 0.189), secondPointerWidth, secondPointerHeight);
            } else {
                resizeRelocate(secondPointer, size * 0.5 - (secondPointerWidth * 0.5), size * 0.5 - secondPointerHeight, secondPointerWidth, secondPointerHeight);
            }

            double knobSize;
            if (Clock.Design.IOS6 == clock.getDesign()) {
                knobSize = size * 0.015;
            } else if (Clock.Design.BRAUN == clock.getDesign()) {
                knobSize = size * 0.085;
            } else if (Clock.Design.BOSCH == clock.getDesign()) {
                knobSize = size * 0.035;
            } else {
                knobSize = size * 0.1;
            }
            resizeRelocate(centerKnob, size * 0.5 - (knobSize * 0.5), size * 0.5 - (knobSize * 0.5), knobSize, knobSize);

            double foregroundWidth = size * 0.955;
            double foregroundHeight = size * 0.495;
            resizeRelocate(foreground, size * 0.5 - (foregroundWidth * 0.5), size * 0.01, foregroundWidth, foregroundHeight);
        }
    }

    /******************************************
     * HARDCODED STYLING (required for WebFx) *
     *****************************************/

    private final static Color POINTER_TICK_KNOB_DAY_COLOR = Color.grayRgb(10);
    private final static Color POINTER_TICK_KNOB_DAY_BOSH_BRAUN_COLOR = Color.grayRgb(44);
    private final static Color POINTER_TICK_KNOB_NIGHT_COLOR = Color.grayRgb(227);
    private final static Color POINTER_TICK_KNOB_NIGHT_BOSH_BRAUN_COLOR = Color.grayRgb(240);
    private final static Color SECOND_POINTER_COLOR = Color.rgb(207, 43, 27);
    private final static Color POINTER_FLOUR_BRAUN_COLOR = Color.web("#D8E0BD");
    private final static Color SECOND_POINTER_BRAUN_COLOR = Color.web("#F0C843");
    Unregisterable radiiBinding;

    private boolean isDayMode() {
        return !nightDayStyleClass.contains("night");
    }

    private void styleBackground(String style) {
        if (STYLE_WITH_CSS)
            background.getStyleClass().setAll(nightDayStyleClass, style);
        else {
            CornerRadii radii = new CornerRadii(background.getWidth() / 2);
            switch (style) {
                case "background-ios6":
                case "background-db":
                    setBackground(background, LinearGradient.valueOf(isDayMode() ? "from 16% 16% to 83% 83% #E3E3E3 0%, white 100%" : "from 16% 16% to 83% 83% rgb(44,44,44) 0%, rgb(50,50,50) 100%"), radii);
                    break;
                case "background-braun":
                    setBackground(background, LinearGradient.valueOf(isDayMode() ? "from 16% 16% to 83% 83% #E3E3E3 0%, white 100%" : "from 16% 16% to 83% 83% rgb(8,8,8) 0%, rgb(10,10,10) 100%"), radii);
                    break;
                case "background-bosch":
                    setBackground(background, LinearGradient.valueOf(isDayMode() ? "from 16% 16% to 83% 83% rgb(201, 201, 197) 0%, rgb(226, 226, 221) 100%" : "from 16% 16% to 83% 83% rgb(8,8,8) 0%, rgb(10,10,10) 100%"), radii);
                    break;
            }
            if (radiiBinding != null)
                radiiBinding.unregister();
            radiiBinding = Properties.runOnPropertiesChange(width -> styleBackground(style), background.widthProperty());
            background.setBorder(new Border(new BorderStroke(Color.web("#303030") /*LinearGradient.valueOf("#202020, #505050")*/, BorderStrokeStyle.SOLID, radii, BorderStroke.THIN)));
        }
    }

    private void styleMajorTick(Region tick, String style) {
        if (STYLE_WITH_CSS)
            tick.getStyleClass().setAll(nightDayStyleClass, style);
        else
            switch (style) {
                case "major-tick-ios6":
                case "major-tick-db":
                    setBackground(tick, isDayMode() ? POINTER_TICK_KNOB_DAY_COLOR : POINTER_TICK_KNOB_NIGHT_COLOR);
                    break;
                case "major-tick-bosch":
                    setBackground(tick, isDayMode() ? POINTER_TICK_KNOB_DAY_BOSH_BRAUN_COLOR : POINTER_TICK_KNOB_NIGHT_BOSH_BRAUN_COLOR);
                    break;
                case "major-tick-braun":
                    setBackground(tick, isDayMode() ? POINTER_TICK_KNOB_DAY_BOSH_BRAUN_COLOR : POINTER_TICK_KNOB_NIGHT_BOSH_BRAUN_COLOR, new CornerRadii(3));
                    break;
            }
    }

    private void styleMinorTick(Region tick, String style) {
        if (STYLE_WITH_CSS)
            tick.getStyleClass().setAll(nightDayStyleClass, style);
        else
            switch (style) {
                case "minor-tick-ios6":
                case "minor-tick-db":
                    setBackground(tick, isDayMode() ? POINTER_TICK_KNOB_DAY_COLOR : POINTER_TICK_KNOB_NIGHT_COLOR);
                    break;
                case "minor-tick-bosch":
                    setBackground(tick, isDayMode() ? POINTER_TICK_KNOB_DAY_BOSH_BRAUN_COLOR : POINTER_TICK_KNOB_NIGHT_BOSH_BRAUN_COLOR);
                    break;
                case "minor-tick-braun":
                    setBackground(tick, isDayMode() ? POINTER_TICK_KNOB_DAY_BOSH_BRAUN_COLOR : POINTER_TICK_KNOB_NIGHT_BOSH_BRAUN_COLOR, new CornerRadii(3));
                    break;
            }
    }

    private void styleTickLabel(Text tickLabel, String style) {
        if (STYLE_WITH_CSS)
            tickLabel.getStyleClass().setAll(nightDayStyleClass, style);
        else
            switch (style) {
                case "tick-label-braun":
                    tickLabel.setFill(isDayMode() ? POINTER_TICK_KNOB_DAY_BOSH_BRAUN_COLOR : POINTER_TICK_KNOB_NIGHT_BOSH_BRAUN_COLOR);
                    break;
                default:
                    tickLabel.setFill(null);
            }
    }

    private void styleHourPointer(Pane pointer, String style) {
        if (STYLE_WITH_CSS)
            pointer.getStyleClass().setAll(nightDayStyleClass, style);
        else {
            switch (style) {
                case "hour-pointer-ios6":
                case "hour-pointer-db":
                    setBackground(pointer, isDayMode() ? POINTER_TICK_KNOB_DAY_BOSH_BRAUN_COLOR : POINTER_TICK_KNOB_NIGHT_BOSH_BRAUN_COLOR);
                    setShape(pointer, null, null);
                    break;
                case "hour-pointer-bosch":
                    setBackground(pointer, null, null);
                    setShape(pointer,
                            isDayMode() ? POINTER_TICK_KNOB_DAY_BOSH_BRAUN_COLOR : POINTER_TICK_KNOB_NIGHT_BOSH_BRAUN_COLOR,
                            "M 98.5 44.15 C 98.5 42.275 101.5 42.275 101.5 44.15 L 103.9 119 L 103.9 119 L 96.1 119 L 96.1 119 L 98.5 44.15 Z"
                    );
                    break;
                case "hour-pointer-braun":
                    setBackground(pointer, null, null);
                    setShape(pointer,
                            isDayMode() ? POINTER_TICK_KNOB_DAY_BOSH_BRAUN_COLOR : POINTER_TICK_KNOB_NIGHT_BOSH_BRAUN_COLOR,
                            "M 98.8626 16.5547 C 98.8626 15.9023 99.3718 15.3735 100 15.3735 C 100.6282 15.3735 101.1374 15.9023 101.1374 16.5547 L 101.1374 51.4836 L 98.8626 51.4836 L 98.8626 16.5547 ZM 97.4003 15.7226 L 97.375 89.8438 C 92.9806 91.0384 89.5 95.0617 89.5 100 C 89.5 105.8712 94.3466 110.5 100 110.5 C 105.6534 110.5 110.5 105.8087 110.5 99.9375 C 110.5 94.9992 106.9881 91.0384 102.5938 89.8438 L 102.5997 15.7226 C 102.5997 14.232 101.4358 13.0236 100 13.0236 C 98.5642 13.0236 97.4003 14.232 97.4003 15.7226 Z"
                    );
                    break;
            }
        }
    }

    private void styleHourPointerFlour(Pane pointer, String style) {
        if (STYLE_WITH_CSS)
            pointer.getStyleClass().setAll(style);
        else
            switch (style) {
                case "hour-pointer-braun-flour":
                    setShape(pointer,
                            POINTER_FLOUR_BRAUN_COLOR,
                            "M 97.4003 15.7226 L 97.375 89.8438 C 92.9806 91.0384 89.5 95.0617 89.5 100 C 89.5 105.8712 94.3466 110.5 100 110.5 C 105.6534 110.5 110.5 105.8087 110.5 99.9375 C 110.5 94.9992 106.9881 91.0384 102.5938 89.8438 L 102.5997 15.7226 C 102.5997 14.232 101.4358 13.0236 100 13.0236 C 98.5642 13.0236 97.4003 14.232 97.4003 15.7226 Z"
                    );
                    break;
                default:
                    setShape(pointer, null, null);
            }
    }

    private void styleMinutePointer(Pane pointer, String style) {
        if (STYLE_WITH_CSS)
            pointer.getStyleClass().setAll(nightDayStyleClass, style);
        else {
            switch (style) {
                case "minute-pointer-ios6":
                case "minute-pointer-db":
                    setBackground(pointer, isDayMode() ? POINTER_TICK_KNOB_DAY_BOSH_BRAUN_COLOR : POINTER_TICK_KNOB_NIGHT_BOSH_BRAUN_COLOR);
                    setShape(pointer, null, null);
                    break;
                case "minute-pointer-bosch":
                    setBackground(pointer, null, null);
                    setShape(pointer,
                            isDayMode() ? POINTER_TICK_KNOB_DAY_BOSH_BRAUN_COLOR : POINTER_TICK_KNOB_NIGHT_BOSH_BRAUN_COLOR,
                            "M 98.5 13.2884 C 98.5 10.6372 101.5 10.6372 101.5 13.2884 L 103.9 119 L 103.9 119 L 96.1 119 L 96.1 119 L 98.5 13.2884 Z"
                    );
                    break;
                case "minute-pointer-braun":
                    setBackground(pointer, null, null);
                    setShape(pointer,
                            isDayMode() ? POINTER_TICK_KNOB_DAY_BOSH_BRAUN_COLOR : POINTER_TICK_KNOB_NIGHT_BOSH_BRAUN_COLOR,
                            "M 97.7253 34.1114 C 97.7253 32.8263 98.7437 31.7847 100 31.7847 C 101.2563 31.7847 102.2747 32.8263 102.2747 34.1114 L 102.2747 50.0657 L 97.7253 50.0657 L 97.7253 34.1114 ZM 89.5 99.9688 C 89.5 105.7513 94.3466 110.4688 100 110.4688 C 105.6534 110.4688 110.5 105.7825 110.5 100 C 110.5 95.631 107.6861 91.8506 103.9688 90.2813 L 103.9688 34.2188 C 103.9688 31.9653 102.2619 30 100.0625 30 C 97.8631 30 96 31.9653 96 34.2188 L 95.9688 90.3125 C 92.3361 91.9205 89.5 95.6624 89.5 99.9688 Z"
                    );
                    break;
            }
        }
    }

    private void styleMinutePointerFlour(Pane pointer, String style) {
        if (STYLE_WITH_CSS)
            pointer.getStyleClass().setAll(style);
        else
            switch (style) {
                case "minute-pointer-braun-flour":
                    setShape(pointer,
                            POINTER_FLOUR_BRAUN_COLOR,
                            "M 89.5 99.9688 C 89.5 105.7513 94.3466 110.4688 100 110.4688 C 105.6534 110.4688 110.5 105.7825 110.5 100 C 110.5 95.631 107.6861 91.8506 103.9688 90.2813 L 103.9688 34.2188 C 103.9688 31.9653 102.2619 30 100.0625 30 C 97.8631 30 96 31.9653 96 34.2188 L 95.9688 90.3125 C 92.3361 91.9205 89.5 95.6624 89.5 99.9688 Z"
                    );
                    break;
                default:
                    setShape(pointer, null, null);
            }
    }

    private void styleSecondPointer(Pane pointer, String style) {
        if (STYLE_WITH_CSS)
            pointer.getStyleClass().setAll(nightDayStyleClass, style);
        else {
            switch (style) {
                case "second-pointer-ios6":
                    setShape(pointer,
                            SECOND_POINTER_COLOR,
                            "M 8 72.3988 C 8.5426 72.3988 8.9825 72.8517 8.9825 73.4104 C 8.9825 73.9691 8.5426 74.422 8 74.422 C 7.4574 74.422 7.0175 73.9691 7.0175 73.4104 C 7.0175 72.8517 7.4574 72.3988 8 72.3988 ZM 8 0 C 3.5817 0 0 3.6878 0 8.237 C 0 12.3437 2.9187 15.7486 6.7368 16.3721 L 6.7368 70.1944 C 5.5024 70.7088 4.6316 71.9549 4.6316 73.4104 C 4.6316 74.8659 5.5024 76.112 6.7368 76.6264 L 6.7368 100 L 9.2632 100 L 9.2632 76.6264 C 10.4976 76.112 11.3684 74.8659 11.3684 73.4104 C 11.3684 71.9549 10.4976 70.7088 9.2632 70.1944 L 9.2632 16.3721 C 13.0813 15.7486 16 12.3437 16 8.237 C 16 3.6878 12.4183 0 8 0 Z"
                    );
                    break;
                case "second-pointer-db":
                    setShape(pointer,
                            SECOND_POINTER_COLOR,
                            "M 58 28.5 C 58 31.5376 60.4624 34 63.5 34 C 66.5376 34 69 31.5376 69 28.5 C 69 25.4624 66.5376 23 63.5 23 C 60.4624 23 58 25.4624 58 28.5 ZM 57 28.5 C 57 25.1732 59.5017 22.4292 62.725 22.045 L 63 6 L 64 6 L 64.275 22.045 C 67.5008 22.427 70 25.1715 70 28.5 C 70 31.7503 67.6157 34.4445 64.5 34.925 L 65 64 L 62 64 L 62.5 34.925 C 59.3843 34.4445 57 31.7503 57 28.5 Z"
                    );
                    break;
                case "second-pointer-braun":
                    setShape(pointer,
                            SECOND_POINTER_BRAUN_COLOR,
                            "M 108.5625 98.375 C 108.5625 93.9001 105.5881 90.6532 101.4375 89.9375 L 100.9098 16.6569 C 100.9098 16.6569 100.5587 12.3988 100 12.3988 C 99.4206 12.3988 99.1487 16.6782 99.1487 16.6782 L 98.5 89.9375 C 94.3627 90.6646 91.3125 93.9724 91.3125 98.4375 C 91.3125 101.8148 93.0277 104.3504 95.6958 105.8991 L 95.6958 115.0436 C 95.6958 116.7373 97.0299 118.1102 98.6756 118.1102 L 101.1588 118.1102 C 102.8045 118.1102 104.1386 116.7373 104.1386 115.0436 L 104.1386 105.9928 C 106.8975 104.4712 108.5625 101.8217 108.5625 98.375 Z"
                    );
                    break;
            }
        }
    }

    private void styleCenterKnob(Pane centerKnob, String style) {
        if (STYLE_WITH_CSS)
            centerKnob.getStyleClass().setAll(nightDayStyleClass, style);
        else
            switch (style) {
                case "center-knob-ios6":
                    setShape(centerKnob,
                            RadialGradient.valueOf("center 50% 50%, radius 50%, black, black 5%, white"),
                            "M 0 100 C 0 44.7708 44.7708 0 100 0 C 155.2292 0 200 44.7708 200 100 C 200 155.2292 155.2292 200 100 200 C 44.7708 200 0 155.2292 0 100 Z"
                    );
                    break;
                case "center-knob-bosch":
                    setShape(centerKnob,
                            RadialGradient.valueOf(isDayMode() ? "center 50% 50%, radius 50%, #404040, #C0C0C0" : "center 50% 50%, radius 50%, #C0C0C0, black"),
                            "M 0 100 C 0 44.7708 44.7708 0 100 0 C 155.2292 0 200 44.7708 200 100 C 200 155.2292 155.2292 200 100 200 C 44.7708 200 0 155.2292 0 100 Z"
                    );
                    break;
                case "center-knob-db":
                    setShape(centerKnob,
                            isDayMode() ? POINTER_TICK_KNOB_DAY_BOSH_BRAUN_COLOR : POINTER_TICK_KNOB_NIGHT_BOSH_BRAUN_COLOR,
                            "M 0 100 C 0 44.7708 44.7708 0 100 0 C 155.2292 0 200 44.7708 200 100 C 200 155.2292 155.2292 200 100 200 C 44.7708 200 0 155.2292 0 100 Z"
                    );
                    break;
                case "center-knob-braun":
                    /*setShape(centerKnob,
                            SECOND_POINTER_BRAUN_COLOR,
                            "M 58 62.5 C 58 59.4624 60.4624 57 63.5 57 C 66.5376 57 69 59.4624 69 62.5 C 69 65.5376 66.5376 68 63.5 68 C 60.4624 68 58 65.5376 58 62.5 Z"
                    );*/
                    setShape(centerKnob, null, null);
                    break;
            }
    }

    private void styleForeground(Pane foreground, String style) {
        if (STYLE_WITH_CSS)
            foreground.getStyleClass().setAll(nightDayStyleClass, style);
        else
            switch (style) {
                case "foreground-ios6":
                case "foreground-db":
                case "foreground-bosch":
                    setShape(foreground,
                            Color.rgb(255, 255, 255, 0.2),
                            "M 4.9839 72.482 C 18.0152 31.1014 53.9389 1.0572 100.1166 1.0572 C 146.5602 1.0572 182.6676 31.4482 195.4718 73.1978 C 196.9087 77.883 150.0777 100.1673 100 100 C 50.2169 99.8337 3.4843 77.2438 4.9839 72.482 Z"
                    );
                    break;
                default:
                    setShape(foreground,null, null);
            }
    }

    private static void setBackground(Region region, Paint fill) {
        setBackground(region, fill, null);
    }

    private static void setBackground(Region region, Paint fill, CornerRadii radii) {
        region.setBackground(fill == null ? null : new Background(new BackgroundFill(fill, radii, null)));
    }

    private void setShape(Pane pane, Paint fill, String svgPath) {
        ObservableList<Node> children = pane.getChildren();
        if (svgPath == null)
            children.clear();
        else {
            SVGPath p = new SVGPath();
            p.setFill(fill);
            p.setContent(svgPath);
            children.setAll(p);
        }
    }

    private static void resizeRelocate(Region region, double x, double y, double width, double height) {
        region.setPrefSize(width, height);
        region.resizeRelocate(x, y, width, height);
        if (region instanceof Pane) {
            //Bounds pb = region.getLayoutBounds();
            //log("x = " + x + ", y = " + y + ", width = " + width + ", height = " + height + ", pb = " + pb);
            ObservableList<Node> children = ((Pane) region).getChildren();
            if (children.size() == 1) {
                Node shape = children.get(0);
                Bounds sb = shape.getLayoutBounds();
                shape.relocate(width / 2 - sb.getWidth() / 2, height / 2 - sb.getHeight() / 2);
                double scale = height / sb.getHeight();
                //log("scale = " + scale + ", sb = " + sb);
                shape.setScaleX(scale);
                shape.setScaleY(scale);
            }
        }
    }

    /*private static void log(String message) {
        webfx.platform.shared.services.log.Logger.log(message);
    }*/
}
