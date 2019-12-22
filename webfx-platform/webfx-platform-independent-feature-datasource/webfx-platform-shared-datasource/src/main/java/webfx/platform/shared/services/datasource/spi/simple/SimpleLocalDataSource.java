package webfx.platform.shared.services.datasource.spi.simple;

import webfx.platform.shared.services.datasource.ConnectionDetails;
import webfx.platform.shared.services.datasource.DBMS;
import webfx.platform.shared.services.datasource.LocalDataSource;
import webfx.platform.shared.services.datasource.QueryTranslator;

/**
 * @author Bruno Salmon
 */
public final class SimpleLocalDataSource implements LocalDataSource {

    private final Object dataSourceId;
    private final DBMS dbms;
    private final ConnectionDetails connectionDetails;
    private final QueryTranslator queryTranslator;

    public SimpleLocalDataSource(Object dataSourceId, DBMS dbms, ConnectionDetails connectionDetails) {
        this(dataSourceId, dbms, connectionDetails,null);
    }

    public SimpleLocalDataSource(Object dataSourceId, DBMS dbms, ConnectionDetails connectionDetails, QueryTranslator queryTranslator) {
        this.dataSourceId = dataSourceId;
        this.dbms = dbms;
        this.connectionDetails = connectionDetails;
        this.queryTranslator = queryTranslator;
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
}
