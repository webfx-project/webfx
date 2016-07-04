package naga.core.queryservice.impl;

import naga.core.queryservice.QueryService;
import naga.core.updateservice.UpdateService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class LocalDataSourceRegistry {

    private static final Map</* dataSourceId */ Object, ConnectionDetails> localDataSourceConnectionDetails = new HashMap<>();
    private static final Map</* dataSourceId */ Object, QueryService> localConnectedQueryServices = new HashMap<>();
    private static final Map</* dataSourceId */ Object, UpdateService> localConnectedUpdateServices = new HashMap<>();

    public static void registerLocalDataSource(Object dataSourceId, ConnectionDetails connectionDetails) {
        localDataSourceConnectionDetails.put(dataSourceId, connectionDetails);
    }

    public static ConnectionDetails getLocalDataSourceConnectionDetails(Object dataSourceId) {
        return localDataSourceConnectionDetails.get(dataSourceId);
    }

    public static void registerLocalConnectedQueryService(Object dataSourceId, QueryService localQueryService) {
        localConnectedQueryServices.put(dataSourceId, localQueryService);
    }

    public static QueryService getLocalConnectedQueryService(Object dataSourceId) {
        return localConnectedQueryServices.get(dataSourceId);
    }

    public static void registerLocalConnectedUpdateService(Object dataSourceId, UpdateService localUpdateService) {
        localConnectedUpdateServices.put(dataSourceId, localUpdateService);
    }

    public static UpdateService getLocalConnectedUpdateService(Object dataSourceId) {
        return localConnectedUpdateServices.get(dataSourceId);
    }
}
