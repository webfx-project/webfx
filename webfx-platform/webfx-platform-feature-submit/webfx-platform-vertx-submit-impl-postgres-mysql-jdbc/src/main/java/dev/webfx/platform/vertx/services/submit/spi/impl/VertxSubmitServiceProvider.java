package dev.webfx.platform.vertx.services.submit.spi.impl;

import dev.webfx.platform.shared.services.submit.spi.impl.LocalSubmitServiceProvider;
import dev.webfx.platform.vertx.services_shared_code.queryupdate.VertxLocalConnectedQuerySubmitServiceProvider;
import dev.webfx.platform.shared.services.datasource.LocalDataSource;
import dev.webfx.platform.shared.services.submit.spi.SubmitServiceProvider;

/**
 * @author Bruno Salmon
 */
public final class VertxSubmitServiceProvider extends LocalSubmitServiceProvider {

    @Override
    protected SubmitServiceProvider createLocalConnectedSubmitService(LocalDataSource localDataSource) {
        return new VertxLocalConnectedQuerySubmitServiceProvider(localDataSource);
    }

}
