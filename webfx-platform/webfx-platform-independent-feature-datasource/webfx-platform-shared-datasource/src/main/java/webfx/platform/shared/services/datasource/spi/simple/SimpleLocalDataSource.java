package webfx.platform.shared.services.datasource.spi.simple;

import webfx.platform.shared.services.datasource.*;

/**
 * @author Bruno Salmon
 */
public final class SimpleLocalDataSource implements LocalDataSource {

    private final Object dataSourceId;
    private final DBMS dbms;
    private final ConnectionDetails connectionDetails;
    private final QueryTranslator queryTranslator;
    private final UpdateTranslator updateTranslator;

    public SimpleLocalDataSource(Object dataSourceId, DBMS dbms, ConnectionDetails connectionDetails) {
        this(dataSourceId, dbms, connectionDetails, null, null);
    }

    public SimpleLocalDataSource(Object dataSourceId, DBMS dbms, ConnectionDetails connectionDetails, QueryTranslator queryTranslator, UpdateTranslator updateTranslator) {
        this.dataSourceId = dataSourceId;
        this.dbms = dbms;
        this.connectionDetails = connectionDetails;
        this.queryTranslator = queryTranslator;
        this.updateTranslator = updateTranslator;
    }

    @Override
    public Object getId() {
        return dataSourceId;
    }

    @Override
    public DBMS getDBMS() {
        return dbms;
    }

    @Override
    public ConnectionDetails getLocalConnectionDetails() {
        return connectionDetails;
    }

    @Override
    public String translateQueryIntoDataSourceDefaultLanguage(String queryLanguage, String query) {
        return queryTranslator == null ? query : queryTranslator.translateQueryIntoDataSourceDefaultLanguage(queryLanguage, query);
    }

    @Override
    public String translateUpdateIntoDataSourceDefaultLanguage(String updateLanguage, String update) {
        return updateTranslator == null ? update : updateTranslator.translateUpdateIntoDataSourceDefaultLanguage(updateLanguage, update);
    }
}
