package webfx.platform.shared.services.query.spi.impl.vertx;

import webfx.platform.providers.vertx.queryupdate.VertxLocalConnectedQueryUpdateServiceProvider;
import webfx.platform.shared.service.datasource.LocalDataSource;
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
