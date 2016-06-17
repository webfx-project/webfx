package naga.core.spi.toolkit.cn1;

import com.codename1.ui.Display;
import com.codename1.ui.util.UITimer;
import naga.core.spi.platform.Scheduled;
import naga.core.spi.platform.Scheduler;

/**
 * @author Bruno Salmon
 */
class Cn1Scheduler implements Scheduler {

    static Cn1Scheduler SINGLETON = new Cn1Scheduler();

    private Cn1Scheduler() {
    }

    @Override
    public void scheduleDeferred(Runnable runnable) {
        Display.getInstance().callSerially(runnable);
    }

    @Override
    public Cn1Scheduled scheduleDelay(long delayMs, Runnable runnable) {
        return new Cn1Scheduled(UITimer.timer((int) delayMs, false, runnable));
    }

    @Override
    public Cn1Scheduled schedulePeriodic(long delayMs, Runnable runnable) {
        return new Cn1Scheduled(UITimer.timer((int) delayMs, true, runnable));
    }

    @Override
    public boolean isUiThread() {
        return Display.getInstance().isEdt();
    }

    @Override
    public void runInBackground(Runnable runnable) {
        Display.getInstance().scheduleBackgroundTask(runnable);
    }

    private static class Cn1Scheduled implements Scheduled {
        private final UITimer cn1Timer;

        private Cn1Scheduled(UITimer cn1Timer) {
            this.cn1Timer = cn1Timer;
        }

        @Override
        public boolean cancel() {
            cn1Timer.cancel();
            return true;
        }
    }
}
