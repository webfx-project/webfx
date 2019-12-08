package webfx.platform.shared.service.datasource.spi;

import webfx.platform.shared.service.datasource.LocalDataSource;

/**
 * @author Bruno Salmon
 */
public interface LocalDataSourceProvider {

    LocalDataSource getLocalDataSource(Object id);

}
