package naga.providers.platform.client.teavm.services.websocket;

import naga.platform.services.websocket.spi.WebSocketServiceProvider;
import naga.platform.services.json.JsonObject;
import naga.platform.services.websocket.WebSocket;

/**
 * @author Bruno Salmon
 */
public final class TeaVmWebSocketServiceProviderImpl implements WebSocketServiceProvider {
    @Override
    public WebSocket createWebSocket(String url, JsonObject options) {
        return new TeaVmWebSocket(url, options);
    }
}
