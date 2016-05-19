package naga.core.bus;

/**
 * @author Bruno Salmon
 */
public class BusOptions {

    private String busPrefix;
    private String clientBusAddressPrefix;
    private String serverBusAddress;

    public BusOptions turnUnsetPropertiesToDefault() {
        busPrefix = getValue(busPrefix, "eventbus");
        clientBusAddressPrefix = getValue(clientBusAddressPrefix, "client");
        serverBusAddress = getValue(serverBusAddress, "server");
        return this;
    }

    protected static <T> T getValue(T currentValue, T defaultValue) {
        return currentValue != null ? currentValue : defaultValue;
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
