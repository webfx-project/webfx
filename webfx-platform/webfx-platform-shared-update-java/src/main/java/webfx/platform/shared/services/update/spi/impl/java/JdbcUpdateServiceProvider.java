package webfx.platform.shared.services.update.spi.impl.java;

import webfx.platform.providers.java.queryupdate.JdbcLocalConnectedQueryUpdateServiceProvider;
import webfx.platform.shared.service.datasource.LocalDataSource;
import webfx.platform.shared.services.update.spi.UpdateServiceProvider;
import webfx.platform.shared.services.update.spi.impl.LocalOrRemoteUpdateServiceProvider;

/**
 * @author Bruno Salmon
 */
public final class JdbcUpdateServiceProvider extends LocalOrRemoteUpdateServiceProvider {

    @Override
    protected UpdateServiceProvider createConnectedUpdateService(LocalDataSource localDataSource) {
        return new JdbcLocalConnectedQueryUpdateServiceProvider(localDataSource);
    }

}
