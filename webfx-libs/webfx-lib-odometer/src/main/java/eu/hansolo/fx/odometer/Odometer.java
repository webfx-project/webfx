/*
 * Copyright (c) 2018 by Gerrit Grunwald
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

package eu.hansolo.fx.odometer;

import eu.hansolo.fx.odometer.event.OdometerEvent;
import eu.hansolo.fx.odometer.event.OdometerObserver;
import eu.hansolo.fx.odometer.event.Type;
import javafx.application.Platform;
import javafx.beans.DefaultProperty;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;


/**
 * User: hansolo
 * Date: 26.11.18
 * Time: 06:43
 */
@DefaultProperty("children")
public class Odometer extends Region {
    public  static final Color                  DEFAULT_DIGIT_BACKGROUND_COLOR   = Color.web("#050505");
    public  static final Color                  DEFAULT_DIGIT_FOREGROUND_COLOR   = Color.web("#f8f8f8");
    public  static final Color                  DEFAULT_DECIMAL_BACKGROUND_COLOR = Color.web("#f0f0f0");
    public  static final Color                  DEFAULT_DECIMAL_FOREGROUND_COLOR = Color.web("#f01010");
    private static final double                 PREFERRED_WIDTH                  = 300;
    private static final double                 PREFERRED_HEIGHT                 = 64;
    private static final double                 MINIMUM_WIDTH                    = 10;
    private static final double                 MINIMUM_HEIGHT                   = 10;
    private static final double                 MAXIMUM_WIDTH                    = 2048;
    private static final double                 MAXIMUM_HEIGHT                   = 2048;
    private        final OdometerEvent          DIGIT_CHANGED_EVENT              = new OdometerEvent(Odometer.this, Type.DIGIT_CHANGED);
    private static       double                 aspectRatio;
    private              boolean                keepAspect;
    private              double                 width;
    private              double                 height;
    private              double                 extendedHeight;
    private              Canvas                 backgroundCanvas;
    private              GraphicsContext        backgroundCtx;
    private              Rectangle              foreground;
    private              Canvas                 digitCanvas;
    private              GraphicsContext        digitCtx;
    private              WritableImage          digitImage;
    private              Canvas                 decimalCanvas;
    private              GraphicsContext        decimalCtx;
    private              WritableImage          decimalImage;
    private              Pane                   pane;
    private              SnapshotParameters     snapshotParameters;
    private              double                 digitWidth;
    private              double                 digitHeight;
    private              double                 columnHeight;
    private              double                 verticalSpace;
    private              double                 zeroOffset;
    private              Color                  _decimalBackgroundColor;
    private              ObjectProperty<Color>  decimalBackgroundColor;
    private              Color                  _decimalForegroundColor;
    private              ObjectProperty<Color>  decimalForegroundColor;
    private              Color                  _digitBackgroundColor;
    private              ObjectProperty<Color>  digitBackgroundColor;
    private              Color                  _digitForegroundColor;
    private              ObjectProperty<Color>  digitForegroundColor;
    private              LinearGradient         foregroundGradient;
    private              int                    _digits;
    private              IntegerProperty        digits;
    private              int                    _decimals;
    private              IntegerProperty        decimals;
    private              Font                   font;
    private              double                 oldValue;
    private              DoubleProperty         value;
    private              boolean                initialized;
    private              BooleanBinding         showing;
    private              List<OdometerObserver> observers;


    // ******************** Constructors **************************************
    public Odometer() {
        this(6, 1);
    }
    public Odometer(final int DIGITS, final int DECIMALS) {
        //getStylesheets().add(Odometer.class.getResource("odometer.css").toExternalForm());
        aspectRatio             = PREFERRED_HEIGHT / PREFERRED_WIDTH;
        keepAspect              = false;
        _digitBackgroundColor   = DEFAULT_DIGIT_BACKGROUND_COLOR;
        _digitForegroundColor   = DEFAULT_DIGIT_FOREGROUND_COLOR;
        _decimalBackgroundColor = DEFAULT_DECIMAL_BACKGROUND_COLOR;
        _decimalForegroundColor = DEFAULT_DECIMAL_FOREGROUND_COLOR;
        snapshotParameters      = new SnapshotParameters();
        value                   = new DoublePropertyBase(0) {
            @Override protected void invalidated() {
                if (get() < 0) { set(0); }
                redraw();
            }
            @Override public Object getBean() { return Odometer.this; }
            @Override public String getName() { return "value"; }
        };
        _digits                 = DIGITS < 0 ? 0 : DIGITS;
        _decimals               = DECIMALS < 1 ? 1 : DECIMALS;
        initialized             = false;
        observers               = new /*CopyOnWrite*/ArrayList<>();
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

        getStyleClass().add("odometer");

        foregroundGradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                                                new Stop(0, Color.rgb(0, 0, 0, 1)),
                                                new Stop(0.1, Color.rgb(0, 0, 0, 0.4)),
                                                new Stop(0.33, Color.rgb(255, 255, 255, 0.45)),
                                                new Stop(0.46, Color.rgb(255, 255, 255, 0)),
                                                new Stop(0.9, Color.rgb(0, 0, 0, 0.4)),
                                                new Stop(1, Color.rgb(0, 0, 0, 1)));

        backgroundCanvas = new Canvas(width, height);
        backgroundCtx    = backgroundCanvas.getGraphicsContext2D();

        digitCanvas = new Canvas(width, height);
        digitCtx    = digitCanvas.getGraphicsContext2D();

        decimalCanvas = new Canvas(width, height);
        decimalCtx    = decimalCanvas.getGraphicsContext2D();

        foreground = new Rectangle();

        pane = new Pane(digitCanvas, decimalCanvas, backgroundCanvas, foreground);
        digitCanvas.relocate(-1000, 0);
        decimalCanvas.relocate(-1000, 0);

        getChildren().setAll(pane);
    }

    private void registerListeners() {
        widthProperty().addListener(o -> resize());
        heightProperty().addListener(o -> resize());
        value.addListener((o, ov, nv) -> oldValue = ov.doubleValue());
        sceneProperty().addListener(o -> {
            if (null == getScene()) { return; }
            Platform.runLater(this::initAndResize);
            getScene().windowProperty().addListener(o1 -> {
                if (null == getScene().getWindow()) { return; }
                showing = Bindings.createBooleanBinding(() -> {
                    if (getScene() != null && getScene().getWindow() != null) {
                        return getScene().getWindow().isShowing();
                    } else {
                        return false;
                    }
                }, sceneProperty(), getScene().windowProperty(), getScene().getWindow().showingProperty());

                showing.addListener(o2 -> {
                    if (showing.get()) initAndResize();
                });
            });
        });
    }

    private void initAndResize() {
        if (isReadyToInit()) {
            init();
            resize();
        }
    }

    private boolean isReadyToInit() {
        return digitCanvas.getScene() != null;
    }

    private void init() {
        //if (!isShowing()) { return; }
        digitCtx.setFill(getDigitBackgroundColor());
        digitCtx.fillRect(0, 0, digitWidth, extendedHeight);

        digitCtx.setLineWidth(1);
        digitCtx.setStroke(Color.web("#f0f0f0"));
        digitCtx.strokeLine(0, 0, 0, extendedHeight);
        digitCtx.setStroke(Color.web("#202020"));
        digitCtx.strokeLine(digitWidth, 0, digitWidth, extendedHeight);

        digitCtx.setTextAlign(TextAlignment.CENTER);
        digitCtx.setTextBaseline(VPos.CENTER);
        digitCtx.setFont(font);
        digitCtx.setFill(getDigitForegroundColor());
        for (int i = 9 ; i < 21 ; i++) {
            digitCtx.fillText(Integer.toString(i % 10), digitWidth * 0.5, verticalSpace * (i - 9) + verticalSpace / 2);
        }
        digitImage = digitCanvas.snapshot(snapshotParameters, null);

        if (_decimals > 0) {
            decimalCtx.setFill(getDecimalBackgroundColor());
            decimalCtx.fillRect(0, 0, digitWidth, extendedHeight);

            decimalCtx.setLineWidth(1);
            decimalCtx.setStroke(Color.web("#f0f0f0"));
            decimalCtx.strokeLine(0, 0, 0, extendedHeight);
            decimalCtx.setStroke(Color.web("#202020"));
            decimalCtx.strokeLine(digitWidth, 0, digitWidth, extendedHeight);

            decimalCtx.setTextAlign(TextAlignment.CENTER);
            decimalCtx.setTextBaseline(VPos.CENTER);
            decimalCtx.setFont(font);
            decimalCtx.setFill(getDecimalForegroundColor());

            for (int i = 9 ; i < 21 ; i++) {
                decimalCtx.fillText(Integer.toString(i % 10), digitWidth * 0.5, verticalSpace * (i - 9) + verticalSpace / 2);
            }
            decimalImage = decimalCanvas.snapshot(snapshotParameters, null);
        }
        initialized = true;
    }


    // ******************** Methods *******************************************
    @Override public void layoutChildren() {
        super.layoutChildren();
    }

    @Override protected double computeMinWidth(final double HEIGHT) { return MINIMUM_WIDTH; }
    @Override protected double computeMinHeight(final double WIDTH) { return MINIMUM_HEIGHT; }

    @Override protected double computeMaxWidth(final double HEIGHT) { return MAXIMUM_WIDTH; }
    @Override protected double computeMaxHeight(final double WIDTH) { return MAXIMUM_HEIGHT; }

    @Override public ObservableList<Node> getChildren() { return super.getChildren(); }

    public boolean isShowing() { return null == showing ? false : showing.get(); }

    public double getValue() { return value.get(); }
    public void setValue(final double VALUE) { value.set(VALUE); }
    public DoubleProperty valueProperty() { return value; }

    public int getDigits() { return null == digits ? _digits : digits.get(); }
    public void setDigits(final int DIGITS) {
        if (null == digits) {
            _digits = DIGITS < 0 ? 0 : DIGITS;
            initialized = false;
            initAndResize();
        } else {
            digits.set(DIGITS);
        }
    }
    public IntegerProperty digitsProperty() {
        if (null == digits) {
            digits = new IntegerPropertyBase(_digits) {
                @Override protected void invalidated() {
                    if (get() < 0) { set(0); }
                    initialized = false;
                    initAndResize();
                }
                @Override public Object getBean() { return Odometer.this; }
                @Override public String getName() { return "digits"; }
            };
        }
        return digits;
    }

    public int getDecimals() { return null == decimals ? _decimals : decimals.get(); }
    public void setDecimals(final int DECIMALS) {
        if (null == decimals) {
            _decimals = DECIMALS < 1 ? 0 : DECIMALS;
            initialized = false;
            initAndResize();
        } else {
            decimals.set(DECIMALS);
        }
    }
    public IntegerProperty decimalsProperty() {
        if (null == decimals) {
            decimals = new IntegerPropertyBase(_decimals) {
                @Override protected void invalidated() {
                    if (get() < 1) { set(1); }
                    initialized = false;
                    initAndResize();
                }
                @Override public Object getBean() { return Odometer.this; }
                @Override public String getName() { return "decimals"; }
            };
        }
        return decimals;
    }
    
    public Color getDigitBackgroundColor() { return null == digitBackgroundColor ? _digitBackgroundColor : digitBackgroundColor.get(); }
    public void setDigitBackgroundColor(final Color COLOR) {
        if (null == digitBackgroundColor) {
            _digitBackgroundColor = COLOR;
            initialized = false;
            initAndResize();
        } else {
            digitBackgroundColor.set(COLOR);
        }
    }
    public ObjectProperty<Color> digitBackgroundColorProperty() {
        if (null == digitBackgroundColor) {
            digitBackgroundColor = new ObjectPropertyBase<Color>(_digitBackgroundColor) {
                @Override protected void invalidated() {
                    initialized = false;
                    initAndResize();
                }
                @Override public Object getBean() { return Odometer.this; }
                @Override public String getName() { return "digitBackgroundColor"; }
            };
            _digitBackgroundColor = null;
        }
        return digitBackgroundColor;
    }

    public Color getDigitForegroundColor() { return null == digitForegroundColor ? _digitForegroundColor : digitForegroundColor.get(); }
    public void setDigitForegroundColor(final Color COLOR) {
        if (null == digitForegroundColor) {
            _digitForegroundColor = COLOR;
            initialized = false;
            initAndResize();
        } else {
            digitForegroundColor.set(COLOR);
        }
    }
    public ObjectProperty<Color> digitForegroundColorProperty() {
        if (null == digitForegroundColor) {
            digitForegroundColor = new ObjectPropertyBase<Color>(_digitForegroundColor) {
                @Override protected void invalidated() {
                    initialized = false;
                    initAndResize();
                }
                @Override public Object getBean() { return Odometer.this; }
                @Override public String getName() { return "digitForegroundColor"; }
            };
            _digitForegroundColor = null;
        }
        return digitForegroundColor;
    }

    public Color getDecimalBackgroundColor() { return null == decimalBackgroundColor ? _decimalBackgroundColor : decimalBackgroundColor.get(); }
    public void setDecimalBackgroundColor(final Color COLOR) {
        if (null == decimalBackgroundColor) {
            _decimalBackgroundColor = COLOR;
            initialized = false;
            initAndResize();
        } else {
            decimalBackgroundColor.set(COLOR);
        }
    }
    public ObjectProperty<Color> decimalBackgroundColorProperty() {
        if (null == decimalBackgroundColor) {
            decimalBackgroundColor = new ObjectPropertyBase<Color>(_decimalBackgroundColor) {
                @Override protected void invalidated() {
                    initialized = false;
                    initAndResize();
                }
                @Override public Object getBean() { return Odometer.this; }
                @Override public String getName() { return "decimalBackgroundColor"; }
            };
            _decimalBackgroundColor = null;
        }
        return decimalBackgroundColor;
    }

    public Color getDecimalForegroundColor() { return null == decimalForegroundColor ? _decimalForegroundColor : decimalForegroundColor.get(); }
    public void setDecimalForegroundColor(final Color COLOR) {
        if (null == decimalForegroundColor) {
            _decimalForegroundColor = COLOR;
            initialized = false;
            initAndResize();
        } else {
            decimalForegroundColor.set(COLOR);
        }
    }
    public ObjectProperty<Color> decimalForegroundColorProperty() {
        if (null == decimalForegroundColor) {
            decimalForegroundColor = new ObjectPropertyBase<Color>(_decimalForegroundColor) {
                @Override protected void invalidated() {
                    initialized = false;
                    initAndResize();
                }
                @Override public Object getBean() { return Odometer.this; }
                @Override public String getName() { return "decimalForegroundColor"; }
            };
            _decimalForegroundColor = null;
        }
        return decimalForegroundColor;
    }
    
    private void drawDigits() {
        if (!initialized) { init(); }

        int    pos   = 1;
        double value = getValue();
        int    i;
        int    num;
        int    numb;
        double fraction;
        String numbString;
        int    prevNum;

        for (i = 0; i < _decimals; i++) { value *= 10; }

        numb       = (int) Math.floor(value);
        fraction   = value - numb;
        numbString = Integer.toString(numb);
        prevNum    = 9;

        if ((int) oldValue != (int) getValue()) { fireOdometerEvent(DIGIT_CHANGED_EVENT); }

        for (i = 0; i < _decimals + _digits; i++) {
            int idx = numbString.length() - i - 1;
            num = idx < 0 ? 0 : Integer.parseInt(numbString.substring(idx, numbString.length() - i));
            if (prevNum != 9) { fraction = 0; }
            if (i < _decimals) {
                backgroundCtx.drawImage(decimalImage, width - digitWidth * pos, -(verticalSpace * (num + fraction) + zeroOffset));
            } else {
                backgroundCtx.drawImage(digitImage, width - digitWidth * pos, -(verticalSpace * (num + fraction) + zeroOffset));
            }
            pos++;
            prevNum = num;
        }
    }


    // ******************** Event handling ************************************
    public void setOnDigitChanged(final OdometerObserver OBSERVER) { addOdometerObserver(OBSERVER); }
    public void addOdometerObserver(final OdometerObserver OBSERVER) { if (!observers.contains(OBSERVER)) { observers.add(OBSERVER); }}
    public void removeOdometerObserver(final OdometerObserver OBSERVER) { if (observers.contains(OBSERVER)) { observers.remove(OBSERVER); }}
    public void removeAllObservers() { observers.clear(); }

    public void fireOdometerEvent(final OdometerEvent EVT) { for(OdometerObserver observer : observers) { observer.onOdometerEvent(EVT); } }


    @Override protected double computePrefWidth(double HEIGHT) {
        double WIDTH = MAXIMUM_WIDTH;

        if (HEIGHT > 0) {
            double digitWidth = Math.floor(HEIGHT * 0.68);
            WIDTH = digitWidth * (_digits + _decimals);
        }

        return WIDTH;
    }

    @Override protected double computePrefHeight(double WIDTH) {
        return WIDTH / (_digits + _decimals) / 0.68;
    }

    // ******************** Resizing ******************************************
    private void resize() {
        width  = getWidth() - getInsets().getLeft() - getInsets().getRight();
        height = getHeight() - getInsets().getTop() - getInsets().getBottom();

        if (keepAspect) {
            if (aspectRatio * width > height) {
                width = 1 / (aspectRatio / height);
            } else if (1 / (aspectRatio / height) > width) {
                height = aspectRatio * width;
            }
        }

        if (width > 0 && height > 0) {
            digitHeight    = Math.floor(height * 0.85);
            digitWidth     = Math.floor(height * 0.68);
            width          = digitWidth * (_digits + _decimals);
            font           = Font.font("Arial", digitHeight);
            columnHeight   = digitHeight * 11;
            extendedHeight = columnHeight * 1.1;
            verticalSpace  = columnHeight / 12;
            zeroOffset     = verticalSpace * 0.81;

            pane.setMaxSize(width, height);
            pane.setPrefSize(width, height);
            pane.relocate((getWidth() - width) * 0.5, (getHeight() - height) * 0.5);

            backgroundCanvas.setWidth(width);
            backgroundCanvas.setHeight(height);

            digitCanvas.setWidth(digitWidth);
            digitCanvas.setHeight(extendedHeight);

            decimalCanvas.setWidth(digitWidth);
            decimalCanvas.setHeight(extendedHeight);

            foreground.setWidth(width);
            foreground.setHeight(height);

            initialized = false;

            redraw();
        }
    }

    private void redraw() {
        if (!isReadyToInit()) return;
        foreground.setFill(foregroundGradient);
        if (!initialized) { init(); }
        drawDigits();
    }
}
