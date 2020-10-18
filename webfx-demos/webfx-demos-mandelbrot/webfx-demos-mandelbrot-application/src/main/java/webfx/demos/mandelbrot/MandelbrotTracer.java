package webfx.demos.mandelbrot;

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
import webfx.platform.shared.services.worker.pool.WorkerPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Bruno Salmon
 */
final class MandelbrotTracer {

    private static final int availableProcessors = UiScheduler.availableProcessors();

    private double width, height;
    private final Canvas canvas;
    private GraphicsContext ctx;
    private MandelbrotModel model;
    private int threadsCount = availableProcessors == -1 ? 2 : Math.max(1, availableProcessors - 1);
    private Runnable onFinished;
    private AnimationTimer animationTimer;
    private final AtomicInteger computingThreadsCount = new AtomicInteger();
    private boolean usingWorkers = true;
    private final WorkerPool<MandelbrotWorker> workerPool = new WorkerPool<>(MandelbrotWorker.class);
    private long currentFrameIterations, lastFrameIterations;
    private long t0, cumulatedComputationTime, lastFrameComputationTime;
    private int lastLineIndex;
    private LineComputationInfo[] lines;
    private List<LineComputationInfo> readyToPaintLines;

    private static class LineComputationInfo {
        private double cy; // vertical position of the line in the canvas
        private double cx; // used only in mono-thread (ex: browser) to memorize the horizontal position where the computation stopped and the end of the animation frame
        private int[] pixelIterations;
    }

    public MandelbrotTracer(Canvas canvas) {
        this.canvas = canvas;
    }

    public void setModel(MandelbrotModel model) {
        this.model = model;
    }

    public MandelbrotModel getModel() {
        return model;
    }

    public int getThreadsCount() {
        return threadsCount;
    }

    public void setThreadsCount(int threadsCount) {
        this.threadsCount = threadsCount;
    }

    public boolean isUsingWorkers() {
        return usingWorkers;
    }

    public void setUsingWorkers(boolean usingWorkers) {
        this.usingWorkers = usingWorkers;
    }

    public void setOnFinished(Runnable onFinished) {
        this.onFinished = onFinished;
    }

    public long getLastFrameIterations() {
        return lastFrameIterations;
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
        computingThreadsCount.set(threadsCount);
        if (availableProcessors > 1) { // Multi-threading :)
            lastLineIndex = -1;
            // Starting computation jobs in the background
            for (int i = 1; i <= threadsCount; i++) // Starting non-UI thread
                if (usingWorkers)
                    startComputingUsingWorker();
                else
                    startComputingUsingThread();
            // Using the UI thread just to paint ready lines on each animation frame
            animationTimer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    paintReadyLines();
                }
            };
        } else { // Mono-threading
            lastLineIndex = 0;
            // Using the UI thread to compute and paint the canvas on each animation frame
            animationTimer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    pulseComputingUi(now);
                    if (computingThreadsCount.get() == 0)
                        finish();
                }
            };
        }
        animationTimer.start();
        //webfx.platform.shared.services.log.Logger.log("Started UI thread + " + (threadCounts == 1 ? 0 : threadCounts) + " background thread(s)");
    }

    private void startComputingUsingThread() {
        UiScheduler.runInBackground(this::computeInThread);
    }

    private void startComputingUsingWorker() {
        Worker worker = workerPool.getWorker();
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
            worker.terminate(); // Will actually put it back into the worker pool
            logIfComplete();
        } else {
            WritableJsonObject json = Json.createObject().set("cy", lineComputationInfo.cy);
            if (unit[0] == null) {
                // Serializing BigDecimals to Strings in JSON
                json.set("sxmin", model.xmin.toString());
                json.set("sxmax", model.xmax.toString());
                json.set("symin", model.ymin.toString());
                json.set("symax", model.ymax.toString());
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
            if (cy == height - 1)
                finish();
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

    private void pulseComputingUi(long now) { // Called by the animation timer every 16ms (60 FPS)
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
