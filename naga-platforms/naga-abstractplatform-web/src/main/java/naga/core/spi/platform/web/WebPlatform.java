package naga.core.spi.platform.web;

import naga.core.client.bus.ReconnectBus;
import naga.core.client.bus.WebSocketBusOptions;
import naga.core.client.bus.WebSocketFactory;
import naga.core.json.JsonFactory;
import naga.core.spi.bus.BusFactory;
import naga.core.spi.bus.BusOptions;
import naga.core.spi.platform.ClientPlatform;
import naga.core.spi.platform.Platform;
import naga.core.spi.platform.ResourceService;
import naga.core.spi.platform.Scheduler;

/**
 * @author Bruno Salmon
 */
public abstract class WebPlatform extends Platform implements ClientPlatform {

    private final JsonFactory jsonFactory;
    private final WebSocketFactory webSocketFactory;
    private final ResourceService resourceService;
    private final WebLocation webLocation;

    public WebPlatform(Scheduler scheduler, JsonFactory jsonFactory, WebSocketFactory webSocketFactory, ResourceService resourceService, WebLocation webLocation) {
        super(scheduler);
        this.jsonFactory = jsonFactory;
        this.webSocketFactory = webSocketFactory;
        this.resourceService = resourceService;
        this.webLocation = webLocation;
    }

    @Override
    public BusFactory busFactory() { // busFactory() ClientPlatform default method doesn't work to implement Platform one
        return ReconnectBus::new; // So repeating it again...
    }

    @Override
    public JsonFactory jsonFactory() {
        return jsonFactory;
    }

    @Override
    public WebSocketFactory webSocketFactory() {
        return webSocketFactory;
    }

    @Override
    public ResourceService resourceService() {
        return resourceService;
    }

    public WebLocation getCurrentLocation() {
        return webLocation;
    }

    @Override
    public void setPlatformBusOptions(BusOptions options) {
        WebSocketBusOptions socketBusOptions = (WebSocketBusOptions) options;
        // Setting protocol to HTTP (unless already explicitly set by the application)
        if (socketBusOptions.getProtocol() == null)
            socketBusOptions.setProtocol(WebSocketBusOptions.Protocol.HTTP);
        // Setting protocol to Web Socket (unless already explicitly set by the application)
        WebLocation webLocation = getCurrentLocation();
        if (socketBusOptions.getServerHost() == null)
            socketBusOptions.setServerHost(webLocation.getHostName());
        if (socketBusOptions.getServerPort() == null) {
            Integer port = webLocation.getPort();
            if (port != null && port == 63342) // Port used by IntelliJ IDEA to serve web pages when testing directly in IDEA
                port = 80; // But the actual naga server web port on the development local machine is 80 in this case
            socketBusOptions.setServerPort(port);
        }
        super.setPlatformBusOptions(options);
    }
}
