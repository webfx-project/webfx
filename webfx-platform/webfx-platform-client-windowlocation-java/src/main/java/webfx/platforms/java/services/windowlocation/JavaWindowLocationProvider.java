package webfx.platforms.java.services.windowlocation;

import webfx.platforms.core.services.windowhistory.WindowHistory;
import webfx.platforms.core.services.windowlocation.spi.WindowLocationProvider;
import webfx.platforms.core.services.windowlocation.spi.impl.BrowsingLocationImpl;
import webfx.platforms.core.services.bus.client.WebSocketBusOptions;
import webfx.platforms.core.services.bus.BusService;

/**
 * @author Bruno Salmon
 */
public final class JavaWindowLocationProvider extends BrowsingLocationImpl implements WindowLocationProvider {

    public JavaWindowLocationProvider() {
        super(((WebSocketBusOptions) BusService.getBusOptions()).isServerSSL() ? "https" : "http", ((WebSocketBusOptions) BusService.getBusOptions()).getServerHost(), null, WindowHistory.getCurrentLocation());
    }
}
