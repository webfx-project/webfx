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

    default Scheduled scheduleAnimationFrame(Runnable runnable) {
        return scheduleAnimationFrame(0, runnable);
    }

    Scheduled scheduleAnimationFrame(long delayMs, Runnable runnable);

    Scheduled schedulePeriodicAnimationFrame(Runnable runnable);

}
