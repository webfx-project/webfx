package webfx.platforms.core.services.browsinglocation;

import webfx.platforms.core.services.browsinglocation.spi.BrowsingLocation;
import webfx.platforms.core.util.serviceloader.SingleServiceLoader;

/**
 * @author Bruno Salmon
 */
public class WindowLocation {

    public static BrowsingLocation get() { // Returns the browser window location
        // The only provider of BrowsingLocation returned by the ServiceLoader should be the browser window location
        return SingleServiceLoader.loadService(BrowsingLocation.class);
    }
}
