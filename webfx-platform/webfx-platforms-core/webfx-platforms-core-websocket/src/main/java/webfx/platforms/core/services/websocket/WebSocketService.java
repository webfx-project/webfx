package webfx.platforms.core.services.websocket;

import webfx.platforms.core.services.json.JsonObject;
import webfx.platforms.core.services.websocket.spi.WebSocketServiceProvider;
import webfx.platforms.core.util.serviceloader.ServiceLoaderHelper;

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
