package naga.core.spi.toolkit.cn1;

import com.codename1.ui.Display;
import com.codename1.ui.util.UITimer;
import naga.core.spi.platform.Scheduler;

/**
 * @author Bruno Salmon
 */
public class Cn1Scheduler implements Scheduler<UITimer> {

    public static Cn1Scheduler SINGLETON = new Cn1Scheduler();

    private Cn1Scheduler() {
    }

    @Override
    public void scheduleDeferred(Runnable runnable) {
        Display.getInstance().callSerially(runnable);
    }

    @Override
    public UITimer scheduleDelay(long delayMs, Runnable runnable) {
        return UITimer.timer((int) delayMs, false, runnable);
    }

    @Override
    public UITimer schedulePeriodic(long delayMs, Runnable runnable) {
        return UITimer.timer((int) delayMs, true, runnable);
    }

    @Override
    public boolean cancelTimer(UITimer timer) {
        timer.cancel();
        return true;
    }

    @Override
    public boolean isUiThread() {
        return Display.getInstance().isEdt();
    }

    @Override
    public void runInBackground(Runnable runnable) {
        Display.getInstance().scheduleBackgroundTask(runnable);
    }
}
