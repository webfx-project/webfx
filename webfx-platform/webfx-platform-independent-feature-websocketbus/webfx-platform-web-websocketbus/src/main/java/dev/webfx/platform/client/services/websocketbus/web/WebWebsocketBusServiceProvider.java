package dev.webfx.platform.client.services.websocketbus.web;

import dev.webfx.platform.shared.services.bus.BusOptions;
import dev.webfx.platform.client.services.websocketbus.WebsocketBusServiceProvider;
import dev.webfx.platform.client.services.websocketbus.WebSocketBusOptions;
import dev.webfx.platform.shared.services.json.Json;
import dev.webfx.platform.shared.services.resource.ResourceService;
import dev.webfx.platform.client.services.windowlocation.WindowLocation;

/**
 * @author Bruno Salmon
 */
public final class WebWebsocketBusServiceProvider extends WebsocketBusServiceProvider {

    @Override
    public void setPlatformBusOptions(BusOptions options) {
        WebSocketBusOptions socketBusOptions = (WebSocketBusOptions) options;
        // Setting protocol to HTTP (unless already explicitly set by the application)
        if (socketBusOptions.getProtocol() == null)
            socketBusOptions.setProtocol(WebSocketBusOptions.Protocol.HTTP);
        // Setting server host from url hostname (if not explicitly set)
        if (socketBusOptions.getServerHost() == null)
            socketBusOptions.setServerHost(WindowLocation.getHostname());
        // Setting server port from url port (if not explicitly set)
        if (socketBusOptions.getServerPort() == null) {
            String port = WindowLocation.getPort();
            if ("63342".equals(port)) // Port used by IntelliJ IDEA to serve web pages when testing directly in IDEA
                port = "80"; // But the actual webfx server web port on the development local machine is 80 in this case
            socketBusOptions.setServerPort(port);
        }
        // Setting server SSL from url protocol (if not explicitly set)
        if (socketBusOptions.isServerSSL() == null)
            socketBusOptions.setServerSSL("https".equals(WindowLocation.getProtocol()));
        super.setPlatformBusOptions(options);
        String json = ResourceService.getText("webfx/platform/core/services/bus/client/conf/BusOptions.json").result();
        if (json != null)
            options.applyJson(Json.parseObject(json));
    }

}
