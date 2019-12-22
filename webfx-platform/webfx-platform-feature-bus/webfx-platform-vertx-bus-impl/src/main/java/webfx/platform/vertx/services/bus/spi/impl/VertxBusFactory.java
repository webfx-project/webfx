package webfx.platform.vertx.services.bus.spi.impl;

import io.vertx.core.eventbus.EventBus;
import webfx.platform.shared.services.bus.Bus;
import webfx.platform.shared.services.bus.BusFactory;
import webfx.platform.shared.services.bus.BusOptions;

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
