package naga.platform.services.push;

/**
 * @author Bruno Salmon
 */
public class ClientPushBusAddressesSharedByBothClientAndServer {

    public final static String PUSH_PING_CLIENT_LISTENER_SERVICE_ADDRESS = "pushPingClientListener";

    public static String computeClientBusCallServiceAddress(Object pushClientId) {
        return "busCallService/client/" + pushClientId;
    }

}
