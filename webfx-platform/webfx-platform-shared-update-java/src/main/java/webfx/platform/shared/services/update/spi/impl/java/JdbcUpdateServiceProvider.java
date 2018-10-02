package webfx.platform.shared.services.update.spi.impl.java;

import webfx.platform.shared.services.update.spi.UpdateServiceProvider;
import webfx.platform.shared.datasource.ConnectionDetails;
import webfx.platform.shared.services.update.spi.impl.LocalOrRemoteUpdateServiceProvider;
import webfx.platform.providers.java.queryupdate.JdbcLocalConnectedQueryUpdateServiceProvider;

/**
 * @author Bruno Salmon
 */
public final class JdbcUpdateServiceProvider extends LocalOrRemoteUpdateServiceProvider {

    @Override
    protected UpdateServiceProvider createConnectedUpdateService(ConnectionDetails connectionDetails) {
        return new JdbcLocalConnectedQueryUpdateServiceProvider(connectionDetails);
    }

}
