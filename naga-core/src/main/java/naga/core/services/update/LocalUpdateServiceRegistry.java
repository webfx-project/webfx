package naga.core.services.update;

import naga.core.services.update.UpdateService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class LocalUpdateServiceRegistry {

    private static Map</* dataSourceId */ Object, UpdateService> localConnectedUpdateServices;

    public static void registerLocalConnectedUpdateService(Object dataSourceId, UpdateService localUpdateService) {
        if (localConnectedUpdateServices == null)
            localConnectedUpdateServices = new HashMap<>();
        localConnectedUpdateServices.put(dataSourceId, localUpdateService);
    }

    public static UpdateService getLocalConnectedUpdateService(Object dataSourceId) {
        return localConnectedUpdateServices == null ? null : localConnectedUpdateServices.get(dataSourceId);
    }
}
