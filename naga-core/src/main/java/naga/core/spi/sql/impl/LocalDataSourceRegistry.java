package naga.core.spi.sql.impl;

import naga.core.spi.sql.SqlService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class LocalDataSourceRegistry {

    private static final Map<Object, ConnectionDetails> dataSourceConnectionDetails = new HashMap<>();
    private static final Map<Object, SqlService> dataSourceConnectedSqlServices = new HashMap<>();

    public static void registerLocalDataSource(Object dataSourceId, ConnectionDetails connectionDetails) {
        dataSourceConnectionDetails.put(dataSourceId, connectionDetails);
    }

    public static void registerConnectedSqlService(Object dataSourceId, SqlService localSqlService) {
        dataSourceConnectedSqlServices.put(dataSourceId, localSqlService);
    }

    public static ConnectionDetails getLocalDataSourceConnectionDetails(Object dataSourceId) {
        return dataSourceConnectionDetails.get(dataSourceId);
    }

    public static SqlService getConnectedSqlService(Object dataSourceId) {
        return dataSourceConnectedSqlServices.get(dataSourceId);
    }
}
