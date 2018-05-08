package naga.platform.services.push.client;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import naga.platform.bus.Registration;
import naga.platform.bus.call.BusCallService;
import naga.platform.services.log.Logger;
import naga.platform.services.push.client.spi.impl.PushClientServiceProviderImpl;
import naga.platform.services.push.client.spi.PushClientServiceProvider;
import naga.util.function.Function;
import naga.util.serviceloader.ServiceLoaderHelper;

import static naga.platform.services.push.SharedClientServerPushInfo.PUSH_PING_CLIENT_LISTENER_SERVICE_ADDRESS;

/**
 * @author Bruno Salmon
 */
public class PushClientService {

    private final static ObjectProperty<Object> pushClientIdProperty = new SimpleObjectProperty<>();

    static {
        // Registering the default push client service provider (can be overridden using the service loader mechanism)
        ServiceLoaderHelper.registerDefaultServiceFactory(PushClientServiceProvider.class, PushClientServiceProviderImpl::new);
        // Registering the client push ping listener. This registration is private (ie just done locally on the client
        // event bus) so not directly visible from the server event bus but the server can reach that listener by calling
        // PushServerService.pingPushClient() because the client bus call service will finally pass the arg to that
        // listener over the local client bus.
        registerPushFunction(PUSH_PING_CLIENT_LISTENER_SERVICE_ADDRESS, arg -> {
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
        return ServiceLoaderHelper.loadService(PushClientServiceProvider.class);
    }

    public static Registration listenServerPushCalls(Object pushClientId) {
        pushClientIdProperty.setValue(pushClientId);
        return getProvider().listenServerPushCalls(pushClientId);
    }

    public static <A, R> Registration registerPushFunction(String address, Function<A, R> javaFunction) {
        return BusCallService.registerJavaFunctionAsCallableService(address, javaFunction);
    }

}
