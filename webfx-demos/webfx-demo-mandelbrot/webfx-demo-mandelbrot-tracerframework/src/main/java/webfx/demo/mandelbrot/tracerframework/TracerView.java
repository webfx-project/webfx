package webfx.demo.mandelbrot.tracerframework;

import eu.hansolo.fx.odometer.Odometer;
import eu.hansolo.fx.odometer.OdometerBuilder;
import javafx.animation.*;
import javafx.beans.value.WritableValue;
import javafx.geometry.*;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
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
public class TracerView {

    private long totalIterations;

    private final int canvasWidth, canvasHeight;
    private final PixelComputer pixelComputer;
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
    private Paint overlayFill = OVERLAY_FILLS[0];

    private StackPane stackPane;
    private VBox overlayVBox;
    private Text selectPlaceText = new Text("Select a place to visit");
    private final DropShadow dropShadow = new DropShadow(5, 4, 4, Color.BLACK);
    private Canvas canvas, overlayCanvas;
    private int placeIndex, frameIndex;
    private TracerEngine tracer;
    private final boolean hasSeveralPlaces;
    private boolean computing, zoomingPaused, requestBenchmark, benchmarking, completed;
    private double completion; // Completion percentage between 0.0 and 1.0
    private final List<Image> snapshots = new ArrayList<>();
    private AnimationTimer snapshotsPlayer;

    // Keeping reference to show or hide these buttons
    private Pane pauseButton, resumeButton, gearButton, exitButton;
    private Arc progressArc;

    private final HBox placeButtonBar = new HBox(6);

    public TracerView(int canvasWidth, int canvasHeight, PixelComputer pixelComputer) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.pixelComputer = pixelComputer;
        requestUsingWebAssembly = pixelComputer.isUsingWebAssembly();
        hasSeveralPlaces = pixelComputer.getPlacesCount() > 1;
    }

    public Parent buildView() {
        BorderPane root = new BorderPane();
        canvas        = new Canvas(canvasWidth, canvasHeight);
        overlayCanvas = new Canvas(canvasWidth, canvasHeight);
        tracer = new TracerEngine(canvas, pixelComputer);

        stackPane = new StackPane(canvas, overlayCanvas, overlayVBox = new VBox(placeButtonBar));
        overlayVBox.setAlignment(Pos.BOTTOM_CENTER);
        stackPane.setMinSize(canvasWidth, canvasHeight);
        stackPane.setMaxSize(canvasWidth, canvasHeight);
        stackPane.setClip(new Rectangle(canvasWidth, canvasHeight)); // To hide the button bar when it's animated down

        placeButtonBar.getChildren().setAll(
                                createSvgButton(SvgButtonPaths.getLightPath(),      this::onLightClicked),
                pauseButton  =  createSvgButton(SvgButtonPaths.getPausePath(),      this::onPauseOrResumeClicked),
                resumeButton =  createSvgButton(SvgButtonPaths.getResumePath(),     this::onPauseOrResumeClicked),
                                createSvgButton(SvgButtonPaths.getPlayPath(),       this::onSlowPlayClicked),
                                createSvgButton(SvgButtonPaths.getPlay2Path(),      this::onFullPlayClicked),
                gearButton   =  createSvgButton(SvgButtonPaths.getGearPath(),       this::onGearClicked),
                exitButton   =  createSvgButton(SvgButtonPaths.getExitPath(),       this::onExitClicked),
                progressArc  =  new Arc(32, 32, 30, 30, 90, 0)
        );
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
            if (barWidth > canvasWidth) {
                placeButtonBar.getChildren().remove(exitButton);
                stackPane.getChildren().add(exitButton);
                StackPane.setAlignment(exitButton, Pos.TOP_RIGHT);
                StackPane.setMargin(exitButton, MARGIN);
                exitButton.translateXProperty().bind(placeButtonBar.translateXProperty());
                exitButton.setVisible(false); // initially invisible
            }
            updatePlaceButtonBar(); // To refresh buttons color
            if (!hasSeveralPlaces)
                showPlace(0);
        });

        root.setCenter(stackPane);
        root.setBackground(BLACK_BACKGROUND);

        if (hasSeveralPlaces)
            showPlacesMenu();

        return root;
    }

    private Pane placesMenu;
    private Node[] placesNodes;
    private final static Insets MARGIN = new Insets(5);

    private void showPlacesMenu() {
        if (placesMenu != null)
            fadeNode(placesMenu, true);
        else {
            int n = pixelComputer.getPlacesCount();
            placesNodes = new Node[n];
            for (int i = 0; i < n; i++)
                placesNodes[i] = createPlaceThumbnail(i);
            placesMenu = new Pane(placesNodes) {
                @Override
                protected void layoutChildren() {
                    int p = (int) Math.sqrt(n);
                    int q = n / p;
                    if (p * q < n) {
                        if (canvasWidth > canvasHeight)
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
                double maxWidth = canvasWidth - 25;
                if (textWidth > maxWidth) {
                    double scale = maxWidth / textWidth;
                    selectPlaceText.setScaleX(scale);
                    selectPlaceText.setScaleY(scale);
                }
                selectPlaceText.setVisible(true);
                setOverlayFill(OVERLAY_FILLS[0]);
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
                5); // 5 frames delay should be ok
    }

    private Node createPlaceThumbnail(int placeIndex) {
        TracerThumbnail placeThumbnail = pixelComputer.getPlaceThumbnail(placeIndex);
        placeThumbnail.setCursor(Cursor.HAND);
        placeThumbnail.setOnMouseClicked(e -> showPlace(placeIndex));
        if (placeIndex == 0)
            placeThumbnail.getThumbnailTracer().setOnFinished(() -> animateProperty(2000, selectPlaceText.opacityProperty(), 0));
        return placeThumbnail;
    }

    static <T> Timeline animateProperty(int durationMillis, WritableValue<T> target, T endValue) {
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
        drawMandelbrot(placeIndex, 0);
        showPlaceButtonBar();
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
    private final Pane incrementButton = createSvgButton(SvgButtonPaths.getUpPath(),   this::increment);
    private final Pane decrementButton = createSvgButton(SvgButtonPaths.getDownPath(), this::decrement);
    private final Text workersText = new Text("Workers"), webAssemblyText = new Text("WebAssembly");
    private int requestedThreadCounts = -1;
    private boolean requestUsingWebAssembly;
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
        animateProperty(1000, getSvgButtonPath(gearButton).rotateProperty(), wasShowing ? 0 : 180);
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

    private void fillBlackCanvas(GraphicsContext ctx) {
        ctx.setFill(Color.BLACK);
        ctx.fillRect(0, 0, canvasWidth, canvasHeight);
    }

    private static Pane createSvgButton(String content, Runnable clickRunnable) {
        SVGPath path = new SVGPath();
        path.setContent(content);
        // We now embed the svg path in a pane. The reason is for a better click experience. Because in JavaFx (not in
        // the browser), the clicking area is only the filled shape, not the empty space in that shape. So when clicking
        // on a gear icon on a mobile for example, even if globally our finger covers the icon, the final click point
        // may be in this empty space, making the button not reacting, leading to a frustrating experience.
        Pane pane = new Pane(path); // Will act as the mouse click area covering the entire surface
        // The pane needs to be reduced to the svg path size (which we can get using the layout bounds).
        path.sceneProperty().addListener(p -> { // This postpone is necessary only when running in the browser, not in standard JavaFx
            Bounds b = path.getLayoutBounds(); // Bounds computation should be correct now even in the browser
            pane.setMinSize(b.getWidth(), b.getHeight());
            pane.setMaxSize(b.getWidth(), b.getHeight());
        });
        pane.setCursor(Cursor.HAND);
        pane.setOnMouseClicked(e -> clickRunnable.run());
        return pane;
    }

    private SVGPath getSvgButtonPath(Pane svgButton) {
        return (SVGPath) svgButton.getChildren().get(0);
    }

    private void updatePlaceButtonBar() {
        placeButtonBar.getChildren().forEach(this::setOverlayFillOnShapes);
        setOverlayFillOnShapes(exitButton, incrementButton, decrementButton, workersText, webAssemblyText);
        switchKnob.setFill(overlayFill == Color.WHITE || overlayFill == Color.YELLOW || overlayFill == Color.CYAN ? Color.BLACK : Color.WHITE);
        switchButton.setBackground(new Background(new BackgroundFill(overlayFill, new CornerRadii(radius), null)));
        showButton(pauseButton, !completed && !zoomingPaused);
        progressArc.setVisible(!completed && !zoomingPaused);
        showButton(resumeButton, !completed && zoomingPaused);
        showButton(gearButton, !completed);
    }

    private void setOverlayFillOnShapes(Node... nodes) {
        for (Node node : nodes) {
            Shape shape = node instanceof Shape ? (Shape) node : getSvgButtonPath((Pane) node);
            shape.setFill(overlayFill);
        }
    }

    private static void showButton(Node button, boolean isShown) {
        button.setVisible(isShown);
        button.setManaged(isShown);
    }

    private void showPlaceButtonBar() {
        if (placeButtonBar.getTranslateX() != 0) {
            placeButtonBar.setTranslateX(-canvasWidth);
            animateProperty(600, placeButtonBar.translateXProperty(), 0);
        }
        animateProperty(200, placeButtonBar.translateYProperty(), 0);
        exitButton.setVisible(hasSeveralPlaces);
    }

    private void hidePlaceButtonBar() {
        stopOverlayTextAnimation();
        hideSettings();
        if (overlayCanvas.getOpacity() == 0) // exiting
            animateProperty(600, placeButtonBar.translateXProperty(), canvasWidth + 64);
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
        } else
            octx.clearRect(0, 0, canvasWidth, canvasHeight);
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
                long count = pixelComputer.getLastFrameIterations();
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

    private void startOrPauseOrResumeComputing() {
        // Resetting the text animation when resuming
        if (!shouldDisplayOverlayText())
            overlayCharactersMax = 1;
        if (!computing) // Start
            startComputing();
        else { // Pause or resume
            zoomingPaused = !zoomingPaused;
            startNextFrameIfComputing();
        }
    }

    private void startComputing() {
        computing = true;
        if (!requestBenchmark)
            zoomingPaused = false;
        if (!tracer.isRunning()) {
            if (requestedThreadCounts != -1)
                tracer.setThreadsCount(requestedThreadCounts);
            pixelComputer.setUsingWebAssembly(requestUsingWebAssembly);
            tracer.setOnFinished(this::takeSnapshotAndStartNextFrameIfComputing);
            benchmarking = requestBenchmark;
            requestBenchmark = false;
            if (benchmarking)
                ctx.clearRect(0, 0, canvasWidth, canvasHeight);
            tracer.setPlaceIndex(placeIndex);
            tracer.setFrameIndex(frameIndex);
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
        int framesCount = pixelComputer.getFramesCount(placeIndex);
        completion = 1.0 * snapshots.size() / framesCount;
        completed = snapshots.size() >= framesCount;
        progressArc.setLength(-360 * completion);
    }

    private void takeSnapshotAndStartNextFrameIfComputing() {
        if (!benchmarking || snapshots.isEmpty()) {
            takeMandelbrotSnapshot();
            startNextFrameIfComputing();
        } else if (requestBenchmark)
            startComputing();
        else
            updateOverlayCanvas();
    }

    private void startNextFrameIfComputing() {
        if (computing) {
            if (!zoomingPaused)
                drawMandelbrot(placeIndex, frameIndex + 1);
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
        totalIterations += pixelComputer.getLastFrameIterations();
        updateCompletion();
        if (completed) {
            stopComputing();
            ctx.drawImage(snapshots.get(0), 0, 0);
            updatePlaceButtonBar();
            playOrStopSnapshots(1);
            Logger.log("totalIterations = " + totalIterations);
        }
    }

    private void drawMandelbrot(int placeIndex, int frameIndex) {
        if (placeIndex != this.placeIndex) {
            stopComputingAndClearSnapshots();
            showPlaceButtonBar();
            updatePlaceButtonBar(); // To refresh pause button for example
            overlayCharactersMax = 1;
            totalIterations = 0;
            benchmarking = requestBenchmark = false;
            this.placeIndex = placeIndex;
        }
        this.frameIndex = frameIndex;
        startComputing();
    }

}
