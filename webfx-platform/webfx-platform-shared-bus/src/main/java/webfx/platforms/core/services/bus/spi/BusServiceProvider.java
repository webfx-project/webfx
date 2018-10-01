package webfx.platforms.core.services.bus.spi;

import webfx.platforms.core.services.bus.Bus;
import webfx.platforms.core.services.bus.BusFactory;
import webfx.platforms.core.services.bus.BusOptions;

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
