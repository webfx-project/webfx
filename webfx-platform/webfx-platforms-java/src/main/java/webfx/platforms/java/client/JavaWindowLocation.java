package webfx.platforms.java.client;

import webfx.platforms.core.services.browsinghistory.WindowHistory;
import webfx.platforms.core.services.browsinglocation.spi.impl.BrowsingLocationImpl;
import webfx.platforms.core.services.bus.client.WebSocketBusOptions;
import webfx.platforms.core.services.bus.BusService;

/**
 * @author Bruno Salmon
 */
public class JavaWindowLocation extends BrowsingLocationImpl {

    public JavaWindowLocation() {
        super(((WebSocketBusOptions) BusService.getBusOptions()).isServerSSL() ? "https" : "http", ((WebSocketBusOptions) BusService.getBusOptions()).getServerHost(), null, WindowHistory.get().getCurrentLocation());
    }
}
