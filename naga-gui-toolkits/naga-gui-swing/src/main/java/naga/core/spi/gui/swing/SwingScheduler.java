package naga.core.spi.gui.swing;

import naga.core.spi.platform.Scheduler;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingScheduler implements Scheduler<Timer> {

    public static SwingScheduler SINGLETON = new SwingScheduler();

    private SwingScheduler() {
    }

    @Override
    public void scheduleDeferred(Runnable runnable) {
        SwingUtilities.invokeLater(runnable);
    }

    @Override
    public Timer scheduleDelay(long delayMs, Runnable runnable) {
        Timer timer = new Timer((int) delayMs, e -> runnable.run());
        timer.setRepeats(false);
        timer.start();
        return timer;
    }

    @Override
    public Timer schedulePeriodic(long delayMs, Runnable runnable) {
        Timer timer = new Timer((int) delayMs, e -> runnable.run());
        timer.setRepeats(true);
        timer.start();
        return timer;
    }

    @Override
    public boolean cancelTimer(Timer timer) {
        timer.stop();
        return true;
    }

    @Override
    public boolean isUiThread() {
        return SwingUtilities.isEventDispatchThread();
    }
}
