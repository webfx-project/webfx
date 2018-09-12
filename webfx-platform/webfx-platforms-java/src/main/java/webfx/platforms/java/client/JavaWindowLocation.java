package webfx.platforms.java.client;

import webfx.platforms.core.client.url.history.History;
import webfx.platforms.core.client.url.location.impl.WindowLocationImpl;
import webfx.platforms.core.services.bus.client.WebSocketBusOptions;
import webfx.platforms.core.services.bus.spi.BusService;

/**
 * @author Bruno Salmon
 */
public class JavaWindowLocation extends WindowLocationImpl {

    public JavaWindowLocation() {
        super(((WebSocketBusOptions) BusService.getBusOptions()).isServerSSL() ? "https" : "http", ((WebSocketBusOptions) BusService.getBusOptions()).getServerHost(), null, History.get().getCurrentLocation());
    }
}
