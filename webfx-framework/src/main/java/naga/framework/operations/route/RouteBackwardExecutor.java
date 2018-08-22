package naga.framework.operations.route;

import naga.platform.client.url.history.History;
import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
final class RouteBackwardExecutor {

    static Future<Void> executeRequest(RouteBackwardRequest rq) {
        return execute(rq.getHistory());
    }

    private static Future<Void> execute(History history) {
        history.goBack();
        return Future.succeededFuture();
    }
}
