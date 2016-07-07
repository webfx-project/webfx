package naga.platform.providers.vertx;

import io.vertx.core.Vertx;
import naga.commons.scheduler.spi.Scheduled;
import naga.commons.scheduler.spi.Scheduler;

/**
 * @author Bruno Salmon
 */
final class VertxScheduler implements Scheduler {

    private final Vertx vertx;

    public VertxScheduler(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void scheduleDeferred(Runnable runnable) {
        scheduleDelay(1, runnable);
    }

    @Override
    public VertxScheduled scheduleDelay(long delayMs, Runnable runnable) {
        return new VertxScheduled(vertx.setTimer(delayMs, event -> runnable.run()));
    }

    @Override
    public VertxScheduled schedulePeriodic(long delayMs, Runnable runnable) {
        return new VertxScheduled(vertx.setPeriodic(delayMs, event -> runnable.run()));
    }

    @Override
    public boolean isUiThread() {
        return false;
    }

    private class VertxScheduled implements Scheduled {
        private final long timerId;

        private VertxScheduled(long timerId) {
            this.timerId = timerId;
        }

        @Override
        public boolean cancel() {
            return vertx.cancelTimer(timerId);
        }
    }
}
