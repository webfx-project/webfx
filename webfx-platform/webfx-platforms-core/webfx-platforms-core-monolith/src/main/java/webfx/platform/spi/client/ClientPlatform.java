package webfx.platform.spi.client;

import webfx.platform.bus.BusFactory;
import webfx.platform.bus.BusOptions;
import webfx.platform.client.bus.ReconnectBus;
import webfx.platform.client.bus.WebSocketBusOptions;
import webfx.platform.client.url.location.WindowLocation;
import webfx.platform.spi.Platform;

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
