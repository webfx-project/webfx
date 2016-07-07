package naga.platform.providers.gwt;

import com.google.gwt.user.client.Timer;
import naga.commons.scheduler.spi.Scheduled;
import naga.commons.scheduler.spi.Scheduler;


/**
 * @author Bruno Salmon
 */
final class GwtScheduler implements Scheduler {

    @Override
    public void scheduleDeferred(Runnable runnable) {
        scheduleDelay(0, runnable);
    }

    @Override
    public GwtScheduled scheduleDelay(long delayMs, Runnable runnable) {
        Timer timer = createTimer(runnable);
        timer.schedule((int) delayMs);
        return new GwtScheduled(timer);
    }

    @Override
    public GwtScheduled schedulePeriodic(long delayMs, Runnable runnable) {
        Timer timer = createTimer(runnable);
        timer.scheduleRepeating((int) delayMs);
        return new GwtScheduled(timer);
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

    private static class GwtScheduled implements Scheduled {
        private final Timer gwtTimer;

        private GwtScheduled(Timer gwtTimer) {
            this.gwtTimer = gwtTimer;
        }

        @Override
        public boolean cancel() {
            gwtTimer.cancel();
            return true;
        }
    }
}
