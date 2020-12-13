package dev.webfx.platform.java.services.submit.spi.impl.jdbc;

import dev.webfx.platform.java.services_shared_code.queryupdate.jdbc.JdbcLocalConnectedQuerySubmitServiceProvider;
import dev.webfx.platform.shared.services.datasource.LocalDataSource;
import dev.webfx.platform.shared.services.submit.spi.SubmitServiceProvider;
import dev.webfx.platform.shared.services.submit.spi.impl.LocalOrRemoteSubmitServiceProvider;

/**
 * @author Bruno Salmon
 */
public final class JdbcSubmitServiceProvider extends LocalOrRemoteSubmitServiceProvider {

    @Override
    protected SubmitServiceProvider createLocalConnectedSubmitService(LocalDataSource localDataSource) {
        return new JdbcLocalConnectedQuerySubmitServiceProvider(localDataSource);
    }

}
