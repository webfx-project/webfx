package webfx.platforms.java.client;

import webfx.platforms.core.client.url.history.History;
import webfx.platforms.core.services.windowlocation.spi.impl.WindowLocationProviderImpl;
import webfx.platforms.core.services.bus.client.WebSocketBusOptions;
import webfx.platforms.core.services.bus.BusService;

/**
 * @author Bruno Salmon
 */
public class JavaWindowLocationProvider extends WindowLocationProviderImpl {

    public JavaWindowLocationProvider() {
        super(((WebSocketBusOptions) BusService.getBusOptions()).isServerSSL() ? "https" : "http", ((WebSocketBusOptions) BusService.getBusOptions()).getServerHost(), null, History.get().getCurrentLocation());
    }
}
