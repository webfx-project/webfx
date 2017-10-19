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
package naga.providers.platform.abstr.java.scheduler;


import naga.scheduler.Scheduled;
import naga.scheduler.SchedulerProvider;
import naga.platform.spi.Platform;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/*
 * @author Bruno Salmon
 */
public class JavaSchedulerProvider implements SchedulerProvider {
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public JavaSchedulerProvider() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // shutdown our thread pool
                try {
                    executor.shutdown();
                    executor.awaitTermination(1, TimeUnit.SECONDS);
                } catch (InterruptedException ie) {
                    // nothing to do here except go ahead and exit
                }
            }
        });
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
                Platform.log("Uncaught exception in scheduled runnable " + runnable, t);
            }
        };
    }

    @Override
    public void runInBackground(Runnable runnable) {
        executor.execute(caughtRunnable(runnable));
    }

    private static class JavaScheduled implements Scheduled {
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
}
