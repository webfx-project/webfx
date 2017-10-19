package naga.providers.platform.abstr.java.services.update;

import naga.platform.services.datasource.ConnectionDetails;
import naga.platform.services.update.spi.UpdateService;
import naga.platform.services.update.remote.RemoteUpdateService;
import naga.providers.platform.abstr.java.services.JdbcConnectedServiceProvider;

/**
 * @author Bruno Salmon
 */
public class JdbcUpdateService extends RemoteUpdateService {

    public static final JdbcUpdateService SINGLETON = new JdbcUpdateService();

    private JdbcUpdateService() {
    }

    @Override
    protected UpdateService createConnectedUpdateService(ConnectionDetails connectionDetails) {
        return new JdbcConnectedServiceProvider(connectionDetails);
    }

}
