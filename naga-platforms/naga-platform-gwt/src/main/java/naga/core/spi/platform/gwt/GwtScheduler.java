package naga.core.spi.platform.gwt;

import com.google.gwt.user.client.Timer;
import naga.core.spi.platform.Scheduler;


/**
 * @author Bruno Salmon
 */
final class GwtScheduler implements Scheduler<Timer> {

    @Override
    public void scheduleDeferred(Runnable runnable) {
        scheduleDelay(0, runnable);
    }

    @Override
    public Timer scheduleDelay(long delayMs, Runnable runnable) {
        Timer timer = createTimer(runnable);
        timer.schedule((int) delayMs);
        return timer;
    }

    @Override
    public Timer schedulePeriodic(long delayMs, Runnable runnable) {
        Timer timer = createTimer(runnable);
        timer.scheduleRepeating((int) delayMs);
        return timer;
    }

    @Override
    public boolean cancelTimer(Timer timer) {
        timer.cancel();
        return true;
    }

    private static Timer createTimer(Runnable runnable) {
        return new Timer() {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

    @Override
    public boolean isUiThread() {
        return true;
    }
}
