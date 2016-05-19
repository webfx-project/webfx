package naga.core.spi.platform;

import naga.core.bus.Bus;
import naga.core.bus.BusOptions;

/**
 * @author Bruno Salmon
 */
public interface BusFactory<O extends BusOptions> {

    Bus createBus(O options);

}
