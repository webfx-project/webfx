package naga.core.queryservice.impl;

/**
 * @author Bruno Salmon
 */
public class ConnectionDetails {

    private final String url;
    private final String host;
    private final int port;
    private final String filePath;
    private final String databaseName;
    private final DBMS dbms;
    private final String username;
    private final String password;

    public ConnectionDetails(String filePath, String databaseName, DBMS dbms, String username, String password) {
        this(null, -1, filePath, databaseName, dbms, null, username, password);
    }

    public ConnectionDetails(String host, int port, String databaseName, DBMS dbms, String username, String password) {
        this(host, port, null, databaseName, dbms, null, username, password);
    }

    private ConnectionDetails(String host, int port, String filePath, String databaseName, DBMS dbms, String url, String username, String password) {
        this.host = host;
        this.port = port;
        this.filePath = filePath;
        this.databaseName = databaseName;
        this.dbms = dbms;
        this.url = url != null ? url : dbms.generateJdbcUrl(this);
        this.username = username;
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public DBMS getDBMS() {
        return dbms;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
