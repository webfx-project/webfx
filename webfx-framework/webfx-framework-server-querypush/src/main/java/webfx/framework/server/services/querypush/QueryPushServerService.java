package webfx.framework.server.services.querypush;

import webfx.framework.server.services.push.PushServerService;
import webfx.platform.server.services.updatelistener.UpdateListener;
import webfx.platform.shared.services.bus.BusService;
import webfx.framework.shared.services.querypush.PulseArgument;
import webfx.framework.shared.services.querypush.QueryPushResult;
import webfx.framework.shared.services.querypush.QueryPushService;
import webfx.platform.shared.services.update.UpdateArgument;
import webfx.platform.shared.util.async.Future;

import static webfx.framework.shared.services.querypush.QueryPushService.QUERY_PUSH_RESULT_LISTENER_CLIENT_SERVICE_ADDRESS;

/**
 * @author Bruno Salmon
 */
public final class QueryPushServerService {

    // Server side (sending a query push result to a specific client)
    public static <T> Future<T> pushQueryResultToClient(QueryPushResult queryPushResult, Object pushClientId) {
        return PushServerService.callClientService(QUERY_PUSH_RESULT_LISTENER_CLIENT_SERVICE_ADDRESS, queryPushResult, BusService.bus(), pushClientId);
    }

    public static class ProvidedUpdateListener implements UpdateListener {
        @Override
        public void onSuccessfulUpdate(UpdateArgument updateArgument) {
            QueryPushService.executePulse(new PulseArgument(updateArgument.getDataSourceId()));
        }
    }
}
