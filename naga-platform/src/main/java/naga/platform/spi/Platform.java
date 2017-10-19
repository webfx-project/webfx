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
package naga.platform.spi;

import naga.platform.bus.Bus;
import naga.platform.bus.BusFactory;
import naga.platform.bus.BusOptions;
import naga.platform.bus.call.ThreadLocalBusContext;
import naga.platform.client.url.history.History;
import naga.platform.client.url.history.memory.MemoryHistory;
import naga.platform.services.query.remote.RemoteQueryService;
import naga.platform.services.query.spi.QueryService;
import naga.platform.services.update.remote.RemoteUpdateService;
import naga.platform.services.update.spi.UpdateService;
import naga.util.function.Consumer;
import naga.util.serviceloader.ServiceLoaderHelper;

/**
 * Generic platform class. New platforms are defined as extension of this abstract class.
 *
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-channel/blob/master/src/main/java/com/goodow/realtime/core/PlatformFactory.java">Original Goodow class</a>
 */
public abstract class Platform {

    public abstract BusFactory busFactory();

    public BusOptions createBusOptions() { return new BusOptions();}

    public void setPlatformBusOptions(BusOptions options) {
        options.turnUnsetPropertiesToDefault();
    }

    public QueryService queryService() {
        return RemoteQueryService.REMOTE_ONLY_QUERY_SERVICE;
    }

    public UpdateService updateService() {
        return RemoteUpdateService.REMOTE_ONLY_UPDATE_SERVICE;
    }

    protected History history;
    public History getBrowserHistory() {
        if (history == null)
            history = new MemoryHistory();
        return history;
    }

    /*** Static access ***/

    private static Platform PLATFORM;

    public static void register(Platform platform) {
        PLATFORM = platform;
    }

    public static synchronized Platform get() {
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

    // BusFactory methods

    private static Bus BUS;
    private static BusOptions busOptions;

    public static Bus bus() {
        Bus bus = ThreadLocalBusContext.getThreadLocalBus();
        if (bus != null)
            return bus;
        if (BUS == null)
            BUS = createBus();
        return BUS;
    }

    public static BusOptions getBusOptions() {
        return busOptions;
    }

    public static void setBusOptions(BusOptions busOptions) {
        Platform.busOptions = busOptions;
    }

    public static Bus createBus() {
        if (busOptions == null)
            busOptions = get().createBusOptions();
        return createBus(busOptions);
    }

    public static Bus createBus(BusOptions options) {
        Platform platform = get();
        platform.setPlatformBusOptions(options);
        Bus bus = platform.busFactory().createBus(options);
        if (BUS == null)
            BUS = bus;
        return bus;
    }

    // QueryService methods

    public static QueryService getQueryService() {
        return get().queryService();
    }

    public static UpdateService getUpdateService() {
        return get().updateService();
    }
}
