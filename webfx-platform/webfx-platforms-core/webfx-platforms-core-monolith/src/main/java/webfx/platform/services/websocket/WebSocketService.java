package webfx.platform.services.websocket;

import webfx.platform.services.json.JsonObject;
import webfx.platform.services.websocket.spi.WebSocketServiceProvider;
import webfx.util.serviceloader.ServiceLoaderHelper;

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
