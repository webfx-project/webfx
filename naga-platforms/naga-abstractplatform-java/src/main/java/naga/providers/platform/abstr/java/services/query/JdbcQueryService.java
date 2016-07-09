package naga.providers.platform.abstr.java.services.query;

import naga.platform.services.datasource.ConnectionDetails;
import naga.platform.services.query.remote.RemoteQueryService;
import naga.platform.services.query.spi.QueryService;
import naga.providers.platform.abstr.java.services.JdbcConnectedService;

/**
 * @author Bruno Salmon
 */
public class JdbcQueryService extends RemoteQueryService {

    public static final JdbcQueryService SINGLETON = new JdbcQueryService();

    private JdbcQueryService() {
    }

    @Override
    protected QueryService createConnectedQueryService(ConnectionDetails connectionDetails) {
        return new JdbcConnectedService(connectionDetails);
    }

}
