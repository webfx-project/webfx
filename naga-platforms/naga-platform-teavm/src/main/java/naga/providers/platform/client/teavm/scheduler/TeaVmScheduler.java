package naga.providers.platform.client.teavm.scheduler;

import naga.commons.scheduler.Scheduled;
import naga.commons.scheduler.Scheduler;
import naga.commons.util.tuples.Unit;
import org.teavm.platform.Platform;
import org.teavm.platform.PlatformRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class TeaVmScheduler implements Scheduler {

    private final Map<Integer, Integer> periodicIds = new HashMap<>();

    @Override
    public void scheduleDeferred(Runnable runnable) {
        Platform.postpone(runnable::run);
    }

    @Override
    public TeaVmScheduled scheduleDelay(long delayMs, Runnable runnable) {
        return new TeaVmScheduled(Platform.schedule(runnable::run, (int) delayMs));
    }

    @Override
    public TeaVmScheduled schedulePeriodic(long delayMs, Runnable runnable) {
        Unit<Integer> timerIdUnit = new Unit<>();
        int timerId = Platform.schedule(new PlatformRunnable() {
            @Override
            public void run() {
                runnable.run();
                int timer2Id = Platform.schedule(this, (int) delayMs);
                periodicIds.put(timerIdUnit.get(), timer2Id);
            }
        }, (int) delayMs);
        periodicIds.put(timerId, timerId);
        timerIdUnit.set(timerId);
        return new TeaVmScheduled(timerId);
    }

    private class TeaVmScheduled implements Scheduled {
        private final int timerId;

        private TeaVmScheduled(int timerId) {
            this.timerId = timerId;
        }

        @Override
        public boolean cancel() {
            Platform.killSchedule(timerId);
            Integer periodicId = periodicIds.remove(timerId);
            if (periodicId != null)
                Platform.killSchedule(periodicId);
            return true;
        }
    }
}
