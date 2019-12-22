package webfx.platform.vertx.services.update.spi.impl;

import webfx.platform.vertx.services_shared_code.queryupdate.VertxLocalConnectedQueryUpdateServiceProvider;
import webfx.platform.shared.services.datasource.LocalDataSource;
import webfx.platform.shared.services.update.spi.UpdateServiceProvider;
import webfx.platform.shared.services.update.spi.impl.LocalUpdateServiceProvider;

/**
 * @author Bruno Salmon
 */
public final class VertxUpdateServiceProvider extends LocalUpdateServiceProvider {

    public VertxUpdateServiceProvider() {
        System.out.println("!!!");
    }

    @Override
    protected UpdateServiceProvider createConnectedUpdateService(LocalDataSource localDataSource) {
        return new VertxLocalConnectedQueryUpdateServiceProvider(localDataSource);
    }

}
