package webfx.platform.vertx.services;

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
import webfx.platform.vertx.global.VertxInstance;
import webfx.platforms.core.datasource.ConnectionDetails;
import webfx.platforms.core.datasource.DBMS;
import webfx.platforms.core.services.log.Logger;
import webfx.platforms.core.services.query.QueryArgument;
import webfx.platforms.core.services.query.QueryResult;
import webfx.platforms.core.services.query.QueryResultBuilder;
import webfx.platforms.core.services.query.spi.QueryServiceProvider;
import webfx.platforms.core.services.querypush.PulseArgument;
import webfx.platforms.core.services.querypush.QueryPushService;
import webfx.platforms.core.services.update.GeneratedKeyBatchIndex;
import webfx.platforms.core.services.update.UpdateArgument;
import webfx.platforms.core.services.update.UpdateResult;
import webfx.platforms.core.services.update.spi.UpdateServiceProvider;
import webfx.platforms.core.util.Arrays;
import webfx.platforms.core.util.async.Batch;
import webfx.platforms.core.util.async.Future;
import java.util.function.BiConsumer;
import webfx.platforms.core.util.tuples.Unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class VertxLocalConnectedQueryUpdateServiceProvider implements QueryServiceProvider, UpdateServiceProvider {

    private final AsyncSQLClient sqlClient;

    public VertxLocalConnectedQueryUpdateServiceProvider(ConnectionDetails connectionDetails) {
        // Generating the Vertx Sql config from the connection details
        JsonObject sqlConfig = new JsonObject()
                // common config with JDBCClient
                .put("username", connectionDetails.getUsername())
                .put("password", connectionDetails.getPassword())
                // used for PostgreSQLClient only
                .put("host", connectionDetails.getHost())
                .put("database", connectionDetails.getDatabaseName());
        Vertx vertx = VertxInstance.getVertx();
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
                final JDBCClient jdbcClient = JDBCClient.createNonShared(vertx, sqlConfig);

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
    public Future<QueryResult> executeQuery(QueryArgument queryArgument) {
        return connectAndExecute(true, (connection, future) -> executeQueryOnConnection(queryArgument, connection, future));
    }

    private void executeQueryOnConnection(QueryArgument queryArgument, SQLConnection connection, Future<QueryResult> future) {
        // long t0 = System.currentTimeMillis();
        executeQueryOnConnection(queryArgument.getQueryString(), queryArgument.getParameters(), connection, ar -> {
            if (ar.failed()) // Sql error
                future.fail(ar.cause());
            else { // Sql succeeded
                // Transforming the result set into columnNames and values arrays
                ResultSet resultSet = ar.result();
                int columnCount = resultSet.getNumColumns();
                int rowCount = resultSet.getNumRows();
                QueryResultBuilder rsb = QueryResultBuilder.create(rowCount, columnCount);
                // deactivated column names serialization - rsb.setColumnNames(resultSet.getColumnNames().toArray(new String[columnCount]));
                List<JsonArray> results = resultSet.getResults();
                for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                    JsonArray jsonArray = results.get(rowIndex);
                    for (int columnIndex = 0; columnIndex < columnCount; columnIndex++)
                        rsb.setValue(rowIndex, columnIndex, jsonArray.getValue(columnIndex));
                }
                // Logger.log("Sql executed in " + (System.currentTimeMillis() - t0) + " ms: " + queryArgument);
                // Building and returning the final QueryResult
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
                // Returning the final QueryResult
                future.complete(new UpdateResult(vertxUpdateResult.getUpdated(), generatedKeys));
                onSuccessfulUpdate(updateArgument);
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
                Logger.log("Error executing " + updateArgument, res.cause());
                future.fail(res.cause());
            } else { // Sql succeeded
                // Transforming the result set into columnNames and values arrays
                ResultSet resultSet = res.result();
                Object[] generatedKeys = resultSet.getResults().get(0).stream().toArray();
                future.complete(new UpdateResult(resultSet.getNumRows(), generatedKeys));
                onSuccessfulUpdate(updateArgument);
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
        batch.executeSerial(batchFuture, UpdateResult[]::new, updateArgument -> {
            Future<UpdateResult> statementFuture = Future.future();
            // Replacing GeneratedKeyBatchIndex parameters with their actual generated keys
            Object[] parameters = updateArgument.getParameters();
            for (int i = 0, length = Arrays.length(parameters); i < length; i++) {
                Object value = parameters[i];
                if (value instanceof GeneratedKeyBatchIndex)
                    parameters[i] = batchIndexGeneratedKeys.get(((GeneratedKeyBatchIndex) value).getBatchIndex());
            }
            executeUpdateOnConnection(updateArgument, connection, false, Future.future()).setHandler(ar -> {
                if (ar.failed()) { // Sql error
                    statementFuture.fail(ar.cause());
                    connection.rollback(event -> closeConnection(connection));
                } else { // Sql succeeded
                    UpdateResult updateResult = ar.result();
                    Object[] generatedKeys = updateResult.getGeneratedKeys();
                    if (!Arrays.isEmpty(generatedKeys))
                        batchIndexGeneratedKeys.set(batchIndex.get(), generatedKeys[0]);
                    batchIndex.set(batchIndex.get() + 1);
                    if (batchIndex.get() < batch.getArray().length)
                        statementFuture.complete(updateResult);
                    else
                        connection.commit(ar2 -> {
                            if (ar2.failed())
                                statementFuture.fail(ar2.cause());
                            else
                                statementFuture.complete(updateResult);
                            closeConnection(connection);
                            onSuccessfulUpdate(batch);
                        });
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
                //Logger.log("open = " + ++open);
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
        //Logger.log("open = " + --open);
    }

    private static void onSuccessfulUpdate(UpdateArgument updateArgument) {
        QueryPushService.executePulse(new PulseArgument(updateArgument.getDataSourceId()));
    }

    private static void onSuccessfulUpdate(Batch<UpdateArgument> batch) {
        onSuccessfulUpdate(batch.getArray()[0]);
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
