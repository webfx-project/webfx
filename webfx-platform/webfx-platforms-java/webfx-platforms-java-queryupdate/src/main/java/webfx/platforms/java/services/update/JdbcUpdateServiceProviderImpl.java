package webfx.platforms.java.services.update;

import webfx.platforms.core.datasource.ConnectionDetails;
import webfx.platforms.core.services.update.spi.remote.RemoteUpdateServiceProviderImpl;
import webfx.platforms.core.services.update.spi.UpdateServiceProvider;
import webfx.platforms.java.services.JdbcConnectedServiceProviderImpl;

/**
 * @author Bruno Salmon
 */
public class JdbcUpdateServiceProviderImpl extends RemoteUpdateServiceProviderImpl {

    @Override
    protected UpdateServiceProvider createConnectedUpdateService(ConnectionDetails connectionDetails) {
        return new JdbcConnectedServiceProviderImpl(connectionDetails);
    }

}
