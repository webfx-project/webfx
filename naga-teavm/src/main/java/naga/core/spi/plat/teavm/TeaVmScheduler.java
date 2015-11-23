package naga.core.spi.plat.teavm;

import naga.core.spi.plat.Scheduler;
import naga.core.util.Holder;
import naga.core.util.async.Handler;
import org.teavm.platform.Platform;
import org.teavm.platform.PlatformRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class TeaVmScheduler implements Scheduler {

    private final Map<Integer, Integer> periodicIds = new HashMap<>();

    @Override
    public void scheduleDeferred(Handler<Void> handler) {
        Platform.postpone(() -> handler.handle(null));
    }

    @Override
    public int scheduleDelay(int delayMs, Handler<Void> handler) {
        return Platform.schedule(() -> handler.handle(null), delayMs);
    }

    @Override
    public int schedulePeriodic(int delayMs, Handler<Void> handler) {
        Holder<Integer> timerIdHolder = new Holder<>();
        PlatformRunnable runnable = new PlatformRunnable() {
            @Override
            public void run() {
                handler.handle(null);
                int timer2Id = Platform.schedule(this, delayMs);
                periodicIds.put(timerIdHolder.get(), timer2Id);
            }
        };
        int timerId = Platform.schedule(runnable, delayMs);
        periodicIds.put(timerId, timerId);
        timerIdHolder.set(timerId);
        return timerId;
    }

    @Override
    public boolean cancelTimer(int id) {
        Platform.killSchedule(id);
        Integer periodicId = periodicIds.remove(id);
        if (periodicId != null)
            Platform.killSchedule(periodicId);
        return true;
    }
}
