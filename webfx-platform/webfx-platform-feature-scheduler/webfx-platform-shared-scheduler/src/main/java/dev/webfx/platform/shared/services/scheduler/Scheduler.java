package dev.webfx.platform.shared.services.scheduler;

import dev.webfx.platform.shared.services.scheduler.spi.SchedulerProvider;
import dev.webfx.platform.shared.util.serviceloader.SingleServiceProvider;

import java.util.ServiceLoader;
import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public class Scheduler {

    public static SchedulerProvider getProvider() {
        return SingleServiceProvider.getProvider(SchedulerProvider.class, () -> ServiceLoader.load(SchedulerProvider.class));
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

    public static int availableProcessors() {
        return getProvider().availableProcessors();
    }

}
