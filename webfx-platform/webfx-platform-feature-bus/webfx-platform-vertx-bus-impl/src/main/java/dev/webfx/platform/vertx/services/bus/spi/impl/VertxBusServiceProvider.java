package dev.webfx.platform.vertx.services.bus.spi.impl;

import dev.webfx.platform.shared.services.bus.BusFactory;
import dev.webfx.platform.vertx.services_shared_code.instance.VertxInstance;
import dev.webfx.platform.shared.services.bus.spi.impl.BusServiceProviderBase;

/**
 * @author Bruno Salmon
 */
public final class VertxBusServiceProvider extends BusServiceProviderBase {

    private final BusFactory vertxBusFactory;

    public VertxBusServiceProvider() {
        vertxBusFactory = new VertxBusFactory(VertxInstance.getVertx().eventBus());
    }

    @Override
    public BusFactory busFactory() {
        return vertxBusFactory;
    }
}
