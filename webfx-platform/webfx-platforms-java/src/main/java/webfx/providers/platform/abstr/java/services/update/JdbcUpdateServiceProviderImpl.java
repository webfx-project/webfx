package webfx.providers.platform.abstr.java.services.update;

import webfx.platform.services.datasource.ConnectionDetails;
import webfx.platform.services.update.spi.remote.RemoteUpdateServiceProviderImpl;
import webfx.platform.services.update.spi.UpdateServiceProvider;
import webfx.providers.platform.abstr.java.services.JdbcConnectedServiceProviderImpl;

/**
 * @author Bruno Salmon
 */
public class JdbcUpdateServiceProviderImpl extends RemoteUpdateServiceProviderImpl {

    @Override
    protected UpdateServiceProvider createConnectedUpdateService(ConnectionDetails connectionDetails) {
        return new JdbcConnectedServiceProviderImpl(connectionDetails);
    }

}
