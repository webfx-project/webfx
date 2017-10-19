package naga.providers.platform.abstr.java.services.update;

import naga.platform.services.datasource.ConnectionDetails;
import naga.platform.services.update.remote.RemoteUpdateServiceProvider;
import naga.platform.services.update.spi.UpdateServiceProvider;
import naga.providers.platform.abstr.java.services.JdbcConnectedServiceProviderProvider;

/**
 * @author Bruno Salmon
 */
public class JdbcUpdateServiceProvider extends RemoteUpdateServiceProvider {

    public static final JdbcUpdateServiceProvider SINGLETON = new JdbcUpdateServiceProvider();

    private JdbcUpdateServiceProvider() {
    }

    @Override
    protected UpdateServiceProvider createConnectedUpdateService(ConnectionDetails connectionDetails) {
        return new JdbcConnectedServiceProviderProvider(connectionDetails);
    }

}
