package naga.core.spi.bus.crossplat;

import naga.core.spi.bus.Bus;
import naga.core.spi.bus.BusFactory;
import naga.core.spi.bus.BusOptions;

/**
 * @author Bruno Salmon
 */
public class ReconnectBusFactory implements BusFactory {

    public final static ReconnectBusFactory SINGLETON = new ReconnectBusFactory();

    @Override
    public Bus createBus(BusOptions options) {
        return new ReconnectBus(options);
    }
}
