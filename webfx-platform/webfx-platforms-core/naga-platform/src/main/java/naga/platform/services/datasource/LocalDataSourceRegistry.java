package naga.platform.services.datasource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class LocalDataSourceRegistry {

    private static final Map</* dataSourceId */ Object, ConnectionDetails> localDataSourceConnectionDetails = new HashMap<>();

    public static void registerLocalDataSource(Object dataSourceId, ConnectionDetails connectionDetails) {
        localDataSourceConnectionDetails.put(dataSourceId, connectionDetails);
    }

    public static ConnectionDetails getLocalDataSourceConnectionDetails(Object dataSourceId) {
        return localDataSourceConnectionDetails.get(dataSourceId);
    }

}
