package naga.platform.services.websocket;

import naga.platform.services.json.JsonObject;
import naga.platform.services.websocket.spi.WebSocketServiceProvider;
import naga.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public class WebSocketService {

    public static WebSocketServiceProvider getProvider() {
        return ServiceLoaderHelper.loadService(WebSocketServiceProvider.class);
    }

    public static WebSocket createWebSocket(String url, JsonObject options) {
        return getProvider().createWebSocket(url, options);
    }

}
