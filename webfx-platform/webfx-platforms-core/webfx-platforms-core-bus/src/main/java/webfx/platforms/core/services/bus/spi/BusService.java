package webfx.platforms.core.services.bus.spi;

import webfx.platforms.core.services.bus.Bus;
import webfx.platforms.core.services.bus.BusFactory;
import webfx.platforms.core.services.bus.BusOptions;
import webfx.platforms.core.util.serviceloader.SingleServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class BusService {

    public static BusServiceProvider getProvider() {
        return SingleServiceLoader.loadService(BusServiceProvider.class);
    }

    public static BusFactory busFactory() {
        return getProvider().busFactory();
    }

    public static BusOptions createBusOptions() { return getProvider().createBusOptions();}

    public static void setPlatformBusOptions(BusOptions options) {
        getProvider().setPlatformBusOptions(options);
    }

    public static Bus bus() {
        return getProvider().bus();
    }

    public static BusOptions getBusOptions() {
        return getProvider().getBusOptions();
    }

    public static void setBusOptions(BusOptions busOptions) {
        getProvider().setBusOptions(busOptions);
    }

    public static Bus createBus() {
        return getProvider().createBus();
    }

    public static Bus createBus(BusOptions options) {
        return getProvider().createBus(options);
    }

}
