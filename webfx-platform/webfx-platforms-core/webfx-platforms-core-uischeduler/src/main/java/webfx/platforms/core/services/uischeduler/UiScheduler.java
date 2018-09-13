package webfx.platforms.core.services.uischeduler;

import webfx.platforms.core.services.scheduler.Scheduled;
import webfx.platforms.core.services.scheduler.Scheduler;
import webfx.platforms.core.services.scheduler.spi.SchedulerProvider;
import webfx.platforms.core.services.uischeduler.spi.UiSchedulerProvider;
import webfx.platforms.core.util.function.Consumer;
import webfx.platforms.core.util.serviceloader.SingleServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class UiScheduler {

    static {
        SchedulerProvider schedulerProvider = Scheduler.getProvider();
        if (schedulerProvider instanceof UiSchedulerProvider)
            SingleServiceLoader.cacheServiceInstance(UiSchedulerProvider.class, (UiSchedulerProvider) schedulerProvider);
    }

    public static UiSchedulerProvider getProvider() {
        return SingleServiceLoader.loadService(UiSchedulerProvider.class);
    }

    public static boolean isUiThread() {
        return getProvider().isUiThread();
    }

    public static void runInUiThread(Runnable runnable) {
        getProvider().runInUiThread(runnable);
    }

    public static void runOutUiThread(Runnable runnable) {
        getProvider().runOutUiThread(runnable);
    }

    public static Scheduled schedulePropertyChangeInNextAnimationFrame(Runnable propertyChangeTask) {
        return getProvider().schedulePropertyChangeInNextAnimationFrame(propertyChangeTask);
    }

    public static Scheduled scheduleInNextAnimationFrame(Runnable animationTask, AnimationFramePass pass) {
        return getProvider().scheduleInNextAnimationFrame(animationTask, pass);
    }

    public static Scheduled scheduleDelayInAnimationFrame(long delayMs, Runnable animationTask, AnimationFramePass pass) {
        return getProvider().scheduleDelayInAnimationFrame(delayMs, animationTask, pass);
    }

    public static Scheduled scheduleInFutureAnimationFrame(int frameCount, Runnable animationTask, AnimationFramePass pass) {
        return getProvider().scheduleInFutureAnimationFrame(frameCount, animationTask, pass);
    }

    public static Scheduled scheduleDelayInAnimationFrame(long delayMs, Consumer<Scheduled> animationTask, AnimationFramePass pass) {
        return getProvider().scheduleDelayInAnimationFrame(delayMs, animationTask, pass);
    }

    public static Scheduled schedulePeriodicInAnimationFrame(Runnable animationTask, AnimationFramePass pass) {
        return getProvider().schedulePeriodicInAnimationFrame(animationTask, pass);
    }

    public static Scheduled schedulePeriodicInAnimationFrame(Consumer<Scheduled> animationTask, AnimationFramePass pass) {
        return getProvider().schedulePeriodicInAnimationFrame(animationTask, pass);
    }

    public static Scheduled schedulePeriodicInAnimationFrame(long delayMs, Runnable animationTask, AnimationFramePass pass) {
        return getProvider().schedulePeriodicInAnimationFrame(delayMs, animationTask, pass);
    }

    public static void requestNextScenePulse() {
        getProvider().requestNextScenePulse();
    }

    public static boolean isAnimationFrameNow() {
        return getProvider().isAnimationFrameNow();
    }

    // Run immediately but isAnimationFrame() returns true -> the layout pass is executed immediately instead of being
    // postponed to the next animation frame. This is can be useful if a node rendering is needed outside the animation
    // frame (for example when rendering a table cell during a repaint triggered by Swing).
    public static void runLikeInAnimationFrame(Runnable runnable) {
        getProvider().runLikeInAnimationFrame(runnable);
    }


    /**
     * A deferred command is executed not now but as soon as possible (ex: after the event loop returns).
     */
    public static void scheduleDeferred(Runnable runnable) {
        getProvider().scheduleDeferred(runnable);
    }

    /**
     * Set a one-shot timer to fire after {@code delayMs} milliseconds, at which point {@code handler}
     * will be called.
     *
     * @return the timer
     */
    public static Scheduled scheduleDelay(long delayMs, Runnable runnable) {
        return getProvider().scheduleDelay(delayMs, runnable);
    }

    public static Scheduled scheduleDelay(long delayMs, Consumer<Scheduled> runnable) {
        return getProvider().scheduleDelay(delayMs, runnable);
    }

    /**
     * Schedules a repeating handler that is scheduled with a constant periodicity. That is, the
     * handler will be invoked every <code>delayMs</code> milliseconds, regardless of how long the
     * previous invocation took to complete.
     *
     * @param delayMs the period with which the handler is executed
     * @param runnable the handler to execute
     * @return the timer
     */
    public static Scheduled schedulePeriodic(long delayMs, Runnable runnable) {
        return getProvider().schedulePeriodic(delayMs, runnable);
    }

    public static Scheduled schedulePeriodic(long delayMs, Consumer<Scheduled> runnable) {
        return getProvider().schedulePeriodic(delayMs, runnable);
    }

    public static void runInBackground(Runnable runnable) {
        getProvider().runInBackground(runnable);
    }

    public static long nanoTime() { // because System.nanoTime() is not GWT compatible
        return getProvider().nanoTime();
    }

}
