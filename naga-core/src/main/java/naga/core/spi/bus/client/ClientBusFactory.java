package naga.core.spi.bus.client;

import naga.core.spi.bus.Bus;
import naga.core.spi.bus.BusFactory;

/**
 * @author Bruno Salmon
 */
public class ClientBusFactory implements BusFactory<WebSocketBusOptions> {

    public final static ClientBusFactory SINGLETON = new ClientBusFactory();

    @Override
    public Bus createBus(WebSocketBusOptions options) {
        return new ReconnectBus(options);
    }
}
