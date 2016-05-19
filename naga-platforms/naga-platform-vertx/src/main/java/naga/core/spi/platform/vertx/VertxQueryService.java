package naga.core.spi.platform.vertx;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.asyncsql.PostgreSQLClient;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import naga.core.queryservice.QueryArgument;
import naga.core.queryservice.QueryResultSet;
import naga.core.queryservice.QueryService;
import naga.core.queryservice.WriteResult;
import naga.core.queryservice.impl.ConnectionDetails;
import naga.core.queryservice.impl.DBMS;
import naga.core.queryservice.impl.RemoteQueryService;
import naga.core.util.Arrays;
import naga.core.util.async.Future;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public class VertxQueryService extends RemoteQueryService {

    private final Vertx vertx;

    public VertxQueryService(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    protected QueryService createConnectedSqlService(ConnectionDetails connectionDetails) {
        return new VertxConnectedQueryService(connectionDetails);
    }

    class VertxConnectedQueryService implements QueryService {

        private final AsyncSQLClient sqlClient;

        public VertxConnectedQueryService(ConnectionDetails connectionDetails) {
            // Generating the Vertx Sql config from the connection details
            JsonObject sqlConfig = new JsonObject()
                    // common config with JDBCClient
                    .put("username", connectionDetails.getUsername())
                    .put("password", connectionDetails.getPassword())
                    // used for PostgreSQLClient only
                    .put("host", connectionDetails.getHost())
                    .put("database", connectionDetails.getDatabaseName())
                    ;
            String poolName = "mypool";
            // Getting the best (non blocking if possible) sql client depending on the dbms
            if (connectionDetails.getDBMS() == DBMS.POSTGRES)
                sqlClient = PostgreSQLClient.createShared(vertx, sqlConfig, poolName); // Non blocking client for Postgres
            else if (connectionDetails.getDBMS() == DBMS.MYSQL)
                sqlClient = MySQLClient.createShared(vertx, sqlConfig, poolName); // Non blocking client for Mysql
            else { // Otherwise using JDBC client: the working thread will be blocked by jdbc calls (synchronous API)
                // used for JDBCClient client only
                sqlConfig
                        .put("url", connectionDetails.getUrl())
                        .put("driver_class", connectionDetails.getDBMS().getJdbcDriverClass())
                        //.put("provider_class", HikariCPDataSourceProvider.class.getName())
                ;
                sqlClient = new AsyncSQLClient() {
                    JDBCClient jdbcClient = JDBCClient.createShared(vertx, sqlConfig, poolName);

                    @Override
                    public void close() {
                        jdbcClient.close();
                    }

                    @Override
                    public void close(Handler<AsyncResult<Void>> handler) {
                        jdbcClient.close();
                        handler.handle(null);
                    }

                    @Override
                    public void getConnection(Handler<AsyncResult<SQLConnection>> handler) {
                        jdbcClient.getConnection(handler);
                    }
                };
            }
        }

        @Override
        public Future<QueryResultSet> read(QueryArgument arg) {
            Future<QueryResultSet> future = Future.future();

            sqlClient.getConnection(con -> {
                if (con.failed()) // Connection failed
                    future.fail(con.cause());
                else { // Connection succeeded
                    SQLConnection connection = con.result();
                    // Preparing the result handler which is the same for query() and queryWithParams()
                    Handler<AsyncResult<ResultSet>> resultHandler = res -> {
                        if (res.failed()) // Sql error
                            future.fail(res.cause());
                        else { // Sql succeeded
                            // Transforming the result set into columnNames and values arrays
                            ResultSet resultSet = res.result();
                            int columnCount = resultSet.getNumColumns();
                            int rowCount = resultSet.getNumRows();
                            String[] columnNames = resultSet.getColumnNames().toArray(new String[columnCount]);
                            Object[] inlineValues = new Object[rowCount * columnCount];
                            List<JsonArray> results = resultSet.getResults();
                            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                                JsonArray jsonArray = results.get(rowIndex);
                                for (int columnIndex = 0; columnIndex < columnCount; columnIndex++)
                                    inlineValues[rowIndex + columnIndex * rowCount] = jsonArray.getValue(columnIndex);
                            }
                            // Returning the final QueryResultSet
                            future.complete(new QueryResultSet(inlineValues, columnNames));
                        }
                        // Closing the connection so it can go back to the pool
                        connection.close();
                    };
                    // Calling query or queryWithParams depending if parameters are provided or not
                    Object[] parameters = arg.getParameters();
                    if (Arrays.isEmpty(parameters))
                        connection.query(arg.getQueryString(), resultHandler);
                    else {
                        JsonArray array = new JsonArray();
                        for (Object value : parameters)
                            array.add(value);
                        connection.queryWithParams(arg.getQueryString(), array, resultHandler);
                    }
                }
            });

            return future;
        }

        @Override
        public Future<WriteResult> write(QueryArgument argument) {
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }
}
