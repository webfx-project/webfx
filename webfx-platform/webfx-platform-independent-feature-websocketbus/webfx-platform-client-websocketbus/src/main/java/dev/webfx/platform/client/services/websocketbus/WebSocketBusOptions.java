package dev.webfx.platform.client.services.websocketbus;

import dev.webfx.platform.shared.util.Objects;
import dev.webfx.platform.shared.services.bus.BusOptions;
import dev.webfx.platform.shared.services.json.JsonObject;

/**
 * @author Bruno Salmon
 */
public final class WebSocketBusOptions extends BusOptions {

    public enum Protocol {
        WS,   // Web Socket protocol, to be used by non web applications (Jre, Android, iOS)
        HTTP  // HTTP protocol, to be used by web applications running in the browser (GWT, TeaVM)
    }

    private Protocol protocol;
    private Boolean serverSSL;
    private String serverHost;
    private String serverPort;

    private Integer pingInterval;
    private String sessionId;
    private String username;
    private String password;
    private String loginTopic;

    private JsonObject socketOptions;

    @Override
    public WebSocketBusOptions turnUnsetPropertiesToDefault() {
        protocol = Objects.coalesce(protocol, Protocol.WS);
        serverSSL = Objects.coalesce(serverSSL, Boolean.FALSE);
        serverHost = Objects.coalesce(serverHost, "localhost");
        serverPort = Objects.coalesce(serverPort, "80");
        pingInterval = Objects.coalesce(pingInterval, 30_000);
        super.turnUnsetPropertiesToDefault();
        return this;
    }

    @Override
    public WebSocketBusOptions applyJson(JsonObject json) {
        super.applyJson(json);
        protocol = Protocol.valueOf(json.getString("protocol", protocol.name()));
        serverSSL = json.getBoolean("serverSSL", serverSSL);
        serverHost = json.getString("serverHost", serverHost);
        serverPort = json.getString("serverPort", serverPort);
        pingInterval = json.getInteger("pingInterval", pingInterval);
        return this;
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

    public WebSocketBusOptions setServerSSL(Boolean serverSSL) {
        this.serverSSL = serverSSL;
        return this;
    }

    public WebSocketBusOptions setServerPort(String serverPort) {
        this.serverPort = serverPort;
        return this;
    }

    public String getServerPort() {
        return serverPort;
    }

    public Boolean isServerSSL() {
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
