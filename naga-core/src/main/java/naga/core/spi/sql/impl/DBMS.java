package naga.core.spi.sql.impl;

import naga.core.sql.SqlSyntaxOptions;

/**
 * @author Bruno Salmon
 */
public class DBMS {

    private final String driverClass;
    private final String jdbcUrlPattern;
    private final SqlSyntaxOptions sqlSyntaxOptions;

    public DBMS(String driverClass, String jdbcUrlPattern, SqlSyntaxOptions sqlSyntaxOptions) {
        this.driverClass = driverClass;
        this.jdbcUrlPattern = jdbcUrlPattern;
        this.sqlSyntaxOptions = sqlSyntaxOptions;
    }

    public String getJdbcDriverClass() {
        return driverClass;
    }

    public String generateJdbcUrl(ConnectionDetails connectionDetails) {
        return jdbcUrlPattern
                .replaceAll("\\{host\\}", connectionDetails.getHost())
                .replaceAll("\\{port\\}", Integer.toString(connectionDetails.getPort()))
                .replaceAll("\\{file\\}", connectionDetails.getFilePath())
                .replaceAll("\\{database\\}", connectionDetails.getDatabaseName());
    }

    public SqlSyntaxOptions getSqlSyntaxOptions() {
        return sqlSyntaxOptions;
    }

    public static final DBMS POSTGRES = new DBMS("org.postgresql.Driver", "jdbc:postgresql://{host}:{port}/{database}", new SqlSyntaxOptions(true));
    public static final DBMS MYSQL = new DBMS("org.postgresql.Driver", "jdbc:postgresql://{host}:{port}/{database}", new SqlSyntaxOptions(true));
    public static final DBMS HSQL = new DBMS("org.hsqldb.jdbc.JDBCDriver", "jdbc:hsqldb:{file}/{database}", new SqlSyntaxOptions(false));

}
