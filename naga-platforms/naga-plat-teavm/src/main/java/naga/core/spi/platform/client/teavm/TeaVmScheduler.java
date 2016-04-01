package naga.core.spi.platform.client.teavm;

import naga.core.spi.platform.Scheduler;
import naga.core.util.Holder;
import org.teavm.platform.Platform;
import org.teavm.platform.PlatformRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
final class TeaVmScheduler implements Scheduler<Integer> {

    private final Map<Integer, Integer> periodicIds = new HashMap<>();

    @Override
    public void scheduleDeferred(Runnable runnable) {
        Platform.postpone(runnable::run);
    }

    @Override
    public Integer scheduleDelay(long delayMs, Runnable runnable) {
        return Platform.schedule(runnable::run, (int) delayMs);
    }

    @Override
    public Integer schedulePeriodic(long delayMs, Runnable runnable) {
        Holder<Integer> timerIdHolder = new Holder<>();
        int timerId = Platform.schedule(new PlatformRunnable() {
            @Override
            public void run() {
                runnable.run();
                int timer2Id = Platform.schedule(this, (int) delayMs);
                periodicIds.put(timerIdHolder.get(), timer2Id);
            }
        }, (int) delayMs);
        periodicIds.put(timerId, timerId);
        timerIdHolder.set(timerId);
        return timerId;
    }

    @Override
    public boolean cancelTimer(Integer id) {
        Platform.killSchedule(id);
        Integer periodicId = periodicIds.remove(id);
        if (periodicId != null)
            Platform.killSchedule(periodicId);
        return true;
    }

    @Override
    public boolean isUiThread() {
        return true;
    }
}
