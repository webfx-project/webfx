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

import naga.core.spi.json.Json;
import naga.core.spi.plat.*;
import naga.core.spi.json.javaplat.JreJsonFactory;

/*
 * @author 田传武 (aka larrytin) - author of Goodow realtime-android project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-android/blob/master/src/main/java/com/goodow/realtime/core/WebSocket.java">Original Goodow class</a>
 */
public class JavaPlatform implements PlatformFactory {
    /**
     * Registers the Java platform with a default configuration.
     */
    public static void register() {
        Platform.setFactory(new JavaPlatform());
        Json.setFactory(new JreJsonFactory());
    }

    protected final JavaNet net;
    protected final JavaScheduler scheduler;
    private final JavaDiff diff;

    protected JavaPlatform(JavaScheduler scheduler) {
        net = new JavaNet();
        this.scheduler = scheduler;
        diff = new JavaDiff();
    }

    private JavaPlatform() {
        this(new JavaScheduler());
    }

    @Override
    public Diff diff() {
        return diff;
    }

    @Override
    public Net net() {
        return net;
    }

    @Override
    public Scheduler scheduler() {
        return scheduler;
    }

    @Override
    public Platform.Type type() {
        return Platform.Type.JAVA;
    }
}