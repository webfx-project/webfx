package dev.webfx.platform.shared.services.datasource.spi;

import dev.webfx.platform.shared.services.datasource.LocalDataSource;

/**
 * @author Bruno Salmon
 */
public interface LocalDataSourceProvider {

    LocalDataSource getLocalDataSource(Object dataSourceId);

    default boolean isDataSourceLocal(Object dataSourceId) {
        return getLocalDataSource(dataSourceId) != null;
    }

}
