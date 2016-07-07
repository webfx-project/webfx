package naga.core.services.query;

import naga.core.services.query.QueryService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class LocalQueryServiceRegistry {

    private static Map</* dataSourceId */ Object, QueryService> localConnectedQueryServices;

    public static void registerLocalConnectedQueryService(Object dataSourceId, QueryService localQueryService) {
        if (localConnectedQueryServices == null)
            localConnectedQueryServices = new HashMap<>();
        localConnectedQueryServices.put(dataSourceId, localQueryService);
    }

    public static QueryService getLocalConnectedQueryService(Object dataSourceId) {
        return localConnectedQueryServices == null ? null : localConnectedQueryServices.get(dataSourceId);
    }
}
