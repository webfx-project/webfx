package naga.toolkit.providers.swing;

import naga.commons.scheduler.spi.Scheduled;
import naga.commons.scheduler.spi.Scheduler;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingScheduler implements Scheduler {

    public static SwingScheduler SINGLETON = new SwingScheduler();

    private SwingScheduler() {
    }

    @Override
    public void scheduleDeferred(Runnable runnable) {
        SwingUtilities.invokeLater(runnable);
    }

    @Override
    public SwingScheduled scheduleDelay(long delayMs, Runnable runnable) {
        Timer timer = new Timer((int) delayMs, e -> runnable.run());
        timer.setRepeats(false);
        timer.start();
        return new SwingScheduled(timer);
    }

    @Override
    public SwingScheduled schedulePeriodic(long delayMs, Runnable runnable) {
        Timer timer = new Timer((int) delayMs, e -> runnable.run());
        timer.setRepeats(true);
        timer.start();
        return new SwingScheduled(timer);
    }

    @Override
    public boolean isUiThread() {
        return SwingUtilities.isEventDispatchThread();
    }

    private static class SwingScheduled implements Scheduled {
        private final Timer swingTimer;

        private SwingScheduled(Timer swingTimer) {
            this.swingTimer = swingTimer;
        }

        @Override
        public boolean cancel() {
            swingTimer.stop();
            return true;
        }
    }
}
