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
package naga.platform.providers.cn1;


import com.codename1.ui.Display;
import naga.commons.scheduler.spi.Scheduled;
import naga.commons.scheduler.spi.Scheduler;

import java.util.Timer;
import java.util.TimerTask;

final class Cn1Scheduler implements Scheduler {

    static Cn1Scheduler SINGLETON = new Cn1Scheduler();

    private final Timer timer = new Timer();

    private Cn1Scheduler() {
    }

    @Override
    public void scheduleDeferred(Runnable runnable) {
        scheduleDelay(1, runnable);
    }

    @Override
    public TimerTaskScheduled scheduleDelay(long delayMs, Runnable runnable) {
        TimerTask timerTask = createTimerTask(runnable);
        timer.schedule(timerTask, delayMs);
        return new TimerTaskScheduled(timerTask);
    }

    @Override
    public TimerTaskScheduled schedulePeriodic(long delayMs, Runnable runnable) {
        TimerTask timerTask = createTimerTask(runnable);
        timer.schedule(timerTask, delayMs, delayMs);
        return new TimerTaskScheduled(timerTask);
    }

    private static TimerTask createTimerTask(final Runnable runnable) {
        return new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

    @Override
    public boolean isUiThread() {
        return Display.getInstance().isEdt();
    }

    @Override
    public void runInUiThread(Runnable runnable) {
        Display.getInstance().callSerially(runnable);
    }

    @Override
    public void runInBackground(Runnable runnable) {
        Display.getInstance().scheduleBackgroundTask(runnable);
    }

    private static class TimerTaskScheduled implements Scheduled {
        private final TimerTask timerTask;

        private TimerTaskScheduled(TimerTask timerTask) {
            this.timerTask = timerTask;
        }

        @Override
        public boolean cancel() {
            return timerTask.cancel();
        }
    }
}
