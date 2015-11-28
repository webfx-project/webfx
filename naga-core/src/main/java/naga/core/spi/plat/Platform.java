/*
 * Note: this code is a fork of Goodow realtime-channel project https://github.com/goodow/realtime-channel
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
package naga.core.spi.plat;

import naga.core.spi.bus.Bus;
import naga.core.spi.bus.BusFactory;
import naga.core.spi.bus.BusOptions;
import naga.core.spi.bus.client.ReconnectBusFactory;
import naga.core.spi.json.Json;
import naga.core.spi.json.JsonFactory;
import naga.core.spi.json.JsonObject;
import naga.core.spi.sched.Scheduler;
import naga.core.spi.sock.WebSocketFactory;
import naga.core.spi.sock.WebSocket;
import naga.core.util.Holder;
import naga.core.util.async.Handler;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generic platform interface. New platforms are defined as implementations of this interface.
 *
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-channel/blob/master/src/main/java/com/goodow/realtime/core/PlatformFactory.java">Original Goodow class</a>
 */
public interface Platform {

    WebSocketFactory webSocketFactory();

    Scheduler scheduler();

    JsonFactory jsonFactory();

    default BusFactory busFactory() { return ReconnectBusFactory.SINGLETON; }

    default Logger logger() {
        //return Logger.getAnonymousLogger(); // compilation error with GWT
        return logger("");
    }

    default Logger logger(String name) {
        return Logger.getLogger(name);
    }

    Holder<Platform> PLATFORM_HOLDER = new Holder<>();

    // Static access

    static void register(Platform platform) {
        PLATFORM_HOLDER.set(platform);
        Json.registerFactory(platform.jsonFactory());
    }

    static Platform get() {
        Platform platform = PLATFORM_HOLDER.get();
        //assert platform != null : "You must register a platform first by invoke {Java|Android}Platform.register()";
        return platform;
    }

    // Static helper methods

    static Bus createBus() {
        return createBus(new BusOptions());
    }

    static Bus createBus(BusOptions options) {
        return get().busFactory().createBus(options);
    }

    static Object scheduleDelay(int delayMs, Handler<Void> handler) {
        return get().scheduler().scheduleDelay(delayMs, handler);
    }

    static void scheduleDeferred(Handler<Void> handler) {
        get().scheduler().scheduleDeferred(handler);
    }

    static Object schedulePeriodic(int delayMs, Handler<Void> handler) {
        return get().scheduler().schedulePeriodic(delayMs, handler);
    }

    static boolean cancelTimer(Object timerId) {
        return get().scheduler().cancelTimer(timerId);
    }

    static WebSocket createWebSocket(String url, JsonObject options) {
        return get().webSocketFactory().createWebSocket(url, options);
    }

    static void log(Object message) {
        log(message == null ? "null" : message.toString());
    }

    static void log(String message) {
        get().logger().log(Level.INFO, message);
    }

    static void log(String message, Throwable error) {
        get().logger().log(Level.SEVERE, message, error);
    }

}
