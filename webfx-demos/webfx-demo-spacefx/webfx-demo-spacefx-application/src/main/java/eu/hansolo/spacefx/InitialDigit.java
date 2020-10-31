/*
 * Copyright (c) 2020 by Gerrit Grunwald
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

package eu.hansolo.spacefx;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import static eu.hansolo.spacefx.Config.SPACEFX_COLOR;


@DefaultProperty("children")
public class InitialDigit extends Region implements Toggle {
    private static final double                      PREFERRED_WIDTH  = 32; //VISUAL_BOUNDS.getHeight() * 0.035;
    private static final double                      PREFERRED_HEIGHT = 45; // VISUAL_BOUNDS.getHeight() * 0.05;
    private static final String[]                    CHARACTERS       = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "-" };
    private static final long                        BLINK_INTERVAL   = 250_000_000l;
    private              int                         counter;
    private              double                      width;
    private              double                      height;
    private              Canvas                      canvas;
    private              GraphicsContext             ctx;
    private              double                      lastY;
    private              double                      startY;
    private              Color                       fontColor;
    private              ObjectProperty<ToggleGroup> toggleGroup;
    private              BooleanProperty             selected;
    private              boolean                     toggle;
    private              long                        lastTimerCalled;
    private              AnimationTimer              timer;
    private              EventHandler<MouseEvent>    mouseHandler;
    private              EventHandler<TouchEvent>    touchHandler;


    // ******************** Constructors **************************************
    public InitialDigit() {
        counter   = 0;
        lastY     = 0;
        fontColor = SPACEFX_COLOR;
        selected  = new BooleanPropertyBase(false) {
            @Override protected void invalidated() {
                //pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, get());
                //notifyAccessibleAttributeChanged(AccessibleAttribute.SELECTED);
                final boolean     selected    = get();
                final ToggleGroup toggleGroup = getToggleGroup();
                if (selected) {
                    timer.start();
                } else {
                    timer.stop();
                    toggle = false;
                    draw();
                }
                if (toggleGroup != null) {
                    if (selected) {
                        toggleGroup.selectToggle(InitialDigit.this);
                    } else if (toggleGroup.getSelectedToggle() == InitialDigit.this) {
                        toggleGroup.getSelectedToggle().setSelected(false);

                        // Avoid deselecting already selected toggle
                        if (!toggleGroup.getSelectedToggle().isSelected()) {
                            for (Toggle toggle : toggleGroup.getToggles()) {
                                if (toggle.isSelected()) {
                                    return;
                                }
                            }
                        }
                        toggleGroup.selectToggle(null);
                    }
                }
            }
            @Override public Object getBean() { return InitialDigit.this; }
            @Override public String getName() { return "selected"; }
        };

        toggle = false;
        lastTimerCalled = WebFxUtil.nanoTime();
        timer = new AnimationTimer() {
            @Override public void handle(final long now) {
                if (now > lastTimerCalled + BLINK_INTERVAL) {
                    toggle ^= true;
                    draw();
                    lastTimerCalled = now;
                    if(getScene() != null && getScene().getWindow() != null && !getScene().getWindow().isShowing()) {
                        timer.stop();
                    }
                }
            }
        };

        mouseHandler = evt -> {
            EventType<? extends MouseEvent> type = evt.getEventType();
            if (MouseEvent.MOUSE_PRESSED.equals(type)) {
                startY = evt.getY();
                if (!isSelected()) { setSelected(true); }
            } else if (MouseEvent.MOUSE_DRAGGED.equals(type)) {
                double dY = Math.abs(evt.getY() - lastY);
                if (dY > 10) {
                    if (evt.getY() < startY) {
                        up();
                    } else if (evt.getY() > startY){
                        down();
                    }
                    lastY = evt.getY();
                }
            }
        };

        touchHandler = evt -> {
            EventType<TouchEvent> type = evt.getEventType();
            if (TouchEvent.TOUCH_PRESSED.equals(type)) {
                startY = evt.getTouchPoint().getY();
                if (!isSelected()) { setSelected(true); }
            } else if (TouchEvent.TOUCH_MOVED.equals(type)) {
                double dY = Math.abs(evt.getTouchPoint().getY() - lastY);
                if (dY > 10) {
                    if (evt.getTouchPoint().getY() < startY) {
                        up();
                    } else if (evt.getTouchPoint().getY() > startY) {
                        down();
                    }
                    lastY = evt.getTouchPoint().getY();
                }
            }
        };

        initGraphics();
        registerListeners();
    }


    // ******************** Initialization ************************************
    private void initGraphics() {
        if (Double.compare(getPrefWidth(), 0.0) <= 0 || Double.compare(getPrefHeight(), 0.0) <= 0 || Double.compare(getWidth(), 0.0) <= 0 ||
            Double.compare(getHeight(), 0.0) <= 0) {
            if (getPrefWidth() > 0 && getPrefHeight() > 0) {
                setPrefSize(getPrefWidth(), getPrefHeight());
            } else {
                setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
            }
        }

        canvas = new Canvas(getPrefWidth(), getPrefHeight());
        ctx    = canvas.getGraphicsContext2D();
        canvas.sceneProperty().addListener(scene -> Platform.runLater(() -> {
            ctx.setTextAlign(TextAlignment.CENTER);
            ctx.setTextBaseline(VPos.CENTER);
            resize();
        }));
        getChildren().setAll(canvas);
    }

    private void registerListeners() {
        widthProperty().addListener(o -> resize());
        heightProperty().addListener(o -> resize());
        //if (isDesktop()) {
            setOnMousePressed(mouseHandler);
            setOnMouseDragged(mouseHandler);
        //} else {
            setOnTouchPressed(touchHandler);
            setOnTouchMoved(touchHandler);
        //}
    }


    // ******************** Methods *******************************************
    public String getCharacter() { return CHARACTERS[counter]; }

    public Color getFontColor() { return fontColor; }
    public void setFontColor(final Color fontColor) {
        this.fontColor = fontColor;
        draw();
    }

    public void up() {
        if (!isSelected()) { return; }
        counter--;
        if (counter < 0) { counter = CHARACTERS.length - 1; }
        draw();
    }

    public void down() {
        if (!isSelected()) { return; }
        counter++;
        if (counter > CHARACTERS.length - 1) { counter = 0; }
        draw();
    }

    @Override public ToggleGroup getToggleGroup() { return null == toggleGroup ? null : toggleGroupProperty().get(); }
    @Override public void setToggleGroup(final ToggleGroup toggleGroup) { toggleGroupProperty().set(toggleGroup); }
    @Override public ObjectProperty<ToggleGroup> toggleGroupProperty() {
        if (null == toggleGroup) {
            toggleGroup = new ObjectPropertyBase<>() {
                private ToggleGroup old;
                private ChangeListener<Toggle> listener = (o, oV, nV) -> {
                    //getImpl_traversalEngine().setOverriddenFocusTraversability(nV != null ? isSelected() : null);
                };

                @Override protected void invalidated() {
                    final ToggleGroup tg = get();
                    if (tg != null && !tg.getToggles().contains(InitialDigit.this)) {
                        if (old != null) {
                            old.getToggles().remove(InitialDigit.this);
                        }
                        tg.getToggles().add(InitialDigit.this);
                        tg.selectedToggleProperty().addListener(listener);
                    } else if (tg == null) {
                        old.selectedToggleProperty().removeListener(listener);
                        old.getToggles().remove(InitialDigit.this);
                    }
                    old = tg;
                }
                @Override public Object getBean() { return InitialDigit.this; }
                @Override public String getName() { return "toggleGroup"; }
            };
        }
        return toggleGroup;
    }

    @Override public boolean isSelected() { return selected.get(); }
    @Override public void setSelected(final boolean selected) { this.selected.set(selected); }
    @Override public BooleanProperty selectedProperty() { return selected; }

    private double clamp(final double min, final double max, final double value) {
        if (value < min) { return min; }
        if (value > max) { return max; }
        return value;
    }


    // ******************** Resize/Redraw *************************************
    private void resize() {
        width  = getWidth() - getInsets().getLeft() - getInsets().getRight();
        height = getHeight() - getInsets().getTop() - getInsets().getBottom();

        if (width > 0 && height > 0) {
            canvas.setWidth(width);
            canvas.setHeight(height);
            canvas.relocate((getWidth() - width) * 0.5, (getHeight() - height) * 0.5);

            draw();
        }
    }

    private void draw() {
        ctx.clearRect(0, 0, width, height);
        ctx.setFont(Fonts.spaceBoy(clamp(6, height / 1.3, height * 0.8)));
        ctx.save();
        ctx.setFill(toggle ? Color.TRANSPARENT : fontColor);
        ctx.fillText(CHARACTERS[counter], width * 0.5, height * 0.5, width * 0.9);
        ctx.restore();
    }
}
