package webfx.framework.operations.route;

import webfx.platform.client.url.history.History;
import webfx.util.async.Future;

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
