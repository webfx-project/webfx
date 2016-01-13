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
        if (socketBusOptions.getServerHost() == null)
            socketBusOptions.setServerHost(getCurrentLocationServerHost());
        if (socketBusOptions.getServerPort() == null)
            socketBusOptions.setServerPort(getCurrentLocationServerPort());
        super.setPlatformBusOptions(options);
    }

    public abstract String getCurrentLocationServerHost();

    public abstract Integer getCurrentLocationServerPort();
}
