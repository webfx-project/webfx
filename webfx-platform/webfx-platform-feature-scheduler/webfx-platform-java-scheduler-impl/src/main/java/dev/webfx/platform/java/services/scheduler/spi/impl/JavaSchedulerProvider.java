/*
 * Note: this code is a fork of Goodow realtime-android project https://github.com/goodow/realtime-android
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
package dev.webfx.platform.java.services.scheduler.spi.impl;


import dev.webfx.platform.shared.services.log.Logger;
import dev.webfx.platform.shared.services.scheduler.Scheduled;
import dev.webfx.platform.shared.services.scheduler.spi.SchedulerProvider;

import java.util.concurrent.*;

/*
 * @author Bruno Salmon
 */
public final class JavaSchedulerProvider implements SchedulerProvider {

    //private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(availableProcessors());

    public JavaSchedulerProvider() {
/* Commented as this scheduler may still be used by other shutdown tasks (avoiding a RejectedExecutionException)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // shutdown our thread pool
            try {
                executor.shutdown();
                executor.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException ie) {
                // nothing to do here except go ahead and exit
            }
        }));
*/
    }

    @Override
    public void scheduleDeferred(Runnable runnable) {
        executor.execute(caughtRunnable(runnable));
    }

    @Override
    public JavaScheduled scheduleDelay(long delayMs, Runnable runnable) {
        return new JavaScheduled(executor.schedule(caughtRunnable(runnable), delayMs, TimeUnit.MILLISECONDS));
    }

    @Override
    public JavaScheduled schedulePeriodic(long delayMs, Runnable runnable) {
        return new JavaScheduled(executor.scheduleAtFixedRate(caughtRunnable(runnable), delayMs, delayMs, TimeUnit.MILLISECONDS));
    }

    private static Runnable caughtRunnable(Runnable runnable) {
        return () -> {
            try {
                runnable.run();
            } catch (Throwable t) {
                Logger.log("Uncaught exception in scheduled runnable " + runnable, t);
            }
        };
    }

    @Override
    public void runInBackground(Runnable runnable) {
        executor.execute(caughtRunnable(runnable));
    }

    private static final class JavaScheduled implements Scheduled {
        private final ScheduledFuture scheduledFuture;

        private JavaScheduled(ScheduledFuture scheduledFuture) {
            this.scheduledFuture = scheduledFuture;
        }

        @Override
        public boolean cancel() {
            return scheduledFuture.cancel(false);
        }
    }

    @Override
    public long nanoTime() {
        return System.nanoTime();
    }

    @Override
    public int availableProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }
}
