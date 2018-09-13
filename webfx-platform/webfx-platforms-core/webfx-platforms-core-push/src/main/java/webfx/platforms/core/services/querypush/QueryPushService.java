package webfx.platforms.core.services.querypush;

import webfx.platforms.core.services.bus.Registration;
import webfx.platforms.core.services.buscall.BusCallService;
import webfx.platforms.core.services.bus.spi.BusService;
import webfx.platforms.core.services.push.client.PushClientService;
import webfx.platforms.core.services.push.server.PushServerService;
import webfx.platforms.core.services.querypush.diff.impl.QueryResultTranslation;
import webfx.platforms.core.services.querypush.spi.QueryPushServiceProvider;
import webfx.platforms.core.services.querypush.spi.remote.RemoteQueryPushServiceProviderImpl;
import webfx.platforms.core.util.async.Future;
import webfx.platforms.core.util.function.Consumer;
import webfx.platforms.core.util.serviceloader.SingleServiceLoader;

/**
 * @author Bruno Salmon
 */
public class QueryPushService {

    public static final String QUERY_PUSH_SERVICE_ADDRESS = "service/querypush";

    static {
        SingleServiceLoader.registerDefaultServiceFactory(QueryPushServiceProvider.class, RemoteQueryPushServiceProviderImpl::new);
        // registerJsonCodecsAndBusCalls() body:
        QueryPushArgument.registerJsonCodec();
        QueryPushResult.registerJsonCodec();
        QueryResultTranslation.registerJsonCodec();
        BusCallService.registerJavaAsyncFunctionAsCallableService(QUERY_PUSH_SERVICE_ADDRESS, QueryPushService::executeQueryPush);
    }

    public static void registerJsonCodecsAndBusCalls() {
        // body actually moved in the static constructor
    }

    public static QueryPushServiceProvider getProvider() {
        return SingleServiceLoader.loadService(QueryPushServiceProvider.class);
    }

    public static void registerProvider(QueryPushServiceProvider provider) {
        SingleServiceLoader.cacheServiceInstance(QueryPushServiceProvider.class, provider);
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
        return PushServerService.callClientService(QUERY_PUSH_RESULT_LISTENER_CLIENT_SERVICE_ADDRESS, queryPushResult, BusService.bus(), pushClientId);
    }

}
