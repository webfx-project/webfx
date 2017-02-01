package naga.commons.scheduler;

/**
 * @author Bruno Salmon
 */
public interface UiScheduler extends Scheduler {

    boolean isUiThread();

    default void runInUiThread(Runnable runnable) {
        if (isUiThread())
            runnable.run();
        else
            scheduleDeferred(runnable);
    }

    default void runOutUiThread(Runnable runnable) {
        if (!isUiThread())
            runnable.run();
        else
            runInBackground(runnable);
    }

    default Scheduled scheduleAnimationFrame(Runnable runnable) {
        return scheduleAnimationFrame(0, runnable);
    }

    Scheduled scheduleAnimationFrame(long delayMs, Runnable runnable);

    Scheduled schedulePeriodicAnimationFrame(Runnable runnable, boolean isPulse);

    void requestNextPulse();

    boolean isAnimationFrame();

    // Run immediately but isAnimationFrame() returns true -> the layout pass is executed immediately instead of being
    // postponed to the next animation frame. This is can be useful if a node rendering is needed outside the animation
    // frame (for example when rendering a table cell during a repaint triggered by Swing).
    void runLikeAnimationFrame(Runnable runnable);

}
