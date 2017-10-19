package naga.platform.client.websocket.spi;

import naga.platform.json.spi.JsonObject;
import naga.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public class WebSocketFactory {

    private static WebSocketFactoryProvider PROVIDER;

    public static WebSocketFactoryProvider getProvider() {
        if (PROVIDER == null)
            registerProvider(ServiceLoaderHelper.loadService(WebSocketFactoryProvider.class));
        return PROVIDER;
    }

    public static void registerProvider(WebSocketFactoryProvider provider) {
        PROVIDER = provider;
    }

    public static WebSocket createWebSocket(String url, JsonObject options) {
        return getProvider().createWebSocket(url, options);
    }

}
