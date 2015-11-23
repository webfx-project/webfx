package naga.core.spi.plat;

import io.vertx.core.Vertx;
import naga.core.util.async.Handler;

/**
 * @author Bruno Salmon
 */
public class VertxScheduler implements Scheduler {

    private final Vertx vertx;

    public VertxScheduler(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void scheduleDeferred(Handler<Void> handler) {
        scheduleDelay(1, handler);
    }

    @Override
    public int scheduleDelay(int delayMs, Handler<Void> handler) {
        return (int) vertx.setTimer(delayMs, new io.vertx.core.Handler<Long>() {
            @Override
            public void handle(Long event) {
                handler.handle(null);
            }
        });
    }

    @Override
    public int schedulePeriodic(int delayMs, Handler<Void> handler) {
        return (int) vertx.setPeriodic(delayMs, new io.vertx.core.Handler<Long>() {
            @Override
            public void handle(Long event) {
                handler.handle(null);
            }
        });
    }

    @Override
    public boolean cancelTimer(int id) {
        return vertx.cancelTimer(id);
    }
}
