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
package naga.core.spi.plat.javaplat;


import naga.core.spi.plat.Scheduler;
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
public class JavaScheduler implements Scheduler {
    private final ScheduledExecutorService executor;
    private final AtomicInteger timerId;
    private final Map<Integer, ScheduledFuture<?>> timers;

    protected JavaScheduler() {
        executor = Executors.newScheduledThreadPool(1);
        timerId = new AtomicInteger(0);
        timers = new HashMap<Integer, ScheduledFuture<?>>();

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
    public boolean cancelTimer(int id) {
        if (timers.containsKey(id)) {
            return timers.remove(id).cancel(false);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handle(Object handler, Object event) {
        ((Handler<Object>) handler).handle(event);
    }

    @Override
    public void scheduleDeferred(final Handler<Void> handler) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                handler.handle(null);
            }
        });
    }

    @Override
    public int scheduleDelay(int delayMs, final Handler<Void> handler) {
        final int id = timerId.getAndIncrement();
        ScheduledFuture<?> future = executor.schedule(new Runnable() {
            @Override
            public void run() {
                timers.remove(id);
                handler.handle(null);
            }
        }, delayMs, TimeUnit.MILLISECONDS);
        timers.put(id, future);
        return id;
    }

    @Override
    public int schedulePeriodic(int delayMs, final Handler<Void> handler) {
        final int id = timerId.getAndIncrement();
        ScheduledFuture<?> future = executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                handler.handle(null);
            }
        }, delayMs, delayMs, TimeUnit.MILLISECONDS);
        timers.put(id, future);
        return id;
    }
}
