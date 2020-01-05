package webfx.framework.server.services.querypush;

import webfx.framework.server.services.push.PushServerService;
import webfx.framework.shared.services.querypush.PulseArgument;
import webfx.framework.shared.services.querypush.QueryPushResult;
import webfx.framework.shared.services.querypush.QueryPushService;
import webfx.platform.server.services.submitlistener.SubmitListener;
import webfx.platform.shared.services.bus.BusService;
import webfx.platform.shared.services.submit.SubmitArgument;
import webfx.platform.shared.util.async.Future;

import static webfx.framework.shared.services.querypush.QueryPushService.QUERY_PUSH_RESULT_LISTENER_CLIENT_SERVICE_ADDRESS;

/**
 * @author Bruno Salmon
 */
public final class QueryPushServerService {

    // Server side push of a query result to a specific client
    public static <T> Future<T> pushQueryResultToClient(QueryPushResult queryPushResult, Object pushClientId) {
        return PushServerService.callClientService(
                QUERY_PUSH_RESULT_LISTENER_CLIENT_SERVICE_ADDRESS,
                queryPushResult,
                BusService.bus(),
                pushClientId
        );
    }

    public static class ProvidedSubmitListener implements SubmitListener {

        @Override
        public void onSuccessfulSubmit(SubmitArgument argument) {
            QueryPushService.executePulse(
                    PulseArgument.createToRefreshAllQueriesImpactedBySchemaScope(
                            argument.getDataSourceId(),
                            argument.getSchemaScope()
                    )
            );
        }

    }
}
