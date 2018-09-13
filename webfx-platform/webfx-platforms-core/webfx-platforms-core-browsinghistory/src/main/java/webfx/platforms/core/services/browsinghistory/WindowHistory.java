package webfx.platforms.core.services.browsinghistory;

import webfx.platforms.core.services.browsinghistory.spi.BrowsingHistory;
import webfx.platforms.core.util.serviceloader.SingleServiceLoader;

/**
 * @author Bruno Salmon
 */
public class WindowHistory {

    public static BrowsingHistory get() { // returns the browser history
        // The only provider of BrowsingHistory returned by the ServiceLoader should be the browser history
        return SingleServiceLoader.loadService(BrowsingHistory.class);
    }
}
