package webfx.platform.shared.service.datasource;

import webfx.platform.shared.service.datasource.spi.LocalDataSourceProvider;
import webfx.platform.shared.util.serviceloader.SingleServiceProvider;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class LocalDataSourceService {

    private final static LocalDataSourceProvider provider = SingleServiceProvider.getProvider(LocalDataSourceProvider.class, () -> ServiceLoader.load(LocalDataSourceProvider.class), SingleServiceProvider.NotFoundPolicy.RETURN_NULL);

    public static LocalDataSource getLocalDataSource(Object id) {
        return provider == null ? null : provider.getLocalDataSource(id);
    }

}
