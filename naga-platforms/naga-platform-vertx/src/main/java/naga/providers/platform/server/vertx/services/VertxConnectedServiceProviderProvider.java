package naga.providers.platform.server.vertx.services;

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
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import naga.util.Arrays;
import naga.util.async.Batch;
import naga.util.async.Future;
import naga.util.function.BiConsumer;
import naga.util.tuples.Unit;
import naga.platform.services.datasource.ConnectionDetails;
import naga.platform.services.datasource.DBMS;
import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.QueryResultSet;
import naga.platform.services.query.QueryResultSetBuilder;
import naga.platform.services.query.spi.QueryServiceProvider;
import naga.platform.services.update.GeneratedKeyBatchIndex;
import naga.platform.services.update.UpdateArgument;
import naga.platform.services.update.UpdateResult;
import naga.platform.services.update.spi.UpdateServiceProvider;
import naga.platform.spi.Platform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class VertxConnectedServiceProviderProvider implements QueryServiceProvider, UpdateServiceProvider {

    private final AsyncSQLClient sqlClient;

    public VertxConnectedServiceProviderProvider(Vertx vertx, ConnectionDetails connectionDetails) {
        // Generating the Vertx Sql config from the connection details
        JsonObject sqlConfig = new JsonObject()
                // common config with JDBCClient
                .put("username", connectionDetails.getUsername())
                .put("password", connectionDetails.getPassword())
                // used for PostgreSQLClient only
                .put("host", connectionDetails.getHost())
                .put("database", connectionDetails.getDatabaseName());
        // Getting the best (non blocking if possible) sql client depending on the dbms
        if (connectionDetails.getDBMS() == DBMS.POSTGRES)
            sqlClient = PostgreSQLClient.createNonShared(vertx, sqlConfig); // Non blocking client for Postgres
        else if (connectionDetails.getDBMS() == DBMS.MYSQL)
            sqlClient = MySQLClient.createNonShared(vertx, sqlConfig); // Non blocking client for Mysql
        else { // Otherwise using JDBC client: the working thread will be blocked by jdbc calls (synchronous API)
            // used for JDBCClient client only
            sqlConfig
                    .put("url", connectionDetails.getUrl())
                    .put("driver_class", connectionDetails.getDBMS().getJdbcDriverClass())
            //.put("provider_class", HikariCPDataSourceProvider.class.getName())
            ;
            sqlClient = new AsyncSQLClient() {
                JDBCClient jdbcClient = JDBCClient.createNonShared(vertx, sqlConfig);

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
                public SQLClient getConnection(Handler<AsyncResult<SQLConnection>> handler) {
                    jdbcClient.getConnection(handler);
                    return this;
                }
            };
        }
    }

    @Override
    public Future<QueryResultSet> executeQuery(QueryArgument queryArgument) {
        return connectAndExecute(true, (connection, future) -> executeQueryOnConnection(queryArgument, connection, future));
    }

    protected void executeQueryOnConnection(QueryArgument queryArgument, SQLConnection connection, Future<QueryResultSet> future) {
        executeQueryOnConnection(queryArgument.getQueryString(), queryArgument.getParameters(), connection, res -> {
            if (res.failed()) // Sql error
                future.fail(res.cause());
            else { // Sql succeeded
                // Transforming the result set into columnNames and values arrays
                ResultSet resultSet = res.result();
                int columnCount = resultSet.getNumColumns();
                int rowCount = resultSet.getNumRows();
                QueryResultSetBuilder rsb = QueryResultSetBuilder.create(rowCount, columnCount);
                // deactivated column names serialization - rsb.setColumnNames(resultSet.getColumnNames().toArray(new String[columnCount]));
                List<JsonArray> results = resultSet.getResults();
                for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                    JsonArray jsonArray = results.get(rowIndex);
                    for (int columnIndex = 0; columnIndex < columnCount; columnIndex++)
                        rsb.setValue(rowIndex, columnIndex, jsonArray.getValue(columnIndex));
                }
                // Building and returning the final QueryResultSet
                future.complete(rsb.build());
            }
            // Closing the connection so it can go back to the pool
            closeConnection(connection);
        });
    }

    private void executeQueryOnConnection(String queryString, Object[] parameters, SQLConnection connection, Handler<AsyncResult<ResultSet>> resultHandler) {
        // Calling query() or queryWithParams() depending if parameters are provided or not
        if (Arrays.isEmpty(parameters))
            connection.query(queryString, resultHandler);
        else
            connection.queryWithParams(queryString, toJsonParameters(parameters), resultHandler);
    }

    @Override
    public Future<UpdateResult> executeUpdate(UpdateArgument updateArgument) {
        return connectAndExecute(true, (connection, future) -> executeUpdateOnConnection(updateArgument, connection, true, future));
    }

    private Future<UpdateResult> executeUpdateOnConnection(UpdateArgument updateArgument, SQLConnection connection, boolean close, Future<UpdateResult> future) {
        // Special case: update with a returning clause must be managed differently using query() instead of update()
        String updateString = updateArgument.getUpdateString().trim();
        String lowerCaseUpdateString = updateString.toLowerCase();
        if (lowerCaseUpdateString.startsWith("select ") || lowerCaseUpdateString.contains(" returning "))
            return executeReturningUpdateOnConnection(updateArgument, connection, close, future);

        // Calling update() or updateWithParams() depending if parameters are provided or not
        executeUpdateOnConnection(updateString, updateArgument.getParameters(), connection, res -> {
            if (res.failed()) // Sql error
                future.fail(res.cause());
            else { // Sql succeeded
                io.vertx.ext.sql.UpdateResult vertxUpdateResult = res.result();
                JsonArray keys = vertxUpdateResult.getKeys();
                Object[] generatedKeys = null;
                if (updateArgument.returnGeneratedKeys() && keys != null && !keys.isEmpty()) {
                    int length = keys.size();
                    generatedKeys = new Object[length];
                    for (int i = 0; i < length; i++)
                        generatedKeys[i] = keys.getValue(i);
                }
                // Returning the final QueryResultSet
                future.complete(new UpdateResult(vertxUpdateResult.getUpdated(), generatedKeys));
            }
            // Closing the connection so it can go back to the pool
            if (close)
                closeConnection(connection);
        });
        return future;
    }

    private void executeUpdateOnConnection(String updateString, Object[] parameters, SQLConnection connection, Handler<AsyncResult<io.vertx.ext.sql.UpdateResult>> resultHandler) {
        if (Arrays.isEmpty(parameters))
            connection.update(updateString, resultHandler);
        else
            connection.updateWithParams(updateString, toJsonParameters(parameters), resultHandler);
    }

    private Future<UpdateResult> executeReturningUpdateOnConnection(UpdateArgument updateArgument, SQLConnection connection, boolean close, Future<UpdateResult> future) {
        executeQueryOnConnection(updateArgument.getUpdateString(), updateArgument.getParameters(), connection, res -> {
            if (res.failed()) { // Sql error
                Platform.log("Error executing " + updateArgument, res.cause());
                future.fail(res.cause());
            } else { // Sql succeeded
                // Transforming the result set into columnNames and values arrays
                ResultSet resultSet = res.result();
                Object[] generatedKeys = resultSet.getResults().get(0).stream().toArray();
                future.complete(new UpdateResult(resultSet.getNumRows(), generatedKeys));
            }
            // Closing the connection so it can go back to the pool
            if (close)
                closeConnection(connection);
        });
        return future;
    }

    @Override
    public Future<Batch<UpdateResult>> executeUpdateBatch(Batch<UpdateArgument> batch) {
        // Singular batch optimization: executing the single sql order in autocommit mode
        Future<Batch<UpdateResult>> singularBatchFuture = batch.executeIfSingularBatch(UpdateResult[]::new, this::executeUpdate);
        if (singularBatchFuture != null)
            return singularBatchFuture;

        // Now handling real batch with several arguments -> no autocommit with explicit commit() or rollback() handling
        return connectAndExecute(false, (connection, batchFuture) -> executeUpdateBatchOnConnection(batch, connection, batchFuture));
    }

    private void executeUpdateBatchOnConnection(Batch<UpdateArgument> batch, SQLConnection connection, Future<Batch<UpdateResult>> batchFuture) {
        List<Object> batchIndexGeneratedKeys = new ArrayList<>(Collections.nCopies(batch.getArray().length, null));
        Unit<Integer> batchIndex = new Unit<>(0);
        batch.executeSerial(batchFuture, UpdateResult[]::new, arg -> {
            Future<UpdateResult> statementFuture = Future.future();
            // Replacing GeneratedKeyBatchIndex parameters with their actual generated keys
            Object[] parameters = arg.getParameters();
            for (int i = 0, length = Arrays.length(parameters); i < length; i++) {
                Object value = parameters[i];
                if (value instanceof GeneratedKeyBatchIndex)
                    parameters[i] = batchIndexGeneratedKeys.get(((GeneratedKeyBatchIndex) value).getBatchIndex());
            }
            executeUpdateOnConnection(arg, connection, false, Future.future()).setHandler(res -> {
                if (res.failed()) { // Sql error
                    statementFuture.fail(res.cause());
                    connection.rollback(event -> closeConnection(connection));
                } else { // Sql succeeded
                    UpdateResult updateResult = res.result();
                    Object[] generatedKeys = updateResult.getGeneratedKeys();
                    if (!Arrays.isEmpty(generatedKeys))
                        batchIndexGeneratedKeys.set(batchIndex.get(), generatedKeys[0]);
                    batchIndex.set(batchIndex.get() + 1);
                    if (batchIndex.get() < batch.getArray().length)
                        statementFuture.complete(updateResult);
                    else
                        commitCompleteAndClose(statementFuture, connection, updateResult);
                }
            });
            return statementFuture;
        });
    }

    //private int open = 0;

    private <T> Future<T> connectAndExecute(boolean autoCommit, BiConsumer<SQLConnection, Future<T>> executor) {
        Future<T> future = Future.future();

        sqlClient.getConnection(connectionAsyncResult -> {
            if (connectionAsyncResult.failed()) // Connection failed
                future.fail(connectionAsyncResult.cause());
            else { // Connection succeeded
                SQLConnection connection = connectionAsyncResult.result();
                //Platform.log("open = " + ++open);
                if (autoCommit)
                    executor.accept(connection, future);
                else
                    connection.setAutoCommit(false, event -> {
                        if (event.failed())
                            future.fail(event.cause());
                        else
                            executor.accept(connection, future);
                    });
            }
        });

        return future;
    }

    private void closeConnection(SQLConnection connection) {
        connection.close();
        //Platform.log("open = " + --open);
    }

    private <T> void commitCompleteAndClose(Future<T> future, SQLConnection connection, T result) {
        connection.commit(asyncResult -> {
            if (asyncResult.failed())
                future.fail(asyncResult.cause());
            else
                future.complete(result);
            closeConnection(connection);
        });
    }

    private static JsonArray toJsonParameters(Object[] parameters) {
        JsonArray array = new JsonArray();
        for (Object value : parameters)
            if (value == null)
                array.addNull();
            else
                array.add(value);
        return array;
    }
}
