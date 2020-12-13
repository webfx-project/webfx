package dev.webfx.platform.vertx.services.scheduler.spi.impl;

import io.vertx.core.Vertx;
import dev.webfx.platform.shared.services.scheduler.Scheduled;
import dev.webfx.platform.vertx.services_shared_code.instance.VertxInstance;
import dev.webfx.platform.shared.services.scheduler.spi.SchedulerProvider;

/**
 * @author Bruno Salmon
 */
public final class VertxSchedulerProvider implements SchedulerProvider {

    private final Vertx vertx = VertxInstance.getVertx();

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

    @Override
    public int availableProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

    private final class VertxScheduled implements Scheduled {
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
