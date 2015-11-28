/*
 * Note: this code is a fork of Goodow realtime-android project https://github.com/goodow/realtime-android
 */

/*
 * Copyright 2013 Goodow.com
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

import naga.core.spi.json.JsonFactory;
import naga.core.spi.json.javaplat.JavaJsonFactory;
import naga.core.spi.plat.Platform;
import naga.core.spi.sched.Scheduler;
import naga.core.spi.sched.javaplat.JavaScheduler;
import naga.core.spi.sock.WebSocketFactory;
import naga.core.spi.sock.javaplat.JavaWebSocketFactory;

import java.util.logging.Logger;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-android project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-android/blob/master/src/main/java/com/goodow/realtime/core/WebSocket.java">Original Goodow class</a>
 */
public abstract class JavaPlatform implements Platform {
    protected final JavaScheduler scheduler;
    protected final JsonFactory jsonFactory = new JavaJsonFactory();
    protected final WebSocketFactory webSocketFactory = new JavaWebSocketFactory();

    protected JavaPlatform() {
        this(new JavaScheduler());
    }

    protected JavaPlatform(JavaScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public Logger logger() {
        return Logger.getAnonymousLogger();
    }

    @Override
    public Scheduler scheduler() {
        return scheduler;
    }

    @Override
    public JsonFactory jsonFactory() {
        return jsonFactory;
    }

    @Override
    public WebSocketFactory webSocketFactory() {
        return webSocketFactory;
    }

}