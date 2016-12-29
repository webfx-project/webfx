package naga.fx.spi.swing;

import naga.commons.scheduler.Scheduled;
import naga.commons.scheduler.impl.UiSchedulerBase;

import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Bruno Salmon
 */
class SwingScheduler extends UiSchedulerBase {

    static SwingScheduler SINGLETON = new SwingScheduler();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    private SwingScheduler() {
    }

    @Override
    public void runInBackground(Runnable runnable) {
        executor.execute(runnable);
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

    private Scheduled pulseTimer;
    @Override
    protected void checkExecuteAnimationPipeIsScheduledForNextAnimationFrame() {
        if (pulseTimer == null)
            pulseTimer = schedulePeriodic(1000 / 60, this::executeAnimationPipe);
    }

    @Override
    protected void onExecuteAnimationPipeFinished(boolean noMoreAnimationScheduled) {
        if (noMoreAnimationScheduled && pulseTimer != null) {
            pulseTimer.cancel();
            pulseTimer = null;
        }
    }
}
