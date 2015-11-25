package naga.core.spi.bus.vertx;

import io.vertx.core.eventbus.EventBus;
import naga.core.spi.bus.Bus;
import naga.core.spi.bus.BusFactory;
import naga.core.spi.bus.BusOptions;

/**
 * @author Bruno Salmon
 */
public final class VertxBusFactory implements BusFactory {

    private final EventBus eventBus;

    public VertxBusFactory(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public Bus createBus(BusOptions options) {
        return new VertxBus(eventBus, options);
    }
}
