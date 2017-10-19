package naga.providers.platform.client.teavm.websocket;

import naga.platform.client.websocket.spi.WebSocketFactoryProvider;
import naga.platform.json.spi.JsonObject;
import naga.platform.client.websocket.spi.WebSocket;

/**
 * @author Bruno Salmon
 */
public final class TeaVmWebSocketFactoryProvider implements WebSocketFactoryProvider {
    @Override
    public WebSocket createWebSocket(String url, JsonObject options) {
        return new TeaVmWebSocket(url, options);
    }
}
