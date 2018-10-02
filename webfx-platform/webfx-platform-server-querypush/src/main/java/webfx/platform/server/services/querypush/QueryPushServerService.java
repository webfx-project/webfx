package webfx.platform.server.services.querypush;

import webfx.platform.server.services.push.PushServerService;
import webfx.platform.shared.services.bus.BusService;
import webfx.platform.shared.services.querypush.QueryPushResult;
import webfx.platform.shared.util.async.Future;

import static webfx.platform.shared.services.querypush.QueryPushService.QUERY_PUSH_RESULT_LISTENER_CLIENT_SERVICE_ADDRESS;

/**
 * @author Bruno Salmon
 */
public final class QueryPushServerService {

    // Server side (sending a query push result to a specific client)
    public static <T> Future<T> pushQueryResultToClient(QueryPushResult queryPushResult, Object pushClientId) {
        return PushServerService.callClientService(QUERY_PUSH_RESULT_LISTENER_CLIENT_SERVICE_ADDRESS, queryPushResult, BusService.bus(), pushClientId);
    }

}
