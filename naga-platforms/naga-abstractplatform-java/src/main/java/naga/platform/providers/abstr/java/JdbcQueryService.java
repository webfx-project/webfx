package naga.platform.providers.abstr.java;

import naga.commons.services.query.spi.QueryService;
import naga.commons.services.datasource.ConnectionDetails;
import naga.commons.services.query.remote.RemoteQueryService;

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
