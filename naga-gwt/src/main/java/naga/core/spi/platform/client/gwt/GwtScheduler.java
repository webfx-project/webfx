package naga.core.spi.platform.client.gwt;

import com.google.gwt.user.client.Timer;
import naga.core.spi.platform.Scheduler;
import naga.core.util.async.Handler;


/**
 * @author Bruno Salmon
 */
final class GwtScheduler implements Scheduler<Timer> {

    @Override
    public void scheduleDeferred(Handler<Void> handler) {
        scheduleDelay(0, handler);
    }

    @Override
    public Timer scheduleDelay(int delayMs, Handler<Void> handler) {
        Timer timer = createTimer(handler);
        timer.schedule(delayMs);
        return timer;
    }

    @Override
    public Timer schedulePeriodic(int delayMs, Handler<Void> handler) {
        Timer timer = createTimer(handler);
        timer.scheduleRepeating(delayMs);
        return timer;
    }

    @Override
    public boolean cancelTimer(Timer timer) {
        timer.cancel();
        return true;
    }

    private static Timer createTimer(Handler<Void> handler) {
        return new Timer() {
            @Override
            public void run() {
                handler.handle(null);
            }
        };
    }

}
