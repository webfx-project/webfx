package webfx.demos.mandelbrot.canvastracer;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import webfx.demos.mandelbrot.computation.MandelbrotComputation;
import webfx.demos.mandelbrot.computation.MandelbrotPoint;
import webfx.demos.mandelbrot.worker.MandelbrotWorker;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.shared.services.json.Json;
import webfx.platform.shared.services.json.WritableJsonArray;
import webfx.platform.shared.services.json.WritableJsonObject;
import webfx.platform.shared.services.worker.Worker;
import webfx.platform.shared.services.worker.WorkerService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Bruno Salmon
 */
public class MandelbrotTracer {

    private final Canvas canvas;
    private MandelbrotModel model;
    private Runnable onFinished;

    public MandelbrotTracer(Canvas canvas) {
        this.canvas = canvas;
    }

    public void setModel(MandelbrotModel model) {
        this.model = model;
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
    private final static boolean useWorkers = true;
    private int currentFrameIterations;
    private int lastFrameIterations;
    private long lastFrameComputationTime;

    private static class LineComputationInfo {
        private double cy; // vertical position of the line in the canvas
        private double cx; // used only in mono-thread (ex: browser) to memorize the horizontal position where the computation stopped and the end of the animation frame
        private int[] pixelIterations;
        //private Color[] pixelColors; // used only in multi-thread to pass the resulting pixel colors to the UI thread for painting
    }

    private LineComputationInfo[] lines;
    private List<LineComputationInfo> readyToPaintLines;

    public int getLastFrameIterations() {
        return lastFrameIterations;
    }

    public long getComputedThreadCount() {
        return computingThreadsCount.get();
    }

    public long getLastFrameComputationTime() {
        return lastFrameComputationTime;
    }

    public void start() {
        stop(); // Stopping any previous running computation eventually
        width = canvas.getWidth();
        height = canvas.getHeight();
        ctx = canvas.getGraphicsContext2D();
        lines = new LineComputationInfo[(int) height];
        readyToPaintLines = new ArrayList<>();
        MandelbrotComputation.init();
        cumulatedComputationTime = 0;
        currentFrameIterations = 0;
        t0 = UiScheduler.nanoTime();
        int availableProcessors = UiScheduler.availableProcessors();
        int threadCounts = Math.max(1, availableProcessors - 1);
        computingThreadsCount.set(threadCounts);
        if (availableProcessors > 1) { // Multi-threading :)
            lastLineIndex = -1;
            // Starting computation jobs in the background
            for (int i = 1; i <= threadCounts; i++) // Starting non-UI thread
                if (useWorkers)
                    startComputingWorker();
                else
                    startComputingThread();
            // Using the UI thread just to paint ready lines on each animation frame
            animationTimer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    int computingThreadsCount = MandelbrotTracer.this.computingThreadsCount.get();
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
                    computeInUi(now);
                    if (computingThreadsCount.get() == 0)
                        finish();
                }
            };
        }
        animationTimer.start();
        webfx.platform.shared.services.log.Logger.log("Started UI thread + " + (threadCounts == 1 ? 0 : threadCounts) + " background thread(s)");
    }

    private void startComputingThread() {
        UiScheduler.runInBackground(this::computeInThread);
    }

    private void startComputingWorker() {
        Worker worker = WorkerService.createWorker(MandelbrotWorker.class);
        LineComputationInfo[] unit = new LineComputationInfo[1];
        worker.setOnMessageHandler(data -> {
            int[] values;
            if (data instanceof int[])
                values = (int[]) data;
            else {
                WritableJsonArray array = Json.createArray(data);
                int n = array.size();
                values = new int[n];
                for (int i = 0; i < n; i++)
                    values[i] = array.getInteger(i);
            }
            LineComputationInfo lineComputationInfo = unit[0];
            lineComputationInfo.pixelIterations = values;
            startComputingLineWorker(worker, unit);
            addReadyToPaintLine(lineComputationInfo);
        });
        startComputingLineWorker(worker, unit);
    }

    private void startComputingLineWorker(Worker worker, LineComputationInfo[] unit) {
        int lineIndex = pickNextLineIndexToCompute();
        LineComputationInfo lineComputationInfo = getLineComputationInfo(lineIndex);
        if (lineComputationInfo == null) {
            worker.terminate();
            logIfComplete();
        } else {
            WritableJsonObject json = Json.createObject().set("cy", lineComputationInfo.cy);
            if (unit[0] == null) {
                json.set("xmin", model.xmin.doubleValue());
                json.set("xmax", model.xmax.doubleValue());
                json.set("ymin", model.ymin.doubleValue());
                json.set("ymax", model.ymax.doubleValue());
                json.set("maxIterations", model.maxIterations);
                json.set("width", width);
                json.set("height", height);
            }
            worker.postMessage(json);
        }
        unit[0] = lineComputationInfo;
    }

    public void stop() {
        if (isRunning()) {
            animationTimer.stop();
            animationTimer = null;
        }
    }

    public boolean isRunning() {
        return animationTimer != null;
    }

    private void finish() {
        stop();
        lastFrameIterations = currentFrameIterations;
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
            for (int cx = 0; cx < lci.pixelIterations.length; cx++) {
                int count = lci.pixelIterations[cx];
                colorizePixel(cx, cy, count);
                currentFrameIterations += count;
            }
        });
    }

    private void colorizePixel(double x, double y, int iteration) {
        Color pixelColor = convertModelPointValueToPixelColor(iteration);
        colorizePixel(x, y, pixelColor);
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

    private LineComputationInfo getLineComputationInfo(int lineIndex) {
        if (lineIndex >= height)
            return null;
        LineComputationInfo lineComputationInfo = lines[lineIndex];
        if (lineComputationInfo == null) {
            lines[lineIndex] = lineComputationInfo = new LineComputationInfo();
            lineComputationInfo.cy = lineIndex;
        }
        return lineComputationInfo;
    }

    private void computeInUi(long now) { // Called by the animation timer every 16ms (60 FPS)
        // Getting the next line index to computed (but for UI thread, we just continue where we stopped last time)
        int lineIndex = lastLineIndex;
        LineComputationInfo lineComputationInfo = getLineComputationInfo(lineIndex);
        while (lineComputationInfo != null) { // non null value until all lines have benn computed
            double cy = lineComputationInfo.cy;
            double cx = lineComputationInfo.cx;
            while (cx < width) {
                MandelbrotPoint mbp = MandelbrotComputation.convertCanvasPixelToModelPoint(cx, cy, width, height, model);
                int count = MandelbrotComputation.computeModelPointValue(mbp.x, mbp.y, model.maxIterations);
                currentFrameIterations += count;
                // if ui thread (=> in animation frame), we paint the pixel right now
                colorizePixel(cx++, cy, count);
                // Also checking if the computation time doesn't exceed the frame max time
                long computationTime = UiScheduler.nanoTime() - now;
                if (computationTime > MAX_PULSE_COMPUTATION_TIME_NS) {
                    cumulatedComputationTime += computationTime;
                    // Memorizing the horizontal position so we can resume just where we stopped on next call
                    lineComputationInfo.cx = cx;
                    return; // End of this frame, the method will be called back for the next
                }
            }
            // Picking up the next line to compute (will return null if all done)
            lineComputationInfo = getLineComputationInfo(pickNextLineIndexToCompute());
        }
        logIfComplete();
    }

    private void logIfComplete() {
        // End of the loop = end the job for this thread
        if (computingThreadsCount.decrementAndGet() == 0) { // Was it the last thread to finish?
            // If yes, logging the computation time
            long totalTime = UiScheduler.nanoTime() - t0;
            lastFrameComputationTime = totalTime / MILLIS_IN_NANO;
            webfx.platform.shared.services.log.Logger.log("Completed in " + lastFrameComputationTime + "ms (computation: " + cumulatedComputationTime / MILLIS_IN_NANO + "ms (" + 100 * cumulatedComputationTime / totalTime + "%) - UI: " + (totalTime - cumulatedComputationTime) / MILLIS_IN_NANO + "ms (" + 100 * (totalTime - cumulatedComputationTime) / totalTime + "%)");
        }
    }

    private void computeInThread() {
        // Getting the next line index to computed (but for UI thread, we just continue where we stopped last time)
        int lineIndex = pickNextLineIndexToCompute();
        LineComputationInfo lineComputationInfo = getLineComputationInfo(lineIndex);
        while (lineComputationInfo != null) { // non null value until all lines have benn computed
            lineComputationInfo.pixelIterations = new int[(int) width];
            double cy = lineComputationInfo.cy;
            double cx = lineComputationInfo.cx;
            while (cx < width) {
                // Passing the canvas pixel for the pixel color computation
                MandelbrotPoint mbp = MandelbrotComputation.convertCanvasPixelToModelPoint(cx, cy, width, height, model);
                int count = MandelbrotComputation.computeModelPointValue(mbp.x, mbp.y, model.maxIterations);
                lineComputationInfo.pixelIterations[(int) cx++] = count;
            }
            // Once all colors of the line have been computed, we pass them to the UI thread
            addReadyToPaintLine(lineComputationInfo);
            // Picking up the next line to compute (will return null if all done)
            lineComputationInfo = getLineComputationInfo(pickNextLineIndexToCompute());
        }
        logIfComplete();
    }

    private Color convertModelPointValueToPixelColor(int count) {
        if (model.paletteColors == null) {
            int offset = model.paletteMapping.getOffset();
            int length = model.paletteMapping.getLength();
            if (length == 0)
                length = model.maxIterations;
            model.paletteColors = model.palette.makeRGBs(length, offset);
        }

        Color color;
        if (count == model.maxIterations)
            color = model.mandelbrotColor;
        else
            color = model.paletteColors[count % model.paletteColors.length];

        return color;
    }
}
