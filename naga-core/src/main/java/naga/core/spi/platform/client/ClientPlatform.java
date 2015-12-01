package naga.core.spi.platform.client;

import naga.core.spi.bus.BusFactory;
import naga.core.spi.bus.client.ClientBusFactory;
import naga.core.spi.bus.client.WebSocketBusOptions;
import naga.core.spi.json.JsonObject;
import naga.core.spi.platform.Platform;

/**
 * @author Bruno Salmon
 */
public interface ClientPlatform extends Platform {

    default BusFactory busFactory() { return ClientBusFactory.SINGLETON; }

    @Override
    default WebSocketBusOptions createBusOptions() {
        return new WebSocketBusOptions();
    }

    WebSocketFactory webSocketFactory();

    /*** Static access ***/

    static ClientPlatform get() {
        return (ClientPlatform) Platform.get();
    }

    /*** Static helper methods ***/
    // WebSocketFactory method

    static WebSocket createWebSocket(String url, JsonObject options) {
        return get().webSocketFactory().createWebSocket(url, options);
    }


}
