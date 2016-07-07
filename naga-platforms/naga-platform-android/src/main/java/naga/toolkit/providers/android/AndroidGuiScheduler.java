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
package naga.toolkit.providers.android;

import android.os.Looper;
import naga.commons.scheduler.spi.Scheduled;
import naga.commons.scheduler.spi.Scheduler;
import naga.commons.util.tuples.Unit;

final class AndroidGuiScheduler implements Scheduler {

    public static AndroidGuiScheduler SINGLETON = new AndroidGuiScheduler();

    private final android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());

    private AndroidGuiScheduler() {
    }

    @Override
    public void scheduleDeferred(Runnable runnable) {
        handler.post(runnable);
    }

    @Override
    public AndroidScheduled scheduleDelay(long delayMs, Runnable runnable) {
        handler.postDelayed(runnable, delayMs);
        return new AndroidScheduled(runnable);
    }

    @Override
    public AndroidScheduled schedulePeriodic(long delayMs, Runnable runnable) {
        final Unit<AndroidScheduled> androidTimerUnit = new Unit<>();
        Runnable repeatingRunnable = new Runnable() {
            @Override
            public void run() {
                runnable.run();
                if (!androidTimerUnit.get().cancelled)
                    handler.postDelayed(this, delayMs);
            }
        };
        AndroidScheduled androidTimer = new AndroidScheduled(repeatingRunnable);
        androidTimerUnit.set(androidTimer);
        handler.postDelayed(androidTimer.runnable, delayMs);
        return androidTimer;
    }

    @Override
    public boolean isUiThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    private class AndroidScheduled implements Scheduled {
        private final Runnable runnable;
        private boolean cancelled;

        private AndroidScheduled(Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        public boolean cancel() {
            handler.removeCallbacks(runnable);
            return cancelled = true;
        }
    }
}