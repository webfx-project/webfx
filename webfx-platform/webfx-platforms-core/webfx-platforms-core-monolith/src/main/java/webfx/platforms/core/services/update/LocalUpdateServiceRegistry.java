package webfx.platforms.core.services.update;

import webfx.platforms.core.services.update.spi.UpdateServiceProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class LocalUpdateServiceRegistry {

    private static Map</* dataSourceId */ Object, UpdateServiceProvider> localConnectedUpdateServices;

    public static void registerLocalConnectedUpdateService(Object dataSourceId, UpdateServiceProvider localUpdateServiceProvider) {
        if (localConnectedUpdateServices == null)
            localConnectedUpdateServices = new HashMap<>();
        localConnectedUpdateServices.put(dataSourceId, localUpdateServiceProvider);
    }

    public static UpdateServiceProvider getLocalConnectedUpdateService(Object dataSourceId) {
        return localConnectedUpdateServices == null ? null : localConnectedUpdateServices.get(dataSourceId);
    }
}
