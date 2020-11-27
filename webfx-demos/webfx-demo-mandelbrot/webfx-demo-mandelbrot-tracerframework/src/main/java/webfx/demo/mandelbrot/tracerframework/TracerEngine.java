package webfx.demo.mandelbrot.tracerframework;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.shared.services.webworker.WebWorker;
import webfx.platform.shared.services.webworker.pool.WebWorkerPool;
import webfx.platform.shared.services.webworker.spi.base.JavaCodedWebWorkerBase;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Bruno Salmon
 */
public final class TracerEngine {

    private int width, height;
    private final Canvas canvas;
    private GraphicsContext ctx;
    private final PixelComputer pixelComputer;
    private int threadsCount = Math.max(2, UiScheduler.availableProcessors());
    private int lastThreadsCount;
    private Runnable onFinished;
    private AnimationTimer animationTimer;
    private final AtomicInteger computingThreadsCount = new AtomicInteger();
    private final boolean usingWorkers;
    private boolean lastFrameUsedWebAssembly;
    private final WebWorkerPool webWorkerPool;
    private long t0, cumulatedComputationTime, lastFrameComputationTime;
    private int lastComputedLineIndex, readyLinesCount, nextLineToPaintIndex, startNumber;
    private LineComputationInfo[] computingLines, readyLines;
    private int placeIndex, frameIndex;

    public TracerEngine(Canvas canvas, PixelComputer pixelComputer) {
        this.canvas = canvas;
        this.pixelComputer = pixelComputer;
        Class<? extends JavaCodedWebWorkerBase> workerClass = pixelComputer.getWorkerClass();
        usingWorkers = workerClass != null;
        webWorkerPool = usingWorkers ? new WebWorkerPool(workerClass) : null;
    }

    public int getPlaceIndex() {
        return placeIndex;
    }

    public void setPlaceIndex(int placeIndex) {
        this.placeIndex = placeIndex;
    }

    public int getFrameIndex() {
        return frameIndex;
    }

    public void setFrameIndex(int frameIndex) {
        this.frameIndex = frameIndex;
    }

    public int getThreadsCount() {
        return threadsCount;
    }

    public void setThreadsCount(int threadsCount) {
        this.threadsCount = threadsCount;
    }

    public boolean wasLastFrameUsingWebAssembly() {
        return lastFrameUsedWebAssembly;
    }

    public void setOnFinished(Runnable onFinished) {
        this.onFinished = onFinished;
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
        width  = (int) canvas.getWidth();
        height = (int) canvas.getHeight();
        ctx = canvas.getGraphicsContext2D();
        if (computingLines == null || computingLines.length != height) {
            // Will contain the computing info of each line (ordered vertically)
            computingLines = new LineComputationInfo[height];
            // Same array be will be filled with computed lines ready to be paint (the order may differ depending on their computation time)
            readyLines     = new LineComputationInfo[height];
        }
        readyLinesCount = nextLineToPaintIndex = 0;
        cumulatedComputationTime = 0;
        pixelComputer.initFrame(width, height, placeIndex, frameIndex);
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
        WebWorker webWorker = webWorkerPool.getWorker();
        LineComputationInfo[] unit = new LineComputationInfo[1];
        int workerStartNumber = startNumber;
        webWorker.setOnMessageHandler(data -> {
            // Checking the tracer hasn't been restarted with new parameters meanwhile
            if (workerStartNumber != startNumber) // If this is the case,
                return; // we don't continue this old stuff computation
            LineComputationInfo lineComputationInfo = unit[0];
            lineComputationInfo.linePixelResultStorage = pixelComputer.getLinePixelResultStorage(data);
            startComputingLineWorker(webWorker, unit);
            addReadyToPaintLine(lineComputationInfo);
        });
        startComputingLineWorker(webWorker, unit);
    }

    private void startComputingLineWorker(WebWorker webWorker, LineComputationInfo[] unit) {
        boolean firstWorkerCall = unit[0] == null;
        int lineIndex = pickNextLineIndexToCompute();
        LineComputationInfo lineComputationInfo = unit[0] = getLineComputationInfo(lineIndex);
        if (lineComputationInfo == null) {
            webWorker.terminate(); // Will actually put it back into the webWorker pool
            logIfComplete();
        } else
            webWorker.postMessage(pixelComputer.getLineWorkerParameters(lineComputationInfo.cy, firstWorkerCall));
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
        pixelComputer.endFrame();
        lastFrameUsedWebAssembly = pixelComputer.isUsingWebAssembly();
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
            for (int cx = 0; cx < width; cx++) {
                Color pixelColor = pixelComputer.getPixelResultColor(cx, cy, lci.linePixelResultStorage);
                colorizePixel(cx, cy, pixelColor);
            }
            if (++nextLineToPaintIndex == height)
                finish();
        }
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
            lineComputationInfo.linePixelResultStorage = pixelComputer.createLinePixelResultStorage();
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
                pixelComputer.computeAndStorePixelResult(cx, cy, lineComputationInfo.linePixelResultStorage);
                // if ui thread (=> in animation frame), we paint the pixel right now
                Color pixelColor = pixelComputer.getPixelResultColor(cx, cy, lineComputationInfo.linePixelResultStorage);
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
            // Picking up the next line to compute (will return null if all done)
            lineComputationInfo = getLineComputationInfo(pickNextLineIndexToCompute());
            if (lineComputationInfo != null)
                lineComputationInfo.cx = 0;
        }
        logIfComplete();
        finish();
    }

    private void logIfComplete() {
        // End of the loop = end the job for this thread
        if (computingThreadsCount.decrementAndGet() <= 0) { // Was it the last thread to finish?
            // If yes, logging the computation time
            long totalTime = UiScheduler.nanoTime() - t0;
            lastFrameComputationTime = totalTime / MILLIS_IN_NANO;
            lastThreadsCount = threadsCount;
            webfx.platform.shared.services.log.Logger.log("Completed in " + lastFrameComputationTime + "ms (computation: " + cumulatedComputationTime / MILLIS_IN_NANO + "ms (" + 100 * cumulatedComputationTime / totalTime + "%) - UI: " + (totalTime - cumulatedComputationTime) / MILLIS_IN_NANO + "ms (" + 100 * (totalTime - cumulatedComputationTime) / totalTime + "%)");
        }
    }

    private void computeInThread() {
        pixelComputer.initFrame(width, height, placeIndex, frameIndex);
        // Getting the next line index to computed (but for UI thread, we just continue where we stopped last time)
        int lineIndex = pickNextLineIndexToCompute();
        LineComputationInfo lineComputationInfo = getLineComputationInfo(lineIndex);
        while (lineComputationInfo != null) { // non null value until all lines have benn computed
            int cy = lineComputationInfo.cy;
            int cx = lineComputationInfo.cx;
            while (cx < width)
                pixelComputer.computeAndStorePixelResult(cx++, cy, lineComputationInfo.linePixelResultStorage);
            // Once all colors of the line have been computed, we pass them to the UI thread
            addReadyToPaintLine(lineComputationInfo);
            // Picking up the next line to compute (will return null if all done)
            lineComputationInfo = getLineComputationInfo(pickNextLineIndexToCompute());
        }
        logIfComplete();
    }
}
