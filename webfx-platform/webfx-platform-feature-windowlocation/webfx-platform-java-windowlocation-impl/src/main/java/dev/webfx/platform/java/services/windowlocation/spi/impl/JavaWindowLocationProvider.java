package dev.webfx.platform.java.services.windowlocation.spi.impl;

import dev.webfx.platform.client.services.windowhistory.WindowHistory;
import dev.webfx.platform.client.services.windowlocation.spi.WindowLocationProvider;
import dev.webfx.platform.client.services.windowlocation.spi.impl.BrowsingLocationImpl;
import dev.webfx.platform.client.services.websocketbus.WebSocketBusOptions;
import dev.webfx.platform.shared.services.bus.BusService;

/**
 * @author Bruno Salmon
 */
public final class JavaWindowLocationProvider extends BrowsingLocationImpl implements WindowLocationProvider {

    public JavaWindowLocationProvider() {
        super(((WebSocketBusOptions) BusService.getBusOptions()).isServerSSL() ? "https" : "http", ((WebSocketBusOptions) BusService.getBusOptions()).getServerHost(), null, WindowHistory.getCurrentLocation());
    }
}
