package naga.toolkit.providers.pivot;

import naga.commons.scheduler.spi.Scheduled;
import naga.commons.scheduler.spi.Scheduler;
import org.apache.pivot.wtk.ApplicationContext;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class PivotScheduler implements Scheduler {

    public static PivotScheduler SINGLETON = new PivotScheduler();

    private PivotScheduler() {
    }

    @Override
    public void scheduleDeferred(Runnable runnable) {
        ApplicationContext.queueCallback(runnable);
    }

    @Override
    public PivotScheduled scheduleDelay(long delayMs, Runnable runnable) {
        return new PivotScheduled(ApplicationContext.scheduleCallback(runnable, delayMs));
    }

    @Override
    public PivotScheduled schedulePeriodic(long delayMs, Runnable runnable) {
        return new PivotScheduled(ApplicationContext.scheduleRecurringCallback(runnable, delayMs, delayMs));
    }

    @Override
    public boolean isUiThread() {
        return EventQueue.isDispatchThread();
    }

    private static class PivotScheduled implements Scheduled {
        private final ApplicationContext.ScheduledCallback scheduledCallback;

        private PivotScheduled(ApplicationContext.ScheduledCallback scheduledCallback) {
            this.scheduledCallback = scheduledCallback;
        }

        @Override
        public boolean cancel() {
            return scheduledCallback.cancel();
        }
    }
}
