package webfx.platforms.web.services.bus;

import webfx.platforms.core.client.url.location.WindowLocation;
import webfx.platforms.core.services.bus.BusOptions;
import webfx.platforms.core.services.bus.client.ClientBusServiceProviderImpl;
import webfx.platforms.core.services.bus.client.WebSocketBusOptions;
import webfx.platforms.core.services.json.Json;
import webfx.platforms.core.services.resource.ResourceService;

/**
 * @author Bruno Salmon
 */
public class WebClientBusServiceProvider extends ClientBusServiceProviderImpl {

    @Override
    public void setPlatformBusOptions(BusOptions options) {
        WebSocketBusOptions socketBusOptions = (WebSocketBusOptions) options;
        // Setting protocol to HTTP (unless already explicitly set by the application)
        if (socketBusOptions.getProtocol() == null)
            socketBusOptions.setProtocol(WebSocketBusOptions.Protocol.HTTP);
        WindowLocation windowLocation = WindowLocation.get();
        // Setting server host from url hostname (if not explicitly set)
        if (socketBusOptions.getServerHost() == null)
            socketBusOptions.setServerHost(windowLocation.getHostname());
        // Setting server port from url port (if not explicitly set)
        if (socketBusOptions.getServerPort() == null) {
            String port = windowLocation.getPort();
            if ("63342".equals(port)) // Port used by IntelliJ IDEA to serve web pages when testing directly in IDEA
                port = "80"; // But the actual webfx server web port on the development local machine is 80 in this case
            socketBusOptions.setServerPort(port);
        }
        // Setting server SSL from url protocol (if not explicitly set)
        if (socketBusOptions.isServerSSL() == null)
            socketBusOptions.setServerSSL("https".equals(windowLocation.getProtocol()));
        super.setPlatformBusOptions(options);
        String json = ResourceService.getText("webfx/platforms/core/services/bus/client/conf/BusOptions.json").result();
        if (json != null)
            options.applyJson(Json.parseObject(json));
    }

}
