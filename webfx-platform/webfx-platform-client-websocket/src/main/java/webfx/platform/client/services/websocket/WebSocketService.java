package webfx.platform.client.services.websocket;

import webfx.platform.shared.services.json.JsonObject;
import webfx.platform.client.services.websocket.spi.WebSocketServiceProvider;
import webfx.platform.shared.util.serviceloader.SingleServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class WebSocketService {

    public static WebSocketServiceProvider getProvider() {
        return SingleServiceLoader.loadService(WebSocketServiceProvider.class);
    }

    public static WebSocket createWebSocket(String url, JsonObject options) {
        return getProvider().createWebSocket(url, options);
    }

}
