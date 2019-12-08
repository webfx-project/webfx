package webfx.platform.shared.service.datasource.jdbc;

import webfx.platform.shared.service.datasource.ConnectionDetails;
import webfx.platform.shared.service.datasource.DBMS;
import webfx.platform.shared.util.Strings;

/**
 * @author Bruno Salmon
 */
public class JdbcDriver {

    private final String driverClass;
    private final String jdbcUrlPattern;

    public JdbcDriver(String driverClass, String jdbcUrlPattern) {
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

    public static final JdbcDriver POSTGRES = new JdbcDriver("org.postgresql.Driver", "jdbc:postgresql://{host}:{port}/{database}");
    public static final JdbcDriver MYSQL = new JdbcDriver("org.postgresql.Driver", "jdbc:postgresql://{host}:{port}/{database}");
    public static final JdbcDriver HSQL = new JdbcDriver("org.hsqldb.jdbc.JDBCDriver", "jdbc:hsqldb:{file}/{database}");

    public static JdbcDriver from(DBMS dbms) {
        switch (dbms) {
            case POSTGRES: return POSTGRES;
            case MYSQL: return MYSQL;
            case HSQL: return HSQL;
        }
        throw new IllegalArgumentException("unknown JdbcDriver found for " + dbms);
    }
}
