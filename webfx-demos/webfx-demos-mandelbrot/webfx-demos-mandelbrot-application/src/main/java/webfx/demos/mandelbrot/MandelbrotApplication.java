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


import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import webfx.demos.mandelbrot.canvastracer.CanvasTracer;
import webfx.demos.mandelbrot.mandelbrotmodel.MandelbrotModel;
import webfx.demos.mandelbrot.mandelbrotmodel.MandelbrotPixelColorComputer;
import webfx.demos.mandelbrot.mandelbrotmodel.MandelbrotPlaces;
import webfx.platform.shared.services.log.Logger;
import webfx.platform.shared.services.resource.ResourceService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class MandelbrotApplication extends Application {

    private final static double FILM_ZOOM_FACTOR = 0.9;
    private final static double BUTTON_ZOOM_FACTOR = 0.5;
    private final static long MILLIS_IN_NANO = 1_000_000;
    private final static long TIME_BETWEEN_FRAME_NS = 16 * MILLIS_IN_NANO; // 16ms per frame
    private static boolean playBetweenComputations = false;

    private static int CANVAS_WIDTH = 640, CANVAS_HEIGHT = 480;
    private Canvas canvas, overlayCanvas;
    private CanvasTracer canvasTracer;
    private MandelbrotModel model;
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
        CANVAS_WIDTH  = Math.min((int) scene.getWidth(), CANVAS_WIDTH);
        CANVAS_HEIGHT = Math.min((int) scene.getHeight(), 640 * 480 / CANVAS_WIDTH);

        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        overlayCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        canvasTracer = new CanvasTracer(canvas);

        stackPane = new StackPane(canvas, overlayCanvas);
        stackPane.setMaxSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        root.setCenter(stackPane);
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

/*
        overlayCanvas.setOnMouseMoved(e -> {
            if (isPlaceMenuShowing())
                updateOverlayCanvas(e.getX(), e.getY());
        });
        overlayCanvas.setOnMouseClicked(e -> {
            if (isPlaceMenuShowing())
                center(e.getX(), e.getY());
            else
                showPlaceMenu();
        });
        overlayCanvas.setOnMouseExited(e -> {
            if (isPlaceMenuShowing())
                updateOverlayCanvas(null, null);
        });
*/
        overlayCanvas.setOnMouseClicked(e -> showPlaceMenu());

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
        stopRecording();
        stackPane.getChildren().add(placesMenu);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().setAll(
                new KeyFrame(new Duration(0)  , Arrays.stream(placesNodes).map(n -> new KeyValue(n.scaleXProperty(), 0)).toArray(KeyValue[]::new)),
                new KeyFrame(new Duration(0)  , Arrays.stream(placesNodes).map(n -> new KeyValue(n.scaleYProperty(), 0)).toArray(KeyValue[]::new)),
                new KeyFrame(new Duration(500), Arrays.stream(placesNodes).map(n -> new KeyValue(n.scaleXProperty(), 1)).toArray(KeyValue[]::new)),
                new KeyFrame(new Duration(500), Arrays.stream(placesNodes).map(n -> new KeyValue(n.scaleYProperty(), 1)).toArray(KeyValue[]::new))
        );
        timeline.play();
    }

    private Node createPlaceNode(int placeIndex) {
        ImageView imageView = new ImageView(ResourceService.toUrl(MandelbrotPlaces.PLACES[placeIndex].thumbnailUrl, getClass())) {
            @Override
            public boolean isResizable() {
                return true;
            }

            @Override
            public void resize(double width, double height) {
                setFitWidth(width);
                setFitHeight(height);
            }

            @Override
            public double minWidth(double height) {
                return 0;
            }

            @Override
            public double maxWidth(double height) {
                return Double.MAX_VALUE;
            }

            @Override
            public double minHeight(double width) {
                return 0;
            }

            @Override
            public double maxHeight(double width) {
                return Double.MAX_VALUE;
            }
        };
        imageView.setCursor(Cursor.HAND);
        imageView.setOnMouseClicked(e -> showPlace(placeIndex));
        return imageView;
    }

    private void showPlace(int placeIndex) {
        stackPane.getChildren().remove(placesMenu);
        drawMandelbrot(MandelbrotPlaces.PLACES[placeIndex]);
        startRecording();
    }

    /*private final CheckBox checkBox = new CheckBox("Play between computations");
    {
        checkBox.setSelected(playBetweenComputations);
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> playBetweenComputations = newValue);
    }*/

    private final HBox placeMenu = new HBox(10,
            //checkBox,
            createButton("Play", e -> onPlaceMenuPlay()),
            createButton("Slow", e -> onPlaceMenuSlow()),
            createButton("Continue", e -> onPlaceMenuContinue()),
            createButton("Exit", e -> onPlaceMenuExit())
            //createButton("+", e -> zoomIn()),
            //createButton("-", e -> zoomOut()),
            //createPrintPlaceButton()
    );
    {
        placeMenu.setMaxHeight(30);
        placeMenu.setPadding(new Insets(10));
        placeMenu.setAlignment(Pos.BOTTOM_CENTER);
        StackPane.setAlignment(placeMenu, Pos.BOTTOM_CENTER);
    }

    private void showPlaceMenu() {
        startOrPauseOrResumeRecording();
        if (!isPlaceMenuShowing())
            stackPane.getChildren().add(placeMenu);
    }

    private void hidePlaceMenu() {
        stackPane.getChildren().remove(placeMenu);
    }

    private boolean isPlaceMenuShowing() {
        return stackPane.getChildren().contains(placeMenu);
    }

    private void onPlaceMenuPlay() {
        playOrStopSnapshots(1);
    }

    private void onPlaceMenuSlow() {
        playOrStopSnapshots(0.5);
    }

    private void onPlaceMenuContinue() {
        hidePlaceMenu();
        startOrPauseOrResumeRecording();
    }

    private void onPlaceMenuExit() {
        hidePlaceMenu();
        showPlacesMenu();
    }

    private Button createPrintPlaceButton() {
        return createButton("Pr", e -> {
            Logger.log("zoom = " + zoom);
            BigDecimal xmin = model.xmin;
            BigDecimal xmax = model.xmax;
            BigDecimal ymin = model.ymin;
            BigDecimal ymax = model.ymax;
            Logger.log(xmin + ",");
            Logger.log(xmax + ",");
            Logger.log(ymin + ",");
            Logger.log(ymax + ",");
            if (zoom != 1) {
                BigDecimal xc = xmin.add(xmax).divide(model.TWO);
                BigDecimal yc = ymin.add(ymax).divide(model.TWO);
                BigDecimal halfWidth = BigDecimal.valueOf(xmax.subtract(xmin).doubleValue() / 2 / zoom);
                BigDecimal halfHeight = BigDecimal.valueOf(ymax.subtract(ymin).doubleValue() / 2 / zoom);
                xmin = xc.subtract(halfWidth);
                xmax = xc.add(halfWidth);
                ymin = yc.subtract(halfHeight);
                ymax = yc.add(halfHeight);
                Logger.log("zoom = 1");
                Logger.log(xmin + ",");
                Logger.log(xmax + ",");
                Logger.log(ymin + ",");
                Logger.log(ymax + ",");
            }
        });
    }

    private Button createButton(String text, EventHandler<ActionEvent> actionHandler) {
        Button button = new Button(text);
        button.setOnAction(actionHandler);
        return button;
    }

    private void updateOverlayCanvas(Double x, Double y) {
        GraphicsContext ctx = overlayCanvas.getGraphicsContext2D();
        ctx.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        if (x != null && y != null) {
            ctx.setStroke(Color.RED);
            ctx.strokeLine(x, 0, x, CANVAS_HEIGHT);
            ctx.strokeLine(0, y, CANVAS_WIDTH, y);
        }
        if (recording && !recordingPaused) {
            ctx.setFill(Color.WHITE);
            ctx.fillText("" + (snapshots.size() + 1), 10, 50);
        }
    }

    private void center(double cx, double cy) {
        model.centerPercent(cx / CANVAS_WIDTH, 1d - cy / CANVAS_HEIGHT);
        drawMandelbrot(model);
    }

    private void zoomIn() {
        stopRecordingAndClearSnapshots();
        zoom(BUTTON_ZOOM_FACTOR);
    }

    private void zoomOut() {
        stopRecordingAndClearSnapshots();
        zoom(1 / BUTTON_ZOOM_FACTOR);
    }

    private double zoom;

    private void zoom(double zoomFactor) {
        zoom *= zoomFactor;
        model.zoom(zoomFactor);
        drawMandelbrot(model);
    }

    private boolean recording;
    private boolean recordingPaused;

    private void startOrPauseOrResumeRecording() {
        if (!recording) { // Start
            startRecording();
        } else { // Pause or resume
            recordingPaused = !recordingPaused;
            if (!recordingPaused)
                zoomInIfRecording();
        }
    }

    private void startRecording() {
        recording = true;
        recordingPaused = false;
        canvasTracer.setOnFinished(this::takeSnapshotAndZoomIfRecording);
        //takeSnapshotAndZoomIfRecording();
    }

    private void stopRecording() {
        recording = false;
        recordingPaused = false;
        canvasTracer.setOnFinished(null);
        canvasTracer.stop();
    }

    private void stopRecordingAndClearSnapshots() {
        canvasTracer.setOnFinished(null);
        recording = false;
        snapshots.clear();
    }

    private void takeSnapshotAndZoomIfRecording() {
        takeSnapshot();
        if (recording && !recordingPaused) {
            if (playBetweenComputations)
                playOrStopSnapshots(1);
            else
                zoomInIfRecording();
        }
    }

    private void zoomInIfRecording() {
        if (recording && !recordingPaused)
            zoom(FILM_ZOOM_FACTOR);
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
                        index++;
                        if (index >= size) {
                            stop();
                            snapshotsPlayer = null;
                            if (playBetweenComputations)
                                zoomInIfRecording();
                            return;
                        }
                        canvas.getGraphicsContext2D().drawImage(snapshots.get(index), 0, 0);
                        lastFrameTime = now;
                    }
                }
            };
            snapshotsPlayer.start();
            updateOverlayCanvas(null, null);
        }
    }

    private void takeSnapshot() {
        snapshots.add(canvas.snapshot(new SnapshotParameters(), null));
    }

    private void drawMandelbrot(MandelbrotModel model) {
        if (model != this.model) {
            this.model = model.duplicate();
            stopRecordingAndClearSnapshots();
            zoom = 1;
        }
        canvasTracer.setPixelComputer(MandelbrotPixelColorComputer.create(model, CANVAS_WIDTH, CANVAS_HEIGHT));
        canvasTracer.start();
        if (recording && !recordingPaused)
            updateOverlayCanvas(null, null);
    }
}
