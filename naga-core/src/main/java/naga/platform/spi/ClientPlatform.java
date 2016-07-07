package naga.platform.spi;

import naga.commons.bus.spi.BusFactory;
import naga.commons.bus.websocket.ReconnectBus;
import naga.commons.websocket.spi.WebSocket;
import naga.commons.bus.websocket.WebSocketBusOptions;
import naga.commons.websocket.spi.WebSocketFactory;
import naga.commons.json.spi.JsonObject;
import naga.commons.bus.spi.BusOptions;

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
