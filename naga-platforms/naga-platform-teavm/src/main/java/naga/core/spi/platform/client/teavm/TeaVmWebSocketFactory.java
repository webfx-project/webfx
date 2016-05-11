package naga.core.spi.platform.client.teavm;

import naga.core.json.JsonObject;
import naga.core.spi.platform.client.WebSocket;
import naga.core.spi.platform.client.WebSocketFactory;

/**
 * @author Bruno Salmon
 */
final class TeaVmWebSocketFactory implements WebSocketFactory {
    @Override
    public WebSocket createWebSocket(String url, JsonObject options) {
        return new TeaVmWebSocket(url, options);
    }
}
