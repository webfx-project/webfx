package webfx.platforms.core.services.push.client;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import webfx.platforms.core.services.bus.Registration;
import webfx.platforms.core.services.bus.call.BusCallService;
import webfx.platforms.core.services.log.Logger;
import webfx.platforms.core.services.push.client.spi.impl.PushClientServiceProviderImpl;
import webfx.platforms.core.services.push.client.spi.PushClientServiceProvider;
import webfx.platforms.core.services.push.ClientPushBusAddressesSharedByBothClientAndServer;
import webfx.platforms.core.util.function.Function;
import webfx.platforms.core.util.serviceloader.SingleServiceLoader;

/**
 * @author Bruno Salmon
 */
public class PushClientService {

    private final static ObjectProperty<Object> pushClientIdProperty = new SimpleObjectProperty<>();

    static {
        // Registering the default push client service provider (can be overridden using the service loader mechanism)
        SingleServiceLoader.registerDefaultServiceFactory(PushClientServiceProvider.class, PushClientServiceProviderImpl::new);
        // Registering the client push ping listener. This registration is private (ie just done locally on the client
        // event bus) so not directly visible from the server event bus but the server can reach that listener by calling
        // PushServerService.pingPushClient() because the client bus call service will finally pass the arg to that
        // listener over the local client bus.
        registerPushFunction(ClientPushBusAddressesSharedByBothClientAndServer.PUSH_PING_CLIENT_LISTENER_SERVICE_ADDRESS, arg -> {
            Logger.log(arg);
            return "OK";
        });
        // But to make this work, the client bus call service must listen server calls. This should be done by calling:
        // PushClientService.listenServerPushCalls() as soon as the push client id has been generated.
    }

    public static Object getPushClientId() {
        return pushClientIdProperty.get();
    }

    public static ObjectProperty<Object> pushClientIdProperty() {
        return pushClientIdProperty;
    }

    public static PushClientServiceProvider getProvider() {
        return SingleServiceLoader.loadService(PushClientServiceProvider.class);
    }

    public static Registration listenServerPushCalls(Object pushClientId) {
        pushClientIdProperty.setValue(pushClientId);
        return getProvider().listenServerPushCalls(pushClientId);
    }

    public static <A, R> Registration registerPushFunction(String address, Function<A, R> javaFunction) {
        return BusCallService.registerJavaFunctionAsCallableService(address, javaFunction);
    }

}