package webfx.platforms.core.services.windowhistory;

import webfx.platforms.core.services.windowhistory.spi.BrowsingHistoryLocation;
import webfx.platforms.core.services.windowhistory.spi.WindowHistoryProvider;
import webfx.platforms.core.util.serviceloader.SingleServiceLoader;

/**
 * @author Bruno Salmon
 */
public class WindowHistory {

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
