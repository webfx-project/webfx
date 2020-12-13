package dev.webfx.platform.shared.services.bus;


import dev.webfx.platform.shared.util.Objects;
import dev.webfx.platform.shared.services.json.JsonObject;

/**
 * @author Bruno Salmon
 */
public class BusOptions {

    private String busPrefix;
    private String clientBusAddressPrefix;
    private String serverBusAddress;

    public BusOptions turnUnsetPropertiesToDefault() {
        busPrefix = Objects.coalesce(busPrefix, "eventbus");
        clientBusAddressPrefix = Objects.coalesce(clientBusAddressPrefix, "client");
        serverBusAddress = Objects.coalesce(serverBusAddress, "server");
        return this;
    }

    public BusOptions applyJson(JsonObject json) {
        busPrefix = json.getString("busPrefix", busPrefix);
        clientBusAddressPrefix = json.getString("clientBusAddressPrefix", clientBusAddressPrefix);
        serverBusAddress = json.getString("serverBusAddress", serverBusAddress);
        return this;
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

}
