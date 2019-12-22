package webfx.platform.vertx.services.query.spi.impl;

import webfx.platform.vertx.services_shared_code.queryupdate.VertxLocalConnectedQueryUpdateServiceProvider;
import webfx.platform.shared.services.datasource.LocalDataSource;
import webfx.platform.shared.services.query.spi.QueryServiceProvider;
import webfx.platform.shared.services.query.spi.impl.LocalQueryServiceProvider;

/**
 * @author Bruno Salmon
 */
public final class VertxQueryServiceProvider extends LocalQueryServiceProvider {

    @Override
    protected QueryServiceProvider createLocalConnectedProvider(LocalDataSource localDataSource) {
        return new VertxLocalConnectedQueryUpdateServiceProvider(localDataSource);
    }

}
