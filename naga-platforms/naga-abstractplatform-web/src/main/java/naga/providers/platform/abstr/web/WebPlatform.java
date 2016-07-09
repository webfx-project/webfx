package naga.providers.platform.abstr.web;

import naga.platform.client.bus.ReconnectBus;
import naga.platform.client.bus.WebSocketBusOptions;
import naga.platform.client.url.history.History;
import naga.platform.client.url.location.WindowLocation;
import naga.platform.client.websocket.WebSocketFactory;
import naga.platform.json.spi.JsonFactory;
import naga.platform.bus.BusFactory;
import naga.platform.bus.BusOptions;
import naga.platform.spi.client.ClientPlatform;
import naga.platform.spi.Platform;
import naga.platform.services.resource.spi.ResourceService;
import naga.commons.scheduler.Scheduler;

/**
 * @author Bruno Salmon
 */
public abstract class WebPlatform extends Platform implements ClientPlatform {

    private final JsonFactory jsonFactory;
    private final WebSocketFactory webSocketFactory;
    private final ResourceService resourceService;
    private final WindowLocation windowLocation;
    private final BrowserHistory browserHistory;

    public WebPlatform(Scheduler scheduler, JsonFactory jsonFactory, WebSocketFactory webSocketFactory, ResourceService resourceService, WindowLocation windowLocation, WindowHistory windowHistory) {
        super(scheduler);
        this.jsonFactory = jsonFactory;
        this.webSocketFactory = webSocketFactory;
        this.resourceService = resourceService;
        this.windowLocation = windowLocation;
        this.browserHistory = new BrowserHistory(windowHistory);
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

    public WindowLocation getCurrentLocation() {
        return windowLocation;
    }

    @Override
    public History getBrowserHistory() {
        return browserHistory;
    }

    @Override
    public void setPlatformBusOptions(BusOptions options) {
        WebSocketBusOptions socketBusOptions = (WebSocketBusOptions) options;
        // Setting protocol to HTTP (unless already explicitly set by the application)
        if (socketBusOptions.getProtocol() == null)
            socketBusOptions.setProtocol(WebSocketBusOptions.Protocol.HTTP);
        // Setting protocol to Web Socket (unless already explicitly set by the application)
        WindowLocation windowLocation = getCurrentLocation();
        if (socketBusOptions.getServerHost() == null)
            socketBusOptions.setServerHost(windowLocation.getHostname());
        if (socketBusOptions.getServerPort() == null) {
            String port = windowLocation.getPort();
            if ("63342".equals(port)) // Port used by IntelliJ IDEA to serve web pages when testing directly in IDEA
                port = "80"; // But the actual naga server web port on the development local machine is 80 in this case
            socketBusOptions.setServerPort(port);
        }
        super.setPlatformBusOptions(options);
    }
}
