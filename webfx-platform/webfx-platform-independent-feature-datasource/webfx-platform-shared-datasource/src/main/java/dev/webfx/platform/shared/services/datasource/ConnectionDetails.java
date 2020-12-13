package dev.webfx.platform.shared.services.datasource;

/**
 * @author Bruno Salmon
 */
public class ConnectionDetails {

    private final String url;
    private final String host;
    private final int port;
    private final String filePath;
    private final String databaseName;
    private final String username;
    private final String password;

    public ConnectionDetails(String filePath, String databaseName, String username, String password) {
        this(null, -1, filePath, databaseName, null, username, password);
    }

    public ConnectionDetails(String host, int port, String databaseName, String username, String password) {
        this(host, port, null, databaseName, null, username, password);
    }

    public ConnectionDetails(String host, int port, String filePath, String databaseName, String url, String username, String password) {
        this.host = host;
        this.port = port;
        this.filePath = filePath;
        this.databaseName = databaseName;
        this.url = url;
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

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
