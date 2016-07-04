package naga.core.datasource;

import naga.core.util.Strings;

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
        String s = Strings.replaceAll(jdbcUrlPattern, "{host}", connectionDetails.getHost());
        s = Strings.replaceAll(s, "{port}", Integer.toString(connectionDetails.getPort()));
        s = Strings.replaceAll(s, "{file}", connectionDetails.getFilePath());
        s = Strings.replaceAll(s, "{database}", connectionDetails.getDatabaseName());
        return s;
    }

    public static final DBMS POSTGRES = new DBMS("org.postgresql.Driver", "jdbc:postgresql://{host}:{port}/{database}");
    public static final DBMS MYSQL = new DBMS("org.postgresql.Driver", "jdbc:postgresql://{host}:{port}/{database}");
    public static final DBMS HSQL = new DBMS("org.hsqldb.jdbc.JDBCDriver", "jdbc:hsqldb:{file}/{database}");

}
