package naga.platform.services.query;

import naga.platform.services.query.spi.QueryServiceProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class LocalQueryServiceRegistry {

    private static Map</* dataSourceId */ Object, QueryServiceProvider> localConnectedQueryServices;

    public static void registerLocalConnectedQueryService(Object dataSourceId, QueryServiceProvider localQueryServiceProvider) {
        if (localConnectedQueryServices == null)
            localConnectedQueryServices = new HashMap<>();
        localConnectedQueryServices.put(dataSourceId, localQueryServiceProvider);
    }

    public static QueryServiceProvider getLocalConnectedQueryService(Object dataSourceId) {
        return localConnectedQueryServices == null ? null : localConnectedQueryServices.get(dataSourceId);
    }
}
