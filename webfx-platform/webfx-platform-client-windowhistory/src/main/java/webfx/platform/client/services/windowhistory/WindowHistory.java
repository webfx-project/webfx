package webfx.platform.client.services.windowhistory;

import webfx.platform.client.services.windowhistory.spi.WindowHistoryProvider;
import webfx.platform.client.services.windowhistory.spi.BrowsingHistoryLocation;
import webfx.platform.shared.util.serviceloader.SingleServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class WindowHistory {

    public static WindowHistoryProvider getProvider() { // returns the browser history
        return SingleServiceLoader.loadService(WindowHistoryProvider.class);
    }

    /**
     *
     * @return the current location
     */
    public static BrowsingHistoryLocation getCurrentLocation() {
        return getProvider().getCurrentLocation();
    }

}
