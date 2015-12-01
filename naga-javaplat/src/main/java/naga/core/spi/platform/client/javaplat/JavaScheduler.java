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
package naga.core.spi.platform.client.javaplat;


import naga.core.util.async.Handler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-android project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-android/blob/master/src/main/java/com/goodow/realtime/core/WebSocket.java">Original Goodow class</a>
 */
public class JavaScheduler implements naga.core.spi.platform.Scheduler<Integer> {
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private final AtomicInteger timerId = new AtomicInteger(0);
    private final Map<Integer, ScheduledFuture<?>> timers = new HashMap<>();

    public JavaScheduler() {
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
    public boolean cancelTimer(Integer id) {
        return timers.containsKey(id) && timers.remove(id).cancel(false);
    }

    @Override
    public void scheduleDeferred(final Handler<Void> handler) {
        executor.execute(() -> handler.handle(null));
    }

    @Override
    public Integer scheduleDelay(int delayMs, final Handler<Void> handler) {
        int id = timerId.getAndIncrement();
        timers.put(id, executor.schedule((Runnable) () -> {
            timers.remove(id);
            handler.handle(null);
        }, delayMs, TimeUnit.MILLISECONDS));
        return id;
    }

    @Override
    public Integer schedulePeriodic(int delayMs, final Handler<Void> handler) {
        int id = timerId.getAndIncrement();
        timers.put(id, executor.scheduleAtFixedRate((Runnable) () -> handler.handle(null), delayMs, delayMs, TimeUnit.MILLISECONDS));
        return id;
    }
}
