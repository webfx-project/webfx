package naga.core.queryservice.impl;

import naga.core.queryservice.QueryService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class LocalDataSourceRegistry {

    private static final Map<Object, ConnectionDetails> dataSourceConnectionDetails = new HashMap<>();
    private static final Map<Object, QueryService> dataSourceConnectedSqlServices = new HashMap<>();

    public static void registerLocalDataSource(Object dataSourceId, ConnectionDetails connectionDetails) {
        dataSourceConnectionDetails.put(dataSourceId, connectionDetails);
    }

    public static void registerConnectedSqlService(Object dataSourceId, QueryService localQueryService) {
        dataSourceConnectedSqlServices.put(dataSourceId, localQueryService);
    }

    public static ConnectionDetails getLocalDataSourceConnectionDetails(Object dataSourceId) {
        return dataSourceConnectionDetails.get(dataSourceId);
    }

    public static QueryService getConnectedSqlService(Object dataSourceId) {
        return dataSourceConnectedSqlServices.get(dataSourceId);
    }
}
