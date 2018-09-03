package webfx.platform.vertx.bus;

import io.vertx.core.eventbus.EventBus;
import webfx.platforms.core.bus.Bus;
import webfx.platforms.core.bus.BusFactory;
import webfx.platforms.core.bus.BusOptions;

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
