package webfx.platforms.java.services.update;

import webfx.platforms.core.datasource.ConnectionDetails;
import webfx.platforms.core.services.update.spi.impl.LocalOrRemoteUpdateServiceProviderImpl;
import webfx.platforms.core.services.update.spi.UpdateServiceProvider;
import webfx.platforms.java.services.JdbcConnectedServiceProviderImpl;

/**
 * @author Bruno Salmon
 */
public final class JdbcUpdateServiceProviderImpl extends LocalOrRemoteUpdateServiceProviderImpl {

    @Override
    protected UpdateServiceProvider createConnectedUpdateService(ConnectionDetails connectionDetails) {
        return new JdbcConnectedServiceProviderImpl(connectionDetails);
    }

}
