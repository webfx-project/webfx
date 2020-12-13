package dev.webfx.platform.shared.services.datasource.jdbc;

import dev.webfx.platform.shared.services.datasource.ConnectionDetails;
import dev.webfx.platform.shared.services.datasource.DBMS;
import dev.webfx.platform.shared.util.Strings;

/**
 * @author Bruno Salmon
 */
public class JdbcDriverInfo {

    private final String driverClass;
    private final String jdbcUrlPattern;

    public JdbcDriverInfo(String driverClass, String jdbcUrlPattern) {
        this.driverClass = driverClass;
        this.jdbcUrlPattern = jdbcUrlPattern;
    }

    public String getJdbcDriverClass() {
        return driverClass;
    }

    public String getUrlOrGenerateJdbcUrl(ConnectionDetails connectionDetails) {
        String url = connectionDetails.getUrl();
        return url != null ? null : generateJdbcUrl(connectionDetails);
    }

    public String generateJdbcUrl(ConnectionDetails connectionDetails) {
        String s = Strings.replaceAll(jdbcUrlPattern, "{host}", connectionDetails.getHost());
        s = Strings.replaceAll(s, "{port}", Integer.toString(connectionDetails.getPort()));
        s = Strings.replaceAll(s, "{file}", connectionDetails.getFilePath());
        s = Strings.replaceAll(s, "{database}", connectionDetails.getDatabaseName());
        return s;
    }

    public static final JdbcDriverInfo POSTGRES = new JdbcDriverInfo("org.postgresql.Driver", "jdbc:postgresql://{host}:{port}/{database}");
    public static final JdbcDriverInfo MYSQL = new JdbcDriverInfo("org.postgresql.Driver", "jdbc:postgresql://{host}:{port}/{database}");
    public static final JdbcDriverInfo HSQL = new JdbcDriverInfo("org.hsqldb.jdbc.JDBCDriver", "jdbc:hsqldb:{file}/{database}");

    public static JdbcDriverInfo from(DBMS dbms) {
        switch (dbms) {
            case POSTGRES: return POSTGRES;
            case MYSQL: return MYSQL;
            case HSQL: return HSQL;
        }
        throw new IllegalArgumentException("unknown JdbcDriverInfo found for " + dbms);
    }
}
