package webfx.platform.vertx.services.bus;

import io.vertx.core.eventbus.EventBus;
import webfx.platforms.core.services.bus.Bus;
import webfx.platforms.core.services.bus.BusFactory;
import webfx.platforms.core.services.bus.BusOptions;

/**
 * @author Bruno Salmon
 */
final class VertxBusFactory implements BusFactory {

    private final EventBus eventBus;

    VertxBusFactory(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public Bus createBus(BusOptions options) {
        return new VertxBus(eventBus, options);
    }
}
