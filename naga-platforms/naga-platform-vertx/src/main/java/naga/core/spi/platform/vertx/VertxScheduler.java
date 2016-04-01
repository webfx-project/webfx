package naga.core.spi.platform.vertx;

import io.vertx.core.Vertx;
import naga.core.spi.platform.Scheduler;

/**
 * @author Bruno Salmon
 */
public final class VertxScheduler implements Scheduler<Long> {

    private final Vertx vertx;

    public VertxScheduler(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void scheduleDeferred(Runnable runnable) {
        scheduleDelay(1, runnable);
    }

    @Override
    public Long scheduleDelay(long delayMs, Runnable runnable) {
        return vertx.setTimer(delayMs, event -> runnable.run());
    }

    @Override
    public Long schedulePeriodic(long delayMs, Runnable runnable) {
        return vertx.setPeriodic(delayMs, event -> runnable.run());
    }

    @Override
    public boolean cancelTimer(Long id) {
        return vertx.cancelTimer(id);
    }

    @Override
    public boolean isUiThread() {
        return false;
    }
}
