package naga.platform.providers.teavm;

import naga.commons.json.spi.JsonObject;
import naga.commons.websocket.spi.WebSocket;
import naga.commons.websocket.spi.WebSocketFactory;

/**
 * @author Bruno Salmon
 */
final class TeaVmWebSocketFactory implements WebSocketFactory {
    @Override
    public WebSocket createWebSocket(String url, JsonObject options) {
        return new TeaVmWebSocket(url, options);
    }
}
