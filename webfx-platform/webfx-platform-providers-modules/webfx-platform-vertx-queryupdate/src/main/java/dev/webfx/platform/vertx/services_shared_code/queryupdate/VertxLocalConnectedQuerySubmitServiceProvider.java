package dev.webfx.platform.vertx.services_shared_code.queryupdate;

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
import dev.webfx.platform.shared.services.submit.SubmitArgument;
import dev.webfx.platform.shared.services.submit.SubmitResult;
import dev.webfx.platform.vertx.services_shared_code.instance.VertxInstance;
import dev.webfx.platform.server.services.submitlistener.SubmitListenerService;
import dev.webfx.platform.shared.services.datasource.ConnectionDetails;
import dev.webfx.platform.shared.services.datasource.DBMS;
import dev.webfx.platform.shared.services.datasource.LocalDataSource;
import dev.webfx.platform.shared.services.datasource.jdbc.JdbcDriverInfo;
import dev.webfx.platform.shared.services.log.Logger;
import dev.webfx.platform.shared.services.query.QueryArgument;
import dev.webfx.platform.shared.services.query.QueryResult;
import dev.webfx.platform.shared.services.query.QueryResultBuilder;
import dev.webfx.platform.shared.services.query.spi.QueryServiceProvider;
import dev.webfx.platform.shared.services.submit.GeneratedKeyBatchIndex;
import dev.webfx.platform.shared.services.submit.spi.SubmitServiceProvider;
import dev.webfx.platform.shared.util.Arrays;
import dev.webfx.platform.shared.util.async.Batch;
import dev.webfx.platform.shared.util.async.Future;
import dev.webfx.platform.shared.util.tuples.Unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author Bruno Salmon
 */
public final class VertxLocalConnectedQuerySubmitServiceProvider implements QueryServiceProvider, SubmitServiceProvider {

    private final AsyncSQLClient sqlClient;

    public VertxLocalConnectedQuerySubmitServiceProvider(LocalDataSource localDataSource) {
        ConnectionDetails connectionDetails = localDataSource.getLocalConnectionDetails();
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
        DBMS dbms = localDataSource.getDBMS();
        if (dbms == DBMS.POSTGRES)
            sqlClient = PostgreSQLClient.createNonShared(vertx, sqlConfig); // Non blocking client for Postgres
        else if (dbms == DBMS.MYSQL)
            sqlClient = MySQLClient.createNonShared(vertx, sqlConfig); // Non blocking client for Mysql
        else { // Otherwise using JDBC client: the working thread will be blocked by jdbc calls (synchronous API)
            // used for JDBCClient client only
            JdbcDriverInfo jdbcDriverInfo = JdbcDriverInfo.from(dbms);
            sqlConfig
                    .put("url", jdbcDriverInfo.getUrlOrGenerateJdbcUrl(connectionDetails))
                    .put("driver_class", jdbcDriverInfo.getJdbcDriverClass())
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
        executeQueryOnConnection(queryArgument.getStatement(), queryArgument.getParameters(), connection, ar -> {
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
    public Future<SubmitResult> executeSubmit(SubmitArgument submitArgument) {
        return connectAndExecute(true, (connection, future) -> executeSubmitOnConnection(submitArgument, connection, false, future));
    }

    private Future<SubmitResult> executeSubmitOnConnection(SubmitArgument submitArgument, SQLConnection connection, boolean batch, Future<SubmitResult> future) {
        // Special case: submit with a returning clause must be managed differently using query() instead of update()
        String submitString = submitArgument.getStatement().trim();
        String lowerCaseSubmitString = submitString.toLowerCase();
        if (lowerCaseSubmitString.startsWith("select ") || lowerCaseSubmitString.contains(" returning "))
            return executeReturningSubmitOnConnection(submitArgument, connection, batch, future);

        // Calling update() or updateWithParams() depending if parameters are provided or not
        executeSubmitOnConnection(submitString, submitArgument.getParameters(), connection, res -> {
            if (res.failed()) // Sql error
                future.fail(res.cause());
            else { // Sql succeeded
                io.vertx.ext.sql.UpdateResult vertxUpdateResult = res.result();
                JsonArray keys = vertxUpdateResult.getKeys();
                Object[] generatedKeys = null;
                if (submitArgument.returnGeneratedKeys() && keys != null && !keys.isEmpty()) {
                    int length = keys.size();
                    generatedKeys = new Object[length];
                    for (int i = 0; i < length; i++)
                        generatedKeys[i] = keys.getValue(i);
                }
                // Returning the final QueryResult
                future.complete(new SubmitResult(vertxUpdateResult.getUpdated(), generatedKeys));
                // Unless from batch, calling on successful submit now
                if (!batch)
                    onSuccessfulSubmit(submitArgument);
            }
            // Unless from batch, closing the connection now so it can go back to the pool
            if (!batch)
                closeConnection(connection);
        });
        return future;
    }

    private void executeSubmitOnConnection(String submitString, Object[] parameters, SQLConnection connection, Handler<AsyncResult<io.vertx.ext.sql.UpdateResult>> resultHandler) {
        if (Arrays.isEmpty(parameters))
            connection.update(submitString, resultHandler);
        else
            connection.updateWithParams(submitString, toJsonParameters(parameters), resultHandler);
    }

    private Future<SubmitResult> executeReturningSubmitOnConnection(SubmitArgument submitArgument, SQLConnection connection, boolean batch, Future<SubmitResult> future) {
        executeQueryOnConnection(submitArgument.getStatement(), submitArgument.getParameters(), connection, res -> {
            if (res.failed()) { // Sql error
                Logger.log("Error executing " + submitArgument, res.cause());
                future.fail(res.cause());
            } else { // Sql succeeded
                // Transforming the result set into columnNames and values arrays
                ResultSet resultSet = res.result();
                Object[] generatedKeys = resultSet.getResults().get(0).stream().toArray();
                future.complete(new SubmitResult(resultSet.getNumRows(), generatedKeys));
                // Unless from batch, calling on successful submit now
                if (!batch)
                    onSuccessfulSubmit(submitArgument);
            }
            // Unless from batch, closing the connection now so it can go back to the pool
            if (!batch)
                closeConnection(connection);
        });
        return future;
    }

    @Override
    public Future<Batch<SubmitResult>> executeSubmitBatch(Batch<SubmitArgument> batch) {
        // Singular batch optimization: executing the single sql order in autocommit mode
        Future<Batch<SubmitResult>> singularBatchFuture = batch.executeIfSingularBatch(SubmitResult[]::new, this::executeSubmit);
        if (singularBatchFuture != null)
            return singularBatchFuture;

        // Now handling real batch with several arguments -> no autocommit with explicit commit() or rollback() handling
        return connectAndExecute(false, (connection, batchFuture) -> executeUpdateBatchOnConnection(batch, connection, batchFuture));
    }

    private void executeUpdateBatchOnConnection(Batch<SubmitArgument> batch, SQLConnection connection, Future<Batch<SubmitResult>> batchFuture) {
        List<Object> batchIndexGeneratedKeys = new ArrayList<>(Collections.nCopies(batch.getArray().length, null));
        Unit<Integer> batchIndex = new Unit<>(0);
        batch.executeSerial(batchFuture, SubmitResult[]::new, updateArgument -> {
            Future<SubmitResult> statementFuture = Future.future();
            // Replacing GeneratedKeyBatchIndex parameters with their actual generated keys
            Object[] parameters = updateArgument.getParameters();
            for (int i = 0, length = Arrays.length(parameters); i < length; i++) {
                Object value = parameters[i];
                if (value instanceof GeneratedKeyBatchIndex)
                    parameters[i] = batchIndexGeneratedKeys.get(((GeneratedKeyBatchIndex) value).getBatchIndex());
            }
            executeSubmitOnConnection(updateArgument, connection, true, Future.future()).setHandler(ar -> {
                if (ar.failed()) { // Sql error
                    statementFuture.fail(ar.cause());
                    connection.rollback(event -> closeConnection(connection));
                } else { // Sql succeeded
                    SubmitResult submitResult = ar.result();
                    Object[] generatedKeys = submitResult.getGeneratedKeys();
                    if (!Arrays.isEmpty(generatedKeys))
                        batchIndexGeneratedKeys.set(batchIndex.get(), generatedKeys[0]);
                    batchIndex.set(batchIndex.get() + 1);
                    if (batchIndex.get() < batch.getArray().length)
                        statementFuture.complete(submitResult);
                    else
                        connection.commit(ar2 -> {
                            if (ar2.failed())
                                statementFuture.fail(ar2.cause());
                            else
                                statementFuture.complete(submitResult);
                            closeConnection(connection);
                            onSuccessfulSubmit(batch);
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

    private static void onSuccessfulSubmit(SubmitArgument submitArgument) {
        SubmitListenerService.fireSuccessfulSubmit(submitArgument);
    }

    private static void onSuccessfulSubmit(Batch<SubmitArgument> batch) {
        SubmitListenerService.fireSuccessfulSubmit(batch.getArray());
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
