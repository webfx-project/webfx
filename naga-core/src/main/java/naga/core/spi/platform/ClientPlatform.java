package naga.core.spi.platform;

import naga.core.client.bus.ReconnectBus;
import naga.core.client.bus.WebSocket;
import naga.core.client.bus.WebSocketBusOptions;
import naga.core.client.bus.WebSocketFactory;
import naga.core.json.JsonObject;
import naga.core.spi.bus.BusFactory;
import naga.core.spi.bus.BusOptions;

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
