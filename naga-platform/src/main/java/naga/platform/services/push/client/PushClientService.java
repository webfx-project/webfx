package naga.platform.services.push.client;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import naga.platform.bus.Registration;
import naga.platform.bus.call.BusCallService;
import naga.platform.services.push.client.spi.impl.PushClientServiceProviderImpl;
import naga.platform.services.push.client.spi.PushClientServiceProvider;
import naga.util.function.Function;
import naga.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public class PushClientService {

    private final static ObjectProperty pushClientIdProperty = new SimpleObjectProperty();

    static {
        ServiceLoaderHelper.registerDefaultServiceFactory(PushClientServiceProvider.class, PushClientServiceProviderImpl::new);
    }

    public static Object getPushClientIdProperty() {
        return pushClientIdProperty.get();
    }

    public static ObjectProperty pushClientIdPropertyProperty() {
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
