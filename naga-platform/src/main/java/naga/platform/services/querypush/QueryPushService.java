package naga.platform.services.querypush;

import naga.platform.bus.Registration;
import naga.platform.bus.call.PendingBusCall;
import naga.platform.services.push.client.PushClientService;
import naga.platform.services.push.server.PushServerService;
import naga.platform.services.querypush.spi.QueryPushServiceProvider;
import naga.platform.services.querypush.spi.remote.RemoteQueryPushServiceProvider;
import naga.platform.spi.Platform;
import naga.util.async.Future;
import naga.util.function.Consumer;
import naga.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public class QueryPushService {

    static {
        ServiceLoaderHelper.registerDefaultServiceFactory(QueryPushServiceProvider.class, RemoteQueryPushServiceProvider::new);
    }

    public static QueryPushServiceProvider getProvider() {
        return ServiceLoaderHelper.loadService(QueryPushServiceProvider.class);
    }

    public static void registerProvider(QueryPushServiceProvider provider) {
        ServiceLoaderHelper.cacheServiceInstance(QueryPushServiceProvider.class, provider);
    }

    public static Future<Object> executeQueryPush(QueryPushArgument argument) {
        return getProvider().executeQueryPush(argument);
    }

    public static void executePulse(PulseArgument argument) {
        getProvider().requestPulse(argument);
    }

    // Additional static helpers for query push notifications

    private static final String QUERY_PUSH_RESULT_LISTENER_CLIENT_SERVICE_ADDRESS = "QueryPushResultClientListener";

    // Client side (registering a consumer that will receive the query push results)
    public static Registration registerQueryPushClientConsumer(Consumer<QueryPushResult> javaFunction) {
        return PushClientService.registerPushFunction(QUERY_PUSH_RESULT_LISTENER_CLIENT_SERVICE_ADDRESS, (QueryPushResult qpr) -> {
            javaFunction.accept(qpr);
           return null;
        });
    }

    // Server side (sending a query push result to a specific client)
    public static <T> PendingBusCall<T> pushQueryResultToClient(QueryPushResult queryPushResult, Object pushClientId) {
        return PushServerService.callClientService(QUERY_PUSH_RESULT_LISTENER_CLIENT_SERVICE_ADDRESS, queryPushResult, Platform.bus(), pushClientId);
    }

}
