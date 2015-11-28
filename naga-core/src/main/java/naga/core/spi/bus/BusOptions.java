package naga.core.spi.bus;

import naga.core.spi.json.JsonObject;

/**
 * @author Bruno Salmon
 */
public class BusOptions {

    public enum Protocol { WS, HTTP }

    private Protocol protocol = Protocol.WS;
    private boolean serverSSL = false;
    private String serverHost = "localhost";
    private int serverPort = 80;
    private String busPrefix = "/eventbus";
    private String clientBusAddressPrefix = "json.client";
    private String serverBusAddress = "json.server";

    private int pingInterval = 5 * 1000;
    private String sessionId;
    private String username;
    private String password;
    private String loginTopic;

    private JsonObject socketOptions;

    public Protocol getProtocol() {
        return protocol;
    }

    public BusOptions setProtocol(Protocol protocol) {
        this.protocol = protocol;
        return this;
    }

    public BusOptions setServerHost(String serverHost) {
        this.serverHost = serverHost;
        return this;
    }

    public String getServerHost() {
        return serverHost;
    }

    public BusOptions setServerSSL(boolean serverSSL) {
        this.serverSSL = serverSSL;
        return this;
    }

    public BusOptions setServerPort(int serverPort) {
        this.serverPort = serverPort;
        return this;
    }

    public int getServerPort() {
        return serverPort;
    }

    public boolean isServerSSL() {
        return serverSSL;
    }

    public BusOptions setBusPrefix(String busPrefix) {
        this.busPrefix = busPrefix;
        return this;
    }

    public String getBusPrefix() {
        return busPrefix;
    }

    public BusOptions setClientBusAddressPrefix(String clientBusAddressPrefix) {
        this.clientBusAddressPrefix = clientBusAddressPrefix;
        return this;
    }

    public String getClientBusAddressPrefix() {
        return clientBusAddressPrefix;
    }

    public BusOptions setServerBusAddress(String serverBusAddress) {
        this.serverBusAddress = serverBusAddress;
        return this;
    }

    public String getServerBusAddress() {
        return serverBusAddress;
    }

    public int getPingInterval() {
        return pingInterval;
    }

    public BusOptions setPingInterval(int pingInterval) {
        this.pingInterval = pingInterval;
        return this;
    }

    public String getSessionId() {
        return sessionId;
    }

    public BusOptions setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public BusOptions setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public BusOptions setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getLoginTopic() {
        return loginTopic;
    }

    public BusOptions setLoginTopic(String loginTopic) {
        this.loginTopic = loginTopic;
        return this;
    }

    public JsonObject getSocketOptions() {
        return socketOptions;
    }

    public BusOptions setSocketOptions(JsonObject socketOptions) {
        this.socketOptions = socketOptions;
        return this;
    }
}
