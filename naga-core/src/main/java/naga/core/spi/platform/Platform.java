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

import naga.core.json.JsonFactory;
import naga.core.spi.bus.Bus;
import naga.core.spi.bus.BusFactory;
import naga.core.spi.bus.BusOptions;
import naga.core.spi.sql.SqlService;
import naga.core.util.function.Consumer;

/**
 * Generic platform interface. New platforms are defined as implementations of this interface.
 *
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-channel/blob/master/src/main/java/com/goodow/realtime/core/PlatformFactory.java">Original Goodow class</a>
 */
public abstract class Platform {

    public abstract Scheduler scheduler();

    public abstract JsonFactory jsonFactory();

    public abstract BusFactory busFactory();

    public BusOptions createBusOptions() { return new BusOptions();}

    public void setPlatformBusOptions(BusOptions options) {
        options.turnUnsetPropertiesToDefault();
    }

    public abstract SqlService sqlService();

    /*** Static access ***/

    private static Platform PLATFORM;

    public static void register(Platform platform) {
        PLATFORM = platform;
    }

    public static Platform get() {
        if (PLATFORM == null)
            register(ServiceLoaderHelper.loadService(Platform.class));
        return PLATFORM;
    }

    /*** Static shortcut methods ***/

    // Logger methods

    public static void log(Object message) {
        log(message == null ? "null" : message.toString());
    }

    public static void log(String message) {
        System.out.println(message);
        if (webLogger != null)
            webLogger.accept(message);
    }

    public static void log(String message, Throwable error) {
        log(message);
        error.printStackTrace();
    }

    // Temporary code to help displaying logs on the web page (and not only on the hidden web console)
    // This will disappear when the user interface will become richer
    private static Consumer<String> webLogger;
    public static void setWebLogger(Consumer<String> webLogger) {
        Platform.webLogger = webLogger;
    }

    // Scheduler methods

    public static void scheduleDeferred(Runnable runnable) {
        get().scheduler().scheduleDeferred(runnable);
    }

    public static Object scheduleDelay(int delayMs, Runnable runnable) {
        return get().scheduler().scheduleDelay(delayMs, runnable);
    }

    public static Object schedulePeriodic(int delayMs, Runnable runnable) {
        return get().scheduler().schedulePeriodic(delayMs, runnable);
    }

    public static boolean cancelTimer(Object timerId) {
        return get().scheduler().cancelTimer(timerId);
    }

    public static void runInBackground(Runnable runnable) {
        get().scheduler().runInBackground(runnable);
    }
    // BusFactory methods

    private static Bus BUS;

    public static Bus bus() {
        if (BUS == null)
            BUS = createBus();
        return BUS;
    }

    public static Bus createBus() {
        return createBus(get().createBusOptions());
    }

    public static Bus createBus(BusOptions options) {
        Platform platform = get();
        platform.setPlatformBusOptions(options);
        return BUS = platform.busFactory().createBus(options);
    }

    // SqlService methods

    public static SqlService sql() {
        return get().sqlService();
    }
}
