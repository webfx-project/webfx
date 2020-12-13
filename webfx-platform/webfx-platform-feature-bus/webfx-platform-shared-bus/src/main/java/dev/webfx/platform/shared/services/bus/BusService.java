package dev.webfx.platform.shared.services.bus;

import dev.webfx.platform.shared.services.bus.spi.BusServiceProvider;
import dev.webfx.platform.shared.util.serviceloader.SingleServiceProvider;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class BusService {

    public static BusServiceProvider getProvider() {
        return SingleServiceProvider.getProvider(BusServiceProvider.class, () -> ServiceLoader.load(BusServiceProvider.class));
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
