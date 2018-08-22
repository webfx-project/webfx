package naga.framework.operations.route;

import naga.platform.client.url.history.History;
import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
final class RouteForwardExecutor {

    static Future<Void> executeRequest(RouteForwardRequest rq) {
        return execute(rq.getHistory());
    }

    private static Future<Void> execute(History history) {
        history.goForward();
        return Future.succeededFuture();
    }
}
