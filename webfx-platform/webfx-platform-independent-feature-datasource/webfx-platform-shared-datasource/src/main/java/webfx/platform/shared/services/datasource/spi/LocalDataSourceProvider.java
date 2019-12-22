package webfx.platform.shared.services.datasource.spi;

import webfx.platform.shared.services.datasource.LocalDataSource;

/**
 * @author Bruno Salmon
 */
public interface LocalDataSourceProvider {

    LocalDataSource getLocalDataSource(Object id);

}
