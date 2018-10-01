package webfx.platform.shared.services.querypush;

import webfx.platform.shared.services.push.client.PushClientService;
import webfx.platform.shared.services.push.server.PushServerService;
import webfx.platform.shared.services.querypush.spi.QueryPushServiceProvider;
import webfx.platform.shared.util.async.Future;
import webfx.platform.shared.services.bus.BusService;
import webfx.platform.shared.services.bus.Registration;
import webfx.platform.shared.util.serviceloader.SingleServiceLoader;

import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public final class QueryPushService {

    public static final String QUERY_PUSH_SERVICE_ADDRESS = "service/querypush";

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
