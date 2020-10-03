package webfx.demos.mandelbrot.canvastracer;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import webfx.platform.client.services.uischeduler.UiScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Bruno Salmon
 */
public class CanvasTracer {

    private final Canvas canvas;
    private PixelColorComputer pixelColorComputer;
    private Runnable onFinished;

    public CanvasTracer(Canvas canvas) {
        this.canvas = canvas;
    }

    public CanvasTracer(Canvas canvas, PixelColorComputer pixelColorComputer) {
        this(canvas);
        this.pixelColorComputer = pixelColorComputer;
    }

    public PixelColorComputer getPixelComputer() {
        return pixelColorComputer;
    }

    public void setPixelComputer(PixelColorComputer pixelColorComputer) {
        this.pixelColorComputer = pixelColorComputer;
    }

    public void setOnFinished(Runnable onFinished) {
        this.onFinished = onFinished;
    }

    private double width, height;
    private GraphicsContext ctx;
    private long t0, cumulatedComputationTime;
    private int lastLineIndex;
    private AnimationTimer animationTimer;
    private final AtomicInteger computingThreadsCount = new AtomicInteger();

    private static class LineComputationInfo {
        // recyclableCanvasPixel will be used on mono pass (maxPass == 1) to reduce memory usage
        private final CanvasPixel recyclableCanvasPixel = new CanvasPixel();
        private double cy; // vertical position of the line in the canvas
        private double cx; // used only in mono-thread (ex: browser) to memorize the horizontal position where the computation stopped and the end of the animation frame
        private Color[] pixelColors; // used only in multi-thread to pass the resulting pixel colors to the UI thread for painting
    }

    private LineComputationInfo[] lines;
    private List<LineComputationInfo> readyToPaintLines;

    public void start() {
        stop(); // Stopping any previous running computation eventually
        width = canvas.getWidth();
        height = canvas.getHeight();
        ctx = canvas.getGraphicsContext2D();
        lines = new LineComputationInfo[(int) height];
        readyToPaintLines = new ArrayList<>();
        cumulatedComputationTime = 0;
        t0 = UiScheduler.nanoTime();
        int availableProcessors = UiScheduler.availableProcessors();
        computingThreadsCount.set(availableProcessors);
        if (availableProcessors > 1) { // Multi-threading :)
            lastLineIndex = -1;
            // Starting computation jobs in the background
            for (int i = 1; i <= availableProcessors; i++) // Starting non-UI thread
                UiScheduler.runInBackground(() -> pulseComputation(UiScheduler.nanoTime()));
            // Using the UI thread just to paint ready lines on each animation frame
            animationTimer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    int computingThreadsCount = CanvasTracer.this.computingThreadsCount.get();
                    paintReadyLines();
                    if (computingThreadsCount == 0)
                        finish();
                }
            };
        } else { // Mono-threading (which is the case when running in the browser)
            lastLineIndex = 0;
            // Using the UI thread to compute and paint the canvas on each animation frame
            animationTimer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    pulseComputation(now);
                    if (computingThreadsCount.get() == 0)
                        finish();
                }
            };
        }
        animationTimer.start();
        webfx.platform.shared.services.log.Logger.log("Started UI thread + " + (availableProcessors == 1 ? 0 : availableProcessors) + " background thread(s)");
    }

    public void stop() {
        if (animationTimer != null)
            animationTimer.stop();
    }

    private void finish() {
        stop();
        if (onFinished != null)
            onFinished.run();
    }

    private List<LineComputationInfo> getReadyToPaintLines() {
        synchronized (this) {
            List<LineComputationInfo> lineInfos = readyToPaintLines;
            readyToPaintLines = new ArrayList<>();
            return lineInfos;
        }
    }

    private void addReadyToPaintLine(LineComputationInfo readyToPaintLine) {
        synchronized (this) {
            readyToPaintLines.add(readyToPaintLine);
        }
    }

    private void paintReadyLines() { // Must be called by UI thread only
        getReadyToPaintLines().forEach(lci -> {
            double cy = lci.cy;
            for (int cx = 0; cx < lci.pixelColors.length; cx++)
                colorizePixel(cx, cy, lci.pixelColors[cx]);
        });
    }

    private void colorizePixel(double x, double y, Color pixelColor) {
        if (pixelColor != null) { // in multi-pass, null means unchanged color
            ctx.setFill(pixelColor);
            ctx.fillRect(x, y, 1, 1);
        }
    }

    private final static long MILLIS_IN_NANO = 1_000_000;
    private final static long MAX_PULSE_COMPUTATION_TIME_NS = 16 * MILLIS_IN_NANO; // 16ms per frame

    private int pickNextLineIndexToCompute() { // Returning height means they have all been computed
        synchronized (this) {
            return ++lastLineIndex;
        }
    }

    private LineComputationInfo getLineComputationInfo(int lineIndex, boolean uiThread) {
        if (lineIndex >= height)
            return null;
        LineComputationInfo lineComputationInfo = lines[lineIndex];
        if (lineComputationInfo == null) {
            lines[lineIndex] = lineComputationInfo = new LineComputationInfo();
            lineComputationInfo.cy = lineIndex;
            lineComputationInfo.pixelColors = uiThread ? null : new Color[(int) width];
        }
        return lineComputationInfo;
    }

    private void pulseComputation(long now) { // Called by the animation timer every 16ms (60 FPS)
        boolean uiThread = UiScheduler.isUiThread();
        // Getting the next line index to computed (but for UI thread, we just continue where we stopped last time)
        int lineIndex = uiThread ? lastLineIndex : pickNextLineIndexToCompute();
        LineComputationInfo lineComputationInfo = getLineComputationInfo(lineIndex, uiThread);
        while (lineComputationInfo != null) { // non null value until all lines have benn computed
            double cy = lineComputationInfo.cy;
            double cx = lineComputationInfo.cx;
            CanvasPixel cp = lineComputationInfo.recyclableCanvasPixel; // To be used for mono-pass only (will be overridden in multi-pass)
            while (cx < width) {
                cp.set(cx, cy, width, height);
                // Passing the canvas pixel for the pixel color computation
                Color pixelColor = pixelColorComputer.computePixelColor(cp);
                if (!uiThread) // if background thread, passing the result to the UI thread through pixelColors array to be painted in the next animation frame
                    lineComputationInfo.pixelColors[(int) cx++] = pixelColor;
                else { // if ui thread (=> in animation frame), we paint the pixel right now
                    colorizePixel(cx++, cy, pixelColor);
                    // Also checking if the computation time doesn't exceed the frame max time
                    long computationTime = UiScheduler.nanoTime() - now;
                    if (computationTime > MAX_PULSE_COMPUTATION_TIME_NS) {
                        cumulatedComputationTime += computationTime;
                        // Memorizing the horizontal position so we can resume just where we stopped on next call
                        lineComputationInfo.cx = cx;
                        return; // End of this frame, the method will be called back for the next
                    }
                }
            }
            if (!uiThread) // Once all colors of the line have been computed, we pass them to the UI thread
                addReadyToPaintLine(lineComputationInfo);
            // Picking up the next line to compute (will return null if all done)
            lineComputationInfo = getLineComputationInfo(pickNextLineIndexToCompute(), uiThread);
        }
        // End of the loop = end the job for this thread
        if (computingThreadsCount.decrementAndGet() == 0) { // Was it the last thread to finish?
            // If yes, logging the computation time
            long totalTime = UiScheduler.nanoTime() - t0;
            webfx.platform.shared.services.log.Logger.log("Completed in " + totalTime / MILLIS_IN_NANO + "ms (computation: " + cumulatedComputationTime / MILLIS_IN_NANO + "ms (" + 100 * cumulatedComputationTime / totalTime + "%) - UI: " + (totalTime - cumulatedComputationTime) / MILLIS_IN_NANO + "ms (" + 100 * (totalTime - cumulatedComputationTime) / totalTime + "%)");
        }
    }
}
