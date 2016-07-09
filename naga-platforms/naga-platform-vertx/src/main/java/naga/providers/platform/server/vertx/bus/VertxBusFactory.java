package naga.providers.platform.server.vertx.bus;

import io.vertx.core.eventbus.EventBus;
import naga.platform.bus.Bus;
import naga.platform.bus.BusFactory;
import naga.platform.bus.BusOptions;

/**
 * @author Bruno Salmon
 */
public class VertxBusFactory implements BusFactory {

    private final EventBus eventBus;

    public VertxBusFactory(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public Bus createBus(BusOptions options) {
        return new VertxBus(eventBus, options);
    }
}
