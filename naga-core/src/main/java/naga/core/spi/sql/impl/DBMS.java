package naga.core.spi.sql.impl;

/**
 * @author Bruno Salmon
 */
public class DBMS {

    private final String driverClass;
    private final String jdbcUrlPattern;

    public DBMS(String driverClass, String jdbcUrlPattern) {
        this.driverClass = driverClass;
        this.jdbcUrlPattern = jdbcUrlPattern;
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

    public static final DBMS POSTGRES = new DBMS("org.postgresql.Driver", "jdbc:postgresql://{host}:{port}/{database}");
    public static final DBMS MYSQL = new DBMS("org.postgresql.Driver", "jdbc:postgresql://{host}:{port}/{database}");
    public static final DBMS HSQL = new DBMS("org.hsqldb.jdbc.JDBCDriver", "jdbc:hsqldb:{file}/{database}");

}
