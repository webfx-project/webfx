package webfx.platform.services.query.push;

import webfx.platform.bus.Registration;
import webfx.platform.services.push.client.PushClientService;
import webfx.platform.services.push.server.PushServerService;
import webfx.platform.services.query.push.spi.QueryPushServiceProvider;
import webfx.platform.services.query.push.spi.remote.RemoteQueryPushServiceProviderImpl;
import webfx.platform.spi.Platform;
import webfx.util.async.Future;
import webfx.util.function.Consumer;
import webfx.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public class QueryPushService {

    static {
        ServiceLoaderHelper.registerDefaultServiceFactory(QueryPushServiceProvider.class, RemoteQueryPushServiceProviderImpl::new);
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
    public static <T> Future<T> pushQueryResultToClient(QueryPushResult queryPushResult, Object pushClientId) {
        return PushServerService.callClientService(QUERY_PUSH_RESULT_LISTENER_CLIENT_SERVICE_ADDRESS, queryPushResult, Platform.bus(), pushClientId);
    }

}
