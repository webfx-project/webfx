package naga.core.spi.platform.java;

import naga.core.services.query.QueryService;
import naga.core.datasource.ConnectionDetails;
import naga.core.services.query.RemoteQueryService;

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
