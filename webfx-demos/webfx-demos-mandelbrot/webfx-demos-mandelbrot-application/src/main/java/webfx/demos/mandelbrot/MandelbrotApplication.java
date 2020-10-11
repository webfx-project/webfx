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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import webfx.demos.mandelbrot.canvastracer.MandelbrotModel;
import webfx.demos.mandelbrot.canvastracer.MandelbrotPlaces;
import webfx.demos.mandelbrot.canvastracer.MandelbrotTracer;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.shared.services.resource.ResourceService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
public class MandelbrotApplication extends Application {

    private final static double COMPUTING_ZOOM_FACTOR = 0.9;
    private final static long MILLIS_IN_NANO = 1_000_000;
    private final static long TIME_BETWEEN_FRAME_NS = 16 * MILLIS_IN_NANO; // 16ms per frame

    private static int CANVAS_WIDTH = 640, CANVAS_HEIGHT = 480; // Requested default size but can be different if in browser
    private Canvas canvas, overlayCanvas;
    private Color overlayFillColor = Color.WHITE;
    private MandelbrotModel model;
    private MandelbrotTracer tracer;
    private final List<Image> snapshots = new ArrayList<>();
    private AnimationTimer snapshotsPlayer;
    private StackPane stackPane;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        // Creating the scene with the specified size (this size is ignored if running in the browser)
        Scene scene = new Scene(root, CANVAS_WIDTH, CANVAS_HEIGHT);
        primaryStage.setScene(scene);
        // Reading back the real window size in case we run in the browser
        CANVAS_WIDTH = Math.min((int) scene.getWidth(), CANVAS_WIDTH);
        CANVAS_HEIGHT = Math.min((int) scene.getHeight(), 640 * 480 / CANVAS_WIDTH);

        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        overlayCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        tracer = new MandelbrotTracer(canvas);

        stackPane = new StackPane(canvas, overlayCanvas, placeButtonBar);
        stackPane.setMaxSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        stackPane.setClip(new Rectangle(CANVAS_WIDTH, CANVAS_HEIGHT)); // To hide the button bar when it's down
        root.setCenter(stackPane);
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

        scene.getStylesheets().setAll(ResourceService.toUrl("mandelbrot.css", getClass()));
        primaryStage.setTitle("MandelbrotFX");
        primaryStage.show();
        showPlacesMenu();
    }

    private Pane placesMenu;
    private Node[] placesNodes;
    private final static Insets MARGIN = new Insets(5);

    private void showPlacesMenu() {
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
        }
        stopComputing();
        stackPane.getChildren().add(placesMenu);
        new Timeline(
                placesNodeKeyFrame(0, Node::scaleXProperty, 0),
                placesNodeKeyFrame(0, Node::scaleYProperty, 0),
                //placesNodeKeyFrame(0, Node::rotateProperty, 360),
                placesNodeKeyFrame(500, Node::scaleXProperty, 1),
                placesNodeKeyFrame(500, Node::scaleYProperty, 1)
                //placesNodeKeyFrame(500, Node::rotateProperty, 0)
        ).play();
        hidePlaceButtonBar();
    }

    private KeyFrame placesNodeKeyFrame(double millis, Function<Node, DoubleProperty> nodePropertyGetter, double endValue) {
        return new KeyFrame(new Duration(millis), Arrays.stream(placesNodes).map(n -> new KeyValue(nodePropertyGetter.apply(n), endValue)).toArray(KeyValue[]::new));
    }

    private Node createPlaceNode(int placeIndex) {
        ImageView imageView = new ResizableImageView(ResourceService.toUrl(MandelbrotPlaces.PLACES[placeIndex].thumbnailUrl, MandelbrotApplication.this.getClass()));
        imageView.setCursor(Cursor.HAND);
        imageView.setOnMouseClicked(e -> showPlace(placeIndex));
        return imageView;
    }

    private void showPlace(int placeIndex) {
        stackPane.getChildren().remove(placesMenu);
        drawMandelbrot(MandelbrotPlaces.PLACES[placeIndex]);
        showPlaceButtonBar();
    }

    private final HBox placeButtonBar = new HBox(8);
    {
        placeButtonBar.setMaxHeight(30);
        placeButtonBar.setPadding(new Insets(10));
        placeButtonBar.setAlignment(Pos.BOTTOM_CENTER);
        placeButtonBar.setTranslateY(100); // Initially down
        StackPane.setAlignment(placeButtonBar, Pos.BOTTOM_CENTER);
        populatePlaceButtonBar();
    }

    private void populatePlaceButtonBar() {
        placeButtonBar.getChildren().setAll(
                createImageButton(imageName("play-#theme.png"),   this::onSlowPlayClicked),
                createImageButton(imageName("play2-#theme.png"),  this::onFullPlayClicked),
                createImageButton(imageName("#compute-#theme.png"), this::onResumedClicked),
                createImageButton(imageName("fill-#theme.png"),   this::onLampClicked),
                createImageButton(imageName("exit-#theme.png"),   this::onExitClicked)
        );
    }

    private String imageName(String patternName) {
        boolean white = overlayFillColor == Color.WHITE;
        return patternName.replaceAll("#theme", white ? "white" : "black").replaceAll("#compute", computingPaused ? "resume" : "pause");
    }

    private Node createImageButton(String imagePath, Runnable clickRunnable) {
        ImageView imageView = new ImageView(ResourceService.toUrl(imagePath, getClass()));
        imageView.setFitWidth(64);
        imageView.setFitHeight(64);
        imageView.setOnMouseClicked(e -> clickRunnable.run());
        return imageView;
    }

    private void showPlaceButtonBar() {
        new Timeline(new KeyFrame(new Duration(200), new KeyValue(placeButtonBar.translateYProperty(), 0, Interpolator.EASE_OUT))).play();
    }

    private void hidePlaceButtonBar() {
        new Timeline(new KeyFrame(new Duration(200), new KeyValue(placeButtonBar.translateYProperty(), 100))).play();
        stopOverlayTextAnimation();
        clearOverlay();
    }

    private void onSlowPlayClicked() {
        playOrStopSnapshots(0.5);
    }

    private void onFullPlayClicked() {
        playOrStopSnapshots(1);
    }

    private void onResumedClicked() {
        startOrPauseOrResumeComputing();
        populatePlaceButtonBar();
    }

    private void onLampClicked() {
        setOverlayFillColor(overlayFillColor == Color.BLACK ? Color.WHITE : Color.BLACK);
    }

    private void onExitClicked() {
        hidePlaceButtonBar();
        showPlacesMenu();
    }

    private void setOverlayFillColor(Color overlayFillColor) {
        if (overlayFillColor != this.overlayFillColor) {
            this.overlayFillColor = overlayFillColor;
            updateOverlayCanvas();
            populatePlaceButtonBar();
        }
    }

    private Font overlayFont;
    private GraphicsContext ctx;

    private void clearOverlay() {
        if (ctx == null) {
            ctx = overlayCanvas.getGraphicsContext2D();
            overlayFont = new Font("Spaceboy", 16);
        }
        ctx.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
    }

    private void updateOverlayCanvas() {
        clearOverlay();
        if (computing && !computingPaused) {
            if (snapshots.isEmpty())
                showOverlayTexts("Waiting frame completion", "");
            else {
                int count = tracer.getLastFrameIterations();
                long t = tracer.getLastFrameComputationTime();
                showOverlayTexts(
                        completeSpace("Frame:"), "" + snapshots.size(),
                        completeSpace("Iterations:"), getIntegerWith1Decimal(count * 10 / 1_000_000) + "M",
                        completeSpace("Time:"), "" + getIntegerWith1Decimal(t * 10 / 1000) + "s",
                        completeSpace("IPS:"), "" + getIntegerWith1Decimal(count * 10 / t * 1_000 / 1_000_000) + "M",
                        completeSpace("FPS:"), "" + getIntegerWith1Decimal(1_000 * 10 / t),
                        completeSpace("CPU cores:"), "" + UiScheduler.availableProcessors(),
                        completeSpace("Threads:"), "" + tracer.getComputedThreadCount() + " workers",
                        completeSpace("Backend:"), "WebAssembly"
                        //"Agent", WebFxKitLauncher.getUserAgent(),
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
        ctx.setFill(overlayFillColor);
        ctx.setFont(overlayFont);
        int charCount = 0;
        for (int i = 0; i < texts.length && charCount < overlayCharactersMax; i++) {
            String text = texts[i];
            int length = text.length();
            charCount += length;
            if (charCount > overlayCharactersMax)
                text = text.substring(0, length - charCount + overlayCharactersMax) + "\u25FC";
            ctx.fillText(text, i % 2 == 0 ? 10 : 175, 50 + 20 * (i / 2));
        }
        if (overlayTextAnimationTimer != null && overlayCharactersMax > charCount + 2) {
            if (overlayCharactersMax / 20 % 2 == 0)
                ctx.fillText("\u25FC", 10, 50 + 20 * (texts.length / 2));
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

    private boolean computing, computingPaused;

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
            setOverlayFillColor(Color.WHITE);
            this.model = model.duplicate();
            tracer.setModel(this.model);
            this.model.adjustAspect(CANVAS_WIDTH, CANVAS_HEIGHT);
        }
        startComputing();
    }
}