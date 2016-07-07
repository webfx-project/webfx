package naga.core.spi.platform.java;

import naga.core.datasource.ConnectionDetails;
import naga.core.services.update.UpdateService;
import naga.core.services.update.RemoteUpdateService;

/**
 * @author Bruno Salmon
 */
class JdbcUpdateService extends RemoteUpdateService {

    public static final JdbcUpdateService SINGLETON = new JdbcUpdateService();

    private JdbcUpdateService() {
    }

    @Override
    protected UpdateService createConnectedUpdateService(ConnectionDetails connectionDetails) {
        return new JdbcConnectedService(connectionDetails);
    }

}
