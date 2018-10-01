package webfx.platform.shared.services.bus.spi.impl.vertx;

import webfx.platform.shared.services.bus.BusFactory;
import webfx.platform.vertx.global.VertxInstance;
import webfx.platform.shared.services.bus.spi.impl.BusServiceProviderBase;

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
