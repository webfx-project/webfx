package naga.core.spi.platform.client.web;

import naga.core.spi.bus.BusOptions;
import naga.core.spi.bus.client.WebSocketBusOptions;
import naga.core.spi.platform.client.ClientPlatform;

/**
 * @author Bruno Salmon
 */
public abstract class WebPlatform extends ClientPlatform {

    @Override
    public void setPlatformBusOptions(BusOptions options) {
        WebSocketBusOptions socketBusOptions = (WebSocketBusOptions) options;
        // Setting protocol to Web Socket (unless already explicitly set by the application)
        WebLocation webLocation = getCurrentLocation();
        if (socketBusOptions.getServerHost() == null)
            socketBusOptions.setServerHost(webLocation.getHostName());
        if (socketBusOptions.getServerPort() == null)
            socketBusOptions.setServerPort(webLocation.getPort());
        super.setPlatformBusOptions(options);
    }

    public abstract WebLocation getCurrentLocation();
}
