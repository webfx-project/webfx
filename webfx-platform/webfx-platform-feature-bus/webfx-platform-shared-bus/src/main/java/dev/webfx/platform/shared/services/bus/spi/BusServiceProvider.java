package dev.webfx.platform.shared.services.bus.spi;

import dev.webfx.platform.shared.services.bus.Bus;
import dev.webfx.platform.shared.services.bus.BusFactory;
import dev.webfx.platform.shared.services.bus.BusOptions;

/**
 * @author Bruno Salmon
 */
public interface BusServiceProvider {

    BusFactory busFactory();

    default BusOptions createBusOptions() { return new BusOptions();}

    default void setPlatformBusOptions(BusOptions options) {
        options.turnUnsetPropertiesToDefault();
    }

    Bus bus();

    BusOptions getBusOptions();

    void setBusOptions(BusOptions busOptions);

    Bus createBus();

    Bus createBus(BusOptions options);

}
