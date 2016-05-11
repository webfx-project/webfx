package naga.core.spi.sql.impl;

import naga.core.Naga;
import naga.core.json.buscall.BusCallService;
import naga.core.spi.sql.SqlArgument;
import naga.core.spi.sql.SqlReadResult;
import naga.core.spi.sql.SqlService;
import naga.core.spi.sql.SqlWriteResult;
import naga.core.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class SqlServiceImpl implements SqlService {

    public static SqlServiceImpl REMOTE_ONLY_SQL_SERVICE = new SqlServiceImpl();

    @Override
    public Future<SqlReadResult> read(SqlArgument argument) {
        SqlService localSqlService = getConnectedLocalSqlService(argument.getDataSourceId());
        if (localSqlService != null)
            return localSqlService.read(argument);
        return executeRemote(argument, true);
    }

    @Override
    public Future<SqlWriteResult> write(SqlArgument argument) {
        SqlService localSqlService = getConnectedLocalSqlService(argument.getDataSourceId());
        if (localSqlService != null)
            return localSqlService.write(argument);
        return executeRemote(argument, false);
    }

    protected SqlService getConnectedLocalSqlService(Object dataSourceId) {
        SqlService connectedSqlService = LocalDataSourceRegistry.getConnectedSqlService(dataSourceId);
        if (connectedSqlService == null) {
            ConnectionDetails connectionDetails = LocalDataSourceRegistry.getLocalDataSourceConnectionDetails(dataSourceId);
            if (connectionDetails != null)
                connectedSqlService = createConnectedSqlService(connectionDetails);
        }
        return connectedSqlService;
    }

    protected SqlService createConnectedSqlService(ConnectionDetails connectionDetails) {
        throw new UnsupportedOperationException("This platform doesn't support local sql service");
    }

    protected <T> Future<T> executeRemote(SqlArgument argument, boolean read) {
        return BusCallService.call(read ? Naga.SQL_READ_ADDRESS : Naga.SQL_WRITE_ADDRESS, argument);
    }
}
