package naga.core.spi.bus.client;

import naga.core.spi.bus.BusOptions;
import naga.core.spi.json.JsonObject;

/**
 * @author Bruno Salmon
 */
public class WebSocketBusOptions extends BusOptions {

    public enum Protocol {
        WS,   // Web Socket protocol, to be used by non web applications (Jre, Android, iOS)
        HTTP  // HTTP protocol, to be used by web applications running in the browser (GWT, TeaVM)
    }

    private Protocol protocol;
    private Boolean serverSSL;
    private String serverHost;
    private Integer serverPort;

    private Integer pingInterval;
    private String sessionId;
    private String username;
    private String password;
    private String loginTopic;

    private JsonObject socketOptions;

    @Override
    public BusOptions turnUnsetPropertiesToDefault() {
        protocol = getValue(protocol, Protocol.HTTP);
        serverSSL = getValue(serverSSL, Boolean.FALSE);
        serverHost = getValue(serverHost, "localhost");
        serverPort = getValue(serverPort, 80);
        pingInterval = getValue(pingInterval, 5 * 1000);
        return super.turnUnsetPropertiesToDefault();
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public WebSocketBusOptions setProtocol(Protocol protocol) {
        this.protocol = protocol;
        return this;
    }

    public WebSocketBusOptions setServerHost(String serverHost) {
        this.serverHost = serverHost;
        return this;
    }

    public String getServerHost() {
        return serverHost;
    }

    public WebSocketBusOptions setServerSSL(boolean serverSSL) {
        this.serverSSL = serverSSL;
        return this;
    }

    public WebSocketBusOptions setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
        return this;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public boolean isServerSSL() {
        return serverSSL;
    }

    public int getPingInterval() {
        return pingInterval;
    }

    public WebSocketBusOptions setPingInterval(Integer pingInterval) {
        this.pingInterval = pingInterval;
        return this;
    }

    public String getSessionId() {
        return sessionId;
    }

    public WebSocketBusOptions setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public WebSocketBusOptions setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public WebSocketBusOptions setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getLoginTopic() {
        return loginTopic;
    }

    public WebSocketBusOptions setLoginTopic(String loginTopic) {
        this.loginTopic = loginTopic;
        return this;
    }

    public JsonObject getSocketOptions() {
        return socketOptions;
    }

    public WebSocketBusOptions setSocketOptions(JsonObject socketOptions) {
        this.socketOptions = socketOptions;
        return this;
    }

}
