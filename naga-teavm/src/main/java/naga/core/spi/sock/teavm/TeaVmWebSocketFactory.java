package naga.core.spi.sock.teavm;

import naga.core.spi.json.JsonObject;
import naga.core.spi.sock.WebSocketFactory;
import naga.core.spi.sock.WebSocket;

/**
 * @author Bruno Salmon
 */
public final class TeaVmWebSocketFactory implements WebSocketFactory {
    @Override
    public WebSocket createWebSocket(String url, JsonObject options) {
        return new TeaVmWebSocket(url, options);
    }
}
