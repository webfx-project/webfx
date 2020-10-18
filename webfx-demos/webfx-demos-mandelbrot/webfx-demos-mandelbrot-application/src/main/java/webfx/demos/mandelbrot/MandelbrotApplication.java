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


import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import webfx.platform.client.services.uischeduler.UiScheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
public final class MandelbrotApplication extends Application {

    private static int CANVAS_WIDTH = 640, CANVAS_HEIGHT = 480; // Initial requested size but final size can be different when running in the browser
    private static final int MAX_PIXELS_COUNT = CANVAS_WIDTH * CANVAS_HEIGHT;
    final static double COMPUTING_ZOOM_FACTOR = 0.9;
    private final static long MILLION = 1_000_000;
    private final static long MILLIS_IN_NANO = MILLION;
    private final static long TIME_BETWEEN_FRAME_NS = 16 * MILLIS_IN_NANO; // 16ms per frame
    private final Paint[] OVERLAY_FILLS = {
            Color.WHITE, Color.BLACK, Color.RED, Color.BLUE,
            LinearGradient.valueOf("linear-gradient(to bottom, blue, green, red)"),
            LinearGradient.valueOf("linear-gradient(to bottom, green, brown, orange)"),
            LinearGradient.valueOf("linear-gradient(to bottom, red, purple, blue)"),
    };

    private StackPane stackPane;
    private Text selectPlaceText = new Text("Select a place to visit");
    private final static DropShadow dropShadow = new DropShadow(5, 4, 4, Color.BLACK);
    private Canvas canvas, overlayCanvas;
    private Paint overlayFill;
    private MandelbrotModel model;
    private MandelbrotTracer tracer;
    private boolean computing, computingPaused;
    private final List<Image> snapshots = new ArrayList<>();
    private AnimationTimer snapshotsPlayer;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        // Creating the scene with the specified size (this size is ignored if running in the browser)
        Scene scene = new Scene(root, CANVAS_WIDTH, CANVAS_HEIGHT);
        primaryStage.setScene(scene);
        // Reading back the real window size in case we run in the browser
        CANVAS_WIDTH = Math.min((int) scene.getWidth(), CANVAS_WIDTH);
        CANVAS_HEIGHT = Math.min((int) scene.getHeight(), MAX_PIXELS_COUNT / CANVAS_WIDTH);

        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        overlayCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        tracer = new MandelbrotTracer(canvas);

        stackPane = new StackPane(canvas, overlayCanvas, placeButtonBar);
        stackPane.setMinSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        stackPane.setMaxSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        stackPane.setClip(new Rectangle(CANVAS_WIDTH, CANVAS_HEIGHT)); // To hide the button bar when it's down
        root.setCenter(stackPane);
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

        primaryStage.setTitle("WebFx Mandelbrot");
        primaryStage.show();
        showPlacesMenu();
        setOverlayFill(OVERLAY_FILLS[0]);
    }

    private Pane placesMenu;
    private Node[] placesNodes;
    private final static Insets MARGIN = new Insets(5);

    private void showPlacesMenu() {
        stopComputing();
        if (placesMenu == null) {
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
        } else
            new Timeline(
                    placesNodeKeyFrame(0, Node::scaleXProperty, 0),
                    placesNodeKeyFrame(0, Node::scaleYProperty, 0),
                    //placesNodeKeyFrame(0, Node::rotateProperty, 360),
                    placesNodeKeyFrame(500, Node::scaleXProperty, 1),
                    placesNodeKeyFrame(500, Node::scaleYProperty, 1)
                    //placesNodeKeyFrame(500, Node::rotateProperty, 0)
            ).play();
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
        hidePlaceButtonBar();
    }

    private void measureNodeWidth(Node node, Consumer<Double> widthConsumer) {
        // WebFx can't calculate layoutBounds before the node is inserted in the DOM so we postpone the call
        UiScheduler.scheduleInAnimationFrame(
                () -> widthConsumer.accept(node.getLayoutBounds().getWidth()),
                2); // 2 frames delay should be ok
    }

    private KeyFrame placesNodeKeyFrame(double millis, Function<Node, DoubleProperty> nodePropertyGetter, double endValue) {
        return new KeyFrame(new Duration(millis), Arrays.stream(placesNodes).map(n -> new KeyValue(nodePropertyGetter.apply(n), endValue)).toArray(KeyValue[]::new));
    }

    private Node createPlaceNode(int placeIndex) {
        ThumbnailCanvas placeNode = MandelbrotPlaces.PLACES[placeIndex].getThumbnailCanvas();
        placeNode.setCursor(Cursor.HAND);
        placeNode.setOnMouseClicked(e -> showPlace(placeIndex));
        if (placeIndex == 0)
            placeNode.getThumbnailTracer().setOnFinished(() -> new Timeline(new KeyFrame(new Duration(2000), new KeyValue(selectPlaceText.opacityProperty(), 0))).play());
        return placeNode;
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
    private final SVGPath pauseButton, resumeButton, exitButton;

    private final HBox placeButtonBar = new HBox(6);
    {
        placeButtonBar.getChildren().setAll(
                                createSvgButton(SvgButtonPaths.getRollColorsPath(), this::onRollColorsClicked),
                pauseButton  =  createSvgButton(SvgButtonPaths.getPausePath(),      this::onResumedClicked),
                resumeButton =  createSvgButton(SvgButtonPaths.getResumePath(),     this::onResumedClicked),
                                createSvgButton(SvgButtonPaths.getPlayPath(),       this::onSlowPlayClicked),
                                createSvgButton(SvgButtonPaths.getPlay2Path(),      this::onFullPlayClicked),
                                createSvgButton(SvgButtonPaths.getGearPath(),       this::onGearClicked),
                exitButton   =  createSvgButton(SvgButtonPaths.getExitPath(),       this::onExitClicked)
        );
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
            }
        });
    }

    private void onSlowPlayClicked() {
        playOrStopSnapshots(0.5);
    }

    private void onFullPlayClicked() {
        playOrStopSnapshots(1);
    }

    private void onResumedClicked() {
        startOrPauseOrResumeComputing();
        updatePlaceButtonBar();
    }

    private void onRollColorsClicked() {
        setOverlayFill(OVERLAY_FILLS[(Arrays.asList(OVERLAY_FILLS).indexOf(overlayFill) + 1) % OVERLAY_FILLS.length]);
    }

    private void onGearClicked() {
    }

    private void onExitClicked() {
        hidePlaceButtonBar();
        showPlacesMenu();
    }

    private static SVGPath createSvgButton(String content, Runnable clickRunnable) {
        SVGPath svgPath = new SVGPath();
        svgPath.setContent(content);
        svgPath.setOnMouseClicked(e -> clickRunnable.run());
        return svgPath;
    }

    private void updatePlaceButtonBar() {
        placeButtonBar.getChildren().stream().filter(n -> n instanceof SVGPath).forEach(n -> ((SVGPath) n).setFill(overlayFill));
        exitButton.setFill(overlayFill); // In case it's not in the button bar but has been moved on right top corner
        showButton(pauseButton, !computingPaused);
        showButton(resumeButton, computingPaused);
    }

    private static void showButton(Node button, boolean isShown) {
        button.setVisible(isShown);
        button.setManaged(isShown);
    }

    private void showPlaceButtonBar() {
        new Timeline(new KeyFrame(new Duration(200), new KeyValue(placeButtonBar.translateYProperty(), 0, Interpolator.EASE_OUT))).play();
        exitButton.setVisible(true);
    }

    private void hidePlaceButtonBar() {
        new Timeline(new KeyFrame(new Duration(200), new KeyValue(placeButtonBar.translateYProperty(), 100))).play();
        stopOverlayTextAnimation();
        clearOverlay();
        exitButton.setVisible(false);
    }

    private void setOverlayFill(Paint overlayFill) {
        if (overlayFill != this.overlayFill) {
            this.overlayFill = overlayFill;
            updateOverlayCanvas();
            updatePlaceButtonBar();
        }
    }

    private Font overlayFont;
    private GraphicsContext ctx;

    private void clearOverlay() {
        if (ctx == null) {
            ctx = overlayCanvas.getGraphicsContext2D();
            overlayFont = Font.font("Courier", FontWeight.BOLD, null, 18);
        }
        ctx.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
    }

    private void updateOverlayCanvas() {
        clearOverlay();
        if (computing && !computingPaused) {
            if (snapshots.isEmpty())
                showOverlayTexts("Waiting frame completion", "");
            else {
                long count = tracer.getLastFrameIterations();
                long t = tracer.getLastFrameComputationTime();
                showOverlayTexts(
                        completeSpace("Frame"), "" + snapshots.size(),
                        completeSpace("Iterations"), getIntegerWith1Decimal(count * 10 / MILLION) + "M",
                        completeSpace("Time"), "" + getIntegerWith1Decimal(t * 10 / 1000) + "s",
                        completeSpace("IPS"), "" + getIntegerWith1Decimal(count * 10 / t * 1_000 / MILLION) + "M",
                        completeSpace("FPS"), "" + getIntegerWith1Decimal(1_000 * 10 / t),
                        completeSpace("CPU cores"), UiScheduler.availableProcessors() <= 0 ? "Unrevealed" : "" + UiScheduler.availableProcessors(),
                        completeSpace("Threads"), "" + tracer.getThreadsCount() + " worker" + (tracer.getThreadsCount() > 1 ? "s" : ""),
                        completeSpace("UI source"), "JavaFX",
                        completeSpace("UI target"), "JavaScript",
                        completeSpace("Compiler"), "GWT",
                        completeSpace("Math source"), "Java",
                        completeSpace("Math target"), "WebAssembly",
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

    private static String getIntegerWith1Decimal(long tenValue) {
        return tenValue / 10 + (tenValue >= 100 ? "" : "." + tenValue % 10);
    }

    private AnimationTimer overlayTextAnimationTimer;
    private int overlayCharactersMax;
    private String[] lastOverlayTexts;

    private void showOverlayTexts(String... texts) {
        if (lastOverlayTexts != null && lastOverlayTexts.length == 2 && texts.length > 2)
            overlayCharactersMax = 1;
        startOverlayTextAnimationIfNeeded();
        ctx.setFill(overlayFill);
        //ctx.setEffect(dropShadow);
        ctx.setFont(overlayFont);
        int charCount = 0;
        for (int i = 0; i < texts.length && charCount < overlayCharactersMax; i++) {
            String text = texts[i];
            int length = text.length();
            charCount += length;
            if (charCount > overlayCharactersMax)
                text = text.substring(0, length - charCount + overlayCharactersMax) + "\u25FC";
            ctx.fillText(text, i % 2 == 0 ? 10 : 175, 20 + 20 * (i / 2));
        }
        if (overlayTextAnimationTimer != null && overlayCharactersMax > charCount + 2) {
            if (overlayCharactersMax / 20 % 2 == 0)
                ctx.fillText("\u25FC", 10, 20 + 20 * (texts.length / 2));
            if (overlayCharactersMax > charCount + 200)
                stopOverlayTextAnimation();
        }
        lastOverlayTexts = texts;
    }

    private void startOverlayTextAnimationIfNeeded() {
        if (overlayTextAnimationTimer == null) {
            overlayTextAnimationTimer = new AnimationTimer() {
                private long lastKeyTime = UiScheduler.nanoTime();

                @Override
                public void handle(long now) {
                    if (now - lastKeyTime >= 30 * 1_000_000) {
                        overlayCharactersMax++;
                        clearOverlay();
                        showOverlayTexts(lastOverlayTexts);
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
        if (!computing || computingPaused)
            overlayCharactersMax = 1;
        if (!computing) // Start
            startComputing();
        else { // Pause or resume
            computingPaused = !computingPaused;
            zoomInIfComputing();
        }
    }

    private void startComputing() {
        computing = true;
        computingPaused = false;
        if (!tracer.isRunning()) {
            tracer.setOnFinished(this::takeSnapshotAndZoomIfComputing);
            tracer.start();
            if (snapshotsPlayer == null)
                updateOverlayCanvas(); // Will start displaying the text overlay animation
        }
    }

    private void stopComputing() {
        computing = computingPaused = false;
        tracer.setOnFinished(null);
        tracer.stop();
    }

    private void stopComputingAndClearSnapshots() {
        stopComputing();
        snapshots.clear();
    }

    private void takeSnapshotAndZoomIfComputing() {
        takeMandelbrotSnapshot();
        zoomInIfComputing();
    }

    private void zoomInIfComputing() {
        if (computing && !computingPaused)
            zoom(COMPUTING_ZOOM_FACTOR);
    }

    private void playOrStopSnapshots(double playSpeed) {
        if (snapshotsPlayer != null) {
            snapshotsPlayer.stop();
            snapshotsPlayer = null;
        } else {
            snapshotsPlayer = new AnimationTimer() {
                private int index = -1;
                private long lastFrameTime;
                private final GraphicsContext ctx = overlayCanvas.getGraphicsContext2D();

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
                        ctx.drawImage(snapshots.get(index), 0, 0);
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
    }

    private void drawMandelbrot(MandelbrotModel model) {
        if (model != this.model) {
            stopComputingAndClearSnapshots();
            showPlaceButtonBar();
            overlayCharactersMax = 1;
            this.model = model.duplicate();
            tracer.setModel(this.model);
            this.model.adjustAspect(CANVAS_WIDTH, CANVAS_HEIGHT);
        }
        startComputing();
    }
}