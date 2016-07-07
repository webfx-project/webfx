package naga.platform.providers.abstr.java;

import naga.commons.services.datasource.ConnectionDetails;
import naga.commons.services.update.spi.UpdateService;
import naga.commons.services.update.remote.RemoteUpdateService;

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
