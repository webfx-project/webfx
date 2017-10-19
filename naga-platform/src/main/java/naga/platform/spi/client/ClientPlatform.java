package naga.platform.spi.client;

import naga.platform.bus.BusFactory;
import naga.platform.bus.BusOptions;
import naga.platform.client.bus.ReconnectBus;
import naga.platform.client.bus.WebSocketBusOptions;
import naga.platform.client.url.location.WindowLocation;
import naga.platform.spi.Platform;

/**
 * @author Bruno Salmon
 */
public interface ClientPlatform {

    default BusFactory busFactory() { return ReconnectBus::new; }

    default BusOptions createBusOptions() {
        return new WebSocketBusOptions();
    }

    default WindowLocation getCurrentLocation() {
        return null; // empty default implementation for deprecated platforms is provided but should be overridden for active platforms
    }

    /*** Static access ***/

    static ClientPlatform get() {
        return (ClientPlatform) Platform.get();
    }

}
