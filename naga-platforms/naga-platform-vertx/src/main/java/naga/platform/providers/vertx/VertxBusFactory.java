package naga.platform.providers.vertx;

import io.vertx.core.eventbus.EventBus;
import naga.commons.bus.spi.Bus;
import naga.commons.bus.spi.BusFactory;
import naga.commons.bus.spi.BusOptions;

/**
 * @author Bruno Salmon
 */
class VertxBusFactory implements BusFactory {

    private final EventBus eventBus;

    public VertxBusFactory(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public Bus createBus(BusOptions options) {
        return new VertxBus(eventBus, options);
    }
}
