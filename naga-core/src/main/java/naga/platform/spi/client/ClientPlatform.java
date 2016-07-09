package naga.platform.spi.client;

import naga.platform.bus.BusFactory;
import naga.platform.client.bus.ReconnectBus;
import naga.platform.spi.Platform;
import naga.platform.client.websocket.WebSocket;
import naga.platform.client.bus.WebSocketBusOptions;
import naga.platform.client.websocket.WebSocketFactory;
import naga.platform.json.spi.JsonObject;
import naga.platform.bus.BusOptions;

/**
 * @author Bruno Salmon
 */
public interface ClientPlatform {

    default BusFactory busFactory() { return ReconnectBus::new; }

    default BusOptions createBusOptions() {
        return new WebSocketBusOptions();
    }

    WebSocketFactory webSocketFactory();

    /*** Static access ***/

    static ClientPlatform get() {
        return (ClientPlatform) Platform.get();
    }

    /*** Static shortcut methods ***/

    // WebSocketFactory method

    static WebSocket createWebSocket(String url, JsonObject options) {
        return get().webSocketFactory().createWebSocket(url, options);
    }

}
