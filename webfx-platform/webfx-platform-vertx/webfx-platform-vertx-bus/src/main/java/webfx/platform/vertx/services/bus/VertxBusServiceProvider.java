package webfx.platform.vertx.services.bus;

import webfx.platform.vertx.global.VertxInstance;
import webfx.platforms.core.services.bus.BusFactory;
import webfx.platforms.core.services.bus.spi.impl.BusServiceProviderBase;

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
