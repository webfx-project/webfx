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

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Bruno Salmon
 */
final class MandelbrotTracer {

    private int width, height;
    private final Canvas canvas;
    private GraphicsContext ctx;
    private MandelbrotModel model;
    private int threadsCount = Math.max(2, UiScheduler.availableProcessors());
    private int lastThreadsCount;
    private Runnable onFinished;
    private AnimationTimer animationTimer;
    private final AtomicInteger computingThreadsCount = new AtomicInteger();
    private boolean usingWorkers = true, usingWebAssembly = true, lastFrameUsedWebAssembly;
    private final WorkerPool<MandelbrotWorker> workerPool = new WorkerPool<>(MandelbrotWorker.class);
    private long currentFrameIterations, lastFrameIterations;
    private long t0, cumulatedComputationTime, lastFrameComputationTime;
    private int lastComputedLineIndex, readyLinesCount, nextLineToPaintIndex, startNumber;
    private LineComputationInfo[] computingLines, readyLines;

    private static class LineComputationInfo {
        private int cy; // vertical position of the line in the canvas
        private int cx; // used only in mono-thread (ex: browser) to memorize the horizontal position where the computation stopped and the end of the animation frame
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

    public boolean isUsingWebAssembly() {
        return usingWebAssembly;
    }

    public void setUsingWebAssembly(boolean usingWebAssembly) {
        this.usingWebAssembly = usingWebAssembly;
    }

    public boolean wasLastFrameUsingWebAssembly() {
        return lastFrameUsedWebAssembly;
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

    public int getLastThreadsCount() {
        return lastThreadsCount;
    }

    public void start() {
        stop(); // Stopping any previous running computation eventually
        startNumber++;
        width = (int) canvas.getWidth();
        height = (int) canvas.getHeight();
        ctx = canvas.getGraphicsContext2D();
        if (computingLines == null || computingLines.length != height) {
            // Will contain the computing info of each line (ordered vertically)
            computingLines = new LineComputationInfo[height];
            // Same array be will be filled with computed lines ready to be paint (the order may differ depending on their computation time)
            readyLines     = new LineComputationInfo[height];
        }
        readyLinesCount = nextLineToPaintIndex = 0;
        MandelbrotComputation.init();
        cumulatedComputationTime = 0;
        currentFrameIterations = 0;
        t0 = UiScheduler.nanoTime();
        computingThreadsCount.set(threadsCount);
        if (threadsCount > 0) { // Using background thread(s) for the computation
            lastComputedLineIndex = -1;
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
        } else { // Using UI thread for the computation
            lastComputedLineIndex = 0;
            // Using the UI thread to compute and paint the canvas on each animation frame
            animationTimer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    pulseComputingUi(now);
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
        int workerStartNumber = startNumber;
        worker.setOnMessageHandler(data -> {
            // Checking the tracer hasn't been restarted with new parameters meanwhile
            if (workerStartNumber != startNumber) // If this is the case,
                return; // we don't continue this old stuff computation
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
        boolean firstWorkerCall = unit[0] == null;
        int lineIndex = pickNextLineIndexToCompute();
        LineComputationInfo lineComputationInfo = unit[0] = getLineComputationInfo(lineIndex);
        if (lineComputationInfo == null) {
            worker.terminate(); // Will actually put it back into the worker pool
            logIfComplete();
        } else {
            WritableJsonObject json = Json.createObject().set("cy", lineComputationInfo.cy);
            if (firstWorkerCall) {
                // Serializing BigDecimals to Strings in JSON
                json.set("sxmin", model.xmin.toString());
                json.set("sxmax", model.xmax.toString());
                json.set("symin", model.ymin.toString());
                json.set("symax", model.ymax.toString());
                json.set("maxIterations", model.maxIterations);
                json.set("width", width);
                json.set("height", height);
                json.set("wasm", usingWebAssembly);
            }
            worker.postMessage(json);
        }
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

    private void addReadyToPaintLine(LineComputationInfo readyToPaintLine) {
        synchronized (this) {
            readyLines[readyLinesCount++] = readyToPaintLine;
        }
    }

    private void paintReadyLines() { // Must be called by UI thread only
        while (nextLineToPaintIndex < readyLinesCount) {
            LineComputationInfo lci = readyLines[nextLineToPaintIndex];
            int cy = lci.cy;
            for (int cx = 0; cx < lci.pixelIterations.length; cx++) {
                int count = lci.pixelIterations[cx];
                colorizePixel(cx, cy, count);
                currentFrameIterations += count;
            }
            if (++nextLineToPaintIndex == height)
                finish();
        }
    }

    private void colorizePixel(int x, int y, int iteration) {
        Color pixelColor = convertModelPointValueToPixelColor(iteration);
        colorizePixel(x, y, pixelColor);
    }

    private void colorizePixel(int x, int y, Color pixelColor) {
        if (pixelColor != null) { // in multi-pass, null means unchanged color
            ctx.setFill(pixelColor);
            ctx.fillRect(x, y, 1, 1);
        }
    }

    private final static long MILLIS_IN_NANO = 1_000_000;
    private final static long MAX_PULSE_COMPUTATION_TIME_NS = 16 * MILLIS_IN_NANO; // 16ms per frame

    private int pickNextLineIndexToCompute() { // Returning height means they have all been computed
        synchronized (this) {
            return ++lastComputedLineIndex;
        }
    }

    private LineComputationInfo getLineComputationInfo(int lineIndex) {
        if (lineIndex >= height)
            return null;
        LineComputationInfo lineComputationInfo = computingLines[lineIndex];
        if (lineComputationInfo == null) {
            computingLines[lineIndex] = lineComputationInfo = new LineComputationInfo();
            lineComputationInfo.cy = lineIndex;
        }
        return lineComputationInfo;
    }

    private void pulseComputingUi(long now) { // Called by the animation timer every 16ms (60 FPS)
        // Getting the next line index to computed (but for UI thread, we just continue where we stopped last time)
        int lineIndex = lastComputedLineIndex;
        LineComputationInfo lineComputationInfo = getLineComputationInfo(lineIndex);
        while (lineComputationInfo != null) { // non null value until all lines have benn computed
            int cy = lineComputationInfo.cy;
            int cx = lineComputationInfo.cx;
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
        finish();
    }

    private void logIfComplete() {
        // End of the loop = end the job for this thread
        if (computingThreadsCount.decrementAndGet() == 0) { // Was it the last thread to finish?
            // If yes, logging the computation time
            long totalTime = UiScheduler.nanoTime() - t0;
            lastFrameComputationTime = totalTime / MILLIS_IN_NANO;
            lastFrameUsedWebAssembly = usingWebAssembly;
            lastThreadsCount = threadsCount;
            webfx.platform.shared.services.log.Logger.log("Completed in " + lastFrameComputationTime + "ms (computation: " + cumulatedComputationTime / MILLIS_IN_NANO + "ms (" + 100 * cumulatedComputationTime / totalTime + "%) - UI: " + (totalTime - cumulatedComputationTime) / MILLIS_IN_NANO + "ms (" + 100 * (totalTime - cumulatedComputationTime) / totalTime + "%)");
        }
    }

    private void computeInThread() {
        // Getting the next line index to computed (but for UI thread, we just continue where we stopped last time)
        int lineIndex = pickNextLineIndexToCompute();
        LineComputationInfo lineComputationInfo = getLineComputationInfo(lineIndex);
        while (lineComputationInfo != null) { // non null value until all lines have benn computed
            lineComputationInfo.pixelIterations = new int[width];
            int cy = lineComputationInfo.cy;
            int cx = lineComputationInfo.cx;
            while (cx < width) {
                // Passing the canvas pixel for the pixel color computation
                MandelbrotPoint mbp = MandelbrotComputation.convertCanvasPixelToModelPoint(cx, cy, width, height, model);
                int count = MandelbrotComputation.computeModelPointValue(mbp.x, mbp.y, model.maxIterations);
                lineComputationInfo.pixelIterations[cx++] = count;
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
