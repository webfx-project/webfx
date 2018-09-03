package webfx.platforms.core.spi.client;

import webfx.platforms.core.bus.BusFactory;
import webfx.platforms.core.bus.BusOptions;
import webfx.platforms.core.client.bus.ReconnectBus;
import webfx.platforms.core.client.bus.WebSocketBusOptions;
import webfx.platforms.core.client.url.location.WindowLocation;
import webfx.platforms.core.spi.Platform;

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
