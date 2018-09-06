package webfx.platform.vertx.services.bus;

import io.vertx.core.Vertx;
import webfx.platforms.core.services.bus.BusFactory;
import webfx.platforms.core.services.bus.spi.impl.BusServiceProviderBase;

/**
 * @author Bruno Salmon
 */
public final class VertxBusServiceProvider extends BusServiceProviderBase {

    private final BusFactory vertxBusFactory;

    public VertxBusServiceProvider(Vertx vertx) {
        vertxBusFactory = new VertxBusFactory(vertx.eventBus());
    }

    @Override
    public BusFactory busFactory() {
        return vertxBusFactory;
    }
}
