package webfx.platform.shared.services.query.spi.impl.java;

import webfx.platform.providers.java.queryupdate.JdbcLocalConnectedQueryUpdateServiceProvider;
import webfx.platform.shared.service.datasource.LocalDataSource;
import webfx.platform.shared.services.query.spi.QueryServiceProvider;
import webfx.platform.shared.services.query.spi.impl.LocalOrRemoteQueryServiceProvider;

/**
 * @author Bruno Salmon
 */
public final class JdbcQueryServiceProvider extends LocalOrRemoteQueryServiceProvider {

    @Override
    protected QueryServiceProvider createLocalConnectedProvider(LocalDataSource localDataSource) {
        return new JdbcLocalConnectedQueryUpdateServiceProvider(localDataSource);
    }

}
