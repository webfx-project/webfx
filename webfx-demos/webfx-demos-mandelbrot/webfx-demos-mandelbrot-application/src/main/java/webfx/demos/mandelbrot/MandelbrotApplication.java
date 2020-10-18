/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package webfx.demos.mandelbrot;


import eu.hansolo.fx.odometer.Odometer;
import eu.hansolo.fx.odometer.OdometerBuilder;
import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.value.WritableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.shared.services.log.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public final class MandelbrotApplication extends Application {

    private long totalIterations;

    private static int CANVAS_WIDTH = 640, CANVAS_HEIGHT = 480; // Initial requested size but final size will probably be different when running in the browser
    private final static int MAX_PIXELS_COUNT = CANVAS_WIDTH * CANVAS_HEIGHT; // Limiting the frame weight as we will take a snapshot for each
    final static double COMPUTING_ZOOM_FACTOR = 0.9;
    private final static long MILLION = 1_000_000;
    private final static long MILLIS_IN_NANO = MILLION;
    private final static long TIME_BETWEEN_FRAME_NS = 16 * MILLIS_IN_NANO; // 16ms per frame
    private final static Background BLACK_BACKGROUND = new Background(new BackgroundFill(Color.BLACK, null, null));
    private final static Paint[] OVERLAY_FILLS = {
            Color.WHITE, Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.PURPLE,
            LinearGradient.valueOf("linear-gradient(to bottom, blue, green, red)"),
            LinearGradient.valueOf("linear-gradient(to bottom, green, orange, brown)"),
            LinearGradient.valueOf("linear-gradient(to bottom, red, purple, blue)"),
    };

    private StackPane stackPane;
    private VBox overlayVBox;
    private Text selectPlaceText = new Text("Select a place to visit");
    private final DropShadow dropShadow = new DropShadow(5, 4, 4, Color.BLACK);
    private Canvas canvas, overlayCanvas;
    private Paint overlayFill;
    private MandelbrotModel model;
    private MandelbrotTracer tracer;
    private boolean computing, zoomingPaused, requestBenchmark, benchmarking, completed;
    private double completion; // Completion percentage between 0.0 and 1.0
    private final List<Image> snapshots = new ArrayList<>();
    private AnimationTimer snapshotsPlayer;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        // Creating the scene with the specified size (this size is ignored if running in the browser)
        Scene scene = new Scene(root, CANVAS_WIDTH, CANVAS_HEIGHT);
        primaryStage.setScene(scene);
        // Reading back the real window size in case we run in the browser
        CANVAS_WIDTH  = Math.min((int) scene.getWidth(),  CANVAS_WIDTH);
        CANVAS_HEIGHT = Math.min((int) scene.getHeight(), MAX_PIXELS_COUNT / CANVAS_WIDTH);

        canvas        = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        overlayCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        tracer = new MandelbrotTracer(canvas);

        stackPane = new StackPane(canvas, overlayCanvas, overlayVBox = new VBox(placeButtonBar));
        overlayVBox.setAlignment(Pos.BOTTOM_CENTER);
        stackPane.setMinSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        stackPane.setMaxSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        stackPane.setClip(new Rectangle(CANVAS_WIDTH, CANVAS_HEIGHT)); // To hide the button bar when it's animated down
        root.setCenter(stackPane);
        root.setBackground(BLACK_BACKGROUND);
        setOverlayFill(OVERLAY_FILLS[0]);

        primaryStage.setTitle("WebFx Mandelbrot");
        primaryStage.show();
        showPlacesMenu();
    }

    private Pane placesMenu;
    private Node[] placesNodes;
    private final static Insets MARGIN = new Insets(5);

    private void showPlacesMenu() {
        if (placesMenu != null)
            fadeNode(placesMenu, true);
        else {
            int n = MandelbrotPlaces.PLACES.length;
            placesNodes = new Node[n];
            for (int i = 0; i < n; i++)
                placesNodes[i] = createPlaceNode(i);
            placesMenu = new Pane(placesNodes) {
                @Override
                protected void layoutChildren() {
                    int p = (int) Math.sqrt(n);
                    int q = n / p;
                    if (p * q < n) {
                        if (CANVAS_WIDTH > CANVAS_HEIGHT)
                            p++;
                        else
                            q++;
                    }
                    double wp = (getWidth() - MARGIN.getLeft() - MARGIN.getRight()) / p;
                    double hp = (getHeight() - MARGIN.getTop() - MARGIN.getBottom()) / q;
                    for (int i = 0; i < n; i++) {
                        int col = i % p, row = i / p;
                        layoutInArea(placesNodes[i], MARGIN.getLeft() + col * wp, MARGIN.getTop() + row * hp, wp, hp, 0, MARGIN, HPos.LEFT, VPos.TOP);
                    }
                }
            };
            selectPlaceText.setFont(Font.font("Arial", FontWeight.BOLD, null, 48));
            selectPlaceText.setFill(Color.WHITE);
            selectPlaceText.setEffect(dropShadow);
            selectPlaceText.setMouseTransparent(true);
        }
        stackPane.getChildren().add(placesMenu);
        if (selectPlaceText != null) {
            stackPane.getChildren().add(selectPlaceText);
            // Responsive design: rescaling the text if too large
            selectPlaceText.setVisible(false);
            measureNodeWidth(selectPlaceText, textWidth -> {
                double maxWidth = CANVAS_WIDTH - 25;
                if (textWidth > maxWidth) {
                    double scale = maxWidth / textWidth;
                    selectPlaceText.setScaleX(scale);
                    selectPlaceText.setScaleY(scale);
                }
                selectPlaceText.setVisible(true);
            });
        }
    }

    private Timeline fadeNode(Node node, boolean fadeIn) {
        node.setOpacity(fadeIn ? 0 : 1);
        return animateProperty(1000, node.opacityProperty(), fadeIn ? 1 : 0);
    }

    private void measureNodeWidth(Node node, Consumer<Double> widthConsumer) {
        // WebFx can't calculate layoutBounds before the node is inserted in the DOM so we postpone the call
        UiScheduler.scheduleInAnimationFrame(
                () -> widthConsumer.accept(node.getLayoutBounds().getWidth()),
                3); // 2 frames delay should be ok
    }

    private Node createPlaceNode(int placeIndex) {
        ThumbnailCanvas placeNode = MandelbrotPlaces.PLACES[placeIndex].getThumbnailCanvas();
        placeNode.setCursor(Cursor.HAND);
        placeNode.setOnMouseClicked(e -> showPlace(placeIndex));
        if (placeIndex == 0)
            placeNode.getThumbnailTracer().setOnFinished(() -> animateProperty(2000, selectPlaceText.opacityProperty(), 0));
        return placeNode;
    }

    private <T> Timeline animateProperty(int durationMillis, WritableValue<T> target, T endValue) {
        Timeline timeline = new Timeline(new KeyFrame(new Duration(durationMillis), new KeyValue(target, endValue, Interpolator.EASE_BOTH)));
        timeline.play();
        return timeline;
    }

    private void showPlace(int placeIndex) {
        stackPane.getChildren().remove(placesMenu);
        if (selectPlaceText != null) {
            stackPane.getChildren().remove(selectPlaceText);
            selectPlaceText = null;
        }
        drawMandelbrot(MandelbrotPlaces.PLACES[placeIndex]);
        showPlaceButtonBar();
    }

    // Keeping reference to show or hide these buttons
    private final SVGPath pauseButton, resumeButton, gearButton, exitButton;
    private final Arc progressArc;
    private final Rotate gearRotate = new Rotate(0, 32, 32);

    private final HBox placeButtonBar = new HBox(6);
    {
        placeButtonBar.getChildren().setAll(
                                createSvgButton(SvgButtonPaths.getLightPath(),      this::onLightClicked,64),
                pauseButton  =  createSvgButton(SvgButtonPaths.getPausePath(),      this::onPauseOrResumeClicked,   64),
                resumeButton =  createSvgButton(SvgButtonPaths.getResumePath(),     this::onPauseOrResumeClicked,   64),
                                createSvgButton(SvgButtonPaths.getPlayPath(),       this::onSlowPlayClicked,  64),
                                createSvgButton(SvgButtonPaths.getPlay2Path(),      this::onFullPlayClicked,  64),
                gearButton   =  createSvgButton(SvgButtonPaths.getGearPath(),       this::onGearClicked,      64),
                exitButton   =  createSvgButton(SvgButtonPaths.getExitPath(),       this::onExitClicked,      64),
                progressArc  =  new Arc(32, 32, 30, 30, 90, 0)
        );
        gearButton.getTransforms().add(gearRotate);
        progressArc.setType(ArcType.ROUND);
        progressArc.setManaged(false);
        progressArc.layoutXProperty().bind(pauseButton.layoutXProperty());
        progressArc.layoutYProperty().bind(pauseButton.layoutYProperty());
        progressArc.setOpacity(0.75);
        progressArc.setMouseTransparent(true);
        placeButtonBar.setMaxHeight(30);
        placeButtonBar.setPadding(new Insets(10));
        placeButtonBar.setAlignment(Pos.BOTTOM_CENTER);
        placeButtonBar.setTranslateY(100); // Initially down
        //placeButtonBar.setEffect(dropShadow); // Doesn't work in the browser
        StackPane.setAlignment(placeButtonBar, Pos.BOTTOM_CENTER);
        // Responsive design: moving the exit button on top right if the button bar is too large for the screen
        measureNodeWidth(placeButtonBar, barWidth -> {
            if (barWidth > CANVAS_WIDTH) {
                placeButtonBar.getChildren().remove(exitButton);
                stackPane.getChildren().add(exitButton);
                StackPane.setAlignment(exitButton, Pos.TOP_RIGHT);
                StackPane.setMargin(exitButton, MARGIN);
                exitButton.translateXProperty().bind(placeButtonBar.translateXProperty());
                exitButton.setVisible(false); // initially invisible
            }
        });
    }

    private void onSlowPlayClicked() {
        playOrStopSnapshots(0.5);
    }

    private void onFullPlayClicked() {
        playOrStopSnapshots(1);
    }

    private void onPauseOrResumeClicked() {
        startOrPauseOrResumeComputing();
        updatePlaceButtonBar();
    }

    private void onLightClicked() {
        setOverlayFill(OVERLAY_FILLS[(Arrays.asList(OVERLAY_FILLS).indexOf(overlayFill) + 1) % OVERLAY_FILLS.length]);
    }

    private GridPane settingsView;
    private Odometer odometer;
    private Timeline odometerTimeline;
    private final SVGPath incrementButton = createSvgButton(SvgButtonPaths.getUpPath(),   this::increment, 24);
    private final SVGPath decrementButton = createSvgButton(SvgButtonPaths.getDownPath(), this::decrement, 24);
    private final Text workersText = new Text("Workers"), webAssemblyText = new Text("WebAssembly");
    private int requestedThreadCounts = -1;
    private boolean requestUsingWebAssembly = true;
    private final static double radius = 18, width = 76;
    private final Circle switchKnob = new Circle(radius - 3, Color.WHITE);
    private final Pane switchButton = new Pane(switchKnob);

    private void onGearClicked() {
        boolean wasShowing = isSettingsViewShowing();
        if (wasShowing)
            hideSettings();
        else {
            if (requestedThreadCounts == -1)
                requestedThreadCounts = tracer.getThreadsCount();
            Font settingsFont = Font.font("Arial", FontWeight.BOLD, null, 28);
            workersText.setFont(settingsFont);
            webAssemblyText.setFont(settingsFont);
            odometer = OdometerBuilder.create()
                    .digits(2)
                    .decimals(0)
                    .digitBackgroundColor(Color.BLACK)
                    .digitForegroundColor(Color.WHITE)
                    .value(requestedThreadCounts)
                    .build();
            odometer.setBackground(BLACK_BACKGROUND);
            odometer.setMaxSize(74, 55);
            VBox odometerButtons = new VBox(7, incrementButton, decrementButton);
            odometerButtons.setAlignment(Pos.CENTER);
            switchKnob.setLayoutX(requestUsingWebAssembly ? width - radius : radius);
            switchKnob.setLayoutY(radius);
            switchButton.setMinSize(width, 2 * radius);
            switchButton.setMaxSize(width, 2 * radius);
            switchButton.setOnMouseClicked(e -> {
                requestUsingWebAssembly = !requestUsingWebAssembly;
                animateProperty(200, switchKnob.layoutXProperty(), requestUsingWebAssembly ? width - radius : radius);
                requestBenchmarking();
            });
            settingsView = new GridPane();
            settingsView.setHgap(30);
            settingsView.setVgap(20);
            settingsView.add(webAssemblyText, 0, 0);
            settingsView.add(switchButton, 1, 0);
            settingsView.add(workersText, 0, 1);
            settingsView.add(new HBox(10, odometer, odometerButtons), 1, 1);
            settingsView.setAlignment(Pos.CENTER);
            overlayVBox.getChildren().add(0, settingsView);
            fadeNode(settingsView, true);
        }
        animateProperty(1000, gearRotate.angleProperty(), wasShowing ? 0 : 360);
    }

    private void hideSettings() {
        if (isSettingsViewShowing()) {
            fadeNode(settingsView, false).setOnFinished(e -> {
                overlayVBox.getChildren().remove(settingsView);
                settingsView = null;
            });
        }
    }

    private boolean isSettingsViewShowing() {
        return settingsView != null;
    }

    private void increment() {
        setRequestedThreadCounts(requestedThreadCounts + 1);
    }

    private void decrement() {
        setRequestedThreadCounts(requestedThreadCounts - 1);
    }

    private void setRequestedThreadCounts(int requestedThreadCounts) {
        if (odometerTimeline != null)
            odometerTimeline.stop();
        requestedThreadCounts = Math.max(requestedThreadCounts, 0);
        this.requestedThreadCounts = requestedThreadCounts;
        odometerTimeline = animateProperty(200, odometer.valueProperty(), this.requestedThreadCounts);
        requestBenchmarking();
    }

    private void requestBenchmarking() {
        if (zoomingPaused) {
            requestBenchmark = true;
            startComputing();
        }
    }

    private void onExitClicked() {
        stopComputing();
        fillBlackCanvas(octx);
        fadeNode(overlayCanvas, true).setOnFinished(e -> {
            fillBlackCanvas(ctx);
            showPlacesMenu();
        });
        hidePlaceButtonBar();
    }

    private static void fillBlackCanvas(GraphicsContext ctx) {
        ctx.setFill(Color.BLACK);
        ctx.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
    }

    private static SVGPath createSvgButton(String content, Runnable clickRunnable, double size) {
        SVGPath svgPath = new SVGPath();
        svgPath.setContent(content);
        svgPath.setOnMouseClicked(e -> clickRunnable.run());
        svgPath.getProperties().put("webfx-svgpath-maxSize", size); // Temporary WebFx hack (see SVGPath.impl_computeGeomBounds() emul code)
        return svgPath;
    }

    private void updatePlaceButtonBar() {
        placeButtonBar.getChildren().stream().filter(n -> n instanceof Shape).forEach(n -> ((Shape) n).setFill(overlayFill));
        setOverlayFillOnShapes(exitButton, incrementButton, decrementButton, workersText, webAssemblyText);
        switchKnob.setFill(overlayFill == Color.WHITE || overlayFill == Color.YELLOW || overlayFill == Color.CYAN ? Color.BLACK : Color.WHITE);
        switchButton.setBackground(new Background(new BackgroundFill(overlayFill, new CornerRadii(radius), null)));
        showButton(pauseButton, !completed && !zoomingPaused);
        progressArc.setVisible(!completed && !zoomingPaused);
        showButton(resumeButton, !completed && zoomingPaused);
        showButton(gearButton, !completed);
    }

    private void setOverlayFillOnShapes(Shape... shapes) {
        for (Shape shape : shapes)
            shape.setFill(overlayFill);
    }

    private static void showButton(Node button, boolean isShown) {
        button.setVisible(isShown);
        button.setManaged(isShown);
    }

    private void showPlaceButtonBar() {
        if (placeButtonBar.getTranslateX() != 0) {
            placeButtonBar.setTranslateX(-CANVAS_WIDTH);
            animateProperty(600, placeButtonBar.translateXProperty(), 0);
        }
        animateProperty(200, placeButtonBar.translateYProperty(), 0);
        exitButton.setVisible(true);
    }

    private void hidePlaceButtonBar() {
        stopOverlayTextAnimation();
        hideSettings();
        if (overlayCanvas.getOpacity() == 0) // exiting
            animateProperty(600, placeButtonBar.translateXProperty(), CANVAS_WIDTH + 64);
        else {
            animateProperty(200, placeButtonBar.translateYProperty(), 100);
            exitButton.setVisible(false);
            clearOverlay();
        }
    }

    private void setOverlayFill(Paint overlayFill) {
        if (overlayFill != this.overlayFill) {
            this.overlayFill = overlayFill;
            updateOverlayCanvas(); // To refresh text color
            updatePlaceButtonBar(); // To refresh buttons color
        }
    }

    private Font overlayFont;
    private GraphicsContext ctx, octx;

    private void clearOverlay() {
        if (octx == null) {
            ctx = canvas.getGraphicsContext2D();
            octx = overlayCanvas.getGraphicsContext2D();
            overlayFont = Font.font("Courier", FontWeight.BOLD, null, 18);
        }
        octx.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
    }

    private final static String WAITING_FRAME_COMPLETION = "Waiting frame completion";
    private final static boolean HIDE_OVERLAY_TEXT_ON_PAUSE = true;

    private boolean shouldDisplayOverlayText() {
        return computing && !(HIDE_OVERLAY_TEXT_ON_PAUSE && zoomingPaused) || benchmarking || isSettingsViewShowing();
    }

    private void updateOverlayCanvas() {
        clearOverlay(); // Hide the previous text
        if (shouldDisplayOverlayText()) {
            if (snapshots.isEmpty())
                showOverlayTexts(WAITING_FRAME_COMPLETION, "");
            else {
                // Resetting the text animation at the end of the computation of the first frame
                boolean firstFrameCompletion = lastOverlayTexts != null && lastOverlayTexts[0] == WAITING_FRAME_COMPLETION && !snapshots.isEmpty();
                if (firstFrameCompletion)
                    overlayCharactersMax = 1;
                long count = tracer.getLastFrameIterations();
                long t = tracer.getLastFrameComputationTime();
                showOverlayTexts(
                        completeSpace("Frame"), "" + snapshots.size() + " (" + (int) (100 * completion) + "%)",
                        completeSpace("Iterations"), getIntegerWith2Decimals(count * 100 / MILLION) + "M",
                        completeSpace("Time"), "" + getIntegerWith2Decimals(t * 100 / 1000) + "s",
                        completeSpace("IPS"), "" + getIntegerWith2Decimals(count * 100 / t * 1_000 / MILLION) + "M",
                        completeSpace("FPS"), "" + getIntegerWith2Decimals(1_000 * 100 / t),
                        completeSpace("CPU cores"), UiScheduler.availableProcessors() <= 0 ? "Unrevealed" : "" + UiScheduler.availableProcessors(),
                        completeSpace("Threads"), "" + tracer.getLastThreadsCount() + " worker" + (tracer.getLastThreadsCount() > 1 ? "s" : ""),
                        completeSpace("UI source"), "JavaFX",
                        completeSpace("UI target"), "JavaScript",
                        completeSpace("Compiler"), "GWT",
                        completeSpace("Math source"), "Java",
                        completeSpace("Math target"), tracer.wasLastFrameUsingWebAssembly() ? "WebAssembly" : "JavaScript",
                        completeSpace("Compiler"), "TeaVM"
                );
            }
        }
    }

    private String completeSpace(String text) {
        StringBuilder sb = new StringBuilder(text);
        while (sb.length() < 11)
            sb.append("\u3000");
        return sb.toString();
    }

    private static String getIntegerWith2Decimals(long hundredValue) {
        return hundredValue / 100 + (hundredValue >= 1000 ? "" : "." + hundredValue % 100);
    }

    private AnimationTimer overlayTextAnimationTimer;
    private int overlayCharactersMax;
    private String[] lastOverlayTexts;
    private final static String CURSOR_CHAR = "\u25FC";

    private void showOverlayTexts(String... texts) {
        startOverlayTextAnimationIfNeeded();
        octx.setFill(overlayFill);
        //octx.setEffect(dropShadow);
        octx.setFont(overlayFont);
        int charCount = 0;
        for (int i = 0; i < texts.length && charCount < overlayCharactersMax; i++) {
            String text = texts[i];
            int length = text.length();
            charCount += length;
            if (charCount > overlayCharactersMax)
                text = text.substring(0, length - charCount + overlayCharactersMax) + CURSOR_CHAR;
            octx.fillText(text, i % 2 == 0 ? 10 : 175, 20 + 20 * (i / 2));
        }
        // Cursor blinking animation
        if (overlayTextAnimationTimer != null && overlayCharactersMax > charCount + 2) {
            if (overlayCharactersMax > charCount + 220) // Stopping animation after a while
                stopOverlayTextAnimation();
            else if (overlayCharactersMax / 20 % 2 == 0) // Blinking cursor every 20 frames
                octx.fillText(CURSOR_CHAR, 10, 20 + 20 * (texts.length / 2));
        }
        lastOverlayTexts = texts;
    }

    private void startOverlayTextAnimationIfNeeded() {
        if (overlayTextAnimationTimer == null) {
            overlayTextAnimationTimer = new AnimationTimer() {
                private long lastKeyTime = UiScheduler.nanoTime();

                @Override
                public void handle(long now) {
                    if (now - lastKeyTime >= 30 * MILLIS_IN_NANO) {
                        overlayCharactersMax++;
                        clearOverlay();
                        if (shouldDisplayOverlayText())
                            showOverlayTexts(lastOverlayTexts);
                        else
                            stopOverlayTextAnimation();
                        lastKeyTime = now;
                    }
                }
            };
            overlayTextAnimationTimer.start();
        }
    }

    private void stopOverlayTextAnimation() {
        if (overlayTextAnimationTimer != null) {
            overlayTextAnimationTimer.stop();
            overlayTextAnimationTimer = null;
        }
    }

    private void zoom(double zoomFactor) {
        model.zoom(zoomFactor);
        drawMandelbrot(model);
    }

    private void startOrPauseOrResumeComputing() {
        // Resetting the text animation when resuming
        if (!shouldDisplayOverlayText())
            overlayCharactersMax = 1;
        if (!computing) // Start
            startComputing();
        else { // Pause or resume
            zoomingPaused = !zoomingPaused;
            zoomInIfComputing();
        }
    }

    private void startComputing() {
        computing = true;
        if (!requestBenchmark)
            zoomingPaused = false;
        if (!tracer.isRunning()) {
            if (requestedThreadCounts != -1)
                tracer.setThreadsCount(requestedThreadCounts);
            tracer.setUsingWebAssembly(requestUsingWebAssembly);
            tracer.setOnFinished(this::takeSnapshotAndZoomIfComputing);
            benchmarking = requestBenchmark;
            requestBenchmark = false;
            if (benchmarking)
                ctx.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
            tracer.start();
            if (snapshotsPlayer == null)
                updateOverlayCanvas(); // Will start displaying the text overlay animation
        }
    }

    private void stopComputing() {
        computing = zoomingPaused = false;
        tracer.setOnFinished(null);
        tracer.stop();
    }

    private void stopComputingAndClearSnapshots() {
        stopComputing();
        snapshots.clear();
        updateCompletion();
    }

    private void updateCompletion() {
        int lastFrame = model == null ? 1 : model.lastFrame;
        completion = 1.0 * snapshots.size() / lastFrame;
        completed = snapshots.size() >= lastFrame;
        progressArc.setLength(-360 * completion);
    }

    private void takeSnapshotAndZoomIfComputing() {
        if (!benchmarking || snapshots.isEmpty()) {
            takeMandelbrotSnapshot();
            zoomInIfComputing();
        } else if (requestBenchmark)
            startComputing();
        else
            updateOverlayCanvas();
    }

    private void zoomInIfComputing() {
        if (computing) {
            if (!zoomingPaused)
                zoom(COMPUTING_ZOOM_FACTOR);
            else
                updateOverlayCanvas(); // Will actually hide the text on pause
        }
    }

    private void playOrStopSnapshots(double playSpeed) {
        if (snapshotsPlayer != null) {
            snapshotsPlayer.stop();
            snapshotsPlayer = null;
        } else {
            snapshotsPlayer = new AnimationTimer() {
                private int index = -1;
                private long lastFrameTime;

                @Override
                public void handle(long now) {
                    int size = snapshots.size();
                    if (playSpeed * (now - lastFrameTime) >= TIME_BETWEEN_FRAME_NS) {
                        if (++index >= size) {
                            stop();
                            snapshotsPlayer = null;
                            showPlaceButtonBar();
                            updateOverlayCanvas();
                            return;
                        }
                        octx.drawImage(snapshots.get(index), 0, 0);
                        lastFrameTime = now;
                    }
                }
            };
            hidePlaceButtonBar();
            snapshotsPlayer.start();
        }
    }

    private void takeMandelbrotSnapshot() {
        snapshots.add(canvas.snapshot(new SnapshotParameters(), null));
        totalIterations += tracer.getLastFrameIterations();
        updateCompletion();
        if (completed) {
            stopComputing();
            ctx.drawImage(snapshots.get(0), 0, 0);
            updatePlaceButtonBar();
            playOrStopSnapshots(1);
            Logger.log("totalIterations = " + totalIterations);
        }
    }

    private void drawMandelbrot(MandelbrotModel model) {
        if (model != this.model) {
            stopComputingAndClearSnapshots();
            showPlaceButtonBar();
            updatePlaceButtonBar(); // To refresh pause button for example
            overlayCharactersMax = 1;
            totalIterations = 0;
            benchmarking = requestBenchmark = false;
            this.model = model.duplicate();
            tracer.setModel(this.model);
            this.model.adjustAspect(CANVAS_WIDTH, CANVAS_HEIGHT);
        }
        startComputing();
    }
}