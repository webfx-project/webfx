package naga.providers.platform.client.teavm.websocket;

import naga.platform.json.spi.JsonObject;
import naga.platform.client.websocket.WebSocket;
import naga.platform.client.websocket.WebSocketFactory;

/**
 * @author Bruno Salmon
 */
public final class TeaVmWebSocketFactory implements WebSocketFactory {
    @Override
    public WebSocket createWebSocket(String url, JsonObject options) {
        return new TeaVmWebSocket(url, options);
    }
}
