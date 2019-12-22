package webfx.platform.shared.services.datasource;

/**
 * @author Bruno Salmon
 */
public interface LocalDataSource extends QueryTranslator, UpdateTranslator {

    Object getId();

    DBMS getDBMS();

    ConnectionDetails getLocalConnectionDetails();

    static LocalDataSource get(Object id) {
        return LocalDataSourceService.getLocalDataSource(id);
    }
}
