package dev.webfx.kit.mapper.peers.javafxmedia.spi.gwt;

import java.util.function.Supplier;

/**
 * @author Bruno Salmon
 */
public final class StopWatch {

    private final Supplier<Long> systemTimeGetter;
    private boolean running;
    private long systemTimeAtStart;
    private long stopWatchElapsedTimeAtPause;
    private long stopWatchCumulativePauseDuration;

    public StopWatch(Supplier<Long> systemTimeGetter) {
        this.systemTimeGetter = systemTimeGetter;
    }

    public static StopWatch createSystemNanoStopWatch() {
        return new StopWatch(System::nanoTime);
    }

    public static StopWatch createSystemMillisStopWatch() {
        return new StopWatch(System::currentTimeMillis);
    }

    public long getSystemTime() {
        return systemTimeGetter.get();
    }

    public long getSystemTimeAtStart() {
        return systemTimeAtStart;
    }

    public long getSystemElapsedTime() {
        return getSystemTime() - getSystemTimeAtStart();
    }

    public long getStopWatchElapsedTime() {
        return isPaused() ? stopWatchElapsedTimeAtPause : getSystemElapsedTime() - stopWatchCumulativePauseDuration;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isPaused() {
        return !isRunning();
    }

    public void start() {
        startAt(0);
    }

    public void startAt(long initialElapsedTime) {
        systemTimeAtStart = getSystemTime() - initialElapsedTime;
        stopWatchCumulativePauseDuration = 0;
        running = true;
    }

    public void pause() {
        if (isRunning()) {
            stopWatchElapsedTimeAtPause = getStopWatchElapsedTime();
            running = false;
        }
    }

    public void resume() {
        if (isPaused()) {
            running = true;
            stopWatchCumulativePauseDuration += getStopWatchElapsedTime() - stopWatchElapsedTimeAtPause;
        }
    }

    public void restart() {
        start();
    }

    public void togglePause() {
        if (isPaused())
            resume();
        else
            pause();
    }

}
