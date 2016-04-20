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
package naga.core.spi.gui.android;

import android.os.Looper;
import naga.core.spi.platform.Scheduler;

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
    public Object scheduleDelay(long delayMs, Runnable runnable) {
        return handler.postDelayed(runnable, delayMs);
    }

    @Override
    public Object schedulePeriodic(long delayMs, Runnable runnable) {
        return handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runnable.run();
                handler.postDelayed(this, delayMs);
            }
        }, delayMs);
    }

    @Override
    public boolean cancelTimer(Object id) {
        return false; // Not yet implemented...
    }

    @Override
    public boolean isUiThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}