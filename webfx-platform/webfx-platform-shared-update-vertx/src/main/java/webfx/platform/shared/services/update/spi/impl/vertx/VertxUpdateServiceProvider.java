package webfx.platform.shared.services.update.spi.impl.vertx;

import webfx.platform.providers.vertx.queryupdate.VertxLocalConnectedQueryUpdateServiceProvider;
import webfx.platform.shared.service.datasource.LocalDataSource;
import webfx.platform.shared.services.update.spi.UpdateServiceProvider;
import webfx.platform.shared.services.update.spi.impl.LocalUpdateServiceProvider;

/**
 * @author Bruno Salmon
 */
public final class VertxUpdateServiceProvider extends LocalUpdateServiceProvider {

    @Override
    protected UpdateServiceProvider createConnectedUpdateService(LocalDataSource localDataSource) {
        return new VertxLocalConnectedQueryUpdateServiceProvider(localDataSource);
    }

}
