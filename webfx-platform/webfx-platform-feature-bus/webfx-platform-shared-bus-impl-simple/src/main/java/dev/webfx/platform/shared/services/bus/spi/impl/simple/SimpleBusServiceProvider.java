package dev.webfx.platform.shared.services.bus.spi.impl.simple;

import dev.webfx.platform.shared.services.bus.BusFactory;
import dev.webfx.platform.shared.services.bus.BusOptions;
import dev.webfx.platform.shared.services.bus.spi.impl.BusServiceProviderBase;
import dev.webfx.platform.shared.services.bus.spi.impl.SimpleBus;

public class SimpleBusServiceProvider extends BusServiceProviderBase {

    @Override
    public BusFactory busFactory() {
        return o -> new SimpleBus();
    }

    @Override
    public BusOptions createBusOptions() {
        return null;
    }

    @Override
    public void setPlatformBusOptions(BusOptions options) {

    }
}
