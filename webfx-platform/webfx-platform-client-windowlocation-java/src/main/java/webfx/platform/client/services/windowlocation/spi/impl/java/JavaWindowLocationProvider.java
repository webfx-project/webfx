package webfx.platform.client.services.windowlocation.spi.impl.java;

import webfx.platform.client.services.windowhistory.WindowHistory;
import webfx.platform.client.services.windowlocation.spi.WindowLocationProvider;
import webfx.platform.client.services.windowlocation.spi.impl.BrowsingLocationImpl;
import webfx.platform.client.services.websocketbus.WebSocketBusOptions;
import webfx.platform.shared.services.bus.BusService;

/**
 * @author Bruno Salmon
 */
public final class JavaWindowLocationProvider extends BrowsingLocationImpl implements WindowLocationProvider {

    public JavaWindowLocationProvider() {
        super(((WebSocketBusOptions) BusService.getBusOptions()).isServerSSL() ? "https" : "http", ((WebSocketBusOptions) BusService.getBusOptions()).getServerHost(), null, WindowHistory.getCurrentLocation());
    }
}
