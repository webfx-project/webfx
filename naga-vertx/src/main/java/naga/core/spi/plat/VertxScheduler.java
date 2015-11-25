package naga.core.spi.plat;

import io.vertx.core.Vertx;
import naga.core.util.async.Handler;

/**
 * @author Bruno Salmon
 */
public class VertxScheduler implements Scheduler<Long> {

    private final Vertx vertx;

    public VertxScheduler(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void scheduleDeferred(Handler<Void> handler) {
        scheduleDelay(1, handler);
    }

    @Override
    public Long scheduleDelay(int delayMs, Handler<Void> handler) {
        return vertx.setTimer(delayMs, event -> handler.handle(null));
    }

    @Override
    public Long schedulePeriodic(int delayMs, Handler<Void> handler) {
        return vertx.setPeriodic(delayMs, event -> handler.handle(null));
    }

    @Override
    public boolean cancelTimer(Long id) {
        return vertx.cancelTimer(id);
    }
}
