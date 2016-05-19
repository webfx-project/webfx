package naga.core.spi.toolkit.pivot;

import naga.core.spi.platform.Scheduler;
import org.apache.pivot.wtk.ApplicationContext;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class PivotScheduler implements Scheduler<ApplicationContext.ScheduledCallback> {

    public static PivotScheduler SINGLETON = new PivotScheduler();

    private PivotScheduler() {
    }


    @Override
    public void scheduleDeferred(Runnable runnable) {
        ApplicationContext.queueCallback(runnable);
    }

    @Override
    public ApplicationContext.ScheduledCallback scheduleDelay(long delayMs, Runnable runnable) {
        return ApplicationContext.scheduleCallback(runnable, delayMs);
    }

    @Override
    public ApplicationContext.ScheduledCallback schedulePeriodic(long delayMs, Runnable runnable) {
        return ApplicationContext.scheduleRecurringCallback(runnable, delayMs, delayMs);
    }

    @Override
    public boolean cancelTimer(ApplicationContext.ScheduledCallback timer) {
        return timer.cancel();
    }

    @Override
    public boolean isUiThread() {
        return EventQueue.isDispatchThread();
    }
}
