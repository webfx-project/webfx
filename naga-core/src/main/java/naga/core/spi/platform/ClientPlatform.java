package naga.core.spi.platform;

import naga.core.bus.client.ReconnectBus;
import naga.core.bus.client.WebSocket;
import naga.core.bus.client.WebSocketBusOptions;
import naga.core.bus.client.WebSocketFactory;
import naga.core.json.JsonObject;
import naga.core.bus.BusFactory;
import naga.core.bus.BusOptions;

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
