package dev.webfx.platform.shared.services.datasource.spi.simple;

import dev.webfx.platform.shared.services.datasource.*;

/**
 * @author Bruno Salmon
 */
public final class SimpleLocalDataSource implements LocalDataSource {

    private final Object dataSourceId;
    private final DBMS dbms;
    private final ConnectionDetails connectionDetails;

    public SimpleLocalDataSource(Object dataSourceId, DBMS dbms, ConnectionDetails connectionDetails) {
        this.dataSourceId = dataSourceId;
        this.dbms = dbms;
        this.connectionDetails = connectionDetails;
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

}
