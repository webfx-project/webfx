package dev.webfx.platform.client.services.uischeduler;

import dev.webfx.platform.client.services.uischeduler.spi.UiSchedulerProvider;
import dev.webfx.platform.shared.services.scheduler.Scheduled;
import dev.webfx.platform.shared.services.scheduler.Scheduler;
import dev.webfx.platform.shared.services.scheduler.spi.SchedulerProvider;
import dev.webfx.platform.shared.util.serviceloader.SingleServiceProvider;

import java.util.ServiceLoader;
import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public final class UiScheduler extends Scheduler {

    static {
        SchedulerProvider schedulerProvider = Scheduler.getProvider();
        if (schedulerProvider instanceof UiSchedulerProvider)
            SingleServiceProvider.registerServiceProvider(UiSchedulerProvider.class, (UiSchedulerProvider) schedulerProvider);
    }

    public static UiSchedulerProvider getProvider() {
        return SingleServiceProvider.getProvider(UiSchedulerProvider.class, () -> ServiceLoader.load(UiSchedulerProvider.class));
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

    public static void requestNextScenePulse() {
        getProvider().requestNextScenePulse();
    }

    public static boolean isAnimationFrameNow() {
        return getProvider().isAnimationFrameNow();
    }

    public static Scheduled scheduleDelayInAnimationFrame(long delayMs, Runnable animationTask, int afterFrameCount, AnimationFramePass pass) {
        return getProvider().scheduleDelayInAnimationFrame(delayMs, animationTask, afterFrameCount, pass);
    }

    public static Scheduled scheduleDelayInAnimationFrame(long delayMs, Runnable animationTask, AnimationFramePass pass) {
        return getProvider().scheduleDelayInAnimationFrame(delayMs, animationTask, pass);
    }

    public static Scheduled scheduleDelayInFutureAnimationFrame(long delayMs, Runnable animationTask, AnimationFramePass pass) {
        return getProvider().scheduleDelayInFutureAnimationFrame(delayMs, animationTask, pass);
    }

    public static Scheduled scheduleInAnimationFrame(Runnable animationTask, int afterFrameCount, AnimationFramePass pass) {
        return getProvider().scheduleInAnimationFrame(animationTask, afterFrameCount, pass);
    }

    public static Scheduled scheduleInAnimationFrame(Runnable animationTask, AnimationFramePass pass) {
        return getProvider().scheduleInAnimationFrame(animationTask, pass);
    }

    public static Scheduled scheduleInFutureAnimationFrame(Runnable animationTask, AnimationFramePass pass) {
        return getProvider().scheduleInFutureAnimationFrame(animationTask, pass);
    }


    public static Scheduled schedulePropertyChangeInAnimationFrame(Runnable propertyChangeTask) {
        return getProvider().schedulePropertyChangeInAnimationFrame(propertyChangeTask);
    }

    public static Scheduled schedulePeriodicInAnimationFrame(long delayMs, Runnable animationTask, AnimationFramePass pass) {
        return getProvider().schedulePeriodicInAnimationFrame(delayMs, animationTask, pass);
    }

    public static Scheduled schedulePeriodicInAnimationFrame(Runnable animationTask, AnimationFramePass pass) {
        return getProvider().schedulePeriodicInAnimationFrame(animationTask, pass);
    }

    public static Scheduled schedulePeriodicInAnimationFrame(long delayMs, Consumer<Scheduled> animationTask, AnimationFramePass pass) {
        return getProvider().schedulePeriodicInAnimationFrame(delayMs, animationTask, pass);
    }


    public static Scheduled schedulePeriodicInAnimationFrame(Consumer<Scheduled> animationTask, AnimationFramePass pass) {
        return getProvider().schedulePeriodicInAnimationFrame(animationTask, pass);
    }

    public static Scheduled scheduleDelayInAnimationFrame(long delayMs, Runnable animationTask, int afterFrameCount) {
        return getProvider().scheduleDelayInAnimationFrame(delayMs, animationTask, afterFrameCount);
    }

    public static Scheduled scheduleDelayInAnimationFrame(long delayMs, Runnable animationTask) {
        return getProvider().scheduleDelayInAnimationFrame(delayMs, animationTask);
    }

    public static Scheduled scheduleDelayInFutureAnimationFrame(long delayMs, Runnable animationTask) {
        return getProvider().scheduleDelayInFutureAnimationFrame(delayMs, animationTask);
    }

    public static Scheduled scheduleInAnimationFrame(Runnable animationTask, int afterFrameCount) {
        return getProvider().scheduleInAnimationFrame(animationTask, afterFrameCount);
    }

    public static Scheduled scheduleInAnimationFrame(Runnable animationTask) {
        return getProvider().scheduleInAnimationFrame(animationTask);
    }

    public static Scheduled scheduleInFutureAnimationFrame(Runnable animationTask) {
        return getProvider().scheduleInFutureAnimationFrame(animationTask);
    }

    public static Scheduled schedulePeriodicInAnimationFrame(long delayMs, Runnable animationTask) {
        return getProvider().schedulePeriodicInAnimationFrame(delayMs, animationTask);
    }

    public static Scheduled schedulePeriodicInAnimationFrame(Runnable animationTask) {
        return getProvider().schedulePeriodicInAnimationFrame(animationTask);
    }

    public static Scheduled schedulePeriodicInAnimationFrame(long delayMs, Consumer<Scheduled> animationTask) {
        return getProvider().schedulePeriodicInAnimationFrame(delayMs, animationTask);
    }

    public static Scheduled schedulePeriodicInAnimationFrame(Consumer<Scheduled> animationTask) {
        return getProvider().schedulePeriodicInAnimationFrame(animationTask);
    }


    // Repeating Scheduler API

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
