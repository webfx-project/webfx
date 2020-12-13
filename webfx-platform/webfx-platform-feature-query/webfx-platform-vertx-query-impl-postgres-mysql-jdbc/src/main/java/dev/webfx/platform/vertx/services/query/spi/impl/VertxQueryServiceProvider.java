package dev.webfx.platform.vertx.services.query.spi.impl;

import dev.webfx.platform.vertx.services_shared_code.queryupdate.VertxLocalConnectedQuerySubmitServiceProvider;
import dev.webfx.platform.shared.services.datasource.LocalDataSource;
import dev.webfx.platform.shared.services.query.spi.QueryServiceProvider;
import dev.webfx.platform.shared.services.query.spi.impl.LocalQueryServiceProvider;

/**
 * @author Bruno Salmon
 */
public final class VertxQueryServiceProvider extends LocalQueryServiceProvider {

    @Override
    protected QueryServiceProvider createLocalConnectedProvider(LocalDataSource localDataSource) {
        return new VertxLocalConnectedQuerySubmitServiceProvider(localDataSource);
    }

}
