package naga.core.spi.platform.vertx;

import io.vertx.core.eventbus.EventBus;
import naga.core.bus.Bus;
import naga.core.bus.BusFactory;
import naga.core.bus.BusOptions;

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
