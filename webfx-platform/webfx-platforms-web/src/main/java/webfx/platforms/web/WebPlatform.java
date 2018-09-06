package webfx.platforms.web;

import webfx.platforms.core.client.url.history.History;
import webfx.platforms.core.client.url.location.WindowLocation;
import webfx.platforms.core.spi.Platform;
import webfx.platforms.core.spi.client.ClientPlatform;

/**
 * @author Bruno Salmon
 */
public abstract class WebPlatform extends Platform implements ClientPlatform {

    private final WindowLocation windowLocation;
    private final BrowserHistory browserHistory;

    public WebPlatform(WindowLocation windowLocation, WindowHistory windowHistory) {
        this.windowLocation = windowLocation;
        this.browserHistory = new BrowserHistory(windowHistory);
    }
/*

    @Override
    public BusFactory busFactory() { // busFactory() ClientPlatform default method doesn't work to implement Platform one
        return ReconnectBus::new; // So repeating it again...
    }

    @Override
    public BusOptions createBusOptions() { // ClientPlatform default method doesn't work while extending JavaPlatform
        return new WebSocketBusOptions(); // So repeating it again...
    }

*/
    @Override
    public WindowLocation getCurrentLocation() {
        return windowLocation;
    }

    @Override
    public History getBrowserHistory() {
        return browserHistory;
    }

/*

    @Override
    public void setPlatformBusOptions(BusOptions options) {
        WebSocketBusOptions socketBusOptions = (WebSocketBusOptions) options;
        // Setting protocol to HTTP (unless already explicitly set by the application)
        if (socketBusOptions.getProtocol() == null)
            socketBusOptions.setProtocol(WebSocketBusOptions.Protocol.HTTP);
        WindowLocation windowLocation = getCurrentLocation();
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
*/
}
