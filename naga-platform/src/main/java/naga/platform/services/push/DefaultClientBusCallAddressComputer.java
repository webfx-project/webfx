package naga.platform.services.push;

/**
 * @author Bruno Salmon
 */
public class DefaultClientBusCallAddressComputer {

    public static String computeClientBusCallServiceAddress(Object pushClientId) {
        return "busCallService/client/" + pushClientId;
    }

}
