package webfx.platforms.java.services.update;

import webfx.platforms.core.datasource.ConnectionDetails;
import webfx.platforms.core.services.update.spi.impl.LocalOrRemoteUpdateServiceProvider;
import webfx.platforms.core.services.update.spi.UpdateServiceProvider;
import webfx.platforms.java.services.JdbcConnectedServiceProvider;

/**
 * @author Bruno Salmon
 */
public final class JdbcUpdateServiceProvider extends LocalOrRemoteUpdateServiceProvider {

    @Override
    protected UpdateServiceProvider createConnectedUpdateService(ConnectionDetails connectionDetails) {
        return new JdbcConnectedServiceProvider(connectionDetails);
    }

}
