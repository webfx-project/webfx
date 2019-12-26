package webfx.platform.java.services_shared_code.queryupdate.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import webfx.platform.shared.services.datasource.ConnectionDetails;
import webfx.platform.shared.services.datasource.LocalDataSource;
import webfx.platform.shared.services.datasource.jdbc.JdbcDriverInfo;
import webfx.platform.shared.services.query.QueryArgument;
import webfx.platform.shared.services.query.QueryResult;
import webfx.platform.shared.services.query.QueryResultBuilder;
import webfx.platform.shared.services.query.spi.QueryServiceProvider;
import webfx.platform.shared.services.submit.SubmitArgument;
import webfx.platform.shared.services.submit.SubmitResult;
import webfx.platform.shared.services.submit.spi.SubmitServiceProvider;
import webfx.platform.shared.util.Arrays;
import webfx.platform.shared.util.async.Future;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class JdbcLocalConnectedQuerySubmitServiceProvider implements QueryServiceProvider, SubmitServiceProvider {

    private final DataSource jdbcDataSource;

    public JdbcLocalConnectedQuerySubmitServiceProvider(LocalDataSource localDataSource) {
        ConnectionDetails connectionDetails = localDataSource.getLocalConnectionDetails();
        HikariDataSource hikariDS = new HikariDataSource();
        JdbcDriverInfo jdbcDriverInfo = JdbcDriverInfo.from(localDataSource.getDBMS());
        hikariDS.setDriverClassName(jdbcDriverInfo.getJdbcDriverClass());
        hikariDS.setJdbcUrl(jdbcDriverInfo.getUrlOrGenerateJdbcUrl(connectionDetails));
        hikariDS.setUsername(connectionDetails.getUsername());
        hikariDS.setPassword(connectionDetails.getPassword());
        jdbcDataSource = hikariDS;
    }

    @Override
    public Future<QueryResult> executeQuery(QueryArgument arg) {
        Future<QueryResult> future = Future.future();

        String sql = arg.getStatement();
        try (
                Connection connection = getConnection();
                Statement statement = getStatement(sql, arg.getParameters(), connection);
                ResultSet resultSet = executeStatementQuery(sql, statement)
        ) {
            // Reading column names
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            QueryResultBuilder rsb = QueryResultBuilder.createUnknownRowCount(columnCount);
            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++)
                rsb.setColumnName(columnIndex, metaData.getColumnName(columnIndex + 1)); // JDBC index starts with 1 (not 0)
            // Reading data through iterating the result set into a temporary growing list of rows (as we don't know yet the rows count)
            while (resultSet.next()) {
                rsb.addRow();
                for (int columnIndex = 0; columnIndex < columnCount; columnIndex++)
                    rsb.setCurrentRowValue(columnIndex, resultSet.getObject(columnIndex + 1)); // JDBC index starts with 1 (not 0)
            }
            // Building and returning the query result set
            future.complete(rsb.build());
        } catch (Throwable throwable) {
            future.fail(throwable);
        }

        return future;
    }

    @Override
    public Future<SubmitResult> executeSubmit(SubmitArgument arg) {
        Future<SubmitResult> future = Future.future();

        String sql = arg.getStatement();
        try (
                Connection connection = getConnection();
                Statement statement = getStatement(sql, arg.getParameters(), connection)
        ) {
            boolean returnGeneratedKeys = arg.returnGeneratedKeys();
            int rowCount = executeStatementUpdate(sql, statement, returnGeneratedKeys);
            Object[] generatedKeys = null;
            if (returnGeneratedKeys) {
                ResultSet rs = statement.getGeneratedKeys();
                List keysList = new ArrayList();
                while (rs.next())
                    keysList.add(rs.getObject(0));
                generatedKeys = keysList.toArray();
            }
            future.complete(new SubmitResult(rowCount, generatedKeys));
        } catch (Throwable throwable) {
            future.fail(throwable);
        }

        return future;
    }

    private Connection getConnection() throws SQLException {
        return jdbcDataSource.getConnection();
    }

    private Statement getStatement(String sql, Object[] parameters, Connection connection) throws SQLException {
        if (Arrays.isEmpty(parameters))
            return connection.createStatement();
        //SqlPrepared p = e.getSqlPrepared();
        //PreparedStatement ps = connection.prepareStatement(p.getQueryString(), p.getAutoGeneratedKeyColumnNames());
        PreparedStatement ps = connection.prepareStatement(sql);
        for (int i = 0; i < parameters.length; i++) {
            Object parameter = parameters[i];
            /*if (parameter instanceof ParameterJoinValue)
                parameter = ((ParameterJoinValue) parameter).getRowId();
            while (parameter instanceof ID)
                parameter = ((ID) parameter).getObjId();*/
            if (parameter instanceof Date)
                ps.setDate(i + 1, (Date) parameter);
            else if (parameter instanceof Timestamp)
                ps.setTimestamp(i + 1, (Timestamp) parameter);
            else if (parameter instanceof java.util.Date) { // Postgres doesn't accept setObject() with dates but requires explicit setDate()
                java.util.Date date = (java.util.Date) parameter;
                if (date.getHours() == 0 && date.getMinutes() == 0 && date.getSeconds() == 0)
                    ps.setDate(i + 1, new Date(date.getTime()));
                else
                    ps.setTimestamp(i + 1, new Timestamp(date.getTime()));
            } else
                //try {
                if (parameter != null)
                    ps.setObject(i + 1, parameter);
                else
                    ps.setNull(i + 1, Types.INTEGER);  // Postgres needs the type in some case (ex: ? is null). Putting Integer to fit keys but what if it's not the case ...?
                /*} catch (SQLException e1) {
                    e1.printStackTrace();
                    throw e1;
                }*/
        }
        return ps;
    }

    private ResultSet executeStatementQuery(String sql, Statement statement) throws SQLException {
        if (statement instanceof PreparedStatement)
            return ((PreparedStatement) statement).executeQuery();
        return statement.executeQuery(sql);
    }

    private int executeStatementUpdate(String sql, Statement statement, boolean returnGeneratedKeys) throws SQLException {
        if (statement instanceof PreparedStatement)
            return ((PreparedStatement) statement).executeUpdate();
        return statement.executeUpdate(sql, returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);
    }

}
