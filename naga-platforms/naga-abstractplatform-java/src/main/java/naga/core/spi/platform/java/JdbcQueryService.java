package naga.core.spi.platform.java;

import naga.core.queryservice.QueryService;
import naga.core.queryservice.impl.ConnectionDetails;
import naga.core.queryservice.impl.RemoteQueryService;

/**
 * @author Bruno Salmon
 */
class JdbcQueryService extends RemoteQueryService {

    public static final JdbcQueryService SINGLETON = new JdbcQueryService();

    private JdbcQueryService() {
    }

    @Override
    protected QueryService createConnectedQueryService(ConnectionDetails connectionDetails) {
        return new JdbcConnectedService(connectionDetails);
    }

}
