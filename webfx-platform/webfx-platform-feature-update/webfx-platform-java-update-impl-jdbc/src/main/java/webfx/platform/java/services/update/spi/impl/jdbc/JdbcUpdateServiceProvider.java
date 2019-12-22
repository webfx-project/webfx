package webfx.platform.java.services.update.spi.impl.jdbc;

import webfx.platform.java.services_shared_code.queryupdate.jdbc.JdbcLocalConnectedQueryUpdateServiceProvider;
import webfx.platform.shared.services.datasource.LocalDataSource;
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
