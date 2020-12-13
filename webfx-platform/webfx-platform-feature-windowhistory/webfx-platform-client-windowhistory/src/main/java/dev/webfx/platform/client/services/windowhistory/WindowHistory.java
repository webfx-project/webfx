package dev.webfx.platform.client.services.windowhistory;

import dev.webfx.platform.client.services.windowhistory.spi.BrowsingHistoryLocation;
import dev.webfx.platform.client.services.windowhistory.spi.WindowHistoryProvider;
import dev.webfx.platform.shared.util.serviceloader.SingleServiceProvider;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class WindowHistory {

    public static WindowHistoryProvider getProvider() { // returns the browser history
        return SingleServiceProvider.getProvider(WindowHistoryProvider.class, () -> ServiceLoader.load(WindowHistoryProvider.class));
    }

    /**
     *
     * @return the current location
     */
    public static BrowsingHistoryLocation getCurrentLocation() {
        return getProvider().getCurrentLocation();
    }

}
