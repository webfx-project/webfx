package webfx.platform.vertx.services.submit.spi.impl;

import webfx.platform.shared.services.submit.spi.impl.LocalSubmitServiceProvider;
import webfx.platform.vertx.services_shared_code.queryupdate.VertxLocalConnectedQuerySubmitServiceProvider;
import webfx.platform.shared.services.datasource.LocalDataSource;
import webfx.platform.shared.services.submit.spi.SubmitServiceProvider;

/**
 * @author Bruno Salmon
 */
public final class VertxSubmitServiceProvider extends LocalSubmitServiceProvider {

    @Override
    protected SubmitServiceProvider createLocalConnectedSubmitService(LocalDataSource localDataSource) {
        return new VertxLocalConnectedQuerySubmitServiceProvider(localDataSource);
    }

}
