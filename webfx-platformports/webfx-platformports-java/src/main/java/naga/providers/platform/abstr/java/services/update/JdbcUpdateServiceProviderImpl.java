package naga.providers.platform.abstr.java.services.update;

import naga.platform.services.datasource.ConnectionDetails;
import naga.platform.services.update.spi.remote.RemoteUpdateServiceProviderImpl;
import naga.platform.services.update.spi.UpdateServiceProvider;
import naga.providers.platform.abstr.java.services.JdbcConnectedServiceProviderImpl;

/**
 * @author Bruno Salmon
 */
public class JdbcUpdateServiceProviderImpl extends RemoteUpdateServiceProviderImpl {

    @Override
    protected UpdateServiceProvider createConnectedUpdateService(ConnectionDetails connectionDetails) {
        return new JdbcConnectedServiceProviderImpl(connectionDetails);
    }

}
