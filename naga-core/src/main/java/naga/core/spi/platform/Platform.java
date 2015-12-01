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
package naga.core.spi.platform;

import naga.core.spi.bus.Bus;
import naga.core.spi.bus.BusFactory;
import naga.core.spi.bus.BusOptions;
import naga.core.spi.json.JsonFactory;
import naga.core.util.Holder;
import naga.core.util.async.Handler;

import java.util.Iterator;
import java.util.ServiceLoader;
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

    default Logger logger() {
        return logger(""); // should be Logger.getAnonymousLogger() but produces a compilation error with GWT
    }

    default Logger logger(String name) {
        return Logger.getLogger(name);
    }

    Scheduler scheduler();

    JsonFactory jsonFactory();

    BusFactory busFactory();

    default BusOptions createBusOptions() { return new BusOptions();}

    default void setPlatformBusOptions(BusOptions options) {
        options.turnUnsetPropertiesToDefault();
    }

    /*** Static access ***/

    Holder<Platform> PLATFORM_HOLDER = new Holder<>();

    static void register(Platform platform) {
        PLATFORM_HOLDER.set(platform);
    }

    static Platform get() {
        Platform platform = PLATFORM_HOLDER.get();
        if (platform == null) {
            ServiceLoader<Platform> platformServiceLoader = ServiceLoader.load(Platform.class);
            if (platformServiceLoader != null) {
                Iterator<Platform> it = platformServiceLoader.iterator();
                if (it != null && it.hasNext())
                    register(platform = it.next());
            }
            if (platform == null)
                throw new IllegalStateException("No platform registered. You must register a platform by invoking {Java|Android}Platform.register()");
        }
        return platform;
    }

    /*** Static helper methods ***/

    // Logger methods

    static void log(Object message) {
        log(message == null ? "null" : message.toString());
    }

    static void log(String message) {
        get().logger().log(Level.INFO, message);
    }

    static void log(String message, Throwable error) {
        get().logger().log(Level.SEVERE, message, error);
    }

    // Scheduler methods

    static void scheduleDeferred(Handler<Void> handler) {
        get().scheduler().scheduleDeferred(handler);
    }

    static Object scheduleDelay(int delayMs, Handler<Void> handler) {
        return get().scheduler().scheduleDelay(delayMs, handler);
    }

    static Object schedulePeriodic(int delayMs, Handler<Void> handler) {
        return get().scheduler().schedulePeriodic(delayMs, handler);
    }

    static boolean cancelTimer(Object timerId) {
        return get().scheduler().cancelTimer(timerId);
    }

    // BusFactory methods

    Holder<Bus> BUS_HOLDER = new Holder<>();

    static Bus bus() {
        Bus bus = BUS_HOLDER.get();
        if (bus == null)
            BUS_HOLDER.set(bus = createBus());
        return bus;
    }

    static Bus createBus() {
        return createBus(get().createBusOptions());
    }

    static Bus createBus(BusOptions options) {
        Platform platform = get();
        platform.setPlatformBusOptions(options);
        return platform.busFactory().createBus(options);
    }

}
