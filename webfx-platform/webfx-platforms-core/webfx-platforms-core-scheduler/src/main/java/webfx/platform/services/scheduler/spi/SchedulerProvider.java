/*
 * Note: this code is a fork of Goodow realtime-channel project https://github.com/goodow/realtime-channel
 */

/*
 * Copyright 2014 Goodow.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package webfx.platform.services.scheduler.spi;

import webfx.platform.services.scheduler.Scheduled;
import webfx.util.function.Consumer;
import webfx.util.tuples.Unit;

/**
 * This class provides low-level task scheduling primitives.
 *
 * @author Bruno Salmon
 */
public interface SchedulerProvider {
    /**
     * A deferred command is executed not now but as soon as possible (ex: after the event loop returns).
     */
    void scheduleDeferred(Runnable runnable);

    /**
     * Set a one-shot timer to fire after {@code delayMs} milliseconds, at which point {@code handler}
     * will be called.
     *
     * @return the timer
     */
    Scheduled scheduleDelay(long delayMs, Runnable runnable);

    default Scheduled scheduleDelay(long delayMs, Consumer<Scheduled> runnable) {
        Unit<Scheduled> scheduledHolder = new Unit<>();
        Scheduled scheduled = scheduleDelay(delayMs, () -> runnable.accept(scheduledHolder.get()));
        scheduledHolder.set(scheduled);
        return scheduled;
    }

    /**
     * Schedules a repeating handler that is scheduled with a constant periodicity. That is, the
     * handler will be invoked every <code>delayMs</code> milliseconds, regardless of how long the
     * previous invocation took to complete.
     *
     * @param delayMs the period with which the handler is executed
     * @param runnable the handler to execute
     * @return the timer
     */
    Scheduled schedulePeriodic(long delayMs, Runnable runnable);

    default Scheduled schedulePeriodic(long delayMs, Consumer<Scheduled> runnable) {
        Unit<Scheduled> scheduledHolder = new Unit<>();
        Scheduled scheduled = schedulePeriodic(delayMs, () -> runnable.accept(scheduledHolder.get()));
        scheduledHolder.set(scheduled);
        return scheduled;
    }

    default void runInBackground(Runnable runnable) {
        scheduleDeferred(runnable);
    }

    long nanoTime(); // because System.nanoTime() is not GWT compatible
}
