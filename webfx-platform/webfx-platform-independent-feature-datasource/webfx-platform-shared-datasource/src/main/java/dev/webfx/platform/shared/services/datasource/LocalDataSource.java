package dev.webfx.platform.shared.services.datasource;

/**
 * @author Bruno Salmon
 */
public interface LocalDataSource {

    Object getId();

    DBMS getDBMS();

    ConnectionDetails getLocalConnectionDetails();

    static LocalDataSource get(Object id) {
        return LocalDataSourceService.getLocalDataSource(id);
    }
}
