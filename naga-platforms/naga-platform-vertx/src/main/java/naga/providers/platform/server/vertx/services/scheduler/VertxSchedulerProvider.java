package naga.providers.platform.server.vertx.services.scheduler;

import io.vertx.core.Vertx;
import naga.platform.services.scheduler.Scheduled;
import naga.platform.services.scheduler.spi.SchedulerProvider;

/**
 * @author Bruno Salmon
 */
public final class VertxSchedulerProvider implements SchedulerProvider {

    private final Vertx vertx;

    public VertxSchedulerProvider(Vertx vertx) {
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
    public long nanoTime() {
        return System.nanoTime();
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
