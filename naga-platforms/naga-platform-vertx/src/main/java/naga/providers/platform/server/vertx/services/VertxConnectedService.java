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
import io.vertx.ext.sql.SQLConnection;
import naga.commons.util.Arrays;
import naga.commons.util.async.Batch;
import naga.commons.util.async.Future;
import naga.commons.util.function.BiConsumer;
import naga.commons.util.tuples.Unit;
import naga.platform.services.datasource.ConnectionDetails;
import naga.platform.services.datasource.DBMS;
import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.QueryResultSet;
import naga.platform.services.query.QueryResultSetBuilder;
import naga.platform.services.query.spi.QueryService;
import naga.platform.services.update.GeneratedKeyBatchIndex;
import naga.platform.services.update.UpdateArgument;
import naga.platform.services.update.UpdateResult;
import naga.platform.services.update.spi.UpdateService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class VertxConnectedService implements QueryService, UpdateService {

    private final AsyncSQLClient sqlClient;

    public VertxConnectedService(Vertx vertx, ConnectionDetails connectionDetails) {
        // Generating the Vertx Sql config from the connection details
        JsonObject sqlConfig = new JsonObject()
                // common config with JDBCClient
                .put("username", connectionDetails.getUsername())
                .put("password", connectionDetails.getPassword())
                // used for PostgreSQLClient only
                .put("host", connectionDetails.getHost())
                .put("database", connectionDetails.getDatabaseName());
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
    public Future<QueryResultSet> executeQuery(QueryArgument arg) {
        return connectAndExecute(true, (connection, future) -> {
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
                    QueryResultSetBuilder rsb = QueryResultSetBuilder.create(rowCount, columnNames);
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
                connection.close();
            };
            // Calling query() or queryWithParams() depending if parameters are provided or not
            Object[] parameters = arg.getParameters();
            if (Arrays.isEmpty(parameters))
                connection.query(arg.getQueryString(), resultHandler);
            else {
                JsonArray array = new JsonArray();
                for (Object value : parameters)
                    array.add(value);
                connection.queryWithParams(arg.getQueryString(), array, resultHandler);
            }
        });
    }

    @Override
    public Future<UpdateResult> executeUpdate(UpdateArgument updateArgument) {
        return connectAndExecute(true, (connection, future) -> {
            Handler<AsyncResult<io.vertx.ext.sql.UpdateResult>> resultHandler = res -> {
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
                connection.close();
            };
            // Calling update() or updateWithParams() depending if parameters are provided or not
            Object[] parameters = updateArgument.getParameters();
            if (Arrays.isEmpty(parameters))
                connection.update(updateArgument.getUpdateString(), resultHandler);
            else {
                JsonArray array = new JsonArray();
                for (Object value : parameters)
                    array.add(value);
                connection.updateWithParams(updateArgument.getUpdateString(), array, resultHandler);
            }
        });
    }

    @Override
    public Future<Batch<UpdateResult>> executeUpdateBatch(Batch<UpdateArgument> batch) {
        // Singular batch optimization: executing the single sql order in autocommit mode
        Future<Batch<UpdateResult>> singularBatchFuture = batch.executeIfSingularBatch(this::executeUpdate, UpdateResult.class);
        if (singularBatchFuture != null)
            return singularBatchFuture;

        // Now handling real batch with several arguments -> no autocommit with explicit commit() or rollback() handling
        List<Object> batchIndexGeneratedKeys = new ArrayList<>();
        Unit<Integer> batchIndex = new Unit<>(0);
        return connectAndExecute(false /* no autocommit */, (connection, batchFuture) -> batch.executeSerial(arg -> {
            Future<UpdateResult> future = Future.future();
            Handler<AsyncResult<io.vertx.ext.sql.UpdateResult>> resultHandler = res -> {
                if (res.failed()) { // Sql error
                    future.fail(res.cause());
                    connection.rollback(event -> connection.close());
                } else { // Sql succeeded
                    io.vertx.ext.sql.UpdateResult vertxUpdateResult = res.result();
                    JsonArray keys = vertxUpdateResult.getKeys();
                    Object[] generatedKeys = null;
                    if (keys != null && !keys.isEmpty()) {
                        batchIndexGeneratedKeys.set(batchIndex.get(), keys.getValue(0));
                        if (arg.returnGeneratedKeys()) {
                            int length = keys.size();
                            generatedKeys = new Object[length];
                            for (int i = 0; i < length; i++)
                                generatedKeys[i] = keys.getValue(i);
                        }
                    }
                    batchIndex.set(batchIndex.get() + 1);
                    // Returning the final UpdateResult
                    UpdateResult updateResult = new UpdateResult(vertxUpdateResult.getUpdated(), generatedKeys);
                    if (batchIndex.get() < batch.getArray().length)
                        future.complete(updateResult);
                    else
                        commitCompleteAndClose(connection, updateResult, future);
                }
            };
            // Calling update() or updateWithParams() depending if parameters are provided or not
            Object[] parameters = arg.getParameters();
            if (Arrays.isEmpty(parameters))
                connection.update(arg.getUpdateString(), resultHandler);
            else {
                JsonArray array = new JsonArray();
                for (Object value : parameters) {
                    if (value instanceof GeneratedKeyBatchIndex)
                        value = batchIndexGeneratedKeys.get(((GeneratedKeyBatchIndex) value).getBatchIndex());
                    array.add(value);
                }
                connection.updateWithParams(arg.getUpdateString(), array, resultHandler);
            }
            return future;
        }, UpdateResult.class, batchFuture));
    }

    private <T> Future<T> connectAndExecute(boolean autoCommit, BiConsumer<SQLConnection, Future<T>> executor) {
        Future<T> future = Future.future();

        sqlClient.getConnection(connectionAsyncResult -> {
            if (connectionAsyncResult.failed()) // Connection failed
                future.fail(connectionAsyncResult.cause());
            else { // Connection succeeded
                SQLConnection connection = connectionAsyncResult.result();
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

    private <T> void commitCompleteAndClose(SQLConnection connection, T result, Future<T> future) {
        connection.commit(asyncResult -> {
            if (asyncResult.failed())
                future.fail(asyncResult.cause());
            else
                future.complete(result);
            connection.close();
        });
    }
}
