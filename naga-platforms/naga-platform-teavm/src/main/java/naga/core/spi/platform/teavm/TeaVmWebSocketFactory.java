package naga.core.spi.platform.teavm;

import naga.core.json.JsonObject;
import naga.core.client.bus.WebSocket;
import naga.core.client.bus.WebSocketFactory;

/**
 * @author Bruno Salmon
 */
final class TeaVmWebSocketFactory implements WebSocketFactory {
    @Override
    public WebSocket createWebSocket(String url, JsonObject options) {
        return new TeaVmWebSocket(url, options);
    }
}
