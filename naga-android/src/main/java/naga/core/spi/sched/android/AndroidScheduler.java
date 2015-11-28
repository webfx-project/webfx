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
package naga.core.spi.sched.android;

import android.os.Looper;
import naga.core.spi.sched.javaplat.JavaScheduler;
import naga.core.util.async.Handler;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-android project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-android/blob/master/src/main/java/com/goodow/android/AndroidScheduler.java">Original Goodow class</a>
 */
public final class AndroidScheduler extends JavaScheduler {

    private final android.os.Handler handler;

    public AndroidScheduler() {
        handler = new android.os.Handler(Looper.getMainLooper());
    }

    @Override
    public void scheduleDeferred(final Handler<Void> handler) {
        this.handler.post(() -> handler.handle(null));
    }
}